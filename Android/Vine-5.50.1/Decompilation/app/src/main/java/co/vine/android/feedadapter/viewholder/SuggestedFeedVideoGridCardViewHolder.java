package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class SuggestedFeedVideoGridCardViewHolder extends CardViewHolder {
    private final TextViewHolder mDayHolder;
    private final PostMosaicViewHolder mMosaicHolder;
    private final int mVideoCount;
    private final PostMosaicVideoGridViewHolder mVideoGridHolder;

    public SuggestedFeedVideoGridCardViewHolder(View view, int videoCount) {
        super(view);
        this.mVideoCount = videoCount;
        this.mMosaicHolder = new PostMosaicViewHolder(view);
        this.mVideoGridHolder = new PostMosaicVideoGridViewHolder(view, ViewType.VIDEO_GRID, videoCount);
        this.mDayHolder = new TextViewHolder(view, ViewType.DAY, R.id.day);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.SUGGESTED_FEED;
    }

    public PostMosaicViewHolder getMosaicHolder() {
        return this.mMosaicHolder;
    }

    public PostMosaicVideoGridViewHolder getVideoGridHolder() {
        return this.mVideoGridHolder;
    }

    public TextViewHolder getDayHolder() {
        return this.mDayHolder;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.view.setOnClickListener(listener);
    }

    @Override // co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder
    public View getMeasuringView() {
        return this.view;
    }
}
