package co.vine.android.cache.video;

import co.vine.android.cache.UrlResource;
import java.io.File;

/* loaded from: classes.dex */
public class UrlVideo extends UrlResource<File> {
    public File mFile;
    private Integer mSize;

    /* JADX WARN: Multi-variable type inference failed */
    public UrlVideo(String url, File file) {
        super(url);
        this.value = file;
        if (file != 0) {
            this.mFile = file;
        }
    }

    public String getAbsolutePath() {
        return this.mFile.getAbsolutePath();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int getSize() {
        if (this.mSize == null || this.mSize.intValue() == 0) {
            if (this.value != 0) {
                this.mSize = Integer.valueOf(safeLongToInt(((File) this.value).length()));
            } else {
                this.mSize = 0;
            }
        }
        return this.mSize.intValue();
    }

    @Override // co.vine.android.cache.UrlResource
    public int size() {
        return getSize();
    }

    private static int safeLongToInt(long l) {
        if ((l & (-2147483648L)) == 0 || (l & (-2147483648L)) == -2147483648L) {
            return (int) l;
        }
        throw new IllegalArgumentException("File is too large to store in cache.");
    }
}
