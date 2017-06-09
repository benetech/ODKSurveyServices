/*
 * Copyright (C) 2012-2013 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.survey.views;

import android.content.ContentValues;
import android.content.Context;

import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.services.sync.service.ServiceConnectionWrapper;
import org.opendatakit.survey.activities.IOdkSurveyActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.FORM_SUBFORM_PAIRS_TABLE_ID;
import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.FORM_TABLE_ID_COLUMN;
import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.FORM_UUID_COLUMN;
import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.MAIN_FORM_TABLE_ID;
import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.SUBFORM_TABLE_ID_COLUMN;
import static org.opendatakit.demoAndroidlibraryClasses.database.DatabaseConstants.SUBFORM_UUID_COLUMN;

/**
 * The class mapped to 'odkSurvey' in the Javascript
 *
 * @author mitchellsundt@gmail.com
 *
 */
public class OdkSurvey {

  public static final String t = "OdkSurvey";
  Context mContext;
  private WeakReference<OdkSurveyWebView> mWebView;
  private IOdkSurveyActivity mActivity;
  private final WebLoggerIf log;
  private final ServiceConnectionWrapper serviceConnectionWrapper;

  public OdkSurvey(IOdkSurveyActivity activity, OdkSurveyWebView webView, Context c) {
    mWebView = new WeakReference<OdkSurveyWebView>(webView);
    mActivity = activity;
    log = WebLogger.getLogger(mActivity.getAppName());
    mContext=c;
    serviceConnectionWrapper = new ServiceConnectionWrapper(mContext, mActivity.getAppName());
  }

  public boolean isInactive() {
    return (mWebView.get() == null) || (mWebView.get().isInactive());
  }


  public OdkSurveyIf getJavascriptInterfaceWithWeakReference() {
    return new OdkSurveyIf(this, mContext);
  }

  public void clearAuxillaryHash() {
    log.d("odkSurvey", "DO: clearAuxillaryHash()");
    mActivity.clearAuxillaryHash();
  }

