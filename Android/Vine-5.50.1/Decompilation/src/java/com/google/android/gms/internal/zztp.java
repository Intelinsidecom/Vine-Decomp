package com.google.android.gms.internal;

import com.googlecode.javacv.cpp.avcodec;
import java.io.IOException;
import java.util.Arrays;

/* loaded from: classes2.dex */
public interface zztp {

    public static final class zza extends zzte<zza> {
        public String[] zzbqn;
        public String[] zzbqo;
        public int[] zzbqp;
        public long[] zzbqq;

        public zza() {
            zzHM();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) o;
            if (zzti.equals(this.zzbqn, zzaVar.zzbqn) && zzti.equals(this.zzbqo, zzaVar.zzbqo) && zzti.equals(this.zzbqp, zzaVar.zzbqp) && zzti.equals(this.zzbqq, zzaVar.zzbqq)) {
                return (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? zzaVar.zzbpQ == null || zzaVar.zzbpQ.isEmpty() : this.zzbpQ.equals(zzaVar.zzbpQ);
            }
            return false;
        }

        public int hashCode() {
            return ((this.zzbpQ == null || this.zzbpQ.isEmpty()) ? 0 : this.zzbpQ.hashCode()) + ((((((((((getClass().getName().hashCode() + 527) * 31) + zzti.hashCode(this.zzbqn)) * 31) + zzti.hashCode(this.zzbqo)) * 31) + zzti.hashCode(this.zzbqp)) * 31) + zzti.hashCode(this.zzbqq)) * 31);
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbqn != null && this.zzbqn.length > 0) {
                for (int i = 0; i < this.zzbqn.length; i++) {
                    String str = this.zzbqn[i];
                    if (str != null) {
                        output.zzb(1, str);
                    }
                }
            }
            if (this.zzbqo != null && this.zzbqo.length > 0) {
                for (int i2 = 0; i2 < this.zzbqo.length; i2++) {
                    String str2 = this.zzbqo[i2];
                    if (str2 != null) {
                        output.zzb(2, str2);
                    }
                }
            }
            if (this.zzbqp != null && this.zzbqp.length > 0) {
                for (int i3 = 0; i3 < this.zzbqp.length; i3++) {
                    output.zzG(3, this.zzbqp[i3]);
                }
            }
            if (this.zzbqq != null && this.zzbqq.length > 0) {
                for (int i4 = 0; i4 < this.zzbqq.length; i4++) {
                    output.zzb(4, this.zzbqq[i4]);
                }
            }
            super.writeTo(output);
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzH, reason: merged with bridge method [inline-methods] */
        public zza mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        int iZzc = zztn.zzc(zztcVar, 10);
                        int length = this.zzbqn == null ? 0 : this.zzbqn.length;
                        String[] strArr = new String[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzbqn, 0, strArr, 0, length);
                        }
                        while (length < strArr.length - 1) {
                            strArr[length] = zztcVar.readString();
                            zztcVar.zzHi();
                            length++;
                        }
                        strArr[length] = zztcVar.readString();
                        this.zzbqn = strArr;
                        break;
                    case 18:
                        int iZzc2 = zztn.zzc(zztcVar, 18);
                        int length2 = this.zzbqo == null ? 0 : this.zzbqo.length;
                        String[] strArr2 = new String[iZzc2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.zzbqo, 0, strArr2, 0, length2);
                        }
                        while (length2 < strArr2.length - 1) {
                            strArr2[length2] = zztcVar.readString();
                            zztcVar.zzHi();
                            length2++;
                        }
                        strArr2[length2] = zztcVar.readString();
                        this.zzbqo = strArr2;
                        break;
                    case 24:
                        int iZzc3 = zztn.zzc(zztcVar, 24);
                        int length3 = this.zzbqp == null ? 0 : this.zzbqp.length;
                        int[] iArr = new int[iZzc3 + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbqp, 0, iArr, 0, length3);
                        }
                        while (length3 < iArr.length - 1) {
                            iArr[length3] = zztcVar.zzHl();
                            zztcVar.zzHi();
                            length3++;
                        }
                        iArr[length3] = zztcVar.zzHl();
                        this.zzbqp = iArr;
                        break;
                    case 26:
                        int iZzmn = zztcVar.zzmn(zztcVar.zzHp());
                        int position = zztcVar.getPosition();
                        int i = 0;
                        while (zztcVar.zzHu() > 0) {
                            zztcVar.zzHl();
                            i++;
                        }
                        zztcVar.zzmp(position);
                        int length4 = this.zzbqp == null ? 0 : this.zzbqp.length;
                        int[] iArr2 = new int[i + length4];
                        if (length4 != 0) {
                            System.arraycopy(this.zzbqp, 0, iArr2, 0, length4);
                        }
                        while (length4 < iArr2.length) {
                            iArr2[length4] = zztcVar.zzHl();
                            length4++;
                        }
                        this.zzbqp = iArr2;
                        zztcVar.zzmo(iZzmn);
                        break;
                    case 32:
                        int iZzc4 = zztn.zzc(zztcVar, 32);
                        int length5 = this.zzbqq == null ? 0 : this.zzbqq.length;
                        long[] jArr = new long[iZzc4 + length5];
                        if (length5 != 0) {
                            System.arraycopy(this.zzbqq, 0, jArr, 0, length5);
                        }
                        while (length5 < jArr.length - 1) {
                            jArr[length5] = zztcVar.zzHk();
                            zztcVar.zzHi();
                            length5++;
                        }
                        jArr[length5] = zztcVar.zzHk();
                        this.zzbqq = jArr;
                        break;
                    case 34:
                        int iZzmn2 = zztcVar.zzmn(zztcVar.zzHp());
                        int position2 = zztcVar.getPosition();
                        int i2 = 0;
                        while (zztcVar.zzHu() > 0) {
                            zztcVar.zzHk();
                            i2++;
                        }
                        zztcVar.zzmp(position2);
                        int length6 = this.zzbqq == null ? 0 : this.zzbqq.length;
                        long[] jArr2 = new long[i2 + length6];
                        if (length6 != 0) {
                            System.arraycopy(this.zzbqq, 0, jArr2, 0, length6);
                        }
                        while (length6 < jArr2.length) {
                            jArr2[length6] = zztcVar.zzHk();
                            length6++;
                        }
                        this.zzbqq = jArr2;
                        zztcVar.zzmo(iZzmn2);
                        break;
                    default:
                        if (!zza(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        public zza zzHM() {
            this.zzbqn = zztn.zzbqg;
            this.zzbqo = zztn.zzbqg;
            this.zzbqp = zztn.zzboD;
            this.zzbqq = zztn.zzboC;
            this.zzbpQ = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        protected int zzz() {
            int length;
            int iZzz = super.zzz();
            if (this.zzbqn == null || this.zzbqn.length <= 0) {
                length = iZzz;
            } else {
                int iZzga = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.zzbqn.length; i2++) {
                    String str = this.zzbqn[i2];
                    if (str != null) {
                        i++;
                        iZzga += zztd.zzga(str);
                    }
                }
                length = iZzz + iZzga + (i * 1);
            }
            if (this.zzbqo != null && this.zzbqo.length > 0) {
                int iZzga2 = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < this.zzbqo.length; i4++) {
                    String str2 = this.zzbqo[i4];
                    if (str2 != null) {
                        i3++;
                        iZzga2 += zztd.zzga(str2);
                    }
                }
                length = length + iZzga2 + (i3 * 1);
            }
            if (this.zzbqp != null && this.zzbqp.length > 0) {
                int iZzmu = 0;
                for (int i5 = 0; i5 < this.zzbqp.length; i5++) {
                    iZzmu += zztd.zzmu(this.zzbqp[i5]);
                }
                length = length + iZzmu + (this.zzbqp.length * 1);
            }
            if (this.zzbqq == null || this.zzbqq.length <= 0) {
                return length;
            }
            int iZzad = 0;
            for (int i6 = 0; i6 < this.zzbqq.length; i6++) {
                iZzad += zztd.zzad(this.zzbqq[i6]);
            }
            return length + iZzad + (this.zzbqq.length * 1);
        }
    }

    public static final class zzb extends zzte<zzb> {
        public String version;
        public int zzbqr;
        public String zzbqs;

        public zzb() {
            zzHN();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb zzbVar = (zzb) o;
            if (this.zzbqr != zzbVar.zzbqr) {
                return false;
            }
            if (this.zzbqs == null) {
                if (zzbVar.zzbqs != null) {
                    return false;
                }
            } else if (!this.zzbqs.equals(zzbVar.zzbqs)) {
                return false;
            }
            if (this.version == null) {
                if (zzbVar.version != null) {
                    return false;
                }
            } else if (!this.version.equals(zzbVar.version)) {
                return false;
            }
            return (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? zzbVar.zzbpQ == null || zzbVar.zzbpQ.isEmpty() : this.zzbpQ.equals(zzbVar.zzbpQ);
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = ((this.version == null ? 0 : this.version.hashCode()) + (((this.zzbqs == null ? 0 : this.zzbqs.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + this.zzbqr) * 31)) * 31)) * 31;
            if (this.zzbpQ != null && !this.zzbpQ.isEmpty()) {
                iHashCode = this.zzbpQ.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbqr != 0) {
                output.zzG(1, this.zzbqr);
            }
            if (!this.zzbqs.equals("")) {
                output.zzb(2, this.zzbqs);
            }
            if (!this.version.equals("")) {
                output.zzb(3, this.version);
            }
            super.writeTo(output);
        }

        public zzb zzHN() {
            this.zzbqr = 0;
            this.zzbqs = "";
            this.version = "";
            this.zzbpQ = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzI, reason: merged with bridge method [inline-methods] */
        public zzb mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 8:
                        int iZzHl = zztcVar.zzHl();
                        switch (iZzHl) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                                this.zzbqr = iZzHl;
                                break;
                        }
                    case 18:
                        this.zzbqs = zztcVar.readString();
                        break;
                    case 26:
                        this.version = zztcVar.readString();
                        break;
                    default:
                        if (!zza(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzbqr != 0) {
                iZzz += zztd.zzI(1, this.zzbqr);
            }
            if (!this.zzbqs.equals("")) {
                iZzz += zztd.zzp(2, this.zzbqs);
            }
            return !this.version.equals("") ? iZzz + zztd.zzp(3, this.version) : iZzz;
        }
    }

    public static final class zzc extends zzte<zzc> {
        public byte[] zzbqt;
        public byte[][] zzbqu;
        public boolean zzbqv;

        public zzc() {
            zzHO();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzc)) {
                return false;
            }
            zzc zzcVar = (zzc) o;
            if (Arrays.equals(this.zzbqt, zzcVar.zzbqt) && zzti.zza(this.zzbqu, zzcVar.zzbqu) && this.zzbqv == zzcVar.zzbqv) {
                return (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? zzcVar.zzbpQ == null || zzcVar.zzbpQ.isEmpty() : this.zzbpQ.equals(zzcVar.zzbpQ);
            }
            return false;
        }

        public int hashCode() {
            return ((this.zzbpQ == null || this.zzbpQ.isEmpty()) ? 0 : this.zzbpQ.hashCode()) + (((this.zzbqv ? 1231 : 1237) + ((((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzbqt)) * 31) + zzti.zza(this.zzbqu)) * 31)) * 31);
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (!Arrays.equals(this.zzbqt, zztn.zzbqi)) {
                output.zza(1, this.zzbqt);
            }
            if (this.zzbqu != null && this.zzbqu.length > 0) {
                for (int i = 0; i < this.zzbqu.length; i++) {
                    byte[] bArr = this.zzbqu[i];
                    if (bArr != null) {
                        output.zza(2, bArr);
                    }
                }
            }
            if (this.zzbqv) {
                output.zzb(3, this.zzbqv);
            }
            super.writeTo(output);
        }

        public zzc zzHO() {
            this.zzbqt = zztn.zzbqi;
            this.zzbqu = zztn.zzbqh;
            this.zzbqv = false;
            this.zzbpQ = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzJ, reason: merged with bridge method [inline-methods] */
        public zzc mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        this.zzbqt = zztcVar.readBytes();
                        break;
                    case 18:
                        int iZzc = zztn.zzc(zztcVar, 18);
                        int length = this.zzbqu == null ? 0 : this.zzbqu.length;
                        byte[][] bArr = new byte[iZzc + length][];
                        if (length != 0) {
                            System.arraycopy(this.zzbqu, 0, bArr, 0, length);
                        }
                        while (length < bArr.length - 1) {
                            bArr[length] = zztcVar.readBytes();
                            zztcVar.zzHi();
                            length++;
                        }
                        bArr[length] = zztcVar.readBytes();
                        this.zzbqu = bArr;
                        break;
                    case 24:
                        this.zzbqv = zztcVar.zzHm();
                        break;
                    default:
                        if (!zza(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (!Arrays.equals(this.zzbqt, zztn.zzbqi)) {
                iZzz += zztd.zzb(1, this.zzbqt);
            }
            if (this.zzbqu != null && this.zzbqu.length > 0) {
                int iZzF = 0;
                int i = 0;
                for (int i2 = 0; i2 < this.zzbqu.length; i2++) {
                    byte[] bArr = this.zzbqu[i2];
                    if (bArr != null) {
                        i++;
                        iZzF += zztd.zzF(bArr);
                    }
                }
                iZzz = iZzz + iZzF + (i * 1);
            }
            return this.zzbqv ? iZzz + zztd.zzc(3, this.zzbqv) : iZzz;
        }
    }

    public static final class zzd extends zzte<zzd> {
        public String tag;
        public boolean zzbqA;
        public zze[] zzbqB;
        public zzb zzbqC;
        public byte[] zzbqD;
        public byte[] zzbqE;
        public byte[] zzbqF;
        public zza zzbqG;
        public String zzbqH;
        public long zzbqI;
        public zzc zzbqJ;
        public byte[] zzbqK;
        public int zzbqL;
        public int[] zzbqM;
        public long zzbqw;
        public long zzbqx;
        public long zzbqy;
        public int zzbqz;
        public int zznN;

        public zzd() {
            zzHP();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            zzd zzdVar = (zzd) o;
            if (this.zzbqw != zzdVar.zzbqw || this.zzbqx != zzdVar.zzbqx || this.zzbqy != zzdVar.zzbqy) {
                return false;
            }
            if (this.tag == null) {
                if (zzdVar.tag != null) {
                    return false;
                }
            } else if (!this.tag.equals(zzdVar.tag)) {
                return false;
            }
            if (this.zzbqz != zzdVar.zzbqz || this.zznN != zzdVar.zznN || this.zzbqA != zzdVar.zzbqA || !zzti.equals(this.zzbqB, zzdVar.zzbqB)) {
                return false;
            }
            if (this.zzbqC == null) {
                if (zzdVar.zzbqC != null) {
                    return false;
                }
            } else if (!this.zzbqC.equals(zzdVar.zzbqC)) {
                return false;
            }
            if (!Arrays.equals(this.zzbqD, zzdVar.zzbqD) || !Arrays.equals(this.zzbqE, zzdVar.zzbqE) || !Arrays.equals(this.zzbqF, zzdVar.zzbqF)) {
                return false;
            }
            if (this.zzbqG == null) {
                if (zzdVar.zzbqG != null) {
                    return false;
                }
            } else if (!this.zzbqG.equals(zzdVar.zzbqG)) {
                return false;
            }
            if (this.zzbqH == null) {
                if (zzdVar.zzbqH != null) {
                    return false;
                }
            } else if (!this.zzbqH.equals(zzdVar.zzbqH)) {
                return false;
            }
            if (this.zzbqI != zzdVar.zzbqI) {
                return false;
            }
            if (this.zzbqJ == null) {
                if (zzdVar.zzbqJ != null) {
                    return false;
                }
            } else if (!this.zzbqJ.equals(zzdVar.zzbqJ)) {
                return false;
            }
            if (Arrays.equals(this.zzbqK, zzdVar.zzbqK) && this.zzbqL == zzdVar.zzbqL && zzti.equals(this.zzbqM, zzdVar.zzbqM)) {
                return (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? zzdVar.zzbpQ == null || zzdVar.zzbpQ.isEmpty() : this.zzbpQ.equals(zzdVar.zzbpQ);
            }
            return false;
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = ((((((((this.zzbqJ == null ? 0 : this.zzbqJ.hashCode()) + (((((this.zzbqH == null ? 0 : this.zzbqH.hashCode()) + (((this.zzbqG == null ? 0 : this.zzbqG.hashCode()) + (((((((((this.zzbqC == null ? 0 : this.zzbqC.hashCode()) + (((((this.zzbqA ? 1231 : 1237) + (((((((this.tag == null ? 0 : this.tag.hashCode()) + ((((((((getClass().getName().hashCode() + 527) * 31) + ((int) (this.zzbqw ^ (this.zzbqw >>> 32)))) * 31) + ((int) (this.zzbqx ^ (this.zzbqx >>> 32)))) * 31) + ((int) (this.zzbqy ^ (this.zzbqy >>> 32)))) * 31)) * 31) + this.zzbqz) * 31) + this.zznN) * 31)) * 31) + zzti.hashCode(this.zzbqB)) * 31)) * 31) + Arrays.hashCode(this.zzbqD)) * 31) + Arrays.hashCode(this.zzbqE)) * 31) + Arrays.hashCode(this.zzbqF)) * 31)) * 31)) * 31) + ((int) (this.zzbqI ^ (this.zzbqI >>> 32)))) * 31)) * 31) + Arrays.hashCode(this.zzbqK)) * 31) + this.zzbqL) * 31) + zzti.hashCode(this.zzbqM)) * 31;
            if (this.zzbpQ != null && !this.zzbpQ.isEmpty()) {
                iHashCode = this.zzbpQ.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (this.zzbqw != 0) {
                output.zzb(1, this.zzbqw);
            }
            if (!this.tag.equals("")) {
                output.zzb(2, this.tag);
            }
            if (this.zzbqB != null && this.zzbqB.length > 0) {
                for (int i = 0; i < this.zzbqB.length; i++) {
                    zze zzeVar = this.zzbqB[i];
                    if (zzeVar != null) {
                        output.zza(3, zzeVar);
                    }
                }
            }
            if (!Arrays.equals(this.zzbqD, zztn.zzbqi)) {
                output.zza(6, this.zzbqD);
            }
            if (this.zzbqG != null) {
                output.zza(7, this.zzbqG);
            }
            if (!Arrays.equals(this.zzbqE, zztn.zzbqi)) {
                output.zza(8, this.zzbqE);
            }
            if (this.zzbqC != null) {
                output.zza(9, this.zzbqC);
            }
            if (this.zzbqA) {
                output.zzb(10, this.zzbqA);
            }
            if (this.zzbqz != 0) {
                output.zzG(11, this.zzbqz);
            }
            if (this.zznN != 0) {
                output.zzG(12, this.zznN);
            }
            if (!Arrays.equals(this.zzbqF, zztn.zzbqi)) {
                output.zza(13, this.zzbqF);
            }
            if (!this.zzbqH.equals("")) {
                output.zzb(14, this.zzbqH);
            }
            if (this.zzbqI != 180000) {
                output.zzc(15, this.zzbqI);
            }
            if (this.zzbqJ != null) {
                output.zza(16, this.zzbqJ);
            }
            if (this.zzbqx != 0) {
                output.zzb(17, this.zzbqx);
            }
            if (!Arrays.equals(this.zzbqK, zztn.zzbqi)) {
                output.zza(18, this.zzbqK);
            }
            if (this.zzbqL != 0) {
                output.zzG(19, this.zzbqL);
            }
            if (this.zzbqM != null && this.zzbqM.length > 0) {
                for (int i2 = 0; i2 < this.zzbqM.length; i2++) {
                    output.zzG(20, this.zzbqM[i2]);
                }
            }
            if (this.zzbqy != 0) {
                output.zzb(21, this.zzbqy);
            }
            super.writeTo(output);
        }

        public zzd zzHP() {
            this.zzbqw = 0L;
            this.zzbqx = 0L;
            this.zzbqy = 0L;
            this.tag = "";
            this.zzbqz = 0;
            this.zznN = 0;
            this.zzbqA = false;
            this.zzbqB = zze.zzHQ();
            this.zzbqC = null;
            this.zzbqD = zztn.zzbqi;
            this.zzbqE = zztn.zzbqi;
            this.zzbqF = zztn.zzbqi;
            this.zzbqG = null;
            this.zzbqH = "";
            this.zzbqI = 180000L;
            this.zzbqJ = null;
            this.zzbqK = zztn.zzbqi;
            this.zzbqL = 0;
            this.zzbqM = zztn.zzboD;
            this.zzbpQ = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzK, reason: merged with bridge method [inline-methods] */
        public zzd mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 8:
                        this.zzbqw = zztcVar.zzHk();
                        break;
                    case 18:
                        this.tag = zztcVar.readString();
                        break;
                    case 26:
                        int iZzc = zztn.zzc(zztcVar, 26);
                        int length = this.zzbqB == null ? 0 : this.zzbqB.length;
                        zze[] zzeVarArr = new zze[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzbqB, 0, zzeVarArr, 0, length);
                        }
                        while (length < zzeVarArr.length - 1) {
                            zzeVarArr[length] = new zze();
                            zztcVar.zza(zzeVarArr[length]);
                            zztcVar.zzHi();
                            length++;
                        }
                        zzeVarArr[length] = new zze();
                        zztcVar.zza(zzeVarArr[length]);
                        this.zzbqB = zzeVarArr;
                        break;
                    case 50:
                        this.zzbqD = zztcVar.readBytes();
                        break;
                    case 58:
                        if (this.zzbqG == null) {
                            this.zzbqG = new zza();
                        }
                        zztcVar.zza(this.zzbqG);
                        break;
                    case 66:
                        this.zzbqE = zztcVar.readBytes();
                        break;
                    case 74:
                        if (this.zzbqC == null) {
                            this.zzbqC = new zzb();
                        }
                        zztcVar.zza(this.zzbqC);
                        break;
                    case 80:
                        this.zzbqA = zztcVar.zzHm();
                        break;
                    case 88:
                        this.zzbqz = zztcVar.zzHl();
                        break;
                    case 96:
                        this.zznN = zztcVar.zzHl();
                        break;
                    case 106:
                        this.zzbqF = zztcVar.readBytes();
                        break;
                    case avcodec.AV_CODEC_ID_MIMIC /* 114 */:
                        this.zzbqH = zztcVar.readString();
                        break;
                    case 120:
                        this.zzbqI = zztcVar.zzHo();
                        break;
                    case 130:
                        if (this.zzbqJ == null) {
                            this.zzbqJ = new zzc();
                        }
                        zztcVar.zza(this.zzbqJ);
                        break;
                    case avcodec.AV_CODEC_ID_BINKVIDEO /* 136 */:
                        this.zzbqx = zztcVar.zzHk();
                        break;
                    case avcodec.AV_CODEC_ID_R10K /* 146 */:
                        this.zzbqK = zztcVar.readBytes();
                        break;
                    case avcodec.AV_CODEC_ID_WMV3IMAGE /* 152 */:
                        int iZzHl = zztcVar.zzHl();
                        switch (iZzHl) {
                            case 0:
                            case 1:
                            case 2:
                                this.zzbqL = iZzHl;
                                break;
                        }
                    case avcodec.AV_CODEC_ID_CDXL /* 160 */:
                        int iZzc2 = zztn.zzc(zztcVar, avcodec.AV_CODEC_ID_CDXL);
                        int length2 = this.zzbqM == null ? 0 : this.zzbqM.length;
                        int[] iArr = new int[iZzc2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.zzbqM, 0, iArr, 0, length2);
                        }
                        while (length2 < iArr.length - 1) {
                            iArr[length2] = zztcVar.zzHl();
                            zztcVar.zzHi();
                            length2++;
                        }
                        iArr[length2] = zztcVar.zzHl();
                        this.zzbqM = iArr;
                        break;
                    case avcodec.AV_CODEC_ID_ZEROCODEC /* 162 */:
                        int iZzmn = zztcVar.zzmn(zztcVar.zzHp());
                        int position = zztcVar.getPosition();
                        int i = 0;
                        while (zztcVar.zzHu() > 0) {
                            zztcVar.zzHl();
                            i++;
                        }
                        zztcVar.zzmp(position);
                        int length3 = this.zzbqM == null ? 0 : this.zzbqM.length;
                        int[] iArr2 = new int[i + length3];
                        if (length3 != 0) {
                            System.arraycopy(this.zzbqM, 0, iArr2, 0, length3);
                        }
                        while (length3 < iArr2.length) {
                            iArr2[length3] = zztcVar.zzHl();
                            length3++;
                        }
                        this.zzbqM = iArr2;
                        zztcVar.zzmo(iZzmn);
                        break;
                    case avcodec.AV_CODEC_ID_MSS2 /* 168 */:
                        this.zzbqy = zztcVar.zzHk();
                        break;
                    default:
                        if (!zza(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzbqw != 0) {
                iZzz += zztd.zzd(1, this.zzbqw);
            }
            if (!this.tag.equals("")) {
                iZzz += zztd.zzp(2, this.tag);
            }
            if (this.zzbqB != null && this.zzbqB.length > 0) {
                int iZzc = iZzz;
                for (int i = 0; i < this.zzbqB.length; i++) {
                    zze zzeVar = this.zzbqB[i];
                    if (zzeVar != null) {
                        iZzc += zztd.zzc(3, zzeVar);
                    }
                }
                iZzz = iZzc;
            }
            if (!Arrays.equals(this.zzbqD, zztn.zzbqi)) {
                iZzz += zztd.zzb(6, this.zzbqD);
            }
            if (this.zzbqG != null) {
                iZzz += zztd.zzc(7, this.zzbqG);
            }
            if (!Arrays.equals(this.zzbqE, zztn.zzbqi)) {
                iZzz += zztd.zzb(8, this.zzbqE);
            }
            if (this.zzbqC != null) {
                iZzz += zztd.zzc(9, this.zzbqC);
            }
            if (this.zzbqA) {
                iZzz += zztd.zzc(10, this.zzbqA);
            }
            if (this.zzbqz != 0) {
                iZzz += zztd.zzI(11, this.zzbqz);
            }
            if (this.zznN != 0) {
                iZzz += zztd.zzI(12, this.zznN);
            }
            if (!Arrays.equals(this.zzbqF, zztn.zzbqi)) {
                iZzz += zztd.zzb(13, this.zzbqF);
            }
            if (!this.zzbqH.equals("")) {
                iZzz += zztd.zzp(14, this.zzbqH);
            }
            if (this.zzbqI != 180000) {
                iZzz += zztd.zze(15, this.zzbqI);
            }
            if (this.zzbqJ != null) {
                iZzz += zztd.zzc(16, this.zzbqJ);
            }
            if (this.zzbqx != 0) {
                iZzz += zztd.zzd(17, this.zzbqx);
            }
            if (!Arrays.equals(this.zzbqK, zztn.zzbqi)) {
                iZzz += zztd.zzb(18, this.zzbqK);
            }
            if (this.zzbqL != 0) {
                iZzz += zztd.zzI(19, this.zzbqL);
            }
            if (this.zzbqM != null && this.zzbqM.length > 0) {
                int iZzmu = 0;
                for (int i2 = 0; i2 < this.zzbqM.length; i2++) {
                    iZzmu += zztd.zzmu(this.zzbqM[i2]);
                }
                iZzz = iZzz + iZzmu + (this.zzbqM.length * 2);
            }
            return this.zzbqy != 0 ? iZzz + zztd.zzd(21, this.zzbqy) : iZzz;
        }
    }

    public static final class zze extends zzte<zze> {
        private static volatile zze[] zzbqN;
        public String key;
        public String value;

        public zze() {
            zzHR();
        }

        public static zze[] zzHQ() {
            if (zzbqN == null) {
                synchronized (zzti.zzbqa) {
                    if (zzbqN == null) {
                        zzbqN = new zze[0];
                    }
                }
            }
            return zzbqN;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            zze zzeVar = (zze) o;
            if (this.key == null) {
                if (zzeVar.key != null) {
                    return false;
                }
            } else if (!this.key.equals(zzeVar.key)) {
                return false;
            }
            if (this.value == null) {
                if (zzeVar.value != null) {
                    return false;
                }
            } else if (!this.value.equals(zzeVar.value)) {
                return false;
            }
            return (this.zzbpQ == null || this.zzbpQ.isEmpty()) ? zzeVar.zzbpQ == null || zzeVar.zzbpQ.isEmpty() : this.zzbpQ.equals(zzeVar.zzbpQ);
        }

        public int hashCode() {
            int iHashCode = 0;
            int iHashCode2 = ((this.value == null ? 0 : this.value.hashCode()) + (((this.key == null ? 0 : this.key.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
            if (this.zzbpQ != null && !this.zzbpQ.isEmpty()) {
                iHashCode = this.zzbpQ.hashCode();
            }
            return iHashCode2 + iHashCode;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
            if (!this.key.equals("")) {
                output.zzb(1, this.key);
            }
            if (!this.value.equals("")) {
                output.zzb(2, this.value);
            }
            super.writeTo(output);
        }

        public zze zzHR() {
            this.key = "";
            this.value = "";
            this.zzbpQ = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzL, reason: merged with bridge method [inline-methods] */
        public zze mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        this.key = zztcVar.readString();
                        break;
                    case 18:
                        this.value = zztcVar.readString();
                        break;
                    default:
                        if (!zza(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zzte, com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (!this.key.equals("")) {
                iZzz += zztd.zzp(1, this.key);
            }
            return !this.value.equals("") ? iZzz + zztd.zzp(2, this.value) : iZzz;
        }
    }
}
