package co.vine.android.service.components.suggestions;

import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VinePagedUsersResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class FetchSuggestedUsersAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", Long.valueOf(request.sessionOwnerId), "following", "suggested");
        VineParserReader vp = VineParserReader.createParserReader(3);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            VinePagedUsersResponse.Data users = (VinePagedUsersResponse.Data) vp.getParsedObject();
            if (users.items != null) {
                request.b.putParcelableArrayList("users", users.items);
            }
        }
        return new VineServiceActionResult(vp, op);
    }
}
