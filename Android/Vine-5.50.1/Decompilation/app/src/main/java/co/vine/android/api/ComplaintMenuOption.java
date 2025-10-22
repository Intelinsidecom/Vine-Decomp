package co.vine.android.api;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class ComplaintMenuOption implements Parcelable {
    public static final Parcelable.Creator<ComplaintMenuOption> CREATOR = new Parcelable.Creator<ComplaintMenuOption>() { // from class: co.vine.android.api.ComplaintMenuOption.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplaintMenuOption createFromParcel(Parcel source) {
            return new ComplaintMenuOption(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ComplaintMenuOption[] newArray(int size) {
            return new ComplaintMenuOption[size];
        }
    };
    public ComplaintChoice[] complaintChoices;
    public String complaintType;
    public String prompt;

    public ComplaintMenuOption() {
    }

    public ComplaintMenuOption(Parcel in) {
        this.complaintType = in.readString();
        this.prompt = in.readString();
        this.complaintChoices = (ComplaintChoice[]) in.createTypedArray(ComplaintChoice.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.complaintType);
        dest.writeString(this.prompt);
        dest.writeTypedArray(this.complaintChoices, 0);
    }

    public static class ComplaintChoice implements Parcelable {
        public static final Parcelable.Creator<ComplaintChoice> CREATOR = new Parcelable.Creator<ComplaintChoice>() { // from class: co.vine.android.api.ComplaintMenuOption.ComplaintChoice.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ComplaintChoice createFromParcel(Parcel source) {
                return new ComplaintChoice(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ComplaintChoice[] newArray(int size) {
                return new ComplaintChoice[size];
            }
        };
        public String confirmation;
        public boolean selected;
        public String title;
        public String value;

        public ComplaintChoice() {
            this.selected = false;
        }

        public ComplaintChoice(Parcel in) {
            this.title = in.readString();
            this.value = in.readString();
            this.confirmation = in.readString();
            this.selected = in.readInt() != 0;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.value);
            dest.writeString(this.confirmation);
            dest.writeInt(this.selected ? 1 : 0);
        }
    }
}
