package co.vine.android.service.components.authentication;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public final class DigitsVerifyAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        VineParserReader vp = VineParserReader.createParserReader(1);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", "verifyDigits");
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, getBody(request.b), vp).execute();
        return new VineServiceActionResult(vp, op);
    }

    private List<BasicNameValuePair> getBody(Bundle bundle) {
        String authHeader = bundle.getString("digits_verify_credentials_authorization");
        String providerHeader = bundle.getString("digits_auth_service_provider");
        List<BasicNameValuePair> body = new ArrayList<>(2);
        body.add(new BasicNameValuePair("digits_verify_credentials_authorization", authHeader));
        body.add(new BasicNameValuePair("digits_auth_service_provider", providerHeader));
        return body;
    }
}
