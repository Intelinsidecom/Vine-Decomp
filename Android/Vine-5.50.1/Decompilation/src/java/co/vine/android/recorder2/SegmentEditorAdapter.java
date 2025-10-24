package co.vine.android.recorder2;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import co.vine.android.dragsort.DragSortWidget;
import co.vine.android.recorder2.model.Draft;
import co.vine.android.recorder2.model.Segment;
import co.vine.android.util.ViewUtil;
import java.io.IOException;

/* loaded from: classes.dex */
public class SegmentEditorAdapter extends BaseAdapter implements ListAdapter, DragSortWidget.DragListener, DragSortWidget.ShouldAnimateInProvider {
    public boolean hasInitialized = true;
    private Context mContext;
    private final Draft mDraft;
    private final InvalidateGhostListener mGhostListener;
    private final int mMargin;
    private final int mSize;

    public SegmentEditorAdapter(Draft draft, Context context, InvalidateGhostListener ghostListener, float density) {
        this.mDraft = draft;
        this.mContext = context;
        this.mMargin = (int) (10.0f * density);
        this.mSize = (int) (72.0f * density);
        this.mGhostListener = ghostListener;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int position) {
        return true;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public void registerDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.registerDataSetObserver(observer);
        }
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mDraft.getSegmentCount();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mDraft.getSegment(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return position;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws IOException {
        ViewGroup.MarginLayoutParams params;
        if (convertView == null) {
            if (this.mContext == null) {
                this.mContext = parent.getContext();
            }
            convertView = new View(this.mContext);
            params = new ViewGroup.MarginLayoutParams(this.mSize, this.mSize);
        } else {
            params = (ViewGroup.MarginLayoutParams) convertView.getLayoutParams();
        }
        params.bottomMargin = position == getCount() + (-1) ? 0 : this.mMargin;
        convertView.setLayoutParams(params);
        Segment item = this.mDraft.getSegment(position);
        if (item.getThumbnailPath() == null) {
            item.generateThumbnail();
        }
        if (item.getDrawable() == null && !TextUtils.isEmpty(item.getThumbnailPath())) {
            item.setThumbnailDrawable(BitmapDrawable.createFromPath(item.getThumbnailPath()));
        }
        ViewUtil.setBackground(convertView, item.getDrawable());
        return convertView;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean isEmpty() {
        return this.mDraft.getSegmentCount() == 0;
    }

    @Override // co.vine.android.dragsort.DragSortWidget.DragListener
    public void drag(int from, int to) {
        if (from != to) {
            this.mDraft.swap(from, to);
            if (from == this.mDraft.getSegmentCount() - 1 || to == this.mDraft.getSegmentCount() - 1) {
                this.mGhostListener.invalidateGhost();
            }
            notifyDataSetChanged();
        }
    }

    @Override // co.vine.android.dragsort.DragSortWidget.ShouldAnimateInProvider
    public boolean shouldAnimateIn(int position) {
        Segment segment = this.mDraft.getSegment(position);
        boolean shouldAnimate = segment.shouldAnimateIn();
        segment.setShouldAnimateIn(false);
        return shouldAnimate;
    }
}
