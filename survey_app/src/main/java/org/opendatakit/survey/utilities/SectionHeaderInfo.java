package org.opendatakit.survey.utilities;

import java.util.HashMap;

/**
 * Created by user on 20.07.17.
 */

public class SectionHeaderInfo {
    public final HashMap<String, String> displayNames;
    public final int sectionType;
    public int emptyQuestions = 0;
    public int answeredQuestions = 0;
    public int redAnswers = 0;
    public int yellowAnswers = 0;
    public int greenAnswers = 0;

    public SectionHeaderInfo(HashMap<String, String> displayNames, int sectionType){
        this.displayNames = displayNames;
        this.sectionType = sectionType;

    }
}
