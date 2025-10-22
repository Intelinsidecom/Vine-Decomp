package com.google.android.gms.internal;

import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.internal.zzfb;
import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzfg extends zzfb.zza {
    private final NativeContentAdMapper zzBN;

    public zzfg(NativeContentAdMapper nativeContentAdMapper) {
        this.zzBN = nativeContentAdMapper;
    }

    @Override // com.google.android.gms.internal.zzfb
    public String getAdvertiser() {
        return this.zzBN.getAdvertiser();
    }

    @Override // com.google.android.gms.internal.zzfb
    public String getBody() {
        return this.zzBN.getBody();
    }

    @Override // com.google.android.gms.internal.zzfb
    public String getCallToAction() {
        return this.zzBN.getCallToAction();
    }

    @Override // com.google.android.gms.internal.zzfb
    public Bundle getExtras() {
        return this.zzBN.getExtras();
    }

    @Override // com.google.android.gms.internal.zzfb
    public String getHeadline() {
        return this.zzBN.getHeadline();
    }

    @Override // com.google.android.gms.internal.zzfb
    public List getImages() {
        List<NativeAd.Image> images = this.zzBN.getImages();
        if (images == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (NativeAd.Image image : images) {
            arrayList.add(new com.google.android.gms.ads.internal.formats.zzc(image.getDrawable(), image.getUri(), image.getScale()));
        }
        return arrayList;
    }

    @Override // com.google.android.gms.internal.zzfb
    public boolean getOverrideClickHandling() {
        return this.zzBN.getOverrideClickHandling();
    }

    @Override // com.google.android.gms.internal.zzfb
    public boolean getOverrideImpressionRecording() {
        return this.zzBN.getOverrideImpressionRecording();
    }

    @Override // com.google.android.gms.internal.zzfb
    public void recordImpression() {
        this.zzBN.recordImpression();
    }

    @Override // com.google.android.gms.internal.zzfb
    public void zzc(com.google.android.gms.dynamic.zzd zzdVar) {
        this.zzBN.handleClick((View) com.google.android.gms.dynamic.zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.internal.zzfb
    public void zzd(com.google.android.gms.dynamic.zzd zzdVar) {
        this.zzBN.trackView((View) com.google.android.gms.dynamic.zze.zzp(zzdVar));
    }

    @Override // com.google.android.gms.internal.zzfb
    public zzcn zzdH() {
        NativeAd.Image logo = this.zzBN.getLogo();
        if (logo != null) {
            return new com.google.android.gms.ads.internal.formats.zzc(logo.getDrawable(), logo.getUri(), logo.getScale());
        }
        return null;
    }
}
