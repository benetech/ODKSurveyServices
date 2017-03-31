package org.opendatakit.demoAndroidlibraryClasses.builder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.opendatakit.demoAndroidlibraryClasses.builder.CsvUtil;
import org.opendatakit.demoAndroidlibraryClasses.builder.ImportRequest;
import org.opendatakit.demoAndroidlibraryClasses.builder.InitializationOutcome;
import org.opendatakit.demoAndroidlibraryClasses.builder.InitializationSupervisor;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbHandle;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.CursorUtils;
import org.opendatakit.demoAndroidlibraryClasses.exception.ServicesAvailabilityException;
import org.opendatakit.demoAndroidlibraryClasses.listener.ImportListener;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;
import org.opendatakit.demoAndroidlibraryClasses.utilities.ODKFileUtils;
import org.opendatakit.survey.R;

public class InitializationUtil {
    private static final String t = "InitializationUtil";
    private Context appContext;
    private InitializationSupervisor supervisor;
    private String appName;
    private String displayTablesProgress;
    private String tableIdInProgress;
    private Map<String, Boolean> importStatus = new TreeMap();

    public InitializationUtil(Context appContext, String appName, InitializationSupervisor supervisor) {
        this.appContext = appContext;
        this.appName = appName;
        this.supervisor = supervisor;
    }

    private InitializationSupervisor getSupervisor() {
        return this.supervisor;
    }

    public InitializationOutcome initialize() {
        InitializationOutcome pendingOutcome = new InitializationOutcome();
        String toolName = this.getSupervisor().getToolName();
        if(toolName != null && !ODKFileUtils.isConfiguredToolApp(this.appName, toolName, this.getSupervisor().getVersionCodeString())) {
            this.getSupervisor().publishProgress(this.appContext.getString(R.string.expansion_unzipping_begins), (String)null);
            this.extractFromRawZip(this.getSupervisor().getSystemZipResourceId(), true, pendingOutcome);
            this.extractFromRawZip(this.getSupervisor().getConfigZipResourceId(), false, pendingOutcome);
            ODKFileUtils.assertConfiguredToolApp(this.appName, toolName, this.getSupervisor().getVersionCodeString());
        }

        try {
            this.updateTableDirs(pendingOutcome);
        } catch (ServicesAvailabilityException var5) {
            WebLogger.getLogger(this.appName).printStackTrace(var5);
            WebLogger.getLogger(this.appName).e("InitializationUtil", "Error accesssing database during table creation sweep");
            pendingOutcome.add(this.appContext.getString(R.string.abort_error_accessing_database));
            return pendingOutcome;
        }

        this.updateFormDirs(pendingOutcome);

        try {
            this.initTables(pendingOutcome);
            return pendingOutcome;
        } catch (ServicesAvailabilityException var4) {
            WebLogger.getLogger(this.appName).printStackTrace(var4);
            WebLogger.getLogger(this.appName).e("InitializationUtil", "Error accesssing database during CSV import sweep");
            pendingOutcome.add(this.appContext.getString(R.string.abort_error_accessing_database));
            return pendingOutcome;
        }
    }

    private final void doActionOnRawZip(int resourceId, boolean overwrite, InitializationOutcome pendingOutcome, InitializationUtil.ZipAction action) {
        String message = null;
        InputStream rawInputStream = null;

        try {
            rawInputStream = this.appContext.getResources().openRawResource(resourceId);
            ZipInputStream e = null;
            ZipEntry entry = null;

            try {
                int e1 = 0;
                e = new ZipInputStream(rawInputStream);

                while((entry = e.getNextEntry()) != null) {
                    message = null;
                    if(this.getSupervisor().isCancelled()) {
                        message = "cancelled";
                        pendingOutcome.add(entry.getName() + " " + message);
                        break;
                    }

                    ++e1;
                    action.doWorker(entry, e, e1, 0L);
                }

                e.close();
                action.done(e1);
            } catch (IOException var34) {
                WebLogger.getLogger(this.appName).printStackTrace(var34);
                pendingOutcome.problemExtractingToolZipContent = true;
                if(var34.getCause() != null) {
                    message = var34.getCause().getMessage();
                } else {
                    message = var34.getMessage();
                }

                if(entry != null) {
                    pendingOutcome.add(entry.getName() + " " + message);
                } else {
                    pendingOutcome.add("Error accessing zipfile resource " + message);
                }
            } finally {
                if(e != null) {
                    try {
                        e.close();
                    } catch (IOException var33) {
                        WebLogger.getLogger(this.appName).printStackTrace(var33);
                        WebLogger.getLogger(this.appName).e("InitializationUtil", "Closing of ZipFile failed: " + var33.toString());
                    }
                }

            }
        } catch (Exception var36) {
            WebLogger.getLogger(this.appName).printStackTrace(var36);
            pendingOutcome.problemExtractingToolZipContent = true;
            if(var36.getCause() != null) {
                message = var36.getCause().getMessage();
            } else {
                message = var36.getMessage();
            }

            pendingOutcome.add("Error accessing zipfile resource " + message);
        } finally {
            if(rawInputStream != null) {
                try {
                    rawInputStream.close();
                } catch (IOException var32) {
                    WebLogger.getLogger(this.appName).printStackTrace(var32);
                }
            }

        }

    }

