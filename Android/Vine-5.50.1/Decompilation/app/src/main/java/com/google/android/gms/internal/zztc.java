package com.google.android.gms.internal;

import com.flurry.android.Constants;
import com.googlecode.javacv.cpp.avformat;
import java.io.IOException;

/* loaded from: classes2.dex */
public final class zztc {
    private final byte[] buffer;
    private int zzbpG;
    private int zzbpH;
    private int zzbpI;
    private int zzbpJ;
    private int zzbpK;
    private int zzbpM;
    private int zzbpL = Integer.MAX_VALUE;
    private int zzbpN = 64;
    private int zzbpO = avformat.AVFMT_SEEK_TO_PTS;

    private zztc(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzbpG = i;
        this.zzbpH = i + i2;
        this.zzbpJ = i;
    }

    public static zztc zzC(byte[] bArr) {
        return zza(bArr, 0, bArr.length);
    }

    private void zzHt() {
        this.zzbpH += this.zzbpI;
        int i = this.zzbpH;
        if (i <= this.zzbpL) {
            this.zzbpI = 0;
        } else {
            this.zzbpI = i - this.zzbpL;
            this.zzbpH -= this.zzbpI;
        }
    }

    public static zztc zza(byte[] bArr, int i, int i2) {
        return new zztc(bArr, i, i2);
    }

