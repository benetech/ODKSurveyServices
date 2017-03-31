package org.opendatakit.demoAndroidlibraryClasses.builder;

public interface InitializationSupervisor extends CsvUtilSupervisor {
    void publishProgress(String var1, String var2);

    boolean isCancelled();

    String getToolName();

    String getVersionCodeString();

    int getSystemZipResourceId();

    int getConfigZipResourceId();
}