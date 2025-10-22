package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import co.vine.android.FindFriendsNUXActivity;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.provider.Vine;
import co.vine.android.provider.VineDatabaseSQL;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.util.Util;
import co.vine.android.widget.UserViewHolder;
import co.vine.android.widgets.PromptDialogSupportFragment;

/* loaded from: classes.dex */
public abstract class FindFriendsBaseFragment extends BaseCursorListFragment implements View.OnClickListener, AdapterView.OnItemClickListener, FindFriendsNUXActivity.FilterableFriendsList {
    protected FindFriendsBaseActivity mActivity;
    protected StyleSpan[] mBold;
    protected boolean mFetched;
    protected FollowScribeActionsLogger mFollowScribeActionsLogger;
    protected Friendships mFriendships;
    protected int mNewFollowsCount;
    protected String[] mProjection;
    protected volatile String mSearchQuery;
    protected String mSortOrder;
    protected final PromptDialogSupportFragment.OnDialogDoneListener mStoreContactDialogDoneListener = new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.FindFriendsBaseFragment.1
        @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
        public void onDialogDone(DialogInterface dialog, int id, int which) {
            AccountManager am = AccountManager.get(FindFriendsBaseFragment.this.getActivity());
            Session session = FindFriendsBaseFragment.this.mAppController.getActiveSession();
            Account account = VineAccountHelper.getAccount(FindFriendsBaseFragment.this.getActivity(), session.getUserId(), session.getUsername());
            VineAccountHelper.setDidShowStoreContactsPrompt(am, account);
            boolean enable = which == -1;
            FindFriendsBaseFragment.this.mAppController.updateEnableAddressBook(enable);
            SharedPreferences.Editor editor = Util.getDefaultSharedPrefs(FindFriendsBaseFragment.this.getActivity()).edit();
            editor.putBoolean("enable_address_book", enable).apply();
            FindFriendsBaseFragment.this.fetchContent(3);
        }
    };

    public abstract void onClick(View view);

    protected abstract void startProfileActivity(long j);

    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (FindFriendsBaseActivity) activity;
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
        } else {
            this.mFriendships = new Friendships();
        }
        this.mAppController.removeUsers(this.mAppController.getActiveSession(), 7);
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.find_friends_address_list_fragment, container);
        this.mListView.setDividerHeight(0);
        return view;
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

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fetch", this.mFetched);
        outState.putParcelable("friendships", this.mFriendships);
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                Uri.Builder uri = ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_FRIENDS_FILTER, this.mAppController.getActiveId()).buildUpon();
                uri.appendQueryParameter("filter", Uri.encode(this.mSearchQuery));
                uri.appendQueryParameter("group_type", String.valueOf(7));
                return new CursorLoader(getActivity(), uri.build(), this.mProjection, null, null, this.mSortOrder);
            default:
                return new CursorLoader(getActivity(), ContentUris.withAppendedId(Vine.UserGroupsView.CONTENT_URI_FIND_FRIENDS_ADDRESS, this.mAppController.getActiveId()), this.mProjection, null, null, this.mSortOrder);
        }
    }

    protected void fetchContent(int fetchType) {
        this.mFetched = true;
        showProgress(fetchType);
        switch (fetchType) {
            case 3:
                this.mAppController.fetchAddressFriends(this.mAppController.getActiveSession());
                break;
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (view.getTag() instanceof UserViewHolder) {
            UserViewHolder h = (UserViewHolder) view.getTag();
            startProfileActivity(h.userId);
        }
    }

    @Override // co.vine.android.FindFriendsNUXActivity.FilterableFriendsList
    public boolean shouldShowSearchIcon() {
        return (this.mCursorAdapter == null || this.mCursorAdapter.isEmpty()) ? false : true;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        super.onLoadFinished(loader, cursor);
        if (cursor != null && cursor.getCount() > 0 && (this.mActivity instanceof FindFriendsNUXActivity)) {
            ((FindFriendsNUXActivity) this.mActivity).showSearchIcon(true);
        }
    }

    @Override // co.vine.android.BaseCursorAdapterFragment, android.support.v4.app.LoaderManager.LoaderCallbacks
    public void onLoaderReset(Loader<Cursor> loader) {
        this.mCursorAdapter.swapCursor(null);
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveTo(int oldPosition) {
        super.onMoveTo(oldPosition);
        getActivity().invalidateOptionsMenu();
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Views getAppNavigationView() {
        return AppNavigation.Views.FIND_FRIENDS;
    }

    @Override // co.vine.android.BaseFragment
    public AppNavigation.Subviews getAppNavigationSubview() {
        return AppNavigation.Subviews.ADDRESS_BOOK;
    }
}
