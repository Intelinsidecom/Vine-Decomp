package co.vine.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import co.vine.android.R;
import co.vine.android.views.SdkEditText;

/* loaded from: classes.dex */
public class TypefacesEditText extends SdkEditText {
    private KeyboardListener mKeyboardListener;
    private int mWeight;

    public interface KeyboardListener {
        void onKeyboardDismissed();

        boolean sendKeyEvent(KeyEvent keyEvent);
    }

    public TypefacesEditText(Context context) {
        super(context);
        this.mKeyboardListener = null;
    }

    public TypefacesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mKeyboardListener = null;
        init(context, attrs);
    }

    public TypefacesEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mKeyboardListener = null;
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VineTextView, R.attr.vineEditTextStyle, 0);
        int style = a.getInt(1, 0);
        this.mWeight = a.getInt(0, 2);
        a.recycle();
        setTypeface(Typefaces.get(context).getContentTypeface(style, this.mWeight), style);
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typefaces.get(getContext()).getContentTypeface(style, this.mWeight));
    }

    public void setKeyboardListener(KeyboardListener listener) {
        this.mKeyboardListener = listener;
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent keyEvent) {
        if (keyCode == 4 && this.mKeyboardListener != null) {
            this.mKeyboardListener.onKeyboardDismissed();
        }
        return super.onKeyPreIme(keyCode, keyEvent);
    }

    public KeyboardListener getKeyboardListener() {
        return this.mKeyboardListener;
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new TypeFacesInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    protected class TypeFacesInputConnection extends InputConnectionWrapper {
        public TypeFacesInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean sendKeyEvent(KeyEvent event) {
            return (TypefacesEditText.this.mKeyboardListener != null && TypefacesEditText.this.mKeyboardListener.sendKeyEvent(event)) || super.sendKeyEvent(event);
        }
    }
}
