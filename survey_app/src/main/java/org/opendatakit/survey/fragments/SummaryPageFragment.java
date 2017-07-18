package org.opendatakit.survey.fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.opendatakit.aggregate.odktables.rest.TableConstants;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.DataTableColumns;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.FormDefSections;
import org.opendatakit.survey.utilities.InstanceListLoader;
import org.opendatakit.survey.utilities.PieGraph;
import org.opendatakit.survey.utilities.PieSlice;
import org.opendatakit.survey.utilities.QuestionInfo;
import org.opendatakit.survey.utilities.QuestionInfoListAdapter;
import org.opendatakit.survey.utilities.QuestionListLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SummaryPageFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Object>>, View.OnClickListener {
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CHOOSEN_TABLE_ID = "table_id";
    private static final String INSTANCE_UUID = "instance_uuid";

    private String firstname;
    private String lastname;
    private String formId;
    private String tableId;
    private String appName;
    private Cursor instanceCursor = null;
    private int[] colors = new int[3];
    private String questionsLeft;
    private String dateFormatted;
    private SimpleDateFormat sdf;
    private static final String t = "SummaryPageFragment";
    private QuestionInfoListAdapter adapter;
    private HashMap<String, String> languages;

    private TextView title;
    private PieGraph pg;
    private TextView formCountView;
    private TextView formDateView;
    private View view;
    private Uri formUri;
    private MenuItem menu;
    private String language;

    //TODO: here pass language to loader and handle it there
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainMenuActivity)getActivity()).setLocale(getLanguage());

        if (getArguments() != null) {
            firstname = getArguments().getString(FIRSTNAME);
            lastname = getArguments().getString(LASTNAME);
            formId = getArguments().getString(INSTANCE_UUID);
            tableId = getArguments().getString(CHOOSEN_TABLE_ID);
        }

        languages = new HashMap<>();
        sdf = new SimpleDateFormat(getActivity().getString(R.string
                .european_date_format));
        appName = ((MainMenuActivity) getActivity()).getAppName();
        if (appName == null || appName.length() == 0) {
            appName = ODKFileUtils.getOdkDefaultAppName();
        }

        formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                + tableId);

        File formDirectory = new File(ODKFileUtils.getFormFolder(appName, tableId, tableId));
        File formDefFile = new File(formDirectory, ODKFileUtils.FORMDEF_JSON_FILENAME);

        HashMap<String, Object> formDef = null;
        try {
            formDef = ODKFileUtils.mapper.readValue(formDefFile, HashMap.class);
        } catch (JsonParseException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        } catch (JsonMappingException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        } catch (IOException e) {
            WebLogger.getLogger(appName).printStackTrace(e);
        }

        Map<String, Object> specification = (Map<String, Object>) formDef
                .get(FormDefSections.SPECIFICATION_SECTION.getText());
        if (specification == null) {
        throw new IllegalArgumentException("File is not a formdef json file! No specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> settings = (Map<String, Object>) specification
                .get(FormDefSections.SETTINGS_SUBSECTION.getText());
        if (settings == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No settings section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        ArrayList<Map<String, Object>> locales = (ArrayList<Map<String, Object>>)((Map<String, Object>) settings
                .get(FormDefSections.LOCALES.getText())).get("value");
        if (locales == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No locales section inside settings element."
                    + formDefFile.getAbsolutePath());
        }

        for (Map<String, Object> locale : locales){
            HashMap<String, Object> display = (HashMap<String, Object>) locale.get(FormDefSections.DISPLAY.getText());
            if (display == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No display section inside locales element."
                        + formDefFile.getAbsolutePath());
            }
            HashMap<String, Object> text = (HashMap<String, Object>) display.get(FormDefSections.TEXT.getText());
            if (text == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No text section inside display element."
                        + formDefFile.getAbsolutePath());
            }
            languages.put((String) text.get(FormDefSections.DEFAULT.getText()), (String)locale.get(FormDefSections.NAME.getText()));
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        for (Map.Entry<String, String> field : languages.entrySet()) {
            this.menu = menu.add(field.getKey());
        }
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new QuestionInfoListAdapter(getActivity(), this);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_summary_page, container, false);
        Button doneButtonView = (Button) view.findViewById(R.id.done_button);
        doneButtonView.setOnClickListener(this);

        return view;
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        QuestionInfo info = (QuestionInfo) adapter.getItem(position);

        Uri formUri = Uri.withAppendedPath(
                Uri.withAppendedPath(FormsProviderAPI.CONTENT_URI,
                        ((IAppAwareActivity) getActivity()).getAppName()), tableId);

        Uri uri = Uri.parse(formUri.toString() + "/instanceId=" + formId + "&screenPath=" + info.path);
        ((MainMenuActivity) getActivity()).chooseForm(uri);
    }

    @Override
    public Loader<ArrayList<Object>> onCreateLoader(int id, Bundle args) {
        return new QuestionListLoader(getActivity(), ((IAppAwareActivity) getActivity()).getAppName(), tableId, formId);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Object>> loader, ArrayList<Object> data) {
        adapter.clear();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        ((MainMenuActivity) getActivity()).populateScreenList(adapter.getAll());
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Object>> loader) {
        adapter.clear();
    }

    @Override public void onResume() {
        super.onResume();

        do{
            WebLogger.getLogger(appName).i(t, "Waiting 100 miliseconds and checking if the new row already exists so we can read data from it");
            android.os.SystemClock.sleep(100);
            instanceCursor = getActivity().getContentResolver().query(formUri, null, "_id=?", new String[]{formId}, null);
        } while (instanceCursor != null && !instanceCursor.moveToFirst());

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

        title = (TextView) view.findViewById(R.id.instanceTitle);
        //In case when we have whole beneficiary information in firstname from InProgressInstancesFragment
        if (lastname != null) {
            title.setText(firstname + " " + lastname + " - " + currentDateandTime);
        } else {
            title.setText(firstname + " - " + currentDateandTime);
        }


        pg = (PieGraph) view.findViewById(R.id.summary_graph);
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
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.poverty_stoplight_green));
            slice.setValue(colors[2]);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.poverty_stoplight_yellow));
            slice.setValue(colors[1]);
            pg.addSlice(slice);
            slice = new PieSlice();
            slice.setColor(ContextCompat.getColor(getActivity(), R.color.poverty_stoplight_red));
            slice.setValue(colors[0]);
            pg.addSlice(slice);
        }

        formCountView = (TextView) view.findViewById(R.id.instanceQuestionsLeft);
        formCountView.setText(questionsLeft);
        formDateView = (TextView) view.findViewById(R.id.instanceSavepointTimestamp);
        formDateView.setText(dateFormatted);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                if (((MainMenuActivity) getActivity()).getSubmenuPage().equals("synced")) {
                    ((MainMenuActivity) getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.SUBMITTED);
                } else if (((MainMenuActivity) getActivity()).getSubmenuPage().equals("new_row")){
                    ((MainMenuActivity) getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.IN_PROGRESS);
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        this.language = item.getTitle().toString();
        ((MainMenuActivity)getActivity()).setLocale(getLanguage());
        adapter.setLanguage(getLanguage());
        adapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    public String getLanguage(){
        if(language == null || language.isEmpty())
            return "default";
        else
            return languages.get(language);
    }
}
