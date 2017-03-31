package org.opendatakit.demoAndroidCommonClasses.task;

import android.os.AsyncTask;
import java.util.ArrayList;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidlibraryClasses.builder.InitializationOutcome;
import org.opendatakit.demoAndroidlibraryClasses.builder.InitializationSupervisor;
import org.opendatakit.demoAndroidlibraryClasses.builder.InitializationUtil;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidCommonClasses.listener.InitializationListener;

public class InitializationTask extends AsyncTask<Void, String, InitializationOutcome> {
    private static final String t = "InitializationTask";
    private CommonApplication appContext;
    private InitializationListener mStateListener;
    private String appName;
    private boolean mSuccess = false;
    private ArrayList<String> mResult = new ArrayList();

    public InitializationTask() {
    }

    protected InitializationOutcome doInBackground(Void... values) {
        InitializationUtil util = new InitializationUtil(this.appContext, this.appName, new InitializationSupervisor() {
            public UserDbInterface getDatabase() {
                return InitializationTask.this.appContext.getDatabase();
            }

            public void publishProgress(String progress, String detail) {
                InitializationTask.this.publishProgress(new String[]{progress, detail});
            }

            public boolean isCancelled() {
                return InitializationTask.this.isCancelled();
            }

            public String getToolName() {
                return InitializationTask.this.appContext.getToolName();
            }

            public String getVersionCodeString() {
                return InitializationTask.this.appContext.getVersionCodeString();
            }

            public int getSystemZipResourceId() {
                return InitializationTask.this.appContext.getSystemZipResourceId();
            }

            public int getConfigZipResourceId() {
                return InitializationTask.this.appContext.getConfigZipResourceId();
            }
        });
        InitializationOutcome pendingOutcome = util.initialize();
        return pendingOutcome;
    }

    protected void onPostExecute(InitializationOutcome pendingOutcome) {
        synchronized(this) {
            this.mResult = pendingOutcome.outcomeLineItems;
            this.mSuccess = !pendingOutcome.problemExtractingToolZipContent && !pendingOutcome.problemDefiningTables && !pendingOutcome.problemDefiningForms && !pendingOutcome.problemImportingAssetCsvContent && pendingOutcome.assetsCsvFileNotFoundSet.isEmpty();
            if(this.mStateListener != null) {
                this.mStateListener.initializationComplete(this.mSuccess, this.mResult);
            }

        }
    }

    protected void onCancelled(InitializationOutcome pendingOutcome) {
        synchronized(this) {
            this.mResult = pendingOutcome == null?new ArrayList():pendingOutcome.outcomeLineItems;
            this.mSuccess = false;
            if(this.mStateListener != null) {
                this.mStateListener.initializationComplete(this.mSuccess, this.mResult);
            }

        }
    }

    protected void onProgressUpdate(String... values) {
        synchronized(this) {
            if(this.mStateListener != null) {
                this.mStateListener.initializationProgressUpdate(values[0] + (values[1] != null?"\n(" + values[1] + ")":""));
            }

        }
    }

    public boolean getOverallSuccess() {
        return this.mSuccess;
    }

    public ArrayList<String> getResult() {
        return this.mResult;
    }

    public void setInitializationListener(InitializationListener sl) {
        synchronized(this) {
            this.mStateListener = sl;
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

    public void setApplication(CommonApplication appContext) {
        synchronized(this) {
            this.appContext = appContext;
        }
    }

    public CommonApplication getApplication() {
        return this.appContext;
    }
}
