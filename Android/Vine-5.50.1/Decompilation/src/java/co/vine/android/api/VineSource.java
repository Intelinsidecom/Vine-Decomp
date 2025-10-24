package co.vine.android.api;

import android.os.Parcelable;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class VineSource implements Parcelable {
    public abstract int getContentType();

    public abstract String getDescription();

    public abstract float getDuration();

    public abstract float getOffsetInPosts();

    public abstract float getOffsetInSource();

    public abstract long getPostId();

    public abstract String getSourcePostId();

    public abstract String getThumbnailUrl();

    public abstract String getUsername();

    public static VineSource create(int contentType, long postId, String username, String description, String thumbnailUrl, String sourcePostId, float offsetInPosts, float offSetInSource, float duration) {
        if (description == null) {
            description = "";
        }
        return new AutoParcel_VineSource(contentType, postId, username, description, thumbnailUrl, duration, sourcePostId, offsetInPosts, offSetInSource);
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sourcePostId", getSourcePostId());
        jsonObject.put("duration", TimeUnit.MILLISECONDS.toSeconds((long) getDuration()));
        jsonObject.put("contentType", getContentType());
        jsonObject.put("offsetInPost", TimeUnit.MILLISECONDS.toSeconds((long) getOffsetInPosts()));
        jsonObject.put("offsetInSource", TimeUnit.MILLISECONDS.toSeconds((long) getOffsetInSource()));
        return jsonObject;
    }

    public static VineSource fromJson(JsonParser p) throws IOException {
        String sourcePostId = null;
        int contenType = 0;
        float duration = 0.0f;
        float offsetInPost = 0.0f;
        float offsetInSource = 0.0f;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case VALUE_STRING:
                    if (!"sourcePostId".equals(p.getCurrentName())) {
                        break;
                    } else {
                        sourcePostId = p.getText();
                        break;
                    }
                case VALUE_NUMBER_INT:
                    if (!"contentType".equals(p.getCurrentName())) {
                        break;
                    } else {
                        contenType = p.getIntValue();
                        break;
                    }
                case VALUE_NUMBER_FLOAT:
                    String cn = p.getCurrentName();
                    if ("duration".equals(cn)) {
                        duration = p.getFloatValue();
                        break;
                    } else if ("offsetInPost".equals(cn)) {
                        offsetInPost = p.getFloatValue();
                        break;
                    } else if (!"offsetInSource".equals(cn)) {
                        break;
                    } else {
                        offsetInSource = p.getFloatValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        return create(contenType, 0L, "", "", "", sourcePostId, offsetInPost, offsetInSource, duration);
    }
}
