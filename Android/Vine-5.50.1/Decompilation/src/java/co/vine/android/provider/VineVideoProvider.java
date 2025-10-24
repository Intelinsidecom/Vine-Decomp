package co.vine.android.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import co.vine.android.util.PlayerUtil;
import java.io.File;
import java.io.FileNotFoundException;

/* loaded from: classes.dex */
public class VineVideoProvider extends ContentProvider {
    public static final String AUTHORITY = PlayerUtil.getAuthority(".provider.VineVideoProvider");
    public static final String CONTENT_AUTHORITY = "content://" + AUTHORITY + "/";

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return "video/mp4";
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File f = new File(uri.getPath());
        if (f.exists()) {
            return ParcelFileDescriptor.open(f, 268435456);
        }
        throw new FileNotFoundException(uri.getPath());
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
