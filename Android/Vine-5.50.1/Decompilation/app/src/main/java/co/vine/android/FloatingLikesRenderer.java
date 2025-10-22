package co.vine.android;

import android.os.Handler;
import co.vine.android.api.VineEndlessLikesRecord;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class FloatingLikesRenderer {
    private Handler mHandler;
    private FloatingLikesListener mListener;
    private FloatingLikesPlaybackInfoProvider mTimingProvider;
    private List<Float> heartsMillis = new ArrayList();
    private long mLastPollTime = -1;
    private Runnable mPollingRunnable = new Runnable() { // from class: co.vine.android.FloatingLikesRenderer.1
        @Override // java.lang.Runnable
        public void run() {
            FloatingLikesRenderer.this.poll();
            FloatingLikesRenderer.this.mHandler.postDelayed(this, 10L);
        }
    };

    public interface FloatingLikesListener {
        void showFloatingLike();
    }

    public interface FloatingLikesPlaybackInfoProvider {
        long getCurrentPosition();

        boolean isInPlaybackState();
    }

    public FloatingLikesRenderer(List<VineEndlessLikesRecord> likes, Handler handler, FloatingLikesPlaybackInfoProvider playbackInfoProvider, FloatingLikesListener listener) {
        this.mTimingProvider = playbackInfoProvider;
        this.mHandler = handler;
        this.mListener = listener;
        for (VineEndlessLikesRecord record : likes) {
            float segmentDurationMS = record.endTime - record.startTime;
            float heartDuration = segmentDurationMS / record.count;
            for (int i = 0; i < record.count; i++) {
                this.heartsMillis.add(Float.valueOf((record.startTime + (i * heartDuration)) * 1000.0f));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void poll() {
        if (this.mTimingProvider.isInPlaybackState()) {
            long currentPosition = this.mTimingProvider.getCurrentPosition();
            Iterator<Float> it = this.heartsMillis.iterator();
            while (it.hasNext()) {
                float heartTime = it.next().floatValue();
                if (heartTime > this.mLastPollTime && heartTime <= currentPosition) {
                    this.mListener.showFloatingLike();
                }
            }
            this.mLastPollTime = currentPosition;
        }
    }

    public void enable() {
        this.mLastPollTime = this.mTimingProvider != null ? this.mTimingProvider.getCurrentPosition() : 0L;
        if (this.mHandler != null) {
            this.mHandler.post(this.mPollingRunnable);
        }
    }

    public void disable() {
        this.mHandler.removeCallbacks(this.mPollingRunnable);
    }
}
