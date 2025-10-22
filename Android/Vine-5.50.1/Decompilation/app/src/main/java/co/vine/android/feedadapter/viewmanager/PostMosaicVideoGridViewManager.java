package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.PostMosaicVideoGridViewHolder;
import co.vine.android.feedadapter.viewholder.VideoViewHolder;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PostMosaicVideoGridViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;
    private final int mVideoCount;
    private final int mVideoHeightLarge;
    private final int mVideoHeightSmall;
    private final VideoViewManager mVideoViewManager;
    private final int mVideoWidthSmall;

    public PostMosaicVideoGridViewManager(Context context, AppController appController, int videoCount, int videoWidthSmall, int videoHeightSmall, int videoHeightLarge) {
        this.mContext = context;
        this.mAppController = appController;
        this.mVideoCount = videoCount;
        this.mVideoViewManager = new VideoViewManager(context, appController, ViewType.PREVIEW);
        this.mVideoWidthSmall = videoWidthSmall;
        this.mVideoHeightSmall = videoHeightSmall;
        this.mVideoHeightLarge = videoHeightLarge;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.VIDEO_GRID;
    }

    public void bind(PostMosaicVideoGridViewHolder h, ArrayList<String> videoUrls) throws IllegalStateException {
        if (videoUrls != null) {
            ArrayList<VideoViewHolder> videoViewHolders = h.getVideoViewHolders();
            for (int i = 0; i < this.mVideoCount; i++) {
                this.mVideoViewManager.bind(videoViewHolders.get(i), videoUrls.get(i));
                switch (i) {
                    case 1:
                        this.mVideoViewManager.scaleVideoAndAlign(videoViewHolders.get(1), this.mVideoHeightSmall, this.mVideoWidthSmall, true);
                        break;
                    case 2:
                        this.mVideoViewManager.scaleVideoAndAlign(videoViewHolders.get(2), this.mVideoHeightLarge, this.mVideoWidthSmall, false);
                        break;
                    case 3:
                        this.mVideoViewManager.scaleVideoAndAlign(videoViewHolders.get(3), this.mVideoHeightLarge, this.mVideoWidthSmall, true);
                        break;
                    case 4:
                        this.mVideoViewManager.scaleVideoAndAlign(videoViewHolders.get(4), this.mVideoWidthSmall, this.mVideoWidthSmall, false);
                        break;
                }
            }
        }
    }
}
