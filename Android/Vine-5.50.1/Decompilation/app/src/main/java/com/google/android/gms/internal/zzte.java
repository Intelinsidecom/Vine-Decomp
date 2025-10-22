package com.google.android.gms.internal;

import com.google.android.gms.internal.zzte;
import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class zzte<M extends zzte<M>> extends zztk {
    protected zztg zzbpQ;

    @Override // com.google.android.gms.internal.zztk
    public void writeTo(zztd output) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbpQ == null) {
            return;
        }
        for (int i = 0; i < this.zzbpQ.size(); i++) {
            this.zzbpQ.zzmD(i).writeTo(output);
        }
    }

    @Override // com.google.android.gms.internal.zztk
    /* renamed from: zzHz, reason: merged with bridge method [inline-methods] */
    public M mo4clone() throws CloneNotSupportedException {
        M m = (M) super.mo4clone();
        zzti.zza(this, m);
        return m;
    }

    protected final boolean zza(zztc zztcVar, int i) throws IOException {
        int position = zztcVar.getPosition();
        if (!zztcVar.zzml(i)) {
            return false;
        }
        int iZzmG = zztn.zzmG(i);
        zztm zztmVar = new zztm(i, zztcVar.zzF(position, zztcVar.getPosition() - position));
        zzth zzthVarZzmC = null;
        if (this.zzbpQ == null) {
            this.zzbpQ = new zztg();
        } else {
            zzthVarZzmC = this.zzbpQ.zzmC(iZzmG);
        }
        if (zzthVarZzmC == null) {
            zzthVarZzmC = new zzth();
            this.zzbpQ.zza(iZzmG, zzthVarZzmC);
        }
        zzthVarZzmC.zza(zztmVar);
        return true;
    }

    @Override // com.google.android.gms.internal.zztk
    protected int zzz() {
        if (this.zzbpQ == null) {
            return 0;
        }
        int iZzz = 0;
        for (int i = 0; i < this.zzbpQ.size(); i++) {
            iZzz += this.zzbpQ.zzmD(i).zzz();
        }
        return iZzz;
    }
}
