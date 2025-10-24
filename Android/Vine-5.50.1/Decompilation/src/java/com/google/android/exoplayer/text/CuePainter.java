package com.google.android.exoplayer.text;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.exoplayer.util.Util;

/* loaded from: classes.dex */
final class CuePainter {
    private boolean applyEmbeddedStyles;
    private int backgroundColor;
    private float bottomPaddingFraction;
    private final float cornerRadius;
    private float cueLine;
    private int cueLineAnchor;
    private int cueLineType;
    private float cuePosition;
    private int cuePositionAnchor;
    private float cueSize;
    private CharSequence cueText;
    private Layout.Alignment cueTextAlignment;
    private int edgeColor;
    private int edgeType;
    private int foregroundColor;
    private final RectF lineBounds = new RectF();
    private final float outlineWidth;
    private final Paint paint;
    private int parentBottom;
    private int parentLeft;
    private int parentRight;
    private int parentTop;
    private final float shadowOffset;
    private final float shadowRadius;
    private final float spacingAdd;
    private final float spacingMult;
    private StaticLayout textLayout;
    private int textLeft;
    private int textPaddingX;
    private final TextPaint textPaint;
    private float textSizePx;
    private int textTop;
    private int windowColor;

    public CuePainter(Context context) {
        int[] viewAttr = {R.attr.lineSpacingExtra, R.attr.lineSpacingMultiplier};
        TypedArray styledAttributes = context.obtainStyledAttributes(null, viewAttr, 0, 0);
        this.spacingAdd = styledAttributes.getDimensionPixelSize(0, 0);
        this.spacingMult = styledAttributes.getFloat(1, 1.0f);
        styledAttributes.recycle();
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int twoDpInPx = Math.round((2.0f * displayMetrics.densityDpi) / 160.0f);
        this.cornerRadius = twoDpInPx;
        this.outlineWidth = twoDpInPx;
        this.shadowRadius = twoDpInPx;
        this.shadowOffset = twoDpInPx;
        this.textPaint = new TextPaint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setSubpixelText(true);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public void draw(Cue cue, boolean applyEmbeddedStyles, CaptionStyleCompat style, float textSizePx, float bottomPaddingFraction, Canvas canvas, int cueBoxLeft, int cueBoxTop, int cueBoxRight, int cueBoxBottom) {
        int textLeft;
        int textRight;
        int textTop;
        int anchorPosition;
        int textLeft2;
        CharSequence cueText = cue.text;
        if (!TextUtils.isEmpty(cueText)) {
            if (!applyEmbeddedStyles) {
                cueText = cueText.toString();
            }
            if (areCharSequencesEqual(this.cueText, cueText) && Util.areEqual(this.cueTextAlignment, cue.textAlignment) && this.cueLine == cue.line && this.cueLineType == cue.lineType && Util.areEqual(Integer.valueOf(this.cueLineAnchor), Integer.valueOf(cue.lineAnchor)) && this.cuePosition == cue.position && Util.areEqual(Integer.valueOf(this.cuePositionAnchor), Integer.valueOf(cue.positionAnchor)) && this.cueSize == cue.size && this.applyEmbeddedStyles == applyEmbeddedStyles && this.foregroundColor == style.foregroundColor && this.backgroundColor == style.backgroundColor && this.windowColor == style.windowColor && this.edgeType == style.edgeType && this.edgeColor == style.edgeColor && Util.areEqual(this.textPaint.getTypeface(), style.typeface) && this.textSizePx == textSizePx && this.bottomPaddingFraction == bottomPaddingFraction && this.parentLeft == cueBoxLeft && this.parentTop == cueBoxTop && this.parentRight == cueBoxRight && this.parentBottom == cueBoxBottom) {
                drawLayout(canvas);
                return;
            }
            this.cueText = cueText;
            this.cueTextAlignment = cue.textAlignment;
            this.cueLine = cue.line;
            this.cueLineType = cue.lineType;
            this.cueLineAnchor = cue.lineAnchor;
            this.cuePosition = cue.position;
            this.cuePositionAnchor = cue.positionAnchor;
            this.cueSize = cue.size;
            this.applyEmbeddedStyles = applyEmbeddedStyles;
            this.foregroundColor = style.foregroundColor;
            this.backgroundColor = style.backgroundColor;
            this.windowColor = style.windowColor;
            this.edgeType = style.edgeType;
            this.edgeColor = style.edgeColor;
            this.textPaint.setTypeface(style.typeface);
            this.textSizePx = textSizePx;
            this.bottomPaddingFraction = bottomPaddingFraction;
            this.parentLeft = cueBoxLeft;
            this.parentTop = cueBoxTop;
            this.parentRight = cueBoxRight;
            this.parentBottom = cueBoxBottom;
            int parentWidth = this.parentRight - this.parentLeft;
            int parentHeight = this.parentBottom - this.parentTop;
            this.textPaint.setTextSize(textSizePx);
            int textPaddingX = (int) ((0.125f * textSizePx) + 0.5f);
            int availableWidth = parentWidth - (textPaddingX * 2);
            if (this.cueSize != Float.MIN_VALUE) {
                availableWidth = (int) (availableWidth * this.cueSize);
            }
            if (availableWidth <= 0) {
                Log.w("CuePainter", "Skipped drawing subtitle cue (insufficient space)");
                return;
            }
            Layout.Alignment textAlignment = this.cueTextAlignment == null ? Layout.Alignment.ALIGN_CENTER : this.cueTextAlignment;
            this.textLayout = new StaticLayout(cueText, this.textPaint, availableWidth, textAlignment, this.spacingMult, this.spacingAdd, true);
            int textHeight = this.textLayout.getHeight();
            int textWidth = 0;
            int lineCount = this.textLayout.getLineCount();
            for (int i = 0; i < lineCount; i++) {
                textWidth = Math.max((int) Math.ceil(this.textLayout.getLineWidth(i)), textWidth);
            }
            int textWidth2 = textWidth + (textPaddingX * 2);
            if (this.cuePosition != Float.MIN_VALUE) {
                int anchorPosition2 = Math.round(parentWidth * this.cuePosition) + this.parentLeft;
                if (this.cuePositionAnchor == 2) {
                    textLeft2 = anchorPosition2 - textWidth2;
                } else {
                    textLeft2 = this.cuePositionAnchor == 1 ? ((anchorPosition2 * 2) - textWidth2) / 2 : anchorPosition2;
                }
                textLeft = Math.max(textLeft2, this.parentLeft);
                textRight = Math.min(textLeft + textWidth2, this.parentRight);
            } else {
                textLeft = (parentWidth - textWidth2) / 2;
                textRight = textLeft + textWidth2;
            }
            if (this.cueLine != Float.MIN_VALUE) {
                if (this.cueLineType == 0) {
                    anchorPosition = Math.round(parentHeight * this.cueLine) + this.parentTop;
                } else {
                    int firstLineHeight = this.textLayout.getLineBottom(0) - this.textLayout.getLineTop(0);
                    if (this.cueLine >= 0.0f) {
                        anchorPosition = Math.round(this.cueLine * firstLineHeight) + this.parentTop;
                    } else {
                        anchorPosition = Math.round(this.cueLine * firstLineHeight) + this.parentBottom;
                    }
                }
                if (this.cueLineAnchor == 2) {
                    textTop = anchorPosition - textHeight;
                } else {
                    textTop = this.cueLineAnchor == 1 ? ((anchorPosition * 2) - textHeight) / 2 : anchorPosition;
                }
                int textBottom = textTop + textHeight;
                if (textBottom > this.parentBottom) {
                    textTop = this.parentBottom - textHeight;
                    int i2 = this.parentBottom;
                } else if (textTop < this.parentTop) {
                    textTop = this.parentTop;
                    int textBottom2 = this.parentTop + textHeight;
                }
            } else {
                textTop = (this.parentBottom - textHeight) - ((int) (parentHeight * bottomPaddingFraction));
                int i3 = textTop + textHeight;
            }
            int textWidth3 = textRight - textLeft;
            this.textLayout = new StaticLayout(cueText, this.textPaint, textWidth3, textAlignment, this.spacingMult, this.spacingAdd, true);
            this.textLeft = textLeft;
            this.textTop = textTop;
            this.textPaddingX = textPaddingX;
            drawLayout(canvas);
        }
    }

    private void drawLayout(Canvas canvas) {
        StaticLayout layout = this.textLayout;
        if (layout != null) {
            int saveCount = canvas.save();
            canvas.translate(this.textLeft, this.textTop);
            if (Color.alpha(this.windowColor) > 0) {
                this.paint.setColor(this.windowColor);
                canvas.drawRect(-this.textPaddingX, 0.0f, layout.getWidth() + this.textPaddingX, layout.getHeight(), this.paint);
            }
            if (Color.alpha(this.backgroundColor) > 0) {
                this.paint.setColor(this.backgroundColor);
                float previousBottom = layout.getLineTop(0);
                int lineCount = layout.getLineCount();
                for (int i = 0; i < lineCount; i++) {
                    this.lineBounds.left = layout.getLineLeft(i) - this.textPaddingX;
                    this.lineBounds.right = layout.getLineRight(i) + this.textPaddingX;
                    this.lineBounds.top = previousBottom;
                    this.lineBounds.bottom = layout.getLineBottom(i);
                    previousBottom = this.lineBounds.bottom;
                    canvas.drawRoundRect(this.lineBounds, this.cornerRadius, this.cornerRadius, this.paint);
                }
            }
            if (this.edgeType == 1) {
                this.textPaint.setStrokeJoin(Paint.Join.ROUND);
                this.textPaint.setStrokeWidth(this.outlineWidth);
                this.textPaint.setColor(this.edgeColor);
                this.textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                layout.draw(canvas);
            } else if (this.edgeType == 2) {
                this.textPaint.setShadowLayer(this.shadowRadius, this.shadowOffset, this.shadowOffset, this.edgeColor);
            } else if (this.edgeType == 3 || this.edgeType == 4) {
                boolean raised = this.edgeType == 3;
                int colorUp = raised ? -1 : this.edgeColor;
                int colorDown = raised ? this.edgeColor : -1;
                float offset = this.shadowRadius / 2.0f;
                this.textPaint.setColor(this.foregroundColor);
                this.textPaint.setStyle(Paint.Style.FILL);
                this.textPaint.setShadowLayer(this.shadowRadius, -offset, -offset, colorUp);
                layout.draw(canvas);
                this.textPaint.setShadowLayer(this.shadowRadius, offset, offset, colorDown);
            }
            this.textPaint.setColor(this.foregroundColor);
            this.textPaint.setStyle(Paint.Style.FILL);
            layout.draw(canvas);
            this.textPaint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            canvas.restoreToCount(saveCount);
        }
    }

    private static boolean areCharSequencesEqual(CharSequence first, CharSequence second) {
        return first == second || (first != null && first.equals(second));
    }
}
