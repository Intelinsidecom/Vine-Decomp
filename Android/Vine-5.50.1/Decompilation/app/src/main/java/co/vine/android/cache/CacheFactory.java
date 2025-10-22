package co.vine.android.cache;

import android.content.Context;
import co.vine.android.cache.image.PhotoImagesCache;
import co.vine.android.cache.text.TextCache;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.client.VineAPI;
import co.vine.android.network.VineNetworkUtils;

/* loaded from: classes.dex */
public final class CacheFactory {
    public static PhotoImagesCache<VineAPI> newImageCache(Context context, int imageSize, int cacheSize, PhotoImagesCache.BlurTool blurTool) {
        return new PhotoImagesCache<>(context, imageSize, cacheSize, blurTool, VineAPI.getInstance(context), VineNetworkUtils.getDefaultNetworkOperationFactory());
    }

    public static TextCache<VineAPI> newTextCache(Context context, VineAPI api, int size) {
        return new TextCache<>(context, size, api, VineNetworkUtils.getDefaultNetworkOperationFactory());
    }

    public static VideoCache<VineAPI> newVideoCache(Context context) {
        return new VideoCache<>(context, VineAPI.getInstance(context), VineNetworkUtils.getDefaultNetworkOperationFactory());
    }
}
