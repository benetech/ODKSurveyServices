package org.opendatakit.demoAndroidlibraryClasses.provider;


import android.net.Uri;
import android.provider.BaseColumns;
import java.util.List;

public final class FormsColumns implements BaseColumns {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.opendatakit.form";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.opendatakit.form";
    public static final String COMMON_BASE_FORM_ID = "framework";
    public static final String TABLE_ID = "tableId";
    public static final String FORM_ID = "formId";
    public static final String SETTINGS = "settings";
    public static final String FORM_VERSION = "formVersion";
    public static final String DISPLAY_NAME = "displayName";
    public static final String DEFAULT_FORM_LOCALE = "defaultFormLocale";
    public static final String INSTANCE_NAME = "instanceName";
    public static final String JSON_MD5_HASH = "jsonMd5Hash";
    public static final String DATE = "date";
    public static final String FILE_LENGTH = "fileLength";
    public static final String[] formsDataColumnNames = new String[]{"tableId", "formId", "settings", "formVersion", "displayName", "defaultFormLocale", "instanceName", "jsonMd5Hash", "fileLength", "date"};

    private FormsColumns() {
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
}
