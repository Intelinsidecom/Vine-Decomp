package co.vine.android;

import android.os.Bundle;
import android.widget.ListAdapter;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppSessionListener;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.util.LinkBuilderUtil;

/* loaded from: classes.dex */
public class SingleFragment extends BaseTimelineFragment {
    private final SingleFragmentWrapper mWrapper = new SingleFragmentWrapper();

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.mWrapper.onCreate(this, new SinglePostListener())) {
            setFocused(true);
        }
        setFlurryEventSource("Single Post");
        Bundle data = new Bundle();
        data.putLong("post_id", this.mWrapper.getPostId());
        this.mApiUrl = LinkBuilderUtil.buildUrl(7, data);
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mListView.setAdapter((ListAdapter) this.mFeedAdapter);
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mWrapper.onResume();
        this.mFeedAdapter.onResume(isFocused());
        if (this.mFetched) {
            return;
        }
        if (this.mWrapper.getPostId() > 0) {
            fetchPostContent(3, UrlCachePolicy.NETWORK_THEN_CACHE);
        } else if (this.mWrapper.getSharedId() != null) {
            fetchPostId(3);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mFeedAdapter.onPause(isFocused());
        this.mWrapper.onPause();
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected String fetchPosts(int page, String anchor, String backAnchor, boolean userInitiated, UrlCachePolicy cachePolicy) {
        throw new IllegalStateException("Cannot fetch on single post");
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected void fetchContent(int fetchType, boolean userInitiated, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
    }

    protected void fetchPostContent(int fetchType, UrlCachePolicy cachePolicy) {
        addRequest(this.mWrapper.fetchPostContent(cachePolicy), fetchType);
        this.mFetched = true;
    }

    private void fetchPostId(int fetchType) {
        addRequest(this.mWrapper.fetchPostId(), fetchType);
    }

    class SinglePostListener extends AppSessionListener {
        SinglePostListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetSinglePostComplete(String reqId, int statusCode, String reasonPhrase, VinePost post) {
            if (statusCode == 200) {
                SingleFragment.this.mFeedAdapter.mergePost(post);
            } else {
                SingleFragment.this.getActivity().finish();
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetPostIdComplete(String reqId, int statusCode, String reasonPhrase, long postId) {
            SingleFragment.this.mWrapper.onGetPostIdComplete(postId);
            SingleFragment.this.fetchPostContent(3, UrlCachePolicy.NETWORK_THEN_CACHE);
        }
    }
}
