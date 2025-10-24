package co.vine.android.search;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.TabbedFeedActivityFactory;
import co.vine.android.api.SearchResult;
import co.vine.android.client.AppController;
import co.vine.android.model.VineTag;
import co.vine.android.util.Util;
import co.vine.android.widget.TagViewHolder;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class TagSearchAdapter extends BaseAdapter {
    public int displayCount;
    protected final AppController mAppController;
    protected Context mContext;
    protected ArrayList<VineTag> mTags;
    private final View.OnClickListener mViewAllClickListener;

    public TagSearchAdapter(Context context, AppController appController, View.OnClickListener viewAllClickListener) {
        this.mContext = context;
        this.mAppController = appController;
        this.mViewAllClickListener = viewAllClickListener;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        int dataCount = this.mTags != null ? 0 + this.mTags.size() : 0;
        return Math.min(dataCount, this.displayCount);
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        if (this.mTags == null || this.mTags.size() <= 0) {
            return null;
        }
        return this.mTags.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
        View view;
        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }
        bindView(position, view);
        return view;
    }

    public View newView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.tag_row_view, parent, false);
        TagSearchViewHolder holder = new TagSearchViewHolder(view, this.mViewAllClickListener);
        view.setTag(holder);
        return view;
    }

    public void bindView(int position, View view) throws Resources.NotFoundException {
        TagSearchViewHolder holder = (TagSearchViewHolder) view.getTag();
        VineTag tag = this.mTags.get(position);
        if (!TextUtils.isEmpty(tag.getTagName())) {
            holder.tagTitle.setText("#" + tag.getTagName());
            holder.tagName = tag.getTagName();
        }
        holder.tagName = tag.getTagName();
        if (position == 0) {
            holder.header.setVisibility(0);
            holder.header.setText(this.mContext.getString(R.string.tags));
        } else {
            holder.header.setVisibility(8);
        }
        if (position == 0 && this.mTags.size() > this.displayCount) {
            holder.viewAll.setVisibility(0);
        } else {
            holder.viewAll.setVisibility(8);
        }
        String postCountFormatted = Util.numberFormat(view.getResources(), tag.getPostCount());
        String postCount = view.getResources().getQuantityString(R.plurals.tag_post_count, (int) tag.getPostCount(), postCountFormatted);
        holder.postCount.setText(postCount);
    }

    private static class TagSearchViewHolder extends TagViewHolder implements QueryableRowHolder {
        TextView header;
        TextView postCount;
        ViewGroup root;
        String tagName;
        TextView viewAll;

        public TagSearchViewHolder(View view, View.OnClickListener viewAllClickListener) {
            super(view);
            this.root = (ViewGroup) view.findViewById(R.id.root_layout);
            this.header = (TextView) view.findViewById(R.id.header_label);
            this.viewAll = (TextView) view.findViewById(R.id.view_all);
            this.postCount = (TextView) view.findViewById(R.id.post_count);
            this.root.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.search.TagSearchAdapter.TagSearchViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    TabbedFeedActivityFactory.startTabbedTagsActivity(v.getContext(), TagSearchViewHolder.this.tagName);
                }
            });
            this.header.setOnClickListener(viewAllClickListener);
        }

        @Override // co.vine.android.search.QueryableRowHolder
        public String getSearchQueryString() {
            return this.tagName;
        }
    }

    public void replaceData(SearchResult data) {
        this.mTags = new ArrayList<>();
        if (data.getTags() != null) {
            this.mTags.addAll(data.getTags().getItems());
            this.displayCount = data.getTags().getDisplayCount();
        }
        notifyDataSetChanged();
    }
}
