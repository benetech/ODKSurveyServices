package org.opendatakit.survey.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 07.07.17.
 */

public enum FormDefSections {
    SETTINGS_SUBSECTION("settings"),
    SPECIFICATION_SECTION("specification"),
    SECTION_NAMES("section_names"),
    SECTION_NAME("section_name"),
    CHOICES("choices"),
    SECTIONS("sections"),
    DISPLAY("display"),
    TITLE("title"),
    TEXT("text"),
    NESTED_SECTIONS("nested_sections"),
    PROMPTS("prompts"),
    PATH("_branch_label_enclosing_screen"),
    SURVEY("survey"),
    OPERATIONS("operations"),
    DEFAULT("default"),
    NAME("name"),
    LOCALES("_locales"),
    VALUE("value");

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
