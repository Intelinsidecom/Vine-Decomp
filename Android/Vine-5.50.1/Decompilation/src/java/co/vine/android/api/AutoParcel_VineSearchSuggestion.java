package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class AutoParcel_VineSearchSuggestion extends VineSearchSuggestion {
    private final String query;
    public static final Parcelable.Creator<AutoParcel_VineSearchSuggestion> CREATOR = new Parcelable.Creator<AutoParcel_VineSearchSuggestion>() { // from class: co.vine.android.api.AutoParcel_VineSearchSuggestion.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineSearchSuggestion createFromParcel(Parcel in) {
            return new AutoParcel_VineSearchSuggestion(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_VineSearchSuggestion[] newArray(int size) {
            return new AutoParcel_VineSearchSuggestion[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_VineSearchSuggestion.class.getClassLoader();

    AutoParcel_VineSearchSuggestion(String query) {
        this.query = query;
    }

    @Override // co.vine.android.api.VineSearchSuggestion
    public String getQuery() {
        return this.query;
    }

    public String toString() {
        return "VineSearchSuggestion{query=" + this.query + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VineSearchSuggestion)) {
            return false;
        }
        VineSearchSuggestion that = (VineSearchSuggestion) o;
        return this.query == null ? that.getQuery() == null : this.query.equals(that.getQuery());
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return h ^ (this.query == null ? 0 : this.query.hashCode());
    }

    private AutoParcel_VineSearchSuggestion(Parcel in) {
        this((String) in.readValue(CL));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.query);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
