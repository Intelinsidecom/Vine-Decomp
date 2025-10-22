package co.vine.android.recorder2.util;

import android.media.MediaExtractor;
import android.media.MediaFormat;

/* loaded from: classes.dex */
public class MediaExtractorUtil {
    public static int selectVideoTrack(MediaExtractor extractor) {
        return selectTrack(extractor, "video/");
    }

    public static int selectAudioTrack(MediaExtractor extractor) {
        return selectTrack(extractor, "audio/");
    }

    public static int selectTrack(MediaExtractor extractor, String type) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString("mime");
            if (mime.startsWith(type)) {
                return i;
            }
        }
        return -1;
    }
}
