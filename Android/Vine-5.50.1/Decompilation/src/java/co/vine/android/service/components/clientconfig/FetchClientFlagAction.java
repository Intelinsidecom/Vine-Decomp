package co.vine.android.service.components.clientconfig;

import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VineClientFlags;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class FetchClientFlagAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        StringBuilder url = VineAPI.buildUponUrl(request.api.getConfigUrl(), "clientflags");
        VineParserReader vp = VineParserReader.createParserReader(29);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            VineClientFlags clientFlags = (VineClientFlags) vp.getParsedObject();
            request.b.putParcelable("client_flags", Parcels.wrap(clientFlags));
        }
        return new VineServiceActionResult(vp, op);
    }
}
