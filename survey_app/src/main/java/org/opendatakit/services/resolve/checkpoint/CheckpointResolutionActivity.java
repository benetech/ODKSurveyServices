/*
 * Copyright (C) 2014 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opendatakit.services.resolve.checkpoint;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.opendatakit.demoAndroidlibraryClasses.consts.IntentConsts;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.services.database.AndroidConnectFactory;
import org.opendatakit.demoAndroidlibraryClasses.fragment.AboutMenuFragment;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.survey.R;

/**
 * An activity for handling checkpoint conflicts.
 * If an IntentConsts.INTENT_KEY_INSTANCE_ID is provided,
 * opens the row resolution fragment. Otherwise, opens
 * the list resolution fragment.
 *
 * @author mitchellsundt@gmail.com
 *
 */
public class CheckpointResolutionActivity extends Activity implements IAppAwareActivity {

  private static final String TAG = CheckpointResolutionActivity.class.getSimpleName();

  public static final int RESOLVE_ROW = 1;

  private String mAppName;
  private String mTableId;
  private String mRowId;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.checkpoint_resolver_activity);

    // IMPORTANT NOTE: the Application object is not yet created!

    // Used to ensure that the singleton has been initialized properly
    AndroidConnectFactory.configure();
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Do this in on resume so that if we resolve a row it will be refreshed
    // when we come back.
    mAppName = getIntent().getStringExtra(IntentConsts.INTENT_KEY_APP_NAME);
    if (mAppName == null) {
      Log.e(TAG, IntentConsts.INTENT_KEY_APP_NAME + " not supplied on intent");
      setResult(Activity.RESULT_CANCELED);
      finish();
      return;
    }
    mTableId = getIntent().getStringExtra(IntentConsts.INTENT_KEY_TABLE_ID);
    if (mTableId == null) {
      WebLogger.getLogger(mAppName).e(TAG, IntentConsts.INTENT_KEY_TABLE_ID +
          " not supplied on intent");
      setResult(Activity.RESULT_CANCELED);
      finish();
      return;
    }

    FragmentManager mgr = getFragmentManager();
    String newFragmentName = null;
    Fragment newFragment = null;
    mRowId = getIntent().getStringExtra(IntentConsts.INTENT_KEY_INSTANCE_ID);
    if ( mRowId == null ) {
      // we want the list fragment
      newFragmentName = CheckpointResolutionListFragment.NAME;
      newFragment = mgr.findFragmentByTag(newFragmentName);
      if ( newFragment == null ) {
        newFragment = new CheckpointResolutionListFragment();
      }
    } else {
      // we want the row fragment
      newFragmentName = CheckpointResolutionRowFragment.NAME;
      newFragment = mgr.findFragmentByTag(newFragmentName);
      if ( newFragment == null ) {
        newFragment = new CheckpointResolutionRowFragment();
      }
    }

    FragmentTransaction trans = mgr.beginTransaction();
    trans.replace(R.id.checkpoint_resolver_activity_view, newFragment, newFragmentName);
    trans.commit();
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_about) {

      FragmentManager mgr = getFragmentManager();
      Fragment newFragment = mgr.findFragmentByTag(AboutMenuFragment.NAME);
      if ( newFragment == null ) {
        newFragment = new AboutMenuFragment();
      }
      FragmentTransaction trans = mgr.beginTransaction();
      trans.replace(R.id.checkpoint_resolver_activity_view, newFragment, AboutMenuFragment.NAME);
      trans.addToBackStack(AboutMenuFragment.NAME);
      trans.commit();

      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public String getAppName() {
    return mAppName;
  }
}