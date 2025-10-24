package com.google.android.gms.internal;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/* loaded from: classes.dex */
public class zzw implements zzy {
    protected final HttpClient zzaD;

    public static final class zza extends HttpEntityEnclosingRequestBase {
        public zza() {
        }

        public zza(String str) {
            setURI(URI.create(str));
        }

        @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
        public String getMethod() {
            return "PATCH";
        }
    }

    public zzw(HttpClient httpClient) {
        this.zzaD = httpClient;
    }

    private static void zza(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, zzk<?> zzkVar) throws com.google.android.gms.internal.zza {
        byte[] bArrZzq = zzkVar.zzq();
        if (bArrZzq != null) {
            httpEntityEnclosingRequestBase.setEntity(new ByteArrayEntity(bArrZzq));
        }
    }

    private static void zza(HttpUriRequest httpUriRequest, Map<String, String> map) {
        for (String str : map.keySet()) {
            httpUriRequest.setHeader(str, map.get(str));
        }
    }

    static HttpUriRequest zzb(zzk<?> zzkVar, Map<String, String> map) throws com.google.android.gms.internal.zza {
        switch (zzkVar.getMethod()) {
            case -1:
                byte[] bArrZzm = zzkVar.zzm();
                if (bArrZzm == null) {
                    return new HttpGet(zzkVar.getUrl());
                }
                HttpPost httpPost = new HttpPost(zzkVar.getUrl());
                httpPost.addHeader("Content-Type", zzkVar.zzl());
                httpPost.setEntity(new ByteArrayEntity(bArrZzm));
                return httpPost;
            case 0:
                return new HttpGet(zzkVar.getUrl());
            case 1:
                HttpPost httpPost2 = new HttpPost(zzkVar.getUrl());
                httpPost2.addHeader("Content-Type", zzkVar.zzp());
                zza(httpPost2, zzkVar);
                return httpPost2;
            case 2:
                HttpPut httpPut = new HttpPut(zzkVar.getUrl());
                httpPut.addHeader("Content-Type", zzkVar.zzp());
                zza(httpPut, zzkVar);
                return httpPut;
            case 3:
                return new HttpDelete(zzkVar.getUrl());
            case 4:
                return new HttpHead(zzkVar.getUrl());
            case 5:
                return new HttpOptions(zzkVar.getUrl());
            case 6:
                return new HttpTrace(zzkVar.getUrl());
            case 7:
                zza zzaVar = new zza(zzkVar.getUrl());
                zzaVar.addHeader("Content-Type", zzkVar.zzp());
                zza(zzaVar, zzkVar);
                return zzaVar;
            default:
                throw new IllegalStateException("Unknown request method.");
        }
    }

    @Override // com.google.android.gms.internal.zzy
    public HttpResponse zza(zzk<?> zzkVar, Map<String, String> map) throws com.google.android.gms.internal.zza, IOException {
        HttpUriRequest httpUriRequestZzb = zzb(zzkVar, map);
        zza(httpUriRequestZzb, map);
        zza(httpUriRequestZzb, zzkVar.getHeaders());
        zza(httpUriRequestZzb);
        HttpParams params = httpUriRequestZzb.getParams();
        int iZzt = zzkVar.zzt();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, iZzt);
        return this.zzaD.execute(httpUriRequestZzb);
    }

    protected void zza(HttpUriRequest httpUriRequest) throws IOException {
    }
}
