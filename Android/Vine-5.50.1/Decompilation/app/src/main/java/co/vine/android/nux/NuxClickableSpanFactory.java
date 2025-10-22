package co.vine.android.nux;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import co.vine.android.ClickableSpanFactory;
import co.vine.android.WebViewActivity;
import co.vine.android.widget.VineClickableSpan;

/* loaded from: classes.dex */
public class NuxClickableSpanFactory extends ClickableSpanFactory {
    public NuxClickableSpanFactory(int color) {
        super(color);
    }

    public VineClickableSpan newVinePrivacyPolicyClickableSpan() {
        return new VineClickableSpan(this.mDefaultColor) { // from class: co.vine.android.nux.NuxClickableSpanFactory.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, (Class<?>) WebViewActivity.class);
                intent.putExtra("type", 1);
                context.startActivity(intent);
            }
        };
    }

    public VineClickableSpan newVineTermsOfServiceClickableSpan() {
        return new VineClickableSpan(this.mDefaultColor) { // from class: co.vine.android.nux.NuxClickableSpanFactory.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, (Class<?>) WebViewActivity.class);
                intent.putExtra("type", 2);
                context.startActivity(intent);
            }
        };
    }
}
