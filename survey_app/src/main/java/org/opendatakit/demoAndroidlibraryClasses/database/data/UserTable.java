package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.ElementType;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ParentTable;
import org.opendatakit.demoAndroidlibraryClasses.database.data.Row;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.BindArgs;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.QueryBounds;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.ResumableQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.queries.SimpleQuery;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.MarshallUtil;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class UserTable implements Parcelable, ParentTable {
    static final String TAG = UserTable.class.getSimpleName();
    private static final String[] primaryKey = new String[]{"rowId", "savepoint_timestamp"};
    private final BaseTable mBaseTable;
    private final OrderedColumns mColumnDefns;
    private final String[] mAdminColumnOrder;
    public static final Creator<UserTable> CREATOR = new Creator() {
        public UserTable createFromParcel(Parcel in) {
            return new UserTable(in);
        }

        public UserTable[] newArray(int size) {
            return new UserTable[size];
        }
    };

    public UserTable(UserTable table, List<Integer> indexes) {
        this.mBaseTable = new BaseTable(table.mBaseTable, indexes);
        this.mColumnDefns = table.mColumnDefns;
        this.mAdminColumnOrder = table.mAdminColumnOrder;
    }

    public UserTable(BaseTable baseTable, OrderedColumns columnDefns, String[] adminColumnOrder) {
        this.mBaseTable = baseTable;
        baseTable.registerParentTable(this);
        this.mColumnDefns = columnDefns;
        this.mAdminColumnOrder = adminColumnOrder;
    }

    public UserTable(OrderedColumns columnDefns, String sqlWhereClause, Object[] sqlBindArgs, String[] sqlGroupByArgs, String sqlHavingClause, String[] sqlOrderByElementKeys, String[] sqlOrderByDirections, String[] adminColumnOrder, Map<String, Integer> elementKeyToIndex, String[] elementKeyForIndex, Integer rowCount) {
        SimpleQuery query = new SimpleQuery(columnDefns.getTableId(), new BindArgs(sqlBindArgs), sqlWhereClause, sqlGroupByArgs, sqlHavingClause, sqlOrderByElementKeys, sqlOrderByDirections, (QueryBounds)null);
        this.mBaseTable = new BaseTable(query, elementKeyForIndex, elementKeyToIndex, primaryKey, rowCount);
        this.mColumnDefns = columnDefns;
        this.mAdminColumnOrder = adminColumnOrder;
    }

    public BaseTable getBaseTable() {
        return this.mBaseTable;
    }

    public void addRow(Row row) {
        this.mBaseTable.addRow(row);
    }

    public String getMetaDataRev() {
        return this.mBaseTable.getMetaDataRev();
    }

    public Row getRowAtIndex(int index) {
        return this.mBaseTable.getRowAtIndex(index);
    }

    public String getElementKey(int colNum) {
        return this.mBaseTable.getElementKey(colNum);
    }

    public Integer getColumnIndexOfElementKey(String elementKey) {
        return this.mBaseTable.getColumnIndexOfElementKey(elementKey);
    }

    public BindArgs getSelectionArgs() {
        return this.mBaseTable.getSqlBindArgs();
    }

    public int getWidth() {
        return this.mBaseTable.getWidth();
    }

    public int getNumberOfRows() {
        return this.mBaseTable.getNumberOfRows();
    }

    public Map<String, Integer> getElementKeyToIndex() {
        return this.mBaseTable.getElementKeyToIndex();
    }

    public boolean getEffectiveAccessCreateRow() {
        return this.mBaseTable.getEffectiveAccessCreateRow();
    }

    public ResumableQuery getQuery() {
        return this.mBaseTable.getQuery();
    }

    public ResumableQuery resumeQueryForward(int limit) {
        return this.mBaseTable.resumeQueryForward(limit);
    }

    public ResumableQuery resumeQueryBackward(int limit) {
        return this.mBaseTable.resumeQueryBackward(limit);
    }

    public String getAppName() {
        return this.mColumnDefns.getAppName();
    }

    public String getTableId() {
        return this.mColumnDefns.getTableId();
    }

    public OrderedColumns getColumnDefinitions() {
        return this.mColumnDefns;
    }

    public String getRowId(int rowIndex) {
        return this.getRowAtIndex(rowIndex).getDataByKey("_id");
    }

    public String getDisplayTextOfData(int rowIndex, ElementType type, String elementKey) {
        Row row = this.getRowAtIndex(rowIndex);
        String raw = row.getDataByKey(elementKey);
        String rowId = row.getDataByKey("_id");
        if(raw == null) {
            return null;
        } else if(raw.length() == 0) {
            throw new IllegalArgumentException("unexpected zero-length string in database! " + elementKey);
        } else if(type == null) {
            return raw;
        } else if(type.getDataType() == ElementDataType.number && raw.indexOf(46) != -1) {
            int var8;
            for(var8 = raw.length() - 1; var8 > 0 && raw.charAt(var8) == 48; --var8) {
                ;
            }

            return var8 >= raw.length() - 2?raw:raw.substring(0, var8 + 2);
        } else if(type.getDataType() == ElementDataType.rowpath) {
            File theFile = ODKFileUtils.getRowpathFile(this.getAppName(), this.getTableId(), rowId, raw);
            return theFile.getName();
        } else {
            return type.getDataType() == ElementDataType.configpath?raw:raw;
        }
    }

    public boolean hasCheckpointRows() {
        List rows = this.mBaseTable.getRows();
        Iterator i$ = rows.iterator();

        String type;
        do {
            if(!i$.hasNext()) {
                return false;
            }

            Row row = (Row)i$.next();
            type = row.getDataByKey("_savepoint_type");
        } while(type != null && type.length() != 0);

        return true;
    }

    public boolean hasConflictRows() {
        List rows = this.mBaseTable.getRows();
        Iterator i$ = rows.iterator();

        String conflictType;
        do {
            if(!i$.hasNext()) {
                return false;
            }

            Row row = (Row)i$.next();
            conflictType = row.getDataByKey("_conflict_type");
        } while(conflictType == null || conflictType.length() == 0);

        return true;
    }

    public int getRowNumFromId(String rowId) {
        for(int i = 0; i < this.mBaseTable.getNumberOfRows(); ++i) {
            if(this.getRowId(i).equals(rowId)) {
                return i;
            }
        }

        return -1;
    }

    public ParentTable getParentTable() {
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        this.mColumnDefns.writeToParcel(out, flags);
        MarshallUtil.marshallStringArray(out, this.mAdminColumnOrder);
        this.mBaseTable.writeToParcel(out, flags);
    }

    public UserTable(Parcel in) {
        this.mColumnDefns = new OrderedColumns(in);
        this.mAdminColumnOrder = MarshallUtil.unmarshallStringArray(in);
        this.mBaseTable = new BaseTable(in);
        this.mBaseTable.registerParentTable(this);
    }
}
