package org.opendatakit.survey.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import org.opendatakit.survey.R;
import org.opendatakit.survey.activities.MainMenuActivity;
import org.opendatakit.survey.utilities.DataPassListener;

import java.util.HashMap;

public class BeneficiaryInformationFragment extends Fragment implements View.OnClickListener{

    private View view;

    AppCompatEditText firstnameEditText;
    AppCompatEditText lastnameEditText;
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";

    DataPassListener mCallback;

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
                hideKeyboard(getActivity());
                getActivity().onBackPressed();
                break;
            case R.id.beneficiary_information_next_button:
                hideKeyboard(getActivity());
                HashMap<String, String> values = new HashMap<>();
                values.put(FIRSTNAME, firstnameEditText.getText().toString());
                values.put(LASTNAME, lastnameEditText.getText().toString());
                mCallback.passData(values);
                ((MainMenuActivity)getActivity()).swapToFragmentView(MainMenuActivity.ScreenList.CHOOSE_FORM); //moze zmienic nazwe
                break;
        }
    }

    @TargetApi(23)
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

    @SuppressWarnings("deprecation")
    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            try {
                mCallback = (DataPassListener)activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement DataPassListener");
            }
        }
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
