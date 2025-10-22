package co.vine.android.api;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class VineRepost implements Externalizable {
    private static final long serialVersionUID = -6283845278265871065L;
    public String avatarUrl;
    public String description;
    public String location;
    public long postId;
    public int priv;
    public long repostId;
    public int unflaggable;
    public long userId;
    public String username;
    public int verified;

    public VineRepost() {
    }

    public VineRepost(String username, String avatarUrl, String location, String description, long userId, long postId, long repostId, int verified, int priv, int unflaggable) {
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.location = location;
        this.description = description;
        this.userId = userId;
        this.postId = postId;
        this.repostId = repostId;
        this.verified = verified;
        this.priv = priv;
        this.unflaggable = unflaggable;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.username = (String) objectInput.readObject();
        this.avatarUrl = (String) objectInput.readObject();
        this.location = (String) objectInput.readObject();
        this.description = (String) objectInput.readObject();
        this.userId = objectInput.readLong();
        this.postId = objectInput.readLong();
        this.repostId = objectInput.readLong();
        this.verified = objectInput.readInt();
        this.priv = objectInput.readInt();
        this.unflaggable = objectInput.readInt();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.username);
        objectOutput.writeObject(this.avatarUrl);
        objectOutput.writeObject(this.location);
        objectOutput.writeObject(this.description);
        objectOutput.writeLong(this.userId);
        objectOutput.writeLong(this.postId);
        objectOutput.writeLong(this.repostId);
        objectOutput.writeInt(this.verified);
        objectOutput.writeInt(this.priv);
        objectOutput.writeInt(this.unflaggable);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineRepost that = (VineRepost) o;
        if (this.postId == that.postId && this.priv == that.priv && this.repostId == that.repostId && this.unflaggable == that.unflaggable && this.userId == that.userId && this.verified == that.verified) {
            if (this.avatarUrl == null ? that.avatarUrl != null : !this.avatarUrl.equals(that.avatarUrl)) {
                return false;
            }
            if (this.description == null ? that.description != null : !this.description.equals(that.description)) {
                return false;
            }
            if (this.location == null ? that.location != null : !this.location.equals(that.location)) {
                return false;
            }
            if (this.username != null) {
                if (this.username.equals(that.username)) {
                    return true;
                }
            } else if (that.username == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.username != null ? this.username.hashCode() : 0;
        return (((((((((((((((((result * 31) + (this.avatarUrl != null ? this.avatarUrl.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + ((int) (this.postId ^ (this.postId >>> 32)))) * 31) + ((int) (this.repostId ^ (this.repostId >>> 32)))) * 31) + this.verified) * 31) + this.priv) * 31) + this.unflaggable;
    }
}
