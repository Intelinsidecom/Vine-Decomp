package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class FeedInfoViewHolder implements ViewHolder {
    public final View container;
    private final AvatarFollowViewHolder mAvatarFollowHolder;
    private final ImageViewHolder mAvatarHolder;
    private final BylineViewHolder mBylineHolder;
    private final TextViewHolder mUsernameHolder;

    public FeedInfoViewHolder(View view) {
        this.container = view.findViewById(R.id.top_content_container);
        this.mBylineHolder = new BylineViewHolder(view);
        this.mAvatarHolder = new ImageViewHolder(view, ViewType.AVATAR, R.id.user_image);
        this.mAvatarFollowHolder = new AvatarFollowViewHolder(view);
        this.mUsernameHolder = new TextViewHolder(view, ViewType.USERNAME, R.id.username);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.FEED_HEADER;
    }

    public BylineViewHolder getBylineHolder() {
        return this.mBylineHolder;
    }

    public ImageViewHolder getAvatarHolder() {
        return this.mAvatarHolder;
    }

    public AvatarFollowViewHolder getAvatarFollowHolder() {
        return this.mAvatarFollowHolder;
    }

    public TextViewHolder getUsernameHolder() {
        return this.mUsernameHolder;
    }
}
