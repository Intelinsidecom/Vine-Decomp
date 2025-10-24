package com.facebook.login.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.facebook.FacebookException;
import com.facebook.LoggingBehavior;
import com.facebook.R;
import com.facebook.internal.ImageDownloader;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.ImageResponse;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.googlecode.javacv.cpp.opencv_core;

/* loaded from: classes2.dex */
public class ProfilePictureView extends FrameLayout {
    public static final String TAG = ProfilePictureView.class.getSimpleName();
    private Bitmap customizedDefaultProfilePicture;
    private ImageView image;
    private Bitmap imageContents;
    private boolean isCropped;
    private ImageRequest lastRequest;
    private OnErrorListener onErrorListener;
    private int presetSizeType;
    private String profileId;
    private int queryHeight;
    private int queryWidth;

    public interface OnErrorListener {
        void onError(FacebookException facebookException);
    }

    public ProfilePictureView(Context context) {
        super(context);
        this.queryHeight = 0;
        this.queryWidth = 0;
        this.isCropped = true;
        this.presetSizeType = -1;
        this.customizedDefaultProfilePicture = null;
        initialize(context);
    }

    public ProfilePictureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.queryHeight = 0;
        this.queryWidth = 0;
        this.isCropped = true;
        this.presetSizeType = -1;
        this.customizedDefaultProfilePicture = null;
        initialize(context);
        parseAttributes(attrs);
    }

    public ProfilePictureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.queryHeight = 0;
        this.queryWidth = 0;
        this.isCropped = true;
        this.presetSizeType = -1;
        this.customizedDefaultProfilePicture = null;
        initialize(context);
        parseAttributes(attrs);
    }

    public final int getPresetSize() {
        return this.presetSizeType;
    }

    public final void setPresetSize(int sizeType) {
        switch (sizeType) {
            case opencv_core.CV_StsNoMem /* -4 */:
            case opencv_core.CV_StsInternal /* -3 */:
            case -2:
            case -1:
                this.presetSizeType = sizeType;
                requestLayout();
                return;
            default:
                throw new IllegalArgumentException("Must use a predefined preset size");
        }
    }

    public final boolean isCropped() {
        return this.isCropped;
    }

    public final void setCropped(boolean showCroppedVersion) {
        this.isCropped = showCroppedVersion;
        refreshImage(false);
    }

    public final String getProfileId() {
        return this.profileId;
    }

    public final void setProfileId(String profileId) {
        boolean force = false;
        if (Utility.isNullOrEmpty(this.profileId) || !this.profileId.equalsIgnoreCase(profileId)) {
            setBlankProfilePicture();
            force = true;
        }
        this.profileId = profileId;
        refreshImage(force);
    }

    public final OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public final void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public final void setDefaultProfilePicture(Bitmap inputBitmap) {
        this.customizedDefaultProfilePicture = inputBitmap;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams params = getLayoutParams();
        boolean customMeasure = false;
        int newHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int newWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) != 1073741824 && params.height == -2) {
            newHeight = getPresetSizeInPixels(true);
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(newHeight, 1073741824);
            customMeasure = true;
        }
        if (View.MeasureSpec.getMode(widthMeasureSpec) != 1073741824 && params.width == -2) {
            newWidth = getPresetSizeInPixels(true);
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(newWidth, 1073741824);
            customMeasure = true;
        }
        if (customMeasure) {
            setMeasuredDimension(newWidth, newHeight);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        refreshImage(false);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle instanceState = new Bundle();
        instanceState.putParcelable("ProfilePictureView_superState", superState);
        instanceState.putString("ProfilePictureView_profileId", this.profileId);
        instanceState.putInt("ProfilePictureView_presetSize", this.presetSizeType);
        instanceState.putBoolean("ProfilePictureView_isCropped", this.isCropped);
        instanceState.putParcelable("ProfilePictureView_bitmap", this.imageContents);
        instanceState.putInt("ProfilePictureView_width", this.queryWidth);
        instanceState.putInt("ProfilePictureView_height", this.queryHeight);
        instanceState.putBoolean("ProfilePictureView_refresh", this.lastRequest != null);
        return instanceState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable state) {
        if (state.getClass() != Bundle.class) {
            super.onRestoreInstanceState(state);
            return;
        }
        Bundle instanceState = (Bundle) state;
        super.onRestoreInstanceState(instanceState.getParcelable("ProfilePictureView_superState"));
        this.profileId = instanceState.getString("ProfilePictureView_profileId");
        this.presetSizeType = instanceState.getInt("ProfilePictureView_presetSize");
        this.isCropped = instanceState.getBoolean("ProfilePictureView_isCropped");
        this.queryWidth = instanceState.getInt("ProfilePictureView_width");
        this.queryHeight = instanceState.getInt("ProfilePictureView_height");
        setImageBitmap((Bitmap) instanceState.getParcelable("ProfilePictureView_bitmap"));
        if (instanceState.getBoolean("ProfilePictureView_refresh")) {
            refreshImage(true);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.lastRequest = null;
    }

    private void initialize(Context context) {
        removeAllViews();
        this.image = new ImageView(context);
        FrameLayout.LayoutParams imageLayout = new FrameLayout.LayoutParams(-1, -1);
        this.image.setLayoutParams(imageLayout);
        this.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(this.image);
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.com_facebook_profile_picture_view);
        setPresetSize(a.getInt(R.styleable.com_facebook_profile_picture_view_com_facebook_preset_size, -1));
        this.isCropped = a.getBoolean(R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped, true);
        a.recycle();
    }

    private void refreshImage(boolean force) {
        boolean changed = updateImageQueryParameters();
        if (this.profileId == null || this.profileId.length() == 0 || (this.queryWidth == 0 && this.queryHeight == 0)) {
            setBlankProfilePicture();
        } else if (changed || force) {
            sendImageRequest(true);
        }
    }

    private void setBlankProfilePicture() {
        if (this.lastRequest != null) {
            ImageDownloader.cancelRequest(this.lastRequest);
        }
        if (this.customizedDefaultProfilePicture == null) {
            int blankImageResource = isCropped() ? R.drawable.com_facebook_profile_picture_blank_square : R.drawable.com_facebook_profile_picture_blank_portrait;
            setImageBitmap(BitmapFactory.decodeResource(getResources(), blankImageResource));
        } else {
            updateImageQueryParameters();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(this.customizedDefaultProfilePicture, this.queryWidth, this.queryHeight, false);
            setImageBitmap(scaledBitmap);
        }
    }

    private void setImageBitmap(Bitmap imageBitmap) {
        if (this.image != null && imageBitmap != null) {
            this.imageContents = imageBitmap;
            this.image.setImageBitmap(imageBitmap);
        }
    }

    private void sendImageRequest(boolean allowCachedResponse) {
        ImageRequest.Builder requestBuilder = new ImageRequest.Builder(getContext(), ImageRequest.getProfilePictureUri(this.profileId, this.queryWidth, this.queryHeight));
        ImageRequest request = requestBuilder.setAllowCachedRedirects(allowCachedResponse).setCallerTag(this).setCallback(new ImageRequest.Callback() { // from class: com.facebook.login.widget.ProfilePictureView.1
            @Override // com.facebook.internal.ImageRequest.Callback
            public void onCompleted(ImageResponse response) {
                ProfilePictureView.this.processResponse(response);
            }
        }).build();
        if (this.lastRequest != null) {
            ImageDownloader.cancelRequest(this.lastRequest);
        }
        this.lastRequest = request;
        ImageDownloader.downloadAsync(request);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processResponse(ImageResponse response) {
        if (response.getRequest() == this.lastRequest) {
            this.lastRequest = null;
            Bitmap responseImage = response.getBitmap();
            Exception error = response.getError();
            if (error != null) {
                OnErrorListener listener = this.onErrorListener;
                if (listener != null) {
                    listener.onError(new FacebookException("Error in downloading profile picture for profileId: " + getProfileId(), error));
                    return;
                } else {
                    Logger.log(LoggingBehavior.REQUESTS, 6, TAG, error.toString());
                    return;
                }
            }
            if (responseImage != null) {
                setImageBitmap(responseImage);
                if (response.isCachedRedirect()) {
                    sendImageRequest(false);
                }
            }
        }
    }

    private boolean updateImageQueryParameters() {
        boolean changed = true;
        int newHeightPx = getHeight();
        int newWidthPx = getWidth();
        if (newWidthPx < 1 || newHeightPx < 1) {
            return false;
        }
        int presetSize = getPresetSizeInPixels(false);
        if (presetSize != 0) {
            newWidthPx = presetSize;
            newHeightPx = presetSize;
        }
        if (newWidthPx <= newHeightPx) {
            newHeightPx = isCropped() ? newWidthPx : 0;
        } else {
            newWidthPx = isCropped() ? newHeightPx : 0;
        }
        if (newWidthPx == this.queryWidth && newHeightPx == this.queryHeight) {
            changed = false;
        }
        this.queryWidth = newWidthPx;
        this.queryHeight = newHeightPx;
        return changed;
    }

    private int getPresetSizeInPixels(boolean forcePreset) {
        int dimensionId;
        switch (this.presetSizeType) {
            case opencv_core.CV_StsNoMem /* -4 */:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_large;
                break;
            case opencv_core.CV_StsInternal /* -3 */:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_normal;
                break;
            case -2:
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_small;
                break;
            case -1:
                if (!forcePreset) {
                    return 0;
                }
                dimensionId = R.dimen.com_facebook_profilepictureview_preset_size_normal;
                break;
            default:
                return 0;
        }
        return getResources().getDimensionPixelSize(dimensionId);
    }
}
