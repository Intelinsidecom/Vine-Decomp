package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class HomePostInfoViewHolder implements ViewHolder {
    public final View container;
    private final AvatarFollowViewHolder mAvatarFollowHolder;
    private final ImageViewHolder mAvatarHolder;
    private final BylineViewHolder mBylineHolder;
    private final LoopCounterViewHolder mLoopCounterHolder;
    private final ViewGroup mTimestampContainer;
    private final TextViewHolder mTimestampHolder;
    private final TextViewHolder mUsernameHolder;

    public HomePostInfoViewHolder(View view) {
        this.container = view.findViewById(R.id.top_content_container);
        this.mBylineHolder = new BylineViewHolder(view);
        this.mAvatarHolder = new ImageViewHolder(view, ViewType.AVATAR, R.id.user_image);
        this.mAvatarFollowHolder = new AvatarFollowViewHolder(view);
        this.mUsernameHolder = new TextViewHolder(view, ViewType.USERNAME, R.id.username);
        this.mTimestampContainer = (ViewGroup) view.findViewById(R.id.timestamp_container);
        this.mTimestampHolder = new TextViewHolder(view, ViewType.TIMESTAMP, R.id.timestamp);
        this.mLoopCounterHolder = new LoopCounterViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST_HEADER;
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

    public TextViewHolder getTimestampHolder() {
        return this.mTimestampHolder;
    }

    public LoopCounterViewHolder getLoopCounterHolder() {
        return this.mLoopCounterHolder;
    }
}
