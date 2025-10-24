package co.vine.android.widget.trimcontrols;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import co.vine.android.R;

/* loaded from: classes.dex */
public class GripTrack extends View {
    private Paint mGripBgPaint;
    private RectF mGripHandle;
    private final int mGripHandleHeight;
    private Paint mGripHandlePaint;
    private final int mGripHandleWidth;
    private GripTrackTouchListener mGripTrackTouchListener;
    private Rect mLeftRect;
    private Rect mRightRect;

    public interface GripTrackTouchListener {
        boolean onGripTouchEvent(MotionEvent motionEvent);
    }

    public GripTrack(Context context, GripTrackTouchListener listener) {
        super(context);
        this.mGripBgPaint = new Paint();
        this.mGripHandlePaint = new Paint();
        this.mLeftRect = new Rect();
        this.mRightRect = new Rect();
        this.mGripHandle = new RectF();
        Resources res = getResources();
        this.mGripBgPaint.setColor(res.getColor(R.color.gripTrackBackground));
        this.mGripHandlePaint.setColor(res.getColor(R.color.white_forty_percent));
        this.mGripHandleWidth = res.getDimensionPixelSize(R.dimen.grip_handle_width);
        this.mGripHandleHeight = res.getDimensionPixelSize(R.dimen.grip_handle_height);
        this.mGripTrackTouchListener = listener;
    }

    public void setBoundaries(int leftPx, int rightPx) {
        this.mLeftRect.left = 0;
        this.mLeftRect.right = leftPx;
        this.mLeftRect.top = 0;
        this.mLeftRect.bottom = getBottom();
        this.mRightRect.left = rightPx;
        this.mRightRect.right = getWidth();
        this.mRightRect.top = 0;
        this.mRightRect.bottom = getBottom();
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mRightRect.contains((int) event.getX(), (int) event.getY())) {
            return this.mGripTrackTouchListener.onGripTouchEvent(event);
        }
        return false;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(this.mLeftRect, this.mGripBgPaint);
        canvas.drawRect(this.mRightRect, this.mGripBgPaint);
        int gripHandleTop = (getHeight() - this.mGripHandleHeight) / 2;
        this.mGripHandle.set(this.mLeftRect.right - (this.mGripHandleWidth * 5), gripHandleTop, this.mLeftRect.right - (this.mGripHandleWidth * 4), this.mGripHandleHeight + gripHandleTop);
        canvas.drawRoundRect(this.mGripHandle, this.mGripHandleWidth / 2, this.mGripHandleWidth / 2, this.mGripHandlePaint);
        this.mGripHandle.offset(this.mGripHandleWidth * 2, 0.0f);
        canvas.drawRoundRect(this.mGripHandle, this.mGripHandleWidth / 2, this.mGripHandleWidth / 2, this.mGripHandlePaint);
    }
}
