package co.vine.android.api.response;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineSingleNotification;
import co.vine.android.api.response.PagedActivityResponse;
import co.vine.android.cache.image.ImageKey;
import co.vine.android.cache.video.VideoKey;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class PagedActivityResponse$Data$$Parcelable implements Parcelable, ParcelWrapper<PagedActivityResponse.Data> {
    public static final PagedActivityResponse$Data$$Parcelable$Creator$$35 CREATOR = new PagedActivityResponse$Data$$Parcelable$Creator$$35();
    private PagedActivityResponse.Data data$$5;

    public PagedActivityResponse$Data$$Parcelable(Parcel parcel$$363) {
        PagedActivityResponse.Data data$$7;
        if (parcel$$363.readInt() == -1) {
            data$$7 = null;
        } else {
            data$$7 = readco_vine_android_api_response_PagedActivityResponse$Data(parcel$$363);
        }
        this.data$$5 = data$$7;
    }

    public PagedActivityResponse$Data$$Parcelable(PagedActivityResponse.Data data$$9) {
        this.data$$5 = data$$9;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$364, int flags) {
        if (this.data$$5 == null) {
            parcel$$364.writeInt(-1);
        } else {
            parcel$$364.writeInt(1);
            writeco_vine_android_api_response_PagedActivityResponse$Data(this.data$$5, parcel$$364, flags);
        }
    }

    private PagedActivityResponse.Data readco_vine_android_api_response_PagedActivityResponse$Data(Parcel parcel$$365) {
        ArrayList<VineSingleNotification> list$$31;
        VineSingleNotification vineSingleNotification$$1;
        PagedActivityResponse.Data data$$6 = new PagedActivityResponse.Data();
        int int$$322 = parcel$$365.readInt();
        if (int$$322 < 0) {
            list$$31 = null;
        } else {
            list$$31 = new ArrayList<>();
            for (int int$$323 = 0; int$$323 < int$$322; int$$323++) {
                if (parcel$$365.readInt() == -1) {
                    vineSingleNotification$$1 = null;
                } else {
                    vineSingleNotification$$1 = readco_vine_android_api_VineSingleNotification(parcel$$365);
                }
                list$$31.add(vineSingleNotification$$1);
            }
        }
        data$$6.items = list$$31;
        data$$6.previousPage = parcel$$365.readInt();
        data$$6.size = parcel$$365.readInt();
        data$$6.nextPage = parcel$$365.readInt();
        data$$6.anchor = parcel$$365.readString();
        data$$6.count = parcel$$365.readInt();
        return data$$6;
    }

    private VineSingleNotification readco_vine_android_api_VineSingleNotification(Parcel parcel$$366) {
        ArrayList<VideoKey> list$$32;
        ArrayList<ImageKey> list$$33;
        ArrayList<VineEntity> list$$34;
        VineSingleNotification vineSingleNotification$$0 = new VineSingleNotification();
        int int$$324 = parcel$$366.readInt();
        if (int$$324 < 0) {
            list$$32 = null;
        } else {
            list$$32 = new ArrayList<>();
            for (int int$$325 = 0; int$$325 < int$$324; int$$325++) {
                list$$32.add((VideoKey) parcel$$366.readParcelable(PagedActivityResponse$Data$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$0.videoKeys = list$$32;
        vineSingleNotification$$0.notificationTypeId = parcel$$366.readInt();
        vineSingleNotification$$0.messageCount = parcel$$366.readInt();
        vineSingleNotification$$0.avatarUrl = parcel$$366.readString();
        vineSingleNotification$$0.conversationId = parcel$$366.readLong();
        vineSingleNotification$$0.onboard = parcel$$366.readString();
        vineSingleNotification$$0.verified = parcel$$366.readInt() == 1;
        vineSingleNotification$$0.unreadMessageCount = parcel$$366.readLong();
        vineSingleNotification$$0.postId = parcel$$366.readLong();
        int int$$326 = parcel$$366.readInt();
        if (int$$326 < 0) {
            list$$33 = null;
        } else {
            list$$33 = new ArrayList<>();
            for (int int$$327 = 0; int$$327 < int$$326; int$$327++) {
                list$$33.add((ImageKey) parcel$$366.readParcelable(PagedActivityResponse$Data$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$0.imageKeys = list$$33;
        vineSingleNotification$$0.title = parcel$$366.readString();
        vineSingleNotification$$0.userId = parcel$$366.readLong();
        vineSingleNotification$$0.url = parcel$$366.readString();
        vineSingleNotification$$0.recipientUserId = parcel$$366.readLong();
        vineSingleNotification$$0.createdAt = parcel$$366.readLong();
        int int$$328 = parcel$$366.readInt();
        if (int$$328 < 0) {
            list$$34 = null;
        } else {
            list$$34 = new ArrayList<>();
            for (int int$$329 = 0; int$$329 < int$$328; int$$329++) {
                list$$34.add((VineEntity) parcel$$366.readParcelable(PagedActivityResponse$Data$$Parcelable.class.getClassLoader()));
            }
        }
        vineSingleNotification$$0.entities = list$$34;
        vineSingleNotification$$0.prettyComment = parcel$$366.readString();
        vineSingleNotification$$0.comment = parcel$$366.readString();
        vineSingleNotification$$0.notificationId = parcel$$366.readLong();
        vineSingleNotification$$0.conversationRowId = parcel$$366.readLong();
        vineSingleNotification$$0.username = parcel$$366.readString();
        vineSingleNotification$$0.thumbnailUrl = parcel$$366.readString();
        return vineSingleNotification$$0;
    }

    private void writeco_vine_android_api_response_PagedActivityResponse$Data(PagedActivityResponse.Data data$$8, Parcel parcel$$367, int flags$$129) {
        if (data$$8.items == null) {
            parcel$$367.writeInt(-1);
        } else {
            parcel$$367.writeInt(data$$8.items.size());
            Iterator<VineSingleNotification> it = data$$8.items.iterator();
            while (it.hasNext()) {
                VineSingleNotification vineSingleNotification$$2 = it.next();
                if (vineSingleNotification$$2 == null) {
                    parcel$$367.writeInt(-1);
                } else {
                    parcel$$367.writeInt(1);
                    writeco_vine_android_api_VineSingleNotification(vineSingleNotification$$2, parcel$$367, flags$$129);
                }
            }
        }
        parcel$$367.writeInt(data$$8.previousPage);
        parcel$$367.writeInt(data$$8.size);
        parcel$$367.writeInt(data$$8.nextPage);
        parcel$$367.writeString(data$$8.anchor);
        parcel$$367.writeInt(data$$8.count);
    }

    private void writeco_vine_android_api_VineSingleNotification(VineSingleNotification vineSingleNotification$$3, Parcel parcel$$368, int flags$$130) {
        if (vineSingleNotification$$3.videoKeys == null) {
            parcel$$368.writeInt(-1);
        } else {
            parcel$$368.writeInt(vineSingleNotification$$3.videoKeys.size());
            Iterator<VideoKey> it = vineSingleNotification$$3.videoKeys.iterator();
            while (it.hasNext()) {
                VideoKey videoKey$$0 = it.next();
                parcel$$368.writeParcelable(videoKey$$0, flags$$130);
            }
        }
        parcel$$368.writeInt(vineSingleNotification$$3.notificationTypeId);
        parcel$$368.writeInt(vineSingleNotification$$3.messageCount);
        parcel$$368.writeString(vineSingleNotification$$3.avatarUrl);
        parcel$$368.writeLong(vineSingleNotification$$3.conversationId);
        parcel$$368.writeString(vineSingleNotification$$3.onboard);
        parcel$$368.writeInt(vineSingleNotification$$3.verified ? 1 : 0);
        parcel$$368.writeLong(vineSingleNotification$$3.unreadMessageCount);
        parcel$$368.writeLong(vineSingleNotification$$3.postId);
        if (vineSingleNotification$$3.imageKeys == null) {
            parcel$$368.writeInt(-1);
        } else {
            parcel$$368.writeInt(vineSingleNotification$$3.imageKeys.size());
            Iterator<ImageKey> it2 = vineSingleNotification$$3.imageKeys.iterator();
            while (it2.hasNext()) {
                ImageKey imageKey$$0 = it2.next();
                parcel$$368.writeParcelable(imageKey$$0, flags$$130);
            }
        }
        parcel$$368.writeString(vineSingleNotification$$3.title);
        parcel$$368.writeLong(vineSingleNotification$$3.userId);
        parcel$$368.writeString(vineSingleNotification$$3.url);
        parcel$$368.writeLong(vineSingleNotification$$3.recipientUserId);
        parcel$$368.writeLong(vineSingleNotification$$3.createdAt);
        if (vineSingleNotification$$3.entities == null) {
            parcel$$368.writeInt(-1);
        } else {
            parcel$$368.writeInt(vineSingleNotification$$3.entities.size());
            Iterator<VineEntity> it3 = vineSingleNotification$$3.entities.iterator();
            while (it3.hasNext()) {
                VineEntity vineEntity$$0 = it3.next();
                parcel$$368.writeParcelable(vineEntity$$0, flags$$130);
            }
        }
        parcel$$368.writeString(vineSingleNotification$$3.prettyComment);
        parcel$$368.writeString(vineSingleNotification$$3.comment);
        parcel$$368.writeLong(vineSingleNotification$$3.notificationId);
        parcel$$368.writeLong(vineSingleNotification$$3.conversationRowId);
        parcel$$368.writeString(vineSingleNotification$$3.username);
        parcel$$368.writeString(vineSingleNotification$$3.thumbnailUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public PagedActivityResponse.Data getParcel() {
        return this.data$$5;
    }
}
