package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePost;
import co.vine.android.client.VineAPI;
import co.vine.android.model.MutableTimelineItemModel;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class UnlikeAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long postId = b.getLong("post_id");
        long repostId = b.getLong("repost_id");
        boolean notify = b.getBoolean("notify", true);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), "likes");
        if (repostId > 0) {
            VineAPI.addParam(url, "repostId", repostId);
        }
        VineParserReader vp = VineParserReader.createParserReader(10);
        NetworkOperation op = request.networkFactory.createBasicAuthDeleteRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            MutableTimelineItemModel model = VineModelFactory.getMutableModelInstance().getMutableTimelineItemModel();
            TimelineItem item = model.getTimelineItem(postId);
            if (item != null) {
                ((VinePost) item).removeLike(request.sessionOwnerId);
                model.updateTimelineItem(item.getId(), item);
            }
            request.dbHelper.removeLike(postId, request.sessionOwnerId, notify);
        }
        return new VineServiceActionResult(vp, op);
    }
}
