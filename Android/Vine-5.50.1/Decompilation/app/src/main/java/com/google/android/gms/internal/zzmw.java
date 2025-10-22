package com.google.android.gms.internal;

import android.graphics.Canvas;
import android.graphics.Path;
import android.widget.ImageView;

/* loaded from: classes2.dex */
public final class zzmw extends ImageView {
    private int zzaiL;
    private zza zzaiM;
    private int zzaiN;
    private float zzaiO;

    public interface zza {
        Path zzl(int i, int i2);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.zzaiM != null) {
            canvas.clipPath(this.zzaiM.zzl(getWidth(), getHeight()));
        }
        super.onDraw(canvas);
        if (this.zzaiL != 0) {
            canvas.drawColor(this.zzaiL);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth;
        int measuredHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        switch (this.zzaiN) {
            case 1:
                measuredHeight = getMeasuredHeight();
                measuredWidth = (int) (measuredHeight * this.zzaiO);
                break;
            case 2:
                measuredWidth = getMeasuredWidth();
                measuredHeight = (int) (measuredWidth / this.zzaiO);
                break;
            default:
                return;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
