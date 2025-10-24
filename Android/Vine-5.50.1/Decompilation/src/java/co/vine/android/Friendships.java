package co.vine.android;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Friendships implements Parcelable {
    public static final Parcelable.Creator<Friendships> CREATOR = new Parcelable.Creator<Friendships>() { // from class: co.vine.android.Friendships.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Friendships createFromParcel(Parcel in) {
            return new Friendships(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Friendships[] newArray(int size) {
            return new Friendships[size];
        }
    };
    private HashMap<Long, Integer> mCache;
    private HashMap<Long, Integer> mChannelCache;

    public Friendships() {
        this.mCache = new HashMap<>();
        this.mChannelCache = new HashMap<>();
    }

    public Friendships(Parcel in) {
        this.mCache = (HashMap) in.readSerializable();
        this.mChannelCache = (HashMap) in.readSerializable();
    }

    public int size() {
        return this.mCache.size();
    }

    public void clear() {
        this.mCache.clear();
    }

    public boolean contains(long userId) {
        return this.mCache.containsKey(Long.valueOf(userId));
    }

    public boolean containsChannel(long channelId) {
        return this.mChannelCache.containsKey(Long.valueOf(channelId));
    }

    public void addFollowing(long userId) {
        Integer state = this.mCache.get(Long.valueOf(userId));
        if (state == null) {
            this.mCache.put(Long.valueOf(userId), Integer.valueOf(setFriendship(0, 1)));
        } else {
            this.mCache.put(Long.valueOf(userId), Integer.valueOf(setFriendship(state.intValue(), 1)));
        }
    }

    public void addChannelFollowing(long channelId) {
        Integer state = this.mChannelCache.get(Long.valueOf(channelId));
        if (state == null) {
            this.mChannelCache.put(Long.valueOf(channelId), Integer.valueOf(setFriendship(0, 1)));
        } else {
            this.mChannelCache.put(Long.valueOf(channelId), Integer.valueOf(setFriendship(state.intValue(), 1)));
        }
    }

    public void removeFollowing(long userId) {
        Integer state = this.mCache.get(Long.valueOf(userId));
        if (state == null) {
            this.mCache.put(Long.valueOf(userId), Integer.valueOf(unsetFriendship(0, 1)));
        } else {
            this.mCache.put(Long.valueOf(userId), Integer.valueOf(unsetFriendship(state.intValue(), 1)));
        }
    }

    public void removeChannelFollowing(long channelId) {
        Integer state = this.mChannelCache.get(Long.valueOf(channelId));
        if (state == null) {
            this.mChannelCache.put(Long.valueOf(channelId), Integer.valueOf(unsetFriendship(0, 1)));
        } else {
            this.mChannelCache.put(Long.valueOf(channelId), Integer.valueOf(unsetFriendship(state.intValue(), 1)));
        }
    }

    public boolean isFollowing(long userId) {
        Integer state = this.mCache.get(Long.valueOf(userId));
        return (state == null || (state.intValue() & 1) == 0) ? false : true;
    }

    public boolean isChannelFollowing(long channelId) {
        Integer state = this.mChannelCache.get(Long.valueOf(channelId));
        return (state == null || (state.intValue() & 1) == 0) ? false : true;
    }

    public static int setFriendship(int state, int friendship) {
        return state | friendship;
    }

    public static int unsetFriendship(int state, int friendship) {
        return (friendship ^ (-1)) & state;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(this.mCache);
        out.writeSerializable(this.mChannelCache);
    }
}
