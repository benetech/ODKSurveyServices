package org.opendatakit.demoAndroidlibraryClasses.database.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.opendatakit.demoAndroidlibraryClasses.database.data.BaseTable;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public final class Row implements Parcelable {
    static final String TAG = Row.class.getSimpleName();
    private final String[] mRowData;
    private final BaseTable mOwnerTable;
    public static final Creator<Row> CREATOR = new Creator() {
        public Row createFromParcel(Parcel in) {
            return new Row(in);
        }

        public Row[] newArray(int size) {
            return new Row[size];
        }
    };

    public Row(String[] rowData, BaseTable ownerTable) {
        if(ownerTable != null && rowData != null) {
            this.mRowData = rowData;
            this.mOwnerTable = ownerTable;
        } else {
            throw new IllegalArgumentException("Null arguments are not permitted");
        }
    }

    public Row(Parcel in, BaseTable ownerTable) {
        if(ownerTable == null) {
            throw new IllegalArgumentException("Null arguments are not permitted");
        } else {
            this.mOwnerTable = ownerTable;
            int dataCount = in.readInt();
            this.mRowData = new String[dataCount];
            in.readStringArray(this.mRowData);
        }
    }

    public Row(Parcel in) {
        throw new IllegalStateException("Row invalid constructor");
    }

    public String getDataByIndex(int cellIndex) {
        return this.mRowData[cellIndex];
    }

    public String getDataByKey(String key) {
        Integer cell = Integer.valueOf(this.getCellIndexByKey(key));
        if(cell == null) {
            return null;
        } else {
            String result = this.getDataByIndex(cell.intValue());
            return result;
        }
    }

    public int getCellIndexByKey(String key) {
        return this.mOwnerTable.getColumnIndexOfElementKey(key).intValue();
    }

    public String[] getElementKeyForIndexMap() {
        return this.mOwnerTable.getElementKeyForIndex();
    }

    public BaseTable getOwnerTable() {
        return this.mOwnerTable;
    }

    public final <T> T getDataType(int cellIndex, Class<T> clazz) {
        try {
            String e = this.getDataByIndex(cellIndex);
            if(e == null) {
                return null;
            } else if(clazz == Long.class) {
                Long b3 = Long.valueOf(Long.parseLong(e));
                return (T) b3;
            } else if(clazz == Integer.class) {
                Integer b2 = Integer.valueOf(Integer.parseInt(e));
                return (T) b2;
            } else if(clazz == Double.class) {
                Double b1 = Double.valueOf(Double.parseDouble(e));
                return (T) b1;
            } else if(clazz == String.class) {
                return (T) e;
            } else if(clazz == Boolean.class) {
                Boolean b = Boolean.valueOf(!e.equals("0"));
                return (T) b;
            } else if(clazz == ArrayList.class) {
                return (T) ODKFileUtils.mapper.readValue(e, ArrayList.class);
            } else if(clazz == HashMap.class) {
                return (T) ODKFileUtils.mapper.readValue(e, HashMap.class);
            } else if(clazz == TreeMap.class) {
                return (T) ODKFileUtils.mapper.readValue(e, TreeMap.class);
            } else {
                throw new IllegalStateException("Unexpected data type in SQLite table");
            }
        } catch (ClassCastException var5) {
            var5.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var5.toString() + " in SQLite table ");
        } catch (JsonParseException var6) {
            var6.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var6.toString() + " on SQLite table");
        } catch (JsonMappingException var7) {
            var7.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var7.toString() + " on SQLite table");
        } catch (IOException var8) {
            var8.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var8.toString() + " on SQLite table");
        }
    }

    public final <T> T getDataType(String elementKey, Class<T> clazz) {
        return this.getDataType(this.getCellIndexByKey(elementKey), clazz);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        String[] emptyString = new String[0];
        if(this.mRowData == null) {
            out.writeInt(0);
            out.writeStringArray(emptyString);
        } else {
            out.writeInt(this.mRowData.length);
            out.writeStringArray(this.mRowData);
        }

    }
}
