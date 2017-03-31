package org.opendatakit.demoAndroidlibraryClasses.database.utilities;

public class QueryUtil {
    private static final String TAG = QueryUtil.class.getSimpleName();
    public static final String GET_ROWS_WITH_ID_WHERE = "_id=?";
    public static final String[] GET_ROWS_WITH_ID_GROUP_BY = null;
    public static final String GET_ROWS_WITH_ID_HAVING = null;
    public static final String[] GET_ROWS_WITH_ID_ORDER_BY_KEYS = new String[]{"_savepoint_timestamp"};
    public static final String[] GET_ROWS_WITH_ID_ORDER_BY_DIR = new String[]{"DESC"};

    private QueryUtil() {
        throw new IllegalStateException("Never Instantiate this static class");
    }

    public static String buildSqlStatement(String tableId, String whereClause, String[] groupBy, String having, String[] orderByElementKey, String[] orderByDirection) {
        StringBuilder s = new StringBuilder();
        s.append("SELECT * FROM \"").append(tableId).append("\" ");
        if(whereClause != null && whereClause.length() != 0) {
            s.append(" WHERE ").append(whereClause);
        }

        boolean directionSpecified;
        int i;
        if(groupBy != null && groupBy.length != 0) {
            s.append(" GROUP BY ");
            directionSpecified = true;
            String[] first = groupBy;
            i = groupBy.length;

            for(int i$ = 0; i$ < i; ++i$) {
                String elementKey = first[i$];
                if(!directionSpecified) {
                    s.append(", ");
                }

                directionSpecified = false;
                s.append(elementKey);
            }

            if(having != null && having.length() != 0) {
                s.append(" HAVING ").append(having);
            }
        }

        directionSpecified = orderByDirection != null && orderByElementKey != null && orderByDirection.length == orderByElementKey.length;
        if(orderByElementKey != null && orderByElementKey.length != 0) {
            boolean var12 = true;

            for(i = 0; i < orderByElementKey.length; ++i) {
                if(orderByElementKey[i] != null && orderByElementKey[i].length() != 0) {
                    if(var12) {
                        s.append(" ORDER BY ");
                    } else {
                        s.append(", ");
                        var12 = false;
                    }

                    s.append(orderByElementKey[i]);
                    if(directionSpecified && orderByDirection[i] != null && orderByDirection.length > 0) {
                        s.append(" ").append(orderByDirection[i]);
                    } else {
                        s.append(" ASC");
                    }
                }
            }
        }

        return s.toString();
    }
}
