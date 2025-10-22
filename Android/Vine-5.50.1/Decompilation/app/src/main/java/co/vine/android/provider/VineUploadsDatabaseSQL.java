package co.vine.android.provider;

/* loaded from: classes.dex */
public class VineUploadsDatabaseSQL {

    public static final class UploadsQuery {
        public static final String[] PROJECTION = {"_id", "path", "hash", "status", "post_info", "video_url", "thumbnail_path", "thumbnail_url", "upload_time", "is_private", "conversation_row_id", "reference", "owner_id", "captcha_url", "max_loops", "message_row"};
    }
}
