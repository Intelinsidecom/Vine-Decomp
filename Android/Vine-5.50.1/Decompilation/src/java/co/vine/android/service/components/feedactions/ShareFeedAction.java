package co.vine.android.service.components.feedactions;

import android.os.Bundle;
import co.vine.android.api.FeedMetadata;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import com.mobileapptracker.MATEvent;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class ShareFeedAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long feedUserId = b.getLong("feed_user_id");
        String comment = b.getString("comment");
        FeedMetadata.FeedType feedType = (FeedMetadata.FeedType) b.getSerializable("feed_type");
        long coverPostId = b.getLong("coverPostId");
        boolean postToVine = b.getBoolean("postToVine");
        boolean postToTwitter = b.getBoolean("postToTwitter");
        boolean postToFacebook = b.getBoolean("postToFacebook");
        boolean postToTumblr = b.getBoolean("postToTumblr");
        String baseUrl = request.api.getBaseUrl();
        Object[] objArr = new Object[4];
        objArr[0] = "feeds";
        objArr[1] = MATEvent.SHARE;
        objArr[2] = feedType == FeedMetadata.FeedType.PROFILE ? "profile" : "channel";
        objArr[3] = Long.valueOf(feedUserId);
        StringBuilder url = VineAPI.buildUponUrl(baseUrl, objArr);
        ArrayList<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("description", comment));
        params.add(new BasicNameValuePair("coverPostId", Long.toString(coverPostId)));
        params.add(new BasicNameValuePair("postToVine", postToVine ? "1" : "0"));
        params.add(new BasicNameValuePair("postToTwitter", postToTwitter ? "1" : "0"));
        params.add(new BasicNameValuePair("postToFacebook", postToFacebook ? "1" : "0"));
        params.add(new BasicNameValuePair("postToTumblr", postToTumblr ? "1" : "0"));
        if (postToTumblr) {
            params.add(new BasicNameValuePair("tumblrOauthToken", b.getString("tumblrOauthToken")));
            params.add(new BasicNameValuePair("tumblrOauthSecret", b.getString("tumblrOauthSecret")));
        }
        VineParserReader vp = VineParserReader.createParserReader(39);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, params, vp).execute();
        return new VineServiceActionResult(vp, op);
    }
}
