package io.realm;

import android.os.Handler;
import android.os.Looper;
import io.realm.RealmCache;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.SharedGroupManager;
import io.realm.internal.Table;
import io.realm.internal.UncheckedRow;
import io.realm.internal.android.ReleaseAndroidLogger;
import io.realm.internal.async.RealmThreadPoolExecutor;
import io.realm.internal.log.RealmLog;
import java.io.Closeable;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
abstract class BaseRealm implements Closeable {
    protected boolean autoRefresh;
    protected RealmConfiguration configuration;
    Handler handler;
    HandlerController handlerController;
    RealmSchema schema;
    protected SharedGroupManager sharedGroupManager;
    protected long threadId = Thread.currentThread().getId();
    protected static final Map<Handler, String> handlers = new ConcurrentHashMap();
    static final RealmThreadPoolExecutor asyncQueryExecutor = RealmThreadPoolExecutor.getInstance();

    protected interface MigrationCallback {
        void migrationComplete();
    }

    static {
        RealmLog.add(new ReleaseAndroidLogger());
    }

    protected BaseRealm(RealmConfiguration configuration, boolean autoRefresh) {
        this.configuration = configuration;
        this.sharedGroupManager = new SharedGroupManager(configuration);
        this.schema = new RealmSchema(this, this.sharedGroupManager.getTransaction());
        setAutoRefresh(autoRefresh);
    }

    public void setAutoRefresh(boolean autoRefresh) {
        checkIfValid();
        if (autoRefresh && Looper.myLooper() == null) {
            throw new IllegalStateException("Cannot set auto-refresh in a Thread without a Looper");
        }
        if (autoRefresh && !this.autoRefresh) {
            this.handlerController = new HandlerController(this);
            this.handler = new Handler(this.handlerController);
            handlers.put(this.handler, this.configuration.getPath());
        } else if (!autoRefresh && this.autoRefresh && this.handler != null) {
            removeHandler();
        }
        this.autoRefresh = autoRefresh;
    }

    protected void removeHandler() {
        handlers.remove(this.handler);
        this.handler.removeCallbacksAndMessages(null);
        this.handler = null;
    }

    public void beginTransaction() {
        checkIfValid();
        this.sharedGroupManager.promoteToWrite();
    }

    public void commitTransaction() {
        checkIfValid();
        this.sharedGroupManager.commitAndContinueAsRead();
        for (Map.Entry<Handler, String> handlerIntegerEntry : handlers.entrySet()) {
            Handler handler = handlerIntegerEntry.getKey();
            String realmPath = handlerIntegerEntry.getValue();
            if (handler.equals(this.handler)) {
                this.handlerController.notifyGlobalListeners();
                this.handlerController.notifyTypeBasedListeners();
                if (this.handlerController.threadContainsAsyncEmptyRealmObject()) {
                    this.handlerController.updateAsyncEmptyRealmObject();
                }
            } else if (realmPath.equals(this.configuration.getPath()) && !handler.hasMessages(14930352) && handler.getLooper().getThread().isAlive() && !handler.sendEmptyMessage(14930352)) {
                RealmLog.w("Cannot update Looper threads when the Looper has quit. Use realm.setAutoRefresh(false) to prevent this.");
            }
        }
    }

    public void cancelTransaction() {
        checkIfValid();
        this.sharedGroupManager.rollbackAndContinueAsRead();
    }

