package io.realm.internal;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class Context {
    static final Integer NATIVE_REFERENCES_VALUE = 0;
    private List<Long> abandonedTables = new ArrayList();
    private List<Long> abandonedTableViews = new ArrayList();
    private List<Long> abandonedQueries = new ArrayList();
    HashMap<Reference<?>, Integer> rowReferences = new HashMap<>();
    ReferenceQueue<NativeObject> referenceQueue = new ReferenceQueue<>();
    private boolean isFinalized = false;

    public void executeDelayedDisposal() {
        synchronized (this) {
            for (int i = 0; i < this.abandonedTables.size(); i++) {
                long nativePointer = this.abandonedTables.get(i).longValue();
                Table.nativeClose(nativePointer);
            }
            this.abandonedTables.clear();
            for (int i2 = 0; i2 < this.abandonedTableViews.size(); i2++) {
                long nativePointer2 = this.abandonedTableViews.get(i2).longValue();
                TableView.nativeClose(nativePointer2);
            }
            this.abandonedTableViews.clear();
            for (int i3 = 0; i3 < this.abandonedQueries.size(); i3++) {
                long nativePointer3 = this.abandonedQueries.get(i3).longValue();
                TableQuery.nativeClose(nativePointer3);
            }
            this.abandonedQueries.clear();
            cleanNativeReferences();
        }
    }

    public void cleanNativeReferences() {
        NativeObjectReference reference = (NativeObjectReference) this.referenceQueue.poll();
        while (reference != null) {
            reference.clear();
            this.rowReferences.remove(reference);
            reference = (NativeObjectReference) this.referenceQueue.poll();
        }
    }

    public void asyncDisposeTable(long nativePointer, boolean isRoot) {
        if (isRoot || this.isFinalized) {
            Table.nativeClose(nativePointer);
        } else {
            this.abandonedTables.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeTableView(long nativePointer) {
        if (this.isFinalized) {
            TableView.nativeClose(nativePointer);
        } else {
            this.abandonedTableViews.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeQuery(long nativePointer) {
        if (this.isFinalized) {
            TableQuery.nativeClose(nativePointer);
        } else {
            this.abandonedQueries.add(Long.valueOf(nativePointer));
        }
    }

    public void asyncDisposeGroup(long nativePointer) {
        Group.nativeClose(nativePointer);
    }

    public void asyncDisposeSharedGroup(long nativePointer) {
        SharedGroup.nativeClose(nativePointer);
    }

    protected void finalize() {
        synchronized (this) {
            this.isFinalized = true;
        }
        executeDelayedDisposal();
    }
}
