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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormListLoader extends AsyncTaskLoader<ArrayList<Object>> {

    private final String appName;
    private final List<String> defaultColumns;

    public FormListLoader(Context context, String appName) {
        super(context);
        this.appName = appName;

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
        SimpleDateFormat sdf = new SimpleDateFormat(getContext().getString(R.string
                .european_date_format));
        ArrayList<Object> forms = new ArrayList<Object>();
        Cursor c = null;

        try {
            c = getContext().getContentResolver().query(baseUri, null, null, null, null);

            if (c != null && c.moveToFirst() ) {
                do {
                    int idxTableId = c.getColumnIndex(FormsColumns.TABLE_ID.getText());
                    int idxFormTitle = c.getColumnIndex(FormsColumns.DISPLAY_NAME.getText());
                    int idxDate = c.getColumnIndex(FormsColumns.DATE.getText());

                    int questions = 0;
                    Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                            + c.getString(idxTableId));
                    Cursor c2 = null;

                    try {
                        c2 = getContext().getContentResolver().query(formUri, null, null, null, DataTableColumns.SAVEPOINT_TIMESTAMP.getText() + " DESC");
                        if (c2 != null) {
                            questions = countColumns(c2);
                        }
                    } finally {
                        if (c2 != null && !c2.isClosed()) {
                            c2.close();
                        }
                    }

                        Date date = new Date(Long.parseLong(c.getString(idxDate)));
                        FormInfo info = new FormInfo(
                                c.getString(idxTableId),
                                LocalizationUtils.getLocalizedDisplayName(c.getString(idxFormTitle)),
                                sdf.format(date),
                                questions);

                        forms.add(info);

                } while (c.moveToNext());
            }} finally {
            if ( c != null && !c.isClosed() ) {
                c.close();
            }
        }

        return forms;
    }

    @Override protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    private int countColumns(Cursor c) {
        int questions = 0;

        for (int i = 0; i < c.getColumnCount(); i++) {
            if(!defaultColumns.contains(c.getColumnName(i))) {
                questions++;
            }
        }
        return questions;
    }
}
