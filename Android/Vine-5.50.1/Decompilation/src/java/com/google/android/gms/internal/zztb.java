package com.google.android.gms.internal;

/* loaded from: classes.dex */
public class zztb {
    private final byte[] zzbpD = new byte[256];
    private int zzbpE;
    private int zzbpF;

    public zztb(byte[] bArr) {
        for (int i = 0; i < 256; i++) {
            this.zzbpD[i] = (byte) i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 256; i3++) {
            i2 = (i2 + this.zzbpD[i3] + bArr[i3 % bArr.length]) & 255;
            byte b = this.zzbpD[i3];
            this.zzbpD[i3] = this.zzbpD[i2];
            this.zzbpD[i2] = b;
        }
        this.zzbpE = 0;
        this.zzbpF = 0;
    }

    public void zzB(byte[] bArr) {
        int i = this.zzbpE;
        int i2 = this.zzbpF;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            i = (i + 1) & 255;
            i2 = (i2 + this.zzbpD[i]) & 255;
            byte b = this.zzbpD[i];
            this.zzbpD[i] = this.zzbpD[i2];
            this.zzbpD[i2] = b;
            bArr[i3] = (byte) (bArr[i3] ^ this.zzbpD[(this.zzbpD[i] + this.zzbpD[i2]) & 255]);
        }
        this.zzbpE = i;
        this.zzbpF = i2;
    }
}
