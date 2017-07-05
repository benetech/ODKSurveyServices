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

package org.opendatakit.survey.fragments;

import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.IOdkSurveyActivity;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.DataPassListener;
import org.opendatakit.survey.utilities.InstanceInfo;
import org.opendatakit.survey.utilities.InstanceListLoader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.opendatakit.survey.utilities.InstanceInfoListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment displaying the list of available forms to fill out.
 *
 * @author mitchellsundt@gmail.com
 */
public class InProgressInstancesFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<ArrayList<Object>> {

  @SuppressWarnings("unused") private static final String t = "InProgressInstancesFragment";
  private static final String FIRSTNAME = "firstname";
  private static final String LASTNAME = "lastname";
  private static final String CHOOSEN_TABLE_ID = "table_id";
  private static final String INSTANCE_UUID = "instance_uuid";

  public static final int ID = R.layout.fragment_in_progress_instances;

  // data to retain across orientation changes

  // data that is not retained
  DataPassListener mCallback;
  private InstanceInfoListAdapter mAdapter;
  private View view;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // render total instance view
    mAdapter = new InstanceInfoListAdapter(getActivity(), R.layout.in_progress_row, R.id.savepointTimestamp,
            R.id.beneficiaryName, R.id.questionsLeft);
    setListAdapter(mAdapter);

    getLoaderManager().initLoader(0, null, this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(ID, container, false);

    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.newFormFloatingActionButton);
    fab.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        ((MainMenuActivity)getActivity()).mNavigationViewTop.setCheckedItem(R.id.new_survey_menuitem);
        ((MainMenuActivity)getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.BENEFICIARY_INFORMATION);
      }
    });

    return view;
  }

  @Override public void onResume() {
    super.onResume();
    ((MainMenuActivity)getActivity()).setSubmenuPage("new_row");
    ((MainMenuActivity)getActivity()).mNavigationViewTop.setCheckedItem(R.id.in_progress_menuitem);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);


    String tableId = ((InstanceInfo)mAdapter.getItem(position)).tableId;
    String rowId = ((InstanceInfo)mAdapter.getItem(position)).UUID;
    HashMap<String, String> values = new HashMap<>();
    values.put(FIRSTNAME, ((InstanceInfo)mAdapter.getItem(position)).beneficiaryInformation);
    values.put(LASTNAME, ((InstanceInfo)mAdapter.getItem(position)).beneficiaryInformation);
    values.put(CHOOSEN_TABLE_ID, tableId);
    values.put(INSTANCE_UUID, rowId);
    mCallback.passData(values);

    ((MainMenuActivity)getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.SUMMARY_PAGE);
  }

  @Override public Loader<ArrayList<Object>> onCreateLoader(int id, Bundle args) {
    // This is called when a new Loader needs to be created. This
    // sample only has one Loader, so we don't care about the ID.
    return new InstanceListLoader(getActivity(), ((IAppAwareActivity) getActivity()).getAppName(), ((MainMenuActivity)getActivity()).getSubmenuPage());
  }

  @Override public void onLoadFinished(Loader<ArrayList<Object>> loader,
      ArrayList<Object> dataset) {
    // Swap the new cursor in. (The framework will take care of closing the
    // old cursor once we return.)
    mAdapter.clear();
    mAdapter.addAll(dataset);
    mAdapter.notifyDataSetChanged();
  }

  @Override public void onLoaderReset(Loader<ArrayList<Object>> loader) {
    // This is called when the last Cursor provided to onLoadFinished()
    // above is about to be closed. We need to make sure we are no
    // longer using it.
    mAdapter.clear();
  }

  @TargetApi(23)
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    // Make sure that container activity implement the callback interface
    try {
      mCallback = (DataPassListener)context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString()
              + " must implement DataPassListener");
    }
  }

  @SuppressWarnings("deprecation")
  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (Build.VERSION.SDK_INT < 23) {
      try {
        mCallback = (DataPassListener)activity;
      } catch (ClassCastException e) {
        throw new ClassCastException(activity.toString()
                + " must implement DataPassListener");
      }
    }
  }

  public InstanceInfoListAdapter getAdapter() {
    return mAdapter;
  }
}
