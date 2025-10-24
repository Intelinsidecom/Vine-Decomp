package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes2.dex */
public final class StringToIntConverter implements SafeParcelable, FastJsonResponse.zza<String, Integer> {
    public static final zzb CREATOR = new zzb();
    private final int mVersionCode;
    private final HashMap<String, Integer> zzakP;
    private final HashMap<Integer, String> zzakQ;
    private final ArrayList<Entry> zzakR;

    public static final class Entry implements SafeParcelable {
        public static final zzc CREATOR = new zzc();
        final int versionCode;
        final String zzakS;
        final int zzakT;

        Entry(int versionCode, String stringValue, int intValue) {
            this.versionCode = versionCode;
            this.zzakS = stringValue;
            this.zzakT = intValue;
        }

        Entry(String stringValue, int intValue) {
            this.versionCode = 1;
            this.zzakS = stringValue;
            this.zzakT = intValue;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            zzc zzcVar = CREATOR;
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            zzc zzcVar = CREATOR;
            zzc.zza(this, out, flags);
        }
    }

    public StringToIntConverter() {
        this.mVersionCode = 1;
        this.zzakP = new HashMap<>();
        this.zzakQ = new HashMap<>();
        this.zzakR = null;
    }

    StringToIntConverter(int versionCode, ArrayList<Entry> serializedMap) {
        this.mVersionCode = versionCode;
        this.zzakP = new HashMap<>();
        this.zzakQ = new HashMap<>();
        this.zzakR = null;
        zzd(serializedMap);
    }

    private void zzd(ArrayList<Entry> arrayList) {
        Iterator<Entry> it = arrayList.iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            zzh(next.zzakS, next.zzakT);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        zzb zzbVar = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzb zzbVar = CREATOR;
        zzb.zza(this, out, flags);
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse.zza
    /* renamed from: zzb, reason: merged with bridge method [inline-methods] */
    public String convertBack(Integer num) {
        String str = this.zzakQ.get(num);
        return (str == null && this.zzakP.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public StringToIntConverter zzh(String str, int i) {
        this.zzakP.put(str, Integer.valueOf(i));
        this.zzakQ.put(Integer.valueOf(i), str);
        return this;
    }

    ArrayList<Entry> zzqS() {
        ArrayList<Entry> arrayList = new ArrayList<>();
        for (String str : this.zzakP.keySet()) {
            arrayList.add(new Entry(str, this.zzakP.get(str).intValue()));
        }
        return arrayList;
    }
}
