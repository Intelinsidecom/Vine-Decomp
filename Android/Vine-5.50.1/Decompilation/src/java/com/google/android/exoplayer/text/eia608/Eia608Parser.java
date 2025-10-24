package com.google.android.exoplayer.text.eia608;

import com.google.android.exoplayer.util.ParsableByteArray;
import com.googlecode.javacv.cpp.avcodec;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
public final class Eia608Parser {
    private static final int[] BASIC_CHARACTER_SET = {32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 225, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, avcodec.AV_CODEC_ID_INDEO5, avcodec.AV_CODEC_ID_MIMIC, 115, 116, 117, 118, 119, 120, 121, 122, 231, 247, 209, 241, 9632};
    private static final int[] SPECIAL_CHARACTER_SET = {174, 176, 189, 191, 8482, avcodec.AV_CODEC_ID_ZEROCODEC, avcodec.AV_CODEC_ID_MSS1, 9834, 224, 32, 232, 226, 234, 238, avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, 251};
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = {193, 201, 211, 218, 220, 252, 8216, avcodec.AV_CODEC_ID_XBM, 42, 39, 8212, avcodec.AV_CODEC_ID_VP9, 8480, 8226, 8220, 8221, 192, 194, 199, HttpResponseCode.OK, 202, 203, 235, 206, 207, 239, 212, 217, 249, 219, avcodec.AV_CODEC_ID_ESCAPE130_DEPRECATED, 187};
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = {195, 227, 205, 204, 236, 210, 242, 213, 245, 123, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, avcodec.AV_CODEC_ID_TSCC2, avcodec.AV_CODEC_ID_MSA1, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};

    public static boolean isSeiMessageEia608(int payloadType, int payloadLength, ParsableByteArray payload) {
        if (payloadType != 4 || payloadLength < 8) {
            return false;
        }
        int startPosition = payload.getPosition();
        int countryCode = payload.readUnsignedByte();
        int providerCode = payload.readUnsignedShort();
        int userIdentifier = payload.readInt();
        int userDataTypeCode = payload.readUnsignedByte();
        payload.setPosition(startPosition);
        return countryCode == 181 && providerCode == 49 && userIdentifier == 1195456820 && userDataTypeCode == 3;
    }
}
