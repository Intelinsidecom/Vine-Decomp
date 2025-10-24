package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class ColorizedCircleButton extends ImageView {
    private int mCurrentColor;
    private final int mGray;
    private Drawable mIcon;
    private int mNextColor;
    private int mState;

    public ColorizedCircleButton(Context context) {
        this(context, null);
    }

    public ColorizedCircleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorizedCircleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources res = getResources();
        this.mGray = res.getColor(R.color.black_thirty_percent);
        this.mNextColor = this.mGray;
        this.mIcon = getDrawable();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        int newColor = (isPressed() || this.mState == 2) ? this.mNextColor : this.mGray;
        if (newColor != this.mCurrentColor) {
            this.mCurrentColor = newColor;
            this.mIcon.mutate().setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
        }
        super.onDraw(canvas);
    }

    public void setColor(int color) {
        this.mNextColor = color;
        invalidate();
    }

    public void setState(int state) {
        this.mState = state;
    }

    @Override // android.widget.ImageView, android.view.View
    public void setSelected(boolean selected) {
        setState(selected ? 2 : 1);
        super.setSelected(selected);
    }
}
