package co.vine.android.service.components.settings;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public final class HomeFeedSettingsSaveAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", "me", "timelines", "home", "settings");
        HashMap<String, String> params = (HashMap) b.getSerializable("notificationSettings");
        ArrayList<BasicNameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
            pairs.add(pair);
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPutRequest(request.context, url, request.api, pairs, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
