package com.google.android.gms.measurement.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* loaded from: classes.dex */
class zzq extends BroadcastReceiver {
    static final String zzRF = zzq.class.getName();
    private boolean zzRG;
    private boolean zzRH;
    private final zzt zzaQX;

    zzq(zzt zztVar) {
        com.google.android.gms.common.internal.zzx.zzy(zztVar);
        this.zzaQX = zztVar;
    }

    private Context getContext() {
        return this.zzaQX.getContext();
    }

    private zzo zzzz() {
        return this.zzaQX.zzzz();
    }

    public boolean isRegistered() {
        this.zzaQX.zziS();
        return this.zzRG;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws IllegalStateException {
        this.zzaQX.zzje();
        String action = intent.getAction();
        zzzz().zzBr().zzj("NetworkBroadcastReceiver received action", action);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            zzzz().zzBm().zzj("NetworkBroadcastReceiver received unknown action", action);
            return;
        }
        final boolean zZzlk = this.zzaQX.zzBE().zzlk();
        if (this.zzRH != zZzlk) {
            this.zzRH = zZzlk;
            this.zzaQX.zzAV().zzg(new Runnable() { // from class: com.google.android.gms.measurement.internal.zzq.1
                @Override // java.lang.Runnable
                public void run() {
                    zzq.this.zzaQX.zzJ(zZzlk);
                }
            });
        }
    }

    public void unregister() {
        this.zzaQX.zzje();
        this.zzaQX.zziS();
        if (isRegistered()) {
            zzzz().zzBr().zzez("Unregistering connectivity change receiver");
            this.zzRG = false;
            this.zzRH = false;
            try {
                getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                zzzz().zzBl().zzj("Failed to unregister the network broadcast receiver", e);
            }
        }
    }

    public void zzlh() {
        this.zzaQX.zzje();
        this.zzaQX.zziS();
        if (this.zzRG) {
            return;
        }
        getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.zzRH = this.zzaQX.zzBE().zzlk();
        zzzz().zzBr().zzj("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzRH));
        this.zzRG = true;
    }
}
