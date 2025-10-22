package io.realm;

import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import io.realm.internal.TableOrView;
import io.realm.internal.TableQuery;
import io.realm.internal.TableView;
import java.util.AbstractList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public final class RealmResults<E extends RealmObject> extends AbstractList<E> {
    String className;
    Class<E> classSpec;
    private long currentTableViewVersion;
    private boolean isCompleted;
    private final List<RealmChangeListener> listeners;
    private Future<Long> pendingQuery;
    private final TableQuery query;
    BaseRealm realm;
    private TableOrView table;

    static <E extends RealmObject> RealmResults<E> createFromTableOrView(BaseRealm realm, TableOrView table, Class<E> clazz) {
        return new RealmResults<>(realm, table, clazz);
    }

    static RealmResults<DynamicRealmObject> createFromDynamicTableOrView(BaseRealm realm, TableOrView table, String className) {
        return new RealmResults<>(realm, table, className);
    }

    private RealmResults(BaseRealm realm, TableOrView table, Class<E> classSpec) {
        this.table = null;
        this.currentTableViewVersion = -1L;
        this.listeners = new CopyOnWriteArrayList();
        this.isCompleted = false;
        this.realm = realm;
        this.classSpec = classSpec;
        this.table = table;
        this.pendingQuery = null;
        this.query = null;
        this.currentTableViewVersion = table.sync();
    }

    private RealmResults(BaseRealm realm, String className) {
        this.table = null;
        this.currentTableViewVersion = -1L;
        this.listeners = new CopyOnWriteArrayList();
        this.isCompleted = false;
        this.realm = realm;
        this.className = className;
        this.pendingQuery = null;
        this.query = null;
    }

    private RealmResults(BaseRealm realm, TableOrView table, String className) {
        this(realm, className);
        this.table = table;
    }

    TableOrView getTable() {
        return this.table == null ? this.realm.schema.getTable((Class<? extends RealmObject>) this.classSpec) : this.table;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i) {
        this.realm.checkIfValid();
        TableOrView table = getTable();
        if (table instanceof TableView) {
            return (E) this.realm.get(this.classSpec, this.className, ((TableView) table).getSourceRowIndex(i));
        }
        return (E) this.realm.get(this.classSpec, this.className, i);
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object o) {
        throw new NoSuchMethodError("indexOf is not supported on RealmResults");
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return !isLoaded() ? Collections.emptyList().iterator() : new RealmResultsIterator();
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return !isLoaded() ? Collections.emptyList().listIterator() : new RealmResultsListIterator(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int location) {
        return !isLoaded() ? Collections.emptyList().listIterator(location) : new RealmResultsListIterator(location);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        if (isLoaded()) {
            return Long.valueOf(getTable().size()).intValue();
        }
        return 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int index) {
        this.realm.checkIfValid();
        TableOrView table = getTable();
        table.remove(index);
        return null;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.realm.checkIfValid();
        TableOrView table = getTable();
        table.clear();
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    @Deprecated
    public boolean add(E element) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractList, java.util.List
    @Deprecated
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    private class RealmResultsIterator implements Iterator<E> {
        int pos = -1;
        long tableViewVersion;

        RealmResultsIterator() {
            this.tableViewVersion = 0L;
            this.tableViewVersion = RealmResults.this.table.sync();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            assertRealmIsStable();
            return this.pos + 1 < RealmResults.this.size();
        }

        @Override // java.util.Iterator
        public E next() {
            assertRealmIsStable();
            this.pos++;
            if (this.pos >= RealmResults.this.size()) {
                throw new IndexOutOfBoundsException("Cannot access index " + this.pos + " when size is " + RealmResults.this.size() + ". Remember to check hasNext() before using next().");
            }
            return (E) RealmResults.this.get(this.pos);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new RealmException("Removing is not supported.");
        }

        protected void assertRealmIsStable() {
            long version = RealmResults.this.table.sync();
            if (this.tableViewVersion > -1 && version != this.tableViewVersion) {
                throw new ConcurrentModificationException("No outside changes to a Realm is allowed while iterating a RealmResults. Use iterators methods instead.");
            }
            this.tableViewVersion = version;
        }
    }

    private class RealmResultsListIterator extends RealmResults<E>.RealmResultsIterator implements ListIterator<E> {
        RealmResultsListIterator(int start) {
            super();
            if (start >= 0 && start <= RealmResults.this.size()) {
                this.pos = start - 1;
                return;
            }
            throw new IndexOutOfBoundsException("Starting location must be a valid index: [0, " + (RealmResults.this.size() - 1) + "]. Yours was " + start);
        }

        @Override // java.util.ListIterator
        public void add(E object) {
            throw new RealmException("Adding elements not supported. Use Realm.createObject() instead.");
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            assertRealmIsStable();
            return this.pos > 0;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            assertRealmIsStable();
            return this.pos + 1;
        }

        @Override // java.util.ListIterator
        public E previous() {
            assertRealmIsStable();
            this.pos--;
            if (this.pos < 0) {
                throw new IndexOutOfBoundsException("Cannot access index less than zero. This was " + this.pos + ". Remember to check hasPrevious() before using previous().");
            }
            return (E) RealmResults.this.get(this.pos);
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            assertRealmIsStable();
            return this.pos;
        }

        @Override // java.util.ListIterator
        public void set(E object) {
            throw new RealmException("Replacing elements not supported.");
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new RealmException("Removing elements not supported.");
        }
    }

    void swapTableViewPointer(long handoverTableViewPointer) {
        this.table = this.query.importHandoverTableView(handoverTableViewPointer, this.realm.sharedGroupManager.getNativePointer());
        this.isCompleted = true;
    }

    public boolean isLoaded() {
        this.realm.checkIfValid();
        return this.pendingQuery == null || this.isCompleted;
    }

    void notifyChangeListeners() {
        if (this.listeners != null && !this.listeners.isEmpty()) {
            if (this.pendingQuery == null || this.isCompleted) {
                long version = this.table.sync();
                if (this.currentTableViewVersion != version) {
                    this.currentTableViewVersion = version;
                    for (RealmChangeListener listener : this.listeners) {
                        listener.onChange();
                    }
                }
            }
        }
    }
}
