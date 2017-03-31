package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public final class SyncETagColumns implements BaseColumns {
    public static final String TABLE_ID = "_table_id";
    public static final String IS_MANIFEST = "_is_manifest";
    public static final String URL = "_url";
    public static final String LAST_MODIFIED_TIMESTAMP = "_last_modified";
    public static final String ETAG_MD5_HASH = "_etag_md5_hash";

    private SyncETagColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_table_id" + " TEXT NULL, " + "_is_manifest" + " INTEGER, " + "_url" + " TEXT NOT NULL, " + "_last_modified" + " TEXT NOT NULL, " + "_etag_md5_hash" + " TEXT NOT NULL)";
    }
}
