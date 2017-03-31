package org.opendatakit.demoAndroidlibraryClasses.utilities;


import android.os.Build;
import android.os.Process;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;

public final class PRNGFixes {
    private static final int VERSION_CODE_JELLY_BEAN = 16;
    private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;
    private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();

    private PRNGFixes() {
    }

    public static void apply() {
        applyOpenSSLFix();
        installLinuxPRNGSecureRandom();
    }

    private static void applyOpenSSLFix() throws SecurityException {
        if(VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 18) {
            try {
                Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_seed", new Class[]{byte[].class}).invoke((Object)null, new Object[]{generateSeed()});
                int e = ((Integer)Class.forName("org.apache.harmony.xnet.provider.jsse.NativeCrypto").getMethod("RAND_load_file", new Class[]{String.class, Long.TYPE}).invoke((Object)null, new Object[]{"/dev/urandom", Integer.valueOf(1024)})).intValue();
                if(e != 1024) {
                    throw new IOException("Unexpected number of bytes read from Linux PRNG: " + e);
                }
            } catch (Exception var1) {
                throw new SecurityException("Failed to seed OpenSSL PRNG", var1);
            }
        }
    }

    private static void installLinuxPRNGSecureRandom() throws SecurityException {
        if(VERSION.SDK_INT <= 18) {
            Provider[] secureRandomProviders = Security.getProviders("SecureRandom.SHA1PRNG");
            if(secureRandomProviders == null || secureRandomProviders.length < 1 || !PRNGFixes.LinuxPRNGSecureRandomProvider.class.equals(secureRandomProviders[0].getClass())) {
                Security.insertProviderAt(new PRNGFixes.LinuxPRNGSecureRandomProvider(), 1);
            }

            SecureRandom rng1 = new SecureRandom();
            if(!PRNGFixes.LinuxPRNGSecureRandomProvider.class.equals(rng1.getProvider().getClass())) {
                throw new SecurityException("new SecureRandom() backed by wrong Provider: " + rng1.getProvider().getClass());
            } else {
                SecureRandom rng2;
                try {
                    rng2 = SecureRandom.getInstance("SHA1PRNG");
                } catch (NoSuchAlgorithmException var4) {
                    throw new SecurityException("SHA1PRNG not available", var4);
                }

                if(!PRNGFixes.LinuxPRNGSecureRandomProvider.class.equals(rng2.getProvider().getClass())) {
                    throw new SecurityException("SecureRandom.getInstance(\"SHA1PRNG\") backed by wrong Provider: " + rng2.getProvider().getClass());
                }
            }
        }
    }

    private static byte[] generateSeed() {
        try {
            ByteArrayOutputStream e = new ByteArrayOutputStream();
            DataOutputStream seedBufferOut = new DataOutputStream(e);
            seedBufferOut.writeLong(System.currentTimeMillis());
            seedBufferOut.writeLong(System.nanoTime());
            seedBufferOut.writeInt(Process.myPid());
            seedBufferOut.writeInt(Process.myUid());
            seedBufferOut.write(BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
            seedBufferOut.close();
            return e.toByteArray();
        } catch (IOException var2) {
            throw new SecurityException("Failed to generate seed", var2);
        }
    }

    private static String getDeviceSerialNumber() {
        try {
            return (String)Build.class.getField("SERIAL").get((Object)null);
        } catch (Exception var1) {
            return null;
        }
    }

    private static byte[] getBuildFingerprintAndDeviceSerial() {
        StringBuilder result = new StringBuilder();
        String fingerprint = Build.FINGERPRINT;
        if(fingerprint != null) {
            result.append(fingerprint);
        }

        String serial = getDeviceSerialNumber();
        if(serial != null) {
            result.append(serial);
        }

        try {
            return result.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            throw new RuntimeException("UTF-8 encoding not supported");
        }
    }

    public static class LinuxPRNGSecureRandom extends SecureRandomSpi {
        private static final File URANDOM_FILE = new File("/dev/urandom");
        private static final Object sLock = new Object();
        private static DataInputStream sUrandomIn;
        private static OutputStream sUrandomOut;
        private boolean mSeeded;

        public LinuxPRNGSecureRandom() {
        }

        protected void engineSetSeed(byte[] bytes) {
            try {
                Object var3 = sLock;
                OutputStream e;
                synchronized(sLock) {
                    e = this.getUrandomOutputStream();
                }

                e.write(bytes);
                e.flush();
            } catch (IOException var10) {
                Log.w(PRNGFixes.class.getSimpleName(), "Failed to mix seed into " + URANDOM_FILE);
            } finally {
                this.mSeeded = true;
            }

        }

        protected void engineNextBytes(byte[] bytes) {
            if(!this.mSeeded) {
                this.engineSetSeed(PRNGFixes.generateSeed());
            }

            try {
                Object var3 = sLock;
                DataInputStream e;
                synchronized(sLock) {
                    e = this.getUrandomInputStream();
                }

                synchronized(e) {
                    e.readFully(bytes);
                }
            } catch (IOException var8) {
                throw new SecurityException("Failed to read from " + URANDOM_FILE, var8);
            }
        }

        protected byte[] engineGenerateSeed(int size) {
            byte[] seed = new byte[size];
            this.engineNextBytes(seed);
            return seed;
        }

        private DataInputStream getUrandomInputStream() {
            Object var1 = sLock;
            synchronized(sLock) {
                if(sUrandomIn == null) {
                    try {
                        sUrandomIn = new DataInputStream(new FileInputStream(URANDOM_FILE));
                    } catch (IOException var4) {
                        throw new SecurityException("Failed to open " + URANDOM_FILE + " for reading", var4);
                    }
                }

                return sUrandomIn;
            }
        }

        private OutputStream getUrandomOutputStream() throws IOException {
            Object var1 = sLock;
            synchronized(sLock) {
                if(sUrandomOut == null) {
                    sUrandomOut = new FileOutputStream(URANDOM_FILE);
                }

                return sUrandomOut;
            }
        }
    }

    private static class LinuxPRNGSecureRandomProvider extends Provider {
        public LinuxPRNGSecureRandomProvider() {
            super("LinuxPRNG", 1.0D, "A Linux-specific random number provider that uses /dev/urandom");
            this.put("SecureRandom.SHA1PRNG", PRNGFixes.LinuxPRNGSecureRandom.class.getName());
            this.put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}
