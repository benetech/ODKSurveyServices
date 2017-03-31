package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

public final class InstanceColumns implements BaseColumns {
    public static final String STATUS_INCOMPLETE = "INCOMPLETE";
    public static final String STATUS_COMPLETE = "COMPLETE";
    public static final String STATUS_SUBMITTED = "submitted";
    public static final String STATUS_SUBMISSION_FAILED = "submissionFailed";
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.opendatakit.instance";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.opendatakit.instance";
    public static final String DATA_INSTANCE_ID = "_instanceId";
    public static final String DATA_TABLE_TABLE_ID = "_tableId";
    public static final String DATA_INSTANCE_NAME = "_instanceName";
    public static final String SUBMISSION_INSTANCE_ID = "_submissionInstanceId";
    public static final String XML_PUBLISH_TIMESTAMP = "_xmlPublishTimestamp";
    public static final String XML_PUBLISH_STATUS = "_xmlPublishStatus";
    public static final String DISPLAY_NAME = "_displayName";
    public static final String DISPLAY_SUBTEXT = "_displaySubtext";

    private InstanceColumns() {
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_instanceId" + " text, " + "_tableId" + " text, " + "_instanceName" + " text, " + "_submissionInstanceId" + " text, " + "_xmlPublishTimestamp" + " text, " + "_xmlPublishStatus" + " text, " + "_displaySubtext" + " text)";
    }
}
