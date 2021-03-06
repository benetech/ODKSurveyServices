package org.opendatakit.demoAndroidlibraryClasses.consts;

public class IntentConsts {
    public static final String INTENT_KEY_APP_NAME = "appName";
    public static final String INTENT_KEY_TABLE_ID = "tableId";
    public static final String INTENT_KEY_INSTANCE_ID = "instanceId";
    public static final String INTENT_KEY_URI_FRAGMENT = "uriFragment";
    public static final String INTENT_KEY_CONTENT_TYPE = "contentType";
    public static final String INTENT_KEY_CURRENT_URI_FRAGMENT = "currentUriFragment";
    public static final String INTENT_KEY_SETTINGS_IN_ADMIN_MODE = "adminMode";
    public static final String INTENT_KEY_SETTINGS_ADMIN_ENABLED = "adminEnabled";

    public IntentConsts() {
    }

    public class Survey {
        public static final String SURVEY_PACKAGE_NAME = "org.opendatakit.survey";
        public static final String SURVEY_MAIN_MENU_ACTIVITY_COMPONENT_NAME = "org.opendatakit.survey.activities.SplashScreenActivity";

        public Survey() {
        }
    }

    public class Database {
        public static final String DATABASE_SERVICE_PACKAGE = "org.opendatakit.survey";
        public static final String DATABASE_SERVICE_CLASS = "org.opendatakit.services.database.service.OdkDatabaseService";

        public Database() {
        }
    }

    public class AppProperties {
        public static final String APPLICATION_NAME = "org.opendatakit.survey";
        public static final String ACTIVITY_NAME = "org.opendatakit.services.preferences.activities.AppPropertiesActivity";

        public AppProperties() {
        }
    }

    public class Sync {
        public static final String APPLICATION_NAME = "org.opendatakit.survey";
        public static final String ACTIVITY_NAME = "org.opendatakit.services.sync.activities.SyncActivity";
        public static final String SYNC_SERVICE_PACKAGE = "org.opendatakit.survey";
        public static final String SYNC_SERVICE_CLASS = "org.opendatakit.services.sync.service.OdkSyncService";

        public Sync() {
        }
    }

    public class ResolveConflict {
        public static final String APPLICATION_NAME = "org.opendatakit.survey";
        public static final String ACTIVITY_NAME = "org.opendatakit.services.resolve.conflict.ConflictResolutionActivity";

        public ResolveConflict() {
        }
    }

    public class ResolveCheckpoint {
        public static final String APPLICATION_NAME = "org.opendatakit.survey";
        public static final String ACTIVITY_NAME = "org.opendatakit.services.resolve.checkpoint.CheckpointResolutionActivity";

        public ResolveCheckpoint() {
        }
    }
}
