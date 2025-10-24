package co.vine.android.util;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.util.Pair;
import java.util.HashMap;

/* loaded from: classes.dex */
public class MediaCodecUtil {
    private static final int SDK_INT = Build.VERSION.SDK_INT;
    private static final HashMap<String, Pair<String, MediaCodecInfo.CodecCapabilities>> codecs = new HashMap<>();

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean secureDecodersExplicit();
    }

    public static String getDecoderInfo(String mimeType) {
        Pair<String, MediaCodecInfo.CodecCapabilities> info = getMediaCodecInfo(mimeType);
        if (info == null) {
            return null;
        }
        return (String) info.first;
    }

    private static synchronized Pair<String, MediaCodecInfo.CodecCapabilities> getMediaCodecInfo(String mimeType) {
        Pair<String, MediaCodecInfo.CodecCapabilities> mediaCodecInfo;
        if (codecs.containsKey(mimeType)) {
            mediaCodecInfo = codecs.get(mimeType);
        } else {
            mediaCodecInfo = getMediaCodecInfo(mimeType, SDK_INT >= 21 ? new MediaCodecListCompatV21() : new MediaCodecListCompatV16());
        }
        return mediaCodecInfo;
    }

    private static Pair<String, MediaCodecInfo.CodecCapabilities> getMediaCodecInfo(String mimeType, MediaCodecListCompat mediaCodecList) {
        int numberOfCodecs = mediaCodecList.getCodecCount();
        boolean secureDecodersExplicit = mediaCodecList.secureDecodersExplicit();
        for (int i = 0; i < numberOfCodecs; i++) {
            MediaCodecInfo info = mediaCodecList.getCodecInfoAt(i);
            String codecName = info.getName();
            if (!info.isEncoder() && codecName.startsWith("OMX.") && (secureDecodersExplicit || !codecName.endsWith(".secure"))) {
                String[] supportedTypes = info.getSupportedTypes();
                for (String supportedType : supportedTypes) {
                    if (supportedType.equalsIgnoreCase(mimeType)) {
                        MediaCodecInfo.CodecCapabilities capabilities = info.getCapabilitiesForType(supportedType);
                        Pair<String, MediaCodecInfo.CodecCapabilities> pair = Pair.create(codecName, capabilities);
                        codecs.put(mimeType, pair);
                        return pair;
                    }
                }
            }
        }
        return null;
    }

    @TargetApi(21)
    private static class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final MediaCodecInfo[] mediaCodecInfos = new MediaCodecList(0).getCodecInfos();

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            return this.mediaCodecInfos.length;
        }

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public MediaCodecInfo getCodecInfoAt(int index) {
            return this.mediaCodecInfos[index];
        }

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return true;
        }
    }

    private static class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public MediaCodecInfo getCodecInfoAt(int index) {
            return MediaCodecList.getCodecInfoAt(index);
        }

        @Override // co.vine.android.util.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return false;
        }
    }
}
