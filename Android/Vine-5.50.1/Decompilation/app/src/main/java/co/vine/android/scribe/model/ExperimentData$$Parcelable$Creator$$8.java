package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class ExperimentData$$Parcelable$Creator$$8 implements Parcelable.Creator<ExperimentData$$Parcelable> {
    private ExperimentData$$Parcelable$Creator$$8() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ExperimentData$$Parcelable createFromParcel(Parcel parcel$$174) {
        return new ExperimentData$$Parcelable(parcel$$174);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ExperimentData$$Parcelable[] newArray(int size) {
        return new ExperimentData$$Parcelable[size];
    }
}
