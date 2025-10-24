package co.vine.android.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

@TargetApi(14)
/* loaded from: classes.dex */
public class StaticSizeSurfaceView extends SurfaceView {
    private int mHeight;
    private int mWidth;

    public StaticSizeSurfaceView(Context context) {
        super(context);
        this.mWidth = 0;
        this.mHeight = 0;
    }

    public StaticSizeSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mWidth = 0;
        this.mHeight = 0;
    }

    public StaticSizeSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mWidth = 0;
        this.mHeight = 0;
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }
}
