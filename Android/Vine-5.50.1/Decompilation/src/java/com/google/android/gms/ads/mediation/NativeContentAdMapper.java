package com.google.android.gms.ads.mediation;

import com.google.android.gms.ads.formats.NativeAd;
import java.util.List;

/* loaded from: classes.dex */
public abstract class NativeContentAdMapper extends NativeAdMapper {
    private NativeAd.Image zzMV;
    private String zzxA;
    private String zzxC;
    private String zzxJ;
    private String zzxy;
    private List<NativeAd.Image> zzxz;

    public final String getAdvertiser() {
        return this.zzxJ;
    }

    public final String getBody() {
        return this.zzxA;
    }

    public final String getCallToAction() {
        return this.zzxC;
    }

    public final String getHeadline() {
        return this.zzxy;
    }

    public final List<NativeAd.Image> getImages() {
        return this.zzxz;
    }

    public final NativeAd.Image getLogo() {
        return this.zzMV;
    }

    public final void setAdvertiser(String advertiser) {
        this.zzxJ = advertiser;
    }

    public final void setBody(String body) {
        this.zzxA = body;
    }

    public final void setCallToAction(String callToAction) {
        this.zzxC = callToAction;
    }

    public final void setHeadline(String headline) {
        this.zzxy = headline;
    }

    public final void setImages(List<NativeAd.Image> images) {
        this.zzxz = images;
    }

    public final void setLogo(NativeAd.Image logo) {
        this.zzMV = logo;
    }
}
