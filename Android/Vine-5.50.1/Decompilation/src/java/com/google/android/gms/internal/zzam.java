package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.internal.zzal;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class zzam extends zzal {
    private static AdvertisingIdClient zznV = null;
    private static CountDownLatch zznW = new CountDownLatch(1);
    private static volatile boolean zznX;
    private boolean zznY;

    class zza {
        private String zznZ;
        private boolean zzoa;

        public zza(String str, boolean z) {
            this.zznZ = str;
            this.zzoa = z;
        }

        public String getId() {
            return this.zznZ;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.zzoa;
        }
    }

    private static final class zzb implements Runnable {
        private Context zzoc;

        public zzb(Context context) {
            this.zzoc = context.getApplicationContext();
            if (this.zzoc == null) {
                this.zzoc = context;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (zzam.class) {
                try {
                    try {
                        try {
                            if (zzam.zznV == null) {
                                AdvertisingIdClient advertisingIdClient = new AdvertisingIdClient(this.zzoc);
                                advertisingIdClient.start();
                                AdvertisingIdClient unused = zzam.zznV = advertisingIdClient;
                            }
                        } catch (GooglePlayServicesRepairableException e) {
                            zzam.zznW.countDown();
                        } catch (IOException e2) {
                            zzam.zznW.countDown();
                        }
                    } catch (GooglePlayServicesNotAvailableException e3) {
                        boolean unused2 = zzam.zznX = true;
                        zzam.zznW.countDown();
                    }
                } finally {
                    zzam.zznW.countDown();
                }
            }
        }
    }

    protected zzam(Context context, zzap zzapVar, zzaq zzaqVar, boolean z) {
        super(context, zzapVar, zzaqVar);
        this.zznY = z;
    }

    public static zzam zza(String str, Context context, boolean z) {
        zzah zzahVar = new zzah();
        zza(str, context, zzahVar);
        if (z) {
            synchronized (zzam.class) {
                if (zznV == null) {
                    new Thread(new zzb(context)).start();
                }
            }
        }
        return new zzam(context, zzahVar, new zzas(239), z);
    }

    zza zzZ() throws IOException {
        zza zzaVar;
        try {
            if (!zznW.await(2L, TimeUnit.SECONDS)) {
                return new zza(null, false);
            }
            synchronized (zzam.class) {
                if (zznV == null) {
                    zzaVar = new zza(null, false);
                } else {
                    AdvertisingIdClient.Info info = zznV.getInfo();
                    zzaVar = new zza(zzk(info.getId()), info.isLimitAdTrackingEnabled());
                }
            }
            return zzaVar;
        } catch (InterruptedException e) {
            return new zza(null, false);
        }
    }

    @Override // com.google.android.gms.internal.zzal, com.google.android.gms.internal.zzak
    protected void zzc(Context context) {
        super.zzc(context);
        try {
            if (zznX || !this.zznY) {
                zza(24, zzf(context));
                zza(24, zznN);
            } else {
                zza zzaVarZzZ = zzZ();
                String id = zzaVarZzZ.getId();
                if (id != null) {
                    zza(28, zzaVarZzZ.isLimitAdTrackingEnabled() ? 1L : 0L);
                    zza(26, 5L);
                    zza(24, id);
                    zza(28, zznN);
                }
            }
        } catch (zzal.zza e) {
        } catch (IOException e2) {
        }
    }
}
