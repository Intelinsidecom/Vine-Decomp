package co.vine.android.service;

import android.os.Bundle;

/* loaded from: classes.dex */
public class PendingAction {
    public final int actionCode;
    public final Bundle bundle;

    public PendingAction(int actionCode, Bundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle cannot be null.");
        }
        this.actionCode = actionCode;
        this.bundle = bundle;
    }
}
