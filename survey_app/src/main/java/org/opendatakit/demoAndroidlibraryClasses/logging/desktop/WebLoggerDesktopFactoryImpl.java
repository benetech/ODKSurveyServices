package org.opendatakit.demoAndroidlibraryClasses.logging.desktop;


import android.annotation.SuppressLint;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerFactoryIf;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;

@SuppressLint({"NewApi"})
public class WebLoggerDesktopFactoryImpl implements WebLoggerFactoryIf {
    public WebLoggerDesktopFactoryImpl() {
    }

    public synchronized WebLoggerIf createWebLogger(String appName) {
        WebLoggerDesktopFactoryImpl.WebLoggerDesktopImpl logger = new WebLoggerDesktopFactoryImpl.WebLoggerDesktopImpl(appName);
        return logger;
    }

    public class WebLoggerDesktopImpl implements WebLoggerIf {
        private final String appName;

        public WebLoggerDesktopImpl(String appName) {
            this.appName = appName;
        }

        public void staleFileScan(long now) {
        }

        public void close() {
        }

        public void log(int severity, String t, String logMsg) {
            Logger.getGlobal().log(Level.INFO, t, "N:" + severity + "/" + logMsg);
        }

        public void a(String t, String logMsg) {
            Logger.getGlobal().log(Level.FINEST, t, logMsg);
        }

        public void t(String t, String logMsg) {
            Logger.getGlobal().log(Level.FINER, t, "Trace/" + logMsg);
        }

        public void v(String t, String logMsg) {
            Logger.getGlobal().log(Level.FINER, t, "Verbose/" + logMsg);
        }

        public void d(String t, String logMsg) {
            Logger.getGlobal().log(Level.FINE, t, logMsg);
        }

        public void i(String t, String logMsg) {
            Logger.getGlobal().log(Level.INFO, t, logMsg);
        }

        public void w(String t, String logMsg) {
            Logger.getGlobal().log(Level.WARNING, t, logMsg);
        }

        public void e(String t, String logMsg) {
            Logger.getGlobal().log(Level.SEVERE, t, logMsg);
        }

        public void s(String t, String logMsg) {
            Logger.getGlobal().log(Level.INFO, t, "Success/" + logMsg);
        }

        public void printStackTrace(Throwable e) {
            Logger.getGlobal().throwing("unknown", "unknown", e);
        }
    }
}
