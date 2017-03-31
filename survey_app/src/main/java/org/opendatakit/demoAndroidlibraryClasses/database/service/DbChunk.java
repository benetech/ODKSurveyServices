package org.opendatakit.demoAndroidlibraryClasses.database.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class DbChunk implements Parcelable {
    private byte[] data = null;
    private UUID thisID = null;
    private UUID nextID = null;
    public static final Creator<DbChunk> CREATOR = new Creator() {
        public DbChunk createFromParcel(Parcel in) {
            return new DbChunk(in);
        }

        public DbChunk[] newArray(int size) {
            return new DbChunk[size];
        }
    };

    public DbChunk(byte[] data, UUID thisID) {
        if(data == null) {
            throw new IllegalArgumentException("null data");
        } else {
            this.data = data;
            this.thisID = thisID;
        }
    }

    public DbChunk(Parcel in) {
        int dataLength = in.readInt();
        if(dataLength < 0) {
            throw new IllegalArgumentException("invalid data length");
        } else {
            this.data = new byte[dataLength];
            in.readByteArray(this.data);
            this.thisID = (UUID)in.readSerializable();
            byte hasNext = in.readByte();
            if(hasNext > 0) {
                this.nextID = (UUID)in.readSerializable();
            }

        }
    }

    public byte[] getData() {
        return this.data;
    }

    public UUID getThisID() {
        return this.thisID;
    }

    public UUID getNextID() {
        return this.nextID;
    }

    public boolean hasNextID() {
        return this.nextID != null;
    }

    public void setNextID(UUID nextID) {
        this.nextID = nextID;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.data.length);
        dest.writeByteArray(this.data);
        dest.writeSerializable(this.thisID);
        if(this.nextID == null) {
            dest.writeByte((byte)0);
        } else {
            dest.writeByte((byte)1);
            dest.writeSerializable(this.nextID);
        }

    }
}