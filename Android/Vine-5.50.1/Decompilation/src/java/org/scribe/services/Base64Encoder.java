package org.scribe.services;

/* loaded from: classes.dex */
public abstract class Base64Encoder {
    private static Base64Encoder instance;

    public abstract String encode(byte[] bArr);

    public abstract String getType();

    public static synchronized Base64Encoder getInstance() {
        if (instance == null) {
            instance = createEncoderInstance();
        }
        return instance;
    }

    private static Base64Encoder createEncoderInstance() {
        return CommonsEncoder.isPresent() ? new CommonsEncoder() : new DatatypeConverterEncoder();
    }

    public static String type() {
        return getInstance().getType();
    }
}
