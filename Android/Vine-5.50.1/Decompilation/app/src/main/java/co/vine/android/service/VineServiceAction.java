package co.vine.android.service;

import android.content.Context;
import android.os.Bundle;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperationFactory;

/* loaded from: classes.dex */
public abstract class VineServiceAction {
    public abstract VineServiceActionResult doAction(Request request);

    public static final class Request {
        public final String accountName;
        public final VineAPI api;
        public final Bundle b;
        public final Context context;
        public final VineDatabaseHelperInterface dbHelper;
        public final String key;
        public final NetworkOperationFactory<VineAPI> networkFactory;
        public final VineServiceInterface service;
        public final long sessionOwnerId;

        public Request(Bundle b, Context context, VineServiceInterface service, String key, String accountName, long sessionOwnerId, VineDatabaseHelperInterface dbHelper, VineAPI api, NetworkOperationFactory<VineAPI> networkFactory) {
            this.b = b;
            this.context = context;
            this.service = service;
            this.key = key;
            this.accountName = accountName;
            this.sessionOwnerId = sessionOwnerId;
            this.dbHelper = dbHelper;
            this.api = api;
            this.networkFactory = networkFactory;
        }
    }
}