    public static long zzaa(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    public int getPosition() {
        return this.zzbpJ - this.zzbpG;
    }

    public byte[] readBytes() throws IOException {
        int iZzHp = zzHp();
        if (iZzHp > this.zzbpH - this.zzbpJ || iZzHp <= 0) {
            return iZzHp == 0 ? zztn.zzbqi : zzmq(iZzHp);
        }
        byte[] bArr = new byte[iZzHp];
        System.arraycopy(this.buffer, this.zzbpJ, bArr, 0, iZzHp);
        this.zzbpJ = iZzHp + this.zzbpJ;
        return bArr;
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(zzHr());
    }

    public String readString() throws IOException {
        int iZzHp = zzHp();
        if (iZzHp > this.zzbpH - this.zzbpJ || iZzHp <= 0) {
            return new String(zzmq(iZzHp), "UTF-8");
        }
        String str = new String(this.buffer, this.zzbpJ, iZzHp, "UTF-8");
        this.zzbpJ = iZzHp + this.zzbpJ;
        return str;
    }

    public byte[] zzF(int i, int i2) {
        if (i2 == 0) {
            return zztn.zzbqi;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzbpG + i, bArr, 0, i2);
        return bArr;
    }

    public int zzHi() throws IOException {
        if (zzHv()) {
            this.zzbpK = 0;
            return 0;
        }
        this.zzbpK = zzHp();
        if (this.zzbpK == 0) {
            throw zztj.zzHF();
        }
        return this.zzbpK;
    }

    public void zzHj() throws IOException {
        int iZzHi;
        do {
            iZzHi = zzHi();
            if (iZzHi == 0) {
                return;
            }
        } while (zzml(iZzHi));
    }

    public long zzHk() throws IOException {
        return zzHq();
    }

    public int zzHl() throws IOException {
        return zzHp();
    }

    public boolean zzHm() throws IOException {
        return zzHp() != 0;
    }

    public long zzHo() throws IOException {
        return zzaa(zzHq());
    }

    public int zzHp() throws IOException {
        byte bZzHw = zzHw();
        if (bZzHw >= 0) {
            return bZzHw;
        }
        int i = bZzHw & 127;
        byte bZzHw2 = zzHw();
        if (bZzHw2 >= 0) {
            return i | (bZzHw2 << 7);
        }
        int i2 = i | ((bZzHw2 & 127) << 7);
        byte bZzHw3 = zzHw();
        if (bZzHw3 >= 0) {
            return i2 | (bZzHw3 << 14);
        }
        int i3 = i2 | ((bZzHw3 & 127) << 14);
        byte bZzHw4 = zzHw();
        if (bZzHw4 >= 0) {
            return i3 | (bZzHw4 << 21);
        }
        int i4 = i3 | ((bZzHw4 & 127) << 21);
        byte bZzHw5 = zzHw();
        int i5 = i4 | (bZzHw5 << 28);
        if (bZzHw5 >= 0) {
            return i5;
        }
        for (int i6 = 0; i6 < 5; i6++) {
            if (zzHw() >= 0) {
                return i5;
            }
        }
        throw zztj.zzHE();
    }

    public long zzHq() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            j |= (r3 & 127) << i;
            if ((zzHw() & 128) == 0) {
                return j;
            }
        }
        throw zztj.zzHE();
    }

    public int zzHr() throws IOException {
        return (zzHw() & Constants.UNKNOWN) | ((zzHw() & Constants.UNKNOWN) << 8) | ((zzHw() & Constants.UNKNOWN) << 16) | ((zzHw() & Constants.UNKNOWN) << 24);
    }

    public long zzHs() throws IOException {
        return ((zzHw() & 255) << 8) | (zzHw() & 255) | ((zzHw() & 255) << 16) | ((zzHw() & 255) << 24) | ((zzHw() & 255) << 32) | ((zzHw() & 255) << 40) | ((zzHw() & 255) << 48) | ((zzHw() & 255) << 56);
    }

    public int zzHu() {
        if (this.zzbpL == Integer.MAX_VALUE) {
            return -1;
        }
        return this.zzbpL - this.zzbpJ;
    }

    public boolean zzHv() {
        return this.zzbpJ == this.zzbpH;
    }

    public byte zzHw() throws IOException {
        if (this.zzbpJ == this.zzbpH) {
            throw zztj.zzHC();
        }
        byte[] bArr = this.buffer;
        int i = this.zzbpJ;
        this.zzbpJ = i + 1;
        return bArr[i];
    }

    public void zza(zztk zztkVar) throws IOException {
        int iZzHp = zzHp();
        if (this.zzbpM >= this.zzbpN) {
            throw zztj.zzHI();
        }
        int iZzmn = zzmn(iZzHp);
        this.zzbpM++;
        zztkVar.mergeFrom(this);
        zzmk(0);
        this.zzbpM--;
        zzmo(iZzmn);
    }

    public void zzmk(int i) throws zztj {
        if (this.zzbpK != i) {
            throw zztj.zzHG();
        }
    }

    public boolean zzml(int i) throws IOException {
        switch (zztn.zzmF(i)) {
            case 0:
                zzHl();
                return true;
            case 1:
                zzHs();
                return true;
            case 2:
                zzmr(zzHp());
                return true;
            case 3:
                zzHj();
                zzmk(zztn.zzL(zztn.zzmG(i), 4));
                return true;
            case 4:
                return false;
            case 5:
                zzHr();
                return true;
            default:
                throw zztj.zzHH();
        }
    }

    public int zzmn(int i) throws zztj {
        if (i < 0) {
            throw zztj.zzHD();
        }
        int i2 = this.zzbpJ + i;
        int i3 = this.zzbpL;
        if (i2 > i3) {
            throw zztj.zzHC();
        }
        this.zzbpL = i2;
        zzHt();
        return i3;
    }

    public void zzmo(int i) {
        this.zzbpL = i;
        zzHt();
    }

    public void zzmp(int i) {
        if (i > this.zzbpJ - this.zzbpG) {
            throw new IllegalArgumentException("Position " + i + " is beyond current " + (this.zzbpJ - this.zzbpG));
        }
        if (i < 0) {
            throw new IllegalArgumentException("Bad position " + i);
        }
        this.zzbpJ = this.zzbpG + i;
    }

    public byte[] zzmq(int i) throws IOException {
        if (i < 0) {
            throw zztj.zzHD();
        }
        if (this.zzbpJ + i > this.zzbpL) {
            zzmr(this.zzbpL - this.zzbpJ);
            throw zztj.zzHC();
        }
        if (i > this.zzbpH - this.zzbpJ) {
            throw zztj.zzHC();
        }
        byte[] bArr = new byte[i];
        System.arraycopy(this.buffer, this.zzbpJ, bArr, 0, i);
        this.zzbpJ += i;
        return bArr;
    }

    public void zzmr(int i) throws IOException {
        if (i < 0) {
            throw zztj.zzHD();
        }
        if (this.zzbpJ + i > this.zzbpL) {
            zzmr(this.zzbpL - this.zzbpJ);
            throw zztj.zzHC();
        }
        if (i > this.zzbpH - this.zzbpJ) {
            throw zztj.zzHC();
        }
        this.zzbpJ += i;
    }
}
