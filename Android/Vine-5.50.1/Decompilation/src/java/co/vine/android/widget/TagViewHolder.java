package co.vine.android.widget;

import android.view.View;
import android.widget.TextView;
import co.vine.android.R;

/* loaded from: classes.dex */
public class TagViewHolder {
    public TextView tagTitle;

    public TagViewHolder(View view) {
        this.tagTitle = (TextView) view.findViewById(R.id.tag_name);
    }
}
