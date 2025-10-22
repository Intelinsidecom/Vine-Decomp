package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class SuggestionDetails$$Parcelable implements Parcelable, ParcelWrapper<SuggestionDetails> {
    public static final SuggestionDetails$$Parcelable$Creator$$5 CREATOR = new SuggestionDetails$$Parcelable$Creator$$5();
    private SuggestionDetails suggestionDetails$$9;

    public SuggestionDetails$$Parcelable(Parcel parcel$$153) {
        SuggestionDetails suggestionDetails$$11;
        if (parcel$$153.readInt() == -1) {
            suggestionDetails$$11 = null;
        } else {
            suggestionDetails$$11 = readco_vine_android_scribe_model_SuggestionDetails(parcel$$153);
        }
        this.suggestionDetails$$9 = suggestionDetails$$11;
    }

    public SuggestionDetails$$Parcelable(SuggestionDetails suggestionDetails$$13) {
        this.suggestionDetails$$9 = suggestionDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$154, int flags) {
        if (this.suggestionDetails$$9 == null) {
            parcel$$154.writeInt(-1);
        } else {
            parcel$$154.writeInt(1);
            writeco_vine_android_scribe_model_SuggestionDetails(this.suggestionDetails$$9, parcel$$154, flags);
        }
    }

    private SuggestionDetails readco_vine_android_scribe_model_SuggestionDetails(Parcel parcel$$155) {
        SuggestionDetails suggestionDetails$$10 = new SuggestionDetails();
        suggestionDetails$$10.suggestedQuery = parcel$$155.readString();
        return suggestionDetails$$10;
    }

    private void writeco_vine_android_scribe_model_SuggestionDetails(SuggestionDetails suggestionDetails$$12, Parcel parcel$$156, int flags$$69) {
        parcel$$156.writeString(suggestionDetails$$12.suggestedQuery);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public SuggestionDetails getParcel() {
        return this.suggestionDetails$$9;
    }
}
