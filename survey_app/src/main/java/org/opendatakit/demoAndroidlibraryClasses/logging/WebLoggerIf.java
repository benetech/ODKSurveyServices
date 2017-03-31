package org.opendatakit.demoAndroidlibraryClasses.logging;

public interface WebLoggerIf {
    void staleFileScan(long var1);

    void close();

    void log(int var1, String var2, String var3);

    void a(String var1, String var2);

    void t(String var1, String var2);

    void v(String var1, String var2);

    void d(String var1, String var2);

    void i(String var1, String var2);

    void w(String var1, String var2);

    void e(String var1, String var2);

    void s(String var1, String var2);

    void printStackTrace(Throwable var1);
}
