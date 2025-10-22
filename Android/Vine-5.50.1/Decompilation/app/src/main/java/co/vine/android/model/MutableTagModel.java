package co.vine.android.model;

import java.util.Collection;

/* loaded from: classes.dex */
public interface MutableTagModel extends TagModel {
    void addTagsForQuery(String str, Collection<VineTag> collection);
}
