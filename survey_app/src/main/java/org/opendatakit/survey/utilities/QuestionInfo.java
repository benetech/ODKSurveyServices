package org.opendatakit.survey.utilities;

import java.util.HashMap;

/**
 * Created by user on 16.06.17.
 */

public class QuestionInfo {
    public final String question;
    public final String answer;
    public final HashMap<String, String> displayNames;
    public final HashMap<String, HashMap<String, String>> translatedChoiceTypeAnswers;
    public final String path;
    public final int questionType;
    public final boolean isChecked;

    public QuestionInfo(String question, HashMap<String, String> displayNames, String path,
                        int questionType, String answer, HashMap<String, HashMap<String, String>> translatedChoiceTypeAnswers){
        this.question = question;
        this.displayNames = displayNames;
        this.path = path;
        this.questionType = questionType;
        this.answer = answer;
        if(answer == null || answer.isEmpty())
            isChecked = false;
        else
            isChecked = true;
        this.translatedChoiceTypeAnswers = translatedChoiceTypeAnswers;
    }
}
