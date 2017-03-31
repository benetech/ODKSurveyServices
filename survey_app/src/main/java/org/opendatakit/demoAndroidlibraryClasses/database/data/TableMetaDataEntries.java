package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;

public class TableMetaDataEntries implements Parcelable {
    private String tableId;
    private String revId;
    private ArrayList<KeyValueStoreEntry> entries;
    public static final Creator<TableMetaDataEntries> CREATOR = new Creator() {
        public TableMetaDataEntries createFromParcel(Parcel in) {
            return new TableMetaDataEntries(in);
        }

        public TableMetaDataEntries[] newArray(int size) {
            return new TableMetaDataEntries[size];
        }
    };

    public TableMetaDataEntries(String tableId, String revId) {
        this.tableId = tableId;
        this.revId = revId;
        this.entries = new ArrayList();
    }

    public TableMetaDataEntries(TableMetaDataEntries other) {
        this.tableId = other.tableId;
        this.revId = other.revId;
        this.entries = new ArrayList(other.entries);
    }

    public TableMetaDataEntries(Parcel in) {
        this.readFromParcel(in);
    }

    public String getTableId() {
        return this.tableId;
    }

    public String getRevId() {
        return this.revId;
    }

    public ArrayList<KeyValueStoreEntry> getEntries() {
        return this.entries;
    }

    public void addEntry(KeyValueStoreEntry e) {
        this.entries.add(e);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeByte((byte)(this.tableId != null?1:0));
        out.writeString(this.tableId);
        out.writeByte((byte)(this.revId != null?1:0));
        out.writeString(this.revId);
        out.writeByte((byte)(this.entries != null?1:0));
        out.writeSerializable(this.entries);
    }

    private void readFromParcel(Parcel in) {
        boolean notNull = in.readByte() == 1;
        this.tableId = notNull?in.readString():null;
        notNull = in.readByte() == 1;
        this.revId = notNull?in.readString():null;
        notNull = in.readByte() == 1;
        this.entries = notNull?(ArrayList)in.readSerializable():null;
    }
}
