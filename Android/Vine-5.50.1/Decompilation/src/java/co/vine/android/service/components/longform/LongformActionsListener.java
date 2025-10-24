package co.vine.android.service.components.longform;

import co.vine.android.api.VineEndlessLikesRecord;
import java.util.List;

/* loaded from: classes.dex */
public interface LongformActionsListener {
    void onFetchEndlessLikesComplete(String str, int i, String str2, List<VineEndlessLikesRecord> list);
}
