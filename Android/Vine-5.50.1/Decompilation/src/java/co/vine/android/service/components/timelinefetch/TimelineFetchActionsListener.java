package co.vine.android.service.components.timelinefetch;

import android.os.Bundle;
import co.vine.android.network.UrlCachePolicy;

/* loaded from: classes.dex */
public interface TimelineFetchActionsListener {
    void onChannelsFetched(String str, int i, String str2, Bundle bundle);

    void onTimelineFetched(String str, int i, String str2, int i2, int i3, boolean z, boolean z2, int i4, String str3, UrlCachePolicy urlCachePolicy, boolean z3, Bundle bundle);
}
