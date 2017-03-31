package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.net.Uri;

public class TablesProviderAPI {
    public static final String AUTHORITY = "org.opendatakit.provider.tables";
    public static final Uri CONTENT_URI = Uri.parse("content://org.opendatakit.provider.tables/");

    private TablesProviderAPI() {
    }
}
