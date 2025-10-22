package com.google.android.exoplayer.upstream;

/* loaded from: classes.dex */
public interface TransferListener {
    void onBytesTransferred(int i);

    void onTransferEnd();

    void onTransferStart();
}
