package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ParentTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.MarshallUtil;

public class BaseTable implements Parcelable {
    static final String TAG = BaseTable.class.getSimpleName();
    protected final ArrayList<Row> mRows;
    protected ParentTable mParent;
    protected String mMetaDataRev;
    protected boolean mEffectiveAccessCreateRow;
    protected ResumableQuery mQuery;
    protected final String[] mPrimaryKey;
    protected final String[] mElementKeyForIndex;
    protected final Map<String, Integer> mElementKeyToIndex;
    public static final Creator<BaseTable> CREATOR = new Creator() {
        public BaseTable createFromParcel(Parcel in) {
            return new BaseTable(in);
        }

        public BaseTable[] newArray(int size) {
            return new BaseTable[size];
        }
    };

    public BaseTable(ResumableQuery query, String[] elementKeyForIndex, Map<String, Integer> elementKeyToIndex, String[] primaryKey, Integer rowCount) {
        this.mParent = null;
        this.mEffectiveAccessCreateRow = false;
        this.mQuery = query;
        if(elementKeyForIndex == null) {
            throw new IllegalStateException("elementKeyForIndex cannot be null");
        } else {
            this.mElementKeyForIndex = elementKeyForIndex;
            if(elementKeyToIndex == null) {
                this.mElementKeyToIndex = this.generateElementKeyToIndex();
            } else {
                this.mElementKeyToIndex = elementKeyToIndex;
            }

            this.mPrimaryKey = primaryKey;
            int numRows = 0;
            if(rowCount != null) {
                numRows = rowCount.intValue();
            }

            this.mRows = new ArrayList(numRows);
        }
    }

    public BaseTable(String[] primaryKey, String[] elementKeyForIndex, Map<String, Integer> elementKeyToIndex, Integer rowCount) {
        this((ResumableQuery)null, elementKeyForIndex, elementKeyToIndex, primaryKey, rowCount);
    }

    public BaseTable(BaseTable table, List<Integer> indexes) {
        this.mRows = new ArrayList(indexes.size());

        for(int i = 0; i < indexes.size(); ++i) {
            Row r = table.getRowAtIndex(((Integer)indexes.get(i)).intValue());
            this.mRows.add(r);
        }

        this.mEffectiveAccessCreateRow = table.mEffectiveAccessCreateRow;
        this.mQuery = table.mQuery;
        this.mPrimaryKey = table.mPrimaryKey;
        this.mElementKeyForIndex = table.mElementKeyForIndex;
        this.mElementKeyToIndex = table.mElementKeyToIndex;
        this.mParent = null;
        this.mMetaDataRev = null;
    }

    public BaseTable(Parcel in) {
        try {
            byte r = in.readByte();
            this.mEffectiveAccessCreateRow = r != 0;
            this.mPrimaryKey = MarshallUtil.unmarshallStringArray(in);
            this.mElementKeyForIndex = MarshallUtil.unmarshallStringArray(in);
            this.mElementKeyToIndex = this.generateElementKeyToIndex();
        } catch (Throwable var4) {
            Log.e(TAG, var4.getMessage());
            Log.e(TAG, Log.getStackTraceString(var4));
            throw var4;
        }

        int dataCount = in.readInt();

        for(this.mRows = new ArrayList(dataCount); dataCount > 0; --dataCount) {
            Row var5 = new Row(in, this);
            this.mRows.add(var5);
        }

        this.mParent = null;
        this.mQuery = null;
        this.mMetaDataRev = null;
    }

    public void setEffectiveAccessCreateRow(boolean canCreateRow) {
        this.mEffectiveAccessCreateRow = canCreateRow;
    }

    public boolean getEffectiveAccessCreateRow() {
        return this.mEffectiveAccessCreateRow;
    }

    public void setQuery(ResumableQuery query) {
        this.mQuery = query;
    }

    public ResumableQuery getQuery() {
        return this.mQuery;
    }

