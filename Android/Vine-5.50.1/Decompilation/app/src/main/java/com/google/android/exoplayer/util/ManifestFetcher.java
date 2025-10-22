package com.google.android.exoplayer.util;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.exoplayer.upstream.Loader;
import com.google.android.exoplayer.upstream.UriDataSource;
import com.google.android.exoplayer.upstream.UriLoadable;
import java.io.IOException;
import java.util.concurrent.CancellationException;

/* loaded from: classes.dex */
public class ManifestFetcher<T> implements Loader.Callback {
    private long currentLoadStartTimestamp;
    private UriLoadable<T> currentLoadable;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private ManifestIOException loadException;
    private int loadExceptionCount;
    private long loadExceptionTimestamp;
    private volatile T manifest;
    private volatile long manifestLoadCompleteTimestamp;
    private volatile long manifestLoadStartTimestamp;
    volatile String manifestUri;
    private final UriLoadable.Parser<T> parser;
    private final UriDataSource uriDataSource;

    public interface EventListener {
        void onManifestError(IOException iOException);

        void onManifestRefreshed();
    }

    public interface ManifestCallback<T> {
        void onSingleManifest(T t);

        void onSingleManifestError(IOException iOException);
    }

    public interface RedirectingManifest {
        String getNextManifestUri();
    }

    public static final class ManifestIOException extends IOException {
        public ManifestIOException(Throwable cause) {
            super(cause);
        }
    }

    public ManifestFetcher(String manifestUri, UriDataSource uriDataSource, UriLoadable.Parser<T> parser) {
        this(manifestUri, uriDataSource, parser, null, null);
    }

    public ManifestFetcher(String manifestUri, UriDataSource uriDataSource, UriLoadable.Parser<T> parser, Handler eventHandler, EventListener eventListener) {
        this.parser = parser;
        this.manifestUri = manifestUri;
        this.uriDataSource = uriDataSource;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
    }

    public void singleLoad(Looper callbackLooper, ManifestCallback<T> callback) {
        ManifestFetcher<T>.SingleFetchHelper fetchHelper = new SingleFetchHelper(new UriLoadable(this.manifestUri, this.uriDataSource, this.parser), callbackLooper, callback);
        fetchHelper.startLoading();
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadCompleted(Loader.Loadable loadable) {
        if (this.currentLoadable == loadable) {
            this.manifest = this.currentLoadable.getResult();
            this.manifestLoadStartTimestamp = this.currentLoadStartTimestamp;
            this.manifestLoadCompleteTimestamp = android.os.SystemClock.elapsedRealtime();
            this.loadExceptionCount = 0;
            this.loadException = null;
            if (this.manifest instanceof RedirectingManifest) {
                RedirectingManifest redirectingManifest = (RedirectingManifest) this.manifest;
                String nextLocation = redirectingManifest.getNextManifestUri();
                if (!TextUtils.isEmpty(nextLocation)) {
                    this.manifestUri = nextLocation;
                }
            }
            notifyManifestRefreshed();
        }
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadCanceled(Loader.Loadable loadable) {
    }

    @Override // com.google.android.exoplayer.upstream.Loader.Callback
    public void onLoadError(Loader.Loadable loadable, IOException exception) {
        if (this.currentLoadable == loadable) {
            this.loadExceptionCount++;
            this.loadExceptionTimestamp = android.os.SystemClock.elapsedRealtime();
            this.loadException = new ManifestIOException(exception);
            notifyManifestError(this.loadException);
        }
    }

    void onSingleFetchCompleted(T result, long loadStartTimestamp) {
        this.manifest = result;
        this.manifestLoadStartTimestamp = loadStartTimestamp;
        this.manifestLoadCompleteTimestamp = android.os.SystemClock.elapsedRealtime();
    }

    private void notifyManifestRefreshed() {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.util.ManifestFetcher.2
                @Override // java.lang.Runnable
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestRefreshed();
                }
            });
        }
    }

    private void notifyManifestError(final IOException e) {
        if (this.eventHandler != null && this.eventListener != null) {
            this.eventHandler.post(new Runnable() { // from class: com.google.android.exoplayer.util.ManifestFetcher.3
                @Override // java.lang.Runnable
                public void run() {
                    ManifestFetcher.this.eventListener.onManifestError(e);
                }
            });
        }
    }

    private class SingleFetchHelper implements Loader.Callback {
        private final Looper callbackLooper;
        private long loadStartTimestamp;
        private final UriLoadable<T> singleUseLoadable;
        private final Loader singleUseLoader = new Loader("manifestLoader:single");
        private final ManifestCallback<T> wrappedCallback;

        public SingleFetchHelper(UriLoadable<T> singleUseLoadable, Looper callbackLooper, ManifestCallback<T> wrappedCallback) {
            this.singleUseLoadable = singleUseLoadable;
            this.callbackLooper = callbackLooper;
            this.wrappedCallback = wrappedCallback;
        }

        public void startLoading() {
            this.loadStartTimestamp = android.os.SystemClock.elapsedRealtime();
            this.singleUseLoader.startLoading(this.callbackLooper, this.singleUseLoadable, this);
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Callback
        public void onLoadCompleted(Loader.Loadable loadable) {
            try {
                T result = this.singleUseLoadable.getResult();
                ManifestFetcher.this.onSingleFetchCompleted(result, this.loadStartTimestamp);
                this.wrappedCallback.onSingleManifest(result);
            } finally {
                releaseLoader();
            }
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Callback
        public void onLoadCanceled(Loader.Loadable loadable) {
            try {
                IOException exception = new ManifestIOException(new CancellationException());
                this.wrappedCallback.onSingleManifestError(exception);
            } finally {
                releaseLoader();
            }
        }

        @Override // com.google.android.exoplayer.upstream.Loader.Callback
        public void onLoadError(Loader.Loadable loadable, IOException exception) {
            try {
                this.wrappedCallback.onSingleManifestError(exception);
            } finally {
                releaseLoader();
            }
        }

        private void releaseLoader() {
            this.singleUseLoader.release();
        }
    }
}
