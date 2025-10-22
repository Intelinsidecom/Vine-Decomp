package com.googlecode.mp4parser.authoring;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/* loaded from: classes2.dex */
public interface Sample {
    long getSize();

    void writeTo(WritableByteChannel writableByteChannel) throws IOException;
}
