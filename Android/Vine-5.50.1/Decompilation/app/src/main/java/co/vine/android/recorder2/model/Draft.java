package co.vine.android.recorder2.model;

import android.net.Uri;
import java.io.BufferedOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class Draft implements Externalizable {
    private static final long serialVersionUID = 1;
    private String mDirectoryPath;
    private ArrayList<Segment> mSegments;
    private String mThumbnailPath;
    private String mVideoPath;
    private static String SERIALIZED_FILENAME = "/draft.bin";
    private static String FINAL_VIDEO_FILENAME = "/final.mp4";

    public Draft() {
    }

    public Draft(String directoryPath) {
        this.mSegments = new ArrayList<>();
        this.mDirectoryPath = directoryPath;
    }

    public Draft(Draft copy) {
        this.mSegments = new ArrayList<>();
        if (copy != null) {
            Iterator<Segment> it = copy.mSegments.iterator();
            while (it.hasNext()) {
                Segment segment = it.next();
                Segment copySegment = new Segment(segment);
                this.mSegments.add(copySegment);
            }
            this.mDirectoryPath = copy.mDirectoryPath;
            this.mVideoPath = copy.mVideoPath;
            this.mThumbnailPath = copy.mThumbnailPath;
        }
    }

    public static String getSerializedFilePath(String directoryPath) {
        return directoryPath + SERIALIZED_FILENAME;
    }

    public void addSegment(Segment segment) {
        this.mSegments.add(segment);
    }

    public int getSegmentCount() {
        return this.mSegments.size();
    }

    public Segment getSegment(int position) {
        return this.mSegments.get(position);
    }

    public String getPath() {
        return this.mDirectoryPath;
    }

    public String getDirectoryName() {
        return Uri.parse(this.mDirectoryPath).getLastPathSegment();
    }

    public long getDuration() {
        long totalDur = 0;
        Iterator<Segment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            Segment s = it.next();
            totalDur += s.getTrimmedDurationMS();
        }
        return totalDur;
    }

    public void remove(int which) {
        this.mSegments.remove(which);
    }

    public void swap(int from, int to) {
        Segment temp = getSegment(from);
        this.mSegments.set(from, getSegment(to));
        this.mSegments.set(to, temp);
    }

    public long[] getSegmentTimestampBounds(int position) {
        if (position >= getSegmentCount()) {
            return new long[]{0, 0};
        }
        long start = 0;
        for (int i = 0; i < position; i++) {
            start += this.mSegments.get(i).getTrimmedDurationMS();
        }
        return new long[]{start, this.mSegments.get(position).getTrimmedDurationMS() + start};
    }

    public Segment getLastSegment() {
        if (this.mSegments.size() > 0) {
            return this.mSegments.get(this.mSegments.size() - 1);
        }
        return null;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof Draft)) {
            return false;
        }
        Draft other = (Draft) o;
        if (this.mDirectoryPath.equals(other.mDirectoryPath) && this.mSegments.size() == other.mSegments.size()) {
            for (int i = 0; i < this.mSegments.size(); i++) {
                boolean sameSegment = this.mSegments.get(i).equals(other.mSegments.get(i));
                if (!sameSegment) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = super.hashCode();
        return (((((((result * 31) + (this.mSegments != null ? this.mSegments.hashCode() : 0)) * 31) + (this.mDirectoryPath != null ? this.mDirectoryPath.hashCode() : 0)) * 31) + (this.mVideoPath != null ? this.mVideoPath.hashCode() : 0)) * 31) + (this.mThumbnailPath != null ? this.mThumbnailPath.hashCode() : 0);
    }

    public void duplicateSegment(int selection, boolean animate) {
        Segment dupe = new Segment(this.mSegments.get(selection));
        dupe.setShouldAnimateIn(animate);
        this.mSegments.add(selection + 1, dupe);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.mSegments = (ArrayList) in.readObject();
        this.mDirectoryPath = (String) in.readObject();
        this.mVideoPath = (String) in.readObject();
        this.mThumbnailPath = (String) in.readObject();
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.mSegments);
        out.writeObject(this.mDirectoryPath);
        out.writeObject(this.mVideoPath);
        out.writeObject(this.mThumbnailPath);
    }

    public void save() throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(getSerializedFilePath(this.mDirectoryPath));
        OutputStream buffer = new BufferedOutputStream(fileOutputStream);
        ObjectOutput output = new ObjectOutputStream(buffer);
        output.writeObject(this);
        output.close();
        buffer.close();
    }

    public boolean hasBeenSaved() {
        File serializedFile = new File(getSerializedFilePath(this.mDirectoryPath));
        return serializedFile.exists();
    }

    public void delete() throws IOException {
        FileUtils.deleteDirectory(new File(this.mDirectoryPath));
    }

    public String getVideoPath() {
        return this.mVideoPath;
    }

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void generateFinalVideoPath() {
        this.mVideoPath = this.mDirectoryPath + FINAL_VIDEO_FILENAME;
    }

    public void generateFinalThumbnailPath() {
        this.mThumbnailPath = this.mDirectoryPath + "/final.mp4.jpg";
    }

    public ArrayList<Segment> getSegments() {
        return this.mSegments;
    }
}
