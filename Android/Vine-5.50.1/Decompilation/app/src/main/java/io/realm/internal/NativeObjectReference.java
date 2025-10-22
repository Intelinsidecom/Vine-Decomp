package io.realm.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/* loaded from: classes.dex */
public abstract class NativeObjectReference extends PhantomReference<NativeObject> {
    protected final long nativePointer;

    protected abstract void cleanup();

    public NativeObjectReference(NativeObject referent, ReferenceQueue<? super NativeObject> referenceQueue) {
        super(referent, referenceQueue);
        this.nativePointer = referent.nativePointer;
    }

    @Override // java.lang.ref.Reference
    public void clear() {
        cleanup();
        super.clear();
    }
}
