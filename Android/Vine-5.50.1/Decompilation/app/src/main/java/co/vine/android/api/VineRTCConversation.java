package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VineRTCConversation implements Parcelable {
    public static final Parcelable.Creator<VineRTCConversation> CREATOR = new Parcelable.Creator<VineRTCConversation>() { // from class: co.vine.android.api.VineRTCConversation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRTCConversation createFromParcel(Parcel in) {
            return new VineRTCConversation(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineRTCConversation[] newArray(int size) {
            return new VineRTCConversation[size];
        }
    };
    public long conversationId;
    public ArrayList<VineRTCParticipant> participants;

    public VineRTCConversation(long conversationId, ArrayList<VineRTCParticipant> participants) {
        this.participants = new ArrayList<>();
        this.conversationId = conversationId;
        this.participants = participants;
    }

    public VineRTCConversation(Parcel in) {
        this.participants = new ArrayList<>();
        this.conversationId = in.readLong();
        in.readTypedList(this.participants, VineRTCParticipant.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.conversationId);
        dest.writeTypedList(this.participants);
    }
}
