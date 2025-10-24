package co.vine.android.search;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.api.SearchResult;
import co.vine.android.api.VineSearchSuggestion;
import co.vine.android.util.Util;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class SearchSuggestionAdapter extends BaseAdapter {
    private Context mContext;
    private View.OnClickListener mOnRowClickListener;
    private String mQuery;
    private boolean mShowNoResultsMessage;
    private ArrayList<VineSearchSuggestion> mSuggestions = new ArrayList<>();
    private boolean showSectionHeader;

    public SearchSuggestionAdapter(Context context, View.OnClickListener listener) {
        this.mContext = context;
        this.mOnRowClickListener = listener;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mSuggestions != null) {
            return this.mSuggestions.size();
        }
        return 0;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 2;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return (position == 0 && this.mShowNoResultsMessage) ? 0 : 1;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(position, view);
        return view;
    }

    private View newView(int position, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            return LayoutInflater.from(this.mContext).inflate(R.layout.search_no_results_message, parent, false);
        }
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.search_suggestion_row, parent, false);
        SearchSuggestionViewHolder holder = new SearchSuggestionViewHolder(view, this.mOnRowClickListener);
        view.setTag(holder);
        return view;
    }

    private void bindView(int position, View view) {
        if (getItemViewType(position) == 0) {
            TextView noResultsMessage = (TextView) view.findViewById(R.id.no_results_message);
            String messageStr = this.mContext.getString(R.string.empty_unified_search_message, this.mQuery);
            StyleSpan[] boldStyle = {new StyleSpan(1)};
            Spanned spanned = Util.getSpannedText(boldStyle, messageStr, '\"');
            noResultsMessage.setText(spanned);
            return;
        }
        SearchSuggestionViewHolder holder = (SearchSuggestionViewHolder) view.getTag();
        holder.position = position;
        if ((position == 0 && this.showSectionHeader) || (position == 1 && this.mShowNoResultsMessage)) {
            holder.header.setVisibility(0);
            holder.header.setText(this.mContext.getString(R.string.search_suggested));
        } else {
            holder.header.setVisibility(8);
        }
        holder.queryString = this.mSuggestions.get(position).getQuery();
        holder.query.setText(holder.queryString);
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        if (position < this.mSuggestions.size()) {
            return this.mSuggestions.get(position);
        }
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return 0L;
    }

    public void replaceData(SearchResult data, boolean showNoResultsMessage) {
        this.mShowNoResultsMessage = showNoResultsMessage;
        this.mSuggestions = new ArrayList<>();
        if (data.getSuggestions() != null) {
            this.mSuggestions.addAll(data.getSuggestions().getItems());
        }
        notifyDataSetChanged();
    }

    static class SearchSuggestionViewHolder implements QueryableRowHolder {
        public TextView header;
        public int position;
        public TextView query;
        String queryString;
        public ViewGroup root;

        public SearchSuggestionViewHolder(View view, View.OnClickListener onClickListener) {
            this.root = (ViewGroup) view.findViewById(R.id.root_layout);
            this.root.setOnClickListener(onClickListener);
            this.header = (TextView) view.findViewById(R.id.header_label);
            this.query = (TextView) view.findViewById(R.id.query);
            ((ImageView) view.findViewById(R.id.icon)).setColorFilter(this.root.getResources().getColor(R.color.black_thirty_five_percent), PorterDuff.Mode.SRC_IN);
        }

        @Override // co.vine.android.search.QueryableRowHolder
        public String getSearchQueryString() {
            return this.queryString;
        }
    }

    public void setNoResultsMessage(String query) {
        this.mQuery = query;
    }

    public void showSectionHeader(boolean show) {
        this.showSectionHeader = show;
    }
}
