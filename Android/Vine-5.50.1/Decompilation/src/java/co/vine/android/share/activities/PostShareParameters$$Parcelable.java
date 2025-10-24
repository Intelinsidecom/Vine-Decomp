package co.vine.android.share.activities;

import android.os.Parcel;
import android.os.Parcelable;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineRecipient;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class PostShareParameters$$Parcelable implements Parcelable, ParcelWrapper<PostShareParameters> {
    public static final PostShareParameters$$Parcelable$Creator$$38 CREATOR = new PostShareParameters$$Parcelable$Creator$$38();
    private PostShareParameters postShareParameters$$0;

    public PostShareParameters$$Parcelable(Parcel parcel$$386) {
        PostShareParameters postShareParameters$$2;
        if (parcel$$386.readInt() == -1) {
            postShareParameters$$2 = null;
        } else {
            postShareParameters$$2 = readco_vine_android_share_activities_PostShareParameters(parcel$$386);
        }
        this.postShareParameters$$0 = postShareParameters$$2;
    }

    public PostShareParameters$$Parcelable(PostShareParameters postShareParameters$$4) {
        this.postShareParameters$$0 = postShareParameters$$4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$387, int flags) {
        if (this.postShareParameters$$0 == null) {
            parcel$$387.writeInt(-1);
        } else {
            parcel$$387.writeInt(1);
            writeco_vine_android_share_activities_PostShareParameters(this.postShareParameters$$0, parcel$$387, flags);
        }
    }

    private PostShareParameters readco_vine_android_share_activities_PostShareParameters(Parcel parcel$$388) {
        Boolean boolean$$88;
        Boolean boolean$$89;
        ArrayList<VineRecipient> list$$38;
        ArrayList<VineEntity> list$$39;
        Boolean boolean$$90;
        Boolean boolean$$91;
        PostShareParameters postShareParameters$$1 = new PostShareParameters();
        postShareParameters$$1.venueName = parcel$$388.readString();
        int int$$337 = parcel$$388.readInt();
        if (int$$337 < 0) {
            boolean$$88 = null;
        } else {
            boolean$$88 = Boolean.valueOf(parcel$$388.readInt() == 1);
        }
        postShareParameters$$1.shareToTumblr = boolean$$88;
        int int$$338 = parcel$$388.readInt();
        if (int$$338 < 0) {
            boolean$$89 = null;
        } else {
            boolean$$89 = Boolean.valueOf(parcel$$388.readInt() == 1);
        }
        postShareParameters$$1.shareToVine = boolean$$89;
        int int$$339 = parcel$$388.readInt();
        if (int$$339 < 0) {
            list$$38 = null;
        } else {
            list$$38 = new ArrayList<>();
            for (int int$$340 = 0; int$$340 < int$$339; int$$340++) {
                list$$38.add((VineRecipient) parcel$$388.readParcelable(PostShareParameters$$Parcelable.class.getClassLoader()));
            }
        }
        postShareParameters$$1.recipients = list$$38;
        int int$$341 = parcel$$388.readInt();
        if (int$$341 < 0) {
            list$$39 = null;
        } else {
            list$$39 = new ArrayList<>();
            for (int int$$342 = 0; int$$342 < int$$341; int$$342++) {
                list$$39.add((VineEntity) parcel$$388.readParcelable(PostShareParameters$$Parcelable.class.getClassLoader()));
            }
        }
        postShareParameters$$1.captionEntities = list$$39;
        postShareParameters$$1.venueId = parcel$$388.readString();
        int int$$343 = parcel$$388.readInt();
        if (int$$343 < 0) {
            boolean$$90 = null;
        } else {
            boolean$$90 = Boolean.valueOf(parcel$$388.readInt() == 1);
        }
        postShareParameters$$1.shareToTwitter = boolean$$90;
        int int$$344 = parcel$$388.readInt();
        if (int$$344 < 0) {
            boolean$$91 = null;
        } else {
            boolean$$91 = Boolean.valueOf(parcel$$388.readInt() == 1);
        }
        postShareParameters$$1.shareToFacebook = boolean$$91;
        postShareParameters$$1.channel = (VineChannel) parcel$$388.readParcelable(PostShareParameters$$Parcelable.class.getClassLoader());
        postShareParameters$$1.caption = parcel$$388.readString();
        return postShareParameters$$1;
    }

    private void writeco_vine_android_share_activities_PostShareParameters(PostShareParameters postShareParameters$$3, Parcel parcel$$389, int flags$$136) {
        parcel$$389.writeString(postShareParameters$$3.venueName);
        if (postShareParameters$$3.shareToTumblr == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(1);
            parcel$$389.writeInt(postShareParameters$$3.shareToTumblr.booleanValue() ? 1 : 0);
        }
        if (postShareParameters$$3.shareToVine == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(1);
            parcel$$389.writeInt(postShareParameters$$3.shareToVine.booleanValue() ? 1 : 0);
        }
        if (postShareParameters$$3.recipients == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(postShareParameters$$3.recipients.size());
            for (VineRecipient vineRecipient$$0 : postShareParameters$$3.recipients) {
                parcel$$389.writeParcelable(vineRecipient$$0, flags$$136);
            }
        }
        if (postShareParameters$$3.captionEntities == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(postShareParameters$$3.captionEntities.size());
            Iterator<VineEntity> it = postShareParameters$$3.captionEntities.iterator();
            while (it.hasNext()) {
                VineEntity vineEntity$$2 = it.next();
                parcel$$389.writeParcelable(vineEntity$$2, flags$$136);
            }
        }
        parcel$$389.writeString(postShareParameters$$3.venueId);
        if (postShareParameters$$3.shareToTwitter == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(1);
            parcel$$389.writeInt(postShareParameters$$3.shareToTwitter.booleanValue() ? 1 : 0);
        }
        if (postShareParameters$$3.shareToFacebook == null) {
            parcel$$389.writeInt(-1);
        } else {
            parcel$$389.writeInt(1);
            parcel$$389.writeInt(postShareParameters$$3.shareToFacebook.booleanValue() ? 1 : 0);
        }
        parcel$$389.writeParcelable(postShareParameters$$3.channel, flags$$136);
        parcel$$389.writeString(postShareParameters$$3.caption);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public PostShareParameters getParcel() {
        return this.postShareParameters$$0;
    }
}
