package com.facebook.internal;

import android.net.Uri;
import com.facebook.internal.FileLruCache;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/* loaded from: classes2.dex */
class UrlRedirectCache {
    private static volatile FileLruCache urlRedirectCache;
    static final String TAG = UrlRedirectCache.class.getSimpleName();
    private static final String REDIRECT_CONTENT_TAG = TAG + "_Redirect";

    UrlRedirectCache() {
    }

    static synchronized FileLruCache getCache() throws IOException {
        if (urlRedirectCache == null) {
            urlRedirectCache = new FileLruCache(TAG, new FileLruCache.Limits());
        }
        return urlRedirectCache;
    }

    static Uri getRedirectedUri(Uri uri) throws Throwable {
        InputStreamReader reader;
        Uri uri2 = null;
        if (uri != null) {
            String uriString = uri.toString();
            InputStreamReader reader2 = null;
            try {
                FileLruCache cache = getCache();
                boolean redirectExists = false;
                while (true) {
                    try {
                        reader = reader2;
                        InputStream stream = cache.get(uriString, REDIRECT_CONTENT_TAG);
                        if (stream == null) {
                            break;
                        }
                        redirectExists = true;
                        reader2 = new InputStreamReader(stream);
                        char[] buffer = new char[128];
                        StringBuilder urlBuilder = new StringBuilder();
                        while (true) {
                            int bufferLength = reader2.read(buffer, 0, buffer.length);
                            if (bufferLength <= 0) {
                                break;
                            }
                            urlBuilder.append(buffer, 0, bufferLength);
                        }
                        Utility.closeQuietly(reader2);
                        uriString = urlBuilder.toString();
                    } catch (IOException e) {
                        reader2 = reader;
                        Utility.closeQuietly(reader2);
                        return uri2;
                    } catch (Throwable th) {
                        th = th;
                        reader2 = reader;
                        Utility.closeQuietly(reader2);
                        throw th;
                    }
                }
                if (redirectExists) {
                    uri2 = Uri.parse(uriString);
                    Utility.closeQuietly(reader);
                } else {
                    Utility.closeQuietly(reader);
                }
            } catch (IOException e2) {
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return uri2;
    }

    static void cacheUriRedirect(Uri fromUri, Uri toUri) throws IOException {
        if (fromUri != null && toUri != null) {
            OutputStream redirectStream = null;
            try {
                FileLruCache cache = getCache();
                redirectStream = cache.openPutStream(fromUri.toString(), REDIRECT_CONTENT_TAG);
                redirectStream.write(toUri.toString().getBytes());
            } catch (IOException e) {
            } finally {
                Utility.closeQuietly(redirectStream);
            }
        }
    }
}
