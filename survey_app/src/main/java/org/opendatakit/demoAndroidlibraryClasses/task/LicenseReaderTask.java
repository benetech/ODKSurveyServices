package org.opendatakit.demoAndroidlibraryClasses.task;

import android.app.Application;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.opendatakit.demoAndroidlibraryClasses.application.AppAwareApplication;
import org.opendatakit.demoAndroidlibraryClasses.listener.LicenseReaderListener;
import org.opendatakit.survey.R;

public class LicenseReaderTask extends AsyncTask<Void, Integer, String> {
    private AppAwareApplication appContext;
    private LicenseReaderListener lrl;
    private String appName;
    private String mResult;

    public LicenseReaderTask() {
    }

    protected String doInBackground(Void... arg0) {
        String result = null;
        StringBuilder interimResult = null;

        try {
            InputStream e = this.appContext.getResources().openRawResource(R.raw.license);
            InputStreamReader licenseInputStreamReader = new InputStreamReader(e);
            BufferedReader r = new BufferedReader(licenseInputStreamReader);
            interimResult = new StringBuilder();

            String line;
            while((line = r.readLine()) != null) {
                interimResult.append(line);
                interimResult.append("\n");
            }

            r.close();
            licenseInputStreamReader.close();
            e.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        result = interimResult == null?null:interimResult.toString();
        return result;
    }

    protected void onPostExecute(String result) {
        synchronized(this) {
            this.mResult = result;
            this.appContext = null;
            if(this.lrl != null) {
                this.lrl.readLicenseComplete(result);
            }

        }
    }

    protected void onCancelled(String result) {
        synchronized(this) {
            this.mResult = result;
            this.appContext = null;
            if(this.lrl != null) {
                this.lrl.readLicenseComplete(result);
            }

        }
    }

    public String getResult() {
        return this.mResult;
    }

    public void setLicenseReaderListener(LicenseReaderListener listener) {
        synchronized(this) {
            this.lrl = listener;
        }
    }

    public void clearLicenseReaderListener(LicenseReaderListener listener) {
        synchronized(this) {
            if(this.lrl == listener) {
                this.lrl = null;
            }

        }
    }

    public void setAppName(String appName) {
        synchronized(this) {
            this.appName = appName;
        }
    }

    public String getAppName() {
        return this.appName;
    }

    public void setApplication(AppAwareApplication appContext) {
        synchronized(this) {
            this.appContext = appContext;
        }
    }

    public Application getApplication() {
        return this.appContext;
    }
}