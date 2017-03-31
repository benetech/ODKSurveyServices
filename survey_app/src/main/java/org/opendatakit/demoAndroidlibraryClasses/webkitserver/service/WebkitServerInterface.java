package org.opendatakit.demoAndroidlibraryClasses.webkitserver.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface WebkitServerInterface extends IInterface {
    boolean restart() throws RemoteException;

    public abstract static class Stub extends Binder implements WebkitServerInterface {
        private static final String DESCRIPTOR = "org.opendatakit.webkitserver.service.WebkitServerInterface";
        static final int TRANSACTION_restart = 1;

        public Stub() {
            this.attachInterface(this, "org.opendatakit.webkitserver.service.WebkitServerInterface");
        }

        public static WebkitServerInterface asInterface(IBinder obj) {
            if(obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("org.opendatakit.webkitserver.service.WebkitServerInterface");
                return (WebkitServerInterface)(iin != null && iin instanceof WebkitServerInterface?(WebkitServerInterface)iin:new WebkitServerInterface.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch(code) {
                case 1:
                    data.enforceInterface("org.opendatakit.webkitserver.service.WebkitServerInterface");
                    boolean _result = this.restart();
                    reply.writeNoException();
                    reply.writeInt(_result?1:0);
                    return true;
                case 1598968902:
                    reply.writeString("org.opendatakit.webkitserver.service.WebkitServerInterface");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements WebkitServerInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "org.opendatakit.webkitserver.service.WebkitServerInterface";
            }

            public boolean restart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.webkitserver.service.WebkitServerInterface");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }
        }
    }
}
