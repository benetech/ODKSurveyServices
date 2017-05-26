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
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.IOdkSurveyActivity;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.InstanceInfo;
import org.opendatakit.survey.utilities.FormListLoader;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.opendatakit.survey.utilities.InstanceInfoListAdapter;

import java.util.ArrayList;

/**
 * Fragment displaying the list of available forms to fill out.
 *
 * @author mitchellsundt@gmail.com
 */
public class InProgressInstancesFragment extends ListFragment
    implements LoaderManager.LoaderCallbacks<ArrayList<Object>> {

  @SuppressWarnings("unused") private static final String t = "InProgressInstancesFragment";
  private static final int FORM_CHOOSER_LIST_LOADER = 0x02;

  public static final int ID = R.layout.fragment_in_progress_instances;

  // data to retain across orientation changes

  // data that is not retained

  private InstanceInfoListAdapter mAdapter;
  private View view;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // render total instance view
    mAdapter = new InstanceInfoListAdapter(getActivity(), R.layout.in_progress_row, R.id.savepointTimestamp,
            R.id.beneficiaryName, R.id.questionsLeft, ((MainMenuActivity)getActivity()).getSubmenuPage());
    setListAdapter(mAdapter);

    getLoaderManager().initLoader(FORM_CHOOSER_LIST_LOADER, null, this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(ID, container, false);

    FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.newFormFloatingActionButton);
    fab.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Toast.makeText(getActivity(), "here we should take user to new form fullfilment when it will be ready", Toast.LENGTH_SHORT).show();
      }
    });

    return view;
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    // get uri to form
    InstanceInfo info = (InstanceInfo) mAdapter.getItem(position);
    Uri formUri = Uri.withAppendedPath(Uri.withAppendedPath(
        Uri.withAppendedPath(FormsProviderAPI.CONTENT_URI,
            ((IAppAwareActivity) getActivity()).getAppName()), info.tableId), info.formId);

    ((IOdkSurveyActivity) getActivity()).chooseForm(formUri);
  }

  @Override public Loader<ArrayList<Object>> onCreateLoader(int id, Bundle args) {
    // This is called when a new Loader needs to be created. This
    // sample only has one Loader, so we don't care about the ID.
    return new FormListLoader(getActivity(), ((IAppAwareActivity) getActivity()).getAppName(), ((MainMenuActivity)getActivity()).getSubmenuPage());
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
}
