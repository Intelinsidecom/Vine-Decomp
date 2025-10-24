package co.vine.android.recordingui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.util.SystemUtil;
import co.vine.android.widget.trimcontrols.ThumbnailRangeFinderLayout;

/* loaded from: classes.dex */
public class MultiImportTrimmerHolder {
    private Context mContext;
    private Point mPoint;
    public ViewGroup root;
    public ThumbnailRangeFinderLayout thumbnailRangeFinder;

    public MultiImportTrimmerHolder(ViewGroup root, Context context) {
        this.mContext = context;
        this.mPoint = SystemUtil.getDisplaySize(this.mContext);
        this.thumbnailRangeFinder = new ThumbnailRangeFinderLayout(this.mContext, this.mPoint.x * 0, true);
        this.root = root;
    }

    public void addToRoot() throws Resources.NotFoundException {
        int height = this.mContext.getResources().getDimensionPixelSize(R.dimen.carousel_height);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, height);
        this.root.addView(this.thumbnailRangeFinder, params);
    }
}
