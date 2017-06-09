package org.opendatakit.survey.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.logic.FormIdStruct;
import org.opendatakit.survey.utilities.DataPassListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SummaryPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CHOOSEN_TABLE_ID = "table_id";
    private static final String CHOOSEN_FORM_ID = "form_id";

    // TODO: Rename and change types of parameters
    private String firstname;
    private String lastname;
    private String formId;
    private String tableId;
    private HashMap<String, Object> formDef;
    private  String appName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstname = getArguments().getString(FIRSTNAME);
            lastname = getArguments().getString(LASTNAME);
            formId = getArguments().getString(CHOOSEN_FORM_ID);
            tableId = getArguments().getString(CHOOSEN_TABLE_ID);
        }

        appName = ((MainMenuActivity)getActivity()).getAppName();
        File formDirectory = new File( ODKFileUtils.getFormFolder(appName, tableId, formId) );
        File formDefFile = new File(formDirectory, ODKFileUtils.FORMDEF_JSON_FILENAME);

        HashMap<String, Object> om = null;
        try {
            om = ODKFileUtils.mapper.readValue(formDefFile, HashMap.class);
        } catch (JsonParseException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        } catch (JsonMappingException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        } catch (IOException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        }
        formDef = om;
        if (formDef == null) {
            throw new IllegalArgumentException("File is not a json file! "
                    + formDefFile.getAbsolutePath());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_page, container, false);
    }

}
