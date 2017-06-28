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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.opendatakit.demoAndroidlibraryClasses.consts.IntentConsts;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of cursor adapter that displays instance info
 *
 */
public class InstanceInfoListAdapter extends BaseAdapter {

  private static final String TAG = InstanceInfoListAdapter.class.getSimpleName();


  private final Context mContext;
  private final int mLayout;
  private final int mSavepointTimestamp;
  private final int mBeneficiaryInformation;
  private final int mQuestionsLeft;
  private final ArrayList<Object> mItems = new ArrayList<Object>();

  private static final int INSTANCE = 0;
  private static final int TYPE_DIVIDER = 1;

  public InstanceInfoListAdapter(Context context, int layout, int savepoint_timestamp_id,
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

        final InstanceInfo info = (InstanceInfo) mItems.get(position);
        TextView formBeneficiaryView = (TextView) view.findViewById(mBeneficiaryInformation);
        formBeneficiaryView.setText(info.beneficiaryInformation);
        PieGraph pg = (PieGraph) view.findViewById(R.id.graph);

        if (((MainMenuActivity)mContext).getSubmenuPage().equals("new_row")) {
          ImageView cloud = (ImageView) view.findViewById(R.id.cloud);

          cloud.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              System.out.println("Cloud clicked!");

//              Activity activity = (Activity) mContext;
//
//              Intent bind_intent = new Intent();
//              bind_intent.setClassName(IntentConsts.Sync.APPLICATION_NAME,
//                      IntentConsts.Sync.SYNC_SERVICE_CLASS);
//              mContext.bindService(bind_intent, mConnnection,
//                      Context.BIND_AUTO_CREATE | ((Build.VERSION.SDK_INT >= 14) ?
//                              Context.BIND_ADJUST_WITH_ACTIVITY :
//                              0));
//
//              ((ISyncServiceInterfaceActivity)activity)
//                      .invokeSyncInterfaceAction(new DoSyncActionCallback() {
//                        @Override public void doAction(OdkSyncServiceInterface syncServiceInterface)
//                                throws RemoteException {
//                          if ( syncServiceInterface != null ) {
//                            final SyncStatus status = syncServiceInterface.getSyncStatus(ODKFileUtils.getOdkDefaultAppName());
//                            //final SyncProgressEvent event = syncServiceInterface.getSyncProgressEvent(getAppName());
//                            if (status == SyncStatus.SYNCING) {
//                              syncAction = SyncActions.MONITOR_SYNCING;
//
////                              handler.post(new Runnable() {
////                                @Override
////                                public void run() {
////                                  showProgressDialog(status, event.progressState,
////                                          event.progressMessageText, event.curProgressBar, event.maxProgressBar);
////                                }
////                              });
//                              return;
//                            }
//
//                            switch (syncAction) {
//                              case SYNC:
//                                syncServiceInterface.synchronizeWithServer(ODKFileUtils.getOdkDefaultAppName(), syncAttachmentState);
//                                syncAction = SyncActions.MONITOR_SYNCING;
//
////                                handler.post(new Runnable() {
////                                  @Override
////                                  public void run() {
////                                    showProgressDialog(SyncStatus.NONE, null, getString(R.string.sync_starting), -1, 0);
////                                  }
////                                });
//                                break;
//                              default:
//                                break;
//                            }
//                          } else {
////                            WebLogger.getLogger(getAppName()).d(TAG, "[" + getId() + "] [tickleInterface] syncServiceInterface == null");
////                            // The service is not bound yet so now we need to try again
////                            handler.postDelayed(new Runnable() {
////                              @Override
////                              public void run() {
////                                tickleInterface();
////                              }
////                            }, 100);
//                          }
//                        }
//                      });

              //TODO: Skip SyncActivity and invoke service methods directly

              ArrayList<String> ids = new ArrayList<String>();
              ids.add(info.UUID);

              Map<String, ArrayList<String>> listToSync = new HashMap<>();
              listToSync.put(info.tableId, ids);
              Intent syncIntent = new Intent();
              syncIntent.setComponent(new ComponentName(
                      IntentConsts.Sync.APPLICATION_NAME,
                      IntentConsts.Sync.ACTIVITY_NAME));
              syncIntent.setAction(Intent.ACTION_DEFAULT);
              Bundle bundle = new Bundle();
              bundle.putString(IntentConsts.INTENT_KEY_APP_NAME, ODKFileUtils.getOdkDefaultAppName());
              syncIntent.putExtras(bundle);
              for (Map.Entry<String, ArrayList<String>> entry : listToSync.entrySet()) {
                bundle.putSerializable(entry.getKey(), entry.getValue());
              }
              syncIntent.putExtras(bundle);

              mContext.startActivity(syncIntent);
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