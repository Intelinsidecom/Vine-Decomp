package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public final class zzhf {
    private final AdRequestInfoParcel zzBu;
    private List<String> zzFH;
    private String zzIb;
    private String zzIc;
    private List<String> zzId;
    private String zzIe;
    private String zzIf;
    private List<String> zzIg;
    private String zzxA;
    private long zzIh = -1;
    private boolean zzIi = false;
    private final long zzIj = -1;
    private long zzIk = -1;
    private int mOrientation = -1;
    private boolean zzIl = false;
    private boolean zzIm = false;
    private boolean zzIn = false;
    private boolean zzIo = true;
    private int zzIp = 0;
    private String zzIq = "";
    private boolean zzIr = false;

    public zzhf(AdRequestInfoParcel adRequestInfoParcel) {
        this.zzBu = adRequestInfoParcel;
    }

    static String zzd(Map<String, List<String>> map, String str) {
        List<String> list = map.get(str);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    static long zze(Map<String, List<String>> map, String str) {
        List<String> list = map.get(str);
        if (list != null && !list.isEmpty()) {
            String str2 = list.get(0);
            try {
                return (long) (Float.parseFloat(str2) * 1000.0f);
            } catch (NumberFormatException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse float from " + str + " header: " + str2);
            }
        }
        return -1L;
    }

    static List<String> zzf(Map<String, List<String>> map, String str) {
        String str2;
        List<String> list = map.get(str);
        if (list == null || list.isEmpty() || (str2 = list.get(0)) == null) {
            return null;
        }
        return Arrays.asList(str2.trim().split("\\s+"));
    }

    private boolean zzg(Map<String, List<String>> map, String str) {
        List<String> list = map.get(str);
        return (list == null || list.isEmpty() || !Boolean.valueOf(list.get(0)).booleanValue()) ? false : true;
    }

    private void zzi(Map<String, List<String>> map) {
        this.zzIb = zzd(map, "X-Afma-Ad-Size");
    }

    private void zzj(Map<String, List<String>> map) {
        List<String> listZzf = zzf(map, "X-Afma-Click-Tracking-Urls");
        if (listZzf != null) {
            this.zzId = listZzf;
        }
    }

    private void zzk(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Debug-Dialog");
        if (list == null || list.isEmpty()) {
            return;
        }
        this.zzIe = list.get(0);
    }

    private void zzl(Map<String, List<String>> map) {
        List<String> listZzf = zzf(map, "X-Afma-Tracking-Urls");
        if (listZzf != null) {
            this.zzIg = listZzf;
        }
    }

    private void zzm(Map<String, List<String>> map) {
        long jZze = zze(map, "X-Afma-Interstitial-Timeout");
        if (jZze != -1) {
            this.zzIh = jZze;
        }
    }

    private void zzn(Map<String, List<String>> map) {
        this.zzIf = zzd(map, "X-Afma-ActiveView");
    }

    private void zzo(Map<String, List<String>> map) {
        this.zzIm = "native".equals(zzd(map, "X-Afma-Ad-Format"));
    }

    private void zzp(Map<String, List<String>> map) {
        this.zzIl |= zzg(map, "X-Afma-Custom-Rendering-Allowed");
    }

    private void zzq(Map<String, List<String>> map) {
        this.zzIi |= zzg(map, "X-Afma-Mediation");
    }

    private void zzr(Map<String, List<String>> map) {
        List<String> listZzf = zzf(map, "X-Afma-Manual-Tracking-Urls");
        if (listZzf != null) {
            this.zzFH = listZzf;
        }
    }

    private void zzs(Map<String, List<String>> map) {
        long jZze = zze(map, "X-Afma-Refresh-Rate");
        if (jZze != -1) {
            this.zzIk = jZze;
        }
    }

    private void zzt(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Orientation");
        if (list == null || list.isEmpty()) {
            return;
        }
        String str = list.get(0);
        if ("portrait".equalsIgnoreCase(str)) {
            this.mOrientation = com.google.android.gms.ads.internal.zzp.zzbz().zzhe();
        } else if ("landscape".equalsIgnoreCase(str)) {
            this.mOrientation = com.google.android.gms.ads.internal.zzp.zzbz().zzhd();
        }
    }

    private void zzu(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Use-HTTPS");
        if (list == null || list.isEmpty()) {
            return;
        }
        this.zzIn = Boolean.valueOf(list.get(0)).booleanValue();
    }

    private void zzv(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Content-Url-Opted-Out");
        if (list == null || list.isEmpty()) {
            return;
        }
        this.zzIo = Boolean.valueOf(list.get(0)).booleanValue();
    }

    private void zzw(Map<String, List<String>> map) {
        List<String> listZzf = zzf(map, "X-Afma-OAuth-Token-Status");
        this.zzIp = 0;
        if (listZzf == null) {
            return;
        }
        for (String str : listZzf) {
            if ("Clear".equalsIgnoreCase(str)) {
                this.zzIp = 1;
                return;
            } else if ("No-Op".equalsIgnoreCase(str)) {
                this.zzIp = 0;
                return;
            }
        }
    }

    private void zzx(Map<String, List<String>> map) {
        List<String> list = map.get("X-Afma-Gws-Query-Id");
        if (list == null || list.isEmpty()) {
            return;
        }
        this.zzIq = list.get(0);
    }

    private void zzy(Map<String, List<String>> map) {
        String strZzd = zzd(map, "X-Afma-Fluid");
        if (strZzd == null || !strZzd.equals("height")) {
            return;
        }
        this.zzIr = true;
    }

    public void zzb(String str, Map<String, List<String>> map, String str2) {
        this.zzIc = str;
        this.zzxA = str2;
        zzh(map);
    }

    public void zzh(Map<String, List<String>> map) {
        zzi(map);
        zzj(map);
        zzk(map);
        zzl(map);
        zzm(map);
        zzq(map);
        zzr(map);
        zzs(map);
        zzt(map);
        zzn(map);
        zzu(map);
        zzp(map);
        zzo(map);
        zzv(map);
        zzw(map);
        zzx(map);
        zzy(map);
    }

    public AdResponseParcel zzj(long j) {
        return new AdResponseParcel(this.zzBu, this.zzIc, this.zzxA, this.zzId, this.zzIg, this.zzIh, this.zzIi, -1L, this.zzFH, this.zzIk, this.mOrientation, this.zzIb, j, this.zzIe, this.zzIf, this.zzIl, this.zzIm, this.zzIn, this.zzIo, false, this.zzIp, this.zzIq, this.zzIr);
    }
}
