package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class QueryBounds implements Parcelable, Serializable {
    public final int mLimit;
    public final int mOffset;
    public static final Creator<QueryBounds> CREATOR = new Creator() {
        public QueryBounds createFromParcel(Parcel in) {
            return new QueryBounds(in);
        }

        public QueryBounds[] newArray(int size) {
            return new QueryBounds[size];
        }
    };

    public QueryBounds() {
        this.mLimit = -1;
        this.mOffset = 0;
    }

    public QueryBounds(int limit, int offset) {
        this.mLimit = limit;
        this.mOffset = offset;
    }

    protected QueryBounds(Parcel in) {
        this.mLimit = in.readInt();
        this.mOffset = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mLimit);
        dest.writeInt(this.mOffset);
    }

    public int describeContents() {
        return 0;
    }
}
