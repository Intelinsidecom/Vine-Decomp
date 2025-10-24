package com.google.android.exoplayer.extractor;

import java.io.IOException;

/* loaded from: classes.dex */
public interface Extractor {
    void init(ExtractorOutput extractorOutput);

    int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws InterruptedException, IOException;

    void seek();

    boolean sniff(ExtractorInput extractorInput) throws InterruptedException, IOException;
}
