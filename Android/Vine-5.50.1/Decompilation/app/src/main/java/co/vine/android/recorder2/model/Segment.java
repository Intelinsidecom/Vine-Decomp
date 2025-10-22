package co.vine.android.recorder2.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import com.edisonwang.android.slog.SLog;
import java.io.Externalizable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Random;

/* loaded from: classes.dex */
public class Segment implements Externalizable {
    private Point mCropOrigin;
    private Drawable mGhostDrawable;
    private String mGhostPath;
    private boolean mHasBeenSilenced;
    private String mImportVideoPath;
    private ArrayList<Long> mKeyframeTimestampsMS;
    private long mMaxTranscodedMs;
    private long mMinTranscodedMs;
    private boolean mShouldAnimateIn;
    private String mSourcePostId;
    private Drawable mThumbnailDrawable;
    private String mThumbnailPath;
    private long mTrimEndMS;
    private long mTrimStartMS;
    private long mUntrimmedDurationMS;
    private String mVideoPath;

    public Segment() {
        this.mUntrimmedDurationMS = 0L;
        this.mTrimStartMS = 0L;
        this.mTrimEndMS = 0L;
        this.mVideoPath = null;
        this.mThumbnailPath = null;
        this.mGhostPath = null;
        this.mThumbnailDrawable = null;
        this.mGhostDrawable = null;
        this.mHasBeenSilenced = false;
        this.mShouldAnimateIn = false;
        this.mKeyframeTimestampsMS = null;
        this.mSourcePostId = null;
        this.mImportVideoPath = null;
        this.mCropOrigin = null;
        this.mMinTranscodedMs = 0L;
        this.mMaxTranscodedMs = 0L;
    }

    public Segment(Segment segment) {
        this();
        if (segment != null) {
            this.mUntrimmedDurationMS = segment.mUntrimmedDurationMS;
            this.mVideoPath = segment.mVideoPath;
            this.mThumbnailPath = segment.mThumbnailPath;
            this.mGhostPath = segment.mGhostPath;
            this.mHasBeenSilenced = segment.mHasBeenSilenced;
            this.mTrimStartMS = segment.mTrimStartMS;
            this.mTrimEndMS = segment.mTrimEndMS;
            this.mKeyframeTimestampsMS = segment.mKeyframeTimestampsMS;
            this.mSourcePostId = segment.mSourcePostId;
            this.mImportVideoPath = segment.mImportVideoPath;
            this.mCropOrigin = segment.mCropOrigin;
            this.mMinTranscodedMs = segment.mMinTranscodedMs;
            this.mMaxTranscodedMs = segment.mMaxTranscodedMs;
        }
    }

    public String getVideoPath() {
        return this.mVideoPath;
    }

    public void generateVideoPath(String draftRootPath) {
        if (this.mVideoPath != null) {
            throw new IllegalStateException("Already generated video path");
        }
        int random = new Random().nextInt(10000);
        this.mVideoPath = draftRootPath + "/" + System.currentTimeMillis() + "_" + random + ".mp4";
    }

    public void generateThumbnail() throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            try {
                if (this.mVideoPath != null) {
                    retriever.setDataSource(getVideoPath());
                    Bitmap b = retriever.getFrameAtTime(0L);
                    this.mThumbnailPath = this.mVideoPath + ".jpg";
                    if (b != null) {
                        b.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(this.mThumbnailPath));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            retriever.release();
        }
    }

