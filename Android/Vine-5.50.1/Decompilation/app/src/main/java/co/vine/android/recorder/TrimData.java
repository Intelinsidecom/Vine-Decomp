package co.vine.android.recorder;

import co.vine.android.recorder.audio.AudioData;
import co.vine.android.recorder.video.VideoData;
import com.edisonwang.android.slog.MessageFormatter;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class TrimData {
    public KeyFrameMode endMode;
    public int endMs;
    public KeyFrameMode startMode;
    public int startMs;

    public enum KeyFrameMode {
        BEFORE,
        AFTER,
        CLOEST
    }

    public TrimData(int startMs, int endMs) {
        this(startMs, KeyFrameMode.CLOEST, endMs, KeyFrameMode.CLOEST);
    }

    public TrimData(int startMs, KeyFrameMode startMode, int endMs, KeyFrameMode endMode) {
        this.startMs = startMs;
        this.startMode = startMode;
        this.endMs = endMs;
        this.endMode = endMode;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0033, code lost:
    
        if (r7 != (-1)) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x003c, code lost:
    
        throw new co.vine.android.recorder.TrimData.InvalidTrimException("No key frame found in all video data.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:?, code lost:
    
        return r7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getVideoStartIndex(java.util.ArrayList<co.vine.android.recorder.video.VideoData> r19, boolean r20) throws co.vine.android.recorder.TrimData.InvalidTrimException {
        /*
            r18 = this;
            r12 = 0
            r0 = r19
            java.lang.Object r12 = r0.get(r12)
            co.vine.android.recorder.video.VideoData r12 = (co.vine.android.recorder.video.VideoData) r12
            long r8 = r12.timestamp
            r0 = r18
            int r12 = r0.startMs
            int r10 = r12 * 1000
            r7 = -1
            r6 = -1
            java.util.Iterator r12 = r19.iterator()
        L17:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L32
            java.lang.Object r11 = r12.next()
            co.vine.android.recorder.video.VideoData r11 = (co.vine.android.recorder.video.VideoData) r11
            int r6 = r6 + 1
            if (r20 == 0) goto L3d
            int r13 = r6 + 1
            int r14 = r19.size()
            if (r13 != r14) goto L3d
            if (r6 != 0) goto L32
            r7 = r6
        L32:
            r12 = -1
            if (r7 != r12) goto L8d
            co.vine.android.recorder.TrimData$InvalidTrimException r12 = new co.vine.android.recorder.TrimData$InvalidTrimException
            java.lang.String r13 = "No key frame found in all video data."
            r12.<init>(r13)
            throw r12
        L3d:
            boolean r13 = r11.keyFrame
            if (r13 == 0) goto L17
            long r14 = r11.timestamp
            long r14 = r14 - r8
            long r0 = (long) r10
            r16 = r0
            int r13 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r13 < 0) goto L66
            long r14 = r11.timestamp
            long r14 = r14 - r8
            long r0 = (long) r10
            r16 = r0
            int r13 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r13 != 0) goto L57
            r12 = r6
        L56:
            return r12
        L57:
            int[] r13 = co.vine.android.recorder.TrimData.AnonymousClass1.$SwitchMap$co$vine$android$recorder$TrimData$KeyFrameMode
            r0 = r18
            co.vine.android.recorder.TrimData$KeyFrameMode r14 = r0.startMode
            int r14 = r14.ordinal()
            r13 = r13[r14]
            switch(r13) {
                case 1: goto L68;
                case 2: goto L6a;
                case 3: goto L6c;
                default: goto L66;
            }
        L66:
            r7 = r6
            goto L17
        L68:
            r12 = r7
            goto L56
        L6a:
            r12 = r6
            goto L56
        L6c:
            r12 = -1
            if (r7 != r12) goto L71
            r12 = r6
            goto L56
        L71:
            long r14 = (long) r10
            r0 = r19
            java.lang.Object r12 = r0.get(r7)
            co.vine.android.recorder.video.VideoData r12 = (co.vine.android.recorder.video.VideoData) r12
            long r12 = r12.timestamp
            long r12 = r12 - r8
            long r4 = r14 - r12
            long r12 = r11.timestamp
            long r12 = r12 - r8
            long r14 = (long) r10
            long r2 = r12 - r14
            int r12 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r12 <= 0) goto L8b
            r12 = r6
            goto L56
        L8b:
            r12 = r7
            goto L56
        L8d:
            r12 = r7
            goto L56
        */
        throw new UnsupportedOperationException("Method not decompiled: co.vine.android.recorder.TrimData.getVideoStartIndex(java.util.ArrayList, boolean):int");
    }

    private int getVideoEndIndex(ArrayList<VideoData> videoDatas, int audioEndMs, int startIndex, int fps) throws InvalidTrimException {
        int audioEndUs = (audioEndMs * 1000) - (1000 / fps);
        int size = videoDatas.size();
        if (videoDatas.size() == 1) {
            return 1;
        }
        long startTimestamp = videoDatas.get(0).timestamp;
        for (int i = startIndex; i < size; i++) {
            if (videoDatas.get(i).timestamp - startTimestamp >= audioEndUs) {
                if (i == startIndex) {
                    int i2 = startIndex + 1;
                    return i2;
                }
                return i;
            }
        }
        return size;
    }

    public static class InvalidTrimException extends Exception {
        public InvalidTrimException(String message) {
            super(message);
        }
    }

    public TrimResult getTrimResultFor(ArrayList<VideoData> videoData, AudioData combinedAudioData, int fps) throws InvalidTrimException {
        long videoEndUs;
        int audioEndCount;
        long startStamp;
        long endStamp;
        SLog.d("Generating trim result for {}ms {} -> {}ms {}.", new Object[]{Integer.valueOf(this.startMs), this.startMode.name(), Integer.valueOf(this.endMs), this.endMode.name()});
        int videoStartIndex = getVideoStartIndex(videoData, false);
        long videoStartUs = videoData.get(videoStartIndex).timestamp - videoData.get(0).timestamp;
        int audioStartCount = (int) (Math.floor(((44.1d * videoStartUs) / 1000.0d) / 512.0d) * 512.0d);
        int audioSizeGuess = (int) Math.min(Math.ceil((44.1d * this.endMs) / 512.0d) * 512.0d, combinedAudioData.size);
        int audioEndMsGuess = (int) (audioSizeGuess / 44.1d);
        int videoEndIndex = getVideoEndIndex(videoData, audioEndMsGuess, videoStartIndex, fps);
        int numFrames = videoEndIndex - videoStartIndex;
        if (numFrames < 1) {
            throw new InvalidTrimException("Number of frames were less than 1.");
        }
        boolean canReachMin = videoData.size() > 2;
        boolean wasTrimTooShort = canReachMin && numFrames < 2;
        if (numFrames < 2 || wasTrimTooShort) {
            while (videoEndIndex < videoData.size()) {
                videoEndIndex++;
                numFrames++;
                if (numFrames >= 2 || !canReachMin) {
                    break;
                }
            }
        }
        if (numFrames < 1 || (canReachMin && numFrames < 2)) {
            videoStartIndex = getVideoStartIndex(videoData, true);
            videoStartUs = videoData.get(videoStartIndex).timestamp - videoData.get(0).timestamp;
            audioStartCount = (int) (Math.floor(((44.1d * videoStartUs) / 1000.0d) / 512.0d) * 512.0d);
            int audioSizeGuess2 = (int) Math.min(Math.ceil((44.1d * this.endMs) / 512.0d) * 512.0d, combinedAudioData.size);
            audioEndMsGuess = (int) (audioSizeGuess2 / 44.1d);
            videoEndIndex = getVideoEndIndex(videoData, audioEndMsGuess, videoStartIndex, fps);
            numFrames = videoEndIndex - videoStartIndex;
            if (numFrames < 1) {
                throw new InvalidTrimException("Number of frames were less than 1.");
            }
            if (numFrames < 2 || wasTrimTooShort) {
                while (videoEndIndex < videoData.size()) {
                    videoEndIndex++;
                    numFrames++;
                    if (numFrames >= 2 || !canReachMin) {
                        break;
                    }
                }
            }
        }
        if (videoEndIndex < videoData.size()) {
            videoEndUs = videoData.get(videoEndIndex).timestamp - videoData.get(0).timestamp;
            audioEndCount = Math.min(combinedAudioData.size, (int) (Math.ceil(((44.1d * videoEndUs) / 1000.0d) / 512.0d) * 512.0d));
        } else {
            videoEndUs = audioEndMsGuess * 1000;
            audioEndCount = combinedAudioData.size;
        }
        int audioEndMs = (int) (audioEndCount / 44.1d);
        TrimResult result = new TrimResult(videoStartIndex, videoEndIndex, audioStartCount, audioEndCount);
        if (videoStartUs > videoEndUs) {
            if (videoStartIndex < videoData.size()) {
                startStamp = videoData.get(videoStartIndex).timestamp;
            } else {
                startStamp = videoStartIndex * (-1);
            }
            if (videoEndIndex < videoData.size()) {
                endStamp = videoData.get(videoEndIndex).timestamp;
            } else {
                endStamp = videoEndIndex * (-1);
            }
            throw new InvalidTrimException(MessageFormatter.format("Invalid video data: size {} {} {}.", Integer.valueOf(videoData.size()), Long.valueOf(startStamp), Long.valueOf(endStamp)).getMessage());
        }
        int audioStartMs = result.getAudioStartMs();
        if (audioStartMs >= audioEndMs) {
            throw new InvalidTrimException("Invalid audio data produced.");
        }
        if (canReachMin && numFrames < 2) {
            throw new InvalidTrimException("This should never happen, but trim frame count is lower than the min allowed.");
        }
        SLog.d("Result trim: V {}us -> {}us A {}ms -> {}ms (Guess: {}ms) .", new Object[]{Long.valueOf(videoStartUs), Long.valueOf(videoEndUs), Integer.valueOf(audioStartMs), Integer.valueOf(audioEndMs), Integer.valueOf(audioEndMsGuess)});
        SLog.d("Result durations: V: {}ms, A: {}ms.", Long.valueOf((videoEndUs - videoStartUs) / 1000), Integer.valueOf(audioEndMs - audioStartMs));
        return result;
    }
}
