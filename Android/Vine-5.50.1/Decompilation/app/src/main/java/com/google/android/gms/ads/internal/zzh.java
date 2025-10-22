package com.google.android.gms.ads.internal;

import android.content.Context;
import android.view.MotionEvent;
import com.google.android.gms.internal.zzaj;
import com.google.android.gms.internal.zzam;
import com.google.android.gms.internal.zzbz;
import com.google.android.gms.internal.zzha;
import com.google.android.gms.internal.zzio;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@zzha
/* loaded from: classes.dex */
class zzh implements zzaj, Runnable {
    private zzq zzoZ;
    private final List<Object[]> zzpw = new Vector();
    private final AtomicReference<zzaj> zzpx = new AtomicReference<>();
    CountDownLatch zzpy = new CountDownLatch(1);

    public zzh(zzq zzqVar) {
        this.zzoZ = zzqVar;
        if (com.google.android.gms.ads.internal.client.zzl.zzcN().zzhr()) {
            zzio.zza(this);
        } else {
            run();
        }
    }

    private void zzbj() {
        if (this.zzpw.isEmpty()) {
            return;
        }
        for (Object[] objArr : this.zzpw) {
            if (objArr.length == 1) {
                this.zzpx.get().zza((MotionEvent) objArr[0]);
            } else if (objArr.length == 3) {
                this.zzpx.get().zza(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), ((Integer) objArr[2]).intValue());
            }
        }
        this.zzpw.clear();
    }

    private Context zzq(Context context) {
        Context applicationContext;
        return (zzbz.zzvr.get().booleanValue() && (applicationContext = context.getApplicationContext()) != null) ? applicationContext : context;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            zza(zzb(this.zzoZ.zzqR.afmaVersion, zzq(this.zzoZ.context), !zzbz.zzvD.get().booleanValue() || this.zzoZ.zzqR.zzLH));
        } finally {
            this.zzpy.countDown();
            this.zzoZ = null;
        }
    }

    @Override // com.google.android.gms.internal.zzaj
    public void zza(int i, int i2, int i3) {
        zzaj zzajVar = this.zzpx.get();
        if (zzajVar == null) {
            this.zzpw.add(new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)});
        } else {
            zzbj();
            zzajVar.zza(i, i2, i3);
        }
    }

    @Override // com.google.android.gms.internal.zzaj
    public void zza(MotionEvent motionEvent) {
        zzaj zzajVar = this.zzpx.get();
        if (zzajVar == null) {
            this.zzpw.add(new Object[]{motionEvent});
        } else {
            zzbj();
            zzajVar.zza(motionEvent);
        }
    }

    protected void zza(zzaj zzajVar) {
        this.zzpx.set(zzajVar);
    }

    protected zzaj zzb(String str, Context context, boolean z) {
        return zzam.zza(str, context, z);
    }

    @Override // com.google.android.gms.internal.zzaj
    public String zzb(Context context) {
        zzaj zzajVar;
        if (!zzbi() || (zzajVar = this.zzpx.get()) == null) {
            return "";
        }
        zzbj();
        return zzajVar.zzb(zzq(context));
    }

    @Override // com.google.android.gms.internal.zzaj
    public String zzb(Context context, String str) {
        zzaj zzajVar;
        if (!zzbi() || (zzajVar = this.zzpx.get()) == null) {
            return "";
        }
        zzbj();
        return zzajVar.zzb(zzq(context), str);
    }

    protected boolean zzbi() throws InterruptedException {
        try {
            this.zzpy.await();
            return true;
        } catch (InterruptedException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzd("Interrupted during GADSignals creation.", e);
            return false;
        }
    }
}
