package co.vine.android.search;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.AppStateProviderSingleton;
import co.vine.android.BaseControllerActionBarActivity;
import co.vine.android.ChannelActivity;
import co.vine.android.LongformActivity;
import co.vine.android.PendingRequest;
import co.vine.android.PendingRequestHelper;
import co.vine.android.PostOptionsDialogActivity;
import co.vine.android.R;
import co.vine.android.api.ListSection;
import co.vine.android.api.SearchResult;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineFeed;
import co.vine.android.api.VinePost;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.TimelineActionResponseHandler;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.TimelineScrollListener;
import co.vine.android.feedadapter.viewholder.CardViewHolder;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.TimelineItemScribeActionsLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.share.activities.PostShareActivity;
import co.vine.android.util.Util;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.FakeActionBar;
import co.vine.android.widget.Typefaces;
import com.edisonwang.android.slog.SLogger;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SearchActivity extends BaseControllerActionBarActivity {
    private ImageView mClearButton;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private RefreshableListView mListView;
    private SearchPendingRequestHelper mPendingRequestHelper;
    private String mPostsAnchor;
    private RequestKeyGetter mRequestKeyGetter;
    private SearchBarEditText mSearchBar;
    private SearchStatePresenter mSearchStatePresenter;
    private TimelineActionResponseHandler mTimelineActionResponseHandler;
    private TimelineItemScribeActionsLogger mTimelineItemScribeActionsLogger;
    private View.OnFocusChangeListener mQueryOnFocusChangeListener = new View.OnFocusChangeListener() { // from class: co.vine.android.search.SearchActivity.2
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus && !SearchActivity.this.mSearchStatePresenter.isInStartedState()) {
                SearchActivity.this.mSearchStatePresenter.setQueryString(SearchActivity.this.mSearchBar.getTrimmedText(), false);
                SearchActivity.this.mSearchStatePresenter.moveToTypingState();
            }
        }
    };
    private TimelineClickListenerFactory.Callback mTimelineClickEventCallback = new TimelineClickListenerFactory.Callback() { // from class: co.vine.android.search.SearchActivity.3
        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onPlaylistButtonClicked(VinePost post) {
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onLikePost(VinePost post, int position) {
            Components.postActionsComponent().likePost(SearchActivity.this.mAppController, null, post.postId, post.getVineRepostRepostId(), false);
            post.addMeLike(SearchActivity.this.mAppController.getActiveSession());
            if (SearchActivity.this.mTimelineItemScribeActionsLogger != null) {
                SearchActivity.this.mTimelineItemScribeActionsLogger.onPostLiked(post, position);
            }
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onUnlikePost(VinePost post) {
            Components.postActionsComponent().unlikePost(SearchActivity.this.mAppController, null, post.postId, post.getVineRepostRepostId(), false);
            post.removeMeLike(SearchActivity.this.mAppController.getActiveId());
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onMoreButtonClicked(TimelineItem timelineItem) {
            if (timelineItem instanceof VinePost) {
                VinePost post = (VinePost) timelineItem;
                int avgSpeed = VideoCache.getCurrentAverageSpeed();
                VideoKey k = Util.getVideoRequestKey(post, false, avgSpeed);
                String videoPath = SearchActivity.this.mAppController.getVideoFilePath(k);
                Intent intent = PostOptionsDialogActivity.getMoreIntentForPost(post, videoPath, SearchActivity.this, SearchActivity.this.mAppController.getActiveId(), post.following, false);
                SearchActivity.this.startActivityForResult(intent, 10);
            }
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onShareClicked(VinePost post) {
            boolean isPostMine = post.userId == SearchActivity.this.mAppController.getActiveId();
            Intent shareIntent = PostShareActivity.getPostShareIntent(SearchActivity.this, post, isPostMine);
            SearchActivity.this.startActivityForResult(shareIntent, 20);
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onClosePromptClicked(TimelineItem item, CardViewHolder holder) {
            SearchActivity.this.mSearchStatePresenter.mFeedAdapter.removeItemAtPosition(holder.getPosition());
            SearchActivity.this.mTimelineItemScribeActionsLogger.onTimelineItemDismissed(item, holder.position);
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onPromptClicked(TimelineItem item, CardViewHolder holder) {
            SearchActivity.this.mTimelineItemScribeActionsLogger.onTimelineItemClicked(item, holder.position);
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onLongformOverlayClicked(VinePost post, int position) {
            Intent i = LongformActivity.getIntent(SearchActivity.this.getApplicationContext(), post);
            SearchActivity.this.startActivityForResult(i, 50);
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onFeedOverlayClicked(VineFeed item) {
            if (item != null && item.feedMetadata != null && item.feedMetadata.userProfile != null) {
                ChannelActivity.startProfile(SearchActivity.this, item.feedMetadata.userProfile.userId, "FeedTimelineItem");
            }
        }

        @Override // co.vine.android.feedadapter.TimelineClickListenerFactory.Callback
        public void onShareFeedButtonClicked(VineFeed item) {
        }
    };
    private TimelineScrollListener mSearchTimelineScrollListener = new TimelineScrollListener() { // from class: co.vine.android.search.SearchActivity.4
        @Override // co.vine.android.feedadapter.ArrayListScrollListener
        protected void onScrollLastItem(int totalItemCount) {
            super.onScrollLastItem(totalItemCount);
            boolean inPostShowingState = SearchActivity.this.mSearchStatePresenter.isInSearchResultsState() || SearchActivity.this.mSearchStatePresenter.isInTypingState();
            if (inPostShowingState && !TextUtils.isEmpty(SearchActivity.this.mPostsAnchor) && this.mFeedAdapter.getCount() < 400) {
                SearchActivity.this.fetchPosts(SearchActivity.this.mSearchBar.getTrimmedText());
            }
        }

        @Override // co.vine.android.feedadapter.TimelineScrollListener, co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int lastScrollState = getScrollState();
            if (!SearchActivity.this.mSearchStatePresenter.isInStartedState() && scrollState == 1 && lastScrollState == 0) {
                Util.setSoftKeyboardVisibility(SearchActivity.this, SearchActivity.this.mListView, false);
            }
            super.onScrollStateChanged(view, scrollState);
        }
    };
    private View.OnClickListener mOnQueryableRowClickListener = new View.OnClickListener() { // from class: co.vine.android.search.SearchActivity.5
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            QueryableRowHolder holder = (QueryableRowHolder) v.getTag();
            if (holder != null && !SearchActivity.this.mSearchStatePresenter.isInSearchResultsState()) {
                SearchActivity.this.mSearchBar.setText((CharSequence) holder.getSearchQueryString(), false);
                SearchActivity.this.validateQueryLengthAndPerformSearch();
            }
        }
    };
    private final TextWatcher mClearButtonVisibilityTextWatcher = new SimpleTextWatcher() { // from class: co.vine.android.search.SearchActivity.7
        @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SearchActivity.this.mClearButton.setVisibility(TextUtils.isEmpty(s) ? 8 : 0);
        }
    };

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null && getIntent().getIntExtra("enter_anim", 0) != 0) {
            overridePendingTransition(getIntent().getIntExtra("enter_anim", 0), R.anim.fade_out_partial);
        }
        super.onCreate(savedInstanceState, R.layout.search_layout, true);
        this.mAppSessionListener = new SearchActivitySessionListener();
        this.mRequestKeyGetter = new RequestKeyGetter(this, SLogger.getCachedEnabledLogger("SearchActivity"));
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.root_layout);
        RefreshableListView listView = (RefreshableListView) findViewById(R.id.results);
        this.mPendingRequestHelper = new SearchPendingRequestHelper(listView);
        this.mPendingRequestHelper.onCreate(this.mAppController, savedInstanceState);
        setupActionBar((Boolean) true, (Boolean) false, 0, (Boolean) true, (Boolean) false);
        listView.setDividerHeight(0);
        listView.disablePTR(true);
        this.mTimelineItemScribeActionsLogger = new TimelineItemScribeActionsLogger(ScribeLoggerSingleton.getInstance(getApplicationContext()), AppStateProviderSingleton.getInstance(this), AppNavigationProviderSingleton.getInstance());
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(getApplicationContext()), AppStateProviderSingleton.getInstance(getApplicationContext()), AppNavigationProviderSingleton.getInstance());
        this.mSearchStatePresenter = new SearchStatePresenter(rootLayout, listView, this.mSearchTimelineScrollListener, this.mTimelineClickEventCallback, this.mOnQueryableRowClickListener, this.mPendingRequestHelper, getFakeActionBar(), this.mTimelineItemScribeActionsLogger, this.mFollowScribeActionsLogger);
        this.mSearchStatePresenter.moveToStartedState();
        this.mListView = listView;
        SearchBarEditText searchBar = (SearchBarEditText) getFakeActionBar().getRoot().findViewById(R.id.search_query);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.search.SearchActivity.1
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 3) {
                    return false;
                }
                SearchActivity.this.validateQueryLengthAndPerformSearch();
                return true;
            }
        });
        searchBar.requestFocusAndShowKeyboardAfterDelay(300L);
        searchBar.setInputType(524288);
        searchBar.setTextChangeHandler(new SearchBarTextChangeHandler(this, this.mSearchStatePresenter, this.mPendingRequestHelper, listView));
        searchBar.addTextChangedListener(this.mClearButtonVisibilityTextWatcher);
        this.mSearchBar = searchBar;
        fetchSearchSuggestions();
        this.mTimelineActionResponseHandler = new TimelineActionResponseHandler(this.mSearchStatePresenter.mFeedAdapter.getPostAdapter(), this.mAppController, this.mPendingRequestHelper, this.mSearchStatePresenter.mFeedAdapter.getCollections(), this.mSearchStatePresenter.mFeedAdapter);
        initClearSearchButton();
    }

    @Override // co.vine.android.BaseActionBarActivity
    protected void setupActionBar(Boolean setHomeButtonEnabled, Boolean setDisplayShowTitleEnabled, int titleRes, Boolean setDisplayHomeAsUpEnabled, Boolean setDisplayLogoEnabled) {
        super.setupActionBar(setHomeButtonEnabled, setDisplayShowTitleEnabled, titleRes, setDisplayHomeAsUpEnabled, setDisplayLogoEnabled);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        RelativeLayout actionBarLeft = getFakeActionBar().getActionBarLeft();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
        params.addRule(1, R.id.ab_back_button);
        actionBarLeft.addView(LayoutInflater.from(this).inflate(R.layout.unified_search_bar, (ViewGroup) null), params);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.mSearchStatePresenter.onBackPressed()) {
            this.mSearchBar.setText((CharSequence) "", false);
            this.mSearchBar.setOnFocusChangeListener(null);
            this.mSearchBar.requestFocusAndShowKeyboardAfterDelay(50L);
            return;
        }
        super.onBackPressed();
    }

    public void fetchSearchSuggestions() {
        fetchData(1, null);
    }

    public void fetchTypeahead(String partialQuery) {
        fetchData(2, partialQuery);
    }

    public void fetchPosts(String query) {
        fetchData(4, query);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchData(int fetchType, String query) {
        if (!this.mPendingRequestHelper.hasPendingRequest(fetchType)) {
            String reqId = null;
            switch (fetchType) {
                case 1:
                    reqId = this.mAppController.fetchSearchSuggestions();
                    break;
                case 2:
                    reqId = this.mAppController.fetchUnifiedSearchAutocomplete(query);
                    break;
                case 3:
                    reqId = this.mAppController.fetchUnifiedSearchResults(query);
                    break;
                case 4:
                    reqId = this.mAppController.fetchPostSearchResults(query, this.mPostsAnchor);
                    break;
            }
            if (reqId != null) {
                this.mPendingRequestHelper.addRequest(reqId, fetchType);
                this.mPendingRequestHelper.showProgress(fetchType);
            }
        }
    }

    public boolean validateQueryLengthAndPerformSearch() {
        if (this.mSearchBar.getTrimmedLength() <= 1) {
            return false;
        }
        String query = this.mSearchBar.getTrimmedText();
        this.mSearchStatePresenter.moveToSearchState();
        this.mSearchStatePresenter.setQueryString(this.mSearchBar.getTrimmedText(), true);
        fetchData(3, query);
        this.mSearchBar.setOnFocusChangeListener(this.mQueryOnFocusChangeListener);
        this.mSearchBar.clearFocusAndHideKeyboardAfterDelay(50L);
        return true;
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mPendingRequestHelper.onResume();
        this.mSearchStatePresenter.onResume();
        Components.postActionsComponent().addListener(this.mTimelineActionResponseHandler);
        AppNavigationProviderSingleton.getInstance().setTimelineApiUrl("/search/sectioned");
        AppNavigationProviderSingleton.getInstance().setViewAndSubview(AppNavigation.Views.SEARCH, null);
    }

    @Override // co.vine.android.BaseControllerActionBarActivity, co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mSearchStatePresenter.onPause();
        Components.postActionsComponent().removeListener(this.mTimelineActionResponseHandler);
    }

    private class SearchActivitySessionListener extends AppSessionListener {
        private SearchActivitySessionListener() {
        }

        private void setOriginUrl(Collection<VinePost> posts, String url) {
            if (posts != null && url != null) {
                for (VinePost post : posts) {
                    post.originUrl = url;
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onSearchPostsComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VinePost> posts, String anchor, String searchUrl) {
            PendingRequest request = SearchActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (request != null && TextUtils.isEmpty(anchor)) {
                SearchActivity.this.mPendingRequestHelper.hideProgress(request.fetchType);
            }
            if (statusCode == 200) {
                setOriginUrl(posts, searchUrl);
                SearchActivity.this.mSearchStatePresenter.onPostSearchReceived(posts);
                SearchActivity.this.mPostsAnchor = anchor;
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchSearchSuggestionsComplete(String reqId, int statusCode, String reasonPhrase, SearchResult results, String searchUrl) {
            PendingRequest request = SearchActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (request != null) {
                SearchActivity.this.mPendingRequestHelper.hideProgress(request.fetchType);
            }
            if (statusCode != 200) {
                SearchActivity.this.mSearchStatePresenter.moveToErrorState(new View.OnClickListener() { // from class: co.vine.android.search.SearchActivity.SearchActivitySessionListener.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SearchActivity.this.fetchSearchSuggestions();
                    }
                });
                return;
            }
            ListSection<VinePost> postListSection = results.getPosts();
            if (postListSection != null) {
                ArrayList<VinePost> posts = postListSection.getItems();
                setOriginUrl(posts, searchUrl);
            }
            SearchActivity.this.mSearchStatePresenter.onSearchSuggestionsReceived(results);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchSearchTypeaheadComplete(String reqId, int statusCode, String reasonPhrase, String query, SearchResult data, String searchUrl) {
            PendingRequest request = SearchActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (request != null) {
                SearchActivity.this.mPendingRequestHelper.hideProgress(request.fetchType);
            }
            if (statusCode != 200) {
                SearchActivity.this.mSearchStatePresenter.moveToErrorState(new View.OnClickListener() { // from class: co.vine.android.search.SearchActivity.SearchActivitySessionListener.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SearchActivity.this.fetchTypeahead(SearchActivity.this.mSearchBar.getTrimmedText());
                    }
                });
                return;
            }
            if (!data.getHasResults()) {
                SearchActivity.this.mSearchStatePresenter.moveToEmptyState(query, data);
                return;
            }
            ListSection<VinePost> postListSection = data.getPosts();
            if (postListSection != null) {
                SearchActivity.this.mPostsAnchor = postListSection.getAnchorStr();
                ArrayList<VinePost> posts = postListSection.getItems();
                setOriginUrl(posts, searchUrl);
            }
            SearchActivity.this.mSearchStatePresenter.onSearchTypeaheadReceived(data);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchSearchResultsComplete(String reqId, int statusCode, String reasonPhrase, String query, SearchResult results, String searchUrl) {
            PendingRequest request = SearchActivity.this.mPendingRequestHelper.removeRequest(reqId);
            if (request != null) {
                SearchActivity.this.mPendingRequestHelper.hideProgress(request.fetchType);
            }
            if (statusCode != 200) {
                SearchActivity.this.mSearchStatePresenter.moveToErrorState(new View.OnClickListener() { // from class: co.vine.android.search.SearchActivity.SearchActivitySessionListener.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        SearchActivity.this.fetchData(3, SearchActivity.this.mSearchBar.getTrimmedText());
                    }
                });
                return;
            }
            if (!results.getHasResults()) {
                SearchActivity.this.mSearchStatePresenter.moveToEmptyState(query, results);
                return;
            }
            ListSection<VinePost> postListSection = results.getPosts();
            if (postListSection != null) {
                SearchActivity.this.mPostsAnchor = postListSection.getAnchorStr();
                ArrayList<VinePost> posts = postListSection.getItems();
                setOriginUrl(posts, searchUrl);
            }
            SearchActivity.this.mSearchStatePresenter.onSearchResultsReceived(results);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            SearchActivity.this.mSearchStatePresenter.setUserImages(images);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
            SearchActivity.this.mSearchStatePresenter.onVideoPathObtained(videos);
        }
    }

    static class SearchPendingRequestHelper extends PendingRequestHelper {
        private RefreshableListView mListView;

        public SearchPendingRequestHelper(RefreshableListView listView) {
            this.mListView = listView;
        }

        @Override // co.vine.android.PendingRequestHelper
        public void showProgress(int progressType) {
            this.mListView.refreshMore(true);
        }

        @Override // co.vine.android.PendingRequestHelper
        public void hideProgress(int progressType) {
            this.mListView.refreshMore(false);
        }
    }

    public String getSearchQuery() {
        return this.mSearchBar != null ? this.mSearchBar.getTrimmedText() : "";
    }

    private void initClearSearchButton() {
        ImageButton clearButton = (ImageButton) findViewById(R.id.clear);
        clearButton.setColorFilter(getResources().getColor(R.color.black_thirty_five_percent), PorterDuff.Mode.SRC_IN);
        clearButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.search.SearchActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SearchBarEditText searchBar = SearchActivity.this.mSearchBar;
                if (searchBar != null && searchBar.getTrimmedLength() > 0) {
                    searchBar.setText((CharSequence) "", false);
                    searchBar.requestFocusAndShowKeyboardAfterDelay(50L);
                    SearchActivity.this.mSearchStatePresenter.moveToStartedState();
                    SearchActivity.this.mPendingRequestHelper.hideProgress(0);
                }
            }
        });
        this.mClearButton = clearButton;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                PostOptionsDialogActivity.Result result = PostOptionsDialogActivity.processActivityResult(this.mAppController, this, resultCode, data, this.mFollowScribeActionsLogger);
                if (result.request != null) {
                    this.mPendingRequestHelper.addRequest(result.request);
                }
                if (result.intent != null) {
                    startActivity(result.intent);
                    break;
                }
                break;
            case 20:
                if (data != null) {
                    long postId = data.getLongExtra("post_id", 0L);
                    long repostId = data.getLongExtra("repost_id", 0L);
                    boolean following = data.getBooleanExtra("following", false);
                    String username = data.getStringExtra("username");
                    long myRepostId = data.getLongExtra("my_repost_id", 0L);
                    if (data.getBooleanExtra("revine", false)) {
                        String reqId = Components.postActionsComponent().revine(this.mAppController, null, postId, repostId, username);
                        this.mPendingRequestHelper.addRequest(reqId);
                        break;
                    } else {
                        this.mPendingRequestHelper.addRequest(Components.postActionsComponent().unRevine(this.mAppController, null, postId, myRepostId, repostId, following, true));
                        break;
                    }
                }
                break;
        }
    }

    @Override // co.vine.android.BaseActionBarActivity
    public boolean isFakeActionBarEnabled() {
        return true;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.app.Activity
    public void onPostCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onPostCreate(savedInstanceState);
        Resources res = getResources();
        FakeActionBar fakeActionBar = getFakeActionBar();
        int backButtonColor = res.getColor(R.color.black_thirty_five_percent);
        Drawable backbutton = res.getDrawable(R.drawable.ic_arrow_back_large);
        backbutton.setColorFilter(new PorterDuffColorFilter(backButtonColor, PorterDuff.Mode.SRC_IN));
        fakeActionBar.getBackIcon().setImageDrawable(backbutton);
        fakeActionBar.getBackIcon().setTag(Integer.valueOf(backButtonColor));
        fakeActionBar.getTitleView().setTypeface(Typefaces.get(this).mediumContent);
        fakeActionBar.getTitleView().setTextSize(2, 18.0f);
    }
}
