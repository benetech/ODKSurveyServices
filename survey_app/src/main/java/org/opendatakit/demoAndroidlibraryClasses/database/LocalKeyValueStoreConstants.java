package org.opendatakit.demoAndroidlibraryClasses.database;

public final class LocalKeyValueStoreConstants {
    public LocalKeyValueStoreConstants() {
    }

    public static final class Tables {
        public static final String TABLE_DEFAULT_VIEW_TYPE = "defaultViewType";
        public static final String KEY_LIST_VIEW_FILE_NAME = "listViewFileName";
        public static final String KEY_DETAIL_VIEW_FILE_NAME = "detailViewFileName";
        public static final String KEY_MAP_LIST_VIEW_FILE_NAME = "mapListViewFileName";

        public Tables() {
        }
    }

    public static final class Spreadsheet {
        public static final String PARTITION = "SpreadsheetView";
        public static final String KEY_COLUMN_WIDTH = "SpreadsheetView.columnWidth";
        public static final String KEY_FONT_SIZE = "fontSize";
        public static final int DEFAULT_COL_WIDTH = 125;
        public static final int MAX_COL_WIDTH = 1000;

        public Spreadsheet() {
        }
    }

    public static final class Map {
        public static final String PARTITION = "TableMapFragment";
        public static final String KEY_MAP_LAT_COL = "keyMapLatCol";
        public static final String KEY_MAP_LONG_COL = "keyMapLongCol";
        public static final String KEY_COLOR_RULE_TYPE = "keyColorRuleType";
        public static final String KEY_COLOR_RULE_COLUMN = "keyColorRuleColumn";
        public static final String COLOR_TYPE_NONE = "None";
        public static final String COLOR_TYPE_TABLE = "Table Color Rules";
        public static final String COLOR_TYPE_STATUS = "Status Column Color Rules";

        public Map() {
        }
    }

    public static final class ColumnColorRules {
        public static final String PARTITION = "ColumnColorRuleGroup";
        public static final String KEY_COLOR_RULES_COLUMN = "ColumnColorRuleGroup.ruleList";

        public ColumnColorRules() {
        }
    }

    public static final class TableColorRules {
        public static final String PARTITION = "TableColorRuleGroup";
        public static final String KEY_COLOR_RULES_ROW = "TableColorRuleGroup.ruleList";
        public static final String KEY_COLOR_RULES_STATUS_COLUMN = "StatusColumn.ruleList";

        public TableColorRules() {
        }
    }

    public static final class TableSecurity {
        public static final String ASPECT = "security";
        public static final String KEY_FILTER_TYPE_ON_CREATION = "filterTypeOnCreation";
        public static final String KEY_UNVERIFIED_USER_CAN_CREATE = "unverifiedUserCanCreate";
        public static final String KEY_LOCKED = "locked";

        public TableSecurity() {
        }
    }
}
