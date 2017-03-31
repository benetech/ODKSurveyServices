package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import org.opendatakit.demoAndroidlibraryClasses.database.service.TableHealthStatus;

public class TableHealthInfo implements Parcelable, Serializable {
    private final String tableId;
    private final TableHealthStatus status;
    public static final Parcelable.Creator<TableHealthInfo> CREATOR = null;

    public String getTableId() {
        return this.tableId;
    }

    public TableHealthStatus getHealthStatus() {
        return this.status;
    }

    public TableHealthInfo(String tableId, TableHealthStatus status) {
        this.tableId = tableId;
        this.status = status;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.tableId);
        this.status.writeToParcel(out, flags);
    }

    private TableHealthInfo(Parcel in) {
        this.tableId = in.readString();
        this.status = (TableHealthStatus)TableHealthStatus.CREATOR.createFromParcel(in);
    }
}
