package co.vine.android.widget;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class TightTextView extends TypefacesTextView {
    public TightTextView(Context context) {
        this(context, null, 0);
    }

    public TightTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Layout layout;
        int linesCount;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specModeW = View.MeasureSpec.getMode(widthMeasureSpec);
        if (specModeW != 1073741824 && (linesCount = (layout = getLayout()).getLineCount()) > 1) {
            float textRealMaxWidth = 0.0f;
            for (int n = 0; n < linesCount; n++) {
                textRealMaxWidth = Math.max(textRealMaxWidth, layout.getLineWidth(n));
            }
            int w = Math.round(textRealMaxWidth);
            if (w < getMeasuredWidth()) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(w, Integer.MIN_VALUE), heightMeasureSpec);
            }
        }
    }
}
