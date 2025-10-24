package com.google.android.exoplayer.extractor.ts;

import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.TrackOutput;
import com.google.android.exoplayer.text.eia608.Eia608Parser;
import com.google.android.exoplayer.util.ParsableByteArray;

/* loaded from: classes.dex */
final class SeiReader {
    private final TrackOutput output;

    public SeiReader(TrackOutput output) {
        this.output = output;
        output.format(MediaFormat.createTextFormat(null, "application/eia-608", -1, -1L, null));
    }

    public void consume(long pesTimeUs, ParsableByteArray seiBuffer) {
        int b;
        int b2;
        while (seiBuffer.bytesLeft() > 1) {
            int payloadType = 0;
            do {
                b = seiBuffer.readUnsignedByte();
                payloadType += b;
            } while (b == 255);
            int payloadSize = 0;
            do {
                b2 = seiBuffer.readUnsignedByte();
                payloadSize += b2;
            } while (b2 == 255);
            if (Eia608Parser.isSeiMessageEia608(payloadType, payloadSize, seiBuffer)) {
                this.output.sampleData(seiBuffer, payloadSize);
                this.output.sampleMetadata(pesTimeUs, 1, payloadSize, 0, null);
            } else {
                seiBuffer.skipBytes(payloadSize);
            }
        }
    }
}
