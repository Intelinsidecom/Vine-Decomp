package com.twitter.sdk.android.core.identity;

import android.app.Activity;
import io.fabric.sdk.android.Fabric;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes2.dex */
class AuthState {
    final AtomicReference<AuthHandler> authHandlerRef = new AtomicReference<>(null);

    AuthState() {
    }

    public boolean beginAuthorize(Activity activity, AuthHandler authHandler) {
        boolean result = false;
        if (isAuthorizeInProgress()) {
            Fabric.getLogger().w("Twitter", "Authorize already in progress");
        } else if (authHandler.authorize(activity) && !(result = this.authHandlerRef.compareAndSet(null, authHandler))) {
            Fabric.getLogger().w("Twitter", "Failed to update authHandler, authorize already in progress.");
        }
        return result;
    }

    public boolean isAuthorizeInProgress() {
        return this.authHandlerRef.get() != null;
    }
}
