package io.realm.internal.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class RealmThreadPoolExecutor extends ThreadPoolExecutor {
    private static final int CORE_POOL_SIZE = (Runtime.getRuntime().availableProcessors() * 2) + 1;
    private static volatile RealmThreadPoolExecutor instance;
    private boolean isPaused;
    private ReentrantLock pauseLock;
    private Condition unpaused;

    public static RealmThreadPoolExecutor getInstance() {
        if (instance == null) {
            synchronized (RealmThreadPoolExecutor.class) {
                if (instance == null) {
                    instance = new RealmThreadPoolExecutor();
                }
            }
        }
        return instance;
    }

    private RealmThreadPoolExecutor() {
        super(CORE_POOL_SIZE, CORE_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(100));
        this.pauseLock = new ReentrantLock();
        this.unpaused = this.pauseLock.newCondition();
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable task) {
        return super.submit(new BgPriorityRunnable(task));
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new BgPriorityCallable(task));
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        this.pauseLock.lock();
        while (this.isPaused) {
            try {
                this.unpaused.await();
            } catch (InterruptedException e) {
                t.interrupt();
                return;
            } finally {
                this.pauseLock.unlock();
            }
        }
    }
}
