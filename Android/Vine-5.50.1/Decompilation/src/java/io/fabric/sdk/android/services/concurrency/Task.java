package io.fabric.sdk.android.services.concurrency;

/* loaded from: classes.dex */
public interface Task {
    boolean isFinished();

    void setError(Throwable th);

    void setFinished(boolean z);
}
