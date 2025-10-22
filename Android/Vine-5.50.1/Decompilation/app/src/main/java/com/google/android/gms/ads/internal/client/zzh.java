package com.google.android.gms.ads.internal.client;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.search.SearchAdRequest;
import com.google.android.gms.internal.zzha;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public class zzh {
    public static final zzh zztU = new zzh();

    protected zzh() {
    }

    public static zzh zzcJ() {
        return zztU;
    }

    public AdRequestParcel zza(Context context, zzy zzyVar) {
        Date birthday = zzyVar.getBirthday();
        long time = birthday != null ? birthday.getTime() : -1L;
        String contentUrl = zzyVar.getContentUrl();
        int gender = zzyVar.getGender();
        Set<String> keywords = zzyVar.getKeywords();
        List listUnmodifiableList = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        boolean zIsTestDevice = zzyVar.isTestDevice(context);
        int iZzcX = zzyVar.zzcX();
        Location location = zzyVar.getLocation();
        Bundle networkExtrasBundle = zzyVar.getNetworkExtrasBundle(AdMobAdapter.class);
        boolean manualImpressionsEnabled = zzyVar.getManualImpressionsEnabled();
        String publisherProvidedId = zzyVar.getPublisherProvidedId();
        SearchAdRequest searchAdRequestZzcU = zzyVar.zzcU();
        SearchAdRequestParcel searchAdRequestParcel = searchAdRequestZzcU != null ? new SearchAdRequestParcel(searchAdRequestZzcU) : null;
        Context applicationContext = context.getApplicationContext();
        return new AdRequestParcel(7, time, networkExtrasBundle, gender, listUnmodifiableList, zIsTestDevice, iZzcX, manualImpressionsEnabled, publisherProvidedId, searchAdRequestParcel, location, contentUrl, zzyVar.zzcW(), zzyVar.getCustomTargeting(), Collections.unmodifiableList(new ArrayList(zzyVar.zzcY())), zzyVar.zzcT(), applicationContext != null ? com.google.android.gms.ads.internal.zzp.zzbx().zza(Thread.currentThread().getStackTrace(), applicationContext.getPackageName()) : null, zzyVar.isDesignedForFamilies());
    }
}
