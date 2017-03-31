package org.opendatakit.demoAndroidCommonClasses.activities;

import android.preference.PreferenceActivity;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;

public abstract class BasePreferenceActivity extends PreferenceActivity implements IAppAwareActivity {
    public BasePreferenceActivity() {
    }

    protected void onResume() {
        super.onResume();
        ((CommonApplication)this.getApplication()).onActivityResume(this);
    }

    protected void onPause() {
        ((CommonApplication)this.getApplication()).onActivityPause(this);
        super.onPause();
    }

    protected void onDestroy() {
        ((CommonApplication)this.getApplication()).onActivityDestroy(this);
        super.onDestroy();
    }
}
