package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum KeyValueStoreColumns implements BaseColumns {
    TABLE_ID("_table_id"),
    PARTITION("_partition"),
    ASPECT("_aspect"),
    KEY("_key"),
    VALUE_TYPE("_type"),
    VALUE("_value");

    private final String text;

    KeyValueStoreColumns() {
        this.text = null;
    }

    KeyValueStoreColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_table_id" + " TEXT NOT NULL, " + "_partition" + " TEXT NOT NULL, " + "_aspect" + " TEXT NOT NULL, " + "_key" + " TEXT NOT NULL, " + "_type" + " TEXT NOT NULL, " + "_value" + " TEXT NOT NULL, " + "PRIMARY KEY ( " + "_table_id" + ", " + "_partition" + ", " + "_aspect" + ", " + "_key" + ") )";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(KeyValueStoreColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
