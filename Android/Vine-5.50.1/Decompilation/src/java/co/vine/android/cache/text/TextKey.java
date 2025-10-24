package co.vine.android.cache.text;

import co.vine.android.cache.CacheKey;

/* loaded from: classes.dex */
public class TextKey extends CacheKey {
    public final String url;

    public TextKey(String url) {
        this.url = url;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.url != null) {
            return this.url.equals(((TextKey) o).url);
        }
        return ((TextKey) o).url == null;
    }

    @Override // co.vine.android.cache.CacheKey
    public String toString() {
        return super.toString() + "\n" + this.url;
    }

    public int hashCode() {
        if (this.url != null) {
            return this.url.hashCode() * 31;
        }
        return 0;
    }
}
