package com.google.android.exoplayer.util;

/* loaded from: classes.dex */
public final class NalUnitUtil {
    public static final byte[] NAL_START_CODE = {0, 0, 0, 1};
    public static final float[] ASPECT_RATIO_IDC_VALUES = {1.0f, 1.0f, 1.0909091f, 0.90909094f, 1.4545455f, 1.2121212f, 2.1818182f, 1.8181819f, 2.909091f, 2.4242425f, 1.6363636f, 1.3636364f, 1.939394f, 1.6161616f, 1.3333334f, 1.5f, 2.0f};
    private static final Object scratchEscapePositionsLock = new Object();
    private static int[] scratchEscapePositions = new int[10];

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:25:0x0056
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public static int unescapeStream(byte[] r14, int r15) {
        /*
            java.lang.Object r12 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositionsLock
            monitor-enter(r12)
            r4 = 0
            r6 = 0
            r7 = r6
        L6:
            if (r4 >= r15) goto L2a
            int r4 = findNextUnescapeIndex(r14, r4, r15)     // Catch: java.lang.Throwable -> L52
            if (r4 >= r15) goto L6
            int[] r11 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions     // Catch: java.lang.Throwable -> L52
            int r11 = r11.length     // Catch: java.lang.Throwable -> L52
            if (r11 > r7) goto L20
            int[] r11 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions     // Catch: java.lang.Throwable -> L52
            int[] r13 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions     // Catch: java.lang.Throwable -> L52
            int r13 = r13.length     // Catch: java.lang.Throwable -> L52
            int r13 = r13 * 2
            int[] r11 = java.util.Arrays.copyOf(r11, r13)     // Catch: java.lang.Throwable -> L52
            com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions = r11     // Catch: java.lang.Throwable -> L52
        L20:
            int[] r11 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions     // Catch: java.lang.Throwable -> L52
            int r6 = r7 + 1
            r11[r7] = r4     // Catch: java.lang.Throwable -> L56
            int r4 = r4 + 3
            r7 = r6
            goto L6
        L2a:
            int r8 = r15 - r7
            r1 = 0
            r9 = 0
            r2 = 0
        L2f:
            if (r2 >= r7) goto L4b
            int[] r11 = com.google.android.exoplayer.util.NalUnitUtil.scratchEscapePositions     // Catch: java.lang.Throwable -> L52
            r3 = r11[r2]     // Catch: java.lang.Throwable -> L52
            int r0 = r3 - r1
            java.lang.System.arraycopy(r14, r1, r14, r9, r0)     // Catch: java.lang.Throwable -> L52
            int r9 = r9 + r0
            int r10 = r9 + 1
            r11 = 0
            r14[r9] = r11     // Catch: java.lang.Throwable -> L52
            int r9 = r10 + 1
            r11 = 0
            r14[r10] = r11     // Catch: java.lang.Throwable -> L52
            int r11 = r0 + 3
            int r1 = r1 + r11
            int r2 = r2 + 1
            goto L2f
        L4b:
            int r5 = r8 - r9
            java.lang.System.arraycopy(r14, r1, r14, r9, r5)     // Catch: java.lang.Throwable -> L52
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L52
            return r8
        L52:
            r11 = move-exception
            r6 = r7
        L54:
            monitor-exit(r12)     // Catch: java.lang.Throwable -> L56
            throw r11
        L56:
            r11 = move-exception
            goto L54
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.util.NalUnitUtil.unescapeStream(byte[], int):int");
    }

    public static byte[] parseChildNalUnit(ParsableByteArray atom) {
        int length = atom.readUnsignedShort();
        int offset = atom.getPosition();
        atom.skipBytes(length);
        return CodecSpecificDataUtil.buildNalUnit(atom.data, offset, length);
    }

    public static int getNalUnitType(byte[] data, int offset) {
        return data[offset + 3] & 31;
    }

    public static int getH265NalUnitType(byte[] data, int offset) {
        return (data[offset + 3] & 126) >> 1;
    }

    public static int findNalUnit(byte[] data, int startOffset, int endOffset, boolean[] prefixFlags) {
        int length = endOffset - startOffset;
        Assertions.checkState(length >= 0);
        if (length != 0) {
            if (prefixFlags != null) {
                if (prefixFlags[0]) {
                    clearPrefixFlags(prefixFlags);
                    return startOffset - 3;
                }
                if (length > 1 && prefixFlags[1] && data[startOffset] == 1) {
                    clearPrefixFlags(prefixFlags);
                    return startOffset - 2;
                }
                if (length > 2 && prefixFlags[2] && data[startOffset] == 0 && data[startOffset + 1] == 1) {
                    clearPrefixFlags(prefixFlags);
                    return startOffset - 1;
                }
            }
            int limit = endOffset - 1;
            int i = startOffset + 2;
            while (i < limit) {
                if ((data[i] & 254) == 0) {
                    if (data[i - 2] == 0 && data[i - 1] == 0 && data[i] == 1) {
                        if (prefixFlags != null) {
                            clearPrefixFlags(prefixFlags);
                        }
                        return i - 2;
                    }
                    i -= 2;
                }
                i += 3;
            }
            if (prefixFlags != null) {
                prefixFlags[0] = length > 2 ? data[endOffset + (-3)] == 0 && data[endOffset + (-2)] == 0 && data[endOffset + (-1)] == 1 : length == 2 ? prefixFlags[2] && data[endOffset + (-2)] == 0 && data[endOffset + (-1)] == 1 : prefixFlags[1] && data[endOffset + (-1)] == 1;
                prefixFlags[1] = length > 1 ? data[endOffset + (-2)] == 0 && data[endOffset + (-1)] == 0 : prefixFlags[2] && data[endOffset + (-1)] == 0;
                prefixFlags[2] = data[endOffset + (-1)] == 0;
                return endOffset;
            }
            return endOffset;
        }
        return endOffset;
    }

    public static void clearPrefixFlags(boolean[] prefixFlags) {
        prefixFlags[0] = false;
        prefixFlags[1] = false;
        prefixFlags[2] = false;
    }

    private static int findNextUnescapeIndex(byte[] bytes, int offset, int limit) {
        for (int i = offset; i < limit - 2; i++) {
            if (bytes[i] == 0 && bytes[i + 1] == 0 && bytes[i + 2] == 3) {
                return i;
            }
        }
        return limit;
    }
}
