package com.facebook.share.model;

import android.os.Bundle;
import android.os.Parcel;

/* loaded from: classes.dex */
public abstract class ShareMedia implements ShareModel {
    private final Bundle params;

    protected ShareMedia(Builder builder) {
        this.params = new Bundle(builder.params);
    }

    ShareMedia(Parcel in) {
        this.params = in.readBundle();
    }

    @Deprecated
    public Bundle getParameters() {
        return new Bundle(this.params);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.params);
    }

    public static abstract class Builder<M extends ShareMedia, B extends Builder> {
        private Bundle params = new Bundle();

        @Deprecated
        public B setParameters(Bundle parameters) {
            this.params.putAll(parameters);
            return this;
        }

        public B readFrom(M model) {
            return model == null ? this : setParameters(model.getParameters());
        }
    }
}
