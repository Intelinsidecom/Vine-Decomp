package co.vine.android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.client.AppController;
import co.vine.android.recorder.RegularProgressView;

/* loaded from: classes.dex */
public class UploadsAdapter extends CursorAdapter {
    protected final AppController mAppController;
    private final int mDividerColor;

    public UploadsAdapter(Context context, AppController appController, int flags, int dividerColorId) {
        super(context, (Cursor) null, flags);
        this.mAppController = appController;
        this.mDividerColor = context.getResources().getColor(dividerColorId);
    }

    public UploadsAdapter(Context context, AppController appController, int flags) {
        this(context, appController, flags, R.color.uploads_list_divider);
    }

    @Override // android.support.v4.widget.CursorAdapter
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.upload_row_view, parent, false);
        v.findViewById(R.id.divider).setBackgroundColor(this.mDividerColor);
        UploadViewHolder holder = new UploadViewHolder(v);
        v.setTag(holder);
        return v;
    }

    @Override // android.support.v4.widget.CursorAdapter
    public void bindView(View view, Context context, Cursor cursor) {
        UploadViewHolder holder = (UploadViewHolder) view.getTag();
        holder.status.setText(R.string.upload_failed);
        holder.retry.setVisibility(8);
        if (cursor.getPosition() == 0) {
            holder.retry.setVisibility(0);
        }
        holder.progressView.setProgressRatio(0.0f);
        String thumbnailPath = cursor.getString(6);
        holder.thumbnail.setImageBitmap(BitmapFactory.decodeFile(thumbnailPath));
    }

    private class UploadViewHolder {
        public RegularProgressView progressView;
        public ImageView retry;
        public TextView status;
        public ImageView thumbnail;

        public UploadViewHolder(View view) {
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.status = (TextView) view.findViewById(R.id.status_message);
            this.retry = (ImageView) view.findViewById(R.id.retry);
            this.progressView = (RegularProgressView) view.findViewById(R.id.progress_view);
        }
    }
}
