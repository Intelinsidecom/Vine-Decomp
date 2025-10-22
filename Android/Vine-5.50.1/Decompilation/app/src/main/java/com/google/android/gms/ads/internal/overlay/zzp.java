package com.google.android.gms.ads.internal.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzcd;
import com.google.android.gms.internal.zzcf;
import com.google.android.gms.internal.zzch;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzit;
import java.util.concurrent.TimeUnit;

@zzha
/* loaded from: classes.dex */
public class zzp {
    private final Context mContext;
    private final String zzDX;
    private final VersionInfoParcel zzDY;
    private final zzcf zzDZ;
    private final zzch zzEa;
    private final long[] zzEc;
    private final String[] zzEd;
    private zzcf zzEe;
    private zzcf zzEf;
    private zzcf zzEg;
    private zzcf zzEh;
    private boolean zzEi;
    private zzi zzEj;
    private boolean zzEk;
    private boolean zzEl;
    private final zzit zzEb = new zzit.zzb().zza("min_1", Double.MIN_VALUE, 1.0d).zza("1_5", 1.0d, 5.0d).zza("5_10", 5.0d, 10.0d).zza("10_20", 10.0d, 20.0d).zza("20_30", 20.0d, 30.0d).zza("30_max", 30.0d, Double.MAX_VALUE).zzhi();
    private long zzEm = -1;

    public zzp(Context context, VersionInfoParcel versionInfoParcel, String str, zzch zzchVar, zzcf zzcfVar) {
        this.mContext = context;
        this.zzDY = versionInfoParcel;
        this.zzDX = str;
        this.zzEa = zzchVar;
        this.zzDZ = zzcfVar;
        String str2 = zzbz.zzvA.get();
        if (str2 == null) {
            this.zzEd = new String[0];
            this.zzEc = new long[0];
            return;
        }
        String[] strArrSplit = TextUtils.split(str2, ",");
        this.zzEd = new String[strArrSplit.length];
        this.zzEc = new long[strArrSplit.length];
        for (int i = 0; i < strArrSplit.length; i++) {
            try {
                this.zzEc[i] = Long.parseLong(strArrSplit[i]);
            } catch (NumberFormatException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("Unable to parse frame hash target time number.", e);
                this.zzEc[i] = -1;
            }
        }
    }

    private void zzc(zzi zziVar) {
        long jLongValue = zzbz.zzvB.get().longValue();
        long currentPosition = zziVar.getCurrentPosition();
        for (int i = 0; i < this.zzEd.length; i++) {
            if (this.zzEd[i] == null && jLongValue > Math.abs(currentPosition - this.zzEc[i])) {
                this.zzEd[i] = zza((TextureView) zziVar);
                return;
            }
        }
    }

    private void zzfA() {
        if (this.zzEg != null && this.zzEh == null) {
            zzcd.zza(this.zzEa, this.zzEg, "vff");
            zzcd.zza(this.zzEa, this.zzDZ, "vtt");
            this.zzEh = zzcd.zzb(this.zzEa);
        }
        long jNanoTime = com.google.android.gms.ads.internal.zzp.zzbB().nanoTime();
        if (this.zzEi && this.zzEl && this.zzEm != -1) {
            this.zzEb.zza(TimeUnit.SECONDS.toNanos(1L) / (jNanoTime - this.zzEm));
        }
        this.zzEl = this.zzEi;
        this.zzEm = jNanoTime;
    }

    public void onStop() {
        if (!zzbz.zzvz.get().booleanValue() || this.zzEk) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", "native-player-metrics");
        bundle.putString("request", this.zzDX);
        bundle.putString("player", this.zzEj.zzeO());
        for (zzit.zza zzaVar : this.zzEb.getBuckets()) {
            bundle.putString("fps_c_" + zzaVar.name, Integer.toString(zzaVar.count));
            bundle.putString("fps_p_" + zzaVar.name, Double.toString(zzaVar.zzLg));
        }
        for (int i = 0; i < this.zzEc.length; i++) {
            String str = this.zzEd[i];
            if (str != null) {
                bundle.putString("fh_" + Long.valueOf(this.zzEc[i]), str);
            }
        }
        com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzDY.afmaVersion, "gmob-apps", bundle, true);
        this.zzEk = true;
    }

    String zza(TextureView textureView) {
        long j;
        Bitmap bitmap = textureView.getBitmap(8, 8);
        long j2 = 0;
        long j3 = 63;
        int i = 0;
        while (i < 8) {
            int i2 = 0;
            long j4 = j2;
            while (true) {
                j = j3;
                int i3 = i2;
                if (i3 < 8) {
                    int pixel = bitmap.getPixel(i3, i);
                    j4 |= (Color.green(pixel) + (Color.blue(pixel) + Color.red(pixel)) > 128 ? 1L : 0L) << ((int) j);
                    i2 = i3 + 1;
                    j3 = j - 1;
                }
            }
            i++;
            j3 = j;
            j2 = j4;
        }
        return String.format("%016X", Long.valueOf(j2));
    }

    public void zza(zzi zziVar) {
        zzcd.zza(this.zzEa, this.zzDZ, "vpc");
        this.zzEe = zzcd.zzb(this.zzEa);
        this.zzEj = zziVar;
    }

    public void zzb(zzi zziVar) {
        zzfA();
        zzc(zziVar);
    }

    public void zzfB() {
        this.zzEi = true;
        if (this.zzEf == null || this.zzEg != null) {
            return;
        }
        zzcd.zza(this.zzEa, this.zzEf, "vfp");
        this.zzEg = zzcd.zzb(this.zzEa);
    }

    public void zzfC() {
        this.zzEi = false;
    }

    public void zzfo() {
        if (this.zzEe == null || this.zzEf != null) {
            return;
        }
        zzcd.zza(this.zzEa, this.zzEe, "vfr");
        this.zzEf = zzcd.zzb(this.zzEa);
    }
}
