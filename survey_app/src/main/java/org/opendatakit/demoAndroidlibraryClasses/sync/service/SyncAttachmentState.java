package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum SyncAttachmentState implements Parcelable {
    SYNC,
    UPLOAD,
    DOWNLOAD,
    NONE;

    public static final Creator<org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState> CREATOR;

    private SyncAttachmentState() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }

    static {
        CREATOR = new Creator() {
            public org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState createFromParcel(Parcel in) {
                return org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState.valueOf(in.readString());
            }

            public org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState[] newArray(int size) {
                return new org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState[size];
            }
        };
    }
}
