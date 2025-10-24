package io.realm;

/* loaded from: classes.dex */
public enum Sort {
    ASCENDING(true),
    DESCENDING(false);

    private final boolean value;

    Sort(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }
}
