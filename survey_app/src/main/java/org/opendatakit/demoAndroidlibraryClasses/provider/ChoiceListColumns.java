package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public final class ChoiceListColumns implements BaseColumns {
    public static final String CHOICE_LIST_ID = "_choice_list_id";
    public static final String CHOICE_LIST_JSON = "_choice_list_json";

    private ChoiceListColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_choice_list_id" + " TEXT NOT NULL, " + "_choice_list_json" + " TEXT NOT NULL)";
    }
}
