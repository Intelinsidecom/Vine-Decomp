package com.google.android.gms.internal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

/* loaded from: classes.dex */
public class zzz implements zzy {
    private final zza zzaE;
    private final SSLSocketFactory zzaF;

    public interface zza {
        String zzh(String str);
    }

    public zzz() {
        this(null);
    }

    public zzz(zza zzaVar) {
        this(zzaVar, null);
    }

    public zzz(zza zzaVar, SSLSocketFactory sSLSocketFactory) {
        this.zzaE = zzaVar;
        this.zzaF = sSLSocketFactory;
    }

    private HttpURLConnection zza(URL url, zzk<?> zzkVar) throws IOException {
        HttpURLConnection httpURLConnectionZza = zza(url);
        int iZzt = zzkVar.zzt();
        httpURLConnectionZza.setConnectTimeout(iZzt);
        httpURLConnectionZza.setReadTimeout(iZzt);
        httpURLConnectionZza.setUseCaches(false);
        httpURLConnectionZza.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.zzaF != null) {
            ((HttpsURLConnection) httpURLConnectionZza).setSSLSocketFactory(this.zzaF);
        }
        return httpURLConnectionZza;
    }

    private static HttpEntity zza(HttpURLConnection httpURLConnection) {
        InputStream errorStream;
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        try {
            errorStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            errorStream = httpURLConnection.getErrorStream();
        }
        basicHttpEntity.setContent(errorStream);
        basicHttpEntity.setContentLength(httpURLConnection.getContentLength());
        basicHttpEntity.setContentEncoding(httpURLConnection.getContentEncoding());
        basicHttpEntity.setContentType(httpURLConnection.getContentType());
        return basicHttpEntity;
    }

    static void zza(HttpURLConnection httpURLConnection, zzk<?> zzkVar) throws com.google.android.gms.internal.zza, IOException {
        switch (zzkVar.getMethod()) {
            case -1:
                byte[] bArrZzm = zzkVar.zzm();
                if (bArrZzm != null) {
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.addRequestProperty("Content-Type", zzkVar.zzl());
                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.write(bArrZzm);
                    dataOutputStream.close();
                    return;
                }
                return;
            case 0:
                httpURLConnection.setRequestMethod("GET");
                return;
            case 1:
                httpURLConnection.setRequestMethod("POST");
                zzb(httpURLConnection, zzkVar);
                return;
            case 2:
                httpURLConnection.setRequestMethod("PUT");
                zzb(httpURLConnection, zzkVar);
                return;
            case 3:
                httpURLConnection.setRequestMethod("DELETE");
                return;
            case 4:
                httpURLConnection.setRequestMethod("HEAD");
                return;
            case 5:
                httpURLConnection.setRequestMethod("OPTIONS");
                return;
            case 6:
                httpURLConnection.setRequestMethod("TRACE");
                return;
            case 7:
                httpURLConnection.setRequestMethod("PATCH");
                zzb(httpURLConnection, zzkVar);
                return;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void zzb(HttpURLConnection httpURLConnection, zzk<?> zzkVar) throws com.google.android.gms.internal.zza, IOException {
        byte[] bArrZzq = zzkVar.zzq();
        if (bArrZzq != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", zzkVar.zzp());
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(bArrZzq);
            dataOutputStream.close();
        }
    }

    protected HttpURLConnection zza(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    @Override // com.google.android.gms.internal.zzy
    public HttpResponse zza(zzk<?> zzkVar, Map<String, String> map) throws com.google.android.gms.internal.zza, IOException {
        String strZzh;
        String url = zzkVar.getUrl();
        HashMap map2 = new HashMap();
        map2.putAll(zzkVar.getHeaders());
        map2.putAll(map);
        if (this.zzaE != null) {
            strZzh = this.zzaE.zzh(url);
            if (strZzh == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
        } else {
            strZzh = url;
        }
        HttpURLConnection httpURLConnectionZza = zza(new URL(strZzh), zzkVar);
        for (String str : map2.keySet()) {
            httpURLConnectionZza.addRequestProperty(str, (String) map2.get(str));
        }
        zza(httpURLConnectionZza, zzkVar);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        if (httpURLConnectionZza.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(new BasicStatusLine(protocolVersion, httpURLConnectionZza.getResponseCode(), httpURLConnectionZza.getResponseMessage()));
        basicHttpResponse.setEntity(zza(httpURLConnectionZza));
        for (Map.Entry<String, List<String>> entry : httpURLConnectionZza.getHeaderFields().entrySet()) {
            if (entry.getKey() != null) {
                basicHttpResponse.addHeader(new BasicHeader(entry.getKey(), entry.getValue().get(0)));
            }
        }
        return basicHttpResponse;
    }
}
