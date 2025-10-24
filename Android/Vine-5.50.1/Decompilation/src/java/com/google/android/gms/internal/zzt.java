package com.google.android.gms.internal;

import android.os.SystemClock;
import com.coremedia.iso.boxes.AuthorBox;
import com.google.android.gms.internal.zzb;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

/* loaded from: classes.dex */
public class zzt implements zzf {
    protected static final boolean DEBUG = zzs.DEBUG;
    private static int zzao = 3000;
    private static int zzap = 4096;
    protected final zzy zzaq;
    protected final zzu zzar;

    public zzt(zzy zzyVar) {
        this(zzyVar, new zzu(zzap));
    }

    public zzt(zzy zzyVar, zzu zzuVar) {
        this.zzaq = zzyVar;
        this.zzar = zzuVar;
    }

    protected static Map<String, String> zza(Header[] headerArr) {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < headerArr.length; i++) {
            treeMap.put(headerArr[i].getName(), headerArr[i].getValue());
        }
        return treeMap;
    }

    private void zza(long j, zzk<?> zzkVar, byte[] bArr, StatusLine statusLine) {
        if (DEBUG || j > zzao) {
            Object[] objArr = new Object[5];
            objArr[0] = zzkVar;
            objArr[1] = Long.valueOf(j);
            objArr[2] = bArr != null ? Integer.valueOf(bArr.length) : "null";
            objArr[3] = Integer.valueOf(statusLine.getStatusCode());
            objArr[4] = Integer.valueOf(zzkVar.zzu().zze());
            zzs.zzb("HTTP response for request=<%s> [lifetime=%d], [size=%s], [rc=%d], [retryCount=%s]", objArr);
        }
    }

    private static void zza(String str, zzk<?> zzkVar, zzr zzrVar) throws zzr {
        zzo zzoVarZzu = zzkVar.zzu();
        int iZzt = zzkVar.zzt();
        try {
            zzoVarZzu.zza(zzrVar);
            zzkVar.zzc(String.format("%s-retry [timeout=%s]", str, Integer.valueOf(iZzt)));
        } catch (zzr e) {
            zzkVar.zzc(String.format("%s-timeout-giveup [timeout=%s]", str, Integer.valueOf(iZzt)));
            throw e;
        }
    }

    private void zza(Map<String, String> map, zzb.zza zzaVar) {
        if (zzaVar == null) {
            return;
        }
        if (zzaVar.zzb != null) {
            map.put("If-None-Match", zzaVar.zzb);
        }
        if (zzaVar.zzd > 0) {
            map.put("If-Modified-Since", DateUtils.formatDate(new Date(zzaVar.zzd)));
        }
    }

    private byte[] zza(HttpEntity httpEntity) throws IOException, zzp {
        zzaa zzaaVar = new zzaa(this.zzar, (int) httpEntity.getContentLength());
        try {
            InputStream content = httpEntity.getContent();
            if (content == null) {
                throw new zzp();
            }
            byte[] bArrZzb = this.zzar.zzb(1024);
            while (true) {
                int i = content.read(bArrZzb);
                if (i == -1) {
                    break;
                }
                zzaaVar.write(bArrZzb, 0, i);
            }
            byte[] byteArray = zzaaVar.toByteArray();
            try {
                httpEntity.consumeContent();
            } catch (IOException e) {
                zzs.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzar.zza(bArrZzb);
            zzaaVar.close();
            return byteArray;
        } catch (Throwable th) {
            try {
                httpEntity.consumeContent();
            } catch (IOException e2) {
                zzs.zza("Error occured when calling consumingContent", new Object[0]);
            }
            this.zzar.zza(null);
            zzaaVar.close();
            throw th;
        }
    }

    @Override // com.google.android.gms.internal.zzf
    public zzi zza(zzk<?> zzkVar) throws zzr, IOException {
        byte[] bArr;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        while (true) {
            HttpResponse httpResponse = null;
            Map<String, String> mapEmptyMap = Collections.emptyMap();
            try {
                try {
                    HashMap map = new HashMap();
                    zza(map, zzkVar.zzi());
                    HttpResponse httpResponseZza = this.zzaq.zza(zzkVar, map);
                    try {
                        StatusLine statusLine = httpResponseZza.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        mapEmptyMap = zza(httpResponseZza.getAllHeaders());
                        if (statusCode == 304) {
                            zzb.zza zzaVarZzi = zzkVar.zzi();
                            if (zzaVarZzi == null) {
                                return new zzi(304, null, mapEmptyMap, true, SystemClock.elapsedRealtime() - jElapsedRealtime);
                            }
                            zzaVarZzi.zzg.putAll(mapEmptyMap);
                            return new zzi(304, zzaVarZzi.data, zzaVarZzi.zzg, true, SystemClock.elapsedRealtime() - jElapsedRealtime);
                        }
                        byte[] bArrZza = httpResponseZza.getEntity() != null ? zza(httpResponseZza.getEntity()) : new byte[0];
                        try {
                            zza(SystemClock.elapsedRealtime() - jElapsedRealtime, zzkVar, bArrZza, statusLine);
                            if (statusCode < 200 || statusCode > 299) {
                                throw new IOException();
                            }
                            return new zzi(statusCode, bArrZza, mapEmptyMap, false, SystemClock.elapsedRealtime() - jElapsedRealtime);
                        } catch (IOException e) {
                            e = e;
                            bArr = bArrZza;
                            httpResponse = httpResponseZza;
                            if (httpResponse == null) {
                                throw new zzj(e);
                            }
                            int statusCode2 = httpResponse.getStatusLine().getStatusCode();
                            zzs.zzc("Unexpected response code %d for %s", Integer.valueOf(statusCode2), zzkVar.getUrl());
                            if (bArr == null) {
                                throw new zzh((zzi) null);
                            }
                            zzi zziVar = new zzi(statusCode2, bArr, mapEmptyMap, false, SystemClock.elapsedRealtime() - jElapsedRealtime);
                            if (statusCode2 != 401 && statusCode2 != 403) {
                                throw new zzp(zziVar);
                            }
                            zza(AuthorBox.TYPE, zzkVar, new zza(zziVar));
                        }
                    } catch (IOException e2) {
                        e = e2;
                        bArr = null;
                        httpResponse = httpResponseZza;
                    }
                } catch (MalformedURLException e3) {
                    throw new RuntimeException("Bad URL " + zzkVar.getUrl(), e3);
                } catch (SocketTimeoutException e4) {
                    zza("socket", zzkVar, new zzq());
                } catch (ConnectTimeoutException e5) {
                    zza("connection", zzkVar, new zzq());
                }
            } catch (IOException e6) {
                e = e6;
                bArr = null;
            }
        }
    }
}
