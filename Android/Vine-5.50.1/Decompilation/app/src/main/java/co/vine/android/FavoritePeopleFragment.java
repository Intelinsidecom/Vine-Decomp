package co.vine.android;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.ArrayListScrollListener;
import co.vine.android.widget.UserViewHolder;
import co.vine.android.widget.VineToggleButton;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class FavoritePeopleFragment extends BaseArrayListFragment implements CompoundButton.OnCheckedChangeListener {
    private HashSet<Long> mFavoritedUserIds = new HashSet<>();
    private boolean mFetched;
    private String mStringAnchor;

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAdapter = new FavoritePeopleAdapter(getActivity(), this.mAppController);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setDividerHeight(0);
        setAppSessionListener(new FavoritePeopleSessionListener());
        this.mListView.setOnScrollListener(new ArrayListScrollListener() { // from class: co.vine.android.FavoritePeopleFragment.1
            @Override // co.vine.android.feedadapter.ArrayListScrollListener
            public void onScrollLastItem(int totalItemCount) {
                super.onScrollLastItem(totalItemCount);
                if (FavoritePeopleFragment.this.mRefreshable) {
                    if ((FavoritePeopleFragment.this.mNextPage > 0 || "1:0".equals(FavoritePeopleFragment.this.mStringAnchor)) && FavoritePeopleFragment.this.mAdapter.getCount() <= 400) {
                        FavoritePeopleFragment.this.fetchContent(1);
                    }
                }
            }
        });
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mAdapter.isEmpty() && !this.mFetched) {
            fetchContent(3);
            this.mFetched = true;
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void refresh() {
        fetchContent(2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchContent(int fetchType) {
        if (!this.mPendingRequestHelper.hasPendingRequest(fetchType)) {
            switch (fetchType) {
                case 2:
                case 3:
                case 4:
                    this.mNextPage = 1;
                    this.mStringAnchor = null;
                    break;
            }
            String reqId = this.mAppController.fetchFavoriteUsers(this.mNextPage, this.mStringAnchor);
            if (reqId != null) {
                this.mPendingRequestHelper.showProgress(fetchType);
                this.mPendingRequestHelper.addRequest(reqId, fetchType);
            }
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        VineUser user = (VineUser) buttonView.getTag();
        if (user != null) {
            long userId = user.userId;
            this.mAppController.favoriteUser(userId, isChecked, user.isFollowing());
            if (isChecked) {
                this.mFavoritedUserIds.add(Long.valueOf(userId));
            } else {
                this.mFavoritedUserIds.remove(Long.valueOf(userId));
            }
        }
    }

    private class FavoritePeopleSessionListener extends AppSessionListener {
        private FavoritePeopleSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onFetchFavoritePeopleComplete(String reqId, int statusCode, String reasonPhrase, ArrayList<VineUser> users, int nextPage, int prevPage, String anchor) {
            switch (statusCode) {
                case HttpResponseCode.OK /* 200 */:
                    PendingRequest req = FavoritePeopleFragment.this.mPendingRequestHelper.removeRequest(reqId);
                    if (req != null) {
                        FavoritePeopleFragment.this.mPendingRequestHelper.hideProgress(req.fetchType);
                        if (FavoritePeopleFragment.this.mAdapter != null) {
                            ((UsersMemoryAdapter) FavoritePeopleFragment.this.mAdapter).mergeData(users, req.fetchType == 1);
                        }
                        FavoritePeopleFragment.this.mNextPage = nextPage;
                        FavoritePeopleFragment.this.mStringAnchor = anchor;
                        break;
                    }
                    break;
                default:
                    FavoritePeopleFragment.this.mPendingRequestHelper.hideProgress(3);
                    break;
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            if (FavoritePeopleFragment.this.mAdapter != null) {
                ((UsersMemoryAdapter) FavoritePeopleFragment.this.mAdapter).setUserImages(images);
            }
        }
    }

    @Override // co.vine.android.BaseArrayListFragment
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (id > 0) {
            ChannelActivity.startProfile(getActivity(), id, "");
        }
    }

    private class FavoritePeopleAdapter extends UsersMemoryAdapter<FavoriteUserViewHolder> {
        public FavoritePeopleAdapter(Context context, AppController appController) {
            super(context, appController);
        }

        @Override // co.vine.android.UsersMemoryAdapter
        public View newView(int position, ViewGroup parent) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(this.mContext);
            switch (getItemViewType(position)) {
                case 1:
                    view = inflater.inflate(R.layout.user_row_favorites_sectioned_view, parent, false);
                    break;
                default:
                    view = inflater.inflate(R.layout.user_row_favorites_view, parent, false);
                    break;
            }
            FavoriteUserViewHolder holder = FavoritePeopleFragment.this.new FavoriteUserViewHolder(view);
            this.mViewHolders.add(new WeakReference(holder));
            view.setTag(holder);
            return view;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // co.vine.android.UsersMemoryAdapter
        public void bindExtraViews(View view, int position, FavoriteUserViewHolder holder, VineUser user) {
            if (getItemViewType(position) == 1) {
                holder.sectionTitle.setText(user.sectionTitle);
            }
            if (user.sectionId == 0 || FavoritePeopleFragment.this.mFavoritedUserIds.contains(Long.valueOf(user.userId))) {
                holder.favorite.setCheckedWithoutEvent(true);
            } else {
                holder.favorite.setCheckedWithoutEvent(false);
            }
            holder.favorite.setTag(user);
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 2;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int position) {
            VineUser user = this.mUsers.get(position);
            return (user == null || !shouldShowSectionTitle(user, position)) ? 0 : 1;
        }

        private boolean shouldShowSectionTitle(VineUser thisUser, int position) {
            if (position > 0 && this.mUsers.size() > 0) {
                VineUser previousUser = this.mUsers.get(position - 1);
                if (previousUser.sectionId != thisUser.sectionId && !TextUtils.isEmpty(thisUser.sectionTitle)) {
                    return true;
                }
            }
            return false;
        }
    }

    private class FavoriteUserViewHolder extends UserViewHolder {
        public VineToggleButton favorite;
        public TextView sectionTitle;

        public FavoriteUserViewHolder(View view) {
            super(view);
            this.sectionTitle = (TextView) view.findViewById(R.id.section_title);
            this.favorite = (VineToggleButton) view.findViewById(R.id.favorite_button);
            this.favorite.setOnCheckedChangeListener(FavoritePeopleFragment.this);
        }
    }
}
