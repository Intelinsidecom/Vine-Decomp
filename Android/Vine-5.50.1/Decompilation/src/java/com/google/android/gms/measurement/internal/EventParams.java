package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Iterator;

/* loaded from: classes.dex */
public class EventParams implements SafeParcelable, Iterable<String> {
    public static final zzi CREATOR = new zzi();
    public final int versionCode;
    private final Bundle zzaSI;

    EventParams(int versionCode, Bundle bundle) {
        this.versionCode = versionCode;
        this.zzaSI = bundle;
    }

    EventParams(Bundle bundle) {
        com.google.android.gms.common.internal.zzx.zzy(bundle);
        this.zzaSI = bundle;
        this.versionCode = 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    Object get(String key) {
        return this.zzaSI.get(key);
    }

    @Override // java.lang.Iterable
    public Iterator<String> iterator() {
        return new Iterator<String>() { // from class: com.google.android.gms.measurement.internal.EventParams.1
            Iterator<String> zzaSJ;

            {
                this.zzaSJ = EventParams.this.zzaSI.keySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.zzaSJ.hasNext();
            }

            @Override // java.util.Iterator
            public String next() {
                return this.zzaSJ.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Remove not supported");
            }
        };
    }

    public int size() {
        return this.zzaSI.size();
    }

    public String toString() {
        return this.zzaSI.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        zzi.zza(this, out, flags);
    }

    public Bundle zzBh() {
        return new Bundle(this.zzaSI);
    }
}
