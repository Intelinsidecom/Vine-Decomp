package co.vine.android;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.feedadapter.ArrayListScrollListener;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.search.UserSearchAdapter;
import co.vine.android.service.PendingAction;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class UsersFragment extends BaseArrayListFragment {
    private String mAnchorArgument;
    private boolean mFetched;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    protected Friendships mFriendships;
    private long mNotificationId;
    private long mPostId;
    private long mProfileId;
    private int mRetries = 0;
    private String mSearchQuery;
    private int mUsersType;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.mUsersType = args.getInt("u_type", -1);
        this.mProfileId = args.getLong("p_id");
        this.mPostId = args.getLong("post_id", -1L);
        this.mNotificationId = args.getLong("notification_id", -1L);
        this.mAnchorArgument = args.getString("anchor");
        this.mSearchQuery = args.getString("q", null);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("state_friendships")) {
                this.mFriendships = (Friendships) savedInstanceState.getParcelable("state_friendships");
            }
            if (this.mFriendships == null) {
                this.mFriendships = new Friendships();
                return;
            }
            return;
        }
        this.mFriendships = new Friendships();
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(getActivity().getApplicationContext()), AppStateProviderSingleton.getInstance(getActivity()), AppNavigationProviderSingleton.getInstance());
        FollowButtonClickListener followClickListener = new FollowButtonClickListener(getActivity(), this.mAppController, this.mPendingRequestHelper, this.mFriendships, this.mFollowScribeActionsLogger);
        if (this.mUsersType == 8) {
            this.mAdapter = new UserSearchAdapter(getActivity(), this.mAppController, new View.OnClickListener() { // from class: co.vine.android.UsersFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    UsersActivity.startUserListForSearchQuery(view.getContext(), UsersFragment.this.mSearchQuery);
                }
            }, followClickListener, this.mFriendships, false);
            ((UserSearchAdapter) this.mAdapter).setDisplayCount(-1);
        } else {
            this.mAdapter = new FollowableUsersMemoryAdapter(getActivity(), this.mAppController, true, false, followClickListener, this.mFriendships);
        }
        setupCallbackListeners();
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setDividerHeight(0);
        this.mListView.setOnScrollListener(new ArrayListScrollListener() { // from class: co.vine.android.UsersFragment.2
            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            public void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (UsersFragment.this.mRefreshable && UsersFragment.this.mNextPage > 0 && UsersFragment.this.mAdapter.getCount() <= 400) {
                    UsersFragment.this.fetchContent(1);
                }
            }
        });
        ((RefreshableListView) this.mListView).disablePTR(true);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mAdapter.isEmpty() && !this.mFetched) {
            fetchContent(3);
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("state_friendships", this.mFriendships);
    }

    @Override // co.vine.android.BaseArrayListFragment
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (id > 0) {
            ChannelActivity.startProfile(getActivity(), id, "");
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void refresh() {
        fetchContent(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(int fetchType) {
        if (!this.mPendingRequestHelper.hasPendingRequest(fetchType)) {
            String reqId = null;
            switch (fetchType) {
                case 2:
                case 3:
                case 4:
                    this.mNextPage = 1;
                    this.mAnchor = this.mAnchorArgument;
                    break;
            }
            switch (this.mUsersType) {
                case 1:
                    reqId = this.mAppController.fetchFollowing(this.mProfileId, this.mNextPage, this.mAnchor);
                    break;
                case 2:
                    reqId = this.mAppController.fetchFollowers(this.mProfileId, this.mNextPage, this.mAnchor);
                    break;
                case 5:
                    reqId = this.mAppController.fetchPostLikers(this.mAppController.getActiveSession(), this.mPostId, this.mNextPage, this.mAnchor);
                    break;
                case 8:
                    reqId = this.mAppController.searchUsersSectioned(this.mSearchQuery, this.mNextPage);
                    break;
                case 9:
                    reqId = this.mAppController.fetchReviners(this.mAppController.getActiveSession(), this.mPostId, this.mNextPage, this.mAnchor);
                    break;
                case 12:
                    reqId = this.mAppController.fetchNotificationUsers(this.mAppController.getActiveSession(), this.mNotificationId, this.mNextPage, this.mAnchor);
                    break;
                case 14:
                    reqId = this.mAppController.fetchTwitterConnectedFollowing(this.mNextPage, this.mAnchor);
                    break;
            }
            if (reqId != null) {
                this.mPendingRequestHelper.showProgress(fetchType);
                this.mPendingRequestHelper.addRequest(reqId, fetchType);
            }
        }
    }

    private void setupCallbackListeners() {
        setAppSessionListener(new AppSessionListener() { // from class: co.vine.android.UsersFragment.3
            @Override // co.vine.android.client.AppSessionListener
            public void onGetUsersComplete(Session session, String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
                switch (statusCode) {
                    case HttpResponseCode.OK /* 200 */:
                        PendingRequest req = UsersFragment.this.mPendingRequestHelper.removeRequest(reqId);
                        if (req != null) {
                            UsersFragment.this.mPendingRequestHelper.hideProgress(req.fetchType);
                            if (UsersFragment.this.mAdapter != null) {
                                ((UsersMemoryAdapter) UsersFragment.this.mAdapter).mergeData(users, req.fetchType == 1);
                            }
                            UsersFragment.this.mNextPage = nextPage;
                            UsersFragment.this.mAnchor = anchor;
                        }
                        if (users.size() == 0) {
                            UsersFragment.this.showSadface(true);
                            break;
                        } else {
                            UsersFragment.this.hideSadface();
                            break;
                        }
                    default:
                        UsersFragment.this.mPendingRequestHelper.hideProgress(3);
                        break;
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                if (UsersFragment.this.mAdapter != null) {
                    ((UsersMemoryAdapter) UsersFragment.this.mAdapter).setUserImages(images);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onCaptchaRequired(String reqId, String captchaUrl, PendingAction action) {
                PendingRequest req = UsersFragment.this.mPendingRequestHelper.removeRequest(reqId);
                if (req != null) {
                    UsersFragment.this.mPendingRequestHelper.onCaptchaRequired(UsersFragment.this.getActivity(), reqId, action.actionCode, action.bundle, captchaUrl);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhoneVerificationRequired(String reqId, String verifyMsg, PendingAction action) {
                PendingRequest req = UsersFragment.this.mPendingRequestHelper.removeRequest(reqId);
                if (req != null) {
                    UsersFragment.this.mPendingRequestHelper.onPhoneVerificationRequired(UsersFragment.this.getActivity(), reqId, action.actionCode, action.bundle, verifyMsg);
                }
            }
        });
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.UsersFragment.4
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                PendingRequest req = UsersFragment.this.mPendingRequestHelper.removeRequest(reqId);
                if (req != null && statusCode != 200) {
                    UsersFragment.this.mFriendships.removeFollowing(userId);
                    UsersFragment.this.mAdapter.notifyDataSetChanged();
                }
            }

            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                PendingRequest req = UsersFragment.this.mPendingRequestHelper.removeRequest(reqId);
                if (req != null && statusCode != 200) {
                    UsersFragment.this.mFriendships.addFollowing(userId);
                    UsersFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        switch (this.mUsersType) {
            case 1:
                return AppNavigation.Views.FOLLOWING;
            case 2:
                return AppNavigation.Views.FOLLOWERS;
            case 3:
            case 4:
            case 6:
            case 7:
            case 10:
            case 11:
            default:
                return AppNavigation.Views.EMPTY;
            case 5:
                return AppNavigation.Views.LIKES;
            case 8:
                return AppNavigation.Views.SEARCH;
            case 9:
                return AppNavigation.Views.REVINES;
            case 12:
                return AppNavigation.Views.ACTIVITY_USER_LIST;
        }
    }
}
