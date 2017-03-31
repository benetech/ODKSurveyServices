package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum SyncOutcome implements Parcelable {
    WORKING,
    SUCCESS,
    FAILURE,
    ACCESS_DENIED_EXCEPTION,
    ACCESS_DENIED_REAUTH_EXCEPTION,
    BAD_CLIENT_CONFIG_EXCEPTION,
    NETWORK_TRANSMISSION_EXCEPTION,
    NOT_OPEN_DATA_KIT_SERVER_EXCEPTION,
    UNEXPECTED_REDIRECT_EXCEPTION,
    INCOMPATIBLE_SERVER_VERSION_EXCEPTION,
    INTERNAL_SERVER_FAILURE_EXCEPTION,
    LOCAL_DATABASE_EXCEPTION,
    APPNAME_DOES_NOT_EXIST_ON_SERVER,
    CLIENT_VERSION_FILES_DO_NOT_EXIST_ON_SERVER,
    NO_LOCAL_TABLES_TO_RESET_ON_SERVER,
    NO_APP_LEVEL_FILES_ON_SERVER_TO_SYNC,
    NO_TABLES_ON_SERVER_TO_SYNC,
    INCOMPLETE_SERVER_CONFIG_MISSING_FILE_BODY,
    TABLE_DOES_NOT_EXIST_ON_SERVER,
    TABLE_SCHEMA_COLUMN_DEFINITION_MISMATCH,
    TABLE_CONTAINS_CHECKPOINTS,
    TABLE_CONTAINS_CONFLICTS,
    TABLE_PENDING_ATTACHMENTS,
    TABLE_REQUIRES_APP_LEVEL_SYNC;

    public static final Creator<SyncOutcome> CREATOR;

    private SyncOutcome() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }

    static {
        CREATOR = new Creator() {
            public SyncOutcome createFromParcel(Parcel in) {
                return SyncOutcome.valueOf(in.readString());
            }

            public SyncOutcome[] newArray(int size) {
                return new SyncOutcome[size];
            }
        };
    }
}
