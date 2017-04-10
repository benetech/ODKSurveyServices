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

public class ConfirmationDialogFragment extends DialogFragment {
    public ConfirmationDialogFragment() {
    }

    public static ConfirmationDialogFragment newInstance(int fragmentId, String title, String message, String okButton, String cancelButton) {
        ConfirmationDialogFragment frag = new ConfirmationDialogFragment();
        Bundle args = new Bundle();
        args.putInt("fragmentId", fragmentId);
        args.putString("title", title);
        args.putString("message", message);
        args.putString("okButton", okButton);
        args.putString("cancelButton", cancelButton);
        frag.setArguments(args);
        return frag;
    }

    public void setMessage(String message) {
        ((AlertDialog)this.getDialog()).setMessage(message);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = this.getArguments().getString("title");
        String message = this.getArguments().getString("message");
        String okButton = this.getArguments().getString("okButton");
        String cancelButton = this.getArguments().getString("cancelButton");
        final Integer fragmentId = Integer.valueOf(this.getArguments().getInt("fragmentId"));
        FragmentManager mgr = this.getFragmentManager();
        Fragment f = mgr.findFragmentById(fragmentId.intValue());
        this.setTargetFragment(f, RequestCodes.CONFIRMATION_DIALOG.ordinal());
        OnClickListener dialogYesNoListener = new OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                FragmentManager mgr = ConfirmationDialogFragment.this.getFragmentManager();
                Fragment f = mgr.findFragmentById(fragmentId.intValue());
                switch(i) {
                    case -2:
                        ((ConfirmationDialogFragment.ConfirmConfirmationDialog)f).cancelConfirmationDialog();
                        dialog.dismiss();
                        break;
                    case -1:
                        ((ConfirmationDialogFragment.ConfirmConfirmationDialog)f).okConfirmationDialog();
                        dialog.dismiss();
                }

            }
        };
        AlertDialog dlg = (new Builder(this.getActivity())).setTitle(title).setMessage(message).setCancelable(false).setPositiveButton(okButton, dialogYesNoListener).setNegativeButton(cancelButton, dialogYesNoListener).create();
        dlg.setCanceledOnTouchOutside(false);
        return dlg;
    }

    public interface ConfirmConfirmationDialog {
        void okConfirmationDialog();

        void cancelConfirmationDialog();
    }
}
