package retrofit.client;

import java.io.IOException;

/* loaded from: classes.dex */
public interface Client {

    public interface Provider {
        Client get();
    }

    Response execute(Request request) throws IOException;
}
