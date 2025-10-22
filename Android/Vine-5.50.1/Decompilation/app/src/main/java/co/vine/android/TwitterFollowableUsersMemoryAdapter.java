package co.vine.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.widget.UserViewHolder;
import co.vine.android.widget.VineToggleButton;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class TwitterFollowableUsersMemoryAdapter extends UsersMemoryAdapter<TwitterFollowableUserViewHolder> {
    private OnClickCallback mCallback;
    private Friendships mFriendships;
    private Set<VineUser> mProcessed;

    public interface OnClickCallback {
        void onUserAdded(long j);

        void onUserSelected(long j, boolean z);
    }

    public TwitterFollowableUsersMemoryAdapter(Context context, AppController appController, OnClickCallback callback, Friendships friendships) {
        super(context, appController);
        this.mProcessed = new HashSet();
        this.mCallback = callback;
        this.mFriendships = friendships;
    }

    @Override // co.vine.android.UsersMemoryAdapter
    protected View newView(int position, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View v = layoutInflater.inflate(R.layout.check_user_row_view, parent, false);
        TwitterFollowableUserViewHolder holder = new TwitterFollowableUserViewHolder(v);
        this.mViewHolders.add(new WeakReference(holder));
        v.setTag(holder);
        return v;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.UsersMemoryAdapter
    public void bindExtraViews(View view, int position, TwitterFollowableUserViewHolder holder, final VineUser user) {
        boolean following;
        holder.userVerified.setVisibility(8);
        final VineToggleButton followButton = holder.followButton;
        followButton.setVisibility(0);
        followButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.TwitterFollowableUsersMemoryAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                boolean follow = followButton.isChecked();
                TwitterFollowableUsersMemoryAdapter.this.mCallback.onUserSelected(user.userId, follow);
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.TwitterFollowableUsersMemoryAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                boolean follow = !followButton.isChecked();
                followButton.setChecked(follow);
                TwitterFollowableUsersMemoryAdapter.this.mCallback.onUserSelected(user.userId, follow);
            }
        });
        Friendships friendships = this.mFriendships;
        long userId = user.userId;
        if (friendships != null && friendships.contains(userId)) {
            following = friendships.isFollowing(userId);
        } else {
            following = user.isFollowing();
        }
        followButton.setChecked(following);
        followButton.setTag(new FollowButtonViewHolder(userId, following));
        TextView byline = holder.suggestionByline;
        if (byline != null) {
            byline.setText(user.twitterScreenname);
        }
        ImageView twitterVerified = holder.twitterVerified;
        if (twitterVerified != null) {
            if (user.twitterVerified) {
                twitterVerified.setVisibility(0);
            } else {
                twitterVerified.setVisibility(8);
            }
        }
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        if (this.mUsers != null) {
            Iterator<VineUser> it = this.mUsers.iterator();
            while (it.hasNext()) {
                VineUser user = it.next();
                if (!this.mProcessed.contains(user)) {
                    this.mProcessed.add(user);
                    this.mCallback.onUserAdded(user.userId);
                }
            }
            super.notifyDataSetChanged();
        }
    }

    class TwitterFollowableUserViewHolder extends UserViewHolder {
        VineToggleButton followButton;
        TextView suggestionByline;
        ImageView twitterVerified;
        ImageView userVerified;

        TwitterFollowableUserViewHolder(View view) {
            super(view);
            this.userVerified = (ImageView) view.findViewById(R.id.user_verified);
            this.followButton = (VineToggleButton) view.findViewById(R.id.user_row_btn_follow);
            this.suggestionByline = (TextView) view.findViewById(R.id.suggestion_byline);
            this.twitterVerified = (ImageView) view.findViewById(R.id.twitter_verified);
        }
    }
}
