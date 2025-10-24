package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import co.vine.android.BaseTimelineFragment;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.TimelineItemType;
import co.vine.android.api.VineMosaic;
import co.vine.android.api.VinePost;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.scribe.FreshPostsScribeLogger;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.inject.InjectionFetchListener;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LinkBuilderUtil;
import co.vine.android.widget.OnTopViewBoundListener;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.googlecode.javacv.cpp.avcodec;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class HomeTimelineFragment extends BaseTimelineFragment {
    public boolean mHasStickyHeader;
    private ArrayList<TimelineItem> mNewItems;
    private long mStaleContentPointTimestamp;
    private VineMosaic mStickyCardItem;
    private View mStickyHeader;
    private View mStickyPlaceholder;
    private View mThumbnails;
    private int mNavBarHeight = 0;
    private int mListOffset = 0;
    private int mScrollState = 0;
    private long mStaleContentPointTimelineItemId = -1;
    private int mStaleContentPointIndex = -1;
    private boolean mFreshPostsCountWasLogged = false;
    private final InjectionFetchListener mInjectionListener = new InjectionFetchListener() { // from class: co.vine.android.HomeTimelineFragment.1
        @Override // co.vine.android.service.components.inject.InjectionFetchListener
        public void onFetchSuggestedMosaicComplete(String reqId, int statusCode, String reasonPhrase, TimelineItem mosaic) {
            PendingRequest req = HomeTimelineFragment.this.removeRequest(reqId);
            if (req != null && statusCode == 200 && HomeTimelineFragment.this.mStaleContentPointIndex != -1 && mosaic != null) {
                HomeTimelineFragment.this.mFeedAdapter.injectTimelineItemAtPosition(mosaic, HomeTimelineFragment.this.mStaleContentPointIndex);
            }
        }
    };
    private final PromptDialogSupportFragment.OnDialogDoneListener mAddWidgetDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.HomeTimelineFragment.4
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (which) {
                case -1:
                    AppImpl.getInstance().setupWidget(HomeTimelineFragment.this.getActivity());
                    break;
            }
            dialog.dismiss();
        }
    };

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new HomeTimelineSessionListener());
        this.mStaleContentPointTimelineItemId = StaleFeedStartIdManager.getStaleFeedStartPointId(getActivity());
        this.mStaleContentPointTimestamp = StaleFeedStartIdManager.getStaleFeedStartFetchTimestamp(getActivity());
        setFlurryEventSource("Home Timeline");
        setFocused(true);
        this.mApiUrl = LinkBuilderUtil.buildUrl(1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prependItems(ArrayList<TimelineItem> items) {
        int badgeCount = this.mFeedAdapter.prependItems(items);
        if (badgeCount < 0) {
            this.mFeedAdapter.replaceItems(items);
        } else if (badgeCount > 0) {
            FragmentActivity activity = getActivity();
            if (activity instanceof HomeTabActivity) {
                ((HomeTabActivity) activity).setHomeNewCount(badgeCount);
            }
        }
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected void onTimelineUpdatesCameIn(ArrayList<TimelineItem> items) {
        if (this.mScrollState != 0) {
            this.mNewItems = items;
        } else {
            prependItems(items);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected OnTopViewBoundListener getOnTopViewBoundListener() {
        return new OnTopViewBoundListener() { // from class: co.vine.android.HomeTimelineFragment.2
            @Override // co.vine.android.widget.OnTopViewBoundListener
            public void onTopViewBound() {
                ((HomeTabActivity) HomeTimelineFragment.this.getActivity()).setHomeNewCount(0);
            }
        };
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        boolean shouldShowAddWidget = false;
        super.onActivityCreated(savedInstanceState);
        this.mNavBarHeight = getResources().getDimensionPixelSize(R.dimen.tabbar_height);
        this.mListView.setAdapter((ListAdapter) this.mFeedAdapter);
        setUpDiscoverCardStickyHeader();
        FragmentActivity activity = getActivity();
        AccountManager am = AccountManager.get(activity);
        Session session = this.mAppController.getActiveSession();
        Account account = VineAccountHelper.getAccount(getActivity(), session.getUserId(), session.getUsername());
        SharedPreferences capturePrefs = getActivity().getSharedPreferences("capture", 0);
        int launchCount = capturePrefs.getInt("recorder_launch_count", 0);
        if (account != null && !BuildUtil.isAmazon() && AppImpl.getInstance().doAddWidget(activity, am, account) && launchCount > 5 && !CrossConstants.DISABLE_ANIMATIONS) {
            shouldShowAddWidget = true;
        }
        if (shouldShowAddWidget) {
            VineAccountHelper.setDidShowAddWidget(am, account);
            PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(992);
            p.setTitle(R.string.add_capture_widget_title);
            p.setMessage(R.string.add_capture_widget);
            p.setNegativeButton(R.string.cancel);
            p.setPositiveButton(R.string.add_widget);
            p.setListener(this.mAddWidgetDialogDoneListener);
            try {
                p.show(activity.getSupportFragmentManager());
            } catch (Exception e) {
            }
        }
        this.mListView.setOnScrollListener(new BaseTimelineFragment.BaseTimelineScrollListener(this.mFeedAdapter) { // from class: co.vine.android.HomeTimelineFragment.3
            @Override // co.vine.android.feedadapter.TimelineScrollListener, co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
            public synchronized void onScrollStateChanged(AbsListView view, int scrollState) {
                super.onScrollStateChanged(view, scrollState);
                HomeTimelineFragment.this.mScrollState = scrollState;
                if (HomeTimelineFragment.this.mNewItems != null) {
                    HomeTimelineFragment.this.prependItems(HomeTimelineFragment.this.mNewItems);
                    HomeTimelineFragment.this.mNewItems = null;
                }
            }

            @Override // co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (HomeTimelineFragment.this.mHasStickyHeader) {
                    HomeTimelineFragment.this.scaleStickyHeader();
                }
            }
        });
    }

    @Override // co.vine.android.BaseTimelineFragment
    public View setUpUnderNavHeader() {
        View v = super.setUpUnderNavHeader();
        this.mStickyPlaceholder = v.findViewById(R.id.stickyViewPlaceholder);
        return v;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scaleStickyHeader() {
        int[] location = {0, 0};
        this.mListView.getLocationOnScreen(location);
        this.mListOffset = location[1];
        this.mStickyPlaceholder.getLocationOnScreen(location);
        int newY = Math.max(location[1] - this.mListOffset, this.mNavBarHeight);
        int minHeight = this.mStickyHeader.getMinimumHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mStickyHeader.getLayoutParams();
        this.mStickyHeader.setY(newY);
        if (this.mListView.getFirstVisiblePosition() == 0) {
            params.height = Math.max(minHeight, ((location[1] + this.mStickyPlaceholder.getHeight()) - this.mListOffset) - newY);
            this.mStickyHeader.setLayoutParams(params);
        } else {
            params.height = this.mStickyHeader.getMinimumHeight();
            this.mStickyHeader.setLayoutParams(params);
        }
        scaleThumbnails();
    }

    private void scaleThumbnails() {
        int deltaHeader = this.mStickyHeader.getHeight() - this.mStickyHeader.getMinimumHeight();
        int delta = (deltaHeader * 64) / avcodec.AV_CODEC_ID_JV;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mThumbnails.getLayoutParams();
        params.height = this.mThumbnails.getMinimumHeight() + delta;
        params.width = params.height * 3;
        params.bottomMargin = ((deltaHeader * 32) / avcodec.AV_CODEC_ID_JV) + getResources().getDimensionPixelSize(R.dimen.suggested_banner_sticky_bottom_margin_minimum);
        this.mThumbnails.setLayoutParams(params);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkStickyCard(ArrayList<TimelineItem> items) {
        if (this.mFeedAdapter.getCount() == 0 && items != null && !items.isEmpty() && items.get(0).getType() == TimelineItemType.POST_MOSAIC) {
            this.mStickyCardItem = (VineMosaic) items.get(0);
            this.mHasStickyHeader = true;
        }
    }

    private void setUpDiscoverCardStickyHeader() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService("layout_inflater");
        this.mStickyHeader = inflater.inflate(R.layout.suggested_banner_sticky, (ViewGroup) null);
        ViewGroup parent = (ViewGroup) this.mListView.getParent();
        parent.addView(this.mStickyHeader);
        parent.bringChildToFront(this.mStickyHeader);
        this.mThumbnails = this.mStickyHeader.findViewById(R.id.thumbnails);
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            this.mFetched = false;
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        registerMergePostReceiver();
        ((HomeTabActivity) getActivity()).showMenuIfNeeded();
        this.mFeedAdapter.onResume(isFocused());
        startLoadingTimeProfiling();
        Components.injectComponent().addListener(this.mInjectionListener);
        if (this.mAdapter.isEmpty()) {
            fetchInitialRequest(UrlCachePolicy.CACHE_ONLY);
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        Components.injectComponent().removeListener(this.mInjectionListener);
        this.mFeedAdapter.onPause(isFocused());
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected String fetchPosts(int page, String anchor, String backAnchor, boolean userInitiated, UrlCachePolicy cachePolicy) {
        this.mAppController.fetchActivityCounts();
        return this.mAppController.fetchPosts(this.mAppController.getActiveSession(), 20, this.mAppController.getActiveId(), 1, page, anchor, backAnchor, userInitiated, String.valueOf(this.mAppController.getActiveId()), null, null, cachePolicy, false);
    }

    class HomeTimelineSessionListener extends BaseTimelineFragment.TimeLineSessionListener {
        HomeTimelineSessionListener() {
            super();
        }

        @Override // co.vine.android.BaseTimelineFragment.TimeLineSessionListener, co.vine.android.client.AppSessionListener
        public void onGetTimeLineComplete(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, ArrayList<TimelineItem> items, String tag, int pageType, int next, int previous, String anchor, String backAnchor, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
            boolean requestPresent = HomeTimelineFragment.this.hasRequest(reqId);
            if (items != null && HomeTimelineFragment.this.mFeedAdapter.getCount() == 0 && !HomeTimelineFragment.this.mHasStickyHeader) {
                HomeTimelineFragment.this.checkStickyCard(items);
            }
            boolean shouldShowStickyHeader = ClientFlagsHelper.showDiscoverStickyHeader(HomeTimelineFragment.this.getActivity());
            if (shouldShowStickyHeader && HomeTimelineFragment.this.mHasStickyHeader && items != null && !items.isEmpty() && items.get(0).getType() == TimelineItemType.POST_MOSAIC) {
                items.remove(0);
            }
            super.onGetTimeLineComplete(reqId, statusCode, reasonPhrase, type, count, memory, items, tag, pageType, next, previous, anchor, backAnchor, userInitiated, size, title, cachePolicy, network, bundle);
            if (shouldShowStickyHeader && HomeTimelineFragment.this.mHasStickyHeader && HomeTimelineFragment.this.mStickyHeader.getVisibility() == 4) {
                SuggestedFeedStickyCardHelper helper = new SuggestedFeedStickyCardHelper();
                helper.initDiscoverStickyHeader(HomeTimelineFragment.this.mStickyHeader, HomeTimelineFragment.this.mStickyPlaceholder, HomeTimelineFragment.this.getActivity(), HomeTimelineFragment.this.mAppController, HomeTimelineFragment.this.mHandler, HomeTimelineFragment.this.mStickyCardItem);
            }
            if (requestPresent && statusCode == 200 && cachePolicy.mNetworkDataAllowed) {
                if (!HomeTimelineFragment.this.mFreshPostsCountWasLogged) {
                    HomeTimelineFragment.this.mFreshPostsCountWasLogged = true;
                    HomeTimelineFragment.this.logFreshPostCount(items);
                }
                if (ClientFlagsHelper.isSuggestedFeedMosaicInjectionEnabled(HomeTimelineFragment.this.getActivity())) {
                    if (next == 2) {
                        HomeTimelineFragment.this.mStaleContentPointIndex = -1;
                    }
                    HomeTimelineFragment.this.fetchSuggestedInjectIfStalePointFound();
                }
                HomeTimelineFragment.this.identifyAndSaveStalePointItemId();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchSuggestedInjectIfStalePointFound() {
        if (this.mStaleContentPointIndex == -1) {
            this.mStaleContentPointIndex = this.mFeedAdapter.getItemIndexForPostWithTimelineItemId(this.mStaleContentPointTimelineItemId);
            if (this.mStaleContentPointIndex >= 10) {
                addRequest(Components.injectComponent().fetchSuggestedFeedMosaic(this.mAppController));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logFreshPostCount(ArrayList<TimelineItem> items) {
        if (this.mStaleContentPointTimelineItemId != -1) {
            ArrayList<VinePost> posts = new ArrayList<>();
            Iterator<TimelineItem> it = items.iterator();
            while (it.hasNext()) {
                TimelineItem item = it.next();
                if (item.getType() == TimelineItemType.POST && ((VinePost) item).timelineItemId == this.mStaleContentPointTimelineItemId) {
                    break;
                } else if (item.getType() == TimelineItemType.POST) {
                    posts.add((VinePost) item);
                }
            }
            FreshPostsScribeLogger.logFreshPosts(ScribeLoggerSingleton.getInstance(getActivity()), AppStateProviderSingleton.getInstance(getActivity()), posts, this.mStaleContentPointTimestamp);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void identifyAndSaveStalePointItemId() {
        for (int i = 0; i < this.mFeedAdapter.getCount(); i++) {
            TimelineItem item = (TimelineItem) this.mFeedAdapter.getItem(i);
            if (item.getType() == TimelineItemType.POST) {
                StaleFeedStartIdManager.setStaleFeedStartPointIdAndTimestamp(getActivity(), ((VinePost) item).timelineItemId);
                return;
            }
        }
    }
}
