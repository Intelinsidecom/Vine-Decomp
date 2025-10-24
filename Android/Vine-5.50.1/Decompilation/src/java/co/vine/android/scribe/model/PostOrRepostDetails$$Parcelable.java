package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class PostOrRepostDetails$$Parcelable implements Parcelable, ParcelWrapper<PostOrRepostDetails> {
    public static final PostOrRepostDetails$$Parcelable$Creator$$19 CREATOR = new PostOrRepostDetails$$Parcelable$Creator$$19();
    private PostOrRepostDetails postOrRepostDetails$$9;

    public PostOrRepostDetails$$Parcelable(Parcel parcel$$235) {
        PostOrRepostDetails postOrRepostDetails$$11;
        if (parcel$$235.readInt() == -1) {
            postOrRepostDetails$$11 = null;
        } else {
            postOrRepostDetails$$11 = readco_vine_android_scribe_model_PostOrRepostDetails(parcel$$235);
        }
        this.postOrRepostDetails$$9 = postOrRepostDetails$$11;
    }

    public PostOrRepostDetails$$Parcelable(PostOrRepostDetails postOrRepostDetails$$13) {
        this.postOrRepostDetails$$9 = postOrRepostDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$236, int flags) {
        if (this.postOrRepostDetails$$9 == null) {
            parcel$$236.writeInt(-1);
        } else {
            parcel$$236.writeInt(1);
            writeco_vine_android_scribe_model_PostOrRepostDetails(this.postOrRepostDetails$$9, parcel$$236, flags);
        }
    }

    private PostOrRepostDetails readco_vine_android_scribe_model_PostOrRepostDetails(Parcel parcel$$237) {
        Long long$$57;
        Long long$$58;
        Long long$$59;
        Boolean boolean$$30;
        Long long$$60;
        Byline byline$$10;
        Boolean boolean$$31;
        Boolean boolean$$32;
        PostOrRepostDetails postOrRepostDetails$$10 = new PostOrRepostDetails();
        int int$$190 = parcel$$237.readInt();
        if (int$$190 < 0) {
            long$$57 = null;
        } else {
            long$$57 = Long.valueOf(parcel$$237.readLong());
        }
        postOrRepostDetails$$10.postAuthorId = long$$57;
        postOrRepostDetails$$10.longformId = parcel$$237.readString();
        int int$$191 = parcel$$237.readInt();
        if (int$$191 < 0) {
            long$$58 = null;
        } else {
            long$$58 = Long.valueOf(parcel$$237.readLong());
        }
        postOrRepostDetails$$10.repostId = long$$58;
        int int$$192 = parcel$$237.readInt();
        if (int$$192 < 0) {
            long$$59 = null;
        } else {
            long$$59 = Long.valueOf(parcel$$237.readLong());
        }
        postOrRepostDetails$$10.repostAuthorId = long$$59;
        int int$$193 = parcel$$237.readInt();
        if (int$$193 < 0) {
            boolean$$30 = null;
        } else {
            boolean$$30 = Boolean.valueOf(parcel$$237.readInt() == 1);
        }
        postOrRepostDetails$$10.hasSimilarPosts = boolean$$30;
        int int$$194 = parcel$$237.readInt();
        if (int$$194 < 0) {
            long$$60 = null;
        } else {
            long$$60 = Long.valueOf(parcel$$237.readLong());
        }
        postOrRepostDetails$$10.postId = long$$60;
        if (parcel$$237.readInt() == -1) {
            byline$$10 = null;
        } else {
            byline$$10 = readco_vine_android_scribe_model_Byline(parcel$$237);
        }
        postOrRepostDetails$$10.byline = byline$$10;
        int int$$201 = parcel$$237.readInt();
        if (int$$201 < 0) {
            boolean$$31 = null;
        } else {
            boolean$$31 = Boolean.valueOf(parcel$$237.readInt() == 1);
        }
        postOrRepostDetails$$10.liked = boolean$$31;
        int int$$202 = parcel$$237.readInt();
        if (int$$202 < 0) {
            boolean$$32 = null;
        } else {
            boolean$$32 = Boolean.valueOf(parcel$$237.readInt() == 1);
        }
        postOrRepostDetails$$10.reposted = boolean$$32;
        return postOrRepostDetails$$10;
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$238) {
        ArrayList<Long> list$$20;
        Long long$$61;
        ArrayList<Long> list$$21;
        Long long$$62;
        Byline byline$$9 = new Byline();
        byline$$9.detailedDescription = parcel$$238.readString();
        byline$$9.actionTitle = parcel$$238.readString();
        int int$$195 = parcel$$238.readInt();
        if (int$$195 < 0) {
            list$$20 = null;
        } else {
            list$$20 = new ArrayList<>();
            for (int int$$196 = 0; int$$196 < int$$195; int$$196++) {
                int int$$197 = parcel$$238.readInt();
                if (int$$197 < 0) {
                    long$$61 = null;
                } else {
                    long$$61 = Long.valueOf(parcel$$238.readLong());
                }
                list$$20.add(long$$61);
            }
        }
        byline$$9.postIds = list$$20;
        int int$$198 = parcel$$238.readInt();
        if (int$$198 < 0) {
            list$$21 = null;
        } else {
            list$$21 = new ArrayList<>();
            for (int int$$199 = 0; int$$199 < int$$198; int$$199++) {
                int int$$200 = parcel$$238.readInt();
                if (int$$200 < 0) {
                    long$$62 = null;
                } else {
                    long$$62 = Long.valueOf(parcel$$238.readLong());
                }
                list$$21.add(long$$62);
            }
        }
        byline$$9.userIds = list$$21;
        byline$$9.description = parcel$$238.readString();
        byline$$9.iconUrl = parcel$$238.readString();
        byline$$9.body = parcel$$238.readString();
        byline$$9.actionIconUrl = parcel$$238.readString();
        return byline$$9;
    }

    private void writeco_vine_android_scribe_model_PostOrRepostDetails(PostOrRepostDetails postOrRepostDetails$$12, Parcel parcel$$239, int flags$$89) {
        if (postOrRepostDetails$$12.postAuthorId == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeLong(postOrRepostDetails$$12.postAuthorId.longValue());
        }
        parcel$$239.writeString(postOrRepostDetails$$12.longformId);
        if (postOrRepostDetails$$12.repostId == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeLong(postOrRepostDetails$$12.repostId.longValue());
        }
        if (postOrRepostDetails$$12.repostAuthorId == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeLong(postOrRepostDetails$$12.repostAuthorId.longValue());
        }
        if (postOrRepostDetails$$12.hasSimilarPosts == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeInt(postOrRepostDetails$$12.hasSimilarPosts.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$12.postId == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeLong(postOrRepostDetails$$12.postId.longValue());
        }
        if (postOrRepostDetails$$12.byline == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            writeco_vine_android_scribe_model_Byline(postOrRepostDetails$$12.byline, parcel$$239, flags$$89);
        }
        if (postOrRepostDetails$$12.liked == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeInt(postOrRepostDetails$$12.liked.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$12.reposted == null) {
            parcel$$239.writeInt(-1);
        } else {
            parcel$$239.writeInt(1);
            parcel$$239.writeInt(postOrRepostDetails$$12.reposted.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$11, Parcel parcel$$240, int flags$$90) {
        parcel$$240.writeString(byline$$11.detailedDescription);
        parcel$$240.writeString(byline$$11.actionTitle);
        if (byline$$11.postIds == null) {
            parcel$$240.writeInt(-1);
        } else {
            parcel$$240.writeInt(byline$$11.postIds.size());
            Iterator<Long> it = byline$$11.postIds.iterator();
            while (it.hasNext()) {
                Long long$$63 = it.next();
                if (long$$63 == null) {
                    parcel$$240.writeInt(-1);
                } else {
                    parcel$$240.writeInt(1);
                    parcel$$240.writeLong(long$$63.longValue());
                }
            }
        }
        if (byline$$11.userIds == null) {
            parcel$$240.writeInt(-1);
        } else {
            parcel$$240.writeInt(byline$$11.userIds.size());
            Iterator<Long> it2 = byline$$11.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$64 = it2.next();
                if (long$$64 == null) {
                    parcel$$240.writeInt(-1);
                } else {
                    parcel$$240.writeInt(1);
                    parcel$$240.writeLong(long$$64.longValue());
                }
            }
        }
        parcel$$240.writeString(byline$$11.description);
        parcel$$240.writeString(byline$$11.iconUrl);
        parcel$$240.writeString(byline$$11.body);
        parcel$$240.writeString(byline$$11.actionIconUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public PostOrRepostDetails getParcel() {
        return this.postOrRepostDetails$$9;
    }
}
