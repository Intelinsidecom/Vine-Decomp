package com.google.android.gms.internal;

/* loaded from: classes2.dex */
public final class zztg implements Cloneable {
    private static final zzth zzbpT = new zzth();
    private int mSize;
    private boolean zzbpU;
    private int[] zzbpV;
    private zzth[] zzbpW;

    zztg() {
        this(10);
    }

    zztg(int i) {
        this.zzbpU = false;
        int iIdealIntArraySize = idealIntArraySize(i);
        this.zzbpV = new int[iIdealIntArraySize];
        this.zzbpW = new zzth[iIdealIntArraySize];
        this.mSize = 0;
    }

    private void gc() {
        int i = this.mSize;
        int[] iArr = this.zzbpV;
        zzth[] zzthVarArr = this.zzbpW;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            zzth zzthVar = zzthVarArr[i3];
            if (zzthVar != zzbpT) {
                if (i3 != i2) {
                    iArr[i2] = iArr[i3];
                    zzthVarArr[i2] = zzthVar;
                    zzthVarArr[i3] = null;
                }
                i2++;
            }
        }
        this.zzbpU = false;
        this.mSize = i2;
    }

    private int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    private int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    private boolean zza(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private boolean zza(zzth[] zzthVarArr, zzth[] zzthVarArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            if (!zzthVarArr[i2].equals(zzthVarArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    private int zzmE(int i) {
        int i2 = 0;
        int i3 = this.mSize - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) >>> 1;
            int i5 = this.zzbpV[i4];
            if (i5 < i) {
                i2 = i4 + 1;
            } else {
                if (i5 <= i) {
                    return i4;
                }
                i3 = i4 - 1;
            }
        }
        return i2 ^ (-1);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zztg)) {
            return false;
        }
        zztg zztgVar = (zztg) o;
        if (size() != zztgVar.size()) {
            return false;
        }
        return zza(this.zzbpV, zztgVar.zzbpV, this.mSize) && zza(this.zzbpW, zztgVar.zzbpW, this.mSize);
    }

    public int hashCode() {
        if (this.zzbpU) {
            gc();
        }
        int iHashCode = 17;
        for (int i = 0; i < this.mSize; i++) {
            iHashCode = (((iHashCode * 31) + this.zzbpV[i]) * 31) + this.zzbpW[i].hashCode();
        }
        return iHashCode;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    int size() {
        if (this.zzbpU) {
            gc();
        }
        return this.mSize;
    }

    /* renamed from: zzHA, reason: merged with bridge method [inline-methods] */
    public final zztg clone() {
        int size = size();
        zztg zztgVar = new zztg(size);
        System.arraycopy(this.zzbpV, 0, zztgVar.zzbpV, 0, size);
        for (int i = 0; i < size; i++) {
            if (this.zzbpW[i] != null) {
                zztgVar.zzbpW[i] = this.zzbpW[i].clone();
            }
        }
        zztgVar.mSize = size;
        return zztgVar;
    }

    void zza(int i, zzth zzthVar) {
        int iZzmE = zzmE(i);
        if (iZzmE >= 0) {
            this.zzbpW[iZzmE] = zzthVar;
            return;
        }
        int iZzmE2 = iZzmE ^ (-1);
        if (iZzmE2 < this.mSize && this.zzbpW[iZzmE2] == zzbpT) {
            this.zzbpV[iZzmE2] = i;
            this.zzbpW[iZzmE2] = zzthVar;
            return;
        }
        if (this.zzbpU && this.mSize >= this.zzbpV.length) {
            gc();
            iZzmE2 = zzmE(i) ^ (-1);
        }
        if (this.mSize >= this.zzbpV.length) {
            int iIdealIntArraySize = idealIntArraySize(this.mSize + 1);
            int[] iArr = new int[iIdealIntArraySize];
            zzth[] zzthVarArr = new zzth[iIdealIntArraySize];
            System.arraycopy(this.zzbpV, 0, iArr, 0, this.zzbpV.length);
            System.arraycopy(this.zzbpW, 0, zzthVarArr, 0, this.zzbpW.length);
            this.zzbpV = iArr;
            this.zzbpW = zzthVarArr;
        }
        if (this.mSize - iZzmE2 != 0) {
            System.arraycopy(this.zzbpV, iZzmE2, this.zzbpV, iZzmE2 + 1, this.mSize - iZzmE2);
            System.arraycopy(this.zzbpW, iZzmE2, this.zzbpW, iZzmE2 + 1, this.mSize - iZzmE2);
        }
        this.zzbpV[iZzmE2] = i;
        this.zzbpW[iZzmE2] = zzthVar;
        this.mSize++;
    }

    zzth zzmC(int i) {
        int iZzmE = zzmE(i);
        if (iZzmE < 0 || this.zzbpW[iZzmE] == zzbpT) {
            return null;
        }
        return this.zzbpW[iZzmE];
    }

    zzth zzmD(int i) {
        if (this.zzbpU) {
            gc();
        }
        return this.zzbpW[i];
    }
}
