package com.google.android.gms.common;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompatExtras;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zznj;
import com.google.android.gms.internal.zznx;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public final class GooglePlayServicesUtil {
    public static final String GMS_ERROR_DIALOG = "GooglePlayServicesErrorDialog";

    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";

    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = zzov();
    public static boolean zzaee = false;
    public static boolean zzaef = false;
    private static int zzaeg = -1;
    private static final Object zzqf = new Object();
    private static String zzaeh = null;
    private static Integer zzaei = null;
    static final AtomicBoolean zzaej = new AtomicBoolean();
    private static final AtomicBoolean zzaek = new AtomicBoolean();

    /* loaded from: classes2.dex */
    private static class zza extends Handler {
        private final Context zzrI;

        zza(Context context) {
            super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
            this.zzrI = context.getApplicationContext();
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
            switch (msg.what) {
                case 1:
                    int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.zzrI);
                    if (GooglePlayServicesUtil.isUserRecoverableError(iIsGooglePlayServicesAvailable)) {
                        GooglePlayServicesUtil.zza(iIsGooglePlayServicesAvailable, this.zzrI);
                        break;
                    }
                    break;
                default:
                    Log.w("GooglePlayServicesUtil", "Don't know how to handle this message: " + msg.what);
                    break;
            }
        }
    }

    private GooglePlayServicesUtil() {
    }

    @Deprecated
    public static Dialog getErrorDialog(int errorCode, Activity activity, int requestCode) {
        return getErrorDialog(errorCode, activity, requestCode, null);
    }

    @Deprecated
    public static Dialog getErrorDialog(int errorCode, Activity activity, int requestCode, DialogInterface.OnCancelListener cancelListener) {
        return zza(errorCode, activity, null, requestCode, cancelListener);
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int errorCode, Context context, int requestCode) {
        return GoogleApiAvailability.getInstance().getErrorResolutionPendingIntent(context, errorCode, requestCode);
    }

    @Deprecated
    public static String getErrorString(int errorCode) {
        return ConnectionResult.getStatusString(errorCode);
    }

    @Deprecated
    public static String getOpenSourceSoftwareLicenseInfo(Context context) throws IOException {
        try {
            InputStream inputStreamOpenInputStream = context.getContentResolver().openInputStream(new Uri.Builder().scheme("android.resource").authority(GOOGLE_PLAY_SERVICES_PACKAGE).appendPath("raw").appendPath("oss_notice").build());
            try {
                String next = new Scanner(inputStreamOpenInputStream).useDelimiter("\\A").next();
                if (inputStreamOpenInputStream == null) {
                    return next;
                }
                inputStreamOpenInputStream.close();
                return next;
            } catch (NoSuchElementException e) {
                if (inputStreamOpenInputStream != null) {
                    inputStreamOpenInputStream.close();
                }
                return null;
            } catch (Throwable th) {
                if (inputStreamOpenInputStream != null) {
                    inputStreamOpenInputStream.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext(GOOGLE_PLAY_SERVICES_PACKAGE, 3);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication(GOOGLE_PLAY_SERVICES_PACKAGE);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e5  */
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int isGooglePlayServicesAvailable(android.content.Context r9) throws android.content.pm.PackageManager.NameNotFoundException {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(android.content.Context):int");
    }

    @Deprecated
    public static boolean isUserRecoverableError(int errorCode) {
        switch (errorCode) {
            case 1:
            case 2:
            case 3:
            case 9:
                return true;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            default:
                return false;
        }
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int errorCode, Activity activity, int requestCode) {
        return showErrorDialogFragment(errorCode, activity, requestCode, null);
    }

    @Deprecated
    public static boolean showErrorDialogFragment(int errorCode, Activity activity, int requestCode, DialogInterface.OnCancelListener cancelListener) {
        return showErrorDialogFragment(errorCode, activity, null, requestCode, cancelListener);
    }

    public static boolean showErrorDialogFragment(int errorCode, Activity activity, Fragment fragment, int requestCode, DialogInterface.OnCancelListener cancelListener) {
        Dialog dialogZza = zza(errorCode, activity, fragment, requestCode, cancelListener);
        if (dialogZza == null) {
            return false;
        }
        zza(activity, cancelListener, GMS_ERROR_DIALOG, dialogZza);
        return true;
    }

    @Deprecated
    public static void showErrorNotification(int errorCode, Context context) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        if (zznj.zzav(context) && errorCode == 2) {
            errorCode = 42;
        }
        if (zzd(context, errorCode) || zzf(context, errorCode)) {
            zzal(context);
        } else {
            zza(errorCode, context);
        }
    }

    private static Dialog zza(int i, Activity activity, Fragment fragment, int i2, DialogInterface.OnCancelListener onCancelListener) {
        AlertDialog.Builder builder = null;
        if (i == 0) {
            return null;
        }
        if (zznj.zzav(activity) && i == 2) {
            i = 42;
        }
        if (zznx.zzrQ()) {
            TypedValue typedValue = new TypedValue();
            activity.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
            if ("Theme.Dialog.Alert".equals(activity.getResources().getResourceEntryName(typedValue.resourceId))) {
                builder = new AlertDialog.Builder(activity, 5);
            }
        }
        if (builder == null) {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setMessage(zzg.zzc(activity, i, zzam(activity)));
        if (onCancelListener != null) {
            builder.setOnCancelListener(onCancelListener);
        }
        Intent intentZza = GoogleApiAvailability.getInstance().zza(activity, i, "d");
        zzh zzhVar = fragment == null ? new zzh(activity, intentZza, i2) : new zzh(fragment, intentZza, i2);
        String strZzh = zzg.zzh(activity, i);
        if (strZzh != null) {
            builder.setPositiveButton(strZzh, zzhVar);
        }
        String strZzg = zzg.zzg(activity, i);
        if (strZzg != null) {
            builder.setTitle(strZzg);
        }
        return builder.create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void zza(int i, Context context) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        zza(i, context, null);
    }

    private static void zza(int i, Context context, String str) throws Resources.NotFoundException, PackageManager.NameNotFoundException {
        Notification notificationBuild;
        Notification notification;
        int i2;
        Resources resources = context.getResources();
        String strZzam = zzam(context);
        String strZzi = zzg.zzi(context, i);
        if (strZzi == null) {
            strZzi = resources.getString(com.google.android.gms.R.string.common_google_play_services_notification_ticker);
        }
        String strZzd = zzg.zzd(context, i, strZzam);
        PendingIntent pendingIntentZza = GoogleApiAvailability.getInstance().zza(context, i, 0, "n");
        if (zznj.zzav(context)) {
            zzx.zzaa(zznx.zzrR());
            notificationBuild = new Notification.Builder(context).setSmallIcon(com.google.android.gms.R.drawable.common_ic_googleplayservices).setPriority(2).setAutoCancel(true).setStyle(new Notification.BigTextStyle().bigText(strZzi + " " + strZzd)).addAction(com.google.android.gms.R.drawable.common_full_open_on_phone, resources.getString(com.google.android.gms.R.string.common_open_on_phone), pendingIntentZza).build();
        } else {
            String string = resources.getString(com.google.android.gms.R.string.common_google_play_services_notification_ticker);
            if (zznx.zzrN()) {
                Notification.Builder autoCancel = new Notification.Builder(context).setSmallIcon(R.drawable.stat_sys_warning).setContentTitle(strZzi).setContentText(strZzd).setContentIntent(pendingIntentZza).setTicker(string).setAutoCancel(true);
                if (zznx.zzrV()) {
                    autoCancel.setLocalOnly(true);
                }
                if (zznx.zzrR()) {
                    autoCancel.setStyle(new Notification.BigTextStyle().bigText(strZzd));
                    notification = autoCancel.build();
                } else {
                    notification = autoCancel.getNotification();
                }
                if (Build.VERSION.SDK_INT == 19) {
                    notification.extras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
                }
                notificationBuild = notification;
            } else {
                notificationBuild = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.stat_sys_warning).setTicker(string).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntentZza).setContentTitle(strZzi).setContentText(strZzd).build();
            }
        }
        if (zzbw(i)) {
            zzaej.set(false);
            i2 = 10436;
        } else {
            i2 = 39789;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (str != null) {
            notificationManager.notify(str, i2, notificationBuild);
        } else {
            notificationManager.notify(i2, notificationBuild);
        }
    }

    public static void zza(Activity activity, DialogInterface.OnCancelListener onCancelListener, String str, Dialog dialog) {
        boolean z;
        try {
            z = activity instanceof FragmentActivity;
        } catch (NoClassDefFoundError e) {
            z = false;
        }
        if (z) {
            SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show(((FragmentActivity) activity).getSupportFragmentManager(), str);
        } else {
            if (!zznx.zzrN()) {
                throw new RuntimeException("This Activity does not support Fragments.");
            }
            ErrorDialogFragment.newInstance(dialog, onCancelListener).show(activity.getFragmentManager(), str);
        }
    }

    @Deprecated
    public static void zzac(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        int iIsGooglePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (iIsGooglePlayServicesAvailable != 0) {
            Intent intentZza = GoogleApiAvailability.getInstance().zza(context, iIsGooglePlayServicesAvailable, "e");
            Log.e("GooglePlayServicesUtil", "GooglePlayServices not available due to error " + iIsGooglePlayServicesAvailable);
            if (intentZza != null) {
                throw new GooglePlayServicesRepairableException(iIsGooglePlayServicesAvailable, "Google Play Services not available", intentZza);
            }
            throw new GooglePlayServicesNotAvailableException(iIsGooglePlayServicesAvailable);
        }
    }

    @Deprecated
    public static void zzaj(Context context) {
        if (zzaej.getAndSet(true)) {
            return;
        }
        try {
            ((NotificationManager) context.getSystemService("notification")).cancel(10436);
        } catch (SecurityException e) {
        }
    }

    private static void zzak(Context context) {
        Integer num;
        if (zzaek.get()) {
            return;
        }
        synchronized (zzqf) {
            if (zzaeh == null) {
                zzaeh = context.getPackageName();
                try {
                    Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                    if (bundle != null) {
                        zzaei = Integer.valueOf(bundle.getInt("com.google.android.gms.version"));
                    } else {
                        zzaei = null;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    Log.wtf("GooglePlayServicesUtil", "This should never happen.", e);
                }
            } else if (!zzaeh.equals(context.getPackageName())) {
                throw new IllegalArgumentException("isGooglePlayServicesAvailable should only be called with Context from your application's package. A previous call used package '" + zzaeh + "' and this call used package '" + context.getPackageName() + "'.");
            }
            num = zzaei;
        }
        if (num == null) {
            throw new IllegalStateException("A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
        }
        if (num.intValue() != GOOGLE_PLAY_SERVICES_VERSION_CODE) {
            throw new IllegalStateException("The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected " + GOOGLE_PLAY_SERVICES_VERSION_CODE + " but found " + num + ".  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />");
        }
    }

    private static void zzal(Context context) {
        zza zzaVar = new zza(context);
        zzaVar.sendMessageDelayed(zzaVar.obtainMessage(1), 120000L);
    }

    public static String zzam(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo;
        String str = context.getApplicationInfo().name;
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo).toString() : packageName;
    }

    @Deprecated
    public static int zzan(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(GOOGLE_PLAY_SERVICES_PACKAGE, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 0;
        }
    }

    public static boolean zzao(Context context) {
        return zznx.zzrW() && context.getPackageManager().hasSystemFeature("cn.google");
    }

    public static boolean zzap(Context context) {
        Bundle applicationRestrictions;
        return zznx.zzrT() && (applicationRestrictions = ((UserManager) context.getSystemService(PropertyConfiguration.USER)).getApplicationRestrictions(context.getPackageName())) != null && "true".equals(applicationRestrictions.getString("restricted_profile"));
    }

    public static boolean zzb(Context context, int i, String str) {
        if (zznx.zzrU()) {
            try {
                ((AppOpsManager) context.getSystemService("appops")).checkPackage(i, str);
                return true;
            } catch (SecurityException e) {
                return false;
            }
        }
        String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (str == null || packagesForUid == null) {
            return false;
        }
        for (String str2 : packagesForUid) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean zzb(android.content.pm.PackageManager r9) {
        /*
            r0 = 1
            r1 = 0
            java.lang.Object r2 = com.google.android.gms.common.GooglePlayServicesUtil.zzqf
            monitor-enter(r2)
            int r3 = com.google.android.gms.common.GooglePlayServicesUtil.zzaeg     // Catch: java.lang.Throwable -> L39
            r4 = -1
            if (r3 != r4) goto L2a
            java.lang.String r3 = "com.google.android.gms"
            r4 = 64
            android.content.pm.PackageInfo r3 = r9.getPackageInfo(r3, r4)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            com.google.android.gms.common.zzd r4 = com.google.android.gms.common.zzd.zzox()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            r5 = 1
            com.google.android.gms.common.zzc$zza[] r5 = new com.google.android.gms.common.zzc.zza[r5]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            r6 = 0
            com.google.android.gms.common.zzc$zza[] r7 = com.google.android.gms.common.zzc.zzadW     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            r8 = 1
            r7 = r7[r8]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            r5[r6] = r7     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            com.google.android.gms.common.zzc$zza r3 = r4.zza(r3, r5)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            if (r3 == 0) goto L30
            r3 = 1
            com.google.android.gms.common.GooglePlayServicesUtil.zzaeg = r3     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
        L2a:
            int r3 = com.google.android.gms.common.GooglePlayServicesUtil.zzaeg     // Catch: java.lang.Throwable -> L39
            if (r3 == 0) goto L3c
        L2e:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L39
            return r0
        L30:
            r3 = 0
            com.google.android.gms.common.GooglePlayServicesUtil.zzaeg = r3     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L34 java.lang.Throwable -> L39
            goto L2a
        L34:
            r3 = move-exception
            r3 = 0
            com.google.android.gms.common.GooglePlayServicesUtil.zzaeg = r3     // Catch: java.lang.Throwable -> L39
            goto L2a
        L39:
            r0 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L39
            throw r0
        L3c:
            r0 = r1
            goto L2e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.GooglePlayServicesUtil.zzb(android.content.pm.PackageManager):boolean");
    }

    @Deprecated
    public static boolean zzb(PackageManager packageManager, String str) {
        return zzd.zzox().zzb(packageManager, str);
    }

    @Deprecated
    public static Intent zzbv(int i) {
        return GoogleApiAvailability.getInstance().zza(null, i, null);
    }

    private static boolean zzbw(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 18:
            case 42:
                return true;
            default:
                return false;
        }
    }

    public static boolean zzc(PackageManager packageManager) {
        return zzb(packageManager) || !zzow();
    }

    @Deprecated
    public static boolean zzd(Context context, int i) {
        if (i == 18) {
            return true;
        }
        if (i == 1) {
            return zzh(context, GOOGLE_PLAY_SERVICES_PACKAGE);
        }
        return false;
    }

    public static boolean zze(Context context, int i) {
        return zzb(context, i, GOOGLE_PLAY_SERVICES_PACKAGE) && zzb(context.getPackageManager(), GOOGLE_PLAY_SERVICES_PACKAGE);
    }

    @Deprecated
    public static boolean zzf(Context context, int i) {
        if (i == 9) {
            return zzh(context, GOOGLE_PLAY_STORE_PACKAGE);
        }
        return false;
    }

    static boolean zzh(Context context, String str) {
        if (zznx.zzrW()) {
            Iterator<PackageInstaller.SessionInfo> it = context.getPackageManager().getPackageInstaller().getAllSessions().iterator();
            while (it.hasNext()) {
                if (str.equals(it.next().getAppPackageName())) {
                    return true;
                }
            }
        }
        if (zzap(context)) {
            return false;
        }
        try {
            return context.getPackageManager().getApplicationInfo(str, 8192).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static int zzov() {
        return 8298000;
    }

    public static boolean zzow() {
        return zzaee ? zzaef : PropertyConfiguration.USER.equals(Build.TYPE);
    }
}
