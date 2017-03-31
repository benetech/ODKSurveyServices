package org.opendatakit.demoAndroidCommonClasses.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.ElementType;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidlibraryClasses.data.ColorRule;
import org.opendatakit.demoAndroidCommonClasses.data.utilities.ColorRuleUtil;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.KeyValueStoreUtils;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;

public class ColorRuleGroup {
    private static final String TAG = ColorRuleGroup.class.getName();
    public static final String DEFAULT_KEY_COLOR_RULES = "[]";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeFactory typeFactory;
    private List<ColorRule> ruleList;
    private ColorRuleGroup.Type mType;
    private String mAppName;
    private String mTableId;
    private String mElementKey;
    private String[] mAdminColumns;
    private boolean mDefault;

    private ColorRuleGroup(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey, ColorRuleGroup.Type type, String[] adminColumns) throws ServicesAvailabilityException {
        this.mType = type;
        this.mAppName = appName;
        this.mTableId = tableId;
        this.mElementKey = elementKey;
        String jsonRulesString = "[]";
        this.mAdminColumns = adminColumns;
        ArrayList entries = null;
        switch(mType.ordinal()) {
            case 1:
                entries = ctxt.getDatabase().getTableMetadata(appName, db, this.mTableId, "ColumnColorRuleGroup", elementKey, "ColumnColorRuleGroup.ruleList", (String)null).getEntries();
                break;
            case 2:
                entries = ctxt.getDatabase().getTableMetadata(appName, db, this.mTableId, "TableColorRuleGroup", "default", "TableColorRuleGroup.ruleList", (String)null).getEntries();
                break;
            case 3:
                entries = ctxt.getDatabase().getTableMetadata(appName, db, this.mTableId, "TableColorRuleGroup", "default", "StatusColumn.ruleList", (String)null).getEntries();
                break;
            default:
                WebLogger.getLogger(this.mAppName).e(TAG, "unrecognized ColorRuleGroup type: " + this.mType);
        }

        this.mDefault = false;
        if(entries.size() != 1) {
            if(this.mType == ColorRuleGroup.Type.STATUS_COLUMN) {
                this.ruleList = new ArrayList();
                this.ruleList.addAll(ColorRuleUtil.getDefaultSyncStateColorRules());
            } else {
                this.ruleList = new ArrayList();
            }
        } else {
            jsonRulesString = KeyValueStoreUtils.getObject(appName, (KeyValueStoreEntry)entries.get(0));
            this.ruleList = this.parseJsonString(jsonRulesString);
        }

    }

    public String[] getAdminColumns() {
        return this.mAdminColumns;
    }

    public static ColorRuleGroup getColumnColorRuleGroup(CommonApplication ctxt, String appName, DbHandle db, String tableId, String elementKey, String[] adminColumns) throws ServicesAvailabilityException {
        return new ColorRuleGroup(ctxt, appName, db, tableId, elementKey, ColorRuleGroup.Type.COLUMN, adminColumns);
    }

    public static ColorRuleGroup getTableColorRuleGroup(CommonApplication ctxt, String appName, DbHandle db, String tableId, String[] adminColumns) throws ServicesAvailabilityException {
        return new ColorRuleGroup(ctxt, appName, db, tableId, (String)null, ColorRuleGroup.Type.TABLE, adminColumns);
    }

    public static ColorRuleGroup getStatusColumnRuleGroup(CommonApplication ctxt, String appName, DbHandle db, String tableId, String[] adminColumns) throws ServicesAvailabilityException {
        return new ColorRuleGroup(ctxt, appName, db, tableId, (String)null, ColorRuleGroup.Type.STATUS_COLUMN, adminColumns);
    }

    private List<ColorRule> parseJsonString(String json) {
        if(json != null && !json.equals("")) {
            Object reclaimedRules = new ArrayList();

            try {
                reclaimedRules = (List)mapper.readValue(json, typeFactory.constructCollectionType(ArrayList.class, ColorRule.class));
            } catch (JsonParseException var4) {
                WebLogger.getLogger(this.mAppName).e(TAG, "problem parsing json to colcolorrule");
                WebLogger.getLogger(this.mAppName).printStackTrace(var4);
            } catch (JsonMappingException var5) {
                WebLogger.getLogger(this.mAppName).e(TAG, "problem mapping json to colcolorrule");
                WebLogger.getLogger(this.mAppName).printStackTrace(var5);
            } catch (IOException var6) {
                WebLogger.getLogger(this.mAppName).e(TAG, "i/o problem with json to colcolorrule");
                WebLogger.getLogger(this.mAppName).printStackTrace(var6);
            }

            return (List)reclaimedRules;
        } else {
            return new ArrayList();
        }
    }

    public List<ColorRule> getColorRules() {
        return this.ruleList;
    }

