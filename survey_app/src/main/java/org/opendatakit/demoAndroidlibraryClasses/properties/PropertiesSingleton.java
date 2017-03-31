package org.opendatakit.demoAndroidlibraryClasses.properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;

public class PropertiesSingleton {
    private static final String t = "PropertiesSingleton";
    private static final String GENERAL_PROPERTIES_FILENAME = "app.properties";
    private static final String DEFAULT_DEVICE_PROPERTIES_FILENAME = "default.device.properties";
    private static final String DEVICE_PROPERTIES_FILENAME = "device.properties";
    private final String mAppName;
    private long lastGeneralModified = 0L;
    private long lastDeviceModified = 0L;
    private final TreeMap<String, String> mGeneralDefaults;
    private final TreeMap<String, String> mDeviceDefaults;
    private final TreeMap<String, String> mSecureDefaults;
    private final Properties mGeneralProps;
    private final Properties mGlobalDeviceProps;
    private final Properties mDeviceProps;
    private Context mBaseContext;
    private static final String TOOL_INITIALIZATION_SUFFIX = ".tool_last_initialization_start_time";

    public String getAppName() {
        return this.mAppName;
    }

    private boolean isSecureProperty(String propertyName) {
        return this.mSecureDefaults.containsKey(propertyName);
    }

    private boolean isDeviceProperty(String propertyName) {
        return this.mDeviceDefaults.containsKey(propertyName);
    }

