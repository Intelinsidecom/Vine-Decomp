package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineLogin implements Parcelable {
    public static final Parcelable.Creator<VineLogin> CREATOR = new Parcelable.Creator<VineLogin>() { // from class: co.vine.android.api.VineLogin.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineLogin createFromParcel(Parcel in) {
            return new VineLogin(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineLogin[] newArray(int size) {
            return new VineLogin[size];
        }
    };
    public String edition;
    public String key;
    public int loginType;
    public String screenName;
    public String twitterSecret;
    public String twitterToken;
    public long twitterUserId;
    public String twitterUsername;
    public long userId;
    public String username;

    public VineLogin(String key, String username, long userId, String edition) {
        this.key = key;
        this.username = username;
        this.userId = userId;
        this.loginType = 1;
        this.edition = edition;
    }

    public VineLogin(String key, String twitterUsername, String twitterToken, String twitterSecret, long twitterUserId, String edition) {
        this.key = key;
        this.twitterUsername = twitterUsername;
        this.twitterToken = twitterToken;
        this.twitterSecret = twitterSecret;
        this.twitterUserId = twitterUserId;
        this.loginType = 2;
        this.edition = edition;
    }

    public VineLogin(Parcel in) {
        this.key = in.readString();
        this.username = in.readString();
        this.screenName = in.readString();
        this.twitterUsername = in.readString();
        this.twitterToken = in.readString();
        this.twitterSecret = in.readString();
        this.userId = in.readLong();
        this.twitterUserId = in.readLong();
        this.loginType = in.readInt();
        this.edition = in.readString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineLogin vineLogin = (VineLogin) o;
        if (this.loginType == vineLogin.loginType && this.twitterUserId == vineLogin.twitterUserId && this.userId == vineLogin.userId) {
            if (this.key == null ? vineLogin.key != null : !this.key.equals(vineLogin.key)) {
                return false;
            }
            if (this.screenName == null ? vineLogin.screenName != null : !this.screenName.equals(vineLogin.screenName)) {
                return false;
            }
            if (this.twitterSecret == null ? vineLogin.twitterSecret != null : !this.twitterSecret.equals(vineLogin.twitterSecret)) {
                return false;
            }
            if (this.twitterToken == null ? vineLogin.twitterToken != null : !this.twitterToken.equals(vineLogin.twitterToken)) {
                return false;
            }
            if (this.twitterUsername == null ? vineLogin.twitterUsername != null : !this.twitterUsername.equals(vineLogin.twitterUsername)) {
                return false;
            }
            if (this.username == null ? vineLogin.username != null : !this.username.equals(vineLogin.username)) {
                return false;
            }
            if (this.edition != null) {
                if (this.edition.equals(vineLogin.edition)) {
                    return true;
                }
            } else if (vineLogin.edition == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int result = this.key != null ? this.key.hashCode() : 0;
        return (((((((((((((((((result * 31) + (this.username != null ? this.username.hashCode() : 0)) * 31) + (this.screenName != null ? this.screenName.hashCode() : 0)) * 31) + (this.twitterToken != null ? this.twitterToken.hashCode() : 0)) * 31) + (this.twitterSecret != null ? this.twitterSecret.hashCode() : 0)) * 31) + (this.twitterUsername != null ? this.twitterUsername.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + ((int) (this.twitterUserId ^ (this.twitterUserId >>> 32)))) * 31) + this.loginType) * 31) + (this.edition != null ? this.edition.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int i) {
        out.writeString(this.key);
        out.writeString(this.username);
        out.writeString(this.screenName);
        out.writeString(this.twitterUsername);
        out.writeString(this.twitterToken);
        out.writeString(this.twitterSecret);
        out.writeLong(this.userId);
        out.writeLong(this.twitterUserId);
        out.writeInt(this.loginType);
        out.writeString(this.edition);
    }
}
