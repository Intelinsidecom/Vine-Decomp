package com.twitter.sdk.android.core.identity;

import android.os.Bundle;
import android.os.ResultReceiver;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

/* loaded from: classes2.dex */
class ShareEmailResultReceiver extends ResultReceiver {
    private final Callback<String> callback;

    @Override // android.os.ResultReceiver
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case -1:
                this.callback.success(new Result<>(resultData.getString("email"), null));
                return;
            case 0:
                this.callback.failure(new TwitterException(resultData.getString("msg")));
                return;
            case 1:
                this.callback.failure((TwitterException) resultData.getSerializable("error"));
                return;
            default:
                throw new IllegalArgumentException("Invalid result code " + resultCode);
        }
    }
}
