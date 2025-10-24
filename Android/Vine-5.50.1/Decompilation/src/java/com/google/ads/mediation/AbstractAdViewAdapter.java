package com.google.ads.mediation;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.internal.client.zzl;
import com.google.android.gms.ads.mediation.MediationAdapter;
import com.google.android.gms.ads.mediation.MediationNativeAdapter;
import com.google.android.gms.ads.mediation.MediationNativeListener;
import com.google.android.gms.ads.mediation.NativeAppInstallAdMapper;
import com.google.android.gms.ads.mediation.NativeContentAdMapper;
import com.google.android.gms.ads.mediation.NativeMediationAdRequest;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzjy;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public abstract class AbstractAdViewAdapter implements com.google.android.gms.ads.mediation.MediationBannerAdapter, MediationNativeAdapter, zzjy {
    public static final String AD_UNIT_ID_PARAMETER = "pubid";
    protected AdView zzaN;
    protected InterstitialAd zzaO;
    private AdLoader zzaP;

    static class zza extends NativeAppInstallAdMapper {
        private final NativeAppInstallAd zzaQ;

        public zza(NativeAppInstallAd nativeAppInstallAd) {
            this.zzaQ = nativeAppInstallAd;
            setHeadline(nativeAppInstallAd.getHeadline().toString());
            setImages(nativeAppInstallAd.getImages());
            setBody(nativeAppInstallAd.getBody().toString());
            setIcon(nativeAppInstallAd.getIcon());
            setCallToAction(nativeAppInstallAd.getCallToAction().toString());
            setStarRating(nativeAppInstallAd.getStarRating().doubleValue());
            setStore(nativeAppInstallAd.getStore().toString());
            setPrice(nativeAppInstallAd.getPrice().toString());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        @Override // com.google.android.gms.ads.mediation.NativeAdMapper
        public void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzaQ);
            }
        }
    }

    static class zzb extends NativeContentAdMapper {
        private final NativeContentAd zzaR;

        public zzb(NativeContentAd nativeContentAd) {
            this.zzaR = nativeContentAd;
            setHeadline(nativeContentAd.getHeadline().toString());
            setImages(nativeContentAd.getImages());
            setBody(nativeContentAd.getBody().toString());
            setLogo(nativeContentAd.getLogo());
            setCallToAction(nativeContentAd.getCallToAction().toString());
            setAdvertiser(nativeContentAd.getAdvertiser().toString());
            setOverrideImpressionRecording(true);
            setOverrideClickHandling(true);
        }

        @Override // com.google.android.gms.ads.mediation.NativeAdMapper
        public void trackView(View view) {
            if (view instanceof NativeAdView) {
                ((NativeAdView) view).setNativeAd(this.zzaR);
            }
        }
    }

    static final class zzc extends AdListener implements com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzaS;
        final com.google.android.gms.ads.mediation.MediationBannerListener zzaT;

        public zzc(AbstractAdViewAdapter abstractAdViewAdapter, com.google.android.gms.ads.mediation.MediationBannerListener mediationBannerListener) {
            this.zzaS = abstractAdViewAdapter;
            this.zzaT = mediationBannerListener;
        }

        @Override // com.google.android.gms.ads.internal.client.zza
        public void onAdClicked() {
            this.zzaT.onAdClicked(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdClosed() {
            this.zzaT.onAdClosed(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdFailedToLoad(int errorCode) {
            this.zzaT.onAdFailedToLoad(this.zzaS, errorCode);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLeftApplication() {
            this.zzaT.onAdLeftApplication(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLoaded() {
            this.zzaT.onAdLoaded(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdOpened() {
            this.zzaT.onAdOpened(this.zzaS);
        }
    }

    static final class zzd extends AdListener implements com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzaS;
        final com.google.android.gms.ads.mediation.MediationInterstitialListener zzaU;

        public zzd(AbstractAdViewAdapter abstractAdViewAdapter, com.google.android.gms.ads.mediation.MediationInterstitialListener mediationInterstitialListener) {
            this.zzaS = abstractAdViewAdapter;
            this.zzaU = mediationInterstitialListener;
        }

        @Override // com.google.android.gms.ads.internal.client.zza
        public void onAdClicked() {
            this.zzaU.onAdClicked(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdClosed() {
            this.zzaU.onAdClosed(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdFailedToLoad(int errorCode) {
            this.zzaU.onAdFailedToLoad(this.zzaS, errorCode);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLeftApplication() {
            this.zzaU.onAdLeftApplication(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLoaded() {
            this.zzaU.onAdLoaded(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdOpened() {
            this.zzaU.onAdOpened(this.zzaS);
        }
    }

    static final class zze extends AdListener implements NativeAppInstallAd.OnAppInstallAdLoadedListener, NativeContentAd.OnContentAdLoadedListener, com.google.android.gms.ads.internal.client.zza {
        final AbstractAdViewAdapter zzaS;
        final MediationNativeListener zzaV;

        public zze(AbstractAdViewAdapter abstractAdViewAdapter, MediationNativeListener mediationNativeListener) {
            this.zzaS = abstractAdViewAdapter;
            this.zzaV = mediationNativeListener;
        }

        @Override // com.google.android.gms.ads.internal.client.zza
        public void onAdClicked() {
            this.zzaV.onAdClicked(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdClosed() {
            this.zzaV.onAdClosed(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdFailedToLoad(int errorCode) {
            this.zzaV.onAdFailedToLoad(this.zzaS, errorCode);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLeftApplication() {
            this.zzaV.onAdLeftApplication(this.zzaS);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLoaded() {
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdOpened() {
            this.zzaV.onAdOpened(this.zzaS);
        }

        @Override // com.google.android.gms.ads.formats.NativeAppInstallAd.OnAppInstallAdLoadedListener
        public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
            this.zzaV.onAdLoaded(this.zzaS, new zza(ad));
        }

        @Override // com.google.android.gms.ads.formats.NativeContentAd.OnContentAdLoadedListener
        public void onContentAdLoaded(NativeContentAd ad) {
            this.zzaV.onAdLoaded(this.zzaS, new zzb(ad));
        }
    }

    public String getAdUnitId(Bundle serverParameters) {
        return serverParameters.getString(AD_UNIT_ID_PARAMETER);
    }

    @Override // com.google.android.gms.ads.mediation.MediationBannerAdapter
    public View getBannerView() {
        return this.zzaN;
    }

    @Override // com.google.android.gms.internal.zzjy
    public Bundle getInterstitialAdapterInfo() {
        return new MediationAdapter.zza().zzS(1).zzie();
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdapter
    public void onDestroy() {
        if (this.zzaN != null) {
            this.zzaN.destroy();
            this.zzaN = null;
        }
        if (this.zzaO != null) {
            this.zzaO = null;
        }
        if (this.zzaP != null) {
            this.zzaP = null;
        }
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdapter
    public void onPause() {
        if (this.zzaN != null) {
            this.zzaN.pause();
        }
    }

    @Override // com.google.android.gms.ads.mediation.MediationAdapter
    public void onResume() {
        if (this.zzaN != null) {
            this.zzaN.resume();
        }
    }

    @Override // com.google.android.gms.ads.mediation.MediationBannerAdapter
    public void requestBannerAd(Context context, com.google.android.gms.ads.mediation.MediationBannerListener bannerListener, Bundle serverParameters, AdSize adSize, com.google.android.gms.ads.mediation.MediationAdRequest mediationAdRequest, Bundle extras) {
        this.zzaN = new AdView(context);
        this.zzaN.setAdSize(new AdSize(adSize.getWidth(), adSize.getHeight()));
        this.zzaN.setAdUnitId(getAdUnitId(serverParameters));
        this.zzaN.setAdListener(new zzc(this, bannerListener));
        this.zzaN.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    @Override // com.google.android.gms.ads.mediation.MediationInterstitialAdapter
    public void requestInterstitialAd(Context context, com.google.android.gms.ads.mediation.MediationInterstitialListener interstitialListener, Bundle serverParameters, com.google.android.gms.ads.mediation.MediationAdRequest mediationAdRequest, Bundle extras) {
        this.zzaO = new InterstitialAd(context);
        this.zzaO.setAdUnitId(getAdUnitId(serverParameters));
        this.zzaO.setAdListener(new zzd(this, interstitialListener));
        this.zzaO.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    @Override // com.google.android.gms.ads.mediation.MediationNativeAdapter
    public void requestNativeAd(Context context, MediationNativeListener listener, Bundle serverParameters, NativeMediationAdRequest mediationAdRequest, Bundle extras) {
        zze zzeVar = new zze(this, listener);
        AdLoader.Builder builderWithAdListener = zza(context, serverParameters.getString(AD_UNIT_ID_PARAMETER)).withAdListener(zzeVar);
        NativeAdOptions nativeAdOptions = mediationAdRequest.getNativeAdOptions();
        if (nativeAdOptions != null) {
            builderWithAdListener.withNativeAdOptions(nativeAdOptions);
        }
        if (mediationAdRequest.isAppInstallAdRequested()) {
            builderWithAdListener.forAppInstallAd(zzeVar);
        }
        if (mediationAdRequest.isContentAdRequested()) {
            builderWithAdListener.forContentAd(zzeVar);
        }
        this.zzaP = builderWithAdListener.build();
        this.zzaP.loadAd(zza(context, mediationAdRequest, extras, serverParameters));
    }

    @Override // com.google.android.gms.ads.mediation.MediationInterstitialAdapter
    public void showInterstitial() {
        this.zzaO.show();
    }

    protected abstract Bundle zza(Bundle bundle, Bundle bundle2);

    AdLoader.Builder zza(Context context, String str) {
        return new AdLoader.Builder(context, str);
    }

    AdRequest zza(Context context, com.google.android.gms.ads.mediation.MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle2) {
        AdRequest.Builder builder = new AdRequest.Builder();
        Date birthday = mediationAdRequest.getBirthday();
        if (birthday != null) {
            builder.setBirthday(birthday);
        }
        int gender = mediationAdRequest.getGender();
        if (gender != 0) {
            builder.setGender(gender);
        }
        Set<String> keywords = mediationAdRequest.getKeywords();
        if (keywords != null) {
            Iterator<String> it = keywords.iterator();
            while (it.hasNext()) {
                builder.addKeyword(it.next());
            }
        }
        Location location = mediationAdRequest.getLocation();
        if (location != null) {
            builder.setLocation(location);
        }
        if (mediationAdRequest.isTesting()) {
            builder.addTestDevice(zzl.zzcN().zzS(context));
        }
        if (mediationAdRequest.taggedForChildDirectedTreatment() != -1) {
            builder.tagForChildDirectedTreatment(mediationAdRequest.taggedForChildDirectedTreatment() == 1);
        }
        builder.setIsDesignedForFamilies(mediationAdRequest.isDesignedForFamilies());
        builder.addNetworkExtrasBundle(AdMobAdapter.class, zza(bundle, bundle2));
        return builder.build();
    }
}
