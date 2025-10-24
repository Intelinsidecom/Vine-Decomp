package com.google.android.gms.internal;

import java.util.PriorityQueue;

@zzha
/* loaded from: classes.dex */
public class zzbq {

    public static class zza {
        final long value;
        final String zztm;

        zza(long j, String str) {
            this.value = j;
            this.zztm = str;
        }

        public boolean equals(Object other) {
            return other != null && (other instanceof zza) && ((zza) other).value == this.value;
        }

        public int hashCode() {
            return (int) this.value;
        }
    }

    static long zza(int i, int i2, long j, long j2, long j3) {
        return ((((((j + 1073807359) - ((((i + 2147483647L) % 1073807359) * j2) % 1073807359)) % 1073807359) * j3) % 1073807359) + ((i2 + 2147483647L) % 1073807359)) % 1073807359;
    }

    static long zza(long j, int i) {
        if (i == 0) {
            return 1L;
        }
        return i != 1 ? i % 2 == 0 ? zza((j * j) % 1073807359, i / 2) % 1073807359 : ((zza((j * j) % 1073807359, i / 2) % 1073807359) * j) % 1073807359 : j;
    }

    static String zza(String[] strArr, int i, int i2) {
        if (strArr.length < i + i2) {
            com.google.android.gms.ads.internal.util.client.zzb.e("Unable to construct shingle");
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = i; i3 < (i + i2) - 1; i3++) {
            stringBuffer.append(strArr[i3]);
            stringBuffer.append(' ');
        }
        stringBuffer.append(strArr[(i + i2) - 1]);
        return stringBuffer.toString();
    }

    static void zza(int i, long j, String str, PriorityQueue<zza> priorityQueue) {
        zza zzaVar = new zza(j, str);
        if ((priorityQueue.size() != i || priorityQueue.peek().value <= zzaVar.value) && !priorityQueue.contains(zzaVar)) {
            priorityQueue.add(zzaVar);
            if (priorityQueue.size() > i) {
                priorityQueue.poll();
            }
        }
    }

    public static void zza(String[] strArr, int i, int i2, PriorityQueue<zza> priorityQueue) {
        long jZzb = zzb(strArr, 0, i2);
        zza(i, jZzb, zza(strArr, 0, i2), priorityQueue);
        long jZza = zza(16785407L, i2 - 1);
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 >= (strArr.length - i2) + 1) {
                return;
            }
            jZzb = zza(zzbo.zzC(strArr[i4 - 1]), zzbo.zzC(strArr[(i4 + i2) - 1]), jZzb, jZza, 16785407L);
            zza(i, jZzb, zza(strArr, i4, i2), priorityQueue);
            i3 = i4 + 1;
        }
    }

    private static long zzb(String[] strArr, int i, int i2) {
        long jZzC = (zzbo.zzC(strArr[i]) + 2147483647L) % 1073807359;
        for (int i3 = i + 1; i3 < i + i2; i3++) {
            jZzC = (((jZzC * 16785407) % 1073807359) + ((zzbo.zzC(strArr[i3]) + 2147483647L) % 1073807359)) % 1073807359;
        }
        return jZzC;
    }
}
