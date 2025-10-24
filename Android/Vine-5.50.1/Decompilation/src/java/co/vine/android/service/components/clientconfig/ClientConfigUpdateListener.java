package co.vine.android.service.components.clientconfig;

import co.vine.android.api.response.VineClientFlags;

/* loaded from: classes.dex */
public interface ClientConfigUpdateListener {
    void onFetchClientFlagsComplete(String str, int i, String str2, VineClientFlags vineClientFlags);
}
