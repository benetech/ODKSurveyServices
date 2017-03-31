package org.opendatakit.demoAndroidCommonClasses.data.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.opendatakit.aggregate.odktables.rest.SyncState;
import org.opendatakit.demoAndroidlibraryClasses.data.ColorRule;
import org.opendatakit.demoAndroidlibraryClasses.data.ColorRule.RuleType;

public class ColorRuleUtil {
    private static final String ID_SYNCED_RULE = "syncStateSynced";
    private static final String ID_IN_CONFLICT_RULE = "defaultRule_syncStateInConflict";
    private static final String ID_NEW_ROW_RULE = "defaultRule_syncStateNewRow";
    private static final String ID_CHANGED_RULE = "defaultRule_syncStateChanged";
    private static final String ID_DELETED_RULE = "defaultRule_syncStateDeleted";
    private static final int DEFAULT_SYNC_STATE_SYNCED_FOREGROUND = -16777216;
    private static final int DEFAULT_SYNC_STATE_SYNCED_BACKGROUND = -1;
    private static final int DEFAULT_SYNC_STATE_NEW_ROW_FOREGROUND = -16777216;
    private static final int DEFAULT_SYNC_STATE_NEW_ROW_BACKGROUND = -16711936;
    private static final int DEFAULT_SYNC_STATE_CHANGED_FOREGROUND = -16777216;
    private static final int DEFAULT_SYNC_STATE_CHANGED_BACKGROUND = -935891;
    private static final int DEFAULT_SYNC_STATE_IN_CONFLICT_FOREGROUND = -16777216;
    private static final int DEFAULT_SYNC_STATE_IN_CONFLICT_BACKGROUND = -65536;
    private static final int DEFAULT_SYNC_STATE_DELETED_FOREGROUND = -16777216;
    private static final int DEFAULT_SYNC_STATE_DELETED_BACKGROUND = -12303292;
    private static final List<ColorRule> defaultSyncStateColorRules;
    private static final Set<String> defaultSyncStateColorRuleIDs;

    public ColorRuleUtil() {
    }

    private static ColorRule getColorRuleForSyncStateSynced() {
        return new ColorRule("syncStateSynced", "_sync_state", RuleType.EQUAL, SyncState.synced.name(), -16777216, -1);
    }

    private static ColorRule getColorRuleForSyncStateNewRow() {
        return new ColorRule("defaultRule_syncStateNewRow", "_sync_state", RuleType.EQUAL, SyncState.new_row.name(), -16777216, -16711936);
    }

    private static ColorRule getColorRuleForSyncStateChanged() {
        return new ColorRule("defaultRule_syncStateChanged", "_sync_state", RuleType.EQUAL, SyncState.changed.name(), -16777216, -935891);
    }

    private static ColorRule getColorRuleForSyncStateDeleted() {
        return new ColorRule("defaultRule_syncStateDeleted", "_sync_state", RuleType.EQUAL, SyncState.deleted.name(), -16777216, -12303292);
    }

    private static ColorRule getColorRuleForSyncStateInConflict() {
        return new ColorRule("defaultRule_syncStateInConflict", "_sync_state", RuleType.EQUAL, SyncState.in_conflict.name(), -16777216, -65536);
    }

    public static List<ColorRule> getDefaultSyncStateColorRules() {
        return defaultSyncStateColorRules;
    }

    public static Set<String> getDefaultSyncStateColorRuleIds() {
        return defaultSyncStateColorRuleIDs;
    }

    static {
        ArrayList ruleList = new ArrayList();
        ruleList.add(getColorRuleForSyncStateSynced());
        ruleList.add(getColorRuleForSyncStateNewRow());
        ruleList.add(getColorRuleForSyncStateChanged());
        ruleList.add(getColorRuleForSyncStateInConflict());
        ruleList.add(getColorRuleForSyncStateDeleted());
        defaultSyncStateColorRules = Collections.unmodifiableList(ruleList);
        HashSet idSet = new HashSet();
        idSet.add("syncStateSynced");
        idSet.add("defaultRule_syncStateInConflict");
        idSet.add("defaultRule_syncStateDeleted");
        idSet.add("defaultRule_syncStateNewRow");
        idSet.add("defaultRule_syncStateChanged");
        defaultSyncStateColorRuleIDs = Collections.unmodifiableSet(idSet);
    }
}
