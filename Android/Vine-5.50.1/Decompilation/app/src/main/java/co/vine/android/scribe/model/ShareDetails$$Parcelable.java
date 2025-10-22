package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ShareDetails$$Parcelable implements Parcelable, ParcelWrapper<ShareDetails> {
    public static final ShareDetails$$Parcelable$Creator$$25 CREATOR = new ShareDetails$$Parcelable$Creator$$25();
    private ShareDetails shareDetails$$9;

    public ShareDetails$$Parcelable(Parcel parcel$$305) {
        ShareDetails shareDetails$$11;
        if (parcel$$305.readInt() == -1) {
            shareDetails$$11 = null;
        } else {
            shareDetails$$11 = readco_vine_android_scribe_model_ShareDetails(parcel$$305);
        }
        this.shareDetails$$9 = shareDetails$$11;
    }

    public ShareDetails$$Parcelable(ShareDetails shareDetails$$13) {
        this.shareDetails$$9 = shareDetails$$13;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$306, int flags) {
        if (this.shareDetails$$9 == null) {
            parcel$$306.writeInt(-1);
        } else {
            parcel$$306.writeInt(1);
            writeco_vine_android_scribe_model_ShareDetails(this.shareDetails$$9, parcel$$306, flags);
        }
    }

    private ShareDetails readco_vine_android_scribe_model_ShareDetails(Parcel parcel$$307) {
        ArrayList<String> list$$27;
        ArrayList<VMRecipient> list$$28;
        VMRecipient vMRecipient$$18;
        Boolean boolean$$44;
        ShareDetails shareDetails$$10 = new ShareDetails();
        int int$$259 = parcel$$307.readInt();
        if (int$$259 < 0) {
            list$$27 = null;
        } else {
            list$$27 = new ArrayList<>();
            for (int int$$260 = 0; int$$260 < int$$259; int$$260++) {
                list$$27.add(parcel$$307.readString());
            }
        }
        shareDetails$$10.shareTargets = list$$27;
        shareDetails$$10.postId = parcel$$307.readString();
        int int$$261 = parcel$$307.readInt();
        if (int$$261 < 0) {
            list$$28 = null;
        } else {
            list$$28 = new ArrayList<>();
            for (int int$$262 = 0; int$$262 < int$$261; int$$262++) {
                if (parcel$$307.readInt() == -1) {
                    vMRecipient$$18 = null;
                } else {
                    vMRecipient$$18 = readco_vine_android_scribe_model_VMRecipient(parcel$$307);
                }
                list$$28.add(vMRecipient$$18);
            }
        }
        shareDetails$$10.messageRecipients = list$$28;
        int int$$267 = parcel$$307.readInt();
        if (int$$267 < 0) {
            boolean$$44 = null;
        } else {
            boolean$$44 = Boolean.valueOf(parcel$$307.readInt() == 1);
        }
        shareDetails$$10.hasComment = boolean$$44;
        return shareDetails$$10;
    }

    private VMRecipient readco_vine_android_scribe_model_VMRecipient(Parcel parcel$$308) {
        Boolean boolean$$41;
        Boolean boolean$$42;
        UserDetails userDetails$$24;
        VMRecipient vMRecipient$$17 = new VMRecipient();
        int int$$263 = parcel$$308.readInt();
        if (int$$263 < 0) {
            boolean$$41 = null;
        } else {
            boolean$$41 = Boolean.valueOf(parcel$$308.readInt() == 1);
        }
        vMRecipient$$17.isPhone = boolean$$41;
        int int$$264 = parcel$$308.readInt();
        if (int$$264 < 0) {
            boolean$$42 = null;
        } else {
            boolean$$42 = Boolean.valueOf(parcel$$308.readInt() == 1);
        }
        vMRecipient$$17.isEmail = boolean$$42;
        if (parcel$$308.readInt() == -1) {
            userDetails$$24 = null;
        } else {
            userDetails$$24 = readco_vine_android_scribe_model_UserDetails(parcel$$308);
        }
        vMRecipient$$17.user = userDetails$$24;
        return vMRecipient$$17;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$309) {
        Boolean boolean$$43;
        Long long$$82;
        UserDetails userDetails$$23 = new UserDetails();
        int int$$265 = parcel$$309.readInt();
        if (int$$265 < 0) {
            boolean$$43 = null;
        } else {
            boolean$$43 = Boolean.valueOf(parcel$$309.readInt() == 1);
        }
        userDetails$$23.following = boolean$$43;
        int int$$266 = parcel$$309.readInt();
        if (int$$266 < 0) {
            long$$82 = null;
        } else {
            long$$82 = Long.valueOf(parcel$$309.readLong());
        }
        userDetails$$23.userId = long$$82;
        return userDetails$$23;
    }

    private void writeco_vine_android_scribe_model_ShareDetails(ShareDetails shareDetails$$12, Parcel parcel$$310, int flags$$115) {
        if (shareDetails$$12.shareTargets == null) {
            parcel$$310.writeInt(-1);
        } else {
            parcel$$310.writeInt(shareDetails$$12.shareTargets.size());
            for (String string$$6 : shareDetails$$12.shareTargets) {
                parcel$$310.writeString(string$$6);
            }
        }
        parcel$$310.writeString(shareDetails$$12.postId);
        if (shareDetails$$12.messageRecipients == null) {
            parcel$$310.writeInt(-1);
        } else {
            parcel$$310.writeInt(shareDetails$$12.messageRecipients.size());
            for (VMRecipient vMRecipient$$19 : shareDetails$$12.messageRecipients) {
                if (vMRecipient$$19 == null) {
                    parcel$$310.writeInt(-1);
                } else {
                    parcel$$310.writeInt(1);
                    writeco_vine_android_scribe_model_VMRecipient(vMRecipient$$19, parcel$$310, flags$$115);
                }
            }
        }
        if (shareDetails$$12.hasComment == null) {
            parcel$$310.writeInt(-1);
        } else {
            parcel$$310.writeInt(1);
            parcel$$310.writeInt(shareDetails$$12.hasComment.booleanValue() ? 1 : 0);
        }
    }

    private void writeco_vine_android_scribe_model_VMRecipient(VMRecipient vMRecipient$$20, Parcel parcel$$311, int flags$$116) {
        if (vMRecipient$$20.isPhone == null) {
            parcel$$311.writeInt(-1);
        } else {
            parcel$$311.writeInt(1);
            parcel$$311.writeInt(vMRecipient$$20.isPhone.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$20.isEmail == null) {
            parcel$$311.writeInt(-1);
        } else {
            parcel$$311.writeInt(1);
            parcel$$311.writeInt(vMRecipient$$20.isEmail.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$20.user == null) {
            parcel$$311.writeInt(-1);
        } else {
            parcel$$311.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(vMRecipient$$20.user, parcel$$311, flags$$116);
        }
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$25, Parcel parcel$$312, int flags$$117) {
        if (userDetails$$25.following == null) {
            parcel$$312.writeInt(-1);
        } else {
            parcel$$312.writeInt(1);
            parcel$$312.writeInt(userDetails$$25.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$25.userId == null) {
            parcel$$312.writeInt(-1);
        } else {
            parcel$$312.writeInt(1);
            parcel$$312.writeLong(userDetails$$25.userId.longValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ShareDetails getParcel() {
        return this.shareDetails$$9;
    }
}
