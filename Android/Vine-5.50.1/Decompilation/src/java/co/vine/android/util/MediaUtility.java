package co.vine.android.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import co.vine.android.api.VineParserReader;
import co.vine.android.client.VineAPI;
import co.vine.android.network.FileNetworkEntity;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationFactory;
import co.vine.android.network.VineNetworkUtils;
import co.vine.android.recorder.RecordSessionVersion;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.util.UUID;

/* loaded from: classes.dex */
public class MediaUtility {
    private final Context mContext;
    private final String mMediaUrl;
    private final NetworkOperationFactory mNetworkFactory = VineNetworkUtils.getDefaultNetworkOperationFactory();

    public MediaUtility(Context context) {
        this.mContext = context;
        this.mMediaUrl = VineAPI.getInstance(context).getMediaUrl();
    }

    public FileInfo upload(FileNetworkEntity.ProgressListener listener, String key, File fileToUpload, String type) throws Exception {
        return upload(listener, key, fileToUpload, type, false);
    }

    public FileInfo upload(FileNetworkEntity.ProgressListener listener, String key, File fileToUpload, String type, boolean isPrivate) throws Exception {
        SLog.d("Upload started for {}, key: {}.", fileToUpload, key);
        RecordSessionVersion version = UploadManager.getVersionFromPath(fileToUpload.getPath());
        if (key.endsWith(version.videoOutputExtension)) {
            StringBuilder url = VineAPI.buildUponUrl(this.mMediaUrl, "upload", "videos", fileToUpload.getName() + version.videoOutputExtension);
            if (isPrivate) {
                VineAPI.addParam(url, "private", "1");
            }
            MeasureOutputStream entity = new MeasureOutputStream(fileToUpload, version.mimeType, listener);
            VineParserReader reader = VineParserReader.createParserReader(16);
            NetworkOperation op = this.mNetworkFactory.createMediaPutRequest(this.mContext, url, reader, entity, VineAPI.getInstance(this.mContext)).execute();
            if (op.isOK()) {
                SLog.d("Upload {} successful.", op.uploadKey);
                return new FileInfo(fileToUpload.getName(), fileToUpload, op.uploadKey);
            }
            SLog.d("{} {} {}", url, Integer.valueOf(op.statusCode), op.statusPhrase);
        } else if (key.endsWith("jpg")) {
            SLog.d("Upload image through media.");
            StringBuilder url2 = VineAPI.buildUponUrl(this.mMediaUrl, "upload", type, fileToUpload.getName() + ".jpg");
            if (isPrivate) {
                VineAPI.addParam(url2, "private", "1");
            }
            MeasureOutputStream entity2 = new MeasureOutputStream(fileToUpload, "image/jpeg", listener);
            VineParserReader reader2 = VineParserReader.createParserReader(16);
            NetworkOperation op2 = this.mNetworkFactory.createMediaPutRequest(this.mContext, url2, reader2, entity2, VineAPI.getInstance(this.mContext)).execute();
            if (op2.isOK()) {
                SLog.d("Upload {} successful.", op2.uploadKey);
                return new FileInfo(fileToUpload.getName(), fileToUpload, op2.uploadKey);
            }
            SLog.e("{} {} {}", new Object[]{url2, Integer.valueOf(op2.statusCode), op2.statusPhrase});
        }
        return null;
    }

    public String getVideoUri(FileNetworkEntity.ProgressListener listener, File file, String filename, boolean isPrivate) throws Exception {
        FileInfo info = upload(listener, "videos/" + filename, file, "videos", isPrivate);
        if (info != null) {
            return info.uploadKey;
        }
        return null;
    }

    public String getPhotoUri(Uri avatarFileUri, Context context) throws Exception {
        String filename = "avatars/" + UUID.randomUUID() + ".jpg";
        if (avatarFileUri == null || TextUtils.isEmpty(avatarFileUri.toString())) {
            return null;
        }
        String realFileUrl = ImageUtils.getRealPathFromImageUri(context, avatarFileUri);
        FileInfo info = upload(null, filename, new File(realFileUrl), "avatars");
        if (info != null) {
            return info.uploadKey;
        }
        return null;
    }

    private static class FileInfo {
        public File file;
        public String filename;
        public String uploadKey;

        public FileInfo(String filename, File file, String uploadKey) {
            this.filename = filename;
            this.file = file;
            this.uploadKey = uploadKey;
        }
    }
}
