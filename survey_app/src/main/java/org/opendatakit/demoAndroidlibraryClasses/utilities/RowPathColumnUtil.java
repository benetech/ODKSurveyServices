package org.opendatakit.demoAndroidlibraryClasses.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidlibraryClasses.database.data.ColumnDefinition;
import org.opendatakit.demoAndroidlibraryClasses.database.data.OrderedColumns;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator;
import org.opendatakit.demoAndroidlibraryClasses.utilities.StaticStateManipulator.IStaticFieldManipulator;

public class RowPathColumnUtil {
    private static RowPathColumnUtil rowPathColumnUtil = new RowPathColumnUtil();

    public static RowPathColumnUtil get() {
        return rowPathColumnUtil;
    }

    public static void set(RowPathColumnUtil util) {
        rowPathColumnUtil = util;
    }

    protected RowPathColumnUtil() {
    }

    public List<ColumnDefinition> getUriColumnDefinitions(OrderedColumns orderedDefns) {
        HashSet uriFragmentList = new HashSet();
        HashSet contentTypeList = new HashSet();
        Iterator cdList = orderedDefns.getColumnDefinitions().iterator();

        while(cdList.hasNext()) {
            ColumnDefinition cd = (ColumnDefinition)cdList.next();
            ColumnDefinition cdParent = cd.getParent();
            if(cd.getElementName().equals("uriFragment") && cd.getType().getDataType() == ElementDataType.rowpath && cdParent != null) {
                uriFragmentList.add(cdParent);
            }

            if(cd.getElementName().equals("contentType") && cd.getType().getDataType() == ElementDataType.string && cdParent != null) {
                contentTypeList.add(cdParent);
            }
        }

        uriFragmentList.retainAll(contentTypeList);
        ArrayList cdList1 = new ArrayList(uriFragmentList);
        Collections.sort(cdList1);
        return cdList1;
    }

    static {
        StaticStateManipulator.get().register(50, new IStaticFieldManipulator() {
            public void reset() {
                RowPathColumnUtil.rowPathColumnUtil = new RowPathColumnUtil();
            }
        });
    }
}
