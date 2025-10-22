package co.vine.android.cache.image;

import co.vine.android.network.HttpResult;
import java.util.HashMap;

/* loaded from: classes.dex */
public interface PhotoImagesListener {
    void onPhotoImageError(PhotoImagesCache photoImagesCache, ImageKey imageKey, HttpResult httpResult);

    void onPhotoImageLoaded(PhotoImagesCache photoImagesCache, HashMap<ImageKey, UrlImage> map);
}
