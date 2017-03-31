package org.opendatakit.demoAndroidlibraryClasses.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.RFC4180CsvReader;
import org.opendatakit.aggregate.odktables.rest.RFC4180CsvWriter;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.KeyValueStoreUtils;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public final class PropertiesFileUtils {
    private static final String TAG = PropertiesFileUtils.class.getSimpleName();

    public PropertiesFileUtils() {
    }

    public static synchronized boolean writePropertiesIntoCsv(String appName, String tableId, OrderedColumns orderedDefns, List<KeyValueStoreEntry> kvsEntries, File definitionCsv, File propertiesCsv) throws ServicesAvailabilityException {
        WebLogger.getLogger(appName).i(TAG, "writePropertiesIntoCsv: tableId: " + tableId);
        OutputStreamWriter output = null;

        boolean colDefRow;
        try {
            FileOutputStream out = new FileOutputStream(definitionCsv);
            output = new OutputStreamWriter(out, "UTF-8");
            RFC4180CsvWriter cw = new RFC4180CsvWriter(output);
            ArrayList e = new ArrayList();
            e.add("_element_key");
            e.add("_element_name");
            e.add("_element_type");
            e.add("_list_child_element_keys");
            cw.writeNext((String[])e.toArray(new String[e.size()]));
            String[] var25 = new String[e.size()];
            Iterator e1 = orderedDefns.getColumnDefinitions().iterator();

            while(e1.hasNext()) {
                ColumnDefinition kvsRow = (ColumnDefinition)e1.next();
                var25[0] = kvsRow.getElementKey();
                var25[1] = kvsRow.getElementName();
                var25[2] = kvsRow.getElementType();
                var25[3] = kvsRow.getListChildElementKeys();
                cw.writeNext(var25);
            }

            cw.flush();
            cw.close();
            out = new FileOutputStream(propertiesCsv);
            output = new OutputStreamWriter(out, "UTF-8");
            cw = new RFC4180CsvWriter(output);
            ArrayList var26 = new ArrayList();
            var26.add("_partition");
            var26.add("_aspect");
            var26.add("_key");
            var26.add("_type");
            var26.add("_value");
            Collections.sort(kvsEntries, new Comparator() {
                @Override
                public int compare(Object lhs1, Object rhs1) {
                    KeyValueStoreEntry lhs = (KeyValueStoreEntry)lhs1;
                    KeyValueStoreEntry rhs = (KeyValueStoreEntry)rhs1;
                    int outcome;
                    if(lhs.partition == null && rhs.partition == null) {
                        outcome = 0;
                    } else {
                        if(lhs.partition == null) {
                            return -1;
                        }

                        if(rhs.partition == null) {
                            return 1;
                        }

                        outcome = lhs.partition.compareTo(rhs.partition);
                    }

                    if(outcome != 0) {
                        return outcome;
                    } else {
                        if(lhs.aspect == null && rhs.aspect == null) {
                            outcome = 0;
                        } else {
                            if(lhs.aspect == null) {
                                return -1;
                            }

                            if(rhs.aspect == null) {
                                return 1;
                            }

                            outcome = lhs.aspect.compareTo(rhs.aspect);
                        }

                        if(outcome != 0) {
                            return outcome;
                        } else {
                            if(lhs.key == null && rhs.key == null) {
                                outcome = 0;
                            } else {
                                if(lhs.key == null) {
                                    return -1;
                                }

                                if(rhs.key == null) {
                                    return 1;
                                }

                                outcome = lhs.key.compareTo(rhs.key);
                            }

                            return outcome;
                        }
                    }
                }
            });
            cw.writeNext((String[])var26.toArray(new String[var26.size()]));
            String[] var27 = new String[var26.size()];

            for(int i = 0; i < kvsEntries.size(); ++i) {
                KeyValueStoreEntry e2 = (KeyValueStoreEntry)kvsEntries.get(i);
                var27[0] = e2.partition;
                var27[1] = e2.aspect;
                var27[2] = e2.key;
                var27[3] = e2.type;
                var27[4] = e2.value;
                cw.writeNext(var27);
            }

            cw.flush();
            cw.close();
            boolean var28 = true;
            return var28;
        } catch (IOException var23) {
            colDefRow = false;
        } finally {
            try {
                output.close();
            } catch (IOException var22) {
                ;
            }

        }

        return colDefRow;
    }

    private static int countUpToLastNonNullElement(String[] row) {
        for(int i = row.length - 1; i >= 0; --i) {
            if(row[i] != null) {
                return i + 1;
            }
        }

        return 0;
    }

    public static synchronized PropertiesFileUtils.DataTableDefinition readPropertiesFromCsv(String appName, String tableId) throws IOException {
        WebLogger.getLogger(appName).i(TAG, "readPropertiesFromCsv: tableId: " + tableId);
        ArrayList columns = new ArrayList();
        ArrayList kvsEntries = new ArrayList();
        File file = null;
        FileInputStream in = null;
        InputStreamReader input = null;
        RFC4180CsvReader cr = null;

        try {
            file = new File(ODKFileUtils.getTableDefinitionCsvFile(appName, tableId));
            in = new FileInputStream(file);
            input = new InputStreamReader(in, "UTF-8");
            cr = new RFC4180CsvReader(input);
            String[] colHeaders = cr.readNext();
            int colHeadersLength = countUpToLastNonNullElement(colHeaders);
            String[] dtd = cr.readNext();

            while(true) {
                String e;
                String aspect;
                String key;
                if(dtd == null || countUpToLastNonNullElement(dtd) == 0) {
                    cr.close();

                    try {
                        input.close();
                    } catch (IOException var32) {
                        ;
                    }

                    try {
                        in.close();
                    } catch (IOException var31) {
                        ;
                    }

                    file = new File(ODKFileUtils.getTablePropertiesCsvFile(appName, tableId));
                    in = new FileInputStream(file);
                    input = new InputStreamReader(in, "UTF-8");
                    cr = new RFC4180CsvReader(input);
                    String[] var35 = cr.readNext();

                    for(dtd = cr.readNext(); dtd != null && countUpToLastNonNullElement(dtd) != 0; dtd = cr.readNext()) {
                        e = null;
                        aspect = null;
                        key = null;
                        String var36 = null;
                        String var37 = null;
                        int rowLength = countUpToLastNonNullElement(dtd);

                        for(int kvsEntry = 0; kvsEntry < rowLength; ++kvsEntry) {
                            if("_partition".equals(var35[kvsEntry])) {
                                e = dtd[kvsEntry];
                            }

                            if("_aspect".equals(var35[kvsEntry])) {
                                aspect = dtd[kvsEntry];
                            }

                            if("_key".equals(var35[kvsEntry])) {
                                key = dtd[kvsEntry];
                            }

                            if("_type".equals(var35[kvsEntry])) {
                                var36 = dtd[kvsEntry];
                            }

                            if("_value".equals(var35[kvsEntry])) {
                                var37 = dtd[kvsEntry];
                            }
                        }

                        KeyValueStoreEntry var38 = KeyValueStoreUtils.buildEntry(tableId, e, aspect, key, ElementDataType.valueOf(var36), var37);
                        kvsEntries.add(var38);
                    }

                    cr.close();

                    try {
                        input.close();
                    } catch (IOException var30) {
                        ;
                    }

                    try {
                        in.close();
                    } catch (IOException var29) {
                        ;
                    }
                    break;
                }

                String kvsHeaders = null;
                e = null;
                aspect = null;
                key = null;
                int type = countUpToLastNonNullElement(dtd);

                for(int value = 0; value < type; ++value) {
                    if(value >= colHeadersLength) {
                        throw new IllegalStateException("data beyond header row of ColumnDefinitions table");
                    }

                    if("_element_key".equals(colHeaders[value])) {
                        kvsHeaders = dtd[value];
                    }

                    if("_element_name".equals(colHeaders[value])) {
                        e = dtd[value];
                    }

                    if("_element_type".equals(colHeaders[value])) {
                        aspect = dtd[value];
                    }

                    if("_list_child_element_keys".equals(colHeaders[value])) {
                        key = dtd[value];
                    }
                }

                if(kvsHeaders == null || aspect == null) {
                    throw new IllegalStateException("ElementKey and ElementType must be specified");
                }

                columns.add(new Column(kvsHeaders, e, aspect, key));
                dtd = cr.readNext();
            }
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException var28) {
                ;
            }

        }

        PropertiesFileUtils.DataTableDefinition var34 = new PropertiesFileUtils.DataTableDefinition();
        var34.columnList = new ColumnList(columns);
        var34.kvsEntries = kvsEntries;
        return var34;
    }

    public static class DataTableDefinition {
        public ColumnList columnList;
        public List<KeyValueStoreEntry> kvsEntries;

        public DataTableDefinition() {
        }
    }
}
