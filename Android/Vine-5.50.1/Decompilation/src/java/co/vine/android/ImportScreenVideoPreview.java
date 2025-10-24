package co.vine.android;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.vine.android.api.VinePost;
import co.vine.android.client.AppController;
import co.vine.android.player.SdkVideoView;
import co.vine.android.util.ResourceLoader;
import co.vine.android.util.SystemUtil;
import co.vine.android.widget.RoundedCornerBitmapImageView;

/* loaded from: classes.dex */
public class ImportScreenVideoPreview extends Fragment {
    private AppController mAppController;
    private RoundedCornerBitmapImageView mAvatar;
    private boolean mIfCameraRoll;
    private ResourceLoader mLoader;
    private View mOriginalVineAttributions;
    private VinePost mPost;
    private TextView mUserName;
    private String mVideoPath;
    private SdkVideoView mVideoPlayer;

    @Override // android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mVideoPath = getArguments().getString("video_path");
        this.mPost = (VinePost) getArguments().getParcelable("creator");
        this.mIfCameraRoll = getArguments().getBoolean("camera");
        this.mAppController = AppController.getInstance(getActivity());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_screen_video_preview, container, false);
        this.mVideoPlayer = (SdkVideoView) view.findViewById(R.id.preview_player);
        this.mOriginalVineAttributions = view.findViewById(R.id.creator_data);
        this.mUserName = (TextView) this.mOriginalVineAttributions.findViewById(R.id.profile_name);
        this.mAvatar = (RoundedCornerBitmapImageView) this.mOriginalVineAttributions.findViewById(R.id.profile_pic);
        this.mLoader = new ResourceLoader(getActivity(), this.mAppController);
        return view;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) throws Throwable {
        super.onActivityCreated(savedInstanceState);
        if (!TextUtils.isEmpty(this.mVideoPath)) {
            Point size = SystemUtil.getDisplaySize(getActivity());
            this.mVideoPlayer.setAutoPlayOnPrepared(true);
            this.mVideoPlayer.setKeepScreenOn(true);
            this.mVideoPlayer.setLooping(true);
            if (this.mIfCameraRoll) {
                scaleVideo(this.mVideoPath, size.x);
                this.mVideoPlayer.setVideoPathDirect(this.mVideoPath);
                this.mOriginalVineAttributions.setVisibility(4);
            } else {
                this.mVideoPlayer.setSize(size.x, size.x);
                this.mLoader.loadVideo(this.mVideoPlayer, this.mVideoPath, true);
                this.mOriginalVineAttributions.setVisibility(0);
                this.mUserName.setText(this.mPost.username);
                ResourceLoader.ImageViewImageSetter imageSetter = new ResourceLoader.ImageViewImageSetter(this.mAvatar);
                this.mLoader.setImageWhenLoaded(imageSetter, this.mPost.avatarUrl, true);
            }
        }
    }

    public static Fragment newInstance(String videoPath, VinePost vinePost, boolean ifCameraRoll) {
        ImportScreenVideoPreview importScreenVideoPreview = new ImportScreenVideoPreview();
        Bundle args = new Bundle();
        args.putString("video_path", videoPath);
        args.putParcelable("creator", vinePost);
        args.putBoolean("camera", ifCameraRoll);
        importScreenVideoPreview.setArguments(args);
        return importScreenVideoPreview;
    }

    public void scaleVideo(String path, int screenHeight) throws Throwable {
        MediaMetadataRetriever retriever = null;
        try {
            MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
            try {
                retriever2.setDataSource(path);
                Bitmap bmp = retriever2.getFrameAtTime();
                int videoHeight = bmp.getHeight();
                int videoWidth = bmp.getWidth();
                bmp.recycle();
                double ratio = (videoWidth * 1.0d) / videoHeight;
                Double w = Double.valueOf(screenHeight * ratio);
                this.mVideoPlayer.setSize(w.intValue(), screenHeight);
                if (retriever2 != null) {
                    retriever2.release();
                }
            } catch (Throwable th) {
                th = th;
                retriever = retriever2;
                if (retriever != null) {
                    retriever.release();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
