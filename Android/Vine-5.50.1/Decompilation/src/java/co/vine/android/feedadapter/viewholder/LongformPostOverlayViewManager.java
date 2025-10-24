package co.vine.android.feedadapter.viewholder;

import android.content.Context;
import android.content.res.Resources;
import co.vine.android.R;
import co.vine.android.api.VinePost;
import co.vine.android.feedadapter.TimelineClickListenerFactory;
import co.vine.android.storage.RealmManager;
import co.vine.android.storage.model.LongformData;
import co.vine.android.storage.operation.QueryOperation;
import co.vine.android.util.ClientFlagsHelper;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import io.realm.Realm;

/* loaded from: classes.dex */
public class LongformPostOverlayViewManager {
    public static void setupOverlay(final Context context, final LongformPostOverlayViewHolder h, final VinePost data, int position, TimelineClickListenerFactory.Callback callback) {
        if (h.overlay != null) {
            if (!ClientFlagsHelper.isLongformEnabled(context) || data.longform == null) {
                h.overlay.setVisibility(8);
                return;
            }
            h.overlay.setOnClickListener(TimelineClickListenerFactory.newLongformOverlayClickListener(callback, data, position));
            RealmManager.executeOperation(new QueryOperation<LongformData>() { // from class: co.vine.android.feedadapter.viewholder.LongformPostOverlayViewManager.1
                @Override // co.vine.android.storage.operation.RealmOperation
                public LongformData execute(Realm realm) {
                    LongformData longform = (LongformData) realm.where(LongformData.class).equalTo("longformId", String.valueOf(data.longform.longformId)).findFirst();
                    Resources resources = context.getResources();
                    h.overlayTimestamp.setText(Util.stringForTime((int) (1000.0f * data.longform.duration)));
                    if (longform == null || !longform.isWatched()) {
                        h.overlayText.setText(resources.getString(R.string.watch_more));
                        h.overlayIcon.setImageResource(R.drawable.ic_watch_more);
                        return null;
                    }
                    if (longform.isReachedEnd()) {
                        h.overlayText.setText(resources.getText(R.string.watch_again));
                        h.overlayIcon.setImageResource(R.drawable.ic_watch_again);
                        return null;
                    }
                    if (longform.isWatched()) {
                        h.overlayText.setText(resources.getText(R.string.continue_content));
                        h.overlayIcon.setImageResource(R.drawable.ic_watch_more);
                        return null;
                    }
                    return null;
                }
            });
            SLog.d("ryango text {}", h.overlayText.getText());
            h.overlay.setVisibility(0);
            h.overlay.invalidate();
        }
    }
}
