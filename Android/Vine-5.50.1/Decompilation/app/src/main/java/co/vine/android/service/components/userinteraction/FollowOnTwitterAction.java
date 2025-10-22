package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public class FollowOnTwitterAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long userIdToFollow = b.getLong("follow_id");
        b.getBoolean("notify");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", Long.valueOf(userIdToFollow), "follow-on-twitter");
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, null, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
