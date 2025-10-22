package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.zzmt;

/* loaded from: classes.dex */
public final class zzk {
    public static zza<Boolean> zzaSO = zza.zzi("measurement.service_enabled", true);
    public static zza<Boolean> zzaSP = zza.zzi("measurement.service_client_enabled", true);
    public static zza<String> zzaSQ = zza.zzj("measurement.log_tag", "GMPM", "GMPM-SVC");
    public static zza<Long> zzaSR = zza.zzf("measurement.ad_id_cache_time", 10000);
    public static zza<Long> zzaSS = zza.zzf("measurement.monitoring.sample_period_millis", 86400000);
    public static zza<Integer> zzaST = zza.zzA("measurement.upload.max_bundles", 100);
    public static zza<Integer> zzaSU = zza.zzA("measurement.upload.max_batch_size", 65536);
    public static zza<String> zzaSV = zza.zzN("measurement.upload.url", "https://app-measurement.com/a");
    public static zza<Long> zzaSW = zza.zzf("measurement.upload.backoff_period", 43200000);
    public static zza<Long> zzaSX = zza.zzf("measurement.upload.window_interval", 3600000);
    public static zza<Long> zzaSY = zza.zzf("measurement.upload.interval", 3600000);
    public static zza<Long> zzaSZ = zza.zzf("measurement.upload.stale_data_deletion_interval", 86400000);
    public static zza<Long> zzaTa = zza.zzf("measurement.upload.initial_upload_delay_time", 15000);
    public static zza<Long> zzaTb = zza.zzf("measurement.upload.retry_time", 1800000);
    public static zza<Integer> zzaTc = zza.zzA("measurement.upload.retry_count", 6);
    public static zza<Long> zzaTd = zza.zzf("measurement.upload.max_queue_time", 2419200000L);
    public static zza<Long> zzaTe = zza.zzf("measurement.service_client.idle_disconnect_millis", 5000);

    public static final class zza<V> {
        private final V zzRg;
        private final zzmt<V> zzRh;
        private V zzRi;

        private zza(zzmt<V> zzmtVar, V v) {
            com.google.android.gms.common.internal.zzx.zzy(zzmtVar);
            this.zzRh = zzmtVar;
            this.zzRg = v;
        }

        static zza<Integer> zzA(String str, int i) {
            return zzo(str, i, i);
        }

        static zza<String> zzN(String str, String str2) {
            return zzj(str, str2, str2);
        }

        static zza<Long> zzb(String str, long j, long j2) {
            return new zza<>(zzmt.zza(str, Long.valueOf(j2)), Long.valueOf(j));
        }

        static zza<Boolean> zzb(String str, boolean z, boolean z2) {
            return new zza<>(zzmt.zzg(str, z2), Boolean.valueOf(z));
        }

        static zza<Long> zzf(String str, long j) {
            return zzb(str, j, j);
        }

        static zza<Boolean> zzi(String str, boolean z) {
            return zzb(str, z, z);
        }

        static zza<String> zzj(String str, String str2, String str3) {
            return new zza<>(zzmt.zzw(str, str3), str2);
        }

        static zza<Integer> zzo(String str, int i, int i2) {
            return new zza<>(zzmt.zza(str, Integer.valueOf(i2)), Integer.valueOf(i));
        }

        public V get() {
            return this.zzRi != null ? this.zzRi : (com.google.android.gms.common.internal.zzd.zzaiU && zzmt.isInitialized()) ? this.zzRh.zzpF() : this.zzRg;
        }
    }
}
