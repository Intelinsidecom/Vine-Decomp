package io.realm.internal;

import io.realm.exceptions.RealmIOException;
import java.io.Closeable;
import java.io.IOError;

/* loaded from: classes.dex */
public class SharedGroup implements Closeable {
    private boolean activeTransaction;
    private final Context context;
    private boolean implicitTransactionsEnabled;
    private long nativePtr;
    private long nativeReplicationPtr;
    private final String path;

    private native long createNativeWithImplicitTransactions(long j, int i, byte[] bArr);

    private native void nativeAdvanceRead(long j, long j2);

    private native void nativeAdvanceReadToVersion(long j, long j2, long j3, long j4);

    private native long nativeBeginImplicit(long j);

    private native long nativeBeginRead(long j);

    private native long nativeBeginWrite(long j);

    protected static native void nativeClose(long j);

    private native void nativeCloseReplication(long j);

    private native void nativeCommit(long j);

    private native void nativeCommitAndContinueAsRead(long j);

    private native boolean nativeCompact(long j);

    private native long nativeCreate(String str, int i, boolean z, boolean z2, byte[] bArr);

    private native long nativeCreateReplication(String str, byte[] bArr);

    private native void nativeEndRead(long j);

    private native String nativeGetDefaultReplicationDatabaseFileName();

    private native long[] nativeGetVersionID(long j);

    private native boolean nativeHasChanged(long j);

    private native void nativePromoteToWrite(long j, long j2);

    private native void nativeReserve(long j, long j2);

    private native void nativeRollback(long j);

    private native void nativeRollbackAndContinueAsRead(long j, long j2);

    static {
        RealmCore.loadLibrary();
    }

    public enum Durability {
        FULL(0),
        MEM_ONLY(1);

        final int value;

        Durability(int value) {
            this.value = value;
        }
    }

    public SharedGroup(String canonicalPath, boolean enableImplicitTransactions, Durability durability, byte[] key) {
        this.implicitTransactionsEnabled = false;
        if (enableImplicitTransactions) {
            this.nativeReplicationPtr = nativeCreateReplication(canonicalPath, key);
            this.nativePtr = createNativeWithImplicitTransactions(this.nativeReplicationPtr, durability.value, key);
            this.implicitTransactionsEnabled = true;
        } else {
            this.nativePtr = nativeCreate(canonicalPath, Durability.FULL.value, false, false, key);
        }
        this.context = new Context();
        this.path = canonicalPath;
        checkNativePtrNotZero();
    }

    void advanceRead() {
        nativeAdvanceRead(this.nativePtr, this.nativeReplicationPtr);
    }

    void advanceRead(VersionID versionID) {
        nativeAdvanceReadToVersion(this.nativePtr, this.nativeReplicationPtr, versionID.version, versionID.index);
    }

    void promoteToWrite() {
        nativePromoteToWrite(this.nativePtr, this.nativeReplicationPtr);
    }

    void commitAndContinueAsRead() {
        nativeCommitAndContinueAsRead(this.nativePtr);
    }

    void rollbackAndContinueAsRead() {
        nativeRollbackAndContinueAsRead(this.nativePtr, this.nativeReplicationPtr);
    }

    public ImplicitTransaction beginImplicitTransaction() {
        if (this.activeTransaction) {
            throw new IllegalStateException("Can't beginImplicitTransaction() during another active transaction");
        }
        long nativeGroupPtr = nativeBeginImplicit(this.nativePtr);
        ImplicitTransaction transaction = new ImplicitTransaction(this.context, this, nativeGroupPtr);
        this.activeTransaction = true;
        return transaction;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                nativeClose(this.nativePtr);
                this.nativePtr = 0L;
                if (this.implicitTransactionsEnabled && this.nativeReplicationPtr != 0) {
                    nativeCloseReplication(this.nativeReplicationPtr);
                    this.nativeReplicationPtr = 0L;
                }
            }
        }
    }

    protected void finalize() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                this.context.asyncDisposeSharedGroup(this.nativePtr);
                this.nativePtr = 0L;
                if (this.implicitTransactionsEnabled && this.nativeReplicationPtr != 0) {
                    nativeCloseReplication(this.nativeReplicationPtr);
                    this.nativeReplicationPtr = 0L;
                }
            }
        }
    }

    public boolean isClosed() {
        return this.nativePtr == 0;
    }

    public String getPath() {
        return this.path;
    }

    private void checkNativePtrNotZero() {
        if (this.nativePtr == 0) {
            throw new IOError(new RealmIOException("Realm could not be opened"));
        }
    }

    public long getNativePointer() {
        return this.nativePtr;
    }

    public long getNativeReplicationPointer() {
        return this.nativeReplicationPtr;
    }

    public VersionID getVersion() {
        long[] versionId = nativeGetVersionID(this.nativePtr);
        return new VersionID(versionId[0], versionId[1]);
    }

    public static class VersionID implements Comparable<VersionID> {
        final long index;
        final long version;

        VersionID(long version, long index) {
            this.version = version;
            this.index = index;
        }

        @Override // java.lang.Comparable
        public int compareTo(VersionID another) {
            if (this.version > another.version) {
                return 1;
            }
            if (this.version < another.version) {
                return -1;
            }
            return 0;
        }

        public String toString() {
            return "VersionID{version=" + this.version + ", index=" + this.index + '}';
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            if (!super.equals(object)) {
                return false;
            }
            VersionID versionID = (VersionID) object;
            return this.version == versionID.version && this.index == versionID.index;
        }

        public int hashCode() {
            int result = super.hashCode();
            return (((result * 31) + ((int) (this.version ^ (this.version >>> 32)))) * 31) + ((int) (this.index ^ (this.index >>> 32)));
        }
    }
}
