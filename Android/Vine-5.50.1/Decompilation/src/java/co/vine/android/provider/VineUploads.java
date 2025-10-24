package co.vine.android.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/* loaded from: classes.dex */
public class VineUploads {

    public static final class Uploads implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse(VineUploadProvider.CONTENT_AUTHORITY + "uploads");
    }
}
