package org.opendatakit.survey.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.InstanceProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.opendatakit.survey.utilities.FormDefSections.SETTINGS_SUBSECTION;

/**
 * Created by user on 16.06.17.
 */
public class QuestionListLoader  extends AsyncTaskLoader<ArrayList<Object>> {

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
                .get(FormDefSections.SPECIFICATION_SECTION.getText());
        if (specification == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> settings = (Map<String, Object>) specification
                .get(SETTINGS_SUBSECTION.getText());
        if (settings == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No settings section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> choices = (Map<String, Object>) specification
                .get(FormDefSections.CHOICES.getText());
        if (choices == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No choices section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> sections = (Map<String, Object>) specification
                .get(FormDefSections.SECTIONS.getText());
        if (sections == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No sections section inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> surveySection = (Map<String, Object>) sections
                .get(FormDefSections.SURVEY.getText());
        if (surveySection == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No surveySection inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        Map<String, Object> nestedSections = (Map<String, Object>) surveySection
                .get("nested_sections");
        if (nestedSections == null) {
            throw new IllegalArgumentException("File is not a formdef json file! No nestedSections inside specification element."
                    + formDefFile.getAbsolutePath());
        }

        sectionNames = new ArrayList<>();

        for (Map.Entry<String, Object> nestedSection : nestedSections.entrySet()) {
            sectionNames.add(nestedSection.getKey());
        }

        //now lets store somewhere displaynames of those for different languages
        for(String sectionName : sectionNames){
            Map<String, Object> settingsOfSeciotn = (Map<String, Object>) settings
                    .get(sectionName);
            if (settingsOfSeciotn == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No settings of section inside specification element."
                        + formDefFile.getAbsolutePath());
            }
            Map<String, Object> display = (Map<String, Object>) settingsOfSeciotn
                    .get(FormDefSections.DISPLAY.getText());
            if (display == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No display section inside settings of section element."
                        + formDefFile.getAbsolutePath());
            }
            if(display.get(FormDefSections.TITLE.getText()) instanceof  String){
                throw new IllegalArgumentException("File is not a formdef json file! Make sure that elements are properly translated."
                        + formDefFile.getAbsolutePath());

            }
            HashMap<String, String> titles = (HashMap<String, String>) display
                    .get(FormDefSections.TITLE.getText());
            if (titles == null) {
                throw new IllegalArgumentException("File is not a formdef json file! No titles section inside display section element."
                        + formDefFile.getAbsolutePath());
            }

            headersDisplayNames.put(sectionName, titles);
        }


        Uri formUri = Uri.withAppendedPath(InstanceProviderAPI.CONTENT_URI, appName + "/"
                + tableId);
        Cursor instanceCursor = context.getContentResolver().query(formUri, null, "_id=?", new String[]{formId}, null);

        if(instanceCursor!=null && instanceCursor.moveToFirst()) {
            SectionHeaderInfo currentSection;
            for (final String sectionName : sectionNames) {
                Map<String, Object> section = (Map<String, Object>) sections
                        .get(sectionName);
                if (section == null) {
                    throw new IllegalArgumentException("File is not a formdef json file! No sections inside section but it was mentioned in section names specification element."
                            + formDefFile.getAbsolutePath());
                }

                //if this is a section that contains all of the others, let's call it menu, we don't want to display it here
                if((section.get(FormDefSections.SECTION_NAME.getText())).equals("survey"))
                    continue;

                if(sectionName.startsWith("stoplight")) {
                    result.add(new SectionHeaderInfo(headersDisplayNames.get(sectionName), QuestionSectionTypes.POVERTY_STOPLIGHT_SECTION_NAME_HEADER));
                }
                else {
                    result.add(new SectionHeaderInfo(headersDisplayNames.get(sectionName), QuestionSectionTypes.SECTION_NAME_HEADER));
                }
                currentSection = (SectionHeaderInfo) result.get(result.size()-1);


                List<Map<String, Object>> prompts = (List<Map<String, Object>>) section
                        .get(FormDefSections.PROMPTS.getText());
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
                                .get(FormDefSections.DISPLAY.getText());
                        if (display == null) {
                            throw new IllegalArgumentException("File is not a formdef json file! No display section inside prompt element."
                                    + formDefFile.getAbsolutePath());
                        }
                        if(display.get(FormDefSections.TEXT.getText()) instanceof  String){
                            throw new IllegalArgumentException("File is not a formdef json file! Make sure that elements are properly translated."
                                    + formDefFile.getAbsolutePath());
                        }
                        HashMap<String, String> text = (HashMap<String, String>) display
                                .get(FormDefSections.TEXT.getText());
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
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION,  context.getResources().getString(R.string.media_file_attached), null));
                                        currentSection.answeredQuestions++;
                                    } else {
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION, null, null));
                                        currentSection.emptyQuestions++;
                                    }
                                }
                            } else if(promptType.equals("geopoint")) {
                                if(instanceCursor.getString(instanceCursor.getColumnIndex("location_accuracy"))!=null &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_altitude"))!=null  &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_latitude"))!=null  &&
                                        instanceCursor.getString(instanceCursor.getColumnIndex("location_longitude"))!=null){
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION, context.getResources().getString(R.string.location_captured), null));
                                    currentSection.answeredQuestions++;
                                } else {
                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION, null, null));
                                    currentSection.emptyQuestions++;
                                }

                            }  else if (instanceCursor.getColumnIndex(columnName) != -1) {
                                String answer = instanceCursor.getString(instanceCursor.getColumnIndex(columnName));
                                if (promptType.equals("select_one_dropdown")) {
                                    HashMap<String, HashMap<String, String>> translatedChoiceTypeAnswers = new HashMap<>();
                                    if (answer != null && !answer.isEmpty()) {
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
                                                    translatedChoiceTypeAnswers.put((String) field.getValue(), ((HashMap<String, HashMap<String, String>>) possibleAnswer.get("display")).get("text"));
                                                    if (field.getValue().equals(answer))
                                                        write = true;
                                                }
                                                if (field.getKey().equals(FormDefSections.DISPLAY.getText()) && write) {
                                                    HashMap<String, Object> textMap = (HashMap<String, Object>) field.getValue();
                                                    HashMap<String, String> translations = (HashMap<String, String>) textMap.get(FormDefSections.TEXT.getText());
                                                    if (translations == null) {
                                                        throw new IllegalArgumentException("File is not a formdef json file! No  text inside possible answer element."
                                                                + formDefFile.getAbsolutePath());
                                                    }
                                                    result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.CHOICE_QUESTION, answer, translatedChoiceTypeAnswers));
                                                    currentSection.answeredQuestions++;
                                                }
                                            }
                                        }
                                    } else {
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.CHOICE_QUESTION, null, null));
                                        currentSection.emptyQuestions++;
                                    }
                                } else{
                                    if (answer != null && (answer.equals("red") || answer.equals("green") || answer.equals("yellow"))) {//TODO: add column filtering to check wheter this is poverty stoplight question
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.POVERTY_STOPLIGHT_QUESTION, answer, null));
                                        switch (answer){
                                            case "red": currentSection.redAnswers++;
                                                break;
                                            case "green": currentSection.greenAnswers++;
                                                break;
                                            case "yellow": currentSection.yellowAnswers++;
                                                break;
                                        }
                                        currentSection.answeredQuestions++; //in case we would need it in future and for tha sake of consistency
                                    } else {
                                        result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION, answer, null));
                                        if(answer == null || answer.isEmpty()){
                                            currentSection.emptyQuestions++;
                                        } else {
                                            currentSection.answeredQuestions++;
                                        }
                                    }
                                }
                            } else
                                result.add(new QuestionInfo(columnName, text, (String) prompt.get(FormDefSections.PATH.getText()), QuestionSectionTypes.TEXT_QUESTION, "SOMETHING WENT WRONG", null));
                                //we should never reach that
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
