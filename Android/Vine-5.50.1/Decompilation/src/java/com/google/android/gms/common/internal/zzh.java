package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

/* loaded from: classes2.dex */
public class zzh implements DialogInterface.OnClickListener {
    private final Activity mActivity;
    private final Intent mIntent;
    private final int zzaeU;
    private final Fragment zzajv;

    public zzh(Activity activity, Intent intent, int i) {
        this.mActivity = activity;
        this.zzajv = null;
        this.mIntent = intent;
        this.zzaeU = i;
    }

    public zzh(Fragment fragment, Intent intent, int i) {
        this.mActivity = null;
        this.zzajv = fragment;
        this.mIntent = intent;
        this.zzaeU = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        try {
            if (this.mIntent != null && this.zzajv != null) {
                this.zzajv.startActivityForResult(this.mIntent, this.zzaeU);
            } else if (this.mIntent != null) {
                this.mActivity.startActivityForResult(this.mIntent, this.zzaeU);
            }
            dialog.dismiss();
        } catch (ActivityNotFoundException e) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}
