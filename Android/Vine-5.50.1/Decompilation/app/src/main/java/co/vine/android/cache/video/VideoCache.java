package co.vine.android.cache.video;

import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import co.vine.android.cache.UrlResourceCache;
import co.vine.android.network.HttpResult;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import com.vandalsoftware.io.DiskLruCache;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class VideoCache<T> extends UrlResourceCache<VideoKey, UrlVideo, File, T> {
    private static DiskLruCache sDiskCache;
    private ArrayList<VideoDownloadDurationListener> mDownloadDurationListeners;
    private final ArrayList<VideoListener> mListeners;
    private static final int[] LOCK = new int[0];
    private static boolean sIsCacheInitialized = false;
    private static final int[] averages = new int[5];
    private static int averagePosition = 0;
    private static int numberRecorded = 0;
    private static final int[] AVG_LOCK = new int[0];

    public interface VideoDownloadDurationListener {
        void onVideoDownloaded(double d, double d2);
    }

    private static void reportAverageSpeed(int speed) {
        synchronized (AVG_LOCK) {
            SLog.d("Latest download speed: {}", Integer.valueOf(speed));
            int nextPos = averagePosition % 5;
            averages[nextPos] = speed;
            averagePosition = (nextPos + 1) % 5;
            numberRecorded++;
        }
    }

    public static int getCurrentAverageSpeed() {
        int i;
        synchronized (AVG_LOCK) {
            int total = 0;
            if (numberRecorded == 0) {
                i = Integer.MAX_VALUE;
            } else {
                int count = Math.min(numberRecorded, 5);
                for (int i2 = 0; i2 < count; i2++) {
                    total += averages[i2];
                }
                i = total / count;
            }
        }
        return i;
    }

    public VideoCache(Context context, T networkContext, NetworkOperationFactory<T> factory) {
        super(context, 0, networkContext, factory);
        this.mDownloadDurationListeners = new ArrayList<>();
        this.mListeners = new ArrayList<>();
    }

    public void addDownloadDurationListener(VideoDownloadDurationListener listener) {
        this.mDownloadDurationListeners.add(listener);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public String getFile(long ownerId, VideoKey key, boolean peekOnly) {
        UrlVideo video = get(ownerId, key, key.url, true, peekOnly);
        if (video != null) {
            return ((File) video.value).getAbsolutePath();
        }
        return null;
    }

    private static DiskLruCache getCacheInstance(Context context) {
        DiskLruCache diskLruCache;
        synchronized (LOCK) {
            if (!sIsCacheInitialized) {
                DiskLruCache diskCache = null;
                try {
                    File cacheDir = CommonUtil.getExternalCacheDir(context);
                    if (cacheDir != null) {
                        diskCache = prepareCache(cacheDir, 157286400L, false);
                    } else if (Build.VERSION.SDK_INT < 14) {
                        diskCache = prepareCache(context.getDir("video_cache", 1), 52428800L, false);
                    } else {
                        diskCache = prepareCache(context.getCacheDir(), 52428800L, false);
                    }
                } catch (IOException e) {
                }
                sDiskCache = diskCache;
                sIsCacheInitialized = true;
            }
            diskLruCache = sDiskCache;
        }
        return diskLruCache;
    }

    public static void invalidateCache() {
        synchronized (LOCK) {
            sDiskCache = null;
            sIsCacheInitialized = false;
        }
    }

    private static DiskLruCache prepareCache(File dir, long absMax, boolean appendCacheName) throws IOException {
        long available = -1;
        try {
            StatFs stat = new StatFs(dir.getPath());
            if (Build.VERSION.SDK_INT >= 18) {
                try {
                    available = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
                } catch (NoSuchMethodError e) {
                }
            }
            if (available < 0) {
                available = stat.getBlockSize() * stat.getAvailableBlocks();
            }
        } catch (Exception e2) {
            CrashUtil.log("Failed to statf, but it is ok, we will get the aval size in other ways on this phone.");
        }
        if (available < 0) {
            available = dir.getUsableSpace();
        }
        long maxSize = (available <= 0 || available > absMax) ? absMax : available / 10;
        SLog.d("Preparing a DiskLruCache with absMax={}, available={}, maxSize={}", Long.valueOf(absMax), Long.valueOf(available), Long.valueOf(maxSize));
        if (appendCacheName) {
            dir = new File(dir, "video_cache");
        }
        return DiskLruCache.open(dir, 2, 2, maxSize);
    }

    public void addListener(VideoListener listener) {
        this.mListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.cache.UrlResourceCache
    public UrlVideo loadResource(long ownerId, VideoKey key, String url) throws NoSuchAlgorithmException {
        UrlVideo urlVideoInstantiateResource = null;
        String diskCacheKey = CommonUtil.md5Digest(url);
        if (diskCacheKey != null) {
            DiskLruCache.Snapshot snapshot = null;
            synchronized (LOCK) {
                DiskLruCache diskCache = getCacheInstance(this.mContext);
                if (diskCache != null && !diskCache.isClosed()) {
                    try {
                        snapshot = diskCache.get(diskCacheKey);
                        if (snapshot != null) {
                            urlVideoInstantiateResource = instantiateResource(key, url, new File(snapshot.getPath(0)));
                        }
                    } catch (IOException e) {
                    } finally {
                        CommonUtil.closeSilently(snapshot);
                    }
                }
            }
        }
        return urlVideoInstantiateResource;
    }

    public void prepopulateVideoCacheForUrl(long ownerId, VideoKey key, String url, InputStream is) throws NoSuchAlgorithmException {
        saveResource(ownerId, key, url, is, false);
        instantiateResource(key, url, (File) null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.cache.UrlResourceCache
    public UrlVideo saveResource(long ownerId, VideoKey key, String url, InputStream is, boolean reportSpeed) throws NoSuchAlgorithmException {
        String diskCacheKey = CommonUtil.md5Digest(url);
        DiskLruCache.Editor editor = null;
        UrlVideo ret = null;
        synchronized (LOCK) {
            DiskLruCache diskCache = getCacheInstance(this.mContext);
            if (diskCache != null && !diskCache.isClosed()) {
                boolean success = false;
                try {
                    editor = diskCache.edit(diskCacheKey);
                    if (editor != null) {
                        long startTime = System.currentTimeMillis();
                        int bytesLen = CommonUtil.readFullyWriteTo(is, editor.newOutputStream(0), 4096);
                        success = bytesLen > 0;
                        double time = (System.currentTimeMillis() - startTime) / 1000.0d;
                        Iterator<VideoDownloadDurationListener> it = this.mDownloadDurationListeners.iterator();
                        while (it.hasNext()) {
                            VideoDownloadDurationListener listener = it.next();
                            listener.onVideoDownloaded(startTime / 1000.0d, time);
                        }
                        SLog.d("Bytes {} Download time: {}.", Integer.valueOf(bytesLen), Double.valueOf(time));
                        if (bytesLen > 0 && reportSpeed) {
                            reportAverageSpeed((int) Math.round((bytesLen / 1024) / Math.max(1.0d, time)));
                        }
                        ByteBuffer buffer = ByteBuffer.allocate(8);
                        buffer.putLong(System.currentTimeMillis());
                        editor.newOutputStream(1).write(buffer.array());
                    }
                    if (editor != null) {
                        try {
                            if (success) {
                                editor.commit();
                                diskCache.flush();
                                File f = new File(diskCache.get(diskCacheKey, false).getPath(0));
                                ret = new UrlVideo(url, f);
                            } else {
                                editor.abort();
                                ret = obtainResource(key, url, is);
                                CrashUtil.log("Failed to save a video to DiskLruCache");
                            }
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    if (editor != null) {
                        try {
                            if (0 != 0) {
                                editor.commit();
                                diskCache.flush();
                                File f2 = new File(diskCache.get(diskCacheKey, false).getPath(0));
                                ret = new UrlVideo(url, f2);
                            } else {
                                editor.abort();
                                ret = obtainResource(key, url, is);
                                CrashUtil.log("Failed to save a video to DiskLruCache");
                            }
                        } catch (IOException e3) {
                        }
                    }
                } catch (Throwable th) {
                    if (editor != null) {
                        try {
                            if (success) {
                                editor.commit();
                                diskCache.flush();
                                File f3 = new File(diskCache.get(diskCacheKey, false).getPath(0));
                                new UrlVideo(url, f3);
                            } else {
                                editor.abort();
                                obtainResource(key, url, is);
                                CrashUtil.log("Failed to save a video to DiskLruCache");
                            }
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } else {
                ret = obtainResource(key, url, is);
                CrashUtil.log("Videos DiskLruCache could not be opened");
            }
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.cache.UrlResourceCache
    public UrlVideo instantiateResource(VideoKey key, String url, File file) {
        return new UrlVideo(url, file);
    }

    @Override // co.vine.android.cache.UrlResourceCache
    protected void onResourceLoaded(HashMap<VideoKey, UrlVideo> resources) {
        for (int i = this.mListeners.size() - 1; i >= 0; i--) {
            this.mListeners.get(i).onVideoPathObtained(this, resources);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.cache.UrlResourceCache
    public void onResourceError(VideoKey key, HttpResult result) {
        for (int i = this.mListeners.size() - 1; i >= 0; i--) {
            this.mListeners.get(i).onVideoPathError(this, key, result);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // co.vine.android.cache.UrlResourceCache
    public UrlVideo obtainResource(VideoKey key, String url, InputStream is) throws Throwable {
        File cacheDir = CommonUtil.getCacheDir(this.mContext);
        if (cacheDir != null) {
            String md5 = CommonUtil.md5Digest(url);
            if (md5 == null) {
                md5 = CommonUtil.randomString(10);
            }
            File f = new File(cacheDir, md5);
            FileOutputStream out = null;
            try {
                try {
                    long startTime = System.currentTimeMillis();
                    FileOutputStream out2 = new FileOutputStream(f);
                    try {
                        int bytesLen = CommonUtil.readFullyWriteTo(is, out2, 4096);
                        if (bytesLen != 0) {
                            long time = (System.currentTimeMillis() - startTime) / 1000;
                            if (bytesLen > 0) {
                                SLog.d("Download time: {}.", Long.valueOf(time));
                                reportAverageSpeed((int) Math.round((bytesLen / 1024) / Math.max(1L, time)));
                            } else {
                                SLog.d("Invalid bytesLen or time: {}", Integer.valueOf(bytesLen));
                            }
                            out2.flush();
                            CommonUtil.closeSilently(out2);
                            return new UrlVideo(url, f);
                        }
                        CommonUtil.closeSilently(out2);
                        return null;
                    } catch (IOException e) {
                        e = e;
                        out = out2;
                        SLog.w("Failed to download file.", (Throwable) e);
                        CommonUtil.closeSilently(out);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        out = out2;
                        CommonUtil.closeSilently(out);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e2) {
                e = e2;
            }
        } else {
            SLog.w("cache dir is null.");
            return null;
        }
    }

    public static long getVideoCacheSize() {
        if (sDiskCache == null) {
            return 0L;
        }
        return sDiskCache.size();
    }
}
