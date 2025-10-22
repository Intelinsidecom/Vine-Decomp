package co.vine.android.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import co.vine.android.R;
import co.vine.android.util.SparseArray;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public final class RowLimitedLinearLayout extends LinearLayout implements View.OnClickListener {
    private ListAdapter mAdapter;
    private DataSetObserver mDataObserver;
    private OnItemClickListener mItemClickListener;
    private int mMaxCount;
    private SparseArray<View> mRecycleBin;
    private Map<View, Integer> mViewToPositionMap;

    public interface OnItemClickListener {
        void onItemClick(ViewGroup viewGroup, View view, int i, long j);
    }

    public RowLimitedLinearLayout(Context context) {
        super(context);
        this.mMaxCount = 3;
        this.mRecycleBin = new SparseArray<>();
        this.mViewToPositionMap = new HashMap();
        this.mDataObserver = new DataSetObserver() { // from class: co.vine.android.widget.RowLimitedLinearLayout.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                RowLimitedLinearLayout.this.refreshViews();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                RowLimitedLinearLayout.this.refreshViews();
            }
        };
    }

    public RowLimitedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mMaxCount = 3;
        this.mRecycleBin = new SparseArray<>();
        this.mViewToPositionMap = new HashMap();
        this.mDataObserver = new DataSetObserver() { // from class: co.vine.android.widget.RowLimitedLinearLayout.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                RowLimitedLinearLayout.this.refreshViews();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                RowLimitedLinearLayout.this.refreshViews();
            }
        };
    }

    public RowLimitedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMaxCount = 3;
        this.mRecycleBin = new SparseArray<>();
        this.mViewToPositionMap = new HashMap();
        this.mDataObserver = new DataSetObserver() { // from class: co.vine.android.widget.RowLimitedLinearLayout.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                RowLimitedLinearLayout.this.refreshViews();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                RowLimitedLinearLayout.this.refreshViews();
            }
        };
    }

    public void setAdapter(ListAdapter adapter) {
        removeAdapterViews();
        this.mRecycleBin.clear();
        this.mViewToPositionMap.clear();
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(this.mDataObserver);
        }
        refreshViews();
    }

    public void setMaxDisplayCount(int count) {
        this.mMaxCount = count;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshViews() {
        removeAdapterViews();
        int numToShow = Math.min(this.mAdapter.getCount(), this.mMaxCount);
        for (int i = 0; i < numToShow; i++) {
            View v = this.mAdapter.getView(i, this.mRecycleBin.get(i), this);
            this.mRecycleBin.put(i, v);
            this.mViewToPositionMap.put(v, Integer.valueOf(i));
            v.setOnClickListener(this);
            addView(v);
        }
        ImageView divider = new ImageView(getContext());
        float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) ((2.0f * scale) + 0.5f);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, pixels);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(getResources().getColor(R.color.solid_super_light_gray));
        addView(divider);
    }

    private void removeAdapterViews() {
        Iterator<View> viewIterator = this.mRecycleBin.valueIterator();
        while (viewIterator.hasNext()) {
            View v = viewIterator.next();
            removeView(v);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        if (this.mItemClickListener != null) {
            int position = this.mViewToPositionMap.get(v).intValue();
            this.mItemClickListener.onItemClick(this, v, position, this.mAdapter.getItemId(position));
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sum = 0;
        for (int i = 0; i < getChildCount(); i++) {
            sum += getChildAt(i).getMeasuredHeight();
        }
        if (sum != getMeasuredHeight() && getAnimation() == null) {
            setMeasuredDimension(getMeasuredWidth(), sum);
        }
    }
}
