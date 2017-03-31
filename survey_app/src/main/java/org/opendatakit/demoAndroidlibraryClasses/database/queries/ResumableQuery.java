package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.Query;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;

public abstract class ResumableQuery implements Query {
    protected final String mTableId;
    protected final BindArgs mBindArgs;
    protected int mLimit;
    protected int mOffset;

    public ResumableQuery(String tableId, BindArgs bindArgs, Integer limit, Integer offset) {
        this.mTableId = tableId;
        this.mBindArgs = bindArgs;
        this.mLimit = limit != null?limit.intValue():-1;
        this.mOffset = offset != null?offset.intValue():-1;
    }

    public ResumableQuery(String tableId, BindArgs bindArgs, QueryBounds bounds) {
        this(tableId, bindArgs, bounds != null?Integer.valueOf(bounds.mLimit):null, bounds != null?Integer.valueOf(bounds.mOffset):null);
    }

    public String getTableId() {
        return this.mTableId;
    }

    public BindArgs getSqlBindArgs() {
        return this.mBindArgs;
    }

    public QueryBounds getSqlQueryBounds() {
        return this.mLimit < 0 && this.mOffset < 0?null:new QueryBounds(this.mLimit, this.getSqlOffset());
    }

    public void setSqlLimit(int limit) {
        this.mLimit = limit;
    }

    public int getSqlLimit() {
        return this.mLimit;
    }

    public void setSqlOffset(int offset) {
        this.mOffset = offset;
    }

    public int getSqlOffset() {
        return this.mOffset;
    }
}
