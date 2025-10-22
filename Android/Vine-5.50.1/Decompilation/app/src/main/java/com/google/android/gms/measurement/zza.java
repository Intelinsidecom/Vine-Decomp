package com.google.android.gms.measurement;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.google.android.gms.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzx;

/* loaded from: classes.dex */
public final class zza {
    private static volatile zza zzaRg;
    private final String zzaRd;
    private final Status zzaRe;
    private final boolean zzaRf;

    zza(Context context) throws Resources.NotFoundException {
        boolean z = true;
        Resources resources = context.getResources();
        String resourcePackageName = resources.getResourcePackageName(R.string.common_google_play_services_unknown_issue);
        int identifier = resources.getIdentifier("google_app_measurement_enable", "integer", resourcePackageName);
        if (identifier != 0 && resources.getInteger(identifier) == 0) {
            z = false;
        }
        this.zzaRf = z;
        int identifier2 = resources.getIdentifier("google_app_id", "string", resourcePackageName);
        if (identifier2 == 0) {
            if (this.zzaRf) {
                this.zzaRe = new Status(10, "Missing an expected resource: 'R.string.google_app_id' for initializing Google services.  Possible causes are missing google-services.json or com.google.gms.google-services gradle plugin.");
            } else {
                this.zzaRe = Status.zzaeX;
            }
            this.zzaRd = null;
            return;
        }
        String string = resources.getString(identifier2);
        if (!TextUtils.isEmpty(string)) {
            this.zzaRd = string;
            this.zzaRe = Status.zzaeX;
        } else {
            if (this.zzaRf) {
                this.zzaRe = new Status(10, "The resource 'R.string.google_app_id' is invalid, expected an app  identifier and found: '" + string + "'.");
            } else {
                this.zzaRe = Status.zzaeX;
            }
            this.zzaRd = null;
        }
    }

    public static Status zzaS(Context context) {
        zzx.zzb(context, "Context must not be null.");
        if (zzaRg == null) {
            synchronized (zza.class) {
                if (zzaRg == null) {
                    zzaRg = new zza(context);
                }
            }
        }
        return zzaRg.zzaRe;
    }

    public static String zzzA() {
        if (zzaRg == null) {
            synchronized (zza.class) {
                if (zzaRg == null) {
                    throw new IllegalStateException("Initialize must be called before getGoogleAppId.");
                }
            }
        }
        return zzaRg.zzzB();
    }

    public static boolean zzzC() {
        if (zzaRg == null) {
            synchronized (zza.class) {
                if (zzaRg == null) {
                    throw new IllegalStateException("Initialize must be called before isMeasurementEnabled.");
                }
            }
        }
        return zzaRg.zzzD();
    }

    String zzzB() {
        if (this.zzaRe.isSuccess()) {
            return this.zzaRd;
        }
        throw new IllegalStateException("Initialize must be successful before calling getGoogleAppId.  The result of the previous call to initialize was: '" + this.zzaRe + "'.");
    }

    boolean zzzD() {
        if (this.zzaRe.isSuccess()) {
            return this.zzaRf;
        }
        throw new IllegalStateException("Initialize must be successful before calling isMeasurementEnabledInternal.  The result of the previous call to initialize was: '" + this.zzaRe + "'.");
    }
}
