package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import java.io.Serializable;

public class KeyValueStoreEntry implements Parcelable, Comparable<KeyValueStoreEntry>, Serializable {
    public String tableId;
    public String partition;
    public String aspect;
    public String key;
    public String type;
    public String value;
    public static final Creator<KeyValueStoreEntry> CREATOR = new Creator() {
        public KeyValueStoreEntry createFromParcel(Parcel in) {
            return new KeyValueStoreEntry(in);
        }

        public KeyValueStoreEntry[] newArray(int size) {
            return new KeyValueStoreEntry[size];
        }
    };

    public KeyValueStoreEntry() {
    }

    public KeyValueStoreEntry(Parcel in) {
        this.readFromParcel(in);
    }

    public String toString() {
        return "[tableId=" + this.tableId + ", partition=" + this.partition + ", aspect=" + this.aspect + ", key=" + this.key + ", type=" + this.type + ", value=" + this.value + "]";
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.tableId == null?0:this.tableId.hashCode());
        result1 = 31 * result1 + (this.partition == null?0:this.partition.hashCode());
        result1 = 31 * result1 + (this.aspect == null?0:this.aspect.hashCode());
        result1 = 31 * result1 + (this.key == null?0:this.key.hashCode());
        result1 = 31 * result1 + (this.type == null?0:this.type.hashCode());
        result1 = 31 * result1 + (this.value == null?0:this.value.hashCode());
        return result1;
    }

    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(obj == this) {
            return true;
        } else if(!(obj instanceof KeyValueStoreEntry)) {
            return false;
        } else {
            boolean var10000;
            label80: {
                KeyValueStoreEntry other = (KeyValueStoreEntry)obj;
                if(this.tableId == null) {
                    if(other.tableId != null) {
                        break label80;
                    }
                } else if(!this.tableId.equals(other.tableId)) {
                    break label80;
                }

                if(this.partition == null) {
                    if(other.partition != null) {
                        break label80;
                    }
                } else if(!this.partition.equals(other.partition)) {
                    break label80;
                }

                if(this.aspect == null) {
                    if(other.aspect != null) {
                        break label80;
                    }
                } else if(!this.aspect.equals(other.aspect)) {
                    break label80;
                }

                if(this.key == null) {
                    if(other.key != null) {
                        break label80;
                    }
                } else if(!this.key.equals(other.key)) {
                    break label80;
                }

                if(this.type == null) {
                    if(other.type != null) {
                        break label80;
                    }
                } else if(!this.type.equals(other.type)) {
                    break label80;
                }

                if(this.value == null) {
                    if(other.value != null) {
                        break label80;
                    }
                } else if(!this.value.equals(other.value)) {
                    break label80;
                }

                var10000 = true;
                return var10000;
            }

            var10000 = false;
            return var10000;
        }
    }

    public int compareTo(@NonNull KeyValueStoreEntry that) {
        int partitionComparison = this.partition.compareTo(that.partition);
        if(partitionComparison != 0) {
            return partitionComparison;
        } else {
            int aspectComparison = this.aspect.compareTo(that.aspect);
            if(aspectComparison != 0) {
                return aspectComparison;
            } else {
                int keyComparison = this.key.compareTo(that.key);
                return keyComparison;
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.tableId);
        out.writeString(this.partition);
        out.writeString(this.aspect);
        out.writeString(this.key);
        out.writeString(this.type);
        out.writeString(this.value);
    }

    private void readFromParcel(Parcel in) {
        this.tableId = in.readString();
        this.partition = in.readString();
        this.aspect = in.readString();
        this.key = in.readString();
        this.type = in.readString();
        this.value = in.readString();
    }
}
