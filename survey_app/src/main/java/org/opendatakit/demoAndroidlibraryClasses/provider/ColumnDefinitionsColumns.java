package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public class ColumnDefinitionsColumns implements BaseColumns {
    public static final String TABLE_ID = "_table_id";
    public static final String ELEMENT_KEY = "_element_key";
    public static final String ELEMENT_NAME = "_element_name";
    public static final String ELEMENT_TYPE = "_element_type";
    public static final String LIST_CHILD_ELEMENT_KEYS = "_list_child_element_keys";

    private ColumnDefinitionsColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + "(" + "_table_id" + " TEXT NOT NULL, " + "_element_key" + " TEXT NOT NULL, " + "_element_name" + " TEXT NOT NULL, " + "_element_type" + " TEXT NOT NULL, " + "_list_child_element_keys" + " TEXT NULL, " + "PRIMARY KEY ( " + "_table_id" + ", " + "_element_key" + ") )";
    }
}
