package co.vine.android.feedadapter;

import android.view.View;
import android.view.ViewGroup;
import co.vine.android.feedadapter.ViewGroupHelper.ViewChildViewHolder;
import co.vine.android.feedadapter.viewholder.CardViewHolder;

/* loaded from: classes.dex */
public class ViewGroupHelper<T extends ViewChildViewHolder> {
    private final ViewGroup mParent;

    public interface ViewChildViewHolder {
        View getMeasuringView();

        int getPosition();
    }

    public ViewGroupHelper(ViewGroup parent) {
        this.mParent = parent;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T getLargestVisibleViewAdapterPosition() {
        int[] listViewBounds = new int[2];
        this.mParent.getLocationInWindow(listViewBounds);
        int[] videoViewBounds = new int[2];
        int count = this.mParent.getChildCount();
        float maxRatio = -1.0f;
        T maxRatioTag = null;
        for (int i = 0; i < count; i++) {
            ViewChildViewHolder validViewHolder = getValidViewHolder(i);
            if (validViewHolder != 0) {
                validViewHolder.getMeasuringView().getLocationInWindow(videoViewBounds);
                int top = Math.max(listViewBounds[1], videoViewBounds[1]);
                int bottom = Math.min(listViewBounds[1] + this.mParent.getMeasuredHeight(), videoViewBounds[1] + validViewHolder.getMeasuringView().getHeight());
                int size = bottom - top;
                if (size > maxRatio && size > 0) {
                    maxRatio = size;
                    maxRatioTag = validViewHolder;
                }
            }
        }
        return maxRatioTag;
    }

    public T getViewHolderFor(int i) {
        int childCount = this.mParent.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            T t = (T) getValidViewHolder(i2);
            if (t != null && t.getPosition() == i) {
                return t;
            }
        }
        return null;
    }

    public T getValidViewHolder(int index) {
        Object tag;
        View c = this.mParent.getChildAt(index);
        if (c == null || (tag = c.getTag()) == null || !(tag instanceof CardViewHolder)) {
            return null;
        }
        return (T) tag;
    }
}
