package co.vine.android.feedadapter.viewmanager;

import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.TextViewHolder;

/* loaded from: classes.dex */
public class TextViewManager implements ViewManager {
    private final ViewType mType;

    public TextViewManager(ViewType type) {
        this.mType = type;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return this.mType;
    }

    public void bind(TextViewHolder h, String description) {
        if (h.text != null) {
            h.text.setText(description);
        }
    }
}
