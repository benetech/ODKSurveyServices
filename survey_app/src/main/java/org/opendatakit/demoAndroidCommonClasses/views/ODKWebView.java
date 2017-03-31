package org.opendatakit.demoAndroidCommonClasses.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.Iterator;
import java.util.LinkedList;
import org.opendatakit.demoAndroidlibraryClasses.activities.IAppAwareActivity;
import org.opendatakit.demoAndroidCommonClasses.activities.IOdkCommonActivity;
import org.opendatakit.demoAndroidCommonClasses.activities.IOdkDataActivity;
import org.opendatakit.demoAndroidCommonClasses.application.CommonApplication;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLoggerIf;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

@SuppressLint({"SetJavaScriptEnabled"})
public abstract class ODKWebView extends WebView {
    private static final String t = "ODKWebView";
    private static final String BASE_STATE = "BASE_STATE";
    private static final String JAVASCRIPT_REQUESTS_WAITING_FOR_PAGE_LOAD = "JAVASCRIPT_REQUESTS_WAITING_FOR_PAGE_LOAD";
    protected WebLoggerIf log;
    private OdkCommon odkCommon;
    private OdkData odkData;
    private boolean isInactive = false;
    private String loadPageUrl = null;
    private boolean isLoadPageFrameworkFinished = false;
    private boolean isLoadPageFinished = false;
    private boolean isJavascriptFlushActive = false;
    private boolean isFirstPageLoad = true;
    private boolean shouldForceLoadDuringReload = false;
    private final LinkedList<String> javascriptRequestsWaitingForPageLoad = new LinkedList();

    public abstract boolean hasPageFramework();

    public abstract void loadPage();

    public abstract void reloadPage();

    public boolean isInactive() {
        return this.isInactive;
    }

    public void setInactive() {
        this.isInactive = true;
    }

    public void serviceChange(boolean ready) {
        if(ready) {
            this.loadPage();
        } else {
            this.resetLoadPageStatus(this.loadPageUrl);
        }

    }

    public String getLoadPageUrl() {
        return this.loadPageUrl;
    }

    protected Parcelable onSaveInstanceState() {
        this.log.i("ODKWebView", "[" + this.hashCode() + "] onSaveInstanceState()");
        Parcelable baseState = super.onSaveInstanceState();
        Bundle savedState = new Bundle();
        if(baseState != null) {
            savedState.putParcelable("BASE_STATE", baseState);
        }

        if(this.javascriptRequestsWaitingForPageLoad.size() == 0) {
            return savedState;
        } else {
            String[] waitQueue = new String[this.javascriptRequestsWaitingForPageLoad.size()];
            int i = 0;

            String s;
            for(Iterator i$ = this.javascriptRequestsWaitingForPageLoad.iterator(); i$.hasNext(); waitQueue[i++] = s) {
                s = (String)i$.next();
            }

            savedState.putStringArray("JAVASCRIPT_REQUESTS_WAITING_FOR_PAGE_LOAD", waitQueue);
            return savedState;
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        this.log.i("ODKWebView", "[" + this.hashCode() + "] onRestoreInstanceState()");
        Bundle savedState = (Bundle)state;
        if(savedState.containsKey("JAVASCRIPT_REQUESTS_WAITING_FOR_PAGE_LOAD")) {
            String[] baseState = savedState.getStringArray("JAVASCRIPT_REQUESTS_WAITING_FOR_PAGE_LOAD");
            String[] arr$ = baseState;
            int len$ = baseState.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String s = arr$[i$];
                this.javascriptRequestsWaitingForPageLoad.add(s);
            }
        }

        this.isFirstPageLoad = true;
        if(savedState.containsKey("BASE_STATE")) {
            Parcelable var8 = savedState.getParcelable("BASE_STATE");
            super.onRestoreInstanceState(var8);
        }

        this.loadPage();
    }

    @SuppressLint({"NewApi"})
    public void onPause() {
        super.onPause();
    }

    @SuppressLint({"NewApi"})
    private void perhapsEnableDebugging() {
        if(VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

    }

    public ODKWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(VERSION.SDK_INT < 11) {
            throw new IllegalStateException("pre-3.0 not supported!");
        } else {
            String appName = ((IAppAwareActivity)context).getAppName();
            this.log = WebLogger.getLogger(appName);
            this.log.i("ODKWebView", "[" + this.hashCode() + "] ODKWebView()");
            this.perhapsEnableDebugging();
            WebSettings ws = this.getSettings();
            ws.setAllowFileAccess(true);
            ws.setAppCacheEnabled(true);
            ws.setAppCachePath(ODKFileUtils.getAppCacheFolder(appName));
            //ws.setCacheMode(-1);
            ws.setDatabaseEnabled(false);
            ws.setDefaultFixedFontSize(((CommonApplication)context.getApplicationContext()).getQuestionFontsize(appName));
            ws.setDefaultFontSize(((CommonApplication)context.getApplicationContext()).getQuestionFontsize(appName));
            ws.setDomStorageEnabled(true);
            ws.setGeolocationDatabasePath(ODKFileUtils.getGeoCacheFolder(appName));
            ws.setGeolocationEnabled(true);
            ws.setJavaScriptCanOpenWindowsAutomatically(true);
            ws.setJavaScriptEnabled(true);
            ws.setBuiltInZoomControls(true);
            ws.setSupportZoom(true);
            ws.setUseWideViewPort(false);
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
            this.setSaveEnabled(true);
            this.setWebChromeClient(new ODKWebChromeClient(this));
            this.setWebViewClient(new ODKWebViewClient(this));
            this.odkCommon = new OdkCommon((IOdkCommonActivity)context, this);
            this.addJavascriptInterface(this.odkCommon.getJavascriptInterfaceWithWeakReference(), "odkCommonIf");
            this.odkData = new OdkData((IOdkDataActivity)context, this);
            this.addJavascriptInterface(this.odkData.getJavascriptInterfaceWithWeakReference(), "odkDataIf");
        }
    }

