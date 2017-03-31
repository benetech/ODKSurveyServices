package org.opendatakit.demoAndroidlibraryClasses.logging;

import android.os.FileObserver;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.joda.time.DateTime;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class WebLoggerImpl implements WebLoggerIf {
    private static final long FLUSH_INTERVAL = 12000L;
    private static final long MILLISECONDS_DAY = 86400000L;
    private static final int ASSERT = 1;
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;
    private static final int SUCCESS = 7;
    private static final int TIP = 8;
    private static final int MIN_LOG_LEVEL_TO_SPEW = 4;
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH";
    private static final String LOG_LINE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private final String appName;
    private String dateStamp = null;
    private OutputStreamWriter logFile = null;
    private long lastFlush = 0L;
    private WebLoggerImpl.LoggingFileObserver loggingObserver = null;

    public String getFormattedFileDateNow() {
        return (new DateTime()).toString("yyyy-MM-dd_HH");
    }

    public String getFormattedLogLineDateNow() {
        return (new DateTime()).toString("yyyy-MM-dd HH:mm:ss.SSS");
    }

    WebLoggerImpl(String appName) {
        this.appName = appName;
    }

    public synchronized void close() {
        if(this.logFile != null) {
            OutputStreamWriter writer = this.logFile;
            this.logFile = null;
            String loggingPath = ODKFileUtils.getLoggingFolder(this.appName);
            File loggingDirectory = new File(loggingPath);
            if(!loggingDirectory.exists()) {
                Log.e("WebLogger", "Logging directory does not exist -- special handling under " + this.appName);

                try {
                    writer.close();
                } catch (IOException var6) {
                    ;
                }

                loggingDirectory.delete();
            } else {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException var5) {
                    Log.e("WebLogger", "Unable to flush and close " + this.appName + " WebLogger");
                }
            }
        }

    }

    public synchronized void staleFileScan(long now) {
        try {
            ODKFileUtils.verifyExternalStorageAvailability();
            ODKFileUtils.assertDirectoryStructure(this.appName);
            String e = ODKFileUtils.getLoggingFolder(this.appName);
            final long distantPast = now - 2592000000L;
            File loggingDirectory = new File(e);
            if(!loggingDirectory.exists() && !loggingDirectory.mkdirs()) {
                Log.e("WebLogger", "Unable to create logging directory");
                return;
            }

            if(!loggingDirectory.isDirectory()) {
                Log.e("WebLogger", "Logging Directory exists but is not a directory!");
                return;
            }

            File[] stale = loggingDirectory.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.lastModified() < distantPast;
                }
            });
            if(stale != null) {
                File[] arr$ = stale;
                int len$ = stale.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    File f = arr$[i$];
                    f.delete();
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

    }

    private synchronized void log(String logMsg) throws IOException {
        String curDateStamp = this.getFormattedFileDateNow();
        if(this.logFile == null || this.dateStamp == null || !curDateStamp.equals(this.dateStamp)) {
            if(this.logFile != null) {
                OutputStreamWriter loggingPath = this.logFile;
                this.logFile = null;

                try {
                    loggingPath.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

            try {
                ODKFileUtils.verifyExternalStorageAvailability();
                ODKFileUtils.assertDirectoryStructure(this.appName);
            } catch (Exception var10) {
                var10.printStackTrace();
                Log.e("WebLogger", "Unable to create logging directory");
                return;
            }

            String loggingPath1 = ODKFileUtils.getLoggingFolder(this.appName);
            File loggingDirectory = new File(loggingPath1);
            if(!loggingDirectory.isDirectory()) {
                Log.e("WebLogger", "Logging Directory exists but is not a directory!");
                return;
            }

            if(this.loggingObserver == null) {
                this.loggingObserver = new WebLoggerImpl.LoggingFileObserver(loggingDirectory.getAbsolutePath());
            }

            File f = new File(loggingDirectory, curDateStamp + ".log");

            try {
                FileOutputStream e = new FileOutputStream(f, true);
                this.logFile = new OutputStreamWriter(new BufferedOutputStream(e), "UTF-8");
                this.dateStamp = curDateStamp;
                this.logFile.write("---- starting ----\n");
            } catch (Exception var9) {
                var9.printStackTrace();
                Log.e("WebLogger", "Unexpected exception while opening logging file: " + var9.toString());

                try {
                    if(this.logFile != null) {
                        this.logFile.close();
                    }
                } catch (Exception var8) {
                    ;
                }

                this.logFile = null;
                return;
            }
        }

        if(this.logFile != null) {
            this.logFile.write(logMsg + "\n");
        }

        if(this.lastFlush + 12000L < System.currentTimeMillis()) {
            this.logFile.write("---- flushing ----\n");
            this.logFile.flush();
            this.lastFlush = System.currentTimeMillis();
        }

    }

    public void log(int severity, String t, String logMsg) {
        try {
            String e = logMsg;
            String curLogLineStamp = this.getFormattedLogLineDateNow();
            if(!logMsg.startsWith(curLogLineStamp.substring(0, 16))) {
                logMsg = curLogLineStamp + " " + logMsg;
            } else {
                e = logMsg.length() > curLogLineStamp.length()?logMsg.substring(curLogLineStamp.length()):logMsg;
            }

            if(e.length() > 128) {
                e = e.substring(0, 125) + "...";
            }

            String androidTag = t;
            int periodIdx = t.lastIndexOf(46);
            if(t.length() > 26 && periodIdx != -1) {
                androidTag = t.substring(periodIdx + 1);
            }

            if(severity >= 4) {
                if(severity == 6) {
                    Log.e(androidTag, e);
                } else if(severity == 5) {
                    Log.w(androidTag, e);
                } else if(severity != 4 && severity != 7 && severity != 8) {
                    if(severity == 3) {
                        Log.d(androidTag, e);
                    } else {
                        Log.v(androidTag, e);
                    }
                } else {
                    Log.i(androidTag, e);
                }
            }

            switch(severity) {
                case 1:
                    logMsg = "A/" + t + ": " + logMsg;
                    break;
                case 2:
                    logMsg = "V/" + t + ": " + logMsg;
                    break;
                case 3:
                    logMsg = "D/" + t + ": " + logMsg;
                    break;
                case 4:
                    logMsg = "I/" + t + ": " + logMsg;
                    break;
                case 5:
                    logMsg = "W/" + t + ": " + logMsg;
                    break;
                case 6:
                    logMsg = "E/" + t + ": " + logMsg;
                    break;
                case 7:
                    logMsg = "S/" + t + ": " + logMsg;
                    break;
                case 8:
                    logMsg = "T/" + t + ": " + logMsg;
                    break;
                default:
                    Log.d(t, logMsg);
                    logMsg = "?/" + t + ": " + logMsg;
            }

            this.log(logMsg);
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    public void a(String t, String logMsg) {
        this.log(1, t, logMsg);
    }

    public void t(String t, String logMsg) {
        this.log(8, t, logMsg);
    }

    public void v(String t, String logMsg) {
        this.log(2, t, logMsg);
    }

    public void d(String t, String logMsg) {
        this.log(3, t, logMsg);
    }

    public void i(String t, String logMsg) {
        this.log(4, t, logMsg);
    }

    public void w(String t, String logMsg) {
        this.log(5, t, logMsg);
    }

    public void e(String t, String logMsg) {
        this.log(6, t, logMsg);
    }

    public void s(String t, String logMsg) {
        this.log(7, t, logMsg);
    }

    public void printStackTrace(Throwable e) {
        e.printStackTrace();
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        try {
            PrintStream w = new PrintStream(ba, false, "UTF-8");
            e.printStackTrace(w);
            w.flush();
            w.close();
            this.log(ba.toString("UTF-8"));
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException("unable to specify UTF-8 Charset!");
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    private class LoggingFileObserver extends FileObserver {
        public LoggingFileObserver(String path) {
            super(path, 3072);
        }

        public void onEvent(int event, String path) {
            if(WebLoggerImpl.this.logFile != null) {
                try {
                    if(event == 2048) {
                        WebLoggerImpl.this.logFile.flush();
                    }

                    WebLoggerImpl.this.logFile.close();
                } catch (IOException var4) {
                    var4.printStackTrace();
                    Log.w("WebLogger", "detected delete or move of logging directory -- shutting down");
                }

                WebLoggerImpl.this.logFile = null;
            }

        }
    }
}
