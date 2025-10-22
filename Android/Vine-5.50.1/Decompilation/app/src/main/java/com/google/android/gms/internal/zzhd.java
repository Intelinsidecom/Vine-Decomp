package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.client.SearchAdRequestParcel;
import com.google.android.gms.ads.internal.formats.NativeAdOptionsParcel;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.internal.zzhm;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzhd {
    private static final SimpleDateFormat zzHU = new SimpleDateFormat("yyyyMMdd", Locale.US);

    private static String zzL(int i) {
        return String.format(Locale.US, "#%06x", Integer.valueOf(16777215 & i));
    }

    /* JADX WARN: Removed duplicated region for block: B:86:0x024e A[PHI: r18
  0x024e: PHI (r18v3 int) = (r18v2 int), (r18v5 int) binds: [B:74:0x01ab, B:79:0x01bc] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.google.android.gms.ads.internal.request.AdResponseParcel zza(android.content.Context r34, com.google.android.gms.ads.internal.request.AdRequestInfoParcel r35, java.lang.String r36) {
        /*
            Method dump skipped, instructions count: 607
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzhd.zza(android.content.Context, com.google.android.gms.ads.internal.request.AdRequestInfoParcel, java.lang.String):com.google.android.gms.ads.internal.request.AdResponseParcel");
    }

    public static JSONObject zza(Context context, AdRequestInfoParcel adRequestInfoParcel, zzhi zzhiVar, zzhm.zza zzaVar, Location location, zzbs zzbsVar, String str, String str2, List<String> list, Bundle bundle) {
        try {
            HashMap map = new HashMap();
            if (list.size() > 0) {
                map.put("eid", TextUtils.join(",", list));
            }
            if (adRequestInfoParcel.zzGp != null) {
                map.put("ad_pos", adRequestInfoParcel.zzGp);
            }
            zza((HashMap<String, Object>) map, adRequestInfoParcel.zzGq);
            map.put("format", adRequestInfoParcel.zzqV.zztV);
            if (adRequestInfoParcel.zzqV.width == -1) {
                map.put("smart_w", "full");
            }
            if (adRequestInfoParcel.zzqV.height == -2) {
                map.put("smart_h", "auto");
            }
            if (adRequestInfoParcel.zzqV.zztZ) {
                map.put("fluid", "height");
            }
            if (adRequestInfoParcel.zzqV.zztX != null) {
                StringBuilder sb = new StringBuilder();
                for (AdSizeParcel adSizeParcel : adRequestInfoParcel.zzqV.zztX) {
                    if (sb.length() != 0) {
                        sb.append("|");
                    }
                    sb.append(adSizeParcel.width == -1 ? (int) (adSizeParcel.widthPixels / zzhiVar.zzGC) : adSizeParcel.width);
                    sb.append("x");
                    sb.append(adSizeParcel.height == -2 ? (int) (adSizeParcel.heightPixels / zzhiVar.zzGC) : adSizeParcel.height);
                }
                map.put("sz", sb);
            }
            if (adRequestInfoParcel.zzGw != 0) {
                map.put("native_version", Integer.valueOf(adRequestInfoParcel.zzGw));
                if (!adRequestInfoParcel.zzqV.zzua) {
                    map.put("native_templates", adRequestInfoParcel.zzrl);
                    map.put("native_image_orientation", zzc(adRequestInfoParcel.zzrj));
                    if (!adRequestInfoParcel.zzGH.isEmpty()) {
                        map.put("native_custom_templates", adRequestInfoParcel.zzGH);
                    }
                }
            }
            map.put("slotname", adRequestInfoParcel.zzqP);
            map.put("pn", adRequestInfoParcel.applicationInfo.packageName);
            if (adRequestInfoParcel.zzGr != null) {
                map.put("vc", Integer.valueOf(adRequestInfoParcel.zzGr.versionCode));
            }
            map.put("ms", str2);
            map.put("seq_num", adRequestInfoParcel.zzGt);
            map.put("session_id", adRequestInfoParcel.zzGu);
            map.put("js", adRequestInfoParcel.zzqR.afmaVersion);
            zza((HashMap<String, Object>) map, zzhiVar, zzaVar);
            map.put("platform", Build.MANUFACTURER);
            map.put("submodel", Build.MODEL);
            if (adRequestInfoParcel.zzGq.versionCode >= 2 && adRequestInfoParcel.zzGq.zzty != null) {
                zza((HashMap<String, Object>) map, adRequestInfoParcel.zzGq.zzty);
            }
            if (adRequestInfoParcel.versionCode >= 2) {
                map.put("quality_signals", adRequestInfoParcel.zzGv);
            }
            if (adRequestInfoParcel.versionCode >= 4 && adRequestInfoParcel.zzGy) {
                map.put("forceHttps", Boolean.valueOf(adRequestInfoParcel.zzGy));
            }
            if (bundle != null) {
                map.put("content_info", bundle);
            }
            if (adRequestInfoParcel.versionCode >= 5) {
                map.put("u_sd", Float.valueOf(adRequestInfoParcel.zzGC));
                map.put("sh", Integer.valueOf(adRequestInfoParcel.zzGB));
                map.put("sw", Integer.valueOf(adRequestInfoParcel.zzGA));
            } else {
                map.put("u_sd", Float.valueOf(zzhiVar.zzGC));
                map.put("sh", Integer.valueOf(zzhiVar.zzGB));
                map.put("sw", Integer.valueOf(zzhiVar.zzGA));
            }
            if (adRequestInfoParcel.versionCode >= 6) {
                if (!TextUtils.isEmpty(adRequestInfoParcel.zzGD)) {
                    try {
                        map.put("view_hierarchy", new JSONObject(adRequestInfoParcel.zzGD));
                    } catch (JSONException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzd("Problem serializing view hierarchy to JSON", e);
                    }
                }
                map.put("correlation_id", Long.valueOf(adRequestInfoParcel.zzGE));
            }
            if (adRequestInfoParcel.versionCode >= 7) {
                map.put("request_id", adRequestInfoParcel.zzGF);
            }
            if (adRequestInfoParcel.versionCode >= 11 && adRequestInfoParcel.zzGJ != null) {
                map.put("capability", adRequestInfoParcel.zzGJ.toBundle());
            }
            zza((HashMap<String, Object>) map, str);
            if (adRequestInfoParcel.versionCode >= 12 && !TextUtils.isEmpty(adRequestInfoParcel.zzGK)) {
                map.put("anchor", adRequestInfoParcel.zzGK);
            }
            if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
                com.google.android.gms.ads.internal.util.client.zzb.v("Ad Request JSON: " + com.google.android.gms.ads.internal.zzp.zzbx().zzz(map).toString(2));
            }
            return com.google.android.gms.ads.internal.zzp.zzbx().zzz(map);
        } catch (JSONException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Problem serializing ad request to JSON: " + e2.getMessage());
            return null;
        }
    }

    private static void zza(HashMap<String, Object> map, Location location) {
        HashMap map2 = new HashMap();
        Float fValueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
        Long lValueOf = Long.valueOf(location.getTime() * 1000);
        Long lValueOf2 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
        Long lValueOf3 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
        map2.put("radius", fValueOf);
        map2.put("lat", lValueOf2);
        map2.put("long", lValueOf3);
        map2.put("time", lValueOf);
        map.put("uule", map2);
    }

    private static void zza(HashMap<String, Object> map, AdRequestParcel adRequestParcel) {
        String strZzgW = zzik.zzgW();
        if (strZzgW != null) {
            map.put("abf", strZzgW);
        }
        if (adRequestParcel.zztq != -1) {
            map.put("cust_age", zzHU.format(new Date(adRequestParcel.zztq)));
        }
        if (adRequestParcel.extras != null) {
            map.put("extras", adRequestParcel.extras);
        }
        if (adRequestParcel.zztr != -1) {
            map.put("cust_gender", Integer.valueOf(adRequestParcel.zztr));
        }
        if (adRequestParcel.zzts != null) {
            map.put("kw", adRequestParcel.zzts);
        }
        if (adRequestParcel.zztu != -1) {
            map.put("tag_for_child_directed_treatment", Integer.valueOf(adRequestParcel.zztu));
        }
        if (adRequestParcel.zztt) {
            map.put("adtest", "on");
        }
        if (adRequestParcel.versionCode >= 2) {
            if (adRequestParcel.zztv) {
                map.put("d_imp_hdr", 1);
            }
            if (!TextUtils.isEmpty(adRequestParcel.zztw)) {
                map.put("ppid", adRequestParcel.zztw);
            }
            if (adRequestParcel.zztx != null) {
                zza(map, adRequestParcel.zztx);
            }
        }
        if (adRequestParcel.versionCode >= 3 && adRequestParcel.zztz != null) {
            map.put("url", adRequestParcel.zztz);
        }
        if (adRequestParcel.versionCode >= 5) {
            if (adRequestParcel.zztB != null) {
                map.put("custom_targeting", adRequestParcel.zztB);
            }
            if (adRequestParcel.zztC != null) {
                map.put("category_exclusions", adRequestParcel.zztC);
            }
            if (adRequestParcel.zztD != null) {
                map.put("request_agent", adRequestParcel.zztD);
            }
        }
        if (adRequestParcel.versionCode >= 6 && adRequestParcel.zztE != null) {
            map.put("request_pkg", adRequestParcel.zztE);
        }
        if (adRequestParcel.versionCode >= 7) {
            map.put("is_designed_for_families", Boolean.valueOf(adRequestParcel.zztF));
        }
    }

    private static void zza(HashMap<String, Object> map, SearchAdRequestParcel searchAdRequestParcel) {
        String str;
        String str2 = null;
        if (Color.alpha(searchAdRequestParcel.zzuI) != 0) {
            map.put("acolor", zzL(searchAdRequestParcel.zzuI));
        }
        if (Color.alpha(searchAdRequestParcel.backgroundColor) != 0) {
            map.put("bgcolor", zzL(searchAdRequestParcel.backgroundColor));
        }
        if (Color.alpha(searchAdRequestParcel.zzuJ) != 0 && Color.alpha(searchAdRequestParcel.zzuK) != 0) {
            map.put("gradientto", zzL(searchAdRequestParcel.zzuJ));
            map.put("gradientfrom", zzL(searchAdRequestParcel.zzuK));
        }
        if (Color.alpha(searchAdRequestParcel.zzuL) != 0) {
            map.put("bcolor", zzL(searchAdRequestParcel.zzuL));
        }
        map.put("bthick", Integer.toString(searchAdRequestParcel.zzuM));
        switch (searchAdRequestParcel.zzuN) {
            case 0:
                str = "none";
                break;
            case 1:
                str = "dashed";
                break;
            case 2:
                str = "dotted";
                break;
            case 3:
                str = "solid";
                break;
            default:
                str = null;
                break;
        }
        if (str != null) {
            map.put("btype", str);
        }
        switch (searchAdRequestParcel.zzuO) {
            case 0:
                str2 = "light";
                break;
            case 1:
                str2 = "medium";
                break;
            case 2:
                str2 = "dark";
                break;
        }
        if (str2 != null) {
            map.put("callbuttoncolor", str2);
        }
        if (searchAdRequestParcel.zzuP != null) {
            map.put("channel", searchAdRequestParcel.zzuP);
        }
        if (Color.alpha(searchAdRequestParcel.zzuQ) != 0) {
            map.put("dcolor", zzL(searchAdRequestParcel.zzuQ));
        }
        if (searchAdRequestParcel.zzuR != null) {
            map.put("font", searchAdRequestParcel.zzuR);
        }
        if (Color.alpha(searchAdRequestParcel.zzuS) != 0) {
            map.put("hcolor", zzL(searchAdRequestParcel.zzuS));
        }
        map.put("headersize", Integer.toString(searchAdRequestParcel.zzuT));
        if (searchAdRequestParcel.zzuU != null) {
            map.put("q", searchAdRequestParcel.zzuU);
        }
    }

    private static void zza(HashMap<String, Object> map, zzhi zzhiVar, zzhm.zza zzaVar) {
        map.put("am", Integer.valueOf(zzhiVar.zzIA));
        map.put("cog", zzy(zzhiVar.zzIB));
        map.put("coh", zzy(zzhiVar.zzIC));
        if (!TextUtils.isEmpty(zzhiVar.zzID)) {
            map.put("carrier", zzhiVar.zzID);
        }
        map.put("gl", zzhiVar.zzIE);
        if (zzhiVar.zzIF) {
            map.put("simulator", 1);
        }
        if (zzhiVar.zzIG) {
            map.put("is_sidewinder", 1);
        }
        map.put("ma", zzy(zzhiVar.zzIH));
        map.put("sp", zzy(zzhiVar.zzII));
        map.put("hl", zzhiVar.zzIJ);
        if (!TextUtils.isEmpty(zzhiVar.zzIK)) {
            map.put("mv", zzhiVar.zzIK);
        }
        map.put("muv", Integer.valueOf(zzhiVar.zzIL));
        if (zzhiVar.zzIM != -2) {
            map.put("cnt", Integer.valueOf(zzhiVar.zzIM));
        }
        map.put("gnt", Integer.valueOf(zzhiVar.zzIN));
        map.put("pt", Integer.valueOf(zzhiVar.zzIO));
        map.put("rm", Integer.valueOf(zzhiVar.zzIP));
        map.put("riv", Integer.valueOf(zzhiVar.zzIQ));
        Bundle bundle = new Bundle();
        bundle.putString("build", zzhiVar.zzIV);
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("is_charging", zzhiVar.zzIS);
        bundle2.putDouble("battery_level", zzhiVar.zzIR);
        bundle.putBundle("battery", bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putInt("active_network_state", zzhiVar.zzIU);
        bundle3.putBoolean("active_network_metered", zzhiVar.zzIT);
        if (zzaVar != null) {
            Bundle bundle4 = new Bundle();
            bundle4.putInt("predicted_latency_micros", zzaVar.zzJa);
            bundle4.putLong("predicted_down_throughput_bps", zzaVar.zzJb);
            bundle4.putLong("predicted_up_throughput_bps", zzaVar.zzJc);
            bundle3.putBundle("predictions", bundle4);
        }
        bundle.putBundle("network", bundle3);
        map.put("device", bundle);
    }

    private static void zza(HashMap<String, Object> map, String str) {
        if (str != null) {
            HashMap map2 = new HashMap();
            map2.put("token", str);
            map.put("pan", map2);
        }
    }

    private static String zzc(NativeAdOptionsParcel nativeAdOptionsParcel) {
        switch (nativeAdOptionsParcel != null ? nativeAdOptionsParcel.zzyd : 0) {
            case 1:
                return "portrait";
            case 2:
                return "landscape";
            default:
                return "any";
        }
    }

    private static Integer zzy(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}
