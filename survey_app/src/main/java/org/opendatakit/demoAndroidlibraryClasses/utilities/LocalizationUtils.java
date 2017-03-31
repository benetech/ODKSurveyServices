package org.opendatakit.demoAndroidlibraryClasses.utilities;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class LocalizationUtils {
    public LocalizationUtils() {
    }

    public static String genUUID() {
        return "uuid:" + UUID.randomUUID().toString();
    }

    public static String getLocalizedDisplayName(String displayName) {
        Locale locale = Locale.getDefault();
        String full_locale = locale.toString();
        int underscore = full_locale.indexOf(95);
        String lang_only_locale = underscore == -1?full_locale:full_locale.substring(0, underscore);
        if(displayName.startsWith("\"") && displayName.endsWith("\"")) {
            return displayName.substring(1, displayName.length() - 1);
        } else if(displayName.startsWith("{") && displayName.endsWith("}")) {
            try {
                Map e = (Map)ODKFileUtils.mapper.readValue(displayName, Map.class);
                String candidate = (String)e.get(full_locale);
                if(candidate != null) {
                    return candidate;
                } else {
                    candidate = (String)e.get(lang_only_locale);
                    if(candidate != null) {
                        return candidate;
                    } else {
                        candidate = (String)e.get("default");
                        return candidate != null?candidate:null;
                    }
                }
            } catch (JsonParseException var7) {
                var7.printStackTrace();
                throw new IllegalStateException("bad displayName: " + displayName);
            } catch (JsonMappingException var8) {
                var8.printStackTrace();
                throw new IllegalStateException("bad displayName: " + displayName);
            } catch (IOException var9) {
                var9.printStackTrace();
                throw new IllegalStateException("bad displayName: " + displayName);
            }
        } else {
            throw new IllegalStateException("bad displayName: " + displayName);
        }
    }

    public static String getLocalizedDisplayName(Map<String, Object> localeMap) {
        Locale locale = Locale.getDefault();
        String full_locale = locale.toString();
        int underscore = full_locale.indexOf(95);
        String lang_only_locale = underscore == -1?full_locale:full_locale.substring(0, underscore);
        String candidate = (String)localeMap.get(full_locale);
        if(candidate != null) {
            return candidate;
        } else {
            candidate = (String)localeMap.get(lang_only_locale);
            if(candidate != null) {
                return candidate;
            } else {
                candidate = (String)localeMap.get("default");
                return candidate != null?candidate:null;
            }
        }
    }
}
