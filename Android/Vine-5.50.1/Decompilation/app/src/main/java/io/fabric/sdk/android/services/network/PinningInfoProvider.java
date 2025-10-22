package io.fabric.sdk.android.services.network;

import java.io.InputStream;

/* loaded from: classes.dex */
public interface PinningInfoProvider {
    String getKeyStorePassword();

    InputStream getKeyStoreStream();

    long getPinCreationTimeInMillis();

    String[] getPins();
}
