package co.vine.android.widget;

import android.text.TextPaint;

/* loaded from: classes.dex */
public class ColoredTypefacesSpan extends TypefacesSpan {
    private int mColor;

    @Override // co.vine.android.widget.TypefacesSpan, android.text.style.TypefaceSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        ds.setColor(this.mColor);
        ds.setUnderlineText(false);
        super.updateDrawState(ds);
    }
}
