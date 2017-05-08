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

import org.apache.commons.lang3.ArrayUtils;
import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
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

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
public class FormListLoader extends AsyncTaskLoader<ArrayList<Object>> {

  private final String appName;
  private String submenuPage;

  private String[] whereArgs = new String[] {
          "new_row"
  };


  public FormListLoader(Context context, String appName, String submenuPage) {
    super(context);
    this.appName = appName;
    this.submenuPage = submenuPage;
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
        int idxFormTitle = c.getColumnIndex(FormsColumns.DISPLAY_NAME.getText());/*
        int idxLastUpdateDate = c.getColumnIndex(FormsColumns.DATE.getText());
        int idxFormVersion = c.getColumnIndex(FormsColumns.FORM_VERSION.getText());

        SimpleDateFormat formatter = new SimpleDateFormat(getContext().getString(R.string
            .last_updated_on_date_at_time), Locale.getDefault());

        do {
          String formVersion = c.isNull(idxFormVersion) ? null :
              c.getString(idxFormVersion);
          long timestamp = c.getLong(idxLastUpdateDate);
          Date lastModificationDate = new Date(timestamp);
          String formTitle = c.getString(idxFormTitle);
          int instancesCounter = getFormInstancesCount(c.getString(idxTableId));

          InstanceInfo info = new InstanceInfo(
              c.getString(idxTableId),
              c.getString(idxFormId),
              formVersion,
              LocalizationUtils.getLocalizedDisplayName(formTitle),
              formatter.format(lastModificationDate),
              instancesCounter);
          if(!c.getString(idxFormVersion).contains("sub"))
            forms.add(info);
        } while ( c.moveToNext());
      }*/
        do {
          Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                  + c.getString(idxTableId));

          Cursor c2 = null;
          try {
            c2 = getContext().getContentResolver().query(formUri, null, "_sync_state=?", whereArgs, DataTableColumns.SAVEPOINT_TIMESTAMP.getText() + " DESC"); //mozna dac     Cursor c = getContext().getContentResolver().query(formUri, null, "_sync_state=?", whereArgs, null);
            if (c2 != null && c2.moveToFirst()) {
              forms.add(LocalizationUtils.getLocalizedDisplayName(c.getString(idxFormTitle)));
              do {
                int idxForm = c2.getColumnIndex(InstanceColumns.DISPLAY_NAME.getText());
                int[] arr = countEmptyAndFilledColumns(c2);
                InstanceInfo info = new InstanceInfo(
                        LocalizationUtils.getLocalizedDisplayName(c.getString(idxFormTitle)),
                        formatter.format(new Date(TableConstants.milliSecondsFromNanos(c2.getString(c2.getColumnIndex(DataTableColumns.SAVEPOINT_TIMESTAMP.getText()))))),
                        "Jan Kowalski",
                        arr[0],
                        arr[1]);
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
    int res = 0;
    int res2 =0;
    boolean isDefColumn;
    //dac to w gore gdzies
    List<String> fields0 = DataTableColumns.getValues();
    fields0.addAll(ChoiceListColumns.getValues());
    fields0.addAll(ColumnDefinitionsColumns.getValues());
    fields0.addAll(FormsColumns.getValues());
    fields0.addAll(InstanceColumns.getValues());
    fields0.addAll(TableDefinitionsColumns.getValues());
    fields0.addAll(SyncETagColumns.getValues());
    fields0.addAll(SurveyConfigurationColumns.getValues());
    fields0.addAll(KeyValueStoreColumns.getValues());

    for (int i = 0; i < c.getColumnCount(); i++) {
      isDefColumn = false;
      for (int j = 0; j < fields0.size(); j++) {
        if (c.getColumnName(i).equals(fields0.get(j))) {
          isDefColumn = true;
          break;
        }
      }
      if (!isDefColumn) {
        switch (c.getType(i)) {
          case FIELD_TYPE_NULL:
            res++;
            break;
          case FIELD_TYPE_INTEGER:
            res2++;
            break;
          case FIELD_TYPE_FLOAT:
            res2++;
            break;
          case FIELD_TYPE_STRING:
            if (!c.getColumnName(i).contains("_contentType"))
              res2++;
            break;
          case FIELD_TYPE_BLOB:
            res2++;
            break;
        }
      }
    }
    int[] array = {res, res2};
    return array;
  }

  @Override protected void onStartLoading() {
    super.onStartLoading();
    forceLoad();
  }
}
