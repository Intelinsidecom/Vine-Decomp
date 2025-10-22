package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PostMosaicVideoGridViewHolder implements ViewHolder {
    private static final int[] IDS = {R.id.video_zero, R.id.video_one, R.id.video_two, R.id.video_three, R.id.video_four};
    private final ViewType mType;
    private final int mVideoCount;
    private final ArrayList<VideoViewHolder> mVideoViewHolders;

    public PostMosaicVideoGridViewHolder(View view, ViewType type, int videoCount) {
        this.mType = type;
        this.mVideoViewHolders = new ArrayList<>(videoCount);
        this.mVideoCount = videoCount;
        for (int i = 0; i < videoCount; i++) {
            this.mVideoViewHolders.add(i, new VideoViewHolder(view, ViewType.PREVIEW, IDS[i]));
        }
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }

    public ArrayList<VideoViewHolder> getVideoViewHolders() {
        return this.mVideoViewHolders;
    }
}
