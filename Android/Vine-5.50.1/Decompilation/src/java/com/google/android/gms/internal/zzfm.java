package com.google.android.gms.internal;

import android.app.Activity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import java.util.Map;
import java.util.Set;

@zzha
/* loaded from: classes.dex */
public class zzfm extends zzfr {
    static final Set<String> zzCa = zznm.zzc("top-left", "top-right", "top-center", "center", "bottom-left", "bottom-right", "bottom-center");
    private AdSizeParcel zzBh;
    private String zzCb;
    private boolean zzCc;
    private int zzCd;
    private int zzCe;
    private int zzCf;
    private int zzCg;
    private final Activity zzCh;
    private ImageView zzCi;
    private LinearLayout zzCj;
    private zzfs zzCk;
    private PopupWindow zzCl;
    private RelativeLayout zzCm;
    private ViewGroup zzCn;
    private int zzov;
    private int zzow;
    private final Object zzpK;
    private final zzjn zzps;

    public zzfm(zzjn zzjnVar, zzfs zzfsVar) {
        super(zzjnVar, "resize");
        this.zzCb = "top-right";
        this.zzCc = true;
        this.zzCd = 0;
        this.zzCe = 0;
        this.zzow = -1;
        this.zzCf = 0;
        this.zzCg = 0;
        this.zzov = -1;
        this.zzpK = new Object();
        this.zzps = zzjnVar;
        this.zzCh = zzjnVar.zzhx();
        this.zzCk = zzfsVar;
    }

    private int[] zzeB() {
        if (!zzeD()) {
            return null;
        }
        if (this.zzCc) {
            return new int[]{this.zzCd + this.zzCf, this.zzCe + this.zzCg};
        }
        int[] iArrZzh = com.google.android.gms.ads.internal.zzp.zzbx().zzh(this.zzCh);
        int[] iArrZzj = com.google.android.gms.ads.internal.zzp.zzbx().zzj(this.zzCh);
        int i = iArrZzh[0];
        int i2 = this.zzCd + this.zzCf;
        int i3 = this.zzCe + this.zzCg;
        if (i2 < 0) {
            i2 = 0;
        } else if (this.zzov + i2 > i) {
            i2 = i - this.zzov;
        }
        if (i3 < iArrZzj[0]) {
            i3 = iArrZzj[0];
        } else if (this.zzow + i3 > iArrZzj[1]) {
            i3 = iArrZzj[1] - this.zzow;
        }
        return new int[]{i2, i3};
    }

    private void zzf(Map<String, String> map) {
        if (!TextUtils.isEmpty(map.get("width"))) {
            this.zzov = com.google.android.gms.ads.internal.zzp.zzbx().zzaA(map.get("width"));
        }
        if (!TextUtils.isEmpty(map.get("height"))) {
            this.zzow = com.google.android.gms.ads.internal.zzp.zzbx().zzaA(map.get("height"));
        }
        if (!TextUtils.isEmpty(map.get("offsetX"))) {
            this.zzCf = com.google.android.gms.ads.internal.zzp.zzbx().zzaA(map.get("offsetX"));
        }
        if (!TextUtils.isEmpty(map.get("offsetY"))) {
            this.zzCg = com.google.android.gms.ads.internal.zzp.zzbx().zzaA(map.get("offsetY"));
        }
        if (!TextUtils.isEmpty(map.get("allowOffscreen"))) {
            this.zzCc = Boolean.parseBoolean(map.get("allowOffscreen"));
        }
        String str = map.get("customClosePosition");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.zzCb = str;
    }

