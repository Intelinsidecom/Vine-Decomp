package com.google.android.exoplayer.upstream;

import android.net.Uri;
import com.google.android.exoplayer.upstream.Loader;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public final class UriLoadable<T> implements Loader.Loadable {
    private final DataSpec dataSpec;
    private volatile boolean isCanceled;
    private final Parser<T> parser;
    private volatile T result;
    private final UriDataSource uriDataSource;

    public interface Parser<T> {
        T parse(String str, InputStream inputStream) throws IOException;
    }

    public UriLoadable(String url, UriDataSource uriDataSource, Parser<T> parser) {
        this.uriDataSource = uriDataSource;
        this.parser = parser;
        this.dataSpec = new DataSpec(Uri.parse(url), 1);
    }

    public final T getResult() {
        return this.result;
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Loadable
    public final void cancelLoad() {
        this.isCanceled = true;
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Loadable
    public final boolean isLoadCanceled() {
        return this.isCanceled;
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Loadable
    public final void load() throws InterruptedException, IOException {
        DataSourceInputStream inputStream = new DataSourceInputStream(this.uriDataSource, this.dataSpec);
        try {
            inputStream.open();
            this.result = this.parser.parse(this.uriDataSource.getUri(), inputStream);
        } finally {
            inputStream.close();
        }
    }
}
