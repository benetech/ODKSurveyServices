package org.opendatakit.survey.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import org.opendatakit.survey.R;

import java.util.ArrayList;

public class FormInfoListAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final int updateDate;
    private final int questions;
    private final ArrayList<Object> forms = new ArrayList<Object>();
    int selectedIndex = -1;


    public FormInfoListAdapter(Context context, int layout, int updateDate,
                                   int questions) {
        this.context = context;
        this.layout = layout;
        this.updateDate = updateDate;
        this.questions = questions;
    }

    public void clear() {
        forms.clear();
    }

    public void addAll(ArrayList<Object> items) {
        forms.addAll(items);
    }


    @Override
    public int getCount() {
        return forms.size();
    }

    @Override
    public Object getItem(int position) {
        return forms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.MAX_VALUE - position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layout, parent, false);
            FormInfo info = (FormInfo) forms.get(position);
            TextView formName = (TextView) view.findViewById(R.id.formName);
            formName.setText(info.formDisplayName);
            TextView updateDateView = (TextView) view.findViewById(updateDate);
            updateDateView.setText(info.updateData);
            TextView questionsView = (TextView) view.findViewById(questions);
            questionsView.setText(String.valueOf(info.questions));

        }

        RadioButton rbSelect = (RadioButton) view.findViewById(R.id.formRadioButton);
        if(selectedIndex == position){
            rbSelect.setChecked(true);
        }
        else{
            rbSelect.setChecked(false);
        }
        return view;
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    public String getSelectedTabeID(){
        return ((FormInfo) forms.get(selectedIndex)).tableId;
    }

}
