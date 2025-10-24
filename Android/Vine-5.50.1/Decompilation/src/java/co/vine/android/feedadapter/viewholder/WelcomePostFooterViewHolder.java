package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class WelcomePostFooterViewHolder implements ViewHolder {
    private final TextViewHolder mDescriptionHolder;

    public WelcomePostFooterViewHolder(View view) {
        this.mDescriptionHolder = new TextViewHolder(view, ViewType.DESCRIPTION, R.id.description);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.POST_FOOTER;
    }

    public TextViewHolder getDescriptionHolder() {
        return this.mDescriptionHolder;
    }
}
