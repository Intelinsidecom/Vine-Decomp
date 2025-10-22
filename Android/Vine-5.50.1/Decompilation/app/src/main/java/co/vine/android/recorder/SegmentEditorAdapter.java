package co.vine.android.recorder;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import co.vine.android.dragsort.DragSortWidget;
import co.vine.android.util.ViewUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SegmentEditorAdapter extends BaseAdapter implements ListAdapter, DragSortWidget.DragListener, DragSortWidget.RemoveListener, DragSortWidget.ShouldAnimateInProvider {
    public boolean hasInitialized;
    private Context mContext;
    private final ArrayList<RecordSegment> mData;
    private final ArrayList<RecordSegment> mDeleted;
    private int mLastDeletedIndex;
    private final int mMargin;
    private final HashMap<RecordSegment, RecordSegment> mOriginals;
    private final int mSize;
    private final DragSortWidget mWidget;

    public SegmentEditorAdapter(ArrayList<RecordSegment> data, Context context, DragSortWidget widget, float density) {
        this.mLastDeletedIndex = -1;
        this.mWidget = widget;
        this.mDeleted = new ArrayList<>();
        this.mData = new ArrayList<>();
        this.mOriginals = new HashMap<>();
        Iterator<RecordSegment> it = data.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                RecordSegment copy = new RecordSegment(segment);
                this.mData.add(copy);
                this.mOriginals.put(copy, segment);
            }
        }
        this.hasInitialized = true;
        this.mContext = context;
        this.mMargin = (int) (10.0f * density);
        this.mSize = (int) (72.0f * density);
    }

    public SegmentEditorAdapter(SegmentEditorAdapter editorAdapter) {
        this.mLastDeletedIndex = -1;
        this.mWidget = editorAdapter.mWidget;
        this.mDeleted = editorAdapter.mDeleted;
        this.mData = editorAdapter.mData;
        this.mContext = editorAdapter.mContext;
        this.mMargin = editorAdapter.mMargin;
        this.mSize = editorAdapter.mSize;
        this.mLastDeletedIndex = editorAdapter.mLastDeletedIndex;
        this.mOriginals = editorAdapter.mOriginals;
    }

    public ArrayList<RecordSegment> getData() {
        return this.mData;
    }

    public ArrayList<RecordSegment> getDeleted() {
        return this.mDeleted;
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
        return this.mData.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.mData.get(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
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
        RecordSegment item = this.mData.get(position);
        if (item.drawable == null && !TextUtils.isEmpty(item.getThumbnailPath())) {
            item.drawable = BitmapDrawable.createFromPath(item.getThumbnailPath());
        }
        ViewUtil.setBackground(convertView, item.drawable);
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
        return this.mData.size() == 0;
    }

    @Override // co.vine.android.dragsort.DragSortWidget.RemoveListener
    public void remove(int which) {
        this.mLastDeletedIndex = which;
        this.mDeleted.add(this.mData.remove(which));
        notifyDataSetChanged();
    }

    public HashSet<RecordSegment> cleanUpForDeletion() {
        Iterator<RecordSegment> it = this.mData.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            RecordSegment original = this.mOriginals.get(segment);
            if (original != null && segment.isDataFileBacked()) {
                original.setFullDataId(segment.getFullDataId());
            }
        }
        HashSet<RecordSegment> filesToDelete = new HashSet<>();
        Iterator<RecordSegment> it2 = this.mDeleted.iterator();
        while (it2.hasNext()) {
            RecordSegment segment2 = it2.next();
            if (this.mOriginals.get(segment2) == null && segment2.isDataFileBacked()) {
                filesToDelete.add(segment2);
            }
        }
        return filesToDelete;
    }

    @Override // co.vine.android.dragsort.DragSortWidget.DragListener
    public void drag(int from, int to) {
        if (from != to) {
            RecordSegment temp = this.mData.get(from);
            this.mData.set(from, this.mData.get(to));
            this.mData.set(to, temp);
            notifyDataSetChanged();
        }
    }

    public void reverse() {
        ArrayList<RecordSegment> newData = new ArrayList<>(this.mData.size());
        for (int i = this.mData.size() - 1; i >= 0; i--) {
            newData.add(this.mData.get(i));
        }
        this.mData.clear();
        this.mData.addAll(newData);
        notifyDataSetChanged();
    }

    public void commitDelete() {
        this.mLastDeletedIndex = -1;
    }

    public boolean compareTo(ArrayList<RecordSegment> editedSegments) {
        if (editedSegments == null) {
            return this.mData == null;
        }
        if (this.mDeleted.size() > 0 || editedSegments.size() != this.mData.size()) {
            return false;
        }
        int size = this.mData.size();
        for (int i = 0; i < size; i++) {
            if (this.mData.get(i) != editedSegments.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void addSegment(RecordSegment parent, RecordSegment segment, int pos, boolean duplicateAnimation) {
        if (parent != null) {
            this.mOriginals.put(segment, parent);
        }
        this.mData.add(pos, segment);
        notifyDataSetChanged();
        segment.shouldAnimateIn = duplicateAnimation && this.mWidget.isVisible(pos);
    }

    @Override // co.vine.android.dragsort.DragSortWidget.ShouldAnimateInProvider
    public boolean shouldAnimateIn(int position) {
        RecordSegment segment = this.mData.get(position);
        boolean shouldAnimate = segment.shouldAnimateIn;
        segment.shouldAnimateIn = false;
        return shouldAnimate;
    }

    public long getDuration() {
        int duration = 0;
        Iterator<RecordSegment> it = getData().iterator();
        while (it.hasNext()) {
            RecordSegment s = it.next();
            duration += s.getDurationMs();
        }
        return duration;
    }
}
