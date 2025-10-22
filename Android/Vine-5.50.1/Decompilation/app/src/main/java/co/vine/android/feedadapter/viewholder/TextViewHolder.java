package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.widget.TextView;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class TextViewHolder implements ViewHolder {
    private final ViewType mType;
    public final TextView text;

    public TextViewHolder(View view, ViewType type, int resId) {
        this.mType = type;
        this.text = (TextView) view.findViewById(resId);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }
}
