package co.vine.android;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.vine.android.api.VineEverydayNotification;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VineUser;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.ArrayListScrollListener;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.GCMNotificationService;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.Util;
import co.vine.android.widget.ActivityViewHolder;
import co.vine.android.widget.OnTabChangedListener;
import co.vine.android.widget.PinnedHeaderListView;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ActivityFragment extends BaseArrayListFragment implements View.OnClickListener, OnTabChangedListener {
    private View mEmptyActivityView;
    private boolean mFetched;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private boolean mPrivate;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCallbackListeners();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("state_private")) {
                this.mPrivate = savedInstanceState.getBoolean("state_private");
            }
        } else {
            SharedPreferences pr = Util.getDefaultSharedPrefs(getActivity());
            this.mPrivate = pr.getBoolean("settings_private", false);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().getBoolean("take_focus", false) && !this.mFetched) {
            HomeTabActivity callback = (HomeTabActivity) getActivity();
            callback.showPage(1, this);
            if (BuildUtil.isLogsOn()) {
                Log.d("ActivityFragment", "Activity tab took focus.");
            }
        }
        this.mListView.setOnScrollListener(new ArrayListScrollListener() { // from class: co.vine.android.ActivityFragment.1
            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            protected void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (ActivityFragment.this.mRefreshable && ActivityFragment.this.mNextPage > 0 && ActivityFragment.this.mAdapter.getCount() <= 400) {
                    ActivityFragment.this.fetchContent(1, UrlCachePolicy.FORCE_REFRESH);
                }
            }
        });
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(getActivity().getApplicationContext()), AppStateProviderSingleton.getInstance(getActivity()), AppNavigationProviderSingleton.getInstance());
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createView(inflater, R.layout.msg_list_fragment_activity, container);
        this.mListView.setDividerHeight(0);
        this.mAdapter = new ActivityAdapter(getActivity(), this.mListView, this.mAppController, this);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        if (getActivity() instanceof HomeTabActivity) {
            View header = LayoutInflater.from(getActivity()).inflate(R.layout.under_nav_header, (ViewGroup) null);
            ((PinnedHeaderListView) this.mListView).setRefreshHeader(header, getResources().getDimensionPixelSize(R.dimen.tabbar_height));
            this.mListView.addHeaderView(header, null, false);
        }
        this.mEmptyActivityView = v.findViewById(R.id.empty_activity_layout);
        return v;
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mAdapter.isEmpty() && !this.mFetched) {
            fetchContent(3, UrlCachePolicy.CACHE_ONLY);
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state_private", this.mPrivate);
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void refresh() {
        fetchContent(2, UrlCachePolicy.FORCE_REFRESH);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        super.onMoveTo(oldPosition);
        this.mFocused = true;
        SharedPreferences pr = Util.getDefaultSharedPrefs(getActivity());
        this.mPrivate = pr.getBoolean("settings_private", false);
        if (this.mAdapter != null && !hasPendingRequest()) {
            if (this.mAdapter.isEmpty()) {
                fetchContent(3, UrlCachePolicy.NETWORK_THEN_CACHE);
            } else {
                fetchContent(2, true, UrlCachePolicy.NETWORK_THEN_CACHE);
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
        super.onMoveAway(newPosition);
        this.mFocused = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(int fetchType, UrlCachePolicy cachePolicy) {
        fetchContent(fetchType, false, cachePolicy);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(int fetchType, boolean silent, UrlCachePolicy cachePolicy) {
        if (!hasPendingRequest(fetchType)) {
            this.mFetched = true;
            if (!silent) {
                showProgress(fetchType);
            }
            switch (fetchType) {
                case 2:
                    this.mNextPage = 1;
                    this.mAnchor = null;
                    break;
                case 3:
                    this.mNextPage = 1;
                    this.mAnchor = null;
                    break;
            }
            addRequest(this.mAppController.fetchActivity(this.mAppController.getActiveSession(), this.mNextPage, this.mAnchor, this.mPrivate, isFocused(), cachePolicy), fetchType);
            this.mAppController.fetchActivityCounts();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        ActivityAdapter adapter = (ActivityAdapter) this.mAdapter;
        int id = view.getId();
        if (id == R.id.user_image_overlay) {
            ChannelActivity.startProfile(getActivity(), ((Long) view.getTag()).longValue(), "Activity Feed");
            return;
        }
        if (id == R.id.follow) {
            long userId = ((Long) view.getTag()).longValue();
            if (!((ActivityAdapter) this.mAdapter).isFollowing(userId)) {
                adapter.follow(userId);
                addRequest(Components.userInteractionsComponent().followUser(this.mAppController, userId, false, this.mFollowScribeActionsLogger));
            } else {
                adapter.unfollow(userId);
                addRequest(Components.userInteractionsComponent().unfollowUser(this.mAppController, userId, false, this.mFollowScribeActionsLogger));
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (v.getTag() instanceof ActivityViewHolder) {
            ActivityViewHolder holder = (ActivityViewHolder) v.getTag();
            if (holder.notification != null && !TextUtils.isEmpty(holder.notification.getLink())) {
                LinkDispatcher.dispatch(holder.notification.getLink(), getActivity());
            }
        }
    }

    @Override // co.vine.android.BaseFragment
    protected AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.ACTIVITY_LIST;
    }

    private void setupCallbackListeners() {
        setAppSessionListener(new AppSessionListener() { // from class: co.vine.android.ActivityFragment.2
            @Override // co.vine.android.client.AppSessionListener
            public void onGetActivityComplete(String reqId, int statusCode, String reasonPhrase, VinePagedData<VineEverydayNotification> notifications, PagedActivityResponse.Data followRequests, UrlCachePolicy policyUsed) {
                PendingRequest req = ActivityFragment.this.removeRequest(reqId);
                FragmentActivity activity = ActivityFragment.this.getActivity();
                if (req != null && activity != null) {
                    if (!policyUsed.mNetworkDataAllowed) {
                        ActivityFragment.this.fetchContent(3, true, UrlCachePolicy.NETWORK_THEN_CACHE);
                    }
                    ActivityFragment.this.hideProgress(req.fetchType);
                    SharedPreferences pr = Util.getDefaultSharedPrefs(activity);
                    ActivityFragment.this.mPrivate = pr.getBoolean("settings_private", false);
                    int count = 0;
                    if (notifications != null) {
                        count = notifications.items == null ? 0 : notifications.items.size();
                    }
                    int followRequestCount = 0;
                    if (followRequests != null) {
                        followRequestCount = followRequests.items == null ? 0 : followRequests.items.size();
                    }
                    updateEmptyState(ActivityFragment.this.mAdapter.getCount() == 0 && count == 0 && followRequestCount == 0, statusCode != 200);
                    if (statusCode == 200) {
                        if (notifications != null) {
                            ActivityFragment.this.mNextPage = notifications.nextPage;
                            ActivityFragment.this.mAnchor = notifications.anchor;
                        }
                        if (ActivityFragment.this.mAdapter != null) {
                            ((ActivityAdapter) ActivityFragment.this.mAdapter).mergeData(notifications == null ? null : notifications.items, followRequests == null ? null : followRequests.items, req.fetchType == 1);
                        }
                    }
                    activity.startService(GCMNotificationService.getClearNotificationIntent(activity, 1));
                }
            }

            private void updateEmptyState(boolean isListEmpty, boolean isError) {
                if (!isListEmpty) {
                    ActivityFragment.this.mEmptyActivityView.setVisibility(8);
                    ActivityFragment.this.hideSadface();
                } else if (!isError) {
                    ActivityFragment.this.mEmptyActivityView.setVisibility(0);
                    ActivityFragment.this.hideSadface();
                } else {
                    ActivityFragment.this.setEmptyStringMessage(R.string.failed_to_load_activity);
                    ActivityFragment.this.mEmptyActivityView.setVisibility(8);
                    ActivityFragment.this.showSadface(true);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                ((ActivityAdapter) ActivityFragment.this.mAdapter).setImages(images);
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onGetUsersMeComplete(String reqId, int statusCode, String reasonPhrase, long sessionOwnerId, VineUser meUser, UrlCachePolicy policy) {
                if (meUser != null && meUser.isPrivate()) {
                    ActivityFragment.this.mPrivate = true;
                }
            }
        });
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.ActivityFragment.3
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                PendingRequest req = ActivityFragment.this.removeRequest(reqId);
                if (req != null && statusCode != 200) {
                    ((ActivityAdapter) ActivityFragment.this.mAdapter).unfollow(userId);
                }
            }

            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                PendingRequest req = ActivityFragment.this.removeRequest(reqId);
                if (req != null && statusCode != 200) {
                    ((ActivityAdapter) ActivityFragment.this.mAdapter).follow(userId);
                }
            }
        });
    }
}
