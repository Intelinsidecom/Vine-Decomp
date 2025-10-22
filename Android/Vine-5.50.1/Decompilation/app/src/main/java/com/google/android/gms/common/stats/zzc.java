package com.google.android.gms.common.stats;

import com.google.android.gms.internal.zzmt;

/* loaded from: classes2.dex */
public final class zzc {
    public static zzmt<Integer> zzalG = zzmt.zza("gms:common:stats:max_num_of_events", (Integer) 100);
    public static zzmt<Integer> zzalH = zzmt.zza("gms:common:stats:max_chunk_size", (Integer) 100);

    public static final class zza {
        public static zzmt<Integer> zzalI = zzmt.zza("gms:common:stats:connections:level", Integer.valueOf(zzd.LOG_LEVEL_OFF));
        public static zzmt<String> zzalJ = zzmt.zzw("gms:common:stats:connections:ignored_calling_processes", "");
        public static zzmt<String> zzalK = zzmt.zzw("gms:common:stats:connections:ignored_calling_services", "");
        public static zzmt<String> zzalL = zzmt.zzw("gms:common:stats:connections:ignored_target_processes", "");
        public static zzmt<String> zzalM = zzmt.zzw("gms:common:stats:connections:ignored_target_services", "com.google.android.gms.auth.GetToken");
        public static zzmt<Long> zzalN = zzmt.zza("gms:common:stats:connections:time_out_duration", (Long) 600000L);
    }
}
