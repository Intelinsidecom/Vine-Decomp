package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import com.googlecode.mp4parser.boxes.apple.TrackLoadSettingsAtom;
import java.util.Map;
import java.util.WeakHashMap;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public final class zzdt implements zzdl {
    private final Map<zzjn, Integer> zzzi = new WeakHashMap();

    private static int zza(Context context, Map<String, String> map, String str, int i) {
        String str2 = map.get(str);
        if (str2 == null) {
            return i;
        }
        try {
            return com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(context, Integer.parseInt(str2));
        } catch (NumberFormatException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse " + str + " in a video GMSG: " + str2);
            return i;
        }
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) throws NumberFormatException {
        int i;
        com.google.android.gms.ads.internal.overlay.zzk zzkVarZzhv;
        String str = map.get("action");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Action missing from video GMSG.");
            return;
        }
        if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(3)) {
            JSONObject jSONObject = new JSONObject(map);
            jSONObject.remove("google.afma.Notify_dt");
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Video GMSG: " + str + " " + jSONObject.toString());
        }
        if ("background".equals(str)) {
            String str2 = map.get("color");
            if (TextUtils.isEmpty(str2)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Color parameter missing from color video GMSG.");
                return;
            }
            try {
                int color = Color.parseColor(str2);
                zzjm zzjmVarZzhJ = zzjnVar.zzhJ();
                if (zzjmVarZzhJ == null || (zzkVarZzhv = zzjmVarZzhJ.zzhv()) == null) {
                    this.zzzi.put(zzjnVar, Integer.valueOf(color));
                } else {
                    zzkVarZzhv.setBackgroundColor(color);
                }
                return;
            } catch (IllegalArgumentException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Invalid color parameter in video GMSG.");
                return;
            }
        }
        zzjm zzjmVarZzhJ2 = zzjnVar.zzhJ();
        if (zzjmVarZzhJ2 == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not get underlay container for a video GMSG.");
            return;
        }
        boolean zEquals = "new".equals(str);
        boolean zEquals2 = "position".equals(str);
        if (zEquals || zEquals2) {
            Context context = zzjnVar.getContext();
            int iZza = zza(context, map, "x", 0);
            int iZza2 = zza(context, map, "y", 0);
            int iZza3 = zza(context, map, "w", -1);
            int iZza4 = zza(context, map, "h", -1);
            try {
                i = Integer.parseInt(map.get("player"));
            } catch (NumberFormatException e2) {
                i = 0;
            }
            if (!zEquals || zzjmVarZzhJ2.zzhv() != null) {
                zzjmVarZzhJ2.zze(iZza, iZza2, iZza3, iZza4);
                return;
            }
            zzjmVarZzhJ2.zza(iZza, iZza2, iZza3, iZza4, i);
            if (this.zzzi.containsKey(zzjnVar)) {
                int iIntValue = this.zzzi.get(zzjnVar).intValue();
                com.google.android.gms.ads.internal.overlay.zzk zzkVarZzhv2 = zzjmVarZzhJ2.zzhv();
                zzkVarZzhv2.setBackgroundColor(iIntValue);
                zzkVarZzhv2.zzft();
                return;
            }
            return;
        }
        com.google.android.gms.ads.internal.overlay.zzk zzkVarZzhv3 = zzjmVarZzhJ2.zzhv();
        if (zzkVarZzhv3 == null) {
            com.google.android.gms.ads.internal.overlay.zzk.zzd(zzjnVar);
            return;
        }
        if ("click".equals(str)) {
            Context context2 = zzjnVar.getContext();
            int iZza5 = zza(context2, map, "x", 0);
            int iZza6 = zza(context2, map, "y", 0);
            long jUptimeMillis = SystemClock.uptimeMillis();
            MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 0, iZza5, iZza6, 0);
            zzkVarZzhv3.zzd(motionEventObtain);
            motionEventObtain.recycle();
            return;
        }
        if ("currentTime".equals(str)) {
            String str3 = map.get("time");
            if (str3 == null) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                zzkVarZzhv3.seekTo((int) (Float.parseFloat(str3) * 1000.0f));
                return;
            } catch (NumberFormatException e3) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse time parameter from currentTime video GMSG: " + str3);
                return;
            }
        }
        if ("hide".equals(str)) {
            zzkVarZzhv3.setVisibility(4);
            return;
        }
        if (TrackLoadSettingsAtom.TYPE.equals(str)) {
            zzkVarZzhv3.zzfs();
            return;
        }
        if ("mimetype".equals(str)) {
            zzkVarZzhv3.setMimeType(map.get("mimetype"));
            return;
        }
        if ("muted".equals(str)) {
            if (Boolean.parseBoolean(map.get("muted"))) {
                zzkVarZzhv3.zzeU();
                return;
            } else {
                zzkVarZzhv3.zzeV();
                return;
            }
        }
        if ("pause".equals(str)) {
            zzkVarZzhv3.pause();
            return;
        }
        if ("play".equals(str)) {
            zzkVarZzhv3.play();
            return;
        }
        if ("show".equals(str)) {
            zzkVarZzhv3.setVisibility(0);
            return;
        }
        if ("src".equals(str)) {
            zzkVarZzhv3.zzao(map.get("src"));
            return;
        }
        if (!"volume".equals(str)) {
            if ("watermark".equals(str)) {
                zzkVarZzhv3.zzft();
                return;
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Unknown video action: " + str);
                return;
            }
        }
        String str4 = map.get("volume");
        if (str4 == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Level parameter missing from volume video GMSG.");
            return;
        }
        try {
            zzkVarZzhv3.zza(Float.parseFloat(str4));
        } catch (NumberFormatException e4) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse volume parameter from volume video GMSG: " + str4);
        }
    }
}
