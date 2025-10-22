package co.vine.android.feedadapter.viewmanager;

import android.content.Context;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.feedadapter.viewholder.LoopCounterViewHolder;
import co.vine.android.util.LoopManager;
import co.vine.android.widget.Typefaces;

/* loaded from: classes.dex */
public class LoopCounterViewManager implements ViewManager {
    private final Context mContext;

    public LoopCounterViewManager(Context context) {
        this.mContext = context;
    }

    @Override // co.vine.android.feedadapter.viewmanager.ViewManager
    public ViewType getType() {
        return ViewType.USERNAME;
    }

    public void bind(LoopCounterViewHolder h, long postId, long loops, double velocity, long lastRefresh, boolean onFire, long created) {
        if (h.counter != null) {
            h.counter.setMinAnimationSeparation(500L);
            h.counter.setMaxAnimationSeparation(3000L);
            h.counter.setTypeFace(Typefaces.get(this.mContext).getContentTypeface(0, 2));
            h.counter.setTextSize(this.mContext.getResources().getDimensionPixelSize(R.dimen.font_size_middle));
            h.counter.setColor(this.mContext.getResources().getColor(R.color.solid_black));
            update(h, postId, loops, velocity, lastRefresh, onFire, created);
        }
    }

    public void update(LoopCounterViewHolder h, long postId, long loops, double velocity, long lastRefresh, boolean onFire, long created) {
        h.counter.reset();
        h.counter.adjustExtraCount(LoopManager.getLocalLoopCount(postId));
        h.counter.setKnownCount(loops, velocity / 1000.0d, lastRefresh);
        h.onFire.setVisibility(onFire ? 0 : 8);
        h.plus.setVisibility(created < 1398196181000L ? 0 : 8);
    }

    protected void adjustLoopCount(LoopCounterViewHolder h, int newCount) {
        if (h.counter != null) {
            h.counter.adjustExtraCount(newCount);
            h.counter.invalidateCounterUI();
        }
    }
}
