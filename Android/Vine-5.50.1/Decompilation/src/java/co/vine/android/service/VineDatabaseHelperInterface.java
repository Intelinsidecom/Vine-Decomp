package co.vine.android.service;

import android.database.Cursor;
import co.vine.android.api.VineLike;
import co.vine.android.api.VineRepost;
import co.vine.android.api.VineUser;
import co.vine.android.model.VineTag;
import co.vine.android.util.LongSparseArray;
import java.util.Collection;

/* loaded from: classes.dex */
public interface VineDatabaseHelperInterface {
    int addLike(VineLike vineLike, boolean z);

    void clearAllData();

    Cursor getOldestSortId(int i);

    void markLastTag();

    int mergeSearchedTags(Collection<VineTag> collection, int i, int i2);

    int mergeUsers(Collection<VineUser> collection, int i, String str, int i2, int i3, LongSparseArray<Long> longSparseArray);

    int removeLike(long j, long j2, boolean z);

    int revine(VineRepost vineRepost, long j, boolean z);

    int unRevine(long j, long j2, boolean z, boolean z2);
}
