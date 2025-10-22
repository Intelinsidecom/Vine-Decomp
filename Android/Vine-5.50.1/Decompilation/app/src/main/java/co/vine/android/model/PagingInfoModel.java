package co.vine.android.model;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class PagingInfoModel {
    private static PagingInfoModel sPagingInfoModel;
    private final Map<String, PagingInformation> mPagingInfoMap = new HashMap();

    public static PagingInfoModel getInstance() {
        if (sPagingInfoModel == null) {
            sPagingInfoModel = new PagingInfoModel();
        }
        return sPagingInfoModel;
    }

    public static final class PagingInformation {
        public final String mAnchor;
        public final int mNextPage;

        PagingInformation(int nextPage, String anchor) {
            this.mNextPage = nextPage;
            this.mAnchor = anchor;
        }
    }

    private PagingInfoModel() {
    }

    public boolean hasMore(String marker) {
        PagingInformation pagingInfo = this.mPagingInfoMap.get(marker);
        return pagingInfo == null || pagingInfo.mNextPage > 0;
    }

    public void updatePagingInfoFromResult(String marker, int nextPage, String anchor) {
        this.mPagingInfoMap.put(marker, new PagingInformation(nextPage, anchor));
    }

    public PagingInformation getPagingInfoFromModel(String marker) {
        return this.mPagingInfoMap.get(marker);
    }
}
