package org.opendatakit.demoAndroidCommonClasses.activities;

import android.os.Bundle;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidCommonClasses.listener.DatabaseConnectionListener;
import org.opendatakit.demoAndroidCommonClasses.views.ExecutorContext;
import org.opendatakit.demoAndroidCommonClasses.views.ExecutorProcessor;

public interface IOdkDataActivity {
    void signalResponseAvailable(String var1);

    String getResponseJSON();

    ExecutorProcessor newExecutorProcessor(ExecutorContext var1);

    void registerDatabaseConnectionBackgroundListener(DatabaseConnectionListener var1);

    UserDbInterface getDatabase();

    String getAppName();

    Bundle getIntentExtras();
}
