package co.vine.android.recorder;

import android.os.Handler;
import co.vine.android.recorder.RecordConfigUtils;

/* loaded from: classes.dex */
public class ProgressTimer {
    private final ProgressTimerRunnable mTask;
    private final Thread mTaskThread;

    public ProgressTimer(BasicVineRecorder controller, Handler handler, int thresholdMs) {
        this.mTask = new ProgressTimerRunnable(controller, handler, thresholdMs);
        this.mTaskThread = new Thread(this.mTask);
    }

    public void start() {
        this.mTaskThread.start();
    }

    public void release() {
        this.mTask.run = false;
        this.mTaskThread.interrupt();
    }

    public static class ProgressTimerRunnable implements Runnable {
        private boolean hasNotifiedThreshold;
        private final BasicVineRecorder mController;
        private final Handler mHandler;
        private final int mThresholdMs;
        private int mProgress = -1;
        public volatile boolean run = true;

        public ProgressTimerRunnable(BasicVineRecorder controller, Handler handler, int thresholdMs) {
            this.mController = controller;
            this.mHandler = handler;
            this.mThresholdMs = thresholdMs;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                exec();
            } catch (InterruptedException e) {
            }
        }

        private void exec() throws InterruptedException {
            int progress;
            final BasicVineRecorder controller = this.mController;
            while (this.run) {
                long time = System.currentTimeMillis();
                long temp = controller.getCurrentDuration();
                if (temp < 0) {
                    progress = (int) (temp + time);
                } else {
                    progress = (int) temp;
                }
                if (progress != this.mProgress) {
                    this.mProgress = progress;
                    controller.postProgressUpdate(this.mProgress);
                }
                if (!this.hasNotifiedThreshold && controller.isRecordingSegment() && this.mProgress >= this.mThresholdMs) {
                    this.hasNotifiedThreshold = true;
                    this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.ProgressTimer.ProgressTimerRunnable.1
                        @Override // java.lang.Runnable
                        public void run() {
                            controller.onProgressThresholdReached();
                        }
                    });
                }
                RecordConfigUtils.RecordConfig config = this.mController.getConfig();
                if (config != null && this.mProgress >= config.maxDuration) {
                    this.mHandler.post(new Runnable() { // from class: co.vine.android.recorder.ProgressTimer.ProgressTimerRunnable.2
                        @Override // java.lang.Runnable
                        public void run() {
                            controller.onProgressMaxReached();
                        }
                    });
                    return;
                } else {
                    while (System.currentTimeMillis() - time < 18) {
                        Thread.sleep(5L);
                    }
                }
            }
        }
    }
}
