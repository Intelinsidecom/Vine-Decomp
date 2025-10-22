package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class BulkFollowChannelsAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        ArrayList<String> channelIds = b.getStringArrayList("channels");
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("action", "add"));
        Iterator<String> it = channelIds.iterator();
        while (it.hasNext()) {
            String channelId = it.next();
            params.add(new BasicNameValuePair("channelIds[]", channelId));
        }
        StringBuilder uri = VineAPI.buildUponUrl(request.api.getBaseUrl(), "channels", "followers");
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, uri, request.api, params, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
