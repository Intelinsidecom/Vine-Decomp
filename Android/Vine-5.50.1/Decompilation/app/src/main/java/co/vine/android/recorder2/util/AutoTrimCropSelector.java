package co.vine.android.recorder2.util;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import co.vine.android.recorder2.model.ImportVideoInfo;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/* loaded from: classes.dex */
public class AutoTrimCropSelector {
    private Point[] mCropOrigins;
    private long mRemainingUs;
    private long mTotalDuration;
    private long[] mTrimEndsUs;
    private long[] mTrimStartsUs;
    private Uri[] mUris;

    public AutoTrimCropSelector(ArrayList<ImportVideoInfo> videoInfos, long remainingUs) throws Throwable {
        MediaMetadataRetriever retriever;
        int size = videoInfos.size();
        this.mRemainingUs = remainingUs;
        this.mTrimStartsUs = new long[size];
        this.mTrimEndsUs = new long[size];
        this.mUris = new Uri[size];
        this.mCropOrigins = new Point[size];
        int width = 0;
        int height = 0;
        int i = 0;
        MediaMetadataRetriever retriever2 = null;
        while (i < size) {
            String videoPath = videoInfos.get(i).getLocalPath();
            this.mUris[i] = new Uri.Builder().scheme("file").path(videoPath).build();
            try {
                retriever = new MediaMetadataRetriever();
            } catch (Throwable th) {
                th = th;
                retriever = retriever2;
            }
            try {
                retriever.setDataSource(videoPath);
                String durationMsStr = retriever.extractMetadata(9);
                if (durationMsStr != null) {
                    long duration = Long.parseLong(durationMsStr) * 1000;
                    this.mTrimStartsUs[i] = 0;
                    this.mTrimEndsUs[i] = duration;
                    this.mTotalDuration += duration;
                }
                if (StringUtils.isBlank(videoInfos.get(i).getSourcePostId())) {
                    String widthStr = retriever.extractMetadata(18);
                    String heightStr = retriever.extractMetadata(19);
                    Bitmap bmp = retriever.getFrameAtTime();
                    if (bmp != null) {
                        height = bmp.getHeight();
                        width = bmp.getWidth();
                    } else {
                        width = widthStr != null ? Integer.parseInt(widthStr) : width;
                        if (heightStr != null) {
                            height = Integer.parseInt(heightStr);
                        }
                    }
                }
                this.mCropOrigins[i] = autoCropOrigin(width, height);
                if (retriever != null) {
                    retriever.release();
                }
                i++;
                retriever2 = retriever;
            } catch (Throwable th2) {
                th = th2;
                if (retriever != null) {
                    retriever.release();
                }
                throw th;
            }
        }
        setAutoTrim();
    }

    private Point autoCropOrigin(int width, int height) {
        int difference = Math.abs(width - height);
        if (difference == 0) {
            return new Point(0, 0);
        }
        if (width > height) {
            return new Point(difference / 2, 0);
        }
        return new Point(0, difference / 2);
    }

    private void setAutoTrim() {
        int size = this.mTrimStartsUs.length;
        long curTrimmedTotalUs = 0;
        if (this.mTotalDuration > this.mRemainingUs) {
            for (int i = 0; i < size; i++) {
                long curDuration = this.mTrimEndsUs[i];
                long trimLength = (((long) (this.mRemainingUs * (curDuration / this.mTotalDuration))) / 1000) * 1000;
                this.mTrimStartsUs[i] = (curDuration - trimLength) / 2;
                this.mTrimEndsUs[i] = this.mTrimStartsUs[i] + trimLength;
                curTrimmedTotalUs += this.mTrimEndsUs[i] - this.mTrimStartsUs[i];
            }
        }
        if (curTrimmedTotalUs < this.mRemainingUs) {
            this.mTrimEndsUs[size - 1] = (this.mTrimEndsUs[size - 1] + this.mRemainingUs) - curTrimmedTotalUs;
        }
    }

    public long getTrimStart(int index) {
        return this.mTrimStartsUs[index];
    }

    public long getTrimEnd(int index) {
        return this.mTrimEndsUs[index];
    }

    public Uri getUri(int index) {
        return this.mUris[index];
    }

    public Point getCropOrigin(int index) {
        return this.mCropOrigins[index];
    }
}
