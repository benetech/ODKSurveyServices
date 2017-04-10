package org.opendatakit.demoAndroidlibraryClasses.properties;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.util.HashMap;
import java.util.Locale;

public class PropertyManager {
    private static final String ANDROID6_FAKE_MAC = "02:00:00:00:00:00";
    private final HashMap<String, String> mProperties = new HashMap();
    public static final String DEVICE_ID_PROPERTY = "deviceid";
    public static final String SUBSCRIBER_ID_PROPERTY = "subscriberid";
    public static final String SIM_SERIAL_PROPERTY = "simserial";
    public static final String PHONE_NUMBER_PROPERTY = "phonenumber";
    public static final String OR_DEVICE_ID_PROPERTY = "uri:deviceid";
    public static final String OR_SUBSCRIBER_ID_PROPERTY = "uri:subscriberid";
    public static final String OR_SIM_SERIAL_PROPERTY = "uri:simserial";
    public static final String OR_PHONE_NUMBER_PROPERTY = "uri:phonenumber";
    public static final String LOCALE = "locale";
    public static final String ACTIVE_USER = "active_user";
    public static final String USERNAME = "username";
    public static final String OR_USERNAME = "uri:username";
    public static final String EMAIL = "email";
    public static final String OR_EMAIL = "uri:email";
    public static final String APP_NAME = "appName";
    public static final String INSTANCE_DIRECTORY = "instancedirectory";
    public static final String URI_FRAGMENT_NEW_INSTANCE_FILE_WITHOUT_COLON = "urifragmentnewinstancefile";
    public static final String URI_FRAGMENT_NEW_INSTANCE_FILE = "urifragmentnewinstancefile:";

    public PropertyManager(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = mTelephonyManager.getDeviceId();
        String orDeviceId = null;
        if(deviceId != null) {
            if(!deviceId.contains("*") && !deviceId.contains("000000000000000")) {
                orDeviceId = "imei:" + deviceId;
            } else {
                deviceId = Secure.getString(context.getContentResolver(), "android_id");
                orDeviceId = "android_id:" + deviceId;
            }
        }

        if(deviceId == null) {
            WifiManager value = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = value.getConnectionInfo();
            if(info != null) {
                String macId = info.getMacAddress();
                if(macId != null && !"02:00:00:00:00:00".equals(macId)) {
                    deviceId = macId;
                    orDeviceId = "mac:" + macId;
                }
            }
        }

        if(deviceId == null) {
            deviceId = Secure.getString(context.getContentResolver(), "android_id");
            orDeviceId = "android_id:" + deviceId;
        }

        this.mProperties.put("deviceid", deviceId);
        this.mProperties.put("uri:deviceid", orDeviceId);
        String value1 = mTelephonyManager.getSubscriberId();
        if(value1 != null) {
            this.mProperties.put("subscriberid", value1);
            this.mProperties.put("uri:subscriberid", "imsi:" + value1);
        }

        value1 = mTelephonyManager.getSimSerialNumber();
        if(value1 != null) {
            this.mProperties.put("simserial", value1);
            this.mProperties.put("uri:simserial", "simserial:" + value1);
        }

        value1 = mTelephonyManager.getLine1Number();
        if(value1 != null) {
            this.mProperties.put("phonenumber", value1);
            this.mProperties.put("uri:phonenumber", "tel:" + value1);
        }

    }

    public String getSingularProperty(String rawPropertyName, PropertyManager.DynamicPropertiesInterface callback) {
        String propertyName = rawPropertyName.toLowerCase(Locale.ENGLISH);
        if("active_user".equals(propertyName)) {
            return callback.getActiveUser();
        } else if("locale".equals(propertyName)) {
            return callback.getLocale();
        } else if("username".equals(propertyName)) {
            return callback.getUsername();
        } else {
            String ext;
            if("uri:username".equals(propertyName)) {
                ext = callback.getUsername();
                return ext == null?null:"username:" + ext;
            } else if("email".equals(propertyName)) {
                return callback.getUserEmail();
            } else if("uri:email".equals(propertyName)) {
                ext = callback.getUserEmail();
                return ext == null?null:"mailto:" + ext;
            } else if("appName".equals(propertyName)) {
                ext = callback.getAppName();
                return ext == null?null:ext;
            } else if("instancedirectory".equals(propertyName)) {
                ext = callback.getInstanceDirectory();
                return ext == null?null:ext;
            } else if(propertyName.startsWith("urifragmentnewinstancefile")) {
                if(propertyName.startsWith("urifragmentnewinstancefile:")) {
                    ext = rawPropertyName.substring("urifragmentnewinstancefile:".length());
                } else {
                    ext = "";
                }

                String value = callback.getUriFragmentNewInstanceFile((String)this.mProperties.get("uri:deviceid"), ext);
                return value;
            } else {
                return (String)this.mProperties.get(propertyName);
            }
        }
    }

    public interface DynamicPropertiesInterface {
        String getActiveUser();

        String getLocale();

        String getUsername();

        String getUserEmail();

        String getAppName();

        String getInstanceDirectory();

        String getUriFragmentNewInstanceFile(String var1, String var2);
    }
}
