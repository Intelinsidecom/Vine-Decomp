package io.realm.internal;

import io.realm.internal.SharedGroup;

/* loaded from: classes.dex */
public class ImplicitTransaction extends Group {
    private final SharedGroup parent;

    public ImplicitTransaction(Context context, SharedGroup sharedGroup, long nativePtr) {
        super(context, nativePtr, true);
        this.parent = sharedGroup;
    }

    public void advanceRead() {
        assertNotClosed();
        this.parent.advanceRead();
    }

    public void advanceRead(SharedGroup.VersionID versionID) {
        assertNotClosed();
        this.parent.advanceRead(versionID);
    }

    public void promoteToWrite() {
        assertNotClosed();
        if (!this.immutable) {
            throw new IllegalStateException("Nested transactions are not allowed. Use commitTransaction() after each beginTransaction().");
        }
        this.immutable = false;
        this.parent.promoteToWrite();
    }

    public void commitAndContinueAsRead() {
        assertNotClosed();
        if (this.immutable) {
            throw new IllegalStateException("Not inside a transaction.");
        }
        this.parent.commitAndContinueAsRead();
        this.immutable = true;
    }

    public void rollbackAndContinueAsRead() {
        assertNotClosed();
        if (this.immutable) {
            throw new IllegalStateException("Not inside a transaction.");
        }
        this.parent.rollbackAndContinueAsRead();
        this.immutable = true;
    }

    private void assertNotClosed() {
        if (isClosed() || this.parent.isClosed()) {
            throw new IllegalStateException("Cannot use ImplicitTransaction after it or its parent has been closed.");
        }
    }

    public String getPath() {
        return this.parent.getPath();
    }

    @Override // io.realm.internal.Group
    protected void finalize() {
    }
}
