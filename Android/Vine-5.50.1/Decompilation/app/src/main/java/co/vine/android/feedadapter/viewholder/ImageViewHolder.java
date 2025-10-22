package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import co.vine.android.client.AppSessionListener;
import co.vine.android.feedadapter.v2.ViewType;

/* loaded from: classes.dex */
public class ImageViewHolder implements ViewHolder {
    public final ImageView image;
    public AppSessionListener imageListener;
    private final ViewType mType;

    public ImageViewHolder(View view, ViewType type, int resId) {
        this.mType = type;
        this.image = (ImageView) view.findViewById(resId);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }
}
