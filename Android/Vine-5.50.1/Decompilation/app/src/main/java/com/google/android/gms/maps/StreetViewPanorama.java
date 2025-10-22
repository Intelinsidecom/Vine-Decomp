package com.google.android.gms.maps;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;

/* loaded from: classes.dex */
public class StreetViewPanorama {
    private final IStreetViewPanoramaDelegate zzaPt;

    protected StreetViewPanorama(IStreetViewPanoramaDelegate sv) {
        this.zzaPt = (IStreetViewPanoramaDelegate) zzx.zzy(sv);
    }
}
