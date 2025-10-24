package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VineRepost;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public final class RevineAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long postId = b.getLong("post_id");
        long repostId = b.getLong("repost_id");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), "repost");
        if (repostId > 0) {
            VineAPI.addParam(url, "repostId", repostId);
        }
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        VineParserReader vp = VineParserReader.createParserReader(19);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, params, vp).execute();
        if (op.isOK()) {
            VineRepost repost = (VineRepost) vp.getParsedObject();
            String username = b.getString("username");
            repost.userId = request.sessionOwnerId;
            repost.username = username;
            b.putSerializable("repost", repost);
            request.dbHelper.revine(repost, request.sessionOwnerId, false);
        }
        return new VineServiceActionResult(vp, op);
    }
}
