package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: Access modifiers changed from: private */
/* loaded from: classes.dex */
public final class SuggestionDetails$$Parcelable$Creator$$5 implements Parcelable.Creator<SuggestionDetails$$Parcelable> {
    private SuggestionDetails$$Parcelable$Creator$$5() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public SuggestionDetails$$Parcelable createFromParcel(Parcel parcel$$157) {
        return new SuggestionDetails$$Parcelable(parcel$$157);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public SuggestionDetails$$Parcelable[] newArray(int size) {
        return new SuggestionDetails$$Parcelable[size];
    }
}
