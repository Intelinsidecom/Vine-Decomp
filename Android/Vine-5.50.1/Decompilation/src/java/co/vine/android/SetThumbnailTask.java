package co.vine.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import co.vine.android.util.ImageUtils;
import java.io.IOException;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class SetThumbnailTask extends AsyncTask<Uri, Void, Bitmap> {
    private final WeakReference<Fragment> mFragmentRef = null;
    private final WeakReference<android.support.v4.app.Fragment> mSupportFragmentRef;

    public interface SetThumbnailListener {
        void setThumbnailImage(Bitmap bitmap);
    }

    public SetThumbnailTask(android.support.v4.app.Fragment fragment) {
        this.mSupportFragmentRef = new WeakReference<>(fragment);
    }

    @TargetApi(11)
    protected Activity getFragmentActivity() {
        Fragment f = this.mFragmentRef.get();
        if (f == null) {
            return null;
        }
        return f.getActivity();
    }

    protected Activity getSupportFragmentActivity() {
        android.support.v4.app.Fragment f = this.mSupportFragmentRef.get();
        if (f == null) {
            return null;
        }
        return f.getActivity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Bitmap doInBackground(Uri... params) throws IOException {
        Context context = null;
        if (this.mSupportFragmentRef != null) {
            context = getSupportFragmentActivity();
        } else if (this.mFragmentRef != null) {
            context = getFragmentActivity();
        }
        if (context == null) {
            return null;
        }
        Uri photoUri = params[0];
        Resources resources = context.getResources();
        float size = resources.getDimensionPixelSize(R.dimen.user_image_size) * resources.getDisplayMetrics().density;
        Bitmap resizedBitmap = ImageUtils.resizeBitmap(context, photoUri, size, size, 0);
        if (resizedBitmap != null) {
            resizedBitmap = ImageUtils.getCroppedBitmap(resizedBitmap, (int) size);
        }
        if (resizedBitmap != null) {
            return ImageUtils.adjustRotation(context, photoUri, resizedBitmap);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Bitmap bitmap) {
        Object fragment = null;
        if (this.mSupportFragmentRef != null) {
            fragment = this.mSupportFragmentRef.get();
        } else if (this.mFragmentRef != null) {
            fragment = this.mFragmentRef.get();
        }
        if (fragment != null) {
            ((SetThumbnailListener) fragment).setThumbnailImage(bitmap);
        }
    }
}
