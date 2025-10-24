package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* loaded from: classes.dex */
public class User implements Serializable {
    private static final long serialVersionUID = 4663450696842173958L;

    @SerializedName("contributors_enabled")
    public final boolean contributorsEnabled;

    @SerializedName("created_at")
    public final String createdAt;

    @SerializedName("default_profile")
    public final boolean defaultProfile;

    @SerializedName("default_profile_image")
    public final boolean defaultProfileImage;

    @SerializedName("description")
    public final String description;

    @SerializedName("email")
    public final String email;

    @SerializedName("entities")
    public final UserEntities entities;

    @SerializedName("favourites_count")
    public final int favouritesCount;

    @SerializedName("follow_request_sent")
    public final boolean followRequestSent;

    @SerializedName("followers_count")
    public final int followersCount;

    @SerializedName("friends_count")
    public final int friendsCount;

    @SerializedName("geo_enabled")
    public final boolean geoEnabled;

    @SerializedName("id")
    public final long id;

    @SerializedName("id_str")
    public final String idStr;

    @SerializedName("is_translator")
    public final boolean isTranslator;

    @SerializedName("lang")
    public final String lang;

    @SerializedName("listed_count")
    public final int listedCount;

    @SerializedName("location")
    public final String location;

    @SerializedName("name")
    public final String name;

    @SerializedName("profile_background_color")
    public final String profileBackgroundColor;

    @SerializedName("profile_background_image_url")
    public final String profileBackgroundImageUrl;

    @SerializedName("profile_background_image_url_https")
    public final String profileBackgroundImageUrlHttps;

    @SerializedName("profile_background_tile")
    public final boolean profileBackgroundTile;

    @SerializedName("profile_banner_url")
    public final String profileBannerUrl;

    @SerializedName("profile_image_url")
    public final String profileImageUrl;

    @SerializedName("profile_image_url_https")
    public final String profileImageUrlHttps;

    @SerializedName("profile_link_color")
    public final String profileLinkColor;

    @SerializedName("profile_sidebar_border_color")
    public final String profileSidebarBorderColor;

    @SerializedName("profile_sidebar_fill_color")
    public final String profileSidebarFillColor;

    @SerializedName("profile_text_color")
    public final String profileTextColor;

    @SerializedName("profile_use_background_image")
    public final boolean profileUseBackgroundImage;

    @SerializedName("protected")
    public final boolean protectedUser;

    @SerializedName("screen_name")
    public final String screenName;

    @SerializedName("show_all_inline_media")
    public final boolean showAllInlineMedia;

    @SerializedName("status")
    public final Tweet status;

    @SerializedName("statuses_count")
    public final int statusesCount;

    @SerializedName("time_zone")
    public final String timeZone;

    @SerializedName("url")
    public final String url;

    @SerializedName("utc_offset")
    public final int utcOffset;

    @SerializedName("verified")
    public final boolean verified;

    @SerializedName("withheld_in_countries")
    public final String withheldInCountries;

    @SerializedName("withheld_scope")
    public final String withheldScope;
}
