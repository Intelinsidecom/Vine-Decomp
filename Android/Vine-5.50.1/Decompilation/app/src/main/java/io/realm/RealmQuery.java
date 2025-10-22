package io.realm;

import io.realm.RealmObject;
import io.realm.internal.LinkView;
import io.realm.internal.TableOrView;
import io.realm.internal.TableQuery;
import io.realm.internal.TableView;
import io.realm.internal.async.ArgumentsHolder;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class RealmQuery<E extends RealmObject> {
    private static final Long INVALID_NATIVE_POINTER = 0L;
    private ArgumentsHolder argumentsHolder;
    private String className;
    private Class<E> clazz;
    private TableQuery query;
    private BaseRealm realm;
    private RealmObjectSchema schema;
    private TableOrView table;
    private LinkView view = null;

    public static <E extends RealmObject> RealmQuery<E> createQuery(Realm realm, Class<E> clazz) {
        return new RealmQuery<>(realm, clazz);
    }

    private RealmQuery(Realm realm, Class<E> clazz) {
        this.realm = realm;
        this.clazz = clazz;
        this.schema = realm.schema.getSchemaForClass(clazz);
        this.table = this.schema.table;
        this.query = this.table.where();
    }

    public RealmQuery<E> equalTo(String fieldName, String value) {
        return equalTo(fieldName, value, Case.SENSITIVE);
    }

    public RealmQuery<E> equalTo(String fieldName, String value, Case casing) {
        long[] columnIndices = this.schema.getColumnIndices(fieldName, RealmFieldType.STRING);
        this.query.equalTo(columnIndices, value, casing);
        return this;
    }

    public RealmQuery<E> equalTo(String fieldName, Long value) {
        long[] columnIndices = this.schema.getColumnIndices(fieldName, RealmFieldType.INTEGER);
        if (value == null) {
            this.query.isNull(columnIndices);
        } else {
            this.query.equalTo(columnIndices, value.longValue());
        }
        return this;
    }

    public RealmResults<E> findAll() {
        RealmResults<E> realmResults;
        checkQueryIsNotReused();
        if (isDynamicQuery()) {
            realmResults = RealmResults.createFromDynamicTableOrView(this.realm, this.query.findAll(), this.className);
        } else {
            realmResults = RealmResults.createFromTableOrView(this.realm, this.query.findAll(), this.clazz);
        }
        if (this.realm.handlerController != null) {
            this.realm.handlerController.addToRealmResults(realmResults);
        }
        return realmResults;
    }

    private boolean isDynamicQuery() {
        return this.className != null;
    }

    public E findFirst() {
        checkQueryIsNotReused();
        long sourceRowIndexForFirstObject = getSourceRowIndexForFirstObject();
        if (sourceRowIndexForFirstObject < 0) {
            return null;
        }
        E e = (E) this.realm.get(this.clazz, this.className, sourceRowIndexForFirstObject);
        if (this.realm.handlerController != null) {
            this.realm.handlerController.realmObjects.put(new WeakReference<>(e, this.realm.handlerController.referenceQueueRealmObject), this);
            return e;
        }
        return e;
    }

    private void checkQueryIsNotReused() {
        if (this.argumentsHolder != null) {
            throw new IllegalStateException("This RealmQuery is already used by a find* query, please create a new query");
        }
    }

    private long getSourceRowIndexForFirstObject() {
        long rowIndex = this.query.find();
        if (rowIndex >= 0) {
            if (this.view != null) {
                return this.view.getTargetRowIndex(rowIndex);
            }
            if (this.table instanceof TableView) {
                return ((TableView) this.table).getSourceRowIndex(rowIndex);
            }
            return rowIndex;
        }
        return rowIndex;
    }

    public ArgumentsHolder getArgument() {
        return this.argumentsHolder;
    }

    long handoverQueryPointer() {
        return this.query.handoverQuery(this.realm.sharedGroupManager.getNativePointer());
    }
}
