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

package org.opendatakit.survey.utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.opendatakit.survey.R;

import java.util.ArrayList;

/**
 * Implementation of cursor adapter that displays instance info
 *
 */
public class InstanceInfoListAdapter extends BaseAdapter {

  private final Context mContext;
  private final int mLayout;
  private final int mSavepointTimestamp;
  private final int mBeneficiaryInformation;
  private final int mQuestionsLeft;
  private final ArrayList<Object> mItems = new ArrayList<Object>();

  private static final String TAG = InstanceInfoListAdapter.class.getSimpleName();
  private static final int TYPE_PERSON = 0;
  private static final int TYPE_DIVIDER = 1;

  public InstanceInfoListAdapter(Context context, int layout,int savepoint_timestamp_id,
                                 int beneficiary_information_id, int questions_left_id) {
    this.mContext = context;
    this.mLayout = layout;
    this.mSavepointTimestamp = savepoint_timestamp_id;
    this.mBeneficiaryInformation = beneficiary_information_id;
    this.mQuestionsLeft = questions_left_id;
  }

  public void clear() {
    mItems.clear();
  }

  public void addAll(ArrayList<Object> items) {
    mItems.addAll(items);
  }

  @Override
  public int getCount() {
    return mItems.size();
  }

  @Override
  public Object getItem(int position) {
    return mItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return Integer.MAX_VALUE - position;
  }

  @Override
  public int getItemViewType(int position) {
    if (getItem(position) instanceof InstanceInfo) {
      return TYPE_PERSON;
    }

    return TYPE_DIVIDER;
  }

  @Override
  public boolean isEnabled(int position) {
    return (getItemViewType(position) == TYPE_PERSON);
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    int type = getItemViewType(position);
    if (view == null) {
      LayoutInflater layoutInflater =
              (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      switch (type) {
        case TYPE_PERSON:
          view = layoutInflater.inflate(mLayout, parent, false);
          break;
        case TYPE_DIVIDER:
          view = layoutInflater.inflate(R.layout.row_header, parent, false);
          break;
      }
    }
    switch (type) {
      case TYPE_PERSON:
    InstanceInfo info = (InstanceInfo) mItems.get(position);

    //if ( mFormLastUpdateDateId != -1) {
      TextView formDateView = (TextView) view.findViewById(mSavepointTimestamp);
      formDateView.setText(info.savepointTimestamp);
   // }
    TextView formBeneficiaryView = (TextView) view.findViewById(mBeneficiaryInformation);
    formBeneficiaryView.setText(info.beneficiaryInformation);
    TextView formCountView = (TextView) view.findViewById(mQuestionsLeft);
    formCountView.setText(String.valueOf(info.questionsLeft));

        PieGraph pg = (PieGraph)view.findViewById(R.id.graph);
        if(pg.getSlices().size()!=2) {
          PieSlice slice = new PieSlice();
          slice.setColor(Color.parseColor("#99CC00"));
          slice.setValue(info.questionsFulfilled);
          pg.addSlice(slice);
          slice = new PieSlice();
          slice.setColor(Color.parseColor("#FFBB33"));
          slice.setValue(info.questionsLeft);
          pg.addSlice(slice);
        }

    /*if ( mTableIdFormVersionId != -1 ) {
      TextView v = (TextView) view.findViewById(mTableIdFormVersionId);
      v.setVisibility(View.VISIBLE);
      if ( info.formVersion != null) {
        v.setText(mContext.getString(R.string.table_id_form_id_version, info.tableId, info.formId,
            info.formVersion));
      } else {
        v.setText(mContext.getString(R.string.table_id_form_id, info.tableId, info.formId));
      }
    }*/
    break;
      case TYPE_DIVIDER:
        TextView title = (TextView)view.findViewById(R.id.headerTitle);
        String titleString = (String)getItem(position);
        title.setText(titleString);
        break;
    }

    return view;
  }

}