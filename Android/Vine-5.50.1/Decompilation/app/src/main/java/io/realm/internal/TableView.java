package io.realm.internal;

import java.io.Closeable;

/* loaded from: classes.dex */
public class TableView implements TableOrView, Closeable {
    private final Context context;
    protected long nativePtr;
    protected final Table parent;
    private final TableQuery query;

    private native long createNativeTableView(Table table, long j);

    private native double nativeAverageDouble(long j, long j2);

    private native double nativeAverageFloat(long j, long j2);

    private native double nativeAverageInt(long j, long j2);

    private native void nativeClear(long j);

    private native void nativeClearSubtable(long j, long j2, long j3);

    static native void nativeClose(long j);

    private native long nativeFindAllBool(long j, long j2, boolean z);

    private native long nativeFindAllDate(long j, long j2, long j3);

    private native long nativeFindAllDouble(long j, long j2, double d);

    private native long nativeFindAllFloat(long j, long j2, float f);

    private native long nativeFindAllInt(long j, long j2, long j3);

    private native long nativeFindAllString(long j, long j2, String str);

    private native long nativeFindFirstBool(long j, long j2, boolean z);

    private native long nativeFindFirstDate(long j, long j2, long j3);

    private native long nativeFindFirstDouble(long j, long j2, double d);

    private native long nativeFindFirstFloat(long j, long j2, float f);

    private native long nativeFindFirstInt(long j, long j2, long j3);

    private native long nativeFindFirstString(long j, long j2, String str);

    private native boolean nativeGetBoolean(long j, long j2, long j3);

    private native byte[] nativeGetByteArray(long j, long j2, long j3);

    private native long nativeGetColumnCount(long j);

    private native long nativeGetColumnIndex(long j, String str);

    private native String nativeGetColumnName(long j, long j2);

    private native int nativeGetColumnType(long j, long j2);

    private native long nativeGetDateTimeValue(long j, long j2, long j3);

    private native double nativeGetDouble(long j, long j2, long j3);

    private native float nativeGetFloat(long j, long j2, long j3);

    private native long nativeGetLink(long j, long j2, long j3);

    private native long nativeGetLong(long j, long j2, long j3);

    private native Mixed nativeGetMixed(long j, long j2, long j3);

    private native int nativeGetMixedType(long j, long j2, long j3);

    private native long nativeGetSourceRowIndex(long j, long j2);

    private native String nativeGetString(long j, long j2, long j3);

    private native long nativeGetSubtable(long j, long j2, long j3);

    private native long nativeGetSubtableSize(long j, long j2, long j3);

    private native boolean nativeIsNullLink(long j, long j2, long j3);

    private native Long nativeMaximumDate(long j, long j2);

    private native Double nativeMaximumDouble(long j, long j2);

    private native Float nativeMaximumFloat(long j, long j2);

    private native Long nativeMaximumInt(long j, long j2);

    private native Long nativeMinimumDate(long j, long j2);

    private native Double nativeMinimumDouble(long j, long j2);

    private native Float nativeMinimumFloat(long j, long j2);

    private native Long nativeMinimumInt(long j, long j2);

    private native void nativeNullifyLink(long j, long j2, long j3);

    private native void nativePivot(long j, long j2, long j3, int i, long j4);

    private native void nativeRemoveRow(long j, long j2);

    private native String nativeRowToString(long j, long j2);

    private native void nativeSetBoolean(long j, long j2, long j3, boolean z);

    private native void nativeSetByteArray(long j, long j2, long j3, byte[] bArr);

    private native void nativeSetDateTimeValue(long j, long j2, long j3, long j4);

    private native void nativeSetDouble(long j, long j2, long j3, double d);

    private native void nativeSetFloat(long j, long j2, long j3, float f);

    private native void nativeSetLink(long j, long j2, long j3, long j4);

    private native void nativeSetLong(long j, long j2, long j3, long j4);

    private native void nativeSetMixed(long j, long j2, long j3, Mixed mixed);

    private native void nativeSetString(long j, long j2, long j3, String str);

    private native long nativeSize(long j);

    private native void nativeSort(long j, long j2, boolean z);

    private native void nativeSortMulti(long j, long[] jArr, boolean[] zArr);

    private native double nativeSumDouble(long j, long j2);

    private native double nativeSumFloat(long j, long j2);

    private native long nativeSumInt(long j, long j2);

    private native long nativeSync(long j);

    private native String nativeToJson(long j);

    private native String nativeToString(long j, long j2);

    private native long nativeWhere(long j);

    protected TableView(Context context, Table parent, long nativePtr) {
        this.context = context;
        this.parent = parent;
        this.nativePtr = nativePtr;
        this.query = null;
    }

    protected TableView(Context context, Table parent, long nativePtr, TableQuery query) {
        this.context = context;
        this.parent = parent;
        this.nativePtr = nativePtr;
        this.query = query;
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

    protected void finalize() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                this.context.asyncDisposeTableView(this.nativePtr);
                this.nativePtr = 0L;
            }
        }
    }

    @Override // io.realm.internal.TableOrView
    public long size() {
        return nativeSize(this.nativePtr);
    }

    public long getSourceRowIndex(long rowIndex) {
        return nativeGetSourceRowIndex(this.nativePtr, rowIndex);
    }

    @Override // io.realm.internal.TableOrView
    public void clear() {
        if (this.parent.isImmutable()) {
            throwImmutable();
        }
        nativeClear(this.nativePtr);
    }

    @Override // io.realm.internal.TableOrView
    public void remove(long rowIndex) {
        if (this.parent.isImmutable()) {
            throwImmutable();
        }
        nativeRemoveRow(this.nativePtr, rowIndex);
    }

    public String toString() {
        return nativeToString(this.nativePtr, 500L);
    }

    @Override // io.realm.internal.TableOrView
    public TableQuery where() {
        this.context.executeDelayedDisposal();
        long nativeQueryPtr = nativeWhere(this.nativePtr);
        try {
            return new TableQuery(this.context, this.parent, nativeQueryPtr, this);
        } catch (RuntimeException e) {
            TableQuery.nativeClose(nativeQueryPtr);
            throw e;
        }
    }

    private void throwImmutable() {
        throw new IllegalStateException("Mutable method call during read transaction.");
    }

    @Override // io.realm.internal.TableOrView
    public long sync() {
        return nativeSync(this.nativePtr);
    }
}
