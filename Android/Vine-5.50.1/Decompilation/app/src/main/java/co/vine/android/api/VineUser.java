package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import co.vine.android.api.response.VineUserResponse;
import co.vine.android.util.CrossConstants;

/* loaded from: classes.dex */
public class VineUser implements Parcelable {
    public static final Parcelable.Creator<VineUser> CREATOR = new Parcelable.Creator<VineUser>() { // from class: co.vine.android.api.VineUser.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineUser createFromParcel(Parcel in) {
            return new VineUser(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineUser[] newArray(int size) {
            return new VineUser[size];
        }
    };
    public boolean acceptsOutOfNetworkConversations;
    public int authoredPostCount;
    public String avatarUrl;
    public int blocked;
    public int blocking;
    public String byline;
    public String description;
    public boolean disableAddressBook;
    public String edition;
    public String email;
    public int explicit;
    public boolean external;
    public int followStatus;
    public int followerCount;
    public int following;
    public int followingCount;
    public boolean followingOnTwitter;
    public long friendIndex;
    public boolean hiddenEmail;
    public boolean hiddenPhoneNumber;
    public boolean hiddenTwitter;
    public long id;
    public int includePromoted;
    public int likeCount;
    public String location;
    public long loopCount;
    public boolean notifyPosts;
    public long orderId;
    public String phoneNumber;
    public int postCount;
    public int privateAccount;
    public int profileBackground;
    public int repostsEnabled;
    public int secondaryColor;
    public int sectionId;
    public String sectionTitle;
    public int twitterConnected;
    public String twitterScreenname;
    public boolean twitterVerified;
    public long userId;
    public String username;
    public int verified;
    public int verifiedInformation;

    public VineUser() {
    }

    public VineUser(String username, String avatarUrl, String description, String byline, String location, String twitterScreenname, long userId, long orderId, int blocked, int blocking, int explicit, boolean external, int followerCount, int followingCount, int following, int likeCount, int postCount, int verified, String email, String phoneNumber, int twitterConnected, int includePromoted, int privateAccount, int repostsEnabled, String edition, boolean acceptsOutOfNetworkConversations, int profileBackground, int secondaryColor, int authoredPostCount, String sectionTitle, boolean disableAddressBook, boolean hiddenEmail, boolean hiddenPhoneNumber, boolean hiddenTwitter, long loopCount, boolean notifyPosts, boolean twitterVerified, boolean followingOnTwitter) {
        if (TextUtils.isEmpty(username)) {
            if (!TextUtils.isEmpty(phoneNumber)) {
                username = phoneNumber;
            } else if (!TextUtils.isEmpty(email)) {
                username = email;
            }
        }
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.byline = byline;
        this.location = location;
        this.twitterScreenname = twitterScreenname;
        this.userId = userId;
        this.orderId = orderId;
        this.blocked = blocked;
        this.blocking = blocking;
        this.explicit = explicit;
        this.external = external;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.following = following;
        this.likeCount = likeCount;
        this.postCount = postCount;
        this.verified = verified;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.twitterConnected = twitterConnected;
        this.includePromoted = includePromoted;
        this.privateAccount = privateAccount;
        this.repostsEnabled = repostsEnabled;
        this.edition = edition;
        this.acceptsOutOfNetworkConversations = acceptsOutOfNetworkConversations;
        this.profileBackground = profileBackground;
        this.secondaryColor = secondaryColor;
        this.authoredPostCount = authoredPostCount;
        this.sectionTitle = sectionTitle;
        this.disableAddressBook = disableAddressBook;
        this.hiddenEmail = hiddenEmail;
        this.hiddenPhoneNumber = hiddenPhoneNumber;
        this.hiddenTwitter = hiddenTwitter;
        this.loopCount = loopCount;
        this.notifyPosts = notifyPosts;
        this.twitterVerified = twitterVerified;
        this.followingOnTwitter = followingOnTwitter;
    }

    public VineUser(Parcel in) {
        this.username = in.readString();
        this.avatarUrl = in.readString();
        this.description = in.readString();
        this.byline = in.readString();
        this.location = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.twitterScreenname = in.readString();
        this.userId = in.readLong();
        this.orderId = in.readLong();
        this.blocked = in.readInt();
        this.blocking = in.readInt();
        this.explicit = in.readInt();
        this.external = in.readInt() == 1;
        this.followerCount = in.readInt();
        this.followingCount = in.readInt();
        this.following = in.readInt();
        this.likeCount = in.readInt();
        this.postCount = in.readInt();
        this.verified = in.readInt();
        this.twitterConnected = in.readInt();
        this.includePromoted = in.readInt();
        this.privateAccount = in.readInt();
        this.followStatus = in.readInt();
        this.repostsEnabled = in.readInt();
        this.edition = in.readString();
        this.acceptsOutOfNetworkConversations = in.readInt() == 1;
        this.profileBackground = in.readInt();
        this.secondaryColor = in.readInt();
        this.authoredPostCount = in.readInt();
        this.verifiedInformation = in.readInt();
        this.disableAddressBook = in.readInt() == 1;
        this.hiddenEmail = in.readInt() == 1;
        this.hiddenPhoneNumber = in.readInt() == 1;
        this.hiddenTwitter = in.readInt() == 1;
        this.loopCount = in.readLong();
        this.notifyPosts = in.readInt() == 1;
        this.sectionId = in.readInt();
        this.sectionTitle = in.readString();
        this.twitterVerified = in.readInt() == 1;
        this.followingOnTwitter = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.username);
        out.writeString(this.avatarUrl);
        out.writeString(this.description);
        out.writeString(this.byline);
        out.writeString(this.location);
        out.writeString(this.email);
        out.writeString(this.phoneNumber);
        out.writeString(this.twitterScreenname);
        out.writeLong(this.userId);
        out.writeLong(this.orderId);
        out.writeInt(this.blocked);
        out.writeInt(this.blocking);
        out.writeInt(this.explicit);
        out.writeInt(this.external ? 1 : 0);
        out.writeInt(this.followerCount);
        out.writeInt(this.followingCount);
        out.writeInt(this.following);
        out.writeInt(this.likeCount);
        out.writeInt(this.postCount);
        out.writeInt(this.verified);
        out.writeInt(this.twitterConnected);
        out.writeInt(this.includePromoted);
        out.writeInt(this.privateAccount);
        out.writeInt(this.followStatus);
        out.writeInt(this.repostsEnabled);
        out.writeString(this.edition);
        out.writeInt(this.acceptsOutOfNetworkConversations ? 1 : 0);
        out.writeInt(this.profileBackground);
        out.writeInt(this.secondaryColor);
        out.writeInt(this.authoredPostCount);
        out.writeInt(this.verifiedInformation);
        out.writeInt(this.disableAddressBook ? 1 : 0);
        out.writeInt(this.hiddenEmail ? 1 : 0);
        out.writeInt(this.hiddenPhoneNumber ? 1 : 0);
        out.writeInt(this.hiddenTwitter ? 1 : 0);
        out.writeLong(this.loopCount);
        out.writeInt(this.notifyPosts ? 1 : 0);
        out.writeInt(this.sectionId);
        out.writeString(this.sectionTitle);
        out.writeInt(this.twitterVerified ? 1 : 0);
        out.writeInt(this.followingOnTwitter ? 1 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VineUser vineUser = (VineUser) o;
        if (this.blocked == vineUser.blocked && this.blocking == vineUser.blocking && this.explicit == vineUser.explicit && this.external == vineUser.external && this.followerCount == vineUser.followerCount && this.following == vineUser.following && this.followingCount == vineUser.followingCount && this.likeCount == vineUser.likeCount && this.orderId == vineUser.orderId && this.postCount == vineUser.postCount && this.twitterConnected == vineUser.twitterConnected && this.includePromoted == vineUser.includePromoted && this.userId == vineUser.userId && this.verified == vineUser.verified && this.privateAccount == vineUser.privateAccount && this.followStatus == vineUser.followStatus) {
            if (this.avatarUrl == null ? vineUser.avatarUrl != null : !this.avatarUrl.equals(vineUser.avatarUrl)) {
                return false;
            }
            if (this.description == null ? vineUser.description != null : !this.description.equals(vineUser.description)) {
                return false;
            }
            if (this.email == null ? vineUser.email != null : !this.email.equals(vineUser.email)) {
                return false;
            }
            if (this.byline == null ? vineUser.byline != null : !this.byline.equals(vineUser.byline)) {
                return false;
            }
            if (this.location == null ? vineUser.location != null : !this.location.equals(vineUser.location)) {
                return false;
            }
            if (this.location == null ? vineUser.location != null : !this.location.equals(vineUser.location)) {
                return false;
            }
            if (this.phoneNumber == null ? vineUser.phoneNumber != null : !this.phoneNumber.equals(vineUser.phoneNumber)) {
                return false;
            }
            if (this.username == null ? vineUser.username != null : !this.username.equals(vineUser.username)) {
                return false;
            }
            if (this.twitterScreenname == null ? vineUser.twitterScreenname != null : !this.twitterScreenname.equals(vineUser.twitterScreenname)) {
                return false;
            }
            if (this.repostsEnabled != vineUser.repostsEnabled) {
                return false;
            }
            if (this.edition == null ? vineUser.edition != null : !this.edition.equals(vineUser.edition)) {
                return false;
            }
            if (this.acceptsOutOfNetworkConversations == vineUser.acceptsOutOfNetworkConversations && this.profileBackground == vineUser.profileBackground && this.secondaryColor == vineUser.secondaryColor && this.authoredPostCount == vineUser.authoredPostCount && this.verifiedInformation == vineUser.verifiedInformation && this.disableAddressBook == vineUser.disableAddressBook && this.hiddenEmail == vineUser.hiddenEmail && this.hiddenPhoneNumber == vineUser.hiddenPhoneNumber && this.hiddenTwitter == vineUser.hiddenTwitter && this.loopCount == vineUser.loopCount && this.notifyPosts == vineUser.notifyPosts && this.sectionId == vineUser.sectionId) {
                if (this.sectionTitle == null ? vineUser.sectionTitle != null : !this.sectionTitle.equals(vineUser.sectionTitle)) {
                    return false;
                }
                return this.twitterVerified == vineUser.twitterVerified && this.followingOnTwitter == vineUser.followingOnTwitter;
            }
            return false;
        }
        return false;
    }

    public boolean isPrivate() {
        return this.privateAccount == 1;
    }

    public boolean isFollowing() {
        return this.following == 1;
    }

    public boolean isExplicit() {
        return this.explicit == 1;
    }

    public boolean isPrivateLocked() {
        return isPrivate() && (!isFollowing() || hasFollowRequested());
    }

    public boolean isBlocked() {
        return this.blocked == 1;
    }

    public boolean isBlocking() {
        return this.blocking == 1;
    }

    public boolean areRepostsEnabled() {
        return this.repostsEnabled == 1;
    }

    public boolean hasFollowApprovalPending() {
        return (this.followStatus & 1) != 0;
    }

    public boolean hasFollowRequested() {
        return (this.followStatus & 2) != 0;
    }

    public void setApprovalPending() {
        this.followStatus |= 1;
    }

    public void setFollowRequested() {
        this.followStatus |= 2;
    }

    public int getFollowStatus() {
        return this.followStatus;
    }

    public void setEmailVerified() {
        this.verifiedInformation |= 1;
    }

    public void setPhoneVerified() {
        this.verifiedInformation |= 2;
    }

    public boolean isVerified() {
        return this.verified == 1;
    }

    public boolean isPhoneVerified() {
        return (this.verifiedInformation & 2) != 0;
    }

    public boolean isEmailVerified() {
        return (this.verifiedInformation & 1) != 0;
    }

    public int hashCode() {
        int result = this.username != null ? this.username.hashCode() : 0;
        return (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((result * 31) + (this.edition != null ? this.edition.hashCode() : 0)) * 31) + (this.avatarUrl != null ? this.avatarUrl.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.location != null ? this.location.hashCode() : 0)) * 31) + (this.twitterScreenname != null ? this.twitterScreenname.hashCode() : 0)) * 31) + (this.phoneNumber != null ? this.phoneNumber.hashCode() : 0)) * 31) + (this.email != null ? this.email.hashCode() : 0)) * 31) + ((int) (this.userId ^ (this.userId >>> 32)))) * 31) + ((int) (this.orderId ^ (this.orderId >>> 32)))) * 31) + ((int) (this.loopCount ^ (this.loopCount >>> 32)))) * 31) + this.blocked) * 31) + this.blocking) * 31) + this.explicit) * 31) + (this.external ? 1 : 0)) * 31) + this.followerCount) * 31) + this.followingCount) * 31) + this.following) * 31) + this.likeCount) * 31) + this.postCount) * 31) + this.verified) * 31) + this.twitterConnected) * 31) + this.includePromoted) * 31) + this.privateAccount) * 31) + this.followStatus) * 31) + this.repostsEnabled) * 31) + (this.acceptsOutOfNetworkConversations ? 1 : 0)) * 31) + this.profileBackground) * 31) + this.secondaryColor) * 31) + this.verifiedInformation) * 31) + (this.notifyPosts ? 1 : 0)) * 31) + (this.twitterVerified ? 1 : 0)) * 31) + (this.followingOnTwitter ? 1 : 0);
    }

    public static VineUser fromVineUserResponse(VineUserResponse response) {
        if (response.userId <= 0) {
            return null;
        }
        int profileBackground = response.profileBackgroundString != null ? (int) Long.parseLong(response.profileBackgroundString.substring(2), 16) : -1;
        int secondaryColor = response.secondaryColorString != null ? (int) Long.parseLong(response.secondaryColorString.substring(2), 16) : -1;
        String twitterScreenname = CrossConstants.SHOW_TWITTER_SCREENNAME ? response.twitterScreenname : null;
        int following = response.following != null ? response.following.intValue() : -1;
        int repostsEnabled = response.repostsEnabled != null ? response.repostsEnabled.intValue() : 1;
        boolean followingOnTwitter = response.followingOnTwitter != null && response.followingOnTwitter.intValue() == 1;
        VineUser u = new VineUser(response.username, response.avatarUrl, response.description, response.byline, response.location, twitterScreenname, response.userId, response.orderId, response.blocked, response.blocking, response.explicit, response.externalUser, response.followerCount, response.followingCount, following, response.likeCount, response.postCount, response.verified, response.email, response.phoneNumber, response.twitterConnected, response.includePromoted, response.privateAccount, repostsEnabled, response.edition, response.acceptsOutOfNetworkConversations, profileBackground, secondaryColor, response.authoredPostCount, response.sectionTitle, response.disableAddressBook, response.hiddenEmail, response.hiddenPhoneNumber, response.hiddenTwitter, response.loopCount, response.notifyPosts, response.twitterVerified, followingOnTwitter);
        u.sectionId = response.sectionId != null ? response.sectionId.intValue() : -1;
        if (response.followApprovalPending) {
            u.setApprovalPending();
        }
        if (response.followRequested) {
            u.setFollowRequested();
        }
        if (response.verifiedEmail) {
            u.setEmailVerified();
        }
        if (response.verifiedPhoneNumber) {
            u.setPhoneVerified();
            return u;
        }
        return u;
    }
}
