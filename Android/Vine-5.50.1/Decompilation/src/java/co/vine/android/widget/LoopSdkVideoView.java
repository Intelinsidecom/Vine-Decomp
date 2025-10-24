package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.analytics.FlurryUtils;

/* loaded from: classes.dex */
public class LoopSdkVideoView extends SdkVideoView {
    private VideoViewInterface.OnCompletionListener mOnCompletionListener;

    public LoopSdkVideoView(Context context) {
        super(context);
        this.mOnCompletionListener = new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.widget.LoopSdkVideoView.1
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) {
                LoopSdkVideoView.this.seekTo(0);
                LoopSdkVideoView.this.start();
                FlurryUtils.trackVineLoopWatched();
            }
        };
        init();
    }

    public LoopSdkVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mOnCompletionListener = new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.widget.LoopSdkVideoView.1
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) {
                LoopSdkVideoView.this.seekTo(0);
                LoopSdkVideoView.this.start();
                FlurryUtils.trackVineLoopWatched();
            }
        };
        init();
    }

    public LoopSdkVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOnCompletionListener = new VideoViewInterface.OnCompletionListener() { // from class: co.vine.android.widget.LoopSdkVideoView.1
            @Override // co.vine.android.embed.player.VideoViewInterface.OnCompletionListener
            public void onCompletion(VideoViewInterface videoView) {
                LoopSdkVideoView.this.seekTo(0);
                LoopSdkVideoView.this.start();
                FlurryUtils.trackVineLoopWatched();
            }
        };
        init();
    }

    private void init() {
        setOnCompletionListener(this.mOnCompletionListener);
    }

    @Override // co.vine.android.embed.player.VideoViewInterface, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
