package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum TableDefinitionsColumns implements BaseColumns {
    CONTENT_TYPE("vnd.android.cursor.dir/vnd.opendatakit.table"),
    CONTENT_ITEM_TYPE("vnd.android.cursor.item/vnd.opendatakit.table"),
    TABLE_ID("_table_id"),
    SCHEMA_ETAG("_schema_etag"),
    LAST_DATA_ETAG("_last_data_etag"),
    LAST_SYNC_TIME("_last_sync_time"),
    REV_ID("_rev_id");

    private final String text;

    TableDefinitionsColumns() {
        this.text = null;
    }

    TableDefinitionsColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        String create = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + "_table_id" + " TEXT NOT NULL PRIMARY KEY, " + "_rev_id" + " TEXT NOT NULL," + "_schema_etag" + " TEXT NULL," + "_last_data_etag" + " TEXT NULL," + "_last_sync_time" + " TEXT NOT NULL )";
        return create;
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(TableDefinitionsColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
