package co.vine.android.service.components.longform;

import android.os.Bundle;
import co.vine.android.api.VineEndlessLikesPostBody;
import co.vine.android.api.VineEndlessLikesPostRecord;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import com.bluelinelabs.logansquare.LoganSquare;
import java.io.IOException;
import java.util.ArrayList;
import org.parceler.Parcels;

/* loaded from: classes.dex */
public final class PostEndlessLikesAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        String longformId = b.getString("longform_id");
        VineEndlessLikesPostRecord postRecord = (VineEndlessLikesPostRecord) Parcels.unwrap(b.getParcelable("endless_likes"));
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "longforms", longformId, "endlessLikes");
        VineParserReader vp = VineParserReader.createParserReader(1);
        ArrayList<VineEndlessLikesPostRecord> likes = new ArrayList<>();
        likes.add(postRecord);
        VineEndlessLikesPostBody body = new VineEndlessLikesPostBody();
        body.likes = likes;
        try {
            NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, LoganSquare.serialize(body), (NetworkOperationReader) vp).execute();
            return new VineServiceActionResult(vp, op);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
