package org.opendatakit.demoAndroidlibraryClasses.utilities;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.opendatakit.demoAndroidlibraryClasses.database.utilities.CursorUtils;
import org.opendatakit.demoAndroidlibraryClasses.logging.WebLogger;

public class MediaUtils {
    private static final String t = "MediaUtils";

    private MediaUtils() {
    }

    private static String escapePath(String path) {
        String ep = path.replaceAll("\\!", "!!");
        ep = ep.replaceAll("_", "!_");
        ep = ep.replaceAll("%", "!%");
        return ep;
    }

    public static Uri getImageUriFromMediaProvider(Context ctxt, String imageFile) {
        String selection = "_data=?";
        String[] selectArgs = new String[]{imageFile};
        String[] projection = new String[]{"_id"};
        Cursor c = null;

        String id;
        try {
            c = ctxt.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, selection, selectArgs, (String)null);
            if(c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = CursorUtils.getIndexAsString(c, c.getColumnIndex("_id"));
                Uri var7 = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id);
                return var7;
            }

            id = null;
        } finally {
            if(c != null) {
                c.close();
            }

        }

        return Uri.parse(id);
    }

    public static int deleteImageFileFromMediaProvider(Context ctxt, String appName, String imageFile) {
        if(imageFile == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor imageCursor = null;

            try {
                String f = "_data=?";
                String[] selectArgs = new String[]{imageFile};
                String[] projection = new String[]{"_id"};
                imageCursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, f, selectArgs, (String)null);
                if(imageCursor != null && imageCursor.getCount() > 0) {
                    imageCursor.moveToFirst();
                    ArrayList imagesToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(imageCursor, imageCursor.getColumnIndex("_id"));
                        imagesToDelete.add(Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, i$));
                    } while(imageCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = imagesToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(imageCursor != null) {
                    imageCursor.close();
                }

            }

            File f1 = new File(imageFile);
            if(f1.exists()) {
                f1.delete();
            }

            return count;
        }
    }

    public static int deleteImagesInFolderFromMediaProvider(Context ctxt, String appName, File folder) {
        if(folder == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor imageCursor = null;

            try {
                String e = "_data like ? escape \'!\'";
                String[] selectArgs = new String[]{escapePath(folder.getAbsolutePath())};
                String[] projection = new String[]{"_id"};
                imageCursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, e, selectArgs, (String)null);
                if(imageCursor != null && imageCursor.getCount() > 0) {
                    imageCursor.moveToFirst();
                    ArrayList imagesToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(imageCursor, imageCursor.getColumnIndex("_id"));
                        imagesToDelete.add(Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, i$));
                    } while(imageCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = imagesToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(imageCursor != null) {
                    imageCursor.close();
                }

            }

            return count;
        }
    }

    public static Uri getAudioUriFromMediaProvider(Context ctxt, String audioFile) {
        String selection = "_data=?";
        String[] selectArgs = new String[]{audioFile};
        String[] projection = new String[]{"_id"};
        Cursor c = null;

        Uri var7;
        try {
            c = ctxt.getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, selectArgs, (String)null);
            String id;
            if(c == null || c.getCount() <= 0) {
                id = null;
                return Uri.parse(id);
            }

            c.moveToFirst();
            id = CursorUtils.getIndexAsString(c, c.getColumnIndex("_id"));
            var7 = Uri.withAppendedPath(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        } finally {
            if(c != null) {
                c.close();
            }

        }

        return var7;
    }

    public static int deleteAudioFileFromMediaProvider(Context ctxt, String appName, String audioFile) {
        if(audioFile == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor audioCursor = null;

            try {
                String f = "_data=?";
                String[] selectArgs = new String[]{audioFile};
                String[] projection = new String[]{"_id"};
                audioCursor = cr.query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, f, selectArgs, (String)null);
                if(audioCursor != null && audioCursor.getCount() > 0) {
                    audioCursor.moveToFirst();
                    ArrayList audioToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(audioCursor, audioCursor.getColumnIndex("_id"));
                        audioToDelete.add(Uri.withAppendedPath(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, i$));
                    } while(audioCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = audioToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(audioCursor != null) {
                    audioCursor.close();
                }

            }

            File f1 = new File(audioFile);
            if(f1.exists()) {
                f1.delete();
            }

            return count;
        }
    }

    public static int deleteAudioInFolderFromMediaProvider(Context ctxt, String appName, File folder) {
        if(folder == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor audioCursor = null;

            try {
                String e = "_data like ? escape \'!\'";
                String[] selectArgs = new String[]{escapePath(folder.getAbsolutePath())};
                String[] projection = new String[]{"_id"};
                audioCursor = cr.query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, e, selectArgs, (String)null);
                if(audioCursor != null && audioCursor.getCount() > 0) {
                    audioCursor.moveToFirst();
                    ArrayList audioToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(audioCursor, audioCursor.getColumnIndex("_id"));
                        audioToDelete.add(Uri.withAppendedPath(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, i$));
                    } while(audioCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = audioToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(audioCursor != null) {
                    audioCursor.close();
                }

            }

            return count;
        }
    }

    public static Uri getVideoUriFromMediaProvider(Context ctxt, String videoFile) {
        String selection = "_data=?";
        String[] selectArgs = new String[]{videoFile};
        String[] projection = new String[]{"_id"};
        Cursor c = null;

        String id;
        try {
            c = ctxt.getContentResolver().query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, selection, selectArgs, (String)null);
            if(c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = CursorUtils.getIndexAsString(c, c.getColumnIndex("_id"));
                Uri var7 = Uri.withAppendedPath(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                return var7;
            }

            id = null;
        } finally {
            if(c != null) {
                c.close();
            }

        }

        return Uri.parse(id);
    }

    public static int deleteVideoFileFromMediaProvider(Context ctxt, String appName, String videoFile) {
        if(videoFile == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor videoCursor = null;

            try {
                String f = "_data=?";
                String[] selectArgs = new String[]{videoFile};
                String[] projection = new String[]{"_id"};
                videoCursor = cr.query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, f, selectArgs, (String)null);
                if(videoCursor != null && videoCursor.getCount() > 0) {
                    videoCursor.moveToFirst();
                    ArrayList videoToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(videoCursor, videoCursor.getColumnIndex("_id"));
                        videoToDelete.add(Uri.withAppendedPath(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, i$));
                    } while(videoCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = videoToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(videoCursor != null) {
                    videoCursor.close();
                }

            }

            File f1 = new File(videoFile);
            if(f1.exists()) {
                f1.delete();
            }

            return count;
        }
    }

    public static int deleteVideoInFolderFromMediaProvider(Context ctxt, String appName, File folder) {
        if(folder == null) {
            return 0;
        } else {
            ContentResolver cr = ctxt.getContentResolver();
            int count = 0;
            Cursor videoCursor = null;

            try {
                String e = "_data like ? escape \'!\'";
                String[] selectArgs = new String[]{escapePath(folder.getAbsolutePath())};
                String[] projection = new String[]{"_id"};
                videoCursor = cr.query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, e, selectArgs, (String)null);
                if(videoCursor != null && videoCursor.getCount() > 0) {
                    videoCursor.moveToFirst();
                    ArrayList videoToDelete = new ArrayList();

                    do {
                        String i$ = CursorUtils.getIndexAsString(videoCursor, videoCursor.getColumnIndex("_id"));
                        videoToDelete.add(Uri.withAppendedPath(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, i$));
                    } while(videoCursor.moveToNext());

                    Uri uri;
                    for(Iterator i$1 = videoToDelete.iterator(); i$1.hasNext(); count += cr.delete(uri, (String)null, (String[])null)) {
                        uri = (Uri)i$1.next();
                        WebLogger.getLogger(appName).i("MediaUtils", "attempting to delete: " + uri);
                    }
                }
            } catch (Exception var15) {
                WebLogger.getLogger(appName).e("MediaUtils", var15.toString());
            } finally {
                if(videoCursor != null) {
                    videoCursor.close();
                }

            }

            return count;
        }
    }

    @SuppressLint({"NewApi"})
    public static String getPathFromUri(Context ctxt, Uri uri, String pathKey) {
        if(VERSION.SDK_INT >= 19) {
            return getPath(ctxt, uri);
        } else if(uri.toString().startsWith("file")) {
            return uri.toString().substring(7);
        } else {
            String[] projection = new String[]{pathKey};
            Cursor c = null;

            String column_index1;
            try {
                c = ctxt.getContentResolver().query(uri, projection, (String)null, (String[])null, (String)null);
                String path = null;
                if(c != null && c.getCount() > 0) {
                    int column_index = c.getColumnIndexOrThrow(pathKey);
                    c.moveToFirst();
                    path = CursorUtils.getIndexAsString(c, column_index);
                }

                column_index1 = path;
            } finally {
                if(c != null) {
                    c.close();
                }

            }

            return column_index1;
        }
    }

    @SuppressLint({"NewApi"})
    public static String getPath(Context context, Uri uri) {
        boolean isKitKat = VERSION.SDK_INT >= 19;
        if(isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String docId;
            String[] split;
            String type;
            if(isExternalStorageDocument(uri)) {
                docId = DocumentsContract.getDocumentId(uri);
                split = docId.split(":");
                type = split[0];
                if("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                if(isDownloadsDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Uri split1 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId).longValue());
                    return getDataColumn(context, split1, (String)null, (String[])null);
                }

                if(isMediaDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    split = docId.split(":");
                    type = split[0];
                    Uri contentUri = null;
                    if("image".equals(type)) {
                        contentUri = Media.EXTERNAL_CONTENT_URI;
                    } else if("video".equals(type)) {
                        contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if("audio".equals(type)) {
                        contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, "_id=?", selectionArgs);
                }
            }
        } else {
            if("content".equalsIgnoreCase(uri.getScheme())) {
                if(isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }

                return getDataColumn(context, uri, (String)null, (String[])null);
            }

            if("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        String[] projection = new String[]{"_data"};

        String var8;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
            if(cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            int column_index = cursor.getColumnIndexOrThrow("_data");
            var8 = CursorUtils.getIndexAsString(cursor, column_index);
        } finally {
            if(cursor != null) {
                cursor.close();
            }

        }

        return var8;
    }
}
