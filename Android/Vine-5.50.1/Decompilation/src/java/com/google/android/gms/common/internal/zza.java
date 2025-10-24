package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzp;

/* loaded from: classes2.dex */
public class zza extends zzp.zza {
    private Context mContext;
    private Account zzSo;
    int zzaiR;

    public static Account zzb(zzp zzpVar) {
        Account account = null;
        if (zzpVar != null) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                account = zzpVar.getAccount();
            } catch (RemoteException e) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return account;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof zza) {
            return this.zzSo.equals(((zza) o).zzSo);
        }
        return false;
    }

    @Override // com.google.android.gms.common.internal.zzp
    public Account getAccount() {
        int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzaiR) {
            return this.zzSo;
        }
        if (!GooglePlayServicesUtil.zze(this.mContext, callingUid)) {
            throw new SecurityException("Caller is not GooglePlayServices");
        }
        this.zzaiR = callingUid;
        return this.zzSo;
    }
}
