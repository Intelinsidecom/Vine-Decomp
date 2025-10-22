package com.google.android.gms.internal;

import java.util.Arrays;

/* loaded from: classes2.dex */
public final class zzti {
    public static final Object zzbqa = new Object();

    public static boolean equals(int[] field1, int[] field2) {
        return (field1 == null || field1.length == 0) ? field2 == null || field2.length == 0 : Arrays.equals(field1, field2);
    }

    public static boolean equals(long[] field1, long[] field2) {
        return (field1 == null || field1.length == 0) ? field2 == null || field2.length == 0 : Arrays.equals(field1, field2);
    }

    public static boolean equals(Object[] field1, Object[] field2) {
        int length = field1 == null ? 0 : field1.length;
        int length2 = field2 == null ? 0 : field2.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= length || field1[i2] != null) {
                int i3 = i;
                while (i3 < length2 && field2[i3] == null) {
                    i3++;
                }
                boolean z = i2 >= length;
                boolean z2 = i3 >= length2;
                if (z && z2) {
                    return true;
                }
                if (z != z2 || !field1[i2].equals(field2[i3])) {
                    return false;
                }
                i = i3 + 1;
                i2++;
            } else {
                i2++;
            }
        }
    }

    public static int hashCode(int[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(long[] field) {
        if (field == null || field.length == 0) {
            return 0;
        }
        return Arrays.hashCode(field);
    }

    public static int hashCode(Object[] field) {
        int iHashCode = 0;
        int length = field == null ? 0 : field.length;
        for (int i = 0; i < length; i++) {
            Object obj = field[i];
            if (obj != null) {
                iHashCode = (iHashCode * 31) + obj.hashCode();
            }
        }
        return iHashCode;
    }

    public static int zza(byte[][] bArr) {
        int iHashCode = 0;
        int length = bArr == null ? 0 : bArr.length;
        for (int i = 0; i < length; i++) {
            byte[] bArr2 = bArr[i];
            if (bArr2 != null) {
                iHashCode = (iHashCode * 31) + Arrays.hashCode(bArr2);
            }
        }
        return iHashCode;
    }

    public static void zza(zzte zzteVar, zzte zzteVar2) {
        if (zzteVar.zzbpQ != null) {
            zzteVar2.zzbpQ = zzteVar.zzbpQ.clone();
        }
    }

    public static boolean zza(byte[][] bArr, byte[][] bArr2) {
        int length = bArr == null ? 0 : bArr.length;
        int length2 = bArr2 == null ? 0 : bArr2.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i2 >= length || bArr[i2] != null) {
                int i3 = i;
                while (i3 < length2 && bArr2[i3] == null) {
                    i3++;
                }
                boolean z = i2 >= length;
                boolean z2 = i3 >= length2;
                if (z && z2) {
                    return true;
                }
                if (z != z2 || !Arrays.equals(bArr[i2], bArr2[i3])) {
                    return false;
                }
                i = i3 + 1;
                i2++;
            } else {
                i2++;
            }
        }
    }
}
