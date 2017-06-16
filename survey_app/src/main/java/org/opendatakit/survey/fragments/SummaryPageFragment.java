package org.opendatakit.survey.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.DataTableColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.InstanceListLoader;
import org.opendatakit.survey.utilities.PieGraph;
import org.opendatakit.survey.utilities.PieSlice;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class SummaryPageFragment extends Fragment {
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CHOOSEN_TABLE_ID = "table_id";
    private static final String INSTANCE_UUID = "instance_uuid";

    private String firstname;
    private String lastname;
    private String formId;
    private String tableId;
    private HashMap<String, Object> formDef;
    private String appName;
    private Cursor instanceCursor = null;
    private int[] colors = new int[3];
    private String questionsLeft;
    private String dateFormatted;
    private SimpleDateFormat sdf;
    private static final String t = "SummaryPageFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            firstname = getArguments().getString(FIRSTNAME);
            lastname = getArguments().getString(LASTNAME);
            formId = getArguments().getString(INSTANCE_UUID);
            tableId = getArguments().getString(CHOOSEN_TABLE_ID);
        }

        sdf = new SimpleDateFormat(getActivity().getString(R.string
                .european_date_format));
        appName = ((MainMenuActivity) getActivity()).getAppName();
        File formDirectory = new File(ODKFileUtils.getFormFolder(appName, tableId, tableId));
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

        Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                + tableId);
        do{
            WebLogger.getLogger(appName).i(t, "Waiting 100 miliseconds and checking if the new row already exists so we can read data from it");
            android.os.SystemClock.sleep(100);
            instanceCursor = getActivity().getContentResolver().query(formUri, null, "_id=?", new String[]{formId}, null);
        } while (!instanceCursor.moveToFirst());

        if (instanceCursor != null) {
            colors = InstanceListLoader.countStoplightAnswers(instanceCursor);
            questionsLeft = String.valueOf(InstanceListLoader.countEmptyAndFilledColumns(instanceCursor)[0]);
            int idxSavepointTimestamp = instanceCursor.getColumnIndex(DataTableColumns.SAVEPOINT_TIMESTAMP.getText());
            dateFormatted = sdf.format((new Date(TableConstants.milliSecondsFromNanos(instanceCursor.getString(idxSavepointTimestamp)))));
        }
        if (instanceCursor != null && !instanceCursor.isClosed()) {
            instanceCursor.close();
        }

        String currentDateandTime = sdf.format(new Date());

        View view = inflater.inflate(R.layout.fragment_summary_page, container, false);
        TextView title = (TextView) view.findViewById(R.id.instanceTitle);
        title.setText(firstname + " " + lastname + " - " + currentDateandTime);


        PieGraph pg = (PieGraph) view.findViewById(R.id.summary_graph);
        int red = colors[0];
        int yellow = colors[1];
        int green = colors[2];
        pg.removeSlices();
        pg.setInnerCircleRatio(100);
        if (red == 0 && green == 0 && yellow == 0) {
            PieSlice slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.pie_graph_empty));
            slice.setValue(1);
            pg.addSlice(slice);
        } else {
            PieSlice slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.submitted_donut_char_green));
            slice.setValue(colors[2]);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.submitted_donut_char_yellow));
            slice.setValue(colors[1]);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.submitted_donut_char_red));
            slice.setValue(colors[0]);
            pg.addSlice(slice);
        }

        TextView formCountView = (TextView) view.findViewById(R.id.instanceQuestionsLeft);
        formCountView.setText(questionsLeft);
        TextView formDateView = (TextView) view.findViewById(R.id.instanceSavepointTimestamp);
        formDateView.setText(dateFormatted);

        return view;
    }
}
