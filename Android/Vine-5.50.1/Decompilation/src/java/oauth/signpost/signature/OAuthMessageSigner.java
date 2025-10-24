package oauth.signpost.signature;

import android.util.Base64;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

/* loaded from: classes.dex */
public abstract class OAuthMessageSigner implements Serializable {
    private static final long serialVersionUID = 4445779788786131202L;
    private String consumerSecret;
    private String tokenSecret;

    public abstract String getSignatureMethod();

    public abstract String sign(HttpRequest httpRequest, HttpParameters httpParameters) throws OAuthMessageSignerException;

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    protected String base64Encode(byte[] b) {
        return new String(Base64.encode(b, 0));
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
    }
}