    protected void checkIfValid() {
        if (this.sharedGroupManager == null || !this.sharedGroupManager.isOpen()) {
            throw new IllegalStateException("This Realm instance has already been closed, making it unusable.");
        }
        if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException("Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.");
        }
    }

    public String getPath() {
        return this.configuration.getPath();
    }

    public RealmConfiguration getConfiguration() {
        return this.configuration;
    }

    public long getVersion() {
        if (!this.sharedGroupManager.hasTable("metadata")) {
            return -1L;
        }
        Table metadataTable = this.sharedGroupManager.getTable("metadata");
        return metadataTable.getLong(0L, 0L);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException("Realm access from incorrect thread. Realm instance can only be closed on the thread it was created.");
        }
        RealmCache.release(this);
    }

    void doClose() {
        if (this.sharedGroupManager != null) {
            this.sharedGroupManager.close();
            this.sharedGroupManager = null;
        }
        if (this.handler != null) {
            removeHandler();
        }
    }

    public boolean isClosed() {
        if (this.threadId != Thread.currentThread().getId()) {
            throw new IllegalStateException("Realm access from incorrect thread. Realm objects can only be accessed on the thread they were created.");
        }
        return this.sharedGroupManager == null || !this.sharedGroupManager.isOpen();
    }

    void setVersion(long version) {
        Table metadataTable = this.sharedGroupManager.getTable("metadata");
        if (metadataTable.getColumnCount() == 0) {
            metadataTable.addColumn(RealmFieldType.INTEGER, "version");
            metadataTable.addEmptyRow();
        }
        metadataTable.setLong(0L, 0L, version);
    }

    public RealmSchema getSchema() {
        return this.schema;
    }

    <E extends RealmObject> E get(Class<E> cls, long j) {
        UncheckedRow uncheckedRow = this.schema.getTable((Class<? extends RealmObject>) cls).getUncheckedRow(j);
        E e = (E) this.configuration.getSchemaMediator().newInstance(cls, this.schema.getColumnInfo(cls));
        e.row = uncheckedRow;
        e.realm = this;
        e.setTableVersion();
        if (this.handlerController != null) {
            this.handlerController.addToRealmObjects(e);
        }
        return e;
    }

    <E extends RealmObject> E get(Class<E> cls, String str, long j) {
        Table table;
        DynamicRealmObject dynamicRealmObject;
        if (str != null) {
            table = this.schema.getTable(str);
            dynamicRealmObject = new DynamicRealmObject();
        } else {
            table = this.schema.getTable((Class<? extends RealmObject>) cls);
            dynamicRealmObject = (E) this.configuration.getSchemaMediator().newInstance(cls, this.schema.getColumnInfo(cls));
        }
        dynamicRealmObject.row = table.getUncheckedRow(j);
        dynamicRealmObject.realm = this;
        dynamicRealmObject.setTableVersion();
        if (this.handlerController != null) {
            this.handlerController.addToRealmObjects(dynamicRealmObject);
        }
        return dynamicRealmObject;
    }

    static boolean deleteRealm(final RealmConfiguration configuration) {
        final AtomicBoolean realmDeleted = new AtomicBoolean(true);
        RealmCache.invokeWithGlobalRefCount(configuration, new RealmCache.Callback() { // from class: io.realm.BaseRealm.1
            @Override // io.realm.RealmCache.Callback
            public void onResult(int count) {
                if (count != 0) {
                    throw new IllegalStateException("It's not allowed to delete the file associated with an open Realm. Remember to close() all the instances of the Realm before deleting its file.");
                }
                String canonicalPath = configuration.getPath();
                File realmFolder = configuration.getRealmFolder();
                String realmFileName = configuration.getRealmFileName();
                List<File> filesToDelete = Arrays.asList(new File(canonicalPath), new File(realmFolder, realmFileName + ".lock"), new File(realmFolder, realmFileName + ".log_a"), new File(realmFolder, realmFileName + ".log_b"), new File(realmFolder, realmFileName + ".log"));
                for (File fileToDelete : filesToDelete) {
                    if (fileToDelete.exists()) {
                        boolean deleteResult = fileToDelete.delete();
                        if (!deleteResult) {
                            realmDeleted.set(false);
                            RealmLog.w("Could not delete the file " + fileToDelete);
                        }
                    }
                }
            }
        });
        return realmDeleted.get();
    }

    protected static void migrateRealm(final RealmConfiguration configuration, final RealmMigration migration, final MigrationCallback callback) {
        if (configuration == null) {
            throw new IllegalArgumentException("RealmConfiguration must be provided");
        }
        if (migration == null && configuration.getMigration() == null) {
            throw new RealmMigrationNeededException(configuration.getPath(), "RealmMigration must be provided");
        }
        RealmCache.invokeWithGlobalRefCount(configuration, new RealmCache.Callback() { // from class: io.realm.BaseRealm.2
            @Override // io.realm.RealmCache.Callback
            public void onResult(int count) {
                if (count != 0) {
                    throw new IllegalStateException("Cannot migrate a Realm file that is already open: " + configuration.getPath());
                }
                RealmMigration realmMigration = migration == null ? configuration.getMigration() : migration;
                DynamicRealm realm = null;
                try {
                    try {
                        realm = DynamicRealm.getInstance(configuration);
                        realm.beginTransaction();
                        long currentVersion = realm.getVersion();
                        realmMigration.migrate(realm, currentVersion, configuration.getSchemaVersion());
                        realm.setVersion(configuration.getSchemaVersion());
                        realm.commitTransaction();
                    } catch (RuntimeException e) {
                        if (realm != null) {
                            realm.cancelTransaction();
                        }
                        throw e;
                    }
                } finally {
                    if (realm != null) {
                        realm.close();
                        callback.migrationComplete();
                    }
                }
            }
        });
    }
}
