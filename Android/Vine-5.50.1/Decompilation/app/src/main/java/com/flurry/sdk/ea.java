package com.flurry.sdk;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.CRC32;

/* loaded from: classes.dex */
public class ea extends MessageDigest {
    private CRC32 a;

    public ea() {
        super("CRC");
        this.a = new CRC32();
    }

    @Override // java.security.MessageDigestSpi
    protected void engineReset() {
        this.a.reset();
    }

    @Override // java.security.MessageDigestSpi
    protected void engineUpdate(byte b) {
        this.a.update(b);
    }

    @Override // java.security.MessageDigestSpi
    protected void engineUpdate(byte[] bArr, int i, int i2) {
        this.a.update(bArr, i, i2);
    }

    @Override // java.security.MessageDigestSpi
    protected byte[] engineDigest() {
        long value = this.a.getValue();
        return new byte[]{(byte) (((-16777216) & value) >> 24), (byte) ((16711680 & value) >> 16), (byte) ((65280 & value) >> 8), (byte) ((value & 255) >> 0)};
    }

    public byte[] a() {
        return engineDigest();
    }

    public int b() {
        return ByteBuffer.wrap(engineDigest()).getInt();
    }
}
