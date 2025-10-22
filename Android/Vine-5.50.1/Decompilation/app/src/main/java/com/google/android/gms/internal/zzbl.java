package com.google.android.gms.internal;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzbl extends Thread {
    private final zzbk zzsP;
    private final zzbj zzsQ;
    private final zzgz zzsR;
    private boolean mStarted = false;
    private boolean zzsO = false;
    private boolean zzam = false;
    private final Object zzpK = new Object();
    private final int zzsC = zzbz.zzvP.get().intValue();
    private final int zzsT = zzbz.zzvQ.get().intValue();
    private final int zzsE = zzbz.zzvR.get().intValue();
    private final int zzsU = zzbz.zzvS.get().intValue();
    private final int zzsS = zzbz.zzvT.get().intValue();

    @zzha
    class zza {
        final int zztb;
        final int zztc;

        zza(int i, int i2) {
            this.zztb = i;
            this.zztc = i2;
        }
    }

    public zzbl(zzbk zzbkVar, zzbj zzbjVar, zzgz zzgzVar) {
        this.zzsP = zzbkVar;
        this.zzsQ = zzbjVar;
        this.zzsR = zzgzVar;
        setName("ContentFetchTask");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.zzam) {
            try {
            } catch (Throwable th) {
                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error in ContentFetchTask", th);
                this.zzsR.zza(th, true);
            }
            if (zzcC()) {
                Activity activity = this.zzsP.getActivity();
                if (activity == null) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("ContentFetchThread: no activity");
                } else {
                    zza(activity);
                }
            } else {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("ContentFetchTask: sleeping");
                zzcE();
            }
            Thread.sleep(this.zzsS * 1000);
            synchronized (this.zzpK) {
                while (this.zzsO) {
                    try {
                        com.google.android.gms.ads.internal.util.client.zzb.zzaF("ContentFetchTask: waiting");
                        this.zzpK.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void wakeup() {
        synchronized (this.zzpK) {
            this.zzsO = false;
            this.zzpK.notifyAll();
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("ContentFetchThread: wakeup");
        }
    }

    zza zza(View view, zzbi zzbiVar) {
        if (view == null) {
            return new zza(0, 0);
        }
        if ((view instanceof TextView) && !(view instanceof EditText)) {
            CharSequence text = ((TextView) view).getText();
            if (TextUtils.isEmpty(text)) {
                return new zza(0, 0);
            }
            zzbiVar.zzw(text.toString());
            return new zza(1, 0);
        }
        if ((view instanceof WebView) && !(view instanceof zzjn)) {
            zzbiVar.zzcx();
            return zza((WebView) view, zzbiVar) ? new zza(0, 1) : new zza(0, 0);
        }
        if (!(view instanceof ViewGroup)) {
            return new zza(0, 0);
        }
        ViewGroup viewGroup = (ViewGroup) view;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < viewGroup.getChildCount(); i3++) {
            zza zzaVarZza = zza(viewGroup.getChildAt(i3), zzbiVar);
            i2 += zzaVarZza.zztb;
            i += zzaVarZza.zztc;
        }
        return new zza(i2, i);
    }

    void zza(Activity activity) {
        if (activity == null) {
            return;
        }
        View viewFindViewById = null;
        if (activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            viewFindViewById = activity.getWindow().getDecorView().findViewById(R.id.content);
        }
        if (viewFindViewById != null) {
            zzf(viewFindViewById);
        }
    }

    void zza(zzbi zzbiVar, WebView webView, String str) {
        zzbiVar.zzcw();
        try {
            if (!TextUtils.isEmpty(str)) {
                String strOptString = new JSONObject(str).optString("text");
                if (TextUtils.isEmpty(webView.getTitle())) {
                    zzbiVar.zzv(strOptString);
                } else {
                    zzbiVar.zzv(webView.getTitle() + "\n" + strOptString);
                }
            }
            if (zzbiVar.zzct()) {
                this.zzsQ.zzb(zzbiVar);
            }
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Json string may be malformed.");
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zza("Failed to get webview content.", th);
            this.zzsR.zza(th, true);
        }
    }

    boolean zza(ActivityManager.RunningAppProcessInfo runningAppProcessInfo) {
        return runningAppProcessInfo.importance == 100;
    }

    boolean zza(final WebView webView, final zzbi zzbiVar) {
        if (!zznx.zzrU()) {
            return false;
        }
        zzbiVar.zzcx();
        webView.post(new Runnable() { // from class: com.google.android.gms.internal.zzbl.2
            ValueCallback<String> zzsX = new ValueCallback<String>() { // from class: com.google.android.gms.internal.zzbl.2.1
                @Override // android.webkit.ValueCallback
                /* renamed from: zzy, reason: merged with bridge method [inline-methods] */
                public void onReceiveValue(String str) {
                    zzbl.this.zza(zzbiVar, webView, str);
                }
            };

            @Override // java.lang.Runnable
            public void run() {
                if (webView.getSettings().getJavaScriptEnabled()) {
                    try {
                        webView.evaluateJavascript("(function() { return  {text:document.body.innerText}})();", this.zzsX);
                    } catch (Throwable th) {
                        this.zzsX.onReceiveValue("");
                    }
                }
            }
        });
        return true;
    }

    public void zzcB() {
        synchronized (this.zzpK) {
            if (this.mStarted) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaF("Content hash thread already started, quiting...");
            } else {
                this.mStarted = true;
                start();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0045, code lost:
    
        if (zza(r0) == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x004b, code lost:
    
        if (r1.inKeyguardRestrictedInputMode() != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0051, code lost:
    
        if (zzs(r3) == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0053, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean zzcC() {
        /*
            r7 = this;
            r2 = 0
            com.google.android.gms.internal.zzbk r0 = r7.zzsP     // Catch: java.lang.Throwable -> L57
            android.content.Context r3 = r0.getContext()     // Catch: java.lang.Throwable -> L57
            if (r3 != 0) goto Lb
            r0 = r2
        La:
            return r0
        Lb:
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r3.getSystemService(r0)     // Catch: java.lang.Throwable -> L57
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0     // Catch: java.lang.Throwable -> L57
            java.lang.String r1 = "keyguard"
            java.lang.Object r1 = r3.getSystemService(r1)     // Catch: java.lang.Throwable -> L57
            android.app.KeyguardManager r1 = (android.app.KeyguardManager) r1     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L1f
            if (r1 != 0) goto L21
        L1f:
            r0 = r2
            goto La
        L21:
            java.util.List r0 = r0.getRunningAppProcesses()     // Catch: java.lang.Throwable -> L57
            if (r0 != 0) goto L29
            r0 = r2
            goto La
        L29:
            java.util.Iterator r4 = r0.iterator()     // Catch: java.lang.Throwable -> L57
        L2d:
            boolean r0 = r4.hasNext()     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L55
            java.lang.Object r0 = r4.next()     // Catch: java.lang.Throwable -> L57
            android.app.ActivityManager$RunningAppProcessInfo r0 = (android.app.ActivityManager.RunningAppProcessInfo) r0     // Catch: java.lang.Throwable -> L57
            int r5 = android.os.Process.myPid()     // Catch: java.lang.Throwable -> L57
            int r6 = r0.pid     // Catch: java.lang.Throwable -> L57
            if (r5 != r6) goto L2d
            boolean r0 = r7.zza(r0)     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L55
            boolean r0 = r1.inKeyguardRestrictedInputMode()     // Catch: java.lang.Throwable -> L57
            if (r0 != 0) goto L55
            boolean r0 = r7.zzs(r3)     // Catch: java.lang.Throwable -> L57
            if (r0 == 0) goto L55
            r0 = 1
            goto La
        L55:
            r0 = r2
            goto La
        L57:
            r0 = move-exception
            r0 = r2
            goto La
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzbl.zzcC():boolean");
    }

    public zzbi zzcD() {
        return this.zzsQ.zzcA();
    }

    public void zzcE() {
        synchronized (this.zzpK) {
            this.zzsO = true;
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("ContentFetchThread: paused, mPause = " + this.zzsO);
        }
    }

    public boolean zzcF() {
        return this.zzsO;
    }

    boolean zzf(final View view) {
        if (view == null) {
            return false;
        }
        view.post(new Runnable() { // from class: com.google.android.gms.internal.zzbl.1
            @Override // java.lang.Runnable
            public void run() {
                zzbl.this.zzg(view);
            }
        });
        return true;
    }

    void zzg(View view) {
        try {
            zzbi zzbiVar = new zzbi(this.zzsC, this.zzsT, this.zzsE, this.zzsU);
            zza zzaVarZza = zza(view, zzbiVar);
            zzbiVar.zzcy();
            if (zzaVarZza.zztb == 0 && zzaVarZza.zztc == 0) {
                return;
            }
            if (zzaVarZza.zztc == 0 && zzbiVar.zzcz() == 0) {
                return;
            }
            if (zzaVarZza.zztc == 0 && this.zzsQ.zza(zzbiVar)) {
                return;
            }
            this.zzsQ.zzc(zzbiVar);
        } catch (Exception e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Exception in fetchContentOnUIThread", e);
            this.zzsR.zza(e, true);
        }
    }

    boolean zzs(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        if (powerManager == null) {
            return false;
        }
        return powerManager.isScreenOn();
    }
}
