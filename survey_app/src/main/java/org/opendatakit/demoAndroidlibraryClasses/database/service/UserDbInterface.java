package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.content.ContentValues;
import android.database.sqlite.SQLiteException;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.opendatakit.aggregate.odktables.rest.entity.RowFilterScope.Type;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.TableDefinitionEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.TableMetaDataEntries;
import org.opendatakit.demoAndroidlibraryClasses.database.data.UserTable;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ArbitraryQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.SimpleQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.service.AidlDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbChunk;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthInfo;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.DbChunkUtil;
import org.opendatakit.demoAndroidlibraryClasses.exception.ActionNotAuthorizedException;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;

public class UserDbInterface {
    private static final String TAG = UserDbInterface.class.getSimpleName();
    private AidlDbInterface dbInterface;
    private Map<String, TableMetaDataEntries> metaDataCache;

    public UserDbInterface(AidlDbInterface dbInterface) throws IllegalArgumentException {
        if(dbInterface == null) {
            throw new IllegalArgumentException("Database Interface must not be null");
        } else {
            this.dbInterface = dbInterface;
            this.metaDataCache = new HashMap();
        }
    }

    public AidlDbInterface getDbInterface() {
        return this.dbInterface;
    }

    public void setDbInterface(AidlDbInterface dbInterface) {
        if(dbInterface == null) {
            throw new IllegalArgumentException("Database Interface must not be null");
        } else {
            this.dbInterface = dbInterface;
        }
    }

    private void rethrowNotAuthorizedRemoteException(Exception e) throws IllegalArgumentException, IllegalStateException, SQLiteException, ActionNotAuthorizedException, ServicesAvailabilityException {
        if(!(e instanceof IllegalStateException) && !(e instanceof RemoteException)) {
            throw new IllegalStateException("not IllegalStateException or RemoteException on AidlDbInterface: " + e.getClass().getName() + ": " + e.toString());
        } else {
            String prefix = "via RemoteException on AidlDbInterface: ";
            String msg = e.getMessage();
            int idx = msg.indexOf(58);
            if(idx == -1) {
                throw new ServicesAvailabilityException(prefix + msg);
            } else {
                String exceptionName = msg.substring(0, idx);
                String message = msg.substring(idx + 2);
                if(!exceptionName.startsWith("org.opendatakit|")) {
                    throw new ServicesAvailabilityException(prefix + msg);
                } else {
                    exceptionName = exceptionName.substring(exceptionName.indexOf(124) + 1);
                    if(exceptionName.equals(ActionNotAuthorizedException.class.getName())) {
                        throw new ActionNotAuthorizedException(prefix + message);
                    } else if(exceptionName.equals(IllegalArgumentException.class.getName())) {
                        throw new IllegalArgumentException(prefix + message);
                    } else if(exceptionName.equals(SQLiteException.class.getName())) {
                        throw new SQLiteException(prefix + message);
                    } else {
                        throw new IllegalStateException(prefix + msg);
                    }
                }
            }
        }
    }

    private void rethrowAlwaysAllowedRemoteException(Exception e) throws IllegalArgumentException, IllegalStateException, SQLiteException, ServicesAvailabilityException {
        if(!(e instanceof IllegalStateException) && !(e instanceof RemoteException)) {
            throw new IllegalStateException("not IllegalStateException or RemoteException on AidlDbInterface: " + e.getClass().getName() + ": " + e.toString());
        } else {
            String prefix = "via RemoteException on AidlDbInterface: ";
            String msg = e.getMessage();
            if(msg == null) {
                throw new IllegalStateException(prefix + e.toString());
            } else {
                int idx = msg.indexOf(58);
                if(idx == -1) {
                    throw new ServicesAvailabilityException(prefix + msg);
                } else {
                    String exceptionName = msg.substring(0, idx);
                    String message = msg.substring(idx + 2);
                    if(!exceptionName.startsWith("org.opendatakit|")) {
                        throw new ServicesAvailabilityException(prefix + msg);
                    } else {
                        exceptionName = exceptionName.substring(exceptionName.indexOf(124) + 1);
                        if(exceptionName.equals(IllegalArgumentException.class.getName())) {
                            throw new IllegalArgumentException(prefix + message);
                        } else if(exceptionName.equals(SQLiteException.class.getName())) {
                            throw new SQLiteException(prefix + message);
                        } else {
                            throw new IllegalStateException(prefix + msg);
                        }
                    }
                }
            }
        }
    }