  public void clearInstanceId(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: clearInstanceId(" + refId + ")");
      return;
    }
    log.d("odkSurvey", "DO: clearInstanceId(" + refId + ")");
    mActivity.setInstanceId(null);
  }

  /**
   * If refId is null, clears the instanceId. If refId matches the current
   * refId, sets the instanceId.
   *
   * @param refId
   * @param instanceId
   */
  public void setInstanceId(String refId, String instanceId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: setInstanceId(" + refId + ", " + instanceId + ")");
      return;
    }
    log.d("odkSurvey", "DO: setInstanceId(" + refId + ", " + instanceId + ")");
    mActivity.setInstanceId(instanceId);
  }

  public String getInstanceId(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: getInstanceId(" + refId + ")");
      return null;
    }
    log.d("odkSurvey", "DO: getInstanceId(" + refId + ")");
    return mActivity.getInstanceId();
  }

  public void pushSectionScreenState(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: pushSectionScreenState(" + refId + ")");
      return;
    }
    log.d("odkSurvey", "DO: pushSectionScreenState(" + refId + ")");
    mActivity.pushSectionScreenState();
  }

  public void setSectionScreenState(String refId, String screenPath, String state) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: setSectionScreenState(" + refId + ", " + screenPath + ", " + state
          + ")");
      return;
    }
    log.d("odkSurvey", "DO: setSectionScreenState(" + refId + ", " + screenPath + ", " + state + ")");
    mActivity.setSectionScreenState(screenPath, state);
  }

  public void clearSectionScreenState(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: clearSectionScreenState(" + refId + ")");
      return;
    }
    log.d("odkSurvey", "DO: clearSectionScreenState(" + refId + ")");
    mActivity.clearSectionScreenState();
  }

  public String getControllerState(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: getControllerState(" + refId + ")");
      return null;
    }
    log.d("odkSurvey", "DO: getControllerState(" + refId + ")");
    return mActivity.getControllerState();
  }

  public String getScreenPath(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: getScreenPath(" + refId + ")");
      return null;
    }
    log.d("odkSurvey", "DO: getScreenPath(" + refId + ")");
    return mActivity.getScreenPath();
  }

  public boolean hasScreenHistory(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: hasScreenHistory(" + refId + ")");
      return false;
    }
    log.d("odkSurvey", "DO: hasScreenHistory(" + refId + ")");
    return mActivity.hasScreenHistory();
  }

  public String popScreenHistory(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: popScreenHistory(" + refId + ")");
      return null;
    }
    log.d("odkSurvey", "DO: popScreenHistory(" + refId + ")");
    return mActivity.popScreenHistory();
  }

  public boolean hasSectionStack(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: hasSectionStack(" + refId + ")");
      return false;
    }
    log.d("odkSurvey", "DO: hasSectionStack(" + refId + ")");
    return mActivity.hasSectionStack();
  }

  public String popSectionStack(String refId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: popSectionStack(" + refId + ")");
      return null;
    }
    log.d("odkSurvey", "DO: popSectionStack(" + refId + ")");
    return mActivity.popSectionStack();
  }

  public void frameworkHasLoaded(String refId, boolean outcome) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: frameworkHasLoaded(" + refId + ", " + outcome + ")");
      return;
    }
    log.d("odkSurvey", "DO: frameworkHasLoaded(" + refId + ", " + outcome + ")");
    mWebView.get().frameworkHasLoaded();
  }

  public void ignoreAllChangesCompleted(String refId, String instanceId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: ignoreAllChangesCompleted(" + refId + ", " + instanceId + ")");
      return;
    }
    log.d("odkSurvey", "DO: ignoreAllChangesCompleted(" + refId + ", " + instanceId + ")");
    mActivity.ignoreAllChangesCompleted(instanceId);
  }

  public void ignoreAllChangesFailed(String refId, String instanceId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: ignoreAllChangesFailed(" + refId + ", " + instanceId + ")");
      return;
    }
    log.d("odkSurvey", "DO: ignoreAllChangesFailed(" + refId + ", " + instanceId + ")");
    mActivity.ignoreAllChangesFailed(instanceId);
  }

  public void saveAllChangesCompleted(String refId, String instanceId, boolean asComplete) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: saveAllChangesCompleted(" + refId + ", " + instanceId + ", "
          + asComplete + ")");
      return;
    }
    // go through the FC because there are additional keys that should be
    // set here...
    log.d("odkSurvey", "DO: saveAllChangesCompleted(" + refId + ", " + instanceId + ", " + asComplete
        + ")");
    mActivity.saveAllChangesCompleted(instanceId, asComplete);
  }

  public void saveAllChangesFailed(String refId, String instanceId) {
    if (!mActivity.getRefId().equals(refId)) {
      log.w("odkSurvey", "IGNORED: saveAllChangesFailed(" + refId + ", " + instanceId + ")");
      return;
    }
    log.d("odkSurvey", "DO: saveAllChangesFailed(" + refId + ", " + instanceId + ")");
    mActivity.saveAllChangesFailed(instanceId);
  }

  public String getSubmenuPage(){
    return mActivity.getSubmenuPage();
  }

  public void setMainFormData(String UUID, String tableId) {
    createMainFormDataLocalTable();
    updateMainFormData(UUID, tableId);
  }

  public void createMainFormDataLocalTable() {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    List<Column> columns = new ArrayList<>();
    columns.add(new Column(FORM_UUID_COLUMN, FORM_UUID_COLUMN, ElementDataType.string.name(), "[]"));
    columns.add(new Column(FORM_TABLE_ID_COLUMN, FORM_TABLE_ID_COLUMN, ElementDataType.string.name(), "[]"));
    ColumnList columnsList = new ColumnList(columns);

    try {
      userDbInterface.createLocalOnlyTableWithColumns(
              ODKFileUtils.getOdkDefaultAppName(),
              userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
              MAIN_FORM_TABLE_ID,
              columnsList);
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }
  }

  public void updateMainFormData(String UUID, String tableId) {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    ContentValues contentValues = new ContentValues();
    contentValues.put(FORM_UUID_COLUMN, UUID);
    contentValues.put(FORM_TABLE_ID_COLUMN, tableId);

    String whereClause = FORM_UUID_COLUMN + "=?";
    String[] bindArgs = new String[] { getMainFormUUID() };

    try {
      // There is a row in the database representing a main form. If it is a newly open main form
      // or the same existing we should update it.
      if (doesTableContainRowWithValue(MAIN_FORM_TABLE_ID, FORM_UUID_COLUMN, UUID) || doesTableContainRowWithValue(MAIN_FORM_TABLE_ID, FORM_UUID_COLUMN, getMainFormUUID())) {
        userDbInterface.updateLocalOnlyRow(
                ODKFileUtils.getOdkDefaultAppName(),
                userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
                MAIN_FORM_TABLE_ID,
                contentValues,
                whereClause,
                bindArgs);
      } else {
        // An empty table in the database, inserting a new row representing the main form
        userDbInterface.insertLocalOnlyRow(
                ODKFileUtils.getOdkDefaultAppName(),
                userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
                MAIN_FORM_TABLE_ID,
                contentValues);
      }
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }
  }

  public String getMainFormUUID() {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    String UUID = null;

    try {
      BaseTable result = userDbInterface.simpleQueryLocalOnlyTables(
              ODKFileUtils.getOdkDefaultAppName(),
              userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
              MAIN_FORM_TABLE_ID, null, null, null, null, null, null, null, null);
      if (result.getNumberOfRows() > 0) {
        UUID = result.getRowAtIndex(0).getDataByKey(FORM_UUID_COLUMN);
      }
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }

    return UUID;
  }

  public void updateFormSubformLocalTable(String formUUID, String subformUUID, String subformTableId) {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    ContentValues contentValues = new ContentValues();
    contentValues.put(FORM_UUID_COLUMN, formUUID);
    contentValues.put(SUBFORM_UUID_COLUMN, subformUUID);
    contentValues.put(SUBFORM_TABLE_ID_COLUMN, subformTableId);

    String whereClause = SUBFORM_UUID_COLUMN + "=?";
    String[] bindArgs = new String[] { subformUUID };

    try {
      // If a form - subform table contains a subform with a certain UUID, we update a row
      // which represents its relation with the main form.
      // If such row doesn't exist, a new row is created.
      if (doesTableContainRowWithValue(FORM_SUBFORM_PAIRS_TABLE_ID, SUBFORM_UUID_COLUMN, subformUUID)) {
        userDbInterface.updateLocalOnlyRow(
                ODKFileUtils.getOdkDefaultAppName(),
                userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
                FORM_SUBFORM_PAIRS_TABLE_ID,
                contentValues,
                whereClause,
                bindArgs);
      } else {
        userDbInterface.insertLocalOnlyRow(
                ODKFileUtils.getOdkDefaultAppName(),
                userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
                FORM_SUBFORM_PAIRS_TABLE_ID,
                contentValues);
      }
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes all rows representing relations between a form and a subform in which
   * one of the forms was deleted.
   *
   * @param UUID UUID of a deleted form/subform instance
   */
  public void deleteOutdatedInstancesFromFormSubFormTable(String UUID) {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    String whereClauseFormColumn = FORM_UUID_COLUMN + "=?";
    String whereClauseSubformColumn = SUBFORM_UUID_COLUMN + "=?";
    String[] bindArgs = new String[]{ UUID };

    try {
      // If form instance was deleted, remove all rows containing UUID in the FORM_UUID_COLUMN
      userDbInterface.deleteLocalOnlyRow(
              ODKFileUtils.getOdkDefaultAppName(),
              userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
              FORM_SUBFORM_PAIRS_TABLE_ID,
              whereClauseFormColumn,
              bindArgs);
      // If subform instance was deleted, remove all rows containing UUID in the SUBFORM_UUID_COLUMN
      userDbInterface.deleteLocalOnlyRow(
              ODKFileUtils.getOdkDefaultAppName(),
              userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
              FORM_SUBFORM_PAIRS_TABLE_ID,
              whereClauseSubformColumn,
              bindArgs);
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if a certain table contains a row with a specified value in a certain column
   *
   * @param tableId ID of a checked table
   * @param columnId ID of a checked column
   * @param value a searched value
   */
  public Boolean doesTableContainRowWithValue(String tableId, String columnId, String value) {
    UserDbInterface userDbInterface = serviceConnectionWrapper.getDatabaseService();

    Boolean result = false;

    String whereClause = columnId + "=?";
    String[] bindArgs = new String[]{ value };

    try {
      BaseTable rows = userDbInterface.simpleQueryLocalOnlyTables(
              ODKFileUtils.getOdkDefaultAppName(),
              userDbInterface.openDatabase(ODKFileUtils.getOdkDefaultAppName()),
              tableId, whereClause, bindArgs, null, null, null, null, null, null);
      if (rows.getNumberOfRows() > 0) {
        result = true;
      }
    } catch (ServicesAvailabilityException e) {
      e.printStackTrace();
    }

    return result;
  }
}