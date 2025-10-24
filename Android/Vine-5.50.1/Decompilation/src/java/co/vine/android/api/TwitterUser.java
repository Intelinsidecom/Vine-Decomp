package co.vine.android.api;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
/* loaded from: classes.dex */
public class TwitterUser {

    @JsonField(name = {"default_profile_image"})
    public boolean defaultProfileImage;

    @JsonField(name = {"description"})
    public String description;

    @JsonField(name = {"location"})
    public String location;

    @JsonField(name = {"name"})
    public String name;

    @JsonField(name = {"profile_image_url"})
    public String profileUrl;

    @JsonField(name = {"screen_name"})
    public String screenName;

    @JsonField(name = {"url"})
    public String url;

    @JsonField(name = {"id"})
    public long userId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TwitterUser that = (TwitterUser) o;
        if (this.defaultProfileImage == that.defaultProfileImage && this.userId == that.userId) {
            if (this.description == null ? that.description != null : !this.description.equals(that.description)) {
                return false;
            }
            if (this.location == null ? that.location != null : !this.location.equals(that.location)) {
                return false;
            }
            if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
                return false;
            }
            if (this.profileUrl == null ? that.profileUrl != null : !this.profileUrl.equals(that.profileUrl)) {
                return false;
            }
            if (this.screenName == null ? that.screenName != null : !this.screenName.equals(that.screenName)) {
                return false;
            }
            if (this.url != null) {
                if (this.url.equals(that.url)) {
                    return true;
                }
            } else if (that.url == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.name != null ? this.name.hashCode() : 0;
        return (((((((((((((result * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.url != null ? this.url.hashCode() : 0)) * 31) + (this.profileUrl != null ? this.profileUrl.hashCode() : 0)) * 31) + (this.defaultProfileImage ? 1 : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)));
    }
}
