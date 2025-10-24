package io.realm.internal;

import io.realm.RealmFieldType;
import java.lang.ref.ReferenceQueue;
import java.util.Date;

/* loaded from: classes.dex */
public class UncheckedRow extends NativeObject implements Row {
    final Context context;
    final Table parent;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeClose(long j);

    protected native boolean nativeGetBoolean(long j, long j2);

    protected native byte[] nativeGetByteArray(long j, long j2);

    protected native long nativeGetColumnCount(long j);

    protected native long nativeGetColumnIndex(long j, String str);

    protected native String nativeGetColumnName(long j, long j2);

    protected native int nativeGetColumnType(long j, long j2);

    protected native long nativeGetDateTime(long j, long j2);

    protected native double nativeGetDouble(long j, long j2);

    protected native float nativeGetFloat(long j, long j2);

    protected native long nativeGetIndex(long j);

    protected native long nativeGetLink(long j, long j2);

    protected native long nativeGetLinkView(long j, long j2);

    protected native long nativeGetLong(long j, long j2);

    protected native Mixed nativeGetMixed(long j, long j2);

    protected native int nativeGetMixedType(long j, long j2);

    protected native String nativeGetString(long j, long j2);

    protected native boolean nativeHasColumn(long j, String str);

    protected native boolean nativeIsAttached(long j);

    protected native boolean nativeIsNull(long j, long j2);

    protected native boolean nativeIsNullLink(long j, long j2);

    protected native void nativeNullifyLink(long j, long j2);

    protected native void nativeSetBoolean(long j, long j2, boolean z);

    protected native void nativeSetByteArray(long j, long j2, byte[] bArr);

    protected native void nativeSetDate(long j, long j2, long j3);

    protected native void nativeSetDouble(long j, long j2, double d);

    protected native void nativeSetFloat(long j, long j2, float f);

    protected native void nativeSetLink(long j, long j2, long j3);

    protected native void nativeSetLong(long j, long j2, long j3);

    protected native void nativeSetMixed(long j, long j2, Mixed mixed);

    protected native void nativeSetNull(long j, long j2);

    protected native void nativeSetString(long j, long j2, String str);

    protected static class UncheckedRowNativeObjectReference extends NativeObjectReference {
        public UncheckedRowNativeObjectReference(NativeObject referent, ReferenceQueue<? super NativeObject> referenceQueue) {
            super(referent, referenceQueue);
        }

        @Override // io.realm.internal.NativeObjectReference
        protected void cleanup() {
            UncheckedRow.nativeClose(this.nativePointer);
        }
    }

    protected UncheckedRow(Context context, Table parent, long nativePtr) {
        this.context = context;
        this.parent = parent;
        this.nativePointer = nativePtr;
        context.cleanNativeReferences();
    }

    public static UncheckedRow getByRowIndex(Context context, Table table, long index) {
        long nativeRowPointer = table.nativeGetRowPtr(table.nativePtr, index);
        UncheckedRow row = new UncheckedRow(context, table, nativeRowPointer);
        context.rowReferences.put(new UncheckedRowNativeObjectReference(row, context.referenceQueue), Context.NATIVE_REFERENCES_VALUE);
        return row;
    }

    public static UncheckedRow getByRowPointer(Context context, Table table, long nativeRowPointer) {
        UncheckedRow row = new UncheckedRow(context, table, nativeRowPointer);
        context.rowReferences.put(new UncheckedRowNativeObjectReference(row, context.referenceQueue), Context.NATIVE_REFERENCES_VALUE);
        return row;
    }

    @Override // io.realm.internal.Row
    public long getColumnCount() {
        return nativeGetColumnCount(this.nativePointer);
    }

    @Override // io.realm.internal.Row
    public String getColumnName(long columnIndex) {
        return nativeGetColumnName(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public long getColumnIndex(String columnName) {
        if (columnName == null) {
            throw new IllegalArgumentException("Column name can not be null.");
        }
        return nativeGetColumnIndex(this.nativePointer, columnName);
    }

    @Override // io.realm.internal.Row
    public RealmFieldType getColumnType(long columnIndex) {
        return RealmFieldType.fromNativeValue(nativeGetColumnType(this.nativePointer, columnIndex));
    }

    @Override // io.realm.internal.Row
    public Table getTable() {
        return this.parent;
    }

    @Override // io.realm.internal.Row
    public long getIndex() {
        return nativeGetIndex(this.nativePointer);
    }

    @Override // io.realm.internal.Row
    public long getLong(long columnIndex) {
        return nativeGetLong(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public boolean getBoolean(long columnIndex) {
        return nativeGetBoolean(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public float getFloat(long columnIndex) {
        return nativeGetFloat(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public double getDouble(long columnIndex) {
        return nativeGetDouble(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public Date getDate(long columnIndex) {
        return new Date(nativeGetDateTime(this.nativePointer, columnIndex) * 1000);
    }

    @Override // io.realm.internal.Row
    public String getString(long columnIndex) {
        return nativeGetString(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public byte[] getBinaryByteArray(long columnIndex) {
        return nativeGetByteArray(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public boolean isNullLink(long columnIndex) {
        return nativeIsNullLink(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public LinkView getLinkList(long columnIndex) {
        long nativeLinkViewPtr = nativeGetLinkView(this.nativePointer, columnIndex);
        return new LinkView(this.context, this.parent, columnIndex, nativeLinkViewPtr);
    }

    @Override // io.realm.internal.Row
    public void setLong(long columnIndex, long value) {
        this.parent.checkImmutable();
        getTable().checkIntValueIsLegal(columnIndex, getIndex(), value);
        nativeSetLong(this.nativePointer, columnIndex, value);
    }

    @Override // io.realm.internal.Row
    public void setBoolean(long columnIndex, boolean value) {
        this.parent.checkImmutable();
        nativeSetBoolean(this.nativePointer, columnIndex, value);
    }

    @Override // io.realm.internal.Row
    public void setString(long columnIndex, String value) {
        this.parent.checkImmutable();
        getTable().checkStringValueIsLegal(columnIndex, getIndex(), value);
        nativeSetString(this.nativePointer, columnIndex, value);
    }

    @Override // io.realm.internal.Row
    public void setNull(long columnIndex) {
        nativeSetNull(this.nativePointer, columnIndex);
    }

    @Override // io.realm.internal.Row
    public boolean isAttached() {
        return this.nativePointer != 0 && nativeIsAttached(this.nativePointer);
    }
}
