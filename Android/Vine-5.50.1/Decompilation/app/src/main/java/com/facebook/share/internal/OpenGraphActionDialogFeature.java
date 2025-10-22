package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;

/* loaded from: classes2.dex */
public enum OpenGraphActionDialogFeature implements DialogFeature {
    OG_ACTION_DIALOG(20130618);

    private int minVersion;

    OpenGraphActionDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
