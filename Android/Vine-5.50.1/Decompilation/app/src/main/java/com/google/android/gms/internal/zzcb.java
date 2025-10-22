package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@zzha
/* loaded from: classes.dex */
public class zzcb {
    final Context mContext;
    final String zzrD;
    String zzwL;
    BlockingQueue<zzch> zzwN;
    ExecutorService zzwO;
    LinkedHashMap<String, String> zzwP = new LinkedHashMap<>();
    Map<String, zzce> zzwQ = new HashMap();
    private AtomicBoolean zzwR = new AtomicBoolean(false);
    private File zzwS;

    public zzcb(Context context, String str, String str2, Map<String, String> map) {
        File externalStorageDirectory;
        this.mContext = context;
        this.zzrD = str;
        this.zzwL = str2;
        this.zzwR.set(zzbz.zzvN.get().booleanValue());
        if (this.zzwR.get() && (externalStorageDirectory = Environment.getExternalStorageDirectory()) != null) {
            this.zzwS = new File(externalStorageDirectory, "sdk_csi_data.txt");
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            this.zzwP.put(entry.getKey(), entry.getValue());
        }
        this.zzwN = new ArrayBlockingQueue(30);
        this.zzwO = Executors.newSingleThreadExecutor();
        this.zzwO.execute(new Runnable() { // from class: com.google.android.gms.internal.zzcb.1
            @Override // java.lang.Runnable
            public void run() throws Throwable {
                zzcb.this.zzdq();
            }
        });
        this.zzwQ.put("action", zzce.zzwV);
        this.zzwQ.put("ad_format", zzce.zzwV);
        this.zzwQ.put("e", zzce.zzwW);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:38:0x003a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void zza(java.io.File r4, java.lang.String r5) throws java.lang.Throwable {
        /*
            r3 = this;
            if (r4 == 0) goto L45
            r2 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.io.IOException -> L22 java.lang.Throwable -> L36
            r0 = 1
            r1.<init>(r4, r0)     // Catch: java.io.IOException -> L22 java.lang.Throwable -> L36
            byte[] r0 = r5.getBytes()     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
            r1.write(r0)     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
            r0 = 10
            r1.write(r0)     // Catch: java.lang.Throwable -> L4b java.io.IOException -> L4d
            if (r1 == 0) goto L1a
            r1.close()     // Catch: java.io.IOException -> L1b
        L1a:
            return
        L1b:
            r0 = move-exception
            java.lang.String r1 = "CsiReporter: Cannot close file: sdk_csi_data.txt."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0)
            goto L1a
        L22:
            r0 = move-exception
            r1 = r2
        L24:
            java.lang.String r2 = "CsiReporter: Cannot write to file: sdk_csi_data.txt."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r0)     // Catch: java.lang.Throwable -> L4b
            if (r1 == 0) goto L1a
            r1.close()     // Catch: java.io.IOException -> L2f
            goto L1a
        L2f:
            r0 = move-exception
            java.lang.String r1 = "CsiReporter: Cannot close file: sdk_csi_data.txt."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r1, r0)
            goto L1a
        L36:
            r0 = move-exception
            r1 = r2
        L38:
            if (r1 == 0) goto L3d
            r1.close()     // Catch: java.io.IOException -> L3e
        L3d:
            throw r0
        L3e:
            r1 = move-exception
            java.lang.String r2 = "CsiReporter: Cannot close file: sdk_csi_data.txt."
            com.google.android.gms.ads.internal.util.client.zzb.zzd(r2, r1)
            goto L3d
        L45:
            java.lang.String r0 = "CsiReporter: File doesn't exists. Cannot write CSI data to file."
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(r0)
            goto L1a
        L4b:
            r0 = move-exception
            goto L38
        L4d:
            r0 = move-exception
            goto L24
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcb.zza(java.io.File, java.lang.String):void");
    }

    private void zzc(Map<String, String> map, String str) throws Throwable {
        String strZza = zza(this.zzwL, map, str);
        if (this.zzwR.get()) {
            zza(this.zzwS, strZza);
        } else {
            com.google.android.gms.ads.internal.zzp.zzbx().zzc(this.mContext, this.zzrD, strZza);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzdq() throws Throwable {
        while (true) {
            try {
                zzch zzchVarTake = this.zzwN.take();
                String strZzdw = zzchVarTake.zzdw();
                if (!TextUtils.isEmpty(strZzdw)) {
                    zzc(zza(this.zzwP, zzchVarTake.zzn()), strZzdw);
                }
            } catch (InterruptedException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("CsiReporter:reporter interrupted", e);
                return;
            }
        }
    }

    public zzce zzP(String str) {
        zzce zzceVar = this.zzwQ.get(str);
        return zzceVar != null ? zzceVar : zzce.zzwU;
    }

    String zza(String str, Map<String, String> map, String str2) {
        Uri.Builder builderBuildUpon = Uri.parse(str).buildUpon();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builderBuildUpon.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        StringBuilder sb = new StringBuilder(builderBuildUpon.build().toString());
        sb.append("&").append("it").append("=").append(str2);
        return sb.toString();
    }

    Map<String, String> zza(Map<String, String> map, Map<String, String> map2) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        if (map2 == null) {
            return linkedHashMap;
        }
        for (Map.Entry<String, String> entry : map2.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            linkedHashMap.put(key, zzP(key).zzc((String) linkedHashMap.get(key), value));
        }
        return linkedHashMap;
    }

    public boolean zza(zzch zzchVar) {
        return this.zzwN.offer(zzchVar);
    }

    public void zzb(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        this.zzwP.put("e", TextUtils.join(",", list));
    }
}
