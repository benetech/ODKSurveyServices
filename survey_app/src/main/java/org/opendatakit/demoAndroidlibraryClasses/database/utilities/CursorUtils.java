package org.opendatakit.demoAndroidlibraryClasses.database.utilities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class CursorUtils {
    private static final String t = "CursorUtils";
    public static final int TABLE_HEALTH_IS_CLEAN = 0;
    public static final int TABLE_HEALTH_HAS_CONFLICTS = 1;
    public static final int TABLE_HEALTH_HAS_CHECKPOINTS = 2;
    public static final int TABLE_HEALTH_HAS_CHECKPOINTS_AND_CONFLICTS = 3;
    public static final String DEFAULT_LOCALE = "default";
    public static final String DEFAULT_CREATOR = "anonymous";

    public CursorUtils() {
    }

    @SuppressLint({"NewApi"})
    public static String getIndexAsString(Cursor c, int i) {
        if(i == -1) {
            return null;
        } else if(c.isNull(i)) {
            return null;
        } else {
            String v;
            switch(c.getType(i)) {
                case 0:
                case 4:
                default:
                    throw new IllegalStateException("Unexpected data type in SQLite table");
                case 1:
                    Long l1 = Long.valueOf(c.getLong(i));
                    v = l1.toString();
                    return v;
                case 2:
                    Double l = Double.valueOf(c.getDouble(i));
                    v = l.toString();
                    return v;
                case 3:
                    return c.getString(i);
            }
        }
    }

    public static Class<?> getIndexDataType(Cursor c, int i) {
        switch(c.getType(i)) {
            case 0:
                return String.class;
            case 1:
                return Long.class;
            case 2:
                return Double.class;
            case 3:
                return String.class;
            case 4:
            default:
                throw new IllegalStateException("Unexpected data type in SQLite table");
        }
    }

    public static <T> T getIndexAsType(Cursor c, Class<T> clazz, int i) {
        try {
            if(i == -1) {
                return null;
            } else if(c.isNull(i)) {
                return null;
            } else if(clazz == Long.class) {
                Long e3 = Long.valueOf(c.getLong(i));
                return (T) e3;
            } else {
                Integer e1;
                if(clazz == Integer.class) {
                    e1 = Integer.valueOf(c.getInt(i));
                    return (T) e1;
                } else if(clazz == Double.class) {
                    Double e2 = Double.valueOf(c.getDouble(i));
                    return (T) e2;
                } else {
                    String e;
                    if(clazz == String.class) {
                        e = c.getString(i);
                        return (T) e;
                    } else if(clazz == Boolean.class) {
                        e1 = Integer.valueOf(c.getInt(i));
                        return (T) Boolean.valueOf(e1.intValue() != 0);
                    } else if(clazz == ArrayList.class) {
                        e = c.getString(i);
                        return (T) ODKFileUtils.mapper.readValue(e, ArrayList.class);
                    } else if(clazz == HashMap.class) {
                        e = c.getString(i);
                        return (T) ODKFileUtils.mapper.readValue(e, HashMap.class);
                    } else {
                        throw new IllegalStateException("Unexpected data type in SQLite table");
                    }
                }
            }
        } catch (ClassCastException var4) {
            var4.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var4.toString() + " in SQLite table ");
        } catch (JsonParseException var5) {
            var5.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var5.toString() + " on SQLite table");
        } catch (JsonMappingException var6) {
            var6.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var6.toString() + " on SQLite table");
        } catch (IOException var7) {
            var7.printStackTrace();
            throw new IllegalStateException("Unexpected data type conversion failure " + var7.toString() + " on SQLite table");
        }
    }
}
