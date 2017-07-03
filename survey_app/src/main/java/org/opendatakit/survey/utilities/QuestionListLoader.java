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
import org.opendatakit.survey.R;
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

    private static final String FORMDEF_SETTINGS_SUBSECTION = "settings";
    private static final String FORMDEF_SPECIFICATION_SECTION = "specification";
    private static final String FORMDEF_SECTION_NAMES = "section_names";
    private static final String FORMDEF_CHOICES = "choices";
    private static final String FORMDEF_SECTIONS = "sections";
    private static final String FORMDEF_DISPLAY = "display";
    private static final String FORMDEF_TITLE = "title";
    private static final String FORMDEF_TEXT = "text";
    private static final String FORMDEF_NESTED_SECTIONS = "nested_sections";
    private static final String FORMDEF_PROMPTS = "prompts";
    private static final String PATH = "_branch_label_enclosing_screen";

    private final String appName;
    private final String tableId;
    private final String formId;
    private HashMap<String, Object> formDef;
    private HashMap<String, HashMap<String, String>> headersDisplayNames = new HashMap();
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

        Map<String, Object> choices = (Map<String, Object>) specification
                .get(FORMDEF_CHOICES);
        if (choices == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No choices section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        //we have a list of sections
        sectionNames = (List<String>) specification.get(FORMDEF_SECTION_NAMES);
        if (sectionNames == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No section names inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        sectionNames.remove("initial");

        //now lets store somewhere displaynames of those for different languages
        for(String sectionName : sectionNames){
            Map<String, Object> settingsOfSeciotn = (Map<String, Object>) settings
                    .get(sectionName);
            if (settingsOfSeciotn == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No settings of section inside specification element."
                        + formDefFile.getAbsolutePath());
            }
            Map<String, Object> display = (Map<String, Object>) settingsOfSeciotn
                    .get(FORMDEF_DISPLAY);
            if (display == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No display section inside settings of section element."
                        + formDefFile.getAbsolutePath());
            }
            if(display.get(FORMDEF_TITLE) instanceof  String){
                throw new IllegalArgumentException("File is not a formdef json file! Make sure that elements are properly translated."
                        + formDefFile.getAbsolutePath());

            }
            HashMap<String, String> titles = (HashMap<String, String>) display
                    .get(FORMDEF_TITLE);
            if (titles == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No titles section inside display section element."
                        + formDefFile.getAbsolutePath());
            }

            headersDisplayNames.put(sectionName, titles);
        }

        Map<String, Object> sections = (Map<String, Object>) specification
                .get(FORMDEF_SECTIONS);
        if (sections == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No sections section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                + tableId);
        Cursor instanceCursor = context.getContentResolver().query(formUri, null, "_id=?", new String[]{formId}, null);

        if(instanceCursor!=null && instanceCursor.moveToFirst()) {
            for (final String sectionName : sectionNames) {
                Map<String, Object> section = (Map<String, Object>) sections
                        .get(sectionName);
                if (section == null) {
                    throw new IllegalArgumentException("File is not a formdef json file! No sections inside section but it was mentioned in section names specification element."
                            + formDefFile.getAbsolutePath());
                }

                //if this is a section that contains all of the others, let's call it menu, we don't want to display it here
                if(((Map<String, Object>)section.get(FORMDEF_NESTED_SECTIONS)).size() == sections.size() -2)
                    continue;

                result.add(new QuestionInfo(null, headersDisplayNames.get(sectionName), null, 2, null));


                List<Map<String, Object>> prompts = (List<Map<String, Object>>) section
                        .get(FORMDEF_PROMPTS);
                if (prompts == null) {
                    throw new IllegalArgumentException("File is not a formdef json file! No prompts section inside section element."
                            + formDefFile.getAbsolutePath());
                }

                for (Map<String, Object> prompt : prompts) {
                    boolean hide = false;// we display only those that have not set hideincontents flag to 1 or true #odkinconsistency
                    Object hideObj = prompt.get("hideInContents");
                    if (prompt.containsKey("hideInContents")) {
                        if (hideObj instanceof Boolean)
                            hide = (boolean) hideObj;
                        else if (hideObj instanceof Integer && (Integer) hideObj == 1)
                            hide = true;
                    }

                    //we don't want to display those as well
                    if (prompt.containsKey("_type")) {
                        String type = (String) prompt.get("_type");
                        if (type.equals("note") || type.equals("_section") || type.equals("contents")) {
                            hide = true;
                        }
                    }
                    if (hide != true) {

                        Map<String, Object> display = (Map<String, Object>) prompt
                                .get(FORMDEF_DISPLAY);
                        if (display == null) {
                            throw new IllegalArgumentException("File is not a formdef json file! No display section inside prompt element."
                                    + formDefFile.getAbsolutePath());
                        }
                        if(display.get(FORMDEF_TEXT) instanceof  String){
                            throw new IllegalArgumentException("File is not a formdef json file! Make sure that elements are properly translated."
                                    + formDefFile.getAbsolutePath());
                        }
                        HashMap<String, String> text = (HashMap<String, String>) display
                                .get(FORMDEF_TEXT);
                        if (text == null) {
                            throw new IllegalArgumentException("File is not a formdef json file! No display text inside prompt element."
                                    + formDefFile.getAbsolutePath());
                        }
                        String columnName = (String) prompt.get("name");
                        String promptType = (String) prompt.get("type");
                        int columnNumber = -1;
                        if (columnName != null && promptType != null) {
                            if (promptType.equals("image") || promptType.equals("video")) {
                                if (promptType.equals("image")) { //TODO:Handle audio
                                    columnNumber = instanceCursor.getColumnIndex("picture_uriFragment");
                                } else if (promptType.equals("video")) {
                                    columnNumber = instanceCursor.getColumnIndex(promptType + "uriFragment");
                                }
                                if (columnNumber != -1) {
                                    if (instanceCursor.getString(columnNumber) != null && !instanceCursor.getString(columnNumber).isEmpty()) {
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0,  context.getResources().getString(R.string.media_file_attached)));
                                    } else {
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, null));
                                    }
                                }
                            } else if(promptType.equals("geopoint")) {
                                if(instanceCursor.getString(instanceCursor.getColumnIndex("location_accuracy"))!=null &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_altitude"))!=null  &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_latitude"))!=null  &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_longitude"))!=null){
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, context.getResources().getString(R.string.location_captured)));
                                } else {
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, null));
                                }

                            }  else if(promptType.equals("select_one_dropdown")) {
                                String answer = instanceCursor.getString(instanceCursor.getColumnIndex(columnName));
                                if(answer!=null && !answer.isEmpty()) {
                                    List<HashMap<String, Object>> answers = (List<HashMap<String, Object>>) choices
                                            .get(prompt.get("values_list"));
                                    if (answers == null) {
                                        throw new IllegalArgumentException("File is not a formdef json file! No answers inside choices element."
                                                + formDefFile.getAbsolutePath());
                                    }
                                    for (HashMap<String, Object> possibleAnswer : answers) {
                                        boolean write = false;
                                        for (Map.Entry<String, Object> field : possibleAnswer.entrySet()) {
                                            if (field.getKey().equals("data_value")) {
                                                if(field.getValue().equals(answer))
                                                   write = true;
                                            }
                                            if (field.getKey().equals(FORMDEF_DISPLAY) && write) {
                                                HashMap<String, Object> textMap = (HashMap<String, Object>)field.getValue();
                                                HashMap<String, String> translations = (HashMap<String, String>)textMap.get(FORMDEF_TEXT);
                                                if (translations == null) {
                                                    throw new IllegalArgumentException("File is not a formdef json file! No  text inside possible answer element."
                                                            + formDefFile.getAbsolutePath());
                                                }
                                                String answerDisplayName = translations.get("default");
                                                result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, answerDisplayName)); //TODO:translate this as well
                                            }
                                        }
                                    }
                                } else {
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, null));
                                }
                            } else if (instanceCursor.getColumnIndex(columnName) != -1) {

                                String answer = instanceCursor.getString(instanceCursor.getColumnIndex(columnName));
                                if (answer != null && (answer.equals("red") || answer.equals("green") || answer.equals("yellow"))) {//TODO: add column filtering to check wheter this is poverty stoplight question
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 1, answer));
                                } else {
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, answer));
                                }
                            } else
                                result.add(new QuestionInfo(columnName, text, (String) prompt.get(PATH), 0, "SOMETHING WENT WRONG"));
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
