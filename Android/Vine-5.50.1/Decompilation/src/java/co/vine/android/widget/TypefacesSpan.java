package co.vine.android.widget;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/* loaded from: classes.dex */
public class TypefacesSpan extends TypefaceSpan {
    private final Typeface newType;

    public TypefacesSpan(Typeface type) {
        super("");
        this.newType = type;
    }

    public TypefacesSpan(String family, Typeface type) {
        super(family);
        this.newType = type;
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, this.newType);
    }

    @Override // android.text.style.TypefaceSpan, android.text.style.MetricAffectingSpan
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, this.newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        paint.setTypeface(tf);
    }
}
