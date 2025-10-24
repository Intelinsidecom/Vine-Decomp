package io.realm.internal;

import io.realm.exceptions.RealmMigrationNeededException;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
public class ColumnInfo {
    private Map<String, Long> indicesMap;

    protected final long getValidColumnIndex(String realmPath, Table table, String className, String columnName) {
        long columnIndex = table.getColumnIndex(columnName);
        if (columnIndex == -1) {
            throw new RealmMigrationNeededException(realmPath, "Field '" + columnName + "' not found for type " + className);
        }
        return columnIndex;
    }

    protected final void setIndicesMap(Map<String, Long> indicesMap) {
        this.indicesMap = Collections.unmodifiableMap(indicesMap);
    }

    public Map<String, Long> getIndicesMap() {
        return this.indicesMap;
    }
}
