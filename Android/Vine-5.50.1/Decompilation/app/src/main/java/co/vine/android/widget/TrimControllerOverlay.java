package co.vine.android.widget;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import co.vine.android.widget.TrimTimeBar;

/* loaded from: classes.dex */
public class TrimControllerOverlay extends FrameLayout implements View.OnClickListener, TrimTimeBar.Listener {
    private OverlayListener mListener;
    private final ImageView mPlayButton;
    private State mState;
    private TrimTimeBar mTimeBar;
    private int mVideoViewHeight;

    public interface OverlayListener {
        void onPlayPause();

        void onReplay();

        void onSeekEnd(int i, int i2, int i3);

        void onSeekMove(int i);

        void onSeekStart();
    }

    protected enum State {
        PLAYING,
        PAUSED,
        ENDED,
        ERROR,
        LOADING
    }

    public void setListener(OverlayListener listener) {
        this.mListener = listener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mListener != null && view == this.mPlayButton) {
            if (this.mState == State.ENDED) {
                this.mListener.onReplay();
            } else if (this.mState == State.PAUSED || this.mState == State.PLAYING) {
                this.mListener.onPlayPause();
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            switch (event.getAction()) {
                case 0:
                    if (this.mState == State.PLAYING || this.mState == State.PAUSED) {
                        this.mListener.onPlayPause();
                    } else if (this.mState == State.ENDED) {
                        this.mListener.onReplay();
                    }
                    break;
                default:
                    return true;
            }
        }
        return true;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = this.mVideoViewHeight > 0 ? View.MeasureSpec.makeMeasureSpec(this.mVideoViewHeight, 1073741824) : heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int w = right - left;
        int y = top + this.mVideoViewHeight;
        this.mTimeBar.layout(0, y - this.mTimeBar.getPreferredHeight(), w, y);
        View playView = this.mPlayButton;
        int centeredLeft = (w - playView.getMeasuredWidth()) / 2;
        int centeredTop = (y - playView.getMeasuredHeight()) / 2;
        int centeredRight = centeredLeft + playView.getMeasuredWidth();
        int centeredBottom = centeredTop + playView.getMeasuredHeight();
        playView.layout(centeredLeft, centeredTop, centeredRight, centeredBottom);
    }

    @Override // co.vine.android.widget.TrimTimeBar.Listener
    public void onScrubbingStart() {
        this.mListener.onSeekStart();
    }

    @Override // co.vine.android.widget.TrimTimeBar.Listener
    public void onScrubbingMove(int time) {
        this.mListener.onSeekMove(time);
    }

    @Override // co.vine.android.widget.TrimTimeBar.Listener
    public void onScrubbingEnd(int time, int trimStartTime, int trimEndTime) {
        this.mListener.onSeekEnd(time, trimStartTime, trimEndTime);
    }
}
