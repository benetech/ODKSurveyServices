package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.QueryUtil;

public class SimpleQuery extends ResumableQuery {
    private final String mWhereClause;
    private final String[] mGroupByArgs;
    private final String mHavingClause;
    private final String[] mOrderByColNames;
    private final String[] mOrderByDirections;

    public SimpleQuery(String tableId, BindArgs bindArgs, String whereClause, String[] groupByArgs, String havingClause, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) {
        super(tableId, bindArgs, limit, offset);
        if(tableId == null) {
            throw new IllegalArgumentException("Table ID must not be null");
        } else {
            this.mWhereClause = whereClause;
            this.mGroupByArgs = groupByArgs;
            this.mHavingClause = havingClause;
            this.mOrderByColNames = orderByColNames;
            this.mOrderByDirections = orderByDirections;
        }
    }

    public SimpleQuery(String tableId, BindArgs bindArgs, String whereClause, String[] groupByArgs, String havingClause, String[] orderByColNames, String[] orderByDirections, QueryBounds bounds) {
        this(tableId, bindArgs, whereClause, groupByArgs, havingClause, orderByColNames, orderByDirections, Integer.valueOf(bounds != null?bounds.mLimit:-1), Integer.valueOf(bounds != null?bounds.mOffset:-1));
    }

    public String getSqlCommand() {
        return QueryUtil.buildSqlStatement(this.mTableId, this.mWhereClause, this.mGroupByArgs, this.mHavingClause, this.mOrderByColNames, this.mOrderByDirections);
    }

    public String getWhereClause() {
        return this.mWhereClause;
    }

    public String[] getGroupByArgs() {
        return this.mGroupByArgs != null?(String[])this.mGroupByArgs.clone():null;
    }

    public String getHavingClause() {
        return this.mHavingClause;
    }

    public String[] getOrderByColNames() {
        return this.mOrderByColNames != null?(String[])this.mOrderByColNames.clone():null;
    }

    public String[] getOrderByDirections() {
        return this.mOrderByDirections != null?(String[])this.mOrderByDirections.clone():null;
    }
}
