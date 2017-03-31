/*
 * Copyright (C) 2016 University of Washington
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

package org.opendatakit.services.preferences.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import org.opendatakit.demoAndroidlibraryClasses.consts.IntentConsts;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;

import java.io.File;

/**
 * @author mitchellsundt@gmail.com
 */
public class ClearAppPropertiesActivity extends Activity {

   AlertDialog mDialog;

   @Override protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      final String mAppName;
      String appName = this.getIntent().getStringExtra(IntentConsts.INTENT_KEY_APP_NAME);
      if (appName == null || appName.length() == 0) {
         mAppName = ODKFileUtils.getOdkDefaultAppName();
      } else {
         mAppName = appName;
      }

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      mDialog = builder.setTitle(R.string.reset_settings)
          .setMessage(R.string.confirm_reset_settings)
          .setCancelable(false)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
             @Override public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();

                // clear the device and secure properties for this appName

                PropertiesSingleton mProps = CommonToolProperties.get(
                    ClearAppPropertiesActivity.this, mAppName);
                mProps.clearSettings();

                // clear the tables.init file that prevents re-reading and re-processing
                // the assets/tables.init file (that preloads data from csv files)
                File f = new File(ODKFileUtils.getTablesInitializationCompleteMarkerFile(mAppName));
                if ( f.exists() ) {
                   f.delete();
                }

                // clear the initialization-complete marker files that prevent the
                // initialization task from being executed.
                ODKFileUtils.clearConfiguredToolFiles(mAppName);

                setResult(RESULT_OK);
                finish();
             }
          })
          .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
             @Override public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
             }
          }).create();
      mDialog.setCanceledOnTouchOutside(false);
      mDialog.show();
   }
}
