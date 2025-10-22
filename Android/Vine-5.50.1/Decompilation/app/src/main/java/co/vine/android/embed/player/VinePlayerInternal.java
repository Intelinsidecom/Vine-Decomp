package co.vine.android.embed.player;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import co.vine.android.embed.player.AudioTrack;
import co.vine.android.embed.player.TextureViewRenderer;
import co.vine.android.util.PriorityHandlerThread;
import java.io.IOException;

/* loaded from: classes.dex */
class VinePlayerInternal {
    private AudioRenderer mAudioRenderer;
    private final MediaClock mClock;
    private final Context mContext;
    private TextureObject mCurrentTextureObject;
    private final Handler mHandler;
    private boolean mHasAudioTrack;
    private Boolean mLastPlayWhenReady;
    private int mLastRenderedIndex;
    private final EventListener mListener;
    private final Handler mListenerHandler;
    private boolean mNeedsLoopWork;
    private TextureObject mNextTextureObject;
    private VideoRenderer mNextVideoRenderer;
    private boolean mPlayWhenReady;
    private TextureViewRenderer mRenderer;
    private VineSampleSource mSampleSource;
    private PlayerState mState;
    private Uri mUri;
    private VideoRenderer mVideoRenderer;
    private boolean mWasLooped;
    private final Runnable mSurfaceUpdatedRunnable = new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.1
        @Override // java.lang.Runnable
        public void run() {
            VinePlayerInternal.this.mListener.onSurfaceUpdated();
        }
    };
    private float mLastVolume = 1.0f;
    private float mVolume = 1.0f;
    private long mDurationMs = -1;
    private final PriorityHandlerThread mPlaybackThread = new PriorityHandlerThread(getClass().getSimpleName() + ":Handler", -16);

    interface EventListener {
        void onError(PlayerState playerState, int i, Throwable th);

        void onGLError(RuntimeException runtimeException);

        void onLoopCompleted();

        void onPastError(Exception exc);

        void onPrepared();

        void onRendererReadinessChanged(boolean z);

        void onSurfaceUpdated();

        void onVideoSizeChanged(int i, int i2);
    }

    public VinePlayerInternal(Context context, EventListener listener) {
        this.mPlaybackThread.start();
        this.mHandler = new Handler(this.mPlaybackThread.getLooper(), new PlayerMessageHandler());
        this.mListenerHandler = new Handler(Looper.getMainLooper());
        this.mState = PlayerState.IDLE;
        this.mContext = context;
        this.mClock = new MediaClock();
        this.mListener = listener;
    }

    public synchronized boolean playWhenReady() {
        return this.mPlayWhenReady;
    }

    public synchronized void setPlayWhenReady(boolean playWhenReady) {
        this.mPlayWhenReady = playWhenReady;
    }

    private void scheduleNextOperation(long thisOperationStartTimeMs, long intervalMs) {
        long nextOperationStartTimeMs = thisOperationStartTimeMs + intervalMs;
        long nextOperationDelayMs = nextOperationStartTimeMs - SystemClock.elapsedRealtime();
        if (nextOperationDelayMs <= 0) {
            this.mHandler.sendEmptyMessage(1);
        } else {
            this.mHandler.sendEmptyMessageDelayed(1, nextOperationDelayMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetInternal() {
        this.mPlayWhenReady = false;
        this.mUri = null;
        this.mVolume = 1.0f;
        this.mLastPlayWhenReady = null;
        this.mLastVolume = 1.0f;
        if (this.mSampleSource != null) {
            this.mSampleSource.release();
            final Exception error = this.mSampleSource.getError();
            if (error != null) {
                this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.2
                    @Override // java.lang.Runnable
                    public void run() {
                        VinePlayerInternal.this.mListener.onPastError(error);
                    }
                });
            }
            this.mSampleSource = null;
        }
        if (this.mVideoRenderer != null) {
            this.mVideoRenderer.release();
            this.mVideoRenderer = null;
        }
        if (this.mNextVideoRenderer != null) {
            this.mNextVideoRenderer.release();
            this.mNextVideoRenderer = null;
        }
        if (this.mAudioRenderer != null) {
            this.mAudioRenderer.release();
            this.mAudioRenderer = null;
        }
        this.mClock.reset();
        setState(PlayerState.IDLE);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        Log.d("VinePlayerInternal", "Player reset-ed.");
    }

    public void reset() {
        if (getState() != PlayerState.IDLE) {
            this.mHandler.sendEmptyMessage(5);
            int s = 0;
            while (getState() != PlayerState.IDLE) {
                s += 10;
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (s >= 1000) {
                    Log.e("VinePlayerInternal", "Taking too long to release, something is wrong.");
                }
            }
        }
    }

    public boolean isSurfaceReady() {
        return this.mCurrentTextureObject != null;
    }

    public synchronized void prepareAsync() {
        if (this.mState != PlayerState.WAITING_TO_PREPARE && this.mState != PlayerState.IDLE) {
            throw new IllegalStateException("You cannot set the state when state is " + this.mState);
        }
        setState(PlayerState.WAITING_TO_PREPARE);
        if (this.mUri == null) {
            throw new IllegalStateException("Uri is not set.");
        }
        if (!this.mPlaybackThread.isAlive()) {
            throw new IllegalStateException("The player is already released.");
        }
        Log.i("VinePlayerInternal", "Prepare async..");
        this.mHandler.sendEmptyMessage(2);
    }

    private synchronized Uri getUri() {
        return this.mUri;
    }

    public synchronized void setUri(Uri uri) {
        this.mUri = uri;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareInternal() throws MediaCodec.CryptoException, AudioTrack.InitializationException, IOException, AudioTrack.WriteException {
        if (this.mCurrentTextureObject == null || this.mNextTextureObject == null) {
            Log.e("VinePlayerInternal", "Surface is not ready.");
            return;
        }
        Log.i("VinePlayerInternal", "Preparing via Renderer " + this.mCurrentTextureObject.texture);
        if (this.mState != PlayerState.WAITING_TO_PREPARE) {
            throw new IllegalStateException("You cannot prepare when state is " + this.mState);
        }
        setState(PlayerState.PREPARING);
        Uri uri = getUri();
        if (uri == null) {
            throw new IllegalArgumentException("You cannot prepare before setting a Uri");
        }
        this.mSampleSource = new VineSampleSource(uri);
        this.mVideoRenderer = new VideoRenderer(0);
        this.mVideoRenderer.onMakeActive();
        this.mNextVideoRenderer = new VideoRenderer(1);
        this.mAudioRenderer = new AudioRenderer();
        this.mSampleSource.prepare(this.mContext);
        this.mAudioRenderer.prepare(this.mSampleSource);
        this.mVideoRenderer.prepare(this.mSampleSource, new Surface(this.mCurrentTextureObject.texture));
        this.mNextVideoRenderer.prepare(this.mSampleSource, new Surface(this.mNextTextureObject.texture));
        if (this.mSampleSource.hasAudioTrack()) {
            this.mDurationMs = this.mAudioRenderer.getDurationMs();
            this.mHasAudioTrack = true;
        } else {
            this.mDurationMs = this.mVideoRenderer.getDurationMs();
            this.mHasAudioTrack = false;
        }
        setState(PlayerState.PLAYABLE);
        this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.3
            @Override // java.lang.Runnable
            public void run() {
                VinePlayerInternal.this.mListener.onPrepared();
            }
        });
        doSomeWorkInternal();
    }

    public int getCurrentPositionMs() {
        return (int) (this.mClock.positionUs() / 1000);
    }

    public synchronized PlayerState getState() {
        return this.mState;
    }

    private synchronized void setState(PlayerState state) {
        this.mState = state;
    }

    public void setVolume(float gain) {
        this.mVolume = gain;
    }

    public boolean isPlaying() {
        return getState() == PlayerState.PLAYABLE && playWhenReady();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSomeWorkInternal() throws MediaCodec.CryptoException, AudioTrack.InitializationException, IOException, AudioTrack.WriteException {
        if (this.mState != PlayerState.IDLE) {
            boolean playWhenReady = playWhenReady();
            this.mLastPlayWhenReady = Boolean.valueOf(playWhenReady);
            this.mClock.operationStart();
            if (this.mState == PlayerState.PLAYABLE && playWhenReady) {
                boolean justLooped = doAudioWork();
                if (this.mNeedsLoopWork) {
                    this.mNeedsLoopWork = false;
                    VideoRenderer renderer = this.mVideoRenderer;
                    this.mLastRenderedIndex = renderer.getLastRenderedIndex();
                    this.mVideoRenderer = this.mNextVideoRenderer;
                    this.mVideoRenderer.onMakeActive();
                    this.mNextVideoRenderer = renderer;
                    this.mNextVideoRenderer.reset();
                }
                if (this.mWasLooped && !this.mClock.hasNegativePosition()) {
                    this.mWasLooped = false;
                    this.mRenderer.queueNextTextureEvent();
                    this.mNeedsLoopWork = true;
                }
                if (justLooped) {
                    this.mWasLooped = true;
                }
                doVideoWork();
            }
            this.mHandler.removeMessages(1);
            if (this.mState == PlayerState.PLAYABLE && playWhenReady) {
                scheduleNextOperation(this.mClock.getOperationStartMs(), 10L);
            } else {
                scheduleNextOperation(this.mClock.getOperationStartMs(), 300L);
            }
            this.mClock.operationEnd();
            return;
        }
        Log.i("VinePlayerInternal", "Do nothing at state " + this.mState);
    }

    public TextureViewRenderer createRenderer(String tag) {
        this.mRenderer = new TextureViewRenderer(tag, new TextureViewRenderer.OnTexturesChangedListener() { // from class: co.vine.android.embed.player.VinePlayerInternal.4
            @Override // co.vine.android.embed.player.TextureViewRenderer.OnTexturesChangedListener
            public void onTexturesChanged(TextureObject current, TextureObject next) {
                VinePlayerInternal.this.onTextureChanged(current, next);
            }
        }, this.mListener);
        return this.mRenderer;
    }

    public void onTextureChanged(TextureObject current, TextureObject next) {
        Log.i("VinePlayerInternal", "Texture changed: " + current);
        this.mCurrentTextureObject = current;
        this.mNextTextureObject = next;
        if (this.mCurrentTextureObject == null) {
            reset();
        }
        this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.5
            @Override // java.lang.Runnable
            public void run() {
                if (VinePlayerInternal.this.mCurrentTextureObject != null && VinePlayerInternal.this.getState() == PlayerState.WAITING_TO_PREPARE) {
                    VinePlayerInternal.this.prepareAsync();
                }
                VinePlayerInternal.this.mListener.onRendererReadinessChanged(VinePlayerInternal.this.mCurrentTextureObject != null);
            }
        });
    }

    public boolean isIdle() {
        return getState() == PlayerState.IDLE && !isReleased();
    }

    public boolean isReleased() {
        return !this.mPlaybackThread.isAlive();
    }

    public long getDurationMs() {
        return this.mDurationMs;
    }

    private void doVideoWork() throws MediaCodec.CryptoException, IOException {
        int width = this.mVideoRenderer.getWidth();
        int height = this.mVideoRenderer.getHeight();
        this.mVideoRenderer.doSomeWork(this.mClock, this.mHasAudioTrack, true);
        if (this.mNextVideoRenderer.getFramesRendered() < 1) {
            this.mNextVideoRenderer.doSomeWork(this.mClock, this.mHasAudioTrack, false);
        }
        final int newWidth = this.mVideoRenderer.getWidth();
        final int newHeight = this.mVideoRenderer.getHeight();
        if (width != newWidth || height != newHeight) {
            this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.6
                @Override // java.lang.Runnable
                public void run() {
                    VinePlayerInternal.this.mListener.onVideoSizeChanged(newWidth, newHeight);
                }
            });
        }
        if (this.mVideoRenderer.getFramesRendered() >= 1) {
            this.mListenerHandler.post(this.mSurfaceUpdatedRunnable);
        }
    }

    private boolean doAudioWork() throws AudioTrack.InitializationException, AudioTrack.WriteException {
        if (this.mVolume != this.mLastVolume) {
            this.mLastVolume = this.mVolume;
            this.mAudioRenderer.setVolume(this.mLastVolume);
        }
        if (!this.mAudioRenderer.doSomeWork(this.mClock)) {
            return false;
        }
        this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.7
            @Override // java.lang.Runnable
            public void run() {
                VinePlayerInternal.this.mListener.onLoopCompleted();
            }
        });
        return true;
    }

    public boolean isPlayable() {
        return getState() == PlayerState.PLAYABLE;
    }

    private class PlayerMessageHandler implements Handler.Callback {
        private PlayerMessageHandler() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(final Message msg) {
            boolean z = true;
            try {
                switch (msg.what) {
                    case 1:
                        VinePlayerInternal.this.doSomeWorkInternal();
                        break;
                    case 2:
                        VinePlayerInternal.this.prepareInternal();
                        break;
                    case 3:
                    case 4:
                    default:
                        z = false;
                        break;
                    case 5:
                        VinePlayerInternal.this.resetInternal();
                        break;
                }
            } catch (Throwable e) {
                VinePlayerInternal.this.mListenerHandler.post(new Runnable() { // from class: co.vine.android.embed.player.VinePlayerInternal.PlayerMessageHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        VinePlayerInternal.this.mListener.onError(VinePlayerInternal.this.mState, msg.what, e);
                    }
                });
            }
            return z;
        }
    }
}
