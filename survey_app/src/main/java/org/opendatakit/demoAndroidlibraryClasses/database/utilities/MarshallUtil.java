package org.opendatakit.demoAndroidlibraryClasses.database.utilities;

import android.os.Parcel;

public class MarshallUtil {
    private static final String TAG = MarshallUtil.class.getSimpleName();

    private MarshallUtil() {
        throw new IllegalStateException("Never Instantiate this static class");
    }

    public static void marshallStringArray(Parcel out, String[] toMarshall) {
        if(toMarshall == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(toMarshall.length);
            out.writeStringArray(toMarshall);
        }

    }

    public static String[] unmarshallStringArray(Parcel in) {
        String[] result = null;
        int dataCount = in.readInt();
        if(dataCount < 0) {
            return null;
        } else {
            result = new String[dataCount];
            in.readStringArray(result);
            return result;
        }
    }
}