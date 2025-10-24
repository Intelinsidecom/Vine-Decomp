package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;

/* loaded from: classes2.dex */
public enum ShareDialogFeature implements DialogFeature {
    SHARE_DIALOG(20130618),
    PHOTOS(20140204),
    VIDEO(20141028);

    private int minVersion;

    ShareDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return "com.facebook.platform.action.request.FEED_DIALOG";
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
