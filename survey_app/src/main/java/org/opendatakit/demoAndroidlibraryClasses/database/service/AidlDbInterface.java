package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbChunk;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;

public interface AidlDbInterface extends IInterface {
    String getRolesList(String var1) throws RemoteException;

    String getUsersList(String var1) throws RemoteException;

    DbHandle openDatabase(String var1) throws RemoteException;

    void closeDatabase(String var1, DbHandle var2) throws RemoteException;

    DbChunk createLocalOnlyTableWithColumns(String var1, DbHandle var2, String var3, ColumnList var4) throws RemoteException;

    void deleteLocalOnlyTable(String var1, DbHandle var2, String var3) throws RemoteException;

    void insertLocalOnlyRow(String var1, DbHandle var2, String var3, ContentValues var4) throws RemoteException;

    void updateLocalOnlyRow(String var1, DbHandle var2, String var3, ContentValues var4, String var5, BindArgs var6) throws RemoteException;

    void deleteLocalOnlyRow(String var1, DbHandle var2, String var3, String var4, BindArgs var5) throws RemoteException;

    void privilegedServerTableSchemaETagChanged(String var1, DbHandle var2, String var3, String var4, String var5) throws RemoteException;

    String setChoiceList(String var1, DbHandle var2, String var3) throws RemoteException;

    String getChoiceList(String var1, DbHandle var2, String var3) throws RemoteException;

    DbChunk createOrOpenTableWithColumns(String var1, DbHandle var2, String var3, ColumnList var4) throws RemoteException;

    DbChunk createOrOpenTableWithColumnsAndProperties(String var1, DbHandle var2, String var3, ColumnList var4, List<KeyValueStoreEntry> var5, boolean var6) throws RemoteException;

    void deleteTableAndAllData(String var1, DbHandle var2, String var3) throws RemoteException;

    void deleteTableMetadata(String var1, DbHandle var2, String var3, String var4, String var5, String var6) throws RemoteException;

    DbChunk getAdminColumns() throws RemoteException;

    DbChunk getAllColumnNames(String var1, DbHandle var2, String var3) throws RemoteException;

    DbChunk getAllTableIds(String var1, DbHandle var2) throws RemoteException;

    DbChunk getTableMetadata(String var1, DbHandle var2, String var3, String var4, String var5, String var6) throws RemoteException;

