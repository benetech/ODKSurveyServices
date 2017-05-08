package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum ColumnDefinitionsColumns implements BaseColumns {
    TABLE_ID("_table_id"),
    ELEMENT_KEY("_element_key"),
    ELEMENT_NAME("_element_name"),
    ELEMENT_TYPE("_element_type"),
    LIST_CHILD_ELEMENT_KEYS ("_list_child_element_keys");

    private final String text;

    ColumnDefinitionsColumns() {
        this.text = null;
    }

    ColumnDefinitionsColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + "(" + "_table_id" + " TEXT NOT NULL, " + "_element_key" + " TEXT NOT NULL, " + "_element_name" + " TEXT NOT NULL, " + "_element_type" + " TEXT NOT NULL, " + "_list_child_element_keys" + " TEXT NULL, " + "PRIMARY KEY ( " + "_table_id" + ", " + "_element_key" + ") )";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(ColumnDefinitionsColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
