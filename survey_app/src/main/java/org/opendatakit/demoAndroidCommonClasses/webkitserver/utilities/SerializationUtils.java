package org.opendatakit.demoAndroidCommonClasses.webkitserver.utilities;

import android.os.Bundle;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;

public class SerializationUtils {
    private static final String tag = "SerializationUtils";

    private SerializationUtils() {
    }

    public static JSONObject convertFromBundle(String appName, Bundle b) throws JSONException {
        JSONObject jo = new JSONObject();
        Set keys = b.keySet();
        Iterator i$ = keys.iterator();

        while(true) {
            while(i$.hasNext()) {
                String key = (String)i$.next();
                Object o = b.get(key);
                if(o == null) {
                    jo.put(key, JSONObject.NULL);
                } else if(!o.getClass().isArray()) {
                    if(o instanceof Bundle) {
                        jo.put(key, convertFromBundle(appName, (Bundle)o));
                    } else if(o instanceof String) {
                        jo.put(key, b.getString(key));
                    } else if(o instanceof Boolean) {
                        jo.put(key, b.getBoolean(key));
                    } else if(o instanceof Integer) {
                        jo.put(key, b.getInt(key));
                    } else if(o instanceof Long) {
                        jo.put(key, b.getLong(key));
                    } else if(o instanceof Double) {
                        jo.put(key, b.getDouble(key));
                    }
                } else {
                    JSONArray ja = new JSONArray();
                    Class t = o.getClass().getComponentType();
                    int i;
                    if(t.equals(Long.TYPE)) {
                        long[] var19 = (long[])((long[])o);

                        for(i = 0; i < var19.length; ++i) {
                            ja.put(var19[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Integer.TYPE)) {
                        int[] var18 = (int[])((int[])o);

                        for(i = 0; i < var18.length; ++i) {
                            ja.put(var18[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Double.TYPE)) {
                        double[] var17 = (double[])((double[])o);

                        for(i = 0; i < var17.length; ++i) {
                            ja.put(var17[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Boolean.TYPE)) {
                        boolean[] var16 = (boolean[])((boolean[])o);

                        for(i = 0; i < var16.length; ++i) {
                            ja.put(var16[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Long.class)) {
                        Long[] var15 = (Long[])((Long[])o);

                        for(i = 0; i < var15.length; ++i) {
                            ja.put(var15[i] == null?JSONObject.NULL:var15[i]);
                        }
                    } else if(t.equals(Integer.class)) {
                        Integer[] var14 = (Integer[])((Integer[])o);

                        for(i = 0; i < var14.length; ++i) {
                            ja.put(var14[i] == null?JSONObject.NULL:var14[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Double.class)) {
                        Double[] var13 = (Double[])((Double[])o);

                        for(i = 0; i < var13.length; ++i) {
                            ja.put(var13[i] == null?JSONObject.NULL:var13[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(Boolean.class)) {
                        Boolean[] var12 = (Boolean[])((Boolean[])o);

                        for(i = 0; i < var12.length; ++i) {
                            ja.put(var12[i] == null?JSONObject.NULL:var12[i]);
                        }

                        jo.put(key, ja);
                    } else if(t.equals(String.class)) {
                        String[] var11 = (String[])((String[])o);

                        for(i = 0; i < var11.length; ++i) {
                            ja.put(var11[i] == null?JSONObject.NULL:var11[i]);
                        }

                        jo.put(key, ja);
                    } else if(!t.equals(Bundle.class) && !Bundle.class.isAssignableFrom(t)) {
                        if(!t.equals(Byte.TYPE)) {
                            throw new JSONException("unrecognized class");
                        }

                        WebLogger.getLogger(appName).w("SerializationUtils", "byte array returned -- ignoring");
                    } else {
                        Bundle[] a = (Bundle[])((Bundle[])o);

                        for(i = 0; i < a.length; ++i) {
                            ja.put(a[i] == null?JSONObject.NULL:convertFromBundle(appName, a[i]));
                        }

                        jo.put(key, ja);
                    }
                }
            }

            return jo;
        }
    }

    public static Bundle convertToBundle(JSONObject valueMap, SerializationUtils.MacroStringExpander expander) throws JSONException {
        Bundle b = new Bundle();
        Iterator cur = valueMap.keys();

        while(true) {
            while(true) {
                String key;
                do {
                    if(!cur.hasNext()) {
                        return b;
                    }

                    key = (String)cur.next();
                } while(valueMap.isNull(key));

                Object o = valueMap.get(key);
                if(o instanceof JSONObject) {
                    Bundle var11 = convertToBundle((JSONObject)o, expander);
                    b.putBundle(key, var11);
                } else if(!(o instanceof JSONArray)) {
                    if(o instanceof String) {
                        String var10 = valueMap.getString(key);
                        if(expander != null) {
                            var10 = expander.expandString(var10);
                        }

                        b.putString(key, var10);
                    } else if(o instanceof Boolean) {
                        b.putBoolean(key, valueMap.getBoolean(key));
                    } else if(o instanceof Integer) {
                        b.putInt(key, valueMap.getInt(key));
                    } else if(o instanceof Long) {
                        b.putLong(key, valueMap.getLong(key));
                    } else if(o instanceof Double) {
                        b.putDouble(key, valueMap.getDouble(key));
                    }
                } else {
                    JSONArray v = (JSONArray)o;
                    Object oe = null;

                    for(int va = 0; va < v.length(); ++va) {
                        if(!v.isNull(va)) {
                            oe = v.get(va);
                            break;
                        }
                    }

                    if(oe != null) {
                        int j;
                        if(oe instanceof JSONObject) {
                            Bundle[] var17 = new Bundle[v.length()];

                            for(j = 0; j < v.length(); ++j) {
                                if(v.isNull(j)) {
                                    var17[j] = null;
                                } else {
                                    var17[j] = convertToBundle(v.getJSONObject(j), expander);
                                }
                            }

                            b.putParcelableArray(key, var17);
                        } else {
                            if(oe instanceof JSONArray) {
                                throw new JSONException("Unable to convert nested arrays");
                            }

                            if(oe instanceof String) {
                                String[] var16 = new String[v.length()];

                                for(j = 0; j < v.length(); ++j) {
                                    if(v.isNull(j)) {
                                        var16[j] = null;
                                    } else {
                                        var16[j] = v.getString(j);
                                    }
                                }

                                b.putStringArray(key, var16);
                            } else if(oe instanceof Boolean) {
                                boolean[] var15 = new boolean[v.length()];

                                for(j = 0; j < v.length(); ++j) {
                                    if(v.isNull(j)) {
                                        var15[j] = false;
                                    } else {
                                        var15[j] = v.getBoolean(j);
                                    }
                                }

                                b.putBooleanArray(key, var15);
                            } else if(oe instanceof Integer) {
                                int[] var14 = new int[v.length()];

                                for(j = 0; j < v.length(); ++j) {
                                    if(v.isNull(j)) {
                                        var14[j] = 0;
                                    } else {
                                        var14[j] = v.getInt(j);
                                    }
                                }

                                b.putIntArray(key, var14);
                            } else if(oe instanceof Long) {
                                long[] var13 = new long[v.length()];

                                for(j = 0; j < v.length(); ++j) {
                                    if(v.isNull(j)) {
                                        var13[j] = 0L;
                                    } else {
                                        var13[j] = v.getLong(j);
                                    }
                                }

                                b.putLongArray(key, var13);
                            } else if(oe instanceof Double) {
                                double[] var12 = new double[v.length()];

                                for(j = 0; j < v.length(); ++j) {
                                    if(v.isNull(j)) {
                                        var12[j] = 0.0D / 0.0;
                                    } else {
                                        var12[j] = v.getDouble(j);
                                    }
                                }

                                b.putDoubleArray(key, var12);
                            }
                        }
                    }
                }
            }
        }
    }

    public interface MacroStringExpander {
        String expandString(String var1);
    }
}
