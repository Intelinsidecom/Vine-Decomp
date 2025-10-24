package co.vine.android.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import co.vine.android.widget.TypefacesEditText;

/* loaded from: classes.dex */
public class VMRecipientsEditText extends TypefacesEditText {
    public VMRecipientsEditText(Context context) {
        super(context);
    }

    public VMRecipientsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VMRecipientsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override // co.vine.android.widget.TypefacesEditText, android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.actionLabel = null;
        outAttrs.inputType = 524432;
        outAttrs.imeOptions = 6;
        VMRecipientsIMEInputConnection connection = new VMRecipientsIMEInputConnection(this, false);
        return connection;
    }

    private class VMRecipientsIMEInputConnection extends VineIMEInputConnection {
        public VMRecipientsIMEInputConnection(View target, boolean mutable) {
            super(target, mutable);
        }

        @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
        public boolean sendKeyEvent(KeyEvent event) {
            TypefacesEditText.KeyboardListener keyboardListener = VMRecipientsEditText.this.getKeyboardListener();
            return (keyboardListener != null && keyboardListener.sendKeyEvent(event)) || super.sendKeyEvent(event);
        }

        @Override // co.vine.android.widget.VineIMEInputConnection, android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (Build.VERSION.SDK_INT >= 14 && beforeLength == 1 && afterLength == 0) {
                boolean upResult = sendKeyEvent(new KeyEvent(0, 67));
                boolean downResult = sendKeyEvent(new KeyEvent(1, 67));
                return upResult && downResult;
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }
}
