package org.opendatakit.survey.utilities;

import java.util.HashMap;

/**
 * Created by user on 16.06.17.
 */

public class QuestionInfo {
    public final String name;
    public final HashMap<String, String> displayNames;
    public final String path;

    public QuestionInfo(String name, HashMap<String, String> displayNames, String path){
        this.name = name;
        this.displayNames = displayNames;
        this.path = path;
    }
}
