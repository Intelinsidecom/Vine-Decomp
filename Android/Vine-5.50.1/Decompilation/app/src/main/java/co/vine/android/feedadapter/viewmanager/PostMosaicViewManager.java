package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.widget.ImageView;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.PostMosaicViewHolder;
import co.vine.android.util.ResourceLoader;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PostMosaicViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;

    public PostMosaicViewManager(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_MOSAIC;
    }

    public void bind(PostMosaicViewHolder h, ArrayList<String> thumbnailUrls) {
        if (thumbnailUrls != null) {
            ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
            int thumbnailCount = h.getThumbnailCount();
            for (int i = 0; i < thumbnailCount; i++) {
                ImageView thumbnail = h.thumbnails.get(i);
                if (thumbnail != null && thumbnailUrls.size() > i) {
                    bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(thumbnail), thumbnailUrls.get(i));
                }
            }
        }
    }
}
