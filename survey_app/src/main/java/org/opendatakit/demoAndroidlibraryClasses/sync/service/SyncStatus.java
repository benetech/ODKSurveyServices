package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum SyncStatus implements Parcelable {
    NONE,
    SYNCING,
    NETWORK_TRANSPORT_ERROR,
    AUTHENTICATION_ERROR,
    SERVER_INTERNAL_ERROR,
    REQUEST_OR_PROTOCOL_ERROR,
    SERVER_IS_NOT_ODK_SERVER,
    DEVICE_ERROR,
    APPNAME_NOT_SUPPORTED_BY_SERVER,
    SERVER_MISSING_CONFIG_FILES,
    SERVER_RESET_FAILED_DEVICE_HAS_NO_CONFIG_FILES,
    RESYNC_BECAUSE_CONFIG_HAS_BEEN_RESET_ERROR,
    CONFLICT_RESOLUTION,
    SYNC_COMPLETE,
    SYNC_COMPLETE_PENDING_ATTACHMENTS;

    public static final Creator<SyncStatus> CREATOR;

    private SyncStatus() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }

    static {
        CREATOR = new Creator() {
            public SyncStatus createFromParcel(Parcel in) {
                return SyncStatus.valueOf(in.readString());
            }

            public SyncStatus[] newArray(int size) {
                return new SyncStatus[size];
            }
        };
    }
}
