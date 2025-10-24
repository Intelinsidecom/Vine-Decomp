package com.google.android.gms.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
class zzth implements Cloneable {
    private zztf<?, ?> zzbpX;
    private Object zzbpY;
    private List<zztm> zzbpZ = new ArrayList();

    zzth() {
    }

    private byte[] toByteArray() throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        byte[] bArr = new byte[zzz()];
        writeTo(zztd.zzD(bArr));
        return bArr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzth)) {
            return false;
        }
        zzth zzthVar = (zzth) o;
        if (this.zzbpY != null && zzthVar.zzbpY != null) {
            if (this.zzbpX == zzthVar.zzbpX) {
                return !this.zzbpX.zzbpR.isArray() ? this.zzbpY.equals(zzthVar.zzbpY) : this.zzbpY instanceof byte[] ? Arrays.equals((byte[]) this.zzbpY, (byte[]) zzthVar.zzbpY) : this.zzbpY instanceof int[] ? Arrays.equals((int[]) this.zzbpY, (int[]) zzthVar.zzbpY) : this.zzbpY instanceof long[] ? Arrays.equals((long[]) this.zzbpY, (long[]) zzthVar.zzbpY) : this.zzbpY instanceof float[] ? Arrays.equals((float[]) this.zzbpY, (float[]) zzthVar.zzbpY) : this.zzbpY instanceof double[] ? Arrays.equals((double[]) this.zzbpY, (double[]) zzthVar.zzbpY) : this.zzbpY instanceof boolean[] ? Arrays.equals((boolean[]) this.zzbpY, (boolean[]) zzthVar.zzbpY) : Arrays.deepEquals((Object[]) this.zzbpY, (Object[]) zzthVar.zzbpY);
            }
            return false;
        }
        if (this.zzbpZ != null && zzthVar.zzbpZ != null) {
            return this.zzbpZ.equals(zzthVar.zzbpZ);
        }
        try {
            return Arrays.equals(toByteArray(), zzthVar.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbpY != null) {
            this.zzbpX.zza(this.zzbpY, output);
            return;
        }
        Iterator<zztm> it = this.zzbpZ.iterator();
        while (it.hasNext()) {
            it.next().writeTo(output);
        }
    }

    /* renamed from: zzHB, reason: merged with bridge method [inline-methods] */
    public final zzth clone() {
        zzth zzthVar = new zzth();
        try {
            zzthVar.zzbpX = this.zzbpX;
            if (this.zzbpZ == null) {
                zzthVar.zzbpZ = null;
            } else {
                zzthVar.zzbpZ.addAll(this.zzbpZ);
            }
            if (this.zzbpY != null) {
                if (this.zzbpY instanceof zztk) {
                    zzthVar.zzbpY = ((zztk) this.zzbpY).mo4clone();
                } else if (this.zzbpY instanceof byte[]) {
                    zzthVar.zzbpY = ((byte[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.zzbpY;
                    byte[][] bArr2 = new byte[bArr.length][];
                    zzthVar.zzbpY = bArr2;
                    for (int i = 0; i < bArr.length; i++) {
                        bArr2[i] = (byte[]) bArr[i].clone();
                    }
                } else if (this.zzbpY instanceof boolean[]) {
                    zzthVar.zzbpY = ((boolean[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof int[]) {
                    zzthVar.zzbpY = ((int[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof long[]) {
                    zzthVar.zzbpY = ((long[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof float[]) {
                    zzthVar.zzbpY = ((float[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof double[]) {
                    zzthVar.zzbpY = ((double[]) this.zzbpY).clone();
                } else if (this.zzbpY instanceof zztk[]) {
                    zztk[] zztkVarArr = (zztk[]) this.zzbpY;
                    zztk[] zztkVarArr2 = new zztk[zztkVarArr.length];
                    zzthVar.zzbpY = zztkVarArr2;
                    for (int i2 = 0; i2 < zztkVarArr.length; i2++) {
                        zztkVarArr2[i2] = zztkVarArr[i2].mo4clone();
                    }
                }
            }
            return zzthVar;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    void zza(zztm zztmVar) {
        this.zzbpZ.add(zztmVar);
    }

    int zzz() {
        int iZzz = 0;
        if (this.zzbpY != null) {
            return this.zzbpX.zzY(this.zzbpY);
        }
        Iterator<zztm> it = this.zzbpZ.iterator();
        while (true) {
            int i = iZzz;
            if (!it.hasNext()) {
                return i;
            }
            iZzz = it.next().zzz() + i;
        }
    }
}
