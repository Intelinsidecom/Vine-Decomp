package co.vine.android.search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class RecentSearchesAdapter extends BaseAdapter {
    private Context mContext;
    private View.OnClickListener mOnClearClickListener;
    private View.OnClickListener mOnRowClickListener;
    private ArrayList<String> mRecents = new ArrayList<>();

    public RecentSearchesAdapter(Context context, View.OnClickListener listener, final RecentSearchesManager recentSearchesManager) {
        this.mContext = context;
        this.mOnRowClickListener = listener;
        this.mOnClearClickListener = new View.OnClickListener() { // from class: co.vine.android.search.RecentSearchesAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RecentSearchViewHolder holder = (RecentSearchViewHolder) view.getTag();
                recentSearchesManager.removeRecentSearch(holder.queryString);
                RecentSearchesAdapter.this.removeItem(holder.position);
                RecentSearchesAdapter.this.notifyDataSetChanged();
            }
        };
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mRecents != null) {
            return this.mRecents.size();
        }
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 1;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        RecentSearchViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.recent_search_row, parent, false);
            holder = new RecentSearchViewHolder(view, this.mOnRowClickListener, this.mOnClearClickListener);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (RecentSearchViewHolder) view.getTag();
        }
        holder.position = position;
        if (position == 0) {
            holder.header.setVisibility(0);
            holder.header.setText(this.mContext.getString(R.string.recent));
        } else {
            holder.header.setVisibility(8);
        }
        holder.queryString = this.mRecents.get(position);
        holder.query.setText(holder.queryString);
        return view;
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        if (position < this.mRecents.size()) {
            return this.mRecents.get(position);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeItem(int position) {
        if (position < this.mRecents.size()) {
            this.mRecents.remove(position);
        }
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public void replaceData(LinkedList<String> newRecents) {
        this.mRecents = new ArrayList<>();
        Iterator<String> it = newRecents.iterator();
        while (it.hasNext()) {
            String recent = it.next();
            if (!TextUtils.isEmpty(recent)) {
                this.mRecents.add(recent);
            }
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mRecents = new ArrayList<>();
        notifyDataSetChanged();
    }

    static class RecentSearchViewHolder implements QueryableRowHolder {
        public ImageView clear;
        public TextView header;
        public int position;
        public TextView query;
        String queryString;
        public ViewGroup root;

        public RecentSearchViewHolder(View view, View.OnClickListener onClickListener, View.OnClickListener onClearClickListener) {
            this.root = (ViewGroup) view.findViewById(R.id.root_layout);
            this.root.setOnClickListener(onClickListener);
            this.header = (TextView) view.findViewById(R.id.header_label);
            this.query = (TextView) view.findViewById(R.id.query);
            ((ImageView) view.findViewById(R.id.icon)).setColorFilter(this.root.getResources().getColor(R.color.black_thirty_five_percent), PorterDuff.Mode.SRC_IN);
            this.clear = (ImageView) view.findViewById(R.id.clear);
            this.clear.setColorFilter(this.root.getResources().getColor(R.color.black_thirty_five_percent), PorterDuff.Mode.SRC_IN);
            this.clear.setOnClickListener(onClearClickListener);
            this.clear.setTag(this);
        }

        @Override // co.vine.android.search.QueryableRowHolder
        public String getSearchQueryString() {
            return this.queryString;
        }
    }
}
