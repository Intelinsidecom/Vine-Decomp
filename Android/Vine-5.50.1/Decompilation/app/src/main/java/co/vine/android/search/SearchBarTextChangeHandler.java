package co.vine.android.search;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import co.vine.android.search.SearchActivity;
import com.twitter.android.widget.RefreshableListView;

/* loaded from: classes.dex */
public final class SearchBarTextChangeHandler extends Handler {
    private RefreshableListView mListView;
    private SearchActivity.SearchPendingRequestHelper mPendingRequestHelper;
    private SearchActivity mSearchActivity;
    private SearchStatePresenter mSearchStatePresenter;

    public SearchBarTextChangeHandler(SearchActivity searchActivity, SearchStatePresenter searchStatePresenter, SearchActivity.SearchPendingRequestHelper pendingRequestHelper, RefreshableListView listView) {
        super(Looper.getMainLooper());
        this.mSearchActivity = searchActivity;
        this.mSearchStatePresenter = searchStatePresenter;
        this.mPendingRequestHelper = pendingRequestHelper;
        this.mListView = listView;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        CharSequence s = (CharSequence) msg.obj;
        switch (msg.what) {
            case 1:
                removeMessages(4);
                if (s != null && s.length() > 1) {
                    if (!this.mSearchStatePresenter.isInTypingState() && !this.mSearchStatePresenter.isInEmptyState()) {
                        this.mSearchStatePresenter.moveToTypingState();
                        this.mPendingRequestHelper.showProgress(3);
                    }
                    this.mSearchStatePresenter.getFeedAdapter().clearData();
                    this.mSearchStatePresenter.setQueryString(this.mSearchActivity.getSearchQuery(), false);
                    this.mListView.scrollTop();
                    break;
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(s)) {
                    String query = s.toString();
                    this.mSearchActivity.fetchTypeahead(query);
                    Message fetchPosts = Message.obtain();
                    fetchPosts.what = 4;
                    fetchPosts.obj = query;
                    sendMessageDelayed(fetchPosts, 500L);
                    break;
                }
                break;
            case 3:
                removeMessages(4);
                this.mSearchStatePresenter.moveToStartedState();
                break;
            case 4:
                String lastQuery = (String) msg.obj;
                if (lastQuery != null && lastQuery.trim().equals(this.mSearchActivity.getSearchQuery())) {
                    this.mSearchActivity.fetchPosts(this.mSearchActivity.getSearchQuery());
                    break;
                }
                break;
        }
    }
}
