package org.opendatakit.demoAndroidlibraryClasses.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ODKFileUtils {
    private static final String t = "ODKFileUtils";
    private static final String ODK_DEFAULT_APP_NAME = "default";
    private static final String ODK_FOLDER_NAME = "opendatakit";
    private static final String CONFIG_FOLDER_NAME = "config";
    private static final String DATA_FOLDER_NAME = "data";
    private static final String OUTPUT_FOLDER_NAME = "output";
    private static final String SYSTEM_FOLDER_NAME = "system";
    private static final String PERMANENT_FOLDER_NAME = "permanent";
    private static final String ASSETS_FOLDER_NAME = "assets";
    private static final String TABLES_FOLDER_NAME = "tables";
    private static final String WEB_DB_FOLDER_NAME = "webDb";
    private static final String GEO_CACHE_FOLDER_NAME = "geoCache";
    private static final String APP_CACHE_FOLDER_NAME = "appCache";
    private static final String CSV_FOLDER_NAME = "csv";
    private static final String LOGGING_FOLDER_NAME = "logging";
    private static final String DEBUG_FOLDER_NAME = "debug";
    private static final String STALE_TABLES_FOLDER_NAME = "tables.deleting";
    private static final String PENDING_TABLES_FOLDER_NAME = "tables.pending";
    private static final String FORMS_FOLDER_NAME = "forms";
    private static final String COLLECT_FORMS_FOLDER_NAME = "collect-forms";
    private static final String INSTANCES_FOLDER_NAME = "instances";
    private static final String DATABASE_NAME = "sqlite.db";
    private static final String DATABASE_LOCK_FILE_NAME = "db.lock";
    private static final String ODK_TABLES_INIT_FILENAME = "tables.init";
    private static final String ODK_TABLES_HOME_SCREEN_FILE_NAME = "index.html";
    private static final String PROPERTIES_CSV = "properties.csv";
    private static final String DEFINITION_CSV = "definition.csv";
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final String MD5_COLON_PREFIX = "md5:";
    public static final String FILENAME_XFORMS_XML = "xforms.xml";
    public static final String FORMDEF_JSON_FILENAME = "formDef.json";
    public static final String VALID_FILENAME = "[ _\\-A-Za-z0-9]*.x[ht]*ml";

    public ODKFileUtils() {
    }

    public static File fileFromUriOnWebServer(String uri) {
        int idxAppName = uri.indexOf(47);
        if(idxAppName == -1) {
            return null;
        } else {
            String appName;
            String uriFragment;
            if(idxAppName == 0) {
                idxAppName = uri.indexOf(47, idxAppName + 1);
                if(idxAppName == -1) {
                    return null;
                }

                appName = uri.substring(1, idxAppName);
                uriFragment = uri.substring(idxAppName + 1);
            } else {
                appName = uri.substring(0, idxAppName);
                uriFragment = uri.substring(idxAppName + 1);
            }

            File filename = getAsFile(appName, uriFragment);
            if(!filename.exists()) {
                return null;
            } else {
                String[] parts = uriFragment.split("/");
                if(parts.length > 1) {
                    if(parts[0].equals("config")) {
                        return filename;
                    }

                    if(parts[0].equals("system")) {
                        return filename;
                    }

                    if(parts[0].equals("permanent")) {
                        return filename;
                    }

                    if(parts[0].equals("data") && parts.length > 2 && parts[1].equals("tables")) {
                        return filename;
                    }
                }

                return null;
            }
        }
    }

    private static String getNameOfLoggingFolder() {
        return "logging";
    }

    private static String getNameOfDataFolder() {
        return "data";
    }

    private static String getNameOfSystemFolder() {
        return "system";
    }

    private static String getNameOfPermanentFolder() {
        return "permanent";
    }

    public static String getNameOfInstancesFolder() {
        return "instances";
    }

    public static String getNameOfSQLiteDatabase() {
        return "sqlite.db";
    }

    public static String getNameOfSQLiteDatabaseLockFile() {
        return "db.lock";
    }

    public static void verifyExternalStorageAvailability() {
        String cardstatus = Environment.getExternalStorageState();
        if(cardstatus.equals("removed") || cardstatus.equals("unmountable") || cardstatus.equals("unmounted") || cardstatus.equals("mounted_ro") || cardstatus.equals("shared")) {
            RuntimeException e = new RuntimeException("ODK reports :: SDCard error: " + Environment.getExternalStorageState());
            throw e;
        }
    }

    public static boolean createFolder(String path) {
        boolean made = true;
        File dir = new File(path);
        if(!dir.exists()) {
            made = dir.mkdirs();
        }

        return made;
    }

    public static String getOdkFolder() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "opendatakit";
        return path;
    }

    public static String getOdkDefaultAppName() {
        return "default";
    }

    public static File[] getAppFolders() {
        File odk = new File(getOdkFolder());
        File[] results = odk.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        return results;
    }

    public static void assertDirectoryStructure(String appName) {
        if(!appName.equals(getOdkDefaultAppName())) {
            byte dirs = 0;
            int var9 = dirs + 1;
        }

        String[] var10 = new String[]{getAppFolder(appName), getConfigFolder(appName), getDataFolder(appName), getOutputFolder(appName), getSystemFolder(appName), getPermanentFolder(appName), getAssetsFolder(appName), getTablesFolder(appName), getAppCacheFolder(appName), getGeoCacheFolder(appName), getWebDbFolder(appName), getTableDataFolder(appName), getLoggingFolder(appName), getOutputCsvFolder(appName), getTablesDebugObjectFolder(appName), getPendingDeletionTablesFolder(appName), getPendingInsertionTablesFolder(appName)};
        String[] nomedia = var10;
        int ex = var10.length;

        for(int e = 0; e < ex; ++e) {
            String dirName = nomedia[e];
            File dir = new File(dirName);
            RuntimeException e1;
            if(!dir.exists()) {
                if(!dir.mkdirs()) {
                    e1 = new RuntimeException("Cannot create directory: " + dirName);
                    throw e1;
                }
            } else if(!dir.isDirectory()) {
                e1 = new RuntimeException(dirName + " exists, but is not a directory");
                throw e1;
            }
        }

        File var11 = new File(getAppFolder(appName), ".nomedia");

        try {
            var11.createNewFile();
        } catch (IOException var8) {
            RuntimeException var12 = new RuntimeException("Cannot create .nomedia in app directory: " + var8.toString());
            throw var12;
        }
    }

    public static void clearConfiguredToolFiles(String appName) {
        File dataDir = new File(getDataFolder(appName));
        File[] filesToDelete = dataDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if(pathname.isDirectory()) {
                    return false;
                } else {
                    String name = pathname.getName();
                    int idx = name.lastIndexOf(46);
                    if(idx == -1) {
                        return false;
                    } else {
                        String type = name.substring(idx + 1);
                        boolean outcome = type.equals("version");
                        return outcome;
                    }
                }
            }
        });
        File[] arr$ = filesToDelete;
        int len$ = filesToDelete.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            File f = arr$[i$];
            f.delete();
        }

    }

    public static void assertConfiguredSurveyApp(String appName, String apkVersion) {
        assertConfiguredOdkApp(appName, "survey.version", apkVersion);
    }

    public static void assertConfiguredTablesApp(String appName, String apkVersion) {
        assertConfiguredOdkApp(appName, "tables.version", apkVersion);
    }

    public static void assertConfiguredScanApp(String appName, String apkVersion) {
        assertConfiguredOdkApp(appName, "scan.version", apkVersion);
    }

    public static void assertConfiguredToolApp(String appName, String toolName, String apkVersion) {
        assertConfiguredOdkApp(appName, toolName + ".version", apkVersion);
    }

    public static void assertConfiguredOdkApp(String appName, String odkAppVersionFile, String apkVersion) {
        File versionFile = new File(getDataFolder(appName), odkAppVersionFile);
        if(!versionFile.exists()) {
            versionFile.getParentFile().mkdirs();
        }

        FileOutputStream fs = null;
        OutputStreamWriter w = null;
        BufferedWriter bw = null;

        try {
            fs = new FileOutputStream(versionFile, false);
            w = new OutputStreamWriter(fs, Charsets.UTF_8);
            bw = new BufferedWriter(w);
            bw.write(apkVersion);
            bw.write("\n");
        } catch (IOException var24) {
            WebLogger.getLogger(appName).printStackTrace(var24);
        } finally {
            if(bw != null) {
                try {
                    bw.flush();
                    bw.close();
                } catch (IOException var23) {
                    WebLogger.getLogger(appName).printStackTrace(var23);
                }
            }

            if(w != null) {
                try {
                    w.close();
                } catch (IOException var22) {
                    WebLogger.getLogger(appName).printStackTrace(var22);
                }
            }

            try {
                if(fs != null) {
                    fs.close();
                }
            } catch (IOException var21) {
                WebLogger.getLogger(appName).printStackTrace(var21);
            }

        }

    }

    public static boolean isConfiguredSurveyApp(String appName, String apkVersion) {
        return isConfiguredOdkApp(appName, "survey.version", apkVersion);
    }

    public static boolean isConfiguredTablesApp(String appName, String apkVersion) {
        return isConfiguredOdkApp(appName, "tables.version", apkVersion);
    }

    public static boolean isConfiguredScanApp(String appName, String apkVersion) {
        return isConfiguredOdkApp(appName, "scan.version", apkVersion);
    }

    public static boolean isConfiguredToolApp(String appName, String toolName, String apkVersion) {
        return isConfiguredOdkApp(appName, toolName + ".version", apkVersion);
    }

    private static boolean isConfiguredOdkApp(String appName, String odkAppVersionFile, String apkVersion) {
        File versionFile = new File(getDataFolder(appName), odkAppVersionFile);
        if(!versionFile.exists()) {
            return false;
        } else {
            String versionLine = null;
            FileInputStream fs = null;
            InputStreamReader r = null;
            BufferedReader br = null;

            label161: {
                boolean arr$;
                try {
                    fs = new FileInputStream(versionFile);
                    r = new InputStreamReader(fs, Charsets.UTF_8);
                    br = new BufferedReader(r);
                    versionLine = br.readLine();
                    break label161;
                } catch (IOException var27) {
                    var27.printStackTrace();
                    arr$ = false;
                } finally {
                    if(br != null) {
                        try {
                            br.close();
                        } catch (IOException var26) {
                            var26.printStackTrace();
                        }
                    }

                    if(r != null) {
                        try {
                            r.close();
                        } catch (IOException var25) {
                            var25.printStackTrace();
                        }
                    }

                    try {
                        if(fs != null) {
                            fs.close();
                        }
                    } catch (IOException var24) {
                        var24.printStackTrace();
                    }

                }

                return arr$;
            }

            String[] versionRange = versionLine.split(";");
            String[] var29 = versionRange;
            int len$ = versionRange.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String version = var29[i$];
                if(version.trim().equals(apkVersion)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static File fromAppPath(String appPath) {
        String[] terms = appPath.split(File.separator);
        if(terms != null && terms.length >= 1) {
            File f = new File(new File(getOdkFolder()), appPath);
            return f;
        } else {
            return null;
        }
    }

    public static String toAppPath(String fullpath) {
        String path = getOdkFolder() + File.separator;
        if(fullpath.startsWith(path)) {
            String var7 = fullpath.substring(path.length());
            String[] var8 = var7.split(File.separator);
            if(var8 != null && var8.length >= 1) {
                return var7;
            } else {
                Log.w("ODKFileUtils", "Missing file path (nothing under \'opendatakit\'): " + fullpath);
                return null;
            }
        } else {
            String[] parts = fullpath.split(File.separator);

            int i;
            for(i = 0; parts.length > i && !parts[i].equals("opendatakit"); ++i) {
                ;
            }

            if(i == parts.length) {
                Log.w("ODKFileUtils", "File path is not under expected \'opendatakit\' Folder (" + path + ") conversion failed for: " + fullpath);
                return null;
            } else {
                int len;
                for(len = 0; i >= 0; --i) {
                    len += parts[i].length() + 1;
                }

                String partialPath = fullpath.substring(len);
                String[] app = partialPath.split(File.separator);
                if(app != null && app.length >= 1) {
                    Log.w("ODKFileUtils", "File path is not under expected \'opendatakit\' Folder -- remapped " + fullpath + " as: " + path + partialPath);
                    return partialPath;
                } else {
                    Log.w("ODKFileUtils", "File path is not under expected \'opendatakit\' Folder (" + path + ") missing file path (nothing under \'" + "opendatakit" + "\'): " + fullpath);
                    return null;
                }
            }
        }
    }

    public static String getAppFolder(String appName) {
        String path = getOdkFolder() + File.separator + appName;
        return path;
    }

    public static String getAndroidObbFolder(String packageName) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "obb" + File.separator + packageName;
        return path;
    }

    public static String getConfigFolder(String appName) {
        String path = getAppFolder(appName) + File.separator + "config";
        return path;
    }

    public static String getDataFolder(String appName) {
        String path = getAppFolder(appName) + File.separator + "data";
        return path;
    }

    public static String getOutputFolder(String appName) {
        String path = getAppFolder(appName) + File.separator + "output";
        return path;
    }

    public static String getSystemFolder(String appName) {
        String path = getAppFolder(appName) + File.separator + "system";
        return path;
    }

    public static String getPermanentFolder(String appName) {
        String path = getAppFolder(appName) + File.separator + "permanent";
        return path;
    }

    public static String getAssetsFolder(String appName) {
        String path = getConfigFolder(appName) + File.separator + "assets";
        return path;
    }

    public static String getAssetsCsvFolder(String appName) {
        String assetsFolder = getAssetsFolder(appName);
        String result = assetsFolder + File.separator + "csv";
        return result;
    }

    public static String getAssetsCsvInstancesFolder(String appName, String tableId) {
        String assetsCsvFolder = getAssetsCsvFolder(appName);
        String result = assetsCsvFolder + File.separator + tableId + File.separator + "instances";
        return result;
    }

    public static String getAssetsCsvInstanceFolder(String appName, String tableId, String instanceId) {
        String assetsCsvInstancesFolder = getAssetsCsvInstancesFolder(appName, tableId);
        String result = assetsCsvInstancesFolder + File.separator + safeInstanceIdFolderName(instanceId);
        return result;
    }

    public static String getTablesInitializationFile(String appName) {
        String assetsFolder = getAssetsFolder(appName);
        String result = assetsFolder + File.separator + "tables.init";
        return result;
    }

    public static String getTablesHomeScreenFile(String appName) {
        String assetsFolder = getAssetsFolder(appName);
        String result = assetsFolder + File.separator + "index.html";
        return result;
    }

    public static String getTablesFolder(String appName) {
        String path = getConfigFolder(appName) + File.separator + "tables";
        return path;
    }

    public static String getTablesFolder(String appName, String tableId) {
        if(tableId != null && tableId.length() != 0) {
            if(!tableId.matches("^\\p{L}\\p{M}*(\\p{L}\\p{M}*|\\p{Nd}|_)+$")) {
                throw new IllegalArgumentException("getFormFolder: tableId does not begin with a letter and contain only letters, digits or underscores!");
            } else {
                String path;
                if("framework".equals(tableId)) {
                    path = getAssetsFolder(appName) + File.separator + "framework";
                } else {
                    path = getTablesFolder(appName) + File.separator + tableId;
                }

                File f = new File(path);
                f.mkdirs();
                return f.getAbsolutePath();
            }
        } else {
            throw new IllegalArgumentException("getTablesFolder: tableId is null or the empty string!");
        }
    }

    public static String getTableDefinitionCsvFile(String appName, String tableId) {
        return getTablesFolder(appName, tableId) + File.separator + "definition.csv";
    }

    public static String getTablePropertiesCsvFile(String appName, String tableId) {
        return getTablesFolder(appName, tableId) + File.separator + "properties.csv";
    }

    public static String getFormsFolder(String appName, String tableId) {
        String path = getTablesFolder(appName, tableId) + File.separator + "forms";
        return path;
    }

    public static String getFormFolder(String appName, String tableId, String formId) {
        if(formId != null && formId.length() != 0) {
            if(!formId.matches("^\\p{L}\\p{M}*(\\p{L}\\p{M}*|\\p{Nd}|_)+$")) {
                throw new IllegalArgumentException("getFormFolder: formId does not begin with a letter and contain only letters, digits or underscores!");
            } else {
                String path = getFormsFolder(appName, tableId) + File.separator + formId;
                return path;
            }
        } else {
            throw new IllegalArgumentException("getFormFolder: formId is null or the empty string!");
        }
    }

    public static String getTablesInitializationCompleteMarkerFile(String appName) {
        String result = getDataFolder(appName) + File.separator + "tables.init";
        return result;
    }

    public static String getAppCacheFolder(String appName) {
        String path = getDataFolder(appName) + File.separator + "appCache";
        return path;
    }

    public static String getTempDrawFile(String appName) {
        String path = getAppCacheFolder(appName) + File.separator + "tmpDraw.jpg";
        return path;
    }

    public static String getTempFile(String appName) {
        String path = getAppCacheFolder(appName) + File.separator + "tmp.jpg";
        return path;
    }

    public static String getGeoCacheFolder(String appName) {
        String path = getDataFolder(appName) + File.separator + "geoCache";
        return path;
    }

    public static String getWebDbFolder(String appName) {
        String path = getDataFolder(appName) + File.separator + "webDb";
        return path;
    }

    private static String getTableDataFolder(String appName) {
        String path = getDataFolder(appName) + File.separator + "tables";
        return path;
    }

    public static String getInstancesFolder(String appName, String tableId) {
        String path = getTableDataFolder(appName) + File.separator + tableId + File.separator + "instances";
        File f = new File(path);
        f.mkdirs();
        return f.getAbsolutePath();
    }

    private static String safeInstanceIdFolderName(String instanceId) {
        if(instanceId != null && instanceId.length() != 0) {
            String instanceFolder = instanceId.replaceAll("(\\p{P}|\\p{Z})", "_");
            return instanceFolder;
        } else {
            throw new IllegalArgumentException("getInstanceFolder: instanceId is null or the empty string!");
        }
    }

    public static String getInstanceFolder(String appName, String tableId, String instanceId) {
        String instanceFolder = safeInstanceIdFolderName(instanceId);
        String path = getInstancesFolder(appName, tableId) + File.separator + instanceFolder;
        File f = new File(path);
        f.mkdirs();
        return f.getAbsolutePath();
    }

    public static File getRowpathFile(String appName, String tableId, String instanceId, String rowpathUri) {
        if(rowpathUri.startsWith("/")) {
            rowpathUri = rowpathUri.substring(1);
        }

        String instanceFolder = getInstanceFolder(appName, tableId, instanceId);
        String instanceUri = asUriFragment(appName, new File(instanceFolder));
        String fileUri;
        if(rowpathUri.startsWith(instanceUri)) {
            WebLogger.getLogger(appName).e("ODKFileUtils", "table [" + tableId + "] contains old-style rowpath constructs!");
            fileUri = rowpathUri;
        } else {
            fileUri = instanceUri + "/" + rowpathUri;
        }

        File theFile = getAsFile(appName, fileUri);
        return theFile;
    }

    public static String asRowpathUri(String appName, String tableId, String instanceId, File rowFile) {
        String instanceFolder = getInstanceFolder(appName, tableId, instanceId);
        String instanceUri = asUriFragment(appName, new File(instanceFolder));
        String rowpathUri = asUriFragment(appName, rowFile);
        if(!rowpathUri.startsWith(instanceUri)) {
            throw new IllegalArgumentException("asRowpathUri -- rowFile is not in a valid rowpath location!");
        } else {
            String relativeUri = rowpathUri.substring(instanceUri.length());
            if(relativeUri.startsWith("/")) {
                relativeUri = relativeUri.substring(1);
            }

            return relativeUri;
        }
    }

    public static String getLoggingFolder(String appName) {
        String path = getOutputFolder(appName) + File.separator + "logging";
        return path;
    }

    public static String getTablesDebugObjectFolder(String appName) {
        String outputFolder = getOutputFolder(appName);
        String result = outputFolder + File.separator + "debug";
        return result;
    }

    public static String getOutputCsvFolder(String appName) {
        String outputFolder = getOutputFolder(appName);
        String result = outputFolder + File.separator + "csv";
        return result;
    }

    public static String getOutputCsvInstanceFolder(String appName, String tableId, String instanceId) {
        String csvOutputFolder = getOutputCsvFolder(appName);
        String result = csvOutputFolder + File.separator + tableId + File.separator + "instances" + File.separator + safeInstanceIdFolderName(instanceId);
        return result;
    }

    public static String getOutputTableCsvFile(String appName, String tableId, String fileQualifier) {
        return getOutputCsvFolder(appName) + File.separator + tableId + (fileQualifier != null && fileQualifier.length() != 0?"." + fileQualifier:"") + ".csv";
    }

    public static String getOutputTableDefinitionCsvFile(String appName, String tableId, String fileQualifier) {
        return getOutputCsvFolder(appName) + File.separator + tableId + (fileQualifier != null && fileQualifier.length() != 0?"." + fileQualifier:"") + "." + "definition.csv";
    }

    public static String getOutputTablePropertiesCsvFile(String appName, String tableId, String fileQualifier) {
        return getOutputCsvFolder(appName) + File.separator + tableId + (fileQualifier != null && fileQualifier.length() != 0?"." + fileQualifier:"") + "." + "properties.csv";
    }

    public static String getPendingDeletionTablesFolder(String appName) {
        String path = getSystemFolder(appName) + File.separator + "tables.deleting";
        return path;
    }

    public static String getPendingInsertionTablesFolder(String appName) {
        String path = getSystemFolder(appName) + File.separator + "tables.pending";
        return path;
    }

    public static boolean isPathUnderAppName(String appName, File path) {
        for(File parentDir = new File(getAppFolder(appName)); path != null && !path.equals(parentDir); path = path.getParentFile()) {
            ;
        }

        return path != null;
    }

    public static String extractAppNameFromPath(File path) {
        if(path == null) {
            return null;
        } else {
            File parent = path.getParentFile();

            for(File odkDir = new File(getOdkFolder()); parent != null && !parent.equals(odkDir); parent = parent.getParentFile()) {
                path = parent;
            }

            return parent == null?null:path.getName();
        }
    }

    public static String asRelativePath(String appName, File fileUnderAppName) {
        File parentDir = new File(getAppFolder(appName));
        ArrayList pathElements = new ArrayList();

        File f;
        for(f = fileUnderAppName; f != null && !f.equals(parentDir); f = f.getParentFile()) {
            pathElements.add(f.getName());
        }

        if(f == null) {
            throw new IllegalArgumentException("file is not located under this appName (" + appName + ")!");
        } else {
            StringBuilder b = new StringBuilder();

            for(int i = pathElements.size() - 1; i >= 0; --i) {
                String element = (String)pathElements.get(i);
                b.append(element);
                if(i != 0) {
                    b.append(File.separator);
                }
            }

            return b.toString();
        }
    }

    public static String asUriFragment(String appName, File fileUnderAppName) {
        String relativePath = asRelativePath(appName, fileUnderAppName);
        String separatorString;
        if(File.separatorChar == 92) {
            separatorString = File.separator + File.separator;
        } else {
            separatorString = File.separator;
        }

        String[] segments = relativePath.split(separatorString);
        StringBuilder b = new StringBuilder();
        boolean first = true;
        String[] arr$ = segments;
        int len$ = segments.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            if(!first) {
                b.append("/");
            }

            first = false;
            b.append(s);
        }

        return b.toString();
    }

    public static File getAsFile(String appName, String uriFragment) {
        if(uriFragment != null && uriFragment.length() != 0) {
            File f = fromAppPath(appName);
            if(f != null && f.exists() && f.isDirectory()) {
                String[] segments = uriFragment.split("/");

                for(int i = 0; i < segments.length; ++i) {
                    String s = segments[i];
                    f = new File(f, s);
                }

                return f;
            } else {
                throw new IllegalArgumentException("Not a valid uriFragment: " + appName + "/" + uriFragment + " invalid application.");
            }
        } else {
            throw new IllegalArgumentException("Not a valid uriFragment: " + appName + "/" + uriFragment + " application or subdirectory not specified.");
        }
    }

    public static File asAppFile(String appName, String relativePath) {
        return new File(getAppFolder(appName) + File.separator + relativePath);
    }

    public static File asConfigFile(String appName, String relativePath) {
        return new File(getConfigFolder(appName) + File.separator + relativePath);
    }

    public static String asConfigRelativePath(String appName, File fileUnderAppConfigName) {
        String relativePath = asRelativePath(appName, fileUnderAppConfigName);
        if(!relativePath.startsWith("config" + File.separator)) {
            throw new IllegalArgumentException("File is not located under config folder");
        } else {
            relativePath = relativePath.substring("config".length() + File.separator.length());
            if(relativePath.contains(File.separator + "..")) {
                throw new IllegalArgumentException("File contains " + File.separator + "..");
            } else {
                return relativePath;
            }
        }
    }

    public static String getRelativeFormPath(String appName, File formDefFile) {
        String relativePath = asRelativePath(appName, formDefFile.getParentFile());
        relativePath = ".." + File.separator + relativePath + File.separator;
        return relativePath;
    }

    public static String getRelativeSystemPath() {
        return "../system";
    }

    public static byte[] getFileAsBytes(String appName, File file) {
        Object bytes = null;
        FileInputStream is = null;

        Object e1;
        try {
            is = new FileInputStream(file);
            long e = file.length();
            if(e <= 2147483647L) {
                byte[] bytes1 = new byte[(int)e];
                int e4 = 0;
                int read = 0;

                Object e3;
                try {
                    while(e4 < bytes1.length && read >= 0) {
                        read = is.read(bytes1, e4, bytes1.length - e4);
                        e4 += read;
                    }
                } catch (IOException var25) {
                    WebLogger.getLogger(appName).e("ODKFileUtils", "Cannot read " + file.getName());
                    var25.printStackTrace();
                    e3 = null;
                    return (byte[])e3;
                }

                if(e4 < bytes1.length) {
                    try {
                        throw new IOException("Could not completely read file " + file.getName());
                    } catch (IOException var24) {
                        WebLogger.getLogger(appName).printStackTrace(var24);
                        e3 = null;
                        return (byte[])e3;
                    }
                }

                byte[] e2 = bytes1;
                return e2;
            }

            WebLogger.getLogger(appName).e("ODKFileUtils", "File " + file.getName() + "is too large");
            e1 = null;
        } catch (FileNotFoundException var26) {
            WebLogger.getLogger(appName).e("ODKFileUtils", "Cannot find " + file.getName());
            WebLogger.getLogger(appName).printStackTrace(var26);
            Object var5 = null;
            return (byte[])var5;
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException var23) {
                WebLogger.getLogger(appName).e("ODKFileUtils", "Cannot close input stream for " + file.getName());
                WebLogger.getLogger(appName).printStackTrace(var23);
            }

        }

        return (byte[])e1;
    }

    public static String getMd5Hash(String appName, File file) {
        return "md5:" + getNakedMd5Hash(appName, file);
    }

    public static long getMostRecentlyModifiedDate(File formDir) {
        long lastModifiedDate = formDir.lastModified();
        Iterator allFiles = FileUtils.iterateFiles(formDir, (String[])null, true);

        while(allFiles.hasNext()) {
            File f = (File)allFiles.next();
            if(f.lastModified() > lastModifiedDate) {
                lastModifiedDate = f.lastModified();
            }
        }

        return lastModifiedDate;
    }

    public static String getNakedMd5Hash(String appName, File file) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            short chunkSize = 8192;
            byte[] chunk = new byte[chunkSize];
            long lLength = file.length();
            if(lLength > 2147483647L) {
                WebLogger.getLogger(appName).e("ODKFileUtils", "File " + file.getName() + "is too large");
                return null;
            } else {
                int length = (int)lLength;
                FileInputStream is = null;
                is = new FileInputStream(file);
                boolean l = false;

                int l1;
                for(l1 = 0; l1 + chunkSize < length; l1 += chunkSize) {
                    is.read(chunk, 0, chunkSize);
                    e.update(chunk, 0, chunkSize);
                }

                int remaining = length - l1;
                if(remaining > 0) {
                    is.read(chunk, 0, remaining);
                    e.update(chunk, 0, remaining);
                }

                byte[] messageDigest = e.digest();
                BigInteger number = new BigInteger(1, messageDigest);

                String md5;
                for(md5 = number.toString(16); md5.length() < 32; md5 = "0" + md5) {
                    ;
                }

                is.close();
                return md5;
            }
        } catch (NoSuchAlgorithmException var14) {
            WebLogger.getLogger(appName).e("MD5", var14.getMessage());
            return null;
        } catch (FileNotFoundException var15) {
            WebLogger.getLogger(appName).e("No Cache File", var15.getMessage());
            return null;
        } catch (IOException var16) {
            WebLogger.getLogger(appName).e("Problem reading from file", var16.getMessage());
            return null;
        }
    }

    public static String getNakedMd5Hash(String appName, String contents) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            short chunkSize = 256;
            byte[] chunk = new byte[chunkSize];
            long lLength = (long)contents.length();
            if(lLength > 2147483647L) {
                WebLogger.getLogger(appName).e("ODKFileUtils", "Contents is too large");
                return null;
            } else {
                int length = (int)lLength;
                ByteArrayInputStream is = null;
                is = new ByteArrayInputStream(contents.getBytes("UTF-8"));
                boolean l = false;

                int l1;
                for(l1 = 0; l1 + chunkSize < length; l1 += chunkSize) {
                    is.read(chunk, 0, chunkSize);
                    e.update(chunk, 0, chunkSize);
                }

                int remaining = length - l1;
                if(remaining > 0) {
                    is.read(chunk, 0, remaining);
                    e.update(chunk, 0, remaining);
                }

                byte[] messageDigest = e.digest();
                BigInteger number = new BigInteger(1, messageDigest);

                String md5;
                for(md5 = number.toString(16); md5.length() < 32; md5 = "0" + md5) {
                    ;
                }

                is.close();
                return md5;
            }
        } catch (NoSuchAlgorithmException var14) {
            WebLogger.getLogger(appName).e("MD5", var14.getMessage());
            return null;
        } catch (FileNotFoundException var15) {
            WebLogger.getLogger(appName).e("No Cache File", var15.getMessage());
            return null;
        } catch (IOException var16) {
            WebLogger.getLogger(appName).e("Problem reading from file", var16.getMessage());
            return null;
        }
    }

    public static Bitmap getBitmapScaledToDisplay(String appName, File f, int screenHeight, int screenWidth) {
        Options o = new Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getAbsolutePath(), o);
        int heightScale = o.outHeight / screenHeight;
        int widthScale = o.outWidth / screenWidth;
        int scale = Math.max(widthScale, heightScale);
        Options options = new Options();
        options.inSampleSize = scale;
        Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
        if(b != null) {
            WebLogger.getLogger(appName).i("ODKFileUtils", "Screen is " + screenHeight + "x" + screenWidth + ".  Image has been scaled down by " + scale + " to " + b.getHeight() + "x" + b.getWidth());
        }

        return b;
    }

    public static String getXMLText(Node n, boolean trim) {
        NodeList nl = n.getChildNodes();
        return nl.getLength() == 0?null:getXMLText(nl, 0, trim);
    }

    private static String getXMLText(NodeList nl, int i, boolean trim) {
        StringBuffer strBuff = null;
        String text = nl.item(i).getTextContent();
        if(text == null) {
            return null;
        } else {
            ++i;

            while(i < nl.getLength() && nl.item(i).getNodeType() == 3) {
                if(strBuff == null) {
                    strBuff = new StringBuffer(text);
                }

                strBuff.append(nl.item(i).getTextContent());
                ++i;
            }

            if(strBuff != null) {
                text = strBuff.toString();
            }

            if(trim) {
                text = text.trim();
            }

            return text;
        }
    }
}
