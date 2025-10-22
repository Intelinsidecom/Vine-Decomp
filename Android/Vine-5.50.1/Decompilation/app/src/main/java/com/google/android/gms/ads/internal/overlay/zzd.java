package com.google.android.gms.ads.internal.overlay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzfu;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzil;
import com.google.android.gms.internal.zzip;
import com.google.android.gms.internal.zzis;
import com.google.android.gms.internal.zzjn;
import com.google.android.gms.internal.zzjo;
import java.io.IOException;
import java.util.Collections;

@zzha
/* loaded from: classes.dex */
public class zzd extends zzfu.zza implements zzo {
    static final int zzDg = Color.argb(0, 0, 0, 0);
    private final Activity mActivity;
    RelativeLayout zzCm;
    AdOverlayInfoParcel zzDh;
    zzc zzDi;
    zzm zzDj;
    FrameLayout zzDl;
    WebChromeClient.CustomViewCallback zzDm;
    private boolean zzDr;
    zzjn zzps;
    boolean zzDk = false;
    boolean zzDn = false;
    boolean zzDo = false;
    boolean zzDp = false;
    int zzDq = 0;
    private boolean zzDs = false;
    private boolean zzDt = true;

    @zzha
    private static final class zza extends Exception {
        public zza(String str) {
            super(str);
        }
    }

    @zzha
    static final class zzb extends RelativeLayout {
        zzis zzry;

        public zzb(Context context, String str) {
            super(context);
            this.zzry = new zzis(context, str);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent event) {
            this.zzry.zze(event);
            return false;
        }
    }

    @zzha
    public static class zzc {
        public final Context context;
        public final int index;
        public final ViewGroup.LayoutParams zzDv;
        public final ViewGroup zzDw;

        public zzc(zzjn zzjnVar) throws zza {
            this.zzDv = zzjnVar.getLayoutParams();
            ViewParent parent = zzjnVar.getParent();
            this.context = zzjnVar.zzhy();
            if (parent == null || !(parent instanceof ViewGroup)) {
                throw new zza("Could not get the parent of the WebView for an overlay.");
            }
            this.zzDw = (ViewGroup) parent;
            this.index = this.zzDw.indexOfChild(zzjnVar.getView());
            this.zzDw.removeView(zzjnVar.getView());
            zzjnVar.zzD(true);
        }
    }

    @zzha
    /* renamed from: com.google.android.gms.ads.internal.overlay.zzd$zzd, reason: collision with other inner class name */
    private class C0023zzd extends zzil {
        private C0023zzd() {
        }

        @Override // com.google.android.gms.internal.zzil
        public void onStop() {
        }

        @Override // com.google.android.gms.internal.zzil
        public void zzbp() throws IOException {
            Bitmap bitmapZze = com.google.android.gms.ads.internal.zzp.zzbx().zze(zzd.this.mActivity, zzd.this.zzDh.zzDL.zzqc);
            if (bitmapZze != null) {
                final Drawable drawableZza = com.google.android.gms.ads.internal.zzp.zzbz().zza(zzd.this.mActivity, bitmapZze, zzd.this.zzDh.zzDL.zzqd, zzd.this.zzDh.zzDL.zzqe);
                zzip.zzKO.post(new Runnable() { // from class: com.google.android.gms.ads.internal.overlay.zzd.zzd.1
                    @Override // java.lang.Runnable
                    public void run() {
                        zzd.this.mActivity.getWindow().setBackgroundDrawable(drawableZza);
                    }
                });
            }
        }
    }

    public zzd(Activity activity) {
        this.mActivity = activity;
    }

