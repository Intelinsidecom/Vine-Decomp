package io.realm;

import co.vine.android.storage.model.SessionData;
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
public class SessionDataRealmProxy extends SessionData implements RealmObjectProxy {
    private static final List<String> FIELD_NAMES;
    private final SessionDataColumnInfo columnInfo;

    static final class SessionDataColumnInfo extends ColumnInfo {
        public final long avatarUrlIndex;
        public final long editionIndex;
        public final long screenNameIndex;
        public final long userIdIndex;
        public final long usernameIndex;

        SessionDataColumnInfo(String path, Table table) {
            Map<String, Long> indicesMap = new HashMap<>(5);
            this.userIdIndex = getValidColumnIndex(path, table, "SessionData", "userId");
            indicesMap.put("userId", Long.valueOf(this.userIdIndex));
            this.usernameIndex = getValidColumnIndex(path, table, "SessionData", "username");
            indicesMap.put("username", Long.valueOf(this.usernameIndex));
            this.screenNameIndex = getValidColumnIndex(path, table, "SessionData", "screenName");
            indicesMap.put("screenName", Long.valueOf(this.screenNameIndex));
            this.avatarUrlIndex = getValidColumnIndex(path, table, "SessionData", "avatarUrl");
            indicesMap.put("avatarUrl", Long.valueOf(this.avatarUrlIndex));
            this.editionIndex = getValidColumnIndex(path, table, "SessionData", "edition");
            indicesMap.put("edition", Long.valueOf(this.editionIndex));
            setIndicesMap(indicesMap);
        }
    }

    static {
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("userId");
        fieldNames.add("username");
        fieldNames.add("screenName");
        fieldNames.add("avatarUrl");
        fieldNames.add("edition");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    SessionDataRealmProxy(ColumnInfo columnInfo) {
        this.columnInfo = (SessionDataColumnInfo) columnInfo;
    }

    @Override // co.vine.android.storage.model.SessionData
    public long getUserId() {
        this.realm.checkIfValid();
        return this.row.getLong(this.columnInfo.userIdIndex);
    }

    @Override // co.vine.android.storage.model.SessionData
    public void setUserId(long value) {
        this.realm.checkIfValid();
        this.row.setLong(this.columnInfo.userIdIndex, value);
    }

    @Override // co.vine.android.storage.model.SessionData
    public String getUsername() {
        this.realm.checkIfValid();
        return this.row.getString(this.columnInfo.usernameIndex);
    }

    @Override // co.vine.android.storage.model.SessionData
    public void setUsername(String value) {
        this.realm.checkIfValid();
        if (value == null) {
            this.row.setNull(this.columnInfo.usernameIndex);
        } else {
            this.row.setString(this.columnInfo.usernameIndex, value);
        }
    }

    @Override // co.vine.android.storage.model.SessionData
    public String getScreenName() {
        this.realm.checkIfValid();
        return this.row.getString(this.columnInfo.screenNameIndex);
    }

    @Override // co.vine.android.storage.model.SessionData
    public void setScreenName(String value) {
        this.realm.checkIfValid();
        if (value == null) {
            this.row.setNull(this.columnInfo.screenNameIndex);
        } else {
            this.row.setString(this.columnInfo.screenNameIndex, value);
        }
    }

    @Override // co.vine.android.storage.model.SessionData
    public String getAvatarUrl() {
        this.realm.checkIfValid();
        return this.row.getString(this.columnInfo.avatarUrlIndex);
    }

    @Override // co.vine.android.storage.model.SessionData
    public void setAvatarUrl(String value) {
        this.realm.checkIfValid();
        if (value == null) {
            this.row.setNull(this.columnInfo.avatarUrlIndex);
        } else {
            this.row.setString(this.columnInfo.avatarUrlIndex, value);
        }
    }

    @Override // co.vine.android.storage.model.SessionData
    public String getEdition() {
        this.realm.checkIfValid();
        return this.row.getString(this.columnInfo.editionIndex);
    }

    @Override // co.vine.android.storage.model.SessionData
    public void setEdition(String value) {
        this.realm.checkIfValid();
        if (value == null) {
            this.row.setNull(this.columnInfo.editionIndex);
        } else {
            this.row.setString(this.columnInfo.editionIndex, value);
        }
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_SessionData")) {
            return transaction.getTable("class_SessionData");
        }
        Table table = transaction.getTable("class_SessionData");
        table.addColumn(RealmFieldType.INTEGER, "userId", false);
        table.addColumn(RealmFieldType.STRING, "username", true);
        table.addColumn(RealmFieldType.STRING, "screenName", true);
        table.addColumn(RealmFieldType.STRING, "avatarUrl", true);
        table.addColumn(RealmFieldType.STRING, "edition", true);
        table.setPrimaryKey("");
        return table;
    }

