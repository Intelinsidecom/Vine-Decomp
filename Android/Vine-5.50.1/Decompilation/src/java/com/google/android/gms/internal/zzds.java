package com.google.android.gms.internal;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.overlay.AdLauncherIntentInfoParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public final class zzds implements zzdl {
    private final com.google.android.gms.ads.internal.zze zzzb;
    private final zzfm zzzc;
    private final zzdn zzze;

    public static class zza extends zzil {
        private final String zzF;
        private final zzjn zzps;
        private final String zzzf = "play.google.com";
        private final String zzzg = "market";
        private final int zzzh = 10;

        public zza(zzjn zzjnVar, String str) {
            this.zzps = zzjnVar;
            this.zzF = str;
        }

        @Override // com.google.android.gms.internal.zzil
        public void onStop() {
        }

        public Intent zzY(String str) {
            Uri uri = Uri.parse(str);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(uri);
            return intent;
        }

        /* JADX WARN: Code restructure failed: missing block: B:28:0x008f, code lost:
        
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Arrived at landing page, this ideally should not happen. Will open it in browser.");
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0097, code lost:
        
            r0 = r2;
         */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0116  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x00aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:65:0x008f A[EDGE_INSN: B:65:0x008f->B:28:0x008f BREAK  A[LOOP:0: B:3:0x0003->B:36:0x00ad], SYNTHETIC] */
        @Override // com.google.android.gms.internal.zzil
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void zzbp() {
            /*
                Method dump skipped, instructions count: 284
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzds.zza.zzbp():void");
        }
    }

    public static class zzb {
        public Intent zza(Intent intent, ResolveInfo resolveInfo) {
            Intent intent2 = new Intent(intent);
            intent2.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            return intent2;
        }

        public ResolveInfo zza(Context context, Intent intent) {
            return zza(context, intent, new ArrayList<>());
        }

        public ResolveInfo zza(Context context, Intent intent, ArrayList<ResolveInfo> arrayList) {
            ResolveInfo resolveInfo;
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return null;
            }
            List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
            ResolveInfo resolveInfoResolveActivity = packageManager.resolveActivity(intent, 65536);
            if (listQueryIntentActivities != null && resolveInfoResolveActivity != null) {
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= listQueryIntentActivities.size()) {
                        break;
                    }
                    ResolveInfo resolveInfo2 = listQueryIntentActivities.get(i2);
                    if (resolveInfoResolveActivity != null && resolveInfoResolveActivity.activityInfo.name.equals(resolveInfo2.activityInfo.name)) {
                        resolveInfo = resolveInfoResolveActivity;
                        break;
                    }
                    i = i2 + 1;
                }
            } else {
                resolveInfo = null;
            }
            arrayList.addAll(listQueryIntentActivities);
            return resolveInfo;
        }

        public Intent zzb(Context context, Map<String, String> map) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
            ResolveInfo resolveInfoZza;
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            String str = map.get("u");
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            Uri uri = Uri.parse(str);
            boolean z = Boolean.parseBoolean(map.get("use_first_package"));
            boolean z2 = Boolean.parseBoolean(map.get("use_running_process"));
            Uri uriBuild = "http".equalsIgnoreCase(uri.getScheme()) ? uri.buildUpon().scheme("https").build() : "https".equalsIgnoreCase(uri.getScheme()) ? uri.buildUpon().scheme("http").build() : null;
            ArrayList<ResolveInfo> arrayList = new ArrayList<>();
            Intent intentZzd = zzd(uri);
            Intent intentZzd2 = zzd(uriBuild);
            ResolveInfo resolveInfoZza2 = zza(context, intentZzd, arrayList);
            if (resolveInfoZza2 != null) {
                return zza(intentZzd, resolveInfoZza2);
            }
            if (intentZzd2 != null && (resolveInfoZza = zza(context, intentZzd2)) != null) {
                Intent intentZza = zza(intentZzd, resolveInfoZza);
                if (zza(context, intentZza) != null) {
                    return intentZza;
                }
            }
            if (arrayList.size() == 0) {
                return intentZzd;
            }
            if (z2 && activityManager != null && (runningAppProcesses = activityManager.getRunningAppProcesses()) != null) {
                Iterator<ResolveInfo> it = arrayList.iterator();
                while (it.hasNext()) {
                    ResolveInfo next = it.next();
                    Iterator<ActivityManager.RunningAppProcessInfo> it2 = runningAppProcesses.iterator();
                    while (it2.hasNext()) {
                        if (it2.next().processName.equals(next.activityInfo.packageName)) {
                            return zza(intentZzd, next);
                        }
                    }
                }
            }
            return z ? zza(intentZzd, arrayList.get(0)) : intentZzd;
        }

        public Intent zzd(Uri uri) {
            if (uri == null) {
                return null;
            }
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(268435456);
            intent.setData(uri);
            intent.setAction("android.intent.action.VIEW");
            return intent;
        }
    }

    public zzds(zzdn zzdnVar, com.google.android.gms.ads.internal.zze zzeVar, zzfm zzfmVar) {
        this.zzze = zzdnVar;
        this.zzzb = zzeVar;
        this.zzzc = zzfmVar;
    }

    private static void zza(Context context, Map<String, String> map) {
        if (TextUtils.isEmpty(map.get("u"))) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Destination url cannot be empty.");
            return;
        }
        try {
            com.google.android.gms.ads.internal.zzp.zzbx().zzb(context, new zzb().zzb(context, map));
        } catch (ActivityNotFoundException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(e.getMessage());
        }
    }

    private static boolean zzc(Map<String, String> map) {
        return "1".equals(map.get("custom_close"));
    }

    private static int zzd(Map<String, String> map) {
        String str = map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return com.google.android.gms.ads.internal.zzp.zzbz().zzhe();
            }
            if ("l".equalsIgnoreCase(str)) {
                return com.google.android.gms.ads.internal.zzp.zzbz().zzhd();
            }
            if ("c".equalsIgnoreCase(str)) {
                return com.google.android.gms.ads.internal.zzp.zzbz().zzhf();
            }
        }
        return -1;
    }

    private static void zze(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("u");
        if (TextUtils.isEmpty(str)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Destination url cannot be empty.");
        } else {
            new zza(zzjnVar, str).zzfR();
        }
    }

    private void zzo(boolean z) {
        if (this.zzzc != null) {
            this.zzzc.zzp(z);
        }
    }

    @Override // com.google.android.gms.internal.zzdl
    public void zza(zzjn zzjnVar, Map<String, String> map) {
        String str = map.get("a");
        if (str == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Action missing from an open GMSG.");
            return;
        }
        if (this.zzzb != null && !this.zzzb.zzbg()) {
            this.zzzb.zzp(map.get("u"));
            return;
        }
        zzjo zzjoVarZzhC = zzjnVar.zzhC();
        if ("expand".equalsIgnoreCase(str)) {
            if (zzjnVar.zzhG()) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Cannot expand WebView that is already expanded.");
                return;
            } else {
                zzo(false);
                zzjoVarZzhC.zza(zzc(map), zzd(map));
                return;
            }
        }
        if ("webapp".equalsIgnoreCase(str)) {
            String str2 = map.get("u");
            zzo(false);
            if (str2 != null) {
                zzjoVarZzhC.zza(zzc(map), zzd(map), str2);
                return;
            } else {
                zzjoVarZzhC.zza(zzc(map), zzd(map), map.get("html"), map.get("baseurl"));
                return;
            }
        }
        if ("in_app_purchase".equalsIgnoreCase(str)) {
            String str3 = map.get("product_id");
            String str4 = map.get("report_urls");
            if (this.zzze != null) {
                if (str4 == null || str4.isEmpty()) {
                    this.zzze.zza(str3, new ArrayList<>());
                    return;
                } else {
                    this.zzze.zza(str3, new ArrayList<>(Arrays.asList(str4.split(" "))));
                    return;
                }
            }
            return;
        }
        if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase(map.get("play_store"))) {
            zze(zzjnVar, map);
            return;
        }
        if ("app".equalsIgnoreCase(str) && "true".equalsIgnoreCase(map.get("system_browser"))) {
            zza(zzjnVar.getContext(), map);
            return;
        }
        zzo(true);
        zzjnVar.zzhE();
        String str5 = map.get("u");
        zzjoVarZzhC.zza(new AdLauncherIntentInfoParcel(map.get("i"), !TextUtils.isEmpty(str5) ? com.google.android.gms.ads.internal.zzp.zzbx().zza(zzjnVar, str5) : str5, map.get("m"), map.get("p"), map.get("c"), map.get("f"), map.get("e")));
    }
}
