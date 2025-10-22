package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class HomePostCardViewHolder extends CardViewHolder implements PostViewHolder {
    private final HomePostFooterViewHolder mFooterHolder;
    private final HomePostInfoViewHolder mHeaderHolder;
    private final PostVideoViewHolder mVideoHolder;

    public HomePostCardViewHolder(View view) {
        super(view);
        this.mHeaderHolder = new HomePostInfoViewHolder(view);
        this.mVideoHolder = new PostVideoViewHolder(view);
        this.mFooterHolder = new HomePostFooterViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST;
    }

    public HomePostInfoViewHolder getHeaderHolder() {
        return this.mHeaderHolder;
    }

    @Override // co.vine.android.feedadapter.viewholder.PostViewHolder
    public TimelineItemVideoViewHolder getVideoHolder() {
        return this.mVideoHolder;
    }

    public HomePostFooterViewHolder getFooterHolder() {
        return this.mFooterHolder;
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
