package co.vine.android.network;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface NetworkOperationReader {
    void onHandleError(HttpResult httpResult);

    void readInput(int i, long j, InputStream inputStream) throws IOException;
}
