package co.vine.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/* loaded from: classes.dex */
public class SdkTextView extends TextView {
    public SdkTextView(Context context) {
        super(context);
    }

    public SdkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SdkTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (IndexOutOfBoundsException | VerifyError e) {
            return false;
        }
    }
}
