package co.vine.android.feedadapter;

import android.widget.AbsListView;
import co.vine.android.views.ListState;

/* loaded from: classes.dex */
public class ArrayListScrollListener implements AbsListView.OnScrollListener {
    private ListState mListState = new ListState();

    protected int getScrollState() {
        return this.mListState.scrollState;
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mListState.firstVisibleItem = firstVisibleItem;
        this.mListState.visibleItemCount = visibleItemCount;
        this.mListState.totalItemCount = totalItemCount;
        if (visibleItemCount != 0 && firstVisibleItem > 0) {
            int position = firstVisibleItem + visibleItemCount;
            if (totalItemCount > 0 && position >= totalItemCount - 1 && this.mListState.hasNewScrollState) {
                this.mListState.hasNewScrollState = false;
                onScrollLastItem(totalItemCount);
            }
        }
    }

    @Override // android.widget.AbsListView.OnScrollListener
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.mListState.hasNewScrollState = true;
        this.mListState.scrollState = scrollState;
        if (scrollState == 0) {
            onScroll(view, this.mListState.firstVisibleItem, this.mListState.visibleItemCount, this.mListState.totalItemCount);
        }
    }

    protected void onScrollLastItem(int totalItemCount) {
    }
}