    public void close() {
        this.zzDq = 2;
        this.mActivity.finish();
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onBackPressed() {
        this.zzDq = 0;
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onCreate(Bundle savedInstanceState) throws zza {
        this.zzDn = savedInstanceState != null ? savedInstanceState.getBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", false) : false;
        try {
            this.zzDh = AdOverlayInfoParcel.zzb(this.mActivity.getIntent());
            if (this.zzDh == null) {
                throw new zza("Could not get info for ad overlay.");
            }
            if (this.zzDh.zzqR.zzLG > 7500000) {
                this.zzDq = 3;
            }
            if (this.mActivity.getIntent() != null) {
                this.zzDt = this.mActivity.getIntent().getBooleanExtra("shouldCallOnOverlayOpened", true);
            }
            if (this.zzDh.zzDL != null) {
                this.zzDo = this.zzDh.zzDL.zzqa;
            } else {
                this.zzDo = false;
            }
            if (zzbz.zzwI.get().booleanValue() && this.zzDo && this.zzDh.zzDL.zzqc != null) {
                new C0023zzd().zzfR();
            }
            if (savedInstanceState == null) {
                if (this.zzDh.zzDB != null && this.zzDt) {
                    this.zzDh.zzDB.zzaY();
                }
                if (this.zzDh.zzDI != 1 && this.zzDh.zzDA != null) {
                    this.zzDh.zzDA.onAdClicked();
                }
            }
            this.zzCm = new zzb(this.mActivity, this.zzDh.zzDK);
            switch (this.zzDh.zzDI) {
                case 1:
                    zzx(false);
                    return;
                case 2:
                    this.zzDi = new zzc(this.zzDh.zzDC);
                    zzx(false);
                    return;
                case 3:
                    zzx(true);
                    return;
                case 4:
                    if (this.zzDn) {
                        this.zzDq = 3;
                        this.mActivity.finish();
                        return;
                    } else {
                        if (com.google.android.gms.ads.internal.zzp.zzbu().zza(this.mActivity, this.zzDh.zzDz, this.zzDh.zzDH)) {
                            return;
                        }
                        this.zzDq = 3;
                        this.mActivity.finish();
                        return;
                    }
                default:
                    throw new zza("Could not determine ad overlay type.");
            }
        } catch (zza e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(e.getMessage());
            this.zzDq = 3;
            this.mActivity.finish();
        }
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onDestroy() {
        if (this.zzps != null) {
            this.zzCm.removeView(this.zzps.getView());
        }
        zzfe();
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onPause() {
        zzfa();
        if (this.zzps != null && (!this.mActivity.isFinishing() || this.zzDi == null)) {
            com.google.android.gms.ads.internal.zzp.zzbz().zzf(this.zzps);
        }
        zzfe();
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onRestart() {
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onResume() {
        if (this.zzDh != null && this.zzDh.zzDI == 4) {
            if (this.zzDn) {
                this.zzDq = 3;
                this.mActivity.finish();
            } else {
                this.zzDn = true;
            }
        }
        if (this.zzps == null || this.zzps.isDestroyed()) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("The webview does not exit. Ignoring action.");
        } else {
            com.google.android.gms.ads.internal.zzp.zzbz().zzg(this.zzps);
        }
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putBoolean("com.google.android.gms.ads.internal.overlay.hasResumed", this.zzDn);
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onStart() {
    }

    @Override // com.google.android.gms.internal.zzfu
    public void onStop() {
        zzfe();
    }

    public void setRequestedOrientation(int requestedOrientation) {
        this.mActivity.setRequestedOrientation(requestedOrientation);
    }

    public void zza(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.zzDl = new FrameLayout(this.mActivity);
        this.zzDl.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.zzDl.addView(view, -1, -1);
        this.mActivity.setContentView(this.zzDl);
        zzaF();
        this.zzDm = customViewCallback;
        this.zzDk = true;
    }

    public void zza(boolean z, boolean z2) {
        if (this.zzDj != null) {
            this.zzDj.zza(z, z2);
        }
    }

    @Override // com.google.android.gms.internal.zzfu
    public void zzaF() {
        this.zzDr = true;
    }

    public void zzfa() {
        if (this.zzDh != null && this.zzDk) {
            setRequestedOrientation(this.zzDh.orientation);
        }
        if (this.zzDl != null) {
            this.mActivity.setContentView(this.zzCm);
            zzaF();
            this.zzDl.removeAllViews();
            this.zzDl = null;
        }
        if (this.zzDm != null) {
            this.zzDm.onCustomViewHidden();
            this.zzDm = null;
        }
        this.zzDk = false;
    }

    @Override // com.google.android.gms.ads.internal.overlay.zzo
    public void zzfb() {
        this.zzDq = 1;
        this.mActivity.finish();
    }

    @Override // com.google.android.gms.internal.zzfu
    public boolean zzfc() {
        this.zzDq = 0;
        if (this.zzps == null) {
            return true;
        }
        boolean zZzhI = this.zzps.zzhI();
        if (zZzhI) {
            return zZzhI;
        }
        this.zzps.zzb("onbackblocked", Collections.emptyMap());
        return zZzhI;
    }

    public void zzfd() {
        this.zzCm.removeView(this.zzDj);
        zzw(true);
    }

    protected void zzfe() {
        if (!this.mActivity.isFinishing() || this.zzDs) {
            return;
        }
        this.zzDs = true;
        if (this.zzps != null) {
            zzy(this.zzDq);
            this.zzCm.removeView(this.zzps.getView());
            if (this.zzDi != null) {
                this.zzps.setContext(this.zzDi.context);
                this.zzps.zzD(false);
                this.zzDi.zzDw.addView(this.zzps.getView(), this.zzDi.index, this.zzDi.zzDv);
                this.zzDi = null;
            }
            this.zzps = null;
        }
        if (this.zzDh == null || this.zzDh.zzDB == null) {
            return;
        }
        this.zzDh.zzDB.zzaX();
    }

    public void zzff() {
        if (this.zzDp) {
            this.zzDp = false;
            zzfg();
        }
    }

    protected void zzfg() {
        this.zzps.zzfg();
    }

    public void zzw(boolean z) {
        this.zzDj = new zzm(this.mActivity, z ? 50 : 32, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(10);
        layoutParams.addRule(z ? 11 : 9);
        this.zzDj.zza(z, this.zzDh.zzDF);
        this.zzCm.addView(this.zzDj, layoutParams);
    }

    protected void zzx(boolean z) throws zza {
        if (!this.zzDr) {
            this.mActivity.requestWindowFeature(1);
        }
        Window window = this.mActivity.getWindow();
        if (window == null) {
            throw new zza("Invalid activity, no window available.");
        }
        if (!this.zzDo || (this.zzDh.zzDL != null && this.zzDh.zzDL.zzqb)) {
            window.setFlags(1024, 1024);
        }
        boolean zZzcb = this.zzDh.zzDC.zzhC().zzcb();
        this.zzDp = false;
        if (zZzcb) {
            if (this.zzDh.orientation == com.google.android.gms.ads.internal.zzp.zzbz().zzhd()) {
                this.zzDp = this.mActivity.getResources().getConfiguration().orientation == 1;
            } else if (this.zzDh.orientation == com.google.android.gms.ads.internal.zzp.zzbz().zzhe()) {
                this.zzDp = this.mActivity.getResources().getConfiguration().orientation == 2;
            }
        }
        com.google.android.gms.ads.internal.util.client.zzb.zzaF("Delay onShow to next orientation change: " + this.zzDp);
        setRequestedOrientation(this.zzDh.orientation);
        if (com.google.android.gms.ads.internal.zzp.zzbz().zza(window)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaF("Hardware acceleration on the AdActivity window enabled.");
        }
        if (this.zzDo) {
            this.zzCm.setBackgroundColor(zzDg);
        } else {
            this.zzCm.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.mActivity.setContentView(this.zzCm);
        zzaF();
        if (z) {
            this.zzps = com.google.android.gms.ads.internal.zzp.zzby().zza(this.mActivity, this.zzDh.zzDC.zzaP(), true, zZzcb, null, this.zzDh.zzqR);
            this.zzps.zzhC().zzb(null, null, this.zzDh.zzDD, this.zzDh.zzDH, true, this.zzDh.zzDJ, null, this.zzDh.zzDC.zzhC().zzhO(), null);
            this.zzps.zzhC().zza(new zzjo.zza() { // from class: com.google.android.gms.ads.internal.overlay.zzd.1
                @Override // com.google.android.gms.internal.zzjo.zza
                public void zza(zzjn zzjnVar, boolean z2) {
                    zzjnVar.zzfg();
                }
            });
            if (this.zzDh.url != null) {
                this.zzps.loadUrl(this.zzDh.url);
            } else {
                if (this.zzDh.zzDG == null) {
                    throw new zza("No URL or HTML to display in ad overlay.");
                }
                this.zzps.loadDataWithBaseURL(this.zzDh.zzDE, this.zzDh.zzDG, "text/html", "UTF-8", null);
            }
            if (this.zzDh.zzDC != null) {
                this.zzDh.zzDC.zzc(this);
            }
        } else {
            this.zzps = this.zzDh.zzDC;
            this.zzps.setContext(this.mActivity);
        }
        this.zzps.zzb(this);
        ViewParent parent = this.zzps.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).removeView(this.zzps.getView());
        }
        if (this.zzDo) {
            this.zzps.setBackgroundColor(zzDg);
        }
        this.zzCm.addView(this.zzps.getView(), -1, -1);
        if (!z && !this.zzDp) {
            zzfg();
        }
        zzw(zZzcb);
        if (this.zzps.zzhD()) {
            zza(zZzcb, true);
        }
    }

    protected void zzy(int i) {
        this.zzps.zzy(i);
    }
}
