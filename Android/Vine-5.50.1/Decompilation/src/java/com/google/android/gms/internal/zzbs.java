package com.google.android.gms.internal;

import android.text.TextUtils;

@zzha
/* loaded from: classes.dex */
public final class zzbs {
    private String zzuV;

    public zzbs() {
        this(zzbz.zzvg.zzdk());
    }

    public zzbs(String str) {
        this.zzuV = TextUtils.isEmpty(str) ? zzbz.zzvg.zzdk() : str;
    }

    public String zzdj() {
        return this.zzuV;
    }
}
