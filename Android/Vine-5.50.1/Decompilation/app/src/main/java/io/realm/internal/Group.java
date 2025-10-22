package io.realm.internal;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class Group implements Closeable {
    private final Context context;
    protected boolean immutable;
    protected long nativePtr;

    protected static native void nativeClose(long j);

    protected native long createNative();

    protected native long createNative(String str, int i);

    protected native long createNative(ByteBuffer byteBuffer);

    protected native long createNative(byte[] bArr);

    protected native void nativeCommit(long j);

    protected native String nativeGetTableName(long j, int i);

    protected native long nativeGetTableNativePtr(long j, String str);

    protected native boolean nativeHasTable(long j, String str);

    protected native boolean nativeIsEmpty(long j);

    protected native long nativeLoadFromMem(byte[] bArr);

    native void nativeRemoveTable(long j, String str);

    native void nativeRenameTable(long j, String str, String str2);

    protected native long nativeSize(long j);

    protected native String nativeToJson(long j);

    protected native String nativeToString(long j);

    protected native void nativeWriteToFile(long j, String str, byte[] bArr) throws IOException;

    protected native byte[] nativeWriteToMem(long j);

    static {
        RealmCore.loadLibrary();
    }

    private void checkNativePtrNotZero() {
        if (this.nativePtr == 0) {
            throw new OutOfMemoryError("Out of native memory.");
        }
    }

    public Group() {
        this.immutable = false;
        this.context = new Context();
        this.nativePtr = createNative();
        checkNativePtrNotZero();
    }

    Group(Context context, long nativePointer, boolean immutable) {
        this.context = context;
        this.nativePtr = nativePointer;
        this.immutable = immutable;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                nativeClose(this.nativePtr);
                this.nativePtr = 0L;
            }
        }
    }

    boolean isClosed() {
        return this.nativePtr == 0;
    }

    protected void finalize() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                this.context.asyncDisposeGroup(this.nativePtr);
                this.nativePtr = 0L;
            }
        }
    }

    private void verifyGroupIsValid() {
        if (this.nativePtr == 0) {
            throw new IllegalStateException("Illegal to call methods on a closed Group.");
        }
    }

    public boolean hasTable(String name) {
        verifyGroupIsValid();
        return name != null && nativeHasTable(this.nativePtr, name);
    }

    public Table getTable(String name) {
        verifyGroupIsValid();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name. Name must be a non-empty String.");
        }
        if (this.immutable && !hasTable(name)) {
            throw new IllegalStateException("Requested table is not in this Realm. Creating it requires a transaction: " + name);
        }
        this.context.executeDelayedDisposal();
        long nativeTablePointer = nativeGetTableNativePtr(this.nativePtr, name);
        try {
            return new Table(this.context, this, nativeTablePointer);
        } catch (RuntimeException e) {
            Table.nativeClose(nativeTablePointer);
            throw e;
        }
    }

    public String toString() {
        return nativeToString(this.nativePtr);
    }
}
