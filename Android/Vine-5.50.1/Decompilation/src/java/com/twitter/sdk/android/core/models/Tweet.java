package com.twitter.sdk.android.core.models;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Tweet {

    @SerializedName("id")
    public final long id;

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Tweet)) {
            return false;
        }
        Tweet other = (Tweet) o;
        return this.id == other.id;
    }

    public int hashCode() {
        return (int) this.id;
    }
}
