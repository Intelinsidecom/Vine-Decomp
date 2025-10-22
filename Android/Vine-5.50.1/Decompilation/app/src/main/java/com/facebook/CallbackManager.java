package com.facebook;

import android.content.Intent;
import com.facebook.internal.CallbackManagerImpl;

/* loaded from: classes2.dex */
public interface CallbackManager {
    boolean onActivityResult(int i, int i2, Intent intent);

    public static class Factory {
        public static CallbackManager create() {
            return new CallbackManagerImpl();
        }
    }
}
