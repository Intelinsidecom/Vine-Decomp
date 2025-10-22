package co.vine.android.recorder;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import co.vine.android.recorder.TrimData;
import co.vine.android.recorder.audio.AudioArray;
import co.vine.android.recorder.audio.AudioArrays;
import co.vine.android.recorder.audio.AudioData;
import co.vine.android.recorder.camera.CameraSetting;
import co.vine.android.recorder.video.VideoData;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;
import com.googlecode.javacpp.BytePointer;
import com.googlecode.javacpp.ShortPointer;
import com.googlecode.javacv.cpp.avutil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RecordSegment implements Externalizable {
    private static final long serialVersionUID = 7122730863787653276L;
    public Drawable drawable;
    public int index;
    private ArrayList<AudioData> mAudioData;
    private int mFrameRate;
    private String mFullDataId;
    private final HashMap<String, String> mImportedMetaData;
    private boolean mIsImported;
    private long mRecordingTime;
    private boolean mScilenced;
    private CameraSetting mSetting;
    private String mThumbnailPath;
    private String mTimeZoneId;
    private int mTrimEndMs;
    private TrimResult mTrimResult;
    private int mTrimStartMs;
    private boolean mValidated;
    private ArrayList<VideoData> mVideoData;
    public boolean removed;
    public boolean shouldAnimateIn;
    public long startTimestamp;
    public String videoPath;

    public RecordSegment() {
        this.mFullDataId = "";
        this.mImportedMetaData = new HashMap<>();
        this.mRecordingTime = -1L;
    }

    public RecordSegment(long currentDuration) {
        this(currentDuration, 30);
    }

    public RecordSegment(long currentDuration, int frameRate) {
        this.mFullDataId = "";
        this.mImportedMetaData = new HashMap<>();
        this.mVideoData = new ArrayList<>();
        this.mAudioData = new ArrayList<>();
        this.mThumbnailPath = "";
        this.startTimestamp = currentDuration;
        this.mRecordingTime = System.currentTimeMillis();
        this.mTimeZoneId = TimeZone.getDefault().getID();
        this.mFrameRate = frameRate;
    }

    public RecordSegment(RecordSegment source) {
        this.mFullDataId = "";
        this.mImportedMetaData = new HashMap<>();
        copyFrom(source);
    }

    private void copyFrom(RecordSegment source) {
        this.mSetting = source.mSetting;
        this.mThumbnailPath = source.mThumbnailPath;
        this.removed = source.removed;
        this.index = source.index;
        this.videoPath = source.videoPath;
        this.startTimestamp = source.startTimestamp;
        this.mVideoData = new ArrayList<>();
        Iterator<VideoData> it = source.mVideoData.iterator();
        while (it.hasNext()) {
            VideoData videoData = it.next();
            this.mVideoData.add(new VideoData(videoData));
        }
        this.mAudioData = new ArrayList<>();
        Iterator<AudioData> it2 = source.mAudioData.iterator();
        while (it2.hasNext()) {
            AudioData audioData = it2.next();
            this.mAudioData.add(new AudioData(audioData));
        }
        this.mFullDataId = source.mFullDataId;
        this.mIsImported = source.mIsImported;
        this.mValidated = source.mValidated;
        this.mTrimResult = source.mTrimResult;
        this.mTrimStartMs = source.mTrimStartMs;
        this.mTrimEndMs = source.mTrimEndMs;
        this.mScilenced = source.mScilenced;
        this.mRecordingTime = source.mRecordingTime;
        this.mTimeZoneId = source.mTimeZoneId;
        this.mFrameRate = source.mFrameRate;
        this.mImportedMetaData.clear();
        this.mImportedMetaData.putAll(source.mImportedMetaData);
    }

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void setThumbnailPath(String path) {
        this.mThumbnailPath = path;
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.mFullDataId);
        out.writeInt(this.mFrameRate);
        out.writeLong(this.mRecordingTime);
        out.writeObject(this.mTimeZoneId);
        out.writeBoolean(this.mValidated);
        out.writeBoolean(this.mIsImported);
        out.writeObject(this.mImportedMetaData);
        out.writeObject(this.mTrimResult);
        out.writeInt(this.mTrimStartMs);
        out.writeInt(this.mTrimEndMs);
        out.writeBoolean(this.mScilenced);
        out.writeObject(this.mSetting);
        out.writeObject(this.mThumbnailPath);
        out.writeObject(this.mVideoData);
        out.writeObject(getCombinedAudioData());
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput stream) throws ClassNotFoundException, IOException {
        Object obj = stream.readObject();
        if (obj instanceof CameraSetting) {
            this.mSetting = (CameraSetting) obj;
            this.mFullDataId = "";
            this.mTrimResult = null;
            this.mRecordingTime = -1L;
            this.mFrameRate = 30;
        } else {
            this.mFullDataId = (String) obj;
            this.mFrameRate = stream.readInt();
            this.mRecordingTime = stream.readLong();
            this.mTimeZoneId = (String) stream.readObject();
            this.mValidated = stream.readBoolean();
            this.mIsImported = stream.readBoolean();
            this.mImportedMetaData.putAll((HashMap) stream.readObject());
            this.mTrimResult = (TrimResult) stream.readObject();
            this.mTrimStartMs = stream.readInt();
            this.mTrimEndMs = stream.readInt();
            this.mScilenced = stream.readBoolean();
            this.mSetting = (CameraSetting) stream.readObject();
        }
        this.mThumbnailPath = (String) stream.readObject();
        this.mVideoData = (ArrayList) stream.readObject();
        this.mAudioData = new ArrayList<>();
        this.mAudioData.add((AudioData) stream.readObject());
    }

    public boolean isTrimmed() {
        return this.mTrimResult != null;
    }

    public String getDataFileName() {
        return this.mFullDataId;
    }

    public static File getSegmentDataFolder(File draftFolder) {
        return new File(draftFolder, "segments");
    }

    public boolean isDataFileBacked() {
        return !TextUtils.isEmpty(this.mFullDataId);
    }

    public void setTrimData(TrimData data) throws TrimData.InvalidTrimException {
        if (data == null) {
            this.mTrimStartMs = 0;
            this.mTrimEndMs = 0;
            this.mTrimResult = null;
            return;
        }
        correctVideoTimestampsIfNeeded();
        ArrayList<VideoData> videoData = getVideoData();
        if (videoData.size() == 0) {
            throw new TrimData.InvalidTrimException("You cannot trim a segment with 0 frames.");
        }
        this.mTrimResult = data.getTrimResultFor(videoData, getCombinedAudioData(), this.mFrameRate);
        this.mTrimStartMs = data.startMs;
        this.mTrimEndMs = data.endMs;
    }

    public synchronized void correctVideoTimestampsIfNeeded() {
        if (!this.mValidated) {
            validateAndFixVideoTimestampsIfNeeded(true);
        }
    }

    private synchronized void correctVideoTimestamps(int usPf, int missingFrameCount, int expectedDurationUs) {
        int size = this.mVideoData.size();
        long offset = this.mVideoData.get(0).timestamp;
        int realUsPf = missingFrameCount < 2 ? usPf : expectedDurationUs / size;
        for (int i = 1; i < size; i++) {
            this.mVideoData.get(i).timestamp = (i * realUsPf) + offset;
        }
    }

    public void setSilenced(boolean silenced) {
        this.mScilenced = silenced;
    }

    public int getDurationMs() {
        return getAudioDurationMs();
    }

    public int getUnTrimmedAudioDurationMs() {
        return RecordConfigUtils.getTimeStampInMsFromSampleCounted(getCombinedAudioData().size);
    }

    public int getAudioDurationMs() {
        return isTrimmed() ? this.mTrimResult.getAudioDuration() : getUnTrimmedAudioDurationMs();
    }

    public synchronized AudioData getCombinedAudioData() {
        AudioData audioData;
        if (this.mAudioData.size() == 0) {
            audioData = new AudioData(0, 0);
        } else {
            if (this.mAudioData.size() > 1) {
                int t = 0;
                Iterator<AudioData> it = this.mAudioData.iterator();
                while (it.hasNext()) {
                    AudioData data = it.next();
                    t += data.size;
                }
                AudioData audioCombined = new AudioData(this.mAudioData.get(0).start, t);
                this.mAudioData.clear();
                this.mAudioData.add(audioCombined);
            }
            audioData = this.mAudioData.get(0);
        }
        return audioData;
    }

    public ArrayList<VideoData> getVideoData() {
        return this.mVideoData;
    }

    public ArrayList<VideoData> getVideoDataTrimmedIfNeeded() {
        if (isTrimmed()) {
            ArrayList<VideoData> trimmed = new ArrayList<>();
            for (int i = this.mTrimResult.videoStartIndex; i < this.mTrimResult.videoEndIndex; i++) {
                trimmed.add(this.mVideoData.get(i));
            }
            return trimmed;
        }
        return this.mVideoData;
    }

    public synchronized void addAudioData(AudioData data) {
        this.mAudioData.add(data);
    }

    public CameraSetting getCameraSetting() {
        return this.mSetting;
    }

    public RecordSegment setCameraSetting(CameraSetting cameraSetting) {
        this.mSetting = cameraSetting;
        return this;
    }

    public AudioData createTrimmedCombinedAudioData() {
        AudioData data = new AudioData(getCombinedAudioData());
        if (isTrimmed()) {
            data.start += this.mTrimResult.audioStartOffset;
            data.size = this.mTrimResult.audioEndCount - this.mTrimResult.audioStartOffset;
        }
        return data;
    }

    public boolean isSilenced() {
        return this.mScilenced;
    }

    public long getTrimmedVideoStartUs() {
        return this.mTrimResult.getVideoStartUs(getVideoData());
    }

    public long getTrimmedVideoStartMs() {
        return getTrimmedVideoStartUs() / 1000;
    }

    public synchronized void validateAndFixVideoTimestampsIfNeeded(boolean fixTimestamps) {
        int videoTime;
        int usPf = avutil.AV_TIME_BASE / this.mFrameRate;
        int msPf = usPf / 1000;
        int audioTimeMs = (int) (getCombinedAudioData().size / 44.1d);
        boolean hasWarning = false;
        boolean validationPasses = true;
        long startBenchmark = System.currentTimeMillis();
        int videoSize = this.mVideoData.size();
        SLog.i("Segment validation: A {} V {}", Integer.valueOf(getCombinedAudioData().size), Integer.valueOf(videoSize));
        if (videoSize == 0) {
            hasWarning = true;
            SLog.e("Video time is 0");
            videoTime = 0;
        } else {
            videoTime = (int) (((this.mVideoData.get(videoSize - 1).timestamp - this.mVideoData.get(0).timestamp) / 1000) + msPf);
        }
        if (audioTimeMs <= 0) {
            hasWarning = true;
            validationPasses = false;
            SLog.w("Audio time is 0");
        }
        if (audioTimeMs < videoTime) {
            hasWarning = true;
            SLog.w("Audio time is smaller than video time.");
            validationPasses = false;
        }
        if (Math.abs(audioTimeMs - videoTime) > 100) {
            hasWarning = true;
            SLog.w("Track differences too large.");
            validationPasses = false;
        }
        boolean wasTimestampsFixed = false;
        if (videoSize > 0) {
            long lastTimestamp = (-msPf) * 1000;
            long timestampError = 0;
            Iterator<VideoData> it = this.mVideoData.iterator();
            while (it.hasNext()) {
                VideoData data = it.next();
                SLog.i("V {}", Long.valueOf(data.timestamp));
                long diff = data.timestamp - lastTimestamp;
                if (diff <= 0) {
                    hasWarning = true;
                    SLog.e("Non-increasing video timestamp found: {}", Long.valueOf(diff));
                }
                if (diff > usPf) {
                    hasWarning = true;
                    SLog.w("Has frames missing: {}.", Long.valueOf(diff));
                }
                timestampError += usPf - diff;
                lastTimestamp = data.timestamp;
            }
            int missingFrameCount = (int) (timestampError / usPf);
            if (timestampError != 0) {
                if (Math.abs(missingFrameCount) < 2) {
                    SLog.d("Video timestamps do eventually correct itself.");
                } else {
                    validationPasses = false;
                    SLog.e("Frames do not match up: {}, missing {} frames.", Long.valueOf(timestampError), Integer.valueOf(missingFrameCount));
                }
                if (fixTimestamps) {
                    wasTimestampsFixed = true;
                    correctVideoTimestamps(usPf, missingFrameCount, (audioTimeMs - msPf) * 1000);
                }
            }
            long expectedLastTimestamp = (videoSize - 1) * usPf;
            SLog.d("Timestamp expectation difference: {}.", Long.valueOf((expectedLastTimestamp - lastTimestamp) + this.mVideoData.get(0).timestamp));
        }
        if (validationPasses) {
            if (hasWarning) {
                SLog.d("Validation passed with warnings.");
            } else {
                SLog.d("Validation passed.");
            }
        } else {
            SLog.e("Validation failed: A {} V {}.", Integer.valueOf(audioTimeMs), Integer.valueOf(videoTime));
        }
        SLog.i("Validation completed in {}ms", Long.valueOf(System.currentTimeMillis() - startBenchmark));
        if (wasTimestampsFixed) {
            validateAndFixVideoTimestampsIfNeeded(false);
        }
    }

    public void setFrameRate(int frameRate) {
        this.mFrameRate = frameRate;
    }

    public int getFrameRate() {
        return this.mFrameRate;
    }

    public int getTrimmedAudioEndUs() {
        return this.mTrimResult.getAudioEndMs() * 1000;
    }

    public void setIsImported(boolean isImported) {
        this.mIsImported = isImported;
    }

    public String getFullDataId() {
        return this.mFullDataId;
    }

    public void setFullDataId(String fullDataId) {
        this.mFullDataId = fullDataId;
    }

    public static class Data {
        final short[] audio;
        final byte[] video;

        public Data(short[] audio, byte[] video) {
            this.audio = audio;
            this.video = video;
        }
    }

    public Data loadDataFromFile(File draftFolder, byte[] videoBuffer, short[] audioBuffer) throws IOException {
        long loadStart = System.currentTimeMillis();
        File folder = getSegmentDataFolder(draftFolder);
        if (!folder.isDirectory()) {
            throw new FileNotFoundException("Segment data folder does not exist.");
        }
        AudioData data = getCombinedAudioData();
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(folder, getDataFileName()))));
        if (audioBuffer == null || audioBuffer.length < data.size) {
            audioBuffer = new short[data.size];
        }
        for (int i = 0; i < data.size; i++) {
            audioBuffer[i] = stream.readShort();
        }
        int size = stream.readInt();
        if (videoBuffer == null || videoBuffer.length < size) {
            videoBuffer = new byte[size];
        }
        if (size > 0) {
            int ret = stream.read(videoBuffer, 0, size);
            if (ret < 0) {
                throw new IOException("video data missing.");
            }
        }
        int count = stream.readInt();
        stream.close();
        SLog.d("Load segment from data file took {}ms, with {} video segments.", Long.valueOf(System.currentTimeMillis() - loadStart), Integer.valueOf(count));
        return new Data(audioBuffer, videoBuffer);
    }

    public String saveDataToDiskIfNeeded(File draftFolder, byte[] videoData, AudioArray audioArray, RecordSegment originalSegment) throws IOException {
        if (!isDataFileBacked()) {
            long saveStart = System.currentTimeMillis();
            this.mFullDataId = String.valueOf(System.nanoTime());
            AudioData combinedAudio = getCombinedAudioData();
            SLog.i("First time trimming this video, create full sized data.");
            File folder = getSegmentDataFolder(draftFolder);
            if (!folder.mkdir() && !folder.isDirectory()) {
                throw new IOException("Failed to create segment data folder.");
            }
            DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(folder, getDataFileName()))));
            audioArray.writeInto(stream, combinedAudio.start, combinedAudio.size);
            combinedAudio.start = 0;
            int videoFileSize = 0;
            ArrayList<VideoData> segmentVideo = getVideoData();
            int total = 0;
            Iterator<VideoData> it = segmentVideo.iterator();
            while (it.hasNext()) {
                VideoData data = it.next();
                if (data.size >= 0) {
                    total += data.size;
                }
            }
            stream.writeInt(total);
            Iterator<VideoData> it2 = segmentVideo.iterator();
            while (it2.hasNext()) {
                VideoData data2 = it2.next();
                if (data2.size >= 0) {
                    int oldStart = data2.start;
                    data2.start = videoFileSize;
                    videoFileSize += data2.size;
                    stream.write(videoData, oldStart, data2.size);
                }
            }
            stream.writeInt(segmentVideo.size());
            stream.flush();
            stream.close();
            SLog.d("Save segment from data file took {}ms.", Long.valueOf(System.currentTimeMillis() - saveStart));
            if (originalSegment != null && originalSegment != this) {
                originalSegment.copyFrom(this);
            }
        }
        return this.mFullDataId;
    }

    public static ArrayList<RecordSegment> applyEditedChanges(File draftFolder, RecordSession session, ArrayList<RecordSegment> editedSegments) throws IOException {
        return applyEditedChanges(draftFolder, session, session.getSegments(), null, null, editedSegments);
    }

    public static ArrayList<RecordSegment> applyForEditedChanges(File draftFolder, RecordSession session, byte[] destinationVideoBuffer, AudioArray destinationAudioBuffer, ArrayList<RecordSegment> editedSegments) throws IOException {
        return applyEditedChanges(draftFolder, session, null, destinationVideoBuffer, destinationAudioBuffer, editedSegments);
    }

    private static ArrayList<RecordSegment> applyEditedChanges(File draftFolder, RecordSession session, ArrayList<RecordSegment> originalSegments, byte[] destinationVideo, AudioArray destinationAudio, ArrayList<RecordSegment> editedSegments) throws IOException {
        ByteBuffer videoBuffer;
        Buffer audioBuffer;
        boolean applyOnSource = destinationVideo == null;
        SLog.i("Apply changes, on source: {}.", Boolean.valueOf(applyOnSource));
        ArrayList<RecordSegment> modified = new ArrayList<>();
        ArrayList<RecordSegment> toRemove = new ArrayList<>();
        if (editedSegments != null) {
            byte[] videoData = session.getVideoData();
            AudioArray audioData = session.getAudioData();
            BytePointer videoDataPtr = null;
            BytePointer audioDataPtrByte = null;
            ShortPointer audioDataPtrShort = null;
            if (applyOnSource) {
                videoDataPtr = new BytePointer(videoData.length);
                videoBuffer = videoDataPtr.asBuffer();
                if (session.getVersion().getAudioArrayType() == AudioArrays.AudioArrayType.SHORT) {
                    audioDataPtrShort = new ShortPointer(audioData.getLength());
                    audioBuffer = audioDataPtrShort.asBuffer();
                } else {
                    audioDataPtrByte = new BytePointer(audioData.getLength());
                    audioBuffer = audioDataPtrByte.asBuffer();
                }
                for (int i = 0; i < originalSegments.size(); i++) {
                    originalSegments.get(i).index = i;
                }
            } else {
                videoBuffer = ByteBuffer.wrap(destinationVideo);
                audioBuffer = destinationAudio.asBuffer();
            }
            int audioTimeSize = 0;
            int audioTimeStampMs = 0;
            long nextTimeStamp = -1;
            Iterator<RecordSegment> it = editedSegments.iterator();
            while (it.hasNext()) {
                RecordSegment originalSegment = it.next();
                if (!originalSegment.removed) {
                    RecordSegment targetSegment = applyOnSource ? originalSegment : new RecordSegment(originalSegment);
                    modified.add(targetSegment);
                    SLog.d("Adding segment: {}.", Integer.valueOf(originalSegment.index));
                    ArrayList<VideoData> segmentVideo = targetSegment.getVideoData();
                    long lastOriginalTimeStamp = -1;
                    long lastTimeStamp = 0;
                    if (targetSegment.isTrimmed() || targetSegment.isDataFileBacked()) {
                        targetSegment.saveDataToDiskIfNeeded(draftFolder, videoData, audioData, originalSegment);
                        audioTimeStampMs += targetSegment.getDurationMs();
                    } else {
                        AudioData combinedAudio = targetSegment.getCombinedAudioData();
                        CrashUtil.log("Audio buffer putting into buffer of size {} start {} size {}", Integer.valueOf(audioBuffer.capacity()), Integer.valueOf(combinedAudio.start), Integer.valueOf(combinedAudio.size));
                        audioData.putInto((AudioArray) audioBuffer, combinedAudio.start, combinedAudio.size);
                        SLog.d("Audio Buffers from {} with size {}.", Integer.valueOf(combinedAudio.start), Integer.valueOf(combinedAudio.size));
                        combinedAudio.start = audioTimeSize;
                        audioTimeSize += combinedAudio.size;
                        audioTimeStampMs = RecordConfigUtils.getTimeStampInMsFromSampleCounted(audioTimeSize);
                    }
                    Iterator<VideoData> it2 = segmentVideo.iterator();
                    while (it2.hasNext()) {
                        VideoData data = it2.next();
                        if (data.size >= 0) {
                            if (lastOriginalTimeStamp == -1) {
                                lastOriginalTimeStamp = data.timestamp;
                                if (nextTimeStamp == -1) {
                                    nextTimeStamp = 0;
                                    data.timestamp = 0L;
                                } else {
                                    data.timestamp = nextTimeStamp;
                                }
                            } else {
                                long thisOriginalTimeStamp = data.timestamp;
                                data.timestamp = (data.timestamp - lastOriginalTimeStamp) + lastTimeStamp;
                                lastOriginalTimeStamp = thisOriginalTimeStamp;
                            }
                            SLog.d("Modifying timestamp from {} to {}.", Long.valueOf(lastOriginalTimeStamp), Long.valueOf(data.timestamp));
                            lastTimeStamp = data.timestamp;
                            if (!targetSegment.isDataFileBacked()) {
                                int oldStart = data.start;
                                data.start = videoBuffer.position();
                                videoBuffer.put(videoData, oldStart, data.size);
                            }
                        }
                    }
                    nextTimeStamp = Math.max(audioTimeStampMs * 1000, (segmentVideo.size() * avutil.AV_TIME_BASE) / targetSegment.mFrameRate) - (avutil.AV_TIME_BASE / targetSegment.mFrameRate);
                    SLog.d("Next timestamp: {}", Long.valueOf(nextTimeStamp));
                } else {
                    toRemove.add(originalSegment);
                }
            }
            int aPosition = audioBuffer.position();
            int vPosition = videoBuffer.position();
            SLog.d("Buffer limits: {}, {}.", Integer.valueOf(aPosition), Integer.valueOf(vPosition));
            if (applyOnSource) {
                videoBuffer.position(0);
                videoBuffer.get(videoData);
                audioBuffer.position(0);
                audioData.getFrom(audioBuffer);
                session.setAudioDataCount(aPosition);
                session.setVideoDataCount(vPosition);
            }
            if (videoDataPtr != null) {
                videoDataPtr.deallocate();
            }
            if (audioDataPtrByte != null) {
                audioDataPtrByte.deallocate();
            }
            if (audioDataPtrShort != null) {
                audioDataPtrShort.deallocate();
            }
            editedSegments.removeAll(toRemove);
            if (originalSegments != null) {
                originalSegments.clear();
                originalSegments.addAll(editedSegments);
            }
        }
        return applyOnSource ? toRemove : modified;
    }

    public Drawable getDrawableCopy(Context context) {
        if (this.drawable != null) {
            return new BitmapDrawable(context.getResources(), ((BitmapDrawable) this.drawable).getBitmap());
        }
        BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), getThumbnailPath());
        this.drawable = new BitmapDrawable(context.getResources(), bitmapDrawable.getBitmap());
        return bitmapDrawable;
    }

    public JSONObject getMetaData(long lastTimestamp) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("startTime", lastTimestamp / 1000.0d);
        obj.put("endTime", (getDurationMs() + lastTimestamp) / 1000.0d);
        if (this.mRecordingTime > 0) {
            obj.put("recordingTime", this.mRecordingTime);
            obj.put("timezone", this.mTimeZoneId);
        }
        obj.put("imported", this.mIsImported);
        CameraSetting cs = getCameraSetting();
        if (cs != null) {
            obj.put("frontFacing", cs.frontFacing);
        }
        for (String key : this.mImportedMetaData.keySet()) {
            obj.put(key, this.mImportedMetaData.get(key));
        }
        return obj;
    }

    public int getVideoDataEncodedPercentage() {
        int encoded = 0;
        ArrayList<VideoData> v = getVideoData();
        if (v.size() <= 0) {
            return 100;
        }
        Iterator<VideoData> it = v.iterator();
        while (it.hasNext()) {
            VideoData data = it.next();
            encoded += data.encoded ? 1 : 0;
        }
        return (encoded * 100) / v.size();
    }

    public static int getFrameRate(Collection<RecordSegment> segments) {
        int frameRate = Integer.MIN_VALUE;
        for (RecordSegment segment : segments) {
            if (frameRate < segment.mFrameRate) {
                frameRate = segment.mFrameRate;
            }
        }
        if (frameRate == Integer.MIN_VALUE) {
            return 30;
        }
        return frameRate;
    }
}
