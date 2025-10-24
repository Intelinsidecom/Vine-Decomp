package co.vine.android.widget;

import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

/* loaded from: classes.dex */
public class VineIMEInputConnection extends BaseInputConnection {
    public static CharSequence GARBAGE_CHARACTER = "\t";
    Editable mEditable;

    public VineIMEInputConnection(View target, boolean mutable) {
        super(target, mutable);
        this.mEditable = null;
    }

    @Override // android.view.inputmethod.BaseInputConnection
    public Editable getEditable() {
        if (Build.VERSION.SDK_INT >= 14) {
            if (this.mEditable == null) {
                this.mEditable = new VineIMEEditable(GARBAGE_CHARACTER);
                Selection.setSelection(this.mEditable, 1);
            } else if (this.mEditable.length() == 0) {
                this.mEditable.append(GARBAGE_CHARACTER);
                Selection.setSelection(this.mEditable, 1);
            }
            return this.mEditable;
        }
        return super.getEditable();
    }

    @Override // android.view.inputmethod.BaseInputConnection, android.view.inputmethod.InputConnection
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        if (Build.VERSION.SDK_INT >= 14 && beforeLength == 1 && afterLength == 0) {
            return super.sendKeyEvent(new KeyEvent(0, 67)) && super.sendKeyEvent(new KeyEvent(1, 67));
        }
        return super.deleteSurroundingText(beforeLength, afterLength);
    }

    public class VineIMEEditable extends SpannableStringBuilder {
        VineIMEEditable(CharSequence source) {
            super(source);
        }

        @Override // android.text.SpannableStringBuilder, android.text.Editable
        public SpannableStringBuilder replace(int spannableStringStart, int spannableStringEnd, CharSequence replacementSequence, int replacementStart, int replacementEnd) {
            if (replacementEnd > replacementStart) {
                super.replace(0, length(), "", 0, 0);
                return super.replace(0, 0, replacementSequence, replacementStart, replacementEnd);
            }
            if (spannableStringEnd > spannableStringStart) {
                super.replace(0, length(), "", 0, 0);
                return super.replace(0, 0, VineIMEInputConnection.GARBAGE_CHARACTER, 0, 1);
            }
            return super.replace(spannableStringStart, spannableStringEnd, replacementSequence, replacementStart, replacementEnd);
        }
    }
}
