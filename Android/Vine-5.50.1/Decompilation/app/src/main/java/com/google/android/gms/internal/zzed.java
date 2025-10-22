package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.util.Base64;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.internal.zzef;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public class zzed {
    private final Map<zzee, zzef> zzzS = new HashMap();
    private final LinkedList<zzee> zzzT = new LinkedList<>();
    private zzea zzzU;

    private static void zza(String str, zzee zzeeVar) {
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
            com.google.android.gms.ads.internal.util.client.zzb.v(String.format(str, zzeeVar));
        }
    }

    private String[] zzad(String str) {
        try {
            String[] strArrSplit = str.split("\u0000");
            for (int i = 0; i < strArrSplit.length; i++) {
                strArrSplit[i] = new String(Base64.decode(strArrSplit[i], 0), "UTF-8");
            }
            return strArrSplit;
        } catch (UnsupportedEncodingException e) {
            return new String[0];
        }
    }

    private String zzdY() {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<zzee> it = this.zzzT.iterator();
            while (it.hasNext()) {
                sb.append(Base64.encodeToString(it.next().toString().getBytes("UTF-8"), 0));
                if (it.hasNext()) {
                    sb.append("\u0000");
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    void flush() {
        while (this.zzzT.size() > 0) {
            zzee zzeeVarRemove = this.zzzT.remove();
            zzef zzefVar = this.zzzS.get(zzeeVarRemove);
            zza("Flushing interstitial queue for %s.", zzeeVarRemove);
            while (zzefVar.size() > 0) {
                zzefVar.zzec().zzzX.zzbo();
            }
            this.zzzS.remove(zzeeVarRemove);
        }
    }

    void restore() {
        if (this.zzzU == null) {
            return;
        }
        SharedPreferences sharedPreferences = this.zzzU.zzdW().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0);
        flush();
        HashMap map = new HashMap();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            try {
                if (!entry.getKey().equals("PoolKeys")) {
                    zzeh zzehVar = new zzeh((String) entry.getValue());
                    zzee zzeeVar = new zzee(zzehVar.zzqo, zzehVar.zzpH);
                    if (!this.zzzS.containsKey(zzeeVar)) {
                        this.zzzS.put(zzeeVar, new zzef(zzehVar.zzqo, zzehVar.zzpH));
                        map.put(zzeeVar.toString(), zzeeVar);
                        zza("Restored interstitial queue for %s.", zzeeVar);
                    }
                }
            } catch (IOException | ClassCastException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Malformed preferences value for InterstitialAdPool.", e);
            }
        }
        for (String str : zzad(sharedPreferences.getString("PoolKeys", ""))) {
            zzee zzeeVar2 = (zzee) map.get(str);
            if (this.zzzS.containsKey(zzeeVar2)) {
                this.zzzT.add(zzeeVar2);
            }
        }
    }

    void save() {
        if (this.zzzU == null) {
            return;
        }
        SharedPreferences.Editor editorEdit = this.zzzU.zzdW().getSharedPreferences("com.google.android.gms.ads.internal.interstitial.InterstitialAdPool", 0).edit();
        editorEdit.clear();
        for (Map.Entry<zzee, zzef> entry : this.zzzS.entrySet()) {
            zzee key = entry.getKey();
            if (key.zzea()) {
                zzef value = entry.getValue();
                editorEdit.putString(key.toString(), new zzeh(value.zzeb(), value.getAdUnitId()).zzef());
                zza("Saved interstitial queue for %s.", key);
            }
        }
        editorEdit.putString("PoolKeys", zzdY());
        editorEdit.commit();
    }

    zzef.zza zza(AdRequestParcel adRequestParcel, String str) {
        zzef zzefVar;
        zzee zzeeVar = new zzee(adRequestParcel, str);
        zzef zzefVar2 = this.zzzS.get(zzeeVar);
        if (zzefVar2 == null) {
            zza("Interstitial pool created at %s.", zzeeVar);
            zzef zzefVar3 = new zzef(adRequestParcel, str);
            this.zzzS.put(zzeeVar, zzefVar3);
            zzefVar = zzefVar3;
        } else {
            zzefVar = zzefVar2;
        }
        this.zzzT.remove(zzeeVar);
        this.zzzT.add(zzeeVar);
        zzeeVar.zzdZ();
        while (this.zzzT.size() > zzbz.zzwl.get().intValue()) {
            zzee zzeeVarRemove = this.zzzT.remove();
            zzef zzefVar4 = this.zzzS.get(zzeeVarRemove);
            zza("Evicting interstitial queue for %s.", zzeeVarRemove);
            while (zzefVar4.size() > 0) {
                zzefVar4.zzec().zzzX.zzbo();
            }
            this.zzzS.remove(zzeeVarRemove);
        }
        while (zzefVar.size() > 0) {
            zzef.zza zzaVarZzec = zzefVar.zzec();
            if (!zzaVarZzec.zzAa || com.google.android.gms.ads.internal.zzp.zzbB().currentTimeMillis() - zzaVarZzec.zzzZ <= 1000 * zzbz.zzwn.get().intValue()) {
                zza("Pooled interstitial returned at %s.", zzeeVar);
                return zzaVarZzec;
            }
            zza("Expired interstitial at %s.", zzeeVar);
        }
        return null;
    }

    void zza(zzea zzeaVar) {
        if (this.zzzU == null) {
            this.zzzU = zzeaVar;
            restore();
        }
    }

    void zzdX() {
        if (this.zzzU == null) {
            return;
        }
        for (Map.Entry<zzee, zzef> entry : this.zzzS.entrySet()) {
            zzee key = entry.getKey();
            zzef value = entry.getValue();
            while (value.size() < zzbz.zzwm.get().intValue()) {
                zza("Pooling one interstitial for %s.", key);
                value.zzb(this.zzzU);
            }
        }
        save();
    }
}
