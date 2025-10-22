package co.vine.android.service.components.suggestions;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.response.VinePagedTagsResponse;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineDatabaseHelperInterface;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.service.VineServicePagedAction;
import co.vine.android.util.analytics.FlurryUtils;
import com.mobileapptracker.MATEvent;

/* loaded from: classes.dex */
public final class FetchTagsTypeaheadAction extends VineServicePagedAction {
    @Override // co.vine.android.service.VineServicePagedAction
    protected String getUniqueMarker(VineServiceAction.Request request) {
        return request.b.getString("q");
    }

    @Override // co.vine.android.service.VineServicePagedAction
    protected VineServiceActionResult doPagedAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        FlurryUtils.trackSearchTags();
        String query = b.getString("q");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "tags", MATEvent.SEARCH, VineAPI.encode(query));
        addPagingInfoToRequest(url, query);
        VineAPI.addParam(url, "size", 50);
        VineParserReader vp = VineParserReader.createParserReader(14);
        NetworkOperation op = request.networkFactory.createBasicAuthGetRequest(request.context, url, request.api, vp, request.key).execute();
        if (op.isOK()) {
            VinePagedTagsResponse.Data tags = (VinePagedTagsResponse.Data) vp.getParsedObject();
            processTagsResult(b, tags, request.dbHelper);
            updatePagingInfoFromResult(tags.nextPage, tags.anchor, query);
        }
        return new VineServiceActionResult(vp, op);
    }

    private void processTagsResult(Bundle b, VinePagedTagsResponse.Data tags, VineDatabaseHelperInterface dbHelper) {
        if (tags.items != null && !tags.items.isEmpty()) {
            dbHelper.mergeSearchedTags(tags.items, tags.nextPage, tags.previousPage);
            b.putParcelableArrayList("tags", tags.items);
        }
        if (tags.nextPage <= 0) {
            dbHelper.markLastTag();
        }
    }
}
