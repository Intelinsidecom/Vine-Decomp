package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.List;

@zzha
/* loaded from: classes.dex */
public class zzit {
    private final String[] zzKZ;
    private final double[] zzLa;
    private final double[] zzLb;
    private final int[] zzLc;
    private int zzLd;

    public static class zza {
        public final int count;
        public final String name;
        public final double zzLe;
        public final double zzLf;
        public final double zzLg;

        public zza(String str, double d, double d2, double d3, int i) {
            this.name = str;
            this.zzLf = d;
            this.zzLe = d2;
            this.zzLg = d3;
            this.count = i;
        }

        public boolean equals(Object other) {
            if (!(other instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) other;
            return com.google.android.gms.common.internal.zzw.equal(this.name, zzaVar.name) && this.zzLe == zzaVar.zzLe && this.zzLf == zzaVar.zzLf && this.count == zzaVar.count && Double.compare(this.zzLg, zzaVar.zzLg) == 0;
        }

        public int hashCode() {
            return com.google.android.gms.common.internal.zzw.hashCode(this.name, Double.valueOf(this.zzLe), Double.valueOf(this.zzLf), Double.valueOf(this.zzLg), Integer.valueOf(this.count));
        }

        public String toString() {
            return com.google.android.gms.common.internal.zzw.zzx(this).zzg("name", this.name).zzg("minBound", Double.valueOf(this.zzLf)).zzg("maxBound", Double.valueOf(this.zzLe)).zzg("percent", Double.valueOf(this.zzLg)).zzg("count", Integer.valueOf(this.count)).toString();
        }
    }

    public static class zzb {
        private final List<String> zzLh = new ArrayList();
        private final List<Double> zzLi = new ArrayList();
        private final List<Double> zzLj = new ArrayList();

        public zzb zza(String str, double d, double d2) {
            int i;
            int i2 = 0;
            while (true) {
                i = i2;
                if (i >= this.zzLh.size()) {
                    break;
                }
                double dDoubleValue = this.zzLj.get(i).doubleValue();
                double dDoubleValue2 = this.zzLi.get(i).doubleValue();
                if (d < dDoubleValue || (dDoubleValue == d && d2 < dDoubleValue2)) {
                    break;
                }
                i2 = i + 1;
            }
            this.zzLh.add(i, str);
            this.zzLj.add(i, Double.valueOf(d));
            this.zzLi.add(i, Double.valueOf(d2));
            return this;
        }

        public zzit zzhi() {
            return new zzit(this);
        }
    }

    private zzit(zzb zzbVar) {
        int size = zzbVar.zzLi.size();
        this.zzKZ = (String[]) zzbVar.zzLh.toArray(new String[size]);
        this.zzLa = zzi(zzbVar.zzLi);
        this.zzLb = zzi(zzbVar.zzLj);
        this.zzLc = new int[size];
        this.zzLd = 0;
    }

    private double[] zzi(List<Double> list) {
        double[] dArr = new double[list.size()];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= dArr.length) {
                return dArr;
            }
            dArr[i2] = list.get(i2).doubleValue();
            i = i2 + 1;
        }
    }

    public List<zza> getBuckets() {
        ArrayList arrayList = new ArrayList(this.zzKZ.length);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.zzKZ.length) {
                return arrayList;
            }
            arrayList.add(new zza(this.zzKZ[i2], this.zzLb[i2], this.zzLa[i2], this.zzLc[i2] / this.zzLd, this.zzLc[i2]));
            i = i2 + 1;
        }
    }

    public void zza(double d) {
        this.zzLd++;
        for (int i = 0; i < this.zzLb.length; i++) {
            if (this.zzLb[i] <= d && d < this.zzLa[i]) {
                int[] iArr = this.zzLc;
                iArr[i] = iArr[i] + 1;
            }
            if (d < this.zzLb[i]) {
                return;
            }
        }
    }
}
