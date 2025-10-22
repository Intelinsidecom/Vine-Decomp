package com.google.android.gms.internal;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import com.google.android.gms.internal.zzei;
import com.google.android.gms.internal.zzjg;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzaz implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnScrollChangedListener {
    private final Context zzrI;
    private final WeakReference<zzie> zzrK;
    private WeakReference<ViewTreeObserver> zzrL;
    private final zzbh zzrM;
    private final zzax zzrN;
    private final zzei zzrO;
    private final zzei.zzd zzrP;
    private boolean zzrQ;
    private final WindowManager zzrR;
    private final PowerManager zzrS;
    private final KeyguardManager zzrT;
    private zzba zzrU;
    private boolean zzrV;
    private boolean zzrX;
    private boolean zzrY;
    BroadcastReceiver zzrZ;
    private zzix zzru;
    private final Object zzpK = new Object();
    private boolean zzqq = false;
    private boolean zzrW = false;
    private final HashSet<zzaw> zzsa = new HashSet<>();
    private final zzdl zzsb = new zzdl() { // from class: com.google.android.gms.internal.zzaz.6
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            if (zzaz.this.zzb(map)) {
                zzaz.this.zza(zzjnVar.getView(), map);
            }
        }
    };
    private final zzdl zzsc = new zzdl() { // from class: com.google.android.gms.internal.zzaz.7
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            if (zzaz.this.zzb(map)) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Received request to untrack: " + zzaz.this.zzrN.zzca());
                zzaz.this.destroy();
            }
        }
    };
    private final zzdl zzsd = new zzdl() { // from class: com.google.android.gms.internal.zzaz.8
        @Override // com.google.android.gms.internal.zzdl
        public void zza(zzjn zzjnVar, Map<String, String> map) {
            if (zzaz.this.zzb(map) && map.containsKey("isVisible")) {
                zzaz.this.zzg(Boolean.valueOf("1".equals(map.get("isVisible")) || "true".equals(map.get("isVisible"))).booleanValue());
            }
        }
    };

    public static class zza implements zzbh {
        private WeakReference<com.google.android.gms.ads.internal.formats.zzh> zzsh;

        public zza(com.google.android.gms.ads.internal.formats.zzh zzhVar) {
            this.zzsh = new WeakReference<>(zzhVar);
        }

        @Override // com.google.android.gms.internal.zzbh
        public View zzcn() {
            com.google.android.gms.ads.internal.formats.zzh zzhVar = this.zzsh.get();
            if (zzhVar != null) {
                return zzhVar.zzdL();
            }
            return null;
        }

        @Override // com.google.android.gms.internal.zzbh
        public boolean zzco() {
            return this.zzsh.get() == null;
        }

        @Override // com.google.android.gms.internal.zzbh
        public zzbh zzcp() {
            return new zzb(this.zzsh.get());
        }
    }

    public static class zzb implements zzbh {
        private com.google.android.gms.ads.internal.formats.zzh zzsi;

        public zzb(com.google.android.gms.ads.internal.formats.zzh zzhVar) {
            this.zzsi = zzhVar;
        }

        @Override // com.google.android.gms.internal.zzbh
        public View zzcn() {
            return this.zzsi.zzdL();
        }

        @Override // com.google.android.gms.internal.zzbh
        public boolean zzco() {
            return this.zzsi == null;
        }

        @Override // com.google.android.gms.internal.zzbh
        public zzbh zzcp() {
            return this;
        }
    }

    public static class zzc implements zzbh {
        private final View mView;
        private final zzie zzsj;

        public zzc(View view, zzie zzieVar) {
            this.mView = view;
            this.zzsj = zzieVar;
        }

        @Override // com.google.android.gms.internal.zzbh
        public View zzcn() {
            return this.mView;
        }

        @Override // com.google.android.gms.internal.zzbh
        public boolean zzco() {
            return this.zzsj == null || this.mView == null;
        }

        @Override // com.google.android.gms.internal.zzbh
        public zzbh zzcp() {
            return this;
        }
    }

    public static class zzd implements zzbh {
        private final WeakReference<View> zzsk;
        private final WeakReference<zzie> zzsl;

        public zzd(View view, zzie zzieVar) {
            this.zzsk = new WeakReference<>(view);
            this.zzsl = new WeakReference<>(zzieVar);
        }

        @Override // com.google.android.gms.internal.zzbh
        public View zzcn() {
            return this.zzsk.get();
        }

        @Override // com.google.android.gms.internal.zzbh
        public boolean zzco() {
            return this.zzsk.get() == null || this.zzsl.get() == null;
        }

        @Override // com.google.android.gms.internal.zzbh
        public zzbh zzcp() {
            return new zzc(this.zzsk.get(), this.zzsl.get());
        }
    }

    public zzaz(Context context, AdSizeParcel adSizeParcel, zzie zzieVar, VersionInfoParcel versionInfoParcel, zzbh zzbhVar, zzei zzeiVar) {
        zzbh zzbhVarZzcp = zzbhVar.zzcp();
        this.zzrO = zzeiVar;
        this.zzrK = new WeakReference<>(zzieVar);
        this.zzrM = zzbhVar;
        this.zzrL = new WeakReference<>(null);
        this.zzrX = true;
        this.zzru = new zzix(200L);
        this.zzrN = new zzax(UUID.randomUUID().toString(), versionInfoParcel, adSizeParcel.zztV, zzieVar.zzJE, zzieVar.zzcb(), adSizeParcel.zztY);
        this.zzrP = this.zzrO.zzei();
        this.zzrR = (WindowManager) context.getSystemService("window");
        this.zzrS = (PowerManager) context.getApplicationContext().getSystemService("power");
        this.zzrT = (KeyguardManager) context.getSystemService("keyguard");
        this.zzrI = context;
        try {
            final JSONObject jSONObjectZzd = zzd(zzbhVarZzcp.zzcn());
            this.zzrP.zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzaz.1
                @Override // com.google.android.gms.internal.zzjg.zzc
                /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                public void zzc(zzbe zzbeVar) {
                    zzaz.this.zza(jSONObjectZzd);
                }
            }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzaz.2
                @Override // com.google.android.gms.internal.zzjg.zza
                public void run() {
                }
            });
        } catch (RuntimeException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failure while processing active view data.", e);
        } catch (JSONException e2) {
        }
        this.zzrP.zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzaz.3
            @Override // com.google.android.gms.internal.zzjg.zzc
            /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
            public void zzc(zzbe zzbeVar) {
                zzaz.this.zzrQ = true;
                zzaz.this.zza(zzbeVar);
                zzaz.this.zzcd();
                zzaz.this.zzh(false);
            }
        }, new zzjg.zza() { // from class: com.google.android.gms.internal.zzaz.4
            @Override // com.google.android.gms.internal.zzjg.zza
            public void run() {
                zzaz.this.destroy();
            }
        });
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Tracking ad unit: " + this.zzrN.zzca());
    }

    protected void destroy() {
        synchronized (this.zzpK) {
            zzcj();
            zzce();
            this.zzrX = false;
            zzcg();
            this.zzrP.release();
        }
    }

    boolean isScreenOn() {
        return this.zzrS.isScreenOn();
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        zzh(false);
    }

    @Override // android.view.ViewTreeObserver.OnScrollChangedListener
    public void onScrollChanged() {
        zzh(true);
    }

    public void pause() {
        synchronized (this.zzpK) {
            this.zzqq = true;
            zzh(false);
        }
    }

    public void resume() {
        synchronized (this.zzpK) {
            this.zzqq = false;
            zzh(false);
        }
    }

    public void stop() {
        synchronized (this.zzpK) {
            this.zzrW = true;
            zzh(false);
        }
    }

    protected int zza(int i, DisplayMetrics displayMetrics) {
        return (int) (i / displayMetrics.density);
    }

    protected void zza(View view, Map<String, String> map) {
        zzh(false);
    }

    public void zza(zzaw zzawVar) {
        this.zzsa.add(zzawVar);
    }

    public void zza(zzba zzbaVar) {
        synchronized (this.zzpK) {
            this.zzrU = zzbaVar;
        }
    }

    protected void zza(zzbe zzbeVar) {
        zzbeVar.zza("/updateActiveView", this.zzsb);
        zzbeVar.zza("/untrackActiveViewUnit", this.zzsc);
        zzbeVar.zza("/visibilityChanged", this.zzsd);
    }

    protected void zza(JSONObject jSONObject) {
        try {
            JSONArray jSONArray = new JSONArray();
            final JSONObject jSONObject2 = new JSONObject();
            jSONArray.put(jSONObject);
            jSONObject2.put("units", jSONArray);
            this.zzrP.zza(new zzjg.zzc<zzbe>() { // from class: com.google.android.gms.internal.zzaz.9
                @Override // com.google.android.gms.internal.zzjg.zzc
                /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
                public void zzc(zzbe zzbeVar) {
                    zzbeVar.zza("AFMA_updateActiveView", jSONObject2);
                }
            }, new zzjg.zzb());
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Skipping active view message.", th);
        }
    }

    protected boolean zzb(Map<String, String> map) {
        if (map == null) {
            return false;
        }
        String str = map.get("hashCode");
        return !TextUtils.isEmpty(str) && str.equals(this.zzrN.zzca());
    }

    protected void zzcd() {
        synchronized (this.zzpK) {
            if (this.zzrZ != null) {
                return;
            }
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.zzrZ = new BroadcastReceiver() { // from class: com.google.android.gms.internal.zzaz.5
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    zzaz.this.zzh(false);
                }
            };
            this.zzrI.registerReceiver(this.zzrZ, intentFilter);
        }
    }

    protected void zzce() {
        synchronized (this.zzpK) {
            if (this.zzrZ != null) {
                try {
                    this.zzrI.unregisterReceiver(this.zzrZ);
                } catch (IllegalStateException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Failed trying to unregister the receiver", e);
                } catch (Exception e2) {
                    com.google.android.gms.ads.internal.zzp.zzbA().zzb((Throwable) e2, true);
                }
                this.zzrZ = null;
            }
        }
    }

    public void zzcf() {
        synchronized (this.zzpK) {
            if (this.zzrX) {
                this.zzrY = true;
                try {
                    try {
                        zza(zzcm());
                    } catch (JSONException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zzb("JSON failure while processing active view data.", e);
                    }
                } catch (RuntimeException e2) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Failure while processing active view data.", e2);
                }
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Untracking ad unit: " + this.zzrN.zzca());
            }
        }
    }

    protected void zzcg() {
        if (this.zzrU != null) {
            this.zzrU.zza(this);
        }
    }

    public boolean zzch() {
        boolean z;
        synchronized (this.zzpK) {
            z = this.zzrX;
        }
        return z;
    }

    protected void zzci() {
        ViewTreeObserver viewTreeObserver;
        ViewTreeObserver viewTreeObserver2;
        View viewZzcn = this.zzrM.zzcp().zzcn();
        if (viewZzcn == null || (viewTreeObserver2 = viewZzcn.getViewTreeObserver()) == (viewTreeObserver = this.zzrL.get())) {
            return;
        }
        zzcj();
        if (!this.zzrV || (viewTreeObserver != null && viewTreeObserver.isAlive())) {
            this.zzrV = true;
            viewTreeObserver2.addOnScrollChangedListener(this);
            viewTreeObserver2.addOnGlobalLayoutListener(this);
        }
        this.zzrL = new WeakReference<>(viewTreeObserver2);
    }

    protected void zzcj() {
        ViewTreeObserver viewTreeObserver = this.zzrL.get();
        if (viewTreeObserver == null || !viewTreeObserver.isAlive()) {
            return;
        }
        viewTreeObserver.removeOnScrollChangedListener(this);
        viewTreeObserver.removeGlobalOnLayoutListener(this);
    }

    protected JSONObject zzck() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("afmaVersion", this.zzrN.zzbY()).put("activeViewJSON", this.zzrN.zzbZ()).put("timestamp", com.google.android.gms.ads.internal.zzp.zzbB().elapsedRealtime()).put("adFormat", this.zzrN.zzbX()).put("hashCode", this.zzrN.zzca()).put("isMraid", this.zzrN.zzcb()).put("isStopped", this.zzrW).put("isPaused", this.zzqq).put("isScreenOn", isScreenOn()).put("isNative", this.zzrN.zzcc());
        return jSONObject;
    }

    protected JSONObject zzcl() throws JSONException {
        return zzck().put("isAttachedToWindow", false).put("isScreenOn", isScreenOn()).put("isVisible", false);
    }

    protected JSONObject zzcm() throws JSONException {
        JSONObject jSONObjectZzck = zzck();
        jSONObjectZzck.put("doneReasonCode", "u");
        return jSONObjectZzck;
    }

    protected JSONObject zzd(View view) throws JSONException {
        if (view == null) {
            return zzcl();
        }
        boolean zIsAttachedToWindow = com.google.android.gms.ads.internal.zzp.zzbz().isAttachedToWindow(view);
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        try {
            view.getLocationOnScreen(iArr);
            view.getLocationInWindow(iArr2);
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Failure getting view location.", e);
        }
        DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        Rect rect = new Rect();
        rect.left = iArr[0];
        rect.top = iArr[1];
        rect.right = rect.left + view.getWidth();
        rect.bottom = rect.top + view.getHeight();
        Rect rect2 = new Rect();
        rect2.right = this.zzrR.getDefaultDisplay().getWidth();
        rect2.bottom = this.zzrR.getDefaultDisplay().getHeight();
        Rect rect3 = new Rect();
        boolean globalVisibleRect = view.getGlobalVisibleRect(rect3, null);
        Rect rect4 = new Rect();
        boolean localVisibleRect = view.getLocalVisibleRect(rect4);
        Rect rect5 = new Rect();
        view.getHitRect(rect5);
        JSONObject jSONObjectZzck = zzck();
        jSONObjectZzck.put("windowVisibility", view.getWindowVisibility()).put("isAttachedToWindow", zIsAttachedToWindow).put("viewBox", new JSONObject().put("top", zza(rect2.top, displayMetrics)).put("bottom", zza(rect2.bottom, displayMetrics)).put("left", zza(rect2.left, displayMetrics)).put("right", zza(rect2.right, displayMetrics))).put("adBox", new JSONObject().put("top", zza(rect.top, displayMetrics)).put("bottom", zza(rect.bottom, displayMetrics)).put("left", zza(rect.left, displayMetrics)).put("right", zza(rect.right, displayMetrics))).put("globalVisibleBox", new JSONObject().put("top", zza(rect3.top, displayMetrics)).put("bottom", zza(rect3.bottom, displayMetrics)).put("left", zza(rect3.left, displayMetrics)).put("right", zza(rect3.right, displayMetrics))).put("globalVisibleBoxVisible", globalVisibleRect).put("localVisibleBox", new JSONObject().put("top", zza(rect4.top, displayMetrics)).put("bottom", zza(rect4.bottom, displayMetrics)).put("left", zza(rect4.left, displayMetrics)).put("right", zza(rect4.right, displayMetrics))).put("localVisibleBoxVisible", localVisibleRect).put("hitBox", new JSONObject().put("top", zza(rect5.top, displayMetrics)).put("bottom", zza(rect5.bottom, displayMetrics)).put("left", zza(rect5.left, displayMetrics)).put("right", zza(rect5.right, displayMetrics))).put("screenDensity", displayMetrics.density).put("isVisible", zze(view));
        return jSONObjectZzck;
    }

    protected boolean zze(View view) {
        return view.getVisibility() == 0 && view.isShown() && isScreenOn() && (!this.zzrT.inKeyguardRestrictedInputMode() || com.google.android.gms.ads.internal.zzp.zzbx().zzgY());
    }

    protected void zzg(boolean z) {
        Iterator<zzaw> it = this.zzsa.iterator();
        while (it.hasNext()) {
            it.next().zza(this, z);
        }
    }

    protected void zzh(boolean z) {
        synchronized (this.zzpK) {
            if (this.zzrQ && this.zzrX) {
                if (!z || this.zzru.tryAcquire()) {
                    if (this.zzrM.zzco()) {
                        zzcf();
                        return;
                    }
                    try {
                        zza(zzd(this.zzrM.zzcn()));
                    } catch (RuntimeException | JSONException e) {
                        com.google.android.gms.ads.internal.util.client.zzb.zza("Active view update failed.", e);
                    }
                    zzci();
                    zzcg();
                }
            }
        }
    }
}
