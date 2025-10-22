package co.vine.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

/* loaded from: classes.dex */
public class ListenableEditText extends TypefacesEditText {
    private EditTextBackListener mListener;

    public interface EditTextBackListener {
        void onBackPressed();
    }

    public ListenableEditText(Context context) {
        super(context);
    }

    public ListenableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEditTextBackListener(EditTextBackListener listener) {
        this.mListener = listener;
    }

    @Override // co.vine.android.widget.TypefacesEditText, android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() != 4 || event.getAction() != 1 || this.mListener == null) {
            return super.onKeyPreIme(keyCode, event);
        }
        this.mListener.onBackPressed();
        return true;
    }
}
