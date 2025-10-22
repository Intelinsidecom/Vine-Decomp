package com.google.android.gms.internal;

import java.security.MessageDigest;

@zzha
/* loaded from: classes.dex */
public class zzbp extends zzbm {
    private MessageDigest zztl;

    byte[] zza(String[] strArr) {
        byte[] bArr = new byte[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            bArr[i] = zzk(zzbo.zzC(strArr[i]));
        }
        return bArr;
    }

    byte zzk(int i) {
        return (byte) ((((i & 255) ^ ((65280 & i) >> 8)) ^ ((16711680 & i) >> 16)) ^ (((-16777216) & i) >> 24));
    }

    @Override // com.google.android.gms.internal.zzbm
    public byte[] zzz(String str) {
        byte[] bArr;
        byte[] bArrZza = zza(str.split(" "));
        this.zztl = zzcG();
        synchronized (this.zzpK) {
            if (this.zztl == null) {
                bArr = new byte[0];
            } else {
                this.zztl.reset();
                this.zztl.update(bArrZza);
                byte[] bArrDigest = this.zztl.digest();
                bArr = new byte[bArrDigest.length <= 4 ? bArrDigest.length : 4];
                System.arraycopy(bArrDigest, 0, bArr, 0, bArr.length);
            }
        }
        return bArr;
    }
}
