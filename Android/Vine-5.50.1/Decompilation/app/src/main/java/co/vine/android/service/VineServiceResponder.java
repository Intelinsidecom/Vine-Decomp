package co.vine.android.service;

import android.os.Binder;
import android.os.Bundle;

/* loaded from: classes.dex */
public abstract class VineServiceResponder extends Binder {
    public abstract void onServiceResponse(int i, int i2, String str, Bundle bundle);
}
