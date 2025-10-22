package com.google.android.gms.internal;

import com.googlecode.javacv.cpp.avutil;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

/* loaded from: classes2.dex */
public final class zztd {
    private final ByteBuffer zzbpP;

    public static class zza extends IOException {
        zza(int i, int i2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + i + " limit " + i2 + ").");
        }
    }

    private zztd(ByteBuffer byteBuffer) {
        this.zzbpP = byteBuffer;
        this.zzbpP.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zztd(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    public static zztd zzD(byte[] bArr) {
        return zzb(bArr, 0, bArr.length);
    }

    public static int zzF(byte[] bArr) {
        return zzmz(bArr.length) + bArr.length;
    }

    public static int zzI(int i, int i2) {
        return zzmx(i) + zzmu(i2);
    }

    private static int zza(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        int i3 = i;
        while (i3 < length) {
            char cCharAt = charSequence.charAt(i3);
            if (cCharAt < 2048) {
                i2 += (127 - cCharAt) >>> 31;
            } else {
                i2 += 2;
                if (55296 <= cCharAt && cCharAt <= 57343) {
                    if (Character.codePointAt(charSequence, i3) < 65536) {
                        throw new IllegalArgumentException("Unpaired surrogate at index " + i3);
                    }
                    i3++;
                }
            }
            i3++;
        }
        return i2;
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int length = charSequence.length();
        int i4 = 0;
        int i5 = i + i2;
        while (i4 < length && i4 + i < i5) {
            char cCharAt = charSequence.charAt(i4);
            if (cCharAt >= 128) {
                break;
            }
            bArr[i + i4] = (byte) cCharAt;
            i4++;
        }
        if (i4 == length) {
            return i + length;
        }
        int i6 = i + i4;
        while (i4 < length) {
            char cCharAt2 = charSequence.charAt(i4);
            if (cCharAt2 < 128 && i6 < i5) {
                i3 = i6 + 1;
                bArr[i6] = (byte) cCharAt2;
            } else if (cCharAt2 < 2048 && i6 <= i5 - 2) {
                int i7 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> 6) | 960);
                i3 = i7 + 1;
                bArr[i7] = (byte) ((cCharAt2 & '?') | 128);
            } else {
                if ((cCharAt2 >= 55296 && 57343 >= cCharAt2) || i6 > i5 - 3) {
                    if (i6 > i5 - 4) {
                        if (55296 > cCharAt2 || cCharAt2 > 57343 || (i4 + 1 != charSequence.length() && Character.isSurrogatePair(cCharAt2, charSequence.charAt(i4 + 1)))) {
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + cCharAt2 + " at index " + i6);
                        }
                        throw new IllegalArgumentException("Unpaired surrogate at index " + i4);
                    }
                    if (i4 + 1 != charSequence.length()) {
                        i4++;
                        char cCharAt3 = charSequence.charAt(i4);
                        if (Character.isSurrogatePair(cCharAt2, cCharAt3)) {
                            int codePoint = Character.toCodePoint(cCharAt2, cCharAt3);
                            int i8 = i6 + 1;
                            bArr[i6] = (byte) ((codePoint >>> 18) | 240);
                            int i9 = i8 + 1;
                            bArr[i8] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i10 = i9 + 1;
                            bArr[i9] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i3 = i10 + 1;
                            bArr[i10] = (byte) ((codePoint & 63) | 128);
                        }
                    }
                    throw new IllegalArgumentException("Unpaired surrogate at index " + (i4 - 1));
                }
                int i11 = i6 + 1;
                bArr[i6] = (byte) ((cCharAt2 >>> '\f') | 480);
                int i12 = i11 + 1;
                bArr[i11] = (byte) (((cCharAt2 >>> 6) & 63) | 128);
                i3 = i12 + 1;
                bArr[i12] = (byte) ((cCharAt2 & '?') | 128);
            }
            i4++;
            i6 = i3;
        }
        return i6;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (!byteBuffer.hasArray()) {
            zzb(charSequence, byteBuffer);
            return;
        }
        try {
            byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
        } catch (ArrayIndexOutOfBoundsException e) {
            BufferOverflowException bufferOverflowException = new BufferOverflowException();
            bufferOverflowException.initCause(e);
            throw bufferOverflowException;
        }
    }

    public static int zzad(long j) {
        return zzag(j);
    }

    public static int zzae(long j) {
        return zzag(zzai(j));
    }

    public static int zzag(long j) {
        if (((-128) & j) == 0) {
            return 1;
        }
        if (((-16384) & j) == 0) {
            return 2;
        }
        if (((-2097152) & j) == 0) {
            return 3;
        }
        if (((-268435456) & j) == 0) {
            return 4;
        }
        if (((-34359738368L) & j) == 0) {
            return 5;
        }
        if (((-4398046511104L) & j) == 0) {
            return 6;
        }
        if (((-562949953421312L) & j) == 0) {
            return 7;
        }
        if (((-72057594037927936L) & j) == 0) {
            return 8;
        }
        return (Long.MIN_VALUE & j) == 0 ? 9 : 10;
    }

    public static long zzai(long j) {
        return (j << 1) ^ (j >> 63);
    }

    public static int zzay(boolean z) {
        return 1;
    }

    public static int zzb(int i, zztk zztkVar) {
        return (zzmx(i) * 2) + zzd(zztkVar);
    }

    public static int zzb(int i, byte[] bArr) {
        return zzmx(i) + zzF(bArr);
    }

    public static zztd zzb(byte[] bArr, int i, int i2) {
        return new zztd(bArr, i, i2);
    }

    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            char cCharAt = charSequence.charAt(i);
            if (cCharAt < 128) {
                byteBuffer.put((byte) cCharAt);
            } else if (cCharAt < 2048) {
                byteBuffer.put((byte) ((cCharAt >>> 6) | 960));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            } else {
                if (cCharAt >= 55296 && 57343 >= cCharAt) {
                    if (i + 1 != charSequence.length()) {
                        i++;
                        char cCharAt2 = charSequence.charAt(i);
                        if (Character.isSurrogatePair(cCharAt, cCharAt2)) {
                            int codePoint = Character.toCodePoint(cCharAt, cCharAt2);
                            byteBuffer.put((byte) ((codePoint >>> 18) | 240));
                            byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                            byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                            byteBuffer.put((byte) ((codePoint & 63) | 128));
                        }
                    }
                    throw new IllegalArgumentException("Unpaired surrogate at index " + (i - 1));
                }
                byteBuffer.put((byte) ((cCharAt >>> '\f') | 480));
                byteBuffer.put((byte) (((cCharAt >>> 6) & 63) | 128));
                byteBuffer.put((byte) ((cCharAt & '?') | 128));
            }
            i++;
        }
    }

    public static int zzc(int i, float f) {
        return zzmx(i) + zzj(f);
    }

    public static int zzc(int i, zztk zztkVar) {
        return zzmx(i) + zze(zztkVar);
    }

    public static int zzc(int i, boolean z) {
        return zzmx(i) + zzay(z);
    }

    private static int zzc(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < 128) {
            i++;
        }
        int i2 = i;
        int iZza = length;
        while (true) {
            if (i2 < length) {
                char cCharAt = charSequence.charAt(i2);
                if (cCharAt >= 2048) {
                    iZza += zza(charSequence, i2);
                    break;
                }
                i2++;
                iZza = ((127 - cCharAt) >>> 31) + iZza;
            } else {
                break;
            }
        }
        if (iZza < length) {
            throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (iZza + avutil.AV_CH_WIDE_RIGHT));
        }
        return iZza;
    }

    public static int zzd(int i, long j) {
        return zzmx(i) + zzad(j);
    }

    public static int zzd(zztk zztkVar) {
        return zztkVar.getSerializedSize();
    }

    public static int zze(int i, long j) {
        return zzmx(i) + zzae(j);
    }

    public static int zze(zztk zztkVar) {
        int serializedSize = zztkVar.getSerializedSize();
        return serializedSize + zzmz(serializedSize);
    }

    public static int zzga(String str) {
        int iZzc = zzc(str);
        return iZzc + zzmz(iZzc);
    }

    public static int zzj(float f) {
        return 4;
    }

    public static int zzmu(int i) {
        if (i >= 0) {
            return zzmz(i);
        }
        return 10;
    }

    public static int zzmx(int i) {
        return zzmz(zztn.zzL(i, 0));
    }

    public static int zzmz(int i) {
        if ((i & (-128)) == 0) {
            return 1;
        }
        if ((i & (-16384)) == 0) {
            return 2;
        }
        if (((-2097152) & i) == 0) {
            return 3;
        }
        return ((-268435456) & i) == 0 ? 4 : 5;
    }

    public static int zzp(int i, String str) {
        return zzmx(i) + zzga(str);
    }

    public void zzE(byte[] bArr) throws IOException {
        zzmy(bArr.length);
        zzG(bArr);
    }

    public void zzG(int i, int i2) throws IOException {
        zzK(i, 0);
        zzms(i2);
    }

    public void zzG(byte[] bArr) throws IOException {
        zzc(bArr, 0, bArr.length);
    }

    public int zzHx() {
        return this.zzbpP.remaining();
    }

    public void zzHy() {
        if (zzHx() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }

    public void zzK(int i, int i2) throws IOException {
        zzmy(zztn.zzL(i, i2));
    }

    public void zza(int i, zztk zztkVar) throws IOException {
        zzK(i, 2);
        zzc(zztkVar);
    }

    public void zza(int i, byte[] bArr) throws IOException {
        zzK(i, 2);
        zzE(bArr);
    }

    public void zzab(long j) throws IOException {
        zzaf(j);
    }

    public void zzac(long j) throws IOException {
        zzaf(zzai(j));
    }

    public void zzaf(long j) throws IOException {
        while (((-128) & j) != 0) {
            zzmw((((int) j) & 127) | 128);
            j >>>= 7;
        }
        zzmw((int) j);
    }

    public void zzax(boolean z) throws IOException {
        zzmw(z ? 1 : 0);
    }

    public void zzb(byte b) throws IOException {
        if (!this.zzbpP.hasRemaining()) {
            throw new zza(this.zzbpP.position(), this.zzbpP.limit());
        }
        this.zzbpP.put(b);
    }

    public void zzb(int i, float f) throws IOException {
        zzK(i, 5);
        zzi(f);
    }

    public void zzb(int i, long j) throws IOException {
        zzK(i, 0);
        zzab(j);
    }

    public void zzb(int i, String str) throws IOException {
        zzK(i, 2);
        zzfZ(str);
    }

    public void zzb(int i, boolean z) throws IOException {
        zzK(i, 0);
        zzax(z);
    }

    public void zzb(zztk zztkVar) throws IOException {
        zztkVar.writeTo(this);
    }

    public void zzc(int i, long j) throws IOException {
        zzK(i, 0);
        zzac(j);
    }

    public void zzc(zztk zztkVar) throws IOException {
        zzmy(zztkVar.getCachedSize());
        zztkVar.writeTo(this);
    }

    public void zzc(byte[] bArr, int i, int i2) throws IOException {
        if (this.zzbpP.remaining() < i2) {
            throw new zza(this.zzbpP.position(), this.zzbpP.limit());
        }
        this.zzbpP.put(bArr, i, i2);
    }

    public void zzfZ(String str) throws IOException {
        try {
            int iZzmz = zzmz(str.length());
            if (iZzmz != zzmz(str.length() * 3)) {
                zzmy(zzc(str));
                zza(str, this.zzbpP);
                return;
            }
            int iPosition = this.zzbpP.position();
            if (this.zzbpP.remaining() < iZzmz) {
                throw new zza(iZzmz + iPosition, this.zzbpP.limit());
            }
            this.zzbpP.position(iPosition + iZzmz);
            zza(str, this.zzbpP);
            int iPosition2 = this.zzbpP.position();
            this.zzbpP.position(iPosition);
            zzmy((iPosition2 - iPosition) - iZzmz);
            this.zzbpP.position(iPosition2);
        } catch (BufferOverflowException e) {
            zza zzaVar = new zza(this.zzbpP.position(), this.zzbpP.limit());
            zzaVar.initCause(e);
            throw zzaVar;
        }
    }

    public void zzi(float f) throws IOException {
        zzmA(Float.floatToIntBits(f));
    }

    public void zzmA(int i) throws IOException {
        if (this.zzbpP.remaining() < 4) {
            throw new zza(this.zzbpP.position(), this.zzbpP.limit());
        }
        this.zzbpP.putInt(i);
    }

    public void zzms(int i) throws IOException {
        if (i >= 0) {
            zzmy(i);
        } else {
            zzaf(i);
        }
    }

    public void zzmw(int i) throws IOException {
        zzb((byte) i);
    }

    public void zzmy(int i) throws IOException {
        while ((i & (-128)) != 0) {
            zzmw((i & 127) | 128);
            i >>>= 7;
        }
        zzmw(i);
    }
}
