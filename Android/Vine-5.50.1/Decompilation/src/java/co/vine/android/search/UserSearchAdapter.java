package co.vine.android.search;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.vine.android.ChannelActivity;
import co.vine.android.FollowButtonClickListener;
import co.vine.android.FollowableUserViewHolder;
import co.vine.android.FollowableUsersMemoryAdapter;
import co.vine.android.Friendships;
import co.vine.android.R;
import co.vine.android.api.SearchResult;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.util.Util;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class UserSearchAdapter extends FollowableUsersMemoryAdapter {
    private int mDisplayCount;
    private final LayoutInflater mInflater;
    private final boolean mShowSectionHeader;
    private final View.OnClickListener mViewAllClickListener;

    public UserSearchAdapter(Context context, AppController appController, View.OnClickListener viewAllClickListener, FollowButtonClickListener followButtonClickListener, Friendships friendships, boolean showSectionHeader) {
        super(context, appController, true, false, followButtonClickListener, friendships);
        this.mDisplayCount = 0;
        this.mViewAllClickListener = viewAllClickListener;
        this.mShowSectionHeader = showSectionHeader;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    @Override // co.vine.android.FollowableUsersMemoryAdapter, co.vine.android.UsersMemoryAdapter
    public View newView(int position, ViewGroup parent) {
        View view = this.mInflater.inflate(R.layout.user_row_header_view, parent, false);
        UserSearchViewHolder holder = new UserSearchViewHolder(view, this.mViewAllClickListener);
        this.mViewHolders.add(new WeakReference(holder));
        view.setTag(holder);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.FollowableUsersMemoryAdapter, co.vine.android.UsersMemoryAdapter
    public void bindExtraViews(View view, int position, FollowableUserViewHolder h, VineUser user) throws Resources.NotFoundException {
        super.bindExtraViews(view, position, h, user);
        UserSearchViewHolder holder = (UserSearchViewHolder) h;
        holder.userId = user.userId;
        if (position == 0 && this.mShowSectionHeader) {
            holder.header.setVisibility(0);
            holder.header.setText(this.mContext.getString(R.string.people));
        } else {
            holder.header.setVisibility(8);
        }
        if (position == 0 && this.mUsers.size() > this.mDisplayCount && this.mShowSectionHeader) {
            holder.viewAll.setVisibility(0);
        } else {
            holder.viewAll.setVisibility(8);
        }
        if (user.loopCount > 0) {
            String loopCountFormatted = Util.numberFormat(view.getResources(), user.loopCount);
            String loopCount = view.getResources().getQuantityString(R.plurals.profile_loops, (int) user.loopCount, loopCountFormatted);
            holder.loopCount.setText(loopCount);
            holder.loopCount.setVisibility(0);
            return;
        }
        holder.loopCount.setVisibility(4);
    }

    @Override // co.vine.android.UsersMemoryAdapter, android.widget.Adapter
    public int getCount() {
        int dataCount = this.mUsers != null ? 0 + this.mUsers.size() : 0;
        if (this.mDisplayCount > 0) {
            return Math.min(dataCount, this.mDisplayCount);
        }
        return dataCount;
    }

    private static class UserSearchViewHolder extends FollowableUserViewHolder {
        TextView header;
        TextView loopCount;
        long userId;
        TextView viewAll;

        public UserSearchViewHolder(View view, View.OnClickListener viewAllClickListener) {
            super(view);
            this.header = (TextView) view.findViewById(R.id.header_label);
            this.viewAll = (TextView) view.findViewById(R.id.view_all);
            this.loopCount = (TextView) view.findViewById(R.id.loops_count);
            this.loopCount.setVisibility(0);
            this.root.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.search.UserSearchAdapter.UserSearchViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (UserSearchViewHolder.this.userId > 0) {
                        ChannelActivity.startProfile(UserSearchViewHolder.this.root.getContext(), UserSearchViewHolder.this.userId, "Search");
                    }
                }
            });
            this.header.setOnClickListener(viewAllClickListener);
        }
    }

    public void replaceData(SearchResult data) {
        this.mUsers = new ArrayList<>();
        if (data.getUsers() != null) {
            this.mUsers.addAll(data.getUsers().getItems());
            this.mDisplayCount = data.getUsers().getDisplayCount();
        }
        notifyDataSetChanged();
    }

    public void setDisplayCount(int displayCount) {
        this.mDisplayCount = displayCount;
    }
}
