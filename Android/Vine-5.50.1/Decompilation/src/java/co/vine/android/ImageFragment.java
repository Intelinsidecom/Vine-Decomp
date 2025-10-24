package co.vine.android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.client.AppSessionListener;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.network.HttpResult;
import co.vine.android.util.Util;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ImageFragment extends BaseControllerFragment implements View.OnTouchListener {
    private long mFirstTouch;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Bitmap mImageBitmap;
    private ImageKey mImageKey;
    private ImageView mImageView;
    private ProgressBar mProgress;

    @Override // co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppSessionListener(new ImageSessionListener());
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_layout, container, false);
        this.mImageView = (ImageView) v.findViewById(R.id.image);
        this.mProgress = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            Bundle args = getArguments();
            String imageUrl = args.getString("image_url");
            if (!TextUtils.isEmpty(imageUrl)) {
                this.mImageKey = new ImageKey(imageUrl);
                Bitmap imageBitmap = this.mAppController.getPhotoBitmap(this.mImageKey);
                if (imageBitmap != null) {
                    this.mImageBitmap = imageBitmap;
                } else {
                    this.mProgress.setVisibility(0);
                }
            }
            this.mImageView.setOnTouchListener(this);
            return;
        }
        this.mImageBitmap = (Bitmap) savedInstanceState.getParcelable("state_image_bitmap");
    }

    @Override // co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Bitmap imageBitmap = this.mImageBitmap;
        if (imageBitmap != null) {
            this.mImageView.setImageDrawable(new RecyclableBitmapDrawable(getResources(), imageBitmap));
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != 1) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (now - this.mFirstTouch < 400) {
            getActivity().finish();
            return true;
        }
        this.mFirstTouch = now;
        return true;
    }

    @Override // android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mImageBitmap != null) {
            outState.putParcelable("state_image_bitmap", this.mImageBitmap);
        }
    }

    private class ImageSessionListener extends AppSessionListener {
        private ImageSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
            UrlImage image = images.get(ImageFragment.this.mImageKey);
            if (image != null && image.isValid()) {
                ImageFragment.this.mProgress.setVisibility(8);
                ImageFragment.this.mImageView.setImageDrawable(new RecyclableBitmapDrawable(ImageFragment.this.mImageView.getResources(), image.bitmap));
            }
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onPhotoImageError(ImageKey key, HttpResult result) {
            if (key.equals(ImageFragment.this.mImageKey)) {
                finishFailed();
            }
        }

        private void finishFailed() {
            ImageFragment.this.mHandler.post(new Runnable() { // from class: co.vine.android.ImageFragment.ImageSessionListener.1
                @Override // java.lang.Runnable
                public void run() {
                    Util.showCenteredToast(ImageFragment.this.getActivity(), R.string.load_image_failure);
                    ImageFragment.this.getActivity().finish();
                }
            });
        }
    }
}
