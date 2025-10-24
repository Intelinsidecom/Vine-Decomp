package co.vine.android.service.components.feedactions;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public class DeleteFeedAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long feedId = b.getLong("feed_id");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "feeds", Long.valueOf(feedId));
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthDeleteRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            b.putLong("feed_id", feedId);
        }
        return new VineServiceActionResult(vp, op);
    }
}
