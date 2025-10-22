package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

@zzha
/* loaded from: classes.dex */
class zzee {
    private final Object[] mParams;
    private boolean zzzV;

    zzee(AdRequestParcel adRequestParcel, String str) {
        this.mParams = zzb(adRequestParcel, str);
    }

    private static Object[] zzb(AdRequestParcel adRequestParcel, String str) {
        HashSet hashSet = new HashSet(Arrays.asList(zzbz.zzwk.get().split(",")));
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        if (hashSet.contains("birthday")) {
            arrayList.add(Long.valueOf(adRequestParcel.zztq));
        }
        if (hashSet.contains("extras")) {
            arrayList.add(zzc(adRequestParcel.extras));
        }
        if (hashSet.contains("gender")) {
            arrayList.add(Integer.valueOf(adRequestParcel.zztr));
        }
        if (hashSet.contains("keywords")) {
            if (adRequestParcel.zzts != null) {
                arrayList.add(adRequestParcel.zzts.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("isTestDevice")) {
            arrayList.add(Boolean.valueOf(adRequestParcel.zztt));
        }
        if (hashSet.contains("tagForChildDirectedTreatment")) {
            arrayList.add(Integer.valueOf(adRequestParcel.zztu));
        }
        if (hashSet.contains("manualImpressionsEnabled")) {
            arrayList.add(Boolean.valueOf(adRequestParcel.zztv));
        }
        if (hashSet.contains("publisherProvidedId")) {
            arrayList.add(adRequestParcel.zztw);
        }
        if (hashSet.contains("location")) {
            if (adRequestParcel.zzty != null) {
                arrayList.add(adRequestParcel.zzty.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("contentUrl")) {
            arrayList.add(adRequestParcel.zztz);
        }
        if (hashSet.contains("networkExtras")) {
            arrayList.add(zzc(adRequestParcel.zztA));
        }
        if (hashSet.contains("customTargeting")) {
            arrayList.add(zzc(adRequestParcel.zztB));
        }
        if (hashSet.contains("categoryExclusions")) {
            if (adRequestParcel.zztC != null) {
                arrayList.add(adRequestParcel.zztC.toString());
            } else {
                arrayList.add(null);
            }
        }
        if (hashSet.contains("requestAgent")) {
            arrayList.add(adRequestParcel.zztD);
        }
        if (hashSet.contains("requestPackage")) {
            arrayList.add(adRequestParcel.zztE);
        }
        return arrayList.toArray();
    }

    private static String zzc(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Collections.sort(new ArrayList(bundle.keySet()));
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            Object obj = bundle.get(it.next());
            sb.append(obj == null ? "null" : obj instanceof Bundle ? zzc((Bundle) obj) : obj.toString());
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof zzee) {
            return Arrays.equals(this.mParams, ((zzee) o).mParams);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.mParams);
    }

    public String toString() {
        return "[InterstitialAdPoolKey " + Arrays.toString(this.mParams) + "]";
    }

    void zzdZ() {
        this.zzzV = true;
    }

    boolean zzea() {
        return this.zzzV;
    }
}
