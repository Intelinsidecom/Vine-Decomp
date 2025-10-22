package com.google.android.gms.ads.mediation;

import android.os.Bundle;

/* loaded from: classes.dex */
public interface MediationAdapter {

    public static class zza {
        private int zzMT;

        public zza zzS(int i) {
            this.zzMT = i;
            return this;
        }

        public Bundle zzie() {
            Bundle bundle = new Bundle();
            bundle.putInt("capabilities", this.zzMT);
            return bundle;
        }
    }

    void onDestroy();

    void onPause();

    void onResume();
}
