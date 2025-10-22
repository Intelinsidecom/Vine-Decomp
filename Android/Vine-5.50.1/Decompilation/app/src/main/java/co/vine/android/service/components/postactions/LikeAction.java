package co.vine.android.service.components.postactions;

import android.os.Bundle;
import co.vine.android.api.TimelineItem;
import co.vine.android.api.VineLike;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePost;
import co.vine.android.client.VineAPI;
import co.vine.android.model.MutableTimelineItemModel;
import co.vine.android.model.impl.VineModelFactory;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class LikeAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long postId = b.getLong("post_id");
        boolean notify = b.getBoolean("notify", true);
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "posts", Long.valueOf(postId), "likes");
        long repostId = b.getLong("repost_id");
        if (repostId > 0) {
            VineAPI.addParam(url, "repostId", repostId);
        }
        VineParserReader vp = VineParserReader.createParserReader(10);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, null, vp).execute();
        if (op.isOK()) {
            Long likeId = (Long) vp.getParsedObject();
            long meUserId = b.getLong("user_id");
            String username = b.getString("username");
            VineLike like = new VineLike();
            like.likeId = likeId.longValue();
            like.userId = meUserId;
            like.username = username;
            like.postId = postId;
            MutableTimelineItemModel model = VineModelFactory.getMutableModelInstance().getMutableTimelineItemModel();
            TimelineItem item = model.getTimelineItem(postId);
            if (item != null) {
                ((VinePost) item).addLike(like);
                model.updateTimelineItem(item.getId(), item);
            }
            request.dbHelper.addLike(like, notify);
        }
        return new VineServiceActionResult(vp, op);
    }
}
