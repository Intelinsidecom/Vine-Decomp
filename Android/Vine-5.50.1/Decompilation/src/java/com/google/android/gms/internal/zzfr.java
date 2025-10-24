package com.google.android.gms.internal;

import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzfr {
    private final String zzCJ;
    private final zzjn zzps;

    public zzfr(zzjn zzjnVar) {
        this(zzjnVar, "");
    }

    public zzfr(zzjn zzjnVar, String str) {
        this.zzps = zzjnVar;
        this.zzCJ = str;
    }

    public void zza(int i, int i2, int i3, int i4, float f, int i5) throws JSONException {
        try {
            this.zzps.zzb("onScreenInfoChanged", new JSONObject().put("width", i).put("height", i2).put("maxSizeWidth", i3).put("maxSizeHeight", i4).put("density", f).put("rotation", i5));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while obtaining screen information.", e);
        }
    }

    public void zzal(String str) throws JSONException {
        try {
            this.zzps.zzb("onError", new JSONObject().put("message", str).put("action", this.zzCJ));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occurred while dispatching error event.", e);
        }
    }

    public void zzam(String str) throws JSONException {
        try {
            this.zzps.zzb("onReadyEventReceived", new JSONObject().put("js", str));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while dispatching ready Event.", e);
        }
    }

    public void zzan(String str) throws JSONException {
        try {
            this.zzps.zzb("onStateChanged", new JSONObject().put("state", str));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while dispatching state change.", e);
        }
    }

    public void zzb(int i, int i2, int i3, int i4) throws JSONException {
        try {
            this.zzps.zzb("onSizeChanged", new JSONObject().put("x", i).put("y", i2).put("width", i3).put("height", i4));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while dispatching size change.", e);
        }
    }

    public void zzc(int i, int i2, int i3, int i4) throws JSONException {
        try {
            this.zzps.zzb("onDefaultPositionReceived", new JSONObject().put("x", i).put("y", i2).put("width", i3).put("height", i4));
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while dispatching default position.", e);
        }
    }
}
