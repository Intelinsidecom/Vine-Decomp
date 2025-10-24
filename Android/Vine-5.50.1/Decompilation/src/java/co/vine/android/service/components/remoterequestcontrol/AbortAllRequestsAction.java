package co.vine.android.service.components.remoterequestcontrol;

import co.vine.android.network.NetworkOperation;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import java.util.Iterator;

/* loaded from: classes.dex */
public final class AbortAllRequestsAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        Iterator<NetworkOperation.CancelableRequest> iterator = request.service.getActiveRequests().iterator();
        while (iterator.hasNext()) {
            NetworkOperation.CancelableRequest r = iterator.next();
            if (r != null && !r.isCancelled()) {
                r.cancel();
            }
            iterator.remove();
        }
        return null;
    }
}
