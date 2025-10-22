package com.facebook.share.internal;

import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.PlatformServiceClient;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes2.dex */
final class LikeStatusClient extends PlatformServiceClient {
    private String objectId;

    LikeStatusClient(Context context, String applicationId, String objectId) {
        super(context, avcodec.AV_CODEC_ID_PCM_MULAW, avcodec.AV_CODEC_ID_PCM_ALAW, 20141001, applicationId);
        this.objectId = objectId;
    }

    @Override // com.facebook.internal.PlatformServiceClient
    protected void populateRequestBundle(Bundle data) {
        data.putString("com.facebook.platform.extra.OBJECT_ID", this.objectId);
    }
}
