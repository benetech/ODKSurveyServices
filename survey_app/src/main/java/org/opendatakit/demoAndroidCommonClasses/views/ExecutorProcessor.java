package org.opendatakit.demoAndroidCommonClasses.views;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidCommonClasses.data.utilities.ColumnUtil;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.data.TableDefinitionEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.UserTable;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.exception.ActionNotAuthorizedException;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.DataHelper;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public abstract class ExecutorProcessor implements Runnable {
    private static final String TAG = "ExecutorProcessor";
    protected static final List<String> ADMIN_COLUMNS;
    private ExecutorContext context;
    private ExecutorRequest request;
    private UserDbInterface dbInterface;
    private String transId;
    private DbHandle dbHandle;

    protected ExecutorProcessor(ExecutorContext context) {
        this.context = context;
    }

    public void run() {
        this.request = this.context.peekRequest();
        if(this.request != null) {
            this.dbInterface = this.context.getDatabase();
            if(this.dbInterface != null) {
                try {
                    this.dbHandle = this.dbInterface.openDatabase(this.context.getAppName());
                    if(this.dbHandle == null) {
                        this.context.reportError(this.request.callbackJSON, (String)null, IllegalStateException.class.getName() + ": Unable to open database connection");
                        this.context.popRequest(true);
                        return;
                    }

                    this.transId = UUID.randomUUID().toString();
                    this.context.registerActiveConnection(this.transId, this.dbHandle);
                    switch(request.executorRequestType.ordinal()) {
                        case 1:
                            this.updateExecutorContext();
                            break;
                        case 2:
                            this.getRolesList();
                            break;
                        case 3:
                            this.getUsersList();
                            break;
                        case 4:
                            this.getAllTableIds();
                            break;
                        case 5:
                            this.arbitraryQuery();
                            break;
                        case 6:
                            this.userTableQuery();
                            break;
                        case 7:
                            this.getRows();
                            break;
                        case 8:
                            this.getMostRecentRow();
                            break;
                        case 9:
                            this.updateRow();
                            break;
                        case 10:
                            this.changeAccessFilterRow();
                            break;
                        case 11:
                            this.deleteRow();
                            break;
                        case 12:
                            this.addRow();
                            break;
                        case 13:
                            this.addCheckpoint();
                            break;
                        case 14:
                            this.saveCheckpointAsIncomplete();
                            break;
                        case 15:
                            this.saveCheckpointAsComplete();
                            break;
                        case 16:
                            this.deleteAllCheckpoints();
                            break;
                        case 17:
                            this.deleteLastCheckpoint();
                            break;
                        default:
                            this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": ExecutorProcessor has not implemented this request type!");
                    }
                } catch (ActionNotAuthorizedException var2) {
                    this.reportErrorAndCleanUp(ActionNotAuthorizedException.class.getName() + ": Not Authorized - " + var2.getMessage());
                } catch (ServicesAvailabilityException var3) {
                    this.reportErrorAndCleanUp(ServicesAvailabilityException.class.getName() + ": " + var3.getMessage());
                } catch (SQLiteException var4) {
                    this.reportErrorAndCleanUp(SQLiteException.class.getName() + ": " + var4.getMessage());
                } catch (Throwable var5) {
                    this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": ExecutorProcessor unexpected exception " + var5.toString());
                }

            }
        }
    }

    private void reportErrorAndCleanUp(String errorMessage) {
        try {
            this.dbInterface.closeDatabase(this.context.getAppName(), this.dbHandle);
        } catch (Exception var6) {
            WebLogger.getLogger(this.context.getAppName()).printStackTrace(var6);
            WebLogger.getLogger(this.context.getAppName()).w("ExecutorProcessor", "error while releasing database conneciton");
        } finally {
            this.context.removeActiveConnection(this.transId);
            this.context.reportError(this.request.callbackJSON, (String)null, errorMessage);
            this.context.popRequest(true);
        }

    }

    private void reportSuccessAndCleanUp(ArrayList<List<Object>> data, Map<String, Object> metadata) {
        boolean successful = false;
        String exceptionString = null;

        try {
            this.dbInterface.closeDatabase(this.context.getAppName(), this.dbHandle);
            successful = true;
        } catch (ServicesAvailabilityException var11) {
            exceptionString = var11.getClass().getName() + ": error while closing database: " + var11.toString();
            WebLogger.getLogger(this.context.getAppName()).printStackTrace(var11);
            WebLogger.getLogger(this.context.getAppName()).w("ExecutorProcessor", exceptionString);
        } catch (Throwable var12) {
            String msg = var12.getMessage();
            if(msg == null) {
                msg = var12.toString();
            }

            exceptionString = IllegalStateException.class.getName() + ": unexpected exception " + var12.getClass().getName() + " while closing database: " + msg;
            WebLogger.getLogger(this.context.getAppName()).printStackTrace(var12);
            WebLogger.getLogger(this.context.getAppName()).w("ExecutorProcessor", exceptionString);
        } finally {
            this.context.removeActiveConnection(this.transId);
            if(successful) {
                this.context.reportSuccess(this.request.callbackJSON, (String)null, data, metadata);
            } else {
                this.context.reportError(this.request.callbackJSON, (String)null, exceptionString);
            }

            this.context.popRequest(true);
        }

    }

    private ContentValues convertJSON(OrderedColumns columns, String stringifiedJSON) {
        ContentValues cvValues = new ContentValues();
        if(stringifiedJSON == null) {
            return cvValues;
        } else {
            try {
                HashMap e = (HashMap)ODKFileUtils.mapper.readValue(stringifiedJSON, HashMap.class);
                Iterator i$ = e.keySet().iterator();

                while(i$.hasNext()) {
                    Object okey = i$.next();
                    String key = (String)okey;
                    if(!key.equals("_form_id") && !key.equals("_locale") && !key.equals("_savepoint_creator") && !key.equals("_filter_type") && !key.equals("_filter_value")) {
                        ColumnDefinition value = columns.find(key);
                        if(!value.isUnitOfRetention()) {
                            throw new IllegalStateException("key is not a database column name: " + key);
                        }
                    }

                    Object value1 = e.get(key);
                    if(value1 == null) {
                        cvValues.putNull(key);
                    } else if(value1 instanceof Long) {
                        cvValues.put(key, (Long)value1);
                    } else if(value1 instanceof Integer) {
                        cvValues.put(key, (Integer)value1);
                    } else if(value1 instanceof Float) {
                        cvValues.put(key, (Float)value1);
                    } else if(value1 instanceof Double) {
                        cvValues.put(key, (Double)value1);
                    } else if(value1 instanceof String) {
                        cvValues.put(key, (String)value1);
                    } else {
                        if(!(value1 instanceof Boolean)) {
                            throw new IllegalStateException("unimplemented case");
                        }

                        cvValues.put(key, (Boolean)value1);
                    }
                }

                return cvValues;
            } catch (IOException var9) {
                WebLogger.getLogger(this.context.getAppName()).printStackTrace(var9);
                throw new IllegalStateException("should never be reached");
            }
        }
    }

    private void updateExecutorContext() {
        this.request.oldContext.releaseResources("switching to new WebFragment");
        this.context.popRequest(false);
    }

    private void getRolesList() throws ServicesAvailabilityException {
        String rolesList = this.dbInterface.getRolesList(this.context.getAppName());
        this.reportRolesListSuccessAndCleanUp(rolesList);
    }

    private void getUsersList() throws ServicesAvailabilityException {
        String usersList = this.dbInterface.getUsersList(this.context.getAppName());
        this.reportUsersListSuccessAndCleanUp(usersList);
    }

    private void getAllTableIds() throws ServicesAvailabilityException {
        List tableIds = this.dbInterface.getAllTableIds(this.context.getAppName(), this.dbHandle);
        if(tableIds == null) {
            this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to obtain list of all tableIds");
        } else {
            this.reportListOfTableIdsSuccessAndCleanUp(tableIds);
        }

    }

    private void arbitraryQuery() throws ServicesAvailabilityException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            BaseTable baseTable = this.dbInterface.arbitrarySqlQuery(this.context.getAppName(), this.dbHandle, this.request.tableId, this.request.sqlCommand, this.request.sqlBindParams, this.request.limit, this.request.offset);
            if(baseTable == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to rawQuery against: " + this.request.tableId + " sql: " + this.request.sqlCommand);
            } else {
                this.reportArbitraryQuerySuccessAndCleanUp(columns, baseTable);
            }

        }
    }

    private void populateKeyValueStoreList(Map<String, Object> metadata, List<KeyValueStoreEntry> entries) {
        if(entries != null) {
            ArrayList kvsArray = new ArrayList();
            Iterator i$ = entries.iterator();

            while(i$.hasNext()) {
                KeyValueStoreEntry entry = (KeyValueStoreEntry)i$.next();
                Object value = null;

                try {
                    ElementDataType anEntry = ElementDataType.valueOf(entry.type);
                    if(entry.value != null) {
                        if(anEntry == ElementDataType.integer) {
                            value = Long.valueOf(Long.parseLong(entry.value));
                        } else if(anEntry == ElementDataType.bool) {
                            value = entry.value;
                            if(value != null) {
                                try {
                                    value = Boolean.valueOf(DataHelper.intToBool(Integer.parseInt(entry.value)));
                                } catch (Exception var11) {
                                    WebLogger.getLogger(this.context.getAppName()).e("ExecutorProcessor", "ElementDataType: " + entry.type + " could not be converted from int");

                                    try {
                                        value = Boolean.valueOf(DataHelper.stringToBool(entry.value));
                                    } catch (Exception var10) {
                                        WebLogger.getLogger(this.context.getAppName()).e("ExecutorProcessor", "ElementDataType: " + entry.type + " could not be converted from string");
                                        var10.printStackTrace();
                                    }
                                }
                            }
                        } else if(anEntry == ElementDataType.number) {
                            value = Double.valueOf(Double.parseDouble(entry.value));
                        } else {
                            value = entry.value;
                        }
                    }
                } catch (IllegalArgumentException var12) {
                    value = entry.value;
                    WebLogger.getLogger(this.context.getAppName()).e("ExecutorProcessor", "Unrecognized ElementDataType: " + entry.type);
                    WebLogger.getLogger(this.context.getAppName()).printStackTrace(var12);
                }

                HashMap anEntry1 = new HashMap();
                anEntry1.put("partition", entry.partition);
                anEntry1.put("aspect", entry.aspect);
                anEntry1.put("key", entry.key);
                anEntry1.put("type", entry.type);
                anEntry1.put("value", value);
                kvsArray.add(anEntry1);
            }

            metadata.put("keyValueStoreList", kvsArray);
        }

    }

    private void reportArbitraryQuerySuccessAndCleanUp(OrderedColumns columnDefinitions, BaseTable userTable) throws ServicesAvailabilityException {
        ArrayList entries = null;
        entries = this.dbInterface.getTableMetadata(this.context.getAppName(), this.dbHandle, this.request.tableId, (String)null, (String)null, (String)null, userTable.getMetaDataRev()).getEntries();
        TableDefinitionEntry tdef = this.dbInterface.getTableDefinitionEntry(this.context.getAppName(), this.dbHandle, this.request.tableId);
        HashMap elementKeyToIndexMap = new HashMap();
        ArrayList data = new ArrayList();
        if(userTable != null) {
            Class[] q = new Class[userTable.getWidth()];

            int metadata;
            for(metadata = 0; metadata < userTable.getWidth(); ++metadata) {
                q[metadata] = String.class;
                String dataTableModel = userTable.getElementKey(metadata);
                elementKeyToIndexMap.put(dataTableModel, Integer.valueOf(metadata));
                if(dataTableModel.lastIndexOf(46) != -1) {
                    dataTableModel = dataTableModel.substring(dataTableModel.lastIndexOf(46) + 1);
                }

                if(dataTableModel.equals("_conflict_type")) {
                    q[metadata] = Integer.class;
                } else {
                    try {
                        ColumnDefinition r = columnDefinitions.find(dataTableModel);
                        ElementDataType values = r.getType().getDataType();
                        Class clazz = ColumnUtil.get().getOdkDataIfType(values);
                        q[metadata] = clazz;
                    } catch (Exception var13) {
                        ;
                    }
                }
            }

            for(int var16 = 0; var16 < userTable.getNumberOfRows(); ++var16) {
                Row var17 = userTable.getRowAtIndex(var16);
                Object[] var19 = new Object[userTable.getWidth()];

                for(metadata = 0; metadata < userTable.getWidth(); ++metadata) {
                    var19[metadata] = var17.getDataType(metadata, q[metadata]);
                }

                data.add(Arrays.asList(var19));
            }
        }

        HashMap var14 = new HashMap();
        ResumableQuery var15 = userTable.getQuery();
        if(var15 != null) {
            var14.put("limit", Integer.valueOf(var15.getSqlLimit()));
            var14.put("offset", Integer.valueOf(var15.getSqlOffset()));
        }

        var14.put("canCreateRow", Boolean.valueOf(userTable.getEffectiveAccessCreateRow()));
        var14.put("tableId", columnDefinitions.getTableId());
        var14.put("schemaETag", tdef.getSchemaETag());
        var14.put("lastDataETag", tdef.getLastDataETag());
        var14.put("lastSyncTime", tdef.getLastSyncTime());
        var14.put("elementKeyMap", elementKeyToIndexMap);
        TreeMap var18 = columnDefinitions.getExtendedDataModel();
        var14.put("dataTableModel", var18);
        this.populateKeyValueStoreList(var14, entries);
        this.reportSuccessAndCleanUp(data, var14);
    }

    private void reportRolesListSuccessAndCleanUp(String rolesList) throws ServicesAvailabilityException {
        ArrayList roles = null;
        if(rolesList != null) {
            TypeReference metadata = new TypeReference() {
            };

            try {
                roles = (ArrayList)ODKFileUtils.mapper.readValue(rolesList, metadata);
            } catch (IOException var5) {
                WebLogger.getLogger(this.context.getAppName()).printStackTrace(var5);
            }
        }

        HashMap metadata1 = new HashMap();
        metadata1.put("roles", roles);
        this.reportSuccessAndCleanUp((ArrayList)null, metadata1);
    }

    private void reportUsersListSuccessAndCleanUp(String usersList) throws ServicesAvailabilityException {
        ArrayList users = null;
        if(usersList != null) {
            TypeReference metadata = new TypeReference() {
            };

            try {
                users = (ArrayList)ODKFileUtils.mapper.readValue(usersList, metadata);
            } catch (IOException var5) {
                WebLogger.getLogger(this.context.getAppName()).printStackTrace(var5);
            }
        }

        HashMap metadata1 = new HashMap();
        metadata1.put("users", users);
        this.reportSuccessAndCleanUp((ArrayList)null, metadata1);
    }

    private void reportListOfTableIdsSuccessAndCleanUp(List<String> tableIds) throws ServicesAvailabilityException {
        HashMap metadata = new HashMap();
        metadata.put("tableIds", tableIds);
        this.reportSuccessAndCleanUp((ArrayList)null, metadata);
    }

    private void userTableQuery() throws ServicesAvailabilityException {
        String[] emptyArray = new String[0];
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.simpleQuery(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.whereClause, this.request.sqlBindParams, this.request.groupBy, this.request.having, this.request.orderByElementKey == null?emptyArray:new String[]{this.request.orderByElementKey}, this.request.orderByDirection == null?emptyArray:new String[]{this.request.orderByDirection}, this.request.limit, this.request.offset);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to query " + this.request.tableId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void reportSuccessAndCleanUp(UserTable userTable) throws ServicesAvailabilityException {
        ArrayList entries = null;
        entries = this.dbInterface.getTableMetadata(this.context.getAppName(), this.dbHandle, this.request.tableId, (String)null, (String)null, (String)null, userTable.getMetaDataRev()).getEntries();
        TableDefinitionEntry tdef = this.dbInterface.getTableDefinitionEntry(this.context.getAppName(), this.dbHandle, this.request.tableId);
        ArrayList data = new ArrayList();
        Map elementKeyToIndexMap = userTable.getElementKeyToIndex();
        Integer idxEffectiveAccessColumn = (Integer)elementKeyToIndexMap.get("_effective_access");
        OrderedColumns columnDefinitions = userTable.getColumnDefinitions();

        for(int metadata = 0; metadata < userTable.getNumberOfRows(); ++metadata) {
            Row q = userTable.getRowAtIndex(metadata);
            Object[] dataTableModel = new Object[elementKeyToIndexMap.size()];
            List typedValuesAsList = Arrays.asList(dataTableModel);
            data.add(typedValuesAsList);
            Iterator elementKeys = ADMIN_COLUMNS.iterator();

            while(elementKeys.hasNext()) {
                String i$ = (String)elementKeys.next();
                int name = ((Integer)elementKeyToIndexMap.get(i$)).intValue();
                if(i$.equals("_conflict_type")) {
                    Integer idx = (Integer)q.getDataType(i$, Integer.class);
                    dataTableModel[name] = idx;
                } else {
                    String var26 = (String)q.getDataType(i$, String.class);
                    dataTableModel[name] = var26;
                }
            }

            ArrayList var23 = columnDefinitions.getRetentionColumnNames();

            Object value;
            int var27;
            for(Iterator var24 = var23.iterator(); var24.hasNext(); dataTableModel[var27] = value) {
                String var25 = (String)var24.next();
                var27 = ((Integer)elementKeyToIndexMap.get(var25)).intValue();
                ColumnDefinition defn = columnDefinitions.find(var25);
                ElementDataType dataType = defn.getType().getDataType();
                Class clazz = ColumnUtil.get().getOdkDataIfType(dataType);
                value = q.getDataType(var25, clazz);
            }

            if(idxEffectiveAccessColumn != null) {
                dataTableModel[idxEffectiveAccessColumn.intValue()] = q.getDataType("_effective_access", String.class);
            }
        }

        HashMap var20 = new HashMap();
        ResumableQuery var21 = userTable.getQuery();
        if(var21 != null) {
            var20.put("limit", Integer.valueOf(var21.getSqlLimit()));
            var20.put("offset", Integer.valueOf(var21.getSqlOffset()));
        }

        var20.put("canCreateRow", Boolean.valueOf(userTable.getEffectiveAccessCreateRow()));
        var20.put("tableId", userTable.getTableId());
        var20.put("schemaETag", tdef.getSchemaETag());
        var20.put("lastDataETag", tdef.getLastDataETag());
        var20.put("lastSyncTime", tdef.getLastSyncTime());
        var20.put("elementKeyMap", elementKeyToIndexMap);
        TreeMap var22 = columnDefinitions.getExtendedDataModel();
        var20.put("dataTableModel", var22);
        this.populateKeyValueStoreList(var20, entries);
        this.extendQueryMetadata(this.dbHandle, entries, userTable, var20);
        this.reportSuccessAndCleanUp(data, var20);
    }

    protected abstract void extendQueryMetadata(DbHandle var1, List<KeyValueStoreEntry> var2, UserTable var3, Map<String, Object> var4);

    private void getRows() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.getRowsWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to getRows for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void getMostRecentRow() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.getMostRecentRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to getMostRecentRow for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void updateRow() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            ContentValues cvValues = this.convertJSON(columns, this.request.stringifiedJSON);
            UserTable t = this.dbInterface.updateRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, cvValues, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to updateRow for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void changeAccessFilterRow() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            ContentValues cvValues = this.convertJSON(columns, this.request.stringifiedJSON);
            String filterType = cvValues.getAsString("_filter_type");
            String filterValue = cvValues.getAsString("_filter_value");
            UserTable t = this.dbInterface.changeRowFilterWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, filterType, filterValue, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to changeAccessFilterRow for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void deleteRow() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.deleteRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to deleteRow for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void addRow() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            ContentValues cvValues = this.convertJSON(columns, this.request.stringifiedJSON);
            UserTable t = this.dbInterface.insertRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, cvValues, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to addRow for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void addCheckpoint() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            ContentValues cvValues = this.convertJSON(columns, this.request.stringifiedJSON);
            UserTable t = this.dbInterface.insertCheckpointRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, cvValues, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to addCheckpoint for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void saveCheckpointAsIncomplete() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.saveAsIncompleteMostRecentCheckpointRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to saveCheckpointAsIncomplete for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void saveCheckpointAsComplete() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.saveAsCompleteMostRecentCheckpointRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to saveCheckpointAsComplete for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void deleteLastCheckpoint() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.deleteLastCheckpointRowWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to deleteLastCheckpoint for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    private void deleteAllCheckpoints() throws ServicesAvailabilityException, ActionNotAuthorizedException {
        if(this.request.tableId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": tableId cannot be null");
        } else if(this.request.rowId == null) {
            this.reportErrorAndCleanUp(IllegalArgumentException.class.getName() + ": rowId cannot be null");
        } else {
            OrderedColumns columns = this.context.getOrderedColumns(this.request.tableId);
            if(columns == null) {
                columns = this.dbInterface.getUserDefinedColumns(this.context.getAppName(), this.dbHandle, this.request.tableId);
                this.context.putOrderedColumns(this.request.tableId, columns);
            }

            UserTable t = this.dbInterface.deleteAllCheckpointRowsWithId(this.context.getAppName(), this.dbHandle, this.request.tableId, columns, this.request.rowId);
            if(t == null) {
                this.reportErrorAndCleanUp(IllegalStateException.class.getName() + ": Unable to deleteAllCheckpoints for " + this.request.tableId + "._id = " + this.request.rowId);
            } else {
                this.reportSuccessAndCleanUp(t);
            }

        }
    }

    static {
        ArrayList adminColumns = new ArrayList();
        adminColumns.add("_id");
        adminColumns.add("_row_etag");
        adminColumns.add("_sync_state");
        adminColumns.add("_conflict_type");
        adminColumns.add("_filter_type");
        adminColumns.add("_filter_value");
        adminColumns.add("_form_id");
        adminColumns.add("_locale");
        adminColumns.add("_savepoint_type");
        adminColumns.add("_savepoint_timestamp");
        adminColumns.add("_savepoint_creator");
        Collections.sort(adminColumns);
        ADMIN_COLUMNS = Collections.unmodifiableList(adminColumns);
    }
}
