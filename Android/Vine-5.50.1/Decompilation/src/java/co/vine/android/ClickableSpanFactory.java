package co.vine.android;

import android.content.Context;
import android.view.View;
import co.vine.android.widget.VineClickableSpan;

/* loaded from: classes.dex */
public class ClickableSpanFactory {
    protected final int mDefaultColor;

    private ClickableSpanFactory() {
        this.mDefaultColor = 0;
    }

    public ClickableSpanFactory(int defaultColor) {
        this.mDefaultColor = defaultColor;
    }

    public VineClickableSpan newSinglePostClickableSpan(Context context, int color, final long postId) {
        return new VineClickableSpan(context, color, 4) { // from class: co.vine.android.ClickableSpanFactory.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                SingleActivity.start(view.getContext(), postId);
            }
        };
    }
}
