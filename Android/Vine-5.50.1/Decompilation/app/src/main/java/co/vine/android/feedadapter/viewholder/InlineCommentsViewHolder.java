package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class InlineCommentsViewHolder implements ViewHolder {
    public final TextView comment1;
    public final TextView comment2;
    public final TextView comment3;
    public final View container;
    public final View divider;
    public final TextView viewAll;

    public InlineCommentsViewHolder(View view) {
        this.container = view.findViewById(R.id.comment_list);
        this.divider = view.findViewById(R.id.post_comments_divider);
        this.viewAll = (TextView) view.findViewById(R.id.view_all_comments);
        this.comment1 = (TextView) view.findViewById(R.id.comments_1);
        this.comment2 = (TextView) view.findViewById(R.id.comments_2);
        this.comment3 = (TextView) view.findViewById(R.id.comments_3);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.INLINE_COMMENTS;
    }
}
