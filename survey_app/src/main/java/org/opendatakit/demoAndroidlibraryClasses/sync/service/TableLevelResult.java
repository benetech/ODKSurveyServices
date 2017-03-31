package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncOutcome;

public class TableLevelResult implements Parcelable {
    private static final String TAG = "TableLevelResult";
    private final String mTableId;
    private String mDisplayName;
    private SyncOutcome mSyncOutcome;
    private String mMessage;
    private boolean mPulledServerSchema;
    private boolean mPulledServerProps;
    private boolean mPulledServerData;
    private boolean mPushedLocalProps;
    private boolean mPushedLocalData;
    private boolean mHadLocalPropChanges;
    private boolean mHadLocalDataChanges;
    private boolean mHadServerSchemaChanges;
    private boolean mHadServerPropChanges;
    private boolean mHadServerDataChanges;
    private int mServerNumUpserts;
    private int mServerNumDeletes;
    private int mLocalNumInserts;
    private int mLocalNumUpdates;
    private int mLocalNumDeletes;
    private int mLocalNumConflicts;
    private int mLocalNumAttachmentRetries;
    public static final Creator<TableLevelResult> CREATOR = new Creator() {
        public TableLevelResult createFromParcel(Parcel in) {
            return new TableLevelResult(in);
        }

        public TableLevelResult[] newArray(int size) {
            return new TableLevelResult[size];
        }
    };

