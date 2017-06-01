/*
 * Copyright (C) 2015 University of Washington
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

package org.opendatakit.survey.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.provider.ChoiceListColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.ColumnDefinitionsColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.DataTableColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.provider.KeyValueStoreColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.SurveyConfigurationColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.SyncETagColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.TableDefinitionsColumns;
import org.opendatakit.demoAndroidlibraryClasses.utilities.LocalizationUtils;
import org.opendatakit.survey.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_NULL;
import static android.database.Cursor.FIELD_TYPE_STRING;

/**
 * @author mitchellsundt@gmail.com
 */
public class InstanceListLoader extends AsyncTaskLoader<ArrayList<Object>> {

  private final String appName;

  private String[] whereArgs = new String[] {
          ""
  };

  private final List<String> defaultColumns;


  public InstanceListLoader(Context context, String appName, String submenuPage) {
    super(context);
    this.appName = appName;
    whereArgs[0] = submenuPage;

    defaultColumns = DataTableColumns.getValues();
    defaultColumns.addAll(ChoiceListColumns.getValues());
    defaultColumns.addAll(ColumnDefinitionsColumns.getValues());
    defaultColumns.addAll(FormsColumns.getValues());
    defaultColumns.addAll(InstanceColumns.getValues());
    defaultColumns.addAll(TableDefinitionsColumns.getValues());
    defaultColumns.addAll(SyncETagColumns.getValues());
    defaultColumns.addAll(SurveyConfigurationColumns.getValues());
    defaultColumns.addAll(KeyValueStoreColumns.getValues());
  }

  @Override public ArrayList<Object> loadInBackground() {
    // This is called when a new Loader needs to be created. This
    // sample only has one Loader, so we don't care about the ID.
    // First, pick the base URI to use depending on whether we are
    // currently filtering.
    Uri baseUri = Uri.withAppendedPath(FormsProviderAPI.CONTENT_URI, appName);

    ArrayList<Object> forms = new ArrayList<Object>();
    SimpleDateFormat formatter = new SimpleDateFormat(getContext().getString(R.string
            .last_updated_on_date_at_time), Locale.getDefault());
    Cursor c = null;
    try {
      c = getContext().getContentResolver().query(baseUri, null, null, null, null);

      if (c != null && c.moveToFirst() ) {
        int idxTableId = c.getColumnIndex(FormsColumns.TABLE_ID.getText());
        int idxFormId = c.getColumnIndex(FormsColumns.FORM_ID.getText());
        int idxFormTitle = c.getColumnIndex(FormsColumns.DISPLAY_NAME.getText());

        do {
          Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                  + c.getString(idxTableId));

          Cursor c2 = null;
          try {
            c2 = getContext().getContentResolver().query(formUri, null, "_sync_state=?", whereArgs, DataTableColumns.SAVEPOINT_TIMESTAMP.getText() + " DESC");
            if (c2 != null && c2.moveToFirst()) {
              forms.add(LocalizationUtils.getLocalizedDisplayName(c.getString(idxFormTitle)));
              do {
                int[] emptyFilledColumns = countEmptyAndFilledColumns(c2);
                int[] spotlightAnswers = countStoplightAnswers(c2);
                InstanceInfo info = new InstanceInfo(
                        c.getString(idxTableId),
                        c.getString(idxFormId),
                        LocalizationUtils.getLocalizedDisplayName(c.getString(idxFormTitle)),
                        formatter.format(new Date(TableConstants.milliSecondsFromNanos(c2.getString(c2.getColumnIndex(DataTableColumns.SAVEPOINT_TIMESTAMP.getText()))))),
                        "Jan Kowalski",
                        emptyFilledColumns[0],
                        emptyFilledColumns[1],
                        spotlightAnswers[0],
                        spotlightAnswers[1],
                        spotlightAnswers[2]);
                forms.add(info);
              } while (c2.moveToNext());
            }

          } finally {
            if (c2 != null && !c2.isClosed()) {
              c2.close();
            }
          }
        }
        while (c.moveToNext());


      }} finally {
      if ( c != null && !c.isClosed() ) {
        c.close();
      }
    }

    return forms;
  }

  private int[] countEmptyAndFilledColumns(Cursor c) {
    int empty = 0;
    int fulfilled = 0;

    for (int i = 0; i < c.getColumnCount(); i++) {
      if (!defaultColumns.contains(c.getColumnName(i))) {
        switch (c.getType(i)) {
          case FIELD_TYPE_NULL:
            empty++;
            break;
          case FIELD_TYPE_INTEGER:
            fulfilled++;
            break;
          case FIELD_TYPE_FLOAT:
            fulfilled++;
            break;
          case FIELD_TYPE_STRING:
            if (!c.getColumnName(i).contains("_contentType"))
              fulfilled++;
            break;
          case FIELD_TYPE_BLOB:
            fulfilled++;
            break;
        }
      }
    }
    int[] array = {empty, fulfilled};
    return array;
  }

  private int[] countStoplightAnswers(Cursor c) {
    int red = 0;
    int yellow = 0;
    int green = 0;
    boolean isDefColumn;

    for (int i = 0; i < c.getColumnCount(); i++) {
      isDefColumn = false;
      if(defaultColumns.contains(c.getColumnName(i))) {
        isDefColumn = true;
      }
      if (!isDefColumn) {
        if (c.getType(i) == FIELD_TYPE_STRING) {
            //if (!c.getColumnName(i).contains("_contentType")) here we will filter it
          switch(c.getString(i)){
            case "red":
              red++;
              break;
            case "yellow":
              yellow++;
              break;
            case "green":
              green++;
              break;
          }
        }
      }
    }
    int[] array = {red, yellow, green};
    return array;
  }

  @Override protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }
}
