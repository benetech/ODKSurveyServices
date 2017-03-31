package org.opendatakit.demoAndroidCommonClasses.listener;

import java.util.ArrayList;

public interface InitializationListener {
    void initializationComplete(boolean var1, ArrayList<String> var2);

    void initializationProgressUpdate(String var1);
}
