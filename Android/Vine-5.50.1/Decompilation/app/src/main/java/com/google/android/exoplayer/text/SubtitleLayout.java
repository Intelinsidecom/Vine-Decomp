package com.google.android.exoplayer.text;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class SubtitleLayout extends View {
    private boolean applyEmbeddedStyles;
    private float bottomPaddingFraction;
    private List<Cue> cues;
    private final List<CuePainter> painters;
    private CaptionStyleCompat style;
    private float textSize;
    private int textSizeType;

    public SubtitleLayout(Context context) {
        this(context, null);
    }

    public SubtitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.painters = new ArrayList();
        this.textSizeType = 0;
        this.textSize = 0.0533f;
        this.applyEmbeddedStyles = true;
        this.style = CaptionStyleCompat.DEFAULT;
        this.bottomPaddingFraction = 0.08f;
    }

    public void setCues(List<Cue> cues) {
        if (this.cues != cues) {
            this.cues = cues;
            int cueCount = cues == null ? 0 : cues.size();
            while (this.painters.size() < cueCount) {
                this.painters.add(new CuePainter(getContext()));
            }
            invalidate();
        }
    }

    public void setFractionalTextSize(float fractionOfHeight) {
        setFractionalTextSize(fractionOfHeight, false);
    }

    public void setFractionalTextSize(float fractionOfHeight, boolean ignorePadding) {
        setTextSize(ignorePadding ? 1 : 0, fractionOfHeight);
    }

    private void setTextSize(int textSizeType, float textSize) {
        if (this.textSizeType != textSizeType || this.textSize != textSize) {
            this.textSizeType = textSizeType;
            this.textSize = textSize;
            invalidate();
        }
    }

    public void setApplyEmbeddedStyles(boolean applyEmbeddedStyles) {
        if (this.applyEmbeddedStyles != applyEmbeddedStyles) {
            this.applyEmbeddedStyles = applyEmbeddedStyles;
            invalidate();
        }
    }

    public void setStyle(CaptionStyleCompat style) {
        if (this.style != style) {
            this.style = style;
            invalidate();
        }
    }

    public void setBottomPaddingFraction(float bottomPaddingFraction) {
        if (this.bottomPaddingFraction != bottomPaddingFraction) {
            this.bottomPaddingFraction = bottomPaddingFraction;
            invalidate();
        }
    }

    @Override // android.view.View
    public void dispatchDraw(Canvas canvas) {
        float textSizePx;
        int cueCount = this.cues == null ? 0 : this.cues.size();
        int rawTop = getTop();
        int rawBottom = getBottom();
        int left = getLeft() + getPaddingLeft();
        int top = rawTop + getPaddingTop();
        int right = getRight() + getPaddingRight();
        int bottom = rawBottom - getPaddingBottom();
        if (bottom > top && right > left) {
            if (this.textSizeType == 2) {
                textSizePx = this.textSize;
            } else {
                textSizePx = this.textSize * (this.textSizeType == 0 ? bottom - top : rawBottom - rawTop);
            }
            if (textSizePx > 0.0f) {
                for (int i = 0; i < cueCount; i++) {
                    this.painters.get(i).draw(this.cues.get(i), this.applyEmbeddedStyles, this.style, textSizePx, this.bottomPaddingFraction, canvas, left, top, right, bottom);
                }
            }
        }
    }
}
