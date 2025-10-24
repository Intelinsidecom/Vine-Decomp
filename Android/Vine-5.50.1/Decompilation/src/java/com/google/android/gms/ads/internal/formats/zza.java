package com.google.android.gms.ads.internal.formats;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.google.android.gms.internal.zzha;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zza {
    private static final int zzxk = Color.rgb(12, 174, 206);
    private static final int zzxl = Color.rgb(204, 204, 204);
    static final int zzxm = zzxl;
    static final int zzxn = zzxk;
    private final int mTextColor;
    private final String zzxo;
    private final List<Drawable> zzxp;
    private final int zzxq;
    private final int zzxr;
    private final int zzxs;

    public zza(String str, List<Drawable> list, Integer num, Integer num2, Integer num3, int i) {
        this.zzxo = str;
        this.zzxp = list;
        this.zzxq = num != null ? num.intValue() : zzxm;
        this.mTextColor = num2 != null ? num2.intValue() : zzxn;
        this.zzxr = num3 != null ? num3.intValue() : 12;
        this.zzxs = i;
    }

    public int getBackgroundColor() {
        return this.zzxq;
    }

    public String getText() {
        return this.zzxo;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public int getTextSize() {
        return this.zzxr;
    }

    public int zzdA() {
        return this.zzxs;
    }

    public List<Drawable> zzdz() {
        return this.zzxp;
    }
}
