package co.vine.android;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.drawable.RecyclableBitmapDrawable;
import co.vine.android.util.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public class ImportGalleryFragment extends BaseGridViewFragment {
    public static final Uri URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private ContentResolver mContentResolver;
    private List<Video> mVideoList;

    @Override // co.vine.android.BaseGridViewFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.mVideoList = new ArrayList();
        }
        new ImportVideoTask().execute(new Void[0]);
    }

    @Override // co.vine.android.BaseGridViewFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        this.mAdapter = new ImportGalleryVideosAdapter(this.mVideoList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        return v;
    }

    private class ImportVideoTask extends AsyncTask<Void, Void, Void> {
        int count;
        Cursor cursor;

        private ImportVideoTask() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            ImportGalleryFragment.this.mContentResolver = ImportGalleryFragment.this.getActivity().getContentResolver();
            this.cursor = ImportGalleryFragment.this.mContentResolver.query(ImportGalleryFragment.URI, null, null, null, "date_added ASC");
            this.count = 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            if (ImportGalleryFragment.this.mContentResolver != null && this.cursor.getCount() != 0 && this.cursor != null) {
                while (this.cursor.moveToNext()) {
                    this.count++;
                    long millis = this.cursor.getLong(this.cursor.getColumnIndexOrThrow("duration"));
                    String dur = DateTimeUtil.getMinSecFormat(millis);
                    String videoPath = this.cursor.getString(this.cursor.getColumnIndexOrThrow("_data"));
                    long id = this.cursor.getLong(this.cursor.getColumnIndexOrThrow("_id"));
                    BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
                    bitmapOption.outHeight = HttpResponseCode.INTERNAL_SERVER_ERROR;
                    bitmapOption.outWidth = HttpResponseCode.INTERNAL_SERVER_ERROR;
                    Bitmap thumbnail = MediaStore.Video.Thumbnails.getThumbnail(ImportGalleryFragment.this.mContentResolver, id, 1, bitmapOption);
                    if (thumbnail == null) {
                        thumbnail = BitmapFactory.decodeResource(ImportGalleryFragment.this.getResources(), R.drawable.background_sadface);
                    }
                    RecyclableBitmapDrawable recyclableBitmapDrawable = new RecyclableBitmapDrawable(ImportGalleryFragment.this.getActivity().getResources(), ThumbnailUtils.extractThumbnail(thumbnail, HttpResponseCode.INTERNAL_SERVER_ERROR, HttpResponseCode.INTERNAL_SERVER_ERROR));
                    Video video = new Video(id, dur, videoPath, recyclableBitmapDrawable);
                    ImportGalleryFragment.this.mVideoList.add(video);
                    if (this.count % 12 == 0) {
                        publishProgress(new Void[0]);
                    }
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate((Object[]) values);
            ImportGalleryFragment.this.mAdapter.notifyDataSetChanged();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void aVoid) {
            super.onPostExecute((ImportVideoTask) aVoid);
            ImportGalleryFragment.this.mAdapter.notifyDataSetChanged();
        }
    }

    private class ImportGalleryVideosAdapter extends ThumbnailGridViewAdapter {
        private List<Video> mList;

        public ImportGalleryVideosAdapter(List<Video> mList) {
            this.mList = mList;
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final Video video = this.mList.get(position);
            if (holder.getItemViewType() == 1) {
                ThumbnailViewHolder viewHolder = (ThumbnailViewHolder) holder;
                viewHolder.getImageView().setImageDrawable(video.thumbnail);
                viewHolder.setDuration(video.duration);
                viewHolder.setSelectedOrder(ImportGalleryFragment.this.getVideoOrder(video.videoPath));
                holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ImportGalleryFragment.ImportGalleryVideosAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        ImportGalleryFragment.this.updateSelection(video.videoPath, video.videoPath, null);
                        ImportGalleryFragment.this.mAdapter.notifyDataSetChanged();
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: co.vine.android.ImportGalleryFragment.ImportGalleryVideosAdapter.2
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        ImportGalleryFragment.this.toPreviewFragment(video.videoPath, null);
                        return true;
                    }
                });
            }
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mList.size();
        }

        @Override // android.support.v7.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return this.mList.get(position) != null ? 1 : -1;
        }
    }
}
