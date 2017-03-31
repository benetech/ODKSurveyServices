package org.opendatakit.demoAndroidlibraryClasses.logging;

import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerFactoryIf;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerImpl;

public class WebLoggerFactoryImpl implements WebLoggerFactoryIf {
    public WebLoggerFactoryImpl() {
    }

    public synchronized WebLoggerIf createWebLogger(String appName) {
        WebLoggerImpl logger = new WebLoggerImpl(appName);
        return logger;
    }
}
