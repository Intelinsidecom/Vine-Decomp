package co.vine.android.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VineUploadParsers {
    public static PostInfo parsePostInfo(JsonParser p) throws IOException {
        boolean postToTwitter = false;
        boolean postToFacebook = false;
        boolean postToTumblr = false;
        long channelId = -1;
        String foursquareVenueId = "";
        long postId = -1;
        long created = 0;
        String description = "";
        String message = "";
        String videoUrl = "";
        String thumbUrl = "";
        ArrayList<VineEntity> entities = null;
        ArrayList<VineRecipient> recipients = null;
        ArrayList<VineSource> sources = null;
        boolean hidden = false;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case VALUE_STRING:
                    if ("description".equals(p.getCurrentName())) {
                        description = p.getText();
                    }
                    if ("message".equals(p.getCurrentName())) {
                        message = p.getText();
                    }
                    if ("videoUrl".equals(p.getCurrentName())) {
                        videoUrl = p.getText();
                    }
                    if ("thumbUrl".equals(p.getCurrentName())) {
                        thumbUrl = p.getText();
                    }
                    if (!"foursquareVenueId".equals(p.getCurrentName())) {
                        break;
                    } else {
                        foursquareVenueId = p.getText();
                        break;
                    }
                case VALUE_FALSE:
                case VALUE_TRUE:
                    if ("postToTwitter".equals(p.getCurrentName())) {
                        postToTwitter = p.getBooleanValue();
                        break;
                    } else if ("postToFacebook".equals(p.getCurrentName())) {
                        postToFacebook = p.getBooleanValue();
                        break;
                    } else if ("postToTumblr".equals(p.getCurrentName())) {
                        postToTumblr = p.getBooleanValue();
                        break;
                    } else if (!"hidden".equals(p.getCurrentName())) {
                        break;
                    } else {
                        hidden = p.getBooleanValue();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    if ("channelId".equals(p.getCurrentName())) {
                        channelId = p.getLongValue();
                        break;
                    } else if ("created".equals(p.getCurrentName())) {
                        created = p.getLongValue();
                        break;
                    } else if (!"postId".equals(p.getCurrentName())) {
                        break;
                    } else {
                        postId = p.getLongValue();
                        break;
                    }
                case START_ARRAY:
                    String name = p.getCurrentName();
                    if ("entities".equals(name)) {
                        JsonToken t2 = p.nextToken();
                        while (t2 != null && t2 != JsonToken.END_ARRAY) {
                            VineEntity entity = VineParsers.parseEntity(p);
                            if (entities == null) {
                                entities = new ArrayList<>();
                            }
                            if (entity != null) {
                                entities.add(entity);
                            }
                            t2 = p.nextToken();
                        }
                        break;
                    } else if ("recipients".equals(name)) {
                        JsonToken t3 = p.nextToken();
                        while (t3 != null && t3 != JsonToken.END_ARRAY) {
                            VineRecipient recipient = VineRecipient.fromJson(p);
                            if (recipients == null) {
                                recipients = new ArrayList<>();
                            }
                            if (recipient != null) {
                                recipients.add(recipient);
                            }
                            t3 = p.nextToken();
                        }
                        break;
                    } else if ("sources".equals(name)) {
                        JsonToken t4 = p.nextToken();
                        while (t4 != null && t4 != JsonToken.END_ARRAY) {
                            VineSource source = VineSource.fromJson(p);
                            if (sources == null) {
                                sources = new ArrayList<>();
                            }
                            if (source != null) {
                                sources.add(source);
                            }
                            t4 = p.nextToken();
                        }
                        break;
                    } else {
                        p.skipChildren();
                        break;
                    }
            }
            t = p.nextToken();
        }
        return new PostInfo(description, postToTwitter, postToFacebook, postToTumblr, foursquareVenueId, channelId, entities, message, postId, videoUrl, thumbUrl, created, recipients, hidden, sources);
    }
}
