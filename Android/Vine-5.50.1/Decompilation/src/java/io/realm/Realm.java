package io.realm;

import android.os.Looper;
import io.realm.BaseRealm;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnIndices;
import io.realm.internal.ColumnInfo;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Table;
import io.realm.internal.Util;
import io.realm.internal.log.RealmLog;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class Realm extends BaseRealm {
    private final Map<Class<? extends RealmObject>, Table> classToTable;

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ void beginTransaction() {
        super.beginTransaction();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ void cancelTransaction() {
        super.cancelTransaction();
    }

    @Override // io.realm.BaseRealm, java.io.Closeable, java.lang.AutoCloseable
    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ void commitTransaction() {
        super.commitTransaction();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ RealmConfiguration getConfiguration() {
        return super.getConfiguration();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ String getPath() {
        return super.getPath();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ RealmSchema getSchema() {
        return super.getSchema();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ long getVersion() {
        return super.getVersion();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ boolean isClosed() {
        return super.isClosed();
    }

    @Override // io.realm.BaseRealm
    public /* bridge */ /* synthetic */ void setAutoRefresh(boolean z) {
        super.setAutoRefresh(z);
    }

    Realm(RealmConfiguration configuration, boolean autoRefresh) {
        super(configuration, autoRefresh);
        this.classToTable = new HashMap();
    }

    protected void finalize() throws Throwable {
        if (this.sharedGroupManager != null && this.sharedGroupManager.isOpen()) {
            RealmLog.w("Remember to call close() on all Realm instances. Realm " + this.configuration.getPath() + " is being finalized without being closed, this can lead to running out of native memory.");
        }
        super.finalize();
    }

    public static Realm getInstance(RealmConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("A non-null RealmConfiguration must be provided");
        }
        return (Realm) RealmCache.createRealmOrGetFromCache(configuration, Realm.class);
    }

    static Realm createInstance(RealmConfiguration configuration, ColumnIndices columnIndices) {
        try {
            return createAndValidate(configuration, columnIndices);
        } catch (RealmMigrationNeededException e) {
            if (configuration.shouldDeleteRealmIfMigrationNeeded()) {
                deleteRealm(configuration);
            } else {
                migrateRealm(configuration);
            }
            return createAndValidate(configuration, columnIndices);
        }
    }

    static Realm createAndValidate(RealmConfiguration configuration, ColumnIndices columnIndices) {
        boolean autoRefresh = Looper.myLooper() != null;
        Realm realm = new Realm(configuration, autoRefresh);
        long currentVersion = realm.getVersion();
        long requiredVersion = configuration.getSchemaVersion();
        if (currentVersion != -1 && currentVersion < requiredVersion && columnIndices == null) {
            realm.doClose();
            throw new RealmMigrationNeededException(configuration.getPath(), String.format("Realm on disk need to migrate from v%s to v%s", Long.valueOf(currentVersion), Long.valueOf(requiredVersion)));
        }
        if (currentVersion != -1 && requiredVersion < currentVersion && columnIndices == null) {
            realm.doClose();
            throw new IllegalArgumentException(String.format("Realm on disk is newer than the one specified: v%s vs. v%s", Long.valueOf(currentVersion), Long.valueOf(requiredVersion)));
        }
        if (columnIndices == null) {
            try {
                initializeRealm(realm);
            } catch (RuntimeException e) {
                realm.doClose();
                throw e;
            }
        } else {
            realm.schema.columnIndices = columnIndices;
        }
        return realm;
    }

    private static void initializeRealm(Realm realm) {
        long version = realm.getVersion();
        boolean commitNeeded = false;
        try {
            realm.beginTransaction();
            if (version == -1) {
                commitNeeded = true;
                realm.setVersion(realm.configuration.getSchemaVersion());
            }
            RealmProxyMediator mediator = realm.configuration.getSchemaMediator();
            Set<Class<? extends RealmObject>> modelClasses = mediator.getModelClasses();
            Map<Class<? extends RealmObject>, ColumnInfo> columnInfoMap = new HashMap<>(modelClasses.size());
            for (Class<? extends RealmObject> modelClass : modelClasses) {
                if (version == -1) {
                    mediator.createTable(modelClass, realm.sharedGroupManager.getTransaction());
                }
                columnInfoMap.put(modelClass, mediator.validateTable(modelClass, realm.sharedGroupManager.getTransaction()));
            }
            realm.schema.columnIndices = new ColumnIndices(columnInfoMap);
            if (commitNeeded) {
                realm.commitTransaction();
            } else {
                realm.cancelTransaction();
            }
        } catch (Throwable th) {
            if (commitNeeded) {
                realm.commitTransaction();
            } else {
                realm.cancelTransaction();
            }
            throw th;
        }
    }

    public <E extends RealmObject> E createObject(Class<E> cls) {
        checkIfValid();
        return (E) get(cls, getTable(cls).addEmptyRow());
    }

    public <E extends RealmObject> E copyToRealm(E e) {
        checkNotNullObject(e);
        return (E) copyOrUpdate(e, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <E extends RealmObject> E copyToRealmOrUpdate(E e) {
        checkNotNullObject(e);
        checkHasPrimaryKey(e.getClass());
        return (E) copyOrUpdate(e, true);
    }

    public <E extends RealmObject> RealmQuery<E> where(Class<E> clazz) {
        checkIfValid();
        return RealmQuery.createQuery(this, clazz);
    }

    private <E extends RealmObject> E copyOrUpdate(E e, boolean z) {
        checkIfValid();
        return (E) this.configuration.getSchemaMediator().copyOrUpdate(this, e, z, new HashMap());
    }

    private <E extends RealmObject> void checkNotNullObject(E object) {
        if (object == null) {
            throw new IllegalArgumentException("Null objects cannot be copied into Realm.");
        }
    }

    private void checkHasPrimaryKey(Class<? extends RealmObject> clazz) {
        if (!getTable(clazz).hasPrimaryKey()) {
            throw new IllegalArgumentException("A RealmObject with no @PrimaryKey cannot be updated: " + clazz.toString());
        }
    }

    public static void migrateRealm(RealmConfiguration configuration) {
        migrateRealm(configuration, null);
    }

    public static void migrateRealm(RealmConfiguration configuration, RealmMigration migration) {
        BaseRealm.migrateRealm(configuration, migration, new BaseRealm.MigrationCallback() { // from class: io.realm.Realm.2
            @Override // io.realm.BaseRealm.MigrationCallback
            public void migrationComplete() {
            }
        });
    }

    public static boolean deleteRealm(RealmConfiguration configuration) {
        return BaseRealm.deleteRealm(configuration);
    }

    static String getCanonicalPath(File realmFile) {
        try {
            return realmFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RealmException("Could not resolve the canonical path to the Realm file: " + realmFile.getAbsolutePath());
        }
    }

    public Table getTable(Class<? extends RealmObject> clazz) {
        Table table = this.classToTable.get(clazz);
        if (table == null) {
            Class<? extends RealmObject> clazz2 = Util.getOriginalModelClass(clazz);
            Table table2 = this.sharedGroupManager.getTable(this.configuration.getSchemaMediator().getTableName(clazz2));
            this.classToTable.put(clazz2, table2);
            return table2;
        }
        return table;
    }

    public static Object getDefaultModule() throws ClassNotFoundException {
        try {
            Class<?> clazz = Class.forName("io.realm.DefaultRealmModule");
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[0]);
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            throw new RealmException("Could not create an instance of io.realm.DefaultRealmModule", e2);
        } catch (InstantiationException e3) {
            throw new RealmException("Could not create an instance of io.realm.DefaultRealmModule", e3);
        } catch (InvocationTargetException e4) {
            throw new RealmException("Could not create an instance of io.realm.DefaultRealmModule", e4);
        }
    }
}
