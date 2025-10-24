package com.digits.sdk.android;

import android.os.Bundle;

/* loaded from: classes.dex */
class BundleManager {
    static boolean assertContains(Bundle bundle, String... keys) {
        if (bundle == null || keys == null) {
            return false;
        }
        for (String key : keys) {
            if (!bundle.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
}
