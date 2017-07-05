package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;
import org.opendatakit.aggregate.odktables.rest.entity.RowFilterScope;

import java.util.ArrayList;
import java.util.List;

public enum DataTableColumns implements BaseColumns {
    ID("_id"),
    ROW_ETAG("_row_etag"),
    SYNC_STATE("_sync_state"),
    CONFLICT_TYPE("_conflict_type"),
    FILTER_TYPE("_filter_type"),
    FILTER_VALUE("_filter_value"),
    SAVEPOINT_TIMESTAMP("_savepoint_timestamp"),
    SAVEPOINT_TYPE("_savepoint_type"),
    SAVEPOINT_CREATOR("_savepoint_creator"),
    FORM_ID("_form_id"),
    LOCALE("_locale"),
    FIRSTNAME("beneficiary_firstname_autodefault"),
    LASTNAME("beneficiary_lastname_autodefault"),
    REPORTER_ID("reporter_id_autodefault"),
    REPORTER_NAME("reporter_name_autodefault"),
    EFFECTIVE_ACCESS("_effective_access"),
    DEFAULT_ROW_ETAG(),
    DEFAULT_FILTER_TYPE(RowFilterScope.EMPTY_ROW_FILTER.getType().name()),
    DEFAULT_FILTER_VALUE(RowFilterScope.EMPTY_ROW_FILTER.getValue());

    private final String text;

    DataTableColumns() {
        this.text = null;
    }

    DataTableColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(DataTableColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
