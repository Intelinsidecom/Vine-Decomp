package co.vine.android.api;

import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class ListSection<T> implements Parcelable {
    public abstract String getAnchorStr();

    public abstract String getBackAnchor();

    public abstract int getDisplayCount();

    public abstract ArrayList<T> getItems();

    public abstract String getType();

    public static ListSection create(int displayCount, String anchor, String backAnchor, String type, ArrayList items) {
        return new AutoParcel_ListSection(displayCount, anchor, backAnchor, type, items);
    }
}
