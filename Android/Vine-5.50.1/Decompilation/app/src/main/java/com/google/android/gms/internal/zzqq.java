package com.google.android.gms.internal;

import com.googlecode.javacv.cpp.avcodec;
import java.io.IOException;

/* loaded from: classes.dex */
public interface zzqq {

    public static final class zza extends zztk {
        private static volatile zza[] zzaVj;
        public Integer count;
        public String name;
        public zzb[] zzaVk;
        public Long zzaVl;
        public Long zzaVm;

        public zza() {
            zzCc();
        }

        public static zza[] zzCb() {
            if (zzaVj == null) {
                synchronized (zzti.zzbqa) {
                    if (zzaVj == null) {
                        zzaVj = new zza[0];
                    }
                }
            }
            return zzaVj;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zza)) {
                return false;
            }
            zza zzaVar = (zza) o;
            if (!zzti.equals(this.zzaVk, zzaVar.zzaVk)) {
                return false;
            }
            if (this.name == null) {
                if (zzaVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzaVar.name)) {
                return false;
            }
            if (this.zzaVl == null) {
                if (zzaVar.zzaVl != null) {
                    return false;
                }
            } else if (!this.zzaVl.equals(zzaVar.zzaVl)) {
                return false;
            }
            if (this.zzaVm == null) {
                if (zzaVar.zzaVm != null) {
                    return false;
                }
            } else if (!this.zzaVm.equals(zzaVar.zzaVm)) {
                return false;
            }
            return this.count == null ? zzaVar.count == null : this.count.equals(zzaVar.count);
        }

        public int hashCode() {
            return (((this.zzaVm == null ? 0 : this.zzaVm.hashCode()) + (((this.zzaVl == null ? 0 : this.zzaVl.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((((getClass().getName().hashCode() + 527) * 31) + zzti.hashCode(this.zzaVk)) * 31)) * 31)) * 31)) * 31) + (this.count != null ? this.count.hashCode() : 0);
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (this.zzaVk != null && this.zzaVk.length > 0) {
                for (int i = 0; i < this.zzaVk.length; i++) {
                    zzb zzbVar = this.zzaVk[i];
                    if (zzbVar != null) {
                        output.zza(1, zzbVar);
                    }
                }
            }
            if (this.name != null) {
                output.zzb(2, this.name);
            }
            if (this.zzaVl != null) {
                output.zzb(3, this.zzaVl.longValue());
            }
            if (this.zzaVm != null) {
                output.zzb(4, this.zzaVm.longValue());
            }
            if (this.count != null) {
                output.zzG(5, this.count.intValue());
            }
            super.writeTo(output);
        }

        public zza zzCc() {
            this.zzaVk = zzb.zzCd();
            this.name = null;
            this.zzaVl = null;
            this.zzaVm = null;
            this.count = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzt, reason: merged with bridge method [inline-methods] */
        public zza mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        int iZzc = zztn.zzc(zztcVar, 10);
                        int length = this.zzaVk == null ? 0 : this.zzaVk.length;
                        zzb[] zzbVarArr = new zzb[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzaVk, 0, zzbVarArr, 0, length);
                        }
                        while (length < zzbVarArr.length - 1) {
                            zzbVarArr[length] = new zzb();
                            zztcVar.zza(zzbVarArr[length]);
                            zztcVar.zzHi();
                            length++;
                        }
                        zzbVarArr[length] = new zzb();
                        zztcVar.zza(zzbVarArr[length]);
                        this.zzaVk = zzbVarArr;
                        break;
                    case 18:
                        this.name = zztcVar.readString();
                        break;
                    case 24:
                        this.zzaVl = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 32:
                        this.zzaVm = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 40:
                        this.count = Integer.valueOf(zztcVar.zzHl());
                        break;
                    default:
                        if (!zztn.zzb(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzaVk != null && this.zzaVk.length > 0) {
                for (int i = 0; i < this.zzaVk.length; i++) {
                    zzb zzbVar = this.zzaVk[i];
                    if (zzbVar != null) {
                        iZzz += zztd.zzc(1, zzbVar);
                    }
                }
            }
            if (this.name != null) {
                iZzz += zztd.zzp(2, this.name);
            }
            if (this.zzaVl != null) {
                iZzz += zztd.zzd(3, this.zzaVl.longValue());
            }
            if (this.zzaVm != null) {
                iZzz += zztd.zzd(4, this.zzaVm.longValue());
            }
            return this.count != null ? iZzz + zztd.zzI(5, this.count.intValue()) : iZzz;
        }
    }

    public static final class zzb extends zztk {
        private static volatile zzb[] zzaVn;
        public String name;
        public Float zzaVi;
        public Long zzaVo;
        public String zzakS;

        public zzb() {
            zzCe();
        }

        public static zzb[] zzCd() {
            if (zzaVn == null) {
                synchronized (zzti.zzbqa) {
                    if (zzaVn == null) {
                        zzaVn = new zzb[0];
                    }
                }
            }
            return zzaVn;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzb)) {
                return false;
            }
            zzb zzbVar = (zzb) o;
            if (this.name == null) {
                if (zzbVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzbVar.name)) {
                return false;
            }
            if (this.zzakS == null) {
                if (zzbVar.zzakS != null) {
                    return false;
                }
            } else if (!this.zzakS.equals(zzbVar.zzakS)) {
                return false;
            }
            if (this.zzaVo == null) {
                if (zzbVar.zzaVo != null) {
                    return false;
                }
            } else if (!this.zzaVo.equals(zzbVar.zzaVo)) {
                return false;
            }
            return this.zzaVi == null ? zzbVar.zzaVi == null : this.zzaVi.equals(zzbVar.zzaVi);
        }

        public int hashCode() {
            return (((this.zzaVo == null ? 0 : this.zzaVo.hashCode()) + (((this.zzakS == null ? 0 : this.zzakS.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + (this.zzaVi != null ? this.zzaVi.hashCode() : 0);
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (this.name != null) {
                output.zzb(1, this.name);
            }
            if (this.zzakS != null) {
                output.zzb(2, this.zzakS);
            }
            if (this.zzaVo != null) {
                output.zzb(3, this.zzaVo.longValue());
            }
            if (this.zzaVi != null) {
                output.zzb(4, this.zzaVi.floatValue());
            }
            super.writeTo(output);
        }

        public zzb zzCe() {
            this.name = null;
            this.zzakS = null;
            this.zzaVo = null;
            this.zzaVi = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzu, reason: merged with bridge method [inline-methods] */
        public zzb mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        this.name = zztcVar.readString();
                        break;
                    case 18:
                        this.zzakS = zztcVar.readString();
                        break;
                    case 24:
                        this.zzaVo = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 37:
                        this.zzaVi = Float.valueOf(zztcVar.readFloat());
                        break;
                    default:
                        if (!zztn.zzb(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.name != null) {
                iZzz += zztd.zzp(1, this.name);
            }
            if (this.zzakS != null) {
                iZzz += zztd.zzp(2, this.zzakS);
            }
            if (this.zzaVo != null) {
                iZzz += zztd.zzd(3, this.zzaVo.longValue());
            }
            return this.zzaVi != null ? iZzz + zztd.zzc(4, this.zzaVi.floatValue()) : iZzz;
        }
    }

    public static final class zzc extends zztk {
        public zzd[] zzaVp;

        public zzc() {
            zzCf();
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return (o instanceof zzc) && zzti.equals(this.zzaVp, ((zzc) o).zzaVp);
        }

        public int hashCode() {
            return ((getClass().getName().hashCode() + 527) * 31) + zzti.hashCode(this.zzaVp);
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (this.zzaVp != null && this.zzaVp.length > 0) {
                for (int i = 0; i < this.zzaVp.length; i++) {
                    zzd zzdVar = this.zzaVp[i];
                    if (zzdVar != null) {
                        output.zza(1, zzdVar);
                    }
                }
            }
            super.writeTo(output);
        }

        public zzc zzCf() {
            this.zzaVp = zzd.zzCg();
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzv, reason: merged with bridge method [inline-methods] */
        public zzc mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        int iZzc = zztn.zzc(zztcVar, 10);
                        int length = this.zzaVp == null ? 0 : this.zzaVp.length;
                        zzd[] zzdVarArr = new zzd[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzaVp, 0, zzdVarArr, 0, length);
                        }
                        while (length < zzdVarArr.length - 1) {
                            zzdVarArr[length] = new zzd();
                            zztcVar.zza(zzdVarArr[length]);
                            zztcVar.zzHi();
                            length++;
                        }
                        zzdVarArr[length] = new zzd();
                        zztcVar.zza(zzdVarArr[length]);
                        this.zzaVp = zzdVarArr;
                        break;
                    default:
                        if (!zztn.zzb(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzaVp != null && this.zzaVp.length > 0) {
                for (int i = 0; i < this.zzaVp.length; i++) {
                    zzd zzdVar = this.zzaVp[i];
                    if (zzdVar != null) {
                        iZzz += zztd.zzc(1, zzdVar);
                    }
                }
            }
            return iZzz;
        }
    }

    public static final class zzd extends zztk {
        private static volatile zzd[] zzaVq;
        public String appId;
        public String osVersion;
        public String zzaKi;
        public String zzaSn;
        public String zzaSo;
        public String zzaSr;
        public String zzaVA;
        public String zzaVB;
        public Integer zzaVC;
        public Long zzaVD;
        public Long zzaVE;
        public String zzaVF;
        public Boolean zzaVG;
        public String zzaVH;
        public Long zzaVI;
        public Integer zzaVJ;
        public Boolean zzaVK;
        public Integer zzaVr;
        public zza[] zzaVs;
        public zze[] zzaVt;
        public Long zzaVu;
        public Long zzaVv;
        public Long zzaVw;
        public Long zzaVx;
        public Long zzaVy;
        public String zzaVz;

        public zzd() {
            zzCh();
        }

        public static zzd[] zzCg() {
            if (zzaVq == null) {
                synchronized (zzti.zzbqa) {
                    if (zzaVq == null) {
                        zzaVq = new zzd[0];
                    }
                }
            }
            return zzaVq;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zzd)) {
                return false;
            }
            zzd zzdVar = (zzd) o;
            if (this.zzaVr == null) {
                if (zzdVar.zzaVr != null) {
                    return false;
                }
            } else if (!this.zzaVr.equals(zzdVar.zzaVr)) {
                return false;
            }
            if (zzti.equals(this.zzaVs, zzdVar.zzaVs) && zzti.equals(this.zzaVt, zzdVar.zzaVt)) {
                if (this.zzaVu == null) {
                    if (zzdVar.zzaVu != null) {
                        return false;
                    }
                } else if (!this.zzaVu.equals(zzdVar.zzaVu)) {
                    return false;
                }
                if (this.zzaVv == null) {
                    if (zzdVar.zzaVv != null) {
                        return false;
                    }
                } else if (!this.zzaVv.equals(zzdVar.zzaVv)) {
                    return false;
                }
                if (this.zzaVw == null) {
                    if (zzdVar.zzaVw != null) {
                        return false;
                    }
                } else if (!this.zzaVw.equals(zzdVar.zzaVw)) {
                    return false;
                }
                if (this.zzaVx == null) {
                    if (zzdVar.zzaVx != null) {
                        return false;
                    }
                } else if (!this.zzaVx.equals(zzdVar.zzaVx)) {
                    return false;
                }
                if (this.zzaVy == null) {
                    if (zzdVar.zzaVy != null) {
                        return false;
                    }
                } else if (!this.zzaVy.equals(zzdVar.zzaVy)) {
                    return false;
                }
                if (this.zzaVz == null) {
                    if (zzdVar.zzaVz != null) {
                        return false;
                    }
                } else if (!this.zzaVz.equals(zzdVar.zzaVz)) {
                    return false;
                }
                if (this.osVersion == null) {
                    if (zzdVar.osVersion != null) {
                        return false;
                    }
                } else if (!this.osVersion.equals(zzdVar.osVersion)) {
                    return false;
                }
                if (this.zzaVA == null) {
                    if (zzdVar.zzaVA != null) {
                        return false;
                    }
                } else if (!this.zzaVA.equals(zzdVar.zzaVA)) {
                    return false;
                }
                if (this.zzaVB == null) {
                    if (zzdVar.zzaVB != null) {
                        return false;
                    }
                } else if (!this.zzaVB.equals(zzdVar.zzaVB)) {
                    return false;
                }
                if (this.zzaVC == null) {
                    if (zzdVar.zzaVC != null) {
                        return false;
                    }
                } else if (!this.zzaVC.equals(zzdVar.zzaVC)) {
                    return false;
                }
                if (this.zzaSo == null) {
                    if (zzdVar.zzaSo != null) {
                        return false;
                    }
                } else if (!this.zzaSo.equals(zzdVar.zzaSo)) {
                    return false;
                }
                if (this.appId == null) {
                    if (zzdVar.appId != null) {
                        return false;
                    }
                } else if (!this.appId.equals(zzdVar.appId)) {
                    return false;
                }
                if (this.zzaKi == null) {
                    if (zzdVar.zzaKi != null) {
                        return false;
                    }
                } else if (!this.zzaKi.equals(zzdVar.zzaKi)) {
                    return false;
                }
                if (this.zzaVD == null) {
                    if (zzdVar.zzaVD != null) {
                        return false;
                    }
                } else if (!this.zzaVD.equals(zzdVar.zzaVD)) {
                    return false;
                }
                if (this.zzaVE == null) {
                    if (zzdVar.zzaVE != null) {
                        return false;
                    }
                } else if (!this.zzaVE.equals(zzdVar.zzaVE)) {
                    return false;
                }
                if (this.zzaVF == null) {
                    if (zzdVar.zzaVF != null) {
                        return false;
                    }
                } else if (!this.zzaVF.equals(zzdVar.zzaVF)) {
                    return false;
                }
                if (this.zzaVG == null) {
                    if (zzdVar.zzaVG != null) {
                        return false;
                    }
                } else if (!this.zzaVG.equals(zzdVar.zzaVG)) {
                    return false;
                }
                if (this.zzaVH == null) {
                    if (zzdVar.zzaVH != null) {
                        return false;
                    }
                } else if (!this.zzaVH.equals(zzdVar.zzaVH)) {
                    return false;
                }
                if (this.zzaVI == null) {
                    if (zzdVar.zzaVI != null) {
                        return false;
                    }
                } else if (!this.zzaVI.equals(zzdVar.zzaVI)) {
                    return false;
                }
                if (this.zzaVJ == null) {
                    if (zzdVar.zzaVJ != null) {
                        return false;
                    }
                } else if (!this.zzaVJ.equals(zzdVar.zzaVJ)) {
                    return false;
                }
                if (this.zzaSr == null) {
                    if (zzdVar.zzaSr != null) {
                        return false;
                    }
                } else if (!this.zzaSr.equals(zzdVar.zzaSr)) {
                    return false;
                }
                if (this.zzaSn == null) {
                    if (zzdVar.zzaSn != null) {
                        return false;
                    }
                } else if (!this.zzaSn.equals(zzdVar.zzaSn)) {
                    return false;
                }
                return this.zzaVK == null ? zzdVar.zzaVK == null : this.zzaVK.equals(zzdVar.zzaVK);
            }
            return false;
        }

        public int hashCode() {
            return (((this.zzaSn == null ? 0 : this.zzaSn.hashCode()) + (((this.zzaSr == null ? 0 : this.zzaSr.hashCode()) + (((this.zzaVJ == null ? 0 : this.zzaVJ.hashCode()) + (((this.zzaVI == null ? 0 : this.zzaVI.hashCode()) + (((this.zzaVH == null ? 0 : this.zzaVH.hashCode()) + (((this.zzaVG == null ? 0 : this.zzaVG.hashCode()) + (((this.zzaVF == null ? 0 : this.zzaVF.hashCode()) + (((this.zzaVE == null ? 0 : this.zzaVE.hashCode()) + (((this.zzaVD == null ? 0 : this.zzaVD.hashCode()) + (((this.zzaKi == null ? 0 : this.zzaKi.hashCode()) + (((this.appId == null ? 0 : this.appId.hashCode()) + (((this.zzaSo == null ? 0 : this.zzaSo.hashCode()) + (((this.zzaVC == null ? 0 : this.zzaVC.hashCode()) + (((this.zzaVB == null ? 0 : this.zzaVB.hashCode()) + (((this.zzaVA == null ? 0 : this.zzaVA.hashCode()) + (((this.osVersion == null ? 0 : this.osVersion.hashCode()) + (((this.zzaVz == null ? 0 : this.zzaVz.hashCode()) + (((this.zzaVy == null ? 0 : this.zzaVy.hashCode()) + (((this.zzaVx == null ? 0 : this.zzaVx.hashCode()) + (((this.zzaVw == null ? 0 : this.zzaVw.hashCode()) + (((this.zzaVv == null ? 0 : this.zzaVv.hashCode()) + (((this.zzaVu == null ? 0 : this.zzaVu.hashCode()) + (((((((this.zzaVr == null ? 0 : this.zzaVr.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzti.hashCode(this.zzaVs)) * 31) + zzti.hashCode(this.zzaVt)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + (this.zzaVK != null ? this.zzaVK.hashCode() : 0);
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (this.zzaVr != null) {
                output.zzG(1, this.zzaVr.intValue());
            }
            if (this.zzaVs != null && this.zzaVs.length > 0) {
                for (int i = 0; i < this.zzaVs.length; i++) {
                    zza zzaVar = this.zzaVs[i];
                    if (zzaVar != null) {
                        output.zza(2, zzaVar);
                    }
                }
            }
            if (this.zzaVt != null && this.zzaVt.length > 0) {
                for (int i2 = 0; i2 < this.zzaVt.length; i2++) {
                    zze zzeVar = this.zzaVt[i2];
                    if (zzeVar != null) {
                        output.zza(3, zzeVar);
                    }
                }
            }
            if (this.zzaVu != null) {
                output.zzb(4, this.zzaVu.longValue());
            }
            if (this.zzaVv != null) {
                output.zzb(5, this.zzaVv.longValue());
            }
            if (this.zzaVw != null) {
                output.zzb(6, this.zzaVw.longValue());
            }
            if (this.zzaVy != null) {
                output.zzb(7, this.zzaVy.longValue());
            }
            if (this.zzaVz != null) {
                output.zzb(8, this.zzaVz);
            }
            if (this.osVersion != null) {
                output.zzb(9, this.osVersion);
            }
            if (this.zzaVA != null) {
                output.zzb(10, this.zzaVA);
            }
            if (this.zzaVB != null) {
                output.zzb(11, this.zzaVB);
            }
            if (this.zzaVC != null) {
                output.zzG(12, this.zzaVC.intValue());
            }
            if (this.zzaSo != null) {
                output.zzb(13, this.zzaSo);
            }
            if (this.appId != null) {
                output.zzb(14, this.appId);
            }
            if (this.zzaKi != null) {
                output.zzb(16, this.zzaKi);
            }
            if (this.zzaVD != null) {
                output.zzb(17, this.zzaVD.longValue());
            }
            if (this.zzaVE != null) {
                output.zzb(18, this.zzaVE.longValue());
            }
            if (this.zzaVF != null) {
                output.zzb(19, this.zzaVF);
            }
            if (this.zzaVG != null) {
                output.zzb(20, this.zzaVG.booleanValue());
            }
            if (this.zzaVH != null) {
                output.zzb(21, this.zzaVH);
            }
            if (this.zzaVI != null) {
                output.zzb(22, this.zzaVI.longValue());
            }
            if (this.zzaVJ != null) {
                output.zzG(23, this.zzaVJ.intValue());
            }
            if (this.zzaSr != null) {
                output.zzb(24, this.zzaSr);
            }
            if (this.zzaSn != null) {
                output.zzb(25, this.zzaSn);
            }
            if (this.zzaVx != null) {
                output.zzb(26, this.zzaVx.longValue());
            }
            if (this.zzaVK != null) {
                output.zzb(28, this.zzaVK.booleanValue());
            }
            super.writeTo(output);
        }

        public zzd zzCh() {
            this.zzaVr = null;
            this.zzaVs = zza.zzCb();
            this.zzaVt = zze.zzCi();
            this.zzaVu = null;
            this.zzaVv = null;
            this.zzaVw = null;
            this.zzaVx = null;
            this.zzaVy = null;
            this.zzaVz = null;
            this.osVersion = null;
            this.zzaVA = null;
            this.zzaVB = null;
            this.zzaVC = null;
            this.zzaSo = null;
            this.appId = null;
            this.zzaKi = null;
            this.zzaVD = null;
            this.zzaVE = null;
            this.zzaVF = null;
            this.zzaVG = null;
            this.zzaVH = null;
            this.zzaVI = null;
            this.zzaVJ = null;
            this.zzaSr = null;
            this.zzaSn = null;
            this.zzaVK = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzw, reason: merged with bridge method [inline-methods] */
        public zzd mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 8:
                        this.zzaVr = Integer.valueOf(zztcVar.zzHl());
                        break;
                    case 18:
                        int iZzc = zztn.zzc(zztcVar, 18);
                        int length = this.zzaVs == null ? 0 : this.zzaVs.length;
                        zza[] zzaVarArr = new zza[iZzc + length];
                        if (length != 0) {
                            System.arraycopy(this.zzaVs, 0, zzaVarArr, 0, length);
                        }
                        while (length < zzaVarArr.length - 1) {
                            zzaVarArr[length] = new zza();
                            zztcVar.zza(zzaVarArr[length]);
                            zztcVar.zzHi();
                            length++;
                        }
                        zzaVarArr[length] = new zza();
                        zztcVar.zza(zzaVarArr[length]);
                        this.zzaVs = zzaVarArr;
                        break;
                    case 26:
                        int iZzc2 = zztn.zzc(zztcVar, 26);
                        int length2 = this.zzaVt == null ? 0 : this.zzaVt.length;
                        zze[] zzeVarArr = new zze[iZzc2 + length2];
                        if (length2 != 0) {
                            System.arraycopy(this.zzaVt, 0, zzeVarArr, 0, length2);
                        }
                        while (length2 < zzeVarArr.length - 1) {
                            zzeVarArr[length2] = new zze();
                            zztcVar.zza(zzeVarArr[length2]);
                            zztcVar.zzHi();
                            length2++;
                        }
                        zzeVarArr[length2] = new zze();
                        zztcVar.zza(zzeVarArr[length2]);
                        this.zzaVt = zzeVarArr;
                        break;
                    case 32:
                        this.zzaVu = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 40:
                        this.zzaVv = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 48:
                        this.zzaVw = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 56:
                        this.zzaVy = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 66:
                        this.zzaVz = zztcVar.readString();
                        break;
                    case 74:
                        this.osVersion = zztcVar.readString();
                        break;
                    case 82:
                        this.zzaVA = zztcVar.readString();
                        break;
                    case 90:
                        this.zzaVB = zztcVar.readString();
                        break;
                    case 96:
                        this.zzaVC = Integer.valueOf(zztcVar.zzHl());
                        break;
                    case 106:
                        this.zzaSo = zztcVar.readString();
                        break;
                    case avcodec.AV_CODEC_ID_MIMIC /* 114 */:
                        this.appId = zztcVar.readString();
                        break;
                    case 130:
                        this.zzaKi = zztcVar.readString();
                        break;
                    case avcodec.AV_CODEC_ID_BINKVIDEO /* 136 */:
                        this.zzaVD = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 144:
                        this.zzaVE = Long.valueOf(zztcVar.zzHk());
                        break;
                    case avcodec.AV_CODEC_ID_UTVIDEO /* 154 */:
                        this.zzaVF = zztcVar.readString();
                        break;
                    case avcodec.AV_CODEC_ID_CDXL /* 160 */:
                        this.zzaVG = Boolean.valueOf(zztcVar.zzHm());
                        break;
                    case avcodec.AV_CODEC_ID_AIC /* 170 */:
                        this.zzaVH = zztcVar.readString();
                        break;
                    case 176:
                        this.zzaVI = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 184:
                        this.zzaVJ = Integer.valueOf(zztcVar.zzHl());
                        break;
                    case 194:
                        this.zzaSr = zztcVar.readString();
                        break;
                    case 202:
                        this.zzaSn = zztcVar.readString();
                        break;
                    case 208:
                        this.zzaVx = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 224:
                        this.zzaVK = Boolean.valueOf(zztcVar.zzHm());
                        break;
                    default:
                        if (!zztn.zzb(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzaVr != null) {
                iZzz += zztd.zzI(1, this.zzaVr.intValue());
            }
            if (this.zzaVs != null && this.zzaVs.length > 0) {
                int iZzc = iZzz;
                for (int i = 0; i < this.zzaVs.length; i++) {
                    zza zzaVar = this.zzaVs[i];
                    if (zzaVar != null) {
                        iZzc += zztd.zzc(2, zzaVar);
                    }
                }
                iZzz = iZzc;
            }
            if (this.zzaVt != null && this.zzaVt.length > 0) {
                for (int i2 = 0; i2 < this.zzaVt.length; i2++) {
                    zze zzeVar = this.zzaVt[i2];
                    if (zzeVar != null) {
                        iZzz += zztd.zzc(3, zzeVar);
                    }
                }
            }
            if (this.zzaVu != null) {
                iZzz += zztd.zzd(4, this.zzaVu.longValue());
            }
            if (this.zzaVv != null) {
                iZzz += zztd.zzd(5, this.zzaVv.longValue());
            }
            if (this.zzaVw != null) {
                iZzz += zztd.zzd(6, this.zzaVw.longValue());
            }
            if (this.zzaVy != null) {
                iZzz += zztd.zzd(7, this.zzaVy.longValue());
            }
            if (this.zzaVz != null) {
                iZzz += zztd.zzp(8, this.zzaVz);
            }
            if (this.osVersion != null) {
                iZzz += zztd.zzp(9, this.osVersion);
            }
            if (this.zzaVA != null) {
                iZzz += zztd.zzp(10, this.zzaVA);
            }
            if (this.zzaVB != null) {
                iZzz += zztd.zzp(11, this.zzaVB);
            }
            if (this.zzaVC != null) {
                iZzz += zztd.zzI(12, this.zzaVC.intValue());
            }
            if (this.zzaSo != null) {
                iZzz += zztd.zzp(13, this.zzaSo);
            }
            if (this.appId != null) {
                iZzz += zztd.zzp(14, this.appId);
            }
            if (this.zzaKi != null) {
                iZzz += zztd.zzp(16, this.zzaKi);
            }
            if (this.zzaVD != null) {
                iZzz += zztd.zzd(17, this.zzaVD.longValue());
            }
            if (this.zzaVE != null) {
                iZzz += zztd.zzd(18, this.zzaVE.longValue());
            }
            if (this.zzaVF != null) {
                iZzz += zztd.zzp(19, this.zzaVF);
            }
            if (this.zzaVG != null) {
                iZzz += zztd.zzc(20, this.zzaVG.booleanValue());
            }
            if (this.zzaVH != null) {
                iZzz += zztd.zzp(21, this.zzaVH);
            }
            if (this.zzaVI != null) {
                iZzz += zztd.zzd(22, this.zzaVI.longValue());
            }
            if (this.zzaVJ != null) {
                iZzz += zztd.zzI(23, this.zzaVJ.intValue());
            }
            if (this.zzaSr != null) {
                iZzz += zztd.zzp(24, this.zzaSr);
            }
            if (this.zzaSn != null) {
                iZzz += zztd.zzp(25, this.zzaSn);
            }
            if (this.zzaVx != null) {
                iZzz += zztd.zzd(26, this.zzaVx.longValue());
            }
            return this.zzaVK != null ? iZzz + zztd.zzc(28, this.zzaVK.booleanValue()) : iZzz;
        }
    }

    public static final class zze extends zztk {
        private static volatile zze[] zzaVL;
        public String name;
        public Long zzaVM;
        public Float zzaVi;
        public Long zzaVo;
        public String zzakS;

        public zze() {
            zzCj();
        }

        public static zze[] zzCi() {
            if (zzaVL == null) {
                synchronized (zzti.zzbqa) {
                    if (zzaVL == null) {
                        zzaVL = new zze[0];
                    }
                }
            }
            return zzaVL;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof zze)) {
                return false;
            }
            zze zzeVar = (zze) o;
            if (this.zzaVM == null) {
                if (zzeVar.zzaVM != null) {
                    return false;
                }
            } else if (!this.zzaVM.equals(zzeVar.zzaVM)) {
                return false;
            }
            if (this.name == null) {
                if (zzeVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzeVar.name)) {
                return false;
            }
            if (this.zzakS == null) {
                if (zzeVar.zzakS != null) {
                    return false;
                }
            } else if (!this.zzakS.equals(zzeVar.zzakS)) {
                return false;
            }
            if (this.zzaVo == null) {
                if (zzeVar.zzaVo != null) {
                    return false;
                }
            } else if (!this.zzaVo.equals(zzeVar.zzaVo)) {
                return false;
            }
            return this.zzaVi == null ? zzeVar.zzaVi == null : this.zzaVi.equals(zzeVar.zzaVi);
        }

        public int hashCode() {
            return (((this.zzaVo == null ? 0 : this.zzaVo.hashCode()) + (((this.zzakS == null ? 0 : this.zzakS.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + (((this.zzaVM == null ? 0 : this.zzaVM.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31) + (this.zzaVi != null ? this.zzaVi.hashCode() : 0);
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (this.zzaVM != null) {
                output.zzb(1, this.zzaVM.longValue());
            }
            if (this.name != null) {
                output.zzb(2, this.name);
            }
            if (this.zzakS != null) {
                output.zzb(3, this.zzakS);
            }
            if (this.zzaVo != null) {
                output.zzb(4, this.zzaVo.longValue());
            }
            if (this.zzaVi != null) {
                output.zzb(5, this.zzaVi.floatValue());
            }
            super.writeTo(output);
        }

        public zze zzCj() {
            this.zzaVM = null;
            this.name = null;
            this.zzakS = null;
            this.zzaVo = null;
            this.zzaVi = null;
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        /* renamed from: zzx, reason: merged with bridge method [inline-methods] */
        public zze mergeFrom(zztc zztcVar) throws IOException {
            while (true) {
                int iZzHi = zztcVar.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 8:
                        this.zzaVM = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 18:
                        this.name = zztcVar.readString();
                        break;
                    case 26:
                        this.zzakS = zztcVar.readString();
                        break;
                    case 32:
                        this.zzaVo = Long.valueOf(zztcVar.zzHk());
                        break;
                    case 45:
                        this.zzaVi = Float.valueOf(zztcVar.readFloat());
                        break;
                    default:
                        if (!zztn.zzb(zztcVar, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            if (this.zzaVM != null) {
                iZzz += zztd.zzd(1, this.zzaVM.longValue());
            }
            if (this.name != null) {
                iZzz += zztd.zzp(2, this.name);
            }
            if (this.zzakS != null) {
                iZzz += zztd.zzp(3, this.zzakS);
            }
            if (this.zzaVo != null) {
                iZzz += zztd.zzd(4, this.zzaVo.longValue());
            }
            return this.zzaVi != null ? iZzz + zztd.zzc(5, this.zzaVi.floatValue()) : iZzz;
        }
    }
}