    public void replaceColorRuleList(List<ColorRule> newRules) {
        this.ruleList.clear();
        this.ruleList.addAll(newRules);
        this.mDefault = false;
    }

    public ColorRuleGroup.Type getType() {
        return this.mType;
    }

    public void saveRuleList(CommonApplication ctxt) throws ServicesAvailabilityException {
        if(!this.mDefault) {
            DbHandle db = null;

            try {
                db = ctxt.getDatabase().openDatabase(this.mAppName);

                try {
                    String e = mapper.writeValueAsString(this.ruleList);
                    KeyValueStoreEntry entry = null;
                    switch(mType.ordinal()) {
                        case 1:
                            entry = KeyValueStoreUtils.buildEntry(this.mTableId, "ColumnColorRuleGroup", this.mElementKey, "ColumnColorRuleGroup.ruleList", ElementDataType.array, e);
                            break;
                        case 2:
                            entry = KeyValueStoreUtils.buildEntry(this.mTableId, "TableColorRuleGroup", "default", "TableColorRuleGroup.ruleList", ElementDataType.array, e);
                            break;
                        case 3:
                            entry = KeyValueStoreUtils.buildEntry(this.mTableId, "TableColorRuleGroup", "default", "StatusColumn.ruleList", ElementDataType.array, e);
                    }

                    ctxt.getDatabase().replaceTableMetadata(this.mAppName, db, entry);
                } catch (JsonGenerationException var10) {
                    WebLogger.getLogger(this.mAppName).e(TAG, "problem parsing list of color rules");
                    WebLogger.getLogger(this.mAppName).printStackTrace(var10);
                } catch (JsonMappingException var11) {
                    WebLogger.getLogger(this.mAppName).e(TAG, "problem mapping list of color rules");
                    WebLogger.getLogger(this.mAppName).printStackTrace(var11);
                } catch (IOException var12) {
                    WebLogger.getLogger(this.mAppName).e(TAG, "i/o problem with json list of color rules");
                    WebLogger.getLogger(this.mAppName).printStackTrace(var12);
                }
            } finally {
                if(db != null) {
                    ctxt.getDatabase().closeDatabase(this.mAppName, db);
                }

            }

        }
    }

    public void updateRule(ColorRule updatedRule) {
        for(int i = 0; i < this.ruleList.size(); ++i) {
            if(((ColorRule)this.ruleList.get(i)).getRuleId().equals(updatedRule.getRuleId())) {
                this.ruleList.set(i, updatedRule);
                this.mDefault = false;
                return;
            }
        }

        WebLogger.getLogger(this.mAppName).e(TAG, "tried to update a rule that matched no saved ids");
    }

    public void removeRule(ColorRule rule) {
        for(int i = 0; i < this.ruleList.size(); ++i) {
            if(((ColorRule)this.ruleList.get(i)).getRuleId().equals(rule.getRuleId())) {
                this.ruleList.remove(i);
                this.mDefault = false;
                return;
            }
        }

        WebLogger.getLogger(this.mAppName).d(TAG, "a rule was passed to deleteRule that did not match the id of any rules in the list");
    }

    public int getRuleCount() {
        return this.ruleList.size();
    }

    public ColorGuide getColorGuide(OrderedColumns orderedDefns, Row row) {
        for(int i = 0; i < this.ruleList.size(); ++i) {
            ColorRule cr = (ColorRule)this.ruleList.get(i);
            String elementKey = cr.getColumnElementKey();
            ColumnDefinition cd = null;

            try {
                cd = orderedDefns.find(cr.getColumnElementKey());
            } catch (Exception var9) {
                ;
            }

            ElementDataType type;
            if(cd == null) {
                if(!Arrays.asList(this.mAdminColumns).contains(elementKey)) {
                    throw new IllegalArgumentException("element key passed to ColorRule#checkMatch didn\'t have a mapping and was not a metadata elementKey: " + elementKey);
                }

                if(elementKey.equals("_conflict_type")) {
                    type = ElementDataType.integer;
                } else {
                    type = ElementDataType.string;
                }
            } else {
                ElementType elementType = cd.getType();
                type = elementType.getDataType();
            }

            if(cr.checkMatch(type, row)) {
                return new ColorGuide(cr.getForeground(), cr.getBackground());
            }
        }

        return null;
    }

    static {
        mapper.setVisibilityChecker(mapper.getVisibilityChecker().withFieldVisibility(Visibility.ANY));
        mapper.setVisibilityChecker(mapper.getVisibilityChecker().withCreatorVisibility(Visibility.ANY));
        typeFactory = mapper.getTypeFactory();
    }

    public static enum Type {
        COLUMN,
        TABLE,
        STATUS_COLUMN;

        private Type() {
        }
    }
}
