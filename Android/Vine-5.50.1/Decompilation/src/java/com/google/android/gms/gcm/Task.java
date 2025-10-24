package com.google.android.gms.gcm;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/* loaded from: classes.dex */
public abstract class Task implements Parcelable {
    private final Bundle mExtras;
    private final String mTag;
    private final String zzaJt;
    private final boolean zzaJu;
    private final boolean zzaJv;
    private final int zzaJw;
    private final boolean zzaJx;
    private final zzd zzaJy;

    @Deprecated
    Task(Parcel in) {
        Log.e("Task", "Constructing a Task object using a parcel.");
        this.zzaJt = in.readString();
        this.mTag = in.readString();
        this.zzaJu = in.readInt() == 1;
        this.zzaJv = in.readInt() == 1;
        this.zzaJw = 2;
        this.zzaJx = false;
        this.zzaJy = zzd.zzaJo;
        this.mExtras = null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.zzaJt);
        parcel.writeString(this.mTag);
        parcel.writeInt(this.zzaJu ? 1 : 0);
        parcel.writeInt(this.zzaJv ? 1 : 0);
    }
}
