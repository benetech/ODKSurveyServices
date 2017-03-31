package org.opendatakit.demoAndroidCommonClasses.activities;

import android.app.ListActivity;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;

public abstract class BaseListActivity extends ListActivity implements IAppAwareActivity {
    public BaseListActivity() {
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
