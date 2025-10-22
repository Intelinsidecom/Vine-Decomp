package co.vine.android.api;

import co.vine.android.api.FoursquareVenue;

/* loaded from: classes.dex */
final class AutoParcel_FoursquareVenue_Category extends FoursquareVenue.Category {
    private final String leafName;
    private final String parentName;
    private final String shortName;

    AutoParcel_FoursquareVenue_Category(String shortName, String leafName, String parentName) {
        this.shortName = shortName;
        this.leafName = leafName;
        this.parentName = parentName;
    }

    @Override // co.vine.android.api.FoursquareVenue.Category
    public String getShortName() {
        return this.shortName;
    }

    @Override // co.vine.android.api.FoursquareVenue.Category
    public String getLeafName() {
        return this.leafName;
    }

    @Override // co.vine.android.api.FoursquareVenue.Category
    public String getParentName() {
        return this.parentName;
    }

    public String toString() {
        return "Category{shortName=" + this.shortName + ", leafName=" + this.leafName + ", parentName=" + this.parentName + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FoursquareVenue.Category)) {
            return false;
        }
        FoursquareVenue.Category that = (FoursquareVenue.Category) o;
        if (this.shortName != null ? this.shortName.equals(that.getShortName()) : that.getShortName() == null) {
            if (this.leafName != null ? this.leafName.equals(that.getLeafName()) : that.getLeafName() == null) {
                if (this.parentName == null) {
                    if (that.getParentName() == null) {
                        return true;
                    }
                } else if (this.parentName.equals(that.getParentName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int h = 1 * 1000003;
        return ((((h ^ (this.shortName == null ? 0 : this.shortName.hashCode())) * 1000003) ^ (this.leafName == null ? 0 : this.leafName.hashCode())) * 1000003) ^ (this.parentName != null ? this.parentName.hashCode() : 0);
    }
}
