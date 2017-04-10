package org.opendatakit.demoAndroidCommonClasses.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.AsyncTask.Status;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import java.util.ArrayList;
import org.opendatakit.demoAndroidlibraryClasses.application.AppAwareApplication;
import org.opendatakit.demoAndroidlibraryClasses.database.service.UserDbInterface;
import org.opendatakit.demoAndroidCommonClasses.listener.DatabaseConnectionListener;
import org.opendatakit.demoAndroidCommonClasses.listener.InitializationListener;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidCommonClasses.task.InitializationTask;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.demoAndroidCommonClasses.views.ODKWebView;
import org.opendatakit.demoAndroidlibraryClasses.webkitserver.service.WebkitServerInterface;
import org.opendatakit.demoAndroidlibraryClasses.webkitserver.service.WebkitServerInterface.Stub;

public abstract class CommonApplication extends AppAwareApplication implements InitializationListener {
    private static final String t = "CommonApplication";
    public static final String PERMISSION_WEBSERVER = "org.opendatakit.webkitserver.RUN_WEBSERVER";
    public static final String PERMISSION_DATABASE = "org.opendatakit.database.RUN_DATABASE";
    private static boolean isMocked = false;
    private static boolean disableInitializeCascade = true;
    private static UserDbInterface mockDatabaseService = null;
    private static WebkitServerInterface mockWebkitServerService = null;
    private final CommonApplication.BackgroundTasks mBackgroundTasks = new CommonApplication.BackgroundTasks();
    private final CommonApplication.BackgroundServices mBackgroundServices = new CommonApplication.BackgroundServices();
    private InitializationListener mInitializationListener = null;
    private boolean shuttingDown = false;
    private Activity activeActivity = null;
    private Activity databaseListenerActivity = null;

    public static void setMocked() {
        isMocked = true;
    }

    public static boolean isMocked() {
        return isMocked;
    }

    public static boolean isDisableInitializeCascade() {
        return disableInitializeCascade;
    }

    public static void setEnableInitializeCascade() {
        disableInitializeCascade = false;
    }

    public static void setDisableInitializeCascade() {
        disableInitializeCascade = true;
    }

    public static void setMockDatabase(UserDbInterface mock) {
        mockDatabaseService = mock;
    }

    public static void setMockWebkitServer(WebkitServerInterface mock) {
        mockWebkitServerService = mock;
    }

    public static void mockServiceConnected(CommonApplication app, String name, IBinder service) {
        ComponentName className = null;
        if(name.equals("org.opendatakit.services.webkitservice.service.OdkWebkitServerService")) {
            className = new ComponentName("org.opendatakit.survey", "org.opendatakit.services.webkitservice.service.OdkWebkitServerService");
        }

        if(name.equals("org.opendatakit.services.database.service.OdkDatabaseService")) {
            className = new ComponentName("org.opendatakit.survey", "org.opendatakit.services.database.service.OdkDatabaseService");
        }

        if(className == null) {
            throw new IllegalStateException("unrecognized mockService");
        } else {
            app.mBackgroundServices.doServiceConnected(app, className, service);
        }
    }

    public static void mockServiceDisconnected(CommonApplication app, String name) {
        ComponentName className = null;
        if(name.equals("org.opendatakit.services.webkitservice.service.OdkWebkitServerService")) {
            className = new ComponentName("org.opendatakit.survey", "org.opendatakit.services.webkitservice.service.OdkWebkitServerService");
        }

        if(name.equals("org.opendatakit.services.database.service.OdkDatabaseService")) {
            className = new ComponentName("org.opendatakit.survey", "org.opendatakit.services.database.service.OdkDatabaseService");
        }

        if(className == null) {
            throw new IllegalStateException("unrecognized mockService");
        } else {
            app.mBackgroundServices.doServiceDisconnected(app, className);
        }
    }

    public static void createODKDirs(String appName) throws RuntimeException {
        ODKFileUtils.verifyExternalStorageAvailability();
        ODKFileUtils.assertDirectoryStructure(appName);
    }

