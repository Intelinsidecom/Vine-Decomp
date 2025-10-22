package co.vine.android.api.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.ArrayList;

@JsonObject
/* loaded from: classes.dex */
public class FoursquareResponse {

    @JsonField(name = {"response"})
    public Response response;

    @JsonObject
    public static class Category {

        @JsonField(name = {"icon"})
        public Icon icon;

        @JsonField(name = {"shortName"})
        public String shortName;
    }

    @JsonObject
    public static class Icon {

        @JsonField(name = {"prefix"})
        public String prefix;
    }

    @JsonObject
    public static class Location {

        @JsonField(name = {"address"})
        public String address;

        @JsonField(name = {"cc"})
        public String cc;

        @JsonField(name = {"city"})
        public String city;

        @JsonField(name = {"country"})
        public String country;

        @JsonField(name = {"distance"})
        public double distance;

        @JsonField(name = {"lat"})
        public double lat;

        @JsonField(name = {"lng"})
        public double lng;

        @JsonField(name = {"postalCode"})
        public String postalCode;

        @JsonField(name = {"state"})
        public String state;
    }

    @JsonObject
    public static class Response {

        @JsonField(name = {"venues"})
        public ArrayList<Venue> venues;
    }

    @JsonObject
    public static class Venue {

        @JsonField(name = {"categories"})
        public ArrayList<Category> categories;

        @JsonField(name = {"id"})
        public String id;

        @JsonField(name = {"location"})
        public Location location;

        @JsonField(name = {"name"})
        public String name;
    }
}