    public void retrieveMetadata() throws Throwable {
        long nanos = System.nanoTime();
        MediaMetadataRetriever retriever = null;
        try {
            try {
                if (this.mVideoPath != null) {
                    MediaMetadataRetriever retriever2 = new MediaMetadataRetriever();
                    try {
                        retriever2.setDataSource(this.mVideoPath);
                        Bitmap thumb = retriever2.getFrameAtTime(0L);
                        generateThumbnailPath();
                        if (thumb != null) {
                            thumb.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(this.mThumbnailPath));
                        }
                        retriever = retriever2;
                    } catch (IOException e) {
                        e = e;
                        retriever = retriever2;
                        throw new RuntimeException(e);
                    } catch (Throwable th) {
                        th = th;
                        retriever = retriever2;
                        if (retriever != null) {
                            retriever.release();
                        }
                        throw th;
                    }
                }
                if (retriever != null) {
                    retriever.release();
                }
                SLog.d("ryango metadata {}", Long.valueOf((System.nanoTime() - nanos) / 1000000));
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void generateGhostPath() {
        this.mGhostPath = this.mVideoPath + "ghost.jpg";
    }

    public void generateThumbnailPath() {
        this.mThumbnailPath = this.mVideoPath + ".jpg";
    }

    public long getUntrimmedDurationMS() {
        return this.mUntrimmedDurationMS;
    }

    public long getTrimmedDurationMS() {
        return this.mTrimEndMS - this.mTrimStartMS;
    }

    public Drawable getDrawable() {
        return this.mThumbnailDrawable;
    }

    public void setThumbnailDrawable(Drawable drawable) {
        this.mThumbnailDrawable = drawable;
    }

    public String getGhostPath() {
        return this.mGhostPath;
    }

    public Drawable getGhostDrawable() {
        return this.mGhostDrawable;
    }

    public void setGhostDrawable(Drawable drawable) {
        this.mGhostDrawable = drawable;
    }

    public void setImportVideoPath(String importVideoPath) {
        this.mImportVideoPath = importVideoPath;
    }

    public String getImportVideoPath() {
        return this.mImportVideoPath;
    }

    public void setCropOrigin(Point cropOrigin) {
        this.mCropOrigin = cropOrigin;
    }

    public Point getCropOrigin() {
        return this.mCropOrigin;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Segment)) {
            return false;
        }
        Segment other = (Segment) o;
        if (!this.mVideoPath.equals(other.mVideoPath) || !this.mThumbnailPath.equals(other.mThumbnailPath) || !this.mGhostPath.equals(other.mGhostPath) || this.mUntrimmedDurationMS != other.mUntrimmedDurationMS || this.mHasBeenSilenced != other.mHasBeenSilenced || this.mTrimStartMS != other.mTrimStartMS || this.mTrimEndMS != other.mTrimEndMS) {
            return false;
        }
        if (this.mSourcePostId == null) {
            if (other.mSourcePostId != null) {
                return false;
            }
        } else if (!this.mSourcePostId.equals(other.mSourcePostId)) {
            return false;
        }
        if (this.mImportVideoPath == null) {
            if (other.mImportVideoPath != null) {
                return false;
            }
        } else if (!this.mImportVideoPath.equals(other.mImportVideoPath)) {
            return false;
        }
        if (this.mCropOrigin == null) {
            if (other.mCropOrigin != null) {
                return false;
            }
        } else if (!this.mCropOrigin.equals(other.mCropOrigin)) {
            return false;
        }
        return this.mMinTranscodedMs == other.mMinTranscodedMs && this.mMaxTranscodedMs == other.mMaxTranscodedMs;
    }

    public void toggleSilence() {
        this.mHasBeenSilenced = !this.mHasBeenSilenced;
    }

    public void setShouldAnimateIn(boolean animate) {
        this.mShouldAnimateIn = animate;
    }

