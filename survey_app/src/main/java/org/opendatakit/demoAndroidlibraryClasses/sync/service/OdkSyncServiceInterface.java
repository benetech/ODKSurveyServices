package org.opendatakit.demoAndroidlibraryClasses.sync.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncAttachmentState;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncOverallResult;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncProgressEvent;
import org.opendatakit.demoAndroidlibraryClasses.sync.service.SyncStatus;

public interface OdkSyncServiceInterface extends IInterface {
    boolean verifyServerSettings(String var1) throws RemoteException;

    SyncStatus getSyncStatus(String var1) throws RemoteException;

    SyncProgressEvent getSyncProgressEvent(String var1) throws RemoteException;

    SyncOverallResult getSyncResult(String var1) throws RemoteException;

    boolean synchronizeWithServer(String var1, SyncAttachmentState var2) throws RemoteException;

    boolean resetServer(String var1, SyncAttachmentState var2) throws RemoteException;

    boolean clearAppSynchronizer(String var1) throws RemoteException;

    public abstract static class Stub extends Binder implements OdkSyncServiceInterface {
        private static final String DESCRIPTOR = "org.opendatakit.sync.service.OdkSyncServiceInterface";
        static final int TRANSACTION_verifyServerSettings = 1;
        static final int TRANSACTION_getSyncStatus = 2;
        static final int TRANSACTION_getSyncProgressEvent = 3;
        static final int TRANSACTION_getSyncResult = 4;
        static final int TRANSACTION_synchronizeWithServer = 5;
        static final int TRANSACTION_resetServer = 6;
        static final int TRANSACTION_clearAppSynchronizer = 7;

        public Stub() {
            this.attachInterface(this, "org.opendatakit.sync.service.OdkSyncServiceInterface");
        }

        public static OdkSyncServiceInterface asInterface(IBinder obj) {
            if(obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                return (OdkSyncServiceInterface)(iin != null && iin instanceof OdkSyncServiceInterface?(OdkSyncServiceInterface)iin:new OdkSyncServiceInterface.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _arg0;
            boolean _result;
            boolean _result1;
            SyncAttachmentState _result2;
            switch(code) {
                case 1:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    _result = this.verifyServerSettings(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result?1:0);
                    return true;
                case 2:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    SyncStatus _result5 = this.getSyncStatus(_arg0);
                    reply.writeNoException();
                    if(_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 3:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    SyncProgressEvent _result4 = this.getSyncProgressEvent(_arg0);
                    reply.writeNoException();
                    if(_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 4:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    SyncOverallResult _result3 = this.getSyncResult(_arg0);
                    reply.writeNoException();
                    if(_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 5:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (SyncAttachmentState)SyncAttachmentState.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _result1 = this.synchronizeWithServer(_arg0, _result2);
                    reply.writeNoException();
                    reply.writeInt(_result1?1:0);
                    return true;
                case 6:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    if(0 != data.readInt()) {
                        _result2 = (SyncAttachmentState)SyncAttachmentState.CREATOR.createFromParcel(data);
                    } else {
                        _result2 = null;
                    }

                    _result1 = this.resetServer(_arg0, _result2);
                    reply.writeNoException();
                    reply.writeInt(_result1?1:0);
                    return true;
                case 7:
                    data.enforceInterface("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _arg0 = data.readString();
                    _result = this.clearAppSynchronizer(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result?1:0);
                    return true;
                case 1598968902:
                    reply.writeString("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements OdkSyncServiceInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "org.opendatakit.sync.service.OdkSyncServiceInterface";
            }

            public boolean verifyServerSettings(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public SyncStatus getSyncStatus(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                SyncStatus _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (SyncStatus)SyncStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public SyncProgressEvent getSyncProgressEvent(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                SyncProgressEvent _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (SyncProgressEvent)SyncProgressEvent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public SyncOverallResult getSyncResult(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                SyncOverallResult _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if(0 != _reply.readInt()) {
                        _result = (SyncOverallResult)SyncOverallResult.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean synchronizeWithServer(String appName, SyncAttachmentState syncAttachments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    if(syncAttachments != null) {
                        _data.writeInt(1);
                        syncAttachments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean resetServer(String appName, SyncAttachmentState syncAttachments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    if(syncAttachments != null) {
                        _data.writeInt(1);
                        syncAttachments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearAppSynchronizer(String appName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("org.opendatakit.sync.service.OdkSyncServiceInterface");
                    _data.writeString(appName);
                    this.mRemote.transact(7, _data, _reply, 0);
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