    void setCurrentContext(Context context) {
        try {
            this.mBaseContext = context;
            if(this.isModified()) {
                this.init();
            }

        } catch (Exception var3) {
            var3.printStackTrace();
            throw new IllegalStateException("ODK Services must be installed!");
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        try {
            return !context.getPackageName().equals("org.opendatakit.survey")?null:context.getSharedPreferences(context.getPackageName(), 4);
        } catch (Exception var2) {
            Log.e("PropertiesSingleton", "Unable to access SharedPreferences!");
            return null;
        }
    }

    public boolean containsKey(String propertyName) {
        if(this.isSecureProperty(propertyName)) {
            SharedPreferences sharedPreferences = getSharedPreferences(this.mBaseContext);
            return sharedPreferences == null?false:sharedPreferences.contains(this.mAppName + "_" + propertyName);
        } else {
            return this.isDeviceProperty(propertyName)?this.mDeviceProps.containsKey(propertyName):this.mGeneralProps.containsKey(propertyName);
        }
    }

    public String getProperty(String propertyName) {
        if(this.isSecureProperty(propertyName)) {
            SharedPreferences sharedPreferences = getSharedPreferences(this.mBaseContext);
            return sharedPreferences == null?null:sharedPreferences.getString(this.mAppName + "_" + propertyName, (String)null);
        } else {
            return this.isDeviceProperty(propertyName)?this.mDeviceProps.getProperty(propertyName):this.mGeneralProps.getProperty(propertyName);
        }
    }

    public Boolean getBooleanProperty(String propertyName) {
        Boolean booleanSetting = Boolean.TRUE;
        String value = this.getProperty(propertyName);
        if(value != null && value.length() != 0) {
            if(!"true".equalsIgnoreCase(value)) {
                booleanSetting = Boolean.FALSE;
            }

            return booleanSetting;
        } else {
            return null;
        }
    }

    public void setBooleanProperty(String propertyName, boolean value) {
        this.setProperty(propertyName, Boolean.toString(value));
    }

    public Integer getIntegerProperty(String propertyName) {
        String value = this.getProperty(propertyName);
        if(value == null) {
            return null;
        } else {
            try {
                int e = Integer.parseInt(value);
                return Integer.valueOf(e);
            } catch (NumberFormatException var4) {
                return null;
            }
        }
    }

    public void setIntegerProperty(String propertyName, int value) {
        this.setProperty(propertyName, Integer.toString(value));
    }

    public void removeProperty(String propertyName) {
        if(this.isSecureProperty(propertyName)) {
            SharedPreferences sharedPreferences = getSharedPreferences(this.mBaseContext);
            if(sharedPreferences == null) {
                throw new IllegalStateException("Unable to remove SharedPreferences");
            }

            sharedPreferences.edit().remove(this.mAppName + "_" + propertyName).commit();
        } else if(this.isDeviceProperty(propertyName)) {
            this.mDeviceProps.remove(propertyName);
        } else {
            this.mGeneralProps.remove(propertyName);
        }

    }

    public void setProperty(String propertyName, String value) {
        if(this.isSecureProperty(propertyName)) {
            SharedPreferences sharedPreferences = getSharedPreferences(this.mBaseContext);
            if(sharedPreferences == null) {
                throw new IllegalStateException("Unable to write SharedPreferences");
            }

            sharedPreferences.edit().putString(this.mAppName + "_" + propertyName, value).commit();
        } else {
            if(this.isModified()) {
                this.readProperties(false);
            }

            if(this.isDeviceProperty(propertyName)) {
                this.mDeviceProps.setProperty(propertyName, value);
            } else {
                this.mGeneralProps.setProperty(propertyName, value);
            }

            this.writeProperties();
        }

    }

    public boolean shouldRunInitializationTask(String toolName) {
        if(this.isModified()) {
            this.readProperties(false);
        }

        String value = this.mDeviceProps.getProperty(toolInitializationPropertyName(toolName));
        return value != null && value.length() != 0?Boolean.FALSE.booleanValue():Boolean.TRUE.booleanValue();
    }

    public void clearRunInitializationTask(String toolName) {
        if(this.isModified()) {
            this.readProperties(false);
        }

        this.mDeviceProps.setProperty(toolInitializationPropertyName(toolName), TableConstants.nanoSecondsFromMillis(Long.valueOf(System.currentTimeMillis())));
        this.writeProperties();
    }

    public void setRunInitializationTask(String toolName) {
        if(this.isModified()) {
            this.readProperties(false);
        }

        this.mDeviceProps.remove(toolInitializationPropertyName(toolName));
        this.writeProperties();
    }

    public void setAllRunInitializationTasks() {
        if(this.isModified()) {
            this.readProperties(false);
        }

        ArrayList keysToRemove = new ArrayList();
        Iterator i$ = this.mDeviceProps.keySet().iterator();

        Object okey;
        while(i$.hasNext()) {
            okey = i$.next();
            if(isToolInitializationPropertyName((String)okey)) {
                keysToRemove.add(okey);
            }
        }

        i$ = keysToRemove.iterator();

        while(i$.hasNext()) {
            okey = i$.next();
            this.mDeviceProps.remove(okey);
        }

        this.writeProperties();
    }

    public String getActiveUser() {
        String CREDENTIAL_TYPE_NONE = this.mBaseContext.getString(R.string.credential_type_none);
        String CREDENTIAL_TYPE_USERNAME_PASSWORD = this.mBaseContext.getString(R.string.credential_type_username_password);
        String CREDENTIAL_TYPE_GOOGLE_ACCOUNT = this.mBaseContext.getString(R.string.credential_type_google_account);
        String authType = this.getProperty("common.auth_credentials");
        if(authType.equals(CREDENTIAL_TYPE_NONE)) {
            return "anonymous";
        } else {
            String name;
            if(authType.equals(CREDENTIAL_TYPE_USERNAME_PASSWORD)) {
                name = this.getProperty("common.username");
                return name != null?"username:" + name:"anonymous";
            } else if(authType.equals(CREDENTIAL_TYPE_GOOGLE_ACCOUNT)) {
                name = this.getProperty("common.account");
                return name != null?"mailto:" + name:"anonymous";
            } else {
                throw new IllegalStateException("unexpected authentication type!");
            }
        }
    }

    public String getLocale() {
        return Locale.getDefault().toString();
    }

    private static String toolInitializationPropertyName(String toolName) {
        return toolName + ".tool_last_initialization_start_time";
    }

    private static boolean isToolInitializationPropertyName(String toolName) {
        return toolName.endsWith(".tool_last_initialization_start_time");
    }

    public static String toolVersionPropertyName(String toolName) {
        return toolName + ".tool_version_code";
    }

    public static String toolFirstRunPropertyName(String toolName) {
        return toolName + ".tool_first_run";
    }

    PropertiesSingleton(Context context, String appName, TreeMap<String, String> plainDefaults, TreeMap<String, String> deviceDefaults, TreeMap<String, String> secureDefaults) {
        this.mAppName = appName;
        this.mGeneralDefaults = plainDefaults;
        this.mDeviceDefaults = deviceDefaults;
        this.mSecureDefaults = secureDefaults;
        this.mGeneralProps = new Properties();
        this.mGlobalDeviceProps = new Properties();
        this.mDeviceProps = new Properties();
        this.setCurrentContext(context);
    }

    void init() {
        this.lastGeneralModified = 0L;
        this.lastDeviceModified = 0L;
        this.mGeneralProps.clear();
        this.mGlobalDeviceProps.clear();
        this.mDeviceProps.clear();
        this.readProperties(true);
        boolean dirtyProps = false;
        Iterator sharedPreferences = this.mGeneralDefaults.entrySet().iterator();

        Entry i$;
        while(sharedPreferences.hasNext()) {
            i$ = (Entry)sharedPreferences.next();
            if(!this.mGeneralProps.containsKey(i$.getKey())) {
                this.mGeneralProps.setProperty((String)i$.getKey(), (String)i$.getValue());
                dirtyProps = true;
            }
        }

        sharedPreferences = this.mDeviceDefaults.entrySet().iterator();

        while(sharedPreferences.hasNext()) {
            i$ = (Entry)sharedPreferences.next();
            if(this.mGlobalDeviceProps.containsKey(i$.getKey())) {
                i$.setValue(this.mGlobalDeviceProps.getProperty((String)i$.getKey()));
            }
        }

        sharedPreferences = this.mDeviceDefaults.entrySet().iterator();

        while(sharedPreferences.hasNext()) {
            i$ = (Entry)sharedPreferences.next();
            if(!this.mDeviceProps.containsKey(i$.getKey())) {
                this.mDeviceProps.setProperty((String)i$.getKey(), (String)i$.getValue());
                dirtyProps = true;
            }
        }

        sharedPreferences = this.mSecureDefaults.entrySet().iterator();

        while(sharedPreferences.hasNext()) {
            i$ = (Entry)sharedPreferences.next();
            if(this.mGeneralProps.containsKey(i$.getKey())) {
                this.mGeneralProps.remove(i$.getKey());
                dirtyProps = true;
            }
        }

        SharedPreferences sharedPreferences1 = getSharedPreferences(this.mBaseContext);
        if(sharedPreferences1 != null) {
            Iterator i$1 = this.mSecureDefaults.entrySet().iterator();

            while(i$1.hasNext()) {
                Entry entry = (Entry)i$1.next();
                if(!sharedPreferences1.contains(this.mAppName + "_" + (String)entry.getKey())) {
                    sharedPreferences1.edit().putString(this.mAppName + "_" + (String)entry.getKey(), (String)entry.getValue()).commit();
                }
            }
        }

        if(dirtyProps) {
            this.writeProperties();
        }

    }

    private void verifyDirectories() {
        try {
            ODKFileUtils.verifyExternalStorageAvailability();
            ODKFileUtils.assertDirectoryStructure(this.mAppName);
        } catch (Exception var2) {
            Log.e("PropertiesSingleton", "External storage not available");
            throw new IllegalArgumentException("External storage not available");
        }
    }

    public boolean isModified() {
        File configFile = new File(ODKFileUtils.getAssetsFolder(this.mAppName), "app.properties");
        if(configFile.exists()) {
            if(this.lastGeneralModified != configFile.lastModified()) {
                return true;
            } else {
                configFile = new File(ODKFileUtils.getDataFolder(this.mAppName), "device.properties");
                return configFile.exists()?this.lastDeviceModified != configFile.lastModified():true;
            }
        } else {
            return true;
        }
    }

    public void readProperties(boolean includingGlobalDeviceProps) {
        this.verifyDirectories();
        FileInputStream configFileInputStream = null;

        File e;
        try {
            e = new File(ODKFileUtils.getAssetsFolder(this.mAppName), "app.properties");
            if(e.exists()) {
                configFileInputStream = new FileInputStream(e);
                this.mGeneralProps.loadFromXML(configFileInputStream);
                this.lastGeneralModified = e.lastModified();
            }
        } catch (Exception var54) {
            WebLogger.getLogger(this.mAppName).printStackTrace(var54);
        } finally {
            if(configFileInputStream != null) {
                try {
                    configFileInputStream.close();
                } catch (IOException var50) {
                    WebLogger.getLogger(this.mAppName).printStackTrace(var50);
                }
            }

        }

        if(includingGlobalDeviceProps) {
            configFileInputStream = null;

            try {
                e = new File(ODKFileUtils.getAssetsFolder(this.mAppName), "default.device.properties");
                if(e.exists()) {
                    configFileInputStream = new FileInputStream(e);
                    this.mGlobalDeviceProps.loadFromXML(configFileInputStream);
                }
            } catch (Exception var53) {
                WebLogger.getLogger(this.mAppName).printStackTrace(var53);
            } finally {
                if(configFileInputStream != null) {
                    try {
                        configFileInputStream.close();
                    } catch (IOException var51) {
                        WebLogger.getLogger(this.mAppName).printStackTrace(var51);
                    }
                }

            }
        }

        configFileInputStream = null;

        try {
            e = new File(ODKFileUtils.getDataFolder(this.mAppName), "device.properties");
            if(e.exists()) {
                configFileInputStream = new FileInputStream(e);
                this.mDeviceProps.loadFromXML(configFileInputStream);
                this.lastDeviceModified = e.lastModified();
            }
        } catch (Exception var52) {
            WebLogger.getLogger(this.mAppName).printStackTrace(var52);
        } finally {
            if(configFileInputStream != null) {
                try {
                    configFileInputStream.close();
                } catch (IOException var49) {
                    WebLogger.getLogger(this.mAppName).printStackTrace(var49);
                }
            }

        }

    }

    public void writeProperties() {
        this.verifyDirectories();

        File e;
        FileOutputStream configFileOutputStream;
        File configFile;
        boolean fileSuccess;
        try {
            e = new File(ODKFileUtils.getAssetsFolder(this.mAppName), "app.properties.temp");
            configFileOutputStream = new FileOutputStream(e, false);
            this.mGeneralProps.storeToXML(configFileOutputStream, (String)null, "UTF-8");
            configFileOutputStream.close();
            configFile = new File(ODKFileUtils.getAssetsFolder(this.mAppName), "app.properties");
            fileSuccess = e.renameTo(configFile);
            if(!fileSuccess) {
                WebLogger.getLogger(this.mAppName).i("PropertiesSingleton", "Temporary General Config File Rename Failed!");
            } else {
                this.lastGeneralModified = configFile.lastModified();
            }
        } catch (Exception var6) {
            WebLogger.getLogger(this.mAppName).printStackTrace(var6);
        }

        try {
            e = new File(ODKFileUtils.getDataFolder(this.mAppName), "device.properties.temp");
            configFileOutputStream = new FileOutputStream(e, false);
            this.mDeviceProps.storeToXML(configFileOutputStream, (String)null, "UTF-8");
            configFileOutputStream.close();
            configFile = new File(ODKFileUtils.getDataFolder(this.mAppName), "device.properties");
            fileSuccess = e.renameTo(configFile);
            if(!fileSuccess) {
                WebLogger.getLogger(this.mAppName).i("PropertiesSingleton", "Temporary Device Config File Rename Failed!");
            } else {
                this.lastDeviceModified = configFile.lastModified();
            }
        } catch (Exception var5) {
            WebLogger.getLogger(this.mAppName).printStackTrace(var5);
        }

    }

    public void clearSettings() {
        try {
            File f = new File(ODKFileUtils.getDataFolder(this.mAppName), "device.properties");
            if(f.exists()) {
                f.delete();
            }

            SharedPreferences sharedPreferences = getSharedPreferences(this.mBaseContext);
            if(sharedPreferences == null) {
                throw new IllegalStateException("Clearing settings should only be done within ODK Services");
            }

            Map allPreferences = sharedPreferences.getAll();
            Iterator i$ = allPreferences.keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                if(key.startsWith(this.mAppName + "_")) {
                    sharedPreferences.edit().remove(key).commit();
                }
            }
        } finally {
            this.init();
        }

    }
}
