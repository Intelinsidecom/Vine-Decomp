package com.google.android.exoplayer.extractor;

/* loaded from: classes.dex */
public interface SeekMap {
    public static final SeekMap UNSEEKABLE = new SeekMap() { // from class: com.google.android.exoplayer.extractor.SeekMap.1
        @Override // com.google.android.exoplayer.extractor.SeekMap
        public boolean isSeekable() {
            return false;
        }

        @Override // com.google.android.exoplayer.extractor.SeekMap
        public long getPosition(long timeUs) {
            return 0L;
        }
    };

    long getPosition(long j);

    boolean isSeekable();
}
