package io.realm.internal;

import java.lang.ref.ReferenceQueue;

/* loaded from: classes.dex */
public class LinkView extends NativeObject {
    final long columnIndexInParent;
    private final Context context;
    final Table parent;

    private native void nativeAdd(long j, long j2);

    private native void nativeClear(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeClose(long j);

    private native long nativeGetTargetRowIndex(long j, long j2);

    private native void nativeInsert(long j, long j2, long j3);

    private native boolean nativeIsAttached(long j);

    private native boolean nativeIsEmpty(long j);

    private native void nativeMove(long j, long j2, long j3);

    private native void nativeRemove(long j, long j2);

    private native void nativeSet(long j, long j2, long j3);

    private native long nativeSize(long j);

    native long nativeGetRow(long j, long j2);

    protected native long nativeWhere(long j);

    private static class LinkViewReference extends NativeObjectReference {
        public LinkViewReference(NativeObject referent, ReferenceQueue<? super NativeObject> referenceQueue) {
            super(referent, referenceQueue);
        }

        @Override // io.realm.internal.NativeObjectReference
        protected void cleanup() {
            LinkView.nativeClose(this.nativePointer);
        }
    }

    public LinkView(Context context, Table parent, long columnIndexInParent, long nativeLinkViewPtr) {
        this.context = context;
        this.parent = parent;
        this.columnIndexInParent = columnIndexInParent;
        this.nativePointer = nativeLinkViewPtr;
        context.cleanNativeReferences();
        context.rowReferences.put(new LinkViewReference(this, context.referenceQueue), Context.NATIVE_REFERENCES_VALUE);
    }

    public long getTargetRowIndex(long pos) {
        return nativeGetTargetRowIndex(this.nativePointer, pos);
    }

    public void add(long rowIndex) {
        checkImmutable();
        nativeAdd(this.nativePointer, rowIndex);
    }

    public void insert(long pos, long rowIndex) {
        checkImmutable();
        nativeInsert(this.nativePointer, pos, rowIndex);
    }

    public void set(long pos, long rowIndex) {
        checkImmutable();
        nativeSet(this.nativePointer, pos, rowIndex);
    }

    public void remove(long pos) {
        checkImmutable();
        nativeRemove(this.nativePointer, pos);
    }

    public void clear() {
        checkImmutable();
        nativeClear(this.nativePointer);
    }

    public long size() {
        return nativeSize(this.nativePointer);
    }

    public boolean isAttached() {
        return nativeIsAttached(this.nativePointer);
    }

    private void checkImmutable() {
        if (this.parent.isImmutable()) {
            throw new IllegalStateException("Changing Realm data can only be done from inside a transaction.");
        }
    }
}
