package org.opendatakit.survey.utilities;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.QuestionInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 16.06.17.
 */
public class QuestionListLoader  extends AsyncTaskLoader<ArrayList<Object>> {

    private static final String FORMDEF_TITLE_ELEMENT = "title";
    private static final String FORMDEF_DISPLAY_ELEMENT = "display";
    private static final String FORMDEF_SURVEY_SETTINGS = "survey";
    private static final String FORMDEF_SETTINGS_SUBSECTION = "settings";
    private static final String FORMDEF_SPECIFICATION_SECTION = "specification";
    private static final String FORMDEF_SECTION_NAMES = "section_names";

    private final String appName;
    private final String tableId;
    private final String formId;
    private HashMap<String, Object> formDef;
    private HashMap<String, Map<String, Object>> headersDisplayNames = new HashMap();
    private List<String> sectionNames;
    private Context context;

    public QuestionListLoader(Context context, String appName, String tableId, String formId) {
        super(context);
        this.appName = appName;
        this.tableId = tableId;
        this.context = context;
        this.formId = formId;
    }


    @Override
    public ArrayList<Object> loadInBackground() {
        ArrayList<Object> result = new ArrayList();

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

        Map<String, Object> specification = (Map<String, Object>) formDef
                .get(FORMDEF_SPECIFICATION_SECTION);
        if (specification == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> settings = (Map<String, Object>) specification
                .get(FORMDEF_SETTINGS_SUBSECTION);
        if (settings == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No settings section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        //we have a list of sections
        sectionNames = (List<String>)specification.get(FORMDEF_SECTION_NAMES);
        //TODO: check if null and throw smth
        sectionNames.remove("initial");

        //now lets store somewhere displaynames of those for different languages mapa map ?
        for(String sectionName : sectionNames){
            Map<String, Object> settingsOfSeciotn = (Map<String, Object>) settings
                    .get(sectionName);
            //TODO: if null throw smth...
            Map<String, Object> display = (Map<String, Object>) settingsOfSeciotn
                    .get("display");
            //TODO: if null throw smth...
            Map<String, Object> titles = (Map<String, Object>) display
                    .get("title");
            //TODO: if null throw smth...

            headersDisplayNames.put(sectionName, titles);
        }

        Map<String, Object> sections = (Map<String, Object>) specification
                .get("sections");
        if (settings == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No settings section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                + tableId);
        Cursor instanceCursor = context.getContentResolver().query(formUri, null, "_id=?", new String[]{formId}, null);

        if(instanceCursor!=null && instanceCursor.moveToFirst()) {

            //ok seems like we have it
            //sooo we need to somehow switch the language we want in future TODO:different languages
            //for now return default
            for (final String sectionName : sectionNames) {
                result.add(headersDisplayNames.get(sectionName).get("default"));
                //time for sum questions, right here cause we have to maintain the order
                Map<String, Object> section = (Map<String, Object>) sections
                        .get(sectionName);
                //TODO: if null throw smth...
                List<Map<String, Object>> prompts = (List<Map<String, Object>>) section
                        .get("prompts");
                //TODO: if null throw smth...
                for (Map<String, Object> prompt : prompts) {
                    boolean hide = false;// we display only those that have not set hideincontents flag to 1
                    Object hideObj = prompt.get("hideInContents");
                    if (prompt.containsKey("hideInContents")) {
                        if (hideObj instanceof Boolean)
                            hide = (boolean) hideObj;
                        else if (hideObj instanceof Integer && (Integer) hideObj == 1)
                            hide = true;
                    }

                    if (prompt.containsKey("_type")) {
                        String type = (String) prompt.get("_type");
                        if (type.equals("note") || type.equals("_section") || type.equals("contents")) {
                            hide = true;
                        }
                    }
                    if (hide != true) {

                        Map<String, Object> display = (Map<String, Object>) prompt
                                .get("display");
                        //TODO: if null throw smth...
                        HashMap<String, String> text = (HashMap<String, String>) display
                                .get("text");
                        result.add(new QuestionInfo((String) prompt.get("name"), text, (String) prompt.get("_branch_label_enclosing_screen"), 0, instanceCursor.getString(instanceCursor.getColumnIndex((String)prompt.get("name")))));
                        //result.add("\tQ: " + text.get("default"));

                    }
                }

                //result.add(new QuestionInfo(text.get("default"), ));

            }

        }



        return result;
    }

    @Override protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
