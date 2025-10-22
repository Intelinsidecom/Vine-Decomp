package com.google.android.gms.ads;

import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.internal.client.zzy;
import com.google.android.gms.ads.mediation.MediationAdapter;
import java.util.Date;

/* loaded from: classes.dex */
public final class AdRequest {
    public static final String DEVICE_ID_EMULATOR = zzy.DEVICE_ID_EMULATOR;
    private final zzy zzot;

    public static final class Builder {
        private final zzy.zza zzou = new zzy.zza();

        public Builder() {
            this.zzou.zzG(AdRequest.DEVICE_ID_EMULATOR);
        }

        public Builder addKeyword(String keyword) {
            this.zzou.zzF(keyword);
            return this;
        }

        public Builder addNetworkExtrasBundle(Class<? extends MediationAdapter> adapterClass, Bundle networkExtras) {
            this.zzou.zza(adapterClass, networkExtras);
            if (adapterClass.equals(AdMobAdapter.class) && networkExtras.getBoolean("_emulatorLiveAds")) {
                this.zzou.zzH(AdRequest.DEVICE_ID_EMULATOR);
            }
            return this;
        }

        public Builder addTestDevice(String deviceId) {
            this.zzou.zzG(deviceId);
            return this;
        }

        public AdRequest build() {
            return new AdRequest(this);
        }

        public Builder setBirthday(Date birthday) {
            this.zzou.zza(birthday);
            return this;
        }

        public Builder setGender(int gender) {
            this.zzou.zzn(gender);
            return this;
        }

        public Builder setIsDesignedForFamilies(boolean isDesignedForFamilies) {
            this.zzou.zzl(isDesignedForFamilies);
            return this;
        }

        public Builder setLocation(Location location) {
            this.zzou.zzb(location);
            return this;
        }

        public Builder tagForChildDirectedTreatment(boolean tagForChildDirectedTreatment) {
            this.zzou.zzk(tagForChildDirectedTreatment);
            return this;
        }
    }

    private AdRequest(Builder builder) {
        this.zzot = new zzy(builder.zzou);
    }

    public zzy zzaG() {
        return this.zzot;
    }
}
