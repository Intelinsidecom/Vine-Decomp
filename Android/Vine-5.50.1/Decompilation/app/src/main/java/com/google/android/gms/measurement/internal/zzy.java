package com.google.android.gms.measurement.internal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.internal.zznl;

/* loaded from: classes.dex */
public class zzy extends zzw {
    private zza zzaUI;

    /* renamed from: com.google.android.gms.measurement.internal.zzy$1, reason: invalid class name */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ boolean zzaUL;
        final /* synthetic */ zzy zzaUM;

        @Override // java.lang.Runnable
        public void run() throws IllegalStateException {
            this.zzaUM.zzaq(this.zzaUL);
        }
    }

    private class zza implements Application.ActivityLifecycleCallbacks {
        private zza() {
        }

        /* synthetic */ zza(zzy zzyVar, AnonymousClass1 anonymousClass1) {
            this();
        }

        private boolean zzeD(String str) throws IllegalStateException {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            zzy.this.zza("auto", "_ldl", str);
            return true;
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Uri data;
            try {
                zzy.this.zzzz().zzBr().zzez("onActivityCreated");
                Intent intent = activity.getIntent();
                if (intent != null && (data = intent.getData()) != null && data.isHierarchical()) {
                    String queryParameter = data.getQueryParameter("referrer");
                    if (!TextUtils.isEmpty(queryParameter)) {
                        if (queryParameter.contains("gclid")) {
                            zzy.this.zzzz().zzBq().zzj("Activity created with referrer", queryParameter);
                            zzeD(queryParameter);
                        } else {
                            zzy.this.zzzz().zzBq().zzez("Activity created with data 'referrer' param without gclid");
                        }
                    }
                }
            } catch (Throwable th) {
                zzy.this.zzzz().zzBl().zzj("Throwable caught in onActivityCreated", th);
            }
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }
    }

    protected zzy(zzt zztVar) {
        super(zztVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zza(String str, String str2, Object obj, long j) throws IllegalStateException {
        com.google.android.gms.common.internal.zzx.zzcG(str);
        com.google.android.gms.common.internal.zzx.zzcG(str2);
        zziS();
        zziR();
        zzje();
        if (!zzAW().zzzC()) {
            zzzz().zzBp().zzez("User attribute not set since app measurement is disabled");
        } else if (this.zzaQX.zzBz()) {
            zzzz().zzBq().zze("Setting user attribute (FE)", str2, obj);
            zzAT().zza(new UserAttributeParcel(str2, j, obj, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void zzaq(boolean z) throws IllegalStateException {
        zziS();
        zziR();
        zzje();
        zzzz().zzBq().zzj("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzAW().setMeasurementEnabled(z);
        zzAT().zzBV();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zzAR() {
        super.zzAR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzm zzAS() {
        return super.zzAS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzz zzAT() {
        return super.zzAT();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzae zzAU() {
        return super.zzAU();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzs zzAV() {
        return super.zzAV();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzr zzAW() {
        return super.zzAW();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzc zzAX() {
        return super.zzAX();
    }

    public void zzBR() {
        if (getContext().getApplicationContext() instanceof Application) {
            Application application = (Application) getContext().getApplicationContext();
            if (this.zzaUI == null) {
                this.zzaUI = new zza(this, null);
            }
            application.unregisterActivityLifecycleCallbacks(this.zzaUI);
            application.registerActivityLifecycleCallbacks(this.zzaUI);
            zzzz().zzBr().zzez("Registered activity lifecycle callback");
        }
    }

    public void zzBS() {
        zziS();
        zziR();
        zzje();
        if (this.zzaQX.zzBz()) {
            zzAT().zzBS();
        }
    }

    public void zza(final String str, final String str2, Object obj) throws IllegalStateException {
        com.google.android.gms.common.internal.zzx.zzcG(str);
        final long jCurrentTimeMillis = zziT().currentTimeMillis();
        zzAU().zzeF(str2);
        if (obj == null) {
            zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzy.4
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    zzy.this.zza(str, str2, null, jCurrentTimeMillis);
                }
            });
            return;
        }
        zzAU().zzl(str2, obj);
        final Object objZzm = zzAU().zzm(str2, obj);
        if (objZzm != null) {
            zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzy.3
                @Override // java.lang.Runnable
                public void run() throws IllegalStateException {
                    zzy.this.zza(str, str2, objZzm, jCurrentTimeMillis);
                }
            });
        }
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziR() {
        super.zziR();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ void zziS() {
        super.zziS();
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zznl zziT() {
        return super.zziT();
    }

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() {
    }

    @Override // com.google.android.gms.measurement.internal.zzv
    public /* bridge */ /* synthetic */ zzo zzzz() {
        return super.zzzz();
    }
}
