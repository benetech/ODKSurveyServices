package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.aggregate.odktables.rest.ElementType;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnList;

public class OrderedColumns implements Parcelable {
    private final String appName;
    private final String tableId;
    private final ArrayList<ColumnDefinition> orderedDefns;
    public static final Creator<OrderedColumns> CREATOR = new Creator() {
        public OrderedColumns createFromParcel(Parcel in) {
            return new OrderedColumns(in);
        }

        public OrderedColumns[] newArray(int size) {
            return new OrderedColumns[size];
        }
    };

    public OrderedColumns(String appName, String tableId, List<Column> columns) {
        this.appName = appName;
        this.tableId = tableId;
        this.orderedDefns = ColumnDefinition.buildColumnDefinitions(appName, tableId, columns);
    }

    public ColumnDefinition find(String elementKey) {
        return ColumnDefinition.find(this.orderedDefns, elementKey);
    }

    public ArrayList<String> getRetentionColumnNames() {
        ArrayList writtenColumns = new ArrayList();
        Iterator i$ = this.orderedDefns.iterator();

        while(i$.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition)i$.next();
            if(cd.isUnitOfRetention()) {
                writtenColumns.add(cd.getElementKey());
            }
        }

        return writtenColumns;
    }

    public boolean graphViewIsPossible() {
        Iterator i$ = this.orderedDefns.iterator();

        ElementDataType type;
        do {
            ColumnDefinition cd;
            do {
                if(!i$.hasNext()) {
                    return false;
                }

                cd = (ColumnDefinition)i$.next();
            } while(!cd.isUnitOfRetention());

            ElementType elementType = cd.getType();
            type = elementType.getDataType();
        } while(type != ElementDataType.number && type != ElementDataType.integer);

        return this.orderedDefns.size() > 1;
    }

    public ArrayList<ColumnDefinition> getGeopointColumnDefinitions() {
        ArrayList cdList = new ArrayList();
        Iterator i$ = this.orderedDefns.iterator();

        while(i$.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition)i$.next();
            if(cd.getType().getElementType().equals("geopoint")) {
                cdList.add(cd);
            }
        }

        return cdList;
    }

    public boolean isLatitudeColumnDefinition(List<ColumnDefinition> geoPointList, ColumnDefinition cd) {
        if(!cd.isUnitOfRetention()) {
            return false;
        } else {
            ElementDataType type = cd.getType().getDataType();
            if(type != ElementDataType.number && type != ElementDataType.integer) {
                return false;
            } else {
                ColumnDefinition cdParent = cd.getParent();
                boolean outcome = cdParent != null && geoPointList.contains(cdParent) && cd.getElementName().equals("latitude");
                return outcome;
            }
        }
    }

    public boolean isLongitudeColumnDefinition(List<ColumnDefinition> geoPointList, ColumnDefinition cd) {
        if(!cd.isUnitOfRetention()) {
            return false;
        } else {
            ElementDataType type = cd.getType().getDataType();
            if(type != ElementDataType.number && type != ElementDataType.integer) {
                return false;
            } else {
                ColumnDefinition cdParent = cd.getParent();
                boolean outcome = cdParent != null && geoPointList.contains(cdParent) && cd.getElementName().equals("longitude");
                return outcome;
            }
        }
    }

    public boolean mapViewIsPossible() {
        ArrayList geoPoints = this.getGeopointColumnDefinitions();
        if(geoPoints.size() != 0) {
            return true;
        } else {
            boolean hasLatitude = false;
            boolean hasLongitude = false;

            ColumnDefinition cd;
            for(Iterator i$ = this.orderedDefns.iterator(); i$.hasNext(); hasLongitude = hasLongitude || this.isLongitudeColumnDefinition(geoPoints, cd)) {
                cd = (ColumnDefinition)i$.next();
                hasLatitude = hasLatitude || this.isLatitudeColumnDefinition(geoPoints, cd);
            }

            return hasLatitude && hasLongitude;
        }
    }

    public ArrayList<ColumnDefinition> getColumnDefinitions() {
        return this.orderedDefns;
    }

    public String getAppName() {
        return this.appName;
    }

    public String getTableId() {
        return this.tableId;
    }

    public ArrayList<Column> getColumns() {
        return ColumnDefinition.getColumns(this.orderedDefns);
    }

    public TreeMap<String, Object> getDataModel() {
        return ColumnDefinition.getDataModel(this.orderedDefns);
    }

    public TreeMap<String, Object> getExtendedDataModel() {
        return ColumnDefinition.getExtendedDataModel(this.orderedDefns);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.appName);
        out.writeString(this.tableId);
        ColumnList cl = new ColumnList(this.getColumns());
        cl.writeToParcel(out, flags);
    }

    public OrderedColumns(Parcel in) {
        this.appName = in.readString();
        this.tableId = in.readString();
        ColumnList cl = new ColumnList(in);
        this.orderedDefns = ColumnDefinition.buildColumnDefinitions(this.appName, this.tableId, cl.getColumns());
    }

    public interface OrderedColumnsIterator {
        void doAction(ColumnDefinition var1) throws Exception;
    }
}
