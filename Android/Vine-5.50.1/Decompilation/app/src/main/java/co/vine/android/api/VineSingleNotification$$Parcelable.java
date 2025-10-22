package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.VideoKey;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VineSingleNotification$$Parcelable implements Parcelable, ParcelWrapper<VineSingleNotification> {
    public static final VineSingleNotification$$Parcelable$Creator$$36 CREATOR = new VineSingleNotification$$Parcelable$Creator$$36();
    private VineSingleNotification vineSingleNotification$$4;

    public VineSingleNotification$$Parcelable(Parcel parcel$$370) {
        VineSingleNotification vineSingleNotification$$6;
        if (parcel$$370.readInt() == -1) {
            vineSingleNotification$$6 = null;
        } else {
            vineSingleNotification$$6 = readco_vine_android_api_VineSingleNotification(parcel$$370);
        }
        this.vineSingleNotification$$4 = vineSingleNotification$$6;
    }

    public VineSingleNotification$$Parcelable(VineSingleNotification vineSingleNotification$$8) {
        this.vineSingleNotification$$4 = vineSingleNotification$$8;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$371, int flags) {
        if (this.vineSingleNotification$$4 == null) {
            parcel$$371.writeInt(-1);
        } else {
            parcel$$371.writeInt(1);
            writeco_vine_android_api_VineSingleNotification(this.vineSingleNotification$$4, parcel$$371, flags);
        }
    }

    private VineSingleNotification readco_vine_android_api_VineSingleNotification(Parcel parcel$$372) {
        ArrayList<VideoKey> list$$35;
        ArrayList<ImageKey> list$$36;
        ArrayList<VineEntity> list$$37;
        VineSingleNotification vineSingleNotification$$5 = new VineSingleNotification();
        int int$$330 = parcel$$372.readInt();
        if (int$$330 < 0) {
            list$$35 = null;
        } else {
            list$$35 = new ArrayList<>();
            for (int int$$331 = 0; int$$331 < int$$330; int$$331++) {
                list$$35.add((VideoKey) parcel$$372.readParcelable(VineSingleNotification$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$5.videoKeys = list$$35;
        vineSingleNotification$$5.notificationTypeId = parcel$$372.readInt();
        vineSingleNotification$$5.messageCount = parcel$$372.readInt();
        vineSingleNotification$$5.avatarUrl = parcel$$372.readString();
        vineSingleNotification$$5.conversationId = parcel$$372.readLong();
        vineSingleNotification$$5.onboard = parcel$$372.readString();
        vineSingleNotification$$5.verified = parcel$$372.readInt() == 1;
        vineSingleNotification$$5.unreadMessageCount = parcel$$372.readLong();
        vineSingleNotification$$5.postId = parcel$$372.readLong();
        int int$$332 = parcel$$372.readInt();
        if (int$$332 < 0) {
            list$$36 = null;
        } else {
            list$$36 = new ArrayList<>();
            for (int int$$333 = 0; int$$333 < int$$332; int$$333++) {
                list$$36.add((ImageKey) parcel$$372.readParcelable(VineSingleNotification$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$5.imageKeys = list$$36;
        vineSingleNotification$$5.title = parcel$$372.readString();
        vineSingleNotification$$5.userId = parcel$$372.readLong();
        vineSingleNotification$$5.url = parcel$$372.readString();
        vineSingleNotification$$5.recipientUserId = parcel$$372.readLong();
        vineSingleNotification$$5.createdAt = parcel$$372.readLong();
        int int$$334 = parcel$$372.readInt();
        if (int$$334 < 0) {
            list$$37 = null;
        } else {
            list$$37 = new ArrayList<>();
            for (int int$$335 = 0; int$$335 < int$$334; int$$335++) {
                list$$37.add((VineEntity) parcel$$372.readParcelable(VineSingleNotification$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$5.entities = list$$37;
        vineSingleNotification$$5.prettyComment = parcel$$372.readString();
        vineSingleNotification$$5.comment = parcel$$372.readString();
        vineSingleNotification$$5.notificationId = parcel$$372.readLong();
        vineSingleNotification$$5.conversationRowId = parcel$$372.readLong();
        vineSingleNotification$$5.username = parcel$$372.readString();
        vineSingleNotification$$5.thumbnailUrl = parcel$$372.readString();
        return vineSingleNotification$$5;
    }

    private void writeco_vine_android_api_VineSingleNotification(VineSingleNotification vineSingleNotification$$7, Parcel parcel$$373, int flags$$131) {
        if (vineSingleNotification$$7.videoKeys == null) {
            parcel$$373.writeInt(-1);
        } else {
            parcel$$373.writeInt(vineSingleNotification$$7.videoKeys.size());
            Iterator<VideoKey> it = vineSingleNotification$$7.videoKeys.iterator();
            while (it.hasNext()) {
                VideoKey videoKey$$1 = it.next();
                parcel$$373.writeParcelable(videoKey$$1, flags$$131);
            }
        }
        parcel$$373.writeInt(vineSingleNotification$$7.notificationTypeId);
        parcel$$373.writeInt(vineSingleNotification$$7.messageCount);
        parcel$$373.writeString(vineSingleNotification$$7.avatarUrl);
        parcel$$373.writeLong(vineSingleNotification$$7.conversationId);
        parcel$$373.writeString(vineSingleNotification$$7.onboard);
        parcel$$373.writeInt(vineSingleNotification$$7.verified ? 1 : 0);
        parcel$$373.writeLong(vineSingleNotification$$7.unreadMessageCount);
        parcel$$373.writeLong(vineSingleNotification$$7.postId);
        if (vineSingleNotification$$7.imageKeys == null) {
            parcel$$373.writeInt(-1);
        } else {
            parcel$$373.writeInt(vineSingleNotification$$7.imageKeys.size());
            Iterator<ImageKey> it2 = vineSingleNotification$$7.imageKeys.iterator();
            while (it2.hasNext()) {
                ImageKey imageKey$$1 = it2.next();
                parcel$$373.writeParcelable(imageKey$$1, flags$$131);
            }
        }
        parcel$$373.writeString(vineSingleNotification$$7.title);
        parcel$$373.writeLong(vineSingleNotification$$7.userId);
        parcel$$373.writeString(vineSingleNotification$$7.url);
        parcel$$373.writeLong(vineSingleNotification$$7.recipientUserId);
        parcel$$373.writeLong(vineSingleNotification$$7.createdAt);
        if (vineSingleNotification$$7.entities == null) {
            parcel$$373.writeInt(-1);
        } else {
            parcel$$373.writeInt(vineSingleNotification$$7.entities.size());
            Iterator<VineEntity> it3 = vineSingleNotification$$7.entities.iterator();
            while (it3.hasNext()) {
                VineEntity vineEntity$$1 = it3.next();
                parcel$$373.writeParcelable(vineEntity$$1, flags$$131);
            }
        }
        parcel$$373.writeString(vineSingleNotification$$7.prettyComment);
        parcel$$373.writeString(vineSingleNotification$$7.comment);
        parcel$$373.writeLong(vineSingleNotification$$7.notificationId);
        parcel$$373.writeLong(vineSingleNotification$$7.conversationRowId);
        parcel$$373.writeString(vineSingleNotification$$7.username);
        parcel$$373.writeString(vineSingleNotification$$7.thumbnailUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VineSingleNotification getParcel() {
        return this.vineSingleNotification$$4;
    }
}
