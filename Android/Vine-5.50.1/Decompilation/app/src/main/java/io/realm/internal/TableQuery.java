package io.realm.internal;

import io.realm.Case;
import io.realm.Sort;
import java.io.Closeable;

/* loaded from: classes.dex */
public class TableQuery implements Closeable {
    private final Context context;
    protected long nativePtr;
    private final TableOrView origin;
    protected final Table table;
    protected boolean DEBUG = false;
    private boolean queryValidated = true;

    private native double nativeAverageDouble(long j, long j2, long j3, long j4, long j5);

    private native double nativeAverageFloat(long j, long j2, long j3, long j4, long j5);

    private native double nativeAverageInt(long j, long j2, long j3, long j4, long j5);

    public static native long[] nativeBatchUpdateQueries(long j, long j2, long[] jArr, long[][] jArr2, long[][] jArr3, boolean[][] zArr);

    private native void nativeBeginsWith(long j, long[] jArr, String str, boolean z);

    private native void nativeBetween(long j, long[] jArr, double d, double d2);

    private native void nativeBetween(long j, long[] jArr, float f, float f2);

    private native void nativeBetween(long j, long[] jArr, long j2, long j3);

    private native void nativeBetweenDateTime(long j, long[] jArr, long j2, long j3);

    protected static native void nativeClose(long j);

    public static native void nativeCloseQueryHandover(long j);

    private native void nativeContains(long j, long[] jArr, String str, boolean z);

    private native long nativeCount(long j, long j2, long j3, long j4);

    private native void nativeEndGroup(long j);

    private native void nativeEndsWith(long j, long[] jArr, String str, boolean z);

    private native void nativeEqual(long j, long[] jArr, double d);

    private native void nativeEqual(long j, long[] jArr, float f);

    private native void nativeEqual(long j, long[] jArr, long j2);

    private native void nativeEqual(long j, long[] jArr, String str, boolean z);

    private native void nativeEqual(long j, long[] jArr, boolean z);

    private native void nativeEqualDateTime(long j, long[] jArr, long j2);

    private native long nativeFind(long j, long j2);

    private native long nativeFindAll(long j, long j2, long j3, long j4);

    public static native long nativeFindAllMultiSortedWithHandover(long j, long j2, long j3, long j4, long j5, long j6, long[] jArr, boolean[] zArr);

    public static native long nativeFindAllSortedWithHandover(long j, long j2, long j3, long j4, long j5, long j6, long j7, boolean z);

    public static native long nativeFindAllWithHandover(long j, long j2, long j3, long j4, long j5, long j6);

    public static native long nativeFindWithHandover(long j, long j2, long j3, long j4);

    public static native long nativeGetDistinctViewWithHandover(long j, long j2, long j3, long j4);

    private native void nativeGreater(long j, long[] jArr, double d);

    private native void nativeGreater(long j, long[] jArr, float f);

    private native void nativeGreater(long j, long[] jArr, long j2);

    private native void nativeGreaterDateTime(long j, long[] jArr, long j2);

    private native void nativeGreaterEqual(long j, long[] jArr, double d);

    private native void nativeGreaterEqual(long j, long[] jArr, float f);

    private native void nativeGreaterEqual(long j, long[] jArr, long j2);

    private native void nativeGreaterEqualDateTime(long j, long[] jArr, long j2);

    private native void nativeGroup(long j);

    private native long nativeHandoverQuery(long j, long j2);

    public static native long nativeImportHandoverRowIntoSharedGroup(long j, long j2);

    private native long nativeImportHandoverTableViewIntoSharedGroup(long j, long j2);

    private native void nativeIsEmpty(long j, long[] jArr);

    private native void nativeIsNotNull(long j, long[] jArr);

    private native void nativeIsNull(long j, long[] jArr);

    private native void nativeLess(long j, long[] jArr, double d);

    private native void nativeLess(long j, long[] jArr, float f);

    private native void nativeLess(long j, long[] jArr, long j2);

    private native void nativeLessDateTime(long j, long[] jArr, long j2);

    private native void nativeLessEqual(long j, long[] jArr, double d);

    private native void nativeLessEqual(long j, long[] jArr, float f);

    private native void nativeLessEqual(long j, long[] jArr, long j2);

    private native void nativeLessEqualDateTime(long j, long[] jArr, long j2);

    private native Long nativeMaximumDate(long j, long j2, long j3, long j4, long j5);

    private native Double nativeMaximumDouble(long j, long j2, long j3, long j4, long j5);

    private native Float nativeMaximumFloat(long j, long j2, long j3, long j4, long j5);

    private native Long nativeMaximumInt(long j, long j2, long j3, long j4, long j5);

