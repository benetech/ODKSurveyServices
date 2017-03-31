package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public class KeyValueStoreColumns implements BaseColumns {
    public static final String TABLE_ID = "_table_id";
    public static final String PARTITION = "_partition";
    public static final String ASPECT = "_aspect";
    public static final String KEY = "_key";
    public static final String VALUE_TYPE = "_type";
    public static final String VALUE = "_value";

    public KeyValueStoreColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_table_id" + " TEXT NOT NULL, " + "_partition" + " TEXT NOT NULL, " + "_aspect" + " TEXT NOT NULL, " + "_key" + " TEXT NOT NULL, " + "_type" + " TEXT NOT NULL, " + "_value" + " TEXT NOT NULL, " + "PRIMARY KEY ( " + "_table_id" + ", " + "_partition" + ", " + "_aspect" + ", " + "_key" + ") )";
    }
}
