package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.support.annotation.NonNull;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.ElementType;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.NameUtil;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class ColumnDefinition implements Comparable<ColumnDefinition> {
    private static final String TAG = "ColumnDefinition";
    private static final String JSON_SCHEMA_NOT_UNIT_OF_RETENTION = "notUnitOfRetention";
    private static final String JSON_SCHEMA_IS_NOT_NULLABLE = "isNotNullable";
    private static final String JSON_SCHEMA_ELEMENT_SET = "elementSet";
    private static final String JSON_SCHEMA_INSTANCE_METADATA_VALUE = "instanceMetadata";
    private static final String JSON_SCHEMA_INSTANCE_DATA_VALUE = "data";
    private static final String JSON_SCHEMA_DEFAULT = "default";
    private static final String JSON_SCHEMA_ELEMENT_KEY = "elementKey";
    private static final String JSON_SCHEMA_ELEMENT_PATH = "elementPath";
    private static final String JSON_SCHEMA_ELEMENT_NAME = "elementName";
    private static final String JSON_SCHEMA_ELEMENT_TYPE = "elementType";
    private static final String JSON_SCHEMA_LIST_CHILD_ELEMENT_KEYS = "listChildElementKeys";
    private static final String JSON_SCHEMA_PROPERTIES = "properties";
    private static final String JSON_SCHEMA_ITEMS = "items";
    private static final String JSON_SCHEMA_TYPE = "type";
    private final Column column;
    private boolean isUnitOfRetention = true;
    final ArrayList<ColumnDefinition> children = new ArrayList();
    ElementType type = null;
    ColumnDefinition parent = null;

    ColumnDefinition(String elementKey, String elementName, String elementType, String listChildElementKeys) {
        this.column = new Column(elementKey, elementName, elementType, listChildElementKeys);
    }

    public String getElementKey() {
        return this.column.getElementKey();
    }

    public String getElementName() {
        return this.column.getElementName();
    }

    public String getElementType() {
        return this.column.getElementType();
    }

    public synchronized ElementType getType() {
        if(this.type == null) {
            this.type = ElementType.parseElementType(this.getElementType(), !this.getChildren().isEmpty());
        }

        return this.type;
    }

    public String getListChildElementKeys() {
        return this.column.getListChildElementKeys();
    }

    private void setParent(ColumnDefinition parent) {
        this.parent = parent;
    }

    public ColumnDefinition getParent() {
        return this.parent;
    }

    public void addChild(ColumnDefinition child) {
        child.setParent(this);
        this.children.add(child);
    }

    public List<ColumnDefinition> getChildren() {
        return Collections.unmodifiableList(this.children);
    }

    public boolean isUnitOfRetention() {
        return this.isUnitOfRetention;
    }

    void setNotUnitOfRetention() {
        this.isUnitOfRetention = false;
    }

    public String toString() {
        return this.column.toString();
    }

    public int hashCode() {
        return this.column.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj != null && obj instanceof ColumnDefinition) {
            ColumnDefinition o = (ColumnDefinition)obj;
            return this.column.equals(o.column);
        } else {
            return false;
        }
    }

    static ColumnDefinition find(ArrayList<ColumnDefinition> orderedDefns, String elementKey) throws IllegalArgumentException {
        if(elementKey == null) {
            throw new NullPointerException("elementKey cannot be null in ColumnDefinition::find()");
        } else {
            int iLow = 0;
            int iHigh = orderedDefns.size();

            int iGuess;
            ColumnDefinition cd;
            for(iGuess = (iLow + iHigh) / 2; iLow != iHigh; iGuess = (iLow + iHigh) / 2) {
                cd = (ColumnDefinition)orderedDefns.get(iGuess);
                int cmp = elementKey.compareTo(cd.getElementKey());
                if(cmp == 0) {
                    return cd;
                }

                if(cmp < 0) {
                    iHigh = iGuess;
                } else {
                    iLow = iGuess + 1;
                }
            }

            if(iLow >= orderedDefns.size()) {
                throw new IllegalArgumentException("could not find elementKey in columns list: " + elementKey);
            } else {
                cd = (ColumnDefinition)orderedDefns.get(iGuess);
                if(cd.getElementKey().equals(elementKey)) {
                    return cd;
                } else {
                    throw new IllegalArgumentException("could not find elementKey in columns list: " + elementKey);
                }
            }
        }
    }

    static ArrayList<ColumnDefinition> buildColumnDefinitions(String appName, String tableId, List<Column> columns) {
        if(appName != null && appName.length() != 0) {
            if(tableId != null && tableId.length() != 0) {
                if(columns == null) {
                    throw new IllegalArgumentException("columns cannot be null");
                } else {
                    WebLogger.getLogger(appName).d("ColumnDefinition", "[buildColumnDefinitions] tableId: " + tableId + " size: " + columns.size() + " first column: " + (columns.isEmpty()?"<none>":((Column)columns.get(0)).getElementKey()));
                    HashMap colDefs = new HashMap();
                    ArrayList ccList = new ArrayList();

                    Iterator defns;
                    ColumnDefinition defn;
                    String i$;
                    for(defns = columns.iterator(); defns.hasNext(); colDefs.put(defn.getElementKey(), defn)) {
                        Column cc = (Column)defns.next();
                        if(!NameUtil.isValidUserDefinedDatabaseName(cc.getElementKey())) {
                            throw new IllegalArgumentException("ColumnDefinition: invalid user-defined column name: " + cc.getElementKey());
                        }

                        defn = new ColumnDefinition(cc.getElementKey(), cc.getElementName(), cc.getElementType(), cc.getListChildElementKeys());
                        ColumnDefinition.ColumnContainer type = new ColumnDefinition.ColumnContainer();
                        type.defn = defn;
                        i$ = cc.getListChildElementKeys();
                        if(i$ != null && i$.length() != 0) {
                            ArrayList child;
                            try {
                                child = (ArrayList)ODKFileUtils.mapper.readValue(i$, ArrayList.class);
                            } catch (JsonParseException var12) {
                                WebLogger.getLogger(appName).printStackTrace(var12);
                                throw new IllegalArgumentException("Invalid list of children: " + i$);
                            } catch (JsonMappingException var13) {
                                WebLogger.getLogger(appName).printStackTrace(var13);
                                throw new IllegalArgumentException("Invalid list of children: " + i$);
                            } catch (IOException var14) {
                                WebLogger.getLogger(appName).printStackTrace(var14);
                                throw new IllegalArgumentException("Invalid list of children: " + i$);
                            }

                            type.children = child;
                            ccList.add(type);
                        }
                    }

                    defns = ccList.iterator();

                    ColumnDefinition.ColumnContainer cc1;
                    ColumnDefinition child1;
                    while(defns.hasNext()) {
                        cc1 = (ColumnDefinition.ColumnContainer)defns.next();
                        defn = cc1.defn;
                        Iterator type1 = cc1.children.iterator();

                        while(type1.hasNext()) {
                            i$ = (String)type1.next();
                            child1 = (ColumnDefinition)colDefs.get(i$);
                            if(child1 == null) {
                                throw new IllegalArgumentException("Child elementkey " + i$ + " was never defined but referenced in " + defn.getElementKey() + "!");
                            }

                            defn.addChild(child1);
                        }
                    }

                    defns = ccList.iterator();

                    while(defns.hasNext()) {
                        cc1 = (ColumnDefinition.ColumnContainer)defns.next();
                        defn = cc1.defn;
                        if(defn.getChildren().size() != cc1.children.size()) {
                            throw new IllegalArgumentException("Not all children of element have been defined! " + defn.getElementKey());
                        }

                        ElementType type2 = defn.getType();
                        if(type2.getDataType() == ElementDataType.array) {
                            if(defn.getChildren().isEmpty()) {
                                throw new IllegalArgumentException("Column is an array but does not list its children");
                            }

                            if(defn.getChildren().size() != 1) {
                                throw new IllegalArgumentException("Column is an array but has more than one item entry");
                            }
                        }

                        Iterator i$1 = defn.getChildren().iterator();

                        while(i$1.hasNext()) {
                            child1 = (ColumnDefinition)i$1.next();
                            if(child1.getParent() != defn) {
                                throw new IllegalArgumentException("Column is enclosed by two or more groupings: " + defn.getElementKey());
                            }

                            if(!child1.getElementKey().equals(defn.getElementKey() + "_" + child1.getElementName())) {
                                throw new IllegalArgumentException("Children are expected to have elementKey equal to parent\'s elementKey-underscore-childElementName: " + child1.getElementKey());
                            }
                        }
                    }

                    markUnitOfRetention(colDefs);
                    ArrayList defns1 = new ArrayList(colDefs.values());
                    Collections.sort(defns1);
                    return defns1;
                }
            } else {
                throw new IllegalArgumentException("tableId cannot be null or an empty string");
            }
        } else {
            throw new IllegalArgumentException("appName cannot be null or an empty string");
        }
    }

    private static void markUnitOfRetention(Map<String, ColumnDefinition> defn) {
        Iterator i$ = defn.keySet().iterator();

        while(true) {
            ColumnDefinition colDefn;
            ElementType type;
            do {
                do {
                    String startKey;
                    if(!i$.hasNext()) {
                        i$ = defn.keySet().iterator();

                        while(i$.hasNext()) {
                            startKey = (String)i$.next();
                            colDefn = (ColumnDefinition)defn.get(startKey);
                            if(colDefn.isUnitOfRetention()) {
                                type = colDefn.getType();
                                if(ElementDataType.array != type.getDataType() && !colDefn.getChildren().isEmpty()) {
                                    colDefn.setNotUnitOfRetention();
                                }
                            }
                        }

                        return;
                    }

                    startKey = (String)i$.next();
                    colDefn = (ColumnDefinition)defn.get(startKey);
                } while(!colDefn.isUnitOfRetention());

                type = colDefn.getType();
            } while(ElementDataType.array != type.getDataType());

            ArrayList descendantsOfArray = new ArrayList(colDefn.getChildren());
            ArrayList scratchArray = new ArrayList();

            while(true) {
                Iterator i$1 = descendantsOfArray.iterator();

                while(i$1.hasNext()) {
                    ColumnDefinition subDefn = (ColumnDefinition)i$1.next();
                    if(subDefn.isUnitOfRetention()) {
                        subDefn.setNotUnitOfRetention();
                        scratchArray.addAll(subDefn.getChildren());
                    }
                }

                descendantsOfArray.clear();
                descendantsOfArray.addAll(scratchArray);
                scratchArray.clear();
                if(descendantsOfArray.isEmpty()) {
                    break;
                }
            }
        }
    }

    public static ArrayList<Column> getColumns(ArrayList<ColumnDefinition> orderedDefns) {
        ArrayList columns = new ArrayList();
        Iterator i$ = orderedDefns.iterator();

        while(i$.hasNext()) {
            ColumnDefinition col = (ColumnDefinition)i$.next();
            columns.add(col.column);
        }

        return columns;
    }

    static TreeMap<String, Object> getExtendedDataModel(List<ColumnDefinition> orderedDefns) {
        TreeMap model = getDataModel(orderedDefns);
        TreeMap jsonSchema = new TreeMap();
        model.put("_id", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("isNotNullable", Boolean.TRUE);
        jsonSchema.put("elementKey", "_id");
        jsonSchema.put("elementName", "_id");
        jsonSchema.put("elementPath", "_id");
        jsonSchema = new TreeMap();
        model.put("_row_etag", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementKey", "_row_etag");
        jsonSchema.put("elementName", "_row_etag");
        jsonSchema.put("elementPath", "_row_etag");
        jsonSchema = new TreeMap();
        model.put("_sync_state", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("isNotNullable", Boolean.TRUE);
        jsonSchema.put("elementKey", "_sync_state");
        jsonSchema.put("elementName", "_sync_state");
        jsonSchema.put("elementPath", "_sync_state");
        jsonSchema = new TreeMap();
        model.put("_conflict_type", jsonSchema);
        jsonSchema.put("type", ElementDataType.integer.name());
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementKey", "_conflict_type");
        jsonSchema.put("elementName", "_conflict_type");
        jsonSchema.put("elementPath", "_conflict_type");
        jsonSchema = new TreeMap();
        model.put("_filter_type", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementKey", "_filter_type");
        jsonSchema.put("elementName", "_filter_type");
        jsonSchema.put("elementPath", "_filter_type");
        jsonSchema = new TreeMap();
        model.put("_filter_value", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_filter_value");
        jsonSchema.put("elementName", "_filter_value");
        jsonSchema.put("elementPath", "_filter_value");
        jsonSchema = new TreeMap();
        model.put("_form_id", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_form_id");
        jsonSchema.put("elementName", "_form_id");
        jsonSchema.put("elementPath", "_form_id");
        jsonSchema = new TreeMap();
        model.put("_locale", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_locale");
        jsonSchema.put("elementName", "_locale");
        jsonSchema.put("elementPath", "_locale");
        jsonSchema = new TreeMap();
        model.put("_savepoint_type", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_savepoint_type");
        jsonSchema.put("elementName", "_savepoint_type");
        jsonSchema.put("elementPath", "_savepoint_type");
        jsonSchema = new TreeMap();
        model.put("_savepoint_timestamp", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.TRUE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_savepoint_timestamp");
        jsonSchema.put("elementName", "_savepoint_timestamp");
        jsonSchema.put("elementPath", "_savepoint_timestamp");
        jsonSchema = new TreeMap();
        model.put("_savepoint_creator", jsonSchema);
        jsonSchema.put("type", ElementDataType.string.name());
        jsonSchema.put("isNotNullable", Boolean.FALSE);
        jsonSchema.put("elementSet", "instanceMetadata");
        jsonSchema.put("elementKey", "_savepoint_creator");
        jsonSchema.put("elementName", "_savepoint_creator");
        jsonSchema.put("elementPath", "_savepoint_creator");
        return model;
    }

    static TreeMap<String, Object> getDataModel(List<ColumnDefinition> orderedDefns) {
        TreeMap model = new TreeMap();
        Iterator i$ = orderedDefns.iterator();

        while(i$.hasNext()) {
            ColumnDefinition c = (ColumnDefinition)i$.next();
            if(c.getParent() == null) {
                TreeMap jsonSchema = new TreeMap();
                model.put(c.getElementName(), jsonSchema);
                jsonSchema.put("elementPath", c.getElementName());
                getDataModelHelper(jsonSchema, c, false);
                if(!c.isUnitOfRetention()) {
                    jsonSchema.put("notUnitOfRetention", Boolean.TRUE);
                }
            }
        }

        return model;
    }

    private static void getDataModelHelper(TreeMap<String, Object> jsonSchema, ColumnDefinition c, boolean nestedInsideUnitOfRetention) {
        ElementType type = c.getType();
        ElementDataType dataType = type.getDataType();
        jsonSchema.put("elementSet", "data");
        jsonSchema.put("elementName", c.getElementName());
        jsonSchema.put("elementKey", c.getElementKey());
        if(nestedInsideUnitOfRetention) {
            jsonSchema.put("notUnitOfRetention", Boolean.TRUE);
        }

        if(dataType == ElementDataType.array) {
            jsonSchema.put("type", dataType.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }

            ColumnDefinition propertiesSchema = (ColumnDefinition)c.getChildren().get(0);
            TreeMap keys = new TreeMap();
            jsonSchema.put("items", keys);
            keys.put("elementPath", (String)jsonSchema.get("elementPath") + '.' + propertiesSchema.getElementName());
            getDataModelHelper(keys, propertiesSchema, true);
            ArrayList i$ = new ArrayList();
            i$.add(propertiesSchema.getElementKey());
            jsonSchema.put("listChildElementKeys", i$);
        } else if(dataType == ElementDataType.bool) {
            jsonSchema.put("type", dataType.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }
        } else if(dataType == ElementDataType.configpath) {
            jsonSchema.put("type", ElementDataType.string.name());
            jsonSchema.put("elementType", c.getElementType());
        } else if(dataType == ElementDataType.integer) {
            jsonSchema.put("type", dataType.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }
        } else if(dataType == ElementDataType.number) {
            jsonSchema.put("type", dataType.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }
        } else if(dataType == ElementDataType.object) {
            jsonSchema.put("type", dataType.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }

            TreeMap propertiesSchema1 = new TreeMap();
            jsonSchema.put("properties", propertiesSchema1);
            ArrayList keys1 = new ArrayList();
            Iterator i$1 = c.getChildren().iterator();

            while(i$1.hasNext()) {
                ColumnDefinition ch = (ColumnDefinition)i$1.next();
                TreeMap itemSchema = new TreeMap();
                propertiesSchema1.put(ch.getElementName(), itemSchema);
                itemSchema.put("elementPath", (String)jsonSchema.get("elementPath") + '.' + ch.getElementName());
                getDataModelHelper(itemSchema, ch, nestedInsideUnitOfRetention);
                keys1.add(ch.getElementKey());
            }

            jsonSchema.put("listChildElementKeys", keys1);
        } else if(dataType == ElementDataType.rowpath) {
            jsonSchema.put("type", ElementDataType.string.name());
            jsonSchema.put("elementType", ElementDataType.rowpath.name());
        } else {
            if(dataType != ElementDataType.string) {
                throw new IllegalStateException("unexpected alternative ElementDataType");
            }

            jsonSchema.put("type", ElementDataType.string.name());
            if(!c.getElementType().equals(dataType.name())) {
                jsonSchema.put("elementType", c.getElementType());
            }
        }

    }

    public int compareTo(@NonNull ColumnDefinition another) {
        return this.getElementKey().compareTo(another.getElementKey());
    }

    private static class ColumnContainer {
        public ColumnDefinition defn;
        public ArrayList<String> children;

        private ColumnContainer() {
            this.defn = null;
            this.children = null;
        }
    }
}
