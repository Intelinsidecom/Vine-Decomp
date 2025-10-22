package com.twitter.android.widget;

/* loaded from: classes.dex */
public interface RefreshListener {
    ItemPosition getFirstItemPosition();

    int getPositionForItemId(long j);

    void onRefreshCancelled();

    void onRefreshFinished();

    void onRefreshFinishedNewData();

    void onRefreshFinishedNoChange();

    void onRefreshPulled();

    void onRefreshReleased(boolean z);
}
