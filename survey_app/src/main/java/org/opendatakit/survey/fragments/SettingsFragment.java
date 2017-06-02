package org.opendatakit.survey.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static final String t = "SettingsFragment";

    private View view;
    private PropertiesSingleton properties;
    private EditText reporterNameEditText;
    private EditText reporterIdEditText;
    private EditText officeIdEditText;
    private EditText syncServerUrlEditText;
    private String appName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        appName = ((MainMenuActivity)getActivity()).getAppName();
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button saveButton = (Button) view.findViewById(R.id.settings_save_button);
        saveButton.setOnClickListener(this);

        reporterNameEditText = (EditText) view.findViewById(R.id.reporter_name_edit_text);
        reporterIdEditText = (EditText) view.findViewById(R.id.reporter_id_edit_text);
        officeIdEditText = (EditText) view.findViewById(R.id.office_id_edit_text);
        syncServerUrlEditText = (EditText) view.findViewById(R.id.sync_server_url_edit_text);

        // Get saved properties singleton
        properties = CommonToolProperties.get(this.getActivity().getApplicationContext(), appName);

        reporterNameEditText.setText(properties.getProperty(CommonToolProperties.KEY_REPORTER_NAME));
        reporterIdEditText.setText(properties.getProperty(CommonToolProperties.KEY_REPORTER_ID));
        officeIdEditText.setText(properties.getProperty(CommonToolProperties.KEY_OFFICE_ID));
        syncServerUrlEditText.setText(properties.getProperty(CommonToolProperties.KEY_SYNC_SERVER_URL));

        // Enable automatic resizing of the fragment space when the keyboard is visible so it does
        // not hide the editText fields underneath it
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WebLogger.getLogger(appName).i(t, "Starting fragment view");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_save_button:
                saveSettings();
                break;
        }
    }

    private void saveSettings() {
        WebLogger.getLogger(appName).i(t, "Start saving settings in properties");

        // Set properties which hold setting to be saved
        properties.setProperty(CommonToolProperties.KEY_REPORTER_NAME, reporterNameEditText.getText().toString());
        properties.setProperty(CommonToolProperties.KEY_REPORTER_ID, reporterIdEditText.getText().toString());
        properties.setProperty(CommonToolProperties.KEY_OFFICE_ID, officeIdEditText.getText().toString());
        properties.setProperty(CommonToolProperties.KEY_SYNC_SERVER_URL, syncServerUrlEditText.getText().toString());

        // Save properties
        try {
            properties.writeProperties();

            Toast.makeText(this.getActivity(),
                    getString(R.string.save_settings_success),
                    Toast.LENGTH_LONG).show();
            WebLogger.getLogger(appName).i(t, "Settings saved");
        } catch (Exception e) {
            Toast.makeText(this.getActivity(),
                    getString(R.string.save_settings_failure),
                    Toast.LENGTH_LONG).show();
            WebLogger.getLogger(appName).e(t, "Error while saving settings");
            WebLogger.getLogger(appName).printStackTrace(e);

        }

        // Return to the previous view
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