    public int getStartIndex() {
        if(this.mQuery != null && this.mQuery.getSqlQueryBounds() != null) {
            QueryBounds bounds = this.mQuery.getSqlQueryBounds();
            return bounds.mOffset <= 0?0:bounds.mOffset;
        } else {
            return 0;
        }
    }

    public int getEndIndex() {
        return this.getStartIndex() + this.getNumberOfRows() - 1;
    }

    public ResumableQuery resumeQueryForward(int limit) {
        if(this.mQuery == null) {
            return null;
        } else {
            int numCurrentRows = this.getNumberOfRows();
            if(numCurrentRows != 0 && numCurrentRows >= this.mQuery.getSqlLimit()) {
                this.mQuery.setSqlLimit(limit);
                this.mQuery.setSqlOffset(this.getEndIndex() + 1);
                return this.mQuery;
            } else {
                return null;
            }
        }
    }

    public ResumableQuery resumeQueryBackward(int limit) {
        if(this.mQuery == null) {
            return null;
        } else {
            int startIndex = this.getStartIndex();
            if(startIndex == 0) {
                return null;
            } else {
                if(limit > startIndex) {
                    limit = startIndex;
                    startIndex = 0;
                }

                this.mQuery.setSqlLimit(limit);
                this.mQuery.setSqlOffset(startIndex - limit);
                return this.mQuery;
            }
        }
    }

    public void addRow(Row row) {
        this.mRows.add(row);
    }

    public void setMetaDataRev(String metaDataRev) {
        this.mMetaDataRev = metaDataRev;
    }

    public String getMetaDataRev() {
        return this.mMetaDataRev;
    }

    public Row getRowAtIndex(int index) {
        return (Row)this.mRows.get(index);
    }

    public List<Row> getRows() {
        return this.mRows;
    }

    public String getElementKey(int colNum) {
        return this.mElementKeyForIndex[colNum];
    }

    public Integer getColumnIndexOfElementKey(String elementKey) {
        return (Integer)this.mElementKeyToIndex.get(elementKey);
    }

    public String getSqlCommand() {
        return this.mQuery != null?this.mQuery.getSqlCommand():null;
    }

    public BindArgs getSqlBindArgs() {
        return this.mQuery != null?this.mQuery.getSqlBindArgs():null;
    }

    public String[] getPrimaryKey() {
        return this.mPrimaryKey == null?null:this.mPrimaryKey;
    }

    public String[] getElementKeyForIndex() {
        return this.mElementKeyForIndex;
    }

    public Map<String, Integer> getElementKeyToIndex() {
        return new HashMap(this.mElementKeyToIndex);
    }

    private Map<String, Integer> generateElementKeyToIndex() {
        HashMap elementKeyToIndex = new HashMap();

        for(int i = 0; i < this.mElementKeyForIndex.length; ++i) {
            elementKeyToIndex.put(this.mElementKeyForIndex[i], Integer.valueOf(i));
        }

        return elementKeyToIndex;
    }

    public int getWidth() {
        return this.mElementKeyForIndex.length;
    }

    public int getNumberOfRows() {
        return this.mRows.size();
    }

    public void registerParentTable(ParentTable table) {
        this.mParent = table;
    }

    public void writeToParcel(Parcel out, int flags) {
        try {
            out.writeByte((byte)(this.mEffectiveAccessCreateRow?1:0));
            MarshallUtil.marshallStringArray(out, this.mPrimaryKey);
            MarshallUtil.marshallStringArray(out, this.mElementKeyForIndex);
        } catch (Throwable var5) {
            Log.e(TAG, var5.getMessage());
            Log.e(TAG, Log.getStackTraceString(var5));
            throw var5;
        }

        out.writeInt(this.mRows.size());
        Iterator i$ = this.mRows.iterator();

        while(i$.hasNext()) {
            Row r = (Row)i$.next();
            r.writeToParcel(out, flags);
        }

    }

    public int describeContents() {
        return 0;
    }
}
