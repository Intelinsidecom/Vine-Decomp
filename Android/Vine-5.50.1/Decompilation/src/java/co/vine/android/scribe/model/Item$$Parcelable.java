package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class Item$$Parcelable implements Parcelable, ParcelWrapper<Item> {
    public static final Item$$Parcelable$Creator$$4 CREATOR = new Item$$Parcelable$Creator$$4();
    private Item item$$8;

    public Item$$Parcelable(Parcel parcel$$130) {
        Item item$$10;
        if (parcel$$130.readInt() == -1) {
            item$$10 = null;
        } else {
            item$$10 = readco_vine_android_scribe_model_Item(parcel$$130);
        }
        this.item$$8 = item$$10;
    }

    public Item$$Parcelable(Item item$$12) {
        this.item$$8 = item$$12;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$131, int flags) {
        if (this.item$$8 == null) {
            parcel$$131.writeInt(-1);
        } else {
            parcel$$131.writeInt(1);
            writeco_vine_android_scribe_model_Item(this.item$$8, parcel$$131, flags);
        }
    }

    private Item readco_vine_android_scribe_model_Item(Parcel parcel$$132) {
        PostOrRepostDetails postOrRepostDetails$$7;
        ActivityDetails activityDetails$$7;
        MosaicDetails mosaicDetails$$9;
        SuggestionDetails suggestionDetails$$7;
        MosaicDetails mosaicDetails$$10;
        CommentDetails commentDetails$$7;
        ItemPosition itemPosition$$7;
        TagDetails tagDetails$$7;
        UserDetails userDetails$$9;
        Item item$$9 = new Item();
        if (parcel$$132.readInt() == -1) {
            postOrRepostDetails$$7 = null;
        } else {
            postOrRepostDetails$$7 = readco_vine_android_scribe_model_PostOrRepostDetails(parcel$$132);
        }
        item$$9.postOrRepost = postOrRepostDetails$$7;
        item$$9.reference = parcel$$132.readString();
        item$$9.itemType = parcel$$132.readString();
        if (parcel$$132.readInt() == -1) {
            activityDetails$$7 = null;
        } else {
            activityDetails$$7 = readco_vine_android_scribe_model_ActivityDetails(parcel$$132);
        }
        item$$9.activity = activityDetails$$7;
        if (parcel$$132.readInt() == -1) {
            mosaicDetails$$9 = null;
        } else {
            mosaicDetails$$9 = readco_vine_android_scribe_model_MosaicDetails(parcel$$132);
        }
        item$$9.postMosaic = mosaicDetails$$9;
        if (parcel$$132.readInt() == -1) {
            suggestionDetails$$7 = null;
        } else {
            suggestionDetails$$7 = readco_vine_android_scribe_model_SuggestionDetails(parcel$$132);
        }
        item$$9.suggestion = suggestionDetails$$7;
        if (parcel$$132.readInt() == -1) {
            mosaicDetails$$10 = null;
        } else {
            mosaicDetails$$10 = readco_vine_android_scribe_model_MosaicDetails(parcel$$132);
        }
        item$$9.userMosaic = mosaicDetails$$10;
        if (parcel$$132.readInt() == -1) {
            commentDetails$$7 = null;
        } else {
            commentDetails$$7 = readco_vine_android_scribe_model_CommentDetails(parcel$$132);
        }
        item$$9.comment = commentDetails$$7;
        if (parcel$$132.readInt() == -1) {
            itemPosition$$7 = null;
        } else {
            itemPosition$$7 = readco_vine_android_scribe_model_ItemPosition(parcel$$132);
        }
        item$$9.position = itemPosition$$7;
        if (parcel$$132.readInt() == -1) {
            tagDetails$$7 = null;
        } else {
            tagDetails$$7 = readco_vine_android_scribe_model_TagDetails(parcel$$132);
        }
        item$$9.tag = tagDetails$$7;
        if (parcel$$132.readInt() == -1) {
            userDetails$$9 = null;
        } else {
            userDetails$$9 = readco_vine_android_scribe_model_UserDetails(parcel$$132);
        }
        item$$9.user = userDetails$$9;
        return item$$9;
    }

    private PostOrRepostDetails readco_vine_android_scribe_model_PostOrRepostDetails(Parcel parcel$$133) {
        Long long$$38;
        Long long$$39;
        Long long$$40;
        Boolean boolean$$20;
        Long long$$41;
        Byline byline$$7;
        Boolean boolean$$21;
        Boolean boolean$$22;
        PostOrRepostDetails postOrRepostDetails$$6 = new PostOrRepostDetails();
        int int$$136 = parcel$$133.readInt();
        if (int$$136 < 0) {
            long$$38 = null;
        } else {
            long$$38 = Long.valueOf(parcel$$133.readLong());
        }
        postOrRepostDetails$$6.postAuthorId = long$$38;
        postOrRepostDetails$$6.longformId = parcel$$133.readString();
        int int$$137 = parcel$$133.readInt();
        if (int$$137 < 0) {
            long$$39 = null;
        } else {
            long$$39 = Long.valueOf(parcel$$133.readLong());
        }
        postOrRepostDetails$$6.repostId = long$$39;
        int int$$138 = parcel$$133.readInt();
        if (int$$138 < 0) {
            long$$40 = null;
        } else {
            long$$40 = Long.valueOf(parcel$$133.readLong());
        }
        postOrRepostDetails$$6.repostAuthorId = long$$40;
        int int$$139 = parcel$$133.readInt();
        if (int$$139 < 0) {
            boolean$$20 = null;
        } else {
            boolean$$20 = Boolean.valueOf(parcel$$133.readInt() == 1);
        }
        postOrRepostDetails$$6.hasSimilarPosts = boolean$$20;
        int int$$140 = parcel$$133.readInt();
        if (int$$140 < 0) {
            long$$41 = null;
        } else {
            long$$41 = Long.valueOf(parcel$$133.readLong());
        }
        postOrRepostDetails$$6.postId = long$$41;
        if (parcel$$133.readInt() == -1) {
            byline$$7 = null;
        } else {
            byline$$7 = readco_vine_android_scribe_model_Byline(parcel$$133);
        }
        postOrRepostDetails$$6.byline = byline$$7;
        int int$$147 = parcel$$133.readInt();
        if (int$$147 < 0) {
            boolean$$21 = null;
        } else {
            boolean$$21 = Boolean.valueOf(parcel$$133.readInt() == 1);
        }
        postOrRepostDetails$$6.liked = boolean$$21;
        int int$$148 = parcel$$133.readInt();
        if (int$$148 < 0) {
            boolean$$22 = null;
        } else {
            boolean$$22 = Boolean.valueOf(parcel$$133.readInt() == 1);
        }
        postOrRepostDetails$$6.reposted = boolean$$22;
        return postOrRepostDetails$$6;
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$134) {
        ArrayList<Long> list$$15;
        Long long$$42;
        ArrayList<Long> list$$16;
        Long long$$43;
        Byline byline$$6 = new Byline();
        byline$$6.detailedDescription = parcel$$134.readString();
        byline$$6.actionTitle = parcel$$134.readString();
        int int$$141 = parcel$$134.readInt();
        if (int$$141 < 0) {
            list$$15 = null;
        } else {
            list$$15 = new ArrayList<>();
            for (int int$$142 = 0; int$$142 < int$$141; int$$142++) {
                int int$$143 = parcel$$134.readInt();
                if (int$$143 < 0) {
                    long$$42 = null;
                } else {
                    long$$42 = Long.valueOf(parcel$$134.readLong());
                }
                list$$15.add(long$$42);
            }
        }
        byline$$6.postIds = list$$15;
        int int$$144 = parcel$$134.readInt();
        if (int$$144 < 0) {
            list$$16 = null;
        } else {
            list$$16 = new ArrayList<>();
            for (int int$$145 = 0; int$$145 < int$$144; int$$145++) {
                int int$$146 = parcel$$134.readInt();
                if (int$$146 < 0) {
                    long$$43 = null;
                } else {
                    long$$43 = Long.valueOf(parcel$$134.readLong());
                }
                list$$16.add(long$$43);
            }
        }
        byline$$6.userIds = list$$16;
        byline$$6.description = parcel$$134.readString();
        byline$$6.iconUrl = parcel$$134.readString();
        byline$$6.body = parcel$$134.readString();
        byline$$6.actionIconUrl = parcel$$134.readString();
        return byline$$6;
    }

    private ActivityDetails readco_vine_android_scribe_model_ActivityDetails(Parcel parcel$$135) {
        Long long$$44;
        Integer integer$$12;
        ActivityDetails activityDetails$$6 = new ActivityDetails();
        int int$$149 = parcel$$135.readInt();
        if (int$$149 < 0) {
            long$$44 = null;
        } else {
            long$$44 = Long.valueOf(parcel$$135.readLong());
        }
        activityDetails$$6.activityId = long$$44;
        int int$$150 = parcel$$135.readInt();
        if (int$$150 < 0) {
            integer$$12 = null;
        } else {
            integer$$12 = Integer.valueOf(parcel$$135.readInt());
        }
        activityDetails$$6.nMore = integer$$12;
        activityDetails$$6.activityType = parcel$$135.readString();
        return activityDetails$$6;
    }

    private MosaicDetails readco_vine_android_scribe_model_MosaicDetails(Parcel parcel$$136) {
        MosaicDetails mosaicDetails$$8 = new MosaicDetails();
        mosaicDetails$$8.mosaicType = parcel$$136.readString();
        mosaicDetails$$8.link = parcel$$136.readString();
        return mosaicDetails$$8;
    }

    private SuggestionDetails readco_vine_android_scribe_model_SuggestionDetails(Parcel parcel$$137) {
        SuggestionDetails suggestionDetails$$6 = new SuggestionDetails();
        suggestionDetails$$6.suggestedQuery = parcel$$137.readString();
        return suggestionDetails$$6;
    }

    private CommentDetails readco_vine_android_scribe_model_CommentDetails(Parcel parcel$$138) {
        Long long$$45;
        Long long$$46;
        CommentDetails commentDetails$$6 = new CommentDetails();
        int int$$151 = parcel$$138.readInt();
        if (int$$151 < 0) {
            long$$45 = null;
        } else {
            long$$45 = Long.valueOf(parcel$$138.readLong());
        }
        commentDetails$$6.commentId = long$$45;
        int int$$152 = parcel$$138.readInt();
        if (int$$152 < 0) {
            long$$46 = null;
        } else {
            long$$46 = Long.valueOf(parcel$$138.readLong());
        }
        commentDetails$$6.authorId = long$$46;
        return commentDetails$$6;
    }

    private ItemPosition readco_vine_android_scribe_model_ItemPosition(Parcel parcel$$139) {
        Integer integer$$13;
        ItemPosition itemPosition$$6 = new ItemPosition();
        int int$$153 = parcel$$139.readInt();
        if (int$$153 < 0) {
            integer$$13 = null;
        } else {
            integer$$13 = Integer.valueOf(parcel$$139.readInt());
        }
        itemPosition$$6.offset = integer$$13;
        return itemPosition$$6;
    }

    private TagDetails readco_vine_android_scribe_model_TagDetails(Parcel parcel$$140) {
        TagDetails tagDetails$$6 = new TagDetails();
        tagDetails$$6.tagId = parcel$$140.readString();
        return tagDetails$$6;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$141) {
        Boolean boolean$$23;
        Long long$$47;
        UserDetails userDetails$$8 = new UserDetails();
        int int$$154 = parcel$$141.readInt();
        if (int$$154 < 0) {
            boolean$$23 = null;
        } else {
            boolean$$23 = Boolean.valueOf(parcel$$141.readInt() == 1);
        }
        userDetails$$8.following = boolean$$23;
        int int$$155 = parcel$$141.readInt();
        if (int$$155 < 0) {
            long$$47 = null;
        } else {
            long$$47 = Long.valueOf(parcel$$141.readLong());
        }
        userDetails$$8.userId = long$$47;
        return userDetails$$8;
    }

    private void writeco_vine_android_scribe_model_Item(Item item$$11, Parcel parcel$$142, int flags$$59) {
        if (item$$11.postOrRepost == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_PostOrRepostDetails(item$$11.postOrRepost, parcel$$142, flags$$59);
        }
        parcel$$142.writeString(item$$11.reference);
        parcel$$142.writeString(item$$11.itemType);
        if (item$$11.activity == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_ActivityDetails(item$$11.activity, parcel$$142, flags$$59);
        }
        if (item$$11.postMosaic == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$11.postMosaic, parcel$$142, flags$$59);
        }
        if (item$$11.suggestion == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_SuggestionDetails(item$$11.suggestion, parcel$$142, flags$$59);
        }
        if (item$$11.userMosaic == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_MosaicDetails(item$$11.userMosaic, parcel$$142, flags$$59);
        }
        if (item$$11.comment == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_CommentDetails(item$$11.comment, parcel$$142, flags$$59);
        }
        if (item$$11.position == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_ItemPosition(item$$11.position, parcel$$142, flags$$59);
        }
        if (item$$11.tag == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_TagDetails(item$$11.tag, parcel$$142, flags$$59);
        }
        if (item$$11.user == null) {
            parcel$$142.writeInt(-1);
        } else {
            parcel$$142.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(item$$11.user, parcel$$142, flags$$59);
        }
    }

    private void writeco_vine_android_scribe_model_PostOrRepostDetails(PostOrRepostDetails postOrRepostDetails$$8, Parcel parcel$$143, int flags$$60) {
        if (postOrRepostDetails$$8.postAuthorId == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeLong(postOrRepostDetails$$8.postAuthorId.longValue());
        }
        parcel$$143.writeString(postOrRepostDetails$$8.longformId);
        if (postOrRepostDetails$$8.repostId == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeLong(postOrRepostDetails$$8.repostId.longValue());
        }
        if (postOrRepostDetails$$8.repostAuthorId == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeLong(postOrRepostDetails$$8.repostAuthorId.longValue());
        }
        if (postOrRepostDetails$$8.hasSimilarPosts == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeInt(postOrRepostDetails$$8.hasSimilarPosts.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$8.postId == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeLong(postOrRepostDetails$$8.postId.longValue());
        }
        if (postOrRepostDetails$$8.byline == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            writeco_vine_android_scribe_model_Byline(postOrRepostDetails$$8.byline, parcel$$143, flags$$60);
        }
        if (postOrRepostDetails$$8.liked == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeInt(postOrRepostDetails$$8.liked.booleanValue() ? 1 : 0);
        }
        if (postOrRepostDetails$$8.reposted == null) {
            parcel$$143.writeInt(-1);
        } else {
            parcel$$143.writeInt(1);
            parcel$$143.writeInt(postOrRepostDetails$$8.reposted.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$8, Parcel parcel$$144, int flags$$61) {
        parcel$$144.writeString(byline$$8.detailedDescription);
        parcel$$144.writeString(byline$$8.actionTitle);
        if (byline$$8.postIds == null) {
            parcel$$144.writeInt(-1);
        } else {
            parcel$$144.writeInt(byline$$8.postIds.size());
            Iterator<Long> it = byline$$8.postIds.iterator();
            while (it.hasNext()) {
                Long long$$48 = it.next();
                if (long$$48 == null) {
                    parcel$$144.writeInt(-1);
                } else {
                    parcel$$144.writeInt(1);
                    parcel$$144.writeLong(long$$48.longValue());
                }
            }
        }
        if (byline$$8.userIds == null) {
            parcel$$144.writeInt(-1);
        } else {
            parcel$$144.writeInt(byline$$8.userIds.size());
            Iterator<Long> it2 = byline$$8.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$49 = it2.next();
                if (long$$49 == null) {
                    parcel$$144.writeInt(-1);
                } else {
                    parcel$$144.writeInt(1);
                    parcel$$144.writeLong(long$$49.longValue());
                }
            }
        }
        parcel$$144.writeString(byline$$8.description);
        parcel$$144.writeString(byline$$8.iconUrl);
        parcel$$144.writeString(byline$$8.body);
        parcel$$144.writeString(byline$$8.actionIconUrl);
    }

    private void writeco_vine_android_scribe_model_ActivityDetails(ActivityDetails activityDetails$$8, Parcel parcel$$145, int flags$$62) {
        if (activityDetails$$8.activityId == null) {
            parcel$$145.writeInt(-1);
        } else {
            parcel$$145.writeInt(1);
            parcel$$145.writeLong(activityDetails$$8.activityId.longValue());
        }
        if (activityDetails$$8.nMore == null) {
            parcel$$145.writeInt(-1);
        } else {
            parcel$$145.writeInt(1);
            parcel$$145.writeInt(activityDetails$$8.nMore.intValue());
        }
        parcel$$145.writeString(activityDetails$$8.activityType);
    }

    private void writeco_vine_android_scribe_model_MosaicDetails(MosaicDetails mosaicDetails$$11, Parcel parcel$$146, int flags$$63) {
        parcel$$146.writeString(mosaicDetails$$11.mosaicType);
        parcel$$146.writeString(mosaicDetails$$11.link);
    }

    private void writeco_vine_android_scribe_model_SuggestionDetails(SuggestionDetails suggestionDetails$$8, Parcel parcel$$147, int flags$$64) {
        parcel$$147.writeString(suggestionDetails$$8.suggestedQuery);
    }

    private void writeco_vine_android_scribe_model_CommentDetails(CommentDetails commentDetails$$8, Parcel parcel$$148, int flags$$65) {
        if (commentDetails$$8.commentId == null) {
            parcel$$148.writeInt(-1);
        } else {
            parcel$$148.writeInt(1);
            parcel$$148.writeLong(commentDetails$$8.commentId.longValue());
        }
        if (commentDetails$$8.authorId == null) {
            parcel$$148.writeInt(-1);
        } else {
            parcel$$148.writeInt(1);
            parcel$$148.writeLong(commentDetails$$8.authorId.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ItemPosition(ItemPosition itemPosition$$8, Parcel parcel$$149, int flags$$66) {
        if (itemPosition$$8.offset == null) {
            parcel$$149.writeInt(-1);
        } else {
            parcel$$149.writeInt(1);
            parcel$$149.writeInt(itemPosition$$8.offset.intValue());
        }
    }

    private void writeco_vine_android_scribe_model_TagDetails(TagDetails tagDetails$$8, Parcel parcel$$150, int flags$$67) {
        parcel$$150.writeString(tagDetails$$8.tagId);
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$10, Parcel parcel$$151, int flags$$68) {
        if (userDetails$$10.following == null) {
            parcel$$151.writeInt(-1);
        } else {
            parcel$$151.writeInt(1);
            parcel$$151.writeInt(userDetails$$10.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$10.userId == null) {
            parcel$$151.writeInt(-1);
        } else {
            parcel$$151.writeInt(1);
            parcel$$151.writeLong(userDetails$$10.userId.longValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public Item getParcel() {
        return this.item$$8;
    }
}