    protected TableLevelResult(Parcel in) {
        this.mSyncOutcome = SyncOutcome.WORKING;
        this.mMessage = SyncOutcome.WORKING.name();
        this.mServerNumUpserts = 0;
        this.mServerNumDeletes = 0;
        this.mLocalNumInserts = 0;
        this.mLocalNumUpdates = 0;
        this.mLocalNumDeletes = 0;
        this.mLocalNumConflicts = 0;
        this.mLocalNumAttachmentRetries = 0;
        this.mTableId = in.readString();
        this.mDisplayName = in.readString();
        this.mMessage = in.readString();
        this.mPulledServerSchema = in.readByte() != 0;
        this.mPulledServerProps = in.readByte() != 0;
        this.mPulledServerData = in.readByte() != 0;
        this.mPushedLocalProps = in.readByte() != 0;
        this.mPushedLocalData = in.readByte() != 0;
        this.mHadLocalPropChanges = in.readByte() != 0;
        this.mHadLocalDataChanges = in.readByte() != 0;
        this.mHadServerSchemaChanges = in.readByte() != 0;
        this.mHadServerPropChanges = in.readByte() != 0;
        this.mHadServerDataChanges = in.readByte() != 0;
        this.mServerNumUpserts = in.readInt();
        this.mServerNumDeletes = in.readInt();
        this.mLocalNumInserts = in.readInt();
        this.mLocalNumUpdates = in.readInt();
        this.mLocalNumDeletes = in.readInt();
        this.mLocalNumConflicts = in.readInt();
        this.mLocalNumAttachmentRetries = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTableId);
        dest.writeString(this.mDisplayName);
        dest.writeString(this.mMessage);
        dest.writeByte((byte)(this.mPulledServerSchema?1:0));
        dest.writeByte((byte)(this.mPulledServerProps?1:0));
        dest.writeByte((byte)(this.mPulledServerData?1:0));
        dest.writeByte((byte)(this.mPushedLocalProps?1:0));
        dest.writeByte((byte)(this.mPushedLocalData?1:0));
        dest.writeByte((byte)(this.mHadLocalPropChanges?1:0));
        dest.writeByte((byte)(this.mHadLocalDataChanges?1:0));
        dest.writeByte((byte)(this.mHadServerSchemaChanges?1:0));
        dest.writeByte((byte)(this.mHadServerPropChanges?1:0));
        dest.writeByte((byte)(this.mHadServerDataChanges?1:0));
        dest.writeInt(this.mServerNumUpserts);
        dest.writeInt(this.mServerNumDeletes);
        dest.writeInt(this.mLocalNumInserts);
        dest.writeInt(this.mLocalNumUpdates);
        dest.writeInt(this.mLocalNumDeletes);
        dest.writeInt(this.mLocalNumConflicts);
        dest.writeInt(this.mLocalNumAttachmentRetries);
    }

    public int describeContents() {
        return 0;
    }

    public void incServerUpserts() {
        ++this.mServerNumUpserts;
    }

    public void incServerDeletes() {
        ++this.mServerNumDeletes;
    }

    public void incLocalInserts() {
        ++this.mLocalNumInserts;
    }

    public void incLocalUpdates() {
        ++this.mLocalNumUpdates;
    }

    public void incLocalDeletes() {
        ++this.mLocalNumDeletes;
    }

    public void incLocalConflicts() {
        ++this.mLocalNumConflicts;
    }

    public void incLocalAttachmentRetries() {
        ++this.mLocalNumAttachmentRetries;
    }

    public TableLevelResult(String tableId) {
        this.mSyncOutcome = SyncOutcome.WORKING;
        this.mMessage = SyncOutcome.WORKING.name();
        this.mServerNumUpserts = 0;
        this.mServerNumDeletes = 0;
        this.mLocalNumInserts = 0;
        this.mLocalNumUpdates = 0;
        this.mLocalNumDeletes = 0;
        this.mLocalNumConflicts = 0;
        this.mLocalNumAttachmentRetries = 0;
        this.mTableId = tableId;
        this.mDisplayName = tableId;
        this.mPulledServerData = false;
        this.mPulledServerProps = false;
        this.mPulledServerSchema = false;
        this.mPushedLocalData = false;
        this.mPushedLocalData = false;
        this.mHadLocalDataChanges = false;
        this.mHadLocalPropChanges = false;
        this.mHadServerDataChanges = false;
        this.mHadServerPropChanges = false;
        this.mHadServerSchemaChanges = false;
    }

    public String getTableId() {
        return this.mTableId;
    }

    public void setTableDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getTableDisplayName() {
        return this.mDisplayName;
    }

    public SyncOutcome getSyncOutcome() {
        return this.mSyncOutcome;
    }

    public boolean pulledServerData() {
        return this.mPulledServerData;
    }

    public boolean pulledServerProperties() {
        return this.mPulledServerProps;
    }

    public boolean pulledServerSchema() {
        return this.mPulledServerSchema;
    }

    public boolean pushedLocalProperties() {
        return this.mPushedLocalProps;
    }

    public boolean pushedLocalData() {
        return this.mPushedLocalData;
    }

    public boolean hadLocalDataChanges() {
        return this.mHadLocalDataChanges;
    }

    public boolean hadLocalPropertiesChanges() {
        return this.mHadLocalPropChanges;
    }

    public boolean serverHadDataChanges() {
        return this.mHadServerDataChanges;
    }

    public boolean serverHadPropertiesChanges() {
        return this.mHadServerPropChanges;
    }

    public boolean serverHadSchemaChanges() {
        return this.mHadServerSchemaChanges;
    }

    public void setPulledServerData(boolean pulledData) {
        this.mPulledServerData = pulledData;
    }

    public void setPulledServerProperties(boolean pulledProperties) {
        this.mPulledServerProps = pulledProperties;
    }

    public void setPulledServerSchema(boolean pulledSchema) {
        this.mPulledServerSchema = pulledSchema;
    }

    public void setPushedLocalProperties(boolean pushedProperties) {
        this.mPushedLocalProps = pushedProperties;
    }

    public void setPushedLocalData(boolean pushedData) {
        this.mPushedLocalData = pushedData;
    }

    public void setHadLocalPropertiesChanges(boolean hadChanges) {
        this.mHadLocalPropChanges = hadChanges;
    }

    public void setHadLocalDataChanges(boolean hadChanges) {
        this.mHadLocalDataChanges = hadChanges;
    }

    public void setServerHadSchemaChanges(boolean serverHadChanges) {
        this.mHadServerSchemaChanges = serverHadChanges;
    }

    public void setServerHadPropertiesChanges(boolean serverHadChanges) {
        this.mHadServerPropChanges = serverHadChanges;
    }

    public void setServerHadDataChanges(boolean serverHadChanges) {
        this.mHadServerDataChanges = serverHadChanges;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public void setSyncOutcome(SyncOutcome newSyncOutcome) {
        if(this.mSyncOutcome != SyncOutcome.WORKING) {
            throw new UnsupportedOperationException("Tried to set TableLevelResult status to " + newSyncOutcome.name() + " when it had already been set to " + this.mSyncOutcome.name());
        } else {
            this.mSyncOutcome = newSyncOutcome;
        }
    }

    public void resetSyncOutcome() {
        this.mSyncOutcome = SyncOutcome.WORKING;
    }
}
