package co.vine.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import co.vine.android.VineAccountAuthenticator;

/* loaded from: classes.dex */
public class VineAuthenticationService extends Service {
    private VineAccountAuthenticator mAuthenticator;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.mAuthenticator = new VineAccountAuthenticator(this);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mAuthenticator = null;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mAuthenticator.getIBinder();
    }
}
