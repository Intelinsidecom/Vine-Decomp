package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.Settings;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class ColorPickerCircle extends ImageView {
    private int mIndex;
    private final Runnable mIndexChangeRunnable;
    private int mNextIndex;
    private boolean mSelected;
    private Drawable mWhiteCircle;

    public ColorPickerCircle(Context context) {
        this(context, null);
    }

    public ColorPickerCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIndexChangeRunnable = new Runnable() { // from class: co.vine.android.widget.ColorPickerCircle.1
            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException {
                ColorPickerCircle.this.setIndex(ColorPickerCircle.this.mNextIndex);
            }
        };
        Resources res = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerCircle, defStyleAttr, 0);
        a.recycle();
        this.mWhiteCircle = res.getDrawable(R.drawable.circle_shape_white);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mSelected) {
            int width = getWidth();
            this.mWhiteCircle.setBounds(width / 4, width / 4, (width * 3) / 4, (width * 3) / 4);
            this.mWhiteCircle.draw(canvas);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        this.mSelected = selected;
        invalidate();
    }

    public void setIndex(int index) throws Resources.NotFoundException {
        this.mIndex = index;
        Drawable d = getResources().getDrawable(R.drawable.circle_shape_light);
        d.setColorFilter(Settings.PROFILE_BACKGROUND_COLORS[index] | ViewCompat.MEASURED_STATE_MASK, PorterDuff.Mode.SRC_ATOP);
        ViewUtil.setBackground(this, d);
        invalidate();
    }

    public int getIndex() {
        return this.mIndex;
    }

    public void postIndexChange(int index) {
        this.mNextIndex = index;
        post(this.mIndexChangeRunnable);
    }
}
