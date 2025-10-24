package co.vine.android.widget;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import co.vine.android.util.CrashUtil;

/* loaded from: classes.dex */
public class SectionAdapter extends BaseAdapter {
    private final BaseAdapter[] mAdapters;
    boolean mNotifyOnChange = true;
    private final AdapterDataSetObserver mDataSetObserver = new AdapterDataSetObserver();

    public SectionAdapter(BaseAdapter... adapters) {
        this.mAdapters = adapters;
        for (BaseAdapter adapter : adapters) {
            adapter.registerDataSetObserver(this.mDataSetObserver);
        }
    }

    private int getAdapterCount(BaseAdapter adapter) {
        try {
            return adapter.getCount();
        } catch (Exception e) {
            CrashUtil.logException(e);
            return 0;
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        int total = 0;
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                total += count;
            }
        }
        return total;
    }

    public int getNumberOfAdapters() {
        return this.mAdapters.length;
    }

    @Override // android.widget.Adapter
    public Object getItem(int pos) {
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                if (pos < count) {
                    return adapter.getItem(pos);
                }
                pos -= count;
            }
        }
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int pos) {
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                if (pos < count) {
                    return adapter.getItemId(pos);
                }
                pos -= count;
            }
        }
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int pos, View view, ViewGroup parent) {
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                if (pos < count) {
                    return adapter.getView(pos, view, parent);
                }
                pos -= count;
            }
        }
        return view;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int pos) {
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                if (pos < count) {
                    return adapter.isEnabled(pos);
                }
                pos -= count;
            }
        }
        return super.isEnabled(pos);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int pos) {
        int typeCount = 1;
        for (BaseAdapter adapter : this.mAdapters) {
            int count = getAdapterCount(adapter);
            if (count > 0) {
                if (pos < count) {
                    return adapter.getItemViewType(pos) + typeCount;
                }
                pos -= count;
            }
            typeCount += adapter.getViewTypeCount();
        }
        return super.getItemViewType(pos);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        int count = 0;
        for (BaseAdapter adapter : this.mAdapters) {
            count += adapter.getViewTypeCount();
        }
        return count + 1;
    }

    public BaseAdapter getAdapter(int index) {
        return this.mAdapters[index];
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.mNotifyOnChange = true;
    }

    class AdapterDataSetObserver extends DataSetObserver {
        AdapterDataSetObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            SectionAdapter.this.notifyDataSetInvalidated();
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (SectionAdapter.this.mNotifyOnChange) {
                SectionAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
