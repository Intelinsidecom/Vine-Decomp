package com.google.android.gms.internal;

import android.os.SystemClock;
import com.google.android.gms.internal.zzb;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class zzv implements com.google.android.gms.internal.zzb {
    private final int zzaA;
    private final Map<String, zza> zzax;
    private long zzay;
    private final File zzaz;

    static class zza {
        public String key;
        public long zzaB;
        public String zzb;
        public long zzc;
        public long zzd;
        public long zze;
        public long zzf;
        public Map<String, String> zzg;

        private zza() {
        }

        public zza(String str, zzb.zza zzaVar) {
            this.key = str;
            this.zzaB = zzaVar.data.length;
            this.zzb = zzaVar.zzb;
            this.zzc = zzaVar.zzc;
            this.zzd = zzaVar.zzd;
            this.zze = zzaVar.zze;
            this.zzf = zzaVar.zzf;
            this.zzg = zzaVar.zzg;
        }

        public static zza zzf(InputStream inputStream) throws IOException {
            zza zzaVar = new zza();
            if (zzv.zzb(inputStream) != 538247942) {
                throw new IOException();
            }
            zzaVar.key = zzv.zzd(inputStream);
            zzaVar.zzb = zzv.zzd(inputStream);
            if (zzaVar.zzb.equals("")) {
                zzaVar.zzb = null;
            }
            zzaVar.zzc = zzv.zzc(inputStream);
            zzaVar.zzd = zzv.zzc(inputStream);
            zzaVar.zze = zzv.zzc(inputStream);
            zzaVar.zzf = zzv.zzc(inputStream);
            zzaVar.zzg = zzv.zze(inputStream);
            return zzaVar;
        }

        public boolean zza(OutputStream outputStream) throws IOException {
            try {
                zzv.zza(outputStream, 538247942);
                zzv.zza(outputStream, this.key);
                zzv.zza(outputStream, this.zzb == null ? "" : this.zzb);
                zzv.zza(outputStream, this.zzc);
                zzv.zza(outputStream, this.zzd);
                zzv.zza(outputStream, this.zze);
                zzv.zza(outputStream, this.zzf);
                zzv.zza(this.zzg, outputStream);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                zzs.zzb("%s", e.toString());
                return false;
            }
        }

        public zzb.zza zzb(byte[] bArr) {
            zzb.zza zzaVar = new zzb.zza();
            zzaVar.data = bArr;
            zzaVar.zzb = this.zzb;
            zzaVar.zzc = this.zzc;
            zzaVar.zzd = this.zzd;
            zzaVar.zze = this.zze;
            zzaVar.zzf = this.zzf;
            zzaVar.zzg = this.zzg;
            return zzaVar;
        }
    }

    private static class zzb extends FilterInputStream {
        private int zzaC;

        private zzb(InputStream inputStream) {
            super(inputStream);
            this.zzaC = 0;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            int i = super.read();
            if (i != -1) {
                this.zzaC++;
            }
            return i;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] buffer, int offset, int count) throws IOException {
            int i = super.read(buffer, offset, count);
            if (i != -1) {
                this.zzaC += i;
            }
            return i;
        }
    }

    public zzv(File file) {
        this(file, 5242880);
    }

    public zzv(File file, int i) {
        this.zzax = new LinkedHashMap(16, 0.75f, true);
        this.zzay = 0L;
        this.zzaz = file;
        this.zzaA = i;
    }

    private void removeEntry(String key) {
        zza zzaVar = this.zzax.get(key);
        if (zzaVar != null) {
            this.zzay -= zzaVar.zzaB;
            this.zzax.remove(key);
        }
    }

    private static int zza(InputStream inputStream) throws IOException {
        int i = inputStream.read();
        if (i == -1) {
            throw new EOFException();
        }
        return i;
    }

    static void zza(OutputStream outputStream, int i) throws IOException {
        outputStream.write((i >> 0) & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write((i >> 24) & 255);
    }

    static void zza(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) (j >>> 0));
        outputStream.write((byte) (j >>> 8));
        outputStream.write((byte) (j >>> 16));
        outputStream.write((byte) (j >>> 24));
        outputStream.write((byte) (j >>> 32));
        outputStream.write((byte) (j >>> 40));
        outputStream.write((byte) (j >>> 48));
        outputStream.write((byte) (j >>> 56));
    }

    static void zza(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        zza(outputStream, bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    private void zza(String str, zza zzaVar) {
        if (this.zzax.containsKey(str)) {
            this.zzay = (zzaVar.zzaB - this.zzax.get(str).zzaB) + this.zzay;
        } else {
            this.zzay += zzaVar.zzaB;
        }
        this.zzax.put(str, zzaVar);
    }

    static void zza(Map<String, String> map, OutputStream outputStream) throws IOException {
        if (map == null) {
            zza(outputStream, 0);
            return;
        }
        zza(outputStream, map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            zza(outputStream, entry.getKey());
            zza(outputStream, entry.getValue());
        }
    }

    private static byte[] zza(InputStream inputStream, int i) throws IOException {
        byte[] bArr = new byte[i];
        int i2 = 0;
        while (i2 < i) {
            int i3 = inputStream.read(bArr, i2, i - i2);
            if (i3 == -1) {
                break;
            }
            i2 += i3;
        }
        if (i2 != i) {
            throw new IOException("Expected " + i + " bytes, read " + i2 + " bytes");
        }
        return bArr;
    }

    static int zzb(InputStream inputStream) throws IOException {
        return 0 | (zza(inputStream) << 0) | (zza(inputStream) << 8) | (zza(inputStream) << 16) | (zza(inputStream) << 24);
    }

    static long zzc(InputStream inputStream) throws IOException {
        return 0 | ((zza(inputStream) & 255) << 0) | ((zza(inputStream) & 255) << 8) | ((zza(inputStream) & 255) << 16) | ((zza(inputStream) & 255) << 24) | ((zza(inputStream) & 255) << 32) | ((zza(inputStream) & 255) << 40) | ((zza(inputStream) & 255) << 48) | ((zza(inputStream) & 255) << 56);
    }

    private void zzc(int i) {
        int i2;
        if (this.zzay + i < this.zzaA) {
            return;
        }
        if (zzs.DEBUG) {
            zzs.zza("Pruning old cache entries.", new Object[0]);
        }
        long j = this.zzay;
        long jElapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<Map.Entry<String, zza>> it = this.zzax.entrySet().iterator();
        int i3 = 0;
        while (true) {
            if (!it.hasNext()) {
                i2 = i3;
                break;
            }
            zza value = it.next().getValue();
            if (zzf(value.key).delete()) {
                this.zzay -= value.zzaB;
            } else {
                zzs.zzb("Could not delete cache entry for key=%s, filename=%s", value.key, zze(value.key));
            }
            it.remove();
            i2 = i3 + 1;
            if (this.zzay + i < this.zzaA * 0.9f) {
                break;
            } else {
                i3 = i2;
            }
        }
        if (zzs.DEBUG) {
            zzs.zza("pruned %d files, %d bytes, %d ms", Integer.valueOf(i2), Long.valueOf(this.zzay - j), Long.valueOf(SystemClock.elapsedRealtime() - jElapsedRealtime));
        }
    }

    static String zzd(InputStream inputStream) throws IOException {
        return new String(zza(inputStream, (int) zzc(inputStream)), "UTF-8");
    }

    private String zze(String str) {
        int length = str.length() / 2;
        return String.valueOf(str.substring(0, length).hashCode()) + String.valueOf(str.substring(length).hashCode());
    }

    static Map<String, String> zze(InputStream inputStream) throws IOException {
        int iZzb = zzb(inputStream);
        Map<String, String> mapEmptyMap = iZzb == 0 ? Collections.emptyMap() : new HashMap<>(iZzb);
        for (int i = 0; i < iZzb; i++) {
            mapEmptyMap.put(zzd(inputStream).intern(), zzd(inputStream).intern());
        }
        return mapEmptyMap;
    }

    public synchronized void remove(String key) {
        boolean zDelete = zzf(key).delete();
        removeEntry(key);
        if (!zDelete) {
            zzs.zzb("Could not delete cache entry for key=%s, filename=%s", key, zze(key));
        }
    }

    @Override // com.google.android.gms.internal.zzb
    public synchronized zzb.zza zza(String str) {
        zzb zzbVar;
        zzb.zza zzaVarZzb;
        zza zzaVar = this.zzax.get(str);
        if (zzaVar == null) {
            zzaVarZzb = null;
        } else {
            try {
                File fileZzf = zzf(str);
                try {
                    zzbVar = new zzb(new FileInputStream(fileZzf));
                } catch (IOException e) {
                    e = e;
                    zzbVar = null;
                } catch (Throwable th) {
                    th = th;
                    zzbVar = null;
                    if (zzbVar != null) {
                        try {
                            zzbVar.close();
                        } catch (IOException e2) {
                            zzaVarZzb = null;
                        }
                    }
                    throw th;
                }
                try {
                    zza.zzf(zzbVar);
                    zzaVarZzb = zzaVar.zzb(zza(zzbVar, (int) (fileZzf.length() - zzbVar.zzaC)));
                    if (zzbVar != null) {
                        try {
                            zzbVar.close();
                        } catch (IOException e3) {
                            zzaVarZzb = null;
                        }
                    }
                } catch (IOException e4) {
                    e = e4;
                    zzs.zzb("%s: %s", fileZzf.getAbsolutePath(), e.toString());
                    remove(str);
                    if (zzbVar != null) {
                        try {
                            zzbVar.close();
                            zzaVarZzb = null;
                        } catch (IOException e5) {
                            zzaVarZzb = null;
                        }
                    } else {
                        zzaVarZzb = null;
                    }
                    return zzaVarZzb;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return zzaVarZzb;
    }

    @Override // com.google.android.gms.internal.zzb
    public synchronized void zza() {
        BufferedInputStream bufferedInputStream;
        if (this.zzaz.exists()) {
            File[] fileArrListFiles = this.zzaz.listFiles();
            if (fileArrListFiles != null) {
                for (File file : fileArrListFiles) {
                    BufferedInputStream bufferedInputStream2 = null;
                    try {
                        bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    } catch (IOException e) {
                        bufferedInputStream = null;
                    } catch (Throwable th) {
                        th = th;
                    }
                    try {
                        try {
                            zza zzaVarZzf = zza.zzf(bufferedInputStream);
                            zzaVarZzf.zzaB = file.length();
                            zza(zzaVarZzf.key, zzaVarZzf);
                            if (bufferedInputStream != null) {
                                try {
                                    bufferedInputStream.close();
                                } catch (IOException e2) {
                                }
                            }
                        } catch (Throwable th2) {
                            bufferedInputStream2 = bufferedInputStream;
                            th = th2;
                            if (bufferedInputStream2 != null) {
                                try {
                                    bufferedInputStream2.close();
                                } catch (IOException e3) {
                                }
                            }
                            throw th;
                        }
                    } catch (IOException e4) {
                        if (file != null) {
                            file.delete();
                        }
                        if (bufferedInputStream != null) {
                            try {
                                bufferedInputStream.close();
                            } catch (IOException e5) {
                            }
                        }
                    }
                }
            }
        } else if (!this.zzaz.mkdirs()) {
            zzs.zzc("Unable to create cache dir %s", this.zzaz.getAbsolutePath());
        }
    }

    @Override // com.google.android.gms.internal.zzb
    public synchronized void zza(String str, zzb.zza zzaVar) {
        FileOutputStream fileOutputStream;
        zza zzaVar2;
        zzc(zzaVar.data.length);
        File fileZzf = zzf(str);
        try {
            fileOutputStream = new FileOutputStream(fileZzf);
            zzaVar2 = new zza(str, zzaVar);
        } catch (IOException e) {
            if (!fileZzf.delete()) {
                zzs.zzb("Could not clean up file %s", fileZzf.getAbsolutePath());
            }
        }
        if (!zzaVar2.zza(fileOutputStream)) {
            fileOutputStream.close();
            zzs.zzb("Failed to write header for %s", fileZzf.getAbsolutePath());
            throw new IOException();
        }
        fileOutputStream.write(zzaVar.data);
        fileOutputStream.close();
        zza(str, zzaVar2);
    }

    public File zzf(String str) {
        return new File(this.zzaz, zze(str));
    }
}
