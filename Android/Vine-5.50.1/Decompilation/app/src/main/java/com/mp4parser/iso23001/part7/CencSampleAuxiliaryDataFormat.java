package com.mp4parser.iso23001.part7;

import com.coremedia.iso.Hex;
import java.math.BigInteger;
import java.util.Arrays;

/* loaded from: classes2.dex */
public class CencSampleAuxiliaryDataFormat {
    public byte[] iv = new byte[0];
    public Pair[] pairs = null;

    public interface Pair {
        int clear();

        long encrypted();
    }

    public int getSize() {
        int size = this.iv.length;
        if (this.pairs != null && this.pairs.length > 0) {
            return size + 2 + (this.pairs.length * 6);
        }
        return size;
    }

    public Pair createPair(int clear, long encrypted) {
        if (clear <= 127) {
            if (encrypted <= 127) {
                return new ByteBytePair(clear, encrypted);
            }
            if (encrypted <= 32767) {
                return new ByteShortPair(clear, encrypted);
            }
            if (encrypted <= 2147483647L) {
                return new ByteIntPair(clear, encrypted);
            }
            return new ByteLongPair(clear, encrypted);
        }
        if (clear <= 32767) {
            if (encrypted <= 127) {
                return new ShortBytePair(clear, encrypted);
            }
            if (encrypted <= 32767) {
                return new ShortShortPair(clear, encrypted);
            }
            if (encrypted <= 2147483647L) {
                return new ShortIntPair(clear, encrypted);
            }
            return new ShortLongPair(clear, encrypted);
        }
        if (encrypted <= 127) {
            return new IntBytePair(clear, encrypted);
        }
        if (encrypted <= 32767) {
            return new IntShortPair(clear, encrypted);
        }
        if (encrypted <= 2147483647L) {
            return new IntIntPair(clear, encrypted);
        }
        return new IntLongPair(clear, encrypted);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CencSampleAuxiliaryDataFormat entry = (CencSampleAuxiliaryDataFormat) o;
        if (!new BigInteger(this.iv).equals(new BigInteger(entry.iv))) {
            return false;
        }
        if (this.pairs != null) {
            if (Arrays.equals(this.pairs, entry.pairs)) {
                return true;
            }
        } else if (entry.pairs == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this.iv != null ? Arrays.hashCode(this.iv) : 0;
        return (result * 31) + (this.pairs != null ? Arrays.hashCode(this.pairs) : 0);
    }

    public String toString() {
        return "Entry{iv=" + Hex.encodeHex(this.iv) + ", pairs=" + Arrays.toString(this.pairs) + '}';
    }

    private class ByteBytePair extends AbstractPair {
        private byte clear;
        private byte encrypted;

        public ByteBytePair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (byte) clear;
            this.encrypted = (byte) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ByteShortPair extends AbstractPair {
        private byte clear;
        private short encrypted;

        public ByteShortPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (byte) clear;
            this.encrypted = (short) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ByteIntPair extends AbstractPair {
        private byte clear;
        private int encrypted;

        public ByteIntPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (byte) clear;
            this.encrypted = (int) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ByteLongPair extends AbstractPair {
        private byte clear;
        private long encrypted;

        public ByteLongPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (byte) clear;
            this.encrypted = encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ShortBytePair extends AbstractPair {
        private short clear;
        private byte encrypted;

        public ShortBytePair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (short) clear;
            this.encrypted = (byte) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ShortShortPair extends AbstractPair {
        private short clear;
        private short encrypted;

        public ShortShortPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (short) clear;
            this.encrypted = (short) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ShortIntPair extends AbstractPair {
        private short clear;
        private int encrypted;

        public ShortIntPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (short) clear;
            this.encrypted = (int) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class ShortLongPair extends AbstractPair {
        private short clear;
        private long encrypted;

        public ShortLongPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = (short) clear;
            this.encrypted = encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class IntBytePair extends AbstractPair {
        private int clear;
        private byte encrypted;

        public IntBytePair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = clear;
            this.encrypted = (byte) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class IntShortPair extends AbstractPair {
        private int clear;
        private short encrypted;

        public IntShortPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = clear;
            this.encrypted = (short) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class IntIntPair extends AbstractPair {
        private int clear;
        private int encrypted;

        public IntIntPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = clear;
            this.encrypted = (int) encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private class IntLongPair extends AbstractPair {
        private int clear;
        private long encrypted;

        public IntLongPair(int clear, long encrypted) {
            super(CencSampleAuxiliaryDataFormat.this, null);
            this.clear = clear;
            this.encrypted = encrypted;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public int clear() {
            return this.clear;
        }

        @Override // com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat.Pair
        public long encrypted() {
            return this.encrypted;
        }
    }

    private abstract class AbstractPair implements Pair {
        private AbstractPair() {
        }

        /* synthetic */ AbstractPair(CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat, AbstractPair abstractPair) {
            this();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pair pair = (Pair) o;
            return clear() == pair.clear() && encrypted() == pair.encrypted();
        }

        public String toString() {
            return "P(" + clear() + "|" + encrypted() + ")";
        }
    }
}
