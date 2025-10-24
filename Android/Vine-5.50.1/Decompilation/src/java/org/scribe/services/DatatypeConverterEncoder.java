package org.scribe.services;

import javax.xml.bind.DatatypeConverter;

/* loaded from: classes.dex */
public class DatatypeConverterEncoder extends Base64Encoder {
    @Override // org.scribe.services.Base64Encoder
    public String encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }

    @Override // org.scribe.services.Base64Encoder
    public String getType() {
        return "DatatypeConverter";
    }
}
