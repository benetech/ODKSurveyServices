package org.opendatakit.demoAndroidlibraryClasses.logging;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerFactoryIf;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerFactoryImpl;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator.IStaticFieldManipulator;

public class WebLogger {
    private static final long MILLISECONDS_DAY = 86400000L;
    private static long lastStaleScan = 0L;
    private static final Map<String, WebLoggerIf> loggers = new HashMap();
    private static WebLoggerFactoryIf webLoggerFactory = new WebLoggerFactoryImpl();
    private static WebLogger.ThreadLogger contextLogger;

    public WebLogger() {
    }

    public static void setFactory(WebLoggerFactoryIf webLoggerFactoryImpl) {
        webLoggerFactory = webLoggerFactoryImpl;
    }

    public static synchronized void closeAll() {
        Iterator i$ = loggers.values().iterator();

        while(i$.hasNext()) {
            WebLoggerIf l = (WebLoggerIf)i$.next();
            l.close();
        }

        loggers.clear();
        contextLogger = new WebLogger.ThreadLogger();
    }

    public static WebLoggerIf getContextLogger() {
        String appNameOfThread = (String)contextLogger.get();
        return appNameOfThread != null?getLogger(appNameOfThread):null;
    }

    public static synchronized WebLoggerIf getLogger(String appName) {
        WebLoggerIf logger = (WebLoggerIf)loggers.get(appName);
        if(logger == null) {
            logger = webLoggerFactory.createWebLogger(appName);
            loggers.put(appName, logger);
        }

        contextLogger.set(appName);
        long now = System.currentTimeMillis();
        if(lastStaleScan + 86400000L < now) {
            try {
                logger.staleFileScan(now);
            } finally {
                lastStaleScan = now;
            }
        }

        return logger;
    }

    static {
        StaticStateManipulator.get().register(99, new IStaticFieldManipulator() {
            public void reset() {
                WebLogger.closeAll();
            }
        });
        contextLogger = new WebLogger.ThreadLogger();
    }

    private static class ThreadLogger extends ThreadLocal<String> {
        private ThreadLogger() {
        }

        protected String initialValue() {
            return null;
        }
    }
}
