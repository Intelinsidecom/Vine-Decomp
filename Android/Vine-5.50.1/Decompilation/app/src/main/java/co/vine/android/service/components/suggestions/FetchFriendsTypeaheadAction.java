package co.vine.android.service.components.suggestions;

import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;

/* loaded from: classes.dex */
public final class FetchFriendsTypeaheadAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        return FetchUsersTypeaheadAction.executeUserFetch(request.context, request.dbHelper, request.key, request.b, request.api, request.networkFactory, "message");
    }
}
