package org.opendatakit.survey.utilities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.opendatakit.survey.R;

import java.util.ArrayList;

/**
 * Created by user on 16.06.17.
 */
public class QuestionInfoListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Object> questions = new ArrayList<Object>();


    //TODO: to do jakiejs klasy?
    private static final int TEXT_CHOICE_QUESTION = 0;
    private static final int POVERTY_STOPLIGHT_QUESTION = 1;
    private static final int SECTION_NAME_HEADER= 2;

    ColorStateList greenColorStateList;
    ColorStateList redColorStateList;
    ColorStateList yellowColorStateList;

    public QuestionInfoListAdapter(Context context) {
        this.context = context;
        greenColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled}
                },
                new int[] {
                        ContextCompat.getColor(context, R.color.poverty_stoplight_green)

                }
        );

        redColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled}
                },
                new int[] {
                        ContextCompat.getColor(context, R.color.poverty_stoplight_red)

                }
        );

        yellowColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled}
                },
                new int[] {
                        ContextCompat.getColor(context, R.color.poverty_stoplight_yellow)

                }
        );
    }

    public void clear() {
        questions.clear();
    }

    public void addAll(ArrayList<Object> items) {
        questions.addAll(items);
    }

    public ArrayList<Object> getAll() {
        return questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.MAX_VALUE - position;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) != SECTION_NAME_HEADER);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case TEXT_CHOICE_QUESTION: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.text_choice_types_question_row, parent, false);
                }
                QuestionInfo item = (QuestionInfo)getItem(position);
                TextView questionView = (TextView) view.findViewById(R.id.question);
                questionView.setText(item.displayNames.get("default"));

                final RadioButton radioButtonView = (RadioButton) view.findViewById(R.id.questionRadioButton);

                TextView answerView = (TextView) view.findViewById(R.id.answer);
                radioButtonView.setChecked(item.isChecked);
                radioButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        radioButtonView.setSelected(isChecked);
                    }
                });

                answerView.setText(((QuestionInfo)getItem(position)).answer);


                break;
            }
            case POVERTY_STOPLIGHT_QUESTION: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.poverty_stoplight_question_row, parent, false);
                }
                QuestionInfo item = (QuestionInfo)getItem(position);
                TextView questionView = (TextView) view.findViewById(R.id.question);
                questionView.setText(item.displayNames.get("default"));

                AppCompatRadioButton radioButtonView = (AppCompatRadioButton) view.findViewById(R.id.questionRadioButton);

                switch(((QuestionInfo) getItem(position)).answer) {
                    case "red" :
                        radioButtonView.setSupportButtonTintList(redColorStateList);
                        break;
                    case "yellow" :
                        radioButtonView.setSupportButtonTintList(yellowColorStateList);
                        break;
                    case "green" :
                        radioButtonView.setSupportButtonTintList(greenColorStateList);
                        break;
                }

                break;
            }
            case SECTION_NAME_HEADER: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.row_header, parent, false);
                }

                //TODO: Language?
                TextView title = (TextView) view.findViewById(R.id.headerTitle);
                QuestionInfo item = (QuestionInfo)getItem(position);
                title.setText(item.displayNames.get("default"));
                //String titleString = (String) getItem(position);
                //title.setText(titleString);
                break;
            }


        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof QuestionInfo) {
            return ((QuestionInfo)getItem(position)).questionType;
        }

        return -1;
    }
}
