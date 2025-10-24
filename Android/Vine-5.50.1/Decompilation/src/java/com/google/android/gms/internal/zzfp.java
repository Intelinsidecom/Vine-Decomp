package com.google.android.gms.internal;

import org.json.JSONException;
import org.json.JSONObject;

@zzha
/* loaded from: classes.dex */
public class zzfp {
    private final boolean zzCu;
    private final boolean zzCv;
    private final boolean zzCw;
    private final boolean zzCx;
    private final boolean zzCy;

    public static final class zza {
        private boolean zzCu;
        private boolean zzCv;
        private boolean zzCw;
        private boolean zzCx;
        private boolean zzCy;

        public zzfp zzeE() {
            return new zzfp(this);
        }

        public zza zzq(boolean z) {
            this.zzCu = z;
            return this;
        }

        public zza zzr(boolean z) {
            this.zzCv = z;
            return this;
        }

        public zza zzs(boolean z) {
            this.zzCw = z;
            return this;
        }

        public zza zzt(boolean z) {
            this.zzCx = z;
            return this;
        }

        public zza zzu(boolean z) {
            this.zzCy = z;
            return this;
        }
    }

    private zzfp(zza zzaVar) {
        this.zzCu = zzaVar.zzCu;
        this.zzCv = zzaVar.zzCv;
        this.zzCw = zzaVar.zzCw;
        this.zzCx = zzaVar.zzCx;
        this.zzCy = zzaVar.zzCy;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject().put("sms", this.zzCu).put("tel", this.zzCv).put("calendar", this.zzCw).put("storePicture", this.zzCx).put("inlineVideo", this.zzCy);
        } catch (JSONException e) {
            com.google.android.gms.ads.internal.util.client.zzb.zzb("Error occured while obtaining the MRAID capabilities.", e);
            return null;
        }
    }
}
