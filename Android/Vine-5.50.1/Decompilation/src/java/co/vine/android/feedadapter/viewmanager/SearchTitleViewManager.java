package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.feedadapter.viewholder.SearchTitleViewHolder;

/* loaded from: classes.dex */
public final class SearchTitleViewManager {
    private Context mContext;
    private View.OnClickListener mViewAllPostsClickListener;

    public SearchTitleViewManager(Context context, View.OnClickListener listener) {
        this.mContext = context;
        this.mViewAllPostsClickListener = listener;
    }

    public void bind(SearchTitleViewHolder h, boolean showSearchHeader) {
        if (showSearchHeader) {
            TextView label = h.getSearchLabelHolder().text;
            label.setText(this.mContext.getString(R.string.posts));
            label.setVisibility(0);
            TextView viewAll = h.getViewAllHolder().text;
            viewAll.setVisibility(0);
            viewAll.setOnClickListener(this.mViewAllPostsClickListener);
            return;
        }
        h.getSearchLabelHolder().text.setVisibility(8);
        h.getViewAllHolder().text.setVisibility(8);
    }
}
