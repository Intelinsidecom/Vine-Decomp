package com.google.android.gms.ads.internal.purchase;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import com.google.android.gms.internal.zzha;

@zzha
/* loaded from: classes.dex */
public class zzb {
    private final Context mContext;
    Object zzEy;
    private final boolean zzEz;

    public zzb(Context context) {
        this(context, true);
    }

    public zzb(Context context, boolean z) {
        this.mContext = context;
        this.zzEz = z;
    }

    public void destroy() {
        this.zzEy = null;
    }

    public void zzN(IBinder iBinder) {
        try {
            this.zzEy = this.mContext.getClassLoader().loadClass("com.android.vending.billing.IInAppBillingService$Stub").getDeclaredMethod("asInterface", IBinder.class).invoke(null, iBinder);
        } catch (Exception e) {
            if (this.zzEz) {
                com.google.android.gms.ads.internal.util.client.zzb.zzaH("IInAppBillingService is not available, please add com.android.vending.billing.IInAppBillingService to project.");
            }
        }
    }

    public int zzb(int i, String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = this.mContext.getClassLoader().loadClass("com.android.vending.billing.IInAppBillingService");
            return ((Integer) clsLoadClass.getDeclaredMethod("isBillingSupported", Integer.TYPE, String.class, String.class).invoke(clsLoadClass.cast(this.zzEy), Integer.valueOf(i), str, str2)).intValue();
        } catch (Exception e) {
            if (this.zzEz) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("IInAppBillingService is not available, please add com.android.vending.billing.IInAppBillingService to project.", e);
            }
            return 5;
        }
    }

    public Bundle zzb(String str, String str2, String str3) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = this.mContext.getClassLoader().loadClass("com.android.vending.billing.IInAppBillingService");
            return (Bundle) clsLoadClass.getDeclaredMethod("getBuyIntent", Integer.TYPE, String.class, String.class, String.class, String.class).invoke(clsLoadClass.cast(this.zzEy), 3, str, str2, "inapp", str3);
        } catch (Exception e) {
            if (this.zzEz) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("IInAppBillingService is not available, please add com.android.vending.billing.IInAppBillingService to project.", e);
            }
            return null;
        }
    }

    public int zzh(String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = this.mContext.getClassLoader().loadClass("com.android.vending.billing.IInAppBillingService");
            return ((Integer) clsLoadClass.getDeclaredMethod("consumePurchase", Integer.TYPE, String.class, String.class).invoke(clsLoadClass.cast(this.zzEy), 3, str, str2)).intValue();
        } catch (Exception e) {
            if (this.zzEz) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("IInAppBillingService is not available, please add com.android.vending.billing.IInAppBillingService to project.", e);
            }
            return 5;
        }
    }

    public Bundle zzi(String str, String str2) throws ClassNotFoundException {
        try {
            Class<?> clsLoadClass = this.mContext.getClassLoader().loadClass("com.android.vending.billing.IInAppBillingService");
            return (Bundle) clsLoadClass.getDeclaredMethod("getPurchases", Integer.TYPE, String.class, String.class, String.class).invoke(clsLoadClass.cast(this.zzEy), 3, str, "inapp", str2);
        } catch (Exception e) {
            if (this.zzEz) {
                com.google.android.gms.ads.internal.util.client.zzb.zzd("IInAppBillingService is not available, please add com.android.vending.billing.IInAppBillingService to project.", e);
            }
            return null;
        }
    }
}
