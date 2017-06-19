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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;

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
  private final String submenuPage;
  private final ArrayList<Object> mItems = new ArrayList<Object>();

  private static final int INSTANCE = 0;
  private static final int TYPE_DIVIDER = 1;

  public InstanceInfoListAdapter(Context context, int layout, int savepoint_timestamp_id,
                                 int beneficiary_information_id, int questions_left_id,
                                 String submenuPage) {
    this.mContext = context;
    this.mLayout = layout;
    this.mSavepointTimestamp = savepoint_timestamp_id;
    this.mBeneficiaryInformation = beneficiary_information_id;
    this.mQuestionsLeft = questions_left_id;
    this.submenuPage = submenuPage;
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
      return INSTANCE;
    }

    return TYPE_DIVIDER;
  }

  @Override
  public boolean isEnabled(int position) {
    return (getItemViewType(position) == INSTANCE);
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    int type = getItemViewType(position);

    switch (type) {
      case INSTANCE:
        if (view == null) {
          LayoutInflater layoutInflater =
                  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          view = layoutInflater.inflate(mLayout, parent, false);
        }

        InstanceInfo info = (InstanceInfo) mItems.get(position);
        TextView formBeneficiaryView = (TextView) view.findViewById(mBeneficiaryInformation);
        formBeneficiaryView.setText(info.beneficiaryInformation);
        PieGraph pg = (PieGraph) view.findViewById(R.id.graph);

        if (submenuPage.equals("new_row")) {
          ImageView cloud = (ImageView) view.findViewById(R.id.cloud);

          cloud.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              System.out.println("Cloud clicked!");
            }
          });

          TextView formDateView = (TextView) view.findViewById(mSavepointTimestamp);
          formDateView.setText(info.savepointTimestamp);
          TextView formCountView = (TextView) view.findViewById(mQuestionsLeft);
          formCountView.setText(String.valueOf(info.questionsLeft));

          if (info.questionsLeft == 0) {
            pg.setVisibility(View.GONE);
            cloud.setVisibility(View.VISIBLE);
          } else {
            cloud.setVisibility(View.GONE);
            pg.setVisibility(View.VISIBLE);
          }

          pg.removeSlices();
          pg.setBorderColor(ContextCompat.getColor(mContext, R.color.in_progress_pie_chart_background));
          pg.setBorderSize(7);
          PieSlice slice = new PieSlice();
          slice.setColor(Color.WHITE);
          slice.setValue(info.questionsFulfilled);
          pg.addSlice(slice);
          slice = new PieSlice();
          slice.setColor(ContextCompat.getColor(mContext, R.color.in_progress_pie_chart_background));
          slice.setValue(info.questionsLeft);
          pg.addSlice(slice);

        } else {
          TextView formDateView = (TextView) view.findViewById(mSavepointTimestamp);
          formDateView.setText(info.savepointTimestamp);

          pg.setInnerCircleRatio(100);
          pg.removeSlices();
          if(info.greenAnswers == 0 && info.yellowAnswers == 0 || info.redAnswers == 0){
            PieSlice slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(mContext, R.color.pie_graph_empty));
            slice.setValue(1);
            pg.addSlice(slice);
          } else {
            PieSlice slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(mContext, R.color.submitted_donut_char_green));
            slice.setValue(info.greenAnswers);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(mContext, R.color.submitted_donut_char_yellow));
            slice.setValue(info.yellowAnswers);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(mContext, R.color.submitted_donut_char_red));
            slice.setValue(info.redAnswers);
            pg.addSlice(slice);
          }
        }
        break;

        case TYPE_DIVIDER:
          if (view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.row_header, parent, false);
          }
          TextView title = (TextView) view.findViewById(R.id.headerTitle);
          String titleString = (String) getItem(position);
          title.setText(titleString);
          break;
    }

    return view;
  }
}