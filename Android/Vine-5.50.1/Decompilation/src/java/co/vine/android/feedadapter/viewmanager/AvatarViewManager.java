package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.content.res.Resources;
import co.vine.android.R;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ImageViewHolder;
import co.vine.android.util.ResourceLoader;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class AvatarViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;
    private final int mVineGreen;

    public AvatarViewManager(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
        Resources res = this.mContext.getResources();
        this.mVineGreen = res.getColor(R.color.vine_green);
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.AVATAR;
    }

    public void bind(ImageViewHolder h, String url, boolean revined, int color) {
        bind(h, url, revined, color, false);
    }

    public void bind(ImageViewHolder h, String url, boolean revined, int color, boolean circleCropped) {
        if (h.image != null && url != null && !"".equals(url)) {
            h.image.setImageResource(R.drawable.circle_shape_light);
            if (h.imageListener != null) {
                this.mAppController.removeListener(h.imageListener);
            }
            if (color == 0) {
                color = this.mVineGreen;
            }
            if (Util.isDefaultAvatarUrl(url)) {
                if (revined) {
                    Util.safeSetDefaultAvatar(h.image, Util.ProfileImageSize.MEDIUM, this.mVineGreen);
                    return;
                } else {
                    Util.safeSetDefaultAvatar(h.image, Util.ProfileImageSize.MEDIUM, color);
                    return;
                }
            }
            h.image.clearColorFilter();
            ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
            h.imageListener = bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(h.image), url, Util.getUserImageSize(this.mContext.getResources()), true);
        }
    }
}
