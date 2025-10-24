package com.google.android.gms.measurement.internal;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzqq;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.measurement.AppMeasurementReceiver;
import com.google.android.gms.measurement.AppMeasurementService;
import com.google.android.gms.measurement.internal.zzp;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class zzt {
    private static zzx zzaUg;
    private static volatile zzt zzaUh;
    private final Context mContext;
    private final boolean zzOQ;
    private final zzc zzaUi;
    private final zzr zzaUj;
    private final zzo zzaUk;
    private final zzs zzaUl;
    private final AppMeasurement zzaUm;
    private final zzae zzaUn;
    private final zzd zzaUo;
    private final zzp zzaUp;
    private final zzz zzaUq;
    private final zzf zzaUr;
    private final zzy zzaUs;
    private final zzm zzaUt;
    private final zzq zzaUu;
    private final zzab zzaUv;
    private Boolean zzaUw;
    private List<Long> zzaUx;
    private int zzaUy;
    private int zzaUz;
    private final zznl zzqD;

    zzt(zzx zzxVar) throws IllegalStateException {
        com.google.android.gms.common.internal.zzx.zzy(zzxVar);
        this.mContext = zzxVar.mContext;
        this.zzqD = zzxVar.zzj(this);
        this.zzaUi = zzxVar.zza(this);
        zzr zzrVarZzb = zzxVar.zzb(this);
        zzrVarZzb.zza();
        this.zzaUj = zzrVarZzb;
        zzo zzoVarZzc = zzxVar.zzc(this);
        zzoVarZzc.zza();
        this.zzaUk = zzoVarZzc;
        this.zzaUn = zzxVar.zzg(this);
        zzf zzfVarZzl = zzxVar.zzl(this);
        zzfVarZzl.zza();
        this.zzaUr = zzfVarZzl;
        zzm zzmVarZzm = zzxVar.zzm(this);
        zzmVarZzm.zza();
        this.zzaUt = zzmVarZzm;
        zzd zzdVarZzh = zzxVar.zzh(this);
        zzdVarZzh.zza();
        this.zzaUo = zzdVarZzh;
        zzp zzpVarZzi = zzxVar.zzi(this);
        zzpVarZzi.zza();
        this.zzaUp = zzpVarZzi;
        zzz zzzVarZzk = zzxVar.zzk(this);
        zzzVarZzk.zza();
        this.zzaUq = zzzVarZzk;
        zzy zzyVarZzf = zzxVar.zzf(this);
        zzyVarZzf.zza();
        this.zzaUs = zzyVarZzf;
        zzab zzabVarZzo = zzxVar.zzo(this);
        zzabVarZzo.zza();
        this.zzaUv = zzabVarZzo;
        this.zzaUu = zzxVar.zzn(this);
        this.zzaUm = zzxVar.zze(this);
        zzs zzsVarZzd = zzxVar.zzd(this);
        zzsVarZzd.zza();
        this.zzaUl = zzsVarZzd;
        if (this.zzaUy != this.zzaUz) {
            zzzz().zzBl().zze("Not all components initialized", Integer.valueOf(this.zzaUy), Integer.valueOf(this.zzaUz));
        }
        this.zzOQ = true;
        if (!this.zzaUi.zzka() && !zzBI()) {
            if (!(this.mContext.getApplicationContext() instanceof Application)) {
                zzzz().zzBm().zzez("Application context is not an Application");
            } else if (Build.VERSION.SDK_INT >= 14) {
                zzBB().zzBR();
            } else {
                zzzz().zzBq().zzez("Not tracking deep linking pre-ICS");
            }
        }
        this.zzaUl.zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzt.1
            @Override // java.lang.Runnable
            public void run() {
                zzt.this.start();
            }
        });
    }

    private boolean zzBJ() {
        zziS();
        return this.zzaUx != null;
    }

    private boolean zzBL() {
        return !TextUtils.isEmpty(zzBD().zzAY());
    }

    private void zzBM() {
        zziS();
        zzje();
        if (!zzBz() || !zzBL()) {
            zzBG().unregister();
            zzBH().cancel();
            return;
        }
        long jZzBN = zzBN();
        if (jZzBN == 0) {
            zzBG().unregister();
            zzBH().cancel();
            return;
        }
        if (!zzBE().zzlk()) {
            zzBG().zzlh();
            zzBH().cancel();
            return;
        }
        long j = zzAW().zzaTH.get();
        long jZzAL = zzAX().zzAL();
        if (!zzAU().zzc(j, jZzAL)) {
            jZzBN = Math.max(jZzBN, j + jZzAL);
        }
        zzBG().unregister();
        long jCurrentTimeMillis = jZzBN - zziT().currentTimeMillis();
        if (jCurrentTimeMillis <= 0) {
            zzBH().zzt(1L);
        } else {
            zzzz().zzBr().zzj("Upload scheduled in approximately ms", Long.valueOf(jCurrentTimeMillis));
            zzBH().zzt(jCurrentTimeMillis);
        }
    }

    private long zzBN() {
        long jCurrentTimeMillis = zziT().currentTimeMillis();
        long jZzAO = zzAX().zzAO();
        long jZzAM = zzAX().zzAM();
        long j = zzAW().zzaTF.get();
        long j2 = zzAW().zzaTG.get();
        long jZzBb = zzBD().zzBb();
        if (jZzBb == 0) {
            return 0L;
        }
        long jAbs = jCurrentTimeMillis - Math.abs(jZzBb - jCurrentTimeMillis);
        long jZzAP = jZzAO + jAbs;
        if (!zzAU().zzc(j, jZzAM)) {
            jZzAP = j + jZzAM;
        }
        if (j2 == 0 || j2 < jAbs) {
            return jZzAP;
        }
        for (int i = 0; i < zzAX().zzAQ(); i++) {
            jZzAP += (1 << i) * zzAX().zzAP();
            if (jZzAP > j2) {
                return jZzAP;
            }
        }
        return 0L;
    }

    private void zza(zzv zzvVar) {
        if (zzvVar == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private void zza(zzw zzwVar) {
        if (zzwVar == null) {
            throw new IllegalStateException("Component not created");
        }
        if (!zzwVar.isInitialized()) {
            throw new IllegalStateException("Component not initialized");
        }
    }

    public static zzt zzaU(Context context) {
        com.google.android.gms.common.internal.zzx.zzy(context);
        com.google.android.gms.common.internal.zzx.zzy(context.getApplicationContext());
        if (zzaUh == null) {
            synchronized (zzt.class) {
                if (zzaUh == null) {
                    zzaUh = (zzaUg != null ? zzaUg : new zzx(context)).zzBQ();
                }
            }
        }
        return zzaUh;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzb(int i, Throwable th, byte[] bArr) {
        zziS();
        zzje();
        if (bArr == null) {
            bArr = new byte[0];
        }
        List<Long> list = this.zzaUx;
        this.zzaUx = null;
        if ((i != 200 && i != 204) || th != null) {
            zzzz().zzBr().zze("Network upload failed. Will retry later. code, error", Integer.valueOf(i), th);
            zzAW().zzaTG.set(zziT().currentTimeMillis());
            if (i == 503 || i == 429) {
                zzAW().zzaTH.set(zziT().currentTimeMillis());
            }
            zzBM();
            return;
        }
        zzAW().zzaTF.set(zziT().currentTimeMillis());
        zzAW().zzaTG.set(0L);
        zzBM();
        zzzz().zzBr().zze("Successful upload. Got network response. code, size", Integer.valueOf(i), Integer.valueOf(bArr.length));
        zzBD().beginTransaction();
        try {
            Iterator<Long> it = list.iterator();
            while (it.hasNext()) {
                zzBD().zzP(it.next().longValue());
            }
            zzBD().setTransactionSuccessful();
            zzBD().endTransaction();
            if (zzBE().zzlk() && zzBL()) {
                zzBK();
            } else {
                zzBM();
            }
        } catch (Throwable th2) {
            zzBD().endTransaction();
            throw th2;
        }
    }

    private void zze(AppMetadata appMetadata) throws NoSuchAlgorithmException {
        zziS();
        zzje();
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        com.google.android.gms.common.internal.zzx.zzcG(appMetadata.packageName);
        zza zzaVarZzew = zzBD().zzew(appMetadata.packageName);
        String strZzBu = zzAW().zzBu();
        boolean z = false;
        if (zzaVarZzew == null) {
            zzaVarZzew = new zza(appMetadata.packageName, zzAW().zzBv(), appMetadata.zzaSn, strZzBu, 0L, 0L, appMetadata.zzaKi, appMetadata.zzaSo, appMetadata.zzaSp, appMetadata.zzaSq, appMetadata.zzaSs);
            z = true;
        } else if (!strZzBu.equals(zzaVarZzew.zzaSg)) {
            zzaVarZzew = zzaVarZzew.zzJ(zzAW().zzBv(), strZzBu);
            z = true;
        }
        if (!TextUtils.isEmpty(appMetadata.zzaSn) && (!appMetadata.zzaSn.equals(zzaVarZzew.zzaSf) || appMetadata.zzaSp != zzaVarZzew.zzaSk)) {
            zzaVarZzew = zzaVarZzew.zze(appMetadata.zzaSn, appMetadata.zzaSp);
            z = true;
        }
        if (!TextUtils.isEmpty(appMetadata.zzaKi) && (!appMetadata.zzaKi.equals(zzaVarZzew.zzRl) || !appMetadata.zzaSo.equals(zzaVarZzew.zzaSj))) {
            zzaVarZzew = zzaVarZzew.zzK(appMetadata.zzaKi, appMetadata.zzaSo);
            z = true;
        }
        if (appMetadata.zzaSq != zzaVarZzew.zzaSl) {
            zzaVarZzew = zzaVarZzew.zzO(appMetadata.zzaSq);
            z = true;
        }
        if (appMetadata.zzaSs != zzaVarZzew.zzaSm) {
            zzaVarZzew = zzaVarZzew.zzao(appMetadata.zzaSs);
            z = true;
        }
        if (z) {
            zzBD().zza(zzaVarZzew);
        }
    }

    private void zzx(List<Long> list) {
        com.google.android.gms.common.internal.zzx.zzab(!list.isEmpty());
        if (this.zzaUx != null) {
            zzzz().zzBl().zzez("Set uploading progress before finishing the previous upload");
        } else {
            this.zzaUx = new ArrayList(list);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    protected void start() {
        zziS();
        zzzz().zzBp().zzez("App measurement is starting up");
        zzzz().zzBq().zzez("Debug logging enabled");
        if (zzBI() && (!this.zzaUl.isInitialized() || this.zzaUl.zzBP())) {
            zzzz().zzBl().zzez("Scheduler shutting down before Scion.start() called");
            return;
        }
        zzBD().zzAZ();
        if (!zzBz()) {
            if (!zzAU().zzbh("android.permission.INTERNET")) {
                zzzz().zzBl().zzez("App is missing INTERNET permission");
            }
            if (!zzAU().zzbh("android.permission.ACCESS_NETWORK_STATE")) {
                zzzz().zzBl().zzez("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!AppMeasurementReceiver.zzX(getContext())) {
                zzzz().zzBl().zzez("AppMeasurementReceiver not registered/enabled");
            }
            if (!AppMeasurementService.zzY(getContext())) {
                zzzz().zzBl().zzez("AppMeasurementService not registered/enabled");
            }
            zzzz().zzBl().zzez("Uploading is not possible. App measurement disabled");
        } else if (!zzAX().zzka() && !zzBI() && !TextUtils.isEmpty(zzAS().zzBi())) {
            zzBB().zzBS();
        }
        zzBM();
    }

    public zzm zzAS() {
        zza((zzw) this.zzaUt);
        return this.zzaUt;
    }

    public zzz zzAT() {
        zza((zzw) this.zzaUq);
        return this.zzaUq;
    }

    public zzae zzAU() {
        zza(this.zzaUn);
        return this.zzaUn;
    }

    public zzs zzAV() {
        zza((zzw) this.zzaUl);
        return this.zzaUl;
    }

    public zzr zzAW() {
        zza((zzv) this.zzaUj);
        return this.zzaUj;
    }

    public zzc zzAX() {
        return this.zzaUi;
    }

    zzs zzBA() {
        return this.zzaUl;
    }

    public zzy zzBB() {
        zza((zzw) this.zzaUs);
        return this.zzaUs;
    }

    public zzd zzBD() {
        zza((zzw) this.zzaUo);
        return this.zzaUo;
    }

    public zzp zzBE() {
        zza((zzw) this.zzaUp);
        return this.zzaUp;
    }

    public zzf zzBF() {
        zza((zzw) this.zzaUr);
        return this.zzaUr;
    }

    public zzq zzBG() {
        if (this.zzaUu == null) {
            throw new IllegalStateException("Network broadcast receiver not created");
        }
        return this.zzaUu;
    }

    public zzab zzBH() {
        zza((zzw) this.zzaUv);
        return this.zzaUv;
    }

    protected boolean zzBI() {
        return false;
    }

    public void zzBK() {
        String str;
        List<Pair<zzqq.zzd, Long>> listSubList;
        zziS();
        zzje();
        if (!zzAX().zzka()) {
            Boolean boolZzBx = zzAW().zzBx();
            if (boolZzBx == null) {
                zzzz().zzBm().zzez("Upload data called on the client side before use of service was decided");
                return;
            } else if (boolZzBx.booleanValue()) {
                zzzz().zzBl().zzez("Upload called in the client side when service should be used");
                return;
            }
        }
        if (zzBJ()) {
            zzzz().zzBm().zzez("Uploading requested multiple times");
            return;
        }
        if (!zzBE().zzlk()) {
            zzzz().zzBm().zzez("Network not connected, ignoring upload request");
            zzBM();
            return;
        }
        long j = zzAW().zzaTF.get();
        if (j != 0) {
            zzzz().zzBq().zzj("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(zziT().currentTimeMillis() - j)));
        }
        String strZzAY = zzBD().zzAY();
        if (TextUtils.isEmpty(strZzAY)) {
            return;
        }
        List<Pair<zzqq.zzd, Long>> listZzn = zzBD().zzn(strZzAY, zzAX().zzAI(), zzAX().zzAJ());
        if (listZzn.isEmpty()) {
            return;
        }
        Iterator<Pair<zzqq.zzd, Long>> it = listZzn.iterator();
        while (true) {
            if (!it.hasNext()) {
                str = null;
                break;
            }
            zzqq.zzd zzdVar = (zzqq.zzd) it.next().first;
            if (!TextUtils.isEmpty(zzdVar.zzaVF)) {
                str = zzdVar.zzaVF;
                break;
            }
        }
        if (str != null) {
            for (int i = 0; i < listZzn.size(); i++) {
                zzqq.zzd zzdVar2 = (zzqq.zzd) listZzn.get(i).first;
                if (!TextUtils.isEmpty(zzdVar2.zzaVF) && !zzdVar2.zzaVF.equals(str)) {
                    listSubList = listZzn.subList(0, i);
                    break;
                }
            }
            listSubList = listZzn;
        } else {
            listSubList = listZzn;
        }
        zzqq.zzc zzcVar = new zzqq.zzc();
        zzcVar.zzaVp = new zzqq.zzd[listSubList.size()];
        ArrayList arrayList = new ArrayList(listSubList.size());
        long jCurrentTimeMillis = zziT().currentTimeMillis();
        for (int i2 = 0; i2 < zzcVar.zzaVp.length; i2++) {
            zzcVar.zzaVp[i2] = (zzqq.zzd) listSubList.get(i2).first;
            arrayList.add(listSubList.get(i2).second);
            zzcVar.zzaVp[i2].zzaVE = Long.valueOf(zzAX().zzAE());
            zzcVar.zzaVp[i2].zzaVu = Long.valueOf(jCurrentTimeMillis);
            zzcVar.zzaVp[i2].zzaVK = Boolean.valueOf(zzAX().zzka());
        }
        byte[] bArrZza = zzAU().zza(zzcVar);
        String strZzAK = zzAX().zzAK();
        try {
            URL url = new URL(strZzAK);
            zzx(arrayList);
            zzAW().zzaTG.set(zziT().currentTimeMillis());
            zzBE().zza(url, bArrZza, new zzp.zza() { // from class: com.google.android.gms.measurement.internal.zzt.2
                @Override // com.google.android.gms.measurement.internal.zzp.zza
                public void zza(int i3, Throwable th, byte[] bArr) {
                    zzt.this.zzb(i3, th, bArr);
                }
            });
        } catch (MalformedURLException e) {
            zzzz().zzBl().zzj("Failed to parse upload URL. Not uploading", strZzAK);
        }
    }

    void zzBO() {
        this.zzaUz++;
    }

    protected boolean zzBz() {
        zzje();
        zziS();
        if (this.zzaUw == null) {
            this.zzaUw = Boolean.valueOf(zzAU().zzbh("android.permission.INTERNET") && zzAU().zzbh("android.permission.ACCESS_NETWORK_STATE") && AppMeasurementReceiver.zzX(getContext()) && AppMeasurementService.zzY(getContext()));
            if (this.zzaUw.booleanValue() && !zzAX().zzka()) {
                this.zzaUw = Boolean.valueOf(TextUtils.isEmpty(zzAS().zzBi()) ? false : true);
            }
        }
        return this.zzaUw.booleanValue();
    }

    public void zzJ(boolean z) {
        zzBM();
    }

    zzqq.zzd zza(zzg[] zzgVarArr, AppMetadata appMetadata) {
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        com.google.android.gms.common.internal.zzx.zzy(zzgVarArr);
        zziS();
        zzqq.zzd zzdVar = new zzqq.zzd();
        zzdVar.zzaVr = 1;
        zzdVar.zzaVz = "android";
        zzdVar.appId = appMetadata.packageName;
        zzdVar.zzaSo = appMetadata.zzaSo;
        zzdVar.zzaKi = appMetadata.zzaKi;
        zzdVar.zzaVD = Long.valueOf(appMetadata.zzaSp);
        zzdVar.zzaSn = appMetadata.zzaSn;
        zzdVar.zzaVI = appMetadata.zzaSq == 0 ? null : Long.valueOf(appMetadata.zzaSq);
        Pair<String, Boolean> pairZzBt = zzAW().zzBt();
        if (pairZzBt != null && pairZzBt.first != null && pairZzBt.second != null) {
            zzdVar.zzaVF = (String) pairZzBt.first;
            zzdVar.zzaVG = (Boolean) pairZzBt.second;
        }
        zzdVar.zzaVA = zzBF().zzhb();
        zzdVar.osVersion = zzBF().zzBe();
        zzdVar.zzaVC = Integer.valueOf((int) zzBF().zzBf());
        zzdVar.zzaVB = zzBF().zzBg();
        zzdVar.zzaVE = null;
        zzdVar.zzaVu = null;
        zzdVar.zzaVv = Long.valueOf(zzgVarArr[0].zzacS);
        zzdVar.zzaVw = Long.valueOf(zzgVarArr[0].zzacS);
        for (int i = 1; i < zzgVarArr.length; i++) {
            zzdVar.zzaVv = Long.valueOf(Math.min(zzdVar.zzaVv.longValue(), zzgVarArr[i].zzacS));
            zzdVar.zzaVw = Long.valueOf(Math.max(zzdVar.zzaVw.longValue(), zzgVarArr[i].zzacS));
        }
        zza zzaVarZzew = zzBD().zzew(appMetadata.packageName);
        if (zzaVarZzew == null) {
            zzaVarZzew = new zza(appMetadata.packageName, zzAW().zzBv(), appMetadata.zzaSn, zzAW().zzBu(), 0L, 0L, appMetadata.zzaKi, appMetadata.zzaSo, appMetadata.zzaSp, appMetadata.zzaSq, appMetadata.zzaSs);
        }
        zza zzaVarZza = zzaVarZzew.zza(zzzz(), zzdVar.zzaVw.longValue());
        zzBD().zza(zzaVarZza);
        zzdVar.zzaVH = zzaVarZza.zzaSe;
        zzdVar.zzaVJ = Integer.valueOf((int) zzaVarZza.zzaSh);
        zzdVar.zzaVy = zzaVarZzew.zzaSi == 0 ? null : Long.valueOf(zzaVarZzew.zzaSi);
        zzdVar.zzaVx = zzdVar.zzaVy;
        List<zzac> listZzev = zzBD().zzev(appMetadata.packageName);
        zzdVar.zzaVt = new zzqq.zze[listZzev.size()];
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= listZzev.size()) {
                break;
            }
            zzqq.zze zzeVar = new zzqq.zze();
            zzdVar.zzaVt[i3] = zzeVar;
            zzeVar.name = listZzev.get(i3).mName;
            zzeVar.zzaVM = Long.valueOf(listZzev.get(i3).zzaVf);
            zzAU().zza(zzeVar, listZzev.get(i3).zzLI);
            i2 = i3 + 1;
        }
        zzdVar.zzaVs = new zzqq.zza[zzgVarArr.length];
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= zzgVarArr.length) {
                zzdVar.zzaSr = zzzz().zzBs();
                return zzdVar;
            }
            zzqq.zza zzaVar = new zzqq.zza();
            zzdVar.zzaVs[i5] = zzaVar;
            zzaVar.name = zzgVarArr[i5].mName;
            zzaVar.zzaVl = Long.valueOf(zzgVarArr[i5].zzacS);
            zzaVar.zzaVk = new zzqq.zzb[zzgVarArr[i5].zzaSE.size()];
            Iterator<String> it = zzgVarArr[i5].zzaSE.iterator();
            int i6 = 0;
            while (it.hasNext()) {
                String next = it.next();
                zzqq.zzb zzbVar = new zzqq.zzb();
                zzaVar.zzaVk[i6] = zzbVar;
                zzbVar.name = next;
                zzAU().zza(zzbVar, zzgVarArr[i5].zzaSE.get(next));
                i6++;
            }
            i4 = i5 + 1;
        }
    }

    void zza(EventParcel eventParcel, String str) {
        zza zzaVarZzew = zzBD().zzew(str);
        if (zzaVarZzew == null || TextUtils.isEmpty(zzaVarZzew.zzRl)) {
            zzzz().zzBq().zzj("No app data available; dropping event", str);
        } else {
            zzb(eventParcel, new AppMetadata(str, zzaVarZzew.zzaSf, zzaVarZzew.zzRl, zzaVarZzew.zzaSj, zzaVarZzew.zzaSk, zzaVarZzew.zzaSl, null, zzaVarZzew.zzaSm));
        }
    }

    void zzb(EventParcel eventParcel, AppMetadata appMetadata) {
        zzh zzhVarZzQ;
        zziS();
        zzje();
        com.google.android.gms.common.internal.zzx.zzcG(appMetadata.packageName);
        if (TextUtils.isEmpty(appMetadata.zzaSn)) {
            return;
        }
        zzzz().zzBr().zzj("Logging event", eventParcel);
        zzg zzgVar = new zzg(this, eventParcel.zzaSM, appMetadata.packageName, eventParcel.name, eventParcel.zzaSN, 0L, eventParcel.zzaSL.zzBh());
        zzBD().beginTransaction();
        try {
            zze(appMetadata);
            zzh zzhVarZzL = zzBD().zzL(appMetadata.packageName, zzgVar.mName);
            if (zzhVarZzL == null) {
                zzhVarZzQ = new zzh(appMetadata.packageName, zzgVar.mName, 1L, 1L, zzgVar.zzacS);
            } else {
                zzgVar = zzgVar.zza(this, zzhVarZzL.zzaSH);
                zzhVarZzQ = zzhVarZzL.zzQ(zzgVar.zzacS);
            }
            zzBD().zza(zzhVarZzQ);
            zzBD().zza(zza(new zzg[]{zzgVar}, appMetadata));
            zzBD().setTransactionSuccessful();
            zzzz().zzBq().zzj("Event logged", zzgVar);
            zzBD().endTransaction();
            zzBM();
        } catch (Throwable th) {
            zzBD().endTransaction();
            throw th;
        }
    }

    void zzb(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) {
        zziS();
        zzje();
        if (TextUtils.isEmpty(appMetadata.zzaSn)) {
            return;
        }
        zzAU().zzeF(userAttributeParcel.name);
        Object objZzm = zzAU().zzm(userAttributeParcel.name, userAttributeParcel.getValue());
        if (objZzm != null) {
            zzac zzacVar = new zzac(appMetadata.packageName, userAttributeParcel.name, userAttributeParcel.zzaVg, objZzm);
            zzzz().zzBq().zze("Setting user attribute", zzacVar.mName, objZzm);
            zzBD().beginTransaction();
            try {
                zze(appMetadata);
                zzBD().zza(zzacVar);
                zzBD().setTransactionSuccessful();
                zzzz().zzBq().zze("User attribute set", zzacVar.mName, zzacVar.zzLI);
            } finally {
                zzBD().endTransaction();
            }
        }
    }

    void zzb(zzw zzwVar) {
        this.zzaUy++;
    }

    void zzc(AppMetadata appMetadata) throws NoSuchAlgorithmException {
        zziS();
        zzje();
        com.google.android.gms.common.internal.zzx.zzcG(appMetadata.packageName);
        zze(appMetadata);
    }

    void zzc(UserAttributeParcel userAttributeParcel, AppMetadata appMetadata) {
        zziS();
        zzje();
        if (TextUtils.isEmpty(appMetadata.zzaSn)) {
            return;
        }
        zzzz().zzBq().zzj("Removing user attribute", userAttributeParcel.name);
        zzBD().beginTransaction();
        try {
            zze(appMetadata);
            zzBD().zzM(appMetadata.packageName, userAttributeParcel.name);
            zzBD().setTransactionSuccessful();
            zzzz().zzBq().zzj("User attribute removed", userAttributeParcel.name);
        } finally {
            zzBD().endTransaction();
        }
    }

    public void zzd(AppMetadata appMetadata) throws NoSuchAlgorithmException {
        zziS();
        zzje();
        com.google.android.gms.common.internal.zzx.zzy(appMetadata);
        com.google.android.gms.common.internal.zzx.zzcG(appMetadata.packageName);
        if (TextUtils.isEmpty(appMetadata.zzaSn)) {
            return;
        }
        zze(appMetadata);
        if (zzBD().zzL(appMetadata.packageName, "_f") == null) {
            long jCurrentTimeMillis = zziT().currentTimeMillis();
            zzb(new UserAttributeParcel("_fot", jCurrentTimeMillis, Long.valueOf(3600000 * ((jCurrentTimeMillis / 3600000) + 1)), "auto"), appMetadata);
            Bundle bundle = new Bundle();
            bundle.putLong("_c", 1L);
            zzb(new EventParcel("_f", new EventParams(bundle), "auto", jCurrentTimeMillis), appMetadata);
        }
    }

    void zziR() {
        if (zzAX().zzka()) {
            throw new IllegalStateException("Unexpected call on package side");
        }
    }

    public void zziS() {
        zzAV().zziS();
    }

    public zznl zziT() {
        return this.zzqD;
    }

    void zzje() {
        if (!this.zzOQ) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    public zzo zzzz() {
        zza((zzw) this.zzaUk);
        return this.zzaUk;
    }
}
