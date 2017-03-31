package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DbHandle implements Parcelable {
    private final String databaseHandle;
    public static final Creator<DbHandle> CREATOR = new Creator() {
        public DbHandle createFromParcel(Parcel in) {
            return new DbHandle(in);
        }

        public DbHandle[] newArray(int size) {
            return new DbHandle[size];
        }
    };

    public DbHandle(String databaseHandle) {
        if(databaseHandle == null) {
            throw new IllegalArgumentException("null databaseHandle");
        } else {
            this.databaseHandle = databaseHandle;
        }
    }

    public DbHandle(Parcel in) {
        this.databaseHandle = in.readString();
        if(this.databaseHandle == null) {
            throw new IllegalArgumentException("null databaseHandle");
        }
    }

    public String getDatabaseHandle() {
        return this.databaseHandle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getDatabaseHandle());
    }
}