    public void zza(int i, int i2, boolean z) {
        synchronized (this.zzpK) {
            this.zzCd = i;
            this.zzCe = i2;
            if (this.zzCl != null && z) {
                int[] iArrZzeB = zzeB();
                if (iArrZzeB != null) {
                    this.zzCl.update(com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCh, iArrZzeB[0]), com.google.android.gms.ads.internal.client.zzl.zzcN().zzb(this.zzCh, iArrZzeB[1]), this.zzCl.getWidth(), this.zzCl.getHeight());
                    zzd(iArrZzeB[0], iArrZzeB[1]);
                } else {
                    zzp(true);
                }
            }
        }
    }

    void zzc(int i, int i2) {
        if (this.zzCk != null) {
            this.zzCk.zza(i, i2, this.zzov, this.zzow);
        }
    }

    void zzd(int i, int i2) {
        zzb(i, i2 - com.google.android.gms.ads.internal.zzp.zzbx().zzj(this.zzCh)[0], this.zzov, this.zzow);
    }

    public void zze(int i, int i2) {
        this.zzCd = i;
        this.zzCe = i2;
    }

    boolean zzeA() {
        return this.zzov > -1 && this.zzow > -1;
    }

    public boolean zzeC() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzCl != null;
        }
        return z;
    }

    boolean zzeD() {
        int i;
        int i2;
        int[] iArrZzh = com.google.android.gms.ads.internal.zzp.zzbx().zzh(this.zzCh);
        int[] iArrZzj = com.google.android.gms.ads.internal.zzp.zzbx().zzj(this.zzCh);
        int i3 = iArrZzh[0];
        int i4 = iArrZzh[1];
        if (this.zzov < 50 || this.zzov > i3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Width is too small or too large.");
            return false;
        }
        if (this.zzow < 50 || this.zzow > i4) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Height is too small or too large.");
            return false;
        }
        if (this.zzow == i4 && this.zzov == i3) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Cannot resize to a full-screen ad.");
            return false;
        }
        if (this.zzCc) {
            switch (this.zzCb) {
                case "top-left":
                    i = this.zzCf + this.zzCd;
                    i2 = this.zzCe + this.zzCg;
                    break;
                case "top-center":
                    i = ((this.zzCd + this.zzCf) + (this.zzov / 2)) - 25;
                    i2 = this.zzCe + this.zzCg;
                    break;
                case "center":
                    i = ((this.zzCd + this.zzCf) + (this.zzov / 2)) - 25;
                    i2 = ((this.zzCe + this.zzCg) + (this.zzow / 2)) - 25;
                    break;
                case "bottom-left":
                    i = this.zzCf + this.zzCd;
                    i2 = ((this.zzCe + this.zzCg) + this.zzow) - 50;
                    break;
                case "bottom-center":
                    i = ((this.zzCd + this.zzCf) + (this.zzov / 2)) - 25;
                    i2 = ((this.zzCe + this.zzCg) + this.zzow) - 50;
                    break;
                case "bottom-right":
                    i = ((this.zzCd + this.zzCf) + this.zzov) - 50;
                    i2 = ((this.zzCe + this.zzCg) + this.zzow) - 50;
                    break;
                default:
                    i = ((this.zzCd + this.zzCf) + this.zzov) - 50;
                    i2 = this.zzCe + this.zzCg;
                    break;
            }
            if (i < 0 || i + 50 > i3 || i2 < iArrZzj[0] || i2 + 50 > iArrZzj[1]) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0155  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void zzg(java.util.Map<java.lang.String, java.lang.String> r13) {
        /*
            Method dump skipped, instructions count: 728
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzfm.zzg(java.util.Map):void");
    }

    public void zzp(boolean z) {
        synchronized (this.zzpK) {
            if (this.zzCl != null) {
                this.zzCl.dismiss();
                this.zzCm.removeView(this.zzps.getView());
                if (this.zzCn != null) {
                    this.zzCn.removeView(this.zzCi);
                    this.zzCn.addView(this.zzps.getView());
                    this.zzps.zza(this.zzBh);
                }
                if (z) {
                    zzan("default");
                    if (this.zzCk != null) {
                        this.zzCk.zzbe();
                    }
                }
                this.zzCl = null;
                this.zzCm = null;
                this.zzCn = null;
                this.zzCj = null;
            }
        }
    }
}