    public boolean shouldAnimateIn() {
        return this.mShouldAnimateIn;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.mVideoPath = (String) in.readObject();
        this.mThumbnailPath = (String) in.readObject();
        this.mGhostPath = (String) in.readObject();
        this.mUntrimmedDurationMS = in.readLong();
        this.mHasBeenSilenced = in.readBoolean();
        this.mTrimStartMS = in.readLong();
        this.mTrimEndMS = in.readLong();
        this.mKeyframeTimestampsMS = (ArrayList) in.readObject();
        this.mSourcePostId = (String) in.readObject();
        this.mImportVideoPath = (String) in.readObject();
        this.mCropOrigin = (Point) in.readObject();
        this.mMinTranscodedMs = in.readLong();
        this.mMaxTranscodedMs = in.readLong();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.mVideoPath);
        out.writeObject(this.mThumbnailPath);
        out.writeObject(this.mGhostPath);
        out.writeLong(this.mUntrimmedDurationMS);
        out.writeBoolean(this.mHasBeenSilenced);
        out.writeLong(this.mTrimStartMS);
        out.writeLong(this.mTrimEndMS);
        out.writeObject(this.mKeyframeTimestampsMS);
        out.writeObject(this.mSourcePostId);
        out.writeObject(this.mImportVideoPath);
        out.writeObject(this.mCropOrigin);
        out.writeLong(this.mMinTranscodedMs);
        out.writeLong(this.mMaxTranscodedMs);
    }

    public boolean isSilenced() {
        return this.mHasBeenSilenced;
    }

    public void setDuration(long currentRecordingDurationMillis) {
        this.mUntrimmedDurationMS = currentRecordingDurationMillis;
        this.mTrimEndMS = this.mUntrimmedDurationMS;
    }

    public void setSourcePostId(String id) {
        this.mSourcePostId = id;
    }

    public String getSourcePostId() {
        return this.mSourcePostId;
    }

    public void setTrimStart(long trimStartMs) {
        this.mTrimStartMS = Math.max(0L, trimStartMs);
    }

    public void setTrimEnd(long trimEndMs) {
        this.mTrimEndMS = Math.min(trimEndMs, this.mUntrimmedDurationMS);
    }

    public long[] getTrimPointsMs() {
        return new long[]{this.mTrimStartMS, this.mTrimEndMS};
    }

    public ArrayList<Long> getKeyframeTimestampsMS() {
        return this.mKeyframeTimestampsMS;
    }

    public void onKeyframeWritten(long timestampUS) {
        if (this.mKeyframeTimestampsMS == null) {
            this.mKeyframeTimestampsMS = new ArrayList<>();
        }
        this.mKeyframeTimestampsMS.add(Long.valueOf(timestampUS / 1000));
    }

    public int hashCode() {
        int result = super.hashCode();
        return (((((((((((((((((((((((((((((((result * 31) + (this.mVideoPath != null ? this.mVideoPath.hashCode() : 0)) * 31) + (this.mThumbnailPath != null ? this.mThumbnailPath.hashCode() : 0)) * 31) + (this.mGhostPath != null ? this.mGhostPath.hashCode() : 0)) * 31) + ((int) (this.mUntrimmedDurationMS ^ (this.mUntrimmedDurationMS >>> 32)))) * 31) + (this.mHasBeenSilenced ? 1 : 0)) * 31) + ((int) (this.mTrimStartMS ^ (this.mTrimStartMS >>> 32)))) * 31) + ((int) (this.mTrimEndMS ^ (this.mTrimEndMS >>> 32)))) * 31) + (this.mKeyframeTimestampsMS != null ? this.mKeyframeTimestampsMS.hashCode() : 0)) * 31) + (this.mShouldAnimateIn ? 1 : 0)) * 31) + (this.mThumbnailDrawable != null ? this.mThumbnailDrawable.hashCode() : 0)) * 31) + (this.mGhostDrawable != null ? this.mGhostDrawable.hashCode() : 0)) * 31) + (this.mSourcePostId != null ? this.mSourcePostId.hashCode() : 0)) * 31) + (this.mImportVideoPath != null ? this.mImportVideoPath.hashCode() : 0)) * 31) + (this.mCropOrigin != null ? this.mCropOrigin.hashCode() : 0)) * 31) + ((int) (this.mMinTranscodedMs ^ (this.mMinTranscodedMs >>> 32)))) * 31) + ((int) (this.mMaxTranscodedMs ^ (this.mMaxTranscodedMs >>> 32)));
    }
}
