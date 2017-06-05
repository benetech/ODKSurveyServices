package org.opendatakit.demoAndroidlibraryClasses.properties;

import android.content.Context;
import java.util.TreeMap;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingletonFactory;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator.IStaticFieldManipulator;
import org.opendatakit.survey.R;

public class CommonToolProperties {
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final String GROUPING_PASSWORD_SCREEN = "group.common.password_screen";
    public static final String GROUPING_DEVICE_CATEGORY = "group.common.device";
    public static final String GROUPING_SERVER_CATEGORY = "group.common.server";
    public static final String GROUPING_TOOL_TABLES_CATEGORY = "group.common.tables";
    public static final String GROUPING_TOOL_SURVEY_CATEGORY = "group.common.survey";
    public static final String GROUPING_TOOL_SCAN_CATEGORY = "group.common.scan";
    public static final String GROUPING_TOOL_SENSORS_CATEGORY = "group.common.sensors";
    public static final String KEY_SYNC_SERVER_URL = "common.sync_server_url";
    public static final String KEY_AUTHENTICATION_TYPE = "common.auth_credentials";
    public static final String KEY_ACCOUNT = "common.account";
    public static final String KEY_USERNAME = "common.username";
    public static final String KEY_FONT_SIZE = "common.font_size";
    public static final String KEY_SHOW_SPLASH = "common.show_splash";
    public static final String KEY_SPLASH_PATH = "common.splash_path";
    public static final String KEY_USE_HOME_SCREEN = "tables.custom_home_screen";
    public static final String KEY_CHANGE_SYNC_SERVER = "common.change_sync_server";
    public static final String KEY_CHANGE_AUTHENTICATION_TYPE = "common.change_auth_credentials";
    public static final String KEY_CHANGE_GOOGLE_ACCOUNT = "common.change_google_account";
    public static final String KEY_CHANGE_USERNAME_PASSWORD = "common.change_username_password";
    public static final String KEY_CHANGE_FONT_SIZE = "common.change_font_size";
    public static final String KEY_CHANGE_SPLASH_SETTINGS = "common.change_splash_settings";
    public static final String KEY_CHANGE_USE_HOME_SCREEN = "tables.change_custom_home_screen";
    public static final String KEY_AUTH = "common.auth";
    public static final String KEY_PASSWORD = "common.password";
    public static final String KEY_ROLES_LIST = "common.roles";
    public static final String KEY_USERS_LIST = "common.users";
    public static final String KEY_ADMIN_PW = "common.admin_pw";
    public static final String KEY_OFFICE_ID = "common.office_id";
    public static final String LAST_FORMS_UPDATE_TIME = "common.last_forms_update_time";
    public static final String KEY_REPORTER_NAME = "common.reporter_name";
    public static final String KEY_REPORTER_ID = "common.reporter_id";
    private static CommonToolProperties.CommonPropertiesSingletonFactory factory = null;

    public CommonToolProperties() {
    }

    public static void accumulateProperties(Context context, TreeMap<String, String> generalProperties, TreeMap<String, String> deviceProperties, TreeMap<String, String> secureProperties) {
        generalProperties.put("common.sync_server_url", context.getString(R.string.default_sync_server_url));
        generalProperties.put("common.font_size", Integer.toString(16));
        generalProperties.put("common.show_splash", "true");
        generalProperties.put("common.splash_path", "ODK Default");
        generalProperties.put("common.change_sync_server", "true");
        generalProperties.put("common.change_auth_credentials", "true");
        generalProperties.put("common.change_google_account", "true");
        generalProperties.put("common.change_username_password", "true");
        generalProperties.put("common.change_font_size", "true");
        generalProperties.put("common.change_splash_settings", "true");
        generalProperties.put("common.last_forms_update_time", "");
        deviceProperties.put("common.auth_credentials", "none");
        deviceProperties.put("common.account", "");
        deviceProperties.put("common.username", "");
        deviceProperties.put("common.office_id", "");
        deviceProperties.put(PropertiesSingleton.toolVersionPropertyName("survey"), "");
        deviceProperties.put(PropertiesSingleton.toolVersionPropertyName("scan"), "");
        deviceProperties.put(PropertiesSingleton.toolVersionPropertyName("tables"), "");
        deviceProperties.put(PropertiesSingleton.toolVersionPropertyName("sensors"), "");
        deviceProperties.put(PropertiesSingleton.toolFirstRunPropertyName("survey"), "");
        deviceProperties.put(PropertiesSingleton.toolFirstRunPropertyName("scan"), "");
        deviceProperties.put(PropertiesSingleton.toolFirstRunPropertyName("tables"), "");
        deviceProperties.put(PropertiesSingleton.toolFirstRunPropertyName("sensors"), "");
        secureProperties.put("common.auth", "");
        secureProperties.put("common.password", "");
        secureProperties.put("common.roles", "");
        secureProperties.put("common.users", "");
        secureProperties.put("common.admin_pw", "");
    }

    public static synchronized PropertiesSingleton get(Context context, String appName) {
        if(factory == null) {
            TreeMap generalProperties = new TreeMap();
            TreeMap deviceProperties = new TreeMap();
            TreeMap secureProperties = new TreeMap();
            accumulateProperties(context, generalProperties, deviceProperties, secureProperties);
            factory = new CommonToolProperties.CommonPropertiesSingletonFactory(generalProperties, deviceProperties, secureProperties);
        }

        return factory.getSingleton(context, appName);
    }

    static {
        StaticStateManipulator.get().register(50, new IStaticFieldManipulator() {
            public void reset() {
                CommonToolProperties.factory = null;
            }
        });
    }

    private static class CommonPropertiesSingletonFactory extends PropertiesSingletonFactory {
        private CommonPropertiesSingletonFactory(TreeMap<String, String> generalDefaults, TreeMap<String, String> deviceDefaults, TreeMap<String, String> secureDefaults) {
            super(generalDefaults, deviceDefaults, secureDefaults);
        }
    }
}
