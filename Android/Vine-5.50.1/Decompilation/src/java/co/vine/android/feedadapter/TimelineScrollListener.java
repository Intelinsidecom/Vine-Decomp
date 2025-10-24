package co.vine.android.feedadapter;

import android.widget.AbsListView;
import co.vine.android.feedadapter.v2.FeedAdapter;

/* loaded from: classes.dex */
public class TimelineScrollListener extends ArrayListScrollListener {
    protected FeedAdapter mFeedAdapter;

    public TimelineScrollListener() {
    }

    public TimelineScrollListener(FeedAdapter feedAdapter) {
        this.mFeedAdapter = feedAdapter;
    }

    @Override // co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
        if (this.mFeedAdapter != null && scrollState == 0) {
            this.mFeedAdapter.onScrollIdle();
        }
    }

    public void setFeedAdapter(FeedAdapter feedAdapter) {
        this.mFeedAdapter = feedAdapter;
    }
}
