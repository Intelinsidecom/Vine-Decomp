package co.vine.android.widget.tabs;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TabSavedState implements Parcelable {
    public static final Parcelable.Creator<TabSavedState> CREATOR = new Parcelable.Creator<TabSavedState>() { // from class: co.vine.android.widget.tabs.TabSavedState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TabSavedState createFromParcel(Parcel in) {
            return new TabSavedState(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TabSavedState[] newArray(int size) {
            return new TabSavedState[size];
        }
    };
    public final String[] tags;

    public TabSavedState(ArrayList<TabInfo> infos) {
        int size = infos.size();
        String[] tabs = new String[size];
        for (int i = 0; i < size; i++) {
            TabInfo info = infos.get(i);
            tabs[i] = info.mTag;
        }
        this.tags = tabs;
    }

    public TabSavedState(Parcel in) {
        this.tags = in.createStringArray();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(this.tags);
    }
}
