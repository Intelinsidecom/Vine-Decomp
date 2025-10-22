package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public final class BylineViewHolder implements ViewHolder {
    public final View container;
    public final ImageView gearIcon;
    public final ImageView icon;
    public final TextView text;

    public BylineViewHolder(View view) {
        this.container = view.findViewById(R.id.revine_and_byline_container);
        this.icon = (ImageView) view.findViewById(R.id.byline_icon);
        this.text = (TextView) view.findViewById(R.id.revine_line_text);
        this.gearIcon = (ImageView) view.findViewById(R.id.byline_icon_gear);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.BYLINE;
    }
}