    public void extractFromRawZip(int resourceId, final boolean overwrite, InitializationOutcome result) {
        final InitializationUtil.ZipEntryCounter countTotal = new InitializationUtil.ZipEntryCounter();
        if(resourceId != -1) {
            this.doActionOnRawZip(resourceId, overwrite, result, countTotal);
            if(countTotal.totalFiles != -1) {
                InitializationUtil.ZipAction worker = new InitializationUtil.ZipAction() {
                    long bytesProcessed = 0L;
                    long lastBytesProcessedThousands = 0L;

                    public void doWorker(ZipEntry entry, ZipInputStream zipInputStream, int indexIntoZip, long size) throws IOException {
                        File tempFile = new File(ODKFileUtils.getAppFolder(appName), entry.getName());
                        String formattedString = appContext.getString(R.string.expansion_unzipping_without_detail, new Object[]{entry.getName(), Integer.valueOf(indexIntoZip), Integer.valueOf(countTotal.totalFiles)});
                        String detail;
                        if(entry.isDirectory()) {
                            detail = appContext.getString(R.string.expansion_create_dir_detail);
                            getSupervisor().publishProgress(formattedString, detail);
                            tempFile.mkdirs();
                        } else if(overwrite || !tempFile.exists()) {
                            short bufferSize = 8192;
                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile, false), bufferSize);

                            int bread;
                            for(byte[] buffer = new byte[bufferSize]; (bread = zipInputStream.read(buffer)) != -1; out.write(buffer, 0, bread)) {
                                this.bytesProcessed += (long)bread;
                                long curThousands = this.bytesProcessed / 1000L;
                                if(curThousands != this.lastBytesProcessedThousands) {
                                    detail = appContext.getString(R.string.expansion_unzipping_detail);
                                    getSupervisor().publishProgress(formattedString, detail);
                                    this.lastBytesProcessedThousands = curThousands;
                                }
                            }

                            out.flush();
                            out.close();
                            detail = appContext.getString(R.string.expansion_unzipping_detail);
                            getSupervisor().publishProgress(formattedString, detail);
                        }

                        WebLogger.getLogger(appName).i("InitializationUtil", "Extracted ZipEntry: " + entry.getName());
                    }

                    public void done(int totalCount) {
                        String completionString = appContext.getString(R.string.expansion_unzipping_complete);
                        getSupervisor().publishProgress(completionString, (String)null);
                    }
                };
                this.doActionOnRawZip(resourceId, overwrite, result, worker);
            }
        }
    }

    private final void removeStaleFormInfo(List<File> discoveredFormDefDirs) {
        Uri formsProviderContentUri = Uri.parse("content://org.opendatakit.provider.forms");
        String completionString = this.appContext.getString(R.string.searching_for_deleted_forms);
        this.getSupervisor().publishProgress(completionString, (String)null);
        WebLogger.getLogger(this.appName).i("InitializationUtil", "removeStaleFormInfo " + this.appName + " begin");
        ArrayList badEntries = new ArrayList();
        Cursor c = null;

        label160: {
            try {
                c = this.appContext.getContentResolver().query(Uri.withAppendedPath(formsProviderContentUri, this.appName), (String[])null, (String)null, (String[])null, (String)null);
                if(c != null) {
                    if(!c.moveToFirst()) {
                        break label160;
                    }

                    while(true) {
                        String i$ = CursorUtils.getIndexAsString(c, c.getColumnIndex("tableId"));
                        String badUri = CursorUtils.getIndexAsString(c, c.getColumnIndex("formId"));
                        Uri e = Uri.withAppendedPath(Uri.withAppendedPath(Uri.withAppendedPath(formsProviderContentUri, this.appName), i$), badUri);
                        String examString = this.appContext.getString(R.string.examining_form, new Object[]{i$, badUri});
                        this.getSupervisor().publishProgress(examString, (String)null);
                        String formDir = ODKFileUtils.getFormFolder(this.appName, i$, badUri);
                        File f = new File(formDir);
                        File formDefJson = new File(f, "formDef.json");
                        if(f.exists() && f.isDirectory() && formDefJson.exists() && formDefJson.isFile()) {
                            String json_md5 = CursorUtils.getIndexAsString(c, c.getColumnIndex("jsonMd5Hash"));
                            String fileMd5 = ODKFileUtils.getMd5Hash(this.appName, formDefJson);
                            if(json_md5.equals(fileMd5)) {
                                discoveredFormDefDirs.remove(f);
                            }
                        } else {
                            badEntries.add(e);
                        }

                        if(!c.moveToNext()) {
                            break label160;
                        }
                    }
                }

                WebLogger.getLogger(this.appName).w("InitializationUtil", "removeStaleFormInfo " + this.appName + " null cursor returned from query.");
            } catch (Exception var20) {
                WebLogger.getLogger(this.appName).e("InitializationUtil", "removeStaleFormInfo " + this.appName + " exception: " + var20.toString());
                WebLogger.getLogger(this.appName).printStackTrace(var20);
                break label160;
            } finally {
                if(c != null && !c.isClosed()) {
                    c.close();
                }

            }

            return;
        }

        Iterator i$1 = badEntries.iterator();

        while(i$1.hasNext()) {
            Uri badUri1 = (Uri)i$1.next();
            WebLogger.getLogger(this.appName).i("InitializationUtil", "removeStaleFormInfo: " + this.appName + " deleting: " + badUri1.toString());

            try {
                this.appContext.getContentResolver().delete(badUri1, (String)null, (String[])null);
            } catch (Exception var19) {
                WebLogger.getLogger(this.appName).e("InitializationUtil", "removeStaleFormInfo " + this.appName + " exception: " + var19.toString());
                WebLogger.getLogger(this.appName).printStackTrace(var19);
            }
        }

        WebLogger.getLogger(this.appName).i("InitializationUtil", "removeStaleFormInfo " + this.appName + " end");
    }

    private final File moveToStaleDirectory(File mediaPath, String baseStaleMediaPath) throws IOException {
        int i = 0;

        File tempMediaPath;
        for(tempMediaPath = new File(baseStaleMediaPath + mediaPath.getName() + "_" + Integer.toString(i)); tempMediaPath.exists(); tempMediaPath = new File(baseStaleMediaPath + mediaPath.getName() + "_" + Integer.toString(i))) {
            ++i;
        }

        FileUtils.moveDirectory(mediaPath, tempMediaPath);
        return tempMediaPath;
    }

    private final void updateTableDirs(InitializationOutcome pendingOutcome) throws ServicesAvailabilityException {
        CsvUtil util = new CsvUtil(this.getSupervisor(), this.appName);
        ODKFileUtils.assertDirectoryStructure(this.appName);
        File tablesDir = new File(ODKFileUtils.getTablesFolder(this.appName));
        File[] tableIdDirs = tablesDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for(int db = 0; db < tableIdDirs.length; ++db) {
            File tableIdDir = tableIdDirs[db];
            String tableId = tableIdDir.getName();
            File definitionCsv = new File(ODKFileUtils.getTableDefinitionCsvFile(this.appName, tableId));
            File propertiesCsv = new File(ODKFileUtils.getTablePropertiesCsvFile(this.appName, tableId));
            if(definitionCsv.exists() && definitionCsv.isFile() && propertiesCsv.exists() && propertiesCsv.isFile()) {
                String formattedString = this.appContext.getString(R.string.scanning_for_table_definitions, new Object[]{tableId, Integer.valueOf(db + 1), Integer.valueOf(tableIdDirs.length)});
                String detail = this.appContext.getString(R.string.processing_file);
                this.displayTablesProgress = formattedString;
                this.tableIdInProgress = tableId;
                this.getSupervisor().publishProgress(formattedString, detail);

                try {
                    util.updateTablePropertiesFromCsv(tableId);
                } catch (IOException var16) {
                    pendingOutcome.add(this.appContext.getString(R.string.defining_tableid_error, new Object[]{tableId}));
                    WebLogger.getLogger(this.appName).e("InitializationUtil", "Unexpected error during update from csv");
                }
            }
        }

        DbHandle var18 = null;

        try {
            var18 = this.getSupervisor().getDatabase().openDatabase(this.appName);
            this.getSupervisor().getDatabase().deleteAppAndTableLevelManifestSyncETags(this.appName, var18);
        } finally {
            if(var18 != null) {
                this.getSupervisor().getDatabase().closeDatabase(this.appName, var18);
            }

        }

    }

    private final void initTables(final InitializationOutcome pendingOutcome) throws ServicesAvailabilityException {
        String EMPTY_STRING = "";
        String SPACE = " ";
        String TOP_LEVEL_KEY_TABLE_KEYS = "table_keys";
        String COMMA = ",";
        String KEY_SUFFIX_CSV_FILENAME = ".filename";
        HashMap mKeyToFileMap = new HashMap();
        File init = new File(ODKFileUtils.getTablesInitializationFile(this.appName));
        File completedFile = new File(ODKFileUtils.getTablesInitializationCompleteMarkerFile(this.appName));
        if(init.exists()) {
            boolean processFile = false;
            String table_keys;
            if(!completedFile.exists()) {
                processFile = true;
            } else {
                String prop = ODKFileUtils.getMd5Hash(this.appName, init);
                table_keys = ODKFileUtils.getMd5Hash(this.appName, completedFile);
                processFile = !prop.equals(table_keys);
            }

            if(processFile) {
                Properties var33 = new Properties();

                try {
                    var33.load(new FileInputStream(init));
                } catch (IOException var32) {
                    WebLogger.getLogger(this.appName).printStackTrace(var32);
                    pendingOutcome.add(this.appContext.getString(R.string.poorly_formatted_init_file));
                    pendingOutcome.problemImportingAssetCsvContent = true;
                    return;
                }

                try {
                    FileUtils.copyFile(init, completedFile);
                } catch (IOException var31) {
                    WebLogger.getLogger(this.appName).printStackTrace(var31);
                }

                if(var33 != null) {
                    table_keys = var33.getProperty("table_keys");
                    if(table_keys == null) {
                        pendingOutcome.add(this.appContext.getString(R.string.poorly_formatted_init_file));
                        pendingOutcome.problemImportingAssetCsvContent = true;
                        return;
                    }

                    String[] keys = table_keys.replace(" ", "").split(",");
                    int fileCount = keys.length;
                    int curFileCount = 0;
                    String detail = this.appContext.getString(R.string.processing_file);
                    CsvUtil cu = new CsvUtil(this.getSupervisor(), this.appName);
                    String[] arr$ = keys;
                    int len$ = keys.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        String key = arr$[i$];
                        ++curFileCount;
                        String filename = var33.getProperty(key + ".filename");
                        this.importStatus.put(key, Boolean.valueOf(false));
                        File file = new File(ODKFileUtils.getAppFolder(this.appName), filename);
                        mKeyToFileMap.put(key, filename);
                        String formattedString;
                        if(!file.exists()) {
                            pendingOutcome.assetsCsvFileNotFoundSet.add(key);
                            WebLogger.getLogger(this.appName).i("InitializationUtil", "putting in file not found map true: " + key);
                            formattedString = this.appContext.getString(R.string.csv_file_not_found, new Object[]{filename});
                            pendingOutcome.add(formattedString);
                        } else {
                            formattedString = this.appContext.getString(R.string.importing_file_without_detail, new Object[]{Integer.valueOf(curFileCount), Integer.valueOf(fileCount), filename});
                            this.displayTablesProgress = formattedString;
                            this.getSupervisor().publishProgress(formattedString, detail);
                            ImportRequest request = null;
                            String assetsCsvDirPath = ODKFileUtils.asRelativePath(this.appName, new File(ODKFileUtils.getAssetsCsvFolder(this.appName)));
                            if(filename.startsWith(assetsCsvDirPath)) {
                                String csvFilename = filename.substring(assetsCsvDirPath.length() + 1);
                                String[] terms = csvFilename.split("\\.");
                                String success;
                                String fileQualifier;
                                if(terms.length == 2 && terms[1].equals("csv")) {
                                    success = terms[0];
                                    fileQualifier = null;
                                    request = new ImportRequest(success, fileQualifier);
                                } else if(terms.length == 3 && terms[1].equals("properties") && terms[2].equals("csv")) {
                                    success = terms[0];
                                    fileQualifier = null;
                                    request = new ImportRequest(success, fileQualifier);
                                } else if(terms.length == 3 && terms[2].equals("csv")) {
                                    success = terms[0];
                                    fileQualifier = terms[1];
                                    request = new ImportRequest(success, fileQualifier);
                                } else if(terms.length == 4 && terms[2].equals("properties") && terms[3].equals("csv")) {
                                    success = terms[0];
                                    fileQualifier = terms[1];
                                    request = new ImportRequest(success, fileQualifier);
                                }

                                if(request != null) {
                                    this.tableIdInProgress = request.getTableId();
                                    boolean var34 = false;
                                    var34 = cu.importSeparable(new ImportListener() {
                                        public void updateProgressDetail(String progressDetailString) {
                                            getSupervisor().publishProgress(displayTablesProgress, progressDetailString);
                                        }

                                        public void importComplete(boolean outcome) {
                                            if(outcome) {
                                                pendingOutcome.add(appContext.getString(R.string.import_csv_success, new Object[]{tableIdInProgress}));
                                            } else {
                                                pendingOutcome.add(appContext.getString(R.string.import_csv_failure, new Object[]{tableIdInProgress}));
                                            }

                                            pendingOutcome.problemImportingAssetCsvContent = pendingOutcome.problemImportingAssetCsvContent || !outcome;
                                        }
                                    }, request.getTableId(), request.getFileQualifier(), true);
                                    this.importStatus.put(key, Boolean.valueOf(var34));
                                    if(var34) {
                                        detail = this.appContext.getString(R.string.import_success);
                                        this.getSupervisor().publishProgress(this.appContext.getString(R.string.importing_file_without_detail, new Object[]{Integer.valueOf(curFileCount), Integer.valueOf(fileCount), filename}), detail);
                                    }
                                }
                            }

                            if(request == null) {
                                pendingOutcome.add(this.appContext.getString(R.string.poorly_formatted_init_file));
                                pendingOutcome.problemImportingAssetCsvContent = true;
                                return;
                            }
                        }
                    }
                }

            }
        }
    }

    private final void updateFormDirs(InitializationOutcome pendingOutcome) {
        String completionString = this.appContext.getString(R.string.searching_for_form_defs);
        this.getSupervisor().publishProgress(completionString, (String)null);
        File tablesDir = new File(ODKFileUtils.getTablesFolder(this.appName));
        File[] tableIdDirs = tablesDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        ArrayList formDirs = new ArrayList();
        File[] i = tableIdDirs;
        int formDir = tableIdDirs.length;

        String examString;
        for(int formId = 0; formId < formDir; ++formId) {
            File tableId = i[formId];
            examString = tableId.getName();
            File formDir1 = new File(ODKFileUtils.getFormsFolder(this.appName, examString));
            File[] formIdDirs = formDir1.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    File formDef = new File(pathname, "formDef.json");
                    return pathname.isDirectory() && formDef.exists() && formDef.isFile();
                }
            });
            if(formIdDirs != null) {
                formDirs.addAll(Arrays.asList(formIdDirs));
            }
        }

        this.removeStaleFormInfo(formDirs);

        for(int var13 = 0; var13 < formDirs.size(); ++var13) {
            File var14 = (File)formDirs.get(var13);
            String var15 = var14.getName();
            String var16 = var14.getParentFile().getParentFile().getName();
            WebLogger.getLogger(this.appName).i("InitializationUtil", "updateFormInfo: form: " + var14.getAbsolutePath());
            examString = this.appContext.getString(R.string.updating_form_information, new Object[]{var14.getName(), Integer.valueOf(var13 + 1), Integer.valueOf(formDirs.size())});
            this.getSupervisor().publishProgress(examString, (String)null);
            this.updateFormDir(var16, var15, var14, ODKFileUtils.getPendingDeletionTablesFolder(this.appName) + File.separator, pendingOutcome);
        }

    }

    private final void updateFormDir(String tableId, String formId, File formDir, String baseStaleMediaPath, InitializationOutcome pendingOutcome) {
        Uri formsProviderContentUri = Uri.parse("content://org.opendatakit.provider.forms");
        String formDirectoryPath = formDir.getAbsolutePath();
        WebLogger.getLogger(this.appName).i("InitializationUtil", "updateFormDir: " + formDirectoryPath);
        String successMessage = this.appContext.getString(R.string.form_register_success, new Object[]{tableId, formId});
        String failureMessage = this.appContext.getString(R.string.form_register_failure, new Object[]{tableId, formId});
        Cursor c = null;

        label174: {
            try {
                String e1 = "tableId=? AND formId=?";
                String[] e2 = new String[]{tableId, formId};
                c = this.appContext.getContentResolver().query(Uri.withAppendedPath(formsProviderContentUri, this.appName), (String[])null, e1, e2, (String)null);
                if(c == null) {
                    WebLogger.getLogger(this.appName).w("InitializationUtil", "updateFormDir: " + formDirectoryPath + " null cursor -- cannot update!");
                    pendingOutcome.add(failureMessage);
                    pendingOutcome.problemDefiningForms = true;
                    return;
                }

                if(c.getCount() > 1) {
                    c.close();
                    WebLogger.getLogger(this.appName).w("InitializationUtil", "updateFormDir: " + formDirectoryPath + " multiple records from cursor -- delete all and restore!");
                    File cv = this.moveToStaleDirectory(formDir, baseStaleMediaPath);
                    this.appContext.getContentResolver().delete(Uri.withAppendedPath(formsProviderContentUri, this.appName), e1, e2);
                    FileUtils.moveDirectory(cv, formDir);
                    ContentValues cv1 = new ContentValues();
                    cv1.put("tableId", tableId);
                    cv1.put("formId", formId);
                    this.appContext.getContentResolver().insert(Uri.withAppendedPath(formsProviderContentUri, this.appName), cv1);
                } else {
                    ContentValues cv2;
                    if(c.getCount() == 1) {
                        c.close();
                        cv2 = new ContentValues();
                        cv2.put("tableId", tableId);
                        cv2.put("formId", formId);
                        this.appContext.getContentResolver().update(Uri.withAppendedPath(formsProviderContentUri, this.appName), cv2, (String)null, (String[])null);
                    } else if(c.getCount() == 0) {
                        c.close();
                        cv2 = new ContentValues();
                        cv2.put("tableId", tableId);
                        cv2.put("formId", formId);
                        this.appContext.getContentResolver().insert(Uri.withAppendedPath(formsProviderContentUri, this.appName), cv2);
                    }
                }
                break label174;
            } catch (IOException var22) {
                WebLogger.getLogger(this.appName).printStackTrace(var22);
                WebLogger.getLogger(this.appName).e("InitializationUtil", "updateFormDir: " + formDirectoryPath + " exception: " + var22.toString());
                pendingOutcome.add(failureMessage);
                pendingOutcome.problemDefiningForms = true;
            } catch (IllegalArgumentException var23) {
                IllegalArgumentException e = var23;
                WebLogger.getLogger(this.appName).printStackTrace(var23);
                WebLogger.getLogger(this.appName).e("InitializationUtil", "updateFormDir: " + formDirectoryPath + " exception: " + var23.toString());

                try {
                    FileUtils.deleteDirectory(formDir);
                    WebLogger.getLogger(this.appName).i("InitializationUtil", "updateFormDir: " + formDirectoryPath + " Removing -- unable to parse formDef file: " + e.toString());
                } catch (IOException var21) {
                    WebLogger.getLogger(this.appName).printStackTrace(var21);
                    WebLogger.getLogger(this.appName).i("InitializationUtil", "updateFormDir: " + formDirectoryPath + " Removing -- unable to delete form directory: " + formDir.getName() + " error: " + var23.toString());
                }

                pendingOutcome.add(failureMessage);
                pendingOutcome.problemDefiningForms = true;
                return;
            } catch (Exception var24) {
                WebLogger.getLogger(this.appName).printStackTrace(var24);
                WebLogger.getLogger(this.appName).e("InitializationUtil", "updateFormDir: " + formDirectoryPath + " exception: " + var24.toString());
                pendingOutcome.add(failureMessage);
                pendingOutcome.problemDefiningForms = true;
                return;
            } finally {
                if(c != null && !c.isClosed()) {
                    c.close();
                }

            }

            return;
        }

        pendingOutcome.add(successMessage);
    }

    static class ZipEntryCounter implements InitializationUtil.ZipAction {
        int totalFiles = -1;

        ZipEntryCounter() {
        }

        public void doWorker(ZipEntry entry, ZipInputStream zipInputStream, int indexIntoZip, long size) {
        }

        public void done(int totalCount) {
            this.totalFiles = totalCount;
        }
    }

    interface ZipAction {
        void doWorker(ZipEntry var1, ZipInputStream var2, int var3, long var4) throws FileNotFoundException, IOException;

        void done(int var1);
    }
}
