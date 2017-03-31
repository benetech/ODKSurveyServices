package org.opendatakit.demoAndroidlibraryClasses.properties;

import android.content.Context;
import java.util.TreeMap;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;

public abstract class PropertiesSingletonFactory {
    private final TreeMap<String, String> mGeneralDefaults;
    private final TreeMap<String, String> mDeviceDefaults;
    private final TreeMap<String, String> mSecureDefaults;
    private String gAppName = null;
    private PropertiesSingleton gSingleton = null;

    protected PropertiesSingletonFactory(TreeMap<String, String> generalDefaults, TreeMap<String, String> deviceDefaults, TreeMap<String, String> secureDefaults) {
        this.mGeneralDefaults = generalDefaults;
        this.mDeviceDefaults = deviceDefaults;
        this.mSecureDefaults = secureDefaults;
    }

    public synchronized PropertiesSingleton getSingleton(Context context, String appName) {
        if(appName != null && appName.length() != 0) {
            if(this.gSingleton == null || this.gAppName == null || !this.gAppName.equals(appName)) {
                this.gSingleton = new PropertiesSingleton(context, appName, this.mGeneralDefaults, this.mDeviceDefaults, this.mSecureDefaults);
                this.gAppName = appName;
            }

            this.gSingleton.setCurrentContext(context);
            return this.gSingleton;
        } else {
            throw new IllegalArgumentException("Unexpectedly null or empty appName");
        }
    }
}
