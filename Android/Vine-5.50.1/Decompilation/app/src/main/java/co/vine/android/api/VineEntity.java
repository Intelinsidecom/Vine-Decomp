package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class VineEntity implements Parcelable, Externalizable, Comparable<VineEntity> {
    public static final Parcelable.Creator<VineEntity> CREATOR = new Parcelable.Creator<VineEntity>() { // from class: co.vine.android.api.VineEntity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineEntity createFromParcel(Parcel in) {
            return new VineEntity(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineEntity[] newArray(int size) {
            return new VineEntity[size];
        }
    };
    private static final long serialVersionUID = -4017215936161446091L;
    public boolean adjusted;
    public int end;
    public long id;
    public String link;
    public int start;
    public String title;
    public String type;

    public VineEntity() {
        this.adjusted = false;
    }

    public VineEntity(String type, String title, String link, int start, int end, long id) {
        this.adjusted = false;
        this.type = type;
        this.title = title;
        this.link = link;
        this.start = start;
        this.end = end;
        this.id = id;
        this.adjusted = false;
    }

    public VineEntity(VineEntity entity) {
        this(entity.type, entity.title, entity.link, entity.start, entity.end, entity.id);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.type = (String) objectInput.readObject();
        this.title = (String) objectInput.readObject();
        this.link = (String) objectInput.readObject();
        this.start = objectInput.readInt();
        this.end = objectInput.readInt();
        this.id = objectInput.readLong();
        this.adjusted = objectInput.readBoolean();
    }

    public VineEntity duplicate() {
        return new VineEntity(this.type, this.title, this.link, this.start, this.end, this.id);
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.type);
        objectOutput.writeObject(this.title);
        objectOutput.writeObject(this.link);
        objectOutput.writeInt(this.start);
        objectOutput.writeInt(this.end);
        objectOutput.writeLong(this.id);
        objectOutput.writeBoolean(this.adjusted);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineEntity entity = (VineEntity) o;
        if (this.adjusted == entity.adjusted && this.end == entity.end && this.id == entity.id && this.start == entity.start) {
            if (this.link == null ? entity.link != null : !this.link.equals(entity.link)) {
                return false;
            }
            if (this.title == null ? entity.title != null : !this.title.equals(entity.title)) {
                return false;
            }
            if (this.type != null) {
                if (this.type.equals(entity.type)) {
                    return true;
                }
            } else if (entity.type == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        return (((((((((((result * 31) + (this.title != null ? this.title.hashCode() : 0)) * 31) + (this.link != null ? this.link.hashCode() : 0)) * 31) + this.start) * 31) + this.end) * 31) + ((int) (this.id ^ (this.id >>> 32)))) * 31) + (this.adjusted ? 1 : 0);
    }

    @Override // java.lang.Comparable
    public int compareTo(VineEntity vineEntity) {
        return Integer.valueOf(this.end).compareTo(Integer.valueOf(vineEntity.end));
    }

    public boolean isUserType() {
        return PropertyConfiguration.USER.equals(this.type) || "mention".equals(this.type) || "post".equals(this.type);
    }

    public boolean isTagType() {
        return "tag".equals(this.type);
    }

    public boolean isUserListType() {
        return "userList".equals(this.type);
    }

    public boolean isCommentListType() {
        return "commentList".equals(this.type);
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", this.id);
        object.put("type", this.type);
        object.put("text", this.title);
        JSONArray range = new JSONArray();
        range.put(this.start);
        range.put(this.end);
        object.put("range", range);
        return object;
    }

    public VineEntity(Parcel in) {
        this.adjusted = false;
        this.type = in.readString();
        this.title = in.readString();
        this.link = in.readString();
        this.start = in.readInt();
        this.end = in.readInt();
        this.id = in.readLong();
        this.adjusted = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.type);
        out.writeString(this.title);
        out.writeString(this.link);
        out.writeInt(this.start);
        out.writeInt(this.end);
        out.writeLong(this.id);
        out.writeInt(this.adjusted ? 1 : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void generateEntityLinkForComment() {
        if (isUserType()) {
            this.link = generateUserLink(this.id);
        } else if (isTagType() && !TextUtils.isEmpty(this.title)) {
            this.link = "vine://tag/" + this.title.replace("#", "");
        }
    }

    public static String generateUserLink(long id) {
        return "vine://user-id/" + id;
    }

    public static class Range {
        public VineEntity entity;
        public ForegroundColorSpan span;
        public int upper;

        public Range(int upper, VineEntity entity, ForegroundColorSpan span) {
            this.upper = upper;
            this.entity = entity;
            this.span = span;
        }
    }
}
