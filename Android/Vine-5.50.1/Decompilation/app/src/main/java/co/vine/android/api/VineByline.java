package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public class VineByline implements Parcelable {
    public static final Parcelable.Creator<VineByline> CREATOR = new Parcelable.Creator<VineByline>() { // from class: co.vine.android.api.VineByline.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineByline createFromParcel(Parcel in) {
            return new VineByline(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineByline[] newArray(int size) {
            return new VineByline[size];
        }
    };
    public String body;
    public VineBylineAction bylineAction;
    public ArrayList<VineEntity> entities;
    public String iconUrl;

    public VineByline(String body, String iconUrl, ArrayList<VineEntity> entities, VineBylineAction bylineAction) {
        this.body = body;
        this.iconUrl = iconUrl;
        this.entities = entities;
        this.bylineAction = bylineAction;
    }

    public VineByline(Parcel in) {
        this.body = in.readString();
        this.iconUrl = in.readString();
        this.entities = new ArrayList<>();
        in.readTypedList(this.entities, VineEntity.CREATOR);
        this.bylineAction = (VineBylineAction) Parcels.unwrap(in.readParcelable(VineBylineAction.class.getClassLoader()));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.body);
        out.writeString(this.iconUrl);
        if (this.entities != null) {
            out.writeTypedList(this.entities);
        }
        out.writeParcelable(Parcels.wrap(this.bylineAction), flags);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
