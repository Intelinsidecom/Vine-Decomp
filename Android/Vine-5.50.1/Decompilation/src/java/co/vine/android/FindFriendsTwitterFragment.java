package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.vine.android.FindFriendsNUXActivity;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.TwitterVineApp;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsComponent;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widget.UserViewHolder;
import com.twitter.android.sdk.Twitter;
import com.twitter.android.widget.ItemPosition;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FindFriendsTwitterFragment extends BaseCursorListFragment implements View.OnClickListener, AdapterView.OnItemClickListener, FindFriendsNUXActivity.FilterableFriendsList {
    private FindFriendsNUXActivity mActivity;
    private StyleSpan[] mBold;
    private RelativeLayout mContactsCountContainer;
    private TextView mContactsText;
    private boolean mFetched;
    private boolean mFilterLoaderIsReady;
    private Runnable mFilterRunnable = new Runnable() { // from class: co.vine.android.FindFriendsTwitterFragment.1
        @Override // java.lang.Runnable
        public void run() {
            if (!FindFriendsTwitterFragment.this.mFilterLoaderIsReady) {
                FindFriendsTwitterFragment.this.initLoader(1);
                FindFriendsTwitterFragment.this.mFilterLoaderIsReady = true;
            } else {
                FindFriendsTwitterFragment.this.restartLoader(1);
            }
        }
    };
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private Friendships mFriendships;
    private boolean mFromSignup;
    private int mNewFollowsCount;
    private String[] mProjection;
    private volatile String mSearchQuery;
    private String mSecret;
    private TextView mSelectAllToggle;
    private boolean mSelected;
    private String mSortOrder;
    private String mToken;
    private Twitter mTwitter;
    private ArrayList<VineUser> mTwitterFriends;

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FindFriendsNUXActivity) {
            this.mActivity = (FindFriendsNUXActivity) activity;
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mBold = new StyleSpan[]{new StyleSpan(1)};
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("fetch")) {
                this.mFetched = savedInstanceState.getBoolean("fetch");
            }
            if (savedInstanceState.containsKey("friendships")) {
                this.mFriendships = (Friendships) savedInstanceState.getParcelable("friendships");
            }
            if (savedInstanceState.containsKey("from_sign_up")) {
                this.mFromSignup = savedInstanceState.getBoolean("from_sign_up");
            }
        } else {
            this.mFriendships = new Friendships();
            Bundle args = getArguments();
            if (args != null) {
                this.mFromSignup = args.getBoolean("from_sign_up");
            }
        }
        this.mAppController.removeUsers(this.mAppController.getActiveSession(), 6);
        this.mTwitter = new Twitter(TwitterVineApp.API_KEY, TwitterVineApp.API_SECRET);
        setupCallbackListeners();
        this.mSelected = true;
        this.mTwitterFriends = new ArrayList<>();
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.find_friends_twitter_list_fragment, container, false);
        this.mListView = (ListView) v.findViewById(android.R.id.list);
        this.mListView.setEmptyView(v.findViewById(android.R.id.empty));
        this.mSadface = v.findViewById(R.id.real_sadface);
        this.mListView.setDividerHeight(0);
        this.mListView.setOnItemClickListener(this);
        this.mContactsCountContainer = (RelativeLayout) v.findViewById(R.id.contacts_count_container);
        this.mContactsText = (TextView) v.findViewById(R.id.contacts_text);
        this.mSelectAllToggle = (TextView) v.findViewById(R.id.select_all_toggle);
        this.mSelectAllToggle.setOnClickListener(this);
        getTwitterTokenAndSecretFromAccountManager();
        this.mEmptyText = (TextView) v.findViewById(R.id.empty_text);
        if (this.mEmptyTextStringRes > 0 && this.mEmptyText != null) {
            this.mEmptyText.setText(this.mEmptyTextStringRes);
        }
        Button actionButton = (Button) v.findViewById(R.id.friends_btn);
        if (this.mToken != null && this.mSecret != null) {
            showSadface(false);
            actionButton.setVisibility(8);
            fetchTwitterFriends(this.mToken, this.mSecret);
        } else {
            showSadface(true);
            if (this.mEmptyText != null) {
                this.mEmptyText.setVisibility(8);
            }
            actionButton.setText(Util.getSpannedText(this.mBold, getString(R.string.find_friends_twitter_cta), '\"'));
            actionButton.setOnClickListener(this);
        }
        return v;
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!this.mFromSignup) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View title = inflater.inflate(R.layout.list_header, (ViewGroup) null);
            this.mListView.addHeaderView(title);
            ((TextView) title.findViewById(R.id.subtitle)).setText(R.string.find_friends_twitter_subtitle);
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (this.mCursorAdapter == null) {
            this.mProjection = VineDatabaseSQL.UsersQuery.PROJECTION;
            this.mSortOrder = "username ASC";
            this.mCursorAdapter = new UsersAdapter(activity, this.mAppController, true, this, this.mFriendships, 0);
        }
        this.mListView.setAdapter((ListAdapter) this.mCursorAdapter);
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(activity.getApplicationContext()), AppStateProviderSingleton.getInstance(activity), AppNavigationProviderSingleton.getInstance());
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // co.vine.android.BaseCursorListFragment, com.twitter.android.widget.RefreshListener
    public ItemPosition getFirstItemPosition() {
        return null;
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fetch", this.mFetched);
        outState.putParcelable("friendships", this.mFriendships);
        outState.putBoolean("from_sign_up", this.mFromSignup);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (this.mActivity != null && cursor.getCount() > 0 && this.mFromSignup) {
            this.mActivity.showSearchIcon(true);
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                Uri.Builder uri = ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_FRIENDS_FILTER, this.mAppController.getActiveId()).buildUpon();
                uri.appendQueryParameter("filter", Uri.encode(this.mSearchQuery));
                uri.appendQueryParameter("group_type", String.valueOf(6));
                return new CursorLoader(getActivity(), uri.build(), this.mProjection, null, null, this.mSortOrder);
            default:
                return new CursorLoader(getActivity(), ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_FIND_FRIENDS_TWITTER, this.mAppController.getActiveId()), this.mProjection, null, null, this.mSortOrder);
        }
    }

    private void fetchContent(int fetchType) {
        this.mFetched = true;
        switch (fetchType) {
            case 3:
                if (this.mToken == null || this.mSecret == null) {
                    AppController.startTwitterAuthWithFinish(this.mTwitter, getActivity());
                    break;
                } else {
                    fetchTwitterFriends(this.mToken, this.mSecret);
                    break;
                }
                break;
        }
    }

    private void getTwitterTokenAndSecretFromAccountManager() {
        Activity activity = getActivity();
        Session session = this.mAppController.getActiveSession();
        String uniqueLogin = session.getUsername();
        Account ac = VineAccountHelper.getAccount(activity, session.getUserId(), uniqueLogin);
        if (ac == null) {
            CrashUtil.logException(new VineLoggingException("Find Friends Twitter account was null"), "Find Friends Twitter account was null. UniqueLogin: {} ", uniqueLogin);
            Util.showCenteredToast(activity, R.string.find_friends_twitter_error);
        } else {
            AccountManager am = AccountManager.get(getActivity());
            this.mToken = am.getUserData(ac, "account_t_token");
            this.mSecret = am.getUserData(ac, "account_t_secret");
        }
    }

    private void fetchTwitterFriends(String token, String secret) {
        this.mAppController.fetchTwitterFriends(this.mAppController.getActiveSession(), token, secret);
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    this.mToken = data.getStringExtra("tk");
                    this.mSecret = data.getStringExtra("ts");
                    fetchTwitterFriends(this.mToken, this.mSecret);
                    break;
                } else if (resultCode != 0) {
                    Util.showCenteredToast(getActivity(), R.string.find_friends_twitter_sdk_error);
                    break;
                }
                break;
            case 2:
                if (resultCode == -1) {
                    this.mToken = data.getStringExtra("token");
                    this.mSecret = data.getStringExtra("secret");
                    fetchTwitterFriends(this.mToken, this.mSecret);
                    break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.friends_btn) {
            fetchContent(3);
            return;
        }
        if (id == R.id.user_row_btn_follow) {
            FollowButtonViewHolder holder = (FollowButtonViewHolder) view.getTag();
            if (holder != null) {
                if (!holder.following) {
                    if (this.mFromSignup) {
                        this.mActivity.addUserToFollow(holder.userId);
                    } else {
                        Components.userInteractionsComponent().followUser(this.mAppController, holder.userId, true, this.mFollowScribeActionsLogger);
                    }
                    this.mFriendships.addFollowing(holder.userId);
                    this.mCursorAdapter.notifyDataSetChanged();
                    this.mNewFollowsCount++;
                    return;
                }
                if (this.mFromSignup) {
                    this.mActivity.removeUserToFollow(holder.userId);
                } else {
                    Components.userInteractionsComponent().unfollowUser(this.mAppController, holder.userId, true, this.mFollowScribeActionsLogger);
                }
                this.mFriendships.removeFollowing(holder.userId);
                this.mCursorAdapter.notifyDataSetChanged();
                return;
            }
            return;
        }
        if (id == R.id.select_all_toggle) {
            this.mSelected = this.mSelected ? false : true;
            this.mActivity.toggleFollowAll(this.mSelected, this.mTwitterFriends, this.mFriendships);
            this.mSelectAllToggle.setText(this.mSelected ? R.string.deselect_all : R.string.select_all);
            this.mCursorAdapter.notifyDataSetChanged();
            if (this.mFollowScribeActionsLogger != null) {
                if (this.mSelected) {
                    this.mFollowScribeActionsLogger.onFollowAll(this.mTwitterFriends);
                } else {
                    this.mFollowScribeActionsLogger.onUnfollowAll(this.mTwitterFriends);
                }
            }
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
        super.onMoveAway(newPosition);
        if (this.mFetched) {
            String count = this.mNewFollowsCount > 15 ? ">15" : String.valueOf(this.mNewFollowsCount);
            FlurryUtils.trackTwitterNewFollowingCount(count);
        }
        this.mNewFollowsCount = 0;
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        super.onMoveTo(oldPosition);
        getActivity().invalidateOptionsMenu();
        if (this.mActivity != null) {
            this.mActivity.clearSearch();
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view.getTag() instanceof UserViewHolder) {
            UserViewHolder h = (UserViewHolder) view.getTag();
            startProfileActivity(h.userId);
        }
    }

    private void startProfileActivity(long userId) {
        ChannelActivity.startProfile(getActivity(), userId, "Find Friends: Twitter", false);
    }

    @Override // co.vine.android.FindFriendsNUXActivity.FilterableFriendsList
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.mHandler.removeCallbacks(this.mFilterRunnable);
        this.mSearchQuery = s.toString();
        if (!TextUtils.isEmpty(this.mSearchQuery)) {
            this.mHandler.postDelayed(this.mFilterRunnable, SuggestionsComponent.getTypeaheadThrottle());
        } else {
            restartLoader(0);
        }
    }

    @Override // co.vine.android.FindFriendsNUXActivity.FilterableFriendsList
    public boolean shouldShowSearchIcon() {
        return (this.mCursorAdapter == null || this.mCursorAdapter.isEmpty()) ? false : true;
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> loader) {
        this.mCursorAdapter.swapCursor(null);
    }

    private void setupCallbackListeners() {
        setAppSessionListener(new AppSessionListener() { // from class: co.vine.android.FindFriendsTwitterFragment.2
            @Override // co.vine.android.client.AppSessionListener
            public void onGetTwitterFriendsComplete(String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users) {
                if (reqId != null && statusCode == 200) {
                    FlurryUtils.trackFindFriendsTwitterCount(count);
                    if (FindFriendsTwitterFragment.this.mFromSignup && count > 0) {
                        FindFriendsTwitterFragment.this.showSadface(false);
                        FindFriendsTwitterFragment.this.mContactsCountContainer.setVisibility(0);
                        FindFriendsTwitterFragment.this.mContactsText.setText(FindFriendsTwitterFragment.this.getString(R.string.find_friends_twitter_count, Integer.valueOf(count)));
                        FindFriendsTwitterFragment.this.mSelectAllToggle.setText(FindFriendsTwitterFragment.this.mSelected ? R.string.deselect_all : R.string.select_all);
                        FindFriendsTwitterFragment.this.mActivity.addUsersToFollow(users, FindFriendsTwitterFragment.this.mFriendships);
                        FindFriendsTwitterFragment.this.mTwitterFriends.addAll(users);
                        return;
                    }
                    if (count == 0) {
                        FindFriendsTwitterFragment.this.showSadface(true);
                        return;
                    }
                    return;
                }
                FlurryUtils.trackFindFriendsTwitterFailure();
                Util.showCenteredToast(FindFriendsTwitterFragment.this.getActivity(), R.string.find_friends_twitter_error);
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onRemoveUsersComplete(String reqId) {
                if (FindFriendsTwitterFragment.this.mCursorAdapter.getCursor() == null) {
                    FindFriendsTwitterFragment.this.initLoader();
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                ((UsersAdapter) FindFriendsTwitterFragment.this.mCursorAdapter).setUserImages(images);
            }
        });
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.FindFriendsTwitterFragment.3
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsTwitterFragment.this.mFriendships.removeFollowing(userId);
                }
            }

            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsTwitterFragment.this.mFriendships.addFollowing(userId);
                }
            }
        });
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.FIND_FRIENDS;
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Subviews getAppNavigationSubview() {
        return AppNavigation.Subviews.TWITTER;
    }
}
