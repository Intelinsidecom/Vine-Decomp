package co.vine.android.service;

import android.text.TextUtils;
import co.vine.android.client.VineAPI;
import co.vine.android.model.PagingInfoModel;
import co.vine.android.service.VineServiceAction;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public abstract class VineServicePagedAction extends VineServiceAction {
    private AtomicBoolean mHasPendingRequest = new AtomicBoolean(false);

    protected abstract VineServiceActionResult doPagedAction(VineServiceAction.Request request);

    protected abstract String getUniqueMarker(VineServiceAction.Request request);

    protected VineServicePagedAction() {
    }

    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) {
        if (this.mHasPendingRequest.getAndSet(true)) {
            return null;
        }
        this.mHasPendingRequest.set(false);
        return doPagedAction(request);
    }

    protected void updatePagingInfoFromResult(int nextPage, String anchor, String marker) {
        PagingInfoModel.getInstance().updatePagingInfoFromResult(marker, nextPage, anchor);
    }

    protected void addPagingInfoToRequest(StringBuilder url, String marker) {
        PagingInfoModel.PagingInformation pagingInformation = PagingInfoModel.getInstance().getPagingInfoFromModel(marker);
        if (pagingInformation != null) {
            if (pagingInformation.mNextPage > 0) {
                VineAPI.addParam(url, "page", pagingInformation.mNextPage);
            }
            if (!TextUtils.isEmpty(pagingInformation.mAnchor)) {
                VineAPI.addParam(url, "anchor", pagingInformation.mAnchor);
            }
        }
    }
}
