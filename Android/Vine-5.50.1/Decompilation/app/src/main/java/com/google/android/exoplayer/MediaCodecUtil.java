package com.google.android.exoplayer;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.io.IOException;
import java.util.HashMap;

@TargetApi(16)
/* loaded from: classes.dex */
public final class MediaCodecUtil {
    private static final HashMap<CodecKey, Pair<String, MediaCodecInfo.CodecCapabilities>> codecs = new HashMap<>();

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, MediaCodecInfo.CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    public static class DecoderQueryException extends IOException {
        private DecoderQueryException(Throwable cause) {
            super("Failed to query underlying media codecs", cause);
        }
    }

    public static DecoderInfo getDecoderInfo(String mimeType, boolean secure) throws DecoderQueryException {
        Pair<String, MediaCodecInfo.CodecCapabilities> info = getMediaCodecInfo(mimeType, secure);
        if (info == null) {
            return null;
        }
        return new DecoderInfo((String) info.first, isAdaptive((MediaCodecInfo.CodecCapabilities) info.second));
    }

    public static synchronized Pair<String, MediaCodecInfo.CodecCapabilities> getMediaCodecInfo(String mimeType, boolean secure) throws DecoderQueryException {
        Pair<String, MediaCodecInfo.CodecCapabilities> pair;
        CodecKey key = new CodecKey(mimeType, secure);
        if (codecs.containsKey(key)) {
            pair = codecs.get(key);
        } else {
            MediaCodecListCompat mediaCodecList = Util.SDK_INT >= 21 ? new MediaCodecListCompatV21(secure) : new MediaCodecListCompatV16();
            Pair<String, MediaCodecInfo.CodecCapabilities> codecInfo = getMediaCodecInfo(key, mediaCodecList);
            if (secure && codecInfo == null && 21 <= Util.SDK_INT && Util.SDK_INT <= 23) {
                MediaCodecListCompat mediaCodecList2 = new MediaCodecListCompatV16();
                codecInfo = getMediaCodecInfo(key, mediaCodecList2);
                if (codecInfo != null) {
                    Log.w("MediaCodecUtil", "MediaCodecList API didn't list secure decoder for: " + mimeType + ". Assuming: " + ((String) codecInfo.first));
                }
            }
            pair = codecInfo;
        }
        return pair;
    }

    private static Pair<String, MediaCodecInfo.CodecCapabilities> getMediaCodecInfo(CodecKey key, MediaCodecListCompat mediaCodecList) throws DecoderQueryException {
        try {
            return getMediaCodecInfoInternal(key, mediaCodecList);
        } catch (Exception e) {
            throw new DecoderQueryException(e);
        }
    }

