package co.vine.android;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RecommendedUserAdapter extends RecyclerView.Adapter<ViewHolder> {
    private int mButtonWidth;
    private Context mContext;
    private View.OnClickListener mOnClickListener;
    private ResourceLoader mResourceLoader;
    private List<VineUser> mUserRecs;

    public RecommendedUserAdapter(Context context, List<VineUser> userRecs, View.OnClickListener onFollowClickListener) {
        this.mUserRecs = new ArrayList();
        this.mContext = context;
        this.mResourceLoader = new ResourceLoader(this.mContext, AppController.getInstance(this.mContext));
        this.mUserRecs = userRecs;
        this.mOnClickListener = onFollowClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        View clickableRegion;
        Button followButton;
        TextView userName;

        public ViewHolder(View v) {
            super(v);
            this.avatar = (ImageView) v.findViewById(R.id.recommended_user_avatar);
            this.userName = (TextView) v.findViewById(R.id.recommended_user_name);
            this.followButton = (Button) v.findViewById(R.id.recommended_user_follow);
            this.clickableRegion = v.findViewById(R.id.clickable_region);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        VineUser user = this.mUserRecs.get(position);
        return ((user instanceof FollowableUser) && ((FollowableUser) user).isTwitterFollow) ? 0 : 1;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recommendedUserView;
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == 0) {
            recommendedUserView = inflater.inflate(R.layout.recommended_twitter_user, parent, false);
        } else {
            recommendedUserView = inflater.inflate(R.layout.recommended_user, parent, false);
        }
        return new ViewHolder(recommendedUserView);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder holder, int position) throws Resources.NotFoundException {
        int color;
        VineUser user = this.mUserRecs.get(position);
        if ((user instanceof FollowableUser) && ((FollowableUser) user).isTwitterFollow) {
            holder.userName.setText("@" + user.twitterScreenname);
            holder.followButton.setSelected(user.followingOnTwitter);
            holder.clickableRegion.setTag(user.twitterScreenname);
        } else {
            holder.userName.setText(user.username);
            holder.followButton.setSelected(user.isFollowing());
            holder.followButton.setText(user.isFollowing() ? R.string.profile_following : R.string.profile_follow);
            Button button = holder.followButton;
            if (user.isFollowing()) {
                color = this.mContext.getResources().getColor(R.color.solid_white);
            } else {
                color = this.mContext.getResources().getColor(R.color.black_eighty_percent);
            }
            button.setTextColor(color);
            holder.clickableRegion.setTag(Long.valueOf(user.userId));
        }
        this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(holder.avatar), this.mUserRecs.get(position).avatarUrl, true);
        holder.followButton.setTag(new FollowButtonViewHolder(user, holder.followButton));
        holder.clickableRegion.setOnClickListener(this.mOnClickListener);
        holder.followButton.setOnClickListener(this.mOnClickListener);
        holder.followButton.setWidth(this.mButtonWidth);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mUserRecs.size();
    }
}
