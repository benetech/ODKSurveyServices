package org.opendatakit.demoAndroidCommonClasses.activities;

import android.support.v7.app.AppCompatActivity;

import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidCommonClasses.listener.DatabaseConnectionListener;

public abstract class BaseActivity extends AppCompatActivity implements DatabaseConnectionListener, IAppAwareActivity {
    public BaseActivity() {
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
