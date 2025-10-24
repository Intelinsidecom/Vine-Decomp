package co.vine.android.recorder2.audio;

import android.media.AudioRecord;
import android.util.Log;
import com.googlecode.javacv.cpp.avcodec;
import java.util.concurrent.ArrayBlockingQueue;

/* loaded from: classes.dex */
public class AudioSoftwarePoller {
    private AudioEncoder mAudioEncoder;
    private boolean mIsLooping = false;
    private RecorderTask mRecorderTask = new RecorderTask();

    public void setAudioEncoder(AudioEncoder avcEncoder) {
        this.mAudioEncoder = avcEncoder;
    }

    public void recycleInputBuffer(byte[] buffer) {
        this.mRecorderTask.mDataBuffer.offer(buffer);
    }

    public void startPolling() {
        new Thread(this.mRecorderTask).start();
    }

    public void stopPolling() {
        this.mIsLooping = false;
    }

    public class RecorderTask implements Runnable {
        public int mBufferSize;
        public int mBufferWriteIndex = 0;
        public int mTotalFramesWritten = 0;
        ArrayBlockingQueue<byte[]> mDataBuffer = new ArrayBlockingQueue<>(50);
        int mReadResult = 0;

        public RecorderTask() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalStateException {
            byte[] thisBuffer;
            int minBufferSize = AudioRecord.getMinBufferSize(44100, 16, 2);
            this.mBufferSize = avcodec.MB_TYPE_L1;
            if (this.mBufferSize < minBufferSize) {
                this.mBufferSize = ((minBufferSize / 2048) + 1) * 2048 * 2;
            }
            for (int x = 0; x < 25; x++) {
                this.mDataBuffer.add(new byte[2048]);
            }
            AudioRecord audioRecorder = new AudioRecord(1, 44100, 16, 2, this.mBufferSize);
            audioRecorder.startRecording();
            AudioSoftwarePoller.this.mIsLooping = true;
            Log.i("AudioSoftwarePoller", "SW recording begin");
            while (AudioSoftwarePoller.this.mIsLooping) {
                long audioPresentationTimeNs = System.nanoTime();
                if (this.mDataBuffer.isEmpty()) {
                    thisBuffer = new byte[2048];
                } else {
                    thisBuffer = this.mDataBuffer.poll();
                }
                this.mReadResult = audioRecorder.read(thisBuffer, 0, 2048);
                if (this.mReadResult == -2 || this.mReadResult == -3) {
                    Log.e("AudioSoftwarePoller", "Read error");
                }
                this.mTotalFramesWritten++;
                if (AudioSoftwarePoller.this.mAudioEncoder != null) {
                    AudioSoftwarePoller.this.mAudioEncoder.offerAudioEncoder(thisBuffer, audioPresentationTimeNs);
                }
            }
            if (audioRecorder != null) {
                audioRecorder.setRecordPositionUpdateListener(null);
                audioRecorder.release();
                Log.i("AudioSoftwarePoller", "stopped");
            }
        }
    }
}
