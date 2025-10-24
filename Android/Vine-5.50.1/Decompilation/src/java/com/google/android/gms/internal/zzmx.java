package com.google.android.gms.internal;

import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;

/* loaded from: classes2.dex */
public final class zzmx extends LruCache<zza, Drawable> {

    public static final class zza {
        public final int zzaiP;
        public final int zzaiQ;

        public zza(int i, int i2) {
            this.zzaiP = i;
            this.zzaiQ = i2;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof zza)) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            zza zzaVar = (zza) obj;
            return zzaVar.zzaiP == this.zzaiP && zzaVar.zzaiQ == this.zzaiQ;
        }

        public int hashCode() {
            return com.google.android.gms.common.internal.zzw.hashCode(Integer.valueOf(this.zzaiP), Integer.valueOf(this.zzaiQ));
        }
    }
}
