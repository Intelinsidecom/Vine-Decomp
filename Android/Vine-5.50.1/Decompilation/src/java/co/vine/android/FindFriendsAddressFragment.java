package co.vine.android;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.client.Session;
import co.vine.android.client.VineAccountHelper;
import co.vine.android.scribe.model.AppNavigation;
import co.vine.android.service.components.Components;
import co.vine.android.service.components.userinteraction.UserInteractionsListener;
import co.vine.android.util.Util;
import co.vine.android.util.analytics.FlurryUtils;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.twitter.android.widget.ItemPosition;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class FindFriendsAddressFragment extends FindFriendsBaseFragment {
    @Override // co.vine.android.FindFriendsBaseFragment, co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCallbackListeners();
    }

    @Override // co.vine.android.BaseCursorListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View title = inflater.inflate(R.layout.list_header, (ViewGroup) null);
        this.mListView.addHeaderView(title);
        ((RefreshableListView) this.mListView).disablePTR();
        ((TextView) title.findViewById(R.id.subtitle)).setText(R.string.find_friends_address_subtitle);
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (!this.mFetched) {
            AccountManager am = AccountManager.get(getActivity());
            Session session = this.mAppController.getActiveSession();
            Account account = VineAccountHelper.getAccount(getActivity(), session.getUserId(), session.getUsername());
            if (VineAccountHelper.shouldShowStoreContactsPrompt(am, account)) {
                PromptDialogSupportFragment p = PromptDialogSupportFragment.newInstance(1);
                p.setListener(this.mStoreContactDialogDoneListener);
                p.setMessage(R.string.store_contacts_desc);
                p.setTitle(R.string.store_contacts);
                p.setNegativeButton(R.string.no);
                p.setPositiveButton(R.string.ok);
                p.setCancelable(false);
                p.show(getFragmentManager());
                return;
            }
            fetchContent(3);
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, com.twitter.android.widget.RefreshListener
    public ItemPosition getFirstItemPosition() {
        return null;
    }

    @Override // co.vine.android.FindFriendsBaseFragment, android.view.View.OnClickListener
    public void onClick(View view) {
        FollowButtonViewHolder holder;
        if (view.getId() == R.id.user_row_btn_follow && (holder = (FollowButtonViewHolder) view.getTag()) != null) {
            if (!holder.following) {
                Components.userInteractionsComponent().followUser(this.mAppController, holder.userId, true, this.mFollowScribeActionsLogger);
                this.mFriendships.addFollowing(holder.userId);
                this.mCursorAdapter.notifyDataSetChanged();
                this.mNewFollowsCount++;
                return;
            }
            Components.userInteractionsComponent().followUser(this.mAppController, holder.userId, true, this.mFollowScribeActionsLogger);
            this.mFriendships.removeFollowing(holder.userId);
            this.mCursorAdapter.notifyDataSetChanged();
        }
    }

    @Override // co.vine.android.BaseCursorListFragment, co.vine.android.widget.OnTabChangedListener
    public void onMoveAway(int newPosition) {
        super.onMoveAway(newPosition);
        if (this.mFetched) {
            String count = this.mNewFollowsCount > 15 ? ">15" : String.valueOf(this.mNewFollowsCount);
            FlurryUtils.trackAddressNewFollowingCount(count);
        }
        this.mNewFollowsCount = 0;
    }

    @Override // co.vine.android.FindFriendsBaseFragment
    protected void startProfileActivity(long userId) {
        ChannelActivity.startProfile(getActivity(), userId, "Find Friends: Address", true);
    }

    @Override // co.vine.android.FindFriendsNUXActivity.FilterableFriendsList
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void setupCallbackListeners() {
        setAppSessionListener(new AppSessionListener() { // from class: co.vine.android.FindFriendsAddressFragment.1
            @Override // co.vine.android.client.AppSessionListener
            public void onGetAddressFriendsComplete(String reqId, int statusCode, String reasonPhrase, int count, ArrayList<VineUser> users) {
                FindFriendsAddressFragment.this.hideProgress(3);
                if (reqId != null && statusCode == 200) {
                    if (FindFriendsAddressFragment.this.mCursorAdapter.isEmpty()) {
                        FindFriendsAddressFragment.this.showSadface(true);
                    }
                    FlurryUtils.trackFindFriendsAddressCount(count);
                } else {
                    FindFriendsAddressFragment.this.showSadface(true);
                    Util.showCenteredToast(FindFriendsAddressFragment.this.getActivity(), R.string.find_friends_address_error);
                    FlurryUtils.trackFindFriendsAddressFailure();
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onRemoveUsersComplete(String reqId) {
                if (FindFriendsAddressFragment.this.mCursorAdapter.getCursor() == null) {
                    FindFriendsAddressFragment.this.initLoader();
                }
            }

            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                ((UsersAdapter) FindFriendsAddressFragment.this.mCursorAdapter).setUserImages(images);
            }
        });
        Components.userInteractionsComponent().addListener(new UserInteractionsListener() { // from class: co.vine.android.FindFriendsAddressFragment.2
            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsAddressFragment.this.mFriendships.removeFollowing(userId);
                }
            }

            @Override // co.vine.android.service.components.userinteraction.UserInteractionsListener
            public void onUnFollowComplete(String reqId, int statusCode, String reasonPhrase, long userId) {
                if (reqId != null && statusCode != 200) {
                    FindFriendsAddressFragment.this.mFriendships.addFollowing(userId);
                }
            }
        });
    }

    @Override // co.vine.android.FindFriendsBaseFragment, co.vine.android.BaseFragment
    public AppNavigation.Subviews getAppNavigationSubview() {
        return AppNavigation.Subviews.ADDRESS_BOOK;
    }
}