    public static SessionDataColumnInfo validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_SessionData")) {
            Table table = transaction.getTable("class_SessionData");
            if (table.getColumnCount() != 5) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 5 but was " + table.getColumnCount());
            }
            Map<String, RealmFieldType> columnTypes = new HashMap<>();
            for (long i = 0; i < 5; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            SessionDataColumnInfo columnInfo = new SessionDataColumnInfo(transaction.getPath(), table);
            if (!columnTypes.containsKey("userId")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'userId' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("userId") != RealmFieldType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'long' for field 'userId' in existing Realm file.");
            }
            if (table.isColumnNullable(columnInfo.userIdIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'userId' does support null values in the existing Realm file. Use corresponding boxed type for field 'userId' or migrate using io.realm.internal.Table.convertColumnToNotNullable().");
            }
            if (!columnTypes.containsKey("username")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'username' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("username") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'username' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.usernameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'username' is required. Either set @Required to field 'username' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("screenName")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'screenName' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("screenName") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'screenName' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.screenNameIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'screenName' is required. Either set @Required to field 'screenName' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("avatarUrl")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'avatarUrl' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("avatarUrl") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'avatarUrl' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.avatarUrlIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'avatarUrl' is required. Either set @Required to field 'avatarUrl' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            if (!columnTypes.containsKey("edition")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'edition' in existing Realm file. Either remove field or migrate using io.realm.internal.Table.addColumn().");
            }
            if (columnTypes.get("edition") != RealmFieldType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'edition' in existing Realm file.");
            }
            if (!table.isColumnNullable(columnInfo.editionIndex)) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field 'edition' is required. Either set @Required to field 'edition' or migrate using io.realm.internal.Table.convertColumnToNullable().");
            }
            return columnInfo;
        }
        throw new RealmMigrationNeededException(transaction.getPath(), "The SessionData class is missing from the schema for this Realm.");
    }

    public static String getTableName() {
        return "class_SessionData";
    }

    public static SessionData copyOrUpdate(Realm realm, SessionData object, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        return (object.realm == null || !object.realm.getPath().equals(realm.getPath())) ? copy(realm, object, update, cache) : object;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static SessionData copy(Realm realm, SessionData newObject, boolean update, Map<RealmObject, RealmObjectProxy> cache) {
        SessionData sessionData = (SessionData) realm.createObject(SessionData.class);
        cache.put(newObject, (RealmObjectProxy) sessionData);
        sessionData.setUserId(newObject.getUserId());
        sessionData.setUsername(newObject.getUsername());
        sessionData.setScreenName(newObject.getScreenName());
        sessionData.setAvatarUrl(newObject.getAvatarUrl());
        sessionData.setEdition(newObject.getEdition());
        return sessionData;
    }

    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("SessionData = [");
        stringBuilder.append("{userId:");
        stringBuilder.append(getUserId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{username:");
        stringBuilder.append(getUsername() != null ? getUsername() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{screenName:");
        stringBuilder.append(getScreenName() != null ? getScreenName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avatarUrl:");
        stringBuilder.append(getAvatarUrl() != null ? getAvatarUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{edition:");
        stringBuilder.append(getEdition() != null ? getEdition() : "null");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
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
        SessionDataRealmProxy aSessionData = (SessionDataRealmProxy) o;
        String path = this.realm.getPath();
        String otherPath = aSessionData.realm.getPath();
        if (path == null ? otherPath != null : !path.equals(otherPath)) {
            return false;
        }
        String tableName = this.row.getTable().getName();
        String otherTableName = aSessionData.row.getTable().getName();
        if (tableName == null ? otherTableName != null : !tableName.equals(otherTableName)) {
            return false;
        }
        return this.row.getIndex() == aSessionData.row.getIndex();
    }
}
