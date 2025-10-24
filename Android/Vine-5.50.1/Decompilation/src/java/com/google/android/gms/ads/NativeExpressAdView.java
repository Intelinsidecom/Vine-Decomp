package com.google.android.gms.ads;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;

/* loaded from: classes.dex */
public final class NativeExpressAdView extends BaseAdView {
    public NativeExpressAdView(Context context) {
        super(context, 2);
    }

    public NativeExpressAdView(Context context, AttributeSet attrs) {
        super(context, attrs, 2);
    }

    public NativeExpressAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle, 2);
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void destroy() {
        super.destroy();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ AdListener getAdListener() {
        return super.getAdListener();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ AdSize getAdSize() {
        return super.getAdSize();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ String getAdUnitId() {
        return super.getAdUnitId();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ InAppPurchaseListener getInAppPurchaseListener() {
        return super.getInAppPurchaseListener();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ String getMediationAdapterClassName() {
        return super.getMediationAdapterClassName();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void loadAd(AdRequest x0) {
        super.loadAd(x0);
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void pause() {
        super.pause();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void resume() {
        super.resume();
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void setAdListener(AdListener x0) {
        super.setAdListener(x0);
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void setAdSize(AdSize x0) {
        super.setAdSize(x0);
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void setAdUnitId(String x0) {
        super.setAdUnitId(x0);
    }

    @Override // com.google.android.gms.ads.BaseAdView
    public /* bridge */ /* synthetic */ void setInAppPurchaseListener(InAppPurchaseListener x0) {
        super.setInAppPurchaseListener(x0);
    }
}
