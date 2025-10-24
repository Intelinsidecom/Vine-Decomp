package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class WelcomePostHeaderViewHolder implements ViewHolder {
    public final View container;
    private final ImageViewHolder mAvatarHolder;
    private final ToggleButtonViewHolder mFollowHolder;
    private final TextViewHolder mUsernameHolder;

    public WelcomePostHeaderViewHolder(View view) {
        this.container = view.findViewById(R.id.top_content_container);
        this.mAvatarHolder = new ImageViewHolder(view, ViewType.AVATAR, R.id.user_image);
        this.mUsernameHolder = new TextViewHolder(view, ViewType.USERNAME, R.id.username);
        this.mFollowHolder = new ToggleButtonViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST_HEADER;
    }

    public ImageViewHolder getAvatarHolder() {
        return this.mAvatarHolder;
    }

    public TextViewHolder getUsernameHolder() {
        return this.mUsernameHolder;
    }

    public ToggleButtonViewHolder getFollowHolder() {
        return this.mFollowHolder;
    }
}
