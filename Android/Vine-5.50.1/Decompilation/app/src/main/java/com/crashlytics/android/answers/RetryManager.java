package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.concurrency.internal.RetryState;

/* loaded from: classes.dex */
class RetryManager {
    long lastRetry;
    private RetryState retryState;

    public RetryManager(RetryState retryState) {
        if (retryState == null) {
            throw new NullPointerException("retryState must not be null");
        }
        this.retryState = retryState;
    }

    public boolean canRetry(long timeNanos) {
        long delayInNanos = 1000000 * this.retryState.getRetryDelay();
        return timeNanos - this.lastRetry >= delayInNanos;
    }

    public void recordRetry(long timeNanos) {
        this.lastRetry = timeNanos;
        this.retryState = this.retryState.nextRetryState();
    }

    public void reset() {
        this.lastRetry = 0L;
        this.retryState = this.retryState.initialRetryState();
    }
}
