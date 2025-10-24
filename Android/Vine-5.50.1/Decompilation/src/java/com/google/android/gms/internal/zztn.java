package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: classes2.dex */
public final class zztn {
    public static final int[] zzboD = new int[0];
    public static final long[] zzboC = new long[0];
    public static final float[] zzbqd = new float[0];
    public static final double[] zzbqe = new double[0];
    public static final boolean[] zzbqf = new boolean[0];
    public static final String[] zzbqg = new String[0];
    public static final byte[][] zzbqh = new byte[0][];
    public static final byte[] zzbqi = new byte[0];

    static int zzL(int i, int i2) {
        return (i << 3) | i2;
    }

    public static boolean zzb(zztc zztcVar, int i) throws IOException {
        return zztcVar.zzml(i);
    }

    public static final int zzc(zztc zztcVar, int i) throws IOException {
        int i2 = 1;
        int position = zztcVar.getPosition();
        zztcVar.zzml(i);
        while (zztcVar.zzHi() == i) {
            zztcVar.zzml(i);
            i2++;
        }
        zztcVar.zzmp(position);
        return i2;
    }

    static int zzmF(int i) {
        return i & 7;
    }

    public static int zzmG(int i) {
        return i >>> 3;
    }
}
