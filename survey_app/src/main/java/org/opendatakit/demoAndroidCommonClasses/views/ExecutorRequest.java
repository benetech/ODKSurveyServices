package org.opendatakit.demoAndroidCommonClasses.views;

public class ExecutorRequest {
    public final ExecutorRequestType executorRequestType;
    public final ExecutorContext oldContext;
    public final String sqlCommand;
    public final Object[] sqlBindParams;
    public final String tableId;
    public final String whereClause;
    public final String[] groupBy;
    public final String having;
    public final String orderByElementKey;
    public final String orderByDirection;
    public final Integer limit;
    public final Integer offset;
    public final boolean includeKeyValueStoreMap;
    public final String stringifiedJSON;
    public final String rowId;
    public final boolean deleteAllCheckpoints;
    public final boolean commitTransaction;
    public final String callbackJSON;

    public ExecutorRequest(ExecutorContext oldContext) {
        this.executorRequestType = ExecutorRequestType.UPDATE_EXECUTOR_CONTEXT;
        this.oldContext = oldContext;
        this.sqlCommand = null;
        this.sqlBindParams = null;
        this.callbackJSON = null;
        this.tableId = null;
        this.whereClause = null;
        this.groupBy = null;
        this.having = null;
        this.orderByElementKey = null;
        this.orderByDirection = null;
        this.limit = null;
        this.offset = null;
        this.includeKeyValueStoreMap = false;
        this.stringifiedJSON = null;
        this.rowId = null;
        this.deleteAllCheckpoints = false;
        this.commitTransaction = false;
    }

    public ExecutorRequest(String tableId, String sqlCommand, Object[] sqlBindParams, Integer limit, Integer offset, String callbackJSON) {
        this.executorRequestType = ExecutorRequestType.ARBITRARY_QUERY;
        this.tableId = tableId;
        this.sqlCommand = sqlCommand;
        this.sqlBindParams = sqlBindParams;
        this.limit = limit;
        this.offset = offset;
        this.callbackJSON = callbackJSON;
        this.oldContext = null;
        this.whereClause = null;
        this.groupBy = null;
        this.having = null;
        this.orderByElementKey = null;
        this.orderByDirection = null;
        this.includeKeyValueStoreMap = false;
        this.stringifiedJSON = null;
        this.rowId = null;
        this.deleteAllCheckpoints = false;
        this.commitTransaction = false;
    }

    public ExecutorRequest(String tableId, String whereClause, Object[] sqlBindParams, String[] groupBy, String having, String orderByElementKey, String orderByDirection, Integer limit, Integer offset, boolean includeKeyValueStoreMap, String callbackJSON) {
        this.executorRequestType = ExecutorRequestType.USER_TABLE_QUERY;
        this.tableId = tableId;
        this.whereClause = whereClause;
        this.sqlBindParams = sqlBindParams;
        this.groupBy = groupBy;
        this.having = having;
        this.orderByElementKey = orderByElementKey;
        this.orderByDirection = orderByDirection;
        this.limit = limit;
        this.offset = offset;
        this.includeKeyValueStoreMap = includeKeyValueStoreMap;
        this.callbackJSON = callbackJSON;
        this.oldContext = null;
        this.sqlCommand = null;
        this.stringifiedJSON = null;
        this.rowId = null;
        this.deleteAllCheckpoints = false;
        this.commitTransaction = false;
    }

    public ExecutorRequest(ExecutorRequestType executorRequestType, String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.executorRequestType = executorRequestType;
        this.tableId = tableId;
        this.stringifiedJSON = stringifiedJSON;
        this.rowId = rowId;
        this.callbackJSON = callbackJSON;
        this.oldContext = null;
        this.sqlCommand = null;
        this.whereClause = null;
        this.sqlBindParams = null;
        this.groupBy = null;
        this.having = null;
        this.orderByElementKey = null;
        this.orderByDirection = null;
        this.limit = null;
        this.offset = null;
        this.includeKeyValueStoreMap = false;
        this.deleteAllCheckpoints = false;
        this.commitTransaction = false;
    }

    public ExecutorRequest(ExecutorRequestType executorRequestType, String callbackJSON) {
        this.executorRequestType = executorRequestType;
        this.tableId = null;
        this.stringifiedJSON = null;
        this.rowId = null;
        this.callbackJSON = callbackJSON;
        this.oldContext = null;
        this.sqlCommand = null;
        this.whereClause = null;
        this.sqlBindParams = null;
        this.groupBy = null;
        this.having = null;
        this.orderByElementKey = null;
        this.orderByDirection = null;
        this.limit = null;
        this.offset = null;
        this.includeKeyValueStoreMap = false;
        this.deleteAllCheckpoints = false;
        this.commitTransaction = false;
    }
}
