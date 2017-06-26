package org.opendatakit.survey.utilities;

import java.util.HashMap;

/**
 * Created by user on 16.06.17.
 */

public class QuestionInfo {
    public final String question;
    public final String answer;
    public final HashMap<String, String> displayNames;
    public final String path;
    public final int questionType;

    public QuestionInfo(String question, HashMap<String, String> displayNames, String path, int questionType, String answer){
        this.question = question;
        this.displayNames = displayNames;
        this.path = path;
        this.questionType = questionType;
        this.answer = answer;
    }
}
