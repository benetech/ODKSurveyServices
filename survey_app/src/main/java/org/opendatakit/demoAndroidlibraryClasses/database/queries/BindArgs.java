package org.opendatakit.demoAndroidlibraryClasses.database.queries;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class BindArgs implements Parcelable, Serializable {
    public final Object[] bindArgs;
    public static final Creator<BindArgs> CREATOR = new Creator() {
        public BindArgs createFromParcel(Parcel in) {
            return new BindArgs(in);
        }

        public BindArgs[] newArray(int size) {
            return new BindArgs[size];
        }
    };

    public BindArgs() {
        this.bindArgs = null;
    }

    public BindArgs(Object[] args) {
        this.bindArgs = args;
        if(args != null) {
            for(int i = 0; i < args.length; ++i) {
                Object o = args[i];
                if(o != null && !(o instanceof String) && !(o instanceof Integer) && !(o instanceof Boolean) && !(o instanceof Double) && !(o instanceof Float) && !(o instanceof Long)) {
                    throw new IllegalArgumentException("bind args must be a primitive type");
                }
            }
        }

    }

    private static void marshallObject(Parcel out, Object toMarshall) {
        if(toMarshall == null) {
            out.writeInt(0);
        } else if(toMarshall instanceof String) {
            out.writeInt(1);
            out.writeString((String)toMarshall);
        } else if(toMarshall instanceof Integer) {
            out.writeInt(2);
            out.writeInt(((Integer)toMarshall).intValue());
        } else if(toMarshall instanceof Boolean) {
            if(((Boolean)toMarshall).booleanValue()) {
                out.writeInt(3);
            } else {
                out.writeInt(4);
            }
        } else if(toMarshall instanceof Double) {
            out.writeInt(5);
            out.writeDouble(((Double)toMarshall).doubleValue());
        } else if(toMarshall instanceof Float) {
            out.writeInt(6);
            out.writeFloat(((Float)toMarshall).floatValue());
        } else {
            if(!(toMarshall instanceof Long)) {
                throw new IllegalStateException("should have been prevented in constructor");
            }

            out.writeInt(7);
            out.writeLong(((Long)toMarshall).longValue());
        }

    }

    private static Object unmarshallObject(Parcel in) {
        int dataType = in.readInt();
        switch(dataType) {
            case 0:
                return null;
            case 1:
                return in.readString();
            case 2:
                return Integer.valueOf(in.readInt());
            case 3:
                return Boolean.TRUE;
            case 4:
                return Boolean.FALSE;
            case 5:
                return Double.valueOf(in.readDouble());
            case 6:
                return Float.valueOf(in.readFloat());
            case 7:
                return Long.valueOf(in.readLong());
            default:
                throw new IllegalStateException("should have been prevented in constructor");
        }
    }

    protected BindArgs(Parcel in) {
        int dataCount = in.readInt();
        if(dataCount < 0) {
            this.bindArgs = null;
        } else {
            Object[] result = new Object[dataCount];

            for(int i = 0; i < dataCount; ++i) {
                result[i] = unmarshallObject(in);
            }

            this.bindArgs = result;
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        if(this.bindArgs == null) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(this.bindArgs.length);

            for(int i = 0; i < this.bindArgs.length; ++i) {
                marshallObject(dest, this.bindArgs[i]);
            }
        }

    }

    public int describeContents() {
        return 0;
    }
}
