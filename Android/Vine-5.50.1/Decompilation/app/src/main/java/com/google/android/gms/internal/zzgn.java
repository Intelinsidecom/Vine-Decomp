package com.google.android.gms.internal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import com.google.android.gms.ads.internal.request.AdResponseParcel;
import com.google.android.gms.internal.zzjo;

@zzha
/* loaded from: classes.dex */
public class zzgn implements Runnable {
    private final Handler zzFi;
    private final long zzFj;
    private long zzFk;
    private zzjo.zza zzFl;
    protected boolean zzFm;
    protected boolean zzFn;
    private final int zzov;
    private final int zzow;
    protected final zzjn zzps;

    protected final class zza extends AsyncTask<Void, Void, Boolean> {
        private final WebView zzFo;
        private Bitmap zzFp;

        public zza(WebView webView) {
            this.zzFo = webView;
        }

        @Override // android.os.AsyncTask
        protected synchronized void onPreExecute() {
            this.zzFp = Bitmap.createBitmap(zzgn.this.zzov, zzgn.this.zzow, Bitmap.Config.ARGB_8888);
            this.zzFo.setVisibility(0);
            this.zzFo.measure(View.MeasureSpec.makeMeasureSpec(zzgn.this.zzov, 0), View.MeasureSpec.makeMeasureSpec(zzgn.this.zzow, 0));
            this.zzFo.layout(0, 0, zzgn.this.zzov, zzgn.this.zzow);
            this.zzFo.draw(new Canvas(this.zzFp));
            this.zzFo.invalidate();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public synchronized Boolean doInBackground(Void... voidArr) {
            boolean zValueOf;
            int width = this.zzFp.getWidth();
            int height = this.zzFp.getHeight();
            if (width == 0 || height == 0) {
                zValueOf = false;
            } else {
                int i = 0;
                for (int i2 = 0; i2 < width; i2 += 10) {
                    for (int i3 = 0; i3 < height; i3 += 10) {
                        if (this.zzFp.getPixel(i2, i3) != 0) {
                            i++;
                        }
                    }
                }
                zValueOf = Boolean.valueOf(((double) i) / (((double) (width * height)) / 100.0d) > 0.1d);
            }
            return zValueOf;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: zza, reason: merged with bridge method [inline-methods] */
        public void onPostExecute(Boolean bool) {
            zzgn.zzc(zzgn.this);
            if (bool.booleanValue() || zzgn.this.zzfU() || zzgn.this.zzFk <= 0) {
                zzgn.this.zzFn = bool.booleanValue();
                zzgn.this.zzFl.zza(zzgn.this.zzps, true);
            } else if (zzgn.this.zzFk > 0) {
                if (com.google.android.gms.ads.internal.util.client.zzb.zzQ(2)) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzaF("Ad not detected, scheduling another run.");
                }
                zzgn.this.zzFi.postDelayed(zzgn.this, zzgn.this.zzFj);
            }
        }
    }

    public zzgn(zzjo.zza zzaVar, zzjn zzjnVar, int i, int i2) {
        this(zzaVar, zzjnVar, i, i2, 200L, 50L);
    }

    public zzgn(zzjo.zza zzaVar, zzjn zzjnVar, int i, int i2, long j, long j2) {
        this.zzFj = j;
        this.zzFk = j2;
        this.zzFi = new Handler(Looper.getMainLooper());
        this.zzps = zzjnVar;
        this.zzFl = zzaVar;
        this.zzFm = false;
        this.zzFn = false;
        this.zzow = i2;
        this.zzov = i;
    }

    static /* synthetic */ long zzc(zzgn zzgnVar) {
        long j = zzgnVar.zzFk - 1;
        zzgnVar.zzFk = j;
        return j;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.zzps == null || zzfU()) {
            this.zzFl.zza(this.zzps, true);
        } else {
            new zza(this.zzps.getWebView()).execute(new Void[0]);
        }
    }

    public void zza(AdResponseParcel adResponseParcel) {
        zza(adResponseParcel, new zzjw(this, this.zzps, adResponseParcel.zzGU));
    }

    public void zza(AdResponseParcel adResponseParcel, zzjw zzjwVar) {
        this.zzps.setWebViewClient(zzjwVar);
        this.zzps.loadDataWithBaseURL(TextUtils.isEmpty(adResponseParcel.zzDE) ? null : com.google.android.gms.ads.internal.zzp.zzbx().zzaz(adResponseParcel.zzDE), adResponseParcel.body, "text/html", "UTF-8", null);
    }

    public void zzfS() {
        this.zzFi.postDelayed(this, this.zzFj);
    }

    public synchronized void zzfT() {
        this.zzFm = true;
    }

    public synchronized boolean zzfU() {
        return this.zzFm;
    }

    public boolean zzfV() {
        return this.zzFn;
    }
}
