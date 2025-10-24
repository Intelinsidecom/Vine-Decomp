package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class PostVideoViewHolder extends TimelineItemVideoViewHolder implements VideoViewInterface.SurfaceUpdatedListener, ViewHolder {
    public View hiddenOverlay;
    public LongformPostOverlayViewHolder longformOverlayHolder;

    public PostVideoViewHolder(View view) {
        super(view);
        this.hiddenOverlay = view.findViewById(R.id.hidden_vine_overlay_container);
        this.longformOverlayHolder = new LongformPostOverlayViewHolder(view);
    }

    @Override // co.vine.android.feedadapter.viewholder.TimelineItemVideoViewHolder, co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST_VIDEO;
    }
}
