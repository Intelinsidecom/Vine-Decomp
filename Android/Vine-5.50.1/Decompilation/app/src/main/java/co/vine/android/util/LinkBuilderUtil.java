package co.vine.android.util;

import android.net.Uri;
import android.os.Bundle;
import co.vine.android.client.VineAPI;
import com.mobileapptracker.MATEvent;

/* loaded from: classes.dex */
public final class LinkBuilderUtil {
    public static String buildUrl(int type, Bundle data) {
        StringBuilder url;
        switch (type) {
            case 1:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "graph");
                break;
            case 2:
            case 10:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "users", Long.valueOf(data.getLong("profile_id")));
                break;
            case 3:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "users", Long.valueOf(data.getLong("profile_id")), "likes");
                break;
            case 4:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "users", "trending");
                break;
            case 5:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "popular");
                break;
            case 6:
            case 16:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "tags", data.getString("tag_name"));
                VineAPI.addParam(url, "sort", type == 16 ? "recent" : "top");
                break;
            case 7:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "posts", Long.valueOf(data.getLong("post_id", -1L)));
                break;
            case 8:
            case 9:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "channels", data.getString("channelId"), data.getString("sort"));
                break;
            case 11:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines");
                Uri uri = (Uri) data.getParcelable("data");
                if (uri != null) {
                    for (String pathSegment : uri.getPathSegments()) {
                        url = VineAPI.buildUponUrl(url.toString(), pathSegment);
                    }
                    break;
                }
                break;
            case 12:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            default:
                throw new IllegalArgumentException("Tried to fetch timeline with unsupported type " + type);
            case 13:
                StringBuilder url2 = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines");
                url = VineAPI.buildUponUrl(url2.toString(), "venues");
                Uri uri2 = (Uri) data.getParcelable("data");
                if (uri2 != null) {
                    for (String pathSegment2 : uri2.getPathSegments()) {
                        url = VineAPI.buildUponUrl(url.toString(), pathSegment2);
                    }
                    break;
                }
                break;
            case 14:
            case 15:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", MATEvent.SEARCH, "posts");
                VineAPI.addParam(url, "sort", type == 15 ? "recent" : "top");
                break;
            case 17:
            case 18:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines");
                Uri uri3 = (Uri) data.getParcelable("data");
                if (uri3 != null) {
                    for (String pathSegment3 : uri3.getPathSegments()) {
                        url = VineAPI.buildUponUrl(url.toString(), pathSegment3);
                    }
                }
                VineAPI.addParam(url, "sort", type == 18 ? "recent" : "top");
                break;
            case 30:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "welcome");
                break;
            case 36:
                url = VineAPI.buildUponUrl("https://api.vineapp.com", "timelines", "genius", "me", "channels", data.getString("channelId"));
                break;
        }
        return url.toString();
    }
}
