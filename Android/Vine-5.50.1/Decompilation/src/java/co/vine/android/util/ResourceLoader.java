package co.vine.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.image.UrlImage;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.player.SdkVideoView;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class ResourceLoader {
    private final AppController mAppController;
    private final Context mContext;

    public interface ImageSetter {
        View getControllingView();

        void setImage(RecyclableBitmapDrawable recyclableBitmapDrawable);

        void startAnimation(Animation animation);
    }

    public static final class ImageViewImageSetter implements ImageSetter {
        private final ImageView imageView;

        public ImageViewImageSetter(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public void setImage(RecyclableBitmapDrawable drawable) {
            this.imageView.setImageDrawable(drawable);
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public View getControllingView() {
            return this.imageView;
        }

        @Override // co.vine.android.util.ResourceLoader.ImageSetter
        public void startAnimation(Animation animation) {
            this.imageView.startAnimation(animation);
        }
    }

    public ResourceLoader(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    public AppSessionListener setImageWhenLoaded(ImageSetter view, String sourceUrl) {
        return setImageWhenLoaded(view, sourceUrl, 0, 0, false, 0, null, false);
    }

    public AppSessionListener setImageWhenLoadedWithAnimation(ImageSetter view, String sourceUrl, boolean circularCropped, Animation animation, boolean zoomInOnLetterbox) {
        return setImageWhenLoaded(view, sourceUrl, 0, 0, circularCropped, 0, animation, zoomInOnLetterbox);
    }

    public AppSessionListener setImageWhenLoaded(ImageSetter view, String sourceUrl, boolean circularCropped) {
        return setImageWhenLoaded(view, sourceUrl, 0, 0, circularCropped, 0, null, false);
    }

    public AppSessionListener setImageWhenLoaded(ImageSetter view, String sourceUrl, int size, boolean circularCropped) {
        return setImageWhenLoaded(view, sourceUrl, size, 0, circularCropped, 0, null, false);
    }

    public AppSessionListener setImageWhenLoaded(ImageSetter view, String sourceUrl, boolean circularCropped, int edgeRadius, int size) {
        return setImageWhenLoaded(view, sourceUrl, size, 0, circularCropped, edgeRadius, null, false);
    }

    public AppSessionListener setImageWhenLoaded(ImageSetter imageSetter, String sourceUrl, int size, int blurRadius, boolean circularCropped, int radius) {
        return setImageWhenLoaded(imageSetter, sourceUrl, size, blurRadius, circularCropped, radius, null, false);
    }

    public AppSessionListener setImageWhenLoaded(final ImageSetter imageSetter, String sourceUrl, final int size, final int blurRadius, boolean circularCropped, final int radius, final Animation animation, final boolean zoomInOnLetterbox) {
        if (sourceUrl == null) {
            return null;
        }
        final boolean blurred = blurRadius > 0;
        final ImageKey thumbnailImageKey = new ImageKey(sourceUrl, size, size, circularCropped, blurred, blurRadius, false, zoomInOnLetterbox);
        Bitmap bitmap = this.mAppController.getPhotoBitmap(thumbnailImageKey);
        if (bitmap != null && !bitmap.isRecycled()) {
            if (zoomInOnLetterbox && new LetterBoxDetector(bitmap).isLetterBox()) {
                bitmap = zoomInOnLetterbox(bitmap);
            }
            if (blurred) {
                bitmap = RenderscriptUtils.getBlurredBitmap(this.mContext, bitmap, blurRadius, false);
            }
            if (radius != 0) {
                bitmap = ImageUtils.roundBitmapCorners(this.mContext, bitmap, size, size, radius);
            }
            RecyclableBitmapDrawable recyclableBitmap = new RecyclableBitmapDrawable(this.mContext.getResources(), bitmap);
            if (animation != null) {
                imageSetter.startAnimation(animation);
            }
            imageSetter.setImage(recyclableBitmap);
            return null;
        }
        final AppSessionListener sessionListener = new AppSessionListener() { // from class: co.vine.android.util.ResourceLoader.1
            @Override // co.vine.android.client.AppSessionListener
            public void onPhotoImageLoaded(HashMap<ImageKey, UrlImage> images) {
                UrlImage image = images.get(thumbnailImageKey);
                if (image != null && image.isValid()) {
                    Bitmap bitmap2 = image.bitmap;
                    if (zoomInOnLetterbox && new LetterBoxDetector(bitmap2).isLetterBox()) {
                        bitmap2 = ResourceLoader.this.zoomInOnLetterbox(bitmap2);
                    }
                    if (blurred) {
                        bitmap2 = RenderscriptUtils.getBlurredBitmap(ResourceLoader.this.mContext, bitmap2, blurRadius, false);
                    }
                    if (radius != 0) {
                        bitmap2 = ImageUtils.roundBitmapCorners(ResourceLoader.this.mContext, bitmap2, size, size, radius);
                    }
                    RecyclableBitmapDrawable recyclableBitmap2 = new RecyclableBitmapDrawable(ResourceLoader.this.mContext.getResources(), bitmap2);
                    if (animation != null) {
                        imageSetter.startAnimation(animation);
                    }
                    imageSetter.setImage(recyclableBitmap2);
                    ResourceLoader.this.mAppController.removeListener(this);
                }
            }
        };
        this.mAppController.addListener(sessionListener);
        imageSetter.getControllingView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: co.vine.android.util.ResourceLoader.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
                ResourceLoader.this.mAppController.removeListener(sessionListener);
            }
        });
        return sessionListener;
    }

    public AppSessionListener loadVideo(final SdkVideoView view, String sourceUrl, final boolean shouldPlay) throws IllegalStateException {
        if (sourceUrl == null) {
            return null;
        }
        final VideoKey videoKey = new VideoKey(sourceUrl);
        String video = this.mAppController.getVideoFilePath(videoKey);
        if (video != null) {
            view.setVideoPath(video);
            view.seekTo(0);
            if (!shouldPlay) {
                return null;
            }
            view.start();
            return null;
        }
        final AppSessionListener sessionListener = new AppSessionListener() { // from class: co.vine.android.util.ResourceLoader.3
            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) throws IllegalStateException {
                UrlVideo video2 = videos.get(videoKey);
                if (video2 != null && video2.isValid()) {
                    view.setVideoPath(video2.getAbsolutePath());
                    view.seekTo(0);
                    if (shouldPlay) {
                        view.start();
                    }
                    ResourceLoader.this.mAppController.removeListener(this);
                }
            }
        };
        this.mAppController.addListener(sessionListener);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: co.vine.android.util.ResourceLoader.4
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) throws IllegalStateException {
                view.stopPlayback();
                ResourceLoader.this.mAppController.removeListener(sessionListener);
            }
        });
        return sessionListener;
    }

    public void prefetchImage(String sourceUrl, int blurRadius, boolean zoomInOnLetterbox) {
        if (sourceUrl != null) {
            boolean blurred = blurRadius > 0;
            ImageKey thumbnailImageKey = new ImageKey(sourceUrl, 0, 0, false, blurred, blurRadius, false, zoomInOnLetterbox);
            this.mAppController.getPhotoBitmap(thumbnailImageKey);
        }
    }

    public void prefetchImage(String sourceUrl, int blurRadius) {
        prefetchImage(sourceUrl, blurRadius, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap zoomInOnLetterbox(Bitmap bitmap) {
        double orignalWidth = bitmap.getWidth();
        double orignalHeight = bitmap.getHeight();
        return Bitmap.createBitmap(Bitmap.createScaledBitmap(bitmap, (int) (orignalWidth * 1.7777777777777777d), (int) (orignalHeight * 1.7777777777777777d), true), (int) (0.38888888888888884d * orignalWidth), (int) (0.38888888888888884d * orignalHeight), (int) orignalWidth, (int) orignalHeight);
    }
}
