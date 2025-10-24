package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class VineRTCParticipant implements Parcelable {
    public static final Parcelable.Creator<VineRTCParticipant> CREATOR = new Parcelable.Creator<VineRTCParticipant>() { // from class: co.vine.android.api.VineRTCParticipant.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRTCParticipant createFromParcel(Parcel in) {
            return new VineRTCParticipant(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRTCParticipant[] newArray(int size) {
            return new VineRTCParticipant[size];
        }
    };
    public boolean isConnected;
    public boolean isTyping;
    public long lastMessageId;
    public long userId;

    public VineRTCParticipant(long userId, boolean connected, boolean typing, long lastMessageId) {
        this.userId = userId;
        this.lastMessageId = lastMessageId;
        this.isConnected = connected;
        this.isTyping = typing;
    }

    public VineRTCParticipant(Parcel in) {
        this.userId = in.readLong();
        this.lastMessageId = in.readLong();
        this.isConnected = in.readInt() == 1;
        this.isTyping = in.readInt() == 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeLong(this.lastMessageId);
        dest.writeInt(this.isConnected ? 1 : 0);
        dest.writeInt(this.isTyping ? 1 : 0);
    }
}
