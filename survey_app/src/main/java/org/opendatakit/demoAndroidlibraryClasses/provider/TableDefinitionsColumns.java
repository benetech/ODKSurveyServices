package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public class TableDefinitionsColumns implements BaseColumns {
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.opendatakit.table";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.opendatakit.table";
    public static final String TABLE_ID = "_table_id";
    public static final String SCHEMA_ETAG = "_schema_etag";
    public static final String LAST_DATA_ETAG = "_last_data_etag";
    public static final String LAST_SYNC_TIME = "_last_sync_time";
    public static final String REV_ID = "_rev_id";

    private TableDefinitionsColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        String create = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + "_table_id" + " TEXT NOT NULL PRIMARY KEY, " + "_rev_id" + " TEXT NOT NULL," + "_schema_etag" + " TEXT NULL," + "_last_data_etag" + " TEXT NULL," + "_last_sync_time" + " TEXT NOT NULL )";
        return create;
    }
}
