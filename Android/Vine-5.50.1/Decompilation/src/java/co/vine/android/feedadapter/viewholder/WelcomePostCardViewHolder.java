package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class WelcomePostCardViewHolder extends CardViewHolder implements PostViewHolder {
    private final WelcomePostFooterViewHolder mFooterHolder;
    private final WelcomePostHeaderViewHolder mHeaderHolder;
    private final PostVideoViewHolder mVideoHolder;

    public WelcomePostCardViewHolder(View view) {
        super(view);
        this.mHeaderHolder = new WelcomePostHeaderViewHolder(view);
        this.mVideoHolder = new PostVideoViewHolder(view);
        this.mFooterHolder = new WelcomePostFooterViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST;
    }

    public WelcomePostHeaderViewHolder getHeaderHolder() {
        return this.mHeaderHolder;
    }

    @Override // co.vine.android.feedadapter.viewholder.PostViewHolder
    public TimelineItemVideoViewHolder getVideoHolder() {
        return this.mVideoHolder;
    }

    public WelcomePostFooterViewHolder getFooterHolder() {
        return this.mFooterHolder;
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
