package org.opendatakit.demoAndroidCommonClasses.views;

import android.os.Bundle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.opendatakit.demoAndroidCommonClasses.activities.IOdkDataActivity;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.services.MainActivity;
import org.opendatakit.survey.activities.MainMenuActivity;

public class OdkData {
    public static final String descOrder = "DESC";
    private WeakReference<ODKWebView> mWebView;
    private IOdkDataActivity mActivity;
    private static final String TAG = OdkData.class.getSimpleName();
    private ExecutorContext context;

    public OdkData(IOdkDataActivity activity, ODKWebView webView) {
        this.mActivity = activity;
        this.mWebView = new WeakReference(webView);
        this.context = ExecutorContext.getContext(this.mActivity);
    }

    public boolean isInactive() {
        return this.mWebView.get() == null || ((ODKWebView)this.mWebView.get()).isInactive();
    }

    public synchronized void refreshContext() {
        if(!this.context.isAlive()) {
            this.context = ExecutorContext.getContext(this.mActivity);
        }

    }

    public synchronized void shutdownContext() {
        this.context.shutdownWorker();
    }

    private void logDebug(String loggingString) {
        WebLogger.getLogger(this.mActivity.getAppName()).d("odkData", loggingString);
    }

    private void queueRequest(ExecutorRequest request) {
        this.context.queueRequest(request);
    }

    public OdkDataIf getJavascriptInterfaceWithWeakReference() {
        return new OdkDataIf(this);
    }

    public String getResponseJSON() {
        return this.mActivity.getResponseJSON();
    }

    public void getViewData(String callbackJSON, Integer limit, Integer offset) {
        this.logDebug("getViewData");
        Bundle bundle = this.mActivity.getIntentExtras();
        String tableId = bundle.getString("tableId");
        if(tableId != null && !tableId.isEmpty()) {
            String rowId = bundle.getString("instanceId");
            String whereClause = bundle.getString("sqlWhereClause");
            String[] selArgs = bundle.getStringArray("sqlSelectionArgs");
            String[] groupBy = bundle.getStringArray("sqlGroupByArgs");
            String havingClause = bundle.getString("sqlHavingClause");
            String orderByElemKey = bundle.getString("sqlOrderByElementKey");
            String orderByDir = bundle.getString("sqlOrderByDirection");
            if(rowId != null && !rowId.isEmpty()) {
                this.query(tableId, "_id=?", new String[]{rowId}, (String[])null, (String)null, "_savepoint_timestamp", "DESC", limit, offset, true, callbackJSON);
            } else {
                this.query(tableId, whereClause, selArgs, groupBy, havingClause, orderByElemKey, orderByDir, limit, offset, true, callbackJSON);
            }

        } else {
            throw new IllegalArgumentException("Tables view launched without tableId specified");
        }
    }

