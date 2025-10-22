package com.google.android.exoplayer.upstream;

import java.util.PriorityQueue;

/* loaded from: classes.dex */
public final class NetworkLock {
    public static final NetworkLock instance = new NetworkLock();
    private final Object lock = new Object();
    private final PriorityQueue<Integer> queue = new PriorityQueue<>();
    private int highestPriority = Integer.MAX_VALUE;

    private NetworkLock() {
    }

    public void add(int priority) {
        synchronized (this.lock) {
            this.queue.add(Integer.valueOf(priority));
            this.highestPriority = Math.min(this.highestPriority, priority);
        }
    }

    public void remove(int priority) {
        synchronized (this.lock) {
            this.queue.remove(Integer.valueOf(priority));
            this.highestPriority = this.queue.isEmpty() ? Integer.MAX_VALUE : this.queue.peek().intValue();
            this.lock.notifyAll();
        }
    }
}
