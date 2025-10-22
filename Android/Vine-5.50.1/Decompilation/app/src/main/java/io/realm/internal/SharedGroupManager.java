package io.realm.internal;

import io.realm.RealmConfiguration;
import io.realm.internal.SharedGroup;
import java.io.Closeable;

/* loaded from: classes.dex */
public class SharedGroupManager implements Closeable {
    private SharedGroup sharedGroup;
    private ImplicitTransaction transaction;

    public SharedGroupManager(RealmConfiguration configuration) {
        this.sharedGroup = new SharedGroup(configuration.getPath(), true, configuration.getDurability(), configuration.getEncryptionKey());
        this.transaction = this.sharedGroup.beginImplicitTransaction();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.sharedGroup.close();
        this.sharedGroup = null;
        this.transaction = null;
    }

    public boolean isOpen() {
        return this.sharedGroup != null;
    }

    public void advanceRead() {
        this.transaction.advanceRead();
    }

    public void advanceRead(SharedGroup.VersionID version) {
        this.transaction.advanceRead(version);
    }

    public Table getTable(String tableName) {
        return this.transaction.getTable(tableName);
    }

    public SharedGroup.VersionID getVersion() {
        return this.sharedGroup.getVersion();
    }

    public void promoteToWrite() {
        this.transaction.promoteToWrite();
    }

    public void commitAndContinueAsRead() {
        this.transaction.commitAndContinueAsRead();
    }

    public void rollbackAndContinueAsRead() {
        this.transaction.rollbackAndContinueAsRead();
    }

    public boolean hasTable(String tableName) {
        return this.transaction.hasTable(tableName);
    }

    public ImplicitTransaction getTransaction() {
        return this.transaction;
    }

    public long getNativePointer() {
        return this.sharedGroup.getNativePointer();
    }
}
