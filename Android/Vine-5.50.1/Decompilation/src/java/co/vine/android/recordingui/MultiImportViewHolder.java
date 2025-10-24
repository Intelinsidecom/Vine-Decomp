package co.vine.android.recordingui;

import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.vine.android.R;
import co.vine.android.player.StaticSizeExoPlayerTextureView;
import co.vine.android.recorder2.model.Segment;

/* loaded from: classes.dex */
public class MultiImportViewHolder {
    private Segment mSegment;
    private Uri mUri;
    private long mVideoDuration;
    public ViewGroup root;
    public StaticSizeExoPlayerTextureView textureView;
    public ImageView thumbnail;
    public RelativeLayout videoContainer;
    public ViewGroup videoViewPanner;

    public MultiImportViewHolder(ViewGroup root, String url, Segment segment) {
        this.videoContainer = (RelativeLayout) root.findViewById(R.id.video_container);
        this.thumbnail = (ImageView) root.findViewById(R.id.thumbnail);
        this.mUri = new Uri.Builder().scheme("file").path(url).build();
        this.mSegment = segment;
        this.root = root;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setDuration(long duration) {
        this.mVideoDuration = duration;
    }

    public long getDuration() {
        return this.mVideoDuration;
    }

    public Segment getSegment() {
        return this.mSegment;
    }
}
