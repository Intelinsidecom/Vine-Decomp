package io.realm.internal;

import io.realm.RealmFieldType;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public class Table implements TableOrView, Closeable {
    public static final String TABLE_PREFIX = Util.getTablePrefix();
    static AtomicInteger tableCount = new AtomicInteger(0);
    private long cachedPrimaryKeyColumnIndex;
    private final Context context;
    protected long nativePtr;
    protected final Object parent;

    private native long nativeAddColumn(long j, int i, String str, boolean z);

    private native long nativeAddColumnLink(long j, int i, String str, long j2);

    private native long nativeAddEmptyRow(long j, long j2);

    private native void nativeAddSearchIndex(long j, long j2);

    private native double nativeAverageDouble(long j, long j2);

    private native double nativeAverageFloat(long j, long j2);

    private native double nativeAverageInt(long j, long j2);

    private native void nativeClear(long j);

    private native void nativeClearSubtable(long j, long j2, long j3);

    static native void nativeClose(long j);

    private native void nativeConvertColumnToNotNullable(long j, long j2);

    private native void nativeConvertColumnToNullable(long j, long j2);

    private native long nativeCountDouble(long j, long j2, double d);

    private native long nativeCountFloat(long j, long j2, float f);

    private native long nativeCountLong(long j, long j2, long j3);

    private native long nativeCountString(long j, long j2, String str);

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

    private native long nativeGetDateTime(long j, long j2, long j3);

    private native long nativeGetDistinctView(long j, long j2);

    private native double nativeGetDouble(long j, long j2, long j3);

    private native float nativeGetFloat(long j, long j2, long j3);

    private native long nativeGetLink(long j, long j2, long j3);

    private native long nativeGetLinkTarget(long j, long j2);

    private native long nativeGetLong(long j, long j2, long j3);

    private native Mixed nativeGetMixed(long j, long j2, long j3);

    private native int nativeGetMixedType(long j, long j2, long j3);

    private native String nativeGetName(long j);

    private native long nativeGetSortedView(long j, long j2, boolean z);

    private native long nativeGetSortedViewMulti(long j, long[] jArr, boolean[] zArr);

    private native String nativeGetString(long j, long j2, long j3);

    private native long nativeGetSubtable(long j, long j2, long j3);

    private native long nativeGetSubtableDuringInsert(long j, long j2, long j3);

    private native long nativeGetSubtableSize(long j, long j2, long j3);

    private native TableSpec nativeGetTableSpec(long j);

    private native boolean nativeHasSameSchema(long j, long j2);

    private native boolean nativeHasSearchIndex(long j, long j2);

    private native boolean nativeIsColumnNullable(long j, long j2);

    private native boolean nativeIsNullLink(long j, long j2, long j3);

    private native boolean nativeIsRootTable(long j);

    private native boolean nativeIsValid(long j);

    private native long nativeLowerBoundInt(long j, long j2, long j3);

    private native long nativeMaximumDate(long j, long j2);

    private native double nativeMaximumDouble(long j, long j2);

    private native float nativeMaximumFloat(long j, long j2);

    private native long nativeMaximumInt(long j, long j2);

    private native void nativeMigratePrimaryKeyTableIfNeeded(long j, long j2);

    private native long nativeMinimumDate(long j, long j2);

    private native double nativeMinimumDouble(long j, long j2);

    private native float nativeMinimumFloat(long j, long j2);

    private native long nativeMinimumInt(long j, long j2);

    private native void nativeMoveLastOver(long j, long j2);

    private native void nativeNullifyLink(long j, long j2, long j3);

    private native void nativeOptimize(long j);

    private native void nativePivot(long j, long j2, long j3, int i, long j4);

    private native void nativeRemove(long j, long j2);

    private native void nativeRemoveColumn(long j, long j2);

    private native void nativeRemoveLast(long j);

    private native void nativeRemoveSearchIndex(long j, long j2);

    private native void nativeRenameColumn(long j, long j2, String str);

    private native String nativeRowToString(long j, long j2);

    private native void nativeSetBoolean(long j, long j2, long j3, boolean z);

    private native void nativeSetByteArray(long j, long j2, long j3, byte[] bArr);

    private native void nativeSetDate(long j, long j2, long j3, long j4);

    private native void nativeSetDouble(long j, long j2, long j3, double d);

    private native void nativeSetFloat(long j, long j2, long j3, float f);

    private native void nativeSetLink(long j, long j2, long j3, long j4);

    private native void nativeSetLong(long j, long j2, long j3, long j4);

    private native void nativeSetMixed(long j, long j2, long j3, Mixed mixed);

    private native long nativeSetPrimaryKey(long j, long j2, String str);

    private native void nativeSetString(long j, long j2, long j3, String str);

    private native long nativeSize(long j);

    private native double nativeSumDouble(long j, long j2);

    private native double nativeSumFloat(long j, long j2);

    private native long nativeSumInt(long j, long j2);

    private native String nativeToJson(long j);

    private native String nativeToString(long j, long j2);

    private native void nativeUpdateFromSpec(long j, TableSpec tableSpec);

    private native long nativeUpperBoundInt(long j, long j2, long j3);

    private native long nativeVersion(long j);

    private native long nativeWhere(long j);

    protected native long createNative();

    native long nativeGetRowPtr(long j, long j2);

    static {
        RealmCore.loadLibrary();
    }

    public Table() {
        this.cachedPrimaryKeyColumnIndex = -1L;
        this.parent = null;
        this.context = new Context();
        this.nativePtr = createNative();
        if (this.nativePtr == 0) {
            throw new java.lang.OutOfMemoryError("Out of native memory.");
        }
    }

    Table(Context context, Object parent, long nativePointer) {
        this.cachedPrimaryKeyColumnIndex = -1L;
        this.context = context;
        this.parent = parent;
        this.nativePtr = nativePointer;
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
                boolean isRoot = this.parent == null;
                this.context.asyncDisposeTable(this.nativePtr, isRoot);
                this.nativePtr = 0L;
            }
        }
    }

    private void verifyColumnName(String name) {
        if (name.length() > 63) {
            throw new IllegalArgumentException("Column names are currently limited to max 63 characters.");
        }
    }

    public long addColumn(RealmFieldType type, String name, boolean isNullable) {
        verifyColumnName(name);
        return nativeAddColumn(this.nativePtr, type.getNativeValue(), name, isNullable);
    }

    public long addColumn(RealmFieldType type, String name) {
        return addColumn(type, name, false);
    }

    public void removeColumn(long columnIndex) {
        nativeRemoveColumn(this.nativePtr, columnIndex);
    }

    public boolean isColumnNullable(long columnIndex) {
        return nativeIsColumnNullable(this.nativePtr, columnIndex);
    }

    @Override // io.realm.internal.TableOrView
    public long size() {
        return nativeSize(this.nativePtr);
    }

    @Override // io.realm.internal.TableOrView
    public void clear() {
        checkImmutable();
        nativeClear(this.nativePtr);
    }

    public long getColumnCount() {
        return nativeGetColumnCount(this.nativePtr);
    }

    public String getColumnName(long columnIndex) {
        return nativeGetColumnName(this.nativePtr, columnIndex);
    }

    public long getColumnIndex(String columnName) {
        if (columnName == null) {
            throw new IllegalArgumentException("Column name can not be null.");
        }
        return nativeGetColumnIndex(this.nativePtr, columnName);
    }

    public RealmFieldType getColumnType(long columnIndex) {
        return RealmFieldType.fromNativeValue(nativeGetColumnType(this.nativePtr, columnIndex));
    }

    @Override // io.realm.internal.TableOrView
    public void remove(long rowIndex) {
        checkImmutable();
        nativeRemove(this.nativePtr, rowIndex);
    }

    public long addEmptyRow() {
        checkImmutable();
        if (hasPrimaryKey()) {
            long primaryKeyColumnIndex = getPrimaryKey();
            RealmFieldType type = getColumnType(primaryKeyColumnIndex);
            switch (type) {
                case STRING:
                    if (findFirstString(primaryKeyColumnIndex, "") != -1) {
                        throwDuplicatePrimaryKeyException("");
                        break;
                    }
                    break;
                case INTEGER:
                    if (findFirstLong(primaryKeyColumnIndex, 0L) != -1) {
                        throwDuplicatePrimaryKeyException(0L);
                        break;
                    }
                    break;
                default:
                    throw new RealmException("Cannot check for duplicate rows for unsupported primary key type: " + type);
            }
        }
        return nativeAddEmptyRow(this.nativePtr, 1L);
    }

    private boolean isPrimaryKeyColumn(long columnIndex) {
        return columnIndex == getPrimaryKey();
    }

    public long getPrimaryKey() {
        if (this.cachedPrimaryKeyColumnIndex >= 0 || this.cachedPrimaryKeyColumnIndex == -2) {
            return this.cachedPrimaryKeyColumnIndex;
        }
        Table pkTable = getPrimaryKeyTable();
        if (pkTable == null) {
            return -2L;
        }
        String tableName = getName();
        if (tableName.startsWith(TABLE_PREFIX)) {
            tableName = tableName.substring(TABLE_PREFIX.length());
        }
        long rowIndex = pkTable.findFirstString(0L, tableName);
        if (rowIndex != -1) {
            String pkColumnName = pkTable.getUncheckedRow(rowIndex).getString(1L);
            this.cachedPrimaryKeyColumnIndex = getColumnIndex(pkColumnName);
        } else {
            this.cachedPrimaryKeyColumnIndex = -2L;
        }
        return this.cachedPrimaryKeyColumnIndex;
    }

    public boolean isPrimaryKey(long columnIndex) {
        return columnIndex >= 0 && columnIndex == getPrimaryKey();
    }

    public boolean hasPrimaryKey() {
        return getPrimaryKey() >= 0;
    }

    void checkStringValueIsLegal(long columnIndex, long rowToUpdate, String value) {
        if (isPrimaryKey(columnIndex)) {
            long rowIndex = findFirstString(columnIndex, value);
            if (rowIndex != rowToUpdate && rowIndex != -1) {
                throwDuplicatePrimaryKeyException(value);
            }
        }
    }

    void checkIntValueIsLegal(long columnIndex, long rowToUpdate, long value) {
        if (isPrimaryKeyColumn(columnIndex)) {
            long rowIndex = findFirstLong(columnIndex, value);
            if (rowIndex != rowToUpdate && rowIndex != -1) {
                throwDuplicatePrimaryKeyException(Long.valueOf(value));
            }
        }
    }

    private void throwDuplicatePrimaryKeyException(Object value) {
        throw new RealmPrimaryKeyConstraintException("Value already exists: " + value);
    }

    public long getLong(long columnIndex, long rowIndex) {
        return nativeGetLong(this.nativePtr, columnIndex, rowIndex);
    }

    public Table getLinkTarget(long columnIndex) {
        this.context.executeDelayedDisposal();
        long nativeTablePointer = nativeGetLinkTarget(this.nativePtr, columnIndex);
        try {
            return new Table(this.context, this.parent, nativeTablePointer);
        } catch (RuntimeException e) {
            nativeClose(nativeTablePointer);
            throw e;
        }
    }

    public UncheckedRow getUncheckedRow(long index) {
        return UncheckedRow.getByRowIndex(this.context, this, index);
    }

    public UncheckedRow getUncheckedRowByPointer(long nativeRowPointer) {
        return UncheckedRow.getByRowPointer(this.context, this, nativeRowPointer);
    }

    public void setLong(long columnIndex, long rowIndex, long value) {
        checkImmutable();
        checkIntValueIsLegal(columnIndex, rowIndex, value);
        nativeSetLong(this.nativePtr, columnIndex, rowIndex, value);
    }

    public void addSearchIndex(long columnIndex) {
        checkImmutable();
        nativeAddSearchIndex(this.nativePtr, columnIndex);
    }

    public void removeSearchIndex(long columnIndex) {
        checkImmutable();
        nativeRemoveSearchIndex(this.nativePtr, columnIndex);
    }

    public void setPrimaryKey(String columnName) {
        Table pkTable = getPrimaryKeyTable();
        if (pkTable == null) {
            throw new RealmException("Primary keys are only supported if Table is part of a Group");
        }
        this.cachedPrimaryKeyColumnIndex = nativeSetPrimaryKey(pkTable.nativePtr, this.nativePtr, columnName);
    }

    private Table getPrimaryKeyTable() {
        Group group = getTableGroup();
        if (group == null) {
            return null;
        }
        Table pkTable = group.getTable("pk");
        if (pkTable.getColumnCount() == 0) {
            pkTable.addColumn(RealmFieldType.STRING, "pk_table");
            pkTable.addColumn(RealmFieldType.STRING, "pk_property");
            return pkTable;
        }
        migratePrimaryKeyTableIfNeeded(group, pkTable);
        return pkTable;
    }

    private void migratePrimaryKeyTableIfNeeded(Group group, Table pkTable) {
        nativeMigratePrimaryKeyTableIfNeeded(group.nativePtr, pkTable.nativePtr);
    }

    Group getTableGroup() {
        if (this.parent instanceof Group) {
            return (Group) this.parent;
        }
        if (this.parent instanceof Table) {
            return ((Table) this.parent).getTableGroup();
        }
        return null;
    }

    public boolean hasSearchIndex(long columnIndex) {
        return nativeHasSearchIndex(this.nativePtr, columnIndex);
    }

    boolean isImmutable() {
        if (this.parent instanceof Table) {
            return ((Table) this.parent).isImmutable();
        }
        return this.parent != null && ((Group) this.parent).immutable;
    }

    void checkImmutable() {
        if (isImmutable()) {
            throwImmutable();
        }
    }

    @Override // io.realm.internal.TableOrView
    public TableQuery where() {
        this.context.executeDelayedDisposal();
        long nativeQueryPtr = nativeWhere(this.nativePtr);
        try {
            return new TableQuery(this.context, this, nativeQueryPtr);
        } catch (RuntimeException e) {
            TableQuery.nativeClose(nativeQueryPtr);
            throw e;
        }
    }

    public long findFirstLong(long columnIndex, long value) {
        return nativeFindFirstInt(this.nativePtr, columnIndex, value);
    }

    public long findFirstString(long columnIndex, String value) {
        if (value == null) {
            throw new IllegalArgumentException("null is not supported");
        }
        return nativeFindFirstString(this.nativePtr, columnIndex, value);
    }

    public String getName() {
        return nativeGetName(this.nativePtr);
    }

    public String toString() {
        return nativeToString(this.nativePtr, -1L);
    }

    @Override // io.realm.internal.TableOrView
    public long sync() {
        throw new RuntimeException("Not supported for tables");
    }

    private void throwImmutable() {
        throw new IllegalStateException("Changing Realm data can only be done from inside a transaction.");
    }

    public long version() {
        return nativeVersion(this.nativePtr);
    }
}
