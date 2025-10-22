package co.vine.android.api;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PostInfo {
    public final String caption;
    public long channelId;
    public long created;
    public final ArrayList<VineEntity> entities;
    public String foursquareVenueId;
    public boolean hidden;
    public final String message;
    public final long postId;
    public boolean postToFacebook;
    public boolean postToTumblr;
    public boolean postToTwitter;
    public ArrayList<VineRecipient> recipients;
    public final String sharedPostThumbUrl;
    public final String sharedPostVideoUrl;
    public ArrayList<VineSource> sources;

    public PostInfo(String caption, boolean postToTwitter, boolean postToFacebook, boolean postToTumblr, String foursquareVenueId, long channelId, ArrayList<VineEntity> entities, String message, long postId, String videoUrl, String thumbUrl, long created, ArrayList<VineRecipient> recipients, boolean hidden, ArrayList<VineSource> sources) {
        this.caption = caption == null ? "" : caption;
        this.postToTwitter = postToTwitter;
        this.postToFacebook = postToFacebook;
        this.postToTumblr = postToTumblr;
        this.entities = entities;
        this.channelId = channelId;
        this.foursquareVenueId = foursquareVenueId;
        this.created = created;
        this.message = message;
        this.postId = postId;
        this.recipients = recipients;
        this.sharedPostVideoUrl = videoUrl;
        this.sharedPostThumbUrl = thumbUrl;
        this.hidden = hidden;
        this.sources = sources;
    }

    public PostInfo(String caption, boolean postToTwitter, boolean postToFacebook, boolean postToTumblr, String foursquareVenueId, long channelId, ArrayList<VineEntity> entities, String message, long postId, String videoUrl, String thumbUrl, long created, ArrayList<VineRecipient> recipients) {
        this.caption = caption == null ? "" : caption;
        this.postToTwitter = postToTwitter;
        this.postToFacebook = postToFacebook;
        this.postToTumblr = postToTumblr;
        this.entities = entities;
        this.channelId = channelId;
        this.foursquareVenueId = foursquareVenueId;
        this.created = created;
        this.message = message;
        this.postId = postId;
        this.recipients = recipients;
        this.sharedPostVideoUrl = videoUrl;
        this.sharedPostThumbUrl = thumbUrl;
        this.hidden = false;
    }

    public String toString() {
        try {
            return toJson().toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", this.caption);
        jsonObject.put("postToTwitter", this.postToTwitter);
        jsonObject.put("postToFacebook", this.postToFacebook);
        jsonObject.put("postToTumblr", this.postToTumblr);
        jsonObject.put("channelId", this.channelId);
        jsonObject.put("foursquareVenueId", this.foursquareVenueId);
        if (!TextUtils.isEmpty(this.sharedPostVideoUrl)) {
            jsonObject.put("videoUrl", this.sharedPostVideoUrl);
        }
        if (!TextUtils.isEmpty(this.sharedPostThumbUrl)) {
            jsonObject.put("thumbUrl", this.sharedPostThumbUrl);
        }
        if (this.entities != null && !this.entities.isEmpty()) {
            jsonObject.put("entities", entitiesToJsonArray(this.entities));
        }
        if (this.recipients != null && !this.recipients.isEmpty()) {
            jsonObject.put("recipients", recipientsToJsonArray(this.recipients));
        }
        jsonObject.put("message", this.message);
        jsonObject.put("postId", this.postId);
        jsonObject.put("created", this.created);
        jsonObject.put("hidden", this.hidden);
        if (this.sources != null && !this.sources.isEmpty()) {
            jsonObject.put("sources", sourcesToJsonArray(this.sources));
        }
        return jsonObject;
    }

    public static JSONArray entitiesToJsonArray(ArrayList<VineEntity> entities) throws JSONException {
        JSONArray array = new JSONArray();
        Iterator<VineEntity> it = entities.iterator();
        while (it.hasNext()) {
            VineEntity entity = it.next();
            array.put(entity.toJsonObject());
        }
        return array;
    }

    public static JSONArray recipientsToJsonArray(ArrayList<VineRecipient> recipients) throws JSONException {
        JSONArray array = new JSONArray();
        Iterator<VineRecipient> it = recipients.iterator();
        while (it.hasNext()) {
            VineRecipient recipient = it.next();
            array.put(recipient.toJsonObject());
        }
        return array;
    }

    public static JSONObject recipientsToVmShare(ArrayList<VineRecipient> recipients) throws JSONException {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        Iterator<VineRecipient> it = recipients.iterator();
        while (it.hasNext()) {
            VineRecipient recipient = it.next();
            array.put(recipient.toJsonObject());
        }
        object.put("to", array);
        return object;
    }

    public static JSONArray sourcesToJsonArray(ArrayList<VineSource> sources) throws JSONException {
        JSONArray array = new JSONArray();
        Iterator<VineSource> it = sources.iterator();
        while (it.hasNext()) {
            VineSource source = it.next();
            array.put(source.toJsonObject());
        }
        return array;
    }
}
