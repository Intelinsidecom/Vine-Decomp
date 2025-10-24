package com.flurry.sdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class ev implements eu<String> {
    @Override // com.flurry.sdk.eu
    public void a(OutputStream outputStream, String str) throws IOException {
        if (outputStream != null && str != null) {
            byte[] bytes = str.getBytes("utf-8");
            outputStream.write(bytes, 0, bytes.length);
        }
    }

    @Override // com.flurry.sdk.eu
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public String a(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        fb.a(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }
}
