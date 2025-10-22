package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class VineRecipient implements Parcelable, Externalizable {
    public static final Parcelable.Creator<VineRecipient> CREATOR = new Parcelable.Creator<VineRecipient>() { // from class: co.vine.android.api.VineRecipient.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRecipient createFromParcel(Parcel in) {
            return new VineRecipient(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRecipient[] newArray(int size) {
            return new VineRecipient[size];
        }
    };
    public String avatarUrl;
    public int color;
    public long contactId;
    private String display;
    public long friendIndex;
    public String key;
    public long lastFriendRefresh = -1;
    public long recipientId;
    public int sectionIndex;
    public String sectionTitle;
    public boolean twitterHidden;
    public String twitterScreenname;
    public long userId;
    public String value;
    public boolean verified;

    private VineRecipient(String key, String value, long userId, String display, int color, long recipientId) {
        this.key = key;
        this.value = value;
        this.userId = userId;
        this.color = color;
        this.recipientId = recipientId;
        setDisplay(display);
    }

    public static VineRecipient fromJson(JsonParser p) throws IOException {
        String key = null;
        String value = null;
        String display = null;
        String recipientIdString = null;
        long userId = 0;
        JsonToken t = p.nextToken();
        while (t != null && t != JsonToken.END_OBJECT) {
            switch (t) {
                case VALUE_STRING:
                    String cn = p.getCurrentName();
                    if ("phoneNumber".equals(cn) || "email".equals(cn)) {
                        key = cn;
                        value = p.getText();
                        break;
                    } else if ("recipientId".equals(cn)) {
                        recipientIdString = p.getText();
                        break;
                    } else if (!"display".equals(cn)) {
                        break;
                    } else {
                        display = p.getText();
                        break;
                    }
                    break;
                case VALUE_NUMBER_INT:
                    String cn2 = p.getCurrentName();
                    if (!"userId".equals(cn2)) {
                        break;
                    } else {
                        key = cn2;
                        userId = p.getLongValue();
                        break;
                    }
            }
            t = p.nextToken();
        }
        long recipientId = -1;
        if (!TextUtils.isEmpty(recipientIdString)) {
            recipientId = Long.valueOf(recipientIdString).longValue();
        }
        return new VineRecipient(key, value, userId, display, 0, recipientId);
    }

    public static VineRecipient fromUser(long userId) {
        return fromUser(null, userId, 0, -1L);
    }

    public static VineRecipient fromUser(String display, long userId, int color, long recipientId) {
        return new VineRecipient("userId", null, userId, display, color, recipientId);
    }

    public static VineRecipient fromEmail(String display, long userId, String email, long recipientId) {
        return new VineRecipient("email", email, userId, display, -1, recipientId);
    }

    public static VineRecipient fromPhone(String display, long userId, String phone, long recipientId) {
        return new VineRecipient("phoneNumber", phone, userId, display, -1, recipientId);
    }

    public boolean isFromUser() {
        return "userId".equals(this.key);
    }

    public boolean isFromPhone() {
        return "phoneNumber".equals(this.key);
    }

    public boolean isFromEmail() {
        return "email".equals(this.key);
    }

    public VineRecipient(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
        this.userId = in.readLong();
        this.recipientId = in.readLong();
        setDisplay(in.readString());
        this.color = in.readInt();
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if ("userId".equals(this.key)) {
            jsonObject.put(this.key, this.userId);
        } else {
            jsonObject.put(this.key, this.value);
        }
        if (this.recipientId > 0) {
            jsonObject.put("recipientId", String.valueOf(this.recipientId));
        }
        if (getDisplay() != null) {
            jsonObject.put("display", getDisplay());
        }
        return jsonObject;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.key);
        out.writeString(this.value);
        out.writeLong(this.userId);
        out.writeLong(this.recipientId);
        out.writeString(getDisplay());
        out.writeInt(this.color);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.key = (String) objectInput.readObject();
        this.value = (String) objectInput.readObject();
        this.userId = objectInput.readLong();
        this.recipientId = objectInput.readLong();
        setDisplay(objectInput.readLine());
        this.color = objectInput.readInt();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.key);
        objectOutput.writeObject(this.value);
        objectOutput.writeLong(this.userId);
        objectOutput.writeLong(this.recipientId);
        objectOutput.writeChars(getDisplay());
        objectOutput.writeInt(this.color);
    }

    public String getTextSortKey() {
        return TextUtils.isEmpty(this.display) ? "" : this.display.substring(0, 1).toUpperCase();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineRecipient that = (VineRecipient) o;
        if (getDisplay() == null ? that.getDisplay() != null : !getDisplay().equals(that.getDisplay())) {
            return false;
        }
        if (this.key == null ? that.key != null : !this.key.equals(that.key)) {
            return false;
        }
        if (this.value == null ? that.value != null : !this.value.equals(that.value)) {
            return false;
        }
        return this.userId == that.userId && this.recipientId == that.recipientId;
    }

    public int hashCode() {
        int result = this.key != null ? this.key.hashCode() : 0;
        return (((((result * 31) + (this.value != null ? this.value.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + ((int) (this.recipientId ^ (this.recipientId >>> 32)));
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        if (display == null) {
            display = "";
        }
        this.display = display;
    }
}
