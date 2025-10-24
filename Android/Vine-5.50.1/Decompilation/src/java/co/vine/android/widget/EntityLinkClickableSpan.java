package co.vine.android.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import co.vine.android.util.LinkDispatcher;
import co.vine.android.util.LinkSuppressor;

/* loaded from: classes.dex */
public class EntityLinkClickableSpan extends VineClickableSpan {
    private String mLink;
    private LinkSuppressor mSuppressor;

    public EntityLinkClickableSpan(Context context, int color, int weight, String link, LinkSuppressor suppressor) {
        super(context, color, weight);
        this.mLink = link;
        this.mSuppressor = suppressor;
    }

    public EntityLinkClickableSpan(Context context, int textWeight, int color, String link) {
        this(context, textWeight, color, link, null);
    }

    public EntityLinkClickableSpan(Context context, int color, String link) {
        this(context, color, 4, link, null);
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) throws PackageManager.NameNotFoundException {
        LinkDispatcher.dispatch(this.mLink, view.getContext(), this.mSuppressor);
    }
}
