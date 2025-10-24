package co.vine.android.service;

import android.content.Context;
import android.os.Bundle;
import co.vine.android.network.NetworkOperation;
import java.util.Collection;
import twitter4j.Twitter;

/* loaded from: classes.dex */
public interface VineServiceInterface {
    VineServiceActionResult fetchActivity(long j, Context context, Bundle bundle);

    VineServiceActionResult fetchActivityCounts(long j, Context context, Bundle bundle);

    NetworkOperation fetchUsersMe(Bundle bundle);

    Collection<NetworkOperation.CancelableRequest> getActiveRequests();

    NetworkOperation getPosts(StringBuilder sb, int i, int i2, VineDatabaseHelperInterface vineDatabaseHelperInterface, String str, String str2, Bundle bundle, boolean z);

    Twitter getTwitter();

    void setTwitter(Twitter twitter);
}
