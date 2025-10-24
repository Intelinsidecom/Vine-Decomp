package co.vine.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.InputDeviceCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class FlowLayout extends ViewGroup {
    private boolean debugDraw;
    private int horizontalSpacing;
    private int orientation;
    private int verticalSpacing;

    public FlowLayout(Context context) {
        super(context);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
        this.orientation = 0;
        this.debugDraw = false;
        readStyleParameters(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
        this.orientation = 0;
        this.debugDraw = false;
        readStyleParameters(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
        this.orientation = 0;
        this.debugDraw = false;
        readStyleParameters(context, attributeSet);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int mode;
        int controlMaxLength;
        int controlMaxThickness;
        int childLength;
        int childThickness;
        int spacingLength;
        int spacingThickness;
        int posX;
        int posY;
        int sizeWidth = (View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight()) - getPaddingLeft();
        int sizeHeight = (View.MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()) - getPaddingBottom();
        int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);
        if (this.orientation == 0) {
            size = sizeWidth;
            mode = modeWidth;
        } else {
            size = sizeHeight;
            mode = modeHeight;
        }
        int lineThicknessWithSpacing = 0;
        int lineThickness = 0;
        int lineLengthWithSpacing = 0;
        int prevLinePosition = 0;
        int controlMaxLength2 = 0;
        int controlMaxThickness2 = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width), getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height));
                int hSpacing = getHorizontalSpacing(lp);
                int vSpacing = getVerticalSpacing(lp);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (this.orientation == 0) {
                    childLength = childWidth;
                    childThickness = childHeight;
                    spacingLength = hSpacing;
                    spacingThickness = vSpacing;
                } else {
                    childLength = childHeight;
                    childThickness = childWidth;
                    spacingLength = vSpacing;
                    spacingThickness = hSpacing;
                }
                int lineLength = lineLengthWithSpacing + childLength;
                lineLengthWithSpacing = lineLength + spacingLength;
                boolean newLine = lp.newLine || (mode != 0 && lineLength > size);
                if (newLine) {
                    prevLinePosition += lineThicknessWithSpacing;
                    lineThickness = childThickness;
                    lineLength = childLength;
                    lineThicknessWithSpacing = childThickness + spacingThickness;
                    lineLengthWithSpacing = lineLength + spacingLength;
                }
                lineThicknessWithSpacing = Math.max(lineThicknessWithSpacing, childThickness + spacingThickness);
                lineThickness = Math.max(lineThickness, childThickness);
                if (this.orientation == 0) {
                    posX = (getPaddingLeft() + lineLength) - childLength;
                    posY = getPaddingTop() + prevLinePosition;
                } else {
                    posX = getPaddingLeft() + prevLinePosition;
                    posY = (getPaddingTop() + lineLength) - childHeight;
                }
                lp.setPosition(posX, posY);
                controlMaxLength2 = Math.max(controlMaxLength2, lineLength);
                controlMaxThickness2 = prevLinePosition + lineThickness;
            }
        }
        if (this.orientation == 0) {
            controlMaxLength = controlMaxLength2 + getPaddingLeft() + getPaddingRight();
            controlMaxThickness = controlMaxThickness2 + getPaddingBottom() + getPaddingTop();
        } else {
            controlMaxLength = controlMaxLength2 + getPaddingBottom() + getPaddingTop();
            controlMaxThickness = controlMaxThickness2 + getPaddingLeft() + getPaddingRight();
        }
        if (this.orientation == 0) {
            setMeasuredDimension(resolveSize(controlMaxLength, widthMeasureSpec), resolveSize(controlMaxThickness, heightMeasureSpec));
        } else {
            setMeasuredDimension(resolveSize(controlMaxThickness, widthMeasureSpec), resolveSize(controlMaxLength, heightMeasureSpec));
        }
    }

    private int getVerticalSpacing(LayoutParams lp) {
        if (!lp.verticalSpacingSpecified()) {
            int vSpacing = this.verticalSpacing;
            return vSpacing;
        }
        int vSpacing2 = lp.verticalSpacing;
        return vSpacing2;
    }

    private int getHorizontalSpacing(LayoutParams lp) {
        if (!lp.horizontalSpacingSpecified()) {
            int hSpacing = this.horizontalSpacing;
            return hSpacing;
        }
        int hSpacing2 = lp.horizontalSpacing;
        return hSpacing2;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        drawDebugInfo(canvas, child);
        return more;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
        this.orientation = 0;
        this.debugDraw = false;
    }

    private void drawDebugInfo(Canvas canvas, View child) {
        if (this.debugDraw) {
            Paint childPaint = createPaint(InputDeviceCompat.SOURCE_ANY);
            Paint layoutPaint = createPaint(-16711936);
            Paint newLinePaint = createPaint(-65536);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.horizontalSpacing > 0) {
                float x = child.getRight();
                float y = child.getTop() + (child.getHeight() / 2.0f);
                canvas.drawLine(x, y, x + lp.horizontalSpacing, y, childPaint);
                canvas.drawLine((lp.horizontalSpacing + x) - 4.0f, y - 4.0f, x + lp.horizontalSpacing, y, childPaint);
                canvas.drawLine((lp.horizontalSpacing + x) - 4.0f, y + 4.0f, x + lp.horizontalSpacing, y, childPaint);
            } else if (this.horizontalSpacing > 0) {
                float x2 = child.getRight();
                float y2 = child.getTop() + (child.getHeight() / 2.0f);
                canvas.drawLine(x2, y2, x2 + this.horizontalSpacing, y2, layoutPaint);
                canvas.drawLine((this.horizontalSpacing + x2) - 4.0f, y2 - 4.0f, x2 + this.horizontalSpacing, y2, layoutPaint);
                canvas.drawLine((this.horizontalSpacing + x2) - 4.0f, y2 + 4.0f, x2 + this.horizontalSpacing, y2, layoutPaint);
            }
            if (lp.verticalSpacing > 0) {
                float x3 = child.getLeft() + (child.getWidth() / 2.0f);
                float y3 = child.getBottom();
                canvas.drawLine(x3, y3, x3, y3 + lp.verticalSpacing, childPaint);
                canvas.drawLine(x3 - 4.0f, (lp.verticalSpacing + y3) - 4.0f, x3, y3 + lp.verticalSpacing, childPaint);
                canvas.drawLine(x3 + 4.0f, (lp.verticalSpacing + y3) - 4.0f, x3, y3 + lp.verticalSpacing, childPaint);
            } else if (this.verticalSpacing > 0) {
                float x4 = child.getLeft() + (child.getWidth() / 2.0f);
                float y4 = child.getBottom();
                canvas.drawLine(x4, y4, x4, y4 + this.verticalSpacing, layoutPaint);
                canvas.drawLine(x4 - 4.0f, (this.verticalSpacing + y4) - 4.0f, x4, y4 + this.verticalSpacing, layoutPaint);
                canvas.drawLine(x4 + 4.0f, (this.verticalSpacing + y4) - 4.0f, x4, y4 + this.verticalSpacing, layoutPaint);
            }
            if (lp.newLine) {
                if (this.orientation == 0) {
                    float x5 = child.getLeft();
                    float y5 = child.getTop() + (child.getHeight() / 2.0f);
                    canvas.drawLine(x5, y5 - 6.0f, x5, y5 + 6.0f, newLinePaint);
                } else {
                    float x6 = child.getLeft() + (child.getWidth() / 2.0f);
                    float y6 = child.getTop();
                    canvas.drawLine(x6 - 6.0f, y6, x6 + 6.0f, y6, newLinePaint);
                }
            }
        }
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(2.0f);
        return paint;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private static int NO_SPACING = -1;
        private int horizontalSpacing;
        private boolean newLine;
        private int verticalSpacing;
        private int x;
        private int y;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.horizontalSpacing = NO_SPACING;
            this.verticalSpacing = NO_SPACING;
            this.newLine = false;
            readStyleParameters(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.horizontalSpacing = NO_SPACING;
            this.verticalSpacing = NO_SPACING;
            this.newLine = false;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.horizontalSpacing = NO_SPACING;
            this.verticalSpacing = NO_SPACING;
            this.newLine = false;
        }

        public boolean horizontalSpacingSpecified() {
            return this.horizontalSpacing != NO_SPACING;
        }

        public boolean verticalSpacingSpecified() {
            return this.verticalSpacing != NO_SPACING;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private void readStyleParameters(Context context, AttributeSet attributeSet) {
            this.horizontalSpacing = NO_SPACING;
            this.verticalSpacing = NO_SPACING;
            this.newLine = false;
        }
    }
}
