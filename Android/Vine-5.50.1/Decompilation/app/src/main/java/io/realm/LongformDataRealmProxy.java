package io.realm;

import co.vine.android.storage.model.LongformData;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class LongformDataRealmProxy extends LongformData implements RealmObjectProxy {
    private static final List<String> FIELD_NAMES;
    private final LongformDataColumnInfo columnInfo;

    static final class LongformDataColumnInfo extends ColumnInfo {
        public final long cursorTimeIndex;
        public final long longformIdIndex;
        public final long reachedEndIndex;
        public final long watchedIndex;

        LongformDataColumnInfo(String path, Table table) {
            Map<String, Long> indicesMap = new HashMap<>(4);
            this.longformIdIndex = getValidColumnIndex(path, table, "LongformData", "longformId");
            indicesMap.put("longformId", Long.valueOf(this.longformIdIndex));
            this.cursorTimeIndex = getValidColumnIndex(path, table, "LongformData", "cursorTime");
            indicesMap.put("cursorTime", Long.valueOf(this.cursorTimeIndex));
            this.watchedIndex = getValidColumnIndex(path, table, "LongformData", "watched");
            indicesMap.put("watched", Long.valueOf(this.watchedIndex));
            this.reachedEndIndex = getValidColumnIndex(path, table, "LongformData", "reachedEnd");
            indicesMap.put("reachedEnd", Long.valueOf(this.reachedEndIndex));
            setIndicesMap(indicesMap);
        }
    }

    static {
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("longformId");
        fieldNames.add("cursorTime");
        fieldNames.add("watched");
        fieldNames.add("reachedEnd");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    LongformDataRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (LongformDataColumnInfo) columnInfo;
    }

    @Override // co.vine.android.storage.model.LongformData
    public String getLongformId() {
        this.realm.checkIfValid();
        return this.row.getString(this.columnInfo.longformIdIndex);
    }

    @Override // co.vine.android.storage.model.LongformData
    public void setLongformId(String value) {
        this.realm.checkIfValid();
        if (value == null) {
            throw new IllegalArgumentException("Trying to set non-nullable field longformId to null.");
        }
        this.row.setString(this.columnInfo.longformIdIndex, value);
    }

    @Override // co.vine.android.storage.model.LongformData
    public long getCursorTime() {
        this.realm.checkIfValid();
        return this.row.getLong(this.columnInfo.cursorTimeIndex);
    }

    @Override // co.vine.android.storage.model.LongformData
    public void setCursorTime(long value) {
        this.realm.checkIfValid();
        this.row.setLong(this.columnInfo.cursorTimeIndex, value);
    }

    @Override // co.vine.android.storage.model.LongformData
    public boolean isWatched() {
        this.realm.checkIfValid();
        return this.row.getBoolean(this.columnInfo.watchedIndex);
    }

    @Override // co.vine.android.storage.model.LongformData
    public void setWatched(boolean value) {
        this.realm.checkIfValid();
        this.row.setBoolean(this.columnInfo.watchedIndex, value);
    }

    @Override // co.vine.android.storage.model.LongformData
    public boolean isReachedEnd() {
        this.realm.checkIfValid();
        return this.row.getBoolean(this.columnInfo.reachedEndIndex);
    }

    @Override // co.vine.android.storage.model.LongformData
    public void setReachedEnd(boolean value) {
        this.realm.checkIfValid();
        this.row.setBoolean(this.columnInfo.reachedEndIndex, value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_LongformData")) {
            return transaction.getTable("class_LongformData");
        }
        Table table = transaction.getTable("class_LongformData");
        table.addColumn(RealmFieldType.STRING, "longformId", false);
        table.addColumn(RealmFieldType.INTEGER, "cursorTime", false);
        table.addColumn(RealmFieldType.BOOLEAN, "watched", false);
        table.addColumn(RealmFieldType.BOOLEAN, "reachedEnd", false);
        table.setPrimaryKey("");
        return table;
    }

    public static LongformDataColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_LongformData")) {
            Table table = transaction.getTable("class_LongformData");
            if (table.getColumnCount() != 4) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 4 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<>();
            for (long i = 0; i < 4; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            LongformDataColumnInfo columnInfo = new LongformDataColumnInfo(transaction.getPath(), table);
            if (!columnTypes.containsKey("longformId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'longformId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("longformId") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'longformId' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.longformIdIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'longformId' does support null values in the existing Realm file. Remove @Required or @PrimaryKey from field 'longformId' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("cursorTime")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'cursorTime' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("cursorTime") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'cursorTime' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.cursorTimeIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'cursorTime' does support null values in the existing Realm file. Use corresponding boxed type for field 'cursorTime' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("watched")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'watched' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("watched") != RealmFieldType.BOOLEAN) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'boolean' for field 'watched' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.watchedIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'watched' does support null values in the existing Realm file. Use corresponding boxed type for field 'watched' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("reachedEnd")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'reachedEnd' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("reachedEnd") != RealmFieldType.BOOLEAN) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'boolean' for field 'reachedEnd' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.reachedEndIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'reachedEnd' does support null values in the existing Realm file. Use corresponding boxed type for field 'reachedEnd' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            return columnInfo;
        }
        throw new RealmMigrationNeededException(transaction.getPath(), "The LongformData class is missing from the schema for this Realm.");
    }

    public static String getTableName() {
        return "class_LongformData";
    }

    public static LongformData copyOrUpdate(Realm realm, LongformData object, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        return (object.realm == null || !object.realm.getPath().equals(realm.getPath())) ? copy(realm, object, update, cache) : object;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static LongformData copy(Realm realm, LongformData newObject, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        LongformData longformData = (LongformData) realm.createObject(LongformData.class);
        cache.put(newObject, (RealmObjectProxy) longformData);
        longformData.setLongformId(newObject.getLongformId());
        longformData.setCursorTime(newObject.getCursorTime());
        longformData.setWatched(newObject.isWatched());
        longformData.setReachedEnd(newObject.isReachedEnd());
        return longformData;
    }

    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        return "LongformData = [{longformId:" + getLongformId() + "},{cursorTime:" + getCursorTime() + "},{watched:" + isWatched() + "},{reachedEnd:" + isReachedEnd() + "}]";
    }

    public int hashCode() {
        String realmName = this.realm.getPath();
        String tableName = this.row.getTable().getName();
        long rowIndex = this.row.getIndex();
        int result = (realmName != null ? realmName.hashCode() : 0) + 527;
        return (((result * 31) + (tableName != null ? tableName.hashCode() : 0)) * 31) + ((int) ((rowIndex >>> 32) ^ rowIndex));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LongformDataRealmProxy aLongformData = (LongformDataRealmProxy) o;
        String path = this.realm.getPath();
        String otherPath = aLongformData.realm.getPath();
        if (path == null ? otherPath != null : !path.equals(otherPath)) {
            return false;
        }
        String tableName = this.row.getTable().getName();
        String otherTableName = aLongformData.row.getTable().getName();
        if (tableName == null ? otherTableName != null : !tableName.equals(otherTableName)) {
            return false;
        }
        return this.row.getIndex() == aLongformData.row.getIndex();
    }
}
