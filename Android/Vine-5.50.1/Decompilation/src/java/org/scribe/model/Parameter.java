package org.scribe.model;

import org.scribe.utils.OAuthEncoder;

/* loaded from: classes.dex */
public class Parameter implements Comparable<Parameter> {
    private final String key;
    private final String value;

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String asUrlEncodedPair() {
        return OAuthEncoder.encode(this.key).concat("=").concat(OAuthEncoder.encode(this.value));
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Parameter)) {
            return false;
        }
        Parameter otherParam = (Parameter) other;
        return otherParam.key.equals(this.key) && otherParam.value.equals(this.value);
    }

    public int hashCode() {
        return this.key.hashCode() + this.value.hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(Parameter parameter) {
        int keyDiff = this.key.compareTo(parameter.key);
        return keyDiff != 0 ? keyDiff : this.value.compareTo(parameter.value);
    }
}
