package co.vine.android.service.components.share;

import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.analytics.FlurryUtils;
import com.mobileapptracker.MATEvent;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class NetworkShareAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long postId = b.getLong("post_id");
        String network = b.getString("network");
        String message = b.getString("message");
        String oauthToken = b.getString("token");
        String oauthSecret = b.getString("secret");
        FlurryUtils.trackSharePost(network, postId);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), MATEvent.SHARE, network);
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        if (!TextUtils.isEmpty(message)) {
            params.add(new BasicNameValuePair("message", message));
        }
        if ("tumblr".equalsIgnoreCase("tumblr")) {
            params.add(new BasicNameValuePair("tumblrOauthToken", oauthToken));
            params.add(new BasicNameValuePair("tumblrOauthSecret", oauthSecret));
        }
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, params, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
