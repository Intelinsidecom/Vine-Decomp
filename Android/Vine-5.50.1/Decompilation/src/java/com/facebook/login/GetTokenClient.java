package com.facebook.login;

import android.content.Context;
import android.os.Bundle;
import com.facebook.internal.PlatformServiceClient;
import com.googlecode.javacv.cpp.avcodec;

/* loaded from: classes2.dex */
final class GetTokenClient extends PlatformServiceClient {
    GetTokenClient(Context context, String applicationId) {
        super(context, 65536, avcodec.AV_CODEC_ID_PCM_S16BE, 20121101, applicationId);
    }

    @Override // com.facebook.internal.PlatformServiceClient
    protected void populateRequestBundle(Bundle data) {
    }
}
