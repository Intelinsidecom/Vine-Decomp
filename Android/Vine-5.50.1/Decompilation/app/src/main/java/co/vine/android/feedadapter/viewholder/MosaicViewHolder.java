package co.vine.android.feedadapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;
import java.util.ArrayList;

/* loaded from: classes.dex */
public abstract class MosaicViewHolder implements ViewHolder {
    private static final int[] IDS = {R.id.thumbnail_zero, R.id.thumbnail_one, R.id.thumbnail_two, R.id.thumbnail_three, R.id.thumbnail_four};
    private final int mThumbnailCount;
    private final ViewType mType;
    public final ArrayList<ImageView> thumbnails;

    public MosaicViewHolder(View view, ViewType type, int thumbnailCount) {
        this.mType = type;
        this.mThumbnailCount = thumbnailCount;
        this.thumbnails = new ArrayList<>(this.mThumbnailCount);
        for (int i = 0; i < this.mThumbnailCount; i++) {
            this.thumbnails.add(i, (ImageView) view.findViewById(IDS[i]));
        }
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return this.mType;
    }

    public int getThumbnailCount() {
        return this.mThumbnailCount;
    }
}
