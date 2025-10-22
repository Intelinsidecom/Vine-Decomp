package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.client.AppController;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.VideoViewHolder;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public class VideoViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;
    private final ViewType mType;

    public VideoViewManager(Context context, AppController appController, ViewType type) {
        this.mContext = context;
        this.mAppController = appController;
        this.mType = type;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return this.mType;
    }

    public void bind(VideoViewHolder h, String videoUrl) throws IllegalStateException {
        if (h.video != null) {
            h.video.setMute(true);
            h.video.setAutoPlayOnPrepared(true);
            h.video.setOnPreparedListener(null);
            h.video.setOnErrorListener(new VideoViewInterface.OnErrorListener() { // from class: co.vine.android.feedadapter.viewmanager.VideoViewManager.1
                @Override // co.vine.android.embed.player.VideoViewInterface.OnErrorListener
                public boolean onError(VideoViewInterface view, int what, int extra) {
                    view.setVisibility(8);
                    return true;
                }
            });
            h.video.setOnCompletionListener(new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.feedadapter.viewmanager.VideoViewManager.2
                @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
                public void onCompletion(VideoViewInterface view) throws IllegalStateException {
                    ((SdkVideoView) view).seekTo(0);
                    view.start();
                }
            });
            ResourceLoader videoLoader = new ResourceLoader(this.mContext, this.mAppController);
            videoLoader.loadVideo(h.video, videoUrl, true);
        }
    }

    public void scaleVideoAndAlign(VideoViewHolder h, int curHeight, int side, boolean alignBottom) {
        h.video.scaleVideoAndAlign(curHeight, side, alignBottom);
    }
}
