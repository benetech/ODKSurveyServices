package org.opendatakit.survey.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07.07.17.
 */

public enum FormDefSections {
    FORMDEF_SETTINGS_SUBSECTION("settings"),
    FORMDEF_SPECIFICATION_SECTION("specification"),
    FORMDEF_SECTION_NAMES("section_names"),
    FORMDEF_CHOICES("choices"),
    FORMDEF_SECTIONS("sections"),
    FORMDEF_DISPLAY("display"),
    FORMDEF_TITLE("title"),
    FORMDEF_TEXT("text"),
    FORMDEF_NESTED_SECTIONS("nested_sections"),
    FORMDEF_PROMPTS("prompts"),
    PATH("_branch_label_enclosing_screen"),
    SURVEY("survey"),
    OPERATIONS("operations");

    private final String text;

    FormDefSections() {
        this.text = null;
    }

    FormDefSections(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static List<String> getValues(){
        List<String> res = new ArrayList();
        for(FormDefSections f : values()) {
            res.add(f.getText());
        }
        return res;
    }

}
