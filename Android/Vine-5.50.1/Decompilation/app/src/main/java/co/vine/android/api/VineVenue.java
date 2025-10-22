package co.vine.android.api;

import co.vine.android.util.FlexibleStringHashMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class VineVenue implements Externalizable {
    private static final long serialVersionUID = 362104582584848336L;
    public String address;
    public String categoryIconUrl;
    public String categoryId;
    public String categoryName;
    public String city;
    public String countryCode;
    public int displayType;
    public String state;
    public String venueName;
    public static final Pattern FOURSQUARE_CATEGORY = Pattern.compile("/([^/]+?)/([^/]+?)_\\d*?.png");
    private static final HashMap<String, Integer> venueMap = new FlexibleStringHashMap();

    static {
        venueMap.put("vine hq", 1);
        venueMap.put("verse hq", 1);
        venueMap.put("twitter nyc", 9);
        venueMap.put("twitter, inc", 9);
        venueMap.put("square, inc", 8);
        venueMap.put("squarespace hq", 7);
        venueMap.put("jetsetter", 19);
        venueMap.put("gilt city", 4);
        venueMap.put("gilt groupe", 4);
        venueMap.put("facebook", 5);
        venueMap.put("ebay", 3);
        venueMap.put("big human", 2);
        venueMap.put("arts_entertainment", 11);
        venueMap.put("education", 18);
        venueMap.put("event", 11);
        venueMap.put("food", 12);
        venueMap.put("home", 10);
        venueMap.put("nightlife", 13);
        venueMap.put("parks_outdoors", 14);
        venueMap.put("travel", 17);
        venueMap.put("shops", 16);
        venueMap.put("nightlife", 13);
        venueMap.put("tech startup", 15);
    }

    public VineVenue() {
    }

    public VineVenue(String categoryIconUrl, String categoryName, String categoryId, String city, String countryCode, String venueName, String state, String address) {
        String venueNameLower = venueName != null ? venueName.toLowerCase() : null;
        String categoryNameLower = categoryName != null ? categoryName.toLowerCase() : null;
        this.categoryIconUrl = categoryIconUrl;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.city = city;
        this.countryCode = countryCode;
        this.venueName = venueName;
        this.state = state;
        this.address = address;
        Integer display = venueMap.get(venueNameLower);
        if ((display == null || display.intValue() <= 0) && (((display = venueMap.get(categoryNameLower)) == null || display.intValue() <= 0) && categoryIconUrl != null)) {
            Matcher m = FOURSQUARE_CATEGORY.matcher(categoryIconUrl);
            if (m.find()) {
                String urlCategoryName = m.group(2).toLowerCase();
                display = venueMap.get(urlCategoryName);
                if (display == null || display.intValue() <= 0) {
                    String urlParentName = m.group(1).toLowerCase();
                    display = venueMap.get(urlParentName);
                }
            }
        }
        this.displayType = ((display == null || display.intValue() <= 0) ? 20 : display).intValue();
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.categoryIconUrl = (String) objectInput.readObject();
        this.categoryName = (String) objectInput.readObject();
        this.categoryId = (String) objectInput.readObject();
        this.city = (String) objectInput.readObject();
        this.countryCode = (String) objectInput.readObject();
        this.venueName = (String) objectInput.readObject();
        this.state = (String) objectInput.readObject();
        this.address = (String) objectInput.readObject();
        this.displayType = objectInput.readInt();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.categoryIconUrl);
        objectOutput.writeObject(this.categoryName);
        objectOutput.writeObject(this.categoryId);
        objectOutput.writeObject(this.city);
        objectOutput.writeObject(this.countryCode);
        objectOutput.writeObject(this.venueName);
        objectOutput.writeObject(this.state);
        objectOutput.writeObject(this.address);
        objectOutput.writeInt(this.displayType);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineVenue vineVenue = (VineVenue) o;
        if (this.displayType != vineVenue.displayType) {
            return false;
        }
        if (this.address == null ? vineVenue.address != null : !this.address.equals(vineVenue.address)) {
            return false;
        }
        if (this.categoryIconUrl == null ? vineVenue.categoryIconUrl != null : !this.categoryIconUrl.equals(vineVenue.categoryIconUrl)) {
            return false;
        }
        if (this.categoryId == null ? vineVenue.categoryId != null : !this.categoryId.equals(vineVenue.categoryId)) {
            return false;
        }
        if (this.categoryName == null ? vineVenue.categoryName != null : !this.categoryName.equals(vineVenue.categoryName)) {
            return false;
        }
        if (this.city == null ? vineVenue.city != null : !this.city.equals(vineVenue.city)) {
            return false;
        }
        if (this.countryCode == null ? vineVenue.countryCode != null : !this.countryCode.equals(vineVenue.countryCode)) {
            return false;
        }
        if (this.state == null ? vineVenue.state != null : !this.state.equals(vineVenue.state)) {
            return false;
        }
        if (this.venueName != null) {
            if (this.venueName.equals(vineVenue.venueName)) {
                return true;
            }
        } else if (vineVenue.venueName == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.categoryIconUrl != null ? this.categoryIconUrl.hashCode() : 0;
        return (((((((((((((((result * 31) + (this.categoryName != null ? this.categoryName.hashCode() : 0)) * 31) + (this.categoryId != null ? this.categoryId.hashCode() : 0)) * 31) + (this.city != null ? this.city.hashCode() : 0)) * 31) + (this.countryCode != null ? this.countryCode.hashCode() : 0)) * 31) + (this.venueName != null ? this.venueName.hashCode() : 0)) * 31) + (this.state != null ? this.state.hashCode() : 0)) * 31) + (this.address != null ? this.address.hashCode() : 0)) * 31) + this.displayType;
    }
}
