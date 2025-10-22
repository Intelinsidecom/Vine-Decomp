package co.vine.android.cache.image;

import android.graphics.Bitmap;
import co.vine.android.cache.UrlResource;
import co.vine.android.util.ImageUtils;

/* loaded from: classes.dex */
public class UrlImage extends UrlResource<ImageUtils.BitmapInfo> {
    public Bitmap bitmap;

    /* JADX WARN: Multi-variable type inference failed */
    public UrlImage(String url, ImageUtils.BitmapInfo bitmapInfo) {
        super(url);
        this.value = bitmapInfo;
        if (bitmapInfo != 0) {
            this.bitmap = bitmapInfo.bitmap;
        }
    }

    @Override // co.vine.android.cache.UrlResource
    public int size() {
        if (this.bitmap != null) {
            return this.bitmap.getRowBytes() * this.bitmap.getHeight();
        }
        return 0;
    }
}
