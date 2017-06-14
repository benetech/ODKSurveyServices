package org.opendatakit.survey.utilities;

/**
 * Created by user on 29.05.17.
 */

public class FormInfo {
    public final String formDisplayName;
    public final String updateData;
    public final int questions;
    public final String tableId;

    FormInfo(String tableId, String formDisplayName, String updateData, int questions) {
        this.tableId = tableId;
        this.formDisplayName = formDisplayName;
        this.updateData = updateData;
        this.questions = questions;
    }
}