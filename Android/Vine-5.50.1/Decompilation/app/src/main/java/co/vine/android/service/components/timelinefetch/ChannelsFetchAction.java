package co.vine.android.service.components.timelinefetch;

import android.os.Bundle;
import co.vine.android.api.VineChannel;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VineChannelsResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ChannelsFetchAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        VineChannelsResponse.Data data;
        Bundle b = request.b;
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "channels", "featured");
        VineAPI.addParam(url, "include_timelines", 1);
        VineAPI.addParam(url, "size", 30);
        VineAPI.addParam(url, "all", 1);
        VineParserReader vp = VineParserReader.createParserReader(18);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK() && (data = (VineChannelsResponse.Data) vp.getParsedObject()) != null) {
            ArrayList<VineChannel> channels = data.items;
            b.putParcelableArrayList("channels", channels);
        }
        return new VineServiceActionResult(vp, op);
    }
}
