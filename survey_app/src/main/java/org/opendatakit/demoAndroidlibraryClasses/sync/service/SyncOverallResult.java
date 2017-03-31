package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncOutcome;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.TableLevelResult;

public class SyncOverallResult implements Parcelable {
    private SyncOutcome appLevelSyncOutcome;
    private final TreeMap<String, TableLevelResult> mResults;
    public static final Creator<SyncOverallResult> CREATOR = new Creator() {
        public SyncOverallResult createFromParcel(Parcel in) {
            return new SyncOverallResult(in);
        }

        public SyncOverallResult[] newArray(int size) {
            return new SyncOverallResult[size];
        }
    };

    public SyncOverallResult() {
        this.appLevelSyncOutcome = SyncOutcome.WORKING;
        this.mResults = new TreeMap();
    }

    protected SyncOverallResult(Parcel in) {
        this.appLevelSyncOutcome = SyncOutcome.WORKING;
        this.mResults = new TreeMap();
        this.appLevelSyncOutcome = (SyncOutcome)in.readParcelable(SyncOutcome.class.getClassLoader());
        int count = in.readInt();

        for(int i = 0; i < count; ++i) {
            String tableId = in.readString();
            TableLevelResult tableLevelResult = (TableLevelResult)in.readParcelable(TableLevelResult.class.getClassLoader());
            this.mResults.put(tableId, tableLevelResult);
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.appLevelSyncOutcome, flags);
        dest.writeInt(this.mResults.size());
        Iterator i$ = this.mResults.keySet().iterator();

        while(i$.hasNext()) {
            String tableId = (String)i$.next();
            TableLevelResult tableLevelResult = (TableLevelResult)this.mResults.get(tableId);
            dest.writeString(tableId);
            dest.writeParcelable(tableLevelResult, flags);
        }

    }

    public int describeContents() {
        return 0;
    }

    public SyncOutcome getAppLevelSyncOutcome() {
        return this.appLevelSyncOutcome;
    }

    public void setAppLevelSyncOutcome(SyncOutcome syncOutcome) {
        this.appLevelSyncOutcome = syncOutcome;
    }

    public List<TableLevelResult> getTableLevelResults() {
        ArrayList r = new ArrayList();
        r.addAll(this.mResults.values());
        Collections.sort(r, new Comparator() {
            @Override
            public int compare(Object lhs1, Object rhs1) {
                TableLevelResult lhs = (TableLevelResult)lhs1;
                TableLevelResult rhs = (TableLevelResult)rhs1;
                return lhs.getTableId().compareTo(rhs.getTableId());
            }
        });
        return r;
    }

    public void setTableLevelResult(String tableId, TableLevelResult tableLevelResult) {
        this.mResults.put(tableId, tableLevelResult);
    }

    public TableLevelResult fetchTableLevelResult(String tableId) {
        TableLevelResult r = (TableLevelResult)this.mResults.get(tableId);
        if(r == null) {
            r = new TableLevelResult(tableId);
            this.mResults.put(tableId, r);
        }

        return r;
    }
}
