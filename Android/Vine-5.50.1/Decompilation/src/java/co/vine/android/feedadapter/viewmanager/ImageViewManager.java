package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ImageViewHolder;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public class ImageViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;
    private final ViewType mType;

    public ImageViewManager(Context context, AppController appController, ViewType type) {
        this.mContext = context;
        this.mAppController = appController;
        this.mType = type;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return this.mType;
    }

    public void bind(ImageViewHolder h, String url) {
        bind(h, url, 0, 0, false);
    }

    public void bind(ImageViewHolder h, String url, int blurRadius) {
        bind(h, url, 0, blurRadius, false);
    }

    public void bind(ImageViewHolder h, String url, int size, int blurRadius, boolean circularCropped) {
        if (h.image != null && url != null && !"".equals(url)) {
            ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
            bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(h.image), url, size, blurRadius, circularCropped, 0);
        }
    }
}
