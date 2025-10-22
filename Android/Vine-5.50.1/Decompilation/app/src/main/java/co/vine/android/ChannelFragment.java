package co.vine.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import co.vine.android.BaseTimelineFragment;
import co.vine.android.ChannelHeaderAdapter;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VinePost;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.TimelineScrollListener;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.PagingInfoModel;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineItemModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.Timeline;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener;
import co.vine.android.share.activities.FeedShareActivity;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.LinkBuilderUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.widget.SectionAdapter;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class ChannelFragment extends BaseTimelineFragment implements ViewOffsetResolver {
    private String mBackgroundColor;
    private VineChannel mChannel;
    private ChannelHeaderAdapter mChannelHeaderAdapter;
    private long mChannelId;
    private int mCurrentMode;
    protected String mFlurryFollowEventSource;
    private boolean mFollowing;
    private boolean mHeaderAdded;
    private int mMainRGB;
    private int mPostGroup;
    private SectionAdapter mSectionAdapter;
    private boolean mShouldStartTheaterMode;
    private boolean mShowRecent;
    private String mSort;
    private TimelineDetails mTimelineDetails;
    private TimelineFetchActionsListener mTimelineFetchActionsListener;
    private String mTitle;
    private MenuItem mWatchIcon;

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        this.mChannel = (VineChannel) b.getParcelable("channel");
        this.mChannelId = this.mChannel.channelId;
        this.mTitle = this.mChannel.channel;
        if (this.mChannel.backgroundColor != null && !this.mChannel.backgroundColor.startsWith("#")) {
            this.mBackgroundColor = "#" + this.mBackgroundColor.trim();
        } else {
            this.mBackgroundColor = this.mChannel.backgroundColor;
        }
        if (this.mBackgroundColor != null) {
            this.mMainRGB = Color.parseColor(this.mBackgroundColor);
        } else {
            this.mMainRGB = getActivity().getResources().getColor(R.color.vine_green);
        }
        this.mShowRecent = this.mChannel.showRecent.booleanValue();
        this.mShouldStartTheaterMode = b.getBoolean("watch_mode");
        this.mFollowing = this.mChannel.following;
        setFlurryEventSource("Channel: Tab 1");
        this.mFlurryFollowEventSource = b.getString("event_source");
        boolean takeFocus = b.getBoolean("take_focus", false);
        if (takeFocus) {
            setFocused(true);
        }
        setAppSessionListener(new ExploreChannelsAppSessionListener());
        if (savedInstanceState != null) {
            this.mCurrentMode = savedInstanceState.getInt("state_mode");
            if (savedInstanceState.containsKey("stated_header_added")) {
                this.mHeaderAdded = savedInstanceState.getBoolean("stated_header_added");
            }
        } else {
            this.mCurrentMode = 1;
            this.mHeaderAdded = false;
        }
        updatePostGroupAndSort();
        updateTimelineApiUrl();
        hideSadface();
        ModelEvents.ModelListener modelListener = new LifetimeSafeModelListener(this, new ModelListener());
        VineModelFactory.getModelInstance().getModelEvents().addListener(modelListener);
        this.mTimelineFetchActionsListener = new TimelineFetchActionsListener() { // from class: co.vine.android.ChannelFragment.1
            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onTimelineFetched(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
                PendingRequest req = ChannelFragment.this.removeRequest(reqId);
                if (req != null) {
                    if (statusCode == 200 && bundle != null) {
                        ChannelFragment.this.mChannel = (VineChannel) Parcels.unwrap(bundle.getParcelable("channels"));
                        if (ChannelFragment.this.mChannel != null) {
                            ChannelFragment.this.mFollowing = ChannelFragment.this.mChannel.following;
                            boolean shouldForceRefresh = ChannelFragment.this.mSectionAdapter == null || ChannelFragment.this.mSectionAdapter.getNumberOfAdapters() == 1;
                            ChannelFragment.this.refreshUnlockedSectionAdapter(shouldForceRefresh);
                        } else if (bundle.getBoolean("for_you")) {
                            boolean shouldForceRefresh2 = ChannelFragment.this.mSectionAdapter == null || ChannelFragment.this.mSectionAdapter.getNumberOfAdapters() == 1;
                            ChannelFragment.this.refreshUnlockedSectionAdapter(shouldForceRefresh2);
                        }
                    }
                    if (!cachePolicy.mNetworkDataAllowed) {
                        ChannelFragment.this.fetchInitialRequest(UrlCachePolicy.NETWORK_THEN_CACHE);
                    }
                    ChannelFragment.this.hideProgress(req.fetchType);
                }
            }

            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onChannelsFetched(String reqId, int statusCode, String reasonPhrase, Bundle bundle) {
            }
        };
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() throws Resources.NotFoundException {
        super.onResume();
        if (this.mAdapter.isEmpty()) {
            fetchInitialRequest(UrlCachePolicy.CACHE_ONLY);
        } else if (this.mTimelineDetails != null) {
            updateFeedAdapter();
        }
        setup(isFocused());
        bindChannelData();
        Components.timelineFetchComponent().addListener(this.mTimelineFetchActionsListener);
    }

    void bindChannelData() throws Resources.NotFoundException {
        if (this.mFeedAdapter != null) {
            this.mFeedAdapter.setProfileColor(this.mMainRGB);
        }
        if (this.mFeedAdapter != null && this.mListView != null) {
            RefreshableListView rlv = (RefreshableListView) this.mListView;
            int color = (-16777216) | this.mMainRGB;
            rlv.setPullToRefreshBackgroundColor(color);
            rlv.colorizePTR(-1);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.explore_channels, menu);
        if (BuildUtil.isAmazon() || !ClientFlagsHelper.isTheaterModeEnabled(getActivity())) {
            menu.removeItem(R.id.feed);
        }
        if (!ClientFlagsHelper.isProfileShareEnabled(getActivity())) {
            menu.removeItem(R.id.share_channel);
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity context;
        int id = item.getItemId();
        if (id == R.id.share_channel) {
            if (this.mChannel == null || (context = getActivity()) == null) {
                return true;
            }
            TimelineItem currentItem = (TimelineItem) this.mFeedAdapter.getItem(this.mFeedAdapter.getCurrentPosition());
            if (currentItem instanceof VinePost) {
                VinePost post = (VinePost) currentItem;
                RequestKeyGetter keyGetter = new RequestKeyGetter(context, this.mLogger);
                VideoKey videoKey = keyGetter.getRequestKey(post, false);
                VideoKey lowVideoKey = keyGetter.getRequestKey(post, true);
                String remoteVideoUrl = post.videoUrl;
                Intent shareIntent = FeedShareActivity.getIntent(context, post.postId, post.shareUrl, post.username, post.thumbnailUrl, remoteVideoUrl, videoKey, lowVideoKey, FeedMetadata.FeedType.CHANNEL, this.mChannelId);
                context.startActivityForResult(shareIntent, 21);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override // android.support.v4.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        this.mWatchIcon = menu.findItem(R.id.feed);
        if (this.mWatchIcon != null) {
            this.mWatchIcon.setVisible(false);
            this.mWatchIcon.setEnabled(false);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected String fetchPosts(int page, String anchor, String backAnchor, boolean userInitiated, UrlCachePolicy cachePolicy) {
        throw new IllegalStateException("Don't call fetch posts for ChannelFragment, this fragment handles its own fetching.");
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected void fetchContent(int fetchType, boolean userInitiated, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
        this.mLastFetchType = fetchType;
        if (fetchType == 2) {
            showProgress(fetchType);
        }
        if (this.mTimelineDetails == null) {
            this.mTimelineDetails = new TimelineDetails(this.mPostGroup, Long.valueOf(this.mChannelId), this.mSort);
        } else {
            this.mTimelineDetails.type = this.mPostGroup;
            this.mTimelineDetails.channelId = this.mChannelId;
            this.mTimelineDetails.sort = this.mSort;
        }
        addRequest(Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), 20, this.mChannelId, this.mPostGroup, userInitiated, String.valueOf(this.mChannelId), this.mSort, null, cachePolicy, forceReplacePosts, -1L, fetchType), fetchType);
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRefreshableHeaderOffset(6);
        showProgress(3);
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.msg_list_fragment_profile, container);
        return view;
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mListView.setOnScrollListener(new TimelineScrollListener(this.mFeedAdapter) { // from class: co.vine.android.ChannelFragment.2
            private int visiblePercent = 100;
            private Rect mScrollBounds = new Rect();

            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            protected void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (ChannelFragment.this.mRefreshable && PagingInfoModel.getInstance().hasMore(ChannelFragment.this.mTimelineDetails.getUniqueMarker()) && this.mFeedAdapter.getCount() <= 400) {
                    this.mFeedAdapter.onFocusChanged(ChannelFragment.this.isFocused());
                    ChannelFragment.this.mLastFetchType = 1;
                    ChannelFragment.this.showProgress(1);
                    ChannelFragment.this.fetchContent(1, true, UrlCachePolicy.FORCE_REFRESH, false);
                }
            }

            @Override // co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                this.visiblePercent = ViewUtil.getViewVisiblePercentVertical(ChannelFragment.this.mListView, this.mScrollBounds, view.findViewById(R.id.watch_button));
                ChannelFragment.this.setWatchIconVisible(this.visiblePercent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setWatchIconVisible(int visiblePercent) {
        if (this.mWatchIcon != null) {
            if (visiblePercent == 100) {
                this.mWatchIcon.setVisible(false);
                this.mWatchIcon.setEnabled(false);
            } else {
                this.mWatchIcon.setVisible(true);
                this.mWatchIcon.setEnabled(true);
                int alpha = visiblePercent == -1 ? 255 : ((100 - visiblePercent) * 255) / 100;
                this.mWatchIcon.getIcon().setAlpha(alpha);
            }
            this.mWatchIcon.getIcon().invalidateSelf();
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                if (data != null) {
                    long postId = data.getLongExtra("id", -1L);
                    int index = this.mFeedAdapter.getItemIndexForPostWithTimelineItemId(postId);
                    scrollTo(index + 1);
                    break;
                }
                break;
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("state_mode", this.mCurrentMode);
        outState.putBoolean("stated_header_added", this.mHeaderAdded);
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment
    protected void refresh() {
        fetchContent(2, true, UrlCachePolicy.FORCE_REFRESH, false);
    }

    private void updatePostGroupAndSort() {
        if (this.mCurrentMode == 1) {
            this.mPostGroup = 8;
            this.mSort = "popular";
        } else if (this.mCurrentMode == 2) {
            this.mPostGroup = 9;
            this.mSort = "recent";
        } else if (this.mCurrentMode == 3) {
            this.mPostGroup = 36;
            this.mSort = "for_you";
        }
    }

    public void updateMode(int mode) {
        if (this.mCurrentMode != mode) {
            this.mCurrentMode = mode;
            updatePostGroupAndSort();
            setFlurryEventSource("Channel: Tab " + mode);
            this.mSectionAdapter = new SectionAdapter(this.mChannelHeaderAdapter);
            this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
            fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, false);
            updateTimelineApiUrl();
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mFeedAdapter.onPause(isFocused());
        Components.timelineFetchComponent().removeListener(this.mTimelineFetchActionsListener);
    }

    private void setup(boolean takeFocus) {
        if (this.mFeedAdapter != null && takeFocus) {
            this.mFeedAdapter.onResume(true);
        }
    }

    public void toTheaterMode(Activity activity) {
        if (activity != null) {
            int currentPosition = this.mShouldStartTheaterMode ? 0 : this.mFeedAdapter.getCurrentPosition();
            long currentId = this.mFeedAdapter.findNextPost(currentPosition);
            Intent i = TheaterActivity.getIntent(activity, this.mTimelineDetails, currentId, this.mTitle, this.mApiUrl);
            activity.startActivityForResult(i, 3);
            activity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.fade_out);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshUnlockedSectionAdapter(boolean forceRefresh) {
        if (forceRefresh) {
            if (this.mChannelHeaderAdapter == null) {
                this.mChannelHeaderAdapter = new ChannelHeaderAdapter(getActivity(), this.mAppController, new ChannelFragmentHeaderAdapterListener(), this.mChannel, this.mMainRGB, this.mShowRecent);
            }
            this.mSectionAdapter = new SectionAdapter(this.mChannelHeaderAdapter, this.mFeedAdapter);
            this.mFeedAdapter.setOffsetResolver(this);
            this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
            this.mSectionAdapter.notifyDataSetChanged();
        }
    }

    private class ChannelFragmentHeaderAdapterListener implements ChannelHeaderAdapter.ChannelHeaderListener {
        private ChannelFragmentHeaderAdapterListener() {
        }

        @Override // co.vine.android.ChannelHeaderAdapter.ChannelHeaderListener
        public void onFollowChannelClicked() {
            boolean newState = !ChannelFragment.this.mFollowing;
            ChannelFragment.this.mAppController.followChannel(ChannelFragment.this.mChannelId, newState);
            ChannelFragment.this.mFollowing = newState;
            if (ChannelFragment.this.mFollowing) {
                ChannelFragment.this.mFriendships.addChannelFollowing(ChannelFragment.this.mChannelId);
            } else {
                ChannelFragment.this.mFriendships.removeChannelFollowing(ChannelFragment.this.mChannelId);
            }
        }

        @Override // co.vine.android.ChannelHeaderAdapter.ChannelHeaderListener
        public void onPopularTabClicked() {
            ChannelFragment.this.updateMode(1);
        }

        @Override // co.vine.android.ChannelHeaderAdapter.ChannelHeaderListener
        public void onRecentTabClicked() {
            ChannelFragment.this.updateMode(2);
        }

        @Override // co.vine.android.ChannelHeaderAdapter.ChannelHeaderListener
        public void onForYouTabClicked() {
            ChannelFragment.this.updateMode(3);
        }

        @Override // co.vine.android.ChannelHeaderAdapter.ChannelHeaderListener
        public void onTheaterModeClicked() {
            ChannelFragment.this.toTheaterMode(ChannelFragment.this.getActivity());
        }
    }

    private final class ModelListener implements ModelEvents.ModelListener {
        private ModelListener() {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTagsAdded(TagModel tagModel, String query) {
        }

        @Override // co.vine.android.model.ModelEvents.ModelListener
        public void onTimelineUpdated(TimelineModel timelineModel, TimelineDetails timelineDetails) {
            if (timelineDetails.equals(ChannelFragment.this.mTimelineDetails)) {
                ChannelFragment.this.updateFeedAdapter();
                if (ChannelFragment.this.mShouldStartTheaterMode) {
                    ChannelFragment.this.toTheaterMode(ChannelFragment.this.getActivity());
                    ChannelFragment.this.mShouldStartTheaterMode = false;
                }
            }
        }
    }

    class ExploreChannelsAppSessionListener extends BaseTimelineFragment.TimeLineSessionListener {
        ExploreChannelsAppSessionListener() {
            super();
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFollowChannelComplete(String reqId, int statusCode, String reasonPhrase) {
            if (statusCode != 200) {
                ChannelFragment.this.mFollowing = !ChannelFragment.this.mFollowing;
                ChannelFragment.this.mChannelHeaderAdapter.setFollowing(ChannelFragment.this.mFollowing);
                Util.showCenteredToast(ChannelFragment.this.getActivity(), R.string.generic_error);
            }
        }
    }

    private void updateTimelineApiUrl() {
        Bundle data = new Bundle();
        data.putString("channelId", String.valueOf(this.mChannelId));
        data.putString("sort", this.mSort);
        this.mApiUrl = LinkBuilderUtil.buildUrl(this.mPostGroup, data);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFeedAdapter() {
        Timeline timeline = VineModelFactory.getModelInstance().getTimelineModel().getUserTimeline(this.mTimelineDetails);
        if (timeline != null && timeline.itemIds != null) {
            ArrayList<TimelineItem> timelineItems = new ArrayList<>();
            TimelineItemModel timelineItemModel = VineModelFactory.getModelInstance().getTimelineItemModel();
            Iterator<Long> it = timeline.itemIds.iterator();
            while (it.hasNext()) {
                long id = it.next().longValue();
                timelineItems.add(timelineItemModel.getTimelineItem(id));
            }
            this.mFeedAdapter.replaceItems(timelineItems);
        }
    }
}
