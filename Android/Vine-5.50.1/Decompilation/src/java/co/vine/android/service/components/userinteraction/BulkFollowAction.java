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
public class BulkFollowAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        ArrayList<String> userIdsToFollow = b.getStringArrayList("follow_ids");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", "followers");
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        Iterator<String> it = userIdsToFollow.iterator();
        while (it.hasNext()) {
            String userId = it.next();
            params.add(new BasicNameValuePair("userIds[]", userId));
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, params, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
