package org.opendatakit.survey.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;

import java.io.File;

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

        // Set onFocusChangeListeners to detect when to hide the soft keyboard
        reporterNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        reporterIdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        officeIdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });
        syncServerUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(view);
                }
            }
        });

        // Get saved properties singleton
        properties = CommonToolProperties.get(this.getActivity().getApplicationContext(), appName);
        if (!properties.getProperty(CommonToolProperties.KEY_REPORTER_NAME).equals("undefined")) {
            reporterNameEditText.setText(properties.getProperty(CommonToolProperties.KEY_REPORTER_NAME));
        } else {
            reporterNameEditText.setText("");
        }
        if (!properties.getProperty(CommonToolProperties.KEY_REPORTER_ID).equals("undefined")) {
            reporterIdEditText.setText(properties.getProperty(CommonToolProperties.KEY_REPORTER_ID));
        } else {
            reporterIdEditText.setText("");
        }
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
                // Force soft keyboard to hide
                hideKeyboard(getActivity().getCurrentFocus());

                // Check if we changed the Office ID
                String oldOfficeId = properties.getProperty(CommonToolProperties.KEY_OFFICE_ID);
                if (!officeIdEditText.getText().toString().equals(oldOfficeId)) {
                    showConfirmDialog();
                } else {
                    saveSettings();
                }
                break;
        }
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_change_office_id);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAllForms();
                saveSettings();
            }
        });
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void deleteAllForms() {
        WebLogger.getLogger(appName).i(t, "Deleting all data related to the officeID: " + properties.getProperty(CommonToolProperties.KEY_OFFICE_ID));
        File tables = new File(Environment.getExternalStorageDirectory() + "/opendatakit/" + appName + "/config/tables");
        File data = new File(Environment.getExternalStorageDirectory() + "/opendatakit/" + appName + "/data");
        try {
            FileUtils.cleanDirectory(tables);
            FileUtils.cleanDirectory(data);
        } catch (Exception e) {
            e.printStackTrace();
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
        MainMenuActivity activity = (MainMenuActivity)getActivity();
        activity.popBackStack();
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