    public CommonApplication() {
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        this.shuttingDown = false;
        super.onCreate();
        if(VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("CommonApplication", "onConfigurationChanged");
    }

    public void onTerminate() {
        this.cleanShutdown();
        super.onTerminate();
        Log.i("CommonApplication", "onTerminate");
    }

    public abstract int getConfigZipResourceId();

    public abstract int getSystemZipResourceId();

    public abstract int getWebKitResourceId();

    public boolean shouldRunInitializationTask(String appName) {
        if(isMocked() && isDisableInitializeCascade()) {
            return false;
        } else {
            PropertiesSingleton props = CommonToolProperties.get(this, appName);
            return props.shouldRunInitializationTask(this.getToolName());
        }
    }

    public void clearRunInitializationTask(String appName) {
        PropertiesSingleton props = CommonToolProperties.get(this, appName);
        props.clearRunInitializationTask(this.getToolName());
    }

    public void setRunInitializationTask(String appName) {
        PropertiesSingleton props = CommonToolProperties.get(this, appName);
        props.setRunInitializationTask(this.getToolName());
    }

    public void onActivityPause(Activity activity) {
        if(this.activeActivity == activity) {
            this.mInitializationListener = null;
            if(this.mBackgroundTasks.mInitializationTask != null) {
                this.mBackgroundTasks.mInitializationTask.setInitializationListener((InitializationListener)null);
            }
        }

    }

    public void onActivityDestroy(Activity activity) {
        if(this.activeActivity == activity) {
            this.activeActivity = null;
            this.mInitializationListener = null;
            if(this.mBackgroundTasks.mInitializationTask != null) {
                this.mBackgroundTasks.mInitializationTask.setInitializationListener((InitializationListener)null);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    CommonApplication.this.testForShutdown();
                }
            }, 500L);
        }

    }

    private void cleanShutdown() {
        try {
            this.shuttingDown = true;
            Log.i("CommonApplication", "cleanShutdown (initiating)");
            this.shutdownServices();
        } finally {
            this.shuttingDown = false;
            Log.i("CommonApplication", "cleanShutdown (resetting shuttingDown to false)");
        }

    }

    private void testForShutdown() {
        if(this.activeActivity == null) {
            this.cleanShutdown();
        }

    }

    public void onActivityResume(Activity activity) {
        this.databaseListenerActivity = null;
        this.activeActivity = activity;
        if(this.mBackgroundTasks.mInitializationTask != null) {
            this.mBackgroundTasks.mInitializationTask.setInitializationListener(this);
        }

        this.mBackgroundServices.clearDestroyingFlag();
        this.configureView();
        this.bindToService();
    }

    private void shutdownServices() {
        this.mBackgroundServices.shutdownServices(this);
    }

    private void bindToService() {
        if(!isMocked) {
            if(!this.shuttingDown) {
                PackageManager pm = this.getPackageManager();
                boolean useWebServer = pm.checkPermission("org.opendatakit.webkitserver.RUN_WEBSERVER", "org.opendatakit.survey") == 0;
                boolean useDatabase = pm.checkPermission("org.opendatakit.database.RUN_DATABASE", "org.opendatakit.survey") == 0;
                Log.i("CommonApplication", "bindToService -- useWebServer " + Boolean.toString(useWebServer) + " useDatabase " + Boolean.toString(useDatabase));
                this.mBackgroundServices.bindToService(this, useWebServer, useDatabase);
            }

        }
    }

    public UserDbInterface getDatabase() {
        return isMocked?mockDatabaseService:this.mBackgroundServices.getDatabase();
    }

    private WebkitServerInterface getWebkitServer() {
        return isMocked?mockWebkitServerService:this.mBackgroundServices.getWebkitServer();
    }

    public void configureView() {
        if(this.activeActivity != null) {
            Log.i("CommonApplication", "configureView - possibly updating service information within ODKWebView");
            if(this.getWebKitResourceId() != -1) {
                View v = this.activeActivity.findViewById(this.getWebKitResourceId());
                if(v != null && v instanceof ODKWebView) {
                    ODKWebView wv = (ODKWebView)v;
                    if(this.mBackgroundServices.isDestroyingFlag()) {
                        wv.serviceChange(false);
                    } else {
                        WebkitServerInterface webkitServerIf = this.getWebkitServer();
                        UserDbInterface dbIf = this.getDatabase();
                        wv.serviceChange(webkitServerIf != null && dbIf != null);
                    }
                }
            }
        }

    }

    public void establishDatabaseConnectionListener(Activity activity) {
        this.databaseListenerActivity = activity;
        this.triggerDatabaseEvent(true);
    }

    public void establishDoNotFireDatabaseConnectionListener(Activity activity) {
        this.databaseListenerActivity = activity;
    }

    public void fireDatabaseConnectionListener() {
        this.triggerDatabaseEvent(true);
    }

    public void possiblyFireDatabaseCallback(Activity activity, DatabaseConnectionListener listener) {
        if(this.activeActivity != null && this.activeActivity == this.databaseListenerActivity && this.databaseListenerActivity == activity) {
            if(this.getDatabase() == null) {
                listener.databaseUnavailable();
            } else {
                listener.databaseAvailable();
            }
        }

    }

    private void triggerDatabaseEvent(boolean availableOnly) {
        if(this.activeActivity != null && this.activeActivity == this.databaseListenerActivity && this.activeActivity instanceof DatabaseConnectionListener) {
            if(this.getDatabase() == null) {
                if(!availableOnly) {
                    ((DatabaseConnectionListener)this.activeActivity).databaseUnavailable();
                }
            } else {
                ((DatabaseConnectionListener)this.activeActivity).databaseAvailable();
            }
        }

    }

    public void establishInitializationListener(InitializationListener listener) {
        this.mInitializationListener = listener;
        if(this.mBackgroundTasks.mInitializationTask != null && this.mBackgroundTasks.mInitializationTask.getStatus() == Status.FINISHED) {
            this.initializationComplete(this.mBackgroundTasks.mInitializationTask.getOverallSuccess(), this.mBackgroundTasks.mInitializationTask.getResult());
        }

    }

    public synchronized boolean initializeAppName(String appName, InitializationListener listener) {
        this.mInitializationListener = listener;
        if(this.mBackgroundTasks.mInitializationTask != null && this.mBackgroundTasks.mInitializationTask.getStatus() != Status.FINISHED) {
            return true;
        } else if(this.getDatabase() != null) {
            InitializationTask cf = new InitializationTask();
            cf.setApplication(this);
            cf.setAppName(appName);
            cf.setInitializationListener(this);
            this.mBackgroundTasks.mInitializationTask = cf;
            this.mBackgroundTasks.mInitializationTask.execute(new Void[]{(Void)null});
            return true;
        } else {
            return false;
        }
    }

    public synchronized void clearInitializationTask() {
        this.mInitializationListener = null;
        if(this.mBackgroundTasks.mInitializationTask != null) {
            this.mBackgroundTasks.mInitializationTask.setInitializationListener((InitializationListener)null);
            if(this.mBackgroundTasks.mInitializationTask.getStatus() != Status.FINISHED) {
                this.mBackgroundTasks.mInitializationTask.cancel(true);
            }
        }

        this.mBackgroundTasks.mInitializationTask = null;
    }

    public synchronized void cancelInitializationTask() {
        if(this.mBackgroundTasks.mInitializationTask != null && this.mBackgroundTasks.mInitializationTask.getStatus() != Status.FINISHED) {
            this.mBackgroundTasks.mInitializationTask.cancel(true);
        }

    }

    public void initializationComplete(boolean overallSuccess, ArrayList<String> result) {
        if(this.mInitializationListener != null) {
            this.mInitializationListener.initializationComplete(overallSuccess, result);
        }

    }

    public void initializationProgressUpdate(String status) {
        if(this.mInitializationListener != null) {
            this.mInitializationListener.initializationProgressUpdate(status);
        }

    }

    private static final class BackgroundServices {
        private ServiceConnection webkitfilesServiceConnection = null;
        private WebkitServerInterface webkitfilesService = null;
        private ServiceConnection databaseServiceConnection = null;
        private UserDbInterface databaseService = null;
        private boolean isDestroying = false;

        BackgroundServices() {
        }

        synchronized void clearDestroyingFlag() {
            Log.i("CommonApplication", "isDestroying reset to false");
            this.isDestroying = false;
        }

        synchronized boolean isDestroyingFlag() {
            return this.isDestroying;
        }

        public synchronized UserDbInterface getDatabase() {
            return this.databaseService;
        }

        private synchronized WebkitServerInterface getWebkitServer() {
            return this.webkitfilesService;
        }

        private void bindToService(final CommonApplication application, boolean useWebServer, boolean useDatabase) {
            ServiceConnection webkitServerBinder = null;
            ServiceConnection databaseBinder = null;
            synchronized(this) {
                if(!this.isDestroying) {
                    Log.i("CommonApplication", "bindToService -- processing...");
                    if(useWebServer && this.webkitfilesService == null && this.webkitfilesServiceConnection == null) {
                        this.webkitfilesServiceConnection = webkitServerBinder = new ServiceConnection() {
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                BackgroundServices.this.doServiceConnected(application, name, service);
                            }

                            public void onServiceDisconnected(ComponentName name) {
                                BackgroundServices.this.doServiceDisconnected(application, name);
                            }
                        };
                    }

                    if(useDatabase && this.databaseService == null && this.databaseServiceConnection == null) {
                        this.databaseServiceConnection = databaseBinder = new ServiceConnection() {
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                BackgroundServices.this.doServiceConnected(application, name, service);
                            }

                            public void onServiceDisconnected(ComponentName name) {
                                BackgroundServices.this.doServiceDisconnected(application, name);
                            }
                        };
                    }
                } else {
                    Log.i("CommonApplication", "bindToService -- ignored -- isDestroying is true!");
                }
            }

            Intent bind_intent;
            if(webkitServerBinder != null) {
                Log.i("CommonApplication", "Attempting bind to WebkitServer service");
                bind_intent = new Intent();
                bind_intent.setClassName("org.opendatakit.survey", "org.opendatakit.services.webkitservice.service.OdkWebkitServerService");
                application.bindService(bind_intent, webkitServerBinder, Context.BIND_AUTO_CREATE);
            }

            if(databaseBinder != null) {
                Log.i("CommonApplication", "Attempting bind to Database service");
                bind_intent = new Intent();
                bind_intent.setClassName("org.opendatakit.survey", "org.opendatakit.services.database.service.OdkDatabaseService");
                application.bindService(bind_intent, databaseBinder, Context.BIND_AUTO_CREATE);
            }

        }

        private void doServiceConnected(CommonApplication application, ComponentName className, IBinder service) {
            if(className.getClassName().equals("org.opendatakit.services.webkitservice.service.OdkWebkitServerService")) {
                Log.i("CommonApplication", "Bound to WebServer service");
                synchronized(this) {
                    try {
                        this.webkitfilesService = service == null?null:Stub.asInterface(service);
                    } catch (Exception var10) {
                        this.webkitfilesService = null;
                    }
                }
            }

            if(className.getClassName().equals("org.opendatakit.services.database.service.OdkDatabaseService")) {
                Log.i("CommonApplication", "Bound to Database service");
                synchronized(this) {
                    try {
                        this.databaseService = service == null?null:new UserDbInterface(org.opendatakit.demoAndroidlibraryClasses.database.service.AidlDbInterface.Stub.asInterface(service));
                    } catch (Exception var8) {
                        this.databaseService = null;
                    }
                }

                application.triggerDatabaseEvent(false);
            }

            application.configureView();
        }

        private void doServiceDisconnected(CommonApplication application, ComponentName className) {
            ServiceConnection tmpDb;
            if(className.getClassName().equals("org.opendatakit.services.webkitservice.service.OdkWebkitServerService")) {
                tmpDb = null;
                synchronized(this) {
                    if(this.isDestroying) {
                        Log.i("CommonApplication", "Unbound from WebServer service (intentionally)");
                    } else {
                        Log.w("CommonApplication", "Unbound from WebServer service (unexpected)");
                    }

                    this.webkitfilesService = null;
                    tmpDb = this.webkitfilesServiceConnection;
                    this.webkitfilesServiceConnection = null;
                }

                try {
                    if(tmpDb != null) {
                        application.unbindService(tmpDb);
                    }
                } catch (Exception var9) {
                    var9.printStackTrace();
                }
            }

            if(className.getClassName().equals("org.opendatakit.services.database.service.OdkDatabaseService")) {
                tmpDb = null;
                synchronized(this) {
                    if(this.isDestroying) {
                        Log.i("CommonApplication", "Unbound from Database service (intentionally)");
                    } else {
                        Log.w("CommonApplication", "Unbound from Database service (unexpected)");
                    }

                    this.databaseService = null;
                    tmpDb = this.databaseServiceConnection;
                    this.databaseServiceConnection = null;
                }

                try {
                    if(tmpDb != null) {
                        application.unbindService(tmpDb);
                    }
                } catch (Exception var7) {
                    var7.printStackTrace();
                }

                application.triggerDatabaseEvent(false);
            }

            application.configureView();
            application.bindToService();
        }

        private void unbindWebkitServerWrapper(CommonApplication application) {
            ServiceConnection tmpWeb = null;
            synchronized(this) {
                tmpWeb = this.webkitfilesServiceConnection;
                this.webkitfilesServiceConnection = null;
            }

            try {
                if(tmpWeb != null) {
                    application.unbindService(tmpWeb);
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }

        private void unbindDatabaseWrapper(CommonApplication application) {
            ServiceConnection tmpDb = null;
            synchronized(this) {
                tmpDb = this.databaseServiceConnection;
                this.databaseServiceConnection = null;
            }

            try {
                if(tmpDb != null) {
                    application.unbindService(tmpDb);
                    application.triggerDatabaseEvent(false);
                }
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }

        private void shutdownServices(CommonApplication application) {
            Log.i("CommonApplication", "shutdownServices - Releasing WebServer and database service");
            ServiceConnection tmpWeb = null;
            ServiceConnection tmpDb = null;
            synchronized(this) {
                this.isDestroying = true;
                this.webkitfilesService = null;
                this.databaseService = null;
                tmpWeb = this.webkitfilesServiceConnection;
                tmpDb = this.databaseServiceConnection;
                this.webkitfilesServiceConnection = null;
                this.databaseServiceConnection = null;
            }

            try {
                if(tmpWeb != null) {
                    application.unbindService(tmpWeb);
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            try {
                if(tmpDb != null) {
                    application.unbindService(tmpDb);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            application.configureView();
            application.triggerDatabaseEvent(false);
        }
    }

    private static final class BackgroundTasks {
        InitializationTask mInitializationTask = null;

        BackgroundTasks() {
        }
    }
}
