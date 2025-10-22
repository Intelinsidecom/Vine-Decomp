package co.vine.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.DisplayMetrics;
import co.vine.android.VineLoggingException;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacv.cpp.avcodec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class MediaUtil {
    public static Bitmap[] getVideoFrames(String videoPath, long[] frameTime) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            try {
                retriever.setDataSource(videoPath);
                Bitmap[] r = new Bitmap[frameTime.length];
                int i = 0;
                long duration = -1;
                for (long time : frameTime) {
                    if (time == -1) {
                        if (duration == -1) {
                            String durationString = retriever.extractMetadata(9);
                            duration = Long.parseLong(durationString);
                            SLog.d("Got duration: {}.", Long.valueOf(duration));
                        }
                        time = duration * 1000;
                    }
                    r[i] = retriever.getFrameAtTime(time, 1);
                    if (r[i] == null) {
                        r[i] = retriever.getFrameAtTime(time, 3);
                    }
                    i++;
                    SLog.d("Got frame at {}ms.", Long.valueOf(time));
                }
                try {
                    return r;
                } catch (RuntimeException e) {
                    return r;
                }
            } catch (RuntimeException e2) {
                SLog.e("Failed to get frame.", (Throwable) e2);
                try {
                    retriever.release();
                } catch (RuntimeException e3) {
                    SLog.e("Failed to release.", (Throwable) e3);
                }
                return null;
            }
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e4) {
                SLog.e("Failed to release.", (Throwable) e4);
            }
        }
    }

    public static void scanFile(Context context, File file, String mimeType) {
        MediaConnectionClient client = new MediaConnectionClient(file.getAbsolutePath(), mimeType);
        MediaScannerConnection connection = new MediaScannerConnection(context, client);
        client.connection = connection;
        connection.connect();
    }

    public static class MediaConnectionClient implements MediaScannerConnection.MediaScannerConnectionClient {
        public MediaScannerConnection connection;
        private final String mMimeType;
        private final String mPath;

        public MediaConnectionClient(String mPath, String mimeType) {
            this.mPath = mPath;
            this.mMimeType = mimeType;
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
        public void onMediaScannerConnected() {
            this.connection.scanFile(this.mPath, this.mMimeType);
            this.connection.disconnect();
        }

        @Override // android.media.MediaScannerConnection.OnScanCompletedListener
        public void onScanCompleted(String path, Uri uri) {
            SLog.d("Scan completed: {}, {}.", path, uri);
        }
    }

    public static class GenerateThumbnailsRunnable implements Runnable {
        private final long duration;
        private final String lastFramePath;
        private final String thumbnailPath;
        private final String videoPath;

        @Override // java.lang.Runnable
        public void run() throws IOException {
            try {
                MediaUtil.generateThumbnails(this.duration, this.videoPath, this.thumbnailPath, this.lastFramePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void generateThumbnails(long duration, String videoPath, String thumbnailPath, String lastFramePath) throws IOException {
        long start = System.currentTimeMillis();
        SLog.d("Use duration: {}.", Long.valueOf(duration));
        Bitmap[] bitmaps = getVideoFrames(videoPath, lastFramePath != null ? new long[]{0, duration} : new long[]{0});
        SLog.b("Grabbing using MediaUtil took {}ms.", Long.valueOf(System.currentTimeMillis() - start));
        if (bitmaps != null) {
            if (bitmaps.length == 0 || bitmaps[0] == null) {
                CrashUtil.log("Mayday, mayday! Failed to get thumbnails, this will fail and crash: {}.", videoPath);
            }
            bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(thumbnailPath));
            if (lastFramePath != null) {
                if (bitmaps[1] != null) {
                    bitmaps[1].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(lastFramePath));
                } else {
                    CrashUtil.logException(new VineLoggingException("FAILED TO GENERATE GHOST IMAGE"));
                }
            }
        }
        try {
            if (!new File(thumbnailPath).exists()) {
                CrashUtil.log("Retry generating thumbnail.");
                Bitmap[] bitmaps2 = getVideoFrames(videoPath, new long[]{0});
                if (bitmaps2 != null && bitmaps2.length == 1) {
                    bitmaps2[0].compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(thumbnailPath));
                    SLog.i("First Thumbnail generated.");
                    return;
                }
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            CrashUtil.log("Failed to generate thumbnail.");
        }
    }

    public static int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (metrics.densityDpi * dp) / avcodec.AV_CODEC_ID_CDXL;
        return px;
    }
}
