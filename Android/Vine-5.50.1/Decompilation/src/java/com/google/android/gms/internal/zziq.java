package com.google.android.gms.internal;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;

@zzha
/* loaded from: classes.dex */
public class zziq {

    public static class zza extends zziq {
        public zza() {
            super();
        }

        @Override // com.google.android.gms.internal.zziq
        public boolean zza(DownloadManager.Request request) {
            request.setShowRunningNotification(true);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public int zzhd() {
            return 6;
        }

        @Override // com.google.android.gms.internal.zziq
        public int zzhe() {
            return 7;
        }
    }

    public static class zzb extends zza {
        @Override // com.google.android.gms.internal.zziq.zza, com.google.android.gms.internal.zziq
        public boolean zza(DownloadManager.Request request) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(1);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public boolean zza(final Context context, WebSettings webSettings) {
            super.zza(context, webSettings);
            if (((File) zziz.zzb(new Callable<File>() { // from class: com.google.android.gms.internal.zziq.zzb.1
                @Override // java.util.concurrent.Callable
                /* renamed from: zzhh, reason: merged with bridge method [inline-methods] */
                public File call() {
                    return context.getCacheDir();
                }
            })) != null) {
                webSettings.setAppCachePath(context.getCacheDir().getAbsolutePath());
                webSettings.setAppCacheMaxSize(0L);
                webSettings.setAppCacheEnabled(true);
            }
            webSettings.setDatabasePath(context.getDatabasePath("com.google.android.gms.ads.db").getAbsolutePath());
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setAllowContentAccess(false);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public boolean zza(Window window) {
            window.setFlags(16777216, 16777216);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public zzjo zzb(zzjn zzjnVar, boolean z) {
            return new zzju(zzjnVar, z);
        }

        @Override // com.google.android.gms.internal.zziq
        public Set<String> zzf(Uri uri) {
            return uri.getQueryParameterNames();
        }

        @Override // com.google.android.gms.internal.zziq
        public WebChromeClient zzh(zzjn zzjnVar) {
            return new zzjt(zzjnVar);
        }

        @Override // com.google.android.gms.internal.zziq
        public boolean zzm(View view) {
            view.setLayerType(0, null);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public boolean zzn(View view) {
            view.setLayerType(1, null);
            return true;
        }
    }

    public static class zzc extends zzb {
        @Override // com.google.android.gms.internal.zziq
        public String zza(SslError sslError) {
            return sslError.getUrl();
        }

        @Override // com.google.android.gms.internal.zziq.zzb, com.google.android.gms.internal.zziq
        public WebChromeClient zzh(zzjn zzjnVar) {
            return new zzjv(zzjnVar);
        }
    }

    public static class zzd extends zzf {
        @Override // com.google.android.gms.internal.zziq
        public String getDefaultUserAgent(Context context) {
            return WebSettings.getDefaultUserAgent(context);
        }

        @Override // com.google.android.gms.internal.zziq
        public Drawable zza(Context context, Bitmap bitmap, boolean z, float f) {
            if (!z || f <= 0.0f || f > 25.0f) {
                return new BitmapDrawable(context.getResources(), bitmap);
            }
            try {
                Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmapCreateScaledBitmap);
                RenderScript renderScriptCreate = RenderScript.create(context);
                ScriptIntrinsicBlur scriptIntrinsicBlurCreate = ScriptIntrinsicBlur.create(renderScriptCreate, Element.U8_4(renderScriptCreate));
                Allocation allocationCreateFromBitmap = Allocation.createFromBitmap(renderScriptCreate, bitmapCreateScaledBitmap);
                Allocation allocationCreateFromBitmap2 = Allocation.createFromBitmap(renderScriptCreate, bitmapCreateBitmap);
                scriptIntrinsicBlurCreate.setRadius(f);
                scriptIntrinsicBlurCreate.setInput(allocationCreateFromBitmap);
                scriptIntrinsicBlurCreate.forEach(allocationCreateFromBitmap2);
                allocationCreateFromBitmap2.copyTo(bitmapCreateBitmap);
                return new BitmapDrawable(context.getResources(), bitmapCreateBitmap);
            } catch (RuntimeException e) {
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        @Override // com.google.android.gms.internal.zziq.zzf, com.google.android.gms.internal.zziq.zzb, com.google.android.gms.internal.zziq
        public boolean zza(Context context, WebSettings webSettings) {
            super.zza(context, webSettings);
            webSettings.setMediaPlaybackRequiresUserGesture(false);
            return true;
        }
    }

    public static class zze extends zzd {
        @Override // com.google.android.gms.internal.zziq
        public boolean isAttachedToWindow(View view) {
            return super.isAttachedToWindow(view) || view.getWindowId() != null;
        }

        @Override // com.google.android.gms.internal.zziq
        public int zzhf() {
            return 14;
        }
    }

    public static class zzf extends zzc {
        @Override // com.google.android.gms.internal.zziq
        public void zza(View view, Drawable drawable) {
            view.setBackground(drawable);
        }

        @Override // com.google.android.gms.internal.zziq
        public void zza(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        @Override // com.google.android.gms.internal.zziq.zzb, com.google.android.gms.internal.zziq
        public boolean zza(Context context, WebSettings webSettings) {
            super.zza(context, webSettings);
            webSettings.setAllowFileAccessFromFileURLs(false);
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            return true;
        }

        @Override // com.google.android.gms.internal.zziq
        public void zzb(Activity activity, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            Window window = activity.getWindow();
            if (window == null || window.getDecorView() == null || window.getDecorView().getViewTreeObserver() == null) {
                return;
            }
            zza(window.getDecorView().getViewTreeObserver(), onGlobalLayoutListener);
        }
    }

    public static class zzg extends zze {
        @Override // com.google.android.gms.internal.zziq.zze, com.google.android.gms.internal.zziq
        public boolean isAttachedToWindow(View view) {
            return view.isAttachedToWindow();
        }

        @Override // com.google.android.gms.internal.zziq
        public ViewGroup.LayoutParams zzhg() {
            return new ViewGroup.LayoutParams(-1, -1);
        }
    }

    private zziq() {
    }

    public static zziq zzP(int i) {
        return i >= 19 ? new zzg() : i >= 18 ? new zze() : i >= 17 ? new zzd() : i >= 16 ? new zzf() : i >= 14 ? new zzc() : i >= 11 ? new zzb() : i >= 9 ? new zza() : new zziq();
    }

    public String getDefaultUserAgent(Context context) {
        return "";
    }

    public boolean isAttachedToWindow(View view) {
        return (view.getWindowToken() == null && view.getWindowVisibility() == 8) ? false : true;
    }

    public Drawable zza(Context context, Bitmap bitmap, boolean z, float f) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public String zza(SslError sslError) {
        return "";
    }

    public void zza(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }

    public void zza(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }

    public boolean zza(DownloadManager.Request request) {
        return false;
    }

    public boolean zza(Context context, WebSettings webSettings) {
        return false;
    }

    public boolean zza(Window window) {
        return false;
    }

    public zzjo zzb(zzjn zzjnVar, boolean z) {
        return new zzjo(zzjnVar, z);
    }

    public void zzb(Activity activity, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null || window.getDecorView().getViewTreeObserver() == null) {
            return;
        }
        zza(window.getDecorView().getViewTreeObserver(), onGlobalLayoutListener);
    }

    public Set<String> zzf(Uri uri) {
        String encodedQuery;
        if (!uri.isOpaque() && (encodedQuery = uri.getEncodedQuery()) != null) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            int i = 0;
            do {
                int iIndexOf = encodedQuery.indexOf(38, i);
                if (iIndexOf == -1) {
                    iIndexOf = encodedQuery.length();
                }
                int iIndexOf2 = encodedQuery.indexOf(61, i);
                if (iIndexOf2 > iIndexOf || iIndexOf2 == -1) {
                    iIndexOf2 = iIndexOf;
                }
                linkedHashSet.add(Uri.decode(encodedQuery.substring(i, iIndexOf2)));
                i = iIndexOf + 1;
            } while (i < encodedQuery.length());
            return Collections.unmodifiableSet(linkedHashSet);
        }
        return Collections.emptySet();
    }

    public boolean zzf(zzjn zzjnVar) {
        if (zzjnVar == null) {
            return false;
        }
        zzjnVar.onPause();
        return true;
    }

    public boolean zzg(zzjn zzjnVar) {
        if (zzjnVar == null) {
            return false;
        }
        zzjnVar.onResume();
        return true;
    }

    public WebChromeClient zzh(zzjn zzjnVar) {
        return null;
    }

    public int zzhd() {
        return 0;
    }

    public int zzhe() {
        return 1;
    }

    public int zzhf() {
        return 5;
    }

    public ViewGroup.LayoutParams zzhg() {
        return new ViewGroup.LayoutParams(-2, -2);
    }

    public boolean zzm(View view) {
        return false;
    }

    public boolean zzn(View view) {
        return false;
    }
}
