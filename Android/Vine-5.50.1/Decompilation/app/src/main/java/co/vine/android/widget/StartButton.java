package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/* loaded from: classes.dex */
public class StartButton extends Button {
    public StartButton(Context context) {
        super(context);
    }

    public StartButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StartButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTextTypeface(int style, int weight) {
        setTypeface(Typefaces.get(getContext()).getContentTypeface(style, weight));
    }

    @Override // android.widget.TextView
    public int getCompoundPaddingLeft() {
        return 0;
    }
}
