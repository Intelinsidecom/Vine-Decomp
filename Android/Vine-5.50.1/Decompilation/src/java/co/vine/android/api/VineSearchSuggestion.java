package co.vine.android.api;

import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class VineSearchSuggestion implements Parcelable {
    public abstract String getQuery();

    public static VineSearchSuggestion create(String query) {
        return new AutoParcel_VineSearchSuggestion(query);
    }
}
