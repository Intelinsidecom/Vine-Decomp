package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.ads.internal.util.client.VersionInfoParcel;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.LinkedList;

@zzha
/* loaded from: classes.dex */
public class zzgz implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private VersionInfoParcel zzDY;
    private Thread.UncaughtExceptionHandler zzGb;
    private Thread.UncaughtExceptionHandler zzGc;

    public zzgz(Context context, VersionInfoParcel versionInfoParcel, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, Thread.UncaughtExceptionHandler uncaughtExceptionHandler2) {
        this.zzGb = uncaughtExceptionHandler;
        this.zzGc = uncaughtExceptionHandler2;
        this.mContext = context;
        this.zzDY = versionInfoParcel;
    }

    private static boolean zzA(Context context) {
        return zzbz.zzvl.get().booleanValue();
    }

    public static zzgz zza(Context context, Thread thread, VersionInfoParcel versionInfoParcel) {
        if (context == null || thread == null || versionInfoParcel == null) {
            return null;
        }
        if (!zzA(context)) {
            return null;
        }
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = thread.getUncaughtExceptionHandler();
        zzgz zzgzVar = new zzgz(context, versionInfoParcel, uncaughtExceptionHandler, Thread.getDefaultUncaughtExceptionHandler());
        if (uncaughtExceptionHandler != null && (uncaughtExceptionHandler instanceof zzgz)) {
            return (zzgz) uncaughtExceptionHandler;
        }
        try {
            thread.setUncaughtExceptionHandler(zzgzVar);
            return zzgzVar;
        } catch (SecurityException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzc("Fail to set UncaughtExceptionHandler.", e);
            return null;
        }
    }

    private Throwable zzb(Throwable th) {
        Throwable th2;
        if (zzbz.zzvm.get().booleanValue()) {
            return th;
        }
        LinkedList linkedList = new LinkedList();
        while (th != null) {
            linkedList.push(th);
            th = th.getCause();
        }
        Throwable th3 = null;
        while (!linkedList.isEmpty()) {
            Throwable th4 = (Throwable) linkedList.pop();
            StackTraceElement[] stackTrace = th4.getStackTrace();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new StackTraceElement(th4.getClass().getName(), "<filtered>", "<filtered>", 1));
            boolean z = false;
            for (StackTraceElement stackTraceElement : stackTrace) {
                if (zzas(stackTraceElement.getClassName())) {
                    arrayList.add(stackTraceElement);
                    z = true;
                } else if (zzat(stackTraceElement.getClassName())) {
                    arrayList.add(stackTraceElement);
                } else {
                    arrayList.add(new StackTraceElement("<filtered>", "<filtered>", "<filtered>", 1));
                }
            }
            if (z) {
                th2 = th3 == null ? new Throwable(th4.getMessage()) : new Throwable(th4.getMessage(), th3);
                th2.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]));
            } else {
                th2 = th3;
            }
            th3 = th2;
        }
        return th3;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable exception) {
        if (zza(exception)) {
            if (Looper.getMainLooper().getThread() != thread) {
                zza(exception, true);
                return;
            }
            zza(exception, false);
        }
        if (this.zzGb != null) {
            this.zzGb.uncaughtException(thread, exception);
        } else if (this.zzGc != null) {
            this.zzGc.uncaughtException(thread, exception);
        }
    }

    String zza(Class cls, Throwable th, boolean z) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return new Uri.Builder().scheme("https").path("//pagead2.googlesyndication.com/pagead/gen_204").appendQueryParameter("id", "gmob-apps-report-exception").appendQueryParameter("os", Build.VERSION.RELEASE).appendQueryParameter("api", String.valueOf(Build.VERSION.SDK_INT)).appendQueryParameter("device", com.google.android.gms.ads.internal.zzp.zzbx().zzhb()).appendQueryParameter("js", this.zzDY.afmaVersion).appendQueryParameter("appid", this.mContext.getApplicationContext().getPackageName()).appendQueryParameter("exceptiontype", cls.getName()).appendQueryParameter("stacktrace", stringWriter.toString()).appendQueryParameter("eids", TextUtils.join(",", zzbz.zzdl())).appendQueryParameter("trapped", String.valueOf(z)).toString();
    }

    public void zza(Throwable th, boolean z) {
        Throwable thZzb;
        if (zzA(this.mContext) && (thZzb = zzb(th)) != null) {
            Class<?> cls = th.getClass();
            ArrayList arrayList = new ArrayList();
            arrayList.add(zza(cls, thZzb, z));
            com.google.android.gms.ads.internal.zzp.zzbx().zza(this.mContext, this.zzDY.afmaVersion, arrayList, com.google.android.gms.ads.internal.zzp.zzbA().zzgP());
        }
    }

    protected boolean zza(Throwable th) {
        if (th == null) {
            return false;
        }
        boolean z = false;
        boolean z2 = false;
        while (th != null) {
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (zzas(stackTraceElement.getClassName())) {
                    z2 = true;
                }
                if (getClass().getName().equals(stackTraceElement.getClassName())) {
                    z = true;
                }
            }
            th = th.getCause();
        }
        return z2 && !z;
    }

    protected boolean zzas(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!str.startsWith("com.google.android.gms.ads") && !str.startsWith("com.google.ads")) {
            try {
                return Class.forName(str).isAnnotationPresent(zzha.class);
            } catch (Exception e) {
                com.google.android.gms.ads.internal.util.client.zzb.zza("Fail to check class type for class " + str, e);
                return false;
            }
        }
        return true;
    }

    protected boolean zzat(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith("android.") || str.startsWith("java.");
    }
}
