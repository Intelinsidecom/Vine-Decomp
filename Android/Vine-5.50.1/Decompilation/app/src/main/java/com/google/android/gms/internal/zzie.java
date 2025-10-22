package com.google.android.gms.internal;

import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.formats.zzh;
import com.google.android.gms.ads.internal.request.AdRequestInfoParcel;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzie {
    public final int errorCode;
    public final int orientation;
    public final List<String> zzAQ;
    public final List<String> zzAR;
    public final long zzAU;
    public final zzem zzBp;
    public final zzex zzBq;
    public final String zzBr;
    public final zzep zzBs;
    public final zzjn zzDC;
    public final long zzGM;
    public final boolean zzGN;
    public final long zzGO;
    public final List<String> zzGP;
    public final String zzGS;
    public final AdRequestParcel zzGq;
    public final String zzGt;
    public final JSONObject zzJE;
    public final zzen zzJF;
    public final AdSizeParcel zzJG;
    public final long zzJH;
    public final long zzJI;
    public final zzh.zza zzJJ;

    @zzha
    public static final class zza {
        public final int errorCode;
        public final JSONObject zzJE;
        public final zzen zzJF;
        public final long zzJH;
        public final long zzJI;
        public final AdRequestInfoParcel zzJK;
        public final AdResponseParcel zzJL;
        public final AdSizeParcel zzqV;

        public zza(AdRequestInfoParcel adRequestInfoParcel, AdResponseParcel adResponseParcel, zzen zzenVar, AdSizeParcel adSizeParcel, int i, long j, long j2, JSONObject jSONObject) {
            this.zzJK = adRequestInfoParcel;
            this.zzJL = adResponseParcel;
            this.zzJF = zzenVar;
            this.zzqV = adSizeParcel;
            this.errorCode = i;
            this.zzJH = j;
            this.zzJI = j2;
            this.zzJE = jSONObject;
        }
    }

    public zzie(AdRequestParcel adRequestParcel, zzjn zzjnVar, List<String> list, int i, List<String> list2, List<String> list3, int i2, long j, String str, boolean z, zzem zzemVar, zzex zzexVar, String str2, zzen zzenVar, zzep zzepVar, long j2, AdSizeParcel adSizeParcel, long j3, long j4, long j5, String str3, JSONObject jSONObject, zzh.zza zzaVar) {
        this.zzGq = adRequestParcel;
        this.zzDC = zzjnVar;
        this.zzAQ = list != null ? Collections.unmodifiableList(list) : null;
        this.errorCode = i;
        this.zzAR = list2 != null ? Collections.unmodifiableList(list2) : null;
        this.zzGP = list3 != null ? Collections.unmodifiableList(list3) : null;
        this.orientation = i2;
        this.zzAU = j;
        this.zzGt = str;
        this.zzGN = z;
        this.zzBp = zzemVar;
        this.zzBq = zzexVar;
        this.zzBr = str2;
        this.zzJF = zzenVar;
        this.zzBs = zzepVar;
        this.zzGO = j2;
        this.zzJG = adSizeParcel;
        this.zzGM = j3;
        this.zzJH = j4;
        this.zzJI = j5;
        this.zzGS = str3;
        this.zzJE = jSONObject;
        this.zzJJ = zzaVar;
    }

    public zzie(zza zzaVar, zzjn zzjnVar, zzem zzemVar, zzex zzexVar, String str, zzep zzepVar, zzh.zza zzaVar2) {
        this(zzaVar.zzJK.zzGq, zzjnVar, zzaVar.zzJL.zzAQ, zzaVar.errorCode, zzaVar.zzJL.zzAR, zzaVar.zzJL.zzGP, zzaVar.zzJL.orientation, zzaVar.zzJL.zzAU, zzaVar.zzJK.zzGt, zzaVar.zzJL.zzGN, zzemVar, zzexVar, str, zzaVar.zzJF, zzepVar, zzaVar.zzJL.zzGO, zzaVar.zzqV, zzaVar.zzJL.zzGM, zzaVar.zzJH, zzaVar.zzJI, zzaVar.zzJL.zzGS, zzaVar.zzJE, zzaVar2);
    }

    public boolean zzcb() {
        if (this.zzDC == null || this.zzDC.zzhC() == null) {
            return false;
        }
        return this.zzDC.zzhC().zzcb();
    }
}
