package co.vine.android.widget;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/* loaded from: classes.dex */
public abstract class VineClickableSpan extends ClickableSpan {
    private int mColor;
    private Context mContext;
    private int mWeight;

    public VineClickableSpan(int color) {
        this.mColor = color;
        this.mWeight = 0;
    }

    public VineClickableSpan(Context context, int color, int weight) {
        this.mContext = context;
        this.mColor = color;
        this.mWeight = weight;
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint ds) {
        ds.linkColor = this.mColor;
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        if (this.mContext != null && this.mWeight != 0) {
            ds.setTypeface(Typefaces.get(this.mContext).getContentTypeface(0, this.mWeight));
        }
    }
}
