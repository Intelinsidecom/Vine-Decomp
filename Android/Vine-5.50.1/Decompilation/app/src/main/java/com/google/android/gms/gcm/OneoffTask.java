package com.google.android.gms.gcm;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class OneoffTask extends Task {
    public static final Parcelable.Creator<OneoffTask> CREATOR = new Parcelable.Creator<OneoffTask>() { // from class: com.google.android.gms.gcm.OneoffTask.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: zzez, reason: merged with bridge method [inline-methods] */
        public OneoffTask createFromParcel(Parcel parcel) {
            return new OneoffTask(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: zzgW, reason: merged with bridge method [inline-methods] */
        public OneoffTask[] newArray(int i) {
            return new OneoffTask[i];
        }
    };
    private final long zzaJi;
    private final long zzaJj;

    @Deprecated
    private OneoffTask(Parcel in) {
        super(in);
        this.zzaJi = in.readLong();
        this.zzaJj = in.readLong();
    }

    public long getWindowEnd() {
        return this.zzaJj;
    }

    public long getWindowStart() {
        return this.zzaJi;
    }

    public String toString() {
        return super.toString() + " windowStart=" + getWindowStart() + " windowEnd=" + getWindowEnd();
    }

    @Override // com.google.android.gms.gcm.Task, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(this.zzaJi);
        parcel.writeLong(this.zzaJj);
    }
}
