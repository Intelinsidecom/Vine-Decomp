package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class VineConversation implements Parcelable, Externalizable {
    public static final Parcelable.Creator<VineConversation> CREATOR = new Parcelable.Creator<VineConversation>() { // from class: co.vine.android.api.VineConversation.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineConversation createFromParcel(Parcel in) {
            return new VineConversation(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineConversation[] newArray(int size) {
            return new VineConversation[size];
        }
    };
    public long conversationId;
    public long conversationObjectId;
    public long createdBy;
    public long lastMessage;
    public ArrayList<VinePrivateMessage> messages;
    public int networkType;
    public long unreadMessageCount;
    public ArrayList<Long> users;

    public VineConversation() {
    }

    public VineConversation(long conversationId, long lastMessage, long createdBy, ArrayList<VinePrivateMessage> messages, ArrayList<Long> users, int networkType, long unreadMessageCount) {
        this.conversationId = conversationId;
        this.lastMessage = lastMessage;
        this.createdBy = createdBy;
        this.unreadMessageCount = unreadMessageCount;
        this.messages = messages;
        this.users = users;
        this.networkType = networkType;
    }

    public VineConversation(long conversationId, long lastMessage, ArrayList<VinePrivateMessage> messages, long unreadMessageCount) {
        this.conversationId = conversationId;
        this.lastMessage = lastMessage;
        this.messages = messages;
        this.unreadMessageCount = unreadMessageCount;
    }

    public VinePrivateMessage lastMessage() {
        if (this.messages.isEmpty()) {
            return null;
        }
        Iterator<VinePrivateMessage> it = this.messages.iterator();
        while (it.hasNext()) {
            VinePrivateMessage message = it.next();
            if (message.messageId == this.lastMessage) {
                return message;
            }
        }
        return this.messages.get(0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public VineConversation(Parcel in) {
        this.conversationId = in.readLong();
        this.lastMessage = in.readLong();
        this.createdBy = in.readLong();
        this.unreadMessageCount = in.readLong();
        this.messages = (ArrayList) in.readSerializable();
        this.users = (ArrayList) in.readSerializable();
        this.networkType = in.readInt();
        this.conversationObjectId = in.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.conversationId);
        out.writeLong(this.lastMessage);
        out.writeLong(this.createdBy);
        out.writeLong(this.unreadMessageCount);
        out.writeSerializable(this.messages);
        out.writeSerializable(this.users);
        out.writeInt(this.networkType);
        out.writeLong(this.conversationObjectId);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.conversationId = objectInput.readLong();
        this.lastMessage = objectInput.readLong();
        this.createdBy = objectInput.readLong();
        this.unreadMessageCount = objectInput.readLong();
        this.messages = (ArrayList) objectInput.readObject();
        this.users = (ArrayList) objectInput.readObject();
        this.networkType = objectInput.readInt();
        this.conversationObjectId = objectInput.readLong();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeLong(this.conversationId);
        objectOutput.writeLong(this.lastMessage);
        objectOutput.writeLong(this.createdBy);
        objectOutput.writeLong(this.unreadMessageCount);
        objectOutput.writeObject(this.messages);
        objectOutput.writeObject(this.users);
        objectOutput.writeInt(this.networkType);
    }
}
