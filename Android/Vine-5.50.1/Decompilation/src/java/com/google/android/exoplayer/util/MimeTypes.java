package com.google.android.exoplayer.util;

import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;

/* loaded from: classes.dex */
public final class MimeTypes {
    public static boolean isAudio(String mimeType) {
        return getTopLevelType(mimeType).equals("audio");
    }

    public static boolean isVideo(String mimeType) {
        return getTopLevelType(mimeType).equals("video");
    }

    public static boolean isText(String mimeType) {
        return getTopLevelType(mimeType).equals("text");
    }

    private static String getTopLevelType(String mimeType) {
        int indexOfSlash = mimeType.indexOf(47);
        if (indexOfSlash == -1) {
            throw new IllegalArgumentException("Invalid mime type: " + mimeType);
        }
        return mimeType.substring(0, indexOfSlash);
    }

    public static String getVideoMediaMimeType(String codecs) {
        if (codecs == null) {
            return "video/x-unknown";
        }
        String[] codecList = codecs.split(",");
        for (String str : codecList) {
            String codec = str.trim();
            if (codec.startsWith(VisualSampleEntry.TYPE3) || codec.startsWith(VisualSampleEntry.TYPE4)) {
                return "video/avc";
            }
            if (codec.startsWith(VisualSampleEntry.TYPE7) || codec.startsWith(VisualSampleEntry.TYPE6)) {
                return "video/hevc";
            }
            if (codec.startsWith("vp9")) {
                return "video/x-vnd.on2.vp9";
            }
            if (codec.startsWith("vp8")) {
                return "video/x-vnd.on2.vp8";
            }
        }
        return "video/x-unknown";
    }
}
