package org.opendatakit.demoAndroidlibraryClasses.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class RoleConsts {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SUPER_USER = "ROLE_SUPER_USER_TABLES";
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTER_TABLES";
    public static final String ANONYMOUS_ROLES_LIST = "";
    public static final String USER_ROLES_LIST;
    public static final String SUPER_USER_ROLES_LIST;
    public static final String ADMIN_ROLES_LIST;

    public RoleConsts() {
    }

    static {
        ArrayList adminRoleList = new ArrayList();
        adminRoleList.add("ROLE_USER");
        adminRoleList.add("ROLE_SUPER_USER_TABLES");
        adminRoleList.add("ROLE_ADMINISTER_TABLES");
        String value = null;

        try {
            value = ODKFileUtils.mapper.writeValueAsString(adminRoleList);
        } catch (JsonProcessingException var5) {
            var5.printStackTrace();
        }

        ADMIN_ROLES_LIST = value;
        adminRoleList.clear();
        adminRoleList.add("ROLE_USER");
        adminRoleList.add("ROLE_SUPER_USER_TABLES");
        value = null;

        try {
            value = ODKFileUtils.mapper.writeValueAsString(adminRoleList);
        } catch (JsonProcessingException var4) {
            var4.printStackTrace();
        }

        SUPER_USER_ROLES_LIST = value;
        adminRoleList.clear();
        adminRoleList.add("ROLE_USER");
        value = null;

        try {
            value = ODKFileUtils.mapper.writeValueAsString(adminRoleList);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
        }

        USER_ROLES_LIST = value;
    }
}
