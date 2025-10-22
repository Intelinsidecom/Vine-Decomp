package io.realm;

import android.os.Handler;
import android.os.Message;
import io.realm.internal.IdentitySet;
import io.realm.internal.Row;
import io.realm.internal.SharedGroup;
import io.realm.internal.async.QueryUpdateTask;
import io.realm.internal.log.RealmLog;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public class HandlerController implements Handler.Callback {
    final BaseRealm realm;
    private Future updateAsyncQueriesTask;
    protected final CopyOnWriteArrayList<RealmChangeListener> changeListeners = new CopyOnWriteArrayList<>();
    protected final List<WeakReference<RealmChangeListener>> weakChangeListeners = new CopyOnWriteArrayList();
    final ReferenceQueue<RealmResults<? extends RealmObject>> referenceQueueAsyncRealmResults = new ReferenceQueue<>();
    final ReferenceQueue<RealmResults<? extends RealmObject>> referenceQueueSyncRealmResults = new ReferenceQueue<>();
    final ReferenceQueue<RealmObject> referenceQueueRealmObject = new ReferenceQueue<>();
    final Map<WeakReference<RealmResults<? extends RealmObject>>, RealmQuery<? extends RealmObject>> asyncRealmResults = new IdentityHashMap();
    final Map<WeakReference<RealmObject>, RealmQuery<? extends RealmObject>> emptyAsyncRealmObject = new IdentityHashMap();
    final IdentitySet<WeakReference<RealmResults<? extends RealmObject>>> syncRealmResults = new IdentitySet<>();
    final Map<WeakReference<RealmObject>, RealmQuery<? extends RealmObject>> realmObjects = new IdentityHashMap();

    public HandlerController(BaseRealm realm) {
        this.realm = realm;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (this.realm.sharedGroupManager != null) {
            switch (message.what) {
                case 14930352:
                    realmChanged();
                    return true;
                case 24157817:
                    QueryUpdateTask.Result result = (QueryUpdateTask.Result) message.obj;
                    completedAsyncQueriesUpdate(result);
                    return true;
                case 39088169:
                    QueryUpdateTask.Result result2 = (QueryUpdateTask.Result) message.obj;
                    completedAsyncRealmResults(result2);
                    return true;
                case 63245986:
                    QueryUpdateTask.Result result3 = (QueryUpdateTask.Result) message.obj;
                    completedAsyncRealmObject(result3);
                    return true;
                case 102334155:
                    throw ((Error) message.obj);
                default:
                    return true;
            }
        }
        return true;
    }

    void notifyGlobalListeners() {
        Iterator<RealmChangeListener> iteratorStrongListeners = this.changeListeners.iterator();
        while (iteratorStrongListeners.hasNext()) {
            iteratorStrongListeners.next().onChange();
        }
        List<WeakReference<RealmChangeListener>> toRemoveList = null;
        for (WeakReference<RealmChangeListener> weakRef : this.weakChangeListeners) {
            RealmChangeListener listener = weakRef.get();
            if (listener == null) {
                if (toRemoveList == null) {
                    toRemoveList = new ArrayList<>(this.weakChangeListeners.size());
                }
                toRemoveList.add(weakRef);
            } else {
                listener.onChange();
            }
        }
        if (toRemoveList != null) {
            this.weakChangeListeners.removeAll(toRemoveList);
        }
    }

    void notifyTypeBasedListeners() {
        notifyAsyncRealmResultsCallbacks();
        notifySyncRealmResultsCallbacks();
        notifyRealmObjectCallbacks();
    }

    void updateAsyncEmptyRealmObject() {
        Iterator<Map.Entry<WeakReference<RealmObject>, RealmQuery<?>>> iterator = this.emptyAsyncRealmObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WeakReference<RealmObject>, RealmQuery<?>> next = iterator.next();
            if (next.getKey().get() != null) {
                Realm.asyncQueryExecutor.submit(QueryUpdateTask.newBuilder().realmConfiguration(this.realm.getConfiguration()).addObject(next.getKey(), next.getValue().handoverQueryPointer(), next.getValue().getArgument()).sendToHandler(this.realm.handler, 63245986).build());
            } else {
                iterator.remove();
            }
        }
    }

    private void notifyAsyncRealmResultsCallbacks() {
        notifyRealmResultsCallbacks(this.asyncRealmResults.keySet().iterator());
    }

    private void notifySyncRealmResultsCallbacks() {
        notifyRealmResultsCallbacks(this.syncRealmResults.keySet().iterator());
    }

    private void notifyRealmResultsCallbacks(Iterator<WeakReference<RealmResults<? extends RealmObject>>> iterator) {
        List<RealmResults<? extends RealmObject>> resultsToBeNotified = new ArrayList<>();
        while (iterator.hasNext()) {
            WeakReference<RealmResults<? extends RealmObject>> weakRealmResults = iterator.next();
            RealmResults<? extends RealmObject> realmResults = weakRealmResults.get();
            if (realmResults == null) {
                iterator.remove();
            } else {
                resultsToBeNotified.add(realmResults);
            }
        }
        Iterator<RealmResults<? extends RealmObject>> it = resultsToBeNotified.iterator();
        while (it.hasNext()) {
            it.next().notifyChangeListeners();
        }
    }

    private void notifyRealmObjectCallbacks() {
        List<RealmObject> objectsToBeNotified = new ArrayList<>();
        Iterator<WeakReference<RealmObject>> iterator = this.realmObjects.keySet().iterator();
        while (iterator.hasNext()) {
            WeakReference<? extends RealmObject> weakRealmObject = iterator.next();
            RealmObject realmObject = weakRealmObject.get();
            if (realmObject == null) {
                iterator.remove();
            } else if (realmObject.row.isAttached()) {
                objectsToBeNotified.add(realmObject);
            } else if (realmObject.row != Row.EMPTY_ROW) {
                iterator.remove();
            }
        }
        Iterator<RealmObject> it = objectsToBeNotified.iterator();
        while (it.hasNext()) {
            it.next().notifyChangeListeners();
        }
    }

    private void updateAsyncQueries() {
        if (this.updateAsyncQueriesTask != null && !this.updateAsyncQueriesTask.isDone()) {
            this.updateAsyncQueriesTask.cancel(true);
            Realm.asyncQueryExecutor.getQueue().remove(this.updateAsyncQueriesTask);
            RealmLog.d("REALM_CHANGED realm:" + this + " cancelling pending COMPLETED_UPDATE_ASYNC_QUERIES updates");
        }
        RealmLog.d("REALM_CHANGED realm:" + this + " updating async queries, total: " + this.asyncRealmResults.size());
        QueryUpdateTask.Builder.UpdateQueryStep updateQueryStep = QueryUpdateTask.newBuilder().realmConfiguration(this.realm.getConfiguration());
        QueryUpdateTask.Builder.RealmResultsQueryStep realmResultsQueryStep = null;
        Iterator<Map.Entry<WeakReference<RealmResults<? extends RealmObject>>, RealmQuery<?>>> iterator = this.asyncRealmResults.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WeakReference<RealmResults<? extends RealmObject>>, RealmQuery<?>> entry = iterator.next();
            WeakReference<RealmResults<? extends RealmObject>> weakReference = entry.getKey();
            RealmResults<? extends RealmObject> realmResults = weakReference.get();
            if (realmResults == null) {
                iterator.remove();
            } else {
                realmResultsQueryStep = updateQueryStep.add(weakReference, entry.getValue().handoverQueryPointer(), entry.getValue().getArgument());
            }
        }
        if (realmResultsQueryStep != null) {
            QueryUpdateTask queryUpdateTask = realmResultsQueryStep.sendToHandler(this.realm.handler, 24157817).build();
            this.updateAsyncQueriesTask = Realm.asyncQueryExecutor.submit(queryUpdateTask);
        }
    }

    private void realmChanged() {
        deleteWeakReferences();
        if (threadContainsAsyncQueries()) {
            updateAsyncQueries();
            return;
        }
        RealmLog.d("REALM_CHANGED realm:" + this + " no async queries, advance_read");
        this.realm.sharedGroupManager.advanceRead();
        notifyGlobalListeners();
        if (!this.realm.isClosed()) {
            notifySyncRealmResultsCallbacks();
        }
        if (!this.realm.isClosed()) {
            notifyRealmObjectCallbacks();
        }
        if (!this.realm.isClosed() && threadContainsAsyncEmptyRealmObject()) {
            updateAsyncEmptyRealmObject();
        }
    }

    private void completedAsyncRealmResults(QueryUpdateTask.Result result) {
        Set<WeakReference<RealmResults<? extends RealmObject>>> updatedTableViewsKeys = result.updatedTableViews.keySet();
        if (updatedTableViewsKeys.size() > 0) {
            WeakReference<RealmResults<? extends RealmObject>> weakRealmResults = updatedTableViewsKeys.iterator().next();
            RealmResults<? extends RealmObject> realmResults = weakRealmResults.get();
            if (realmResults == null) {
                this.asyncRealmResults.remove(weakRealmResults);
                RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] realm:" + this + " RealmResults GC'd ignore results");
                return;
            }
            SharedGroup.VersionID callerVersionID = this.realm.sharedGroupManager.getVersion();
            int compare = callerVersionID.compareTo(result.versionID);
            if (compare == 0) {
                if (!realmResults.isLoaded()) {
                    RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] , realm:" + this + " same versions, using results (RealmResults is not loaded)");
                    realmResults.swapTableViewPointer(result.updatedTableViews.get(weakRealmResults).longValue());
                    realmResults.notifyChangeListeners();
                    return;
                }
                RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] , realm:" + this + " ignoring result the RealmResults (is already loaded)");
                return;
            }
            if (compare > 0) {
                if (!realmResults.isLoaded()) {
                    RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] , realm:" + this + " caller is more advanced & RealmResults is not loaded, rerunning the query against the latest version");
                    RealmQuery<?> query = this.asyncRealmResults.get(weakRealmResults);
                    QueryUpdateTask queryUpdateTask = QueryUpdateTask.newBuilder().realmConfiguration(this.realm.getConfiguration()).add(weakRealmResults, query.handoverQueryPointer(), query.getArgument()).sendToHandler(this.realm.handler, 39088169).build();
                    Realm.asyncQueryExecutor.submit(queryUpdateTask);
                    return;
                }
                RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] , realm:" + this + " caller is more advanced & RealmResults is loaded ignore the outdated result");
                return;
            }
            RealmLog.d("[COMPLETED_ASYNC_REALM_RESULTS " + weakRealmResults + "] , realm:" + this + " caller thread behind worker thread, ignore results (a batch update will update everything including this query)");
        }
    }

    private void completedAsyncQueriesUpdate(QueryUpdateTask.Result result) {
        SharedGroup.VersionID callerVersionID = this.realm.sharedGroupManager.getVersion();
        int compare = callerVersionID.compareTo(result.versionID);
        if (compare > 0) {
            RealmLog.d("COMPLETED_UPDATE_ASYNC_QUERIES realm:" + this + " caller is more advanced, rerun updates");
            this.realm.handler.sendEmptyMessage(14930352);
            return;
        }
        if (compare != 0) {
            RealmLog.d("COMPLETED_UPDATE_ASYNC_QUERIES realm:" + this + " caller is behind  advance_read");
            this.realm.sharedGroupManager.advanceRead(result.versionID);
        }
        ArrayList<RealmResults<? extends RealmObject>> callbacksToNotify = new ArrayList<>(result.updatedTableViews.size());
        for (Map.Entry<WeakReference<RealmResults<? extends RealmObject>>, Long> query : result.updatedTableViews.entrySet()) {
            WeakReference<RealmResults<? extends RealmObject>> weakRealmResults = query.getKey();
            RealmResults<? extends RealmObject> realmResults = weakRealmResults.get();
            if (realmResults == null) {
                this.asyncRealmResults.remove(weakRealmResults);
            } else {
                callbacksToNotify.add(realmResults);
                RealmLog.d("COMPLETED_UPDATE_ASYNC_QUERIES realm:" + this + " updating RealmResults " + weakRealmResults);
                realmResults.swapTableViewPointer(query.getValue().longValue());
            }
        }
        Iterator<RealmResults<? extends RealmObject>> it = callbacksToNotify.iterator();
        while (it.hasNext()) {
            it.next().notifyChangeListeners();
        }
        if (compare != 0) {
            notifyGlobalListeners();
            notifySyncRealmResultsCallbacks();
            notifyRealmObjectCallbacks();
        }
        this.updateAsyncQueriesTask = null;
    }

    private void completedAsyncRealmObject(QueryUpdateTask.Result result) {
        WeakReference<RealmObject> realmObjectWeakReference;
        RealmObject realmObject;
        Set<WeakReference<RealmObject>> updatedRowKey = result.updatedRow.keySet();
        if (updatedRowKey.size() > 0 && (realmObject = (realmObjectWeakReference = updatedRowKey.iterator().next()).get()) != null) {
            SharedGroup.VersionID callerVersionID = this.realm.sharedGroupManager.getVersion();
            int compare = callerVersionID.compareTo(result.versionID);
            if (compare == 0) {
                long rowPointer = result.updatedRow.get(realmObjectWeakReference).longValue();
                if (rowPointer != 0 && this.emptyAsyncRealmObject.containsKey(realmObjectWeakReference)) {
                    this.emptyAsyncRealmObject.remove(realmObjectWeakReference);
                    this.realmObjects.put(realmObjectWeakReference, null);
                }
                realmObject.onCompleted(Long.valueOf(rowPointer));
                realmObject.notifyChangeListeners();
                return;
            }
            if (compare > 0) {
                if (realmObject.isValid()) {
                    realmObject.notifyChangeListeners();
                    return;
                }
                RealmQuery<?> realmQuery = this.realmObjects.get(realmObjectWeakReference);
                if (realmQuery == null) {
                    realmQuery = this.emptyAsyncRealmObject.get(realmObjectWeakReference);
                }
                QueryUpdateTask queryUpdateTask = QueryUpdateTask.newBuilder().realmConfiguration(this.realm.getConfiguration()).addObject(realmObjectWeakReference, realmQuery.handoverQueryPointer(), realmQuery.getArgument()).sendToHandler(this.realm.handler, 63245986).build();
                Realm.asyncQueryExecutor.submit(queryUpdateTask);
                return;
            }
            throw new IllegalStateException("Caller thread behind the worker thread");
        }
    }

    private boolean threadContainsAsyncQueries() {
        boolean isEmpty = true;
        Iterator<Map.Entry<WeakReference<RealmResults<? extends RealmObject>>, RealmQuery<?>>> iterator = this.asyncRealmResults.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WeakReference<RealmResults<? extends RealmObject>>, RealmQuery<?>> next = iterator.next();
            if (next.getKey().get() == null) {
                iterator.remove();
            } else {
                isEmpty = false;
            }
        }
        return !isEmpty;
    }

    boolean threadContainsAsyncEmptyRealmObject() {
        boolean isEmpty = true;
        Iterator<Map.Entry<WeakReference<RealmObject>, RealmQuery<?>>> iterator = this.emptyAsyncRealmObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<WeakReference<RealmObject>, RealmQuery<?>> next = iterator.next();
            if (next.getKey().get() == null) {
                iterator.remove();
            } else {
                isEmpty = false;
            }
        }
        return !isEmpty;
    }

    private void deleteWeakReferences() {
        while (true) {
            Reference<? extends RealmResults<? extends RealmObject>> weakReferenceResults = this.referenceQueueAsyncRealmResults.poll();
            if (weakReferenceResults == null) {
                break;
            } else {
                this.asyncRealmResults.remove(weakReferenceResults);
            }
        }
        while (true) {
            Reference<? extends RealmResults<? extends RealmObject>> weakReferenceResults2 = this.referenceQueueSyncRealmResults.poll();
            if (weakReferenceResults2 == null) {
                break;
            } else {
                this.syncRealmResults.remove(weakReferenceResults2);
            }
        }
        while (true) {
            Reference<? extends RealmObject> weakReferenceObject = this.referenceQueueRealmObject.poll();
            if (weakReferenceObject != null) {
                this.realmObjects.remove(weakReferenceObject);
            } else {
                return;
            }
        }
    }

    void addToRealmResults(RealmResults<? extends RealmObject> realmResults) {
        WeakReference<RealmResults<? extends RealmObject>> realmResultsWeakReference = new WeakReference<>(realmResults, this.referenceQueueSyncRealmResults);
        this.syncRealmResults.add(realmResultsWeakReference);
    }

    <E extends RealmObject> void addToRealmObjects(E realmobject) {
        this.realmObjects.put(new WeakReference<>(realmobject), null);
    }
}
