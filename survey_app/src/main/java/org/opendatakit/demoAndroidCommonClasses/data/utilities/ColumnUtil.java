package org.opendatakit.demoAndroidCommonClasses.data.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidCommonClasses.data.JoinColumn;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.KeyValueStoreUtils;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.LocalizationUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.NameUtil;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator.IStaticFieldManipulator;

public class ColumnUtil {
    private static ColumnUtil columnUtil = new ColumnUtil();

    public static ColumnUtil get() {
        return columnUtil;
    }

    public static void set(ColumnUtil util) {
        columnUtil = util;
    }

    protected ColumnUtil() {
    }

    public String getElementKeyFromElementPath(String elementPath) {
        String hackPath = elementPath.replace(".", "_");
        return hackPath;
    }

    public String getLocalizedDisplayName(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        String jsonDisplayName = this.getRawDisplayName(ctxt, appName, db, tableId, elementKey);
        String displayName = LocalizationUtils.getLocalizedDisplayName(jsonDisplayName);
        return displayName;
    }

    public String getRawDisplayName(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        ArrayList displayNameList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Column", elementKey, "displayName", (String)null).getEntries();
        if(displayNameList.size() != 1) {
            return NameUtil.normalizeDisplayName(NameUtil.constructSimpleDisplayName(elementKey));
        } else {
            String jsonDisplayName = KeyValueStoreUtils.getObject(appName, (KeyValueStoreEntry)displayNameList.get(0));
            if(jsonDisplayName == null) {
                jsonDisplayName = NameUtil.normalizeDisplayName(NameUtil.constructSimpleDisplayName(elementKey));
            }

            return jsonDisplayName;
        }
    }

    public ArrayList<Map<String, Object>> getDisplayChoicesList(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        ArrayList choicesListList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Column", elementKey, "displayChoicesList", (String)null).getEntries();
        if(choicesListList.size() != 1) {
            return new ArrayList();
        } else {
            String choiceListId = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)choicesListList.get(0));
            if(choiceListId != null && choiceListId.trim().length() != 0) {
                String choiceListJSON = ctxt.getDatabase().getChoiceList(appName, db, choiceListId);
                if(choiceListJSON != null && choiceListJSON.trim().length() != 0) {
                    CollectionType javaType = ODKFileUtils.mapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
                    ArrayList result = null;

                    try {
                        result = (ArrayList)ODKFileUtils.mapper.readValue(choiceListJSON, javaType);
                    } catch (JsonParseException var15) {
                        WebLogger.getLogger(appName).e("ColumnUtil", "getDisplayChoicesList: problem parsing json list entry from the kvs");
                        WebLogger.getLogger(appName).printStackTrace(var15);
                    } catch (JsonMappingException var16) {
                        WebLogger.getLogger(appName).e("ColumnUtil", "getDisplayChoicesList: problem mapping json list entry from the kvs");
                        WebLogger.getLogger(appName).printStackTrace(var16);
                    } catch (IOException var17) {
                        WebLogger.getLogger(appName).e("ColumnUtil", "getDisplayChoicesList: i/o problem with json for list entry from the kvs");
                        WebLogger.getLogger(appName).printStackTrace(var17);
                    }

                    if(result == null) {
                        return new ArrayList();
                    } else {
                        ArrayList jsonDisplayChoices = new ArrayList();
                        Iterator i$ = result.iterator();

                        while(i$.hasNext()) {
                            Map m = (Map)i$.next();
                            jsonDisplayChoices.add(m);
                        }

                        return jsonDisplayChoices;
                    }
                } else {
                    return new ArrayList();
                }
            } else {
                return new ArrayList();
            }
        }
    }

    public void setDisplayChoicesList(CommonApplication ctxt, String appName, DbHandle db, String tableId, ColumnDefinition cd, ArrayList<Map<String, Object>> choices) throws ServicesAvailabilityException {
        String choiceListJSON = null;

        try {
            choiceListJSON = ODKFileUtils.mapper.writeValueAsString(choices);
        } catch (JsonProcessingException var10) {
            var10.printStackTrace();
            throw new IllegalArgumentException("Unexpected displayChoices conversion failure!");
        }

        String choiceListId = ctxt.getDatabase().setChoiceList(appName, db, choiceListJSON);
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Column", cd.getElementKey(), "displayChoicesList", ElementDataType.string, choiceListId);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public ArrayList<JoinColumn> getJoins(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        ArrayList joinsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Column", elementKey, "joins", (String)null).getEntries();
        if(joinsList.size() != 1) {
            return new ArrayList();
        } else {
            ArrayList joins = null;

            try {
                joins = JoinColumn.fromSerialization(KeyValueStoreUtils.getObject(appName, (KeyValueStoreEntry)joinsList.get(0)));
            } catch (JsonParseException var9) {
                var9.printStackTrace();
            } catch (JsonMappingException var10) {
                var10.printStackTrace();
            } catch (IOException var11) {
                var11.printStackTrace();
            }

            return joins == null?new ArrayList():joins;
        }
    }

    public int getColumnWidth(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "SpreadsheetView", elementKey, "SpreadsheetView.columnWidth", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return 125;
        } else {
            Integer value = KeyValueStoreUtils.getInteger(appName, (KeyValueStoreEntry)kvsList.get(0));
            return value != null && value.intValue() > 0?(value.intValue() > 1000?1000:value.intValue()):125;
        }
    }

    public void setColumnWidth(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey, Integer width) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "SpreadsheetView", elementKey, "SpreadsheetView.columnWidth", ElementDataType.integer, width == null?null:Integer.toString(width.intValue()));
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetColumnWidth(CommonApplication ctxt, String appName, String tableId, String elementKey, Integer width) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setColumnWidth(ctxt, appName, db, tableId, elementKey, width);
        } catch (ServicesAvailabilityException var15) {
            WebLogger.getLogger(appName).printStackTrace(var15);
            throw var15;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var14) {
                    WebLogger.getLogger(appName).printStackTrace(var14);
                    throw var14;
                }
            }

        }

    }

    public Map<String, Integer> getColumnWidths(CommonApplication ctxt, String appName, DbHandle db, String tableId, OrderedColumns columns) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "SpreadsheetView", (String)null, "SpreadsheetView.columnWidth", (String)null).getEntries();
        HashMap colWidths = new HashMap();

        Iterator i$;
        KeyValueStoreEntry cd;
        Integer value;
        for(i$ = kvsList.iterator(); i$.hasNext(); colWidths.put(cd.aspect, value)) {
            cd = (KeyValueStoreEntry)i$.next();
            value = KeyValueStoreUtils.getInteger(appName, cd);
            if(value == null || value.intValue() <= 0) {
                value = Integer.valueOf(125);
            }

            if(value.intValue() > 1000) {
                value = Integer.valueOf(1000);
            }
        }

        i$ = columns.getColumnDefinitions().iterator();

        while(i$.hasNext()) {
            ColumnDefinition cd1 = (ColumnDefinition)i$.next();
            if(!colWidths.containsKey(cd1.getElementKey())) {
                colWidths.put(cd1.getElementKey(), Integer.valueOf(125));
            }
        }

        return colWidths;
    }

    public Class<?> getOdkDataIfType(ElementDataType dataType) {
        return dataType == ElementDataType.integer?Long.class:(dataType == ElementDataType.number?Double.class:(dataType == ElementDataType.bool?Boolean.class:String.class));
    }

    static {
        StaticStateManipulator.get().register(50, new IStaticFieldManipulator() {
            public void reset() {
                ColumnUtil.columnUtil = new ColumnUtil();
            }
        });
    }
}
