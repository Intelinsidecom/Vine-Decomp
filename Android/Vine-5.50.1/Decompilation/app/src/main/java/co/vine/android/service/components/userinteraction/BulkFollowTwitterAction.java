package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class BulkFollowTwitterAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        Object userIdsToFollow = b.getLongArray("follow_ids");
        boolean followAll = b.getBoolean("followAll", false);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", "followers", "twitter");
        Map<String, Object> params = new HashMap<>();
        params.put("userIds", userIdsToFollow);
        params.put("followAll", Integer.valueOf(followAll ? 1 : 0));
        String json = new GsonBuilder().create().toJson(params);
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, json, (NetworkOperationReader) vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
