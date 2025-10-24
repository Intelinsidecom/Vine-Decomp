package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class Byline$$Parcelable implements Parcelable, ParcelWrapper<Byline> {
    public static final Byline$$Parcelable$Creator$$50 CREATOR = new Byline$$Parcelable$Creator$$50();
    private Byline byline$$15;

    public Byline$$Parcelable(Parcel parcel$$450) {
        Byline byline$$17;
        if (parcel$$450.readInt() == -1) {
            byline$$17 = null;
        } else {
            byline$$17 = readco_vine_android_scribe_model_Byline(parcel$$450);
        }
        this.byline$$15 = byline$$17;
    }

    public Byline$$Parcelable(Byline byline$$19) {
        this.byline$$15 = byline$$19;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$451, int flags) {
        if (this.byline$$15 == null) {
            parcel$$451.writeInt(-1);
        } else {
            parcel$$451.writeInt(1);
            writeco_vine_android_scribe_model_Byline(this.byline$$15, parcel$$451, flags);
        }
    }

    private Byline readco_vine_android_scribe_model_Byline(Parcel parcel$$452) {
        ArrayList<Long> list$$42;
        Long long$$88;
        ArrayList<Long> list$$43;
        Long long$$89;
        Byline byline$$16 = new Byline();
        byline$$16.detailedDescription = parcel$$452.readString();
        byline$$16.actionTitle = parcel$$452.readString();
        int int$$352 = parcel$$452.readInt();
        if (int$$352 < 0) {
            list$$42 = null;
        } else {
            list$$42 = new ArrayList<>();
            for (int int$$353 = 0; int$$353 < int$$352; int$$353++) {
                int int$$354 = parcel$$452.readInt();
                if (int$$354 < 0) {
                    long$$88 = null;
                } else {
                    long$$88 = Long.valueOf(parcel$$452.readLong());
                }
                list$$42.add(long$$88);
            }
        }
        byline$$16.postIds = list$$42;
        int int$$355 = parcel$$452.readInt();
        if (int$$355 < 0) {
            list$$43 = null;
        } else {
            list$$43 = new ArrayList<>();
            for (int int$$356 = 0; int$$356 < int$$355; int$$356++) {
                int int$$357 = parcel$$452.readInt();
                if (int$$357 < 0) {
                    long$$89 = null;
                } else {
                    long$$89 = Long.valueOf(parcel$$452.readLong());
                }
                list$$43.add(long$$89);
            }
        }
        byline$$16.userIds = list$$43;
        byline$$16.description = parcel$$452.readString();
        byline$$16.iconUrl = parcel$$452.readString();
        byline$$16.body = parcel$$452.readString();
        byline$$16.actionIconUrl = parcel$$452.readString();
        return byline$$16;
    }

    private void writeco_vine_android_scribe_model_Byline(Byline byline$$18, Parcel parcel$$453, int flags$$150) {
        parcel$$453.writeString(byline$$18.detailedDescription);
        parcel$$453.writeString(byline$$18.actionTitle);
        if (byline$$18.postIds == null) {
            parcel$$453.writeInt(-1);
        } else {
            parcel$$453.writeInt(byline$$18.postIds.size());
            Iterator<Long> it = byline$$18.postIds.iterator();
            while (it.hasNext()) {
                Long long$$90 = it.next();
                if (long$$90 == null) {
                    parcel$$453.writeInt(-1);
                } else {
                    parcel$$453.writeInt(1);
                    parcel$$453.writeLong(long$$90.longValue());
                }
            }
        }
        if (byline$$18.userIds == null) {
            parcel$$453.writeInt(-1);
        } else {
            parcel$$453.writeInt(byline$$18.userIds.size());
            Iterator<Long> it2 = byline$$18.userIds.iterator();
            while (it2.hasNext()) {
                Long long$$91 = it2.next();
                if (long$$91 == null) {
                    parcel$$453.writeInt(-1);
                } else {
                    parcel$$453.writeInt(1);
                    parcel$$453.writeLong(long$$91.longValue());
                }
            }
        }
        parcel$$453.writeString(byline$$18.description);
        parcel$$453.writeString(byline$$18.iconUrl);
        parcel$$453.writeString(byline$$18.body);
        parcel$$453.writeString(byline$$18.actionIconUrl);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public Byline getParcel() {
        return this.byline$$15;
    }
}
