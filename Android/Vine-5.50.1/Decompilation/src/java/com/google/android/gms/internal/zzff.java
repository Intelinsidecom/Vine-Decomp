package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.internal.zzfa;
import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzff extends zzfa.zza {
    private final NativeAppInstallAdMapper zzBM;

    public zzff(NativeAppInstallAdMapper nativeAppInstallAdMapper) {
        this.zzBM = nativeAppInstallAdMapper;
    }

    @Override // com.google.android.gms.internal.zzfa
    public String getBody() {
        return this.zzBM.getBody();
    }

    @Override // com.google.android.gms.internal.zzfa
    public String getCallToAction() {
        return this.zzBM.getCallToAction();
    }

    @Override // com.google.android.gms.internal.zzfa
    public Bundle getExtras() {
        return this.zzBM.getExtras();
    }

    @Override // com.google.android.gms.internal.zzfa
    public String getHeadline() {
        return this.zzBM.getHeadline();
    }

    @Override // com.google.android.gms.internal.zzfa
    public List getImages() {
        List<NativeAd.Image> images = this.zzBM.getImages();
        if (images == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (NativeAd.Image image : images) {
            arrayList.add(new com.google.android.gms.ads.internal.formats.zzc(image.getDrawable(), image.getUri(), image.getScale()));
        }
        return arrayList;
    }

    @Override // com.google.android.gms.internal.zzfa
    public boolean getOverrideClickHandling() {
        return this.zzBM.getOverrideClickHandling();
    }

    @Override // com.google.android.gms.internal.zzfa
    public boolean getOverrideImpressionRecording() {
        return this.zzBM.getOverrideImpressionRecording();
    }

    @Override // com.google.android.gms.internal.zzfa
    public String getPrice() {
        return this.zzBM.getPrice();
    }

    @Override // com.google.android.gms.internal.zzfa
    public double getStarRating() {
        return this.zzBM.getStarRating();
    }

    @Override // com.google.android.gms.internal.zzfa
    public String getStore() {
        return this.zzBM.getStore();
    }

    @Override // com.google.android.gms.internal.zzfa
    public void recordImpression() {
        this.zzBM.recordImpression();
    }

    @Override // com.google.android.gms.internal.zzfa
    public void zzc(com.google.android.gms.dynamic.zzd zzdVar) {
        this.zzBM.handleClick((View) com.google.android.gms.dynamic.zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.internal.zzfa
    public void zzd(com.google.android.gms.dynamic.zzd zzdVar) {
        this.zzBM.trackView((View) com.google.android.gms.dynamic.zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.internal.zzfa
    public zzcn zzdD() {
        NativeAd.Image icon = this.zzBM.getIcon();
        if (icon != null) {
            return new com.google.android.gms.ads.internal.formats.zzc(icon.getDrawable(), icon.getUri(), icon.getScale());
        }
        return null;
    }
}
