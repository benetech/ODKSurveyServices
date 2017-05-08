package org.opendatakit.demoAndroidlibraryClasses.builder;

import android.content.ContentValues;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.RFC4180CsvReader;
import org.opendatakit.aggregate.odktables.rest.RFC4180CsvWriter;
import org.opendatakit.aggregate.odktables.rest.SavepointTypeManipulator;
import org.opendatakit.aggregate.odktables.rest.SyncState;
import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.builder.CsvUtilSupervisor;
import org.opendatakit.demoAndroidlibraryClasses.builder.PropertiesFileUtils;
import org.opendatakit.demoAndroidlibraryClasses.builder.PropertiesFileUtils.DataTableDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.data.UserTable;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.listener.ExportListener;
import org.opendatakit.demoAndroidlibraryClasses.listener.ImportListener;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.DataTableColumns;
import org.opendatakit.demoAndroidlibraryClasses.utilities.LocalizationUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class CsvUtil {
    private static final String TAG = CsvUtil.class.getSimpleName();
    private final String appName;
    private final CsvUtilSupervisor supervisor;

    public CsvUtil(CsvUtilSupervisor supervisor, String appName) {
        this.supervisor = supervisor;
        this.appName = appName;
    }

    public boolean exportSeparable(ExportListener exportListener, DbHandle db, String tableId, OrderedColumns orderedDefns, String fileQualifier) throws ServicesAvailabilityException {
        ArrayList columns = new ArrayList();
        WebLogger.getLogger(this.appName).i(TAG, "exportSeparable: tableId: " + tableId + " fileQualifier: " + (fileQualifier == null?"<null>":fileQualifier));
        columns.add("_id");
        columns.add("_form_id");
        columns.add("_locale");
        columns.add("_savepoint_type");
        columns.add("_savepoint_timestamp");
        columns.add("_savepoint_creator");
        Iterator exportColumns = orderedDefns.getColumnDefinitions().iterator();

        while(exportColumns.hasNext()) {
            ColumnDefinition tableInstancesFolder = (ColumnDefinition)exportColumns.next();
            if(tableInstancesFolder.isUnitOfRetention()) {
                columns.add(tableInstancesFolder.getElementKey());
            }
        }

        String[] var40 = this.supervisor.getDatabase().getExportColumns();
        String[] var41 = var40;
        int instancesWithData = var40.length;

        for(int output = 0; output < instancesWithData; ++output) {
            String outputCsv = var41[output];
            if(!columns.contains(outputCsv)) {
                columns.add(outputCsv);
            }
        }

        File var42 = new File(ODKFileUtils.getInstancesFolder(this.appName, tableId));
        HashSet var43 = new HashSet();
        if(var42.exists() && var42.isDirectory()) {
            File[] var44 = var42.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isDirectory() && pathname.list().length != 0;
                }
            });
            var43.addAll(Arrays.asList(var44));
        }

        OutputStreamWriter var45 = null;
        File var46 = null;

        boolean var47;
        try {
            File e1;
            boolean e2;
            try {
                var46 = new File(ODKFileUtils.getOutputTableCsvFile(this.appName, tableId, fileQualifier));
                var46.mkdirs();
                File e = new File(ODKFileUtils.getOutputTableDefinitionCsvFile(this.appName, tableId, fileQualifier));
                e1 = new File(ODKFileUtils.getOutputTablePropertiesCsvFile(this.appName, tableId, fileQualifier));
                if(!this.writePropertiesCsv(db, tableId, orderedDefns, e, e1)) {
                    e2 = false;
                    return e2;
                }

                String var48 = "_savepoint_type IS NOT NULL AND (_conflict_type IS NULL OR _conflict_type = " + Integer.toString(1) + ")";
                String[] e3 = new String[0];
                UserTable table = this.supervisor.getDatabase().simpleQuery(this.appName, db, tableId, orderedDefns, var48, e3, e3, (String)null, (String[])null, (String[])null, (Integer)null, (Integer)null);
                File file = new File(var46, tableId + (fileQualifier != null && fileQualifier.length() != 0?"." + fileQualifier:"") + ".csv");
                FileOutputStream out = new FileOutputStream(file);
                var45 = new OutputStreamWriter(out, "UTF-8");
                RFC4180CsvWriter cw = new RFC4180CsvWriter(var45);
                cw.writeNext((String[])columns.toArray(new String[columns.size()]));
                String[] row = new String[columns.size()];

                for(int i = 0; i < table.getNumberOfRows(); ++i) {
                    Row e4 = table.getRowAtIndex(i);

                    for(int instanceId = 0; instanceId < columns.size(); ++instanceId) {
                        row[instanceId] = e4.getDataByKey((String)columns.get(instanceId));
                    }

                    cw.writeNext(row);
                    String var50 = table.getRowId(i);
                    File tableInstanceFolder = new File(ODKFileUtils.getInstanceFolder(this.appName, tableId, var50));
                    if(var43.contains(tableInstanceFolder)) {
                        File outputInstanceFolder = new File(ODKFileUtils.getOutputCsvInstanceFolder(this.appName, tableId, var50));
                        outputInstanceFolder.mkdirs();
                        FileUtils.copyDirectory(tableInstanceFolder, outputInstanceFolder);
                        var43.remove(tableInstanceFolder);
                    }
                }

                cw.flush();
                cw.close();
                boolean var49 = true;
                return var49;
            } catch (IOException var38) {
                if(var46 != null) {
                    try {
                        for(e1 = new File(ODKFileUtils.getOutputCsvFolder(this.appName)); FileUtils.directoryContains(e1, var46); var46 = var46.getParentFile()) {
                            FileUtils.deleteDirectory(var46);
                        }
                    } catch (IOException var37) {
                        var37.printStackTrace();
                        e2 = false;
                        return e2;
                    }
                }
            }

            var47 = false;
        } finally {
            try {
                if(var45 != null) {
                    var45.close();
                }
            } catch (IOException var36) {
                ;
            }

        }

        return var47;
    }

    public boolean writePropertiesCsv(DbHandle db, String tableId, OrderedColumns orderedDefns) throws ServicesAvailabilityException {
        File definitionCsv = new File(ODKFileUtils.getTableDefinitionCsvFile(this.appName, tableId));
        File propertiesCsv = new File(ODKFileUtils.getTablePropertiesCsvFile(this.appName, tableId));
        return this.writePropertiesCsv(db, tableId, orderedDefns, definitionCsv, propertiesCsv);
    }

    private boolean writePropertiesCsv(DbHandle db, String tableId, OrderedColumns orderedDefns, File definitionCsv, File propertiesCsv) throws ServicesAvailabilityException {
        WebLogger.getLogger(this.appName).i(TAG, "writePropertiesCsv: tableId: " + tableId);
        ArrayList kvsEntries = this.supervisor.getDatabase().getTableMetadata(this.appName, db, tableId, (String)null, (String)null, (String)null, (String)null).getEntries();

        for(int i = 0; i < kvsEntries.size(); ++i) {
            KeyValueStoreEntry entry = (KeyValueStoreEntry)kvsEntries.get(i);
            if(entry.partition.equals("Column") && entry.key.equals("displayChoicesList")) {
                entry.type = ElementDataType.array.name();
                if(entry.value != null && entry.value.trim().length() != 0) {
                    String choiceListJSON = this.supervisor.getDatabase().getChoiceList(this.appName, db, entry.value);
                    entry.value = choiceListJSON;
                } else {
                    entry.value = null;
                }
            }
        }

        return PropertiesFileUtils.writePropertiesIntoCsv(this.appName, tableId, orderedDefns, kvsEntries, definitionCsv, propertiesCsv);
    }

    private int countUpToLastNonNullElement(String[] row) {
        for(int i = row.length - 1; i >= 0; --i) {
            if(row[i] != null) {
                return i + 1;
            }
        }

        return 0;
    }

    public synchronized void updateTablePropertiesFromCsv(String tableId) throws IOException, ServicesAvailabilityException {
        DataTableDefinition dtd = PropertiesFileUtils.readPropertiesFromCsv(this.appName, tableId);
        DbHandle db = null;

        try {
            db = this.supervisor.getDatabase().openDatabase(this.appName);
            Iterator i$ = dtd.kvsEntries.iterator();

            while(i$.hasNext()) {
                KeyValueStoreEntry entry = (KeyValueStoreEntry)i$.next();
                if(entry.partition.equals("Column") && entry.key.equals("displayChoicesList")) {
                    entry.type = ElementDataType.string.name();
                    if(entry.value != null && entry.value.trim().length() != 0) {
                        String choiceListId = this.supervisor.getDatabase().setChoiceList(this.appName, db, entry.value);
                        entry.value = choiceListId;
                    } else {
                        entry.value = null;
                    }
                }
            }

            this.supervisor.getDatabase().createOrOpenTableWithColumnsAndProperties(this.appName, db, tableId, dtd.columnList, dtd.kvsEntries, true);
        } finally {
            if(db != null) {
                this.supervisor.getDatabase().closeDatabase(this.appName, db);
            }

        }
    }

    public boolean importSeparable(ImportListener importListener, String tableId, String fileQualifier, boolean createIfNotPresent) throws ServicesAvailabilityException {
        DbHandle db = null;

        HashSet instancesHavingData = null;
        try {
            db = this.supervisor.getDatabase().openDatabase(this.appName);
            if(!this.supervisor.getDatabase().hasTableId(this.appName, db, tableId)) {
                boolean var60;
                if(!createIfNotPresent) {
                    var60 = false;
                    return var60;
                }

                this.updateTablePropertiesFromCsv(tableId);
                if(!this.supervisor.getDatabase().hasTableId(this.appName, db, tableId)) {
                    var60 = false;
                    return var60;
                }
            }

            OrderedColumns e = this.supervisor.getDatabase().getUserDefinedColumns(this.appName, db, tableId);
            WebLogger.getLogger(this.appName).i(TAG, "importSeparable: tableId: " + tableId + " fileQualifier: " + (fileQualifier == null?"<null>":fileQualifier));
            InputStreamReader var61 = null;

            try {
                File e1 = new File(ODKFileUtils.getAssetsCsvInstancesFolder(this.appName, tableId));
                instancesHavingData = new HashSet();
                if(e1.exists() && e1.isDirectory()) {
                    File[] e2 = e1.listFiles(new FileFilter() {
                        public boolean accept(File pathname) {
                            return pathname.isDirectory() && pathname.list().length != 0;
                        }
                    });
                    instancesHavingData.addAll(Arrays.asList(e2));
                }

                File var62 = new File(ODKFileUtils.getAssetsCsvFolder(this.appName));
                File file = new File(var62, tableId + (fileQualifier != null && fileQualifier.length() != 0?"." + fileQualifier:"") + ".csv");
                FileInputStream in = new FileInputStream(file);
                var61 = new InputStreamReader(in, "UTF-8");
                RFC4180CsvReader cr = new RFC4180CsvReader(var61);
                String[] columnsInFile = cr.readNext();
                int columnsInFileLength = this.countUpToLastNonNullElement(columnsInFile);
                HashMap valueMap = new HashMap();
                int rowCount = 0;

                while(true) {
                    String[] row = cr.readNext();
                    ++rowCount;
                    if(rowCount % 5 == 0) {
                        importListener.updateProgressDetail("Row " + rowCount);
                    }

                    int rowLength;
                    if(row == null || this.countUpToLastNonNullElement(row) == 0) {
                        cr.close();
                        rowLength = 1;
                        //return (boolean)rowLength;
                    }

                    rowLength = this.countUpToLastNonNullElement(row);
                    String v_id = UUID.randomUUID().toString();
                    String v_form_id = null;
                    String v_locale = "default";
                    String v_savepoint_type = SavepointTypeManipulator.complete();
                    String v_savepoint_creator = "anonymous";
                    String v_savepoint_timestamp = TableConstants.nanoSecondsFromMillis(Long.valueOf(System.currentTimeMillis()));
                    String v_row_etag = null;
                    String v_filter_type = DataTableColumns.DEFAULT_FILTER_TYPE.getText();
                    String v_filter_value = DataTableColumns.DEFAULT_FILTER_VALUE.getText();
                    valueMap.clear();
                    boolean e3 = false;

                    String assetsInstanceFolder;
                    for(int table = 0; table < columnsInFileLength && table <= rowLength; ++table) {
                        String syncState = columnsInFile[table];
                        assetsInstanceFolder = row[table];
                        if("_id".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                e3 = true;
                                v_id = assetsInstanceFolder;
                            }
                        } else if("_form_id".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_form_id = assetsInstanceFolder;
                            }
                        } else if("_locale".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_locale = assetsInstanceFolder;
                            }
                        } else if("_savepoint_type".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_savepoint_type = assetsInstanceFolder;
                            }
                        } else if("_savepoint_creator".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_savepoint_creator = assetsInstanceFolder;
                            }
                        } else if("_savepoint_timestamp".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_savepoint_timestamp = assetsInstanceFolder;
                            }
                        } else if("_row_etag".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_row_etag = assetsInstanceFolder;
                            }
                        } else if("_filter_type".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_filter_type = assetsInstanceFolder;
                            }
                        } else if("_filter_value".equals(syncState)) {
                            if(assetsInstanceFolder != null && assetsInstanceFolder.length() != 0) {
                                v_filter_value = assetsInstanceFolder;
                            }
                        } else {
                            try {
                                e.find(syncState);
                                valueMap.put(syncState, assetsInstanceFolder);
                            } catch (IllegalArgumentException var55) {
                                ;
                            }
                        }
                    }

                    UserTable var63 = this.supervisor.getDatabase().privilegedGetRowsWithId(this.appName, db, tableId, e, v_id);
                    if(var63.getNumberOfRows() > 1) {
                        throw new IllegalStateException("There are either checkpoint or conflict rows in the destination table");
                    }

                    SyncState var64 = null;
                    if(e3 && var63.getNumberOfRows() == 1) {
                        assetsInstanceFolder = var63.getRowAtIndex(0).getDataByKey("_sync_state");
                        if(assetsInstanceFolder == null) {
                            throw new IllegalStateException("Unexpected null syncState value");
                        }

                        var64 = SyncState.valueOf(assetsInstanceFolder);
                    }

                    ContentValues var65;
                    Iterator tableInstanceFolder;
                    String column;
                    if(var64 != null) {
                        var65 = new ContentValues();
                        tableInstanceFolder = valueMap.keySet().iterator();

                        while(tableInstanceFolder.hasNext()) {
                            column = (String)tableInstanceFolder.next();
                            if(column != null) {
                                var65.put(column, (String)valueMap.get(column));
                            }
                        }

                        var65.put("_form_id", v_form_id);
                        var65.put("_locale", v_locale);
                        var65.put("_savepoint_type", v_savepoint_type);
                        var65.put("_savepoint_timestamp", v_savepoint_timestamp);
                        var65.put("_savepoint_creator", v_savepoint_creator);
                        var65.put("_row_etag", v_row_etag);
                        var65.put("_filter_type", v_filter_type);
                        var65.put("_filter_value", v_filter_value);
                        var65.put("_sync_state", SyncState.new_row.name());
                        var65.putNull("_conflict_type");
                        if(v_id != null) {
                            var65.put("_id", v_id);
                        }

                        if(var64 == SyncState.new_row) {
                            this.supervisor.getDatabase().privilegedDeleteRowWithId(this.appName, db, tableId, e, v_id);
                            this.supervisor.getDatabase().privilegedInsertRowWithId(this.appName, db, tableId, e, var65, v_id, true);
                        }
                    } else {
                        var65 = new ContentValues();
                        tableInstanceFolder = valueMap.keySet().iterator();

                        while(tableInstanceFolder.hasNext()) {
                            column = (String)tableInstanceFolder.next();
                            if(column != null) {
                                var65.put(column, (String)valueMap.get(column));
                            }
                        }

                        var65.put("_form_id", v_form_id);
                        var65.put("_locale", v_locale);
                        var65.put("_savepoint_type", v_savepoint_type);
                        var65.put("_savepoint_timestamp", v_savepoint_timestamp);
                        var65.put("_savepoint_creator", v_savepoint_creator);
                        var65.put("_row_etag", v_row_etag);
                        var65.put("_filter_type", v_filter_type);
                        var65.put("_filter_value", v_filter_value);
                        var65.put("_sync_state", SyncState.new_row.name());
                        var65.putNull("_conflict_type");
                        if(v_id == null) {
                            v_id = LocalizationUtils.genUUID();
                        }

                        var65.put("_id", v_id);
                        this.supervisor.getDatabase().privilegedInsertRowWithId(this.appName, db, tableId, e, var65, v_id, true);
                    }

                    File var66 = new File(ODKFileUtils.getAssetsCsvInstanceFolder(this.appName, tableId, v_id));
                    if(instancesHavingData.contains(var66)) {
                        File var67 = new File(ODKFileUtils.getInstanceFolder(this.appName, tableId, v_id));
                        var67.mkdirs();
                        FileUtils.copyDirectory(var66, var67);
                        instancesHavingData.remove(var66);
                    }
                }
            } catch (IOException var56) {
                //instancesHavingData = false;
            } finally {
                try {
                    var61.close();
                } catch (IOException var54) {
                    ;
                }

            }
        } catch (IOException var58) {
            boolean input = false;
            return input;
        } finally {
            if(db != null) {
                this.supervisor.getDatabase().closeDatabase(this.appName, db);
            }

        }

        return false; //whatever we won't use that class
    }
}
