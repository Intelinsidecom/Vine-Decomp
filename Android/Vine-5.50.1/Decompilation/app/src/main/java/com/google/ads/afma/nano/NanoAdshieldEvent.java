package com.google.ads.afma.nano;

import com.google.android.gms.internal.zztc;
import com.google.android.gms.internal.zztd;
import com.google.android.gms.internal.zzti;
import com.google.android.gms.internal.zztj;
import com.google.android.gms.internal.zztk;
import com.google.android.gms.internal.zztn;
import java.io.IOException;

/* loaded from: classes.dex */
public interface NanoAdshieldEvent {

    public static final class AdShieldEvent extends zztk {
        private static volatile AdShieldEvent[] zzaK;
        public String appId;

        public AdShieldEvent() {
            clear();
        }

        public static AdShieldEvent[] emptyArray() {
            if (zzaK == null) {
                synchronized (zzti.zzbqa) {
                    if (zzaK == null) {
                        zzaK = new AdShieldEvent[0];
                    }
                }
            }
            return zzaK;
        }

        public static AdShieldEvent parseFrom(zztc input) throws IOException {
            return new AdShieldEvent().mergeFrom(input);
        }

        public static AdShieldEvent parseFrom(byte[] data) throws zztj {
            return (AdShieldEvent) zztk.mergeFrom(new AdShieldEvent(), data);
        }

        public AdShieldEvent clear() {
            this.appId = "";
            this.zzbqb = -1;
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        public AdShieldEvent mergeFrom(zztc input) throws IOException {
            while (true) {
                int iZzHi = input.zzHi();
                switch (iZzHi) {
                    case 0:
                        break;
                    case 10:
                        this.appId = input.readString();
                        break;
                    default:
                        if (!zztn.zzb(input, iZzHi)) {
                            break;
                        } else {
                            break;
                        }
                }
            }
            return this;
        }

        @Override // com.google.android.gms.internal.zztk
        public void writeTo(zztd output) throws IOException {
            if (!this.appId.equals("")) {
                output.zzb(1, this.appId);
            }
            super.writeTo(output);
        }

        @Override // com.google.android.gms.internal.zztk
        protected int zzz() {
            int iZzz = super.zzz();
            return !this.appId.equals("") ? iZzz + zztd.zzp(1, this.appId) : iZzz;
        }
    }
}
