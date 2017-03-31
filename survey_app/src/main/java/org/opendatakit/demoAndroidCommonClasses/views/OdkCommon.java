package org.opendatakit.demoAndroidCommonClasses.views;

import android.net.Uri;
import android.os.Build.VERSION;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.opendatakit.demoAndroidCommonClasses.activities.IOdkCommonActivity;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.provider.FormsProviderAPI;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class OdkCommon {
    private WeakReference<ODKWebView> mWebView;
    private IOdkCommonActivity mActivity;

    public OdkCommonIf getJavascriptInterfaceWithWeakReference() {
        return new OdkCommonIf(this);
    }

    public OdkCommon(IOdkCommonActivity activity, ODKWebView webView) {
        this.mActivity = activity;
        this.mWebView = new WeakReference(webView);
    }

    public boolean isInactive() {
        return this.mWebView.get() == null || ((ODKWebView)this.mWebView.get()).isInactive();
    }

    private void logDebug(String loggingString) {
        WebLogger.getLogger(this.mActivity.getAppName()).d("odkCommon", loggingString);
    }

    public String getPlatformInfo() {
        this.logDebug("getPlatformInfo()");
        String appName = this.mActivity.getAppName();
        HashMap platformInfo = new HashMap();
        platformInfo.put("version", VERSION.RELEASE);
        platformInfo.put("container", "Android");
        platformInfo.put("appName", appName);
        platformInfo.put("baseUri", this.getBaseContentUri());
        platformInfo.put("formsUri", FormsProviderAPI.CONTENT_URI.toString());
        platformInfo.put("activeUser", this.getActiveUser());
        platformInfo.put("logLevel", "D");
        JSONObject jsonObject = new JSONObject(platformInfo);
        String result = jsonObject.toString();
        return result;
    }

    public String getFileAsUrl(String relativePath) {
        this.logDebug("getFileAsUrl(" + relativePath + ")");
        String baseUri = this.getBaseContentUri();
        String result = baseUri + relativePath;
        return result;
    }

    public String getRowFileAsUrl(String tableId, String rowId, String rowPathUri) {
        this.logDebug("getRowFileAsUrl(" + tableId + ", " + rowId + ", " + rowPathUri + ")");
        String appName = this.mActivity.getAppName();
        String baseUri = this.getBaseContentUri();
        File rowpathFile = ODKFileUtils.getRowpathFile(appName, tableId, rowId, rowPathUri);
        String uriFragment = ODKFileUtils.asUriFragment(appName, rowpathFile);
        return baseUri + uriFragment;
    }

    public void log(String level, String loggingString) {
        char l = level == null?73:level.charAt(0);
        switch(l) {
            case 'A':
                WebLogger.getLogger(this.mActivity.getAppName()).a("odkCommon", loggingString);
                break;
            case 'B':
            case 'C':
            case 'F':
            case 'G':
            case 'H':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            default:
                WebLogger.getLogger(this.mActivity.getAppName()).i("odkCommon", loggingString);
                break;
            case 'D':
                WebLogger.getLogger(this.mActivity.getAppName()).d("odkCommon", loggingString);
                break;
            case 'E':
                WebLogger.getLogger(this.mActivity.getAppName()).e("odkCommon", loggingString);
                break;
            case 'I':
                WebLogger.getLogger(this.mActivity.getAppName()).i("odkCommon", loggingString);
                break;
            case 'S':
                WebLogger.getLogger(this.mActivity.getAppName()).s("odkCommon", loggingString);
                break;
            case 'V':
                WebLogger.getLogger(this.mActivity.getAppName()).v("odkCommon", loggingString);
                break;
            case 'W':
                WebLogger.getLogger(this.mActivity.getAppName()).w("odkCommon", loggingString);
        }

    }

    public String getActiveUser() {
        this.logDebug("getActiveUser()");
        return this.mActivity.getActiveUser();
    }

    public String getProperty(String propertyId) {
        this.logDebug("getProperty(" + propertyId + ")");
        return this.mActivity.getProperty(propertyId);
    }

    public String getBaseUrl() {
        this.logDebug("getBaseUrl()");
        return ODKFileUtils.getRelativeSystemPath();
    }

    private String getBaseContentUri() {
        this.logDebug("getBaseContentUri()");
        Uri contentUri = Uri.parse(this.mActivity.getWebViewContentUri());
        String appName = this.mActivity.getAppName();
        contentUri = Uri.withAppendedPath(contentUri, Uri.encode(appName));
        return contentUri.toString() + "/";
    }

    public void setSessionVariable(String elementPath, String jsonValue) {
        this.logDebug("setSessionVariable(" + elementPath + ", ...)");
        this.mActivity.setSessionVariable(elementPath, jsonValue);
    }

    public String getSessionVariable(String elementPath) {
        this.logDebug("getSessionVariable(" + elementPath + ")");
        return this.mActivity.getSessionVariable(elementPath);
    }

    public String doAction(String dispatchString, String action, String jsonMap) {
        this.logDebug("doAction(" + dispatchString + ", " + action + ", ...)");
        JSONObject valueMap = null;

        try {
            if(jsonMap != null && jsonMap.length() != 0) {
                valueMap = (JSONObject)(new JSONTokener(jsonMap)).nextValue();
            }
        } catch (JSONException var6) {
            var6.printStackTrace();
            this.log("E", "doAction(" + dispatchString + ", " + action + ", ...) " + var6.toString());
            return "ERROR";
        }

        return this.mActivity.doAction(dispatchString, action, valueMap);
    }

    public String viewFirstQueuedAction() {
        this.logDebug("viewFirstQueuedAction()");
        return this.mActivity.viewFirstQueuedAction();
    }

    public void removeFirstQueuedAction() {
        this.logDebug("removeFirstQueuedAction()");
        this.mActivity.removeFirstQueuedAction();
    }

    private static class PlatformInfoKeys {
        public static final String CONTAINER = "container";
        public static final String VERSION = "version";
        public static final String APP_NAME = "appName";
        public static final String BASE_URI = "baseUri";
        public static final String LOG_LEVEL = "logLevel";
        public static final String FORMS_URI = "formsUri";
        public static final String ACTIVE_USER = "activeUser";

        private PlatformInfoKeys() {
        }
    }
}