    private static Pair<String, MediaCodecInfo.CodecCapabilities> getMediaCodecInfoInternal(CodecKey key, MediaCodecListCompat mediaCodecList) {
        String mimeType = key.mimeType;
        int numberOfCodecs = mediaCodecList.getCodecCount();
        boolean secureDecodersExplicit = mediaCodecList.secureDecodersExplicit();
        for (int i = 0; i < numberOfCodecs; i++) {
            MediaCodecInfo info = mediaCodecList.getCodecInfoAt(i);
            String codecName = info.getName();
            if (isCodecUsableDecoder(info, codecName, secureDecodersExplicit)) {
                String[] supportedTypes = info.getSupportedTypes();
                for (String supportedType : supportedTypes) {
                    if (supportedType.equalsIgnoreCase(mimeType)) {
                        MediaCodecInfo.CodecCapabilities capabilities = info.getCapabilitiesForType(supportedType);
                        boolean secure = mediaCodecList.isSecurePlaybackSupported(key.mimeType, capabilities);
                        if (!secureDecodersExplicit) {
                            codecs.put(key.secure ? new CodecKey(mimeType, false) : key, Pair.create(codecName, capabilities));
                            if (secure) {
                                codecs.put(key.secure ? key : new CodecKey(mimeType, true), Pair.create(codecName + ".secure", capabilities));
                            }
                        } else {
                            codecs.put(key.secure == secure ? key : new CodecKey(mimeType, secure), Pair.create(codecName, capabilities));
                        }
                        if (codecs.containsKey(key)) {
                            return codecs.get(key);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isCodecUsableDecoder(MediaCodecInfo info, String name, boolean secureDecodersExplicit) {
        if (info.isEncoder()) {
            return false;
        }
        if (!secureDecodersExplicit && name.endsWith(".secure")) {
            return false;
        }
        if ((Util.SDK_INT < 21 && "CIPAACDecoder".equals(name)) || "CIPMP3Decoder".equals(name) || "CIPVorbisDecoder".equals(name) || "AACDecoder".equals(name) || "MP3Decoder".equals(name)) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.SEC.MP3.Decoder".equals(name)) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.mp3".equals(name) && ("dlxu".equals(Util.DEVICE) || "protou".equals(Util.DEVICE) || "C6602".equals(Util.DEVICE) || "C6603".equals(Util.DEVICE) || "C6606".equals(Util.DEVICE) || "C6616".equals(Util.DEVICE) || "L36h".equals(Util.DEVICE) || "SO-02E".equals(Util.DEVICE))) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(name) && ("C1504".equals(Util.DEVICE) || "C1505".equals(Util.DEVICE) || "C1604".equals(Util.DEVICE) || "C1605".equals(Util.DEVICE))) {
            return false;
        }
        return Util.SDK_INT > 19 || Util.DEVICE == null || !((Util.DEVICE.startsWith("d2") || Util.DEVICE.startsWith("serrano")) && "samsung".equals(Util.MANUFACTURER) && name.equals("OMX.SEC.vp8.dec"));
    }

    private static boolean isAdaptive(MediaCodecInfo.CodecCapabilities capabilities) {
        if (Util.SDK_INT >= 19) {
            return isAdaptiveV19(capabilities);
        }
        return false;
    }

    @TargetApi(19)
    private static boolean isAdaptiveV19(MediaCodecInfo.CodecCapabilities capabilities) {
        return capabilities.isFeatureSupported("adaptive-playback");
    }

    @TargetApi(21)
    public static boolean isSizeSupportedV21(String mimeType, boolean secure, int width, int height) throws DecoderQueryException {
        Assertions.checkState(Util.SDK_INT >= 21);
        MediaCodecInfo.VideoCapabilities videoCapabilities = getVideoCapabilitiesV21(mimeType, secure);
        return videoCapabilities != null && videoCapabilities.isSizeSupported(width, height);
    }

    @TargetApi(21)
    public static boolean isSizeAndRateSupportedV21(String mimeType, boolean secure, int width, int height, double frameRate) throws DecoderQueryException {
        Assertions.checkState(Util.SDK_INT >= 21);
        MediaCodecInfo.VideoCapabilities videoCapabilities = getVideoCapabilitiesV21(mimeType, secure);
        return videoCapabilities != null && videoCapabilities.areSizeAndRateSupported(width, height, frameRate);
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        int maxH264DecodableFrameSize = 0;
        Pair<String, MediaCodecInfo.CodecCapabilities> info = getMediaCodecInfo("video/avc", false);
        if (info != null) {
            maxH264DecodableFrameSize = 0;
            MediaCodecInfo.CodecCapabilities capabilities = (MediaCodecInfo.CodecCapabilities) info.second;
            for (int i = 0; i < capabilities.profileLevels.length; i++) {
                MediaCodecInfo.CodecProfileLevel profileLevel = capabilities.profileLevels[i];
                maxH264DecodableFrameSize = Math.max(avcLevelToMaxFrameSize(profileLevel.level), maxH264DecodableFrameSize);
            }
        }
        return maxH264DecodableFrameSize;
    }

    @TargetApi(21)
    private static MediaCodecInfo.VideoCapabilities getVideoCapabilitiesV21(String mimeType, boolean secure) throws DecoderQueryException {
        Pair<String, MediaCodecInfo.CodecCapabilities> info = getMediaCodecInfo(mimeType, secure);
        if (info == null) {
            return null;
        }
        return ((MediaCodecInfo.CodecCapabilities) info.second).getVideoCapabilities();
    }

    private static int avcLevelToMaxFrameSize(int avcLevel) {
        switch (avcLevel) {
            case 1:
            case 2:
                return 25344;
            case 8:
                return 101376;
            case 16:
                return 101376;
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
                return 414720;
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
                return 2097152;
            case 4096:
                return 2097152;
            case 8192:
                return 2228224;
            case 16384:
                return 5652480;
            case 32768:
                return 9437184;
            default:
                return -1;
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;

        public MediaCodecListCompatV21(boolean includeSecure) {
            this.codecKind = includeSecure ? 1 : 0;
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public MediaCodecInfo getCodecInfoAt(int index) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[index];
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return true;
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return capabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public MediaCodecInfo getCodecInfoAt(int index) {
            return MediaCodecList.getCodecInfoAt(index);
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return false;
        }

        @Override // com.google.android.exoplayer.MediaCodecUtil.MediaCodecListCompat
        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return "video/avc".equals(mimeType);
        }
    }

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String mimeType, boolean secure) {
            this.mimeType = mimeType;
            this.secure = secure;
        }

        public int hashCode() {
            int result = (this.mimeType == null ? 0 : this.mimeType.hashCode()) + 31;
            return (result * 31) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != CodecKey.class) {
                return false;
            }
            CodecKey other = (CodecKey) obj;
            return TextUtils.equals(this.mimeType, other.mimeType) && this.secure == other.secure;
        }
    }
}
