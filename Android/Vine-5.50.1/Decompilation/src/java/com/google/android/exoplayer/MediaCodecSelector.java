package com.google.android.exoplayer;

import com.google.android.exoplayer.MediaCodecUtil;

/* loaded from: classes.dex */
public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = new MediaCodecSelector() { // from class: com.google.android.exoplayer.MediaCodecSelector.1
        @Override // com.google.android.exoplayer.MediaCodecSelector
        public DecoderInfo getDecoderInfo(MediaFormat format, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(format.mimeType, requiresSecureDecoder);
        }

        @Override // com.google.android.exoplayer.MediaCodecSelector
        public String getPassthroughDecoderName() throws MediaCodecUtil.DecoderQueryException {
            return "OMX.google.raw.decoder";
        }
    };

    DecoderInfo getDecoderInfo(MediaFormat mediaFormat, boolean z) throws MediaCodecUtil.DecoderQueryException;

    String getPassthroughDecoderName() throws MediaCodecUtil.DecoderQueryException;
}
