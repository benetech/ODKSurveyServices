package org.opendatakit.demoAndroidCommonClasses.views;

import android.webkit.JavascriptInterface;
import java.lang.ref.WeakReference;

public class OdkCommonIf {
    public static final String TAG = "OdkCommonIf";
    private WeakReference<OdkCommon> weakControl;

    OdkCommonIf(OdkCommon odkCommon) {
        this.weakControl = new WeakReference(odkCommon);
    }

    private boolean isInactive() {
        return this.weakControl.get() == null || ((OdkCommon)this.weakControl.get()).isInactive();
    }

    @JavascriptInterface
    public String getPlatformInfo() {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getPlatformInfo();
    }

    @JavascriptInterface
    public String getFileAsUrl(String relativePath) {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getFileAsUrl(relativePath);
    }

    @JavascriptInterface
    public String getRowFileAsUrl(String tableId, String rowId, String rowPathUri) {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getRowFileAsUrl(tableId, rowId, rowPathUri);
    }

    @JavascriptInterface
    public void log(String level, String loggingString) {
        if(!this.isInactive()) {
            ((OdkCommon)this.weakControl.get()).log(level, loggingString);
        }
    }

    @JavascriptInterface
    public String getActiveUser() {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getActiveUser();
    }

    @JavascriptInterface
    public String getProperty(String propertyId) {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getProperty(propertyId);
    }

    @JavascriptInterface
    public String getBaseUrl() {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getBaseUrl();
    }

    @JavascriptInterface
    public void setSessionVariable(String elementPath, String jsonValue) {
        if(!this.isInactive()) {
            ((OdkCommon)this.weakControl.get()).setSessionVariable(elementPath, jsonValue);
        }
    }

    @JavascriptInterface
    public String getSessionVariable(String elementPath) {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).getSessionVariable(elementPath);
    }

    @JavascriptInterface
    public String doAction(String dispatchString, String action, String jsonMap) {
        return this.isInactive()?"IGNORE":((OdkCommon)this.weakControl.get()).doAction(dispatchString, action, jsonMap);
    }

    @JavascriptInterface
    public String viewFirstQueuedAction() {
        return this.isInactive()?null:((OdkCommon)this.weakControl.get()).viewFirstQueuedAction();
    }

    @JavascriptInterface
    public void removeFirstQueuedAction() {
        if(!this.isInactive()) {
            ((OdkCommon)this.weakControl.get()).removeFirstQueuedAction();
        }
    }
}
