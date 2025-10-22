package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class UnrevineAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        boolean following = b.getBoolean("following");
        long postId = b.getLong("post_id");
        long myRepostId = b.getLong("my_repost_id");
        long repostId = b.getLong("repost_id");
        boolean notify = b.getBoolean("notify", true);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), "repost", Long.valueOf(myRepostId));
        if (repostId > 0) {
            VineAPI.addParam(url, "repostId", repostId);
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthDeleteRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            request.dbHelper.unRevine(postId, request.sessionOwnerId, following, notify);
        }
        return new VineServiceActionResult(vp, op);
    }
}
