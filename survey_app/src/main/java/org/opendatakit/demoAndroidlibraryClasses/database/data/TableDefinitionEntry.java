package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TableDefinitionEntry implements Parcelable {
    public String tableId;
    public String revId;
    public String schemaETag;
    public String lastDataETag;
    public String lastSyncTime;
    public static final Creator<TableDefinitionEntry> CREATOR = new Creator() {
        public TableDefinitionEntry createFromParcel(Parcel in) {
            return new TableDefinitionEntry(in);
        }

        public TableDefinitionEntry[] newArray(int size) {
            return new TableDefinitionEntry[size];
        }
    };

    public TableDefinitionEntry(String tableId) {
        this.tableId = tableId;
    }

    public TableDefinitionEntry(Parcel in) {
        this.readFromParcel(in);
    }

    public String getTableId() {
        return this.tableId;
    }

    public String getRevId() {
        return this.revId;
    }

    public void setRevId(String revId) {
        this.revId = revId;
    }

    public String getSchemaETag() {
        return this.schemaETag;
    }

    public void setSchemaETag(String schemaETag) {
        this.schemaETag = schemaETag;
    }

    public String getLastDataETag() {
        return this.lastDataETag;
    }

    public void setLastDataETag(String lastDataETag) {
        this.lastDataETag = lastDataETag;
    }

    public String getLastSyncTime() {
        return this.lastSyncTime;
    }

    public void setLastSyncTime(String lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.tableId);
        out.writeString(this.revId);
        out.writeString(this.schemaETag);
        out.writeString(this.lastDataETag);
        out.writeString(this.lastSyncTime);
    }

    private void readFromParcel(Parcel in) {
        this.tableId = in.readString();
        this.revId = in.readString();
        this.schemaETag = in.readString();
        this.lastDataETag = in.readString();
        this.lastSyncTime = in.readString();
    }
}
