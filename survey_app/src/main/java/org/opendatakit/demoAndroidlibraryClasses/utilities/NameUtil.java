package org.opendatakit.demoAndroidlibraryClasses.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Pattern;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class NameUtil {
    private static final String TAG = NameUtil.class.getSimpleName();
    private static final ArrayList<String> reservedNamesSortedList;
    private static final Pattern letterFirstPattern = Pattern.compile("^\\p{L}\\p{M}*(\\p{L}\\p{M}*|\\p{Nd}|_)*$", Pattern.UNICODE_CASE);

    public NameUtil() {
    }

    public static boolean isValidUserDefinedDatabaseName(String name) {
        boolean matchHit = letterFirstPattern.matcher(name).matches();
        boolean reserveHit = Collections.binarySearch(reservedNamesSortedList, name.toUpperCase(Locale.US)) >= 0;
        return !reserveHit && matchHit;
    }

    public static String constructSimpleDisplayName(String name) {
        String displayName = name.replaceAll("_", " ");
        if(displayName.startsWith(" ")) {
            displayName = "_" + displayName;
        }

        if(displayName.endsWith(" ")) {
            displayName = displayName + "_";
        }

        return displayName;
    }

    public static String normalizeDisplayName(String displayName) {
        if((!displayName.startsWith("\"") || !displayName.endsWith("\"")) && (!displayName.startsWith("{") || !displayName.endsWith("}"))) {
            try {
                return ODKFileUtils.mapper.writeValueAsString(displayName);
            } catch (JsonProcessingException var2) {
                var2.printStackTrace();
                throw new IllegalArgumentException("normalizeDisplayName: Invalid displayName " + displayName);
            }
        } else {
            return displayName;
        }
    }

    static {
        ArrayList reservedNames = new ArrayList();
        reservedNames.add("ROW_ETAG");
        reservedNames.add("SYNC_STATE");
        reservedNames.add("CONFLICT_TYPE");
        reservedNames.add("SAVEPOINT_TIMESTAMP");
        reservedNames.add("SAVEPOINT_CREATOR");
        reservedNames.add("SAVEPOINT_TYPE");
        reservedNames.add("FILTER_TYPE");
        reservedNames.add("FILTER_VALUE");
        reservedNames.add("FORM_ID");
        reservedNames.add("LOCALE");
        reservedNames.add("ABORT");
        reservedNames.add("ACTION");
        reservedNames.add("ADD");
        reservedNames.add("AFTER");
        reservedNames.add("ALL");
        reservedNames.add("ALTER");
        reservedNames.add("ANALYZE");
        reservedNames.add("AND");
        reservedNames.add("AS");
        reservedNames.add("ASC");
        reservedNames.add("ATTACH");
        reservedNames.add("AUTOINCREMENT");
        reservedNames.add("BEFORE");
        reservedNames.add("BEGIN");
        reservedNames.add("BETWEEN");
        reservedNames.add("BY");
        reservedNames.add("CASCADE");
        reservedNames.add("CASE");
        reservedNames.add("CAST");
        reservedNames.add("CHECK");
        reservedNames.add("COLLATE");
        reservedNames.add("COLUMN");
        reservedNames.add("COMMIT");
        reservedNames.add("CONFLICT");
        reservedNames.add("CONSTRAINT");
        reservedNames.add("CREATE");
        reservedNames.add("CROSS");
        reservedNames.add("CURRENT_DATE");
        reservedNames.add("CURRENT_TIME");
        reservedNames.add("CURRENT_TIMESTAMP");
        reservedNames.add("DATABASE");
        reservedNames.add("DEFAULT");
        reservedNames.add("DEFERRABLE");
        reservedNames.add("DEFERRED");
        reservedNames.add("DELETE");
        reservedNames.add("DESC");
        reservedNames.add("DETACH");
        reservedNames.add("DISTINCT");
        reservedNames.add("DROP");
        reservedNames.add("EACH");
        reservedNames.add("ELSE");
        reservedNames.add("END");
        reservedNames.add("ESCAPE");
        reservedNames.add("EXCEPT");
        reservedNames.add("EXCLUSIVE");
        reservedNames.add("EXISTS");
        reservedNames.add("EXPLAIN");
        reservedNames.add("FAIL");
        reservedNames.add("FOR");
        reservedNames.add("FOREIGN");
        reservedNames.add("FROM");
        reservedNames.add("FULL");
        reservedNames.add("GLOB");
        reservedNames.add("GROUP");
        reservedNames.add("HAVING");
        reservedNames.add("IF");
        reservedNames.add("IGNORE");
        reservedNames.add("IMMEDIATE");
        reservedNames.add("IN");
        reservedNames.add("INDEX");
        reservedNames.add("INDEXED");
        reservedNames.add("INITIALLY");
        reservedNames.add("INNER");
        reservedNames.add("INSERT");
        reservedNames.add("INSTEAD");
        reservedNames.add("INTERSECT");
        reservedNames.add("INTO");
        reservedNames.add("IS");
        reservedNames.add("ISNULL");
        reservedNames.add("JOIN");
        reservedNames.add("KEY");
        reservedNames.add("LEFT");
        reservedNames.add("LIKE");
        reservedNames.add("LIMIT");
        reservedNames.add("MATCH");
        reservedNames.add("NATURAL");
        reservedNames.add("NO");
        reservedNames.add("NOT");
        reservedNames.add("NOTNULL");
        reservedNames.add("NULL");
        reservedNames.add("OF");
        reservedNames.add("OFFSET");
        reservedNames.add("ON");
        reservedNames.add("OR");
        reservedNames.add("ORDER");
        reservedNames.add("OUTER");
        reservedNames.add("PLAN");
        reservedNames.add("PRAGMA");
        reservedNames.add("PRIMARY");
        reservedNames.add("QUERY");
        reservedNames.add("RAISE");
        reservedNames.add("REFERENCES");
        reservedNames.add("REGEXP");
        reservedNames.add("REINDEX");
        reservedNames.add("RELEASE");
        reservedNames.add("RENAME");
        reservedNames.add("REPLACE");
        reservedNames.add("RESTRICT");
        reservedNames.add("RIGHT");
        reservedNames.add("ROLLBACK");
        reservedNames.add("ROW");
        reservedNames.add("SAVEPOINT");
        reservedNames.add("SELECT");
        reservedNames.add("SET");
        reservedNames.add("TABLE");
        reservedNames.add("TEMP");
        reservedNames.add("TEMPORARY");
        reservedNames.add("THEN");
        reservedNames.add("TO");
        reservedNames.add("TRANSACTION");
        reservedNames.add("TRIGGER");
        reservedNames.add("UNION");
        reservedNames.add("UNIQUE");
        reservedNames.add("UPDATE");
        reservedNames.add("USING");
        reservedNames.add("VACUUM");
        reservedNames.add("VALUES");
        reservedNames.add("VIEW");
        reservedNames.add("VIRTUAL");
        reservedNames.add("WHEN");
        reservedNames.add("WHERE");
        Collections.sort(reservedNames);
        reservedNamesSortedList = reservedNames;
    }
}
