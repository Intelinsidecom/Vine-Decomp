package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrashUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class ClosePromptAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) throws JSONException {
        Bundle b = request.b;
        JSONObject postBody = new JSONObject();
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "feedback");
        try {
            postBody.put("reference", b.getString("reference"));
            postBody.put("action", "close");
        } catch (JSONException e) {
            CrashUtil.logOrThrowInDebug(e);
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, postBody, (NetworkOperationReader) vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
