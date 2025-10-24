package com.google.android.gms.internal;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes.dex */
public class zzar {
    private final zzap zznq;
    private final SecureRandom zzoj;

    public class zza extends Exception {
        public zza() {
        }

        public zza(Throwable th) {
            super(th);
        }
    }

    public zzar(zzap zzapVar, SecureRandom secureRandom) {
        this.zznq = zzapVar;
        this.zzoj = secureRandom;
    }

    static void zze(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ 68);
        }
    }

    public byte[] zzc(byte[] bArr, String str) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, zza, InvalidAlgorithmParameterException {
        if (bArr.length != 16) {
            throw new zza();
        }
        try {
            byte[] bArrZza = this.zznq.zza(str, false);
            if (bArrZza.length <= 16) {
                throw new zza();
            }
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArrZza.length);
            byteBufferAllocate.put(bArrZza);
            byteBufferAllocate.flip();
            byte[] bArr2 = new byte[16];
            byte[] bArr3 = new byte[bArrZza.length - 16];
            byteBufferAllocate.get(bArr2);
            byteBufferAllocate.get(bArr3);
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, new IvParameterSpec(bArr2));
            return cipher.doFinal(bArr3);
        } catch (IllegalArgumentException e) {
            throw new zza(e);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new zza(e2);
        } catch (InvalidKeyException e3) {
            throw new zza(e3);
        } catch (NoSuchAlgorithmException e4) {
            throw new zza(e4);
        } catch (BadPaddingException e5) {
            throw new zza(e5);
        } catch (IllegalBlockSizeException e6) {
            throw new zza(e6);
        } catch (NoSuchPaddingException e7) {
            throw new zza(e7);
        }
    }

    public byte[] zzl(String str) throws zza {
        try {
            byte[] bArrZza = this.zznq.zza(str, false);
            if (bArrZza.length != 32) {
                throw new zza();
            }
            byte[] bArr = new byte[16];
            ByteBuffer.wrap(bArrZza, 4, 16).get(bArr);
            zze(bArr);
            return bArr;
        } catch (IllegalArgumentException e) {
            throw new zza(e);
        }
    }
}
