package org.opendatakit.demoAndroidlibraryClasses.utilities;

import android.content.Context;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;

public class FileSet {
    private static final String APPLICATION_XML = "application/xml";
    private static final String URI_FRAGMENT = "uriFragment";
    private static final String CONTENT_TYPE = "contentType";
    public File instanceFile = null;
    public final String appName;
    public ArrayList<FileSet.MimeFile> attachmentFiles = new ArrayList();

    public FileSet(String appName) {
        this.appName = appName;
    }

    public void addAttachmentFile(File file, String contentType) {
        FileSet.MimeFile f = new FileSet.MimeFile();
        f.file = file;
        f.contentType = contentType;
        this.attachmentFiles.add(f);
    }

    public String serializeUriFragmentList(Context context) throws JsonGenerationException, JsonMappingException, IOException {
        ArrayList str = new ArrayList();
        HashMap map = new HashMap();
        map.put("uriFragment", ODKFileUtils.asUriFragment(this.appName, this.instanceFile));
        map.put("contentType", "application/xml");
        str.add(map);
        Iterator serializedString = this.attachmentFiles.iterator();

        while(serializedString.hasNext()) {
            FileSet.MimeFile f = (FileSet.MimeFile)serializedString.next();
            map = new HashMap();
            map.put("uriFragment", ODKFileUtils.asUriFragment(this.appName, f.file));
            map.put("contentType", f.contentType);
            str.add(map);
        }

        String serializedString1 = ODKFileUtils.mapper.writeValueAsString(str);
        return serializedString1;
    }

    public static FileSet parse(Context context, String appName, InputStream src) throws JsonParseException, JsonMappingException, IOException {
        CollectionType javaType = ODKFileUtils.mapper.getTypeFactory().constructCollectionType(ArrayList.class, Map.class);
        ArrayList mapArrayList = null;

        try {
            mapArrayList = (ArrayList)ODKFileUtils.mapper.readValue(src, javaType);
        } catch (JsonParseException var11) {
            WebLogger.getLogger(appName).e("FileSet", "parse: problem parsing json list entry from the fileSet");
            WebLogger.getLogger(appName).printStackTrace(var11);
        } catch (JsonMappingException var12) {
            WebLogger.getLogger(appName).e("FileSet", "parse: problem mapping json list entry from the fileSet");
            WebLogger.getLogger(appName).printStackTrace(var12);
        } catch (IOException var13) {
            WebLogger.getLogger(appName).e("FileSet", "parse: i/o problem with json for list entry from the fileSet");
            WebLogger.getLogger(appName).printStackTrace(var13);
        }

        FileSet fs = new FileSet(appName);
        String instanceRelativePath = (String)((Map)mapArrayList.get(0)).get("uriFragment");
        fs.instanceFile = ODKFileUtils.getAsFile(appName, instanceRelativePath);

        for(int i = 1; i < mapArrayList.size(); ++i) {
            String relativePath = (String)((Map)mapArrayList.get(i)).get("uriFragment");
            String contentType = (String)((Map)mapArrayList.get(i)).get("contentType");
            FileSet.MimeFile f = new FileSet.MimeFile();
            f.file = ODKFileUtils.getAsFile(appName, relativePath);
            f.contentType = contentType;
            fs.attachmentFiles.add(f);
        }

        return fs;
    }

    public static final class MimeFile {
        public File file;
        public String contentType;

        public MimeFile() {
        }
    }
}
