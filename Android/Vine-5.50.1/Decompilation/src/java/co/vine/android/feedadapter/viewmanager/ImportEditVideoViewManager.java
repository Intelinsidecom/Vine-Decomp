package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.graphics.Point;
import co.vine.android.player.StaticSizeExoPlayerTextureView;
import co.vine.android.recordingui.MultiImportViewHolder;
import co.vine.android.util.SystemUtil;
import co.vine.android.views.LockableHorizontalScrollView;
import co.vine.android.views.LockableScrollView;

/* loaded from: classes.dex */
public class ImportEditVideoViewManager {
    private Context mContext;
    private int mPlayerHeight;
    private int mPlayerWidth;
    private Point mSize;

    public ImportEditVideoViewManager(Context context) {
        this.mContext = context;
        this.mSize = SystemUtil.getDisplaySize(this.mContext);
    }

    public void bind(final MultiImportViewHolder h, int width, int height, int rotation) {
        h.textureView = new StaticSizeExoPlayerTextureView(this.mContext);
        if (rotation == 90 || rotation == 270) {
            width = height;
            height = width;
        }
        if (width > height) {
            h.videoViewPanner = new LockableHorizontalScrollView(this.mContext);
            this.mPlayerWidth = (int) ((width / height) * this.mSize.x);
            this.mPlayerHeight = this.mSize.x;
        } else {
            h.videoViewPanner = new LockableScrollView(this.mContext);
            this.mPlayerWidth = this.mSize.x;
            this.mPlayerHeight = (int) ((height / width) * this.mSize.x);
        }
        h.textureView.setSize(this.mPlayerWidth, this.mPlayerHeight);
        h.textureView.setLooping(true);
        h.textureView.setMute(false);
        h.videoViewPanner.addView(h.textureView);
        h.videoViewPanner.setHorizontalScrollBarEnabled(false);
        h.videoViewPanner.setVerticalScrollBarEnabled(false);
        if (h.getSegment().getCropOrigin() != null) {
            h.videoViewPanner.post(new Runnable() { // from class: co.vine.android.feedadapter.viewmanager.ImportEditVideoViewManager.1
                @Override // java.lang.Runnable
                public void run() {
                    h.videoViewPanner.scrollTo(h.getSegment().getCropOrigin().x, h.getSegment().getCropOrigin().y);
                }
            });
        }
        h.videoContainer.addView(h.videoViewPanner);
    }

    public void toggleCrop(MultiImportViewHolder h) {
        if (h.videoViewPanner instanceof LockableHorizontalScrollView) {
            ((LockableHorizontalScrollView) h.videoViewPanner).toggleScroll();
            if (!((LockableHorizontalScrollView) h.videoViewPanner).getScrollable()) {
                h.getSegment().setCropOrigin(new Point((int) h.videoViewPanner.getX(), (int) h.videoViewPanner.getY()));
                return;
            }
            return;
        }
        ((LockableScrollView) h.videoViewPanner).toggleScroll();
        if (!((LockableScrollView) h.videoViewPanner).getScrollable()) {
            h.getSegment().setCropOrigin(new Point((int) h.videoViewPanner.getX(), (int) h.videoViewPanner.getY()));
        }
    }
}