    private native Long nativeMinimumDate(long j, long j2, long j3, long j4, long j5);

    private native Double nativeMinimumDouble(long j, long j2, long j3, long j4, long j5);

    private native Float nativeMinimumFloat(long j, long j2, long j3, long j4, long j5);

    private native Long nativeMinimumInt(long j, long j2, long j3, long j4, long j5);

    private native void nativeNot(long j);

    private native void nativeNotEqual(long j, long[] jArr, double d);

    private native void nativeNotEqual(long j, long[] jArr, float f);

    private native void nativeNotEqual(long j, long[] jArr, long j2);

    private native void nativeNotEqual(long j, long[] jArr, String str, boolean z);

    private native void nativeNotEqualDateTime(long j, long[] jArr, long j2);

    private native void nativeOr(long j);

    private native void nativeParent(long j);

    private native long nativeRemove(long j, long j2, long j3, long j4);

    private native void nativeSubtable(long j, long j2);

    private native double nativeSumDouble(long j, long j2, long j3, long j4, long j5);

    private native double nativeSumFloat(long j, long j2, long j3, long j4, long j5);

    private native long nativeSumInt(long j, long j2, long j3, long j4, long j5);

    private native void nativeTableview(long j, long j2);

    private native String nativeValidateQuery(long j);

    public TableQuery(Context context, Table table, long nativeQueryPtr) {
        if (this.DEBUG) {
            System.err.println("++++++ new TableQuery, ptr= " + nativeQueryPtr);
        }
        this.context = context;
        this.table = table;
        this.nativePtr = nativeQueryPtr;
        this.origin = null;
    }

    public TableQuery(Context context, Table table, long nativeQueryPtr, TableOrView origin) {
        if (this.DEBUG) {
            System.err.println("++++++ new TableQuery, ptr= " + nativeQueryPtr);
        }
        this.context = context;
        this.table = table;
        this.nativePtr = nativeQueryPtr;
        this.origin = origin;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                nativeClose(this.nativePtr);
                if (this.DEBUG) {
                    System.err.println("++++ Query CLOSE, ptr= " + this.nativePtr);
                }
                this.nativePtr = 0L;
            }
        }
    }

    protected void finalize() {
        synchronized (this.context) {
            if (this.nativePtr != 0) {
                this.context.asyncDisposeQuery(this.nativePtr);
                this.nativePtr = 0L;
            }
        }
    }

    private void validateQuery() {
        if (!this.queryValidated) {
            String invalidMessage = nativeValidateQuery(this.nativePtr);
            if (invalidMessage.equals("")) {
                this.queryValidated = true;
                return;
            }
            throw new UnsupportedOperationException(invalidMessage);
        }
    }

    public TableQuery equalTo(long[] columnIndexes, long value) {
        nativeEqual(this.nativePtr, columnIndexes, value);
        this.queryValidated = false;
        return this;
    }

    public TableQuery equalTo(long[] columnIndexes, String value, Case caseSensitive) {
        nativeEqual(this.nativePtr, columnIndexes, value, caseSensitive.getValue());
        this.queryValidated = false;
        return this;
    }

    public long find() {
        validateQuery();
        return nativeFind(this.nativePtr, 0L);
    }

    public TableView findAll() {
        validateQuery();
        this.context.executeDelayedDisposal();
        long nativeViewPtr = nativeFindAll(this.nativePtr, 0L, -1L, -1L);
        try {
            return new TableView(this.context, this.table, nativeViewPtr, this);
        } catch (RuntimeException e) {
            TableView.nativeClose(nativeViewPtr);
            throw e;
        }
    }

    public TableView importHandoverTableView(long handoverPtr, long callerSharedGroupPtr) {
        long nativeTvPtr = 0;
        try {
            nativeTvPtr = nativeImportHandoverTableViewIntoSharedGroup(handoverPtr, callerSharedGroupPtr);
            return new TableView(this.context, this.table, nativeTvPtr);
        } catch (RuntimeException e) {
            if (nativeTvPtr != 0) {
                TableView.nativeClose(nativeTvPtr);
            }
            throw e;
        }
    }

    public long handoverQuery(long callerSharedGroupPtr) {
        return nativeHandoverQuery(callerSharedGroupPtr, this.nativePtr);
    }

    public TableQuery isNull(long[] columnIndices) {
        nativeIsNull(this.nativePtr, columnIndices);
        this.queryValidated = false;
        return this;
    }

    public static boolean[] getNativeSortOrderValues(Sort[] sortOrders) {
        boolean[] nativeValues = new boolean[sortOrders.length];
        for (int i = 0; i < sortOrders.length; i++) {
            nativeValues[i] = sortOrders[i].getValue();
        }
        return nativeValues;
    }
}
