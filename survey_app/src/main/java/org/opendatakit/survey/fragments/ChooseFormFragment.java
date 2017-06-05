package org.opendatakit.survey.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.services.preferences.activities.IOdkAppPropertiesActivity;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.FormInfoListAdapter;
import org.opendatakit.survey.utilities.FormListLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChooseFormFragment extends ListFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<ArrayList<Object>>{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CHOOSEN_TABLE_ID = "table_id";

    private String firstname;
    private String lastname;
    private FormInfoListAdapter mAdapter;
    Button nextButton = null;

    BeneficiaryInformationFragment.DataPassListener mCallback;
    PropertiesSingleton props;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstname = getArguments().getString(FIRSTNAME);
            lastname = getArguments().getString(LASTNAME);
        }
        props = ((IOdkAppPropertiesActivity) this.getActivity()).getProps();
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new FormInfoListAdapter(getActivity(), R.layout.choose_form_row, R.id.updateDate,
               R.id.questions);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SimpleDateFormat sdf = new SimpleDateFormat(getActivity().getString(R.string
                .european_date_format));
        String currentDateandTime = sdf.format(new Date());
        String lastUpdateTime = props.getProperty(CommonToolProperties.LAST_FORMS_UPDATE_TIME);

        View view = inflater.inflate(R.layout.fragment_choose_form, container, false);
        TextView test = (TextView) view.findViewById(R.id.beneficiaryTitle);
        test.setText(firstname + " " + lastname + " - " + currentDateandTime);
        TextView updateTime = (TextView) view.findViewById(R.id.lastUpdateTime);
        updateTime.setText(calculateTimeFromLastUpdate(lastUpdateTime));
        Button checkForUpdatesButton = (Button) view.findViewById(R.id.checkForUpdatesButton);
        Button cancelButton = (Button) view.findViewById(R.id.chooseFormCancelButton);
        nextButton = (Button) view.findViewById(R.id.chooseFormNextButton);
        cancelButton.setOnClickListener(this);
        checkForUpdatesButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        nextButton.setEnabled(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseFormCancelButton:
                getActivity().onBackPressed();
                break;
            case R.id.chooseFormNextButton:
                HashMap<String, String> values = new HashMap<>();
                values.put(FIRSTNAME, firstname);
                values.put(LASTNAME, lastname);
                values.put(CHOOSEN_TABLE_ID, mAdapter.getSelectedTabeID());
                mCallback.passData(values);
                ((MainMenuActivity)getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.SUMMARY_PAGE);
                break;
            case  R.id.checkForUpdatesButton:
                props.setProperty(CommonToolProperties.LAST_FORMS_UPDATE_TIME, new Date().toString());
                Toast.makeText(getActivity(), "Here we should download forms and later update the Last updated indicator", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override public Loader<ArrayList<Object>> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created. This
        // sample only has one Loader, so we don't care about the ID.
        return new FormListLoader(getActivity(), ((IAppAwareActivity) getActivity()).getAppName());
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mAdapter.setSelectedIndex(position);
        if(!nextButton.isEnabled())
            nextButton.setEnabled(true);
        mAdapter.notifyDataSetChanged();
    }

    private String calculateTimeFromLastUpdate(String lastUpdateTime) {
        if(lastUpdateTime == null || lastUpdateTime.isEmpty()) {
            return getActivity().getString(R.string.never);
        }
        Date lastUpdate = new Date(lastUpdateTime);
        Date currentTime = new Date();
        long diff = currentTime.getTime() - lastUpdate.getTime();
        long seconds = diff/1000;
        long minutes = seconds/60;
        long hours = minutes/60;
        long days = hours/24;
        if(seconds < 60) {
            return getActivity().getString(R.string.just_now);
        } else if (minutes >= 1) {
            return minutes +  " " + getActivity().getString(R.string.minutes) + " " + getActivity().getString(R.string.ago);
        } else if (hours >= 1) {
            return hours + " " + getActivity().getString(R.string.hours) + " " + getActivity().getString(R.string.ago);
        } else {
            return days + " " + getActivity().getString(R.string.days) + " " + getActivity().getString(R.string.ago);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure that container activity implement the callback interface
        try {
            mCallback = (BeneficiaryInformationFragment.DataPassListener)context;
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
                mCallback = (BeneficiaryInformationFragment.DataPassListener)activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement DataPassListener");
            }
        }
    }

}
