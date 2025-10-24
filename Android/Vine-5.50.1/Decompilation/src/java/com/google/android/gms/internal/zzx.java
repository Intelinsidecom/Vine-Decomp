package com.google.android.gms.internal;

import com.google.android.gms.internal.zzb;
import java.util.Map;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

/* loaded from: classes.dex */
public class zzx {
    public static String zza(Map<String, String> map) {
        return zzb(map, "ISO-8859-1");
    }

    public static zzb.zza zzb(zzi zziVar) throws NumberFormatException {
        boolean z;
        boolean z2;
        long j;
        long j2;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Map<String, String> map = zziVar.zzA;
        long j3 = 0;
        long j4 = 0;
        String str = map.get("Date");
        long jZzg = str != null ? zzg(str) : 0L;
        String str2 = map.get("Cache-Control");
        if (str2 != null) {
            String[] strArrSplit = str2.split(",");
            z = false;
            long j5 = 0;
            long j6 = 0;
            for (String str3 : strArrSplit) {
                String strTrim = str3.trim();
                if (strTrim.equals("no-cache") || strTrim.equals("no-store")) {
                    return null;
                }
                if (strTrim.startsWith("max-age=")) {
                    try {
                        j6 = Long.parseLong(strTrim.substring(8));
                    } catch (Exception e) {
                    }
                } else if (strTrim.startsWith("stale-while-revalidate=")) {
                    try {
                        j5 = Long.parseLong(strTrim.substring(23));
                    } catch (Exception e2) {
                    }
                } else if (strTrim.equals("must-revalidate") || strTrim.equals("proxy-revalidate")) {
                    z = true;
                }
            }
            j3 = j6;
            j4 = j5;
            z2 = true;
        } else {
            z = false;
            z2 = false;
        }
        String str4 = map.get("Expires");
        long jZzg2 = str4 != null ? zzg(str4) : 0L;
        String str5 = map.get("Last-Modified");
        long jZzg3 = str5 != null ? zzg(str5) : 0L;
        String str6 = map.get("ETag");
        if (z2) {
            j2 = jCurrentTimeMillis + (1000 * j3);
            j = z ? j2 : (1000 * j4) + j2;
        } else if (jZzg <= 0 || jZzg2 < jZzg) {
            j = 0;
            j2 = 0;
        } else {
            j = (jZzg2 - jZzg) + jCurrentTimeMillis;
            j2 = j;
        }
        zzb.zza zzaVar = new zzb.zza();
        zzaVar.data = zziVar.data;
        zzaVar.zzb = str6;
        zzaVar.zzf = j2;
        zzaVar.zze = j;
        zzaVar.zzc = jZzg;
        zzaVar.zzd = jZzg3;
        zzaVar.zzg = map;
        return zzaVar;
    }

    public static String zzb(Map<String, String> map, String str) {
        String str2 = map.get("Content-Type");
        if (str2 == null) {
            return str;
        }
        String[] strArrSplit = str2.split(";");
        for (int i = 1; i < strArrSplit.length; i++) {
            String[] strArrSplit2 = strArrSplit[i].trim().split("=");
            if (strArrSplit2.length == 2 && strArrSplit2[0].equals("charset")) {
                return strArrSplit2[1];
            }
        }
        return str;
    }

    public static long zzg(String str) {
        try {
            return DateUtils.parseDate(str).getTime();
        } catch (DateParseException e) {
            return 0L;
        }
    }
}
