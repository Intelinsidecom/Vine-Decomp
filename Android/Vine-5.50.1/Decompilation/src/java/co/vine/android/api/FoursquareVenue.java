package co.vine.android.api;

import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class FoursquareVenue implements Parcelable {
    public abstract String getAddress();

    public abstract Category getCategory();

    public abstract String getName();

    public abstract String getVenueId();

    public static abstract class Category {
        public abstract String getLeafName();

        public abstract String getParentName();

        public abstract String getShortName();

        public static Category create(String shortName, String leafName, String parentName) {
            return new AutoParcel_FoursquareVenue_Category(shortName, leafName, parentName);
        }
    }

    public static FoursquareVenue create(String venueId, String name, String address, Category category) {
        return new AutoParcel_FoursquareVenue(venueId, name, address, category);
    }
}
