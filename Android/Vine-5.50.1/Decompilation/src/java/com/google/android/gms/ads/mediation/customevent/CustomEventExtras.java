package com.google.android.gms.ads.mediation.customevent;

import com.google.ads.mediation.NetworkExtras;
import java.util.HashMap;

@Deprecated
/* loaded from: classes.dex */
public final class CustomEventExtras implements NetworkExtras {
    private final HashMap<String, Object> zzNb = new HashMap<>();

    public Object getExtra(String label) {
        return this.zzNb.get(label);
    }
}
