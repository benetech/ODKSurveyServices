package org.opendatakit.survey.utilities;

/**
 * @author mitchellsundt@gmail.com
 */
public final class InstanceInfo {

  public final String formDisplayName;
  public final String savepointTimestamp;
  public final String beneficiaryInformation;
  public final int questionsLeft;
  public final int questionsFulfilled;
  public final String tableId;
  public final String formId;

  InstanceInfo(String tableId, String formId, String formDisplayName, String savepointTimestamp, String beneficiaryInformation,
               int questionsleft, int questionsFulfilled) {
    this.tableId = tableId;
    this.formId = formId;
    this.formDisplayName = formDisplayName;
    this.savepointTimestamp = savepointTimestamp;
    this.beneficiaryInformation = beneficiaryInformation;
    this.questionsLeft = questionsleft;
    this.questionsFulfilled = questionsFulfilled;
  }
}
