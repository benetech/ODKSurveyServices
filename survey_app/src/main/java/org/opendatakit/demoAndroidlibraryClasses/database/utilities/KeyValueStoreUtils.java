package org.opendatakit.demoAndroidlibraryClasses.database.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.ArrayList;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.demoAndroidlibraryClasses.database.data.KeyValueStoreEntry;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.DataHelper;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class KeyValueStoreUtils {
    public KeyValueStoreUtils() {
    }

    public static KeyValueStoreEntry buildEntry(String tableId, String partition, String aspect, String key, ElementDataType type, String serializedValue) {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.tableId = tableId;
        entry.partition = partition;
        entry.aspect = aspect;
        entry.key = key;
        entry.type = type.name();
        entry.value = serializedValue;
        return entry;
    }

    public static Double getNumber(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.number.name())) {
            throw new IllegalArgumentException("requested number entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.number.name());
        } else {
            try {
                return Double.valueOf(Double.parseDouble(entry.value));
            } catch (NumberFormatException var3) {
                throw new IllegalArgumentException("requested int entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.number.name());
            }
        }
    }

    public static Long getLong(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.integer.name())) {
            throw new IllegalArgumentException("requested int entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.integer.name());
        } else {
            try {
                return Long.valueOf(Long.parseLong(entry.value));
            } catch (NumberFormatException var3) {
                throw new IllegalArgumentException("requested int entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.integer.name());
            }
        }
    }

    public static Integer getInteger(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.integer.name())) {
            throw new IllegalArgumentException("requested int entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.integer.name());
        } else {
            try {
                return Integer.valueOf(Integer.parseInt(entry.value));
            } catch (NumberFormatException var3) {
                throw new IllegalArgumentException("requested int entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.integer.name());
            }
        }
    }

    public static Boolean getBoolean(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.bool.name())) {
            throw new IllegalArgumentException("requested boolean entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.bool.name());
        } else if(entry.value == null) {
            return null;
        } else if(entry.value.compareToIgnoreCase("true") == 0) {
            return Boolean.valueOf(true);
        } else if(entry.value.compareToIgnoreCase("false") == 0) {
            return Boolean.valueOf(false);
        } else {
            try {
                return Boolean.valueOf(DataHelper.intToBool(Integer.parseInt(entry.value)));
            } catch (NumberFormatException var3) {
                throw new IllegalArgumentException("requested boolean entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.bool.name());
            }
        }
    }

    public static String getString(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        return entry == null?null:entry.value;
    }

    public static <T> ArrayList<T> getArray(String appName, KeyValueStoreEntry entry, Class<T> clazz) throws IllegalArgumentException {
        CollectionType javaType = ODKFileUtils.mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.array.name())) {
            throw new IllegalArgumentException("requested list entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.array.name());
        } else {
            ArrayList result = null;

            try {
                if(entry.value != null && entry.value.length() != 0) {
                    result = (ArrayList)ODKFileUtils.mapper.readValue(entry.value, javaType);
                }

                return result;
            } catch (JsonParseException var6) {
                WebLogger.getLogger(appName).e("KeyValueStoreUtils", "getArray: problem parsing json list entry from the kvs");
                WebLogger.getLogger(appName).printStackTrace(var6);
                throw new IllegalArgumentException("requested list entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.array.name());
            } catch (JsonMappingException var7) {
                WebLogger.getLogger(appName).e("KeyValueStoreUtils", "getArray: problem mapping json list entry from the kvs");
                WebLogger.getLogger(appName).printStackTrace(var7);
                throw new IllegalArgumentException("requested list entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.array.name());
            } catch (IOException var8) {
                WebLogger.getLogger(appName).e("KeyValueStoreUtils", "getArray: i/o problem with json for list entry from the kvs");
                WebLogger.getLogger(appName).printStackTrace(var8);
                throw new IllegalArgumentException("requested list entry for key: " + entry.key + ", but the value in the store failed to " + "parse to type: " + ElementDataType.array.name());
            }
        }
    }

    public static String getObject(String appName, KeyValueStoreEntry entry) throws IllegalArgumentException {
        if(entry == null) {
            return null;
        } else if(!entry.type.equals(ElementDataType.object.name()) && !entry.type.equals(ElementDataType.array.name())) {
            throw new IllegalArgumentException("requested object entry for key: " + entry.key + ", but the corresponding entry in the store was " + "not of type: " + ElementDataType.object.name() + " or: " + ElementDataType.array.name());
        } else {
            return entry.value;
        }
    }
}
