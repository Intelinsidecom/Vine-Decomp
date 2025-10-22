package com.google.android.gms.ads.formats;

import android.content.Context;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.internal.util.client.zzb;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.dynamic.zzd;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.internal.zzcp;

/* loaded from: classes.dex */
public abstract class NativeAdView extends FrameLayout {
    private final FrameLayout zzoF;
    private final zzcp zzoG;

    public NativeAdView(Context context) {
        super(context);
        this.zzoF = zzn(context);
        this.zzoG = zzaK();
    }

    public NativeAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.zzoF = zzn(context);
        this.zzoG = zzaK();
    }

    public NativeAdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.zzoF = zzn(context);
        this.zzoG = zzaK();
    }

    private zzcp zzaK() {
        zzx.zzb(this.zzoF, "createDelegate must be called after mOverlayFrame has been created");
        return zzl.zzcQ().zza(this.zzoF.getContext(), this, this.zzoF);
    }

    private FrameLayout zzn(Context context) {
        FrameLayout frameLayoutZzo = zzo(context);
        frameLayoutZzo.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(frameLayoutZzo);
        return frameLayoutZzo;
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        super.bringChildToFront(this.zzoF);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void bringChildToFront(View child) {
        super.bringChildToFront(child);
        if (this.zzoF != child) {
            super.bringChildToFront(this.zzoF);
        }
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        super.removeAllViews();
        super.addView(this.zzoF);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View child) {
        if (this.zzoF == child) {
            return;
        }
        super.removeView(child);
    }

    public void setNativeAd(NativeAd ad) {
        try {
            this.zzoG.zzb((zzd) ad.zzaJ());
        } catch (RemoteException e) {
            zzb.zzb("Unable to call setNativeAd on delegate", e);
        }
    }

    protected void zza(String str, View view) {
        try {
            this.zzoG.zza(str, zze.zzB(view));
        } catch (RemoteException e) {
            zzb.zzb("Unable to call setAssetView on delegate", e);
        }
    }

    protected View zzm(String str) {
        try {
            zzd zzdVarZzU = this.zzoG.zzU(str);
            if (zzdVarZzU != null) {
                return (View) zze.zzp(zzdVarZzU);
            }
        } catch (RemoteException e) {
            zzb.zzb("Unable to call getAssetView on delegate", e);
        }
        return null;
    }

    FrameLayout zzo(Context context) {
        return new FrameLayout(context);
    }
}
