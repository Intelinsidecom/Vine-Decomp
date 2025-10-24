package com.google.android.exoplayer.util;

import android.util.Log;
import android.util.Pair;

/* loaded from: classes.dex */
public final class CodecSpecificDataUtil {
    private static final byte[] NAL_START_CODE = {0, 0, 0, 1};
    private static final int[] AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350};
    private static final int[] AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE = {0, 1, 2, 3, 4, 5, 6, 8, -1, -1, -1, 7, 8, -1, 8, -1};

    public static final class SpsData {
        public final int height;
        public final float pixelWidthAspectRatio;
        public final int width;

        public SpsData(int width, int height, float pixelWidthAspectRatio) {
            this.width = width;
            this.height = height;
            this.pixelWidthAspectRatio = pixelWidthAspectRatio;
        }
    }

    public static Pair<Integer, Integer> parseAacAudioSpecificConfig(byte[] audioSpecificConfig) {
        int sampleRate;
        ParsableBitArray bitArray = new ParsableBitArray(audioSpecificConfig);
        int audioObjectType = bitArray.readBits(5);
        int frequencyIndex = bitArray.readBits(4);
        if (frequencyIndex == 15) {
            sampleRate = bitArray.readBits(24);
        } else {
            Assertions.checkArgument(frequencyIndex < 13);
            sampleRate = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[frequencyIndex];
        }
        int channelConfiguration = bitArray.readBits(4);
        if (audioObjectType == 5 || audioObjectType == 29) {
            int frequencyIndex2 = bitArray.readBits(4);
            if (frequencyIndex2 == 15) {
                sampleRate = bitArray.readBits(24);
            } else {
                Assertions.checkArgument(frequencyIndex2 < 13);
                sampleRate = AUDIO_SPECIFIC_CONFIG_SAMPLING_RATE_TABLE[frequencyIndex2];
            }
            if (bitArray.readBits(5) == 22) {
                channelConfiguration = bitArray.readBits(4);
            }
        }
        int channelCount = AUDIO_SPECIFIC_CONFIG_CHANNEL_COUNT_TABLE[channelConfiguration];
        Assertions.checkArgument(channelCount != -1);
        return Pair.create(Integer.valueOf(sampleRate), Integer.valueOf(channelCount));
    }

    public static byte[] buildAacAudioSpecificConfig(int audioObjectType, int sampleRateIndex, int channelConfig) {
        byte[] audioSpecificConfig = {(byte) (((audioObjectType << 3) & 248) | ((sampleRateIndex >> 1) & 7)), (byte) (((sampleRateIndex << 7) & 128) | ((channelConfig << 3) & 120))};
        return audioSpecificConfig;
    }

    public static byte[] buildNalUnit(byte[] data, int offset, int length) {
        byte[] nalUnit = new byte[NAL_START_CODE.length + length];
        System.arraycopy(NAL_START_CODE, 0, nalUnit, 0, NAL_START_CODE.length);
        System.arraycopy(data, offset, nalUnit, NAL_START_CODE.length, length);
        return nalUnit;
    }

    public static SpsData parseSpsNalUnit(ParsableBitArray bitArray) {
        int cropUnitX;
        int cropUnitY;
        int profileIdc = bitArray.readBits(8);
        bitArray.skipBits(16);
        bitArray.readUnsignedExpGolombCodedInt();
        int chromaFormatIdc = 1;
        if (profileIdc == 100 || profileIdc == 110 || profileIdc == 122 || profileIdc == 244 || profileIdc == 44 || profileIdc == 83 || profileIdc == 86 || profileIdc == 118 || profileIdc == 128 || profileIdc == 138) {
            chromaFormatIdc = bitArray.readUnsignedExpGolombCodedInt();
            if (chromaFormatIdc == 3) {
                bitArray.skipBits(1);
            }
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.skipBits(1);
            boolean seqScalingMatrixPresentFlag = bitArray.readBit();
            if (seqScalingMatrixPresentFlag) {
                int limit = chromaFormatIdc != 3 ? 8 : 12;
                int i = 0;
                while (i < limit) {
                    boolean seqScalingListPresentFlag = bitArray.readBit();
                    if (seqScalingListPresentFlag) {
                        skipScalingList(bitArray, i < 6 ? 16 : 64);
                    }
                    i++;
                }
            }
        }
        bitArray.readUnsignedExpGolombCodedInt();
        long picOrderCntType = bitArray.readUnsignedExpGolombCodedInt();
        if (picOrderCntType == 0) {
            bitArray.readUnsignedExpGolombCodedInt();
        } else if (picOrderCntType == 1) {
            bitArray.skipBits(1);
            bitArray.readSignedExpGolombCodedInt();
            bitArray.readSignedExpGolombCodedInt();
            long numRefFramesInPicOrderCntCycle = bitArray.readUnsignedExpGolombCodedInt();
            for (int i2 = 0; i2 < numRefFramesInPicOrderCntCycle; i2++) {
                bitArray.readUnsignedExpGolombCodedInt();
            }
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.skipBits(1);
        int picWidthInMbs = bitArray.readUnsignedExpGolombCodedInt() + 1;
        int picHeightInMapUnits = bitArray.readUnsignedExpGolombCodedInt() + 1;
        boolean frameMbsOnlyFlag = bitArray.readBit();
        int frameHeightInMbs = (2 - (frameMbsOnlyFlag ? 1 : 0)) * picHeightInMapUnits;
        if (!frameMbsOnlyFlag) {
            bitArray.skipBits(1);
        }
        bitArray.skipBits(1);
        int frameWidth = picWidthInMbs * 16;
        int frameHeight = frameHeightInMbs * 16;
        boolean frameCroppingFlag = bitArray.readBit();
        if (frameCroppingFlag) {
            int frameCropLeftOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropRightOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropTopOffset = bitArray.readUnsignedExpGolombCodedInt();
            int frameCropBottomOffset = bitArray.readUnsignedExpGolombCodedInt();
            if (chromaFormatIdc == 0) {
                cropUnitX = 1;
                cropUnitY = 2 - (frameMbsOnlyFlag ? 1 : 0);
            } else {
                int subWidthC = chromaFormatIdc == 3 ? 1 : 2;
                int subHeightC = chromaFormatIdc == 1 ? 2 : 1;
                cropUnitX = subWidthC;
                cropUnitY = subHeightC * (2 - (frameMbsOnlyFlag ? 1 : 0));
            }
            frameWidth -= (frameCropLeftOffset + frameCropRightOffset) * cropUnitX;
            frameHeight -= (frameCropTopOffset + frameCropBottomOffset) * cropUnitY;
        }
        float pixelWidthHeightRatio = 1.0f;
        boolean vuiParametersPresentFlag = bitArray.readBit();
        if (vuiParametersPresentFlag) {
            boolean aspectRatioInfoPresentFlag = bitArray.readBit();
            if (aspectRatioInfoPresentFlag) {
                int aspectRatioIdc = bitArray.readBits(8);
                if (aspectRatioIdc == 255) {
                    int sarWidth = bitArray.readBits(16);
                    int sarHeight = bitArray.readBits(16);
                    if (sarWidth != 0 && sarHeight != 0) {
                        pixelWidthHeightRatio = sarWidth / sarHeight;
                    }
                } else if (aspectRatioIdc < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                    pixelWidthHeightRatio = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[aspectRatioIdc];
                } else {
                    Log.w("CodecSpecificDataUtil", "Unexpected aspect_ratio_idc value: " + aspectRatioIdc);
                }
            }
        }
        return new SpsData(frameWidth, frameHeight, pixelWidthHeightRatio);
    }

    private static void skipScalingList(ParsableBitArray bitArray, int size) {
        int lastScale = 8;
        int nextScale = 8;
        for (int i = 0; i < size; i++) {
            if (nextScale != 0) {
                int deltaScale = bitArray.readSignedExpGolombCodedInt();
                nextScale = ((lastScale + deltaScale) + 256) % 256;
            }
            if (nextScale != 0) {
                lastScale = nextScale;
            }
        }
    }
}
