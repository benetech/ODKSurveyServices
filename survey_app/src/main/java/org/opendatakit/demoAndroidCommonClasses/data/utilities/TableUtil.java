package org.opendatakit.demoAndroidCommonClasses.data.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidCommonClasses.data.TableViewType;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.KeyValueStoreUtils;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidlibraryClasses.utilities.LocalizationUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.NameUtil;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator.IStaticFieldManipulator;

public class TableUtil {
    public static final String DEFAULT_KEY_SORT_ORDER = "ASC";
    public static final TableViewType DEFAULT_KEY_CURRENT_VIEW_TYPE;
    private static TableUtil tableUtil;

    public static TableUtil get() {
        return tableUtil;
    }

    public static void set(TableUtil util) {
        tableUtil = util;
    }

    protected TableUtil() {
    }

    public boolean isTableLocked(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList lockedList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "security", "locked", (String)null).getEntries();
        if(lockedList.size() == 0) {
            return false;
        } else if(lockedList.size() != 1) {
            throw new IllegalStateException("should be impossible");
        } else {
            Boolean outcome = KeyValueStoreUtils.getBoolean(appName, (KeyValueStoreEntry)lockedList.get(0));
            return outcome.booleanValue();
        }
    }

    public boolean canAddRowToTable(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        String rolesList = ctxt.getDatabase().getRolesList(appName);
        if(rolesList == null || !rolesList.contains("ROLE_ADMINISTER_TABLES") && !rolesList.contains("ROLE_SUPER_USER_TABLES")) {
            if(this.isTableLocked(ctxt, appName, db, tableId)) {
                return false;
            } else if(rolesList != null && rolesList.contains("ROLE_USER")) {
                return true;
            } else {
                ArrayList anonAddList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "security", "unverifiedUserCanCreate", (String)null).getEntries();
                if(anonAddList.size() == 0) {
                    return true;
                } else if(anonAddList.size() != 1) {
                    throw new IllegalStateException("should be impossible");
                } else {
                    Boolean outcome = KeyValueStoreUtils.getBoolean(appName, (KeyValueStoreEntry)anonAddList.get(0));
                    return outcome.booleanValue();
                }
            }
        } else {
            return true;
        }
    }

    public String getLocalizedDisplayName(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        String rawDisplayName = this.getRawDisplayName(ctxt, appName, db, tableId);
        String displayName = null;
        if(rawDisplayName != null) {
            displayName = LocalizationUtils.getLocalizedDisplayName(rawDisplayName);
        }

        if(displayName == null) {
            displayName = NameUtil.constructSimpleDisplayName(tableId);
        }

        return displayName;
    }

    public String getRawDisplayName(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList displayNameList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "displayName", (String)null).getEntries();
        if(displayNameList.size() != 1) {
            return NameUtil.normalizeDisplayName(NameUtil.constructSimpleDisplayName(tableId));
        } else {
            String jsonDisplayName = ((KeyValueStoreEntry)displayNameList.get(0)).value;
            if(jsonDisplayName == null) {
                jsonDisplayName = NameUtil.normalizeDisplayName(NameUtil.constructSimpleDisplayName(tableId));
            }

            return jsonDisplayName;
        }
    }

    public void setRawDisplayName(CommonApplication ctxt, String appName, DbHandle db, String tableId, String rawDisplayName) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "displayName", ElementDataType.object, rawDisplayName);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public String atomicSetRawDisplayName(CommonApplication ctxt, String appName, String tableId, String rawDisplayName) throws ServicesAvailabilityException {
        DbHandle db = null;

        String e;
        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setRawDisplayName(ctxt, appName, db, tableId, rawDisplayName);
            if(rawDisplayName == null || rawDisplayName.length() == 0) {
                rawDisplayName = NameUtil.normalizeDisplayName(NameUtil.constructSimpleDisplayName(tableId));
            }

            e = rawDisplayName;
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

        return e;
    }

    public TableViewType getDefaultViewType(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "defaultViewType", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return DEFAULT_KEY_CURRENT_VIEW_TYPE;
        } else {
            String rawViewType = ((KeyValueStoreEntry)kvsList.get(0)).value;
            if(rawViewType == null) {
                return DEFAULT_KEY_CURRENT_VIEW_TYPE;
            } else {
                try {
                    TableViewType e = TableViewType.valueOf(rawViewType);
                    return e;
                } catch (Exception var8) {
                    return DEFAULT_KEY_CURRENT_VIEW_TYPE;
                }
            }
        }
    }

    public void setDefaultViewType(CommonApplication ctxt, String appName, DbHandle db, String tableId, TableViewType viewType) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "defaultViewType", ElementDataType.string, viewType.name());
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetDefaultViewType(CommonApplication ctxt, String appName, String tableId, TableViewType viewType) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setDefaultViewType(ctxt, appName, db, tableId, viewType);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public String getDetailViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "detailViewFileName", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue;
        }
    }

    public void setDetailViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId, String detailViewRelativePath) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "detailViewFileName", ElementDataType.configpath, detailViewRelativePath);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetDetailViewFilename(CommonApplication ctxt, String appName, String tableId, String detailViewRelativePath) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setDetailViewFilename(ctxt, appName, db, tableId, detailViewRelativePath);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public String getListViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "listViewFileName", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue;
        }
    }

    public void setListViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId, String listViewRelativePath) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "listViewFileName", ElementDataType.configpath, listViewRelativePath);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetListViewFilename(CommonApplication ctxt, String appName, String tableId, String listViewRelativePath) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setListViewFilename(ctxt, appName, db, tableId, listViewRelativePath);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public String getMapListViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "mapListViewFileName", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue;
        }
    }

    public void setMapListViewFilename(CommonApplication ctxt, String appName, DbHandle db, String tableId, String mapListViewRelativePath) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "mapListViewFileName", ElementDataType.configpath, mapListViewRelativePath);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetMapListViewFilename(CommonApplication ctxt, String appName, String tableId, String mapListViewRelativePath) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setMapListViewFilename(ctxt, appName, db, tableId, mapListViewRelativePath);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public TableUtil.MapViewColorRuleInfo getMapListViewColorRuleInfo(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "TableMapFragment", "default", (String)null, (String)null).getEntries();
        String colorType = null;
        String colorColumnElementKey = null;
        Iterator info = kvsList.iterator();

        while(info.hasNext()) {
            KeyValueStoreEntry entry = (KeyValueStoreEntry)info.next();
            if(entry.key.equals("keyColorRuleType")) {
                colorType = KeyValueStoreUtils.getString(appName, entry);
            } else if(entry.key.equals("keyColorRuleColumn")) {
                colorColumnElementKey = KeyValueStoreUtils.getString(appName, entry);
            }
        }

        if(colorType != null && !colorType.equals("Table Color Rules") && !colorType.equals("Status Column Color Rules")) {
            colorType = "None";
        }

        TableUtil.MapViewColorRuleInfo info1 = new TableUtil.MapViewColorRuleInfo(colorType, colorColumnElementKey);
        return info1;
    }

    public void setMapListViewColorRuleInfo(CommonApplication ctxt, String appName, DbHandle db, String tableId, TableUtil.MapViewColorRuleInfo info) throws ServicesAvailabilityException {
        KeyValueStoreEntry entryColorElementKey = KeyValueStoreUtils.buildEntry(tableId, "TableMapFragment", "default", "keyColorRuleColumn", ElementDataType.string, (String)null);
        KeyValueStoreEntry entryColorRuleType = KeyValueStoreUtils.buildEntry(tableId, "TableMapFragment", "default", "keyColorRuleType", ElementDataType.string, info == null?null:info.colorType);
        ctxt.getDatabase().replaceTableMetadata(appName, db, entryColorElementKey);
        ctxt.getDatabase().replaceTableMetadata(appName, db, entryColorRuleType);
    }

    public String getMapListViewLatitudeElementKey(CommonApplication ctxt, String appName, DbHandle db, String tableId, OrderedColumns orderedDefns) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "TableMapFragment", "default", "keyMapLatCol", (String)null).getEntries();
        String rawValue = null;
        if(kvsList.size() == 1) {
            rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
        }

        if(rawValue == null) {
            ArrayList geoPointCols = orderedDefns.getGeopointColumnDefinitions();
            Iterator i$ = orderedDefns.getColumnDefinitions().iterator();

            while(i$.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition)i$.next();
                if(orderedDefns.isLatitudeColumnDefinition(geoPointCols, cd)) {
                    rawValue = cd.getElementKey();
                    break;
                }
            }
        }

        return rawValue;
    }

    public String getMapListViewLongitudeElementKey(CommonApplication ctxt, String appName, DbHandle db, String tableId, OrderedColumns orderedDefns) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "TableMapFragment", "default", "keyMapLongCol", (String)null).getEntries();
        String rawValue = null;
        if(kvsList.size() == 1) {
            rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
        }

        if(rawValue == null) {
            ArrayList geoPointCols = orderedDefns.getGeopointColumnDefinitions();
            Iterator i$ = orderedDefns.getColumnDefinitions().iterator();

            while(i$.hasNext()) {
                ColumnDefinition cd = (ColumnDefinition)i$.next();
                if(orderedDefns.isLongitudeColumnDefinition(geoPointCols, cd)) {
                    rawValue = cd.getElementKey();
                    break;
                }
            }
        }

        return rawValue;
    }

    public String getSortColumn(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "sortCol", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue;
        }
    }

    public void setSortColumn(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "sortCol", ElementDataType.string, elementKey);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetSortColumn(CommonApplication ctxt, String appName, String tableId, String elementKey) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setSortColumn(ctxt, appName, db, tableId, elementKey);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public String getSortOrder(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "sortOrder", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue == null?"ASC":rawValue;
        }
    }

    public void setSortOrder(CommonApplication ctxt, String appName, DbHandle db, String tableId, String sortOrder) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "sortOrder", ElementDataType.string, sortOrder);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetSortOrder(CommonApplication ctxt, String appName, String tableId, String sortOrder) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setSortOrder(ctxt, appName, db, tableId, sortOrder);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public String getIndexColumn(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "indexCol", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return null;
        } else {
            String rawValue = KeyValueStoreUtils.getString(appName, (KeyValueStoreEntry)kvsList.get(0));
            return rawValue;
        }
    }

    public void setIndexColumn(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "indexCol", ElementDataType.string, elementKey);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetIndexColumn(CommonApplication ctxt, String appName, String tableId, String elementKey) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setIndexColumn(ctxt, appName, db, tableId, elementKey);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public int getSpreadsheetViewFontSize(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "SpreadsheetView", "default", "fontSize", (String)null).getEntries();
        Integer fontSize = null;
        if(kvsList.size() == 1) {
            fontSize = KeyValueStoreUtils.getInteger(appName, (KeyValueStoreEntry)kvsList.get(0));
        }

        if(fontSize == null) {
            PropertiesSingleton props = CommonToolProperties.get(ctxt, appName);
            Integer fs = props.getIntegerProperty("common.font_size");
            fontSize = Integer.valueOf(fs == null?16:fs.intValue());
        }

        return fontSize.intValue();
    }

    public void setSpreadsheetViewFontSize(CommonApplication ctxt, String appName, DbHandle db, String tableId, Integer fontSize) throws ServicesAvailabilityException {
        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "SpreadsheetView", "default", "fontSize", ElementDataType.integer, Integer.toString(fontSize.intValue()));
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetSpreadsheetViewFontSize(CommonApplication ctxt, String appName, String tableId, Integer fontSize) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setSpreadsheetViewFontSize(ctxt, appName, db, tableId, fontSize);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public ArrayList<String> getGroupByColumns(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "groupByCols", (String)null).getEntries();
        if(kvsList.size() != 1) {
            return new ArrayList();
        } else {
            ArrayList rawValue = KeyValueStoreUtils.getArray(appName, (KeyValueStoreEntry)kvsList.get(0), String.class);
            return rawValue == null?new ArrayList():rawValue;
        }
    }

    public void setGroupByColumns(CommonApplication ctxt, String appName, DbHandle db, String tableId, ArrayList<String> elementKeys) throws ServicesAvailabilityException {
        String list = null;

        try {
            list = ODKFileUtils.mapper.writeValueAsString(elementKeys);
        } catch (JsonProcessingException var8) {
            var8.printStackTrace();
            throw new IllegalArgumentException("Unexpected groupByCols conversion failure!");
        }

        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "groupByCols", ElementDataType.array, list);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicAddGroupByColumn(CommonApplication ctxt, String appName, String tableId, String elementKey) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            ArrayList e = this.getGroupByColumns(ctxt, appName, db, tableId);
            e.remove(elementKey);
            e.add(elementKey);
            this.setGroupByColumns(ctxt, appName, db, tableId, e);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public void atomicRemoveGroupByColumn(CommonApplication ctxt, String appName, String tableId, String elementKey) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            ArrayList e = this.getGroupByColumns(ctxt, appName, db, tableId);
            e.remove(elementKey);
            this.setGroupByColumns(ctxt, appName, db, tableId, e);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public ArrayList<String> getColumnOrder(CommonApplication ctxt, String appName, DbHandle db, String tableId, OrderedColumns columns) throws ServicesAvailabilityException {
        ArrayList kvsList = ctxt.getDatabase().getTableMetadata(appName, db, tableId, "Table", "default", "colOrder", (String)null).getEntries();
        ArrayList rawValue = null;
        if(kvsList.size() == 1) {
            rawValue = KeyValueStoreUtils.getArray(appName, (KeyValueStoreEntry)kvsList.get(0), String.class);
        }

        if(rawValue == null || rawValue.size() == 0) {
            rawValue = new ArrayList(columns.getRetentionColumnNames());
        }

        return rawValue;
    }

    public void setColumnOrder(CommonApplication ctxt, String appName, DbHandle db, String tableId, ArrayList<String> elementKeys) throws ServicesAvailabilityException {
        String list = null;

        try {
            list = ODKFileUtils.mapper.writeValueAsString(elementKeys);
        } catch (JsonProcessingException var8) {
            var8.printStackTrace();
            throw new IllegalArgumentException("Unexpected columnOrder conversion failure!");
        }

        KeyValueStoreEntry e = KeyValueStoreUtils.buildEntry(tableId, "Table", "default", "colOrder", ElementDataType.array, list);
        ctxt.getDatabase().replaceTableMetadata(appName, db, e);
    }

    public void atomicSetColumnOrder(CommonApplication ctxt, String appName, String tableId, ArrayList<String> elementKeys) throws ServicesAvailabilityException {
        DbHandle db = null;

        try {
            db = ctxt.getDatabase().openDatabase(appName);
            this.setColumnOrder(ctxt, appName, db, tableId, elementKeys);
        } catch (ServicesAvailabilityException var14) {
            WebLogger.getLogger(appName).printStackTrace(var14);
            throw var14;
        } finally {
            if(db != null) {
                try {
                    ctxt.getDatabase().closeDatabase(appName, db);
                } catch (ServicesAvailabilityException var13) {
                    WebLogger.getLogger(appName).printStackTrace(var13);
                    throw var13;
                }
            }

        }

    }

    public TableUtil.TableColumns getTableColumns(CommonApplication ctxt, String appName, DbHandle db, String tableId) throws ServicesAvailabilityException {
        String[] adminColumns = ctxt.getDatabase().getAdminColumns();
        HashMap colDisplayNames = new HashMap();
        OrderedColumns orderedDefns = ctxt.getDatabase().getUserDefinedColumns(appName, db, tableId);
        Iterator i$ = orderedDefns.getColumnDefinitions().iterator();

        while(i$.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition)i$.next();
            if(cd.isUnitOfRetention()) {
                String localizedDisplayName = ColumnUtil.get().getLocalizedDisplayName(ctxt, appName, db, tableId, cd.getElementKey());
                colDisplayNames.put(cd.getElementKey(), localizedDisplayName);
            }
        }

        return new TableUtil.TableColumns(orderedDefns, adminColumns, colDisplayNames);
    }

    static {
        DEFAULT_KEY_CURRENT_VIEW_TYPE = TableViewType.SPREADSHEET;
        tableUtil = new TableUtil();
        StaticStateManipulator.get().register(50, new IStaticFieldManipulator() {
            public void reset() {
                TableUtil.tableUtil = new TableUtil();
            }
        });
    }

    public static class TableColumns {
        public final OrderedColumns orderedDefns;
        public final String[] adminColumns;
        public final Map<String, String> localizedDisplayNames;

        TableColumns(OrderedColumns orderedDefns, String[] adminColumns, Map<String, String> localizedDisplayNames) {
            this.orderedDefns = orderedDefns;
            this.adminColumns = adminColumns;
            this.localizedDisplayNames = localizedDisplayNames;
        }
    }

    public static class MapViewColorRuleInfo {
        public final String colorType;
        public final String colorElementKey;

        public MapViewColorRuleInfo(String colorType, String colorElementKey) {
            this.colorType = colorType;
            this.colorElementKey = colorElementKey;
        }
    }
}
