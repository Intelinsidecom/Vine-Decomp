package io.realm.internal;

import java.nio.ByteBuffer;
import java.util.Arrays;

/* loaded from: classes.dex */
public class Mixed {
    static final /* synthetic */ boolean $assertionsDisabled;
    private Object value;

    static {
        $assertionsDisabled = !Mixed.class.desiredAssertionStatus();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Mixed mixed = (Mixed) obj;
        if (this.value.getClass() != mixed.value.getClass()) {
            return false;
        }
        if (this.value instanceof byte[]) {
            return Arrays.equals((byte[]) this.value, (byte[]) mixed.value);
        }
        if (this.value instanceof ByteBuffer) {
            return ((ByteBuffer) this.value).compareTo((ByteBuffer) mixed.value) == 0;
        }
        return this.value.equals(mixed.value);
    }

    public int hashCode() {
        return this.value instanceof byte[] ? Arrays.hashCode((byte[]) this.value) : this.value.hashCode();
    }
}
