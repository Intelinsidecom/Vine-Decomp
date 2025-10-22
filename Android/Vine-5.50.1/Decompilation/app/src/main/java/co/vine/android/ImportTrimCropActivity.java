package co.vine.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import co.vine.android.player.StaticSizeExoPlayerTextureView;
import co.vine.android.recorder.RegularProgressView;
import co.vine.android.scribe.ScribeLoggerSingleton;
import co.vine.android.scribe.VideoImportCompletedScribeLogger;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.SystemUtil;
import co.vine.android.util.Util;
import co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout;
import com.edisonwang.android.slog.SLog;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class ImportTrimCropActivity extends BaseActionBarActivity implements View.OnClickListener, ThumbnailRangeFinderLayout.OnVideoTrimmedListener {
    private long mAddRequestTime;
    private Uri mFileUri;
    private Handler mHandler;
    private int mMaxDurationMs;
    private ImageButton mPlayButton;
    private int mPlayerHeight;
    private int mPlayerWidth;
    private RegularProgressView mProgressView;
    private Point mSize;
    private String mSourcePostId;
    private int mStartDurationMs;
    private float mStartRatio;
    private StaticSizeExoPlayerTextureView mTextureView;
    private ThumbnailRangeFinderLayout mThumbnailRangeFinder;
    private FrameLayout mVideoContainer;
    private int mVideoHeight;
    private ViewGroup mVideoViewPanner;
    private int mVideoWidth;
    private Runnable mProgressChecker = new Runnable() { // from class: co.vine.android.ImportTrimCropActivity.7
        @Override // java.lang.Runnable
        public void run() {
            int position = ImportTrimCropActivity.this.checkTimeBar();
            ImportTrimCropActivity.this.mHandler.postDelayed(ImportTrimCropActivity.this.mProgressChecker, Math.max(66, 200 - (position % HttpResponseCode.OK)));
        }
    };
    private View.OnTouchListener mVideoViewTouchListener = new View.OnTouchListener() { // from class: co.vine.android.ImportTrimCropActivity.8
        private long mTouchTime;

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    this.mTouchTime = System.currentTimeMillis();
                    break;
                case 1:
                    if (System.currentTimeMillis() - this.mTouchTime < 200) {
                        ImportTrimCropActivity.this.playPauseVideo();
                        break;
                    }
                    break;
            }
            return true;
        }
    };

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    @SuppressLint({"MissingSuperCall"})
    protected void onCreate(Bundle savedInstanceState) throws Throwable {
        onCreate(savedInstanceState, R.layout.import_trim_crop_activity, true, true);
    }

    @Override // co.vine.android.BaseActionBarActivity
    public void onCreate(Bundle savedInstanceState, int layout, boolean loginRequired, boolean readOnly) throws Throwable {
        super.onCreate(savedInstanceState, layout, loginRequired, readOnly);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            this.mStartDurationMs = extras.getInt("current_duration");
            this.mMaxDurationMs = extras.getInt("max_duration");
            if (getIntent().getData() != null) {
                this.mFileUri = getIntent().getData();
            }
            this.mSourcePostId = getIntent().getStringExtra("extra_source_post_id");
        }
        Point size = SystemUtil.getDisplaySize(this);
        this.mHandler = new Handler(Looper.getMainLooper());
        ViewGroup rootView = (ViewGroup) findViewById(R.id.import_view_root);
        this.mVideoContainer = (FrameLayout) findViewById(R.id.video_container);
        this.mPlayButton = (ImageButton) findViewById(R.id.import_play_button);
        this.mProgressView = (RegularProgressView) findViewById(R.id.import_progress_view);
        this.mProgressView.setSelectedColor(getResources().getColor(R.color.vine_green));
        this.mProgressView.setColor(getResources().getColor(R.color.trim_yellow));
        this.mStartRatio = this.mStartDurationMs / this.mMaxDurationMs;
        this.mProgressView.setProgressRatio(this.mStartRatio);
        this.mProgressView.setSelectedSection(0.0f, this.mStartRatio);
        findViewById(R.id.importTitle).setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportTrimCropActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ImportTrimCropActivity.this.onBackPressed();
            }
        });
        findViewById(R.id.trimCropBackButton).setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportTrimCropActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ImportTrimCropActivity.this.onBackPressed();
            }
        });
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportTrimCropActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ImportTrimCropActivity.this.add();
            }
        });
        int height = getResources().getDimensionPixelSize(R.dimen.carousel_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, height);
        ThumbnailRangeFinderLayout thumbnailRangeFinder = new ThumbnailRangeFinderLayout(this, (int) (this.mStartRatio * size.x), false);
        thumbnailRangeFinder.setListener(this);
        rootView.addView(thumbnailRangeFinder, params);
        this.mThumbnailRangeFinder = thumbnailRangeFinder;
        StaticSizeExoPlayerTextureView videoView = new StaticSizeExoPlayerTextureView(this);
        videoView.setOnTouchListener(this.mVideoViewTouchListener);
        videoView.setSize(size.x, size.x);
        this.mTextureView = videoView;
        this.mSize = size;
        if (this.mFileUri != null) {
            onUriResult(this.mFileUri);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == 2 && data != null) {
                Uri uri = data.getData();
                try {
                    onUriResult(uri);
                    return;
                } catch (Throwable th) {
                    showInvalidVideoError(uri, R.string.error_import_video_unknown_error, "Failed to validate video.");
                    return;
                }
            }
            if (requestCode == 1) {
                setResult(resultCode, data);
                release();
                finish();
                return;
            }
            return;
        }
        finish();
    }

    @TargetApi(17)
    private void onUriResult(final Uri uri) throws Throwable {
        long videoDurationMs;
        int width;
        int height;
        String filePath = SdkFileUtils.getPath(this, uri);
        if (filePath == null) {
            throw new IllegalArgumentException("File path was null: " + uri);
        }
        MediaMetadataRetriever retriever = null;
        try {
            MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
            try {
                retriever2.setDataSource(filePath);
                String durationMsStr = retriever2.extractMetadata(9);
                String widthStr = retriever2.extractMetadata(18);
                String heightStr = retriever2.extractMetadata(19);
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
                    String rotationStr = retriever2.extractMetadata(24);
                    if (rotationStr != null) {
                        rotation = Integer.parseInt(rotationStr);
                    }
                } else {
                    Bitmap b = retriever2.getFrameAtTime(0L);
                    if (b != null && width != height && height == b.getWidth()) {
                        rotation = 90;
                    }
                }
                if (width != -1 && height != -1 && videoDurationMs != -1) {
                    SLog.i("Width and height are avail from metadata.");
                    this.mThumbnailRangeFinder.setVideoPath(uri, videoDurationMs, this.mMaxDurationMs);
                    prepareVideoView(width, height, rotation, videoDurationMs);
                } else {
                    prepareVideoView(480, 480, rotation, videoDurationMs);
                    this.mTextureView.setVideoSizeChangedListener(new StaticSizeExoPlayerTextureView.VideoSizeChangedListener() { // from class: co.vine.android.ImportTrimCropActivity.4
                        @Override // co.vine.android.player.StaticSizeExoPlayerTextureView.VideoSizeChangedListener
                        public void onVideoSizeChanged(ExoPlayer player, int width2, int height2, int unappliedRotationDegrees) {
                            SLog.i("Width and height are avail from media player.");
                            int duration = (int) player.getDuration();
                            ImportTrimCropActivity.this.mThumbnailRangeFinder.setVideoPath(uri, duration, ImportTrimCropActivity.this.mMaxDurationMs);
                            ImportTrimCropActivity.this.prepareVideoView(width2, height2, unappliedRotationDegrees, duration);
                        }
                    });
                }
                this.mTextureView.setExoPlayerErrorListener(new StaticSizeExoPlayerTextureView.ExoPlayerErrorListener() { // from class: co.vine.android.ImportTrimCropActivity.5
                    @Override // co.vine.android.player.StaticSizeExoPlayerTextureView.ExoPlayerErrorListener
                    public void onError(ExoPlaybackException error) {
                        ImportTrimCropActivity.this.showInvalidVideoError(uri, R.string.error_import_video_not_supported, error);
                    }
                });
                this.mTextureView.setPlaybackEndedListener(new StaticSizeExoPlayerTextureView.PlaybackEndedListener() { // from class: co.vine.android.ImportTrimCropActivity.6
                    @Override // co.vine.android.player.StaticSizeExoPlayerTextureView.PlaybackEndedListener
                    public void onPlaybackEnded() {
                        ImportTrimCropActivity.this.mTextureView.seekTo((int) (ImportTrimCropActivity.this.mThumbnailRangeFinder.trimStartTimeUsec / 1000));
                    }
                });
                this.mFileUri = uri;
                preparePlayer(uri);
                this.mPlayButton.setVisibility(4);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void showInvalidVideoError(Uri uri, int userFacingStringRes, Object error) {
        CrashUtil.logException(new IllegalArgumentException(), "Failed to select video: {} {}.", uri, error);
        Util.showCenteredToast(this, getString(userFacingStringRes));
        VideoImportCompletedScribeLogger.logImportTrimError(ScribeLoggerSingleton.getInstance(this), AppStateProviderSingleton.getInstance(this));
        setResult(0);
        finish();
    }

    private void preparePlayer(Uri uri) {
        if (uri != null) {
            this.mTextureView.openVideo(this.mFileUri);
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mHandler.post(this.mProgressChecker);
        if (getIntent().getData() == null && this.mFileUri == null) {
            Intent pickerIntent = new Intent("android.intent.action.GET_CONTENT");
            pickerIntent.setType("video/*");
            pickerIntent.putExtra("android.intent.extra.LOCAL_ONLY", true);
            try {
                startActivityForResult(Intent.createChooser(pickerIntent, getString(R.string.pick_video)), 2);
            } catch (ActivityNotFoundException e) {
                Util.showCenteredToast(this, getString(R.string.unsupported_feature) + " \n (" + getString(R.string.pick_video) + ") ");
            }
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mHandler.removeCallbacks(this.mProgressChecker);
        if (this.mTextureView.getPlayWhenReady()) {
            playPauseVideo();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareVideoView(int videoWidth, int videoHeight, int rotation, long videoDurationMs) {
        ViewGroup videoViewPanner;
        if (this.mVideoViewPanner != null) {
            this.mVideoViewPanner.removeView(this.mTextureView);
            this.mVideoContainer.removeView(this.mVideoViewPanner);
            this.mVideoViewPanner = null;
        }
        StaticSizeExoPlayerTextureView textureView = this.mTextureView;
        if (rotation == 90 || rotation == 270) {
            videoWidth = videoHeight;
            videoHeight = videoWidth;
        }
        if (videoWidth > videoHeight) {
            videoViewPanner = new HorizontalScrollView(this);
            this.mPlayerWidth = (int) ((videoWidth / videoHeight) * this.mSize.x);
            this.mPlayerHeight = this.mSize.x;
        } else {
            videoViewPanner = new ScrollView(this);
            this.mPlayerWidth = this.mSize.x;
            this.mPlayerHeight = (int) ((videoHeight / videoWidth) * this.mSize.x);
        }
        textureView.setSize(this.mPlayerWidth, this.mPlayerHeight);
        if (Build.VERSION.SDK_INT < 21) {
            textureView.setSurfaceRotation(rotation);
        }
        videoViewPanner.addView(textureView);
        videoViewPanner.setHorizontalScrollBarEnabled(false);
        videoViewPanner.setVerticalScrollBarEnabled(false);
        this.mVideoContainer.addView(videoViewPanner, 0, new LinearLayout.LayoutParams(this.mSize.x, this.mSize.x));
        this.mVideoViewPanner = videoViewPanner;
        this.mVideoWidth = videoWidth;
        this.mVideoHeight = videoHeight;
        this.mProgressView.setProgressRatio(this.mStartRatio + (videoDurationMs / this.mMaxDurationMs));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.import_play_button) {
            playPauseVideo();
        }
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.import_trim, menu);
        return true;
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onVideoTrimmedByScrubber() {
        updatePosition(((int) this.mThumbnailRangeFinder.trimEndTimeUsec) / 1000);
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onVideoTrimmedByCarousel() {
        updatePosition(((int) this.mThumbnailRangeFinder.trimStartTimeUsec) / 1000);
    }

    @Override // co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout.OnVideoTrimmedListener
    public void onStartEndTimeChanged(long trimStartTimeUsec, long trimEndTimeUsec) {
        this.mProgressView.setProgressRatio(this.mStartRatio + (((trimEndTimeUsec - trimStartTimeUsec) / 1000.0f) / this.mMaxDurationMs));
    }

    private void updatePosition(int position) {
        this.mTextureView.setPlayWhenReady(this.mTextureView.getPlayWhenReady());
        this.mTextureView.seekTo(position);
        this.mPlayButton.setVisibility(this.mTextureView.getPlayWhenReady() ? 8 : 0);
    }

    @Override // co.vine.android.BaseActionBarActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add) {
            add();
        } else if (id == 16908332) {
            release();
        }
        return super.onOptionsItemSelected(item);
    }

    private void release() {
        if (this.mThumbnailRangeFinder != null) {
            this.mThumbnailRangeFinder.stop();
        }
        if (this.mTextureView != null) {
            this.mTextureView.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void add() {
        if (this.mVideoViewPanner != null || System.currentTimeMillis() - this.mAddRequestTime < 200) {
            this.mThumbnailRangeFinder.computeStartEndTimes();
            Intent data = new Intent("video_picked", this.mFileUri);
            data.putExtra("trim_start_time_usec", this.mThumbnailRangeFinder.trimStartTimeUsec);
            data.putExtra("trim_end_time_usec", this.mThumbnailRangeFinder.trimEndTimeUsec);
            Point point = new Point();
            if (this.mVideoViewPanner != null) {
                point.x = (int) ((this.mVideoViewPanner.getScrollX() / this.mPlayerWidth) * this.mVideoWidth);
                point.y = (int) ((this.mVideoViewPanner.getScrollY() / this.mPlayerHeight) * this.mVideoHeight);
            }
            data.putExtra("crop_origin", point);
            data.putExtra("extra_source_post_id", this.mSourcePostId);
            setResult(-1, data);
            release();
            finish();
            return;
        }
        this.mAddRequestTime = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playPauseVideo() {
        if (this.mTextureView != null) {
            if (this.mTextureView.getPlayWhenReady()) {
                this.mTextureView.setPlayWhenReady(false);
                this.mPlayButton.setVisibility(0);
            } else {
                this.mTextureView.seekTo(((int) this.mThumbnailRangeFinder.trimStartTimeUsec) / 1000);
                this.mTextureView.setPlayWhenReady(true);
                this.mPlayButton.setVisibility(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int checkTimeBar() {
        int trimEndTimeMs = ((int) this.mThumbnailRangeFinder.trimEndTimeUsec) / 1000;
        int position = this.mTextureView.getCurrentPosition();
        if (position >= trimEndTimeMs && trimEndTimeMs > 0) {
            this.mTextureView.seekTo((int) (this.mThumbnailRangeFinder.trimStartTimeUsec / 1000));
            this.mTextureView.setPlayWhenReady(true);
        }
        return position;
    }

    @Override // co.vine.android.BaseActionBarActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        release();
        super.onBackPressed();
    }
}
