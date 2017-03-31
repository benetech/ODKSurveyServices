package org.opendatakit.demoAndroidCommonClasses.data;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class JoinColumn {
    public static final String DEFAULT_NOT_SET_VALUE = "";
    public static final String JSON_KEY_TABLE_ID = "tableId";
    public static final String JSON_KEY_ELEMENT_KEY = "elementKey";
    private String tableId;
    private String elementKey;

    public static ArrayList<JoinColumn> fromSerialization(String str) throws JsonParseException, JsonMappingException, IOException {
        if(str != null && !"".equals(str)) {
            ArrayList jcs = new ArrayList();
            ArrayList joins = (ArrayList)ODKFileUtils.mapper.readValue(str, ArrayList.class);
            if(joins == null) {
                return null;
            } else {
                Iterator i$ = joins.iterator();

                while(i$.hasNext()) {
                    Object o = i$.next();
                    Map m = (Map)o;
                    String tId = (String)m.get("table_id");
                    String tEK = (String)m.get("element_key");
                    JoinColumn j = new JoinColumn(tId, tEK);
                    jcs.add(j);
                }

                return jcs;
            }
        } else {
            return null;
        }
    }

    public static String toSerialization(ArrayList<JoinColumn> joins) throws JsonGenerationException, JsonMappingException, IOException {
        if(joins == null) {
            return "";
        } else {
            ArrayList jlist = new ArrayList();
            Iterator i$ = joins.iterator();

            while(i$.hasNext()) {
                JoinColumn join = (JoinColumn)i$.next();
                HashMap joJoin = new HashMap();
                joJoin.put("tableId", join.getTableId());
                joJoin.put("elementKey", join.getElementKey());
                jlist.add(joJoin);
            }

            return ODKFileUtils.mapper.writeValueAsString(jlist);
        }
    }

    protected JoinColumn() {
    }

    public JoinColumn(String tableId, String elementKey) {
        this.tableId = tableId;
        this.elementKey = elementKey;
    }

    public String getTableId() {
        return this.tableId;
    }

    public String getElementKey() {
        return this.elementKey;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public void setElementKey(String elementKey) {
        this.elementKey = elementKey;
    }
}
