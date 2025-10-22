package oauth.signpost.signature;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import oauth.signpost.OAuth;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

/* loaded from: classes.dex */
public class HmacSha1MessageSigner extends OAuthMessageSigner {
    @Override // oauth.signpost.signature.OAuthMessageSigner
    public String getSignatureMethod() {
        return "HMAC-SHA1";
    }

    @Override // oauth.signpost.signature.OAuthMessageSigner
    public String sign(HttpRequest request, HttpParameters requestParams) throws NoSuchAlgorithmException, InvalidKeyException, OAuthMessageSignerException, UnsupportedEncodingException {
        try {
            String keyString = OAuth.percentEncode(getConsumerSecret()) + '&' + OAuth.percentEncode(getTokenSecret());
            byte[] keyBytes = keyString.getBytes("UTF-8");
            SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(key);
            String sbs = new SignatureBaseString(request, requestParams).generate();
            OAuth.debugOut("SBS", sbs);
            byte[] text = sbs.getBytes("UTF-8");
            return base64Encode(mac.doFinal(text)).trim();
        } catch (UnsupportedEncodingException e) {
            throw new OAuthMessageSignerException(e);
        } catch (GeneralSecurityException e2) {
            throw new OAuthMessageSignerException(e2);
        }
    }
}