    public String getRolesList(String appName) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getRolesList(appName);
        } catch (Exception var3) {
            this.rethrowAlwaysAllowedRemoteException(var3);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String getUsersList(String appName) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getUsersList(appName);
        } catch (Exception var3) {
            this.rethrowAlwaysAllowedRemoteException(var3);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public DbHandle openDatabase(String appName) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.openDatabase(appName);
        } catch (Exception var3) {
            this.rethrowAlwaysAllowedRemoteException(var3);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void closeDatabase(String appName, DbHandle dbHandleName) throws ServicesAvailabilityException {
        try {
            this.dbInterface.closeDatabase(appName, dbHandleName);
        } catch (Exception var4) {
            this.rethrowAlwaysAllowedRemoteException(var4);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public OrderedColumns createLocalOnlyTableWithColumns(String appName, DbHandle dbHandleName, String tableId, ColumnList columns) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        try {
            return (OrderedColumns)this.fetchAndRebuildChunks(this.dbInterface.createLocalOnlyTableWithColumns(appName, dbHandleName, tableId, columns), OrderedColumns.CREATOR);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteLocalOnlyTable(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        try {
            this.dbInterface.deleteLocalOnlyTable(appName, dbHandleName, tableId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void insertLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, ContentValues rowValues) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        try {
            this.dbInterface.insertLocalOnlyRow(appName, dbHandleName, tableId, rowValues);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void updateLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, ContentValues rowValues, String whereClause, Object[] bindArgs) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        try {
            this.dbInterface.updateLocalOnlyRow(appName, dbHandleName, tableId, rowValues, whereClause, new BindArgs(bindArgs));
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, String whereClause, Object[] bindArgs) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        try {
            this.dbInterface.deleteLocalOnlyRow(appName, dbHandleName, tableId, whereClause, new BindArgs(bindArgs));
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable simpleQueryLocalOnlyTables(String appName, DbHandle dbHandleName, String tableId, String whereClause, Object[] bindArgs, String[] groupBy, String having, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        return this.simpleQuery(appName, dbHandleName, tableId, whereClause, bindArgs, groupBy, having, orderByColNames, orderByDirections, limit, offset);
    }

    public BaseTable arbitrarySqlQueryLocalOnlyTables(String appName, DbHandle dbHandleName, String tableId, String sqlCommand, Object[] bindArgs, Integer limit, Integer offset) throws ServicesAvailabilityException {
        if(!tableId.startsWith("L_")) {
            tableId = "L_" + tableId;
        }

        return this.arbitrarySqlQuery(appName, dbHandleName, tableId, sqlCommand, bindArgs, limit, offset);
    }

    public BaseTable resumeSimpleQueryLocalOnlyTables(String appName, DbHandle dbHandleName, ResumableQuery query) throws ServicesAvailabilityException {
        return this.resumeSimpleQuery(appName, dbHandleName, query);
    }

    public void privilegedServerTableSchemaETagChanged(String appName, DbHandle dbHandleName, String tableId, String schemaETag, String tableInstanceFilesUri) throws ServicesAvailabilityException {
        try {
            this.dbInterface.privilegedServerTableSchemaETagChanged(appName, dbHandleName, tableId, schemaETag, tableInstanceFilesUri);
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String setChoiceList(String appName, DbHandle dbHandleName, String choiceListJSON) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.setChoiceList(appName, dbHandleName, choiceListJSON);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String getChoiceList(String appName, DbHandle dbHandleName, String choiceListId) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getChoiceList(appName, dbHandleName, choiceListId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public OrderedColumns createOrOpenTableWithColumns(String appName, DbHandle dbHandleName, String tableId, ColumnList columns) throws ServicesAvailabilityException {
        try {
            return (OrderedColumns)this.fetchAndRebuildChunks(this.dbInterface.createOrOpenTableWithColumns(appName, dbHandleName, tableId, columns), OrderedColumns.CREATOR);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public OrderedColumns createOrOpenTableWithColumnsAndProperties(String appName, DbHandle dbHandleName, String tableId, ColumnList columns, List<KeyValueStoreEntry> metaData, boolean clear) throws ServicesAvailabilityException {
        try {
            return (OrderedColumns)this.fetchAndRebuildChunks(this.dbInterface.createOrOpenTableWithColumnsAndProperties(appName, dbHandleName, tableId, columns, metaData, clear), OrderedColumns.CREATOR);
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteTableAndAllData(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteTableAndAllData(appName, dbHandleName, tableId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteTableMetadata(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, String key) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteTableMetadata(appName, dbHandleName, tableId, partition, aspect, key);
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String[] getAdminColumns() throws ServicesAvailabilityException {
        try {
            return (String[])this.fetchAndRebuildChunks(this.dbInterface.getAdminColumns(), String[].class);
        } catch (Exception var2) {
            this.rethrowAlwaysAllowedRemoteException(var2);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String[] getAllColumnNames(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            return (String[])this.fetchAndRebuildChunks(this.dbInterface.getAllColumnNames(appName, dbHandleName, tableId), String[].class);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public List<String> getAllTableIds(String appName, DbHandle dbHandleName) throws ServicesAvailabilityException {
        try {
            Serializable e = (Serializable)this.fetchAndRebuildChunks(this.dbInterface.getAllTableIds(appName, dbHandleName), Serializable.class);
            return (List)e;
        } catch (Exception var4) {
            this.rethrowAlwaysAllowedRemoteException(var4);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public TableMetaDataEntries getTableMetadata(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, String key, String revId) throws ServicesAvailabilityException {
        TableMetaDataEntries entries = null;

        try {
            if(tableId == null) {
                entries = (TableMetaDataEntries)this.fetchAndRebuildChunks(this.dbInterface.getTableMetadata(appName, dbHandleName, tableId, partition, aspect, key), TableMetaDataEntries.CREATOR);
            } else {
                TableMetaDataEntries e = (TableMetaDataEntries)this.metaDataCache.get(tableId);
                if(e == null) {
                    e = (TableMetaDataEntries)this.fetchAndRebuildChunks(this.dbInterface.getTableMetadata(appName, dbHandleName, tableId, (String)null, (String)null, (String)null), TableMetaDataEntries.CREATOR);
                    this.metaDataCache.put(tableId, e);
                } else {
                    TableMetaDataEntries newEntries = (TableMetaDataEntries)this.fetchAndRebuildChunks(this.dbInterface.getTableMetadataIfChanged(appName, dbHandleName, tableId, e.getRevId()), TableMetaDataEntries.CREATOR);
                    if(!newEntries.getRevId().equals(e.getRevId())) {
                        this.metaDataCache.put(tableId, newEntries);
                        e = newEntries;
                    }
                }

                entries = this.filterEntries(e, partition, aspect, key);
            }
        } catch (Exception var11) {
            this.rethrowAlwaysAllowedRemoteException(var11);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }

        if(entries == null) {
            entries = new TableMetaDataEntries(tableId, (String)null);
        }

        return entries;
    }

    private TableMetaDataEntries filterEntries(TableMetaDataEntries allEntries, String partition, String aspect, String key) {
        if(partition == null && aspect == null && key == null) {
            return new TableMetaDataEntries(allEntries);
        } else {
            TableMetaDataEntries entries = new TableMetaDataEntries(allEntries.getTableId(), allEntries.getRevId());
            Iterator i$ = allEntries.getEntries().iterator();

            while(true) {
                KeyValueStoreEntry entry;
                do {
                    do {
                        do {
                            if(!i$.hasNext()) {
                                return entries;
                            }

                            entry = (KeyValueStoreEntry)i$.next();
                        } while(partition != null && !entry.partition.equals(partition));
                    } while(aspect != null && !entry.aspect.equals(aspect));
                } while(key != null && !entry.key.equals(key));

                entries.addEntry(entry);
            }
        }
    }

    public String[] getExportColumns() throws ServicesAvailabilityException {
        try {
            return (String[])this.fetchAndRebuildChunks(this.dbInterface.getExportColumns(), String[].class);
        } catch (Exception var2) {
            this.rethrowAlwaysAllowedRemoteException(var2);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public TableDefinitionEntry getTableDefinitionEntry(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            return (TableDefinitionEntry)this.fetchAndRebuildChunks(this.dbInterface.getTableDefinitionEntry(appName, dbHandleName, tableId), TableDefinitionEntry.CREATOR);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public List<TableHealthInfo> getTableHealthStatuses(String appName, DbHandle dbHandleName) throws ServicesAvailabilityException {
        try {
            Serializable e = (Serializable)this.fetchAndRebuildChunks(this.dbInterface.getTableHealthStatuses(appName, dbHandleName), Serializable.class);
            return (List)e;
        } catch (Exception var4) {
            this.rethrowAlwaysAllowedRemoteException(var4);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public OrderedColumns getUserDefinedColumns(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            return (OrderedColumns)this.fetchAndRebuildChunks(this.dbInterface.getUserDefinedColumns(appName, dbHandleName, tableId), OrderedColumns.CREATOR);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public boolean hasTableId(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.hasTableId(appName, dbHandleName, tableId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable simpleQuery(String appName, DbHandle dbHandleName, String tableId, String whereClause, Object[] bindArgs, String[] groupBy, String having, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            SimpleQuery e = new SimpleQuery(tableId, new BindArgs(bindArgs), whereClause, groupBy, having, orderByColNames, orderByDirections, limit, offset);
            BaseTable baseTable = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.simpleQuery(appName, dbHandleName, e.getSqlCommand(), e.getSqlBindArgs(), e.getSqlQueryBounds(), e.getTableId()), BaseTable.CREATOR);
            baseTable.setQuery(e);
            return baseTable;
        } catch (Exception var14) {
            this.rethrowAlwaysAllowedRemoteException(var14);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable privilegedSimpleQuery(String appName, DbHandle dbHandleName, String tableId, String whereClause, Object[] bindArgs, String[] groupBy, String having, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            SimpleQuery e = new SimpleQuery(tableId, new BindArgs(bindArgs), whereClause, groupBy, having, orderByColNames, orderByDirections, limit, offset);
            BaseTable baseTable = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedSimpleQuery(appName, dbHandleName, e.getSqlCommand(), e.getSqlBindArgs(), e.getSqlQueryBounds(), e.getTableId()), BaseTable.CREATOR);
            baseTable.setQuery(e);
            return baseTable;
        } catch (Exception var14) {
            this.rethrowAlwaysAllowedRemoteException(var14);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable arbitrarySqlQuery(String appName, DbHandle dbHandleName, String tableId, String sqlCommand, Object[] bindArgs, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            ArbitraryQuery e = new ArbitraryQuery(tableId, new BindArgs(bindArgs), sqlCommand, limit, offset);
            BaseTable baseTable = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.simpleQuery(appName, dbHandleName, e.getSqlCommand(), e.getSqlBindArgs(), e.getSqlQueryBounds(), e.getTableId()), BaseTable.CREATOR);
            baseTable.setQuery(e);
            return baseTable;
        } catch (Exception var10) {
            this.rethrowAlwaysAllowedRemoteException(var10);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable resumeSimpleQuery(String appName, DbHandle dbHandleName, ResumableQuery query) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.simpleQuery(appName, dbHandleName, query.getSqlCommand(), query.getSqlBindArgs(), query.getSqlQueryBounds(), query.getTableId()), BaseTable.CREATOR);
            e.setQuery(query);
            return e;
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public BaseTable resumePrivilegedSimpleQuery(String appName, DbHandle dbHandleName, ResumableQuery query) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedSimpleQuery(appName, dbHandleName, query.getSqlCommand(), query.getSqlBindArgs(), query.getSqlQueryBounds(), query.getTableId()), BaseTable.CREATOR);
            e.setQuery(query);
            return e;
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void privilegedExecute(String appName, DbHandle dbHandleName, String sqlCommand, Object[] bindArgs) throws ServicesAvailabilityException {
        try {
            this.dbInterface.privilegedExecute(appName, dbHandleName, sqlCommand, new BindArgs(bindArgs));
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable simpleQuery(String appName, DbHandle dbHandleName, String tableId, OrderedColumns columnDefns, String whereClause, Object[] bindArgs, String[] groupBy, String having, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            BaseTable e = this.simpleQuery(appName, dbHandleName, tableId, whereClause, bindArgs, groupBy, having, orderByColNames, orderByDirections, limit, offset);
            return new UserTable(e, columnDefns, this.getAdminColumns());
        } catch (Exception var14) {
            this.rethrowAlwaysAllowedRemoteException(var14);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable privilegedSimpleQuery(String appName, DbHandle dbHandleName, String tableId, OrderedColumns columnDefns, String whereClause, Object[] bindArgs, String[] groupBy, String having, String[] orderByColNames, String[] orderByDirections, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            BaseTable e = this.privilegedSimpleQuery(appName, dbHandleName, tableId, whereClause, bindArgs, groupBy, having, orderByColNames, orderByDirections, limit, offset);
            return new UserTable(e, columnDefns, this.getAdminColumns());
        } catch (Exception var14) {
            this.rethrowAlwaysAllowedRemoteException(var14);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable arbitrarySqlQuery(String appName, DbHandle dbHandleName, String tableId, OrderedColumns columnDefns, String sqlCommand, Object[] bindArgs, Integer limit, Integer offset) throws ServicesAvailabilityException {
        try {
            BaseTable e = this.arbitrarySqlQuery(appName, dbHandleName, tableId, sqlCommand, bindArgs, limit, offset);
            return new UserTable(e, columnDefns, this.getAdminColumns());
        } catch (Exception var10) {
            this.rethrowAlwaysAllowedRemoteException(var10);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable resumeSimpleQuery(String appName, DbHandle dbHandleName, OrderedColumns columnDefns, ResumableQuery query) throws ServicesAvailabilityException {
        try {
            BaseTable e = this.resumeSimpleQuery(appName, dbHandleName, query);
            return new UserTable(e, columnDefns, this.getAdminColumns());
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable resumePrivilegedSimpleQuery(String appName, DbHandle dbHandleName, OrderedColumns columnDefns, ResumableQuery query) throws ServicesAvailabilityException {
        try {
            BaseTable e = this.resumePrivilegedSimpleQuery(appName, dbHandleName, query);
            return new UserTable(e, columnDefns, this.getAdminColumns());
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void replaceTableMetadata(String appName, DbHandle dbHandleName, KeyValueStoreEntry entry) throws ServicesAvailabilityException {
        try {
            this.dbInterface.replaceTableMetadata(appName, dbHandleName, entry);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void replaceTableMetadataList(String appName, DbHandle dbHandleName, String tableId, List<KeyValueStoreEntry> entries, boolean clear) throws ServicesAvailabilityException {
        try {
            this.dbInterface.replaceTableMetadataList(appName, dbHandleName, tableId, entries, clear);
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void replaceTableMetadataSubList(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, List<KeyValueStoreEntry> entries) throws ServicesAvailabilityException {
        try {
            this.dbInterface.replaceTableMetadataSubList(appName, dbHandleName, tableId, partition, aspect, entries);
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void privilegedUpdateTableETags(String appName, DbHandle dbHandleName, String tableId, String schemaETag, String lastDataETag) throws ServicesAvailabilityException {
        try {
            this.dbInterface.privilegedUpdateTableETags(appName, dbHandleName, tableId, schemaETag, lastDataETag);
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void privilegedUpdateTableLastSyncTime(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            this.dbInterface.privilegedUpdateTableLastSyncTime(appName, dbHandleName, tableId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String getSyncState(String appName, DbHandle dbHandleName, String tableId, String rowId) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getSyncState(appName, dbHandleName, tableId, rowId);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void privilegedUpdateRowETagAndSyncState(String appName, DbHandle dbHandleName, String tableId, String rowId, String rowETag, String syncState) throws ServicesAvailabilityException {
        try {
            this.dbInterface.privilegedUpdateRowETagAndSyncState(appName, dbHandleName, tableId, rowId, rowETag, syncState);
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable getRowsWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.getRowsWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable privilegedGetRowsWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedGetRowsWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable getMostRecentRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.getMostRecentRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable privilegedPerhapsPlaceRowIntoConflictWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedPerhapsPlaceRowIntoConflictWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable privilegedInsertRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId, boolean asCsvRequestedChange) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedInsertRowWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId, asCsvRequestedChange), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var9) {
            this.rethrowAlwaysAllowedRemoteException(var9);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable insertCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.insertCheckpointRowWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var8) {
            this.rethrowNotAuthorizedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable insertRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.insertRowWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var8) {
            this.rethrowNotAuthorizedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable deleteAllCheckpointRowsWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.deleteAllCheckpointRowsWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable deleteLastCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.deleteLastCheckpointRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable privilegedDeleteRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.privilegedDeleteRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable deleteRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.deleteRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable saveAsIncompleteMostRecentCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.saveAsIncompleteMostRecentCheckpointRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable saveAsCompleteMostRecentCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.saveAsCompleteMostRecentCheckpointRowWithId(appName, dbHandleName, tableId, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable updateRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.updateRowWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId), BaseTable.CREATOR);
            return new UserTable(e, orderedColumns, this.getAdminColumns());
        } catch (Exception var8) {
            this.rethrowNotAuthorizedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public UserTable changeRowFilterWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, String filterType, String filterValue, String rowId) throws IllegalArgumentException, ActionNotAuthorizedException, ServicesAvailabilityException {
        if(filterType == null) {
            throw new IllegalArgumentException("filterType is null");
        } else {
            Type.valueOf(filterType);
            ContentValues cvValues = new ContentValues();
            cvValues.put("_filter_type", filterType);
            if(filterValue == null) {
                cvValues.putNull("_filter_value");
            } else {
                cvValues.put("_filter_value", filterValue);
            }

            try {
                BaseTable e = (BaseTable)this.fetchAndRebuildChunks(this.dbInterface.updateRowWithId(appName, dbHandleName, tableId, orderedColumns, cvValues, rowId), BaseTable.CREATOR);
                return new UserTable(e, orderedColumns, this.getAdminColumns());
            } catch (Exception var10) {
                this.rethrowNotAuthorizedRemoteException(var10);
                throw new IllegalStateException("unreachable - keep IDE happy");
            }
        }
    }

    public void resolveServerConflictWithDeleteRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            this.dbInterface.resolveServerConflictWithDeleteRowWithId(appName, dbHandleName, tableId, rowId);
        } catch (Exception var6) {
            this.rethrowNotAuthorizedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void resolveServerConflictTakeLocalRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            this.dbInterface.resolveServerConflictTakeLocalRowWithId(appName, dbHandleName, tableId, rowId);
        } catch (Exception var6) {
            this.rethrowNotAuthorizedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void resolveServerConflictTakeLocalRowPlusServerDeltasWithId(String appName, DbHandle dbHandleName, String tableId, ContentValues cvValues, String rowId) throws ActionNotAuthorizedException, ServicesAvailabilityException {
        try {
            this.dbInterface.resolveServerConflictTakeLocalRowPlusServerDeltasWithId(appName, dbHandleName, tableId, cvValues, rowId);
        } catch (Exception var7) {
            this.rethrowNotAuthorizedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void resolveServerConflictTakeServerRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws ServicesAvailabilityException {
        try {
            this.dbInterface.resolveServerConflictTakeServerRowWithId(appName, dbHandleName, tableId, rowId);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteAppAndTableLevelManifestSyncETags(String appName, DbHandle dbHandleName) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteAppAndTableLevelManifestSyncETags(appName, dbHandleName);
        } catch (Exception var4) {
            this.rethrowAlwaysAllowedRemoteException(var4);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteAllSyncETagsForTableId(String appName, DbHandle dbHandleName, String tableId) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteAllSyncETagsForTableId(appName, dbHandleName, tableId);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteAllSyncETagsExceptForServer(String appName, DbHandle dbHandleName, String verifiedUri) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteAllSyncETagsExceptForServer(appName, dbHandleName, verifiedUri);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void deleteAllSyncETagsUnderServer(String appName, DbHandle dbHandleName, String verifiedUri) throws ServicesAvailabilityException {
        try {
            this.dbInterface.deleteAllSyncETagsUnderServer(appName, dbHandleName, verifiedUri);
        } catch (Exception var5) {
            this.rethrowAlwaysAllowedRemoteException(var5);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String getFileSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, long modificationTimestamp) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getFileSyncETag(appName, dbHandleName, verifiedUri, tableId, modificationTimestamp);
        } catch (Exception var8) {
            this.rethrowAlwaysAllowedRemoteException(var8);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public String getManifestSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId) throws ServicesAvailabilityException {
        try {
            return this.dbInterface.getManifestSyncETag(appName, dbHandleName, verifiedUri, tableId);
        } catch (Exception var6) {
            this.rethrowAlwaysAllowedRemoteException(var6);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void updateFileSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, long modificationTimestamp, String eTag) throws ServicesAvailabilityException {
        try {
            this.dbInterface.updateFileSyncETag(appName, dbHandleName, verifiedUri, tableId, modificationTimestamp, eTag);
        } catch (Exception var9) {
            this.rethrowAlwaysAllowedRemoteException(var9);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    public void updateManifestSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, String eTag) throws ServicesAvailabilityException {
        try {
            this.dbInterface.updateManifestSyncETag(appName, dbHandleName, verifiedUri, tableId, eTag);
        } catch (Exception var7) {
            this.rethrowAlwaysAllowedRemoteException(var7);
            throw new IllegalStateException("unreachable - keep IDE happy");
        }
    }

    private <T> T fetchAndRebuildChunks(DbChunk firstChunk, Creator<T> creator) throws RemoteException {
        List aggregatedChunks = this.retrieveChunks(firstChunk);
        return (T) DbChunkUtil.rebuildFromChunks(aggregatedChunks, creator);
    }

    private <T> T fetchAndRebuildChunks(DbChunk firstChunk, Class<T> serializable) throws RemoteException {
        List aggregatedChunks = this.retrieveChunks(firstChunk);

        try {
            return (T) DbChunkUtil.rebuildFromChunks(aggregatedChunks, serializable);
        } catch (Exception var5) {
            Log.e(TAG, "Failed to rebuild serialized object from chunks");
            return null;
        }
    }

    private List<DbChunk> retrieveChunks(DbChunk firstChunk) throws RemoteException {
        LinkedList aggregatedChunks = new LinkedList();
        aggregatedChunks.add(firstChunk);
        DbChunk currChunk = firstChunk;

        while(currChunk.hasNextID()) {
            ParcelUuid parcelUuid = new ParcelUuid(currChunk.getNextID());
            currChunk = this.dbInterface.getChunk(parcelUuid);
            aggregatedChunks.add(currChunk);
        }

        return aggregatedChunks;
    }
}
