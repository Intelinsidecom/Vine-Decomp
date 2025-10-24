package com.facebook.share.model;

import android.os.Bundle;
import android.os.Parcel;
import com.facebook.share.model.ShareOpenGraphValueContainer;
import com.facebook.share.model.ShareOpenGraphValueContainer.Builder;
import java.util.Set;

/* loaded from: classes2.dex */
public abstract class ShareOpenGraphValueContainer<P extends ShareOpenGraphValueContainer, E extends Builder> implements ShareModel {
    private final Bundle bundle;

    protected ShareOpenGraphValueContainer(Builder<P, E> builder) {
        this.bundle = (Bundle) ((Builder) builder).bundle.clone();
    }

    ShareOpenGraphValueContainer(Parcel in) {
        this.bundle = in.readBundle(Builder.class.getClassLoader());
    }

    public Object get(String key) {
        return this.bundle.get(key);
    }

    public String getString(String key) {
        return this.bundle.getString(key);
    }

    public Bundle getBundle() {
        return (Bundle) this.bundle.clone();
    }

    public Set<String> keySet() {
        return this.bundle.keySet();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeBundle(this.bundle);
    }

    public static abstract class Builder<P extends ShareOpenGraphValueContainer, E extends Builder> {
        private Bundle bundle = new Bundle();

        public E putString(String key, String value) {
            this.bundle.putString(key, value);
            return this;
        }

        public E readFrom(P model) {
            if (model != null) {
                this.bundle.putAll(model.getBundle());
            }
            return this;
        }
    }
}
