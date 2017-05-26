package org.opendatakit.survey.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;

public class BeneficiaryInformationFragment extends Fragment implements View.OnClickListener{

    private View view;

    AppCompatEditText firstnameEditText;
    AppCompatEditText lastnameEditText;

    DataPassListener mCallback;
    public interface DataPassListener{
        public void passData(String firstname, String lastname);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beneficiary_information, container, false);

        final TextInputLayout firstnameWrapper = (TextInputLayout) view.findViewById(R.id.firstnameWrapper);
        final TextInputLayout lastnameWrapper = (TextInputLayout) view.findViewById(R.id.lastnameWrapper);
        firstnameWrapper.setHint(getString(R.string.first_name));
        lastnameWrapper.setHint(getString(R.string.last_name));

        Button cancelButton = (Button) view.findViewById(R.id.beneficiary_information_cancel_button);
        final Button nextButton = (Button) view.findViewById(R.id.beneficiary_information_next_button);
        cancelButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        nextButton.setEnabled(false);

        firstnameEditText = (AppCompatEditText) view.findViewById(R.id.firstnameEditText);
        lastnameEditText = (AppCompatEditText) view.findViewById(R.id.lastnameEditText);

        final Boolean filled[] = {false, false};

        firstnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    nextButton.setEnabled(false);
                    filled[0]=false;
                } else {
                    filled[0]=true;
                    if(filled[1]){
                        nextButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        lastnameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    nextButton.setEnabled(false);
                    filled[1]=false;
                } else {
                    filled[1]=true;
                    if(filled[0]){
                        nextButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beneficiary_information_cancel_button:
                getActivity().onBackPressed();
                break;
            case R.id.beneficiary_information_next_button:
                mCallback.passData(firstnameEditText.getText().toString(), lastnameEditText.getText().toString());
                ((MainMenuActivity)getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.CHOOSE_FORM); //moze zmienic nazwe
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Make sure that container activity implement the callback interface
        try {
            mCallback = (DataPassListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement DataPassListener");
        }
    }

}
