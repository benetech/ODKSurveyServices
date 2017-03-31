package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public final class SurveyConfigurationColumns implements BaseColumns {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.opendatakit.survey";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.opendatakit.survey";
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String KEY_COMMON_JAVASCRIPT_PATH = "common_javascript_path";
    public static final String KEY_SERVER_PLATFORM = "server_platform";
    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_AUTH = "auth";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_FORMLIST_URL = "formlist_url";
    public static final String KEY_SUBMISSION_URL = "submission_url";
    public static final String KEY_SHOW_SPLASH = "showSplash";
    public static final String KEY_SPLASH_PATH = "splashPath";

    private SurveyConfigurationColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "key" + " text, " + "value" + " text)";
    }
}
