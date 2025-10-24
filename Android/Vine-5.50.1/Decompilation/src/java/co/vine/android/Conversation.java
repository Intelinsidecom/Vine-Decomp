package co.vine.android;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Conversation extends JSONObject {
    public Conversation(Builder b) throws JSONException {
        put("connected", b.connected);
        put("typing", b.typing);
        if (b.lastMessageId > 0) {
            put("last_message_id", b.lastMessageId);
        }
    }

    public static class Builder {
        private boolean connected = false;
        private boolean typing = false;
        private long lastMessageId = 0;

        public Builder setConnected() {
            this.connected = true;
            return this;
        }

        public Builder setTyping(boolean isTyping) {
            this.typing = isTyping;
            return this;
        }

        public Builder setLastMessageId(long lastMessageId) {
            this.lastMessageId = lastMessageId;
            return this;
        }

        public Conversation build() {
            try {
                return new Conversation(this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
