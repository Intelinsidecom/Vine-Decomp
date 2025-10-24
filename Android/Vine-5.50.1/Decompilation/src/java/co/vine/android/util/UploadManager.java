package co.vine.android.util;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import co.vine.android.api.PostInfo;
import co.vine.android.api.VineSource;
import co.vine.android.api.VineUpload;
import co.vine.android.cache.CacheFactory;
import co.vine.android.cache.video.VideoCache;
import co.vine.android.cache.video.VideoKey;
import co.vine.android.client.AppController;
import co.vine.android.client.SessionManager;
import co.vine.android.client.VineAPI;
import co.vine.android.provider.VineUploadProvider;
import co.vine.android.provider.VineUploads;
import co.vine.android.provider.VineUploadsDatabaseSQL;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.RecordSessionVersion;
import co.vine.android.service.UploadProgressListener;
import co.vine.android.service.VineUploadService;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
public class UploadManager {
    private final MediaUtility mMediaUtility;

    public UploadManager(Context context) {
        this.mMediaUtility = new MediaUtility(context);
    }

    public static void removeFromUploadQueue(Context context, String path) {
        context.startService(VineUploadService.getDiscardIntent(context, path));
    }

    public static RecordSessionVersion getVersionFromPath(String path) {
        RecordSessionVersion version = RecordSessionVersion.SW_MP4;
        String fn = new File(path).getName();
        if (fn.startsWith(RecordSessionVersion.HW.name())) {
            RecordSessionVersion version2 = RecordSessionVersion.HW;
            return version2;
        }
        if (fn.startsWith(RecordSessionVersion.SW_WEBM.name())) {
            RecordSessionVersion version3 = RecordSessionVersion.SW_WEBM;
            return version3;
        }
        return version;
    }

    public static String addToUploadQueue(Context context, String versionName, String videoPath, String thumbnailPath, String reference, String metadata, boolean isMessaging, long conversationId, ArrayList<VineSource> sources) throws Exception {
        File processDir;
        try {
            processDir = new RecordConfigUtils.RecordConfig(context).processDir;
        } catch (Exception e) {
            processDir = context.getCacheDir();
        }
        String fileName = versionName + "_" + System.currentTimeMillis();
        File videoFile = RecordConfigUtils.copyForUpload(processDir, videoPath, fileName);
        String path = videoFile.getPath();
        String thumbnailPath2 = RecordConfigUtils.copyForUpload(processDir, thumbnailPath, RecordConfigUtils.getThumbnailPath(fileName)).getPath();
        RecordConfigUtils.writeForUpload(processDir, RecordConfigUtils.getMetadataPath(fileName), metadata);
        if (sources != null && !sources.isEmpty()) {
            context.startService(VineUploadService.getUploadIntent(context, path, thumbnailPath2, reference, isMessaging, conversationId, sources));
        } else {
            context.startService(VineUploadService.getUploadIntent(context, path, thumbnailPath2, reference, isMessaging, conversationId));
        }
        return path;
    }

    public static Cursor getReferenceCursor(Context context, String reference) {
        if (TextUtils.isEmpty(reference)) {
            return null;
        }
        long ownerId = AppController.getInstance(context).getActiveSessionReadOnly().getUserId();
        Uri contentUri = getBaseUriBuilder().path("uploads/reference").appendEncodedPath(String.valueOf(ownerId)).appendQueryParameter("reference", reference).build();
        return context.getContentResolver().query(contentUri, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, null, null, null);
    }

