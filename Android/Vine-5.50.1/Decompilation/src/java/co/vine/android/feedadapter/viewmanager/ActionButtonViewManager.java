package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import co.vine.android.client.AppController;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.ActionButtonViewHolder;
import co.vine.android.util.ResourceLoader;

/* loaded from: classes.dex */
public class ActionButtonViewManager implements ViewManager {
    private final AppController mAppController;
    private final Context mContext;

    public ActionButtonViewManager(Context context, AppController appController) {
        this.mContext = context;
        this.mAppController = appController;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.ACTION_BUTTON;
    }

    public void bind(ActionButtonViewHolder h, String description, String iconUrl) {
        ImageView icon = h.icon;
        if (!TextUtils.isEmpty(iconUrl) && icon != null) {
            ResourceLoader bitmapLoader = new ResourceLoader(this.mContext, this.mAppController);
            bitmapLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(icon), iconUrl);
        } else {
            h.title.setPadding(h.title.getPaddingRight(), h.title.getPaddingTop(), h.title.getPaddingRight(), h.title.getPaddingBottom());
        }
        h.title.setText(description);
    }
}
