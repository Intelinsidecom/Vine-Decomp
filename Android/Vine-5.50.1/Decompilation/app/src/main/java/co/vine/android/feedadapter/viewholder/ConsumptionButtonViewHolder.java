package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public final class ConsumptionButtonViewHolder extends ButtonViewHolder {
    public final View musicIcon;
    public final View videoIcon;

    public ConsumptionButtonViewHolder(View view) {
        super(view, ViewType.CONSUMPTION_BUTTON, R.id.music_feed_icon_container);
        this.musicIcon = view.findViewById(R.id.music_feed_icon);
        this.videoIcon = view.findViewById(R.id.video_feed_icon);
    }
}
