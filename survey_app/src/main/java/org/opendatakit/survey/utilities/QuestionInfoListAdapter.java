package org.opendatakit.survey.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.opendatakit.survey.R;

import java.util.ArrayList;

/**
 * Created by user on 16.06.17.
 */
public class QuestionInfoListAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final ArrayList<Object> questions = new ArrayList<Object>();


    //TODO: to do jakiejs klasy?
    private static final int TEXT_CHOICE_QUESTION = 0;
    private static final int GPS_QUESTION = 1;
    private static final int MEDIA_QUESTION = 2;
    private static final int POVERTY_STOPLIGHT_QUESTION = 3;
    private static final int SECTION_NAME_HEADER= 4;

    public QuestionInfoListAdapter(Context context, int layout) {
        this.context = context;
        this.layout = layout;
    }

    public void clear() {
        questions.clear();
    }

    public void addAll(ArrayList<Object> items) {
        questions.addAll(items);
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
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            case TEXT_CHOICE_QUESTION:
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(layout, parent, false);

                    TextView question = (TextView) view.findViewById(R.id.question);
                    question.setText(((QuestionInfo)getItem(position)).displayNames.get("default"));

                    TextView answer = (TextView) view.findViewById(R.id.answer);
                    answer.setText(((QuestionInfo)getItem(position)).answer);
                }

                break;
            case GPS_QUESTION:
                break;
            case MEDIA_QUESTION:
                break;
            case POVERTY_STOPLIGHT_QUESTION:
                break;
            case SECTION_NAME_HEADER:
                if (view == null) {
                    LayoutInflater layoutInflater =
                            (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.row_header, parent, false);
                }

                TextView title = (TextView) view.findViewById(R.id.headerTitle);
                String titleString = (String) getItem(position);
                title.setText(titleString);
                break;


        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof QuestionInfo) {
            return ((QuestionInfo)getItem(position)).questionType;
        }

        return SECTION_NAME_HEADER;
    }
}
