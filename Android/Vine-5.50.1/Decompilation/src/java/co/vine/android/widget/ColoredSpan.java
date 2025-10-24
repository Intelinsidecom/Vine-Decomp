package co.vine.android.widget;

import android.text.TextPaint;
import android.text.style.CharacterStyle;

/* loaded from: classes.dex */
public class ColoredSpan extends CharacterStyle {
    private int mColor;

    public ColoredSpan(int color) {
        this.mColor = color;
    }

    @Override // android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        ds.setColor(this.mColor);
        ds.setUnderlineText(false);
    }
}
