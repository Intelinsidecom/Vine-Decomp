package io.realm;

import io.realm.RealmObject;
import io.realm.internal.LinkView;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RealmList<E extends RealmObject> extends AbstractList<E> {
    protected String className;
    protected Class<E> clazz;
    private final boolean managedMode = false;
    private List<E> nonManagedList = new ArrayList();
    protected BaseRealm realm;
    protected LinkView view;

    private boolean isAttached() {
        return this.view != null && this.view.isAttached();
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int location, E object) {
        checkValidObject(object);
        if (this.managedMode) {
            checkValidView();
            this.view.insert(location, copyToRealmIfNeeded(object).row.getIndex());
            return;
        }
        this.nonManagedList.add(location, object);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E object) {
        checkValidObject(object);
        if (this.managedMode) {
            checkValidView();
            this.view.add(copyToRealmIfNeeded(object).row.getIndex());
            return true;
        }
        this.nonManagedList.add(object);
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i, E e) {
        checkValidObject(e);
        if (!this.managedMode) {
            return this.nonManagedList.set(i, e);
        }
        checkValidView();
        RealmObject realmObjectCopyToRealmIfNeeded = copyToRealmIfNeeded(e);
        E e2 = (E) get(i);
        this.view.set(i, realmObjectCopyToRealmIfNeeded.row.getIndex());
        return e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private E copyToRealmIfNeeded(E e) {
        if (e.row == null || !e.realm.getPath().equals(this.realm.getPath())) {
            if (e instanceof DynamicRealmObject) {
                throw new IllegalArgumentException("Automatically copying DynamicRealmObjects from other Realms are not supported");
            }
            Realm realm = (Realm) this.realm;
            if (realm.getTable(e.getClass()).hasPrimaryKey()) {
                return (E) realm.copyToRealmOrUpdate(e);
            }
            return (E) realm.copyToRealm(e);
        }
        return e;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        if (this.managedMode) {
            checkValidView();
            this.view.clear();
        } else {
            this.nonManagedList.clear();
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i) {
        if (!this.managedMode) {
            return this.nonManagedList.remove(i);
        }
        checkValidView();
        E e = (E) get(i);
        this.view.remove(i);
        return e;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i) {
        if (!this.managedMode) {
            return this.nonManagedList.get(i);
        }
        checkValidView();
        return (E) this.realm.get(this.clazz, this.className, this.view.getTargetRowIndex(i));
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        if (this.managedMode) {
            checkValidView();
            long size = this.view.size();
            if (size < 2147483647L) {
                return (int) size;
            }
            return Integer.MAX_VALUE;
        }
        return this.nonManagedList.size();
    }

    private void checkValidObject(E object) {
        if (object == null) {
            throw new IllegalArgumentException("RealmList does not accept null values");
        }
    }

    private void checkValidView() {
        this.realm.checkIfValid();
        if (this.view == null || !this.view.isAttached()) {
            throw new IllegalStateException("Realm instance has been closed or parent object has been removed.");
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.managedMode ? this.clazz.getSimpleName() : getClass().getSimpleName());
        sb.append("@[");
        if (this.managedMode && !isAttached()) {
            sb.append("invalid");
        } else {
            for (int i = 0; i < size(); i++) {
                if (this.managedMode) {
                    sb.append(get(i).row.getIndex());
                } else {
                    sb.append(System.identityHashCode(get(i)));
                }
                if (i < size() - 1) {
                    sb.append(',');
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
