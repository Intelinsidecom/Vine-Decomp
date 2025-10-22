package com.google.android.gms.common.api;

import android.app.Activity;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: classes2.dex */
public abstract class zza {
    private static final Map<Activity, zza> zzaeV = new WeakHashMap();
    private static final Object zzqf = new Object();

    public abstract void remove(int i);
}
