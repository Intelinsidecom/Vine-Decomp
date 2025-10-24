package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class ButtonViewHolder implements ViewHolder {
    public final View button;
    private final ViewType mType;

    public ButtonViewHolder(View view, ViewType type, int resId) {
        this.mType = type;
        this.button = view.findViewById(resId);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }
}
