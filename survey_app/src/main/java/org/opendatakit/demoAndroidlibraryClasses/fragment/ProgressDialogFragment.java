package org.opendatakit.demoAndroidlibraryClasses.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.os.Bundle;
import android.view.View;

import org.opendatakit.survey.R;


public class ProgressDialogFragment extends DialogFragment {
    public ProgressDialogFragment() {
    }

    public static ProgressDialogFragment newInstance(String title, String message) {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public void setMessage(String message) {
        ((ProgressDialog)this.getDialog()).setMessage(message);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = this.getArguments().getString("title");
        String message = this.getArguments().getString("message");
        OnClickListener loadingButtonListener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ProgressDialogFragment f = ProgressDialogFragment.this;
                if(f != null && f instanceof ProgressDialogFragment.CancelProgressDialog) {
                    ((ProgressDialogFragment.CancelProgressDialog)f).cancelProgressDialog();
                }

            }
        };
        OnShowListener showButtonListener = new OnShowListener() {
            public void onShow(DialogInterface dialog) {
                ProgressDialogFragment f = ProgressDialogFragment.this;
                if(f != null && f instanceof ProgressDialogFragment.CancelProgressDialog) {
                    ((ProgressDialog)dialog).getButton(-1).setVisibility(View.VISIBLE);
                } else {
                    ((ProgressDialog)dialog).getButton(-1).setVisibility(View.GONE);
                }

            }
        };
        ProgressDialog mProgressDialog = new ProgressDialog(this.getActivity(), this.getTheme());
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIcon(R.drawable.ic_info_outline_black_24dp);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setButton(-1, this.getString(R.string.cancel), loadingButtonListener);
        mProgressDialog.setOnShowListener(showButtonListener);
        return mProgressDialog;
    }

    public interface CancelProgressDialog {
        void cancelProgressDialog();
    }
}
