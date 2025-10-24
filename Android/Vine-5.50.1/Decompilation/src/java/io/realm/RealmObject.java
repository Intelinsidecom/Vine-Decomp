package io.realm;

import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.TableQuery;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes.dex */
public abstract class RealmObject {
    protected BaseRealm realm;
    protected Row row;
    private final List<RealmChangeListener> listeners = new CopyOnWriteArrayList();
    private boolean isCompleted = false;
    protected long currentTableVersion = -1;

    public final boolean isValid() {
        return this.row != null && this.row.isAttached();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected Table getTable() {
        return this.realm.schema.getTable((Class<? extends RealmObject>) getClass());
    }

    void onCompleted(Long handoverRowPointer) {
        if (handoverRowPointer.longValue() == 0) {
            this.isCompleted = true;
            return;
        }
        if (!this.isCompleted || this.row == Row.EMPTY_ROW) {
            this.isCompleted = true;
            long nativeRowPointer = TableQuery.nativeImportHandoverRowIntoSharedGroup(handoverRowPointer.longValue(), this.realm.sharedGroupManager.getNativePointer());
            Table table = getTable();
            this.row = table.getUncheckedRowByPointer(nativeRowPointer);
        }
    }

    void notifyChangeListeners() {
        if (this.listeners != null && !this.listeners.isEmpty() && this.row.getTable() != null) {
            long version = this.row.getTable().version();
            if (this.currentTableVersion != version) {
                this.currentTableVersion = version;
                for (RealmChangeListener listener : this.listeners) {
                    listener.onChange();
                }
            }
        }
    }

    void setTableVersion() {
        if (this.row.getTable() != null) {
            this.currentTableVersion = this.row.getTable().version();
        }
    }
}
