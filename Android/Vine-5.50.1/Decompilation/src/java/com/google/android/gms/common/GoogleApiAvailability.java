package com.google.android.gms.common;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.ProgressBar;
import com.google.android.gms.common.internal.zzn;

/* loaded from: classes.dex */
public class GoogleApiAvailability {
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final GoogleApiAvailability zzadU = new GoogleApiAvailability();

    GoogleApiAvailability() {
    }

    public static GoogleApiAvailability getInstance() {
        return zzadU;
    }

    private String zzi(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("gcore_");
        sb.append(GOOGLE_PLAY_SERVICES_VERSION_CODE);
        sb.append("-");
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
        }
        sb.append("-");
        if (context != null) {
            sb.append(context.getPackageName());
        }
        sb.append("-");
        if (context != null) {
            try {
                sb.append(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            } catch (PackageManager.NameNotFoundException e) {
            }
        }
        return sb.toString();
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, int errorCode, int requestCode) {
        return zza(context, errorCode, requestCode, null);
    }

    public int isGooglePlayServicesAvailable(Context context) {
        int iIsGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.zzd(context, iIsGooglePlayServicesAvailable)) {
            return 18;
        }
        return iIsGooglePlayServicesAvailable;
    }

    public final boolean isUserResolvableError(int errorCode) {
        return GooglePlayServicesUtil.isUserRecoverableError(errorCode);
    }

    public Dialog zza(Activity activity, DialogInterface.OnCancelListener onCancelListener) throws PackageManager.NameNotFoundException {
        ProgressBar progressBar = new ProgressBar(activity, null, R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(progressBar);
        builder.setMessage(activity.getResources().getString(com.google.android.gms.R.string.common_google_play_services_updating_text, GooglePlayServicesUtil.zzam(activity)));
        builder.setTitle(com.google.android.gms.R.string.common_google_play_services_updating_title);
        builder.setPositiveButton("", (DialogInterface.OnClickListener) null);
        AlertDialog alertDialogCreate = builder.create();
        GooglePlayServicesUtil.zza(activity, onCancelListener, "GooglePlayServicesUpdatingDialog", alertDialogCreate);
        return alertDialogCreate;
    }

    public PendingIntent zza(Context context, int i, int i2, String str) {
        Intent intentZza = zza(context, i, str);
        if (intentZza == null) {
            return null;
        }
        return PendingIntent.getActivity(context, i2, intentZza, 268435456);
    }

    public Intent zza(Context context, int i, String str) {
        switch (i) {
            case 1:
            case 2:
                return zzn.zzy(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, zzi(context, str));
            case 3:
                return zzn.zzcD(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
            case 42:
                return zzn.zzqE();
            default:
                return null;
        }
    }

    public void zzaj(Context context) {
        GooglePlayServicesUtil.zzaj(context);
    }

    @Deprecated
    public Intent zzbu(int i) {
        return zza(null, i, null);
    }

    public boolean zzd(Context context, int i) {
        return GooglePlayServicesUtil.zzd(context, i);
    }

    public boolean zzh(Context context, String str) {
        return GooglePlayServicesUtil.zzh(context, str);
    }
}
