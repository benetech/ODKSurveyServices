package org.opendatakit.services.sync.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import org.opendatakit.demoAndroidlibraryClasses.consts.IntentConsts;
import org.opendatakit.demoAndroidlibraryClasses.database.service.AidlDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;

public class ServiceConnectionWrapper implements ServiceConnection {

    private static final String TAG = "ServiceConnectionWrapper";
    private final Object odkDbInterfaceBindComplete = new Object();
    private boolean active = false;
    private UserDbInterface odkDbInterface;
    private Context context;
    private String appName;

    public ServiceConnectionWrapper(Context context, String appName) {
        this.context = context;
        this.appName = appName;
    }

    @Override public void onServiceConnected(ComponentName name, IBinder service) {

        if (!name.getClassName().equals(IntentConsts.Database.DATABASE_SERVICE_CLASS)) {
            WebLogger.getLogger(appName).e(TAG, "Unrecognized service");
            return;
        }
        synchronized (odkDbInterfaceBindComplete) {
            try {
                odkDbInterface = (service == null) ? null : new UserDbInterface(AidlDbInterface
                        .Stub.asInterface(service));
            } catch (IllegalArgumentException e) {
                odkDbInterface = null;
            }

            active = false;
            odkDbInterfaceBindComplete.notify();
        }
    }

    @Override public void onServiceDisconnected(ComponentName name) {
        synchronized (odkDbInterfaceBindComplete) {
            odkDbInterface = null;
            active = false;
            odkDbInterfaceBindComplete.notify();
        }
    }

    /**
     * Work-around for jacoco ART issue https://code.google.com/p/android/issues/detail?id=80961
     */
    private UserDbInterface invokeBindService() throws InterruptedException {

        WebLogger.getLogger(appName).e(TAG, "Attempting or polling on bind to Database service");
        Intent bind_intent = new Intent();
        bind_intent.setClassName(IntentConsts.Database.DATABASE_SERVICE_PACKAGE,
                IntentConsts.Database.DATABASE_SERVICE_CLASS);

        synchronized (odkDbInterfaceBindComplete) {
            if ( !active ) {
                active = true;
                context.bindService(bind_intent, this,
                        Context.BIND_AUTO_CREATE | ((Build.VERSION.SDK_INT >= 14) ?
                                Context.BIND_ADJUST_WITH_ACTIVITY :
                                0));
            }

            odkDbInterfaceBindComplete.wait();

            if (odkDbInterface != null) {
                return odkDbInterface;
            }
        }
        return null;
    }

    public UserDbInterface getDatabaseService() {

        // block waiting for it to be bound...
        for (;;) {
            try {

                synchronized (odkDbInterfaceBindComplete) {
                    if (odkDbInterface != null) {
                        return odkDbInterface;
                    }
                }

                // call method that waits on odkDbInterfaceBindComplete
                // Work-around for jacoco ART issue https://code.google.com/p/android/issues/detail?id=80961
                UserDbInterface userDbInterface = invokeBindService();
                if ( userDbInterface != null ) {
                    return userDbInterface;
                }

            } catch (InterruptedException e) {
                // expected if we are waiting. Ignore because we log bind attempt if spinning.
            }
        }
    }
}
