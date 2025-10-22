package com.facebook.share.internal;

import com.facebook.internal.DialogFeature;

/* loaded from: classes2.dex */
public enum LikeDialogFeature implements DialogFeature {
    LIKE_DIALOG(20140701);

    private int minVersion;

    LikeDialogFeature(int minVersion) {
        this.minVersion = minVersion;
    }

    @Override // com.facebook.internal.DialogFeature
    public String getAction() {
        return "com.facebook.platform.action.request.LIKE_DIALOG";
    }

    @Override // com.facebook.internal.DialogFeature
    public int getMinVersion() {
        return this.minVersion;
    }
}
