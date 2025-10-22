package co.vine.android.cache.text;

import co.vine.android.cache.UrlResource;
import java.io.IOException;

/* loaded from: classes.dex */
public class UrlText extends UrlResource<byte[]> {
    private final int size;

    public UrlText(String url) {
        super(url);
        this.size = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UrlText(String url, byte[] bArr) throws IOException {
        super(url);
        this.value = bArr;
        if (this.value != 0) {
            this.size = bArr.length;
        } else {
            this.size = 0;
        }
    }

    @Override // co.vine.android.cache.UrlResource
    public int size() {
        return this.size;
    }
}
