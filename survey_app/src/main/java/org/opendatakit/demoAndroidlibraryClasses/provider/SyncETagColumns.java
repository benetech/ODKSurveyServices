package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum SyncETagColumns implements BaseColumns {
    TABLE_ID("_table_id"),
    IS_MANIFEST("_is_manifest"),
    URL("_url"),
    LAST_MODIFIED_TIMESTAMP("_last_modified"),
    ETAG_MD5_HASH("_etag_md5_hash");

    private final String text;

    SyncETagColumns() {
        this.text = null;
    }

    SyncETagColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_table_id" + " TEXT NULL, " + "_is_manifest" + " INTEGER, " + "_url" + " TEXT NOT NULL, " + "_last_modified" + " TEXT NOT NULL, " + "_etag_md5_hash" + " TEXT NOT NULL)";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(SyncETagColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
