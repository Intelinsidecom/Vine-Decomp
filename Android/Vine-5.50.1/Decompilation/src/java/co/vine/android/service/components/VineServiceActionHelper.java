package co.vine.android.service.components;

import android.os.Bundle;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.UrlCachePolicy;

/* loaded from: classes.dex */
public class VineServiceActionHelper {
    public static UrlCachePolicy assignCachePolicy(NetworkOperation op, Bundle b, UrlCachePolicy defaultPolicy) {
        UrlCachePolicy cp = (UrlCachePolicy) b.getParcelable("cache_policy");
        if (cp == null) {
            cp = defaultPolicy;
            b.putParcelable("cache_policy", defaultPolicy);
        }
        op.setCachePolicy(cp);
        return cp;
    }

    public static void assignPollingHeader(NetworkOperation op, Bundle b) {
        VineAPI.addPollingHeader(op, b.getBoolean("is_polling", false));
    }

    public static StringBuilder getUserUrl(VineAPI api, long userId) {
        return VineAPI.buildUponUrl(api.getBaseUrl(), "users", "profiles", Long.valueOf(userId));
    }
}
