package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum SyncProgressState implements Parcelable {
    INACTIVE,
    STARTING,
    APP_FILES,
    TABLE_FILES,
    ROWS,
    FINISHED;

    public static final Creator<SyncProgressState> CREATOR;

    private SyncProgressState() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }

    static {
        CREATOR = new Creator() {
            public SyncProgressState createFromParcel(Parcel in) {
                return SyncProgressState.valueOf(in.readString());
            }

            public SyncProgressState[] newArray(int size) {
                return new SyncProgressState[size];
            }
        };
    }
}
