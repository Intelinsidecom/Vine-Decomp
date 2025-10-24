package co.vine.android.cache;

/* loaded from: classes.dex */
public abstract class UrlResource<V> {
    public long cacheTime;
    public long nextRequestTime = 0;
    public final String url;
    public V value;

    public abstract int size();

    public UrlResource(String url) {
        this.url = url;
    }

    public boolean isValid() {
        return this.value != null && this.nextRequestTime == 0;
    }
}
