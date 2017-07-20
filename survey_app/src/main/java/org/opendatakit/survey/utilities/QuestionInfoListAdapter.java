package org.opendatakit.survey.utilities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.fragments.SummaryPageFragment;

import java.util.ArrayList;

public class QuestionInfoListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Object> questions = new ArrayList<Object>();
    private ColorStateList greenColorStateList;
    private ColorStateList redColorStateList;
    private ColorStateList yellowColorStateList;
    private String language;
    private SummaryPageFragment fragment;


    public QuestionInfoListAdapter(Context context, SummaryPageFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.language = ((MainMenuActivity)context).getLocale();
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
        return (getItemViewType(position) != QuestionSectionTypes.SECTION_NAME_HEADER);
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case QuestionSectionTypes.TEXT_QUESTION: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.text_choice_types_question_row, parent, false);
                }
                QuestionInfo item = (QuestionInfo)getItem(position);
                TextView questionView = (TextView) view.findViewById(R.id.question);
                questionView.setText(item.displayNames.get(language));

                final RadioButton radioButtonView = (RadioButton) view.findViewById(R.id.questionRadioButton);

                TextView answerView = (TextView) view.findViewById(R.id.answer);
                radioButtonView.setChecked(item.isChecked);
                radioButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        radioButtonView.setSelected(isChecked);
                    }
                });

                answerView.setText(item.answer);
                break;
            }
            case QuestionSectionTypes.CHOICE_QUESTION: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.text_choice_types_question_row, parent, false);
                }
                QuestionInfo item = (QuestionInfo)getItem(position);
                TextView questionView = (TextView) view.findViewById(R.id.question);
                questionView.setText(item.displayNames.get(language));

                final RadioButton radioButtonView = (RadioButton) view.findViewById(R.id.questionRadioButton);

                TextView answerView = (TextView) view.findViewById(R.id.answer);
                radioButtonView.setChecked(item.isChecked);
                radioButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        radioButtonView.setSelected(isChecked);
                    }
                });

                if(item.answer != null && !item.answer.isEmpty()) {
                    answerView.setText(item.translatedChoiceTypeAnswers.get(item.answer).get(language));
                }
                break;
            }
            case QuestionSectionTypes.POVERTY_STOPLIGHT_QUESTION: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.poverty_stoplight_question_row, parent, false);
                }
                QuestionInfo item = (QuestionInfo)getItem(position);
                TextView questionView = (TextView) view.findViewById(R.id.question);
                questionView.setText(item.displayNames.get(language));

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
            case QuestionSectionTypes.SECTION_NAME_HEADER: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.row_header_section, parent, false);
                }

                TextView title = (TextView) view.findViewById(R.id.headerTitle);
                SectionHeaderInfo item = (SectionHeaderInfo) getItem(position);
                title.setText(item.displayNames.get(language));
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                double percent = (double) item.answeredQuestions / (item.emptyQuestions + item.answeredQuestions) * 100;
                progressBar.setProgress((int) percent);
                break;
            }
            case QuestionSectionTypes.POVERTY_STOPLIGHT_SECTION_NAME_HEADER: {
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.row_poverty_stoplight_section_header, parent, false);
                }

                TextView title = (TextView) view.findViewById(R.id.headerTitle);
                SectionHeaderInfo item = (SectionHeaderInfo) getItem(position);
                title.setText(item.displayNames.get(language));
                float sum = item.emptyQuestions + item.answeredQuestions;
                ProgressBar progressBarGreen = (ProgressBar) view.findViewById(R.id.progressBarGreen);
                progressBarGreen.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, item.greenAnswers/sum));
                ProgressBar progressBarYellow = (ProgressBar) view.findViewById(R.id.progressBarYellow);
                progressBarYellow.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, item.yellowAnswers/sum));
                ProgressBar progressBarRed = (ProgressBar) view.findViewById(R.id.progressBarRed);
                progressBarRed.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, item.redAnswers/sum));
                ProgressBar progressBarGrey = (ProgressBar) view.findViewById(R.id.progressBarGrey);
                progressBarGrey.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, item.emptyQuestions/sum));
                break;
            }
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof QuestionInfo) {
            return ((QuestionInfo)getItem(position)).questionType;
        } else if (getItem(position) instanceof SectionHeaderInfo){
            return ((SectionHeaderInfo)getItem(position)).sectionType;
        }

        return -1;
    }


    public void setLanguage(String language){
        this.language = language;
    }

}
