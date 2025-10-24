package co.vine.android.recordingui;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.R;
import co.vine.android.feedadapter.viewmanager.ImportEditVideoViewManager;
import co.vine.android.player.StaticSizeExoPlayerTextureView;
import co.vine.android.recorder2.model.Segment;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class MultiTrimPagerAdapter extends PagerAdapter {
    private Context mContext;
    private MultiImportViewHolder mCurrentHolder;
    private MultiImportTrimmerManager mManager;
    private ArrayList<Segment> mSegments;
    private ImportEditVideoViewManager mVideoViewManager;
    private HashMap<Integer, MultiImportViewHolder> mViewHolders = new HashMap<>();

    public MultiTrimPagerAdapter(Context context, ArrayList<Segment> segments, MultiImportTrimmerManager manager) {
        this.mContext = context;
        this.mSegments = segments;
        this.mVideoViewManager = new ImportEditVideoViewManager(context);
        this.mManager = manager;
    }

    @Override // android.support.v4.view.PagerAdapter
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mSegments.size();
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getItemPosition(Object object) {
        return this.mViewHolders.containsValue(object) ? -1 : -2;
    }

    @Override // android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) throws Throwable {
        ViewGroup viewContainer = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R.layout.multi_import_view_holder, (ViewGroup) null);
        Segment segment = this.mSegments.get(position);
        String videoPath = TextUtils.isEmpty(segment.getImportVideoPath()) ? segment.getVideoPath() : segment.getImportVideoPath();
        MultiImportViewHolder holder = new MultiImportViewHolder(viewContainer, videoPath, segment);
        holder.thumbnail.setImageURI(Uri.parse(segment.getThumbnailPath()));
        drawPlayer(videoPath, holder, this.mManager);
        this.mViewHolders.put(Integer.valueOf(position), holder);
        container.addView(viewContainer, 0);
        if (position == 0) {
            show(0);
        }
        return viewContainer;
    }

    public void show(int position) {
        if (this.mCurrentHolder != null) {
            this.mCurrentHolder.thumbnail.setVisibility(0);
            this.mCurrentHolder.textureView.setPlayWhenReady(false);
        }
        MultiImportViewHolder holder = this.mViewHolders.get(Integer.valueOf(position));
        setupAndPlayDraftPreview(holder);
    }

    private void setupAndPlayDraftPreview(MultiImportViewHolder holder) {
        StaticSizeExoPlayerTextureView videoView = holder.textureView;
        holder.thumbnail.setVisibility(8);
        videoView.setVisibility(0);
        holder.textureView.openVideo(holder.getUri());
        this.mCurrentHolder = holder;
        updateCarousel(holder);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(this.mViewHolders.get(Integer.valueOf(position)).root);
    }

    public void release() {
        if (this.mCurrentHolder != null && this.mCurrentHolder.textureView != null) {
            this.mCurrentHolder.textureView.release();
        }
    }

    private void updateCarousel(MultiImportViewHolder holder) {
        this.mManager.updateUri(holder, holder.getDuration());
    }

    private void drawPlayer(String path, final MultiImportViewHolder holder, final MultiImportTrimmerManager manager) throws Throwable {
        MediaMetadataRetriever retriever;
        long videoDurationMs;
        int width;
        int height;
        if (path == null) {
            throw new IllegalArgumentException("File path was null: ");
        }
        MediaMetadataRetriever retriever2 = null;
        try {
            retriever = new MediaMetadataRetriever();
        } catch (Throwable th) {
            th = th;
        }
        try {
            retriever.setDataSource(path);
            String durationMsStr = retriever.extractMetadata(9);
            String widthStr = retriever.extractMetadata(18);
            String heightStr = retriever.extractMetadata(19);
            if (durationMsStr != null) {
                videoDurationMs = Long.parseLong(durationMsStr);
            } else {
                videoDurationMs = -1;
            }
            if (widthStr != null) {
                width = Integer.parseInt(widthStr);
            } else {
                width = -1;
            }
            if (heightStr != null) {
                height = Integer.parseInt(heightStr);
            } else {
                height = -1;
            }
            int rotation = 0;
            if (Build.VERSION.SDK_INT > 16) {
                String rotationStr = retriever.extractMetadata(24);
                if (rotationStr != null) {
                    rotation = Integer.parseInt(rotationStr);
                }
            } else {
                Bitmap b = retriever.getFrameAtTime(0L);
                if (b != null && width != height && height == b.getWidth()) {
                    rotation = 90;
                }
            }
            holder.setDuration(videoDurationMs);
            if (width != -1 && height != -1 && videoDurationMs != -1) {
                SLog.i("Width and height are avail from metadata.");
                this.mVideoViewManager.bind(holder, width, height, rotation);
            } else {
                this.mVideoViewManager.bind(holder, 480, 480, rotation);
            }
            holder.textureView.setPlaybackEndedListener(new StaticSizeExoPlayerTextureView.PlaybackEndedListener() { // from class: co.vine.android.recordingui.MultiTrimPagerAdapter.1
                @Override // co.vine.android.player.StaticSizeExoPlayerTextureView.PlaybackEndedListener
                public void onPlaybackEnded() {
                    manager.seekTo(holder.textureView);
                }
            });
            if (retriever != null) {
                retriever.release();
            }
        } catch (Throwable th2) {
            th = th2;
            retriever2 = retriever;
            if (retriever2 != null) {
                retriever2.release();
            }
            throw th;
        }
    }

    public void toggleCrop() {
        this.mVideoViewManager.toggleCrop(this.mCurrentHolder);
    }
}
