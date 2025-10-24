package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VinePagedData<T> implements Parcelable {
    public static final Parcelable.Creator<VinePagedData> CREATOR = new Parcelable.Creator<VinePagedData>() { // from class: co.vine.android.api.VinePagedData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePagedData createFromParcel(Parcel in) {
            return new VinePagedData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VinePagedData[] newArray(int size) {
            return new VinePagedData[size];
        }
    };
    public String anchor;
    public String backAnchor;
    public VineChannel channel;
    public int count;
    public ArrayList<T> items;
    public long lastMessage;
    public long lastMessageRead;
    public int networkType;
    public int nextPage;
    public ArrayList<VineUser> participants;
    public int previousPage;
    public int size;
    public String title;
    public long unreadMessageCount;

    public VinePagedData() {
        this.previousPage = -1;
        this.unreadMessageCount = 0L;
        this.lastMessageRead = 0L;
        this.lastMessage = 0L;
    }

    public VinePagedData(Parcel in) {
        this.previousPage = -1;
        this.unreadMessageCount = 0L;
        this.lastMessageRead = 0L;
        this.lastMessage = 0L;
        this.anchor = in.readString();
        this.backAnchor = in.readString();
        this.count = in.readInt();
        this.nextPage = in.readInt();
        this.previousPage = in.readInt();
        this.size = in.readInt();
        this.items = new ArrayList<>();
        Parcelable[] array = in.readParcelableArray(VinePagedData.class.getClassLoader());
        for (Parcelable p : array) {
            this.items.add(p);
        }
        this.title = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.anchor);
        out.writeString(this.backAnchor);
        out.writeInt(this.count);
        out.writeInt(this.nextPage);
        out.writeInt(this.previousPage);
        out.writeInt(this.size);
        if (this.items == null) {
            out.writeParcelableArray((Parcelable[]) new ArrayList().toArray(new Parcelable[0]), flags);
        } else {
            out.writeParcelableArray((Parcelable[]) this.items.toArray(new Parcelable[this.items.size()]), flags);
        }
        out.writeString(this.title);
    }
}
