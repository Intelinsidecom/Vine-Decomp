package com.google.android.gms.common.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public final class zzw {

    public static final class zza {
        private final Object zzLx;
        private final List<String> zzaky;

        private zza(Object obj) {
            this.zzLx = zzx.zzy(obj);
            this.zzaky = new ArrayList();
        }

        public String toString() {
            StringBuilder sbAppend = new StringBuilder(100).append(this.zzLx.getClass().getSimpleName()).append('{');
            int size = this.zzaky.size();
            for (int i = 0; i < size; i++) {
                sbAppend.append(this.zzaky.get(i));
                if (i < size - 1) {
                    sbAppend.append(", ");
                }
            }
            return sbAppend.append('}').toString();
        }

        public zza zzg(String str, Object obj) {
            this.zzaky.add(((String) zzx.zzy(str)) + "=" + String.valueOf(obj));
            return this;
        }
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static zza zzx(Object obj) {
        return new zza(obj);
    }
}
