package com.google.common.android.base.android;

/* loaded from: classes.dex */
final class Present<T> extends Optional<T> {
    private static final long serialVersionUID = 0;
    private final T reference;

    Present(T reference) {
        this.reference = reference;
    }

    @Override // com.google.common.android.base.android.Optional
    public boolean isPresent() {
        return true;
    }

    @Override // com.google.common.android.base.android.Optional
    public T get() {
        return this.reference;
    }

    @Override // com.google.common.android.base.android.Optional
    public T orNull() {
        return this.reference;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Present)) {
            return false;
        }
        Present<?> other = (Present) object;
        return this.reference.equals(other.reference);
    }

    public int hashCode() {
        return 1502476572 + this.reference.hashCode();
    }

    public String toString() {
        return "Optional.of(" + this.reference + ")";
    }
}
