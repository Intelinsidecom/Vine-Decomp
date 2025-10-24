package com.google.android.gms.ads;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.ads.internal.client.zzz;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;

/* loaded from: classes.dex */
class BaseAdView extends ViewGroup {
    private final zzz zzoy;

    public BaseAdView(Context context, int adViewType) {
        super(context);
        this.zzoy = new zzz(this, zze(adViewType));
    }

    public BaseAdView(Context context, AttributeSet attrs, int adViewType) {
        super(context, attrs);
        this.zzoy = new zzz(this, attrs, false, zze(adViewType));
    }

    public BaseAdView(Context context, AttributeSet attrs, int defStyle, int adViewType) {
        super(context, attrs, defStyle);
        this.zzoy = new zzz(this, attrs, false, zze(adViewType));
    }

    private static boolean zze(int i) {
        return i == 2;
    }

    public void destroy() {
        this.zzoy.destroy();
    }

    public AdListener getAdListener() {
        return this.zzoy.getAdListener();
    }

    public AdSize getAdSize() {
        return this.zzoy.getAdSize();
    }

    public String getAdUnitId() {
        return this.zzoy.getAdUnitId();
    }

    public InAppPurchaseListener getInAppPurchaseListener() {
        return this.zzoy.getInAppPurchaseListener();
    }

    public String getMediationAdapterClassName() {
        return this.zzoy.getMediationAdapterClassName();
    }

    public void loadAd(AdRequest adRequest) {
        this.zzoy.zza(adRequest.zzaG());
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            return;
        }
        int measuredWidth = childAt.getMeasuredWidth();
        int measuredHeight = childAt.getMeasuredHeight();
        int i = ((right - left) - measuredWidth) / 2;
        int i2 = ((bottom - top) - measuredHeight) / 2;
        childAt.layout(i, i2, measuredWidth + i, measuredHeight + i2);
    }

    @Override // android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthInPixels;
        int heightInPixels = 0;
        View childAt = getChildAt(0);
        if (childAt == null || childAt.getVisibility() == 8) {
            AdSize adSize = getAdSize();
            if (adSize != null) {
                Context context = getContext();
                widthInPixels = adSize.getWidthInPixels(context);
                heightInPixels = adSize.getHeightInPixels(context);
            } else {
                widthInPixels = 0;
            }
        } else {
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
            widthInPixels = childAt.getMeasuredWidth();
            heightInPixels = childAt.getMeasuredHeight();
        }
        setMeasuredDimension(View.resolveSize(Math.max(widthInPixels, getSuggestedMinimumWidth()), widthMeasureSpec), View.resolveSize(Math.max(heightInPixels, getSuggestedMinimumHeight()), heightMeasureSpec));
    }

    public void pause() {
        this.zzoy.pause();
    }

    public void resume() {
        this.zzoy.resume();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setAdListener(AdListener adListener) {
        this.zzoy.setAdListener(adListener);
        if (adListener != 0 && (adListener instanceof com.google.android.gms.ads.internal.client.zza)) {
            this.zzoy.zza((com.google.android.gms.ads.internal.client.zza) adListener);
        } else if (adListener == 0) {
            this.zzoy.zza((com.google.android.gms.ads.internal.client.zza) null);
        }
    }

    public void setAdSize(AdSize adSize) {
        this.zzoy.setAdSizes(adSize);
    }

    public void setAdUnitId(String adUnitId) {
        this.zzoy.setAdUnitId(adUnitId);
    }

    public void setInAppPurchaseListener(InAppPurchaseListener inAppPurchaseListener) {
        this.zzoy.setInAppPurchaseListener(inAppPurchaseListener);
    }
}
