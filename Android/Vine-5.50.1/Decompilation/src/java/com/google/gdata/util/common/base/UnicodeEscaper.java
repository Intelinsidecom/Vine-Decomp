package com.google.gdata.util.common.base;

/* loaded from: classes.dex */
public abstract class UnicodeEscaper {
    private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() { // from class: com.google.gdata.util.common.base.UnicodeEscaper.2
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public char[] initialValue() {
            return new char[1024];
        }
    };

    protected abstract char[] escape(int i);

    protected int nextEscapeIndex(CharSequence csq, int start, int end) {
        int index = start;
        while (index < end) {
            int cp = codePointAt(csq, index, end);
            if (cp < 0 || escape(cp) != null) {
                break;
            }
            index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
        }
        return index;
    }

    public String escape(String string) {
        int end = string.length();
        int index = nextEscapeIndex(string, 0, end);
        return index == end ? string : escapeSlow(string, index);
    }

    protected final String escapeSlow(String s, int index) {
        int end = s.length();
        char[] dest = DEST_TL.get();
        int destIndex = 0;
        int unescapedChunkStart = 0;
        while (index < end) {
            int cp = codePointAt(s, index, end);
            if (cp < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            char[] escaped = escape(cp);
            if (escaped != null) {
                int charsSkipped = index - unescapedChunkStart;
                int sizeNeeded = destIndex + charsSkipped + escaped.length;
                if (dest.length < sizeNeeded) {
                    int destLength = (end - index) + sizeNeeded + 32;
                    dest = growBuffer(dest, destIndex, destLength);
                }
                if (charsSkipped > 0) {
                    s.getChars(unescapedChunkStart, index, dest, destIndex);
                    destIndex += charsSkipped;
                }
                if (escaped.length > 0) {
                    System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
                    destIndex += escaped.length;
                }
            }
            unescapedChunkStart = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
            index = nextEscapeIndex(s, unescapedChunkStart, end);
        }
        int charsSkipped2 = end - unescapedChunkStart;
        if (charsSkipped2 > 0) {
            int endIndex = destIndex + charsSkipped2;
            if (dest.length < endIndex) {
                dest = growBuffer(dest, destIndex, endIndex);
            }
            s.getChars(unescapedChunkStart, end, dest, destIndex);
            destIndex = endIndex;
        }
        return new String(dest, 0, destIndex);
    }

    protected static final int codePointAt(CharSequence seq, int index, int end) {
        if (index < end) {
            int index2 = index + 1;
            char c1 = seq.charAt(index);
            if (c1 >= 55296 && c1 <= 57343) {
                if (c1 <= 56319) {
                    if (index2 == end) {
                        return -c1;
                    }
                    char c2 = seq.charAt(index2);
                    if (Character.isLowSurrogate(c2)) {
                        return Character.toCodePoint(c1, c2);
                    }
                    throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + ((int) c2) + " at index " + index2);
                }
                throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + ((int) c1) + " at index " + (index2 - 1));
            }
            return c1;
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static final char[] growBuffer(char[] dest, int index, int size) {
        char[] copy = new char[size];
        if (index > 0) {
            System.arraycopy(dest, 0, copy, 0, index);
        }
        return copy;
    }
}
