package co.vine.android.feedadapter.viewmanager;

import android.view.View;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ButtonViewHolder;
import co.vine.android.widget.ColorizedCircleButton;

/* loaded from: classes.dex */
public class PostActionButtonViewManager implements ViewManager {
    private final ViewType mType;

    public PostActionButtonViewManager(ViewType type) {
        this.mType = type;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return this.mType;
    }

    public void bind(ButtonViewHolder h, boolean show, boolean selected, int color) {
        if (h.button != null) {
            h.button.setVisibility(show ? 0 : 4);
            h.button.setSelected(selected);
            if (h.button instanceof ColorizedCircleButton) {
                ((ColorizedCircleButton) h.button).setColor(color);
            }
        }
    }

    public void bindClickListener(ButtonViewHolder holder, View.OnClickListener listener) {
        holder.button.setOnClickListener(listener);
    }
}
