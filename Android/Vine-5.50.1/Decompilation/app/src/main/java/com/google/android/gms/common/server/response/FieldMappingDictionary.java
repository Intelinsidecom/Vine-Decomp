package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public class FieldMappingDictionary implements SafeParcelable {
    public static final zzc CREATOR = new zzc();
    private final int mVersionCode;
    private final HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zzale;
    private final ArrayList<Entry> zzalf = null;
    private final String zzalg;

    public static class Entry implements SafeParcelable {
        public static final zzd CREATOR = new zzd();
        final String className;
        final int versionCode;
        final ArrayList<FieldMapPair> zzalh;

        Entry(int versionCode, String className, ArrayList<FieldMapPair> fieldMapping) {
            this.versionCode = versionCode;
            this.className = className;
            this.zzalh = fieldMapping;
        }

        Entry(String className, Map<String, FastJsonResponse.Field<?, ?>> fieldMap) {
            this.versionCode = 1;
            this.className = className;
            this.zzalh = zzF(fieldMap);
        }

        private static ArrayList<FieldMapPair> zzF(Map<String, FastJsonResponse.Field<?, ?>> map) {
            if (map == null) {
                return null;
            }
            ArrayList<FieldMapPair> arrayList = new ArrayList<>();
            for (String str : map.keySet()) {
                arrayList.add(new FieldMapPair(str, map.get(str)));
            }
            return arrayList;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            zzd zzdVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            zzd zzdVar = CREATOR;
            zzd.zza(this, out, flags);
        }

        HashMap<String, FastJsonResponse.Field<?, ?>> zzrm() {
            HashMap<String, FastJsonResponse.Field<?, ?>> map = new HashMap<>();
            int size = this.zzalh.size();
            for (int i = 0; i < size; i++) {
                FieldMapPair fieldMapPair = this.zzalh.get(i);
                map.put(fieldMapPair.key, fieldMapPair.zzali);
            }
            return map;
        }
    }

    public static class FieldMapPair implements SafeParcelable {
        public static final zzb CREATOR = new zzb();
        final String key;
        final int versionCode;
        final FastJsonResponse.Field<?, ?> zzali;

        FieldMapPair(int versionCode, String key, FastJsonResponse.Field<?, ?> value) {
            this.versionCode = versionCode;
            this.key = key;
            this.zzali = value;
        }

        FieldMapPair(String key, FastJsonResponse.Field<?, ?> value) {
            this.versionCode = 1;
            this.key = key;
            this.zzali = value;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            zzb zzbVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            zzb zzbVar = CREATOR;
            zzb.zza(this, out, flags);
        }
    }

    FieldMappingDictionary(int versionCode, ArrayList<Entry> serializedDictionary, String rootClassName) {
        this.mVersionCode = versionCode;
        this.zzale = zze(serializedDictionary);
        this.zzalg = (String) zzx.zzy(rootClassName);
        zzri();
    }

    private static HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zze(ArrayList<Entry> arrayList) {
        HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> map = new HashMap<>();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            Entry entry = arrayList.get(i);
            map.put(entry.className, entry.zzrm());
        }
        return map;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        zzc zzcVar = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.zzale.keySet()) {
            sb.append(str).append(":\n");
            Map<String, FastJsonResponse.Field<?, ?>> map = this.zzale.get(str);
            for (String str2 : map.keySet()) {
                sb.append("  ").append(str2).append(": ");
                sb.append(map.get(str2));
            }
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzc zzcVar = CREATOR;
        zzc.zza(this, out, flags);
    }

    public Map<String, FastJsonResponse.Field<?, ?>> zzcL(String str) {
        return this.zzale.get(str);
    }

    public void zzri() {
        Iterator<String> it = this.zzale.keySet().iterator();
        while (it.hasNext()) {
            Map<String, FastJsonResponse.Field<?, ?>> map = this.zzale.get(it.next());
            Iterator<String> it2 = map.keySet().iterator();
            while (it2.hasNext()) {
                map.get(it2.next()).zza(this);
            }
        }
    }

    ArrayList<Entry> zzrk() {
        ArrayList<Entry> arrayList = new ArrayList<>();
        for (String str : this.zzale.keySet()) {
            arrayList.add(new Entry(str, this.zzale.get(str)));
        }
        return arrayList;
    }

    public String zzrl() {
        return this.zzalg;
    }
}
