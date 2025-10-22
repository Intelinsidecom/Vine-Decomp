package co.vine.android.recorder.audio;

import android.media.AudioRecord;
import android.os.Process;
import co.vine.android.recorder.RecordConfigUtils;
import co.vine.android.service.ResourceService;
import co.vine.android.util.CrashUtil;
import com.edisonwang.android.slog.SLog;

/* loaded from: classes.dex */
public class AudioRecordSource implements AudioSource {
    private final AudioArray mAudioDataBuffer;
    private final AudioRecord mAudioRecord = new AudioRecord(1, 44100, 16, 2, RecordConfigUtils.AUDIO_RUNNABLE_SAMPLE_SIZE);
    private Thread mAudioThread;
    private final String mCurrentTarget;
    private volatile boolean mIsInitialized;
    private boolean mIsStopped;

    public AudioRecordSource(String targetTag, AudioArray buffer) {
        this.mCurrentTarget = targetTag;
        this.mAudioDataBuffer = buffer;
    }

    public class AudioGatherer implements Runnable {
        private final AudioReceiver mReceiver;

        public AudioGatherer(AudioReceiver receiver) {
            this.mReceiver = receiver;
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalStateException, InterruptedException, SecurityException, IllegalArgumentException {
            Process.setThreadPriority(-19);
            SLog.d("mAudioRecord.startRecording()");
            SLog.d("Buffer size {}.", Integer.valueOf(AudioRecordSource.this.mAudioDataBuffer.getLength()));
            AudioRecordSource.this.mIsInitialized = false;
            while (AudioRecordSource.this.mAudioRecord.getState() == 0) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
            AudioRecordSource.this.mIsInitialized = true;
            CrashUtil.log("mAudioRecord.isInitialized: {}.", AudioRecordSource.this.mCurrentTarget);
            AudioRecordSource.this.mAudioRecord.startRecording();
            while (true) {
                if (this.mReceiver.needsMoreData() && !AudioRecordSource.this.mIsStopped) {
                    this.mReceiver.beforeDataCollection();
                    int bufferReadResult = AudioRecordSource.this.mAudioDataBuffer.readFrom(AudioRecordSource.this.mAudioRecord, AudioRecordSource.this.mAudioDataBuffer.getLength());
                    if (bufferReadResult == -3) {
                        SLog.d("Audio Record in invalid state: (valid = 3) " + AudioRecordSource.this.mAudioRecord.getState());
                        break;
                    } else if (bufferReadResult > 0 && !this.mReceiver.onAudioDataReceived(bufferReadResult, AudioRecordSource.this.mAudioDataBuffer)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            ResourceService.releaseAudioBuffer(AudioArrays.typeOf(AudioRecordSource.this.mAudioDataBuffer));
            SLog.d("AudioThread Finished, release mAudioRecord");
            AudioRecordSource.this.mAudioRecord.stop();
            AudioRecordSource.this.mAudioRecord.release();
            AudioRecordSource.this.mIsInitialized = false;
            CrashUtil.log("mAudioRecord released: {}.", AudioRecordSource.this.mCurrentTarget);
            this.mReceiver.onAudioSourceStopped();
        }
    }

    @Override // co.vine.android.recorder.audio.AudioSource
    public void start(AudioReceiver receiver) {
        this.mAudioThread = new Thread(new AudioGatherer(receiver), "AudioRunnable");
        this.mAudioThread.start();
    }

    @Override // co.vine.android.recorder.audio.AudioSource
    public void stop() throws InterruptedException {
        CrashUtil.log("Stop audio record source: {}", Boolean.valueOf(this.mIsInitialized));
        this.mIsStopped = true;
        if (this.mAudioThread != null) {
            try {
                this.mAudioThread.join();
            } catch (InterruptedException e) {
                SLog.e("Error join audio thread.");
            }
        }
    }

    @Override // co.vine.android.recorder.audio.AudioSource
    public boolean isInitialized() {
        return this.mIsInitialized;
    }
}