    DbChunk getTableMetadataIfChanged(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk getExportColumns() throws RemoteException;

    DbChunk getTableDefinitionEntry(String var1, DbHandle var2, String var3) throws RemoteException;

    DbChunk getTableHealthStatuses(String var1, DbHandle var2) throws RemoteException;

    DbChunk getUserDefinedColumns(String var1, DbHandle var2, String var3) throws RemoteException;

    boolean hasTableId(String var1, DbHandle var2, String var3) throws RemoteException;

    DbChunk simpleQuery(String var1, DbHandle var2, String var3, BindArgs var4, QueryBounds var5, String var6) throws RemoteException;

    DbChunk privilegedSimpleQuery(String var1, DbHandle var2, String var3, BindArgs var4, QueryBounds var5, String var6) throws RemoteException;

    void privilegedExecute(String var1, DbHandle var2, String var3, BindArgs var4) throws RemoteException;

    void replaceTableMetadata(String var1, DbHandle var2, KeyValueStoreEntry var3) throws RemoteException;

    void replaceTableMetadataList(String var1, DbHandle var2, String var3, List<KeyValueStoreEntry> var4, boolean var5) throws RemoteException;

    void replaceTableMetadataSubList(String var1, DbHandle var2, String var3, String var4, String var5, List<KeyValueStoreEntry> var6) throws RemoteException;

    void privilegedUpdateTableETags(String var1, DbHandle var2, String var3, String var4, String var5) throws RemoteException;

    void privilegedUpdateTableLastSyncTime(String var1, DbHandle var2, String var3) throws RemoteException;

    String getSyncState(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    void privilegedUpdateRowETagAndSyncState(String var1, DbHandle var2, String var3, String var4, String var5, String var6) throws RemoteException;

    DbChunk getRowsWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk privilegedGetRowsWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk getMostRecentRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk privilegedPerhapsPlaceRowIntoConflictWithId(String var1, DbHandle var2, String var3, OrderedColumns var4, ContentValues var5, String var6) throws RemoteException;

    DbChunk privilegedInsertRowWithId(String var1, DbHandle var2, String var3, OrderedColumns var4, ContentValues var5, String var6, boolean var7) throws RemoteException;

    DbChunk insertCheckpointRowWithId(String var1, DbHandle var2, String var3, OrderedColumns var4, ContentValues var5, String var6) throws RemoteException;

    DbChunk insertRowWithId(String var1, DbHandle var2, String var3, OrderedColumns var4, ContentValues var5, String var6) throws RemoteException;

    DbChunk deleteAllCheckpointRowsWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk deleteLastCheckpointRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk deleteRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk privilegedDeleteRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk saveAsIncompleteMostRecentCheckpointRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk saveAsCompleteMostRecentCheckpointRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    DbChunk updateRowWithId(String var1, DbHandle var2, String var3, OrderedColumns var4, ContentValues var5, String var6) throws RemoteException;

    void resolveServerConflictWithDeleteRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    void resolveServerConflictTakeLocalRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    void resolveServerConflictTakeLocalRowPlusServerDeltasWithId(String var1, DbHandle var2, String var3, ContentValues var4, String var5) throws RemoteException;

    void resolveServerConflictTakeServerRowWithId(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    void deleteAppAndTableLevelManifestSyncETags(String var1, DbHandle var2) throws RemoteException;

    void deleteAllSyncETagsForTableId(String var1, DbHandle var2, String var3) throws RemoteException;

    void deleteAllSyncETagsExceptForServer(String var1, DbHandle var2, String var3) throws RemoteException;

    void deleteAllSyncETagsUnderServer(String var1, DbHandle var2, String var3) throws RemoteException;

    String getFileSyncETag(String var1, DbHandle var2, String var3, String var4, long var5) throws RemoteException;

    String getManifestSyncETag(String var1, DbHandle var2, String var3, String var4) throws RemoteException;

    void updateFileSyncETag(String var1, DbHandle var2, String var3, String var4, long var5, String var7) throws RemoteException;

    void updateManifestSyncETag(String var1, DbHandle var2, String var3, String var4, String var5) throws RemoteException;

    DbChunk getChunk(ParcelUuid var1) throws RemoteException;

    public abstract static class Stub extends Binder implements AidlDbInterface {
        private static final String DESCRIPTOR = "org.opendatakit.database.service.AidlDbInterface";
        static final int TRANSACTION_getRolesList = 1;
        static final int TRANSACTION_getUsersList = 2;
        static final int TRANSACTION_openDatabase = 3;
        static final int TRANSACTION_closeDatabase = 4;
        static final int TRANSACTION_createLocalOnlyTableWithColumns = 5;
        static final int TRANSACTION_deleteLocalOnlyTable = 6;
        static final int TRANSACTION_insertLocalOnlyRow = 7;
        static final int TRANSACTION_updateLocalOnlyRow = 8;
        static final int TRANSACTION_deleteLocalOnlyRow = 9;
        static final int TRANSACTION_privilegedServerTableSchemaETagChanged = 10;
        static final int TRANSACTION_setChoiceList = 11;
        static final int TRANSACTION_getChoiceList = 12;
        static final int TRANSACTION_createOrOpenTableWithColumns = 13;
        static final int TRANSACTION_createOrOpenTableWithColumnsAndProperties = 14;
        static final int TRANSACTION_deleteTableAndAllData = 15;
        static final int TRANSACTION_deleteTableMetadata = 16;
        static final int TRANSACTION_getAdminColumns = 17;
        static final int TRANSACTION_getAllColumnNames = 18;
        static final int TRANSACTION_getAllTableIds = 19;
        static final int TRANSACTION_getTableMetadata = 20;
        static final int TRANSACTION_getTableMetadataIfChanged = 21;
        static final int TRANSACTION_getExportColumns = 22;
        static final int TRANSACTION_getTableDefinitionEntry = 23;
        static final int TRANSACTION_getTableHealthStatuses = 24;
        static final int TRANSACTION_getUserDefinedColumns = 25;
        static final int TRANSACTION_hasTableId = 26;
        static final int TRANSACTION_simpleQuery = 27;
        static final int TRANSACTION_privilegedSimpleQuery = 28;
        static final int TRANSACTION_privilegedExecute = 29;
        static final int TRANSACTION_replaceTableMetadata = 30;
        static final int TRANSACTION_replaceTableMetadataList = 31;
        static final int TRANSACTION_replaceTableMetadataSubList = 32;
        static final int TRANSACTION_privilegedUpdateTableETags = 33;
        static final int TRANSACTION_privilegedUpdateTableLastSyncTime = 34;
        static final int TRANSACTION_getSyncState = 35;
        static final int TRANSACTION_privilegedUpdateRowETagAndSyncState = 36;
        static final int TRANSACTION_getRowsWithId = 37;
        static final int TRANSACTION_privilegedGetRowsWithId = 38;
        static final int TRANSACTION_getMostRecentRowWithId = 39;
        static final int TRANSACTION_privilegedPerhapsPlaceRowIntoConflictWithId = 40;
        static final int TRANSACTION_privilegedInsertRowWithId = 41;
        static final int TRANSACTION_insertCheckpointRowWithId = 42;
        static final int TRANSACTION_insertRowWithId = 43;
        static final int TRANSACTION_deleteAllCheckpointRowsWithId = 44;
        static final int TRANSACTION_deleteLastCheckpointRowWithId = 45;
        static final int TRANSACTION_deleteRowWithId = 46;
        static final int TRANSACTION_privilegedDeleteRowWithId = 47;
        static final int TRANSACTION_saveAsIncompleteMostRecentCheckpointRowWithId = 48;
        static final int TRANSACTION_saveAsCompleteMostRecentCheckpointRowWithId = 49;
        static final int TRANSACTION_updateRowWithId = 50;
        static final int TRANSACTION_resolveServerConflictWithDeleteRowWithId = 51;
        static final int TRANSACTION_resolveServerConflictTakeLocalRowWithId = 52;
        static final int TRANSACTION_resolveServerConflictTakeLocalRowPlusServerDeltasWithId = 53;
        static final int TRANSACTION_resolveServerConflictTakeServerRowWithId = 54;
        static final int TRANSACTION_deleteAppAndTableLevelManifestSyncETags = 55;
        static final int TRANSACTION_deleteAllSyncETagsForTableId = 56;
        static final int TRANSACTION_deleteAllSyncETagsExceptForServer = 57;
        static final int TRANSACTION_deleteAllSyncETagsUnderServer = 58;
        static final int TRANSACTION_getFileSyncETag = 59;
        static final int TRANSACTION_getManifestSyncETag = 60;
        static final int TRANSACTION_updateFileSyncETag = 61;
        static final int TRANSACTION_updateManifestSyncETag = 62;
        static final int TRANSACTION_getChunk = 63;

        public Stub() {
            this.attachInterface(this, "org.opendatakit.database.service.AidlDbInterface");
        }

        public static AidlDbInterface asInterface(IBinder obj) {
            if(obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("org.opendatakit.database.service.AidlDbInterface");
                return (AidlDbInterface)(iin != null && iin instanceof AidlDbInterface?(AidlDbInterface)iin:new AidlDbInterface.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _arg2;
            String _arg3;
            String _arg4;
            String _arg5;
            String _arg51;
            String _arg01;
            DbHandle _result2;
            long _arg41;
            ContentValues _arg31;
            ContentValues _arg42;
            OrderedColumns _arg32;
            DbChunk _arg43;
            DbChunk _arg52;
            DbChunk _arg02;
            DbChunk _arg22;
            QueryBounds _arg45;
            BindArgs _arg34;
            DbChunk _arg36;
            ColumnList _arg37;
            String _result3;
            switch(code) {
                case 1:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    _result3 = this.getRolesList(_arg01);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 2:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    _result3 = this.getUsersList(_arg01);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 3:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    _result2 = this.openDatabase(_arg01);
                    reply.writeNoException();
                    if(_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 4:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    this.closeDatabase(_arg01, _result2);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg37 = (ColumnList)ColumnList.CREATOR.createFromParcel(data);
                    } else {
                        _arg37 = null;
                    }

                    _arg43 = this.createLocalOnlyTableWithColumns(_arg01, _result2, _arg2, _arg37);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 6:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.deleteLocalOnlyTable(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg31 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg31 = null;
                    }

                    this.insertLocalOnlyRow(_arg01, _result2, _arg2, _arg31);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg31 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg31 = null;
                    }

                    _arg4 = data.readString();
                    BindArgs _arg56;
                    if(0 != data.readInt()) {
                        _arg56 = (BindArgs)BindArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg56 = null;
                    }

                    this.updateLocalOnlyRow(_arg01, _result2, _arg2, _arg31, _arg4, _arg56);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    BindArgs _arg47;
                    if(0 != data.readInt()) {
                        _arg47 = (BindArgs)BindArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg47 = null;
                    }

                    this.deleteLocalOnlyRow(_arg01, _result2, _arg2, _arg3, _arg47);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    this.privilegedServerTableSchemaETagChanged(_arg01, _result2, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = this.setChoiceList(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    reply.writeString(_arg3);
                    return true;
                case 12:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = this.getChoiceList(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    reply.writeString(_arg3);
                    return true;
                case 13:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg37 = (ColumnList)ColumnList.CREATOR.createFromParcel(data);
                    } else {
                        _arg37 = null;
                    }

                    _arg43 = this.createOrOpenTableWithColumns(_arg01, _result2, _arg2, _arg37);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 14:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg37 = (ColumnList)ColumnList.CREATOR.createFromParcel(data);
                    } else {
                        _arg37 = null;
                    }

                    ArrayList _arg46 = data.createTypedArrayList(KeyValueStoreEntry.CREATOR);
                    boolean _arg55 = 0 != data.readInt();
                    _arg52 = this.createOrOpenTableWithColumnsAndProperties(_arg01, _result2, _arg2, _arg37, _arg46, _arg55);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 15:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.deleteTableAndAllData(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    _arg5 = data.readString();
                    this.deleteTableMetadata(_arg01, _result2, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg02 = this.getAdminColumns();
                    reply.writeNoException();
                    if(_arg02 != null) {
                        reply.writeInt(1);
                        _arg02.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 18:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg36 = this.getAllColumnNames(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    if(_arg36 != null) {
                        reply.writeInt(1);
                        _arg36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 19:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg22 = this.getAllTableIds(_arg01, _result2);
                    reply.writeNoException();
                    if(_arg22 != null) {
                        reply.writeInt(1);
                        _arg22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 20:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    _arg5 = data.readString();
                    _arg52 = this.getTableMetadata(_arg01, _result2, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 21:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.getTableMetadataIfChanged(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 22:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg02 = this.getExportColumns();
                    reply.writeNoException();
                    if(_arg02 != null) {
                        reply.writeInt(1);
                        _arg02.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 23:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg36 = this.getTableDefinitionEntry(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    if(_arg36 != null) {
                        reply.writeInt(1);
                        _arg36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 24:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg22 = this.getTableHealthStatuses(_arg01, _result2);
                    reply.writeNoException();
                    if(_arg22 != null) {
                        reply.writeInt(1);
                        _arg22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 25:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg36 = this.getUserDefinedColumns(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    if(_arg36 != null) {
                        reply.writeInt(1);
                        _arg36.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 26:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    boolean _arg35 = this.hasTableId(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    reply.writeInt(_arg35?1:0);
                    return true;
                case 27:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg34 = (BindArgs)BindArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg45 = (QueryBounds)QueryBounds.CREATOR.createFromParcel(data);
                    } else {
                        _arg45 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.simpleQuery(_arg01, _result2, _arg2, _arg34, _arg45, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 28:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg34 = (BindArgs)BindArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg45 = (QueryBounds)QueryBounds.CREATOR.createFromParcel(data);
                    } else {
                        _arg45 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.privilegedSimpleQuery(_arg01, _result2, _arg2, _arg34, _arg45, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 29:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg34 = (BindArgs)BindArgs.CREATOR.createFromParcel(data);
                    } else {
                        _arg34 = null;
                    }

                    this.privilegedExecute(_arg01, _result2, _arg2, _arg34);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    KeyValueStoreEntry _arg21;
                    if(0 != data.readInt()) {
                        _arg21 = (KeyValueStoreEntry)KeyValueStoreEntry.CREATOR.createFromParcel(data);
                    } else {
                        _arg21 = null;
                    }

                    this.replaceTableMetadata(_arg01, _result2, _arg21);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    ArrayList _arg33 = data.createTypedArrayList(KeyValueStoreEntry.CREATOR);
                    boolean _arg44 = 0 != data.readInt();
                    this.replaceTableMetadataList(_arg01, _result2, _arg2, _arg33, _arg44);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    ArrayList _arg53 = data.createTypedArrayList(KeyValueStoreEntry.CREATOR);
                    this.replaceTableMetadataSubList(_arg01, _result2, _arg2, _arg3, _arg4, _arg53);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    this.privilegedUpdateTableETags(_arg01, _result2, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.privilegedUpdateTableLastSyncTime(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = this.getSyncState(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeString(_arg4);
                    return true;
                case 36:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    _arg5 = data.readString();
                    this.privilegedUpdateRowETagAndSyncState(_arg01, _result2, _arg2, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.getRowsWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 38:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.privilegedGetRowsWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 39:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.getMostRecentRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 40:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg32 = (OrderedColumns)OrderedColumns.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg42 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.privilegedPerhapsPlaceRowIntoConflictWithId(_arg01, _result2, _arg2, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 41:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg32 = (OrderedColumns)OrderedColumns.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg42 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }

                    _arg5 = data.readString();
                    boolean _arg54 = 0 != data.readInt();
                    DbChunk _result1 = this.privilegedInsertRowWithId(_arg01, _result2, _arg2, _arg32, _arg42, _arg5, _arg54);
                    reply.writeNoException();
                    if(_result1 != null) {
                        reply.writeInt(1);
                        _result1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 42:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg32 = (OrderedColumns)OrderedColumns.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg42 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.insertCheckpointRowWithId(_arg01, _result2, _arg2, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 43:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg32 = (OrderedColumns)OrderedColumns.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg42 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.insertRowWithId(_arg01, _result2, _arg2, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 44:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.deleteAllCheckpointRowsWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 45:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.deleteLastCheckpointRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 46:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.deleteRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 47:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.privilegedDeleteRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 48:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.saveAsIncompleteMostRecentCheckpointRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 49:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg43 = this.saveAsCompleteMostRecentCheckpointRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    if(_arg43 != null) {
                        reply.writeInt(1);
                        _arg43.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 50:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg32 = (OrderedColumns)OrderedColumns.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }

                    if(0 != data.readInt()) {
                        _arg42 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }

                    _arg5 = data.readString();
                    _arg52 = this.updateRowWithId(_arg01, _result2, _arg2, _arg32, _arg42, _arg5);
                    reply.writeNoException();
                    if(_arg52 != null) {
                        reply.writeInt(1);
                        _arg52.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 51:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    this.resolveServerConflictWithDeleteRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    this.resolveServerConflictTakeLocalRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    if(0 != data.readInt()) {
                        _arg31 = (ContentValues)ContentValues.CREATOR.createFromParcel(data);
                    } else {
                        _arg31 = null;
                    }

                    _arg4 = data.readString();
                    this.resolveServerConflictTakeLocalRowPlusServerDeltasWithId(_arg01, _result2, _arg2, _arg31, _arg4);
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    this.resolveServerConflictTakeServerRowWithId(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    this.deleteAppAndTableLevelManifestSyncETags(_arg01, _result2);
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.deleteAllSyncETagsForTableId(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 57:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.deleteAllSyncETagsExceptForServer(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    this.deleteAllSyncETagsUnderServer(_arg01, _result2, _arg2);
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg41 = data.readLong();
                    _arg51 = this.getFileSyncETag(_arg01, _result2, _arg2, _arg3, _arg41);
                    reply.writeNoException();
                    reply.writeString(_arg51);
                    return true;
                case 60:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = this.getManifestSyncETag(_arg01, _result2, _arg2, _arg3);
                    reply.writeNoException();
                    reply.writeString(_arg4);
                    return true;
                case 61:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg41 = data.readLong();
                    _arg51 = data.readString();
                    this.updateFileSyncETag(_arg01, _result2, _arg2, _arg3, _arg41, _arg51);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    _arg01 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (DbHandle)DbHandle.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _arg2 = data.readString();
                    _arg3 = data.readString();
                    _arg4 = data.readString();
                    this.updateManifestSyncETag(_arg01, _result2, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 63:
                    data.enforceInterface("org.opendatakit.database.service.AidlDbInterface");
                    ParcelUuid _arg0;
                    if(0 != data.readInt()) {
                        _arg0 = (ParcelUuid)ParcelUuid.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    DbChunk _result = this.getChunk(_arg0);
                    reply.writeNoException();
                    if(_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 1598968902:
                    reply.writeString("org.opendatakit.database.service.AidlDbInterface");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements AidlDbInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "org.opendatakit.database.service.AidlDbInterface";
            }

            public String getRolesList(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getUsersList(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbHandle openDatabase(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbHandle _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbHandle)DbHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void closeDatabase(String appName, DbHandle dbHandleName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public DbChunk createLocalOnlyTableWithColumns(String appName, DbHandle dbHandleName, String tableId, ColumnList columns) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(columns != null) {
                        _data.writeInt(1);
                        columns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void deleteLocalOnlyTable(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void insertLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, ContentValues rowValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(rowValues != null) {
                        _data.writeInt(1);
                        rowValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void updateLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, ContentValues rowValues, String whereClause, BindArgs sqlBindArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(rowValues != null) {
                        _data.writeInt(1);
                        rowValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(whereClause);
                    if(sqlBindArgs != null) {
                        _data.writeInt(1);
                        sqlBindArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteLocalOnlyRow(String appName, DbHandle dbHandleName, String tableId, String whereClause, BindArgs sqlBindArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(whereClause);
                    if(sqlBindArgs != null) {
                        _data.writeInt(1);
                        sqlBindArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void privilegedServerTableSchemaETagChanged(String appName, DbHandle dbHandleName, String tableId, String schemaETag, String tableInstanceFilesUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(schemaETag);
                    _data.writeString(tableInstanceFilesUri);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String setChoiceList(String appName, DbHandle dbHandleName, String choiceListJSON) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(choiceListJSON);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getChoiceList(String appName, DbHandle dbHandleName, String choiceListId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(choiceListId);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk createOrOpenTableWithColumns(String appName, DbHandle dbHandleName, String tableId, ColumnList columns) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(columns != null) {
                        _data.writeInt(1);
                        columns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk createOrOpenTableWithColumnsAndProperties(String appName, DbHandle dbHandleName, String tableId, ColumnList columns, List<KeyValueStoreEntry> metaData, boolean clear) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(columns != null) {
                        _data.writeInt(1);
                        columns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeTypedList(metaData);
                    _data.writeInt(clear?1:0);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void deleteTableAndAllData(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteTableMetadata(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(partition);
                    _data.writeString(aspect);
                    _data.writeString(key);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public DbChunk getAdminColumns() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getAllColumnNames(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getAllTableIds(String appName, DbHandle dbHandleName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getTableMetadata(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(partition);
                    _data.writeString(aspect);
                    _data.writeString(key);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getTableMetadataIfChanged(String appName, DbHandle dbHandleName, String tableId, String revId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(revId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getExportColumns() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getTableDefinitionEntry(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getTableHealthStatuses(String appName, DbHandle dbHandleName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getUserDefinedColumns(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean hasTableId(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk simpleQuery(String appName, DbHandle dbHandleName, String sqlCommand, BindArgs sqlBindArgs, QueryBounds sqlQueryBounds, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(sqlCommand);
                    if(sqlBindArgs != null) {
                        _data.writeInt(1);
                        sqlBindArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(sqlQueryBounds != null) {
                        _data.writeInt(1);
                        sqlQueryBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk privilegedSimpleQuery(String appName, DbHandle dbHandleName, String sqlCommand, BindArgs sqlBindArgs, QueryBounds sqlQueryBounds, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(sqlCommand);
                    if(sqlBindArgs != null) {
                        _data.writeInt(1);
                        sqlBindArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(sqlQueryBounds != null) {
                        _data.writeInt(1);
                        sqlQueryBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void privilegedExecute(String appName, DbHandle dbHandleName, String sqlCommand, BindArgs sqlBindArgs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(sqlCommand);
                    if(sqlBindArgs != null) {
                        _data.writeInt(1);
                        sqlBindArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void replaceTableMetadata(String appName, DbHandle dbHandleName, KeyValueStoreEntry entry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(entry != null) {
                        _data.writeInt(1);
                        entry.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void replaceTableMetadataList(String appName, DbHandle dbHandleName, String tableId, List<KeyValueStoreEntry> metaData, boolean clear) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeTypedList(metaData);
                    _data.writeInt(clear?1:0);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void replaceTableMetadataSubList(String appName, DbHandle dbHandleName, String tableId, String partition, String aspect, List<KeyValueStoreEntry> metaData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(partition);
                    _data.writeString(aspect);
                    _data.writeTypedList(metaData);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void privilegedUpdateTableETags(String appName, DbHandle dbHandleName, String tableId, String schemaETag, String lastDataETag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(schemaETag);
                    _data.writeString(lastDataETag);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void privilegedUpdateTableLastSyncTime(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getSyncState(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void privilegedUpdateRowETagAndSyncState(String appName, DbHandle dbHandleName, String tableId, String rowId, String rowETag, String syncState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    _data.writeString(rowETag);
                    _data.writeString(syncState);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public DbChunk getRowsWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk privilegedGetRowsWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk getMostRecentRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk privilegedPerhapsPlaceRowIntoConflictWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(orderedColumns != null) {
                        _data.writeInt(1);
                        orderedColumns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk privilegedInsertRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId, boolean asCsvRequestedChange) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(orderedColumns != null) {
                        _data.writeInt(1);
                        orderedColumns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    _data.writeInt(asCsvRequestedChange?1:0);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk insertCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(orderedColumns != null) {
                        _data.writeInt(1);
                        orderedColumns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk insertRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(orderedColumns != null) {
                        _data.writeInt(1);
                        orderedColumns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk deleteAllCheckpointRowsWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk deleteLastCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk deleteRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk privilegedDeleteRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk saveAsIncompleteMostRecentCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk saveAsCompleteMostRecentCheckpointRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public DbChunk updateRowWithId(String appName, DbHandle dbHandleName, String tableId, OrderedColumns orderedColumns, ContentValues cvValues, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(orderedColumns != null) {
                        _data.writeInt(1);
                        orderedColumns.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void resolveServerConflictWithDeleteRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void resolveServerConflictTakeLocalRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void resolveServerConflictTakeLocalRowPlusServerDeltasWithId(String appName, DbHandle dbHandleName, String tableId, ContentValues cvValues, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    if(cvValues != null) {
                        _data.writeInt(1);
                        cvValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(rowId);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void resolveServerConflictTakeServerRowWithId(String appName, DbHandle dbHandleName, String tableId, String rowId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    _data.writeString(rowId);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteAppAndTableLevelManifestSyncETags(String appName, DbHandle dbHandleName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteAllSyncETagsForTableId(String appName, DbHandle dbHandleName, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(tableId);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteAllSyncETagsExceptForServer(String appName, DbHandle dbHandleName, String verifiedUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteAllSyncETagsUnderServer(String appName, DbHandle dbHandleName, String verifiedUri) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getFileSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, long modificationTimestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    _data.writeString(tableId);
                    _data.writeLong(modificationTimestamp);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getManifestSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    _data.writeString(tableId);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void updateFileSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, long modificationTimestamp, String eTag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    _data.writeString(tableId);
                    _data.writeLong(modificationTimestamp);
                    _data.writeString(eTag);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void updateManifestSyncETag(String appName, DbHandle dbHandleName, String verifiedUri, String tableId, String eTag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    _data.writeString(appName);
                    if(dbHandleName != null) {
                        _data.writeInt(1);
                        dbHandleName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(verifiedUri);
                    _data.writeString(tableId);
                    _data.writeString(eTag);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public DbChunk getChunk(ParcelUuid chunkID) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                DbChunk _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.database.service.AidlDbInterface");
                    if(chunkID != null) {
                        _data.writeInt(1);
                        chunkID.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (DbChunk)DbChunk.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }
        }
    }
}
