package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;

public class ArbitraryQuery extends ResumableQuery {
    private final String mSqlCommand;

    public ArbitraryQuery(String tableId, BindArgs bindArgs, String sqlCommand, Integer limit, Integer offset) {
        super(tableId, bindArgs, limit, offset);
        if(sqlCommand == null) {
            throw new IllegalArgumentException("SQL command must not be null");
        } else {
            this.mSqlCommand = sqlCommand;
        }
    }

    public ArbitraryQuery(String tableId, BindArgs bindArgs, String sqlCommand, QueryBounds bounds) {
        this(tableId, bindArgs, sqlCommand, Integer.valueOf(bounds != null?bounds.mLimit:-1), Integer.valueOf(bounds != null?bounds.mOffset:-1));
    }

    public String getSqlCommand() {
        return this.mSqlCommand;
    }
}
