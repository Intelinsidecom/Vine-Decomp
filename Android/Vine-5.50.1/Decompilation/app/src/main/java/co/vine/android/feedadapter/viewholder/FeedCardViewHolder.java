package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class FeedCardViewHolder extends CardViewHolder implements PostViewHolder {
    private final FeedFooterViewHolder mFooterHolder;
    private final FeedInfoViewHolder mHeaderHolder;
    private final FeedVideoViewHolder mVideoHolder;

    public FeedCardViewHolder(View view) {
        super(view);
        this.mHeaderHolder = new FeedInfoViewHolder(view);
        this.mVideoHolder = new FeedVideoViewHolder(view);
        this.mFooterHolder = new FeedFooterViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.FEED;
    }

    public FeedInfoViewHolder getHeaderHolder() {
        return this.mHeaderHolder;
    }

    @Override // co.vine.android.feedadapter.viewholder.PostViewHolder
    public TimelineItemVideoViewHolder getVideoHolder() {
        return this.mVideoHolder;
    }

    public FeedFooterViewHolder getFooterHolder() {
        return this.mFooterHolder;
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
