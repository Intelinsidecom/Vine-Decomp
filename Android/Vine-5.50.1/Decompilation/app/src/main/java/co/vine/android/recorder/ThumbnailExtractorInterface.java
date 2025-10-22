package co.vine.android.recorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

/* loaded from: classes.dex */
public interface ThumbnailExtractorInterface {

    public interface OnThumbnailRetrievedListener {
        void onError();

        void onThumbnailRetrieved(long j, Bitmap bitmap);
    }

    void requestThumbnail(long j);

    void start(Context context, Uri uri, int i, OnThumbnailRetrievedListener onThumbnailRetrievedListener);

    void stop();
}
