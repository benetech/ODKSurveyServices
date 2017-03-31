package org.opendatakit.demoAndroidCommonClasses.webkitserver.utilities;

import android.content.Context;
import android.net.Uri;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {
    private static final String SCHEME_HTTP = "http";

    public UrlUtils() {
    }

    public static Uri getWebViewContentUri(Context c) {
        return Uri.parse("http://localhost:" + Integer.toString(8635) + "/");
    }

    public static String getFileNameFromUriSegment(String segment) {
        int parameterIndex = getIndexOfParameters(segment);
        return parameterIndex == -1?segment:segment.substring(0, parameterIndex);
    }

    static int getIndexOfParameters(String segment) {
        int hashIndex = segment.indexOf(35);
        int qIndex = segment.indexOf(63);
        byte notPresentFlag = -1;
        if(hashIndex == -1 && qIndex == -1) {
            return notPresentFlag;
        } else if(hashIndex == -1 && qIndex != -1) {
            return qIndex;
        } else if(hashIndex != -1 && qIndex == -1) {
            return hashIndex;
        } else {
            int firstSpecialIndex = Math.min(hashIndex, qIndex);
            return firstSpecialIndex;
        }
    }

    public static String getParametersFromSegment(String segment) {
        int parameterIndex = getIndexOfParameters(segment);
        String notPresentFlag = "";
        return parameterIndex == -1?notPresentFlag:segment.substring(parameterIndex);
    }

    public static String encodeSegment(String segment) {
        String encodedSegment = Uri.encode(segment, (String)null);
        return encodedSegment;
    }

    public static String getAsWebViewUri(Context context, String appName, String uriFragment) {
        Uri u = getWebViewContentUri(context);
        u = Uri.withAppendedPath(u, encodeSegment(appName));
        int idxQ = uriFragment.indexOf("?");
        int idxH = uriFragment.indexOf("#");
        String pathPart;
        String queryPart;
        String hashPart;
        if(idxQ != -1) {
            if(idxH != -1) {
                if(idxH < idxQ) {
                    pathPart = uriFragment.substring(0, idxH);
                    queryPart = "";
                    hashPart = uriFragment.substring(idxH);
                } else {
                    pathPart = uriFragment.substring(0, idxQ);
                    queryPart = uriFragment.substring(idxQ, idxH);
                    hashPart = uriFragment.substring(idxH);
                }
            } else {
                pathPart = uriFragment.substring(0, idxQ);
                queryPart = uriFragment.substring(idxQ);
                hashPart = "";
            }
        } else if(idxH != -1) {
            pathPart = uriFragment.substring(0, idxH);
            queryPart = "";
            hashPart = uriFragment.substring(idxH);
        } else {
            pathPart = uriFragment;
            queryPart = "";
            hashPart = "";
        }

        String[] segments = pathPart.split("/");
        String[] arr$ = segments;
        int len$ = segments.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            u = Uri.withAppendedPath(u, encodeSegment(s));
        }

        return u.toString() + queryPart + hashPart;
    }

    public static boolean isValidUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException var2) {
            return false;
        }
    }
}
