package com.google.android.exoplayer.extractor.webm;

import com.google.android.exoplayer.extractor.ExtractorInput;
import java.io.IOException;

/* loaded from: classes.dex */
interface EbmlReader {
    void init(EbmlReaderOutput ebmlReaderOutput);

    boolean read(ExtractorInput extractorInput) throws InterruptedException, IOException;

    void reset();
}
