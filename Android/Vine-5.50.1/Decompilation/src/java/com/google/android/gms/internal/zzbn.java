package com.google.android.gms.internal;

import android.util.Base64OutputStream;
import com.google.android.gms.internal.zzbq;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.PriorityQueue;

@zzha
/* loaded from: classes.dex */
public class zzbn {
    private final int zztf;
    private final zzbm zzth = new zzbp();
    private final int zzte = 6;
    private final int zztg = 0;

    static class zza {
        ByteArrayOutputStream zztj = new ByteArrayOutputStream(4096);
        Base64OutputStream zztk = new Base64OutputStream(this.zztj, 10);

        public String toString() throws IOException {
            String string;
            try {
                this.zztk.close();
            } catch (IOException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzb("HashManager: Unable to convert to Base64.", e);
            }
            try {
                this.zztj.close();
                string = this.zztj.toString();
            } catch (IOException e2) {
                com.google.android.gms.ads.internal.util.client.zzb.zzb("HashManager: Unable to convert to Base64.", e2);
                string = "";
            } finally {
                this.zztj = null;
                this.zztk = null;
            }
            return string;
        }

        public void write(byte[] data) throws IOException {
            this.zztk.write(data);
        }
    }

    public zzbn(int i) {
        this.zztf = i;
    }

    private String zzA(String str) {
        String[] strArrSplit = str.split("\n");
        if (strArrSplit.length == 0) {
            return "";
        }
        zza zzaVarZzcH = zzcH();
        Arrays.sort(strArrSplit, new Comparator<String>() { // from class: com.google.android.gms.internal.zzbn.1
            @Override // java.util.Comparator
            public int compare(String s1, String s2) {
                return s2.length() - s1.length();
            }
        });
        for (int i = 0; i < strArrSplit.length && i < this.zztf; i++) {
            if (strArrSplit[i].trim().length() != 0) {
                try {
                    zzaVarZzcH.write(this.zzth.zzz(strArrSplit[i]));
                } catch (IOException e) {
                    com.google.android.gms.ads.internal.util.client.zzb.zzb("Error while writing hash to byteStream", e);
                }
            }
        }
        return zzaVarZzcH.toString();
    }

    String zzB(String str) {
        String[] strArrSplit = str.split("\n");
        if (strArrSplit.length == 0) {
            return "";
        }
        zza zzaVarZzcH = zzcH();
        PriorityQueue priorityQueue = new PriorityQueue(this.zztf, new Comparator<zzbq.zza>() { // from class: com.google.android.gms.internal.zzbn.2
            @Override // java.util.Comparator
            /* renamed from: zza, reason: merged with bridge method [inline-methods] */
            public int compare(zzbq.zza zzaVar, zzbq.zza zzaVar2) {
                return (int) (zzaVar.value - zzaVar2.value);
            }
        });
        for (String str2 : strArrSplit) {
            String[] strArrZzD = zzbo.zzD(str2);
            if (strArrZzD.length >= this.zzte) {
                zzbq.zza(strArrZzD, this.zztf, this.zzte, (PriorityQueue<zzbq.zza>) priorityQueue);
            }
        }
        Iterator it = priorityQueue.iterator();
        while (it.hasNext()) {
            try {
                zzaVarZzcH.write(this.zzth.zzz(((zzbq.zza) it.next()).zztm));
            } catch (IOException e) {
                com.google.android.gms.ads.internal.util.client.zzb.zzb("Error while writing hash to byteStream", e);
            }
        }
        return zzaVarZzcH.toString();
    }

    public String zza(ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next().toLowerCase(Locale.US));
            stringBuffer.append('\n');
        }
        switch (this.zztg) {
            case 0:
                return zzB(stringBuffer.toString());
            case 1:
                return zzA(stringBuffer.toString());
            default:
                return "";
        }
    }

    zza zzcH() {
        return new zza();
    }
}
