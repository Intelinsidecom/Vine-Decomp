package co.vine.android.recorder2;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import co.vine.android.recorder2.model.Draft;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class CombineSegmentsTask extends AsyncTask<Void, Void, Void> {
    private final Context mContext;
    private final Draft mDraft;
    private final Runnable mOnCompleteRunnable;

    public CombineSegmentsTask(Context context, Draft draft, Runnable onCompleteRunnable) {
        this.mDraft = draft;
        this.mOnCompleteRunnable = onCompleteRunnable;
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... params) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            try {
                this.mDraft.generateFinalVideoPath();
                this.mDraft.generateFinalThumbnailPath();
                SegmentCombiner.combineSegments(this.mContext, this.mDraft.getSegments(), this.mDraft.getVideoPath());
                retriever.setDataSource(this.mDraft.getVideoPath());
                Bitmap b = retriever.getFrameAtTime(0L);
                if (b == null) {
                    b = retriever.getFrameAtTime();
                }
                if (b != null) {
                    b.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(this.mDraft.getThumbnailPath()));
                }
                retriever.release();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Throwable th) {
            retriever.release();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void avoid) {
        this.mOnCompleteRunnable.run();
    }
}
