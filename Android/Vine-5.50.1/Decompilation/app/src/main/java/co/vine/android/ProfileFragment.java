package co.vine.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.vine.android.BaseTimelineFragment;
import co.vine.android.ProfileHeaderAdapter;
import co.vine.android.account.AccountSwitchAdapter;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VinePost;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.feedadapter.RequestKeyGetter;
import co.vine.android.feedadapter.TimelineScrollListener;
import co.vine.android.feedadapter.v2.FeedAdapterItems;
import co.vine.android.feedadapter.v2.FeedNotifier;
import co.vine.android.feedadapter.v2.FeedViewHolderCollection;
import co.vine.android.feedadapter.viewmanager.CardViewManager;
import co.vine.android.feedadapter.viewmanager.FeedCardViewManager;
import co.vine.android.feedadapter.viewmanager.HomePostCardViewManager;
import co.vine.android.feedadapter.viewmanager.SimilarUserCardViewManager;
import co.vine.android.feedadapter.viewmanager.UrlActionCardViewManager;
import co.vine.android.model.ModelEvents;
import co.vine.android.model.PagingInfoModel;
import co.vine.android.model.TagModel;
import co.vine.android.model.TimelineItemModel;
import co.vine.android.model.TimelineModel;
import co.vine.android.model.impl.Timeline;
import co.vine.android.model.impl.TimelineDetails;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.UrlCachePolicy;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.scribe.AppNavigationProvider;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.AppStateProvider;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLogger;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.UIEventScribeLogger;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener;
import co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.share.activities.FeedShareActivity;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.LinkBuilderUtil;
import co.vine.android.util.LinkSuppressor;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.ModalView;
import co.vine.android.widget.OnboardHelper;
import co.vine.android.widget.SectionAdapter;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ProfileFragment extends BaseTimelineFragment implements ViewOffsetResolver {
    private AppNavigationProvider mAppNavProvider;
    private AppStateProvider mAppStateProvider;
    private boolean mColorChangedReceiverIsRegistered;
    private Context mContext;
    private int mCurrentMode;
    private boolean mFavoriteUser;
    protected String mFlurryFollowEventSource;
    private LinearLayout mFollowHeader;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private boolean mFollowing;
    private boolean mHeaderAdded;
    private boolean mHideProfileReposts;
    private MenuItem mHideRevines;
    private boolean mHideUsername;
    private boolean mIsMe;
    private int mLikePage;
    private ModelEvents.ModelListener mModelListener;
    private int mPostPage;
    ProfileHeaderAdapter mProfileHeaderAdapter;
    private ViewGroup mRootView;
    private ScribeLogger mScribeLogger;
    private SectionAdapter mSectionAdapter;
    private SharedPreferences mSharedPreferences;
    private boolean mShouldStartTheaterMode;
    private boolean mShowProfileActions;
    private Boolean mShowingEmptyProfile;
    private boolean mTakeFocus;
    private TimelineDetails mTimelineDetails;
    private TimelineFetchActionsListener mTimelineFetchActionsListener;
    private VineUser mUser;
    private long mUserId;
    private ImageKey mUserProfileImageKeyForShortcut;
    private MenuItem mWatchIcon;
    private final ColorChangedReceiver mColorChangedReceiver = new ColorChangedReceiver();
    private long mWaitingToStartUserId = -1;
    private PromptDialogSupportFragment.OnDialogDoneListener mDownloadDialogListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ProfileFragment.1
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            switch (id) {
                case 69:
                    switch (which) {
                        case -2:
                            ProfileFragment.this.toDownLoader();
                            dialog.dismiss();
                            break;
                        case -1:
                            ProfileFragment.this.toEmail();
                            break;
                    }
            }
        }
    };
    private final AccountSwitchAdapter.AccountSwitchCallback mAccountSwitchCallback = new AccountSwitchAdapter.AccountSwitchCallback() { // from class: co.vine.android.ProfileFragment.2
        @Override // co.vine.android.account.AccountSwitchAdapter.AccountSwitchCallback
        public void onSwitchAccounts() {
            Activity activity = ProfileFragment.this.getActivity();
            if (activity instanceof HomeTabActivity) {
                ((HomeTabActivity) activity).refreshTabs();
            }
        }
    };
    private final UserInteractionsListener mUserInteractionsListener = new UserInteractionsListener() { // from class: co.vine.android.ProfileFragment.3
        @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
        public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
            PendingRequest request = ProfileFragment.this.removeRequest(reqId);
            if (statusCode == 200 && request != null) {
                ProfileFragment.this.mHandler.postDelayed(ProfileFragment.this.mFavoriteUserHintRunnable, 500L);
            }
        }
    };
    private final RemoteRequestControlActionListener mRemoteRequestListener = new RemoteRequestControlActionListener() { // from class: co.vine.android.ProfileFragment.4
        @Override // co.vine.android.service.components.remoterequestcontrol.RemoteRequestControlActionListener
        public void onAbortAllRequestsComplete() {
            ProfileFragment.this.refresh();
        }
    };
    private Runnable mFavoriteUserHintRunnable = new Runnable() { // from class: co.vine.android.ProfileFragment.12
        @Override // java.lang.Runnable
        public void run() {
            Activity context = ProfileFragment.this.getActivity();
            if (!OnboardHelper.getFavoriteUserTooltipShown(context)) {
                View anchor = context.findViewById(R.id.favorite_user);
                if (anchor != null && ProfileFragment.this.mUser != null) {
                    String user = TextUtils.isEmpty(ProfileFragment.this.mUser.username) ? "" : ProfileFragment.this.mUser.username;
                    TooltipOverlayActivity.start(context, anchor, ProfileFragment.this.getString(R.string.favorite_user_tooltip, user), R.drawable.ic_fav_active, 4);
                }
                OnboardHelper.setFavoriteUserTooltipShown(context);
            }
        }
    };

    static /* synthetic */ int access$1008(ProfileFragment x0) {
        int i = x0.mLikePage;
        x0.mLikePage = i + 1;
        return i;
    }

    static /* synthetic */ int access$908(ProfileFragment x0) {
        int i = x0.mPostPage;
        x0.mPostPage = i + 1;
        return i;
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle b = getArguments();
        setUserId(b.getLong("user_id", -1L));
        setFlurryEventSource("Profile: Tab 1");
        this.mFlurryFollowEventSource = b.getString("event_source");
        this.mTakeFocus = b.getBoolean("take_focus", false);
        if (this.mTakeFocus) {
            setFocused(true);
        }
        setAppSessionListener(new ProfileSessionListener());
        if (savedInstanceState != null) {
            this.mCurrentMode = savedInstanceState.getInt("state_mode");
            if (savedInstanceState.containsKey("stated_header_added")) {
                this.mHeaderAdded = savedInstanceState.getBoolean("stated_header_added");
            }
        } else {
            this.mCurrentMode = 1;
            this.mHeaderAdded = false;
        }
        hideSadface();
        if (this.mUser != null) {
            this.mUser.repostsEnabled = 1;
        }
        if (b.getStringArrayList("vanity_url") != null) {
            addRequest(this.mAppController.fetchUserId(b.getStringArrayList("vanity_url")));
        } else if (this.mUserId > 0) {
            this.mIsMe = this.mUserId == this.mAppController.getActiveId();
        }
        if (this.mIsMe) {
            bindProfileDataAndCounts();
        }
        this.mShowProfileActions = b.getBoolean("show_profile_actions", true);
        this.mHideUsername = b.getBoolean("hide_user_name", false);
        this.mShouldStartTheaterMode = b.getBoolean("watch_mode", false);
        this.mModelListener = new LifetimeSafeModelListener(this, new ModelListener());
        VineModelFactory.getModelInstance().getModelEvents().addListener(this.mModelListener);
        this.mTimelineFetchActionsListener = new TimelineFetchActionsListener() { // from class: co.vine.android.ProfileFragment.5
            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onTimelineFetched(String reqId, int statusCode, String reasonPhrase, int type, int count, boolean memory, boolean userInitiated, int size, String title, UrlCachePolicy cachePolicy, boolean network, Bundle bundle) {
                PendingRequest req = ProfileFragment.this.removeRequest(reqId);
                if (req != null) {
                    if (statusCode == 200) {
                        ProfileFragment.this.updateCounts();
                        ProfileFragment.this.mProfileHeaderAdapter.notifyDataSetChanged();
                    }
                    if (memory && statusCode == 200) {
                        boolean shouldForceRefresh = ProfileFragment.this.mSectionAdapter.getNumberOfAdapters() == 1;
                        ProfileFragment.this.refreshUnlockedSectionAdapter(shouldForceRefresh);
                    }
                    if (!cachePolicy.mNetworkDataAllowed) {
                        ProfileFragment.this.fetchInitialRequest(UrlCachePolicy.NETWORK_THEN_CACHE);
                    }
                    ProfileFragment.this.hideProgress(req.fetchType);
                }
            }

            @Override // co.vine.android.service.components.timelinefetch.TimelineFetchActionsListener
            public void onChannelsFetched(String reqId, int statusCode, String reasonPhrase, Bundle bundle) {
            }
        };
        this.mFollowing = false;
        updateTimelineApiUrl();
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mUserId == getAppController().getActiveId()) {
            registerMergePostReceiver();
        }
        if (this.mAdapter.isEmpty()) {
            fetchInitialRequest(UrlCachePolicy.CACHE_ONLY);
        } else if (this.mTimelineDetails != null) {
            updateFeedAdapter();
        }
        setup(isFocused());
        if (getActivity() instanceof HomeTabActivity) {
            HomeTabActivity activity = (HomeTabActivity) getActivity();
            if (activity.shouldChangeActionBarColor()) {
                setActionBarColor();
            }
        }
        initProfile(UrlCachePolicy.CACHE_ONLY);
        if (!this.mColorChangedReceiverIsRegistered) {
            this.mColorChangedReceiverIsRegistered = true;
            getActivity().registerReceiver(this.mColorChangedReceiver, Util.COLOR_CHANGED_INTENT_FILTER, CrossConstants.BROADCAST_PERMISSION, null);
        }
        Components.userInteractionsComponent().addListener(this.mUserInteractionsListener);
        Components.remoteRequestControlComponent().addListener(this.mRemoteRequestListener);
        Components.timelineFetchComponent().addListener(this.mTimelineFetchActionsListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initProfile(UrlCachePolicy cachePolicy) {
        if (this.mIsMe) {
            addRequest(this.mAppController.fetchUsersMe(this.mUserId, cachePolicy));
        } else {
            addRequest(this.mAppController.fetchUser(this.mUserId, cachePolicy));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bindProfileDataAndCounts() throws Resources.NotFoundException {
        if (this.mUser != null) {
            bindUserData(this.mUser);
        }
    }

    void bindUserData(VineUser user) throws Resources.NotFoundException {
        if (user.profileBackground <= 0) {
            user.profileBackground = Settings.DEFAULT_PROFILE_COLOR;
        }
        if (user.profileBackground > 0) {
            if (getActivity() instanceof ChannelActivity) {
                ((BaseControllerActionBarActivity) getActivity()).setActionBarColor(user.profileBackground);
                ((BaseControllerActionBarActivity) getActivity()).notifyColorChange(user.profileBackground);
            }
            if (this.mFeedAdapter != null) {
                this.mFeedAdapter.setProfileColor(user.profileBackground);
            }
        }
        this.mProfileHeaderAdapter.bindUser(user);
        if (user.hasFollowApprovalPending() && !this.mHeaderAdded) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            LinearLayout approvalPending = (LinearLayout) inflater.inflate(R.layout.follow_request_approval_pending, (ViewGroup) null, false);
            if (this.mListView != null) {
                this.mListView.addHeaderView(approvalPending);
                RelativeLayout pendingFollowAccept = (RelativeLayout) approvalPending.findViewById(R.id.follow_request_accept_container);
                pendingFollowAccept.setOnClickListener(this.mProfileHeaderAdapter);
                RelativeLayout pendingFollowReject = (RelativeLayout) approvalPending.findViewById(R.id.follow_request_reject_container);
                pendingFollowReject.setOnClickListener(this.mProfileHeaderAdapter);
                this.mFollowHeader = approvalPending;
                this.mHeaderAdded = true;
            }
            this.mFollowHeader.setVisibility(0);
        }
        setRevineOptionText(user, user.areRepostsEnabled());
        if (user.profileBackground > 0 && this.mFeedAdapter != null && this.mListView != null) {
            this.mFeedAdapter.setProfileColor(user.profileBackground);
            RefreshableListView rlv = (RefreshableListView) this.mListView;
            int color = (-16777216) | user.profileBackground;
            rlv.setPullToRefreshBackgroundColor(color);
            rlv.colorizePTR(-1);
        }
        this.mFavoriteUser = user.notifyPosts;
        if (getActivity() instanceof ChannelActivity) {
            ((ChannelActivity) getActivity()).updateActionBarWithUsername(this.mUser.username, this.mUser.hiddenTwitter ? null : this.mUser.twitterScreenname);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCounts() {
        if (this.mUser != null) {
            if (this.mHideProfileReposts) {
                this.mProfileHeaderAdapter.updatePostCount(this.mUser.authoredPostCount, 0);
            } else if (this.mUser != null) {
                this.mProfileHeaderAdapter.updatePostCount(this.mUser.postCount, 0);
            }
            if (this.mUser != null) {
                this.mProfileHeaderAdapter.updatePostCount(this.mUser.likeCount, 1);
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!this.mIsMe) {
            inflater.inflate(R.menu.profile, menu);
            if (BuildUtil.isAmazon() || !ClientFlagsHelper.isTheaterModeEnabled(getActivity())) {
                menu.removeItem(R.id.feed);
                menu.removeItem(R.id.message);
            }
            if (this.mUser == null || this.mUser.isPrivate()) {
                menu.removeItem(R.id.share_profile);
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem item) {
        Bitmap bitmap;
        Activity context;
        int id = item.getItemId();
        Activity activity = getActivity();
        if (id == R.id.block_or_report) {
            if (this.mUser != null) {
                if (!this.mUser.isBlocking()) {
                    Intent i = ReportingActivity.getReportUserIntent(activity, this.mUser.userId, this.mUser.username, this.mUser.isBlocking());
                    activity.startActivity(i);
                } else {
                    this.mAppController.unblockUser(this.mUser.userId, null);
                }
            }
            return true;
        }
        if (id == R.id.show_revines) {
            if (this.mUser != null) {
                if (this.mUser.areRepostsEnabled()) {
                    addRequest(this.mAppController.disableReposts(this.mUserId));
                } else {
                    addRequest(this.mAppController.enableReposts(this.mUserId));
                }
            }
            return true;
        }
        if (id == R.id.share_profile) {
            if (this.mUser == null || (context = getActivity()) == null) {
                return true;
            }
            if (!ClientFlagsHelper.isProfileShareEnabled(context)) {
                FlurryUtils.trackShareProfile();
                Intent share = new Intent("android.intent.action.SEND");
                share.setType("text/plain");
                share.addFlags(524288);
                share.putExtra("android.intent.extra.SUBJECT", "");
                share.putExtra("android.intent.extra.TEXT", context.getString(R.string.share_profile_txt, new Object[]{this.mUser.username, String.valueOf(this.mUser.userId)}).replace("vine://user", BuildUtil.getWebsiteUrl(context) + "/u"));
                startActivity(Intent.createChooser(share, getString(R.string.send)));
            } else {
                TimelineItem currentItem = (TimelineItem) this.mFeedAdapter.getItem(this.mFeedAdapter.getCurrentPosition());
                if (currentItem instanceof VinePost) {
                    VinePost post = (VinePost) currentItem;
                    RequestKeyGetter keyGetter = new RequestKeyGetter(context, this.mLogger);
                    VideoKey videoKey = keyGetter.getRequestKey(post, false);
                    VideoKey lowVideoKey = keyGetter.getRequestKey(post, true);
                    String remoteVideoUrl = post.videoUrl;
                    Intent shareIntent = FeedShareActivity.getIntent(activity, post.postId, post.shareUrl, post.username, post.thumbnailUrl, remoteVideoUrl, videoKey, lowVideoKey, FeedMetadata.FeedType.PROFILE, this.mUserId);
                    activity.startActivityForResult(shareIntent, 21);
                }
            }
            return true;
        }
        if (id == R.id.create_shortcut) {
            if (this.mUser != null) {
                this.mUserProfileImageKeyForShortcut = new ImageKey(this.mUser.avatarUrl, true);
                if (TextUtils.isEmpty(this.mUser.avatarUrl) || Util.isDefaultAvatarUrl(this.mUser.avatarUrl)) {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_large);
                } else {
                    bitmap = this.mAppController.getPhotoBitmap(this.mUserProfileImageKeyForShortcut);
                }
                if (bitmap != null) {
                    addProfileShortCut(bitmap);
                }
            }
        } else if (id == R.id.message) {
            onMessageUserClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addProfileShortCut(Bitmap bitmap) {
        this.mUserProfileImageKeyForShortcut = null;
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FlurryUtils.trackCreateProfileShortCut();
            Intent shortcutIntent = new Intent(activity, (Class<?>) StartActivity.class);
            shortcutIntent.setAction("android.intent.action.VIEW");
            shortcutIntent.setData(Uri.parse("https://vine.co/u/" + this.mUser.userId));
            shortcutIntent.setFlags(268435456);
            Intent addIntent = new Intent();
            addIntent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
            addIntent.putExtra("android.intent.extra.shortcut.NAME", this.mUser.username);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);
            addIntent.putExtra("android.intent.extra.shortcut.ICON", scaledBitmap);
            addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            activity.getApplicationContext().sendBroadcast(addIntent);
            Util.showCenteredToast(activity, R.string.shorcut_created);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onPrepareOptionsMenu(Menu menu) {
        boolean isBlocking = this.mUser != null && this.mUser.isBlocking();
        MenuItem block = menu.findItem(R.id.block_or_report);
        if (block != null) {
            if (isBlocking) {
                block.setTitle(R.string.unblock_person);
            } else {
                block.setTitle(R.string.block_or_report);
            }
        }
        this.mWatchIcon = menu.findItem(R.id.feed);
        if (this.mWatchIcon != null) {
            this.mWatchIcon.setVisible(false);
            this.mWatchIcon.setEnabled(false);
        }
        this.mHideRevines = menu.findItem(R.id.show_revines);
        boolean revinesEnabled = this.mUser == null || this.mUser.areRepostsEnabled();
        setRevineOptionText(this.mUser, revinesEnabled);
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected LinkSuppressor getLinkSuppressor() {
        return new LinkSuppressor() { // from class: co.vine.android.ProfileFragment.6
            @Override // co.vine.android.util.LinkSuppressor
            public boolean shouldSuppressUserLink(long id) {
                return id == ProfileFragment.this.mUserId;
            }
        };
    }

    @Override // co.vine.android.BaseTimelineFragment
    public ArrayList<CardViewManager> getViewManagers(Activity activity, FeedNotifier feedNotifier, FeedAdapterItems adapterItems, FeedViewHolderCollection viewHolderCollection) {
        ArrayList<CardViewManager> viewManagers = new ArrayList<>();
        HomePostCardViewManager.Builder postViewManagerBuilder = new HomePostCardViewManager.Builder();
        postViewManagerBuilder.items(adapterItems).notifier(feedNotifier).viewHolders(viewHolderCollection).listView(this.mListView).context(activity).callback(this.mTimelineClickEventCallback).friendships(this.mFriendships).linkSuppressor(getLinkSuppressor()).followActionsLogger(this.mFollowScribeActionsLogger).postPlayedListener(this.mTimelineItemScribeActionsLogger).logger(this.mLogger).followEventSource(this.mFlurryEventSource);
        viewManagers.add(postViewManagerBuilder.build());
        SimilarUserCardViewManager.Builder similarViewManagerBuilder = new SimilarUserCardViewManager.Builder();
        similarViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback).mosaicLogger(this.mTimelineItemScribeActionsLogger);
        viewManagers.add(similarViewManagerBuilder.build());
        UrlActionCardViewManager.Builder urlActionViewManagerBuilder = new UrlActionCardViewManager.Builder();
        urlActionViewManagerBuilder.items(adapterItems).viewHolders(viewHolderCollection).context(activity).callback(this.mTimelineClickEventCallback);
        viewManagers.add(urlActionViewManagerBuilder.build());
        FeedCardViewManager.Builder feedCardViewManagerBuilder = new FeedCardViewManager.Builder();
        feedCardViewManagerBuilder.items(adapterItems).notifier(feedNotifier).viewHolders(viewHolderCollection).listView(this.mListView).context(activity).callback(this.mTimelineClickEventCallback).friendships(this.mFriendships).linkSuppressor(getLinkSuppressor()).onTopViewBoundListener(getOnTopViewBoundListener()).followActionsLogger(this.mFollowScribeActionsLogger).postPlayedListener(this.mTimelineItemScribeActionsLogger).logger(this.mLogger).followEventSource(this.mFlurryEventSource);
        viewManagers.add(feedCardViewManagerBuilder.build());
        return viewManagers;
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected String fetchPosts(int page, String anchor, String backAnchor, boolean userInitiated, UrlCachePolicy cachePolicy) {
        throw new IllegalStateException("Don't call fetch posts for ProfileFragment, this fragment handles its own fetching.");
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) throws Resources.NotFoundException {
        super.onMoveTo(oldPosition);
        setup(isFocused());
        bindProfileDataAndCounts();
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
        this.mRootView = (ViewGroup) view.findViewById(R.id.layout_root);
        return view;
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Resources.NotFoundException {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        this.mSharedPreferences = Util.getDefaultSharedPrefs(activity);
        if (getArguments().getBoolean("take_focus", false) && (activity instanceof HomeTabActivity)) {
            HomeTabActivity callback = (HomeTabActivity) activity;
            callback.showPage(3, this);
            if (BuildUtil.isLogsOn()) {
                Log.d("ProfileFragment", "Me tab took focus.");
            }
        }
        this.mListView.setOnScrollListener(new TimelineScrollListener(this.mFeedAdapter) { // from class: co.vine.android.ProfileFragment.7
            private int visiblePercent = 100;
            private Rect mScrollBounds = new Rect();

            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            protected void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (ProfileFragment.this.mRefreshable && PagingInfoModel.getInstance().hasMore(ProfileFragment.this.mTimelineDetails.getUniqueMarker()) && this.mFeedAdapter.getCount() <= 400) {
                    this.mFeedAdapter.onFocusChanged(ProfileFragment.this.isFocused());
                    ProfileFragment.this.mLastFetchType = 1;
                    switch (ProfileFragment.this.mCurrentMode) {
                        case 1:
                            ProfileFragment.this.showProgress(1);
                            ProfileFragment.this.fetchContent(1, true, UrlCachePolicy.FORCE_REFRESH, false);
                            ProfileFragment.access$908(ProfileFragment.this);
                            FlurryUtils.trackTimeLinePageScroll(getClass().getName() + "_Post", ProfileFragment.this.mPostPage);
                            break;
                        case 2:
                            ProfileFragment.this.showProgress(1);
                            ProfileFragment.this.fetchContent(1, true, UrlCachePolicy.FORCE_REFRESH, false);
                            ProfileFragment.access$1008(ProfileFragment.this);
                            FlurryUtils.trackTimeLinePageScroll(getClass().getName() + "_Like", ProfileFragment.this.mLikePage);
                            break;
                    }
                }
            }

            @Override // co.vine.android.feedadapter.ArrayListScrollListener, android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (!ProfileFragment.this.mIsMe) {
                    this.visiblePercent = ViewUtil.getViewVisiblePercentVertical(ProfileFragment.this.mListView, this.mScrollBounds, view.findViewById(R.id.watch_button));
                    ProfileFragment.this.setWatchIconVisible(this.visiblePercent);
                }
            }
        });
        this.mScribeLogger = ScribeLoggerSingleton.getInstance(activity);
        this.mAppStateProvider = AppStateProviderSingleton.getInstance(activity);
        this.mAppNavProvider = AppNavigationProviderSingleton.getInstance();
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(this.mScribeLogger, this.mAppStateProvider, this.mAppNavProvider);
        this.mContext = getActivity();
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
            case 0:
                if (resultCode == 1 && this.mUserId > 0) {
                    addRequest(this.mAppController.fetchFriends(1, 1));
                    addRequest(this.mAppController.fetchFriends(1, 0));
                    break;
                }
                break;
            case 2:
                if (resultCode == -1) {
                    String username = data.getStringExtra("screen_name");
                    String token = data.getStringExtra("token");
                    String secret = data.getStringExtra("secret");
                    long userId = data.getLongExtra("user_id", 0L);
                    this.mAppController.connectTwitter(this.mAppController.getActiveSession(), username, token, secret, userId);
                    break;
                } else if (resultCode != 0) {
                    Util.showCenteredToast(getActivity(), R.string.error_xauth);
                    break;
                }
                break;
            case 3:
                if (data != null) {
                    long postId = data.getLongExtra("id", -1L);
                    int index = this.mFeedAdapter.getItemIndexForPostWithTimelineItemId(postId);
                    if (this.mAppController.getActiveId() == this.mUserId) {
                        scrollTo(index + 2);
                        break;
                    } else {
                        scrollTo(index + 1);
                        break;
                    }
                }
                break;
            case 4:
                this.mFollowing = true;
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
        if (this.mIsMe) {
            addRequest(this.mAppController.fetchUsersMe(this.mAppController.getActiveId(), UrlCachePolicy.FORCE_REFRESH));
        } else {
            addRequest(this.mAppController.fetchUser(this.mUserId, UrlCachePolicy.FORCE_REFRESH));
        }
    }

    private void setRevineOptionText(VineUser user, boolean repostsEnabled) {
        MenuItem hideRevines = this.mHideRevines;
        if (hideRevines != null) {
            if (user != null && !user.isFollowing()) {
                hideRevines.setVisible(false);
            } else if (repostsEnabled) {
                hideRevines.setVisible(true);
                hideRevines.setTitle(R.string.hide_revines);
            } else {
                hideRevines.setVisible(true);
                hideRevines.setTitle(R.string.show_revines);
            }
        }
    }

    public void changeMode(int mode) {
        switch (mode) {
            case 1:
                if (this.mCurrentMode != 1) {
                    this.mCurrentMode = 1;
                    this.mProfileHeaderAdapter.setPostsSortOrder(ProfileSortManager.getInstance().getUserProfilePostsSortOrder(getActivity(), this.mUserId));
                    this.mSectionAdapter = new SectionAdapter(this.mProfileHeaderAdapter);
                    this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
                    setFlurryEventSource("Profile: Tab 1");
                    fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, false);
                    break;
                } else {
                    return;
                }
            case 2:
                if (this.mCurrentMode != 2) {
                    this.mCurrentMode = 2;
                    setFlurryEventSource("Profile: Tab 2");
                    this.mProfileHeaderAdapter.setLikesSortOrder(ProfileSortManager.getInstance().getUserProfileLikesSortOrder(getActivity(), this.mUserId));
                    this.mSectionAdapter = new SectionAdapter(this.mProfileHeaderAdapter);
                    this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
                    fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, false);
                    break;
                } else {
                    return;
                }
        }
        if (getActivity() instanceof HomeTabActivity) {
            ((HomeTabActivity) getActivity()).resetNav();
        }
        updateTimelineApiUrl();
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mFeedAdapter.onPause(isFocused());
        Components.userInteractionsComponent().removeListener(this.mUserInteractionsListener);
        Components.remoteRequestControlComponent().removeListener(this.mRemoteRequestListener);
        Components.timelineFetchComponent().removeListener(this.mTimelineFetchActionsListener);
    }

    @Override // co.vine.android.BaseTimelineFragment, android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (this.mColorChangedReceiverIsRegistered) {
            getActivity().unregisterReceiver(this.mColorChangedReceiver);
            this.mColorChangedReceiverIsRegistered = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setup(boolean takeFocus) {
        if (this.mSectionAdapter == null) {
            if (isLocked()) {
                createLockedSectionAdapter();
            } else {
                refreshUnlockedSectionAdapter(false);
            }
        }
        if (this.mFeedAdapter != null && !isLocked() && takeFocus) {
            this.mFeedAdapter.onResume(true);
        }
    }

    public void toDownloadVines() {
        if (!ClientFlagsHelper.enableEmailDownloader(this.mContext)) {
            toDownLoader();
            return;
        }
        PromptDialogSupportFragment promptDialogSupportFragment = PromptDialogSupportFragment.newInstance(69);
        promptDialogSupportFragment.setListener(this.mDownloadDialogListener);
        promptDialogSupportFragment.setPositiveButton(R.string.send_to_my_email);
        promptDialogSupportFragment.setNegativeButton(R.string.pick_and_save_to_phone);
        promptDialogSupportFragment.setOptionNumber(2);
        promptDialogSupportFragment.setButtonPlacementVertical(true);
        promptDialogSupportFragment.setCancelebleOnOutisde(true);
        promptDialogSupportFragment.show(getActivity().getSupportFragmentManager());
    }

    public void toDownLoader() {
        if (this.mFeedAdapter.isEmpty()) {
            Toast.makeText(this.mContext, R.string.no_posts, 1).show();
        } else {
            Intent i = new Intent(this.mContext, (Class<?>) DownloadVineActivity.class);
            this.mContext.startActivity(i);
        }
    }

    public void toEmail() {
        Resources resources = this.mContext.getResources();
        final String savedEmail = Util.getDefaultSharedPrefs(getActivity()).getString("settings_profile_email", "");
        String message = TextUtils.isEmpty(savedEmail) ? getString(R.string.email_dialog_title_no_email) : getString(R.string.email_dialog_title, savedEmail);
        new AlertDialog.Builder(this.mContext).setMessage(message).setPositiveButton(resources.getString(R.string.confirm_email), new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileFragment.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                String email = savedEmail;
                ProfileFragment.this.mAppController.requestDownloadEmail(email);
            }
        }).setNegativeButton(R.string.go_to_settings, new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileFragment.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent settingIntent = new Intent(ProfileFragment.this.mContext, (Class<?>) SettingsActivity.class);
                ProfileFragment.this.mContext.startActivity(settingIntent);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEmailConfirm() {
        new AlertDialog.Builder(this.mContext).setMessage(this.mContext.getResources().getString(R.string.email_confirm)).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileFragment.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showError() {
        new AlertDialog.Builder(this.mContext).setMessage(R.string.error_requesting).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: co.vine.android.ProfileFragment.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    public void toTheaterMode(Activity activity) {
        if (activity != null) {
            if (this.mFeedAdapter.isEmpty()) {
                Toast.makeText(activity, R.string.no_posts, 1).show();
                return;
            }
            int currentPosition = this.mShouldStartTheaterMode ? 0 : this.mFeedAdapter.getCurrentPosition();
            long currentId = this.mFeedAdapter.findNextPost(currentPosition);
            Intent i = TheaterActivity.getIntent(activity, this.mTimelineDetails, currentId, this.mUser != null ? this.mUser.username : null, this.mApiUrl);
            activity.startActivityForResult(i, 3);
            activity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.fade_out);
        }
    }

    private void createLockedSectionAdapter() {
        this.mProfileHeaderAdapter = new ProfileHeaderAdapter(getActivity(), this.mAppController, new ProfileFragmentHeaderAdapterListener(), this.mHideProfileReposts, ProfileSortManager.getInstance().getUserProfilePostsSortOrder(getActivity(), this.mUserId), ProfileSortManager.getInstance().getUserProfileLikesSortOrder(getActivity(), this.mUserId), true, this.mUser.isBlocked(), this.mAccountSwitchCallback, this.mFlurryFollowEventSource, this.mShowProfileActions, this.mFollowScribeActionsLogger, this.mHideUsername);
        int resourceId = this.mUser.isBlocked() ? R.layout.profile_blocked : R.layout.profile_locked;
        SingleItemAdapter privateAdapter = new SingleItemAdapter(resourceId);
        this.mSectionAdapter = new SectionAdapter(this.mProfileHeaderAdapter, privateAdapter);
        this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
        this.mSectionAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshUnlockedSectionAdapter(boolean forceRefresh) {
        boolean showEmptyProfile = this.mIsMe && this.mFeedAdapter.getCount() == 0;
        if (forceRefresh || this.mShowingEmptyProfile == null || !this.mShowingEmptyProfile.equals(Boolean.valueOf(showEmptyProfile))) {
            if (this.mProfileHeaderAdapter == null) {
                this.mProfileHeaderAdapter = new ProfileHeaderAdapter(getActivity(), this.mAppController, new ProfileFragmentHeaderAdapterListener(), this.mHideProfileReposts, ProfileSortManager.getInstance().getUserProfilePostsSortOrder(getActivity(), this.mUserId), ProfileSortManager.getInstance().getUserProfileLikesSortOrder(getActivity(), this.mUserId), false, false, this.mAccountSwitchCallback, this.mFlurryFollowEventSource, this.mShowProfileActions, this.mFollowScribeActionsLogger, this.mHideUsername);
            }
            if (showEmptyProfile) {
                SingleItemAdapter emptyProfileAdapter = new SingleItemAdapter(this.mCurrentMode == 2 ? R.layout.empty_profile_likes : R.layout.empty_profile_posts);
                this.mSectionAdapter = new SectionAdapter(this.mProfileHeaderAdapter, emptyProfileAdapter);
            } else {
                this.mSectionAdapter = new SectionAdapter(this.mProfileHeaderAdapter, this.mFeedAdapter);
                this.mFeedAdapter.setOffsetResolver(this);
            }
            this.mShowingEmptyProfile = Boolean.valueOf(showEmptyProfile);
            this.mListView.setAdapter((ListAdapter) this.mSectionAdapter);
            this.mSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override // co.vine.android.BaseTimelineFragment
    protected void fetchContent(int fetchType, boolean userInitiated, UrlCachePolicy cachePolicy, boolean forceReplacePosts) {
        String sort;
        this.mLastFetchType = fetchType;
        if (!hasPendingRequest(fetchType) && this.mUserId != -1) {
            if (fetchType == 2) {
                showProgress(fetchType);
            }
            int postGroup = this.mHideProfileReposts ? 10 : 2;
            if (this.mCurrentMode == 2) {
                postGroup = 3;
            }
            if (this.mCurrentMode == 1) {
                sort = ProfileSortManager.getInstance().getUserProfilePostsSortOrder(getActivity(), this.mUserId);
            } else {
                sort = ProfileSortManager.getInstance().getUserProfileLikesSortOrder(getActivity(), this.mUserId);
            }
            if (this.mTimelineDetails == null) {
                this.mTimelineDetails = new TimelineDetails(postGroup, Long.valueOf(this.mUserId), sort);
            } else {
                this.mTimelineDetails.type = postGroup;
                this.mTimelineDetails.channelId = this.mUserId;
                this.mTimelineDetails.sort = sort;
            }
            addRequest(Components.timelineFetchComponent().fetchPosts(this.mAppController, this.mAppController.getActiveSession(), 20, this.mUserId, postGroup, userInitiated, String.valueOf(this.mUserId), sort, null, cachePolicy, forceReplacePosts, -1L, fetchType), fetchType);
        }
    }

    @Override // co.vine.android.BaseFragment
    public void setActionBarColor() {
        if (this.mUser != null) {
            Activity activity = getActivity();
            if (activity instanceof BaseActionBarActivity) {
                if (this.mUser.profileBackground == 0) {
                    this.mUser.profileBackground = Util.getDefaultSharedPrefs(getActivity()).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
                }
                ((BaseActionBarActivity) activity).setActionBarColor(this.mUser.profileBackground);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUserId(long userId) {
        this.mUserId = userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLocked() {
        return (this.mUser == null || this.mIsMe || (!this.mUser.isPrivateLocked() && !this.mUser.isBlocked())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUsersList(int type) {
        Intent i = new Intent(getActivity(), (Class<?>) UsersActivity.class);
        i.putExtra("p_id", this.mUserId);
        i.putExtra("u_type", type);
        startActivity(i);
    }

    private void onMessageUserClicked() {
        if (this.mWaitingToStartUserId == -1) {
            this.mWaitingToStartUserId = this.mUserId;
            this.mAppController.fetchConversationRowIdFromUserRemoteId(this.mUserId, 1);
        }
    }

    private class ProfileFragmentHeaderAdapterListener implements ProfileHeaderAdapter.ProfileHeaderListener {
        private ProfileFragmentHeaderAdapterListener() {
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onFollowUserClicked() {
            ProfileFragment.this.addRequest(Components.userInteractionsComponent().followUser(ProfileFragment.this.mAppController, ProfileFragment.this.mUserId, true, ProfileFragment.this.mFollowScribeActionsLogger));
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onUnfollowUserClicked() {
            ProfileFragment.this.addRequest(Components.userInteractionsComponent().unfollowUser(ProfileFragment.this.mAppController, ProfileFragment.this.mUserId, true, ProfileFragment.this.mFollowScribeActionsLogger));
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onAcceptFollowRequestClicked() {
            ProfileFragment.this.mAppController.acceptFollowRequest(ProfileFragment.this.mAppController.getActiveSession(), ProfileFragment.this.mUserId);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onRejectFollowRequestClicked() {
            ProfileFragment.this.mAppController.rejectFollowRequest(ProfileFragment.this.mAppController.getActiveSession(), ProfileFragment.this.mUserId);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onViewFollowersClicked() {
            ProfileFragment.this.showUsersList(2);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onViewFollowingClicked() {
            ProfileFragment.this.showUsersList(1);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onDropdownActionClicked(int selectionIdx) {
            switch (selectionIdx) {
                case 0:
                    toggleShowRevines();
                    break;
                case 1:
                    setProfileSortOrder("top");
                    break;
                case 2:
                    setProfileSortOrder("recent");
                    break;
                case 3:
                    setProfileSortOrder("oldest");
                    break;
                case 4:
                    setProfileSortOrder("recent");
                    break;
                case 5:
                    setProfileSortOrder("oldest");
                    break;
            }
        }

        private void toggleShowRevines() {
            ProfileFragment.this.mHideProfileReposts = !ProfileFragment.this.mHideProfileReposts;
            FlurryUtils.trackFilterProfileReposts(ProfileFragment.this.mHideProfileReposts, ProfileFragment.this.mUser.following > 0, ProfileFragment.this.mIsMe);
            ProfileFragment.this.mAppController.setHideProfileReposts(ProfileFragment.this.mUserId, ProfileFragment.this.mHideProfileReposts);
            ProfileFragment.this.mFeedAdapter.onFocusChanged(ProfileFragment.this.isFocused());
            ProfileFragment.this.fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, true);
        }

        private void setProfileSortOrder(String sortOrder) {
            Activity activity = ProfileFragment.this.getActivity();
            if (activity != null) {
                if (ProfileFragment.this.mCurrentMode == 1) {
                    ProfileSortManager.getInstance().setUserProfilePostsSortOrder(activity, ProfileFragment.this.mUserId, sortOrder);
                } else {
                    ProfileSortManager.getInstance().setUserProfileLikesSortOrder(activity, ProfileFragment.this.mUserId, sortOrder);
                }
                ProfileFragment.this.mFeedAdapter.onFocusChanged(ProfileFragment.this.isFocused());
                ProfileFragment.this.fetchContent(3, true, UrlCachePolicy.CACHE_THEN_NETWORK, true);
            }
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onDrawerAnimationStart() {
            if (ProfileFragment.this.getActivity() instanceof HomeTabActivity) {
                ((HomeTabActivity) ProfileFragment.this.getActivity()).setScrollAwayNavEnabled(false);
            }
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onDrawerAnimationEnd() {
            if (ProfileFragment.this.getActivity() instanceof HomeTabActivity) {
                ((HomeTabActivity) ProfileFragment.this.getActivity()).setScrollAwayNavEnabled(true);
            }
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onPostsTabClicked() {
            ProfileFragment.this.changeMode(1);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onLikesTabClicked() {
            ProfileFragment.this.changeMode(2);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onOpenTwitterClicked() {
            UIEventScribeLogger.openTwitterTap(ProfileFragment.this.mScribeLogger, ProfileFragment.this.mAppStateProvider, ProfileFragment.this.mAppNavProvider);
            ProfileFragment.this.mProfileHeaderAdapter.hideTwitterTooltip();
            ProfileFragment.this.mProfileHeaderAdapter.openTwitter(ProfileFragment.this.mUser.twitterScreenname);
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onShowAccountSwitchingMenu() {
            if (ClientFlagsHelper.isAccountSwitchingEnabled(ProfileFragment.this.getActivity())) {
                ProfileFragment.this.mProfileHeaderAdapter.toggleAccountSwitchingMenu();
            }
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onTheaterModeClicked() {
            ProfileFragment.this.toTheaterMode(ProfileFragment.this.getActivity());
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onDownloadVideosClicked() {
            ProfileFragment.this.toDownloadVines();
        }

        @Override // co.vine.android.ProfileHeaderAdapter.ProfileHeaderListener
        public void onFavUserClicked() {
            if (ProfileFragment.this.mUser != null) {
                ProfileFragment.this.mFavoriteUser = !ProfileFragment.this.mFavoriteUser;
                ProfileFragment.this.mAppController.favoriteUser(ProfileFragment.this.mUserId, ProfileFragment.this.mFavoriteUser, ProfileFragment.this.mUser.isFollowing());
            }
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
            if (timelineDetails.equals(ProfileFragment.this.mTimelineDetails)) {
                ProfileFragment.this.updateFeedAdapter();
                if (ProfileFragment.this.mShouldStartTheaterMode) {
                    ProfileFragment.this.toTheaterMode(ProfileFragment.this.getActivity());
                    ProfileFragment.this.mShouldStartTheaterMode = false;
                }
            }
        }
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

    class ProfileSessionListener extends BaseTimelineFragment.TimeLineSessionListener {
        ProfileSessionListener() {
            super();
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetConversationRowIdComplete(long recipientId, boolean isRecipientExternal, long conversationObjectId, String username, boolean amFollowing) {
            if (recipientId == ProfileFragment.this.mWaitingToStartUserId) {
                ProfileFragment.this.mWaitingToStartUserId = -1L;
                ProfileFragment.this.mAppController.clearUnreadMessageCount(conversationObjectId);
                ProfileFragment.this.startActivity(ConversationActivity.getIntent(ProfileFragment.this.getActivity(), conversationObjectId, username, recipientId, isRecipientExternal, amFollowing, true));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetUsersMeComplete(String reqId, int statusCode, String reasonPhrase, long sessionOwnerId, VineUser user, UrlCachePolicy policy) throws Resources.NotFoundException {
            PendingRequest req = ProfileFragment.this.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200 && ProfileFragment.this.mIsMe) {
                    ProfileFragment.this.mUser = user;
                    if (user != null) {
                        ProfileFragment.this.bindUserData(user);
                        ProfileFragment.this.updateCounts();
                        ProfileFragment.this.mProfileHeaderAdapter.notifyDataSetChanged();
                    }
                }
                ProfileFragment.this.hideProgress(req.fetchType);
                Intent intent = new Intent("co.vine.android.profileColor");
                ProfileFragment.this.getActivity().sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
                if (!policy.mNetworkDataAllowed) {
                    ProfileFragment.this.initProfile(UrlCachePolicy.NETWORK_THEN_CACHE);
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetUserComplete(String reqId, int statusCode, String reasonPhrase, VineUser user, UrlCachePolicy policy) throws Resources.NotFoundException {
            boolean z = true;
            PendingRequest req = ProfileFragment.this.removeRequest(reqId);
            if (req != null) {
                if (statusCode == 200) {
                    if (ProfileFragment.this.mFollowing) {
                        user.following = 1;
                    }
                    ProfileFragment.this.mUser = user;
                    if (ProfileFragment.this.isLocked()) {
                        ProfileFragment.this.mSectionAdapter = null;
                        ProfileFragment profileFragment = ProfileFragment.this;
                        if (!ProfileFragment.this.isFocused() && !ProfileFragment.this.mTakeFocus) {
                            z = false;
                        }
                        profileFragment.setup(z);
                    }
                    if (ProfileFragment.this.mProfileHeaderAdapter != null && user != null) {
                        ProfileFragment.this.bindUserData(user);
                        ProfileFragment.this.updateCounts();
                        ProfileFragment.this.mProfileHeaderAdapter.notifyDataSetChanged();
                    }
                    if (user != null && user.isFollowing() && !user.notifyPosts) {
                        ProfileFragment.this.mHandler.postDelayed(ProfileFragment.this.mFavoriteUserHintRunnable, 750L);
                    }
                }
                if (!policy.mNetworkDataAllowed) {
                    ProfileFragment.this.initProfile(UrlCachePolicy.NETWORK_THEN_CACHE);
                }
                ProfileFragment.this.hideProgress(req.fetchType);
                ProfileFragment.this.mFollowing = false;
            }
        }

        @Override // co.vine.android.BaseTimelineFragment.TimeLineSessionListener, co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            UrlImage urlImage;
            if (ProfileFragment.this.mProfileHeaderAdapter != null) {
                ProfileFragment.this.mProfileHeaderAdapter.onImageLoaded(images);
                ProfileFragment.this.mFeedAdapter.setImages(images);
            }
            if (ProfileFragment.this.mUserProfileImageKeyForShortcut != null && (urlImage = images.get(ProfileFragment.this.mUserProfileImageKeyForShortcut)) != null && urlImage.isValid()) {
                ProfileFragment.this.addProfileShortCut(urlImage.bitmap);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onBlockUserComplete(String reqId, int statusCode, String reasonPhrase, boolean userBlocked, String blockedUsername) {
            String username;
            FragmentActivity activity = ProfileFragment.this.getActivity();
            if (activity != null) {
                if (statusCode == 200) {
                    if (ProfileFragment.this.mUser != null) {
                        ProfileFragment.this.mUser.blocking = userBlocked ? 1 : 0;
                    }
                    activity.supportInvalidateOptionsMenu();
                    if (isUsernameValid()) {
                        username = ProfileFragment.this.mUser.username;
                    } else {
                        username = ProfileFragment.this.getString(R.string.username_placeholder);
                    }
                    if (!TextUtils.isEmpty(blockedUsername)) {
                        username = blockedUsername;
                    }
                    if (userBlocked) {
                        Util.showDefaultToast(activity, activity.getString(R.string.user_blocked, new Object[]{username}));
                        VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(activity);
                        dbHelper.removeUserWithType(ProfileFragment.this.mUserId, 2, true, Vine.UserGroupsView.CONTENT_URI_FOLLOWERS);
                        return;
                    }
                    Util.showDefaultToast(activity, activity.getString(R.string.user_blocked_error));
                    return;
                }
                Util.showDefaultToast(activity, activity.getString(R.string.user_blocked_error));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onUnblockUserComplete(String reqId, int statusCode, String reasonPhrase, boolean userUnblocked, String blockedUsername) {
            String username;
            if (statusCode == 200) {
                if (ProfileFragment.this.mUser != null) {
                    ProfileFragment.this.mUser.blocking = !userUnblocked ? 1 : 0;
                }
                ProfileFragment.this.getActivity().supportInvalidateOptionsMenu();
                if (isUsernameValid()) {
                    username = ProfileFragment.this.mUser.username;
                } else {
                    username = ProfileFragment.this.getString(R.string.username_placeholder);
                }
                if (!TextUtils.isEmpty(blockedUsername)) {
                    username = blockedUsername;
                }
                if (userUnblocked) {
                    Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.user_unblocked, username));
                    ProfileFragment.this.addRequest(ProfileFragment.this.mAppController.fetchUser(ProfileFragment.this.mUserId, UrlCachePolicy.FORCE_REFRESH));
                    return;
                } else {
                    Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.user_blocked_error));
                    return;
                }
            }
            Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.user_unblocked_error));
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onEnableUserRepostsComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
            String username;
            if (ProfileFragment.this.removeRequest(reqId) != null) {
                if (statusCode == 200) {
                    if (ProfileFragment.this.mUser != null) {
                        ProfileFragment.this.mUser.repostsEnabled = success ? 1 : 0;
                        ProfileFragment.this.getActivity().supportInvalidateOptionsMenu();
                        if (success) {
                            if (isUsernameValid()) {
                                username = ProfileFragment.this.mUser.username;
                            } else {
                                username = ProfileFragment.this.getString(R.string.username_placeholder_alt);
                            }
                            Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_enabled, username));
                            return;
                        }
                        Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_enabled_fail));
                        return;
                    }
                    return;
                }
                Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_enabled_fail));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onDisableUserRepostsComplete(String reqId, int statusCode, String reasonPhrase, boolean success) {
            String username;
            if (ProfileFragment.this.removeRequest(reqId) != null) {
                if (statusCode == 200) {
                    if (ProfileFragment.this.mUser != null) {
                        ProfileFragment.this.mUser.repostsEnabled = success ? 0 : 1;
                        ProfileFragment.this.getActivity().supportInvalidateOptionsMenu();
                        if (success) {
                            if (isUsernameValid()) {
                                username = ProfileFragment.this.mUser.username;
                            } else {
                                username = ProfileFragment.this.getString(R.string.username_placeholder_alt);
                            }
                            Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_disabled, username));
                            return;
                        }
                        Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_disabled_fail));
                        return;
                    }
                    return;
                }
                Util.showDefaultToast(ProfileFragment.this.getActivity(), ProfileFragment.this.getString(R.string.reposts_disabled_fail));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onReportUserComplete(String reqId, int statusCode, String reasonPhrase) {
            Activity activity;
            if (statusCode != 200 && (activity = ProfileFragment.this.getActivity()) != null) {
                Util.showDefaultToast(activity, ProfileFragment.this.getString(R.string.user_reported_error));
            }
        }

        private boolean isUsernameValid() {
            return (ProfileFragment.this.mUser == null || TextUtils.isEmpty(ProfileFragment.this.mUser.username)) ? false : true;
        }

        @Override // co.vine.android.BaseTimelineFragment.TimeLineSessionListener, co.vine.android.client.AppSessionListener
        public void onDeletePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) throws Resources.NotFoundException {
            super.onDeletePostComplete(reqId, postId, statusCode, reasonPhrase);
            if (statusCode == 200) {
                if (ProfileFragment.this.mFeedAdapter != null) {
                    ProfileFragment.this.mFeedAdapter.removeItem(postId);
                    ProfileFragment.this.refreshUnlockedSectionAdapter(false);
                }
                ProfileFragment.this.mSectionAdapter.notifyDataSetChanged();
                ProfileFragment.this.bindProfileDataAndCounts();
            }
        }

        @Override // co.vine.android.BaseTimelineFragment.TimeLineSessionListener, co.vine.android.client.AppSessionListener
        public void onHidePostComplete(String reqId, long postId, int statusCode, String reasonPhrase) {
            super.onHidePostComplete(reqId, postId, statusCode, reasonPhrase);
            if (statusCode == 200 && ProfileFragment.this.mFeedAdapter != null) {
                ProfileFragment.this.mFeedAdapter.togglePostHide(postId);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onRespondFollowRequestComplete(String reqId, int statusCode, String reasonPhrase, boolean accept, long userId) {
            if (statusCode == 200) {
                if (ProfileFragment.this.mFollowHeader != null) {
                    View inner = ProfileFragment.this.mFollowHeader.findViewById(R.id.follow_request_header_inner);
                    inner.setLayoutParams(new LinearLayout.LayoutParams(-1, 0));
                    ProfileFragment.this.mFollowHeader.setVisibility(8);
                    return;
                }
                return;
            }
            if (accept) {
                Util.showCenteredToast(ProfileFragment.this.getActivity(), R.string.accept_failed);
            } else {
                Util.showCenteredToast(ProfileFragment.this.getActivity(), R.string.deny_failed);
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onGetUserIdComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
            PendingRequest req = ProfileFragment.this.removeRequest(reqId);
            if (req == null) {
                return;
            }
            if (userId > 0) {
                ProfileFragment.this.setUserId(userId);
                ProfileFragment.this.mIsMe = ProfileFragment.this.mUserId == ProfileFragment.this.mAppController.getActiveId();
                ProfileFragment.this.setup(ProfileFragment.this.isFocused() || ProfileFragment.this.mTakeFocus);
                ProfileFragment.this.initProfile(UrlCachePolicy.NETWORK_THEN_CACHE);
                ProfileFragment.this.fetchInitialRequest(UrlCachePolicy.NETWORK_THEN_CACHE);
                return;
            }
            ProfileFragment.this.getActivity().finish();
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFavoriteUserComplete(String reqId, int statusCode, String reasonPhrase) {
            FragmentActivity activity = ProfileFragment.this.getActivity();
            if (activity != null) {
                VineUser user = ProfileFragment.this.mUser;
                if (statusCode != 200) {
                    ProfileFragment.this.mFavoriteUser = ProfileFragment.this.mFavoriteUser ? false : true;
                    activity.supportInvalidateOptionsMenu();
                    Util.showCenteredToast(activity, activity.getString(R.string.generic_error));
                    return;
                }
                if (user != null) {
                    user.following = 1;
                    user.notifyPosts = ProfileFragment.this.mFavoriteUser;
                    ProfileFragment.this.mProfileHeaderAdapter.updateFollowButtonState(user);
                    if (ProfileFragment.this.mFavoriteUser) {
                        ModalView modal = new ModalView(ProfileFragment.this.getActivity(), ProfileFragment.this.mRootView, R.layout.favorite_user_modal);
                        if (!TextUtils.isEmpty(ProfileFragment.this.mUser.username)) {
                            TextView textView = (TextView) modal.findViewById(R.id.description);
                            textView.setText(ProfileFragment.this.getString(R.string.favorited_confirmation, ProfileFragment.this.mUser.username));
                        }
                    }
                }
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onConnectTwitterComplete(String reqId, int statusCode, String reasonPhrase, String username, String token, String secret, long userId) throws Resources.NotFoundException {
            if (statusCode == 200 && userId > 0 && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(secret)) {
                ProfileFragment.this.mProfileHeaderAdapter.followOnTwitter(true);
                Session session = ProfileFragment.this.mAppController.getActiveSession();
                VineAccountHelper.saveTwitterInfo(ProfileFragment.this.getActivity(), session.getUserId(), session.getUsername(), username, token, secret, userId);
                ProfileFragment.this.mSharedPreferences.edit().putBoolean("settings_twitter_connected", true).apply();
                return;
            }
            if (TextUtils.isEmpty(reasonPhrase)) {
                reasonPhrase = ProfileFragment.this.getResources().getString(R.string.settings_error_connect_to_twitter);
            }
            Util.showCenteredToast(ProfileFragment.this.getActivity(), reasonPhrase);
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onRequestEmailDownload(String reqId, int statusCode, String reasonPhrase) {
            if (statusCode == 200) {
                ProfileFragment.this.showEmailConfirm();
            } else {
                ProfileFragment.this.showError();
            }
        }
    }

    private final class SingleItemAdapter extends BaseAdapter {
        private final int mResourceId;

        public SingleItemAdapter(int resourceId) {
            this.mResourceId = resourceId;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return 1;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup root) {
            return LayoutInflater.from(ProfileFragment.this.getActivity()).inflate(this.mResourceId, root, false);
        }
    }

    private class ColorChangedReceiver extends BroadcastReceiver {
        private ColorChangedReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "co.vine.android.profileColor".equals(intent.getAction()) && ProfileFragment.this.mUser != null && ProfileFragment.this.mFocused) {
                BaseControllerActionBarActivity activity = (BaseControllerActionBarActivity) ProfileFragment.this.getActivity();
                ProfileFragment.this.mUser.profileBackground = Util.getDefaultSharedPrefs(activity).getInt("profile_background", Settings.DEFAULT_PROFILE_COLOR);
                activity.setActionBarColor(ProfileFragment.this.mUser.profileBackground);
                activity.notifyColorChange(ProfileFragment.this.mUser.profileBackground);
                if (ProfileFragment.this.mFeedAdapter != null) {
                    ProfileFragment.this.mFeedAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override // co.vine.android.BaseTimelineFragment, co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.PROFILE;
    }

    private void updateTimelineApiUrl() {
        Bundle data = new Bundle();
        data.putLong("profile_id", this.mUserId);
        int type = this.mCurrentMode == 1 ? 2 : 3;
        this.mApiUrl = LinkBuilderUtil.buildUrl(type, data);
    }

    public void togglerTwitterTooltip() {
        if (this.mUser.twitterScreenname != null) {
            this.mProfileHeaderAdapter.toggleTwitterTooltip();
        }
    }
}
