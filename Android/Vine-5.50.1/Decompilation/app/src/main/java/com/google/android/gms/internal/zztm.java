package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

/* loaded from: classes2.dex */
final class zztm {
    final int tag;
    final byte[] zzbqc;

    zztm(int i, byte[] bArr) {
        this.tag = i;
        this.zzbqc = bArr;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zztm)) {
            return false;
        }
        zztm zztmVar = (zztm) o;
        return this.tag == zztmVar.tag && Arrays.equals(this.zzbqc, zztmVar.zzbqc);
    }

    public int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzbqc);
    }

    void writeTo(zztd output) throws IOException {
        output.zzmy(this.tag);
        output.zzG(this.zzbqc);
    }

    int zzz() {
        return 0 + zztd.zzmz(this.tag) + this.zzbqc.length;
    }
}
