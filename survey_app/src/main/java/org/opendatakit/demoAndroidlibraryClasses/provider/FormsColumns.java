package org.opendatakit.demoAndroidlibraryClasses.provider;


import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum FormsColumns implements BaseColumns {
    CONTENT_TYPE("vnd.android.cursor.dir/vnd.opendatakit.form"),
    CONTENT_ITEM_TYPE("vnd.android.cursor.item/vnd.opendatakit.form"),
    COMMON_BASE_FORM_ID("framework"),
    TABLE_ID("tableId"),
    FORM_ID("formId"),
    SETTINGS("settings"),
    FORM_VERSION("formVersion"),
    DISPLAY_NAME("displayName"),
    DEFAULT_FORM_LOCALE("defaultFormLocale"),
    INSTANCE_NAME("instanceName"),
    JSON_MD5_HASH("jsonMd5Hash"),
    DATE("date"),
    FILE_LENGTH("fileLength");

    private final String text;

    FormsColumns() {
        this.text = null;
    }

    FormsColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer not null primary key, " + "tableId" + " text not null, " + "formId" + " text not null, " + "settings" + " text not null, " + "formVersion" + " text, " + "displayName" + " text not null, " + "defaultFormLocale" + " text null, " + "instanceName" + " text null, " + "jsonMd5Hash" + " text not null, " + "fileLength" + " integer not null, " + "date" + " integer not null " + ")";
    }

    public static String extractAppNameFromFormsUri(Uri uri) {
        List segments = uri.getPathSegments();
        if(segments.size() < 1) {
            throw new IllegalArgumentException("Unknown URI (incorrect number of segments!) " + uri);
        } else {
            String appName = (String)segments.get(0);
            return appName;
        }
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(FormsColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
