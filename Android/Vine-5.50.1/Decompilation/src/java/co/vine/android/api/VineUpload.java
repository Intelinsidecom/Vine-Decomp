package co.vine.android.api;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.edisonwang.android.slog.SLog;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class VineUpload implements Parcelable {
    public static final Parcelable.Creator<VineUpload> CREATOR = new Parcelable.Creator<VineUpload>() { // from class: co.vine.android.api.VineUpload.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineUpload createFromParcel(Parcel in) {
            return new VineUpload(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VineUpload[] newArray(int size) {
            return new VineUpload[size];
        }
    };
    public String captchaUrl;
    public long conversationRowId;
    public long id;
    public boolean isPrivate;
    public long mergedMessageId;
    public String path;
    public String postInfo;
    public String reference;
    public ArrayList<VineSource> sources;
    public int status;
    public String thumbnailPath;
    public long uploadTime;
    public String videoUrl;

    public VineUpload(String path, String postInfo, String videoUrl, String thumbnailPath, String uploadTime, String reference, int status, String captchaUrl, boolean isPrivate, long conversationRowId, long mergedMessageId, ArrayList<VineSource> sources) {
        this.path = path;
        this.postInfo = postInfo;
        this.videoUrl = videoUrl;
        this.thumbnailPath = thumbnailPath;
        this.reference = reference;
        this.status = status;
        this.isPrivate = isPrivate;
        this.conversationRowId = conversationRowId;
        if (!TextUtils.isEmpty(uploadTime)) {
            this.uploadTime = Long.parseLong(uploadTime);
        } else {
            this.uploadTime = 0L;
        }
        this.captchaUrl = captchaUrl;
        this.mergedMessageId = mergedMessageId;
        this.sources = sources;
    }

    public VineUpload() {
        this.status = 0;
    }

    public VineUpload(Parcel in) {
        this.status = in.readInt();
        this.isPrivate = in.readInt() > 0;
        this.id = in.readLong();
        this.uploadTime = in.readLong();
        this.conversationRowId = in.readLong();
        this.path = in.readString();
        this.postInfo = in.readString();
        this.thumbnailPath = in.readString();
        this.reference = in.readString();
        this.captchaUrl = in.readString();
        this.mergedMessageId = in.readLong();
        this.videoUrl = in.readString();
        this.sources = in.readArrayList(VineSource.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static synchronized VineUpload fromCursor(String path, Cursor c) {
        String postInfo;
        String videoUrl;
        String thumbnailPath;
        String uploadTime;
        String reference;
        int status;
        boolean isPrivate;
        long conversationRowId;
        String captchaUrl;
        long mergedMessageId;
        postInfo = c.getString(4);
        videoUrl = c.getString(5);
        thumbnailPath = c.getString(6);
        uploadTime = c.getString(8);
        reference = c.getString(11);
        status = c.getInt(3);
        isPrivate = c.getInt(9) > 0;
        conversationRowId = c.getLong(10);
        captchaUrl = c.getString(13);
        mergedMessageId = c.getLong(15);
        return new VineUpload(path, postInfo, videoUrl, thumbnailPath, uploadTime, reference, status, captchaUrl, isPrivate, conversationRowId, mergedMessageId, null);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - this.uploadTime > 86400000;
    }

    public PostInfo getPostInfo() {
        SLog.d("Post info: {}.", this.postInfo);
        if (TextUtils.isEmpty(this.postInfo)) {
            return null;
        }
        try {
            JsonParser parser = VineParsers.createParser(this.postInfo);
            return VineUploadParsers.parsePostInfo(parser);
        } catch (IOException e) {
            throw new RuntimeException("This should never happen.", e);
        }
    }

    public ArrayList<VineSource> getSources() {
        return this.sources;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public void setVideoUrl(String uri) {
        this.videoUrl = uri;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.status);
        out.writeInt(this.isPrivate ? 1 : 0);
        out.writeLong(this.id);
        out.writeLong(this.uploadTime);
        out.writeLong(this.conversationRowId);
        out.writeString(this.path);
        out.writeString(this.postInfo);
        out.writeString(this.thumbnailPath);
        out.writeString(this.reference);
        out.writeString(this.captchaUrl);
        out.writeLong(this.mergedMessageId);
        out.writeString(this.videoUrl);
        out.writeList(this.sources);
    }
}
