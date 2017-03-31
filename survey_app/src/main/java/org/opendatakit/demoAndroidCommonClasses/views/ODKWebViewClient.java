package org.opendatakit.demoAndroidCommonClasses.views;

import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ODKWebViewClient extends WebViewClient {
    private static final String t = "ODKWebViewClient";
    private final ODKWebView wrappedView;

    ODKWebViewClient(ODKWebView wrappedView) {
        this.wrappedView = wrappedView;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "shouldOverrideUrlLoading: " + url + " ms: " + Long.toString(System.currentTimeMillis()));
        return super.shouldOverrideUrlLoading(view, url);
    }

    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "doUpdateVisitedHistory: " + url + " ms: " + Long.toString(System.currentTimeMillis()));
    }

    public void onLoadResource(WebView view, String url) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onLoadResource: " + url + " ms: " + Long.toString(System.currentTimeMillis()));
        super.onLoadResource(view, url);
    }

    public void onPageFinished(WebView view, String url) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onPageFinished: " + url + " ms: " + Long.toString(System.currentTimeMillis()));
        this.wrappedView.pageFinished(url);
        super.onPageFinished(view, url);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onReceivedError: " + failingUrl + " ms: " + Long.toString(System.currentTimeMillis()));
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onScaleChanged: " + newScale);
        super.onScaleChanged(view, oldScale, newScale);
    }

    public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onTooManyRedirects: " + cancelMsg.toString());
        super.onTooManyRedirects(view, cancelMsg, continueMsg);
    }

    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        this.wrappedView.getLogger().i("ODKWebViewClient", "onUnhandledKeyEvent: " + event.toString());
        super.onUnhandledKeyEvent(view, event);
    }
}
