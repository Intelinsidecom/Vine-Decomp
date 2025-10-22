package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
final class AutoParcel_ListSection<T> extends ListSection<T> {
    private final String anchorStr;
    private final String backAnchor;
    private final int displayCount;
    private final ArrayList<T> items;
    private final String type;
    public static final Parcelable.Creator<AutoParcel_ListSection> CREATOR = new Parcelable.Creator<AutoParcel_ListSection>() { // from class: co.vine.android.api.AutoParcel_ListSection.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_ListSection createFromParcel(Parcel in) {
            return new AutoParcel_ListSection(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_ListSection[] newArray(int size) {
            return new AutoParcel_ListSection[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_ListSection.class.getClassLoader();

    AutoParcel_ListSection(int displayCount, String anchorStr, String backAnchor, String type, ArrayList<T> items) {
        this.displayCount = displayCount;
        this.anchorStr = anchorStr;
        this.backAnchor = backAnchor;
        this.type = type;
        if (items == null) {
            throw new NullPointerException("Null items");
        }
        this.items = items;
    }

    @Override // co.vine.android.api.ListSection
    public int getDisplayCount() {
        return this.displayCount;
    }

    @Override // co.vine.android.api.ListSection
    public String getAnchorStr() {
        return this.anchorStr;
    }

    @Override // co.vine.android.api.ListSection
    public String getBackAnchor() {
        return this.backAnchor;
    }

    @Override // co.vine.android.api.ListSection
    public String getType() {
        return this.type;
    }

    @Override // co.vine.android.api.ListSection
    public ArrayList<T> getItems() {
        return this.items;
    }

    public String toString() {
        return "ListSection{displayCount=" + this.displayCount + ", anchorStr=" + this.anchorStr + ", backAnchor=" + this.backAnchor + ", type=" + this.type + ", items=" + this.items + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ListSection)) {
            return false;
        }
        ListSection<?> that = (ListSection) o;
        return this.displayCount == that.getDisplayCount() && (this.anchorStr != null ? this.anchorStr.equals(that.getAnchorStr()) : that.getAnchorStr() == null) && (this.backAnchor != null ? this.backAnchor.equals(that.getBackAnchor()) : that.getBackAnchor() == null) && (this.type != null ? this.type.equals(that.getType()) : that.getType() == null) && this.items.equals(that.getItems());
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return ((((((((h ^ this.displayCount) * 1000003) ^ (this.anchorStr == null ? 0 : this.anchorStr.hashCode())) * 1000003) ^ (this.backAnchor == null ? 0 : this.backAnchor.hashCode())) * 1000003) ^ (this.type != null ? this.type.hashCode() : 0)) * 1000003) ^ this.items.hashCode();
    }

    private AutoParcel_ListSection(Parcel in) {
        this(((Integer) in.readValue(CL)).intValue(), (String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (ArrayList) in.readValue(CL));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(Integer.valueOf(this.displayCount));
        dest.writeValue(this.anchorStr);
        dest.writeValue(this.backAnchor);
        dest.writeValue(this.type);
        dest.writeValue(this.items);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