    public static void addOrUpdateUpload(Context context, String path, String reference, boolean isPrivate, long conversationObjectId, long messageRowId) {
        long ownerId = AppController.getInstance(context).getActiveSessionReadOnly().getUserId();
        Uri contentUri = getBaseUriBuilder().path("uploads/upload").appendEncodedPath(String.valueOf(ownerId)).appendQueryParameter("path", path).build();
        Cursor c = context.getContentResolver().query(contentUri, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, null, null, null);
        ContentValues values = new ContentValues();
        if (c == null || c.getCount() < 1) {
            String thumbnailPath = RecordConfigUtils.getThumbnailPath(path);
            values.put("path", path);
            values.put("thumbnail_path", thumbnailPath);
            values.put("status", (Integer) 0);
            values.put("is_private", isPrivate ? "1" : "0");
            values.put("reference", reference);
            values.put("owner_id", Long.valueOf(ownerId));
            if (messageRowId > 0) {
                values.put("message_row", Long.valueOf(messageRowId));
            }
            if (conversationObjectId > 0) {
                values.put("conversation_row_id", Long.valueOf(conversationObjectId));
            }
            Uri contentUri2 = getBaseUriBuilder().path("uploads/put_new_upload").build();
            context.getContentResolver().insert(contentUri2, values);
        } else {
            String[] selectionArgs = {path};
            values.put("status", (Integer) 0);
            values.put("is_private", isPrivate ? "1" : "0");
            Uri contentUri3 = getBaseUriBuilder().path("uploads/put_values").build();
            context.getContentResolver().update(contentUri3, values, "path=?", selectionArgs);
        }
        if (c != null) {
            c.close();
        }
    }

    public static boolean uploadListIsEmpty(Context context) {
        Cursor c = context.getContentResolver().query(VineUploads.Uploads.CONTENT_URI, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, null, null, null);
        if (c != null) {
            cursorIsEmpty = c.getCount() < 1;
            c.close();
        }
        return cursorIsEmpty;
    }

