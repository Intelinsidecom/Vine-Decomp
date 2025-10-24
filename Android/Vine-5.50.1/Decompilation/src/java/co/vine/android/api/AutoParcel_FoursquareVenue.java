package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.FoursquareVenue;

/* loaded from: classes.dex */
final class AutoParcel_FoursquareVenue extends FoursquareVenue {
    private final String address;
    private final FoursquareVenue.Category category;
    private final String name;
    private final String venueId;
    public static final Parcelable.Creator<AutoParcel_FoursquareVenue> CREATOR = new Parcelable.Creator<AutoParcel_FoursquareVenue>() { // from class: co.vine.android.api.AutoParcel_FoursquareVenue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_FoursquareVenue createFromParcel(Parcel in) {
            return new AutoParcel_FoursquareVenue(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutoParcel_FoursquareVenue[] newArray(int size) {
            return new AutoParcel_FoursquareVenue[size];
        }
    };
    private static final ClassLoader CL = AutoParcel_FoursquareVenue.class.getClassLoader();

    AutoParcel_FoursquareVenue(String venueId, String name, String address, FoursquareVenue.Category category) {
        this.venueId = venueId;
        this.name = name;
        this.address = address;
        this.category = category;
    }

    @Override // co.vine.android.api.FoursquareVenue
    public String getVenueId() {
        return this.venueId;
    }

    @Override // co.vine.android.api.FoursquareVenue
    public String getName() {
        return this.name;
    }

    @Override // co.vine.android.api.FoursquareVenue
    public String getAddress() {
        return this.address;
    }

    @Override // co.vine.android.api.FoursquareVenue
    public FoursquareVenue.Category getCategory() {
        return this.category;
    }

    public String toString() {
        return "FoursquareVenue{venueId=" + this.venueId + ", name=" + this.name + ", address=" + this.address + ", category=" + this.category + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FoursquareVenue)) {
            return false;
        }
        FoursquareVenue that = (FoursquareVenue) o;
        if (this.venueId != null ? this.venueId.equals(that.getVenueId()) : that.getVenueId() == null) {
            if (this.name != null ? this.name.equals(that.getName()) : that.getName() == null) {
                if (this.address != null ? this.address.equals(that.getAddress()) : that.getAddress() == null) {
                    if (this.category == null) {
                        if (that.getCategory() == null) {
                            return true;
                        }
                    } else if (this.category.equals(that.getCategory())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return ((((((h ^ (this.venueId == null ? 0 : this.venueId.hashCode())) * 1000003) ^ (this.name == null ? 0 : this.name.hashCode())) * 1000003) ^ (this.address == null ? 0 : this.address.hashCode())) * 1000003) ^ (this.category != null ? this.category.hashCode() : 0);
    }

    private AutoParcel_FoursquareVenue(Parcel in) {
        this((String) in.readValue(CL), (String) in.readValue(CL), (String) in.readValue(CL), (FoursquareVenue.Category) in.readValue(CL));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.venueId);
        dest.writeValue(this.name);
        dest.writeValue(this.address);
        dest.writeValue(this.category);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
