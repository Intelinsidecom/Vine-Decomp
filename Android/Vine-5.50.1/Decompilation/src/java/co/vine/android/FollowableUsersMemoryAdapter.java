package co.vine.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class FollowableUsersMemoryAdapter extends UsersMemoryAdapter<FollowableUserViewHolder> {
    private boolean mFollow;
    private Friendships mFriendships;
    private View.OnClickListener mOnFollowClickListener;
    private boolean mSuggestion;

    public FollowableUsersMemoryAdapter(Context context, AppController appController, boolean follow, boolean suggestion, View.OnClickListener onFollowClickListener, Friendships friendships) {
        super(context, appController);
        this.mFollow = follow;
        this.mSuggestion = suggestion;
        this.mOnFollowClickListener = onFollowClickListener;
        this.mFriendships = friendships;
    }

    @Override // co.vine.android.UsersMemoryAdapter
    protected View newView(int position, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
        View v = layoutInflater.inflate(R.layout.user_row_view, parent, false);
        FollowableUserViewHolder holder = new FollowableUserViewHolder(v);
        this.mViewHolders.add(new WeakReference(holder));
        v.setTag(holder);
        return v;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.UsersMemoryAdapter
    public void bindExtraViews(View view, int position, FollowableUserViewHolder holder, VineUser user) {
        boolean following;
        ImageButton followButton = holder.followButton;
        if (this.mFollow && holder.userId != this.mAppController.getActiveId()) {
            followButton.setOnClickListener(this.mOnFollowClickListener);
            followButton.setVisibility(0);
            Friendships friendships = this.mFriendships;
            long userId = user.userId;
            if (friendships != null && friendships.contains(userId)) {
                following = friendships.isFollowing(userId);
            } else {
                following = user.isFollowing();
            }
            followButton.setSelected(following);
            followButton.setTag(new FollowButtonViewHolder(userId, following));
        } else {
            followButton.setVisibility(4);
        }
        TextView byline = holder.suggestionByline;
        if (byline != null) {
            if (this.mSuggestion) {
                byline.setVisibility(0);
                byline.setText(user.byline);
                holder.twitterScreenname.setVisibility(8);
                return;
            }
            byline.setVisibility(8);
        }
    }
}
