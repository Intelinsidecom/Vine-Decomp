package co.vine.android.service.components.recommendations;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import com.edisonwang.android.slog.SLog;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UserFeedbackAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) throws JSONException {
        Bundle b = request.b;
        long userId = b.getLong("user_id");
        String boost = b.getString("recommendationBoost");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "recommendations", "feedback", "users", Long.toString(userId));
        JSONObject params = new JSONObject();
        try {
            params.put("recommendationBoost", boost);
        } catch (JSONException exception) {
            SLog.e("An error occurred while serializing recommendationBoost for user feedback", (Throwable) exception);
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, params, (NetworkOperationReader) vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
