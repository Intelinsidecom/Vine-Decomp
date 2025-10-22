package oauth.signpost;

import com.google.gdata.util.common.base.PercentEscaper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import oauth.signpost.http.HttpParameters;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class OAuth {
    private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);

    public static String percentEncode(String s) {
        return s == null ? "" : percentEncoder.escape(s);
    }

    public static String percentDecode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException wow) {
            throw new RuntimeException(wow.getMessage(), wow);
        }
    }

    public static HttpParameters decodeForm(String form) {
        String name;
        String value;
        HttpParameters params = new HttpParameters();
        if (!isEmpty(form)) {
            for (String nvp : form.split("\\&")) {
                int equals = nvp.indexOf(61);
                if (equals < 0) {
                    name = percentDecode(nvp);
                    value = null;
                } else {
                    name = percentDecode(nvp.substring(0, equals));
                    value = percentDecode(nvp.substring(equals + 1));
                }
                params.put(name, value);
            }
        }
        return params;
    }

    public static HttpParameters decodeForm(InputStream content) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        StringBuilder sb = new StringBuilder();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            sb.append(line);
        }
        return decodeForm(sb.toString());
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static HttpParameters oauthHeaderToParamsMap(String oauthHeader) {
        HttpParameters params = new HttpParameters();
        if (oauthHeader != null && oauthHeader.startsWith("OAuth ")) {
            String[] elements = oauthHeader.substring("OAuth ".length()).split(",");
            for (String keyValuePair : elements) {
                String[] keyValue = keyValuePair.split("=");
                params.put(keyValue[0].trim(), keyValue[1].replace("\"", "").trim());
            }
        }
        return params;
    }

    public static void debugOut(String key, String value) {
        if (System.getProperty(PropertyConfiguration.DEBUG) != null) {
            System.out.println("[SIGNPOST] " + key + ": " + value);
        }
    }
}
