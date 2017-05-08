package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public enum InstanceColumns implements BaseColumns {
    STATUS_INCOMPLETE("INCOMPLETE"),
    STATUS_COMPLETE("COMPLETE"),
    STATUS_SUBMITTED("submitted"),
    STATUS_SUBMISSION_FAILED("submissionFailed"),
    CONTENT_TYPE("vnd.android.cursor.dir/vnd.opendatakit.instance"),
    CONTENT_ITEM_TYPE("vnd.android.cursor.item/vnd.opendatakit.instance"),
    DATA_INSTANCE_ID("_instanceId"),
    DATA_TABLE_TABLE_ID("_tableId"),
    DATA_INSTANCE_NAME("_instanceName"),
    SUBMISSION_INSTANCE_ID("_submissionInstanceId"),
    XML_PUBLISH_TIMESTAMP("_xmlPublishTimestamp"),
    XML_PUBLISH_STATUS("_xmlPublishStatus"),
    DISPLAY_NAME("_displayName"),
    DISPLAY_SUBTEXT("_displaySubtext");

    private final String text;

    InstanceColumns() {
        this.text = null;
    }

    InstanceColumns(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static String getTableCreateSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + " (" + "_id" + " integer primary key, " + "_instanceId" + " text, " + "_tableId" + " text, " + "_instanceName" + " text, " + "_submissionInstanceId" + " text, " + "_xmlPublishTimestamp" + " text, " + "_xmlPublishStatus" + " text, " + "_displaySubtext" + " text)";
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(InstanceColumns d : values()) {
            res.add(d.getText());
        }
        return res;
    }
}
