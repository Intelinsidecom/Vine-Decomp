package io.realm.internal.async;

import android.os.Handler;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.internal.SharedGroup;
import io.realm.internal.TableQuery;
import io.realm.internal.log.RealmLog;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/* loaded from: classes.dex */
public class QueryUpdateTask implements Runnable {
    private WeakReference<Handler> callerHandler;
    private int message;
    private RealmConfiguration realmConfiguration;
    private Builder.QueryEntry realmObjectEntry;
    private List<Builder.QueryEntry> realmResultsEntries;
    private final int updateMode;

    private QueryUpdateTask(int mode, RealmConfiguration realmConfiguration, List<Builder.QueryEntry> listOfRealmResults, Builder.QueryEntry realmObject, WeakReference<Handler> handler, int message) {
        this.updateMode = mode;
        this.realmConfiguration = realmConfiguration;
        this.realmResultsEntries = listOfRealmResults;
        this.realmObjectEntry = realmObject;
        this.callerHandler = handler;
        this.message = message;
    }

    public static Builder.RealmConfigurationStep newBuilder() {
        return new Builder.Steps();
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        Result result;
        boolean updateSuccessful;
        SharedGroup sharedGroup = null;
        try {
            try {
                SharedGroup sharedGroup2 = new SharedGroup(this.realmConfiguration.getPath(), true, this.realmConfiguration.getDurability(), this.realmConfiguration.getEncryptionKey());
                try {
                    if (this.updateMode == 0) {
                        result = Result.newRealmResultsResponse();
                        AlignedQueriesParameters alignedParameters = prepareQueriesParameters();
                        long[] handoverTableViewPointer = TableQuery.nativeBatchUpdateQueries(sharedGroup2.getNativePointer(), sharedGroup2.getNativeReplicationPointer(), alignedParameters.handoverQueries, alignedParameters.queriesParameters, alignedParameters.multiSortColumnIndices, alignedParameters.multiSortOrder);
                        swapPointers(result, handoverTableViewPointer);
                        updateSuccessful = true;
                        result.versionID = sharedGroup2.getVersion();
                    } else {
                        result = Result.newRealmObjectResponse();
                        updateSuccessful = updateRealmObjectQuery(sharedGroup2, result);
                        result.versionID = sharedGroup2.getVersion();
                    }
                    Handler handler = this.callerHandler.get();
                    if (updateSuccessful && !isTaskCancelled() && isAliveHandler(handler)) {
                        handler.obtainMessage(this.message, result).sendToTarget();
                    }
                    if (sharedGroup2 != null) {
                        sharedGroup2.close();
                        sharedGroup = sharedGroup2;
                    } else {
                        sharedGroup = sharedGroup2;
                    }
                } catch (Exception e) {
                    e = e;
                    sharedGroup = sharedGroup2;
                    RealmLog.e(e.getMessage(), e);
                    if (sharedGroup != null) {
                        sharedGroup.close();
                    }
                } catch (Throwable th) {
                    th = th;
                    sharedGroup = sharedGroup2;
                    if (sharedGroup != null) {
                        sharedGroup.close();
                    }
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private AlignedQueriesParameters prepareQueriesParameters() {
        long[] handoverQueries = new long[this.realmResultsEntries.size()];
        long[][] queriesParameters = (long[][]) Array.newInstance((Class<?>) Long.TYPE, this.realmResultsEntries.size(), 6);
        long[][] multiSortColumnIndices = new long[this.realmResultsEntries.size()][];
        boolean[][] multiSortOrder = new boolean[this.realmResultsEntries.size()][];
        int i = 0;
        for (Builder.QueryEntry queryEntry : this.realmResultsEntries) {
            switch (queryEntry.queryArguments.type) {
                case 0:
                    handoverQueries[i] = queryEntry.handoverQueryPointer;
                    queriesParameters[i][0] = 0;
                    queriesParameters[i][1] = 0;
                    queriesParameters[i][2] = -1;
                    queriesParameters[i][3] = -1;
                    break;
                case 1:
                    handoverQueries[i] = queryEntry.handoverQueryPointer;
                    queriesParameters[i][0] = 1;
                    queriesParameters[i][1] = 0;
                    queriesParameters[i][2] = -1;
                    queriesParameters[i][3] = -1;
                    queriesParameters[i][4] = queryEntry.queryArguments.columnIndex;
                    queriesParameters[i][5] = queryEntry.queryArguments.sortOrder.getValue() ? 1L : 0L;
                    break;
                case 2:
                    handoverQueries[i] = queryEntry.handoverQueryPointer;
                    queriesParameters[i][0] = 2;
                    queriesParameters[i][1] = 0;
                    queriesParameters[i][2] = -1;
                    queriesParameters[i][3] = -1;
                    multiSortColumnIndices[i] = queryEntry.queryArguments.columnIndices;
                    multiSortOrder[i] = TableQuery.getNativeSortOrderValues(queryEntry.queryArguments.sortOrders);
                    break;
                case 3:
                default:
                    throw new IllegalArgumentException("Query mode " + queryEntry.queryArguments.type + " not supported");
                case 4:
                    handoverQueries[i] = queryEntry.handoverQueryPointer;
                    queriesParameters[i][0] = 4;
                    queriesParameters[i][1] = queryEntry.queryArguments.columnIndex;
                    break;
            }
            i++;
        }
        AlignedQueriesParameters alignedParameters = new AlignedQueriesParameters();
        alignedParameters.handoverQueries = handoverQueries;
        alignedParameters.multiSortColumnIndices = multiSortColumnIndices;
        alignedParameters.multiSortOrder = multiSortOrder;
        alignedParameters.queriesParameters = queriesParameters;
        return alignedParameters;
    }

    private void swapPointers(Result result, long[] handoverTableViewPointer) {
        int i = 0;
        for (Builder.QueryEntry queryEntry : this.realmResultsEntries) {
            result.updatedTableViews.put(queryEntry.element, Long.valueOf(handoverTableViewPointer[i]));
            i++;
        }
    }

    private boolean updateRealmObjectQuery(SharedGroup sharedGroup, Result result) {
        if (!isTaskCancelled()) {
            switch (this.realmObjectEntry.queryArguments.type) {
                case 3:
                    long handoverRowPointer = TableQuery.nativeFindWithHandover(sharedGroup.getNativePointer(), sharedGroup.getNativeReplicationPointer(), this.realmObjectEntry.handoverQueryPointer, 0L);
                    result.updatedRow.put(this.realmObjectEntry.element, Long.valueOf(handoverRowPointer));
                    return true;
                default:
                    throw new IllegalArgumentException("Query mode " + this.realmObjectEntry.queryArguments.type + " not supported");
            }
        }
        TableQuery.nativeCloseQueryHandover(this.realmObjectEntry.handoverQueryPointer);
        return false;
    }

    private boolean isTaskCancelled() {
        return Thread.currentThread().isInterrupted();
    }

    private boolean isAliveHandler(Handler handler) {
        return handler != null && handler.getLooper().getThread().isAlive();
    }

    public static class Result {
        public IdentityHashMap<WeakReference<RealmObject>, Long> updatedRow;
        public IdentityHashMap<WeakReference<RealmResults<? extends RealmObject>>, Long> updatedTableViews;
        public SharedGroup.VersionID versionID;

        public static Result newRealmResultsResponse() {
            Result result = new Result();
            result.updatedTableViews = new IdentityHashMap<>(1);
            return result;
        }

        public static Result newRealmObjectResponse() {
            Result result = new Result();
            result.updatedRow = new IdentityHashMap<>(1);
            return result;
        }
    }

    private static class AlignedQueriesParameters {
        long[] handoverQueries;
        long[][] multiSortColumnIndices;
        boolean[][] multiSortOrder;
        long[][] queriesParameters;

        private AlignedQueriesParameters() {
        }
    }

    public static class Builder {

        public interface BuilderStep {
            QueryUpdateTask build();
        }

        public interface HandlerStep {
            BuilderStep sendToHandler(Handler handler, int i);
        }

        public interface RealmConfigurationStep {
            UpdateQueryStep realmConfiguration(RealmConfiguration realmConfiguration);
        }

        public interface RealmResultsQueryStep {
            BuilderStep sendToHandler(Handler handler, int i);
        }

        public interface UpdateQueryStep {
            RealmResultsQueryStep add(WeakReference<RealmResults<? extends RealmObject>> weakReference, long j, ArgumentsHolder argumentsHolder);

            HandlerStep addObject(WeakReference<? extends RealmObject> weakReference, long j, ArgumentsHolder argumentsHolder);
        }

        private static class Steps implements BuilderStep, HandlerStep, RealmConfigurationStep, RealmResultsQueryStep, UpdateQueryStep {
            private WeakReference<Handler> callerHandler;
            private int message;
            private RealmConfiguration realmConfiguration;
            private QueryEntry realmObjectEntry;
            private List<QueryEntry> realmResultsEntries;

            private Steps() {
            }

            @Override // io.realm.internal.async.QueryUpdateTask.Builder.RealmConfigurationStep
            public UpdateQueryStep realmConfiguration(RealmConfiguration realmConfiguration) {
                this.realmConfiguration = realmConfiguration;
                return this;
            }

            @Override // io.realm.internal.async.QueryUpdateTask.Builder.UpdateQueryStep
            public RealmResultsQueryStep add(WeakReference<RealmResults<?>> weakReference, long handoverQueryPointer, ArgumentsHolder queryArguments) {
                if (this.realmResultsEntries == null) {
                    this.realmResultsEntries = new ArrayList(1);
                }
                this.realmResultsEntries.add(new QueryEntry(weakReference, handoverQueryPointer, queryArguments));
                return this;
            }

            @Override // io.realm.internal.async.QueryUpdateTask.Builder.UpdateQueryStep
            public HandlerStep addObject(WeakReference<? extends RealmObject> weakReference, long handoverQueryPointer, ArgumentsHolder queryArguments) {
                this.realmObjectEntry = new QueryEntry(weakReference, handoverQueryPointer, queryArguments);
                return this;
            }

            @Override // io.realm.internal.async.QueryUpdateTask.Builder.HandlerStep, io.realm.internal.async.QueryUpdateTask.Builder.RealmResultsQueryStep
            public BuilderStep sendToHandler(Handler handler, int message) {
                this.callerHandler = new WeakReference<>(handler);
                this.message = message;
                return this;
            }

            @Override // io.realm.internal.async.QueryUpdateTask.Builder.BuilderStep
            public QueryUpdateTask build() {
                return new QueryUpdateTask(this.realmResultsEntries != null ? 0 : 1, this.realmConfiguration, this.realmResultsEntries, this.realmObjectEntry, this.callerHandler, this.message);
            }
        }

        private static class QueryEntry {
            final WeakReference element;
            long handoverQueryPointer;
            final ArgumentsHolder queryArguments;

            private QueryEntry(WeakReference element, long handoverQueryPointer, ArgumentsHolder queryArguments) {
                this.element = element;
                this.handoverQueryPointer = handoverQueryPointer;
                this.queryArguments = queryArguments;
            }
        }
    }
}
