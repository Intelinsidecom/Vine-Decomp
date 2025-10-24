package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import co.vine.android.TwitterFollowableUsersMemoryAdapter;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class TwitterFollowerMigrationFragment extends UsersFragment implements View.OnClickListener, TwitterFollowableUsersMemoryAdapter.OnClickCallback {
    private Activity mActivity;
    private String mSecret;
    private Button mSelectAllView;
    private String mToken;
    private Set<Long> mFollowSet = new HashSet();
    private boolean mFollowAll = false;
    private boolean mDeselectAll = false;
    private final UserInteractionsListener mFollowListener = new UserInteractionsListener() { // from class: co.vine.android.TwitterFollowerMigrationFragment.1
        @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
        public void onBulkFollowTwitterComplete(String reqId, int statusCode, String reasonPhrase) {
            TwitterFollowerMigrationFragment.this.dismissProgressDialog();
            if (statusCode == 200) {
                CommonUtil.getDefaultSharedPrefs(TwitterFollowerMigrationFragment.this.mActivity).edit().putBoolean("followerMigrationDone", true).apply();
                Util.showDefaultToast(TwitterFollowerMigrationFragment.this.mActivity, R.string.login_success);
                TwitterFollowerMigrationFragment.this.mActivity.finish();
            } else {
                Util.showDefaultToast(TwitterFollowerMigrationFragment.this.mActivity, R.string.find_friends_twitter_error);
                TwitterFollowerMigrationFragment.this.mActivity.finish();
            }
        }
    };

    @Override // co.vine.android.UsersFragment, android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mActivity = getActivity();
        this.mAdapter = new TwitterFollowableUsersMemoryAdapter(this.mActivity, this.mAppController, this, this.mFriendships);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        fetchTokenAndSecret();
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createView(inflater, R.layout.twitter_follower_migration_fragment, container);
        this.mSelectAllView = (Button) v.findViewById(R.id.twitter_select_all);
        this.mSelectAllView.setOnClickListener(this);
        this.mListView.setEmptyView(v.findViewById(android.R.id.empty));
        v.findViewById(R.id.done).setOnClickListener(this);
        return v;
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    this.mToken = data.getStringExtra("tk");
                    this.mSecret = data.getStringExtra("ts");
                    showProgressDialog(R.string.sign_up_authorizing);
                    Components.userInteractionsComponent().bulkFollowTwitter(this.mAppController, arrayFromSet(this.mFollowSet), this.mFollowAll);
                    break;
                } else if (resultCode != 0) {
                    Util.showDefaultToast(this.mActivity, R.string.error_twitter_sdk);
                    this.mActivity.finish();
                    break;
                }
                break;
            case 2:
                if (resultCode == -1) {
                    this.mToken = data.getStringExtra("token");
                    this.mSecret = data.getStringExtra("secret");
                    showProgressDialog(R.string.sign_up_authorizing);
                    Components.userInteractionsComponent().bulkFollowTwitter(this.mAppController, arrayFromSet(this.mFollowSet), this.mFollowAll);
                    break;
                } else if (resultCode != 0) {
                    Util.showDefaultToast(this.mActivity, R.string.error_twitter_sdk);
                    this.mActivity.finish();
                    break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override // co.vine.android.UsersFragment, co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Components.userInteractionsComponent().addListener(this.mFollowListener);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        Components.userInteractionsComponent().removeListener(this.mFollowListener);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.twitter_select_all) {
            if (this.mFollowAll) {
                deselectAll();
                return;
            } else {
                selectAll();
                return;
            }
        }
        if (id == R.id.done) {
            if (this.mToken == null || this.mSecret == null) {
                AppController.startTwitterAuthWithFinish(this.mAppController.getTwitter(), this.mActivity);
            } else {
                Components.userInteractionsComponent().bulkFollowTwitter(this.mAppController, arrayFromSet(this.mFollowSet), this.mFollowAll);
            }
        }
    }

    private void fetchTokenAndSecret() {
        Session session = this.mAppController.getActiveSession();
        String uniqueLogin = session.getUsername();
        Account ac = VineAccountHelper.getAccount(this.mActivity, session.getUserId(), uniqueLogin);
        if (ac == null) {
            CrashUtil.logException(new VineLoggingException("Find Friends Twitter account was null"), "Find Friends Twitter account was null. UniqueLogin: {} ", uniqueLogin);
            Util.showDefaultToast(this.mActivity, R.string.find_friends_twitter_error);
        } else {
            AccountManager am = AccountManager.get(getActivity());
            this.mToken = am.getUserData(ac, "account_t_token");
            this.mSecret = am.getUserData(ac, "account_t_secret");
        }
    }

    private long[] arrayFromSet(Set<Long> userIds) {
        long[] result = new long[userIds.size()];
        int index = 0;
        Iterator<Long> it = userIds.iterator();
        while (it.hasNext()) {
            long userId = it.next().longValue();
            result[index] = userId;
            index++;
        }
        return result;
    }

    @Override // co.vine.android.TwitterFollowableUsersMemoryAdapter.OnClickCallback
    public void onUserSelected(long userId, boolean follow) {
        if (follow) {
            this.mFriendships.addFollowing(userId);
            this.mFollowSet.add(Long.valueOf(userId));
        } else {
            this.mFriendships.removeFollowing(userId);
            this.mFollowSet.remove(Long.valueOf(userId));
        }
        updateSelectAll();
    }

    @Override // co.vine.android.TwitterFollowableUsersMemoryAdapter.OnClickCallback
    public void onUserAdded(long userId) {
        this.mFriendships.addFollowing(userId);
        this.mFollowSet.add(Long.valueOf(userId));
        if (this.mDeselectAll) {
            this.mFriendships.removeFollowing(userId);
            this.mFollowSet.remove(Long.valueOf(userId));
        }
        updateSelectAll();
    }

    private void updateSelectAll() {
        if (!this.mFollowAll && this.mFollowSet.size() == this.mFriendships.size()) {
            this.mFollowAll = true;
            this.mSelectAllView.setText(getString(R.string.deselect_all));
        } else if (this.mFollowAll && this.mFollowSet.size() != this.mFriendships.size()) {
            this.mFollowAll = false;
            this.mSelectAllView.setText(getString(R.string.select_all));
        }
    }

    private void deselectAll() {
        this.mFollowAll = false;
        this.mDeselectAll = true;
        Iterator<Long> it = this.mFollowSet.iterator();
        while (it.hasNext()) {
            long userId = it.next().longValue();
            this.mFriendships.removeFollowing(userId);
        }
        this.mFollowSet.clear();
        this.mSelectAllView.setText(getString(R.string.select_all));
        this.mAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        this.mFollowAll = true;
        this.mDeselectAll = false;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            VineUser user = (VineUser) this.mAdapter.getItem(i);
            this.mFriendships.addFollowing(user.userId);
            this.mFollowSet.add(Long.valueOf(user.userId));
        }
        this.mSelectAllView.setText(getString(R.string.deselect_all));
        this.mAdapter.notifyDataSetChanged();
    }
}
