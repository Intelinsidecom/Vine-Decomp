package org.scribe.services;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.scribe.exceptions.OAuthSignatureException;

/* loaded from: classes.dex */
public class CommonsEncoder extends Base64Encoder {
    @Override // org.scribe.services.Base64Encoder
    public String encode(byte[] bytes) {
        try {
            return new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new OAuthSignatureException("Can't perform base64 encoding", e);
        }
    }

    @Override // org.scribe.services.Base64Encoder
    public String getType() {
        return "CommonsCodec";
    }

    public static boolean isPresent() throws ClassNotFoundException {
        try {
            Class.forName("org.apache.commons.codec.binary.Base64");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