    public void getRoles(String callbackJSON) {
        this.logDebug("getRoles");
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.GET_ROLES_LIST, callbackJSON);
        this.queueRequest(request);
    }

    public void getUsers(String callbackJSON) {
        this.logDebug("getUsers");
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.GET_USERS_LIST, callbackJSON);
        this.queueRequest(request);
    }

    public void getAllTableIds(String callbackJSON) {
        this.logDebug("getAllTableIds");
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.GET_ALL_TABLE_IDS, callbackJSON);
        this.queueRequest(request);
    }

    public void query(String tableId, String whereClause, Object[] sqlBindParams, String[] groupBy, String having, String orderByElementKey, String orderByDirection, Integer limit, Integer offset, boolean includeKeyValueStoreMap, String callbackJSON) {
        this.logDebug("query: " + tableId + " whereClause: " + whereClause);
        ExecutorRequest request = new ExecutorRequest(tableId, whereClause, sqlBindParams, groupBy, having, orderByElementKey, orderByDirection, limit, offset, includeKeyValueStoreMap, callbackJSON);
        this.queueRequest(request);
    }

    public void arbitraryQuery(String tableId, String sqlCommand, Object[] sqlBindParams, Integer limit, Integer offset, String callbackJSON) {
        this.logDebug("arbitraryQuery: " + tableId + " sqlCommand: " + sqlCommand);
        ExecutorRequest request = new ExecutorRequest(tableId, sqlCommand, sqlBindParams, limit, offset, callbackJSON);
        this.queueRequest(request);
    }

    public void getRows(String tableId, String rowId, String callbackJSON) {
        this.logDebug("getRows: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_GET_ROWS, tableId, (String)null, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void getMostRecentRow(String tableId, String rowId, String callbackJSON) {
        this.logDebug("getMostRecentRow: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_GET_MOST_RECENT_ROW, tableId, (String)null, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void updateRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.logDebug("updateRow: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_UPDATE_ROW, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void changeAccessFilterOfRow(String tableId, String filterType, String filterValue, String rowId, String callbackJSON) {
        this.logDebug("changeAccessFilter: " + tableId + " _id: " + rowId);
        HashMap valueMap = new HashMap();
        valueMap.put("_filter_type", filterType);
        valueMap.put("_filter_value", filterValue);
        String stringifiedJSON = null;

        try {
            stringifiedJSON = ODKFileUtils.mapper.writeValueAsString(valueMap);
        } catch (JsonProcessingException var9) {
            WebLogger.getLogger(this.mActivity.getAppName()).printStackTrace(var9);
            return;
        }

        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_CHANGE_ACCESS_FILTER_ROW, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void deleteRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        //TODO should delete row from form-subform table
        this.logDebug("deleteRow: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_DELETE_ROW, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void addRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.logDebug("addRow: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_ADD_ROW, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void addCheckpoint(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.logDebug("addCheckpoint: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_ADD_CHECKPOINT, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void saveCheckpointAsIncomplete(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.logDebug("saveCheckpointAsIncomplete: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_SAVE_CHECKPOINT_AS_INCOMPLETE, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void saveCheckpointAsComplete(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        this.logDebug("saveCheckpointAsComplete: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_SAVE_CHECKPOINT_AS_COMPLETE, tableId, stringifiedJSON, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void deleteAllCheckpoints(String tableId, String rowId, String callbackJSON) {
        this.logDebug("deleteAllCheckpoints: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_DELETE_ALL_CHECKPOINTS, tableId, (String)null, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public void deleteLastCheckpoint(String tableId, String rowId, String callbackJSON) {
        this.logDebug("deleteLastCheckpoint: " + tableId + " _id: " + rowId);
        ExecutorRequest request = new ExecutorRequest(ExecutorRequestType.USER_TABLE_DELETE_LAST_CHECKPOINT, tableId, (String)null, rowId, callbackJSON);
        this.queueRequest(request);
    }

    public String getSubmenuPage(){
        return ((MainMenuActivity)mActivity).getSubmenuPage();
    }

    public static class IntentKeys {
        public static final String ACTION_TABLE_ID = "actionTableId";
        public static final String CONFLICT_TABLES = "conflictTables";
        public static final String CHECKPOINT_TABLES = "checkpointTables";
        public static final String TABLE_ID = "tableId";
        public static final String APP_NAME = "appName";
        public static final String TABLE_DISPLAY_VIEW_TYPE = "tableDisplayViewType";
        public static final String FILE_NAME = "filename";
        public static final String ROW_ID = "rowId";
        public static final String GRAPH_NAME = "graphName";
        public static final String ELEMENT_KEY = "elementKey";
        public static final String COLOR_RULE_TYPE = "colorRuleType";
        public static final String TABLE_PREFERENCE_FRAGMENT_TYPE = "tablePreferenceFragmentType";
        public static final String SQL_WHERE = "sqlWhereClause";
        public static final String SQL_SELECTION_ARGS = "sqlSelectionArgs";
        public static final String SQL_GROUP_BY_ARGS = "sqlGroupByArgs";
        public static final String SQL_HAVING = "sqlHavingClause";
        public static final String SQL_ORDER_BY_ELEMENT_KEY = "sqlOrderByElementKey";
        public static final String SQL_ORDER_BY_DIRECTION = "sqlOrderByDirection";

        public IntentKeys() {
        }
    }
}
