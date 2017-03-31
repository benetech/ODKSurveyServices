package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SyncProgressEvent implements Parcelable {
    public final int messageNum;
    public final String progressMessageText;
    public final SyncProgressState progressState;
    public final int curProgressBar;
    public final int maxProgressBar;
    public static final Creator<SyncProgressEvent> CREATOR = new Creator() {
        public SyncProgressEvent createFromParcel(Parcel in) {
            return new SyncProgressEvent(in);
        }

        public SyncProgressEvent[] newArray(int size) {
            return new SyncProgressEvent[size];
        }
    };

    public SyncProgressEvent(int messageNum, String progressMessageText, SyncProgressState progressState, int curProgressBar, int maxProgressBar) {
        this.messageNum = messageNum;
        this.progressMessageText = progressMessageText;
        this.progressState = progressState;
        this.curProgressBar = curProgressBar;
        this.maxProgressBar = maxProgressBar;
    }

    protected SyncProgressEvent(Parcel in) {
        this.messageNum = in.readInt();
        this.progressMessageText = in.readString();
        this.progressState = (SyncProgressState)in.readParcelable(SyncProgressState.class.getClassLoader());
        this.curProgressBar = in.readInt();
        this.maxProgressBar = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.messageNum);
        dest.writeString(this.progressMessageText);
        dest.writeParcelable(this.progressState, flags);
        dest.writeInt(this.curProgressBar);
        dest.writeInt(this.maxProgressBar);
    }

    public int describeContents() {
        return 0;
    }
}
