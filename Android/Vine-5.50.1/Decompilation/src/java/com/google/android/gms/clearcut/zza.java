package com.google.android.gms.clearcut;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzlt;
import com.google.android.gms.internal.zzlu;
import com.google.android.gms.internal.zznl;
import com.google.android.gms.internal.zzno;
import com.google.android.gms.internal.zztp;
import com.google.android.gms.playlog.internal.PlayLoggerContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class zza {
    private final Context mContext;
    private final String zzSp;
    private final int zzadi;
    private String zzadj;
    private int zzadk;
    private String zzadl;
    private String zzadm;
    private final boolean zzadn;
    private int zzado;
    private final com.google.android.gms.clearcut.zzb zzadp;
    private zzc zzadq;
    private final zznl zzqD;
    public static final Api.zzc<zzlu> zzTo = new Api.zzc<>();
    public static final Api.zza<zzlu, Api.ApiOptions.NoOptions> zzTp = new Api.zza<zzlu, Api.ApiOptions.NoOptions>() { // from class: com.google.android.gms.clearcut.zza.1
        @Override // com.google.android.gms.common.api.Api.zza
        /* renamed from: zze, reason: merged with bridge method [inline-methods] */
        public zzlu zza(Context context, Looper looper, zzf zzfVar, Api.ApiOptions.NoOptions noOptions, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
            return new zzlu(context, looper, zzfVar, connectionCallbacks, onConnectionFailedListener);
        }
    };
    public static final Api<Api.ApiOptions.NoOptions> API = new Api<>("ClearcutLogger.API", zzTp, zzTo);
    public static final com.google.android.gms.clearcut.zzb zzadh = new zzlt();

    /* renamed from: com.google.android.gms.clearcut.zza$zza, reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public class C0032zza {
        private String zzadj;
        private int zzadk;
        private String zzadl;
        private String zzadm;
        private int zzado;
        private final zzb zzadr;
        private zzb zzads;
        private ArrayList<Integer> zzadt;
        private final zztp.zzd zzadu;
        private boolean zzadv;

        private C0032zza(zza zzaVar, byte[] bArr) {
            this(bArr, (zzb) null);
        }

        private C0032zza(byte[] bArr, zzb zzbVar) {
            this.zzadk = zza.this.zzadk;
            this.zzadj = zza.this.zzadj;
            this.zzadl = zza.this.zzadl;
            this.zzadm = zza.this.zzadm;
            this.zzado = zza.this.zzado;
            this.zzadt = null;
            this.zzadu = new zztp.zzd();
            this.zzadv = false;
            this.zzadl = zza.this.zzadl;
            this.zzadm = zza.this.zzadm;
            this.zzadu.zzbqw = zza.this.zzqD.currentTimeMillis();
            this.zzadu.zzbqx = zza.this.zzqD.elapsedRealtime();
            this.zzadu.zzbqI = zza.this.zzadq.zzC(this.zzadu.zzbqw);
            if (bArr != null) {
                this.zzadu.zzbqD = bArr;
            }
            this.zzadr = zzbVar;
        }

        public C0032zza zzbq(int i) {
            this.zzadu.zzbqz = i;
            return this;
        }

        public C0032zza zzbr(int i) {
            this.zzadu.zznN = i;
            return this;
        }

        public PendingResult<Status> zzc(GoogleApiClient googleApiClient) {
            if (this.zzadv) {
                throw new IllegalStateException("do not reuse LogEventBuilder");
            }
            this.zzadv = true;
            return zza.this.zzadp.zza(googleApiClient, zzom());
        }

        public LogEventParcelable zzom() {
            return new LogEventParcelable(new PlayLoggerContext(zza.this.zzSp, zza.this.zzadi, this.zzadk, this.zzadj, this.zzadl, this.zzadm, zza.this.zzadn, this.zzado), this.zzadu, this.zzadr, this.zzads, zza.zzb(this.zzadt));
        }
    }

    /* loaded from: classes2.dex */
    public interface zzb {
        byte[] zzon();
    }

    /* loaded from: classes2.dex */
    public static class zzc {
        public long zzC(long j) {
            return TimeZone.getDefault().getOffset(j) / 1000;
        }
    }

    zza(Context context, int i, String str, String str2, String str3, boolean z, com.google.android.gms.clearcut.zzb zzbVar, zznl zznlVar) {
        this.zzadk = -1;
        this.zzado = 0;
        this.mContext = context.getApplicationContext();
        this.zzSp = context.getPackageName();
        this.zzadi = zzah(context);
        this.zzadk = i;
        this.zzadj = str;
        this.zzadl = str2;
        this.zzadm = str3;
        this.zzadn = z;
        this.zzadp = zzbVar;
        this.zzqD = zznlVar;
        this.zzadq = new zzc();
        this.zzado = 0;
        if (this.zzadn) {
            zzx.zzb(this.zzadl == null, "can't be anonymous with an upload account");
        }
    }

    @Deprecated
    public zza(Context context, String str, String str2, String str3) {
        this(context, -1, str, str2, str3, false, zzadh, zzno.zzrM());
    }

    private int zzah(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf("ClearcutLogger", "This can't happen.");
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] zzb(ArrayList<Integer> arrayList) {
        if (arrayList == null) {
            return null;
        }
        int[] iArr = new int[arrayList.size()];
        int i = 0;
        Iterator<Integer> it = arrayList.iterator();
        while (true) {
            int i2 = i;
            if (!it.hasNext()) {
                return iArr;
            }
            i = i2 + 1;
            iArr[i2] = it.next().intValue();
        }
    }

    public boolean zza(GoogleApiClient googleApiClient, long j, TimeUnit timeUnit) {
        return this.zzadp.zza(googleApiClient, j, timeUnit);
    }

    public C0032zza zzi(byte[] bArr) {
        return new C0032zza(bArr);
    }
}
