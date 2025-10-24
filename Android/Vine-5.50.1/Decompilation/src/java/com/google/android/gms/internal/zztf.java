package com.google.android.gms.internal;

import com.google.android.gms.internal.zzte;
import java.io.IOException;
import java.lang.reflect.Array;

/* loaded from: classes2.dex */
public class zztf<M extends zzte<M>, T> {
    public final int tag;
    protected final int type;
    protected final Class<T> zzbpR;
    protected final boolean zzbpS;

    int zzY(Object obj) {
        return this.zzbpS ? zzZ(obj) : zzaa(obj);
    }

    protected int zzZ(Object obj) {
        int iZzaa = 0;
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            if (Array.get(obj, i) != null) {
                iZzaa += zzaa(Array.get(obj, i));
            }
        }
        return iZzaa;
    }

    void zza(Object obj, zztd zztdVar) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (this.zzbpS) {
            zzc(obj, zztdVar);
        } else {
            zzb(obj, zztdVar);
        }
    }

    protected int zzaa(Object obj) {
        int iZzmG = zztn.zzmG(this.tag);
        switch (this.type) {
            case 10:
                return zztd.zzb(iZzmG, (zztk) obj);
            case 11:
                return zztd.zzc(iZzmG, (zztk) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }

    protected void zzb(Object obj, zztd zztdVar) {
        try {
            zztdVar.zzmy(this.tag);
            switch (this.type) {
                case 10:
                    int iZzmG = zztn.zzmG(this.tag);
                    zztdVar.zzb((zztk) obj);
                    zztdVar.zzK(iZzmG, 4);
                    return;
                case 11:
                    zztdVar.zzc((zztk) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected void zzc(Object obj, zztd zztdVar) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                zzb(obj2, zztdVar);
            }
        }
    }
}
