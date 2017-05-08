package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum SurveyConfigurationColumns implements BaseColumns {
    CONTENT_TYPE("vnd.android.cursor.dir/vnd.opendatakit.survey"),
    CONTENT_ITEM_TYPE("vnd.android.cursor.item/vnd.opendatakit.survey"),
    KEY("key"),
    VALUE("value"),
    KEY_COMMON_JAVASCRIPT_PATH("common_javascript_path"),
    KEY_SERVER_PLATFORM("server_platform"),
    KEY_SERVER_URL("server_url"),
    KEY_USERNAME("username"),
    KEY_PASSWORD("password"),
    KEY_AUTH("auth"),
    KEY_ACCOUNT("account"),
    KEY_FORMLIST_URL("formlist_url"),
    KEY_SUBMISSION_URL("submission_url"),
    KEY_SHOW_SPLASH("showSplash"),
    KEY_SPLASH_PATH("splashPath");

    private final String text;

    SurveyConfigurationColumns() {
        this.text = null;
    }

    SurveyConfigurationColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "key" + " text, " + "value" + " text)";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(SurveyConfigurationColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
