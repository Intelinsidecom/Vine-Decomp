package com.google.android.gms.ads.internal.overlay;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zza {
    public boolean zza(Context context, AdLauncherIntentInfoParcel adLauncherIntentInfoParcel, zzn zznVar) throws NumberFormatException {
        int i;
        if (adLauncherIntentInfoParcel == null) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("No intent data for launcher overlay.");
            return false;
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(adLauncherIntentInfoParcel.url)) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Open GMSG did not contain a URL.");
            return false;
        }
        if (TextUtils.isEmpty(adLauncherIntentInfoParcel.mimeType)) {
            intent.setData(Uri.parse(adLauncherIntentInfoParcel.url));
        } else {
            intent.setDataAndType(Uri.parse(adLauncherIntentInfoParcel.url), adLauncherIntentInfoParcel.mimeType);
        }
        intent.setAction("android.intent.action.VIEW");
        if (!TextUtils.isEmpty(adLauncherIntentInfoParcel.packageName)) {
            intent.setPackage(adLauncherIntentInfoParcel.packageName);
        }
        if (!TextUtils.isEmpty(adLauncherIntentInfoParcel.zzCK)) {
            String[] strArrSplit = adLauncherIntentInfoParcel.zzCK.split("/", 2);
            if (strArrSplit.length < 2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse component name from open GMSG: " + adLauncherIntentInfoParcel.zzCK);
                return false;
            }
            intent.setClassName(strArrSplit[0], strArrSplit[1]);
        }
        String str = adLauncherIntentInfoParcel.zzCL;
        if (!TextUtils.isEmpty(str)) {
            try {
                i = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not parse intent flags.");
                i = 0;
            }
            intent.addFlags(i);
        }
        try {
            com.google.android.gms.ads.internal.util.client.zzb.v("Launching an intent: " + intent.toURI());
            com.google.android.gms.ads.internal.zzp.zzbx().zzb(context, intent);
            if (zznVar != null) {
                zznVar.zzaQ();
            }
            return true;
        } catch (ActivityNotFoundException e2) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH(e2.getMessage());
            return false;
        }
    }
}