    public void destroy() {
        this.setInactive();
        if(this.odkData != null) {
            this.odkData.shutdownContext();
        }

        super.destroy();
    }

    public final WebLoggerIf getLogger() {
        return this.log;
    }

    public void signalQueuedActionAvailable() {
        this.log.i("ODKWebView", "[" + this.hashCode() + "] signalQueuedActionAvailable()");
        this.loadJavascriptUrl("javascript:window.odkCommon.signalQueuedActionAvailable()");
    }

    public void signalResponseAvailable() {
        this.log.i("ODKWebView", "[" + this.hashCode() + "] signalResponseAvailable()");
        this.loadJavascriptUrl("javascript:odkData.responseAvailable();");
    }

    private synchronized void loadJavascriptUrl(final String javascriptUrl) {
        if(!this.isInactive()) {
            if(!this.isLoadPageFinished && !this.isJavascriptFlushActive) {
                this.log.i("ODKWebView", "[" + this.hashCode() + "] loadJavascriptUrl: QUEUING: " + javascriptUrl);
                this.javascriptRequestsWaitingForPageLoad.add(javascriptUrl);
            } else {
                this.log.i("ODKWebView", "[" + this.hashCode() + "] loadJavascriptUrl: IMMEDIATE: " + javascriptUrl);
                if(Thread.currentThread() != Looper.getMainLooper().getThread()) {
                    this.post(new Runnable() {
                        public void run() {
                            ODKWebView.this.loadUrl(javascriptUrl);
                        }
                    });
                } else {
                    this.loadUrl(javascriptUrl);
                }
            }

        }
    }

    public void gotoUrlHash(String hash) {
        this.log.i("ODKWebView", "[" + this.hashCode() + "] gotoUrlHash: " + hash);
        ((IOdkCommonActivity)this.getContext()).queueUrlChange(hash);
        this.signalQueuedActionAvailable();
    }

    public void pageFinished(String url) {
        if(!this.hasPageFramework()) {
            String intendedPageToLoad = this.getLoadPageUrl();
            if(url != null && intendedPageToLoad != null) {
                int idxHash = intendedPageToLoad.indexOf(35);
                if(idxHash != -1) {
                    intendedPageToLoad = intendedPageToLoad.substring(0, idxHash);
                }

                idxHash = url.indexOf(35);
                if(idxHash != -1) {
                    url = url.substring(0, idxHash);
                }

                int idxQuestion = intendedPageToLoad.indexOf(63);
                if(idxQuestion == intendedPageToLoad.length() - 1) {
                    intendedPageToLoad = intendedPageToLoad.substring(0, idxQuestion);
                }

                idxQuestion = url.indexOf(63);
                if(idxQuestion == url.length() - 1) {
                    url = url.substring(0, idxQuestion);
                }

                if(url.equals(intendedPageToLoad)) {
                    this.frameworkHasLoaded();
                }
            }
        }

    }

    protected boolean hasPageFrameworkFinishedLoading() {
        return this.isLoadPageFrameworkFinished;
    }

    public void setForceLoadDuringReload() {
        this.shouldForceLoadDuringReload = true;
    }

    protected boolean shouldForceLoadDuringReload() {
        return this.shouldForceLoadDuringReload;
    }

    public synchronized void frameworkHasLoaded() {
        this.isLoadPageFrameworkFinished = true;
        if(!this.isLoadPageFinished && !this.isJavascriptFlushActive) {
            this.log.i("ODKWebView", "[" + this.hashCode() + "] loadPageFinished: BEGINNING FLUSH");
            this.isJavascriptFlushActive = true;

            while(this.isJavascriptFlushActive && !this.javascriptRequestsWaitingForPageLoad.isEmpty()) {
                String s = (String)this.javascriptRequestsWaitingForPageLoad.removeFirst();
                this.log.i("ODKWebView", "[" + this.hashCode() + "] loadPageFinished: DISPATCHING javascriptUrl: " + s);
                this.loadJavascriptUrl(s);
            }

            this.isLoadPageFinished = true;
            this.isJavascriptFlushActive = false;
            this.isFirstPageLoad = false;
        } else {
            this.log.i("ODKWebView", "[" + this.hashCode() + "] loadPageFinished: IGNORING completion event");
        }

    }

    protected synchronized void resetLoadPageStatus(String baseUrl) {
        this.isLoadPageFrameworkFinished = false;
        this.isLoadPageFinished = false;
        this.loadPageUrl = baseUrl;
        this.isJavascriptFlushActive = false;
        this.shouldForceLoadDuringReload = false;
        if(!this.isFirstPageLoad) {
            while(!this.javascriptRequestsWaitingForPageLoad.isEmpty()) {
                String s = (String)this.javascriptRequestsWaitingForPageLoad.removeFirst();
                this.log.i("ODKWebView", "resetLoadPageStatus: DISCARDING javascriptUrl: " + s);
            }
        }

    }
}
