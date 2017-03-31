package org.opendatakit.demoAndroidlibraryClasses.database.utilities;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.opendatakit.demoAndroidlibraryClasses.database.service.DbChunk;

public final class DbChunkUtil {
    private static final String TAG = DbChunkUtil.class.getSimpleName();

    private DbChunkUtil() {
        throw new IllegalStateException("Never Instantiate this static class");
    }

    private static DbChunk createChunk(byte[] sourceData, int dataIndex, int chunkSize, DbChunk prevChunk) {
        UUID chunkID = UUID.randomUUID();
        byte[] chunkData = new byte[chunkSize];
        System.arraycopy(sourceData, dataIndex, chunkData, 0, chunkSize);
        DbChunk chunk = new DbChunk(chunkData, chunkID);
        if(prevChunk != null) {
            prevChunk.setNextID(chunkID);
        }

        return chunk;
    }

    public static List<DbChunk> convertToChunks(Parcelable parcelable, int chunkSize) {
        if(parcelable != null && chunkSize > 0) {
            Parcel parcel = Parcel.obtain();
            parcelable.writeToParcel(parcel, 0);
            byte[] bytes = parcel.marshall();
            parcel.recycle();
            return createChunkList(bytes, chunkSize);
        } else {
            Log.w(TAG, "convertToChunks: Invalid input. Empty chunk list returned");
            return null;
        }
    }

    public static List<DbChunk> convertToChunks(Serializable serializable, int chunkSize) throws IOException {
        if(serializable != null && chunkSize > 0) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(serializable);
            byte[] bytes = bos.toByteArray();
            out.close();
            return createChunkList(bytes, chunkSize);
        } else {
            Log.w(TAG, "convertToChunks: Invalid input. Empty chunk list returned");
            return null;
        }
    }

    private static List<DbChunk> createChunkList(byte[] bytes, int chunkSize) {
        int dataIndex = 0;
        DbChunk currChunk = null;

        LinkedList chunkList;
        for(chunkList = new LinkedList(); dataIndex + chunkSize < bytes.length; dataIndex += chunkSize) {
            currChunk = createChunk(bytes, dataIndex, chunkSize, currChunk);
            chunkList.add(currChunk);
        }

        int remainderSize = bytes.length - dataIndex;
        currChunk = createChunk(bytes, dataIndex, remainderSize, currChunk);
        chunkList.add(currChunk);
        return chunkList;
    }

    public static <T> T rebuildFromChunks(List<DbChunk> chunks, Creator<T> creator) {
        if(chunks != null && chunks.size() != 0 && creator != null) {
            byte[] data = getData(chunks);
            Parcel parcel = Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            return creator.createFromParcel(parcel);
        } else {
            Log.w(TAG, "rebuildFromChunks: Invalid input. Null returned");
            return null;
        }
    }

    public static <T> T rebuildFromChunks(List<DbChunk> chunks, Class<T> serializable) throws IOException, ClassNotFoundException {
        if(chunks != null && chunks.size() != 0 && serializable != null) {
            byte[] data = getData(chunks);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            Object result = in.readObject();
            bis.close();
            in.close();
            return (T) result;
        } else {
            Log.w(TAG, "rebuildFromChunks: Invalid input. Null returned");
            return null;
        }
    }

    private static byte[] getData(List<DbChunk> chunks) {
        int dataSize = 0;

        Iterator iterator;
        for(iterator = chunks.iterator(); iterator.hasNext(); dataSize += ((DbChunk)iterator.next()).getData().length) {
            ;
        }

        int dataIndex = 0;
        byte[] data = new byte[dataSize];

        byte[] currChunkData;
        for(iterator = chunks.iterator(); iterator.hasNext(); dataIndex += currChunkData.length) {
            currChunkData = ((DbChunk)iterator.next()).getData();
            System.arraycopy(currChunkData, 0, data, dataIndex, currChunkData.length);
        }

        return data;
    }
}
