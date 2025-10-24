package co.vine.android.service.components.suggestions;

import android.content.Context;
import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VinePagedUsersResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.service.VineDatabaseHelperInterface;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import com.mobileapptracker.MATEvent;

/* loaded from: classes.dex */
public final class FetchUsersTypeaheadAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        return executeUserFetch(request.context, request.dbHelper, request.key, request.b, request.api, request.networkFactory, "mention");
    }

    static VineServiceActionResult executeUserFetch(Context context, VineDatabaseHelperInterface dbHelper, String key, Bundle b, VineAPI api, NetworkOperationFactory<VineAPI> networkFactory, String searchTerm) {
        String query = b.getString("q");
        String encodedQuery = VineAPI.encode(query.trim());
        StringBuilder url = VineAPI.buildUponUrl(api.getBaseUrl(), "users", MATEvent.SEARCH, encodedQuery);
        VineAPI.addParam(url, "st", searchTerm);
        VineParserReader vp = VineParserReader.createParserReader(3);
        NetworkOperation op = networkFactory.createBasicAuthGetRequest(context, url, api, vp, key).execute();
        if (op.isOK()) {
            VinePagedUsersResponse.Data users = (VinePagedUsersResponse.Data) vp.getParsedObject();
            if (users.items != null) {
                dbHelper.mergeUsers(users.items, 0, null, 0, 0, null);
                b.putString("q", query);
                b.putParcelableArrayList("users", users.items);
            }
        }
        return new VineServiceActionResult(vp, op);
    }
}
