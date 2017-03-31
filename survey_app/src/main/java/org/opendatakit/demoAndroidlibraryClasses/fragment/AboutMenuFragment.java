package org.opendatakit.demoAndroidlibraryClasses.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidlibraryClasses.application.AppAwareApplication;
import org.opendatakit.demoAndroidlibraryClasses.listener.LicenseReaderListener;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.task.LicenseReaderTask;
import org.opendatakit.survey.R;

public class AboutMenuFragment extends Fragment implements LicenseReaderListener {
    private static final String t = "AboutMenuFragment";
    public static final String NAME = "About";
    public static final int ID;
    private static final String LICENSE_TEXT = "LICENSE_TEXT";
    private static LicenseReaderTask licenseReaderTask;
    private TextView mTextView;
    private String mLicenseText = null;

    public AboutMenuFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View aboutMenuView = inflater.inflate(ID, container, false);
        TextView versionBox = (TextView)aboutMenuView.findViewById(R.id.versionText);
        versionBox.setText(((AppAwareApplication)this.getActivity().getApplication()).getVersionedAppName());
        this.mTextView = (TextView)aboutMenuView.findViewById(R.id.text1);
        this.mTextView.setAutoLinkMask(1);
        this.mTextView.setClickable(true);
        if(savedInstanceState != null && savedInstanceState.containsKey("LICENSE_TEXT")) {
            this.mLicenseText = savedInstanceState.getString("LICENSE_TEXT");
            this.mTextView.setText(Html.fromHtml(this.mLicenseText));
        } else {
            this.readLicenseFile();
        }

        return aboutMenuView;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("LICENSE_TEXT", this.mLicenseText);
    }

    public void readLicenseComplete(String result) {
        IAppAwareActivity activity = (IAppAwareActivity)this.getActivity();
        WebLogger.getLogger(activity.getAppName()).i("AboutMenuFragment", "Read license complete");
        if(result != null) {
            this.mLicenseText = result;
            this.mTextView.setText(Html.fromHtml(result));
        } else {
            WebLogger.getLogger(activity.getAppName()).e("AboutMenuFragment", "Failed to read license file");
            Toast.makeText(this.getActivity(), R.string.read_license_fail, Toast.LENGTH_SHORT).show();
        }

    }

    private synchronized void readLicenseFile() {
        IAppAwareActivity activity = (IAppAwareActivity)this.getActivity();
        String appName = activity.getAppName();
        if(licenseReaderTask == null) {
            LicenseReaderTask lrt = new LicenseReaderTask();
            lrt.setApplication((AppAwareApplication)this.getActivity().getApplication());
            lrt.setAppName(appName);
            lrt.setLicenseReaderListener(this);
            licenseReaderTask = lrt;
            licenseReaderTask.execute(new Void[0]);
        } else {
            licenseReaderTask.setLicenseReaderListener(this);
            if(licenseReaderTask.getStatus() != Status.FINISHED) {
                Toast.makeText(this.getActivity(), this.getString(R.string.still_reading_license_file), Toast.LENGTH_SHORT).show();
            } else {
                licenseReaderTask.setLicenseReaderListener((LicenseReaderListener)null);
                this.readLicenseComplete(licenseReaderTask.getResult());
            }
        }

    }

    public void onDestroy() {
        if(licenseReaderTask != null) {
            licenseReaderTask.clearLicenseReaderListener((LicenseReaderListener)null);
        }

        super.onDestroy();
    }

    static {
        ID = R.layout.about_menu_layout;
        licenseReaderTask = null;
    }
}
