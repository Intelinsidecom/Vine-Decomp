package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public class ResubscribeAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long unsubscriberId = request.sessionOwnerId;
        long userId = b.getLong("unsubscribed_id");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", Long.valueOf(unsubscriberId), "unsubscribes", Long.toString(userId));
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthDeleteRequest(request.context, url, request.api, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
