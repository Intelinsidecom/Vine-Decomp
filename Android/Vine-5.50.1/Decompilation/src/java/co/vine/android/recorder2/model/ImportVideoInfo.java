package co.vine.android.recorder2.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/* loaded from: classes.dex */
public class ImportVideoInfo implements Externalizable {
    private String mLocalPath;
    private String mSourcePostId;
    private String mVideoUrl;

    public ImportVideoInfo() {
    }

    public ImportVideoInfo(String videoUrl, String localPath, String sourcePostId) {
        this.mVideoUrl = videoUrl;
        this.mLocalPath = localPath;
        this.mSourcePostId = sourcePostId;
    }

    public String getLocalPath() {
        return this.mLocalPath;
    }

    public void setLocalPath(String localPath) {
        this.mLocalPath = localPath;
    }

    public String getVideoUrl() {
        return this.mVideoUrl;
    }

    public String getSourcePostId() {
        return this.mSourcePostId;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.mVideoUrl);
        out.writeObject(this.mLocalPath);
        out.writeObject(this.mSourcePostId);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.mVideoUrl = (String) in.readObject();
        this.mLocalPath = (String) in.readObject();
        this.mSourcePostId = (String) in.readObject();
    }
}