    public static VineUpload getUpload(Context context, String path) {
        long ownerId = AppController.getInstance(context).getActiveSessionReadOnly().getUserId();
        VineUpload ret = null;
        Uri contentUri = getBaseUriBuilder().path("uploads/upload").appendEncodedPath(String.valueOf(ownerId)).appendQueryParameter("path", path).build();
        Cursor c = context.getContentResolver().query(contentUri, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                ret = VineUpload.fromCursor(path, c);
            }
            c.close();
        }
        return ret;
    }

    public static void deleteUploadRecord(Context context, String path) {
        Uri contentUri = getBaseUriBuilder().path("uploads/delete_upload").build();
        String[] whereArgs = {path};
        context.getContentResolver().delete(contentUri, "path=?", whereArgs);
    }

    private static Uri.Builder getBaseUriBuilder() {
        return new Uri.Builder().scheme("content").authority(VineUploadProvider.AUTHORITY);
    }

    public static String generateFileName(Context context) throws PackageManager.NameNotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/", Locale.US);
        return formatter.format(new Date()) + UUID.randomUUID() + "_" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    }

    public synchronized String upload(VineUploadService.ServiceAsyncTask serviceAsyncTask, UploadProgressListener listener, String encoded, String path, boolean isPrivate, RecordSessionVersion version) throws Exception {
        String videoUri;
        File file = new File(encoded);
        if (!file.exists()) {
            SLog.e("Files does not exist: {}", encoded);
            videoUri = null;
        } else {
            try {
                VineUpload upload = getUpload(serviceAsyncTask.getContext(), path);
                if (upload == null) {
                    throw new Exception("Upload record was null");
                }
                if (serviceAsyncTask.isCancelled()) {
                    SLog.d("Task is already cancelled.");
                    videoUri = null;
                } else {
                    Context context = serviceAsyncTask.getContext();
                    String fileName = generateFileName(context) + version.videoOutputExtension;
                    videoUri = this.mMediaUtility.getVideoUri(listener, file, fileName, isPrivate);
                    if (videoUri == null) {
                        throw new Exception("Failed to upload video.");
                    }
                    if (serviceAsyncTask.isCancelled()) {
                        SLog.d("Task is already cancelled.");
                        videoUri = null;
                    }
                }
            } catch (Exception e) {
                SLog.e("Upload failed: {}", (Object) path, (Throwable) e);
                videoUri = null;
            }
        }
        return videoUri;
    }

    public static LinkedHashMap<String, Integer> getAllPaths(Context context) {
        LinkedHashMap<String, Integer> paths = new LinkedHashMap<>();
        long ownerId = AppController.getInstance(context).getActiveSessionReadOnly().getUserId();
        Uri contentUri = ContentUris.withAppendedId(VineUploads.Uploads.CONTENT_URI, ownerId);
        Cursor c = context.getContentResolver().query(contentUri, VineUploadsDatabaseSQL.UploadsQuery.PROJECTION, null, null, "_id ASC");
        if (c != null) {
            while (c.moveToNext()) {
                String path = c.getString(1);
                int status = c.getInt(3);
                paths.put(path, Integer.valueOf(status));
            }
            c.close();
        }
        return paths;
    }

    public static void setPostInfo(Context context, VineUpload upload, PostInfo info) {
        SLog.d("Setting post info for path=" + upload.path + " with caption=" + info.caption + ", twitter=" + info.postToTwitter + ", facebook=" + info.postToFacebook + ", tumblr=" + info.postToTumblr);
        upload.postInfo = info.toString();
        Uri contentUri = getBaseUriBuilder().path("uploads/put_post_info").appendQueryParameter("path", upload.path).appendQueryParameter("post_info", info.toString()).build();
        context.getContentResolver().update(contentUri, null, null, null);
    }

    public static void setStatus(Context context, VineUpload upload, int status) {
        setStatus(context, upload, status, null);
    }

    public static void setStatus(Context context, VineUpload upload, int status, String captcha) {
        upload.status = status;
        Uri.Builder contentUri = getBaseUriBuilder().path("uploads/put_status").appendQueryParameter("path", upload.path).appendQueryParameter("status", String.valueOf(status));
        if (!TextUtils.isEmpty(captcha)) {
            contentUri.appendQueryParameter("captcha_url", captcha);
        }
        context.getContentResolver().update(contentUri.build(), null, null, null);
    }

    public static void setUploadTime(Context context, VineUpload upload) {
        upload.uploadTime = System.currentTimeMillis();
        String currentTime = String.valueOf(System.currentTimeMillis());
        Uri contentUri = getBaseUriBuilder().path("uploads/put_upload_time").appendQueryParameter("path", upload.path).appendQueryParameter("upload_time", currentTime).build();
        context.getContentResolver().update(contentUri, null, null, null);
    }

    public static void setUri(Context context, VineUpload upload, String uri) {
        upload.setVideoUrl(uri);
        Uri contentUri = getBaseUriBuilder().path("uploads/put_uris").appendQueryParameter("path", upload.path).appendQueryParameter("video_url", uri).build();
        context.getContentResolver().update(contentUri, null, null, null);
    }

    public static void clearUploadCaptchas(Context context) {
        Uri.Builder uri = getBaseUriBuilder().path("uploads/put_values");
        ContentValues values = new ContentValues();
        values.put("captcha_url", "");
        context.getContentResolver().update(uri.build(), values, "1", null);
    }

    public static void setUploadMessageRowId(Context context, String uploadPath, long messageId) {
        Uri contentUri = getBaseUriBuilder().path("uploads/put_message_row_id").appendQueryParameter("path", uploadPath).appendQueryParameter("message_row_id", String.valueOf(messageId)).build();
        context.getContentResolver().update(contentUri, null, null, null);
    }

    public static void prepopulateCache(Context context, String path, String videoUrl) {
        SLog.d("Prepopulating cache. Video url: {}", videoUrl);
        if (videoUrl != null) {
            prepopulateVideoCache(context, path, videoUrl);
        }
        context.sendBroadcast(new Intent("co.vine.android.invalidateCache"), CrossConstants.BROADCAST_PERMISSION);
    }

    private static void prepopulateVideoCache(Context context, String videoPath, String videoUrl) throws NoSuchAlgorithmException {
        VideoCache<VineAPI> videoCache = CacheFactory.newVideoCache(context);
        try {
            FileInputStream fis = new FileInputStream(videoPath);
            videoCache.prepopulateVideoCacheForUrl(SessionManager.getSharedInstance().getCurrentSession().getUserId(), new VideoKey(videoUrl), videoUrl, fis);
        } catch (FileNotFoundException e) {
            CrashUtil.logException(e, "Error prepopulating the cache", new Object[0]);
        }
    }
}
