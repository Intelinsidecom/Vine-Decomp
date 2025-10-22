package co.vine.android.service.components.longform;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VineEndlessLikesResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class FetchEndlessLikesAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        String longformId = b.getString("longform_id");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "longforms", longformId, "endlessLikes");
        VineParserReader vp = VineParserReader.createParserReader(38);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            VineEndlessLikesResponse endlessLikes = (VineEndlessLikesResponse) vp.getParsedObject();
            b.putSerializable("endless_likes", endlessLikes.data);
        }
        return new VineServiceActionResult(vp, op);
    }
}
