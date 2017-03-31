package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;
import org.opendatakit.aggregate.odktables.rest.entity.RowFilterScope;

public class DataTableColumns implements BaseColumns {
    public static final String ID = "_id";
    public static final String ROW_ETAG = "_row_etag";
    public static final String SYNC_STATE = "_sync_state";
    public static final String CONFLICT_TYPE = "_conflict_type";
    public static final String FILTER_TYPE = "_filter_type";
    public static final String FILTER_VALUE = "_filter_value";
    public static final String SAVEPOINT_TIMESTAMP = "_savepoint_timestamp";
    public static final String SAVEPOINT_TYPE = "_savepoint_type";
    public static final String SAVEPOINT_CREATOR = "_savepoint_creator";
    public static final String FORM_ID = "_form_id";
    public static final String LOCALE = "_locale";
    public static final String EFFECTIVE_ACCESS = "_effective_access";
    public static final String DEFAULT_ROW_ETAG = null;
    public static final String DEFAULT_FILTER_TYPE;
    public static final String DEFAULT_FILTER_VALUE;

    private DataTableColumns() {
    }

    static {
        DEFAULT_FILTER_TYPE = RowFilterScope.EMPTY_ROW_FILTER.getType().name();
        DEFAULT_FILTER_VALUE = RowFilterScope.EMPTY_ROW_FILTER.getValue();
    }
}
