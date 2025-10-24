package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.os.OperationCanceledException;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class AsyncTaskLoader<D> extends Loader<D> {
    static final boolean DEBUG = false;
    static final String TAG = "AsyncTaskLoader";

    /* JADX WARN: Incorrect inner types in field signature: Landroid/support/v4/content/AsyncTaskLoader<TD;>.android/support/v4/content/AsyncTaskLoader$android/support/v4/content/AsyncTaskLoader$LoadTask; */
    volatile LoadTask mCancellingTask;
    private final Executor mExecutor;
    Handler mHandler;
    long mLastLoadCompleteTime;

    /* JADX WARN: Incorrect inner types in field signature: Landroid/support/v4/content/AsyncTaskLoader<TD;>.android/support/v4/content/AsyncTaskLoader$android/support/v4/content/AsyncTaskLoader$LoadTask; */
    volatile LoadTask mTask;
    long mUpdateThrottle;

    public abstract D loadInBackground();

    final class LoadTask extends ModernAsyncTask<Void, Void, D> implements Runnable {
        private final CountDownLatch mDone = new CountDownLatch(1);
        boolean waiting;

        LoadTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.support.v4.content.ModernAsyncTask
        public D doInBackground(Void... voidArr) {
            try {
                return (D) AsyncTaskLoader.this.onLoadInBackground();
            } catch (OperationCanceledException e) {
                if (!isCancelled()) {
                    throw e;
                }
                return null;
            }
        }

        @Override // android.support.v4.content.ModernAsyncTask
        protected void onPostExecute(D data) {
            try {
                AsyncTaskLoader.this.dispatchOnLoadComplete(this, data);
            } finally {
                this.mDone.countDown();
            }
        }

        @Override // android.support.v4.content.ModernAsyncTask
        protected void onCancelled(D data) {
            try {
                AsyncTaskLoader.this.dispatchOnCancelled(this, data);
            } finally {
                this.mDone.countDown();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            this.waiting = false;
            AsyncTaskLoader.this.executePendingTask();
        }

        public void waitForLoader() throws InterruptedException {
            try {
                this.mDone.await();
            } catch (InterruptedException e) {
            }
        }
    }

    public AsyncTaskLoader(Context context) {
        this(context, ModernAsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AsyncTaskLoader(Context context, Executor executor) {
        super(context);
        this.mLastLoadCompleteTime = -10000L;
        this.mExecutor = executor;
    }

    public void setUpdateThrottle(long delayMS) {
        this.mUpdateThrottle = delayMS;
        if (delayMS != 0) {
            this.mHandler = new Handler();
        }
    }

    @Override // android.support.v4.content.Loader
    protected void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        this.mTask = new LoadTask();
        executePendingTask();
    }

    @Override // android.support.v4.content.Loader
    protected boolean onCancelLoad() {
        boolean cancelled = false;
        if (this.mTask != null) {
            if (this.mCancellingTask != null) {
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                this.mTask = null;
            } else if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
                this.mTask = null;
            } else {
                cancelled = this.mTask.cancel(false);
                if (cancelled) {
                    this.mCancellingTask = this.mTask;
                    cancelLoadInBackground();
                }
                this.mTask = null;
            }
        }
        return cancelled;
    }

    public void onCanceled(D data) {
    }

    void executePendingTask() {
        if (this.mCancellingTask == null && this.mTask != null) {
            if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
            }
            if (this.mUpdateThrottle > 0) {
                long now = SystemClock.uptimeMillis();
                if (now < this.mLastLoadCompleteTime + this.mUpdateThrottle) {
                    this.mTask.waiting = true;
                    this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
                    return;
                }
            }
            this.mTask.executeOnExecutor(this.mExecutor, (Void[]) null);
        }
    }

    /* JADX WARN: Generic types in debug info not equals: android.support.v4.content.AsyncTaskLoader$LoadTask != android.support.v4.content.AsyncTaskLoader<D>$android.support.v4.content.AsyncTaskLoader$android.support.v4.content.AsyncTaskLoader$LoadTask */
    /* JADX WARN: Incorrect inner types in method signature: (Landroid/support/v4/content/AsyncTaskLoader<TD;>.android/support/v4/content/AsyncTaskLoader$android/support/v4/content/AsyncTaskLoader$LoadTask;TD;)V */
    /* JADX WARN: Multi-variable type inference failed */
    void dispatchOnCancelled(LoadTask loadTask, Object obj) {
        onCanceled(obj);
        if (this.mCancellingTask == loadTask) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            deliverCancellation();
            executePendingTask();
        }
    }

    /* JADX WARN: Generic types in debug info not equals: android.support.v4.content.AsyncTaskLoader$LoadTask != android.support.v4.content.AsyncTaskLoader<D>$android.support.v4.content.AsyncTaskLoader$android.support.v4.content.AsyncTaskLoader$LoadTask */
    /* JADX WARN: Incorrect inner types in method signature: (Landroid/support/v4/content/AsyncTaskLoader<TD;>.android/support/v4/content/AsyncTaskLoader$android/support/v4/content/AsyncTaskLoader$LoadTask;TD;)V */
    /* JADX WARN: Multi-variable type inference failed */
    void dispatchOnLoadComplete(LoadTask loadTask, Object obj) {
        if (this.mTask != loadTask) {
            dispatchOnCancelled(loadTask, obj);
            return;
        }
        if (isAbandoned()) {
            onCanceled(obj);
            return;
        }
        commitContentChanged();
        this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
        this.mTask = null;
        deliverResult(obj);
    }

    protected D onLoadInBackground() {
        return loadInBackground();
    }

    public void cancelLoadInBackground() {
    }

    public boolean isLoadInBackgroundCanceled() {
        return this.mCancellingTask != null;
    }

    /* JADX WARN: Generic types in debug info not equals: android.support.v4.content.AsyncTaskLoader$LoadTask != android.support.v4.content.AsyncTaskLoader<D>$android.support.v4.content.AsyncTaskLoader$android.support.v4.content.AsyncTaskLoader$LoadTask */
    public void waitForLoader() throws InterruptedException {
        LoadTask loadTask = this.mTask;
        if (loadTask != null) {
            loadTask.waitForLoader();
        }
    }

    @Override // android.support.v4.content.Loader
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        if (this.mTask != null) {
            writer.print(prefix);
            writer.print("mTask=");
            writer.print(this.mTask);
            writer.print(" waiting=");
            writer.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            writer.print(prefix);
            writer.print("mCancellingTask=");
            writer.print(this.mCancellingTask);
            writer.print(" waiting=");
            writer.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            writer.print(prefix);
            writer.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, writer);
            writer.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), writer);
            writer.println();
        }
    }
}
