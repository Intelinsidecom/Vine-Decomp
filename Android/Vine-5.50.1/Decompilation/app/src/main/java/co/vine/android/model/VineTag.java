package co.vine.android.model;

import android.os.Parcelable;

/* loaded from: classes.dex */
public abstract class VineTag implements Parcelable {
    public abstract long getPostCount();

    public abstract long getTagId();

    public abstract String getTagName();

    public static VineTag create(String tagName, long tagId, long postCount) {
        return new AutoParcel_VineTag(tagName, tagId, postCount);
    }
}
