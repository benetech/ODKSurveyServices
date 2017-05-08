package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum ChoiceListColumns implements BaseColumns {
    CHOICE_LIST_ID("_choice_list_id"),
    CHOICE_LIST_JSON("_choice_list_json");

    private final String text;

   ChoiceListColumns() {
        this.text = null;
    }

    ChoiceListColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_choice_list_id" + " TEXT NOT NULL, " + "_choice_list_json" + " TEXT NOT NULL)";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(ChoiceListColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
