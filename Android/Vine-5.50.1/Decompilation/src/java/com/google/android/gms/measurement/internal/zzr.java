package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.util.Pair;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
class zzr extends zzw {
    static final Pair<String, Long> zzaTD = new Pair<>("", 0L);
    private SharedPreferences zzRN;
    public final zzb zzaTE;
    public final zza zzaTF;
    public final zza zzaTG;
    public final zza zzaTH;
    public final zza zzaTI;
    private String zzaTJ;
    private boolean zzaTK;
    private long zzaTL;
    private final SecureRandom zzaTM;

    public final class zza {
        private long zzaBq;
        private final long zzaTN;
        private boolean zzaTO;
        private final String zzuX;

        public zza(String str, long j) {
            com.google.android.gms.common.internal.zzx.zzcG(str);
            this.zzuX = str;
            this.zzaTN = j;
        }

        private void zzBy() {
            if (this.zzaTO) {
                return;
            }
            this.zzaTO = true;
            this.zzaBq = zzr.this.zzRN.getLong(this.zzuX, this.zzaTN);
        }

        public long get() {
            zzBy();
            return this.zzaBq;
        }

        public void set(long value) {
            SharedPreferences.Editor editorEdit = zzr.this.zzRN.edit();
            editorEdit.putLong(this.zzuX, value);
            editorEdit.apply();
            this.zzaBq = value;
        }
    }

    public final class zzb {
        private final long zzRR;
        final String zzaTQ;
        private final String zzaTR;
        private final String zzaTS;

        private zzb(String str, long j) {
            com.google.android.gms.common.internal.zzx.zzcG(str);
            com.google.android.gms.common.internal.zzx.zzab(j > 0);
            this.zzaTQ = str + ":start";
            this.zzaTR = str + ":count";
            this.zzaTS = str + ":value";
            this.zzRR = j;
        }

        private void zzlu() {
            zzr.this.zziS();
            long jCurrentTimeMillis = zzr.this.zziT().currentTimeMillis();
            SharedPreferences.Editor editorEdit = zzr.this.zzRN.edit();
            editorEdit.remove(this.zzaTR);
            editorEdit.remove(this.zzaTS);
            editorEdit.putLong(this.zzaTQ, jCurrentTimeMillis);
            editorEdit.apply();
        }

        private long zzlv() {
            zzr.this.zziS();
            long jZzlx = zzlx();
            if (jZzlx != 0) {
                return Math.abs(jZzlx - zzr.this.zziT().currentTimeMillis());
            }
            zzlu();
            return 0L;
        }

        private long zzlx() {
            return zzr.this.zzBw().getLong(this.zzaTQ, 0L);
        }

        public void zzbn(String str) {
            zzg(str, 1L);
        }

        public void zzg(String str, long j) {
            zzr.this.zziS();
            if (zzlx() == 0) {
                zzlu();
            }
            if (str == null) {
                str = "";
            }
            long j2 = zzr.this.zzRN.getLong(this.zzaTR, 0L);
            if (j2 <= 0) {
                SharedPreferences.Editor editorEdit = zzr.this.zzRN.edit();
                editorEdit.putString(this.zzaTS, str);
                editorEdit.putLong(this.zzaTR, j);
                editorEdit.apply();
                return;
            }
            boolean z = (zzr.this.zzaTM.nextLong() & Long.MAX_VALUE) < (Long.MAX_VALUE / (j2 + j)) * j;
            SharedPreferences.Editor editorEdit2 = zzr.this.zzRN.edit();
            if (z) {
                editorEdit2.putString(this.zzaTS, str);
            }
            editorEdit2.putLong(this.zzaTR, j2 + j);
            editorEdit2.apply();
        }

        public Pair<String, Long> zzlw() {
            zzr.this.zziS();
            long jZzlv = zzlv();
            if (jZzlv < this.zzRR) {
                return null;
            }
            if (jZzlv > this.zzRR * 2) {
                zzlu();
                return null;
            }
            String string = zzr.this.zzBw().getString(this.zzaTS, null);
            long j = zzr.this.zzBw().getLong(this.zzaTR, 0L);
            zzlu();
            return (string == null || j <= 0) ? zzr.zzaTD : new Pair<>(string, Long.valueOf(j));
        }
    }

    zzr(zzt zztVar) {
        super(zztVar);
        this.zzaTE = new zzb("health_monitor", zzAX().zzkG());
        this.zzaTF = new zza("last_upload", 0L);
        this.zzaTG = new zza("last_upload_attempt", 0L);
        this.zzaTH = new zza("backoff", 0L);
        this.zzaTI = new zza("last_delete_stale", 0L);
        this.zzaTM = new SecureRandom();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SharedPreferences zzBw() {
        zziS();
        zzje();
        return this.zzRN;
    }

    static MessageDigest zzbs(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 2) {
                return null;
            }
            try {
                messageDigest = MessageDigest.getInstance(str);
            } catch (NoSuchAlgorithmException e) {
            }
            if (messageDigest != null) {
                return messageDigest;
            }
            i = i2 + 1;
        }
    }

    void setMeasurementEnabled(boolean enabled) {
        zziS();
        zzzz().zzBr().zzj("Setting measurementEnabled", Boolean.valueOf(enabled));
        SharedPreferences.Editor editorEdit = zzBw().edit();
        editorEdit.putBoolean("measurement_enabled", enabled);
        editorEdit.apply();
    }

    Pair<String, Boolean> zzBt() {
        zziS();
        long jElapsedRealtime = zziT().elapsedRealtime();
        if (this.zzaTJ != null && jElapsedRealtime < this.zzaTL) {
            return new Pair<>(this.zzaTJ, Boolean.valueOf(this.zzaTK));
        }
        this.zzaTL = jElapsedRealtime + zzAX().zzAD();
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
            this.zzaTJ = advertisingIdInfo.getId();
            this.zzaTK = advertisingIdInfo.isLimitAdTrackingEnabled();
        } catch (Throwable th) {
            zzzz().zzBq().zzj("Unable to get advertising id", th);
            this.zzaTJ = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return new Pair<>(this.zzaTJ, Boolean.valueOf(this.zzaTK));
    }

    String zzBu() throws NoSuchAlgorithmException {
        String str = (String) zzBt().first;
        MessageDigest messageDigestZzbs = zzbs("MD5");
        if (messageDigestZzbs == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, messageDigestZzbs.digest(str.getBytes())));
    }

    String zzBv() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    Boolean zzBx() {
        zziS();
        if (zzBw().contains("use_service")) {
            return Boolean.valueOf(zzBw().getBoolean("use_service", false));
        }
        return null;
    }

    void zzap(boolean z) {
        zziS();
        zzzz().zzBr().zzj("Setting useService", Boolean.valueOf(z));
        SharedPreferences.Editor editorEdit = zzBw().edit();
        editorEdit.putBoolean("use_service", z);
        editorEdit.apply();
    }

    @Override // com.google.android.gms.measurement.internal.zzw
    protected void zzir() {
        this.zzRN = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
    }

    boolean zzzC() {
        zziS();
        return zzBw().getBoolean("measurement_enabled", true);
    }
}
