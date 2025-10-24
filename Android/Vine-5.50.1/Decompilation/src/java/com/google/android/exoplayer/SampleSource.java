package com.google.android.exoplayer;

import java.io.IOException;

/* loaded from: classes.dex */
public interface SampleSource {

    public interface SampleSourceReader {
        boolean continueBuffering(int i, long j);

        void disable(int i);

        void enable(int i, long j);

        long getBufferedPositionUs();

        MediaFormat getFormat(int i);

        int getTrackCount();

        void maybeThrowError() throws IOException;

        boolean prepare(long j);

        int readData(int i, long j, MediaFormatHolder mediaFormatHolder, SampleHolder sampleHolder);

        long readDiscontinuity(int i);

        void release();

        void seekToUs(long j);
    }

    SampleSourceReader register();
}
