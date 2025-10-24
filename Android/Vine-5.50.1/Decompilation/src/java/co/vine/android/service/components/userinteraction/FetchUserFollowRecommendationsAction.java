package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VinePagedUsersResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
final class FetchUserFollowRecommendationsAction extends VineServiceAction {
    FetchUserFollowRecommendationsAction() {
    }

    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long userId = b.getLong("user_id");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "recommendations", "similarUsersByFollowers", Long.valueOf(userId));
        VineParserReader vp = VineParserReader.createParserReader(3);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp, request.key).execute();
        if (op.isOK()) {
            VinePagedUsersResponse.Data users = (VinePagedUsersResponse.Data) vp.getParsedObject();
            if (users.items != null) {
                b.putParcelableArrayList("users", users.items);
            }
        }
        return new VineServiceActionResult(vp, op);
    }
}
