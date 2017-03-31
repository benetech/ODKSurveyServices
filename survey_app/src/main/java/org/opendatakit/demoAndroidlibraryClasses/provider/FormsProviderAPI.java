package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.net.Uri;

public final class FormsProviderAPI {
    public static final String AUTHORITY = "org.opendatakit.provider.forms";
    public static final Uri CONTENT_URI = Uri.parse("content://org.opendatakit.provider.forms/");

    private FormsProviderAPI() {
    }
}
