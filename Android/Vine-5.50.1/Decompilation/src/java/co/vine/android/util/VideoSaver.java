package co.vine.android.util;

import android.content.Context;
import android.os.AsyncTask;
import co.vine.android.cache.video.UrlVideo;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.AppSessionListener;
import co.vine.android.recorder.RecordConfigUtils;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;

/* loaded from: classes.dex */
public class VideoSaver {
    private final AppController mAppController;
    private final Context mContext;
    private VideoSavedListener mVideoSavedListener;

    public interface VideoSavedListener {
        void onVideoSaved(String str);
    }

    public VideoSaver(Context context, AppController appController, VideoSavedListener videoSavedListener) {
        this.mContext = context;
        this.mAppController = appController;
        this.mVideoSavedListener = videoSavedListener;
    }

    public AppSessionListener saveVideo(final String sourceUrl) {
        if (sourceUrl == null) {
            return null;
        }
        final VideoKey videoKey = new VideoKey(sourceUrl);
        String filePath = this.mAppController.getVideoFilePath(videoKey);
        if (filePath != null) {
            String[] videoInfo = {filePath, sourceUrl};
            new SaveFileToCameraRollTask().execute(videoInfo);
            return null;
        }
        AppSessionListener sessionListener = new AppSessionListener() { // from class: co.vine.android.util.VideoSaver.1
            @Override // co.vine.android.client.AppSessionListener
            public void onVideoPathObtained(HashMap<VideoKey, UrlVideo> videos) {
                UrlVideo video = videos.get(videoKey);
                if (video != null && video.isValid()) {
                    String[] videoInfo2 = {video.getAbsolutePath(), sourceUrl};
                    new SaveFileToCameraRollTask().execute(videoInfo2);
                    VideoSaver.this.mAppController.removeListener(this);
                }
            }
        };
        this.mAppController.addListener(sessionListener);
        return sessionListener;
    }

    private class SaveFileToCameraRollTask extends AsyncTask<String[], Void, Void> {
        private SaveFileToCameraRollTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(String[]... params) throws Throwable {
            String filePath = params[0][0];
            if (filePath != null) {
                File sourceFile = new File(filePath);
                File destinationFile = RecordConfigUtils.getCameraRollFile(VideoSaver.this.mContext, System.nanoTime(), FilenameUtils.getExtension(filePath));
                if (sourceFile.exists() && destinationFile != null) {
                    boolean didCopyToFile = RecordConfigUtils.copySilently(sourceFile, destinationFile);
                    if (didCopyToFile) {
                        MediaUtil.scanFile(VideoSaver.this.mContext, destinationFile, "video/mp4");
                        VideoSaver.this.mVideoSavedListener.onVideoSaved(params[0][1]);
                        SLog.d("Copied the file at path {} to {}.", filePath, destinationFile.getAbsolutePath());
                    } else {
                        SLog.d("Failed to copy file at path {}.", filePath);
                    }
                }
            }
            return null;
        }
    }
}
