package com.google.android.gms.internal;

import java.io.IOException;

/* loaded from: classes.dex */
class zzas implements zzaq {
    private zztd zzol;
    private byte[] zzom;
    private final int zzon;

    public zzas(int i) {
        this.zzon = i;
        reset();
    }

    @Override // com.google.android.gms.internal.zzaq
    public void reset() {
        this.zzom = new byte[this.zzon];
        this.zzol = zztd.zzD(this.zzom);
    }

    @Override // com.google.android.gms.internal.zzaq
    public byte[] zzad() throws IOException {
        int iZzHx = this.zzol.zzHx();
        if (iZzHx < 0) {
            throw new IOException();
        }
        if (iZzHx == 0) {
            return this.zzom;
        }
        byte[] bArr = new byte[this.zzom.length - iZzHx];
        System.arraycopy(this.zzom, 0, bArr, 0, bArr.length);
        return bArr;
    }

    @Override // com.google.android.gms.internal.zzaq
    public void zzb(int i, long j) throws IOException {
        this.zzol.zzb(i, j);
    }

    @Override // com.google.android.gms.internal.zzaq
    public void zzb(int i, String str) throws IOException {
        this.zzol.zzb(i, str);
    }
}
