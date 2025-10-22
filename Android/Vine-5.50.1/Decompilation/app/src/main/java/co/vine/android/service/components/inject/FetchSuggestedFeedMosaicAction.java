package co.vine.android.service.components.inject;

import co.vine.android.api.TimelineItem;
import co.vine.android.api.VinePagedData;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class FetchSuggestedFeedMosaicAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "timelines", "suggested", "posts", Long.valueOf(request.sessionOwnerId), "inject");
        VineParserReader vp = VineParserReader.createParserReader(36);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp).execute();
        if (op.isOK()) {
            VinePagedData<TimelineItem> suggestedFeedInject = (VinePagedData) vp.getParsedObject();
            request.b.putParcelable("mosaic_only_timeline", suggestedFeedInject);
        }
        return new VineServiceActionResult(vp, op);
    }
}
