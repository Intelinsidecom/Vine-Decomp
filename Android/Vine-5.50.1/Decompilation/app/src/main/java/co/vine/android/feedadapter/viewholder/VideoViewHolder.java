package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.player.SdkVideoView;

/* loaded from: classes.dex */
public class VideoViewHolder implements ViewHolder {
    private final ViewType mType;
    public final SdkVideoView video;

    public VideoViewHolder(View view, ViewType type, int resId) {
        this.mType = type;
        this.video = (SdkVideoView) view.findViewById(resId);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }
}
