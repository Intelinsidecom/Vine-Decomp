package io.realm;

import io.realm.RealmObjectSchema;
import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.Table;
import io.realm.internal.Util;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class RealmSchema {
    private static final String TABLE_PREFIX = Table.TABLE_PREFIX;
    ColumnIndices columnIndices;
    private final BaseRealm realm;
    private final ImplicitTransaction transaction;
    private final Map<String, Table> dynamicClassToTable = new HashMap();
    private final Map<Class<? extends RealmObject>, Table> classToTable = new HashMap();
    private final Map<Class<? extends RealmObject>, RealmObjectSchema> classToSchema = new HashMap();
    private final Map<String, RealmObjectSchema> dynamicClassToSchema = new HashMap();

    RealmSchema(BaseRealm realm, ImplicitTransaction transaction) {
        this.realm = realm;
        this.transaction = transaction;
    }

    public RealmObjectSchema create(String className) {
        checkEmpty(className, "Null or empty class names are not allowed");
        String internalTableName = TABLE_PREFIX + className;
        if (internalTableName.length() > 56) {
            throw new IllegalArgumentException("Class name is to long. Limit is 57 characters: " + className.length());
        }
        if (this.transaction.hasTable(internalTableName)) {
            throw new IllegalArgumentException("Class already exists: " + className);
        }
        Table table = this.transaction.getTable(internalTableName);
        RealmObjectSchema.DynamicColumnMap columnIndices = new RealmObjectSchema.DynamicColumnMap(table);
        return new RealmObjectSchema(this.realm, table, columnIndices);
    }

    private void checkEmpty(String str, String error) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException(error);
        }
    }

    ColumnInfo getColumnInfo(Class<? extends RealmObject> clazz) {
        ColumnInfo columnInfo = this.columnIndices.getColumnInfo(clazz);
        if (columnInfo == null) {
            throw new IllegalStateException("No validated schema information found for " + this.realm.configuration.getSchemaMediator().getTableName(clazz));
        }
        return columnInfo;
    }

    Table getTable(String className) {
        String className2 = Table.TABLE_PREFIX + className;
        Table table = this.dynamicClassToTable.get(className2);
        if (table == null) {
            if (!this.transaction.hasTable(className2)) {
                throw new IllegalArgumentException("The class " + className2 + " doesn't exist in this Realm.");
            }
            Table table2 = this.transaction.getTable(className2);
            this.dynamicClassToTable.put(className2, table2);
            return table2;
        }
        return table;
    }

    Table getTable(Class<? extends RealmObject> clazz) {
        Table table = this.classToTable.get(clazz);
        if (table == null) {
            Class<? extends RealmObject> clazz2 = Util.getOriginalModelClass(clazz);
            Table table2 = this.transaction.getTable(this.realm.configuration.getSchemaMediator().getTableName(clazz2));
            this.classToTable.put(clazz2, table2);
            return table2;
        }
        return table;
    }

    RealmObjectSchema getSchemaForClass(Class<? extends RealmObject> clazz) {
        RealmObjectSchema classSchema = this.classToSchema.get(clazz);
        if (classSchema == null) {
            Class<? extends RealmObject> clazz2 = Util.getOriginalModelClass(clazz);
            Table table = this.transaction.getTable(this.realm.configuration.getSchemaMediator().getTableName(clazz2));
            RealmObjectSchema classSchema2 = new RealmObjectSchema(this.realm, table, this.columnIndices.getColumnInfo(clazz2).getIndicesMap());
            this.classToSchema.put(clazz2, classSchema2);
            return classSchema2;
        }
        return classSchema;
    }
}
