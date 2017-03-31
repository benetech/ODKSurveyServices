package org.opendatakit.demoAndroidCommonClasses.activities;

import org.json.JSONObject;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;

public interface IOdkCommonActivity extends IAppAwareActivity, IInitResumeActivity {
    String getActiveUser();

    String getProperty(String var1);

    String getWebViewContentUri();

    void setSessionVariable(String var1, String var2);

    String getSessionVariable(String var1);

    String doAction(String var1, String var2, JSONObject var3);

    void queueActionOutcome(String var1);

    void queueUrlChange(String var1);

    String viewFirstQueuedAction();

    void removeFirstQueuedAction();

    void runOnUiThread(Runnable var1);
}
