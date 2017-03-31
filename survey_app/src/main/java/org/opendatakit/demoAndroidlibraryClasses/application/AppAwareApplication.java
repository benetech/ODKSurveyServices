package org.opendatakit.demoAndroidlibraryClasses.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.util.Log;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.properties.CommonToolProperties;
import org.opendatakit.demoAndroidlibraryClasses.properties.PropertiesSingleton;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.demoAndroidlibraryClasses.utilities.PRNGFixes;

public abstract class AppAwareApplication extends Application {
    private static final String t = "AppAwareApplication";

    public static void createODKDirs(String appName) throws RuntimeException {
        ODKFileUtils.verifyExternalStorageAvailability();
        ODKFileUtils.assertDirectoryStructure(appName);
    }

    public AppAwareApplication() {
        PRNGFixes.apply();
    }

    public abstract int getApkDisplayNameResourceId();

    public void onCreate() {
        super.onCreate();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("AppAwareApplication", "onConfigurationChanged");
    }

    public void onTerminate() {
        WebLogger.closeAll();
        super.onTerminate();
        Log.i("AppAwareApplication", "onTerminate");
    }

    public int getQuestionFontsize(String appName) {
        PropertiesSingleton props = CommonToolProperties.get(this, appName);
        Integer question_font = props.getIntegerProperty("common.font_size");
        int questionFontsize = question_font == null?16:question_font.intValue();
        return questionFontsize;
    }

    public String getToolName() {
        String packageName = this.getPackageName();
        String[] parts = packageName.split("\\.");
        return parts[2];
    }

    public String getVersionCodeString() {
        try {
            PackageInfo e = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            int versionNumber = e.versionCode;
            return Integer.toString(versionNumber);
        } catch (NameNotFoundException var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public String getVersionDetail() {
        String versionDetail = "";

        try {
            PackageInfo e = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String versionName = e.versionName;
            versionDetail = " " + versionName;
        } catch (NameNotFoundException var4) {
            var4.printStackTrace();
        }

        return versionDetail;
    }

    public String getVersionedAppName() {
        String versionDetail = this.getVersionDetail();
        return this.getString(this.getApkDisplayNameResourceId()) + versionDetail;
    }
}
