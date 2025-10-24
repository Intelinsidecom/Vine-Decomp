package co.vine.android.service.components.userinteraction;

import android.os.Bundle;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public class FollowAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Bundle b = request.b;
        long userIdToFollow = b.getLong("follow_id");
        boolean notify = b.getBoolean("notify");
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "users", Long.valueOf(userIdToFollow), "followers");
        VineParserReader vp = VineParserReader.createParserReader(1);
        NetworkOperation op = request.networkFactory.createBasicAuthPostRequest(request.context, url, request.api, null, vp).execute();
        if (op.isOK()) {
            VineDatabaseHelper dbHelper = (VineDatabaseHelper) request.dbHelper;
            dbHelper.updateUserFollowingFlag(userIdToFollow, true);
            dbHelper.addFollow(userIdToFollow, request.sessionOwnerId, 0L, notify);
            dbHelper.markLastUser(1, String.valueOf(request.sessionOwnerId), "order_id ASC");
        }
        return new VineServiceActionResult(vp, op);
    }
}
