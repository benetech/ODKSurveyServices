package org.opendatakit.demoAndroidCommonClasses.views;

import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.ConsoleMessage.MessageLevel;
import android.webkit.GeolocationPermissions.Callback;

public class ODKWebChromeClient extends WebChromeClient {
    private static final String t = "ODKWebChromeClient";
    private ODKWebView wrappedWebView;

    public ODKWebChromeClient(ODKWebView wrappedWebView) {
        this.wrappedWebView = wrappedWebView;
    }

    public void getVisitedHistory(ValueCallback<String[]> callback) {
        callback.onReceiveValue(new String[0]);
    }

    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        if(consoleMessage.sourceId() != null && consoleMessage.sourceId().length() != 0) {
            if(consoleMessage.messageLevel() == MessageLevel.DEBUG) {
                this.wrappedWebView.getLogger().d("ODKWebChromeClient", consoleMessage.message());
            } else if(consoleMessage.messageLevel() == MessageLevel.ERROR) {
                this.wrappedWebView.getLogger().e("ODKWebChromeClient", consoleMessage.message());
            } else if(consoleMessage.messageLevel() == MessageLevel.LOG) {
                this.wrappedWebView.getLogger().i("ODKWebChromeClient", consoleMessage.message());
            } else if(consoleMessage.messageLevel() == MessageLevel.TIP) {
                this.wrappedWebView.getLogger().t("ODKWebChromeClient", consoleMessage.message());
            } else if(consoleMessage.messageLevel() == MessageLevel.WARNING) {
                this.wrappedWebView.getLogger().w("ODKWebChromeClient", consoleMessage.message());
            } else {
                this.wrappedWebView.getLogger().e("ODKWebChromeClient", consoleMessage.message());
            }

            return true;
        } else {
            this.wrappedWebView.getLogger().e("ODKWebChromeClient", "onConsoleMessage: Javascript exception: " + consoleMessage.message());
            return true;
        }
    }

    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        this.wrappedWebView.getLogger().w("ODKWebChromeClient", url + ": " + message);
        return false;
    }

    public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
        callback.invoke(origin, true, false);
    }
}
