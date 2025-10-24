package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class CommentDetails$$Parcelable implements Parcelable, ParcelWrapper<CommentDetails> {
    public static final CommentDetails$$Parcelable$Creator$$20 CREATOR = new CommentDetails$$Parcelable$Creator$$20();
    private CommentDetails commentDetails$$9;

    public CommentDetails$$Parcelable(Parcel parcel$$242) {
        CommentDetails commentDetails$$11;
        if (parcel$$242.readInt() == -1) {
            commentDetails$$11 = null;
        } else {
            commentDetails$$11 = readco_vine_android_scribe_model_CommentDetails(parcel$$242);
        }
        this.commentDetails$$9 = commentDetails$$11;
    }

    public CommentDetails$$Parcelable(CommentDetails commentDetails$$13) {
        this.commentDetails$$9 = commentDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$243, int flags) {
        if (this.commentDetails$$9 == null) {
            parcel$$243.writeInt(-1);
        } else {
            parcel$$243.writeInt(1);
            writeco_vine_android_scribe_model_CommentDetails(this.commentDetails$$9, parcel$$243, flags);
        }
    }

    private CommentDetails readco_vine_android_scribe_model_CommentDetails(Parcel parcel$$244) {
        Long long$$65;
        Long long$$66;
        CommentDetails commentDetails$$10 = new CommentDetails();
        int int$$203 = parcel$$244.readInt();
        if (int$$203 < 0) {
            long$$65 = null;
        } else {
            long$$65 = Long.valueOf(parcel$$244.readLong());
        }
        commentDetails$$10.commentId = long$$65;
        int int$$204 = parcel$$244.readInt();
        if (int$$204 < 0) {
            long$$66 = null;
        } else {
            long$$66 = Long.valueOf(parcel$$244.readLong());
        }
        commentDetails$$10.authorId = long$$66;
        return commentDetails$$10;
    }

    private void writeco_vine_android_scribe_model_CommentDetails(CommentDetails commentDetails$$12, Parcel parcel$$245, int flags$$91) {
        if (commentDetails$$12.commentId == null) {
            parcel$$245.writeInt(-1);
        } else {
            parcel$$245.writeInt(1);
            parcel$$245.writeLong(commentDetails$$12.commentId.longValue());
        }
        if (commentDetails$$12.authorId == null) {
            parcel$$245.writeInt(-1);
        } else {
            parcel$$245.writeInt(1);
            parcel$$245.writeLong(commentDetails$$12.authorId.longValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public CommentDetails getParcel() {
        return this.commentDetails$$9;
    }
}
