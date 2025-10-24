package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.widget.ImageView;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.UserMosaicViewHolder;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class UserMosaicViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;

    public UserMosaicViewManager(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.USER_MOSAIC;
    }

    public void bind(UserMosaicViewHolder h, ArrayList<String> avatarUrls) {
        if (avatarUrls != null) {
            ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
            int thumbnailCount = h.getThumbnailCount();
            for (int i = 0; i < thumbnailCount; i++) {
                ImageView thumbnail = h.thumbnails.get(i);
                if (thumbnail != null && avatarUrls.size() > i) {
                    bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), avatarUrls.get(i), true);
                }
            }
        }
    }
}
