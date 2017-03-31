package org.opendatakit.demoAndroidlibraryClasses.provider;

import android.net.Uri;

public final class InstanceProviderAPI {
    public static final String AUTHORITY = "org.opendatakit.provider.instances";
    public static final Uri CONTENT_URI = Uri.parse("content://org.opendatakit.provider.instances/");

    private InstanceProviderAPI() {
    }
}
