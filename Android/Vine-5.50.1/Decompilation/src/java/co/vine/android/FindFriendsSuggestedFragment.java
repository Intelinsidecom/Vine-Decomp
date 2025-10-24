package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.scribe.AppNavigationProviderSingleton;
import co.vine.android.scribe.FollowScribeActionsLogger;
import co.vine.android.scribe.FollowScribeActionsLoggerSingleton;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.suggestions.SuggestionsActionListener;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.BuildUtil;
import com.twitter.android.widget.ItemPosition;
import com.twitter.android.widget.RefreshListener;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class FindFriendsSuggestedFragment extends BaseArrayListFragment {
    private Context mContext;
    private boolean mFirstFetch;
    private TextView mFollowAllToggle;
    private FollowScribeActionsLogger mFollowScribeActionsLogger;
    private boolean mFollowingAll;
    private Friendships mFriendships;
    private int mLayoutRes;
    private int mNewFollowings;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.user_row_btn_follow) {
                FindFriendsSuggestedFragment.this.handleFollowSuggestionClick(view);
            } else if (id == R.id.follow_all_toggle) {
                FindFriendsSuggestedFragment.this.toggleFollowAll();
            }
        }
    };
    private final RefreshListener mRefreshListener = new RefreshListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.2
        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshPulled() {
        }

        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshCancelled() {
        }

        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshReleased(boolean shouldStartRefresh) {
            if (shouldStartRefresh) {
                FindFriendsSuggestedFragment.this.refresh();
            }
        }

        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshFinished() {
        }

        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshFinishedNewData() {
        }

        @Override // com.twitter.android.widget.RefreshListener
        public void onRefreshFinishedNoChange() {
        }

        @Override // com.twitter.android.widget.RefreshListener
        public int getPositionForItemId(long id) {
            return 0;
        }

        @Override // com.twitter.android.widget.RefreshListener
        public ItemPosition getFirstItemPosition() {
            int position = FindFriendsSuggestedFragment.this.mListView.getFirstVisiblePosition();
            View child = FindFriendsSuggestedFragment.this.mListView.getChildAt(0);
            return new ItemPosition(position, FindFriendsSuggestedFragment.this.mListView.getItemIdAtPosition(position), child != null ? child.getTop() : 0);
        }
    };
    private ArrayList<VineUser> mSuggestions;
    private FollowableUsersMemoryAdapter mSuggestionsAdapter;
    private TextView mSuggestionsLabel;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("first_fetch")) {
                this.mFirstFetch = savedInstanceState.getBoolean("first_fetch");
            }
            if (savedInstanceState.containsKey("friendships")) {
                this.mFriendships = (Friendships) savedInstanceState.getParcelable("friendships");
            }
            if (savedInstanceState.containsKey("layout_res")) {
                this.mLayoutRes = savedInstanceState.getInt("layout_res");
            }
        } else {
            this.mFriendships = new Friendships();
            Bundle args = getArguments();
            this.mLayoutRes = args.getInt("layout", 0);
        }
        setupCallbackListeners();
        this.mRefreshable = true;
        this.mFirstFetch = true;
        this.mFollowingAll = false;
        this.mNewFollowings = 0;
        this.mContext = getActivity();
        this.mSuggestions = new ArrayList<>();
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(this.mLayoutRes, container, false);
        this.mListView = (ListView) v.findViewById(android.R.id.list);
        this.mListView.setDividerHeight(0);
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelActivity.startProfile(FindFriendsSuggestedFragment.this.mContext, id, "User Recs");
            }
        });
        ((RefreshableListView) this.mListView).setRefreshListener(this.mRefreshListener);
        View emptyView = v.findViewById(android.R.id.empty);
        if (emptyView != null) {
            this.mListView.setEmptyView(emptyView);
            this.mEmptyProgress = emptyView.findViewById(R.id.list_empty_progress);
        }
        this.mSuggestionsLabel = (TextView) v.findViewById(R.id.suggestions_label);
        this.mSuggestionsLabel.setTypeface(null, 1);
        this.mFollowAllToggle = (TextView) v.findViewById(R.id.follow_all_toggle);
        this.mFollowAllToggle.setVisibility(0);
        this.mFollowAllToggle.setOnClickListener(this.mOnClickListener);
        this.mSuggestionsAdapter = new FollowableUsersMemoryAdapter(this.mContext, AppController.getInstance(this.mContext), true, true, this.mOnClickListener, this.mFriendships);
        this.mListView.setAdapter((ListAdapter) this.mSuggestionsAdapter);
        fetchContent(3);
        return v;
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRefreshableHeaderOffset(6);
        showProgress(3);
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        this.mFollowScribeActionsLogger = FollowScribeActionsLoggerSingleton.getInstance(ScribeLoggerSingleton.getInstance(activity.getApplicationContext()), AppStateProviderSingleton.getInstance(activity), AppNavigationProviderSingleton.getInstance());
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("first_fetch", this.mFirstFetch);
        outState.putParcelable("friendships", this.mFriendships);
        outState.putInt("layout_res", this.mLayoutRes);
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void refresh() {
        fetchContent(4);
    }

    private void fetchContent(int fetchType) {
        switch (fetchType) {
            case 3:
            case 4:
                showProgress(fetchType);
                if (BuildUtil.isProduction()) {
                    Components.suggestionsComponent().fetchSuggestedUsers(this.mAppController);
                    break;
                } else {
                    this.mAppController.fetchFollowing(1264298347905302528L, 1, null);
                    break;
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFollowSuggestionClick(View view) {
        FollowButtonViewHolder holder = (FollowButtonViewHolder) view.getTag();
        if (holder != null) {
            if (!holder.following) {
                Components.userInteractionsComponent().followUser(this.mAppController, holder.userId, false, this.mFollowScribeActionsLogger);
                this.mFriendships.addFollowing(holder.userId);
                this.mSuggestionsAdapter.notifyDataSetChanged();
                int i = this.mNewFollowings + 1;
                this.mNewFollowings = i;
                if (i == this.mSuggestions.size()) {
                    setFollowingAll(true);
                    return;
                }
                return;
            }
            Components.userInteractionsComponent().unfollowUser(this.mAppController, holder.userId, false, this.mFollowScribeActionsLogger);
            this.mFriendships.removeFollowing(holder.userId);
            this.mSuggestionsAdapter.notifyDataSetChanged();
            this.mNewFollowings--;
            setFollowingAll(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toggleFollowAll() {
        if (!this.mFollowingAll) {
            Iterator<VineUser> it = this.mSuggestions.iterator();
            while (it.hasNext()) {
                VineUser user = it.next();
                Components.userInteractionsComponent().followUser(this.mAppController, user.userId, true, null);
                this.mFriendships.addFollowing(user.userId);
            }
            this.mNewFollowings = this.mSuggestions.size();
            if (this.mFollowScribeActionsLogger != null) {
                this.mFollowScribeActionsLogger.onFollowAll(this.mSuggestions);
            }
        } else {
            Iterator<VineUser> it2 = this.mSuggestions.iterator();
            while (it2.hasNext()) {
                VineUser user2 = it2.next();
                Components.userInteractionsComponent().unfollowUser(this.mAppController, user2.userId, true, null);
                this.mFriendships.removeFollowing(user2.userId);
            }
            this.mNewFollowings = 0;
            if (this.mFollowScribeActionsLogger != null) {
                this.mFollowScribeActionsLogger.onUnfollowAll(this.mSuggestions);
            }
        }
        setFollowingAll(this.mFollowingAll ? false : true);
        this.mSuggestionsAdapter.notifyDataSetChanged();
    }

    private void setFollowingAll(boolean followingAll) {
        this.mFollowingAll = followingAll;
        Context activity = getActivity();
        if (activity != null && this.mFollowAllToggle != null) {
            this.mFollowAllToggle.setText(activity.getString(this.mFollowingAll ? R.string.undo_follow_all : R.string.follow_all));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUsers(ArrayList<VineUser> users) {
        this.mSuggestions.clear();
        this.mSuggestions.addAll(users);
        this.mSuggestionsAdapter.mergeData(this.mSuggestions, false);
        if (this.mFirstFetch) {
            hideProgress(3);
            this.mFirstFetch = false;
        } else {
            hideProgress(4);
        }
        this.mSuggestionsAdapter.notifyDataSetChanged();
        this.mNewFollowings = 0;
        setFollowingAll(false);
    }

    private void setupCallbackListeners() {
        setAppSessionListener(new AppSessionListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.4
            @Override // co.vine.android.client.AppSessionListener
            public void onGetUsersComplete(Session session, String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users, int nextPage, int previousPage, String anchor) {
                if (statusCode == 200 && FindFriendsSuggestedFragment.this.getActivity() != null && users != null) {
                    FindFriendsSuggestedFragment.this.updateUsers(users);
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                if (FindFriendsSuggestedFragment.this.mSuggestionsAdapter != null) {
                    FindFriendsSuggestedFragment.this.mSuggestionsAdapter.setUserImages(images);
                }
            }
        });
        Components.suggestionsComponent().addListener(new SuggestionsActionListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.5
            @Override // co.vine.android.service.components.suggestions.SuggestionsActionListener
            public void onFetchSuggestedUsersComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users) {
                if (statusCode == 200 && FindFriendsSuggestedFragment.this.getActivity() != null) {
                    FindFriendsSuggestedFragment.this.updateUsers(users);
                }
            }
        });
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.FindFriendsSuggestedFragment.6
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsSuggestedFragment.this.mFriendships.removeFollowing(userId);
                }
            }

            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsSuggestedFragment.this.mFriendships.addFollowing(userId);
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
        return AppNavigation.Subviews.SUGGESTIONS;
    }
}
