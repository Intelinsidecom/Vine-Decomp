package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class VMRecipient$$Parcelable implements Parcelable, ParcelWrapper<VMRecipient> {
    public static final VMRecipient$$Parcelable$Creator$$11 CREATOR = new VMRecipient$$Parcelable$Creator$$11();
    private VMRecipient vMRecipient$$8;

    public VMRecipient$$Parcelable(Parcel parcel$$185) {
        VMRecipient vMRecipient$$10;
        if (parcel$$185.readInt() == -1) {
            vMRecipient$$10 = null;
        } else {
            vMRecipient$$10 = readco_vine_android_scribe_model_VMRecipient(parcel$$185);
        }
        this.vMRecipient$$8 = vMRecipient$$10;
    }

    public VMRecipient$$Parcelable(VMRecipient vMRecipient$$12) {
        this.vMRecipient$$8 = vMRecipient$$12;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$186, int flags) {
        if (this.vMRecipient$$8 == null) {
            parcel$$186.writeInt(-1);
        } else {
            parcel$$186.writeInt(1);
            writeco_vine_android_scribe_model_VMRecipient(this.vMRecipient$$8, parcel$$186, flags);
        }
    }

    private VMRecipient readco_vine_android_scribe_model_VMRecipient(Parcel parcel$$187) {
        Boolean boolean$$24;
        Boolean boolean$$25;
        UserDetails userDetails$$12;
        VMRecipient vMRecipient$$9 = new VMRecipient();
        int int$$160 = parcel$$187.readInt();
        if (int$$160 < 0) {
            boolean$$24 = null;
        } else {
            boolean$$24 = Boolean.valueOf(parcel$$187.readInt() == 1);
        }
        vMRecipient$$9.isPhone = boolean$$24;
        int int$$161 = parcel$$187.readInt();
        if (int$$161 < 0) {
            boolean$$25 = null;
        } else {
            boolean$$25 = Boolean.valueOf(parcel$$187.readInt() == 1);
        }
        vMRecipient$$9.isEmail = boolean$$25;
        if (parcel$$187.readInt() == -1) {
            userDetails$$12 = null;
        } else {
            userDetails$$12 = readco_vine_android_scribe_model_UserDetails(parcel$$187);
        }
        vMRecipient$$9.user = userDetails$$12;
        return vMRecipient$$9;
    }

    private UserDetails readco_vine_android_scribe_model_UserDetails(Parcel parcel$$188) {
        Boolean boolean$$26;
        Long long$$50;
        UserDetails userDetails$$11 = new UserDetails();
        int int$$162 = parcel$$188.readInt();
        if (int$$162 < 0) {
            boolean$$26 = null;
        } else {
            boolean$$26 = Boolean.valueOf(parcel$$188.readInt() == 1);
        }
        userDetails$$11.following = boolean$$26;
        int int$$163 = parcel$$188.readInt();
        if (int$$163 < 0) {
            long$$50 = null;
        } else {
            long$$50 = Long.valueOf(parcel$$188.readLong());
        }
        userDetails$$11.userId = long$$50;
        return userDetails$$11;
    }

    private void writeco_vine_android_scribe_model_VMRecipient(VMRecipient vMRecipient$$11, Parcel parcel$$189, int flags$$76) {
        if (vMRecipient$$11.isPhone == null) {
            parcel$$189.writeInt(-1);
        } else {
            parcel$$189.writeInt(1);
            parcel$$189.writeInt(vMRecipient$$11.isPhone.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$11.isEmail == null) {
            parcel$$189.writeInt(-1);
        } else {
            parcel$$189.writeInt(1);
            parcel$$189.writeInt(vMRecipient$$11.isEmail.booleanValue() ? 1 : 0);
        }
        if (vMRecipient$$11.user == null) {
            parcel$$189.writeInt(-1);
        } else {
            parcel$$189.writeInt(1);
            writeco_vine_android_scribe_model_UserDetails(vMRecipient$$11.user, parcel$$189, flags$$76);
        }
    }

    private void writeco_vine_android_scribe_model_UserDetails(UserDetails userDetails$$13, Parcel parcel$$190, int flags$$77) {
        if (userDetails$$13.following == null) {
            parcel$$190.writeInt(-1);
        } else {
            parcel$$190.writeInt(1);
            parcel$$190.writeInt(userDetails$$13.following.booleanValue() ? 1 : 0);
        }
        if (userDetails$$13.userId == null) {
            parcel$$190.writeInt(-1);
        } else {
            parcel$$190.writeInt(1);
            parcel$$190.writeLong(userDetails$$13.userId.longValue());
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public VMRecipient getParcel() {
        return this.vMRecipient$$8;
    }
}
