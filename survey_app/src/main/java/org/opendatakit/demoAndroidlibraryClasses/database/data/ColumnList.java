package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.opendatakit.aggregate.odktables.rest.entity.Column;

public class ColumnList implements Parcelable {
    private List<Column> columns;
    public static final Creator<ColumnList> CREATOR = new Creator() {
        public ColumnList createFromParcel(Parcel in) {
            return new ColumnList(in);
        }

        public ColumnList[] newArray(int size) {
            return new ColumnList[size];
        }
    };

    public ColumnList(List<Column> columns) {
        if(columns == null) {
            throw new IllegalArgumentException("list of Columns cannot be null!");
        } else {
            this.columns = columns;
        }
    }

    public ColumnList(Parcel in) {
        this.readFromParcel(in);
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.columns.size());
        Iterator i$ = this.columns.iterator();

        while(i$.hasNext()) {
            Column column = (Column)i$.next();
            out.writeString(column.getElementKey());
            out.writeString(column.getElementName());
            out.writeString(column.getElementType());
            out.writeString(column.getListChildElementKeys());
        }

    }

    private void readFromParcel(Parcel in) {
        int count = in.readInt();
        this.columns = new ArrayList(count);

        for(int i = 0; i < count; ++i) {
            String elementKey = in.readString();
            String elementName = in.readString();
            String elementType = in.readString();
            String listChildElementKeys = in.readString();
            Column col = new Column(elementKey, elementName, elementType, listChildElementKeys);
            this.columns.add(col);
        }

    }
}
