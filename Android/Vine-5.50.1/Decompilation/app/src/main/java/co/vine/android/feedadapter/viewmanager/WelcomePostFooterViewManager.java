package co.vine.android.feedadapter.viewmanager;

import android.app.Activity;
import android.text.TextUtils;
import co.vine.android.api.VinePost;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.WelcomePostFooterViewHolder;
import co.vine.android.feedadapter.viewmanager.PostDescriptionViewManager;
import co.vine.android.util.Util;

/* loaded from: classes.dex */
public class WelcomePostFooterViewManager implements ViewManager {
    private final Activity mContext;
    private final PostDescriptionViewManager mDescriptionManager = new PostDescriptionViewManager();

    public WelcomePostFooterViewManager(Activity context) {
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.POST_FOOTER;
    }

    public void bind(WelcomePostFooterViewHolder h, VinePost data) {
        if (data != null) {
            if (TextUtils.isEmpty(data.description)) {
                h.getDescriptionHolder().text.setVisibility(8);
                return;
            }
            String description = Util.addDirectionalMarkers(data.description);
            if (data.isRTL == null) {
                data.isRTL = Boolean.valueOf(Util.isRtlLanguage(description));
            }
            this.mDescriptionManager.bind(h.getDescriptionHolder(), new PostDescriptionViewManager.Description(this.mContext, description), false, data.isRTL, 0, 2, null);
        }
    }
}
