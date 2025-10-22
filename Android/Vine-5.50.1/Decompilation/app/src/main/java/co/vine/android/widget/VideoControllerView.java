package co.vine.android.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.util.Util;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class VideoControllerView extends FrameLayout {
    private ViewGroup mAnchor;
    private Context mContext;
    private TextView mCurrentTime;
    private boolean mDragging;
    private TextView mEndTime;
    private SeekBar.OnSeekBarChangeListener mExternalSeekbarListener;
    private Handler mHandler;
    private MediaController.MediaPlayerControl mPlayerController;
    private SeekBar mProgress;
    private View mRoot;
    private SeekBar.OnSeekBarChangeListener mSeekListener;

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mHandler = new MessageHandler(this);
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: co.vine.android.widget.VideoControllerView.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = true;
                VideoControllerView.this.mHandler.removeMessages(2);
                if (VideoControllerView.this.mExternalSeekbarListener != null) {
                    VideoControllerView.this.mExternalSeekbarListener.onStartTrackingTouch(bar);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (VideoControllerView.this.mPlayerController != null && fromuser) {
                    long duration = VideoControllerView.this.mPlayerController.getDuration();
                    long newposition = (progress * duration) / 1000;
                    VideoControllerView.this.mPlayerController.seekTo((int) newposition);
                    if (VideoControllerView.this.mCurrentTime != null) {
                        VideoControllerView.this.mCurrentTime.setText(Util.stringForTime((int) newposition));
                    }
                    if (VideoControllerView.this.mExternalSeekbarListener != null) {
                        VideoControllerView.this.mExternalSeekbarListener.onProgressChanged(bar, progress, fromuser);
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = false;
                VideoControllerView.this.setProgress();
                VideoControllerView.this.mHandler.sendEmptyMessage(2);
                if (VideoControllerView.this.mExternalSeekbarListener != null) {
                    VideoControllerView.this.mExternalSeekbarListener.onStopTrackingTouch(bar);
                }
            }
        };
        this.mRoot = null;
        this.mContext = context;
    }

    public VideoControllerView(Context context) {
        super(context);
        this.mHandler = new MessageHandler(this);
        this.mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: co.vine.android.widget.VideoControllerView.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = true;
                VideoControllerView.this.mHandler.removeMessages(2);
                if (VideoControllerView.this.mExternalSeekbarListener != null) {
                    VideoControllerView.this.mExternalSeekbarListener.onStartTrackingTouch(bar);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
                if (VideoControllerView.this.mPlayerController != null && fromuser) {
                    long duration = VideoControllerView.this.mPlayerController.getDuration();
                    long newposition = (progress * duration) / 1000;
                    VideoControllerView.this.mPlayerController.seekTo((int) newposition);
                    if (VideoControllerView.this.mCurrentTime != null) {
                        VideoControllerView.this.mCurrentTime.setText(Util.stringForTime((int) newposition));
                    }
                    if (VideoControllerView.this.mExternalSeekbarListener != null) {
                        VideoControllerView.this.mExternalSeekbarListener.onProgressChanged(bar, progress, fromuser);
                    }
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar bar) {
                VideoControllerView.this.mDragging = false;
                VideoControllerView.this.setProgress();
                VideoControllerView.this.mHandler.sendEmptyMessage(2);
                if (VideoControllerView.this.mExternalSeekbarListener != null) {
                    VideoControllerView.this.mExternalSeekbarListener.onStopTrackingTouch(bar);
                }
            }
        };
        this.mContext = context;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        if (this.mRoot != null) {
            initControllerView(this.mRoot);
        }
    }

    public void setMediaPlayerController(MediaController.MediaPlayerControl controller) {
        this.mPlayerController = controller;
    }

    public void setSeekbarChangeListener(SeekBar.OnSeekBarChangeListener externalListener) {
        this.mExternalSeekbarListener = externalListener;
    }

    public void togglePlayPause() {
        if (this.mPlayerController.isPlaying()) {
            this.mPlayerController.pause();
        } else {
            this.mPlayerController.start();
            this.mHandler.sendEmptyMessage(2);
        }
    }

    public void setAnchorView(ViewGroup view) {
        this.mAnchor = view;
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(-1, -1);
        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
        FrameLayout.LayoutParams parentParams = new FrameLayout.LayoutParams(-1, -2, 17);
        this.mAnchor.addView(this, parentParams);
        this.mHandler.sendEmptyMessage(2);
    }

    private View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        this.mRoot = inflate.inflate(R.layout.video_controller, (ViewGroup) null);
        initControllerView(this.mRoot);
        return this.mRoot;
    }

    private void initControllerView(View v) {
        this.mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_progress);
        this.mProgress.setOnSeekBarChangeListener(this.mSeekListener);
        this.mProgress.setMax(1000);
        this.mEndTime = (TextView) v.findViewById(R.id.time);
        this.mCurrentTime = (TextView) v.findViewById(R.id.time_current);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setProgress() {
        if (this.mPlayerController == null || this.mDragging) {
            return 0;
        }
        int position = this.mPlayerController.getCurrentPosition();
        int duration = this.mPlayerController.getDuration();
        if (this.mProgress != null) {
            if (duration > 0) {
                long pos = (1000 * position) / duration;
                this.mProgress.setProgress((int) pos);
            }
            int percent = this.mPlayerController.getBufferPercentage();
            this.mProgress.setSecondaryProgress(percent * 10);
        }
        if (this.mEndTime != null) {
            this.mEndTime.setText(Util.stringForTime(duration));
        }
        if (this.mCurrentTime != null) {
            this.mCurrentTime.setText(Util.stringForTime(position));
            return position;
        }
        return position;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public int getSeekThumbXPosition() {
        int availableWidth = (this.mProgress.getWidth() - this.mProgress.getPaddingLeft()) - this.mProgress.getPaddingRight();
        float progressRatio = this.mProgress.getProgress() / this.mProgress.getMax();
        return (int) (this.mProgress.getX() + (availableWidth * progressRatio) + this.mProgress.getPaddingLeft());
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        return false;
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        if (this.mProgress != null) {
            this.mProgress.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(VideoControllerView.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(VideoControllerView.class.getName());
    }

    public void detachFromAnchor() {
        this.mAnchor.removeView(this);
    }

    private static class MessageHandler extends Handler {
        private final WeakReference<VideoControllerView> mView;

        MessageHandler(VideoControllerView view) {
            this.mView = new WeakReference<>(view);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            VideoControllerView view = this.mView.get();
            if (view != null && view.mPlayerController != null) {
                switch (msg.what) {
                    case 2:
                        view.setProgress();
                        if (!view.mDragging && view.mPlayerController.isPlaying()) {
                            Message msg2 = obtainMessage(2);
                            sendMessageDelayed(msg2, 30L);
                            break;
                        }
                        break;
                }
            }
        }
    }
}
