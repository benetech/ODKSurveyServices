package org.opendatakit.demoAndroidCommonClasses.views;

import android.webkit.JavascriptInterface;
import java.lang.ref.WeakReference;

public class OdkDataIf {
    public static final String TAG = "OdkDataIf";
    private WeakReference<OdkData> weakData;

    OdkDataIf(OdkData odkData) {
        this.weakData = new WeakReference(odkData);
    }

    private boolean isInactive() {
        return this.weakData.get() == null || ((OdkData)this.weakData.get()).isInactive();
    }

    @JavascriptInterface
    public void getViewData(String callbackJSON, String limit, String offset) {
        if(!this.isInactive()) {
            Integer integerLimit = limit != null?Integer.valueOf(limit):null;
            Integer integerOffset = offset != null?Integer.valueOf(offset):null;
            ((OdkData)this.weakData.get()).getViewData(callbackJSON, integerLimit, integerOffset);
        }
    }

    @JavascriptInterface
    public String getResponseJSON() {
        return this.isInactive()?null:((OdkData)this.weakData.get()).getResponseJSON();
    }

    @JavascriptInterface
    public void getRoles(String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).getRoles(callbackJSON);
        }
    }

    @JavascriptInterface
    public void getUsers(String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).getUsers(callbackJSON);
        }
    }

    @JavascriptInterface
    public void getAllTableIds(String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).getAllTableIds(callbackJSON);
        }
    }

    @JavascriptInterface
    public void query(String tableId, String whereClause, String[] sqlBindParams, String[] groupBy, String having, String orderByElementKey, String orderByDirection, String limit, String offset, boolean includeKeyValueStoreMap, String callbackJSON) {
        if(!this.isInactive()) {
            Integer integerLimit = limit != null?Integer.valueOf(limit):null;
            Integer integerOffset = offset != null?Integer.valueOf(offset):null;
            ((OdkData)this.weakData.get()).query(tableId, whereClause, sqlBindParams, groupBy, having, orderByElementKey, orderByDirection, integerLimit, integerOffset, includeKeyValueStoreMap, callbackJSON);
        }
    }

    @JavascriptInterface
    public void arbitraryQuery(String tableId, String sqlCommand, String[] sqlBindParams, Integer limit, Integer offset, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).arbitraryQuery(tableId, sqlCommand, sqlBindParams, limit, offset, callbackJSON);
        }
    }

    @JavascriptInterface
    public void getRows(String tableId, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).getRows(tableId, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void getMostRecentRow(String tableId, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).getMostRecentRow(tableId, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void updateRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).updateRow(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void changeAccessFilterOfRow(String tableId, String filterType, String filterValue, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).changeAccessFilterOfRow(tableId, filterType, filterValue, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void deleteRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).deleteRow(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void addRow(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).addRow(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void addCheckpoint(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).addCheckpoint(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void saveCheckpointAsIncomplete(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).saveCheckpointAsIncomplete(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void saveCheckpointAsComplete(String tableId, String stringifiedJSON, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).saveCheckpointAsComplete(tableId, stringifiedJSON, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void deleteAllCheckpoints(String tableId, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).deleteAllCheckpoints(tableId, rowId, callbackJSON);
        }
    }

    @JavascriptInterface
    public void deleteLastCheckpoint(String tableId, String rowId, String callbackJSON) {
        if(!this.isInactive()) {
            ((OdkData)this.weakData.get()).deleteLastCheckpoint(tableId, rowId, callbackJSON);
        }
    }
}
