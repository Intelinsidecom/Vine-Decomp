package co.vine.android.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.vine.android.ChannelFollowButtonClickListener;
import co.vine.android.FollowButtonClickListener;
import co.vine.android.Friendships;
import co.vine.android.PendingRequestHelper;
import co.vine.android.R;
import co.vine.android.TabbedFeedActivityFactory;
import co.vine.android.UsersActivity;
import co.vine.android.api.SearchResult;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemUtil;
import co.vine.android.api.VinePost;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.feedadapter.TimelineScrollListener;
import co.vine.android.feedadapter.v2.FeedAdapter;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import co.vine.android.feedadapter.viewmanager.SearchPostCardViewManager;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.TimelineItemScribeActionsLogger;
import co.vine.android.widget.FakeActionBar;
import co.vine.android.widget.SectionAdapter;
import com.edisonwang.android.slog.SLogger;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class SearchStatePresenter {
    private ActionBarThemeChanger mActionBarThemeChanger;
    private int mBlackThirtyFivePercent;
    private ChannelSearchAdapter mChannelsAdapter;
    private ViewGroup mErrorStateViews;
    public SearchFeedAdapter mFeedAdapter;
    private int mLightGray;
    private String mQueryString;
    private RecentSearchesAdapter mRecentSearchesAdapter;
    private RecentSearchesManager mRecentSearchesManager;
    private ViewGroup mRootLayout;
    private SearchResult mSearchResultsData;
    private int mSolidBlack;
    private int mSolidWhite;
    private int mState;
    private SearchResult mSuggestedSearchData;
    private SearchSuggestionAdapter mSuggestedSearchesAdapter;
    private TagSearchAdapter mTagsAdapter;
    private Button mTryAgainButton;
    private SearchResult mTypeaheadData;
    private UserSearchAdapter mUsersAdapter;
    private int mVineGreen;
    View.OnClickListener mViewAllTagsClickListener = new View.OnClickListener() { // from class: co.vine.android.search.SearchStatePresenter.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TagSearchResultsActivity.start(SearchStatePresenter.this.mRootLayout.getContext(), SearchStatePresenter.this.mQueryString);
        }
    };
    View.OnClickListener mViewAllPostsClickListener = new View.OnClickListener() { // from class: co.vine.android.search.SearchStatePresenter.4
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            TabbedFeedActivityFactory.startTabbedPostsActivity(view.getContext(), SearchStatePresenter.this.mQueryString);
        }
    };

    public SearchStatePresenter(ViewGroup rootLayout, ListView listView, TimelineScrollListener timelineScrollListener, TimelineClickListenerFactory.Callback timelineCallbacks, View.OnClickListener onQueryableRowClick, PendingRequestHelper pendingRequestHelper, FakeActionBar fakeActionBar, TimelineItemScribeActionsLogger postPlayedScribeLogger, FollowScribeActionsLogger followScribeLogger) {
        this.mRecentSearchesManager = new RecentSearchesManager(listView.getContext());
        prepareCompositeAdapters(listView.getContext(), listView, onQueryableRowClick, timelineCallbacks, pendingRequestHelper, postPlayedScribeLogger, followScribeLogger);
        BaseAdapter[] adapters = {this.mRecentSearchesAdapter, this.mSuggestedSearchesAdapter, this.mChannelsAdapter, this.mUsersAdapter, this.mTagsAdapter, this.mFeedAdapter};
        SectionAdapter mainAdapter = new SectionAdapter(adapters);
        listView.setAdapter((ListAdapter) mainAdapter);
        this.mRootLayout = rootLayout;
        timelineScrollListener.setFeedAdapter(this.mFeedAdapter);
        listView.setOnScrollListener(timelineScrollListener);
        findViews();
        getColors(listView.getContext());
        this.mRootLayout.setBackgroundColor(this.mLightGray);
        this.mActionBarThemeChanger = new ActionBarThemeChanger(fakeActionBar, 150L);
    }

    private void findViews() {
        this.mTryAgainButton = (Button) this.mRootLayout.findViewById(R.id.try_again);
        this.mErrorStateViews = (ViewGroup) this.mRootLayout.findViewById(R.id.error_state);
    }

    private void getColors(Context context) {
        Resources res = context.getResources();
        this.mLightGray = res.getColor(R.color.black_three_percent);
        this.mVineGreen = res.getColor(R.color.vine_green);
        this.mSolidBlack = res.getColor(R.color.solid_black);
        this.mSolidWhite = res.getColor(R.color.solid_white);
        this.mBlackThirtyFivePercent = res.getColor(R.color.black_thirty_five_percent);
    }

    private void prepareCompositeAdapters(Context context, ListView listView, View.OnClickListener onQueryableRowClick, TimelineClickListenerFactory.Callback timelineCallbacks, PendingRequestHelper pendingRequestHelper, TimelineItemScribeActionsLogger postPlayedScribeLogger, FollowScribeActionsLogger followScribeLogger) {
        AppController appController = AppController.getInstance(context);
        this.mSuggestedSearchesAdapter = new SearchSuggestionAdapter(context, onQueryableRowClick);
        this.mRecentSearchesAdapter = new RecentSearchesAdapter(context, onQueryableRowClick, this.mRecentSearchesManager);
        SLogger logger = SLogger.getCachedEnabledLogger("SearchFeedAdapter");
        FeedAdapterItems items = new FeedAdapterItems(logger);
        Friendships friendships = new Friendships();
        FollowButtonClickListener followButtonClickListener = new FollowButtonClickListener(context, AppController.getInstance(context), pendingRequestHelper, friendships, followScribeLogger);
        this.mUsersAdapter = new UserSearchAdapter(context, appController, new View.OnClickListener() { // from class: co.vine.android.search.SearchStatePresenter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                UsersActivity.startUserListForSearchQuery(view.getContext(), SearchStatePresenter.this.mQueryString);
            }
        }, followButtonClickListener, friendships, true);
        this.mTagsAdapter = new TagSearchAdapter(context, appController, this.mViewAllTagsClickListener);
        ChannelFollowButtonClickListener channelFollowButtonClickListener = new ChannelFollowButtonClickListener(context, appController, friendships);
        this.mChannelsAdapter = new ChannelSearchAdapter(context, appController, channelFollowButtonClickListener, friendships);
        FeedViewHolderCollection collection = new FeedViewHolderCollection();
        ArrayList<CardViewManager> viewManagers = new ArrayList<>();
        FeedNotifier notifier = new FeedNotifier() { // from class: co.vine.android.search.SearchStatePresenter.2
        };
        SearchPostCardViewManager.Builder postViewManagerBuilder = new SearchPostCardViewManager.Builder();
        postViewManagerBuilder.items(items).notifier(notifier).viewHolders(collection).listView(listView).context((Activity) context).callback(timelineCallbacks).friendships(new Friendships()).searchListener(this.mViewAllPostsClickListener).followActionsLogger(followScribeLogger).postPlayedListener(postPlayedScribeLogger).logger(logger).followEventSource(null);
        viewManagers.add(postViewManagerBuilder.build());
        this.mFeedAdapter = new SearchFeedAdapter((Activity) context, listView, viewManagers, items, logger, collection);
    }

    public void updateAdapters(SearchResult data) {
        if (data != null) {
            this.mSuggestedSearchesAdapter.replaceData(data, isInEmptyState());
            this.mUsersAdapter.replaceData(data);
            this.mTagsAdapter.replaceData(data);
            this.mFeedAdapter.replacePosts(data);
            this.mChannelsAdapter.replaceData(data);
            if (!isInStartedState()) {
                this.mRecentSearchesAdapter.clearData();
            }
        }
    }

    private void updateErrorState(SearchResult searchResultsData) {
        if (searchResultsData != null) {
            showErrorState(false);
        }
    }

    public void onSearchSuggestionsReceived(SearchResult data) {
        this.mSuggestedSearchData = data;
        if (isInErrorState()) {
            this.mState = 0;
        }
        if (isInStartedState()) {
            updateAdapters(data);
            updateErrorState(data);
        }
    }

    public void onSearchTypeaheadReceived(SearchResult data) {
        this.mTypeaheadData = data;
        if (isInErrorState()) {
            this.mState = 1;
        }
        if (isInTypingState()) {
            updateAdapters(data);
        } else if (isInEmptyState()) {
            moveToTypingState();
            updateErrorState(data);
        }
    }

    public void onSearchResultsReceived(SearchResult data) {
        this.mSearchResultsData = data;
        if (isInErrorState()) {
            moveToSearchState();
        }
        if (isInSearchResultsState()) {
            updateAdapters(data);
            updateErrorState(data);
        }
    }

    public void onPostSearchReceived(ArrayList<VinePost> posts) {
        ArrayList<TimelineItem> items = TimelineItemUtil.wrapPostsInTimelineItemList(posts);
        if (isInTypingState() || isInSearchResultsState()) {
            this.mFeedAdapter.mergeItems(items);
        }
    }

    public void setUserImages(HashMap<ImageKey, UrlImage> images) {
        this.mUsersAdapter.setUserImages(images);
        this.mFeedAdapter.setImages(images);
        this.mChannelsAdapter.setChannelImages(images);
    }

    public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
        this.mFeedAdapter.onVideoPathObtained(videos);
    }

    public void moveToStartedState() {
        this.mState = 0;
        this.mSuggestedSearchesAdapter.showSectionHeader(true);
        this.mRecentSearchesAdapter.replaceData(this.mRecentSearchesManager.getRecentSearches());
        updateAdapters(this.mSuggestedSearchData);
        updateErrorState(this.mSuggestedSearchData);
        this.mRootLayout.setBackgroundColor(this.mLightGray);
        this.mActionBarThemeChanger.swapToColor(this.mSolidWhite, this.mSolidBlack, this.mBlackThirtyFivePercent, true);
    }

    public void moveToTypingState() {
        this.mState = 1;
        this.mSuggestedSearchesAdapter.showSectionHeader(false);
        if (this.mFeedAdapter.isPlaying()) {
            this.mFeedAdapter.pausePlayer();
        }
        updateAdapters(this.mTypeaheadData);
        updateErrorState(this.mTypeaheadData);
        this.mRootLayout.setBackgroundColor(this.mSolidWhite);
        this.mActionBarThemeChanger.swapToColor(this.mSolidWhite, this.mSolidBlack, this.mBlackThirtyFivePercent, true);
    }

    public void moveToSearchState() {
        this.mState = 2;
        this.mSuggestedSearchesAdapter.showSectionHeader(false);
        updateAdapters(this.mSearchResultsData);
        updateErrorState(this.mSearchResultsData);
        this.mRootLayout.setBackgroundColor(this.mSolidWhite);
        this.mActionBarThemeChanger.swapToColor(this.mVineGreen, this.mSolidWhite, this.mSolidWhite, false);
    }

    public void moveToEmptyState(String query, SearchResult results) {
        this.mState = 3;
        this.mSuggestedSearchesAdapter.showSectionHeader(true);
        this.mSuggestedSearchesAdapter.setNoResultsMessage(query);
        updateAdapters(results);
        updateErrorState(results);
        this.mActionBarThemeChanger.swapToColor(this.mSolidWhite, this.mSolidBlack, this.mBlackThirtyFivePercent, true);
    }

    public void moveToErrorState(View.OnClickListener tryAgainClickListener) {
        this.mState = 4;
        showErrorState(true);
        this.mTryAgainButton.setOnClickListener(tryAgainClickListener);
        this.mActionBarThemeChanger.swapToColor(this.mSolidWhite, this.mSolidBlack, this.mBlackThirtyFivePercent, true);
    }

    public void setQueryString(String query, boolean addToRecentSearches) {
        this.mQueryString = query;
        if (addToRecentSearches) {
            this.mRecentSearchesManager.addRecentSearch(query);
        }
    }

    public FeedAdapter getFeedAdapter() {
        return this.mFeedAdapter;
    }

    private void showErrorState(boolean show) {
        this.mErrorStateViews.setVisibility(show ? 0 : 8);
    }

    public boolean isInTypingState() {
        return this.mState == 1;
    }

    public boolean isInStartedState() {
        return this.mState == 0;
    }

    public boolean isInSearchResultsState() {
        return this.mState == 2;
    }

    public boolean isInEmptyState() {
        return this.mState == 3;
    }

    public boolean isInErrorState() {
        return this.mState == 4;
    }

    public boolean onBackPressed() {
        if (isInStartedState() || isInErrorState()) {
            return false;
        }
        moveToStartedState();
        return true;
    }

    public void onResume() {
        if (this.mFeedAdapter != null) {
            this.mFeedAdapter.onResume(true);
        }
    }

    public void onPause() {
        if (this.mFeedAdapter != null) {
            this.mFeedAdapter.onPause(true);
        }
    }
}
