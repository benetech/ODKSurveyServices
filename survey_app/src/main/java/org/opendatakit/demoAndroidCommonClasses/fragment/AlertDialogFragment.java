package org.opendatakit.demoAndroidCommonClasses.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import org.opendatakit.demoAndroidCommonClasses.properties.RequestCodes;
import org.opendatakit.survey.R;

public class AlertDialogFragment extends DialogFragment {
    public AlertDialogFragment() {
    }

    public static AlertDialogFragment newInstance(int fragmentId, String title, String message) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentId", fragmentId);
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    public void setMessage(String message) {
        ((AlertDialog)this.getDialog()).setMessage(message);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = this.getArguments().getString("title");
        String message = this.getArguments().getString("message");
        final Integer fragmentId = Integer.valueOf(this.getArguments().getInt("fragmentId"));
        FragmentManager mgr = this.getFragmentManager();
        Fragment f = mgr.findFragmentById(fragmentId.intValue());
        this.setTargetFragment(f, RequestCodes.ALERT_DIALOG.ordinal());
        OnClickListener quitListener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                switch(i) {
                    case -1:
                        FragmentManager mgr = AlertDialogFragment.this.getFragmentManager();
                        Fragment f = mgr.findFragmentById(fragmentId.intValue());
                        if(f instanceof AlertDialogFragment.ConfirmAlertDialog) {
                            ((AlertDialogFragment.ConfirmAlertDialog)f).okAlertDialog();
                        }

                        dialog.dismiss();
                    default:
                }
            }
        };
        AlertDialog dlg = (new Builder(this.getActivity())).setIcon(17301659).setTitle(title).setMessage(message).setCancelable(false).setPositiveButton(this.getString(R.string.ok), quitListener).create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }

    public interface ConfirmAlertDialog {
        void okAlertDialog();
    }
}
