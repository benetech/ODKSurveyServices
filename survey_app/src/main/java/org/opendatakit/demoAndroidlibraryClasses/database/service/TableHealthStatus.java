package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum TableHealthStatus implements Parcelable {
    TABLE_HEALTH_IS_CLEAN,
    TABLE_HEALTH_HAS_CONFLICTS,
    TABLE_HEALTH_HAS_CHECKPOINTS,
    TABLE_HEALTH_HAS_CHECKPOINTS_AND_CONFLICTS;

    public static final Creator<org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus> CREATOR;

    private TableHealthStatus() {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name());
    }

    static {
        CREATOR = new Creator() {
            public org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus createFromParcel(Parcel in) {
                return org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus.valueOf(in.readString());
            }

            public org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus[] newArray(int size) {
                return new org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus[size];
            }
        };
    }
}
