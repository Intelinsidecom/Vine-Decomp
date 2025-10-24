package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.android.gms.ads.mediation.customevent.CustomEvent;
import com.google.android.gms.internal.zzew;
import java.util.Map;

@zzha
/* loaded from: classes.dex */
public final class zzev extends zzew.zza {
    private Map<Class<? extends Object>, Object> zzBG;

    private <NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> zzex zzah(String str) throws RemoteException {
        try {
            Class<?> cls = Class.forName(str, false, zzev.class.getClassLoader());
            if (MediationAdapter.class.isAssignableFrom(cls)) {
                MediationAdapter mediationAdapter = (MediationAdapter) cls.newInstance();
                return new zzfi(mediationAdapter, (NetworkExtras) this.zzBG.get(mediationAdapter.getAdditionalParametersType()));
            }
            if (com.google.android.gms.ads.mediation.MediationAdapter.class.isAssignableFrom(cls)) {
                return new zzfd((com.google.android.gms.ads.mediation.MediationAdapter) cls.newInstance());
            }
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not instantiate mediation adapter: " + str + " (not a valid adapter).");
            throw new RemoteException();
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not instantiate mediation adapter: " + str + ". " + th.getMessage());
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.zzew
    public zzex zzaf(String str) throws RemoteException {
        return zzah(str);
    }

    @Override // com.google.android.gms.internal.zzew
    public boolean zzag(String str) throws RemoteException {
        try {
            return CustomEvent.class.isAssignableFrom(Class.forName(str, false, zzev.class.getClassLoader()));
        } catch (Throwable th) {
            com.google.android.gms.ads.internal.util.client.zzb.zzaH("Could not load custom event implementation class: " + str + ", assuming old implementation.");
            return false;
        }
    }

    public void zze(Map<Class<? extends Object>, Object> map) {
        this.zzBG = map;
    }
}
