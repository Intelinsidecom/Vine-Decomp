package co.vine.android.recorder;

import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.InstanceCounter;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes.dex */
public class RecordSession implements Externalizable {
    private static final InstanceCounter sInstanceCounter = new InstanceCounter(5);
    private static final int[] sLock = new int[0];
    private static final long serialVersionUID = 8590159620194730309L;
    private AudioArray mAudioData;
    private RecordConfigUtils.RecordConfig mConfig;
    private RecordSessionVersion mVersion;
    private byte[] mVideoData;
    private final ArrayList<RecordSegment> mSegments = new ArrayList<>();
    private int mVideoDataCount = -1;
    private int mAudioDataCount = -1;

    public RecordSession() {
    }

    public RecordSession(RecordSessionVersion version) {
        synchronized (sLock) {
            this.mVersion = version;
            if (SLog.sLogsOn) {
                sInstanceCounter.onCreate(this);
            }
        }
    }

    public static RecordSession newSession(RecordConfigUtils.RecordConfig config, RecordSessionVersion version) {
        RecordSession session = new RecordSession(version);
        session.mVideoData = new byte[RecordConfigUtils.getVideoBufferMax(config)];
        session.mAudioData = AudioArrays.newInstance(RecordConfigUtils.getMaxAudioBufferSize(config), version.getAudioArrayType());
        session.mConfig = config;
        return session;
    }

    public int getVideoDataCount() {
        if (this.mVideoDataCount == -1) {
            this.mVideoDataCount = calculateVideoCount();
        }
        return this.mVideoDataCount;
    }

    public void setVideoDataCount(int videoDataCount) {
        if (SLog.sLogsOn && videoDataCount != calculateVideoCount()) {
            SLog.e("Video Counts does not match: {} {}.", Integer.valueOf(videoDataCount), Integer.valueOf(calculateVideoCount()));
        }
        this.mVideoDataCount = videoDataCount;
    }

    public void setAudioDataCount(int audioDataCount) {
        if (SLog.sLogsOn && audioDataCount != calculateAudioCount()) {
            SLog.e("Audio Counts does not match: {} {}.", Integer.valueOf(audioDataCount), Integer.valueOf(calculateAudioCount()));
        }
        this.mAudioDataCount = audioDataCount;
    }

    public int calculateVideoCount() {
        int total = 0;
        Iterator<RecordSegment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                ArrayList<VideoData> videoData = segment.getVideoData();
                Iterator<VideoData> it2 = videoData.iterator();
                while (it2.hasNext()) {
                    VideoData data = it2.next();
                    if (data.size > 0) {
                        total += data.size;
                    }
                }
            }
        }
        return total;
    }

    public int calculateAudioCount() {
        int total = 0;
        Iterator<RecordSegment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                total += segment.getCombinedAudioData().size;
            }
        }
        return total;
    }

    public int calculateMemoryBackedAudioCount() {
        int total = 0;
        Iterator<RecordSegment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.isDataFileBacked() && !segment.removed) {
                total += segment.getCombinedAudioData().size;
            }
        }
        return total;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.mVersion);
        out.writeObject(this.mVideoData);
        out.writeObject(this.mAudioData.getData());
        out.writeObject(this.mSegments);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput stream) throws ClassNotFoundException, IOException {
        Object obj = stream.readObject();
        if (obj instanceof byte[]) {
            this.mVideoData = (byte[]) obj;
        } else {
            this.mVersion = (RecordSessionVersion) obj;
            this.mVideoData = (byte[]) stream.readObject();
        }
        Object audioData = stream.readObject();
        this.mAudioData = AudioArrays.wrap(audioData);
        this.mSegments.addAll((ArrayList) stream.readObject());
    }

    public ArrayList<RecordSegment> getSegments() {
        return this.mSegments;
    }

    public byte[] getVideoData() {
        return this.mVideoData;
    }

    public AudioArray getAudioData() {
        return this.mAudioData;
    }

    public void add(RecordSegment currentSegment) {
        this.mSegments.add(currentSegment);
    }

    public RecordConfigUtils.RecordConfig getConfig() {
        return this.mConfig;
    }

    public void setConfig(RecordConfigUtils.RecordConfig config) {
        this.mConfig = config;
    }

    public String toString() {
        return MessageFormatter.format("\nVideoData Count: {}, AudioData Count: {}, Segments: {}", Integer.valueOf(this.mVideoDataCount), Integer.valueOf(this.mAudioDataCount), Integer.valueOf(this.mSegments.size())).getMessage() + (this.mSegments.size() > 0 ? " Last: " + this.mSegments.get(this.mSegments.size() - 1) : "");
    }

    public RecordSessionVersion getVersion() {
        if (this.mVersion == null) {
            this.mVersion = RecordSessionVersion.SW_MP4;
        }
        return this.mVersion;
    }

    public int getDurationMs() {
        int durationMs = 0;
        Iterator<RecordSegment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                durationMs += segment.getDurationMs();
            }
        }
        return durationMs;
    }

    public JSONArray getMetaData() throws JSONException {
        long lastTimeStamp = 0;
        JSONArray array = new JSONArray();
        Iterator<RecordSegment> it = this.mSegments.iterator();
        while (it.hasNext()) {
            RecordSegment segment = it.next();
            if (!segment.removed) {
                array.put(segment.getMetaData(lastTimeStamp));
                lastTimeStamp += segment.getDurationMs();
            }
        }
        return array;
    }
}
