package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.vine.android.api.VineTypeAhead;
import co.vine.android.client.AppController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TagsAutoCompleteAdapter extends CursorAdapter {
    protected final ArrayList<WeakReference<TagDropDownViewHolder>> mViewHolders;

    public TagsAutoCompleteAdapter(Activity activity, AppController appController) {
        super((Context) activity, (Cursor) null, true);
        this.mViewHolders = new ArrayList<>();
    }

    @Override // android.support.v4.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View rowView = layoutInflater.inflate(R.layout.tag_row_view, (ViewGroup) null);
        TagDropDownViewHolder vh = new TagDropDownViewHolder(rowView);
        this.mViewHolders.add(new WeakReference<>(vh));
        rowView.setTag(vh);
        return rowView;
    }

    @Override // android.support.v4.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        TagDropDownViewHolder holder = (TagDropDownViewHolder) view.getTag();
        holder.userId = cursor.getLong(1);
        holder.tagName.setText("#" + cursor.getString(2));
    }

    @Override // android.support.v4.widget.CursorAdapter, android.widget.Adapter
    public Object getItem(int position) {
        long id;
        String tagName;
        Cursor c = (Cursor) super.getItem(position);
        if (c != null) {
            id = c.getLong(1);
            tagName = "#" + c.getString(2);
        } else {
            id = 0;
            tagName = "";
        }
        return new VineTypeAhead("tag", tagName, id);
    }

    @Override // android.support.v4.widget.CursorAdapter, android.widget.Adapter
    public long getItemId(int position) {
        Cursor c = (Cursor) super.getItem(position);
        if (c != null) {
            return c.getLong(1);
        }
        return 0L;
    }

    private class TagDropDownViewHolder {
        public final TextView tagName;
        public long userId;

        public TagDropDownViewHolder(View rowView) {
            this.tagName = (TextView) rowView.findViewById(R.id.tag_name);
        }
    }
}
